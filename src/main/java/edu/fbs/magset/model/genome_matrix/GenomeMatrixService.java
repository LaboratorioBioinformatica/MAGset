package edu.fbs.magset.model.genome_matrix;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.cazy.CazyAnnotation;
import edu.fbs.magset.model.cog.COGAnnotation;
import edu.fbs.magset.model.genome.Gene;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.model.genomic_region_interest.GenomeSegment;
import edu.fbs.magset.model.pangenome.NonconsideredPangenomeGene;
import edu.fbs.magset.model.pangenome.PangenomeGene;

@Service
public class GenomeMatrixService {

	public Map<Genome, GenomeMatrix> getGenomeMatrices(MagsetResults magset) {
		Map<Genome, GenomeMatrix> genomeMatrices = new TreeMap<>();

		if (!magset.shouldExportHtmlCsvFiles()) {
			return genomeMatrices;
		}
		if (!magset.hasAnnotatedGenomes()) {
			return genomeMatrices;
		}

		for (Genome genome : magset.getAllGenomes()) {
			genomeMatrices.put(genome, createGenomeMatrix(genome, magset));
		}
		return genomeMatrices;
	}

	private GenomeMatrix createGenomeMatrix(Genome genome, MagsetResults genocom) {
		String panarooGeneNameSufix = ".mRNA.0.CDS.1";
		GenomeMatrix genomeMatrix = new GenomeMatrix();
		genomeMatrix.setGenome(genome);

		Map<String, GeneMatrix> genesByName = new TreeMap<>();

		Collection<Gene> allGenes = genome.getAllGenes();
		for (Gene gene : allGenes) {
			String locusTag = gene.getLocusTag();
			String pangenomeId = locusTag;

			PangenomeGene pangenomeGene = genocom.getPangenome().getGenes().get(locusTag);

			if (pangenomeGene == null) {
				pangenomeGene = genocom.getPangenome().getGenes().get(locusTag + panarooGeneNameSufix);
			}
			if (pangenomeGene == null) {
				pangenomeGene = new NonconsideredPangenomeGene();
				pangenomeId = "-";
			}

			GenomeSegment gri = genocom.getGenomicRegionsOfInterest().getGenomicRegionByGene(genome, locusTag);
			COGAnnotation cog = genocom.getCogAnnotations().get(genome).getAnnotations().get(locusTag);
			CazyAnnotation cazy = null;
			if (genocom.getConfigurations().isExecuteCazyAnnotations()) {
				cazy = genocom.getCazyAnnotations().get(genome).getAnnotations().get(locusTag);
			}

			GeneMatrix geneMatrix = new GeneMatrix();
			geneMatrix.setCogAnnotation(cog);
			geneMatrix.setCazyAnnotation(cazy);
			geneMatrix.setGene(gene);
			geneMatrix.setGenome(genome);
			geneMatrix.setPangenomeGene(pangenomeGene);
			geneMatrix.setGriGenome(gri);
			geneMatrix.setPangenomeId(pangenomeId);

			genesByName.put(locusTag, geneMatrix);
		}

		genomeMatrix.setGenesByName(genesByName);
		return genomeMatrix;
	}
}
