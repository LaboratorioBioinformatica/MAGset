package edu.fbs.magset.model.pangenome;

import java.util.Map;

import edu.fbs.magset.model.genome.Genome;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PangenomeGene {
	@EqualsAndHashCode.Include
	private String groupName;
	private String annotation;
	private Integer numberOfIsolates;
	private Map<Genome, String> genesByGenome;

	public PangenomeGene(String groupName, String annotation, Integer numberOfIsolates,
			Map<Genome, String> genesByGenome) {
		super();
		this.groupName = groupName;
		this.annotation = annotation;
		this.numberOfIsolates = numberOfIsolates;
		this.genesByGenome = genesByGenome;
	}

	public boolean isSpecific() {
		return genesByGenome.size() == 1;
	}

	public boolean isCore(int genomesQuantity) {
		return genesByGenome.size() == genomesQuantity;
	}

	public boolean isShared(int genomesQuantity) {
		return genesByGenome.size() > 1 && genesByGenome.size() < genomesQuantity;
	}

	public static final String[] CSV_HEADER = { "type", "locus_tag", "pangenome_id", "product", "protein_id", "start",
			"end", "strand", "parent", "core", "shared", "specific", "genomic_region_of_interest", "cog",
			"cog_description" };

}
