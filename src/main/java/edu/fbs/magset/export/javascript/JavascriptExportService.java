package edu.fbs.magset.export.javascript;

import java.io.File;
import java.io.IOException;

import edu.fbs.magset.MagsetResults;
import edu.fbs.magset.util.InputTypeEnum;

public class JavascriptExportService {

	public void exportAll(MagsetResults genocom) throws IOException {
		String javascriptOutputFolder = genocom.getConfigurations().getHTMLResultFolder() + "js/";

		File jsFolder = new File(javascriptOutputFolder);
		jsFolder.mkdir();
		new BasicDataJavascriptExportService().exportToJavascript(genocom, javascriptOutputFolder);
		new AniJavascriptExportService().exportAniResultToJavascript(genocom, javascriptOutputFolder);
		new GRIJavascriptExportService().exportRGIListToJavascript(genocom, javascriptOutputFolder);
		new SummaryJavascriptExportService().exportGenesSummaryToJavascript(genocom, javascriptOutputFolder);

		if (genocom.getConfigurations().getInputType().equals(InputTypeEnum.GBK)) {
			new PangenomeJavascriptExportService().exportPangenomeGeneListToJavascript(genocom, javascriptOutputFolder);
			new GRIGenesJavascriptExportService().exportRGIListToJavascript(genocom, javascriptOutputFolder);
			new MatrixJavascriptExportService().exportToJavascript(genocom, javascriptOutputFolder);
			new COGsJavascriptExportService().exportToJavascript(genocom, javascriptOutputFolder);
			new CazyJavascriptExportService().exportToJavascript(genocom, javascriptOutputFolder);
		}
	}

}
