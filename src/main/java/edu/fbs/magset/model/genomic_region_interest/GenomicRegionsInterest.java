package edu.fbs.magset.model.genomic_region_interest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.fbs.magset.model.genome.Gene;
import edu.fbs.magset.model.genome.Genome;
import lombok.Data;

@Data
public class GenomicRegionsInterest {
	private Map<Integer, GenomicRegionInterest> genomicRegionsById;
	private Map<Genome, Map<String, GenomeSegment>> genomicRegionsGenesByGenome = new LinkedHashMap<>();
	private Map<Genome, List<GenomeSegment>> genomicRegionsByGenome = new LinkedHashMap<>();
	private boolean clustered;

	public void setGris(Set<GenomicRegionInterest> gris) {
		this.genomicRegionsById = new TreeMap<>();

		for (GenomicRegionInterest gri : gris) {
			genomicRegionsById.put(gri.getId(), gri);
			for (GenomeSegment segment : gri.getGenomeSegments()) {

				Genome genome = segment.getGenome();
				Map<String, GenomeSegment> grisByGeneName = genomicRegionsGenesByGenome.get(genome);

				if (grisByGeneName == null) {
					grisByGeneName = new TreeMap<>();
					genomicRegionsGenesByGenome.put(genome, grisByGeneName);
				}
				for (Gene gene : segment.getGenes()) {
					grisByGeneName.put(gene.getLocusTag(), segment);
				}

				List<GenomeSegment> grisByGenome = genomicRegionsByGenome.get(genome);
				if (grisByGenome == null) {
					grisByGenome = new ArrayList<>();
					genomicRegionsByGenome.put(genome, grisByGenome);
				}
				grisByGenome.add(segment);

			}
		}
	}

	public GenomeSegment getGenomicRegionByGene(Genome genome, String locusTag) {
		Map<String, GenomeSegment> griByGeneName = genomicRegionsGenesByGenome.get(genome);
		if (griByGeneName != null) {
			return griByGeneName.get(locusTag);
		}
		return null;
	}
}