package edu.fbs.magset.cazy;

import java.util.Map;

import edu.fbs.magset.genome_file.GenomeFile;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class CazyAnnotations {
	private GenomeFile genomeFile;
	private Map<String, CazyAnnotation> annotations;

	public CazyAnnotations(GenomeFile genomeFile, Map<String, CazyAnnotation> annotations) {
		super();
		this.genomeFile = genomeFile;
		this.annotations = annotations;
	}

}
