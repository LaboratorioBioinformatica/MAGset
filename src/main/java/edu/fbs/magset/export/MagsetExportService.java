package edu.fbs.magset.export;

import java.io.IOException;

import org.springframework.stereotype.Service;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.csv.CSVExportService;
import edu.fbs.magset.export.fasta.GRIExportService;
import edu.fbs.magset.export.html.HtmlExportService;
import edu.fbs.magset.export.javascript.JavascriptExportService;
import lombok.extern.java.Log;

@Service
@Log
public class MagsetExportService {

	public void exportFiles(MagsetResults magset) throws IOException {
		log.info("Exporting results... ");
		if (magset.shouldExportFastaFiles()) {
			new GRIExportService().export(magset);
		}

		if (magset.shouldExportHtmlCsvFiles()) {
			new CSVExportService().export(magset);
			new HtmlExportService().exportAll(magset);
			new JavascriptExportService().exportAll(magset);
		}
		log.info("All files were exported successfully!");
	}
}