package edu.fbs.magset.model.ani;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import edu.fbs.magset.MagsetResults;

@Service
public class AniResultService {

	public AniResults getAniResults(MagsetResults magset) throws IOException {
		AniResults results = new AniResults();
		if (!magset.shouldExportHtmlCsvFiles()) {
			return results;
		}

		Reader in = new FileReader(magset.getConfigurations().getAniFile());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withFirstRecordAsHeader().parse(in);

		for (CSVRecord record : records) {
			String genomeName1 = FilenameUtils.removeExtension(Paths.get(record.get(0)).getFileName().toString());
			String genomeName2 = FilenameUtils.removeExtension(Paths.get(record.get(1)).getFileName().toString());
			Double result = Double.valueOf(record.get(2));
			results.addAniResult(magset.getGenomeFileByNameWithoutExtension(genomeName1),
					magset.getGenomeFileByNameWithoutExtension(genomeName2), result);
		}

		return results;
	}

}
