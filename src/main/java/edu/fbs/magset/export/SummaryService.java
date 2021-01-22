package edu.fbs.magset.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.InputTypeEnum;
import edu.fbs.magset.export.javascript.SummaryResult;
import edu.fbs.magset.genome_file.GenomeFile;
import edu.fbs.magset.genome_matrix.GeneMatrix;
import edu.fbs.magset.genomic_region_interest.GenomeSegment;

public class SummaryService {

	public List<SummaryResult> getSummaryResults(GenomesComparator genocom) throws IOException {
		List<SummaryResult> result = new ArrayList<>();
		Collection<GenomeFile> genomes = genocom.getConfigurations().getAllGenomes();

		for (GenomeFile genome : genomes) {
			int genesQty = genome.getAllGenes().size();
			Collection<GeneMatrix> genes = new ArrayList<>();
			if (genocom.getGenomesMatrix().get(genome) != null) {
				genes = genocom.getGenomesMatrix().get(genome).getGenesByName().values();
			}
			long specificGenesQty = genes.stream().filter(x -> x.getPangenomeGene().isSpecific()).count();
			long sharedGenesQty = genes.stream().filter(x -> x.getPangenomeGene().isShared(genomes.size())).count();
			long coreGenesQty = genes.stream().filter(x -> x.getPangenomeGene().isCore(genomes.size())).count();

			long discartedGenes = genesQty - specificGenesQty - sharedGenesQty - coreGenesQty;

			int cogQty = 0;
			int cazyQty = 0;

			if (!genocom.getConfigurations().getInputType().equals(InputTypeEnum.FASTA)) {
				cogQty = genocom.getCogsAnnotation().get(genome).getAnnotations().size();
				if (genocom.getConfigurations().isExecuteCazyAnnotations()) {
					cazyQty = genocom.getCazyAnnotation().get(genome).getAnnotations().size();
				}
			}
			List<GenomeSegment> gris = genocom.getGenomicRegionsOfInterest().getGenomicRegionsByGenome().get(genome);

			int grisQty = 0;
			long grisSize = 0;
			int genesInGrisQty = 0;
			long grisFoundByMAGCheckQty = 0;
			long grisFoundByMAGCheckSize = 0;
			long genesInGRIsFoundByMAGCheckQty = 0;
			if (gris != null) {
				grisQty = gris.size();
				grisSize = gris.stream().collect(Collectors.summingLong(GenomeSegment::getSize));
				genesInGrisQty = gris.stream().collect(Collectors.summingInt(GenomeSegment::getGenesQty));
				grisFoundByMAGCheckQty = gris.stream().filter(x -> x.foundInRawData(genocom.getConfigurations()))
						.count();
				grisFoundByMAGCheckSize = gris.stream().filter(x -> x.foundInRawData(genocom.getConfigurations()))
						.collect(Collectors.summingLong(GenomeSegment::getSize));
				genesInGRIsFoundByMAGCheckQty = gris.stream().filter(x -> x.foundInRawData(genocom.getConfigurations()))
						.collect(Collectors.summingInt(GenomeSegment::getGenesQty));
			}

			result.add(new SummaryResult( //
					escapeEspecialCharacters(genome.getName()), //
					genome.getGenome().getSize(), //
					genesQty, //
					(int) specificGenesQty, //
					(int) sharedGenesQty, //
					(int) coreGenesQty, //
					(int) discartedGenes, //
					grisQty, //
					grisSize, //
					genesInGrisQty, //
					(int) grisFoundByMAGCheckQty, //
					grisFoundByMAGCheckSize, //
					(int) genesInGRIsFoundByMAGCheckQty, //
					cogQty, //
					cazyQty //
			));

		}
		return result;
	}

	private String escapeEspecialCharacters(String string) {
		return string.replaceAll("\'", "\\\\'");
	}
}
