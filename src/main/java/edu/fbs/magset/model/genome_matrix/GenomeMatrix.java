package edu.fbs.magset.model.genome_matrix;

import java.util.Map;

import edu.fbs.magset.model.genome.Genome;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class GenomeMatrix {
	private Genome genome;
	private Map<String, GeneMatrix> genesByName;
}
