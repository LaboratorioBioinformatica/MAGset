package edu.fbs.magset.genome_matrix;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.cazy.CazyAnnotation;
import edu.fbs.magset.cog.COGAnnotation;
import edu.fbs.magset.genome.Gene;
import edu.fbs.magset.genome_file.GenomeFile;
import edu.fbs.magset.genomic_region_interest.GenomeSegment;
import edu.fbs.magset.pangenome.NonconsideredPangenomeGene;
import edu.fbs.magset.pangenome.PangenomeGene;

public class GenomeMatrixService {

	public Map<GenomeFile, GenomeMatrix> loadGenomesMatrix(GenomesComparator genocom) {
		Map<GenomeFile, GenomeMatrix> genomesMatrix = new TreeMap<>();

		for (GenomeFile genomeFile : genocom.getConfigurations().getAllGenomes()) {
			genomesMatrix.put(genomeFile, createGenomeMatrix(genomeFile, genocom));
		}
		return genomesMatrix;
	}

	private GenomeMatrix createGenomeMatrix(GenomeFile genomeFile, GenomesComparator genocom) {
		GenomeMatrix genomeMatrix = new GenomeMatrix();
		genomeMatrix.setGenomeFile(genomeFile);

		Map<String, GeneMatrix> genesByName = new TreeMap<>();

		Collection<Gene> allGenes = genomeFile.getAllGenes();
		for (Gene gene : allGenes) {
			String locusTag = gene.getLocusTag();
			String pangenomeId = locusTag;

			PangenomeGene pangenomeGene = genocom.getPangenome().getGenes().get(locusTag);

			if (pangenomeGene == null) {
				pangenomeGene = genocom.getPangenome().getReplacedGeneInPangenome(genomeFile, locusTag);
				pangenomeId = genocom.getPangenome().getReplacedIdInPangenome(genomeFile, locusTag);
			}
			if (pangenomeGene == null) {
				pangenomeGene = new NonconsideredPangenomeGene();
				pangenomeId = "-";
			}

			GenomeSegment gri = genocom.getGenomicRegionsOfInterest().getGenomicRegionByGene(genomeFile, locusTag);
			COGAnnotation cog = genocom.getCogsAnnotation().get(genomeFile).getAnnotations().get(locusTag);
			CazyAnnotation cazy = null;
			if (genocom.getConfigurations().isExecuteCazyAnnotations()) {
				cazy = genocom.getCazyAnnotation().get(genomeFile).getAnnotations().get(locusTag);
			}

			GeneMatrix geneMatrix = new GeneMatrix();
			geneMatrix.setCogAnnotation(cog);
			geneMatrix.setCazyAnnotation(cazy);
			geneMatrix.setGene(gene);
			geneMatrix.setGenome(genomeFile);
			geneMatrix.setPangenomeGene(pangenomeGene);
			geneMatrix.setGriGenome(gri);
			geneMatrix.setPangenomeId(pangenomeId);

			genesByName.put(locusTag, geneMatrix);
		}

		genomeMatrix.setGenesByName(genesByName);
		return genomeMatrix;
	}
}
