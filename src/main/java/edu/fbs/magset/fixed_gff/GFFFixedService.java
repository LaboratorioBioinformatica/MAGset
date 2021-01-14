package edu.fbs.magset.fixed_gff;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class GFFFixedService {

	public FixedGFFFile getGFFFixedFile(String pangenomeResultsFolder, String genomeFilename) throws IOException {
		String path = pangenomeResultsFolder + "/fixed_input_files/" + genomeFilename + ".gb.gff";

		FixedGFFFile fixedFile = new FixedGFFFile(path, new TreeMap<String, List<String>>());

		File file = new File(path);
		if (file.exists()) {
			fixedFile.setLocusTagVersusNewIdsMap(getLocusTagNewIdsMap(file));
		}

		return fixedFile;
	}

	private static final Pattern PATTERN = Pattern.compile("ID=(.*?);.*?Name=(.*?)(;|$)");
	private static final Pattern PATTERN_LOCUS_TAG = Pattern.compile("ID=(.*?);.*?locus_tag=(.*?)(;|$)");

	private Map<String, List<String>> getLocusTagNewIdsMap(File file) throws IOException {
		Map<String, List<String>> locusTagToNewIds = new TreeMap<>();
		Reader in = new FileReader(file);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withSkipHeaderRecord().parse(in);

		for (CSVRecord record : records) {
			if (record.size() != 9) {
				continue;
			}
			String data = record.get(8);
			getNewIds(locusTagToNewIds, data, PATTERN);
			getNewIds(locusTagToNewIds, data, PATTERN_LOCUS_TAG);
		}
		return locusTagToNewIds;
	}

	private void getNewIds(Map<String, List<String>> locusTagToNewIds, String data, Pattern pattern) {
		Matcher matcher = pattern.matcher(data);
		if (matcher.find()) {
			String newId = matcher.group(1);
			String originalName = matcher.group(2);

			List<String> newIds = locusTagToNewIds.get(originalName);

			if (newIds == null) {
				newIds = new ArrayList<String>();
				locusTagToNewIds.put(originalName, newIds);
			}
			newIds.add(newId);
		}
	}
}