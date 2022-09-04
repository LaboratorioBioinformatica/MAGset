package edu.fbs.magset.pangenome;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.fixed_gff.FixedGFFFile;
import edu.fbs.magset.fixed_gff.GFFFixedService;
import edu.fbs.magset.genome_file.GenomeFile;

public class PangenomeService {

	public Pangenome getPangenome(GenomesComparator genocom) throws IOException {
		Pangenome pangenome = new Pangenome();
		Map<String, PangenomeGene> pangenomeGenes = getPangenomeGenesMap(
				genocom.getConfigurations().getPangenomeFolder(), genocom.getConfigurations().getAllGenomesMap());
		pangenome.setGenes(pangenomeGenes);

		for (GenomeFile genomeFile : genocom.getConfigurations().getAllGenomes()) {
			FixedGFFFile replacedIds = getReplacedIds(genocom.getConfigurations().getPangenomeFolder(), genomeFile);
			pangenome.addFixedGFFFile(genomeFile, replacedIds);
		}

		return pangenome;
	}

	private FixedGFFFile getReplacedIds(String roaryResultsFolder, GenomeFile genomeFile) throws IOException {
		return new GFFFixedService().getGFFFixedFile(roaryResultsFolder, genomeFile.getName());
	}

	private Map<String, PangenomeGene> getPangenomeGenesMap(String roaryResultsFolder,
			Map<Integer, GenomeFile> genomeFilesMap) throws FileNotFoundException, IOException {
		Reader in = new FileReader(roaryResultsFolder + "gene_presence_absence_roary.csv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

		Map<String, PangenomeGene> allGeneNames = new TreeMap<>();

		List<GenomeFile> genomeFiles = new ArrayList<>(genomeFilesMap.values());
		genomeFiles.sort(GenomeFile.COMPARATOR_BY_NAME);

		for (CSVRecord record : records) {
			createPangenomeGene(allGeneNames, genomeFiles, record);
		}
		return allGeneNames;
	}

	private void createPangenomeGene(Map<String, PangenomeGene> allGeneNames, List<GenomeFile> genomeFiles,
			CSVRecord record) {
		int numberOfcolumnsBeforeGene = 13;
		List<GenomeFile> genomes = new ArrayList<>();
		PangenomeGene genePresenceAbsence = new PangenomeGene(record.get(0), record.get(2),
				Integer.valueOf(record.get(3)), genomes);

		for (int i = 1; i <= genomeFiles.size(); i++) {
			String genesNameInGenome = record.get(numberOfcolumnsBeforeGene + i);
			if (genesNameInGenome == null || genesNameInGenome.isEmpty()) {
				continue;
			}
			
			genomes.add(genomeFiles.get(i - 1));
			
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

	public static void main(String[] args) {
		String[] genes = "Fabio".split(";");
		for (String geneName : genes) {
			System.out.println(geneName);
		}
	}
	
}
