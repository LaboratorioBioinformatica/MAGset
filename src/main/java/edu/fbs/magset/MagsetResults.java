package edu.fbs.magset;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.fbs.magset.model.ani.AniResults;
import edu.fbs.magset.model.cazy.CazyAnnotations;
import edu.fbs.magset.model.cog.COGAnnotations;
import edu.fbs.magset.model.genome.GenomeFile;
import edu.fbs.magset.model.genome_matrix.GenomeMatrix;
import edu.fbs.magset.model.genomic_region_interest.GenomicRegionsInterest;
import edu.fbs.magset.model.pangenome.Pangenome;
import edu.fbs.magset.util.ExportEnum;
import edu.fbs.magset.util.InputTypeEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class MagsetResults {

	private Configurations configurations;
	private GenomeFile magGenomeFile;
	private Map<Integer, GenomeFile> referenceGenomeFiles = new TreeMap<>();
	private AniResults aniResults;
	private Pangenome pangenome;
	private GenomicRegionsInterest genomicRegionsOfInterest;
	private Map<GenomeFile, COGAnnotations> cogAnnotations = new TreeMap<>();
	private Map<GenomeFile, CazyAnnotations> cazyAnnotations = new TreeMap<>();
	private Map<GenomeFile, GenomeMatrix> genomeMatrices = new TreeMap<>();
	private boolean magCheckExecuted;

	public MagsetResults(Configurations configurations) throws Exception {
		this.configurations = configurations;
		loadAllGenomeFiles();
	}

	public GenomeFile getGenomeFileByNameWithoutExtension(String genomeName) {
		Collection<GenomeFile> genomeFiles = getAllGenomes();
		for (GenomeFile genomeFile : genomeFiles) {
			String name = genomeFile.getName();
			if (name.equals(genomeName)) {
				return genomeFile;
			}
		}
		throw new IllegalArgumentException("GenomeName not found: " + genomeName);
	}

	public int getGenomesQty() {
		return getAllGenomes().size();
	}

	public List<GenomeFile> getAllGenomes() {
		List<GenomeFile> genomes = new ArrayList<>();
		genomes.add(getMagGenomeFile());
		genomes.addAll(getReferenceGenomeFiles().values());
		return genomes;
	}

	public Map<Integer, GenomeFile> getAllGenomesMap() {
		Map<Integer, GenomeFile> genomes = new TreeMap<>();
		genomes.put(0, getMagGenomeFile());
		genomes.putAll(getReferenceGenomeFiles());
		return genomes;
	}

	public void loadAllGenomeFiles() throws Exception {
		this.magGenomeFile = new GenomeFile(
				configurations.getConvertedGenomesFolder() + configurations.getMagGenomeFile(),
				configurations.getMagGenomeFile(), configurations);

		for (Entry<Integer, String> entry : configurations.getReferenceGenomeFiles().entrySet()) {
			String filePath = configurations.getConvertedGenomesFolder() + entry.getValue();
			String fileName = Paths.get(filePath).getFileName().toString();
			this.referenceGenomeFiles.put(entry.getKey(),
					new GenomeFile(filePath, fileName, entry.getKey(), configurations));
		}
	}

	public boolean shouldExportFastaFiles() {
		return getConfigurations().getExportType().equals(ExportEnum.EXPORT_CLUSTERED_GRIS)
				|| getConfigurations().getExportType().equals(ExportEnum.EXPORT_GRIS);
	}

	public boolean shouldExportHtmlCsvFiles() {
		return getConfigurations().getExportType().equals(ExportEnum.EXPORT_CSV_HTML);
	}

	public boolean hasAnnotatedGenomes() {
		return configurations.getInputType().equals(InputTypeEnum.GBK);
	}

}
