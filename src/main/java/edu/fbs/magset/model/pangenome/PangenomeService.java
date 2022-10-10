package edu.fbs.magset.model.pangenome;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.Genome;

@Service
public class PangenomeService {

	public Pangenome getPangenome(MagsetResults magset) throws IOException {
		Pangenome pangenome = new Pangenome(magset.getAllGenomes());

		if (!magset.shouldExportHtmlCsvFiles()) {
			return pangenome;
		}
		if (!magset.hasAnnotatedGenomes()) {
			return pangenome;
		}
		loadPangenomeGenes(magset.getConfigurations().getPangenomeFolder(), magset.getAllGenomes(), pangenome);
		return pangenome;
	}

	private void loadPangenomeGenes(String roaryResultsFolder, Collection<Genome> genomes, Pangenome pangenome)
			throws FileNotFoundException, IOException {
		Reader in = new FileReader(roaryResultsFolder + "gene_presence_absence_roary.csv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

		for (CSVRecord record : records) {
			loadPangenomeGene(genomes, record, pangenome);
		}
	}

	private void loadPangenomeGene(Collection<Genome> genomes, CSVRecord record, Pangenome pangenome) {
		Map<Genome, String> genesByGenome = new HashMap<>();
		PangenomeGene pangenomeGene = new PangenomeGene(record.get(0), record.get(2), Integer.valueOf(record.get(3)),
				genesByGenome);

		for (Genome genome : genomes) {
			String genes = record.get(genome.getName());
			if (genes == null || genes.isEmpty()) {
				continue;
			}
			String[] genesArray = genes.split(";");
			for (String gene : genesArray) {
				genesByGenome.put(genome, gene);
			}
		}
		pangenome.addPangenomeGene(pangenomeGene);
	}
}