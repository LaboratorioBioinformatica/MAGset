package edu.fbs.magset.export.javascript;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.Gene;
import edu.fbs.magset.model.genomic_region_interest.GenomeSegment;
import edu.fbs.magset.model.genomic_region_interest.GenomicRegionInterest;

public class GRIGenesJavascriptExportService {

	public void exportRGIListToJavascript(MagsetResults genocom, String javascriptOutputFolder) throws IOException {

		Collection<GenomicRegionInterest> gris = genocom.getGenomicRegionsOfInterest().getGenomicRegionsById().values();

		List<String> lines = new ArrayList<>();

		lines.add("$( document ).ready(function() {");
		for (GenomicRegionInterest gri : gris) {
			for (GenomeSegment genomeSegment : gri.getGenomeSegments()) {
				for (Gene gene : genomeSegment.getGenes()) {
					lines.add("griGenes.push(new GRIGene('" //
							+ genomeSegment.getNameWithoutGenome() + "', '" //
							+ genomeSegment.getGenome().getName() + "', '"//
							+ gene.getType() + "', '" //
							+ gene.getLocusTag() + "', '"//
							+ escapeEspecialCharacters(defaultString(gene.getProduct())) + "', '"//
							+ defaultString(gene.getProteinId()) + "', " //
							+ gene.getMin() + ", " + gene.getMax() + ", '"//
							+ gene.getStrand() + "', '" //
							+ defaultString(gene.getParent())//
							+ "'));");
				}
			}
		}
		lines.add("});");

		Files.write(Paths.get(javascriptOutputFolder + "03_gri_gene_data.js"), lines, StandardCharsets.UTF_8);
	}

	private String escapeEspecialCharacters(String string) {
		return string.replaceAll("\'", "\\\\'");
	}
}
