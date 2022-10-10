package edu.fbs.magset.export.javascript;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.model.pangenome.PangenomeGene;

public class PangenomeJavascriptExportService {

	public void exportPangenomeGeneListToJavascript(MagsetResults genocom, String javascriptOutputFolder)
			throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("$( document ).ready(function() {\r\n");
		for (PangenomeGene gene : genocom.getPangenome().getGenes()) {
			String genomeSymbols = hasGenomeInPangenomeGene(genocom, gene).stream()
					.collect(Collectors.joining("','", "['", "']"));

			lines.add("pangenomeGenes.push(new PangenomeGene('" //
					+ escapeEspecialCharacters(gene.getGroupName()) + "', '"//
					+ escapeEspecialCharacters(gene.getAnnotation()) + "', "//
					+ gene.getNumberOfIsolates() + ", "//
					+ genomeSymbols + "));");
		}
		lines.add("});");

		Files.write(Paths.get(javascriptOutputFolder + "02_pagenome_data.js"), lines, StandardCharsets.UTF_8);
	}

	private List<String> hasGenomeInPangenomeGene(MagsetResults magset, PangenomeGene gene) {
		Collection<Genome> genomes = magset.getAllGenomes();
		List<String> hasGenomeInPangenome = new ArrayList<>();

		Map<Genome, String> geneNames = gene.getGenesByGenome();

		for (Genome genome : genomes) {
			if (geneNames.containsKey(genome)) {
				hasGenomeInPangenome.add(geneNames.get(genome));
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
