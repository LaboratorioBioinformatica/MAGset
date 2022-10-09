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
import edu.fbs.magset.model.genome.Genome;

@Service
public class COGsAnnotationService {

	public Map<Genome, COGAnnotations> getCOGAnnotations(MagsetResults magset) throws IOException {
		Map<Genome, COGAnnotations> result = new TreeMap<>();

		if (!magset.shouldExportHtmlCsvFiles()) {
			return result;
		}
		if (!magset.hasAnnotatedGenomes()) {
			return result;
		}

		for (Genome genome : magset.getAllGenomes()) {
			result.put(genome, getCOGMap(genome, magset.getConfigurations().getCogFolder()));
		}

		return result;
	}

	private COGAnnotations getCOGMap(Genome genome, String cogResultsFolder) throws IOException {
		String genomeName = genome.getName();
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
		return new COGAnnotations(genome, result);
	}
}
