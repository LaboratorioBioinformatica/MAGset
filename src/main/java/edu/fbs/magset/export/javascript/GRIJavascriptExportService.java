package edu.fbs.magset.export.javascript;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.genomic_region_interest.GenomeSegment;
import edu.fbs.magset.genomic_region_interest.GenomicRegionInterest;

public class GRIJavascriptExportService {

	public void exportRGIListToJavascript(GenomesComparator genocom, String javascriptOutputFolder) throws IOException {

		Collection<GenomicRegionInterest> gris = genocom.getGenomicRegionsOfInterest().getGenomicRegionsById().values();

		List<String> lines = new ArrayList<>();
		lines.add("$( document ).ready(function() {");
		for (GenomicRegionInterest gri : gris) {
			for (GenomeSegment genomeSegment : gri.getGenomeSegments()) {
				lines.add("\tgris.push(new GRI('" + genomeSegment.getNameWithoutGenome() //
						+ "', '" + genomeSegment.getGenomeFile().getName() //
						+ "', '" + genomeSegment.getSequenceName() //
						+ "', " + genomeSegment.getSize() //
						+ ", " + genomeSegment.getStart() //
						+ ", " + genomeSegment.getEnd() //
						+ ", " + genomeSegment.getGenes().size() //
						+ ", " + genomeSegment.getReadsCoverage().getCoveredPositions() //
						+ ", " + genomeSegment.getReadsCoverage().getCoverage() //
						+ ", " + genomeSegment.getReadsCoverage().getMeanDepth() //
						+ ", " + genomeSegment.getReadsCoverage().foundInRawData(genocom.getConfigurations()) //
						+ "));");
			}
		}
		lines.add("});");

		Files.write(Paths.get(javascriptOutputFolder + "03_gri_data.js"), lines, StandardCharsets.UTF_8);
	}
}
