package edu.fbs.magset.export.html;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.util.ResourceFinder;

public class HtmlGRIListExportService {

	public void export(MagsetResults genocom, String outputFolder, String header, String footer)
			throws IOException, FileNotFoundException {
		footer = footer.replaceAll("<scriptItems>", getScriptItems(genocom));

		String content = header + IOUtils.toString(ResourceFinder.getResourceAsStream("gri-list.mhtml"), "UTF-8")
				+ footer;

		IOUtils.write(content, new FileOutputStream(outputFolder + "/gri-list.html"), "UTF-8");
	}

	private String getScriptItems(MagsetResults genocom) {
		StringBuilder result = new StringBuilder();
		result.append("<script src=\"js/03_gri_data.js\"></script>");
		result.append("<script src=\"js/03_gri_load.js\"></script>");
		return result.toString();
	}
}
