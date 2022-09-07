package edu.fbs.magset.cazy;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.genome_file.GenomeFile;

public class CazyAnnotationService {

	public Map<GenomeFile, CazyAnnotations> loadCazyAnnotations(GenomesComparator genocom) throws IOException {
		Map<GenomeFile, CazyAnnotations> result = new TreeMap<>();

		for (GenomeFile genomeFile : genocom.getConfigurations().getAllGenomes()) {
			result.put(genomeFile, getMap(genomeFile, genocom.getConfigurations().getCazyFolder()));
		}

		return result;
	}

	private CazyAnnotations getMap(GenomeFile genomeFile, String resultsFolder) throws IOException {
		String genomeName = genomeFile.getName();
		Reader in = new FileReader(
				resultsFolder + "/" + genomeName + "/overview.txt");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withFirstRecordAsHeader().parse(in);

		Map<String, CazyAnnotation> result = new TreeMap<>();
		for (CSVRecord record : records) {
			String locusTag = record.get(0);

			int toolsThatIdentifiedAnnotation = Integer.valueOf(record.get(5));
			if (toolsThatIdentifiedAnnotation < 2) {
				continue;
			}

			CazyAnnotation line = new CazyAnnotation();
			line.setLocusTag(locusTag);
			String cazyCodes = record.get(2);

			if (cazyCodes.equals("-")) {
				cazyCodes = record.get(4);
			}

			line.setCazyCodes(cazyCodes);

			result.put(locusTag, line);
		}
		return new CazyAnnotations(genomeFile, result);
	}
}
