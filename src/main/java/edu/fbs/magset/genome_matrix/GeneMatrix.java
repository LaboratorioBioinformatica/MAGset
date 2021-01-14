package edu.fbs.magset.genome_matrix;

import edu.fbs.magset.cazy.CazyAnnotation;
import edu.fbs.magset.cog.COGAnnotation;
import edu.fbs.magset.genome.Gene;
import edu.fbs.magset.genome_file.GenomeFile;
import edu.fbs.magset.genomic_region_interest.GenomeSegment;
import edu.fbs.magset.pangenome.PangenomeGene;
import lombok.Data;

@Data
public class GeneMatrix {
	private Gene gene;
	private GenomeFile genome;
	private GenomeSegment griGenome;
	private PangenomeGene pangenomeGene;
	private COGAnnotation cogAnnotation;
	private CazyAnnotation cazyAnnotation;
	private String pangenomeId;
}
