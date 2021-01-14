package edu.fbs.magset.nucmer;

import org.apache.commons.lang3.builder.CompareToBuilder;

import lombok.Data;

@Data
public class NucmerResult implements Comparable<NucmerResult> {
	private int referenceMatchStart;
	private int referenceMatchEnd;

	private int queryMatchStart;
	private int queryMatchEnd;

	private int referenceMatchSize;
	private int queryMatchSize;

	private double identity;

	private int referenceSize;
	private int querySize;

	private double referenceMatchCoverage;
	private double queryMatchCoverage;

	private String referenceMatchName;
	private String queryMatchName;

	public NucmerResultMatchNames getNucmerResultMatchNames() {
		return new NucmerResultMatchNames(referenceMatchName, queryMatchName);
	}

	@Override
	public int compareTo(NucmerResult o) {
		return new CompareToBuilder() //
				.append(referenceMatchStart, o.referenceMatchStart) //
				.append(referenceMatchEnd, o.referenceMatchEnd) //
				.append(referenceMatchName, o.referenceMatchName) //
				.append(queryMatchName, o.queryMatchName) //
				.toComparison();
	}

}
