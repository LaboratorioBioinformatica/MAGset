package edu.fbs.magset.export.html;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.util.ResourceFinder;
import edu.fbs.magset.export.util.UnzipFile;
import edu.fbs.magset.model.genome.Genome;
import edu.fbs.magset.util.InputTypeEnum;

public class HtmlExportService {

	public void exportAll(MagsetResults magset) throws IOException {
		copyDefaultFiles(magset.getConfigurations().getHTMLResultFolder());

		String header = IOUtils.toString(ResourceFinder.getResourceAsStream("header.mhtml"), "UTF-8");
		header = header.replaceAll("<cogItems>", getCogItems(magset));
		header = header.replaceAll("<cazyItems>", getCazyItems(magset));
		header = header.replaceAll("<matrixItems>", getMatrixItems(magset));

		String footer = IOUtils.toString(ResourceFinder.getResourceAsStream("footer.mhtml"), "UTF-8");

		String outputFolder = magset.getConfigurations().getHTMLResultFolder();
		new HtmlIndexExportService().export(magset, outputFolder, header, footer);
		new HtmlGRIListExportService().export(magset, outputFolder, header, footer);

		if (magset.getConfigurations().getInputType().equals(InputTypeEnum.GBK)) {
			new HtmlPangenomeListExportService().export(magset, outputFolder, header, footer);
			new HtmlGRIGeneListExportService().export(magset, outputFolder, header, footer);
			new HtmlCogListExportService().export(magset, outputFolder, header, footer);
			if (magset.getConfigurations().isExecuteCazyAnnotations()) {
				new HtmlCazyListExportService().export(magset, outputFolder, header, footer);
			}
			new HtmlMatrixListExportService().export(magset, outputFolder, header, footer);
			new HtmlAdvancedSearchListExportService().export(magset, outputFolder, header, footer);
		}
	}

	private void copyDefaultFiles(String outputFolder) throws IOException {
		InputStream zip = ResourceFinder.getResourceAsStream("static.zip");
		UnzipFile.unzip(zip, outputFolder);
	}

	private String getCogItems(MagsetResults genocom) {
		StringBuilder result = new StringBuilder();

		for (Genome genome : genocom.getAllGenomes()) {
			result.append("<li class=\"nav-item annotated-genomes\"><a class=\"nav-link\" href=\"cog-list-"
					+ genome.getName() + ".html\"><i class=\"fas fa-fw fa-table\"></i> <span>" + genome.getName()
					+ "</span></a></li>");
		}
		return result.toString();
	}

	private String getCazyItems(MagsetResults genocom) {
		StringBuilder result = new StringBuilder();

		for (Genome genome : genocom.getAllGenomes()) {
			result.append("<li class=\"nav-item annotated-genomes\"><a class=\"nav-link\" href=\"cazy-list-"
					+ genome.getName() + ".html\"><i class=\"fas fa-fw fa-table\"></i> <span>" + genome.getName()
					+ "</span></a></li>");
		}
		return result.toString();
	}

	private String getMatrixItems(MagsetResults genocom) {
		StringBuilder result = new StringBuilder();

		for (Genome genome : genocom.getAllGenomes()) {
			result.append("<li class=\"nav-item annotated-genomes\"><a class=\"nav-link\" href=\"matrix-list-"
					+ genome.getName() + ".html\"><i class=\"fas fa-fw fa-table\"></i> <span>" + genome.getName()
					+ "</span></a></li>");
		}
		return result.toString();
	}
}
