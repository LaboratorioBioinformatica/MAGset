package edu.fbs.magset.genomic_region_interest.coverage;

import edu.fbs.magset.Configurations;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class GRIRawReadsCoverage {
	private String griName;
	private int coveredPositions;
	private double coverage;
	private double meanDepth;

	public boolean foundInRawData(Configurations configurations) {
		return foundInRawData(configurations.getMagCheckMinimalCoverage(),
				configurations.getMagCheckMinimalMeanDepth());
	}

	public boolean foundInRawData(double minimalCoveredPositions, double minimalMeanDepth) {
		if (coverage >= minimalCoveredPositions * 100 //
				&& meanDepth >= minimalMeanDepth) {
			return true;
		}
		return false;
	}
}
