# Tutorial - Comparing MAG versus references with GBFF files as input
<p>In this tutorial you will learn how to use MAGset with gbff input files. Using gbff input files, MAGset can execute additional steps:</p>

* Generate pangenome;
* COGs and CAZy annotations; 
* Allow advanced search of genes by gene data;

<p>Please follow the steps below:</p>

* [Install MAGset](How-to-install.md)
* Download the  genome files. All genomes are available in one file here: [Genome files](genome-files-thermobifida-fusca.tar.gz). If you prefer, you can download all genomes from [NCBI](https://www.ncbi.nlm.nih.gov/assembly/organism/2021/latest/). We are using the following genomes (species _Thermobifida fusca_):
  * [GCA_003242745.1](https://ftp.ncbi.nlm.nih.gov/genomes/all/GCA/003/242/745/GCA_003242745.1_ASM324274v1/GCA_003242745.1_ASM324274v1_genomic.gbff.gz) (descompact and rename the file to ZC4RG21.gbff, this is the strain name)
  * [GCA_015034585.1](https://ftp.ncbi.nlm.nih.gov/genomes/all/GCA/015/034/585/GCA_015034585.1_ASM1503458v1/GCA_015034585.1_ASM1503458v1_genomic.gbff.gz) (descompact and rename the file to UPMC_901.gbff, this is the strain name)
  * [GCA_000012405.1](https://ftp.ncbi.nlm.nih.gov/genomes/all/GCA/000/012/405/GCA_000012405.1_ASM1240v1/GCA_000012405.1_ASM1240v1_genomic.gbff.gz) (descompact and rename the file to XY.gbff, this is the strain name)
  * Save all genome files in the same folder
   
* Download the [conf.properties](conf-thermobifida-fusca.properties.tar.gz). 
* Fix the property "genomes\_folder",  using the absolute path where you saved all the genome files, and fix the property "output\_folder", using absolute path too. If your machine supports less than 8 threads (default value), please change the property "num\_threads" for the correct number of threads.<br/>
Example of conf.properties, considering the genomes folder is at /home/fabiosanchez/magset/test/genomes_folder and the output is at /home/fabiosanchez/magset/test/output:<br/>
`title=Thermobifida fusca`<br/>
`genomes_folder=/home/fabiosanchez/magset/test/genomes_folder/`<br/>
`output_folder=/home/fabiosanchez/magset/test/output/`<br/>
`num_threads=8`<br/>
`mag_file=ZC4RG21.gbff`<br/>
`reference_genome_files=XY.gbff,UPMC_901.gbff` <br/>
`input_type=GBK`

* Run MAGset script<br/>
To run the script, just execute the main script and inform the path of the conf.properties file as first parameter: 
`./run-magset.sh conf.properties`. 

The first run can takes some time, because it is necessary download the docker container. After the script execute, you will see the message "MAGset: done!" at the console. If you receive this message, everything is ok!

Inside the output_folder will be possible see all generated files, including "result" folder, with HTML and CSV results.




