package edu.fbs.magset.export.html;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.util.ResourceFinder;
import edu.fbs.magset.export.util.UnzipFile;
import edu.fbs.magset.model.genome.GenomeFile;
import edu.fbs.magset.util.InputTypeEnum;

public class HtmlExportService {

	public void exportAll(MagsetResults genocom) throws IOException {
		copyDefaultFiles(genocom.getConfigurations().getHTMLResultFolder());

		String header = IOUtils.toString(ResourceFinder.getResourceAsStream("header.mhtml"), "UTF-8");
		header = header.replaceAll("<cogItems>", getCogItems(genocom));
		header = header.replaceAll("<cazyItems>", getCazyItems(genocom));
		header = header.replaceAll("<matrixItems>", getMatrixItems(genocom));

		String footer = IOUtils.toString(ResourceFinder.getResourceAsStream("footer.mhtml"), "UTF-8");

		String outputFolder = genocom.getConfigurations().getHTMLResultFolder();
		new HtmlIndexExportService().export(genocom, outputFolder, header, footer);
		new HtmlGRIListExportService().export(genocom, outputFolder, header, footer);

		if (genocom.getConfigurations().getInputType().equals(InputTypeEnum.GBK)) {
			new HtmlPangenomeListExportService().export(genocom, outputFolder, header, footer);
			new HtmlGRIGeneListExportService().export(genocom, outputFolder, header, footer);
			new HtmlCogListExportService().export(genocom, outputFolder, header, footer);
			if (genocom.getConfigurations().isExecuteCazyAnnotations()) {
				new HtmlCazyListExportService().export(genocom, outputFolder, header, footer);
			}
			new HtmlMatrixListExportService().export(genocom, outputFolder, header, footer);
			new HtmlAdvancedSearchListExportService().export(genocom, outputFolder, header, footer);
		}
	}

	private void copyDefaultFiles(String outputFolder) throws IOException {
		InputStream zip = ResourceFinder.getResourceAsStream("static.zip");
		UnzipFile.unzip(zip, outputFolder);
	}

	private String getCogItems(MagsetResults genocom) {
		StringBuilder result = new StringBuilder();

		for (GenomeFile genome : genocom.getAllGenomes()) {
			result.append("<li class=\"nav-item annotated-genomes\"><a class=\"nav-link\" href=\"cog-list-"
					+ genome.getName() + ".html\"><i class=\"fas fa-fw fa-table\"></i> <span>" + genome.getName()
					+ "</span></a></li>");
		}
		return result.toString();
	}

	private String getCazyItems(MagsetResults genocom) {
		StringBuilder result = new StringBuilder();

		for (GenomeFile genome : genocom.getAllGenomes()) {
			result.append("<li class=\"nav-item annotated-genomes\"><a class=\"nav-link\" href=\"cazy-list-"
					+ genome.getName() + ".html\"><i class=\"fas fa-fw fa-table\"></i> <span>" + genome.getName()
					+ "</span></a></li>");
		}
		return result.toString();
	}

	private String getMatrixItems(MagsetResults genocom) {
		StringBuilder result = new StringBuilder();

		for (GenomeFile genome : genocom.getAllGenomes()) {
			result.append("<li class=\"nav-item annotated-genomes\"><a class=\"nav-link\" href=\"matrix-list-"
					+ genome.getName() + ".html\"><i class=\"fas fa-fw fa-table\"></i> <span>" + genome.getName()
					+ "</span></a></li>");
		}
		return result.toString();
	}
}
