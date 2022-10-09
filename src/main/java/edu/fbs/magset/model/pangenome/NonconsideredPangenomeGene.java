package edu.fbs.magset.model.pangenome;

import edu.fbs.magset.model.genome.Genome;

public class NonconsideredPangenomeGene extends PangenomeGene {

	public NonconsideredPangenomeGene() {
		super(null, null, null, null);
	}

	public boolean existsInGenome(Genome genome) {
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
