package edu.fbs.magset.export.html;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import edu.fbs.magset.GenomesComparator;
import edu.fbs.magset.export.util.ResourceFinder;
import edu.fbs.magset.genome_file.GenomeFile;

public class HtmlCogListExportService {

	public void export(GenomesComparator genocom, String outputFolder, String header, String footer)
			throws IOException, FileNotFoundException {
		for (GenomeFile genome : genocom.getConfigurations().getAllGenomes()) {
			String footerUpdated = footer.replaceAll("<scriptItems>", getScriptItems(genocom, genome));

			String content = header + IOUtils.toString(ResourceFinder.getResourceAsStream("cog-list.mhtml"), "UTF-8")
					+ footerUpdated;
			content = content.replaceAll("<genomeName>", genome.getName());
			IOUtils.write(content, new FileOutputStream(outputFolder + "/cog-list-" + genome.getName() + ".html"),
					"UTF-8");
		}
	}

	private String getScriptItems(GenomesComparator genocom, GenomeFile genome) {
		StringBuilder result = new StringBuilder();
		result.append("<script src=\"js/04_cog_" + genome.getName() + "_data.js\"></script>");
		result.append("<script src=\"js/04_cog_load.js\"></script>");
		return result.toString();
	}
}
