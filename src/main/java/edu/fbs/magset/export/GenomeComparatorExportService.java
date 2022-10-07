package edu.fbs.magset.export;

import java.io.IOException;

import org.springframework.stereotype.Service;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.csv.CSVExportService;
import edu.fbs.magset.export.fasta.FastaExportService;
import edu.fbs.magset.export.html.HtmlExportService;
import edu.fbs.magset.export.javascript.JavascriptExportService;

@Service
public class GenomeComparatorExportService {

	public void exportFiles(MagsetResults genocom) throws IOException {
		new FastaExportService().export(genocom);
		// TODO
		// if (genocom.getConfigurations().getExportType().equals(ExportEnum.ALL)) {
		new CSVExportService().export(genocom);
		new HtmlExportService().exportAll(genocom);
		new JavascriptExportService().exportAll(genocom);
		// }
	}
}