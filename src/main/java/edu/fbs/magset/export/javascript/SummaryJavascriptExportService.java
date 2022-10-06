package edu.fbs.magset.export.javascript;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.SummaryService;

public class SummaryJavascriptExportService {

	public void exportGenesSummaryToJavascript(MagsetResults genocom, String javascriptOutputFolder)
			throws IOException {
		List<String> lines = new ArrayList<>();
		Collection<SummaryResult> result = new SummaryService().getSummaryResults(genocom);

		lines.add("$( document ).ready(function() {\r\n");

		for (SummaryResult summary : result) {

			lines.add("summaryResults.push(new SummaryResults('" //
					+ summary.getGenomeName() + "', "//
					+ summary.getGenomeSize() + "," //
					+ summary.getGenesQty() + "," //
					+ summary.getSpecificGenesQty() + "," //
					+ summary.getSharedGenesQty() + "," //
					+ summary.getCoreGenesQty() + "," //
					+ summary.getDiscartedGenes() + "," //
					+ summary.getGrisQty() + "," //
					+ summary.getSumOfGrisSize() + "," //
					+ summary.getGenesInGrisQty() + "," //
					+ summary.getGrisFoundByMAGCheckQty() + "," //
					+ summary.getSumOfGrisFoundByMAGCheckSize() + "," //
					+ summary.getGenesInGRIsFoundByMAGCheckQty() + "," //
					+ summary.getCogQty() + "," //
					+ summary.getCazyQty() //
					+ "));");

		}
		lines.add("});");
		Files.write(Paths.get(javascriptOutputFolder + "01_summary_data.js"), lines, StandardCharsets.UTF_8);
	}

}
