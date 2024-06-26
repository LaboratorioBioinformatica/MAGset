package edu.fbs.magset.model.genomic_region_interest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.biojava.nbio.core.sequence.DNASequence;
import org.springframework.stereotype.Service;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.model.genomic_region_interest.coverage.MAGCheckService;
import edu.fbs.magset.model.nucmer.NucmerResult;
import edu.fbs.magset.model.nucmer.NucmerResultService;
import edu.fbs.magset.model.nucmer.NucmerResultsByMatchName;

@Service
public class GenomicRegionsInterestService {

	private NucmerResultService nucmerService = new NucmerResultService();

	public GenomicRegionsInterest getGRIs(MagsetResults magset) throws IOException, InterruptedException {
		GenomicRegionsInterest genomicRegionsInterest = new GenomicRegionsInterest();
		Set<GenomicRegionInterest> gris = new LinkedHashSet<>();

		Map<Genome, List<GenomicRegionInterest>> grisByGenome = new LinkedHashMap<>();

		for (Genome genome : magset.getReferencesByIndex().values()) {
			List<GenomicRegionInterest> genomeGris = getGRIs(magset, genome, GenomicRegionInterestType.NEGATIVE);
			grisByGenome.put(genome, genomeGris);
		}

		List<GenomicRegionInterest> magGris = getGRIs(magset, magset.getMag(), GenomicRegionInterestType.POSITIVE);
		grisByGenome.put(magset.getMag(), magGris);

		int id = 1;
		for (List<GenomicRegionInterest> grisGenome : grisByGenome.values()) {
			for (GenomicRegionInterest gri : grisGenome) {
				gri.setId(id++);
			}
		}

		gris = clusterGRIsUsingNucmer(grisByGenome, magset);

		id = 1;
		for (GenomicRegionInterest gri : gris) {
			gri.setId(id++);
		}
		new MAGCheckService().loadCoverageRawReads(gris, magset);

		genomicRegionsInterest.setGris(gris);

		File file = new File(magset.getConfigurations().getNucmerByGenomeGRIsFolder());
		if (file.exists()) {
			genomicRegionsInterest.setClustered(true);
		}

		return genomicRegionsInterest;
	}

	private Set<GenomicRegionInterest> clusterGRIsUsingNucmer(Map<Genome, List<GenomicRegionInterest>> grisByGenome,
			MagsetResults genocom) throws IOException, InterruptedException {
		Set<GenomicRegionInterest> result = new LinkedHashSet<GenomicRegionInterest>();

		File file = new File(genocom.getConfigurations().getNucmerByGenomeGRIsFolder());
		if (!file.exists()) {
			grisByGenome.values().forEach(x -> result.addAll(x));
			return result;
		}

		List<NucmerResultsByMatchName> nucmerResults = nucmerService.getGRINucmerResults(genocom);
		Map<String, GenomeSegment> segmentByName = new HashMap<>();
		Map<GenomeSegment, String> nameBySegment = new HashMap<>();
		Map<String, GenomicRegionInterest> clusterBySegmentName = new LinkedHashMap<>();

		grisByGenome.values().stream().forEach(x -> x.forEach(y -> {
			GenomeSegment genomeSegment = y.getGenomeSegments().first();
			segmentByName.put(genomeSegment.getName(), genomeSegment);
			nameBySegment.put(genomeSegment, genomeSegment.getName());
		}));

		for (NucmerResultsByMatchName nucmerResult : nucmerResults) {
			String querySegmentName = nucmerResult.getQueryMatchName();
			String referenceSegmentName = nucmerResult.getReferenceMatchName();

			GenomicRegionInterest clusterQuery = clusterBySegmentName.get(querySegmentName);
			GenomicRegionInterest clusterReference = clusterBySegmentName.get(referenceSegmentName);

			GenomeSegment querySegment = segmentByName.get(querySegmentName);
			GenomeSegment referenceSegment = segmentByName.get(referenceSegmentName);

			if (clusterQuery != null && clusterReference != null) {
				Set<GenomeSegment> referenceClusterSegments = clusterReference.getGenomeSegments();
				for (GenomeSegment genomeSegment : referenceClusterSegments) {
					clusterQuery.addSegment(genomeSegment);
					clusterBySegmentName.put(nameBySegment.get(genomeSegment), clusterQuery);
				}
			} else if (clusterQuery != null) {
				clusterQuery.addSegment(referenceSegment);
				clusterBySegmentName.put(referenceSegmentName, clusterQuery);
			} else if (clusterReference != null) {
				clusterReference.addSegment(querySegment);
				clusterBySegmentName.put(querySegmentName, clusterReference);
			} else {
				Set<GenomeSegment> segments = new LinkedHashSet<>();
				segments.add(querySegment);
				segments.add(referenceSegment);
				GenomicRegionInterest newCluster = new GenomicRegionInterest(
						querySegment.getGenomicRegionInterest().getType(), segments);
				clusterBySegmentName.put(referenceSegmentName, newCluster);
				clusterBySegmentName.put(querySegmentName, newCluster);
			}
		}

		result.addAll(clusterBySegmentName.values());

		return result;
	}

	private List<GenomicRegionInterest> getGRIs(MagsetResults genocom, Genome genome, GenomicRegionInterestType type)
			throws IOException, InterruptedException {
		List<GenomicRegionInterest> result = new ArrayList<>();
		for (Entry<String, DNASequence> dnaSequenceEntry : genome.getDnaSequence().entrySet()) {
			String sequenceName = dnaSequenceEntry.getKey();
			DNASequence dnaSequence = dnaSequenceEntry.getValue();
			List<NucmerResult> nucmerResults = nucmerService.getGenomeNucmerResults(genome, sequenceName,
					dnaSequence.getLength(), genocom);

			NucmerResult previous = nucmerResults.get(0);
			for (int i = 1; i < nucmerResults.size(); i++) {
				NucmerResult current = nucmerResults.get(i);

				if (current.getReferenceMatchStart() - previous.getReferenceMatchEnd() + 1 > genocom.getConfigurations()
						.getMinimumGRISize()) {
					GenomeSegment genomeSegment = new GenomeSegment(genome, sequenceName, dnaSequence.getDNAType(),
							previous.getReferenceMatchEnd() + 1, current.getReferenceMatchStart());
					GenomicRegionInterest gri = new GenomicRegionInterest(type, genomeSegment);
					result.add(gri);
				}
				previous = current;
			}
		}
		return result;
	}

}