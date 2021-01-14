package edu.fbs.magset.genomic_region_interest.coverage;

import java.util.TreeSet;

import lombok.ToString;

@ToString(onlyExplicitlyIncluded = true)
public class Segments {

	TreeSet<Segment> largestSegments = new TreeSet<>();

	public void addNewSegment(Segment newSegment) {
		largestSegments.add(newSegment);
	}

	public Segment getLargestSegment() {
		if (largestSegments.isEmpty()) {
			return null;
		}
		return largestSegments.first();
	}

	public int getSumOfSegments(double minimalAverageCoverage) {
		int result = 0;
		for (Segment segment : largestSegments) {
			if (segment.getAverageCoverage() >= minimalAverageCoverage) {
				result += segment.getSize();
			}
		}
		return result;
	}
}
