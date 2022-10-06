package edu.fbs.magset.model.genome;

import org.apache.commons.lang3.builder.CompareToBuilder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GeneLocation extends Location {

	private String parent;

	public GeneLocation(String parent, int start, int end) {
		super(start, end);
		this.parent = parent;
	}

	@Override
	public int compareTo(Location o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		if (o instanceof GeneLocation) {
			compareToBuilder.append(this.parent, ((GeneLocation) o).parent);
		}
		return compareToBuilder.appendSuper(super.compareTo(o)).toComparison();
	}

}
