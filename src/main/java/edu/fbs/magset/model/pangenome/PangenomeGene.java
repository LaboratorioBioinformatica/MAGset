package edu.fbs.magset.model.pangenome;

import java.util.List;

import edu.fbs.magset.model.genome.GenomeFile;
import lombok.Data;

@Data
public class PangenomeGene {
	private String geneName;
	private String annotation;
	private Integer numberOfIsolates;
	private List<GenomeFile> genomes;

	public PangenomeGene(String geneName, String annotation, Integer numberOfIsolates, List<GenomeFile> genomes) {
		super();
		this.geneName = geneName;
		this.annotation = annotation;
		this.numberOfIsolates = numberOfIsolates;
		this.genomes = genomes;
	}
	
	public boolean existsInGenome(GenomeFile genome) {
		return genomes.contains(genome);
	}

	public boolean isSpecific() {
		return genomes.size() == 1;
	}

	public boolean isCore(int genomesQuantity) {
		return genomes.size() == genomesQuantity;
	}

	public boolean isShared(int genomesQuantity) {
		return genomes.size() > 1 && genomes.size() < genomesQuantity;
	}

	public static final String[] CSV_HEADER = { "type", "locus_tag", "pangenome_id", "product", "protein_id", "start",
			"end", "strand", "parent", "core", "shared", "specific", "genomic_region_of_interest", "cog",
			"cog_description" };

}
