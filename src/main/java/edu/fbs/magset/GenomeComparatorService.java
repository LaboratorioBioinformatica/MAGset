package edu.fbs.magset;

import java.util.Map;

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

public class GenomeComparatorService {

	private AniResultService aniResultService = new AniResultService();
	private PangenomeService pangenomeService = new PangenomeService();
	private GenomicRegionsInterestService griService = new GenomicRegionsInterestService();
	private COGsAnnotationService cogsService = new COGsAnnotationService();
	private CazyAnnotationService cazyService = new CazyAnnotationService();
	private GenomeMatrixService genomeMatrixService = new GenomeMatrixService();

	public MagsetResults createGenomeComparator(Configurations configurations) throws Exception {
		MagsetResults genocom = new MagsetResults(configurations);

		GenomicRegionsInterest genomicRegionsInterest = griService.getGRIs(genocom);
		genocom.setGenomicRegionsOfInterest(genomicRegionsInterest);

		if (configurations.getExportType().equals(ExportEnum.ALL)) {
			AniResults aniResults = aniResultService.getAniResults(genocom);
			genocom.setAniResults(aniResults);

			if (configurations.getInputType().equals(InputTypeEnum.GBK)) {
				Pangenome pangenome = pangenomeService.getPangenome(genocom);
				genocom.setPangenome(pangenome);
	
				genocom.setCogsAnnotation(cogsService.loadCOGsAnnotation(genocom));
	
				if(configurations.isExecuteCazyAnnotations()) {
					genocom.setCazyAnnotation(cazyService.loadCazyAnnotations(genocom));
				}
	
				Map<GenomeFile, GenomeMatrix> genomesMatrix = genomeMatrixService.loadGenomesMatrix(genocom);
				genocom.setGenomesMatrix(genomesMatrix);
			}
		}

		return genocom;
	}
}