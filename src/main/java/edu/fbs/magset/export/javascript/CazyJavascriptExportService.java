package edu.fbs.magset.export.javascript;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.cazy.CazyAnnotation;
import edu.fbs.magset.model.cazy.CazyAnnotations;
import edu.fbs.magset.model.genome.GenomeFile;

public class CazyJavascriptExportService {

	public void exportToJavascript(MagsetResults genocom, String javascriptOutputFolder) throws IOException {
		if (!genocom.getConfigurations().isExecuteCazyAnnotations()) {
			return;
		}

		Map<GenomeFile, CazyAnnotations> genesByGenome = genocom.getCazyAnnotations();
		for (GenomeFile genome : genesByGenome.keySet()) {

			List<String> lines = new ArrayList<>();

			lines.add("$( document ).ready(function() {");

			for (CazyAnnotation annotation : genesByGenome.get(genome).getAnnotations().values()) {

				lines.add("\tcazyGenes.push(new CazyGene('" //
						+ escapeEspecialCharacters(annotation.getLocusTag()) + "', '" //
						+ escapeEspecialCharacters(annotation.getCazyCodes()) //
						+ "'));");
			}
			lines.add("});");
			Files.write(Paths.get(javascriptOutputFolder + "05_cazy_" + genome.getName() + "_data.js"), lines,
					StandardCharsets.UTF_8);
		}
	}

	private String escapeEspecialCharacters(String string) {
		return string.replaceAll("\'", "\\\\'");
	}

}
