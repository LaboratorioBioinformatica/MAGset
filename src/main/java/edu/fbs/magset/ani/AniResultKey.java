package edu.fbs.magset.ani;

import edu.fbs.magset.genome_file.GenomeFile;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AniResultKey {
	private GenomeFile genome1;
	private GenomeFile genome2;

}
