package edu.fbs.magset.model.nucmer;

import org.apache.commons.lang3.builder.CompareToBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NucmerResultMatchNames implements Comparable<NucmerResultMatchNames> {
	private String referenceMatchName;
	private String queryMatchName;

	@Override
	public int compareTo(NucmerResultMatchNames o) {
		return new CompareToBuilder()//
				.append(referenceMatchName, o.referenceMatchName) //
				.append(queryMatchName, o.queryMatchName) //
				.toComparison();
	}
}