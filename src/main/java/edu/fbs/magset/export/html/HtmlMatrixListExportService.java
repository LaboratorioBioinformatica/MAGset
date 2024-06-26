package edu.fbs.magset.export.html;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.util.ResourceFinder;
import edu.fbs.magset.model.genome.Genome;

public class HtmlMatrixListExportService {

	public void export(MagsetResults genocom, String outputFolder, String header, String footer)
			throws IOException, FileNotFoundException {
		for (Genome genome : genocom.getAllGenomes()) {
			String footerUpdated = footer.replaceAll("<scriptItems>", getScriptItems(genocom, genome));

			String content = header + IOUtils.toString(ResourceFinder.getResourceAsStream("matrix-list.mhtml"), "UTF-8")
					+ footerUpdated;
			content = content.replaceAll("<genomeName>", genome.getName());
			IOUtils.write(content, new FileOutputStream(outputFolder + "/matrix-list-" + genome.getName() + ".html"),
					"UTF-8");
		}
	}

	private String getScriptItems(MagsetResults genocom, Genome genome) {
		StringBuilder result = new StringBuilder();
		result.append("<script src=\"js/06_matrix_" + genome.getName() + "_data.js\"></script>");
		result.append("<script src=\"js/06_matrix_load.js\"></script>");
		return result.toString();
	}
}
