package edu.fbs.magset.export.javascript;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.util.InputTypeEnum;

public class BasicDataJavascriptExportService {

	public void exportToJavascript(MagsetResults magset, String javascriptOutputFolder) throws IOException {
		List<String> lines = new ArrayList<>();
		Collection<Genome> genomes = magset.getAllGenomes();
		lines.add("$( document ).ready(function() {");
		lines.add("	title = '" + magset.getConfigurations().getTitle() + "';");
		lines.add("	magOfInterest = '" + magset.getMag().getName() + "';");
		lines.add("	genomesName = " + genomes.stream().map(n -> n.getName()).collect(joining("','", "['", "']")) + ";");
		lines.add("	magCheckExecuted = " + magset.getConfigurations().hasMagCheckResults() + ";");
		lines.add("	annotatedGenomes = " + !magset.getConfigurations().getInputType().equals(InputTypeEnum.FASTA)
				+ ";");
		lines.add("	cazyAnnotations = " + magset.getConfigurations().isExecuteCazyAnnotations() + ";");
		lines.add("});");

		Files.write(Paths.get(javascriptOutputFolder + "00_load_basic_data.js"), lines, StandardCharsets.UTF_8);
	}

}
