package edu.fbs.magset;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import edu.fbs.magset.ani.AniResults;
import edu.fbs.magset.cazy.CazyAnnotations;
import edu.fbs.magset.cog.COGAnnotations;
import edu.fbs.magset.genome_file.GenomeFile;
import edu.fbs.magset.genome_matrix.GenomeMatrix;
import edu.fbs.magset.genomic_region_interest.GenomicRegionsInterest;
import edu.fbs.magset.pangenome.Pangenome;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class GenomesComparator {

	private Configurations configurations;
	private AniResults aniResults;
	private Pangenome pangenome;
	private GenomicRegionsInterest genomicRegionsOfInterest;
	private Map<GenomeFile, COGAnnotations> cogsAnnotation = new TreeMap<>();
	private Map<GenomeFile, CazyAnnotations> cazyAnnotation = new TreeMap<>();
	private Map<GenomeFile, GenomeMatrix> genomesMatrix = new TreeMap<>();
	private boolean magCheckExecuted;

	public GenomesComparator(Configurations configurations) {
		this.configurations = configurations;
	}

	public GenomeFile getGenomeFileByNameWithoutExtension(String genomeName) {
		Collection<GenomeFile> genomeFiles = configurations.getAllGenomes();
		for (GenomeFile genomeFile : genomeFiles) {
			String name = genomeFile.getName();
			if (name.equals(genomeName)) {
				return genomeFile;
			}
		}
		throw new IllegalArgumentException("GenomeName not found: " + genomeName);
	}

	public int getGenomesQty() {
		return configurations.getAllGenomes().size();
	}

	public GenomeFile getMagOfInterest() {
		return configurations.getMagGenomeFile();
	}
}
