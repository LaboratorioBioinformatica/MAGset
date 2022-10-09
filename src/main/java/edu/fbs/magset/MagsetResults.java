package edu.fbs.magset;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.fbs.magset.model.ani.AniResults;
import edu.fbs.magset.model.cazy.CazyAnnotations;
import edu.fbs.magset.model.cog.COGAnnotations;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.model.genome.GenomeBuilder;
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
	private Genome mag;
	private Map<Integer, Genome> referencesByIndex = new LinkedHashMap<>();
	private Map<Integer, Genome> allGenomesByIndex = new LinkedHashMap<>();

	private AniResults aniResults;
	private Pangenome pangenome;
	private GenomicRegionsInterest genomicRegionsOfInterest;
	private Map<Genome, COGAnnotations> cogAnnotations = new TreeMap<>();
	private Map<Genome, CazyAnnotations> cazyAnnotations = new TreeMap<>();
	private Map<Genome, GenomeMatrix> genomeMatrices = new TreeMap<>();

	public MagsetResults(Configurations configurations) throws Exception {
		this.configurations = configurations;
		loadGenomes();
	}

	public int getGenomesQty() {
		return getAllGenomes().size();
	}

	public Collection<Genome> getAllGenomes() {
		return allGenomesByIndex.values();
	}

	public void loadGenomes() throws Exception {
		Integer magIndex = 0;
		this.mag = loadGenome(magIndex, configurations.getMagGenomeFile());
		this.allGenomesByIndex.put(magIndex, this.mag);

		for (Entry<Integer, String> entry : configurations.getReferenceGenomeFiles().entrySet()) {
			Integer referenceIndex = entry.getKey();
			Genome reference = loadGenome(referenceIndex, entry.getValue());
			this.allGenomesByIndex.put(referenceIndex, reference);
			this.referencesByIndex.put(referenceIndex, reference);
		}
	}

	private Genome loadGenome(int index, String genomeFile) throws Exception {
		String magPath = configurations.getConvertedGenomesFolder() + genomeFile;
		return GenomeBuilder.build(magPath, index, configurations);
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
