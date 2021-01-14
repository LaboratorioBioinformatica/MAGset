# Available parameters
All parameters are sent to MAGset by a conf.properties file, as first argument:<br/>
`./run-magset.sh conf.properties`

## Mandatory parameters
### title
Defines the title of the result in HTML pages. Example:<br/>
`title=Analysis of MAG001`
### genome_folder
Defines the input folder, where the genomes files must be available. Example:<br/>
`genome_folder=/opt/magset/caldibacillus-debilis/genomes/`
### output_folder
Defines the output folder. Example:<br/>
`output_folder=/opt/magset/caldibacillus-debilis/output/`
### mag_file
Defines the MAG file. This file must be at the genome_folder. Example:<br/>
`mag_file=ZC4RG01.fasta`
### reference_genome_files
Defines the reference(s) file(s). It is possible to inform more than one file, separated by comma. These files must be at the genome_folder. Example with just one reference:<br/>
`reference_genome_files=REF01.fasta`<br/>
Example with three references:<br/>
`reference_genome_files=REF01.fasta,REF02.fasta,REF03.fasta`<br/>
### input_type
Defines the input type of all genomes files. Supported values: FASTA or GBK (GenBank format). Example:<br/>
`input_type=FASTA`
## Optional parameters
### num_threads
Defines the number of threads. Default value: 8. Example:<br/>
`num_threads=8`
### execute_cazy_annotations
Defines if the cazy annotations will be executed or not. Default value: true. Supported values: true or false. Example:<br/>
`execute_cazy_annotations=true` 
### minimum_gri_size
Defines the minimum size for a genomic region of interest (GRI). Default value: 5000. Example:<br/>
`minimum_gri_size=5000`

### nucmer_minimal_identity
Defines the minimal identity to consider when comparing genomes segments. Default value: 0.95. Supported values: Between 0.8 and 0.99. Example:<br/>
`nucmer_minimal_identity=0.95`
### nucmer_minimal_query_match_size
Defines the minimal match size to consider when comparing genomes segments. Default value: 1000. Example:<br/>
`nucmer_minimal_query_match_size=1000`
### nucmer_minimal_covered_between_similar_gris
Defines the minimal covered between two GRIs to consider then in the same cluster. Default value: 0.8. Supported values: Between 0.5 and 0.99. Example:<br/>
`nucmer_minimal_covered_between_similar_gris=0.8`
 
### raw_reads_folder
This parameter is used by MAGcheck module. Defines the folder where the raw reads files must be available. Example:<br/>
`raw_reads_folder=/opt/raw_reads/`
### raw_reads_files_r1 / raw_reads_files_r2
This parameter is used by MAGcheck module. Defines the paired end raw reads. It is possible to inform more than one file, separated by comma. These files must be at the raw_reads_folder. Example with just one file per parameter:<br/>
`raw_reads_files_r1=rA_1.fastq`<br/>
`raw_reads_files_r2=rA_2.fastq`<br/>
Example with two files per parameter:<br/>
`raw_reads_files_r1=rA_1.fastq,rB_1.fastq`<br/>
`raw_reads_files_r2=rA_2.fastq,rB_2.fastq`<br/>
### raw_reads_files_unpaired
This parameter is used by MAGcheck module. Defines the unpaired end raw reads file. It is possible to inform more than one file, separated by comma. These files must be at the raw_reads_folder. Example with just one file:<br/>
`raw_reads_files_unpaired=reads.fastq`<br/>
Example with two files:<br/>
`raw_reads_files_unpaired=reads.fastq,reads2.fastq`<br/>
### magcheck_minimal_coverage
This parameter is used by MAGcheck module. Defines the minimal coverage to consider the GRI exists in raw data. Default value: 0.8. Supported values: Between 0.5 and 0.99. Example:<br/>
`magcheck_minimal_coverage=0.8`
### magcheck_minimal_mean_depth
This parameter is used by MAGcheck module. Defines the minimal mean depth to consider the GRI exists in raw data. Default value: 1.0. Example: <br/> 
`magcheck_minimal_mean_depth=1.0`
### (Beta) singularity_container_file
If your environment does not support docker, it is possible use a singularity container image. Example: <br/>
`singularity_container_file=/path/container-image.img`