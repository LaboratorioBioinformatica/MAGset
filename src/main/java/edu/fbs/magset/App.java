package edu.fbs.magset;

import java.io.File;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.fbs.magset.export.MagsetExportService;
import edu.fbs.magset.model.ani.AniResultService;
import edu.fbs.magset.model.cazy.CazyAnnotationService;
import edu.fbs.magset.model.cog.COGsAnnotationService;
import edu.fbs.magset.model.genome_matrix.GenomeMatrixService;
import edu.fbs.magset.model.genomic_region_interest.GenomicRegionsInterestService;
import edu.fbs.magset.model.pangenome.PangenomeService;
import lombok.extern.java.Log;

@Log
@SpringBootApplication
public class App implements CommandLineRunner {

	@Autowired
	private AniResultService aniService;
	@Autowired
	private PangenomeService pangenomeService;
	@Autowired
	private GenomicRegionsInterestService griService;
	@Autowired
	private COGsAnnotationService cogsService;
	@Autowired
	private CazyAnnotationService cazyService;
	@Autowired
	private GenomeMatrixService genomeMatrixService;
	@Autowired
	private MagsetExportService exportService;

	@Override
	public void run(String... args) throws Exception {
		log.info("Starting magset-export... " + Arrays.toString(args));
		Configurations configurations = new Configurations(new File(args[0]), args[1]);
		MagsetResults results = new MagsetResults(configurations);

		results.setGenomicRegionsOfInterest(griService.getGRIs(results));
		results.setAniResults(aniService.getAniResults(results));
		results.setPangenome(pangenomeService.getPangenome(results));
		results.setCogAnnotations(cogsService.getCOGAnnotations(results));
		results.setCazyAnnotations(cazyService.getCazyAnnotations(results));
		results.setGenomeMatrices(genomeMatrixService.getGenomeMatrices(results));

		exportService.exportFiles(results);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println(
					"Please send 2 parameters: [conf.properties] [export: EXPORT_GRIS, EXPORT_CLUSTERED_GRIS or EXPORT_CSV_HTML]. Example:\n"
							+ "java -jar magset-export.jar conf.properties EXPORT_CSV_HTML");
			System.exit(1);
		}
		SpringApplication.run(App.class, args);
	}
}