package edu.fbs.magset.model.cog;

import java.util.Map;

import edu.fbs.magset.model.genome.Genome;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class COGAnnotations {
	private Genome genome;
	private Map<String, COGAnnotation> annotations;

	public COGAnnotations(Genome genome, Map<String, COGAnnotation> annotations) {
		super();
		this.genome = genome;
		this.annotations = annotations;
	}

}
