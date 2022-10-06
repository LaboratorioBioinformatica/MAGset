package edu.fbs.magset.model.pangenome;

import java.util.Map;
import java.util.TreeMap;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class Pangenome {
	private Map<String, PangenomeGene> genes;

	public Map<String, PangenomeGene> getUniquePangenomeGenes() {
		Map<String, PangenomeGene> results = new TreeMap<>();

		for (PangenomeGene gene : genes.values()) {
			results.put(gene.getGeneName(), gene);
		}

		return results;
	}
}
