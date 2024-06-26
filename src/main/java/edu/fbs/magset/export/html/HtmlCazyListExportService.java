package edu.fbs.magset.export.html;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.util.ResourceFinder;
import edu.fbs.magset.model.genome.Genome;

public class HtmlCazyListExportService {

	public void export(MagsetResults magset, String outputFolder, String header, String footer)
			throws IOException, FileNotFoundException {
		for (Genome genome : magset.getAllGenomes()) {
			String footerUpdated = footer.replaceAll("<scriptItems>", getScriptItems(magset, genome));

			String content = header + IOUtils.toString(ResourceFinder.getResourceAsStream("cazy-list.mhtml"), "UTF-8")
					+ footerUpdated;
			content = content.replaceAll("<genomeName>", genome.getName());
			IOUtils.write(content, new FileOutputStream(outputFolder + "/cazy-list-" + genome.getName() + ".html"),
					"UTF-8");
		}
	}

	private String getScriptItems(MagsetResults genocom, Genome genome) {
		StringBuilder result = new StringBuilder();
		result.append("<script src=\"js/05_cazy_" + genome.getName() + "_data.js\"></script>");
		result.append("<script src=\"js/05_cazy_load.js\"></script>");
		return result.toString();
	}
}
