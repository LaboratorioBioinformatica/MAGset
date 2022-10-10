package edu.fbs.magset.export.javascript;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.model.genome_matrix.GeneMatrix;
import edu.fbs.magset.model.genome_matrix.GenomeMatrix;

public class MatrixJavascriptExportService {

	public void exportToJavascript(MagsetResults genocom, String javascriptOutputFolder) throws IOException {

		Map<Genome, GenomeMatrix> genesByGenome = genocom.getGenomeMatrices();
		for (Genome genome : genesByGenome.keySet()) {

			List<String> lines = new ArrayList<>();
			lines.add("$( document ).ready(function() {\r\n");

			for (GeneMatrix geneMatrix : genesByGenome.get(genome).getGenesByName().values()) {

				lines.add("matrixGenes.push(new MatrixGene('"
						+ escapeEspecialCharacters(geneMatrix.getGenome().getName()) //
						+ "', '" //
						+ defaultString(geneMatrix.getGene().getLocusTag()) //
						+ "', '" //
						+ defaultString(geneMatrix.getGene().getType()) + "', '" //
						+ escapeEspecialCharacters(geneMatrix.getGene().getProduct()) + "', '"//
						+ defaultString(geneMatrix.getGene().getProteinId()) + "', " //
						+ geneMatrix.getGene().getMin() + ", " //
						+ geneMatrix.getGene().getMax() + ", '" //
						+ geneMatrix.getGene().getStrand() + "', '" //
						+ defaultString(geneMatrix.getGene().getParent()) + "', '" //
						+ escapeEspecialCharacters(geneMatrix.getPangenomeGene().getGroupName()) + "', '" //
						+ geneMatrix.getPangenomeGene().isCore(genesByGenome.size()) + "', '" //
						+ geneMatrix.getPangenomeGene().isShared(genesByGenome.size()) + "', '" //
						+ geneMatrix.getPangenomeGene().isSpecific() + "', '" //
						+ (geneMatrix.getGriGenome() != null ? geneMatrix.getGriGenome().getName() : "") + "', '" //
						+ (geneMatrix.getCogAnnotation() != null ? geneMatrix.getCogAnnotation().getCogId() : "")
						+ "', '" //
						+ (geneMatrix.getCogAnnotation() != null
								? escapeEspecialCharacters(geneMatrix.getCogAnnotation().getCogDescription())
								: "")
						+ "', '" //
						+ (geneMatrix.getCazyAnnotation() != null
								? escapeEspecialCharacters(geneMatrix.getCazyAnnotation().getCazyCodes())
								: "")
						+ "','"
						+ (geneMatrix.getGriGenome() != null
								? geneMatrix.getGriGenome().foundInRawData(genocom.getConfigurations())
								: "false") //
						+ "'));");
			}
			lines.add("});");
			Files.write(Paths.get(javascriptOutputFolder + "06_matrix_" + genome.getName() + "_data.js"), lines,
					StandardCharsets.UTF_8);
		}
	}

	private String escapeEspecialCharacters(String string) {
		if (string == null) {
			return "";
		}
		return string.replaceAll("\'", "\\\\'");
	}
}
