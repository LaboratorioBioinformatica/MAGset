package edu.fbs.magset;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.fbs.magset.export.GenomeComparatorExportService;
import edu.fbs.magset.model.ani.AniResultService;
import edu.fbs.magset.model.ani.AniResults;
import edu.fbs.magset.model.cazy.CazyAnnotationService;
import edu.fbs.magset.model.cog.COGsAnnotationService;
import edu.fbs.magset.model.genome.GenomeFile;
import edu.fbs.magset.model.genome_matrix.GenomeMatrix;
import edu.fbs.magset.model.genome_matrix.GenomeMatrixService;
import edu.fbs.magset.model.genomic_region_interest.GenomicRegionsInterest;
import edu.fbs.magset.model.genomic_region_interest.GenomicRegionsInterestService;
import edu.fbs.magset.model.pangenome.Pangenome;
import edu.fbs.magset.model.pangenome.PangenomeService;
import lombok.extern.java.Log;

@Log
@SpringBootApplication
public class App implements CommandLineRunner {

	@Autowired
	private AniResultService aniResultService;
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
	private GenomeComparatorExportService exportService;

	@Override
	public void run(String... args) throws Exception {
		if (args.length != 2) {
			System.err.println("Please send 2 parameters: [conf.properties] [export: ONLY_GRIS or ALL]. Example:\n"
					+ "java -jar magset-export.jar conf.properties ALL");
			System.exit(1);
		}

		Configurations configurations = new Configurations(new File(args[0]), args[1]);
		log.info("Starting magset-export: " + configurations.getTitle() + " type: " + configurations.getExportType());

		MagsetResults results = new MagsetResults(configurations);
		results.loadAllGenomeFiles();

		GenomicRegionsInterest genomicRegionsInterest = griService.getGRIs(results);
		results.setGenomicRegionsOfInterest(genomicRegionsInterest);

		if (configurations.getExportType().equals(ExportEnum.ALL)) {
			AniResults aniResults = aniResultService.getAniResults(results);
			results.setAniResults(aniResults);

			if (configurations.getInputType().equals(InputTypeEnum.GBK)) {
				Pangenome pangenome = pangenomeService.getPangenome(results);
				results.setPangenome(pangenome);

				results.setCogsAnnotation(cogsService.loadCOGsAnnotation(results));

				if (configurations.isExecuteCazyAnnotations()) {
					results.setCazyAnnotation(cazyService.loadCazyAnnotations(results));
				}

				Map<GenomeFile, GenomeMatrix> genomesMatrix = genomeMatrixService.loadGenomesMatrix(results);
				results.setGenomesMatrix(genomesMatrix);
			}
		}

		log.info("Exporting results... ");
		exportService.exportFiles(results);
		log.info("export finished! ");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}
}