package edu.fbs.magset;

import java.io.File;

import edu.fbs.magset.export.GenomeComparatorExportService;
import lombok.extern.java.Log;

@Log
public class App {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Please send 2 parameters: [conf.properties] [export: ONLY_GRIS or ALL]. Example:\n"
					+ "java -jar magset-export.jar conf.properties ALL");
			System.exit(1);
		}
		Configurations configurations = new Configurations(new File(args[0]), args[1]);

		String title = configurations.getTitle();
		log.info("Starting magset-export: " + title + " type: " + configurations.getExportType());

		GenomesComparator genocom = new GenomeComparatorService().createGenomeComparator(configurations);

		log.info("Exporting results: " + title);
		GenomeComparatorExportService genomeComparatorExportService = new GenomeComparatorExportService();
		genomeComparatorExportService.exportFiles(genocom);
		log.info("Finish: " + title);
	}
}