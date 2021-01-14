package edu.fbs.magset.export.html;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.export.util.ResourceFinder;

public class HtmlGRIGeneListExportService {

	public void export(GenomesComparator genocom, String outputFolder, String header, String footer)
			throws IOException, FileNotFoundException {
		footer = footer.replaceAll("<scriptItems>", getScriptItems(genocom));

		String content = header + IOUtils.toString(ResourceFinder.getResourceAsStream("gri-gene-list.mhtml"), "UTF-8")
				+ footer;

		IOUtils.write(content, new FileOutputStream(outputFolder + "/gri-gene-list.html"), "UTF-8");
	}

	private String getScriptItems(GenomesComparator genocom) {
		StringBuilder result = new StringBuilder();
		result.append("<script src=\"js/03_gri_gene_data.js\"></script>");
		result.append("<script src=\"js/03_gri_gene_load.js\"></script>");
		return result.toString();
	}
}
