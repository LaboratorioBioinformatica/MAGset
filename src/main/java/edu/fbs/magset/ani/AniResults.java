package edu.fbs.magset.ani;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.fbs.magset.genome_file.GenomeFile;
import lombok.Data;

@Data
public class AniResults {
	private Map<AniResultKey, AniResult> results = new LinkedHashMap<>();

	public AniResult getAniResult(GenomeFile genome1, GenomeFile genome2) {
		return results.get(new AniResultKey(genome1, genome2));
	}

	public void addAniResult(GenomeFile genome1, GenomeFile genome2, Double result) {
		AniResultKey key1 = new AniResultKey(genome1, genome2);
		AniResultKey key2 = new AniResultKey(genome2, genome1);
		AniResult value = new AniResult(key1, result);
		results.put(key1, value);
		results.put(key2, value);
	}
}
