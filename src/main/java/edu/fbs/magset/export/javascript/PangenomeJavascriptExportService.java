package edu.fbs.magset.export.javascript;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.genome_file.GenomeFile;
import edu.fbs.magset.pangenome.PangenomeGene;

public class PangenomeJavascriptExportService {

	public void exportPangenomeGeneListToJavascript(GenomesComparator genocom, String javascriptOutputFolder)
			throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add("$( document ).ready(function() {\r\n");
		for (PangenomeGene gene : genocom.getPangenome().getUniquePangenomeGenesWithoutFixedNames().values()) {
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

	private List<String> hasGenomeInPangenomeGene(GenomesComparator genocom, PangenomeGene gene) {
		List<GenomeFile> genomes = genocom.getConfigurations().getAllGenomes();
		genomes.sort(GenomeFile.COMPARATOR_BY_NAME);
		
		List<String> hasGenomeInPangenome = new ArrayList<>();

		List<GenomeFile> genomesInPangenome = gene.getGenomes();
		genomesInPangenome.sort(GenomeFile.COMPARATOR_BY_NAME);
		
		for (GenomeFile genomeFile : genomes) {
			if (genomesInPangenome.contains(genomeFile)) {
				hasGenomeInPangenome.add("<i class=\"fas fa-check\"></i>");
			} else {
				hasGenomeInPangenome.add("---");
			}
		}

		return hasGenomeInPangenome;
	}

	private String escapeEspecialCharacters(String string) {
		return string.replaceAll("\'", "\\\\'");
	}
}
