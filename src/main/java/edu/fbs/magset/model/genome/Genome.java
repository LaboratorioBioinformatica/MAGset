package edu.fbs.magset.model.genome;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.FilenameUtils;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.Strand;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import edu.fbs.magset.Configurations;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Genome implements Comparable<Genome> {
	@ToString.Include
	@EqualsAndHashCode.Include
	private String path;
	@ToString.Include
	private String name;
	private int genomeId;

	private LinkedHashMap<String, DNASequence> dnaSequence;
	private LinkedHashMap<String, ProteinSequence> protSequences;
	private TreeMap<GeneLocation, Gene> genesLocationMap;
	private TreeMap<Location, DNASequence> dnaSequenceLocationMap;
	private long size;

	public Genome(LinkedHashMap<String, DNASequence> dnaSequence, LinkedHashMap<String, ProteinSequence> protSequences,
			String path, int genomeId, Configurations configurations) {
		super();
		this.dnaSequence = dnaSequence;
		this.protSequences = protSequences;
		loadDNASequenceLocationMap(dnaSequence);
		this.path = FilenameUtils.removeExtension(path) + configurations.getInputType().getExtension();
		this.name = FilenameUtils.removeExtension(Paths.get(path).getFileName().toString());
		this.genomeId = genomeId;

		loadGenesLocationMap();
		if (!dnaSequenceLocationMap.isEmpty()) {
			size = dnaSequenceLocationMap.lastKey().getEnd();
		}
	}

	private void loadDNASequenceLocationMap(LinkedHashMap<String, DNASequence> dnaSequences) {
		int currentPosition = 0;
		dnaSequenceLocationMap = new TreeMap<>();
		for (DNASequence dnaSequence : dnaSequences.values()) {
			Location key = new Location(currentPosition, dnaSequence.getLength() + currentPosition);
			currentPosition += dnaSequence.getLength();
			dnaSequenceLocationMap.put(key, dnaSequence);
		}
	}

	public String getDNASequence(String sequenceName, int start, int end) {
		return dnaSequence.get(sequenceName).getSequenceAsString(start, end, Strand.POSITIVE);
	}

	public List<Gene> getGenes(String sequenceName, int start, int end) {
		List<Gene> results = new ArrayList<>();
		ProteinSequence proteinSequence = protSequences.get(sequenceName);
		if (proteinSequence == null) {
			return results;
		}
		List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> features = proteinSequence
				.getFeatures();

		for (FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound> feature : features) {
			if (!isGene(feature)) {
				continue;
			}
			if ((feature.getLocations().getStart().getPosition() >= start
					&& feature.getLocations().getStart().getPosition() < end)
					|| (feature.getLocations().getEnd().getPosition() > start
							&& feature.getLocations().getEnd().getPosition() <= end)) {
				Gene gene = new Gene(sequenceName, feature);
				results.add(gene);
			}
		}

		return results;
	}

	public Collection<Gene> getAllGenes() {
		return getGenesLocationMap().values();
	}

	private void loadGenesLocationMap() {
		genesLocationMap = new TreeMap<>();
		for (Entry<String, ProteinSequence> proteinSequence : protSequences.entrySet()) {
			List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> features = proteinSequence
					.getValue().getFeatures();

			for (FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound> feature : features) {
				if (!isGene(feature)) {
					continue;
				}
				Gene gene = new Gene(proteinSequence.getKey(), feature);
				gene.setMin(gene.getMin());
				gene.setMax(gene.getMax());
				GeneLocation key = new GeneLocation(proteinSequence.getKey(), gene.getMin(), gene.getMax());
				genesLocationMap.put(key, gene);
			}
		}
	}

	private boolean isGene(FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound> feature) {
		String type = feature.getType();
		if (type.equals("CDS") //
				|| type.equals("rRNA") || type.equals("tRNA") || type.equals("misc_RNA")) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(Genome o) {
		return Integer.compare(genomeId, o.genomeId);
	}
}
