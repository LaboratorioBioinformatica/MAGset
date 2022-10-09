package edu.fbs.magset.export.csv;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.SummaryService;
import edu.fbs.magset.export.javascript.SummaryResult;
import edu.fbs.magset.model.genome.Gene;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.model.genome_matrix.GeneMatrix;
import edu.fbs.magset.model.genome_matrix.GenomeMatrix;
import edu.fbs.magset.model.genomic_region_interest.GenomeSegment;
import edu.fbs.magset.model.genomic_region_interest.GenomicRegionInterest;
import edu.fbs.magset.model.pangenome.PangenomeGene;
import edu.fbs.magset.util.InputTypeEnum;

public class CSVExportService {

	public void export(MagsetResults genocom) throws IOException {
		exportSummary(genocom);
		exportGRIsFile(genocom);
		if (genocom.getConfigurations().getInputType().equals(InputTypeEnum.GBK)) {
			exportGRIGeneFiles(genocom);
			exportGenomesMatrix(genocom);
		}
	}

	private void exportSummary(MagsetResults genocom) throws IOException {
		File outputFolderFile = new File(genocom.getConfigurations().getResultFolder() + "/csv/");
		if (!outputFolderFile.exists()) {
			outputFolderFile.mkdirs();
		}
		FileWriter out = new FileWriter(outputFolderFile.getAbsolutePath() + "/summary.csv");
		CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(SummaryResult.CSV_HEADER));

		Collection<SummaryResult> result = new SummaryService().getSummaryResults(genocom);

		for (SummaryResult summary : result) {
			printer.printRecord( //
					summary.getGenomeName(), //
					summary.getGenomeSize(), //
					summary.getGenesQty(), //
					summary.getSpecificGenesQty(), //
					summary.getSharedGenesQty(), //
					summary.getCoreGenesQty(), //
					summary.getDiscartedGenes(), //
					summary.getGrisQty(), //
					summary.getSumOfGrisSize(), //
					summary.getGenesInGrisQty(), //
					summary.getGrisFoundByMAGCheckQty(), //
					summary.getSumOfGrisFoundByMAGCheckSize(), //
					summary.getGenesInGRIsFoundByMAGCheckQty(), //
					summary.getCogQty(), //
					summary.getCazyQty() //
			);
		}
		printer.close();
	}

	private void exportGRIsFile(MagsetResults genocom) throws IOException {
		File outputFolderFile = new File(genocom.getConfigurations().getResultFolder() + "/csv/");
		if (!outputFolderFile.exists()) {
			outputFolderFile.mkdirs();
		}
		FileWriter out = new FileWriter(outputFolderFile.getAbsolutePath() + "/gri_list.csv");
		CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(GenomeSegment.CSV_HEADER));

		for (GenomicRegionInterest gri : genocom.getGenomicRegionsOfInterest().getGenomicRegionsById().values()) {
			for (GenomeSegment genomeSegment : gri.getGenomeSegments()) {
				printer.printRecord( //
						genomeSegment.getNameWithoutGenome(), //
						genomeSegment.getGenome().getName(), //
						genomeSegment.getSequenceName(), //
						genomeSegment.getSize(), //
						genomeSegment.getStart(), //
						genomeSegment.getEnd(), //
						genomeSegment.getGenes().size(), //
						genomeSegment.getReadsCoverage().getCoveredPositions(), //
						genomeSegment.getReadsCoverage().getCoverage(), //
						genomeSegment.getReadsCoverage().getMeanDepth(), //
						genomeSegment.getReadsCoverage().foundInRawData(genocom.getConfigurations()), //
						genomeSegment.getComments() //
				);
			}
		}
		printer.close();
	}

	private void exportGRIGeneFiles(MagsetResults genocom) throws IOException {
		File outputFolderFile = new File(genocom.getConfigurations().getResultFolder() + "/csv/genes_by_gri/");
		if (!outputFolderFile.exists()) {
			outputFolderFile.mkdirs();
		}
		for (GenomicRegionInterest gri : genocom.getGenomicRegionsOfInterest().getGenomicRegionsById().values()) {
			for (GenomeSegment genomeSegment : gri.getGenomeSegments()) {
				FileWriter out = new FileWriter(
						outputFolderFile.getAbsolutePath() + "/" + genomeSegment.getName() + "_genes.csv");
				CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(Gene.CSV_HEADER));

				Collection<Gene> genes = genomeSegment.getGenes();

				for (Gene gene : genes) {
					printer.printRecord(defaultString(gene.getType()), //
							defaultString(gene.getLocusTag()), //
							defaultString(gene.getProduct()), //
							defaultString(gene.getProteinId()), //
							defaultString(gene.getMinString()), //
							defaultString(gene.getMaxString()), //
							gene.getStrand(), //
							defaultString(gene.getParent()), //
							defaultString(gene.getNote())//
					);
				}
				printer.close();
			}
		}
	}

	private void exportGenomesMatrix(MagsetResults genocom) throws IOException {
		File outputFolderFile = new File(genocom.getConfigurations().getResultFolder() + "/csv/genes_matrix/");
		if (!outputFolderFile.exists()) {
			outputFolderFile.mkdirs();
		}

		Map<Integer, Genome> genomes = genocom.getAllGenomesByIndex();

		for (Genome genome : genomes.values()) {
			FileWriter out = new FileWriter(
					outputFolderFile.getAbsolutePath() + "/" + genome.getName() + "_gene_matrix.csv");
			CSVPrinter printer = new CSVPrinter(out,
					CSVFormat.EXCEL.withDelimiter(';').withHeader(PangenomeGene.CSV_HEADER));

			GenomeMatrix genomeMatrix = genocom.getGenomeMatrices().get(genome);
			if (genomeMatrix != null) {
				for (GeneMatrix geneMatrix : genomeMatrix.getGenesByName().values()) {
					printer.printRecord(defaultString(geneMatrix.getGene().getType()), //
							defaultString(geneMatrix.getGene().getLocusTag()), //
							geneMatrix.getPangenomeId(), //
							defaultString(geneMatrix.getGene().getProduct()), //
							defaultString(geneMatrix.getGene().getProteinId()), //
							defaultString(geneMatrix.getGene().getMinString()), //
							defaultString(geneMatrix.getGene().getMaxString()), //
							geneMatrix.getGene().getStrand(), //
							defaultString(geneMatrix.getGene().getParent()), //
							geneMatrix.getPangenomeGene().isCore(genomes.size()), //
							geneMatrix.getPangenomeGene().isShared(genomes.size()), //
							geneMatrix.getPangenomeGene().isSpecific(), //
							(geneMatrix.getGriGenome() != null ? geneMatrix.getGriGenome().getName() : ""), //
							(geneMatrix.getCogAnnotation() != null ? geneMatrix.getCogAnnotation().getCogId() : ""), //
							(geneMatrix.getCogAnnotation() != null ? geneMatrix.getCogAnnotation().getCogDescription()
									: ""));
				}
			}
			printer.close();
		}
	}

}
