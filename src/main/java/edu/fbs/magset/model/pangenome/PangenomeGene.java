package edu.fbs.magset.model.pangenome;

import java.util.Map;

import edu.fbs.magset.model.genome.GenomeFile;
import lombok.Data;

@Data
public class PangenomeGene {
	private String geneName;
	private String annotation;
	private Integer numberOfIsolates;
	private Map<GenomeFile, String> geneNames;

	public PangenomeGene(String geneName, String annotation, Integer numberOfIsolates,
			Map<GenomeFile, String> geneNames) {
		super();
		this.geneName = geneName;
		this.annotation = annotation;
		this.numberOfIsolates = numberOfIsolates;
		this.geneNames = geneNames;
	}

	public boolean isSpecific() {
		return geneNames.size() == 1;
	}

	public boolean isCore(int genomesQuantity) {
		return geneNames.size() == genomesQuantity;
	}

	public boolean isShared(int genomesQuantity) {
		return geneNames.size() > 1 && geneNames.size() < genomesQuantity;
	}

	public static final String[] CSV_HEADER = { "type", "locus_tag", "pangenome_id", "product", "protein_id", "start",
			"end", "strand", "parent", "core", "shared", "specific", "genomic_region_of_interest", "cog",
			"cog_description" };

}
