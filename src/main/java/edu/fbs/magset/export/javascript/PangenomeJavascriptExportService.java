package edu.fbs.magset.export.javascript;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.GenomeFile;
import edu.fbs.magset.model.pangenome.PangenomeGene;

public class PangenomeJavascriptExportService {

	public void exportPangenomeGeneListToJavascript(MagsetResults genocom, String javascriptOutputFolder)
			throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("$( document ).ready(function() {\r\n");
		for (PangenomeGene gene : genocom.getPangenome().getUniquePangenomeGenes().values()) {
			String genomeSymbols = hasGenomeInPangenomeGene(genocom, gene).stream()
					.collect(Collectors.joining("','", "['", "']"));

			lines.add("pangenomeGenes.push(new PangenomeGene('" //
					+ escapeEspecialCharacters(gene.getGeneName()) + "', '"//
					+ escapeEspecialCharacters(gene.getAnnotation()) + "', "//
					+ gene.getNumberOfIsolates() + ", "//
					+ genomeSymbols + "));");
		}
		lines.add("});");

		Files.write(Paths.get(javascriptOutputFolder + "02_pagenome_data.js"), lines, StandardCharsets.UTF_8);
	}

	private List<String> hasGenomeInPangenomeGene(MagsetResults genocom, PangenomeGene gene) {
		List<GenomeFile> genomes = genocom.getAllGenomes();
		genomes.sort(GenomeFile.COMPARATOR_BY_NAME);

		List<String> hasGenomeInPangenome = new ArrayList<>();

		Map<GenomeFile, String> geneNames = gene.getGeneNames();

		for (GenomeFile genomeFile : genomes) {
			if (geneNames.containsKey(genomeFile)) {
				hasGenomeInPangenome.add(geneNames.get(genomeFile));
			} else {
				hasGenomeInPangenome.add("");
			}
		}

		return hasGenomeInPangenome;
	}

	private String escapeEspecialCharacters(String string) {
		return string.replaceAll("\'", "\\\\'");
	}
}
