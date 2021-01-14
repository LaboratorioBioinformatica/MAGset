package edu.fbs.magset.ani;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.fbs.magset.GenomesComparator;

public class AniResultService {

	public AniResults getAniResults(GenomesComparator genocom) throws IOException {
		AniResults results = new AniResults();

		Reader in = new FileReader(genocom.getConfigurations().getAniFile());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withFirstRecordAsHeader().parse(in);

		for (CSVRecord record : records) {
			String genomeName1 = record.get(0);
			String genomeName2 = record.get(1);
			Double result = Double.valueOf(record.get(2));
			results.addAniResult(genocom.getGenomeFileByNameWithoutExtension(genomeName1),
					genocom.getGenomeFileByNameWithoutExtension(genomeName2), result);
		}

		return results;
	}

}
