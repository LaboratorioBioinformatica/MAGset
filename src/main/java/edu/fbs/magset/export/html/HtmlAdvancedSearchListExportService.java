package edu.fbs.magset.export.html;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.export.util.ResourceFinder;
import edu.fbs.magset.genome_file.GenomeFile;

public class HtmlAdvancedSearchListExportService {

	public void export(GenomesComparator genocom, String outputFolder, String header, String footer)
			throws IOException, FileNotFoundException {
		footer = footer.replaceAll("<scriptItems>", getScriptItems(genocom));
		String content = header
				+ IOUtils.toString(ResourceFinder.getResourceAsStream("advanced-search-list.mhtml"), "UTF-8") + footer;
		IOUtils.write(content, new FileOutputStream(outputFolder + "/advanced-search-list.html"), "UTF-8");
	}

	private String getScriptItems(GenomesComparator genocom) {
		StringBuilder result = new StringBuilder();
		for (GenomeFile genome : genocom.getConfigurations().getAllGenomes()) {
			result.append("<script src=\"js/06_matrix_" + genome.getName() + "_data.js\"></script>");

		}
		result.append("<script src=\"js/07_advanced_search_load.js\"></script>");
		return result.toString();
	}
}
