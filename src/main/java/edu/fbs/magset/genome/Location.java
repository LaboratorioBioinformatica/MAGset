package edu.fbs.magset.genome;

import org.apache.commons.lang3.builder.CompareToBuilder;

import lombok.Data;

@Data
public class Location implements Comparable<Location> {

	private int start;
	private int end;

	public Location(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}

	@Override
	public int compareTo(Location o) {
		return new CompareToBuilder().append(this.start, o.start).toComparison();
	}

}
