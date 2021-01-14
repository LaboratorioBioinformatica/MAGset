#!/bin/bash
set -e

version=1.0
 
function prop {
    grep "^${1}" ${file}|cut -d'=' -f2
}

#https://gist.github.com/kongchen/6748525
setProperty(){
  awk -v pat="^$1=" -v value="$1=$2" '{ if ($0 ~ pat) print value; else print $0; }' $3 > $3.tmp
  mv $3.tmp $3
}


if [ "$1" == "-h" ] || [ $# -eq 0 ]; then
  echo "Usage: `basename $0` conf.properties. For more information, please access https://github.com/LaboratorioBioinformatica/magset"
  exit 0
fi

file=$1

if [ ! -f "$file" ]
then
	echo "properties file $file not found."
	exit 1
fi

genomes_folder=$(prop 'genomes_folder');
output_folder=$(prop 'output_folder');
raw_reads_folder=$(prop 'raw_reads_folder');
singularity_container_file=$(prop 'singularity_container_file');

if [ ! -d "$genomes_folder" ]
then
	echo "genomes folder $genomes_folder not found."
	exit 1
fi
echo "genomes_folder: $genomes_folder" 

if [ ! -d "$output_folder" ]
then
	mkdir $output_folder
fi
echo "output_folder: $output_folder" 

date_string=`date "+%Y%m%d%H%M%S"`

if [ -f "$output_folder/conf-container.properties" ]; then
	mv -f $output_folder/conf-container.properties $output_folder/conf-container.properties.$date_string
fi

cp $file $output_folder/conf-container.properties
setProperty "output_folder" "/output/" "$output_folder/conf-container.properties"
setProperty "genomes_folder" "/input/" "$output_folder/conf-container.properties"

container_volume_raw_reads_folder_docker=""
container_volume_raw_reads_folder_singularity=""
if [ ! "$raw_reads_folder" == "" ]
then
	if [ ! -d "$raw_reads_folder" ]
	then
		echo "raw reads folder $raw_reads_folder not found."
		exit 1
	fi
	
	container_volume_raw_reads_folder_docker=" -v $raw_reads_folder:/raw_data/ "
	container_volume_raw_reads_folder_singularity=",$raw_reads_folder:/raw_data/"
	
	setProperty "raw_reads_folder" "/raw_data/" "$output_folder/conf-container.properties"
fi

date_string=`date "+%Y%m%d%H%M%S"`
if [ -d "$output_folder/logs" ]; then
	mv -f $output_folder/logs $output_folder/logs.$date_string
fi
mkdir $output_folder/logs
echo "running version $version" |& tee -a ${output_folder}/logs/magset.log

if [ "$singularity_container_file" == "" ]
then
	if ! [ -x "$(command -v docker)" ]; then
	  	echo 'Error: docker is not installed. Please install docker to run MAGset. Instructions to install docker: https://docs.docker.com/get-docker/. MAGset also supports Singularity container, please look at: https://github.com/LaboratorioBioinformatica/magset for more details.' >&2
	  	exit 1
	else
		docker pull fsanchez/magset:$version
		docker run --rm -t -v $output_folder/conf-container.properties:/opt/conf.properties -v $genomes_folder:/input $container_volume_raw_reads_folder_docker -v $output_folder:/output fsanchez/magset:$version ./magset.sh /opt/conf.properties |& tee -a ${output_folder}/logs/magset.log
	fi
else
	if [ ! -f "$singularity_container_file" ]
	then
		echo "singularity container file $singularity_container_file not found."
		exit 1
	fi
	if ! [ -x "$(command -v singularity)" ]; then
	  	echo 'Error: singularity is not installed. Please install singularity to run MAGset using the parameter singularity_container_file.' >&2
	  	exit 1
	else
		if [ ! -d "$output_folder/tmp" ]
		then
			mkdir $output_folder/tmp
		fi
		singularity exec -B $output_folder/tmp:/tmp$container_volume_raw_reads_folder_singularity,$output_folder:/output,$genomes_folder:/input,$output_folder/conf-container.properties:/opt/conf.properties $singularity_container_file /programs/magset.sh /opt/conf.properties |& tee -a ${output_folder}/logs/magset.log 
	fi
fi