package edu.fbs.magset.export.html;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.export.util.ResourceFinder;

public class HtmlIndexExportService {

	public void export(GenomesComparator genocom, String outputFolder, String header, String footer)
			throws IOException, FileNotFoundException {
		footer = footer.replaceAll("<scriptItems>", getScriptItems(genocom));

		String content = header + IOUtils.toString(ResourceFinder.getResourceAsStream("index.mhtml"), "UTF-8") + footer;

		IOUtils.write(content, new FileOutputStream(outputFolder + "/index.html"), "UTF-8");
	}

	private String getScriptItems(GenomesComparator genocom) {
		StringBuilder result = new StringBuilder();
		result.append("<script src=\"js/01_ani_data.js\"></script>");
		result.append("<script src=\"js/01_ani_load.js\"></script>");
		result.append("<script src=\"js/01_summary_data.js\"></script>");
		result.append("<script src=\"js/01_summary_load.js\"></script>");
		result.append("<script src=\"js/01_charts.js\"></script>");
		result.append("<script src=\"vendor/chart.js/Chart.min.js\"></script>");
		result.append("<script src=\"js/01_charts.js\"></script>");
		return result.toString();
	}
}
