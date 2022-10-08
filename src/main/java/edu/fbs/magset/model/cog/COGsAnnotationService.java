package edu.fbs.magset.model.cog;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.GenomeFile;

@Service
public class COGsAnnotationService {

	public Map<GenomeFile, COGAnnotations> getCOGAnnotations(MagsetResults magset) throws IOException {
		Map<GenomeFile, COGAnnotations> result = new TreeMap<>();

		if (!magset.shouldExportHtmlCsvFiles()) {
			return result;
		}
		if (!magset.hasAnnotatedGenomes()) {
			return result;
		}

		for (GenomeFile genomeFile : magset.getAllGenomes()) {
			result.put(genomeFile, getCOGMap(genomeFile, magset.getConfigurations().getCogFolder()));
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
