package edu.fbs.magset.model.genomic_region_interest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.fbs.magset.model.genome.Gene;
import edu.fbs.magset.model.genome.GenomeFile;
import lombok.Data;

@Data
public class GenomicRegionsInterest {
	private Map<Integer, GenomicRegionInterest> genomicRegionsById;
	private Map<GenomeFile, Map<String, GenomeSegment>> genomicRegionsGenesByGenome = new LinkedHashMap<>();
	private Map<GenomeFile, List<GenomeSegment>> genomicRegionsByGenome = new LinkedHashMap<>();
	private boolean clustered;

	public void setGris(Set<GenomicRegionInterest> gris) {
		this.genomicRegionsById = new TreeMap<>();

		for (GenomicRegionInterest gri : gris) {
			genomicRegionsById.put(gri.getId(), gri);
			for (GenomeSegment segment : gri.getGenomeSegments()) {

				GenomeFile genomeFile = segment.getGenomeFile();
				Map<String, GenomeSegment> grisByGeneName = genomicRegionsGenesByGenome.get(genomeFile);

				if (grisByGeneName == null) {
					grisByGeneName = new TreeMap<>();
					genomicRegionsGenesByGenome.put(genomeFile, grisByGeneName);
				}
				for (Gene gene : segment.getGenes()) {
					grisByGeneName.put(gene.getLocusTag(), segment);
				}

				List<GenomeSegment> grisByGenome = genomicRegionsByGenome.get(genomeFile);
				if (grisByGenome == null) {
					grisByGenome = new ArrayList<>();
					genomicRegionsByGenome.put(genomeFile, grisByGenome);
				}
				grisByGenome.add(segment);

			}
		}
	}

	public GenomeSegment getGenomicRegionByGene(GenomeFile genomeFile, String locusTag) {
		Map<String, GenomeSegment> griByGeneName = genomicRegionsGenesByGenome.get(genomeFile);
		if (griByGeneName != null) {
			return griByGeneName.get(locusTag);
		}
		return null;
	}
}