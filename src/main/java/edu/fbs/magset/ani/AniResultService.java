package edu.fbs.magset.ani;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;

import edu.fbs.magset.GenomesComparator;

public class AniResultService {

	public AniResults getAniResults(GenomesComparator genocom) throws IOException {
		AniResults results = new AniResults();

		Reader in = new FileReader(genocom.getConfigurations().getAniFile());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withFirstRecordAsHeader().parse(in);

		for (CSVRecord record : records) {
			String genomeName1 = FilenameUtils.removeExtension(Paths.get(record.get(0)).getFileName().toString());
			String genomeName2 = FilenameUtils.removeExtension(Paths.get(record.get(1)).getFileName().toString());
			Double result = Double.valueOf(record.get(2));
			results.addAniResult(genocom.getGenomeFileByNameWithoutExtension(genomeName1),
					genocom.getGenomeFileByNameWithoutExtension(genomeName2), result);
		}

		return results;
	}

}
