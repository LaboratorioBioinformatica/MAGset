package edu.fbs.magset.model.pangenome;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.fbs.magset.model.genome.Genome;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class Pangenome {
	private Map<String, PangenomeGene> genesByGroupName = new TreeMap<>();
	private Map<Genome, Map<String, PangenomeGene>> genesByGenomeAndGeneName = new HashMap<>();

	public Pangenome(Collection<Genome> genomes) {
		genomes.stream().forEach(genome -> genesByGenomeAndGeneName.put(genome, new HashMap<>()));
	}

	public void addPangenomeGene(PangenomeGene pangenomeGene) {
		genesByGroupName.put(pangenomeGene.getGroupName(), pangenomeGene);
		Map<Genome, String> genesByGenome = pangenomeGene.getGenesByGenome();
		for (Genome genome : genesByGenome.keySet()) {
			String geneName = genesByGenome.get(genome);
			genesByGenomeAndGeneName.get(genome).put(geneName, pangenomeGene);
		}
	}

	public Collection<PangenomeGene> getGenes() {
		return genesByGroupName.values();
	}
}
