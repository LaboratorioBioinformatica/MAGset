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
import edu.fbs.magset.model.genome.Genome;

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
			String genomeName1 = getGenomeName(record.get(0));
			String genomeName2 = getGenomeName(record.get(1));
			Double result = Double.valueOf(record.get(2));
			Genome genome1 = findGenomeByName(magset, genomeName1);
			Genome genome2 = findGenomeByName(magset, genomeName2);
			results.addAniResult(genome1, genome2, result);
		}

		return results;
	}

	private String getGenomeName(String path) {
		return FilenameUtils.removeExtension(Paths.get(path).getFileName().toString());
	}

	private Genome findGenomeByName(MagsetResults magset, String genomeName) {
		return magset.getAllGenomes().stream().filter(x -> x.getName().equals(genomeName)).findFirst().get();
	}

}
