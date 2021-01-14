package edu.fbs.magset.genome_matrix;

import java.util.Map;

import edu.fbs.magset.genome_file.GenomeFile;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class GenomeMatrix {
	private GenomeFile genomeFile;
	private Map<String, GeneMatrix> genesByName;
}
