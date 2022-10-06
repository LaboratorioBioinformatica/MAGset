package edu.fbs.magset.model.genomic_region_interest.coverage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.model.genomic_region_interest.GenomeSegment;
import edu.fbs.magset.model.genomic_region_interest.GenomicRegionInterest;
import lombok.extern.java.Log;

/**
 * Checks coverage in raw reads, identifying if the negative GRIs were an error
 * in the process of assembling the MAG genome.
 * 
 * @author fabio.sanchez
 *
 */
@Log
public class MAGCheckService {

	public void loadCoverageRawReads(Set<GenomicRegionInterest> gris, MagsetResults genocom) throws IOException {

		String path = genocom.getConfigurations().getMAGCheckFolder();
		File magcheckFolder = new File(path);

		if (!magcheckFolder.exists()) {
			log.info("Folder " + path
					+ " not found. This folder is necessary to execute MAGCheck step, skiping. To execute this optional step, please inform the raw reads fastq files in conf.properties.");
			return;
		}
		String coveragePath = path + "/result.coverage";
		if (!new File(coveragePath).exists()) {
			log.info("Coverage file " + coveragePath
					+ " not found. This file is necessary to execute MAGCheck step, skiping.");
			return;
		}
		genocom.setMagCheckExecuted(true);

		
		Map<String, GRIRawReadsCoverage> coverageMap = readGRICoverageFile(coveragePath);

		for (GenomicRegionInterest gri : gris) {
			for (GenomeSegment segment : gri.getGenomeSegments()) {
				GRIRawReadsCoverage coverage = coverageMap.get(segment.getName());
				segment.setReadsCoverage(coverage);
			}

		}
	}

	private Map<String, GRIRawReadsCoverage> readGRICoverageFile(String path) throws IOException {
		Map<String, GRIRawReadsCoverage> result = new HashMap<>();

		File file = new File(path);

		BufferedReader reader = new BufferedReader(new FileReader(file));

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').parse(reader);

		for (CSVRecord record : records) {
			GRIRawReadsCoverage coverage = new GRIRawReadsCoverage();
			coverage.setGriName(record.get(0));
			coverage.setCoveredPositions(Integer.valueOf(record.get(4)));
			coverage.setCoverage(Double.valueOf(record.get(5)));
			coverage.setMeanDepth(Double.valueOf(record.get(6)));

			result.put(coverage.getGriName(), coverage);
		}

		return result;
	}

}