package edu.fbs.magset;

import java.util.Map;

import edu.fbs.magset.ani.AniResultService;
import edu.fbs.magset.ani.AniResults;
import edu.fbs.magset.cazy.CazyAnnotationService;
import edu.fbs.magset.cog.COGsAnnotationService;
import edu.fbs.magset.genome_file.GenomeFile;
import edu.fbs.magset.genome_matrix.GenomeMatrix;
import edu.fbs.magset.genome_matrix.GenomeMatrixService;
import edu.fbs.magset.genomic_region_interest.GenomicRegionsInterest;
import edu.fbs.magset.genomic_region_interest.GenomicRegionsInterestService;
import edu.fbs.magset.pangenome.Pangenome;
import edu.fbs.magset.pangenome.PangenomeService;

public class GenomeComparatorService {

	private AniResultService aniResultService = new AniResultService();
	private PangenomeService pangenomeService = new PangenomeService();
	private GenomicRegionsInterestService griService = new GenomicRegionsInterestService();
	private COGsAnnotationService cogsService = new COGsAnnotationService();
	private CazyAnnotationService cazyService = new CazyAnnotationService();
	private GenomeMatrixService genomeMatrixService = new GenomeMatrixService();

	public GenomesComparator createGenomeComparator(Configurations configurations) throws Exception {
		GenomesComparator genocom = new GenomesComparator(configurations);

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