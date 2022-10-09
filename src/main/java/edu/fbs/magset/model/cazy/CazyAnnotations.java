package edu.fbs.magset.model.cazy;

import java.util.Map;

import edu.fbs.magset.model.genome.Genome;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class CazyAnnotations {
	private Genome genome;
	private Map<String, CazyAnnotation> annotations;

	public CazyAnnotations(Genome genome, Map<String, CazyAnnotation> annotations) {
		super();
		this.genome = genome;
		this.annotations = annotations;
	}

}
