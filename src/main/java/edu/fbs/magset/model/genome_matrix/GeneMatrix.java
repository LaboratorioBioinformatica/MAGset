package edu.fbs.magset.model.genome_matrix;

import edu.fbs.magset.model.cazy.CazyAnnotation;
import edu.fbs.magset.model.cog.COGAnnotation;
import edu.fbs.magset.model.genome.Gene;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.model.genomic_region_interest.GenomeSegment;
import edu.fbs.magset.model.pangenome.PangenomeGene;
import lombok.Data;

@Data
public class GeneMatrix {
	private Gene gene;
	private Genome genome;
	private GenomeSegment griGenome;
	private PangenomeGene pangenomeGene;
	private COGAnnotation cogAnnotation;
	private CazyAnnotation cazyAnnotation;
	private String pangenomeId;
}
