package edu.fbs.magset.pangenome;

import edu.fbs.magset.genome_file.GenomeFile;

public class NonconsideredPangenomeGene extends PangenomeGene {

	public NonconsideredPangenomeGene() {
		super(null, null, null, null);
	}

	public boolean existsInGenome(GenomeFile genome) {
		return false;
	}

	public boolean isSpecific() {
		return false;
	}

	public boolean isCore(int genomesQuantity) {
		return false;
	}

	public boolean isShared(int genomesQuantity) {
		return false;
	}
}
