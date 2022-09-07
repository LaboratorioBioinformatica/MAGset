package edu.fbs.magset.cog;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.genome_file.GenomeFile;

public class COGsAnnotationService {

	public Map<GenomeFile, COGAnnotations> loadCOGsAnnotation(GenomesComparator genocom) throws IOException {
		Map<GenomeFile, COGAnnotations> result = new TreeMap<>();

		for (GenomeFile genomeFile : genocom.getConfigurations().getAllGenomes()) {
			result.put(genomeFile, getCOGMap(genomeFile, genocom.getConfigurations().getCogFolder()));
		}

		return result;
	}

	private COGAnnotations getCOGMap(GenomeFile genomeFile, String cogResultsFolder) throws IOException {
		String genomeName = genomeFile.getName();
		Reader in = new FileReader(cogResultsFolder + "//results_" + genomeName + "//classifier_result.tsv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withAllowMissingColumnNames()
				.withFirstRecordAsHeader().parse(in);

		Map<String, COGAnnotation> result = new TreeMap<>();
		for (CSVRecord record : records) {
			String locusTag = record.get(0);
			COGAnnotation cogLine = new COGAnnotation();
			cogLine.setLocusTag(locusTag);
			cogLine.setCogId(record.get(1));
			cogLine.setCogDescription(record.get(6));
			result.put(locusTag, cogLine);
		}
		return new COGAnnotations(genomeFile, result);
	}
}
