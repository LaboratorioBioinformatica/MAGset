package edu.fbs.magset.model.ani;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.fbs.magset.model.genome.Genome;
import lombok.Data;

@Data
public class AniResults {
	private Map<AniResultKey, AniResult> results = new LinkedHashMap<>();

	public AniResult getAniResult(Genome genome1, Genome genome2) {
		return results.get(new AniResultKey(genome1, genome2));
	}

	public void addAniResult(Genome genome1, Genome genome2, Double result) {
		AniResultKey key1 = new AniResultKey(genome1, genome2);
		AniResultKey key2 = new AniResultKey(genome2, genome1);
		AniResult value = new AniResult(key1, result);
		results.put(key1, value);
		results.put(key2, value);
	}
}
