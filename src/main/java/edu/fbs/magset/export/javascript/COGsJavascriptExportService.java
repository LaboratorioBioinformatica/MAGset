package edu.fbs.magset.export.javascript;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.cog.COGAnnotation;
import edu.fbs.magset.model.cog.COGAnnotations;
import edu.fbs.magset.model.genome.GenomeFile;

public class COGsJavascriptExportService {

	public void exportToJavascript(MagsetResults genocom, String javascriptOutputFolder) throws IOException {

		Map<GenomeFile, COGAnnotations> genesByGenome = genocom.getCogsAnnotation();
		for (GenomeFile genome : genesByGenome.keySet()) {

			List<String> lines = new ArrayList<>();

			lines.add("$( document ).ready(function() {");

			for (COGAnnotation cogAnnotation : genesByGenome.get(genome).getAnnotations().values()) {

				lines.add("\tcogGenes.push(new CogGene('" //
						+ escapeEspecialCharacters(cogAnnotation.getLocusTag()) + "', '" //
						+ escapeEspecialCharacters(cogAnnotation.getCogId()) + "', '" //
						+ escapeEspecialCharacters(cogAnnotation.getCogDescription()) //
						+ "'));");
			}
			lines.add("});");
			Files.write(Paths.get(javascriptOutputFolder + "04_cog_" + genome.getName() + "_data.js"), lines,
					StandardCharsets.UTF_8);
		}
	}

	private String escapeEspecialCharacters(String string) {
		return string.replaceAll("\'", "\\\\'");
	}

}
