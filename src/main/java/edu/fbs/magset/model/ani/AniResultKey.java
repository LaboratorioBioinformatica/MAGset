package edu.fbs.magset.model.ani;

import edu.fbs.magset.model.genome.GenomeFile;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AniResultKey {
	private GenomeFile genome1;
	private GenomeFile genome2;

}
