package edu.fbs.magset.export.fasta;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map.Entry;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.model.genomic_region_interest.GenomeSegment;
import edu.fbs.magset.model.genomic_region_interest.GenomicRegionInterest;
import lombok.extern.java.Log;

@Log
public class GRIExportService {

	public void export(MagsetResults genocom) throws IOException {
		if (!genocom.getGenomicRegionsOfInterest().getGenomicRegionsById().isEmpty()) {
			exportGRIsFastaFile(genocom);
			exportGRIsFastaFileByGenome(genocom);
		}
	}

	private void exportGRIsFastaFile(MagsetResults genocom) throws IOException {
		String griFolderPath = genocom.getConfigurations().getGriFolder(genocom.getGenomicRegionsOfInterest());
		File griFolder = new File(griFolderPath);
		if (!griFolder.exists()) {
			griFolder.mkdir();
		}
		for (GenomicRegionInterest gri : genocom.getGenomicRegionsOfInterest().getGenomicRegionsById().values()) {
			for (GenomeSegment genomeSegment : gri.getGenomeSegments()) {
				String name = genomeSegment.getName();

				log.info("Exporting .fasta for " + name);

				PrintWriter printer = null;
				try {
					printer = new PrintWriter(griFolderPath + "/" + name + ".fasta");

					exportGRI(genomeSegment, name, printer);
				} catch (Exception e) {
					throw e;
				} finally {
					printer.close();
				}
			}
		}
	}

	private void exportGRIsFastaFileByGenome(MagsetResults magset) throws IOException {

		for (Entry<Genome, List<GenomeSegment>> entry : magset.getGenomicRegionsOfInterest().getGenomicRegionsByGenome()
				.entrySet()) {
			PrintWriter printer = null;
			Genome genome = entry.getKey();
			String griFolderPath = magset.getConfigurations().getGriFolder(magset.getGenomicRegionsOfInterest());
			try {
				File griFolder = new File(griFolderPath + "/by_genome");
				if (!griFolder.exists()) {
					griFolder.mkdir();
				}
				printer = new PrintWriter(griFolder.getAbsolutePath() + "/all_gris_" + genome.getName() + ".fasta");

				for (GenomeSegment genomeSegment : entry.getValue()) {
					String name = genomeSegment.getName();

					exportGRI(genomeSegment, name, printer);
				}
			} catch (Exception e) {
				throw e;
			} finally {
				printer.close();
			}
		}

	}

	private void exportGRI(GenomeSegment genomeSegment, String name, PrintWriter printer) {
		String sequence = genomeSegment.getDNASequence();
		int startAt = 0;
		int lengthByLine = 60;
		int totalSize = sequence.length();

		printer.println(">" + name);

		for (; startAt + lengthByLine < totalSize; startAt += lengthByLine) {
			printer.println(sequence.substring(startAt, startAt + lengthByLine));
		}
		printer.println(sequence.substring(startAt, totalSize));
	}
}
