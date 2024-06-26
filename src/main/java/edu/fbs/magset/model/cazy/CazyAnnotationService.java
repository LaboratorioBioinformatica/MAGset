package edu.fbs.magset.model.cazy;

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
public class CazyAnnotationService {

	public Map<Genome, CazyAnnotations> getCazyAnnotations(MagsetResults magset) throws IOException {
		Map<Genome, CazyAnnotations> result = new TreeMap<>();

		if (!magset.shouldExportHtmlCsvFiles()) {
			return result;
		}
		if (!magset.hasAnnotatedGenomes()) {
			return result;
		}
		if (!magset.getConfigurations().isExecuteCazyAnnotations()) {
			return result;
		}

		for (Genome genome : magset.getAllGenomes()) {
			result.put(genome, getMap(genome, magset.getConfigurations().getCazyFolder()));
		}

		return result;
	}

	private CazyAnnotations getMap(Genome genome, String resultsFolder) throws IOException {
		String genomeName = genome.getName();
		Reader in = new FileReader(resultsFolder + "/" + genomeName + "/overview.txt");
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
		return new CazyAnnotations(genome, result);
	}
}
