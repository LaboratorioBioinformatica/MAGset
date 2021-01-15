package edu.fbs.magset.export;

import java.io.IOException;

import edu.fbs.magset.ExportEnum;
import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.export.csv.CSVExportService;
import edu.fbs.magset.export.fasta.FastaExportService;
import edu.fbs.magset.export.html.HtmlExportService;
import edu.fbs.magset.export.javascript.JavascriptExportService;

public class GenomeComparatorExportService {

	public void exportFiles(GenomesComparator genocom) throws IOException {
		new FastaExportService().export(genocom);
		if (genocom.getConfigurations().getExportType().equals(ExportEnum.ALL)) {
			new CSVExportService().export(genocom);
			new HtmlExportService().exportAll(genocom);
			new JavascriptExportService().exportAll(genocom);
		}
	}
}