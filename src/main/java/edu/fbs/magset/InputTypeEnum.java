package edu.fbs.magset;

public enum InputTypeEnum {
	GFF(".gff"), GBK(".gb"), FASTA(".fasta");

	private String extension;
	
	private InputTypeEnum(String extension) {
		this.extension = extension;
	}



	public String getExtension() {
		return extension;
	}
}
