package edu.fbs.magset.model.cazy;

import java.util.Map;

import edu.fbs.magset.model.genome.GenomeFile;
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
