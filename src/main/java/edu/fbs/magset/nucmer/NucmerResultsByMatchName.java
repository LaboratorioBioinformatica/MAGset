package edu.fbs.magset.nucmer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import lombok.Data;

@Data
public class NucmerResultsByMatchName implements Comparable<NucmerResultsByMatchName> {

	private int referenceSize;
	private int querySize;

	private double sumOfReferenceMatchCoverage;
	private double sumOfQueryMatchCoverage;

	private String referenceMatchName;
	private String queryMatchName;

	private List<NucmerResult> nucmerResults = new ArrayList<>();

	public NucmerResultsByMatchName(String referenceMatchName, String queryMatchName) {
		super();
		this.referenceMatchName = referenceMatchName;
		this.queryMatchName = queryMatchName;
	}

	@Override
	public int compareTo(NucmerResultsByMatchName o) {
		return new CompareToBuilder().append(referenceSize, o.referenceSize) //
				.append(querySize, o.querySize) //
				.append(sumOfReferenceMatchCoverage, o.sumOfReferenceMatchCoverage) //
				.append(sumOfQueryMatchCoverage, o.sumOfQueryMatchCoverage) //
				.append(referenceMatchName, o.referenceMatchName) //
				.append(queryMatchName, o.queryMatchName) //
				.toComparison();
	}

	public void addNucmerResult(NucmerResult nucmerResult) {
		this.referenceSize = nucmerResult.getReferenceSize();
		this.querySize = nucmerResult.getQuerySize();
		this.sumOfReferenceMatchCoverage += nucmerResult.getReferenceMatchCoverage();
		this.sumOfQueryMatchCoverage += nucmerResult.getQueryMatchCoverage();
		nucmerResults.add(nucmerResult);
	}

}