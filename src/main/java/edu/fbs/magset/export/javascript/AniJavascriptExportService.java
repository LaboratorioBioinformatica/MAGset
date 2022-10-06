package edu.fbs.magset.export.javascript;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.ani.AniResult;
import edu.fbs.magset.model.ani.AniResults;
import edu.fbs.magset.model.genome.GenomeFile;

public class AniJavascriptExportService {

	public void exportAniResultToJavascript(MagsetResults genocom, String javascriptOutputFolder) throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("$( document ).ready(function() {\r\n");

		AniResults aniResults = genocom.getAniResults();
		for (GenomeFile genome1 : genocom.getAllGenomes()) {
			List<String> results = new ArrayList<>();

			for (GenomeFile genome2 : genocom.getAllGenomes()) {
				AniResult aniResult = aniResults.getAniResult(genome1, genome2);
				if (aniResult != null) {
					results.add(aniResult.getResult().toString());
				} else if (genome1.getName().equals(genome2.getName())) {
					results.add("100.0");
				} else {
					results.add("0.0");
				}
			}

			String resultsString = results.stream().collect(Collectors.joining(",", "[", "]"));

			lines.add("aniResults.push(new AniResults('" //
					+ escapeEspecialCharacters(genome1.getName()) + "', "//
					+ resultsString + "));");
		}

		lines.add("});");
		Files.write(Paths.get(javascriptOutputFolder + "01_ani_data.js"), lines, StandardCharsets.UTF_8);
	}

	private String escapeEspecialCharacters(String string) {
		return string.replaceAll("\'", "\\\\'");
	}
}
