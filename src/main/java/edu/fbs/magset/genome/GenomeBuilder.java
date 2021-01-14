package edu.fbs.magset.genome;

import java.io.File;
import java.util.LinkedHashMap;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;

import edu.fbs.magset.Configurations;
import edu.fbs.magset.InputTypeEnum;
import edu.fbs.magset.genome_file.GenomeFile;

public class GenomeBuilder {

	public static Genome buildGenome(String path, GenomeFile genomeFile, Configurations configurations)
			throws Exception {

		File file = new File(path);

		LinkedHashMap<String, ProteinSequence> protSequences = new LinkedHashMap<>();
		LinkedHashMap<String, DNASequence> dnaSequence = new LinkedHashMap<>();
		if (configurations.getInputType().equals(InputTypeEnum.GBK)) {
			protSequences = GenbankReaderHelper.readGenbankProteinSequence(file);
			dnaSequence = GenbankReaderHelper.readGenbankDNASequence(file);
		} else if (configurations.getInputType().equals(InputTypeEnum.GFF)) {
			throw new UnsupportedOperationException("GFF files are not supported...");
		} else {
			dnaSequence = FastaReaderHelper.readFastaDNASequence(file);
		}
		return new Genome(dnaSequence, protSequences, genomeFile);
	}

}
