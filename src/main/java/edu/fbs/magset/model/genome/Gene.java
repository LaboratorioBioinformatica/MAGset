package edu.fbs.magset.model.genome;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.List;

import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.features.Qualifier;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import lombok.Data;

@Data
public class Gene implements Comparable<Gene> {

	private String type;
	private String locusTag;
	private String product;
	private String proteinId;
	private Integer min;
	private String minString;
	private Integer max;
	private String maxString;
	private char strand;
	private String parent;
	private String note;

	public Gene(String parent, FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound> feature) {
		this.type = feature.getType();
		this.locusTag = getQualifier(feature, "locus_tag");
		this.product = getQualifier(feature, "product");
		this.proteinId = getQualifier(feature, "protein_id");
		this.note = getQualifier(feature, "note");
		this.parent = parent;

		this.min = feature.getLocations().getStart().getPosition();
		this.minString = feature.getLocations().getStart().getPosition() + "";

		this.max = feature.getLocations().getEnd().getPosition();
		this.maxString = feature.getLocations().getEnd().getPosition() + "";

		this.strand = feature.getLocations().getStrand().getStringRepresentation().charAt(0);
	}

	private String getQualifier(FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound> feature,
			String key) {
		List<Qualifier> results = feature.getQualifiers().get(key);
		if (results != null && results.size() > 0) {
			return results.get(0).getValue();
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(defaultString(getType()));
		result.append("\t");
		result.append(defaultString(getLocusTag()));
		result.append("\t");
		result.append(defaultString(getProduct()));
		result.append("\t");
		result.append(defaultString(getProteinId()));
		result.append("\t");
		result.append(defaultString(getMinString()));
		result.append("\t");
		result.append(defaultString(getMaxString()));
		result.append("\t");
		result.append(getStrand());
		result.append("\t");
		result.append(defaultString(getParent()));
		result.append("\t");
		result.append(defaultString(getNote()));

		return result.toString();
	}

	@Override
	public int compareTo(Gene o) {
		return min.compareTo(o.min);
	}

	public static final String[] CSV_HEADER = { "type", "locus_tag", "product", "protein_id", "segment_id", "start",
			"end", "strand", "parent", "note" };

}
