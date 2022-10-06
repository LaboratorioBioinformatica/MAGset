package edu.fbs.magset.model.nucmer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.GenomeFile;

public class NucmerResultService {

	public List<NucmerResult> getGenomeNucmerResults(GenomeFile genomeFile, String referenceMatchName,
			Integer sequenceSize, MagsetResults genocom) throws IOException, InterruptedException {
		List<NucmerResult> nucmerResults = getNucmerResults(genocom.getConfigurations().getNucmerResultFile(genomeFile),
				genocom);
		List<NucmerResult> filtered = filterNucmerResults(nucmerResults, referenceMatchName,
				genocom.getConfigurations().getNucmerMinimalQueryMatchSize(),
				genocom.getConfigurations().getNucmerMinimalIdentity());

		List<NucmerResult> aggregatedResults = aggregateResults(filtered, sequenceSize);

		return aggregatedResults;
	}

	public List<NucmerResultsByMatchName> getGRINucmerResults(MagsetResults genocom)
			throws IOException, InterruptedException {
		List<NucmerResult> nucmerResults = getNucmerResults(genocom.getConfigurations().getGRINucmerResultFile(),
				genocom);
		nucmerResults = filterNucmerResultsByIdentity(nucmerResults,
				genocom.getConfigurations().getNucmerMinimalIdentity());

		Collection<NucmerResultsByMatchName> nucmerResultsByMatchName = getNucmerResultsByMatchName(nucmerResults);
		return filterNucmerResultsByCoverage(nucmerResultsByMatchName,
				genocom.getConfigurations().getNucmerMinimalCoveredBetweenSimilarGRIsToConsider());
	}

	public List<NucmerResultsByMatchName> getGRINucmerResults2(GenomeFile genomeFile, MagsetResults genocom)
			throws IOException, InterruptedException {
		List<NucmerResult> nucmerResults = getNucmerResults(
				genocom.getConfigurations().getGRINucmerResultFile(genomeFile), genocom);
		nucmerResults = filterNucmerResultsByIdentity(nucmerResults,
				genocom.getConfigurations().getNucmerMinimalIdentity());

		Collection<NucmerResultsByMatchName> nucmerResultsByMatchName = getNucmerResultsByMatchName(nucmerResults);
		return filterNucmerResultsByCoverage(nucmerResultsByMatchName,
				genocom.getConfigurations().getNucmerMinimalCoveredBetweenSimilarGRIsToConsider());
	}

	private Collection<NucmerResultsByMatchName> getNucmerResultsByMatchName(List<NucmerResult> nucmerResults) {
		Map<NucmerResultMatchNames, NucmerResultsByMatchName> results = new TreeMap<>();

		for (NucmerResult nucmerResult : nucmerResults) {
			NucmerResultMatchNames match = nucmerResult.getNucmerResultMatchNames();

			NucmerResultsByMatchName nucmerResultsByMatchName = results.get(match);

			if (nucmerResultsByMatchName == null) {
				nucmerResultsByMatchName = new NucmerResultsByMatchName(match.getReferenceMatchName(),
						match.getQueryMatchName());
				results.put(match, nucmerResultsByMatchName);
			}
			nucmerResultsByMatchName.addNucmerResult(nucmerResult);
		}

		return results.values();
	}

	private List<NucmerResult> aggregateResults(List<NucmerResult> filtered, Integer sequenceSize) {
		List<NucmerResult> result = new ArrayList<>();
		NucmerResult start = new NucmerResult();
		start.setReferenceMatchStart(0);
		start.setReferenceMatchEnd(0);
		result.add(start);

		if (filtered.size() > 0) {
			Collections.sort(filtered);
			NucmerResult previous = filtered.get(0);
			for (int i = 1; i < filtered.size(); i++) {
				NucmerResult current = filtered.get(i);
				if (current.getReferenceMatchStart() > previous.getReferenceMatchEnd()) {
					result.add(previous);
					previous = current;
					continue;
				} else {
					NucmerResult merged = new NucmerResult();
					if (previous.getReferenceMatchStart() > current.getReferenceMatchStart()) {
						merged.setReferenceMatchStart(current.getReferenceMatchStart());
					} else {
						merged.setReferenceMatchStart(previous.getReferenceMatchStart());
					}

					if (previous.getReferenceMatchEnd() < current.getReferenceMatchEnd()) {
						merged.setReferenceMatchEnd(current.getReferenceMatchEnd());
					} else {
						merged.setReferenceMatchEnd(previous.getReferenceMatchEnd());
					}
					previous = merged;
				}
			}
			result.add(previous);
		}

		NucmerResult end = new NucmerResult();
		end.setReferenceMatchStart(sequenceSize);
		end.setReferenceMatchEnd(sequenceSize);
		result.add(end);

		return result;
	}

	private List<NucmerResult> filterNucmerResults(List<NucmerResult> results, String referenceMatchName,
			Integer minimumSize, Double minimumIdentity) {
		final String referenceMatchNameWithoutSpace = referenceMatchName.split("\\s")[0];
		return results.stream() //
				.filter(x -> x.getReferenceMatchName().equals(referenceMatchNameWithoutSpace) //
						&& x.getReferenceMatchSize() >= minimumSize //
						&& x.getIdentity() >= minimumIdentity * 100) //
				.collect(Collectors.toList());
	}

	private List<NucmerResult> filterNucmerResultsByIdentity(List<NucmerResult> results, Double minimumIdentity) {
		return results.stream() //
				.filter(x -> x.getIdentity() >= minimumIdentity * 100) //
				.collect(Collectors.toList());
	}

	private List<NucmerResultsByMatchName> filterNucmerResultsByCoverage(Collection<NucmerResultsByMatchName> results,
			Double minimiumCoverage) {
		return results.stream() //
				.filter(x -> x.getSumOfQueryMatchCoverage() >= minimiumCoverage * 100 //
						&& x.getSumOfReferenceMatchCoverage() >= minimiumCoverage * 100) //
				.collect(Collectors.toList());
	}

	private List<NucmerResult> getNucmerResults(String nucmerFile, MagsetResults genocom)
			throws IOException, InterruptedException {
		List<NucmerResult> result = new ArrayList<>();

		File file = new File(nucmerFile);
		if (!file.exists()) {
			return new ArrayList<>();
		}

		BufferedReader reader = new BufferedReader(new FileReader(nucmerFile));

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').parse(reader);

		for (CSVRecord record : records) {
			NucmerResult nucmer = new NucmerResult();

			nucmer.setReferenceMatchStart(Integer.valueOf(record.get(0)));
			nucmer.setReferenceMatchEnd(Integer.valueOf(record.get(1)));

			nucmer.setQueryMatchStart(Integer.valueOf(record.get(2)));
			nucmer.setQueryMatchEnd(Integer.valueOf(record.get(3)));

			nucmer.setReferenceMatchSize(Integer.valueOf(record.get(4)));
			nucmer.setQueryMatchSize(Integer.valueOf(record.get(5)));

			nucmer.setIdentity(Double.valueOf(record.get(6)));

			nucmer.setReferenceSize(Integer.valueOf(record.get(7)));
			nucmer.setQuerySize(Integer.valueOf(record.get(8)));

			nucmer.setReferenceMatchCoverage(Double.valueOf(record.get(9)));
			nucmer.setQueryMatchCoverage(Double.valueOf(record.get(10)));

			nucmer.setReferenceMatchName(record.get(11));
			nucmer.setQueryMatchName(record.get(12));

			result.add(nucmer);
		}

		return result;
	}

}