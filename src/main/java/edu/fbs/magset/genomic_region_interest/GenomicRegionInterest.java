package edu.fbs.magset.genomic_region_interest;

import java.util.Set;
import java.util.TreeSet;

import lombok.Data;
import lombok.ToString;

/**
 * Genomic region of interest
 * 
 * @author fsanchez
 *
 */
@Data
@ToString(onlyExplicitlyIncluded = true)
public class GenomicRegionInterest {

	@ToString.Include
	private int id;
	@ToString.Include
	private GenomicRegionInterestType type;
	@ToString.Include
	private TreeSet<GenomeSegment> genomeSegments = new TreeSet<>();

	public GenomicRegionInterest(GenomicRegionInterestType type, GenomeSegment genomeSegment) {
		super();
		this.type = type;
		addSegment(genomeSegment);
	}

	public GenomicRegionInterest(GenomicRegionInterestType type, Set<GenomeSegment> genomeSegments) {
		super();
		this.type = type;
		for (GenomeSegment genomeSegment : genomeSegments) {
			addSegment(genomeSegment);
		}
	}

	public void addSegment(GenomeSegment segment) {
		if (!genomeSegments.contains(segment)) {
			genomeSegments.add(segment);
			segment.setGenomicRegionInterest(this);
			segment.setId(genomeSegments.size());
		}
	}

	public String getName() {
		return type.getInitials() + "GRI" + String.format("%04d", this.getId());
	}
}