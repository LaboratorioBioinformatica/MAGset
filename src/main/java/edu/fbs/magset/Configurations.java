package edu.fbs.magset;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import edu.fbs.magset.model.genome.GenomeFile;
import edu.fbs.magset.model.genomic_region_interest.GenomicRegionsInterest;

public class Configurations {
	private String outputFolder;
	private ExportEnum exportType;
	private InputTypeEnum inputType;
	private String title;
	private Integer minimumGRISize;

	private boolean executeCazyAnnotations;

	private double magCheckMinimalCoverage;
	private double magCheckMinimalMeanDepth;

	private double nucmerMinimalIdentity;
	private int nucmerMinimalQueryMatchSize;
	private double nucmerMinimalCoveredBetweenSimilarGRIsToConsider;

	private int backboneAllowedExtraPositionsFromExtraContigs;

	private String magGenomeFile;
	private Map<Integer, String> referenceGenomeFiles = new TreeMap<>();

	public Configurations(File properties, String exportType) throws Exception {
		this.exportType = ExportEnum.valueOf(exportType);

		Properties prop = new Properties();
		prop.load(new FileInputStream(properties));

		this.outputFolder = (String) prop.getProperty("output_folder");
		this.inputType = InputTypeEnum.valueOf(prop.getProperty("input_type", "GBK"));
		this.title = (String) prop.getProperty("title", "Genome comparison");
		this.minimumGRISize = Integer.valueOf(prop.getProperty("minimum_gri_size", "5000"));

		this.executeCazyAnnotations = Boolean.valueOf(prop.getProperty("execute_cazy_annotations", "true"));

		this.magCheckMinimalCoverage = Double.valueOf(prop.getProperty("magcheck_minimal_coverage", "0.8"));
		this.magCheckMinimalMeanDepth = Double.valueOf(prop.getProperty("magcheck_minimal_mean_depth", "1.0"));

		this.nucmerMinimalIdentity = Double.valueOf(prop.getProperty("nucmer_minimal_identity", "0.90"));
		this.nucmerMinimalQueryMatchSize = Integer.valueOf(prop.getProperty("nucmer_minimal_query_match_size", "1000"));
		this.nucmerMinimalCoveredBetweenSimilarGRIsToConsider = Double
				.valueOf(prop.getProperty("nucmer_minimal_covered_between_similar_gris", "0.8"));

		this.magGenomeFile = prop.getProperty("mag_file");

		String[] referenceFilenames = prop.getProperty("reference_genome_files").split(",");
		for (int i = 0; i < referenceFilenames.length; i++) {
			String filePath = this.getConvertedGenomesFolder() + referenceFilenames[i];
			String fileName = Paths.get(filePath).getFileName().toString();
			this.referenceGenomeFiles.put(i + 1, fileName);
		}
	}

	public int getMinimumGRISize() {
		return minimumGRISize;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public ExportEnum getExportType() {
		return exportType;
	}

	public InputTypeEnum getInputType() {
		return inputType;
	}

	public String getTitle() {
		return title;
	}

	public String getMagGenomeFile() {
		return magGenomeFile;
	}

	public boolean isExecuteCazyAnnotations() {
		return executeCazyAnnotations;
	}

	public Map<Integer, String> getReferenceGenomeFiles() {
		return referenceGenomeFiles;
	}

	public double getMagCheckMinimalMeanDepth() {
		return magCheckMinimalMeanDepth;
	}

	public double getMagCheckMinimalCoverage() {
		return magCheckMinimalCoverage;
	}

	public double getNucmerMinimalIdentity() {
		return nucmerMinimalIdentity;
	}

	public int getNucmerMinimalQueryMatchSize() {
		return nucmerMinimalQueryMatchSize;
	}

	public double getNucmerMinimalCoveredBetweenSimilarGRIsToConsider() {
		return nucmerMinimalCoveredBetweenSimilarGRIsToConsider;
	}

	public int getBackboneAllowedExtraPositionsFromExtraContigs() {
		return backboneAllowedExtraPositionsFromExtraContigs;
	}

	public String getNucmerResultFile(GenomeFile genomeFile) {
		return getNucmerByGenomeFolder() + genomeFile.getName() + "/out.coords";
	}

	public String getGRINucmerResultFile(GenomeFile genomeFile) {
		return getNucmerByGenomeGRIsFolder() + genomeFile.getName() + "/out.coords";
	}

	public String getGRINucmerResultFile() {
		return getNucmerByGenomeGRIsFolder() + "/out.coords";
	}

	public String getGriFolder(GenomicRegionsInterest genomicRegionsOfInterest) {
		if (genomicRegionsOfInterest.isClustered()) {
			return getGriFolderClustered();
		}
		return getGriFolderNonClustered();
	}

	public String getConvertedGenomesFolder() {
		return getOutputFolder() + CONVERTED_GENOMES_FOLDER;
	}

	public String getAniFile() {
		return getOutputFolder() + ANI_FILE;
	}

	public String getPangenomeFolder() {
		return getOutputFolder() + PANGENOME_FOLDER;
	}

	public String getGriFolderNonClustered() {
		return getOutputFolder() + GRI_FOLDER_NON_CLUSTERED;
	}

	public String getGriFolderClustered() {
		return getOutputFolder() + GRI_FOLDER_CLUSTERED;
	}

	public String getNucmerByGenomeFolder() {
		return getOutputFolder() + NUCMER_BY_GENOME_FOLDER;
	}

	public String getNucmerByGenomeGRIsFolder() {
		return getOutputFolder() + NUCMER_BY_GENOME_GRIS_FOLDER;
	}

	public String getMAGCheckFolder() {
		return getOutputFolder() + MAGCHECK_FOLDER;
	}

	public String getCogFolder() {
		return getOutputFolder() + COG_FOLDER;
	}

	public String getCazyFolder() {
		return getOutputFolder() + CAZY_FOLDER;
	}

	public String getMatrixFolder() {
		return getOutputFolder() + MATRIX_FOLDER;
	}

	public String getResultFolder() {
		return getOutputFolder() + RESULT_FOLDER;
	}

	public String getHTMLResultFolder() {
		return getResultFolder() + "/html/";
	}

	public static final String CONVERTED_GENOMES_FOLDER = "/00_converted_genomes/";
	public static final String ANI_FILE = "/01_ani/result.output.txt";
	public static final String PANGENOME_FOLDER = "/02_pangenome/results/";
	public static final String BACKBONE_FILE = "/03_gris/mauve.backbone";
	public static final String GRI_FOLDER_NON_CLUSTERED = "/03_gris/non_clustered/";
	public static final String GRI_FOLDER_CLUSTERED = "/03_gris/clustered/";
	public static final String NUCMER_BY_GENOME_FOLDER = "/03_gris/nucmer_by_genome/";
	public static final String NUCMER_BY_GENOME_GRIS_FOLDER = "/03_gris/non_clustered/nucmer/";
	public static final String MAGCHECK_FOLDER = "/07_magcheck/";
	public static final String COG_FOLDER = "/04_cogs/";
	public static final String CAZY_FOLDER = "/05_cazy/";
	public static final String MATRIX_FOLDER = "/06_matrix/";
	public static final String RESULT_FOLDER = "/result/";

}