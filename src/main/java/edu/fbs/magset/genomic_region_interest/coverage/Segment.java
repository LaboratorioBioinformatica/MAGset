package edu.fbs.magset.genomic_region_interest.coverage;

import org.apache.commons.lang3.builder.CompareToBuilder;

import lombok.Data;

@Data
public class Segment implements Comparable<Segment> {

	private int start;
	private int end;
	private double averageCoverage;

	public Segment() {
	}

	public int getSize() {
		return getEnd() - getStart() + 1;
	}

	@Override
	public int compareTo(Segment o) {
		return new CompareToBuilder().append(o.getSize(), getSize()).append(start, o.start).build();
	}

}
