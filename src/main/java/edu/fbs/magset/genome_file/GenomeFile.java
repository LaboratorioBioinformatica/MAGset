package edu.fbs.magset.genome_file;

import java.util.Collection;
import java.util.Comparator;

import org.apache.commons.io.FilenameUtils;

import edu.fbs.magset.Configurations;
import edu.fbs.magset.genome.Gene;
import edu.fbs.magset.genome.Genome;
import edu.fbs.magset.genome.GenomeBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class GenomeFile implements Comparable<GenomeFile> {
	@EqualsAndHashCode.Include
	private String path;
	@ToString.Include
	private String name;
	private int genomeId;
	private Genome genome;

	public GenomeFile(String path, String name, Configurations configurations) throws Exception {
		this(path, name, 0, configurations);
	}

	public GenomeFile(String path, String name, int genomeId, Configurations configurations) throws Exception {
		super();
		this.path = FilenameUtils.removeExtension(path) + configurations.getInputType().getExtension();
		this.name = FilenameUtils.removeExtension(name);
		this.genomeId = genomeId;
		this.genome = GenomeBuilder.buildGenome(this.path, this, configurations);

	}

	@Override
	public int compareTo(GenomeFile o) {
		return Integer.compare(genomeId, o.genomeId);
	}

	public Collection<Gene> getAllGenes() {
		return genome.getGenesLocationMap().values();
	}

	public Collection<Gene> getGenes(String sequenceName, int start, int end) {
		return genome.getGenes(sequenceName, start, end);
	}

	public String getName() {
		return name;
	}

	public static final Comparator<GenomeFile> COMPARATOR_BY_NAME = new Comparator<GenomeFile>() {
		public int compare(GenomeFile o1, GenomeFile o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	};
}
