package edu.fbs.magset.model.cog;

import java.util.Map;

import edu.fbs.magset.model.genome.GenomeFile;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class COGAnnotations {
	private GenomeFile genomeFile;
	private Map<String, COGAnnotation> annotations;

	public COGAnnotations(GenomeFile genomeFile, Map<String, COGAnnotation> annotations) {
		super();
		this.genomeFile = genomeFile;
		this.annotations = annotations;
	}

}
