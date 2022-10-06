package edu.fbs.magset.export.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.export.util.ResourceFinder;

public class HtmlPangenomeChartsExportService {

	public void export(MagsetResults genocom, String pangenomeResultsFolder, String outputFolder, String header,
			String footer) throws IOException, FileNotFoundException {
		footer = footer.replaceAll("<scriptItems>", getScriptItems(genocom));

		String content = header
				+ IOUtils.toString(ResourceFinder.getResourceAsStream("pangenome-charts.mhtml"), "UTF-8") + footer;

		IOUtils.write(content, new FileOutputStream(outputFolder + "/pangenome-charts.html"), "UTF-8");

//		FileUtils.copyFile(new File(pangenomeResultsFolder + "pangenome_frequency.png"),
//				new File(outputFolder + "img/" + "pangenome_frequency.png"));
//		FileUtils.copyFile(new File(pangenomeResultsFolder + "pangenome_matrix.png"),
//				new File(outputFolder + "img/" + "pangenome_matrix.png"));
//		FileUtils.copyFile(new File(pangenomeResultsFolder + "pangenome_pie.png"),
//				new File(outputFolder + "img/" + "pangenome_pie.png"));
	}

	private String getScriptItems(MagsetResults genocom) {
		StringBuilder result = new StringBuilder();
		return result.toString();
	}
}
