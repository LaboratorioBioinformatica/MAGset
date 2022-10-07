package edu.fbs.magset.model.genome;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.DNASequence.DNAType;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import edu.fbs.magset.Configurations;
import edu.fbs.magset.util.InputTypeEnum;

public class GenomeBuilder {

	public static Genome buildGenome(String path, GenomeFile genomeFile, Configurations configurations)
			throws Exception {

		File file = new File(path);

		LinkedHashMap<String, ProteinSequence> protSequences = new LinkedHashMap<>();
		LinkedHashMap<String, DNASequence> dnaSequences = new LinkedHashMap<>();
		if (configurations.getInputType().equals(InputTypeEnum.GBK)) {
			protSequences = GenbankReaderHelper.readGenbankProteinSequence(file);
			dnaSequences = GenbankReaderHelper.readGenbankDNASequence(file);

			for (Entry<String, DNASequence> entry : dnaSequences.entrySet()) {
				List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> sources = entry
						.getValue().getFeaturesByType("source");
				if (sources.size() > 0) {
					if (sources.get(0).getQualifiers().get("plasmid") != null) {
						entry.getValue().setDNAType(DNAType.PLASMID);
					}
				}
			}

		} else if (configurations.getInputType().equals(InputTypeEnum.GFF)) {
			throw new UnsupportedOperationException("GFF files are not supported...");
		} else {
			dnaSequences = FastaReaderHelper.readFastaDNASequence(file);
		}

		return new Genome(dnaSequences, protSequences, genomeFile);
	}

}
