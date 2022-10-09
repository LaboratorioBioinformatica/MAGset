package edu.fbs.magset.model.pangenome;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genome.Genome;

@Service
public class PangenomeService {

	public Pangenome getPangenome(MagsetResults magset) throws IOException {
		Pangenome pangenome = new Pangenome();

		if (!magset.shouldExportHtmlCsvFiles()) {
			return pangenome;
		}
		if (!magset.hasAnnotatedGenomes()) {
			return pangenome;
		}
		Map<String, PangenomeGene> pangenomeGenes = getPangenomeGenesMap(
				magset.getConfigurations().getPangenomeFolder(), magset.getAllGenomes());
		pangenome.setGenes(pangenomeGenes);

		return pangenome;
	}

	private Map<String, PangenomeGene> getPangenomeGenesMap(String roaryResultsFolder, Collection<Genome> genomes)
			throws FileNotFoundException, IOException {
		Reader in = new FileReader(roaryResultsFolder + "gene_presence_absence_roary.csv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

		Map<String, PangenomeGene> allGeneNames = new TreeMap<>();

		for (CSVRecord record : records) {
			createPangenomeGene(allGeneNames, genomes, record);
		}
		return allGeneNames;
	}

	private void createPangenomeGene(Map<String, PangenomeGene> allGeneNames, Collection<Genome> genomes,
			CSVRecord record) {
		Map<Genome, String> geneNames = new HashMap<>();
		PangenomeGene genePresenceAbsence = new PangenomeGene(record.get(0), record.get(2),
				Integer.valueOf(record.get(3)), geneNames);

		for (Genome genome : genomes) {
			String genesNameInGenome = record.get(genome.getName());
			if (genesNameInGenome == null || genesNameInGenome.isEmpty()) {
				continue;
			}

			geneNames.put(genome, genesNameInGenome);

			addPangenomeGenes(allGeneNames, genePresenceAbsence, genesNameInGenome);
		}
	}

	private void addPangenomeGenes(Map<String, PangenomeGene> allGeneNames, PangenomeGene genePresenceAbsence,
			String genesNameInGenome) {
		String[] genes = genesNameInGenome.split(";");
		for (String geneName : genes) {
			allGeneNames.put(geneName, genePresenceAbsence);
		}
	}
}