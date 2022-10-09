package edu.fbs.magset.model.ani;

import edu.fbs.magset.model.genome.Genome;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AniResultKey {
	private Genome genome1;
	private Genome genome2;

}
