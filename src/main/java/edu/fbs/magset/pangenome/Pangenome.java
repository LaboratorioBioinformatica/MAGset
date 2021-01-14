package edu.fbs.magset.pangenome;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.fbs.magset.fixed_gff.FixedGFFFile;
import edu.fbs.magset.genome_file.GenomeFile;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class Pangenome {
	private Map<String, PangenomeGene> genes;
	private Map<GenomeFile, FixedGFFFile> fixedGFFFiles = new TreeMap<GenomeFile, FixedGFFFile>();

	public void addFixedGFFFile(GenomeFile genomeFile, FixedGFFFile replacedIds) {
		fixedGFFFiles.put(genomeFile, replacedIds);
	}

	public PangenomeGene getReplacedGeneInPangenome(GenomeFile genomeFile, String locusTag) {
		FixedGFFFile fixedGFFFile = fixedGFFFiles.get(genomeFile);
		if (fixedGFFFile == null) {
			return null;
		}
		List<String> newIds = fixedGFFFile.getNewIds(locusTag);
		if (newIds != null) {
			for (String newId : newIds) {
				if (newId != null) {
					PangenomeGene genePresenceAbsence = genes.get(newId);
					if (genePresenceAbsence != null) {
						return genePresenceAbsence;
					}
				}
			}
		}
		return null;
	}

	public String getReplacedIdInPangenome(GenomeFile genomeFile, String locusTag) {
		FixedGFFFile fixedGFFFile = fixedGFFFiles.get(genomeFile);
		if (fixedGFFFile == null) {
			return null;
		}
		List<String> newIds = fixedGFFFile.getNewIds(locusTag);
		if (newIds != null) {
			for (String newId : newIds) {
				if (newId != null) {
					PangenomeGene genePresenceAbsence = genes.get(newId);
					if (genePresenceAbsence != null) {
						return newId;
					}
				}
			}
		}
		return null;
	}
	
	public Map<String, PangenomeGene> getUniquePangenomeGenesWithoutFixedNames(){
		Map<String, PangenomeGene> results = new TreeMap<>();
		
		for (PangenomeGene gene : genes.values()) {
			results.put(gene.getGeneName(), gene);
		}
		
		return results;
	}
}
