package edu.fbs.magset.export.javascript;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryResult {

	private String genomeName;
	private long genomeSize;
	private int genesQty;
	private int specificGenesQty;
	private int sharedGenesQty;
	private int coreGenesQty;
	private int discartedGenes;
	private int grisQty;
	private long sumOfGrisSize;
	private int genesInGrisQty;
	private int grisFoundByMAGCheckQty;
	private long sumOfGrisFoundByMAGCheckSize;
	private int genesInGRIsFoundByMAGCheckQty;
	private int cogQty;
	private int cazyQty;

	public static final String[] CSV_HEADER = { "genome_name", "genome_size", "pangenome_genes",
			"pangenome_specific_genes", "pangenome_shared_genes", "pangenome_core_genes", "pangenome_discarted_genes",
			"gris", "sum_of_gris_size", "genes_in_gris", "gris_found_by_magcheck", "sum_of_gris_found_by_magcheck_size",
			"genes_in_gris_found_by_magcheck", "cog_annotations", "cazy_annotations" };
}
