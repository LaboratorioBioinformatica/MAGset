package edu.fbs.magset.model.genomic_region_interest;

public enum GenomicRegionInterestType {
	POSITIVE("P"), NEGATIVE("N");

	private String initials;

	private GenomicRegionInterestType(String initials) {
		this.initials = initials;
	}

	public String getInitials() {
		return initials;
	}
}
