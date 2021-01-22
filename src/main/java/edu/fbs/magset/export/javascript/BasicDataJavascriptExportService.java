package edu.fbs.magset.export.javascript;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.InputTypeEnum;
import edu.fbs.magset.genome_file.GenomeFile;

public class BasicDataJavascriptExportService {

	public void exportToJavascript(GenomesComparator genocom, String javascriptOutputFolder) throws IOException {
		List<String> lines = new ArrayList<>();
		Collection<GenomeFile> genomes = genocom.getConfigurations().getAllGenomes();
		lines.add("$( document ).ready(function() {");
		lines.add("	title = '" + genocom.getConfigurations().getTitle() + "';");
		lines.add("	magOfInterest = '" + genocom.getMagOfInterest().getName() + "';");
		lines.add("	genomesName = " + genomes.stream().map(n -> n.getName()).collect(joining("','", "['", "']")) + ";");
		lines.add("	magCheckExecuted = " + genocom.isMagCheckExecuted() + ";");
		lines.add("	annotatedGenomes = " + !genocom.getConfigurations().getInputType().equals(InputTypeEnum.FASTA)
				+ ";");
		lines.add("	cazyAnnotations = " + genocom.getConfigurations().isExecuteCazyAnnotations() + ";");
		lines.add("});");

		Files.write(Paths.get(javascriptOutputFolder + "00_load_basic_data.js"), lines, StandardCharsets.UTF_8);
	}

}
