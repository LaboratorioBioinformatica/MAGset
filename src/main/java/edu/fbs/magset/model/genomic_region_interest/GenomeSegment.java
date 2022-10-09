package edu.fbs.magset.model.genomic_region_interest;

import java.util.Collection;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.biojava.nbio.core.sequence.DNASequence.DNAType;

import edu.fbs.magset.Configurations;
import edu.fbs.magset.model.genome.Gene;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.model.genomic_region_interest.coverage.GRIRawReadsCoverage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(exclude = "genomicRegionInterest")
public class GenomeSegment implements Comparable<GenomeSegment> {
	private GenomicRegionInterest genomicRegionInterest;
	private int id;
	@ToString.Include
	private Genome genome;
	@ToString.Include
	private String sequenceName;
	@ToString.Include
	private int start;
	@ToString.Include
	private int end;
	private DNAType dnaType;
	private Collection<Gene> genes;
	private GRIRawReadsCoverage readsCoverage = new GRIRawReadsCoverage();

	public GenomeSegment(Genome genome, String sequenceName, DNAType dnaType, int start, int end) {
		super();
		this.sequenceName = sequenceName;
		this.genome = genome;
		this.dnaType = dnaType;
		this.start = start;
		this.end = end;
	}

	public boolean isGreaterOrEqualThan(int minimumRGISize) {
		return getSize() >= minimumRGISize;
	}

	public int getSize() {
		return Math.abs(Math.abs(end) - Math.abs(start)) + 1;
	}

	public Collection<Gene> getGenes() {
		if (genes == null) {
			genes = getGenome().getGenes(sequenceName, this.getStart(), this.getEnd());
		}
		return genes;
	}

	public int getGenesQty() {
		return getGenes().size();
	}

	public String getName() {
		return getNameWithoutGenome() + "_" + genome.getName();
	}

	public String getNameWithoutGenome() {
		return genomicRegionInterest.getName() + "_" + String.format("%02d", this.getId());
	}

	public int getAbsoluteStart() {
		return Math.abs(start);
	}

	public String getDNASequence() {
		return genome.getDNASequence(sequenceName, Math.abs(getStart()), Math.abs(getEnd()));
	}

	public boolean isPositiveStrand() {
		if (getEnd() < 0) {
			return false;
		}
		return true;
	}

	public boolean foundInRawData(Configurations configurations) {
		return readsCoverage.foundInRawData(configurations);
	}

	public static final String[] CSV_HEADER = { "gri_id", "genome_name", "sequence_name", "size", "start", "end",
			"genes_qty", "covered_positions", "coverage", "mean_depth", "found_in_raw_data", "comments" };

	@Override
	public int compareTo(GenomeSegment o) {
		return new CompareToBuilder().append(genome, o.genome).append(sequenceName, o.sequenceName)
				.append(start, o.start).append(end, o.end).build();
	}

	public String getComments() {
		if (getDnaType().equals(DNAType.PLASMID)) {
			return "plasmid region";
		}
		return "";
	}

}