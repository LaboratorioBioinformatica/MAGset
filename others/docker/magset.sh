#!/bin/bash 
set -e

function prop {
    grep "^${1}" ${properties_file}|cut -d'=' -f2
}

if [ "$1" == "-h" ] || [ $# -eq 0 ]; then
  echo "Usage: `basename $0` conf.properties. For more information, please access https://github.com/LaboratorioBioinformatica/magset"
  exit 0
fi

properties_file=$1

if [ ! -f "$properties_file" ]; then
	echo "properties properties_file $properties_file not found."
	exit 1
fi


title=$(prop 'title');
input_type=$(prop 'input_type');
num_threads=$(prop 'num_threads');
execute_cazy_annotations=$(prop 'execute_cazy_annotations');

genomes_folder=$(prop 'genomes_folder');
output_folder=$(prop 'output_folder');
mag_file=$(prop 'mag_file');
IFS=', ' read -r -a reference_genome_files <<< "$(prop 'reference_genome_files')"

raw_reads_folder=$(prop 'raw_reads_folder');
IFS=', ' read -r -a raw_data_files_r1 <<< "$(prop 'raw_reads_files_r1')"
IFS=', ' read -r -a raw_data_files_r2 <<< "$(prop 'raw_reads_files_r2')"
IFS=', ' read -r -a raw_data_files_unpaired <<< "$(prop 'raw_reads_files_unpaired')"
IFS=', ' read -r -a raw_data_files_interleaved <<< "$(prop 'raw_reads_files_interleaved')"

if [ "$title" == "" ]; then
	echo "property title not found in $properties_file"
	exit 1
fi
echo "title=: $title" 

if [ "$input_type" == "" ]; then
	echo "property input_type not found in $properties_file"
	exit 1
fi
echo "input_type=: $input_type" 

if [ ! -d "$genomes_folder" ]; then
	echo "genomes folder $genomes_folder not found."
	exit 1
fi

if [ ! -d "${output_folder}" ]; then
	mkdir ${output_folder}
fi

if [ ! -f "${genomes_folder}/${mag_file}" ]; then
	echo "mag_file ${mag_file} not found in genomes_folder."
	exit 1
fi
echo "mag_file: $mag_file" 

for genome in "${reference_genome_files[@]}"; do
	if [ ! -f "${genomes_folder}/${genome}" ]; then
		echo "reference genome file $genome not found in genomes_folder."
		exit 1
	fi
	if [ "${genome}" == "${mag_file}" ]; then
		echo "reference and mag can't be the same file."
		exit 1		
	fi
    echo "reference: $genome "
done

if [ "$raw_reads_folder" != "" ]; then
	if [ "${#raw_data_files_r1[@]}" != "${#raw_data_files_r2[@]}" ]; then
		echo "raw_data_files_r1 and raw_data_files_r2 should have the same number of files."
		exit 1
	fi

	for raw_data_r1 in "${raw_data_files_r1[@]}"
	do
		if [ ! -f "$raw_reads_folder/$raw_data_r1" ]; then
			echo "raw_data_r1 $raw_data_r1 not found."
			exit 1
		fi
	done

	for raw_data_r2 in "${raw_data_files_r2[@]}"; do
		if [ ! -f "$raw_reads_folder/$raw_data_r2" ]; then
			echo "raw_data_r2 $raw_data_r2 not found."
			exit 1
		fi	
	done 

	for raw_data_unpaired in "${raw_data_files_unpaired[@]}"; do
		if [ ! -f "$raw_reads_folder/$raw_data_unpaired" ]; then
			echo "raw_data_unpaired $raw_data_unpaired not found."
			exit 1
		fi	
	done
	
	for raw_data_interleaved in "${raw_data_files_interleaved[@]}"; do
		if [ ! -f "$raw_reads_folder/$raw_data_interleaved" ]; then
			echo "raw_data_interleaved $raw_data_interleaved not found."
			exit 1
		fi	
	done
fi

if [ "$num_threads" == "" ]; then
	num_threads="8"
fi

if [ "$execute_cazy_annotations" == "" ]; then
	execute_cazy_annotations="true"
fi
echo "threads: ${num_threads}" 

IFS=', ' read -r -a all_genome_files <<< "$(prop 'reference_genome_files')"
all_genome_files+=($(prop 'mag_file'))

cd ${output_folder}

date_string=`date "+%Y%m%d%H%M%S"`

if [ -d "00_converted_genomes" ]; then
	mv -f 00_converted_genomes 00_converted_genomes.$date_string
fi
mkdir 00_converted_genomes 

cd 00_converted_genomes
for genome in "${all_genome_files[@]}"
do
	cp $genomes_folder/${genome} .
done

genomes_folder="${output_folder}/00_converted_genomes"

if [ "$input_type" == "GBK" ]; then
	echo "fixing genome accession/version header, if necessary..."
	for genome in "${all_genome_files[@]}"; do
		if grep -c "ACCESSION\s*$" ${genome}; then
			cat ${genome} | perl -p -e  'BEGIN{undef $/;} s/LOCUS       (.*?) (.*?)ACCESSION  (.*?)KEYWORDS/LOCUS       \1 \2ACCESSION   \1\nVERSION     1\nKEYWORDS/smg' > ${genome}.fixed
			rm ${genome}
			mv ${genome}.fixed ${genome}
			echo "header fixed for: ${genome}"
		fi
	done

	for genome in "${all_genome_files[@]}"; do
		if [ "${genome: -3}" != ".gb" ]
		then
			mv ${genome} $(basename ${genome%.*}).gb
		fi
	done
	
	echo "converting genomes to .fasta, .faa and .gff" 
	# generate .gff
	cd $genomes_folder
	for file in "${all_genome_files[@]}"; do bp_genbank2gff3.pl --outdir ${output_folder}/00_converted_genomes $(basename ${file%.*}).gb || exit 1; done 

	## generate .faa
	cd $genomes_folder

	for file in "${all_genome_files[@]}"; do python /programs/gb_to_faa.py $(basename ${file%.*}).gb $(basename ${file%.*}).faa || exit 1; done

	# generate .fasta
	cd ${output_folder}/00_converted_genomes
	for file in "${all_genome_files[@]}"; do seqret $(basename ${file%.*}).gb -out $(basename ${file%.*}).fasta || exit 1; done 
elif [ "$input_type" == "FASTA" ]; then
	echo "renaming files to .fasta..."
	for genome in "${all_genome_files[@]}"; do
		if [ "${genome: -5}" != "fasta" ]
		then
			mv ${genome} $(basename ${genome%.*}).fasta
		fi
	done
fi


#generate ANI results
echo "starting step 01 - ANI" 
cd ${output_folder}

if [ -d "01_ani" ]; then
	mv -f 01_ani 01_ani.$date_string
fi

mkdir 01_ani
cd 01_ani
#Fix for makeblastdb memory issue in blast+ 2.10
export BLASTDB_LMDB_MAP_SIZE=1000000 
/programs/enveomics/Examples/ani-matrix.bash result.output.txt $(ls -d ${output_folder}/00_converted_genomes/*.fasta) || exit 1

if [ "$input_type" == "GBK" ]; then
#generate pangenome
	echo "starting step 02 - Pangenome" 
	cd ${output_folder}

	if [ -d "02_pangenome" ]; then
		mv -f 02_pangenome 02_pangenome.$date_string
	fi
	mkdir 02_pangenome
	cd 02_pangenome

	roary -f results_roary -e -n -v -p ${num_threads} ${output_folder}/00_converted_genomes/*.gff || exit 1
	cd results_roary
	fasttree -boot 100 -nt -gtr core_gene_alignment.aln > core_gene_alignment.nwk || exit 1
	python /programs/Roary/contrib/roary_plots/roary_plots.py --labels core_gene_alignment.nwk gene_presence_absence.csv || exit 1
fi

#generate alignments to find genomic regions of interest 
echo "starting step 03 - GRIs" 
cd ${output_folder}
if [ -d "03_gris" ]; then
	mv -f 03_gris 03_gris.$date_string
fi
mkdir 03_gris
cd 03_gris

mkdir nucmer_by_genome
cd nucmer_by_genome

for genome in "${reference_genome_files[@]}"; do
	mkdir "$(basename ${genome%.*})"
	cd "$(basename ${genome%.*})"
	
	nucmer -maxmatch "${genomes_folder}/$(basename ${genome%.*}).fasta"  "${genomes_folder}/$(basename ${mag_file%.*}).fasta" || exit 1
	show-coords -THrcl out.delta > out.coords || exit 1
	
	cat "${genomes_folder}/$(basename ${genome%.*}).fasta" >> ../all_references.fasta
	
	cd ..
done

mkdir "$(basename ${mag_file%.*})"
cd "$(basename ${mag_file%.*})"

nucmer -maxmatch "${genomes_folder}/$(basename ${mag_file%.*}).fasta"  ../all_references.fasta || exit 1
show-coords -THrcl out.delta > out.coords || exit 1


if [ "$input_type" == "GBK" ]; then
	echo "starting step 04 - COGs" 
	cd ${output_folder}
	if [ -d "04_cogs" ]; then
		mv -f 04_cogs 04_cogs.$date_string
	fi
	mkdir 04_cogs
	cd 04_cogs

	for file in ${output_folder}/00_converted_genomes/*.faa; do rpsblast -query ${file} -db /databases/cog_files/Cog -out ${output_folder}/04_cogs/$(basename ${file%.*}).rps_blast -evalue 1e-20 -outfmt 7 -num_threads ${num_threads} || exit 1; done

	for file in *.rps_blast; do perl /programs/bac-genomics-scripts/cdd2cog/cdd2cog.pl -r ${file} -c /databases/cog_files/cddid.tbl -f /databases/cog_files/fun.txt -w /databases/cog_files/whog || exit 1; mv results/ results_${file%.*}; done

	echo "starting step 05 - DBCAN" 
	cd ..

	if [ "$execute_cazy_annotations" == "true" ]; then	
		if [ -d "05_cazy" ]; then
			mv -f 05_cazy 05_cazy.$date_string
		fi
		mkdir 05_cazy
		cd 05_cazy
		for file in ${output_folder}/00_converted_genomes/*.faa; do run_dbcan.py  ${output_folder}/00_converted_genomes/$(basename ${file}) protein --out_dir $(basename ${file%.*}) --db_dir /databases/dbcan/ || exit 1; done
	fi
fi

cd ${output_folder}

if [ "$input_type" == "GBK" ]; then
	if [ -d "06_matrix" ]; then
		mv -f 06_matrix 06_matrix.$date_string
	fi
	mkdir 06_matrix
fi

cd ${output_folder}
if [ -d "07_magcheck" ]; then
	mv -f 07_magcheck 07_magcheck.$date_string
fi

java -jar /programs/magset-export.jar ${properties_file} "FASTA_CSV" || exit 1

if [ -d "${output_folder}/03_gris/non_clustered/" ]; then
	cd ${output_folder}/03_gris/non_clustered/
	cat *GRI*.fasta > all_gris.fasta
	
	mkdir ${output_folder}/03_gris/non_clustered/nucmer/
	cd ${output_folder}/03_gris/non_clustered/nucmer/
	
	file1="${output_folder}/03_gris/non_clustered/all_gris.fasta"
	if [ -s $file1 ]; then
		nucmer -maxmatch $file1  $file1 || exit 1
		show-coords -THrcl out.delta > out.coords || exit 1
	fi
	
	java -jar /programs/magset-export.jar ${properties_file} "FASTA_CSV" || exit 1
else
	echo "No GRIs found... ignoring clustering step"
fi
if [ "${#raw_data_files_r1[@]}" -gt 0 ] ||  [ "${#raw_data_files_unpaired[@]}" -gt 0 ] ||   [ "${#raw_data_files_interleaved[@]}" -gt 0 ]; then
	echo "starting magchek..." 
	cd ${output_folder}
	

	mkdir 07_magcheck
	cd 07_magcheck

	if [ -d "${output_folder}/03_gris/clustered/" ] && [ "$(ls -A ${output_folder}/03_gris/clustered/)" ]; then
		cat ${output_folder}/03_gris/clustered/*GRI*.fasta > all-gris.fasta
		
		echo "running bowtie2 --very-sensitive ..." 
		bowtie2-build all-gris.fasta all-gris || exit 1
	
		all_raw_reads_unpaired=""
		if  [ "${#raw_data_files_unpaired[@]}" -gt 0 ]; then
			for raw_data_unpaired in "${raw_data_files_unpaired[@]}"
			do
				all_raw_reads_unpaired="$raw_reads_folder/$raw_data_unpaired,$all_raw_reads_unpaired"
			done
			all_raw_reads_unpaired=" -U $all_raw_reads_unpaired"
		fi
		
		all_raw_reads_interleaved=""
		if  [ "${#raw_data_files_interleaved[@]}" -gt 0 ]; then
			for raw_data_interleaved in "${raw_data_files_interleaved[@]}"
			do
				all_raw_reads_interleaved="$raw_reads_folder/$raw_data_interleaved,$all_raw_reads_interleaved"
			done
			all_raw_reads_interleaved=" --interleaved $all_raw_reads_interleaved"
		fi
	
		all_raw_reads_1=""
		all_raw_reads_2=""
		if [ "${#raw_data_files_r1[@]}" -gt 0 ]; then
			for i in "${!raw_data_files_r1[@]}"
			do
				all_raw_reads_1="$raw_reads_folder/${raw_data_files_r1[i]},$all_raw_reads_1"
				all_raw_reads_2="$raw_reads_folder/${raw_data_files_r2[i]},$all_raw_reads_2"
			done
			all_raw_reads_1=" -1 $all_raw_reads_1"
			all_raw_reads_2=" -2 $all_raw_reads_2"
		fi
		bowtie2 -a -x all-gris $all_raw_reads_unpaired $all_raw_reads_1 $all_raw_reads_2 $all_raw_reads_interleaved -S result.sam -p ${num_threads} --no-unal --very-sensitive || exit 1
	
		echo "running samtools..." 
		samtools view -bS result.sam > result.bam || exit 1
		samtools sort result.bam -o result.sorted.bam || exit 1
	
		samtools coverage --ff 0 -H result.sorted.bam > result.coverage || exit 1
	
		mkdir reads
		samtools sort -n result.bam -o result.sorted-n.bam || exit 1
		rm -f result.bam result.sam result.sorted.bam
		samtools fastq -0 reads/reads_unpaired.fastq -1 reads/reads_1.fastq -2 reads/reads_2.fastq -s reads/reads_unpaired_discordant.fastq result.sorted-n.bam || exit 1
		#Beta/Alpha functionality
		if [ "$(prop 'execute_mag_improve')" == "true" ]; then
			echo "starting mag_improve..." 
			
			cd ${output_folder}
			if [ -d "08_mag_improve" ]; then
				mv -f 08_mag_improve 08_mag_improve.$date_string
			fi
			mkdir 08_mag_improve
			cd 08_mag_improve
			echo "running bowtie2..." 
			original_mag_file=$(prop 'mag_file')
			bowtie2-build ${output_folder}/00_converted_genomes/$(basename ${original_mag_file%.*}).fasta original_mag_file || exit 1
			bowtie2 -x original_mag_file $all_raw_reads_unpaired $all_raw_reads_1 $all_raw_reads_2 $all_raw_reads_interleaved -S magfile.sam -p ${num_threads} --no-unal --very-sensitive || exit 1
			
			echo "running samtools..." 
			samtools view -bS magfile.sam > magfile.bam || exit 1
			samtools sort -n magfile.bam -o magfile.sorted-n.bam || exit 1
			samtools merge -n merged.bam magfile.sorted-n.bam ../07_magcheck/result.sorted-n.bam
			rm -f magfile.bam magfile.sam  magfile.sorted-n.bam
			mkdir reads
			samtools fastq -0 reads/reads_unpaired.fastq -1 reads/reads_1.fastq -2 reads/reads_2.fastq -s reads/reads_unpaired_discordant.fastq merged.bam || exit 1
			
			inputs_spades=""
			if [ -s reads/reads_unpaired.fastq ]; then
				inputs_spades="$inputs_spades --s 1 reads/reads_unpaired.fastq "
			fi 
			
			if [ -s reads/reads_1.fastq ]; then
				inputs_spades="$inputs_spades --pe-1 1 reads/reads_1.fastq --pe-2 1 reads/reads_2.fastq "
			fi 
			
			if [ -s reads/reads_unpaired_discordant.fastq ]; then
				inputs_spades="$inputs_spades --pe-s 1 reads/reads_unpaired_discordant.fastq "
			fi 

			echo "running spades... parameters: $inputs_spades" 
			spades.py ${inputs_spades} -o spades -t ${num_threads} $(prop 'mag_improve_spades_extra_parameters') || exit 1
	
			#creating input files to run magset for fixed fasta file
			mkdir input
			cd input 
			cp ${output_folder}/00_converted_genomes/*.fasta .
			rm $(basename ${original_mag_file%.*}).fasta
			cp ../spades/scaffolds.fasta $(basename ${original_mag_file%.*}).improved.fasta
	
			if [ ! -z $(prop 'mag_improve_contig_minimum_size') ]; then
				echo "Filtering spades result with mag_improve_contig_minimum_size" 
				/programs/bbmap/reformat.sh in=$(basename ${original_mag_file%.*}).improved.fasta out=$(basename ${original_mag_file%.*}).improved.filtered.fasta minlength="$(prop 'mag_improve_contig_minimum_size')"  || exit 1
				rm $(basename ${original_mag_file%.*}).improved.fasta
				mv $(basename ${original_mag_file%.*}).improved.filtered.fasta $(basename ${original_mag_file%.*}).improved.fasta
			fi
	
			cd ..
	
			reference_genome_files_fasta=""
			for genome in "${reference_genome_files[@]}"; do
				if [ "${reference_genome_files_fasta}" == "" ]; then
					reference_genome_files_fasta="$(basename ${genome%.*}).fasta"
				else
		    		reference_genome_files_fasta="${reference_genome_files_fasta},$(basename ${genome%.*}).fasta"
	    		fi
			done
	
			echo "title=${title} after reassembly" >> conf.properties
			echo "genomes_folder=${output_folder}/08_mag_improve/input" >> conf.properties
			echo "output_folder=${output_folder}/08_mag_improve/" >> conf.properties
			echo "mag_file=$(basename ${original_mag_file%.*}).improved.fasta" >> conf.properties
			echo "reference_genome_files=${reference_genome_files_fasta}" >> conf.properties
			echo "num_threads=${num_threads}" >> conf.properties
			echo "input_type=FASTA" >> conf.properties
			echo "raw_reads_folder=${raw_reads_folder}" >> conf.properties
	
			if [ "${#raw_data_files_r1[@]}" -gt 0 ] ; then
				echo "raw_reads_files_r1=$(prop 'raw_reads_files_r1')" >> conf.properties
				echo "raw_reads_files_r2=$(prop 'raw_reads_files_r2')" >> conf.properties
			fi
			if [ "${#raw_data_files_unpaired[@]}" -gt 0 ]; then
				echo "raw_reads_files_unpaired=$(prop 'raw_reads_files_unpaired')" >> conf.properties
			fi
			if [ "${#raw_data_files_interleaved[@]}" -gt 0 ]; then
				echo "raw_reads_files_interleaved=$(prop 'raw_reads_files_interleaved')" >> conf.properties
			fi
			
			if [ ! -z "$(prop 'minimum_gri_size')" ]; then
				echo "minimum_gri_size=$(prop 'minimum_gri_size')" >> conf.properties
			fi
			if [ ! -z "$(prop 'magcheck_minimal_positions_covered')" ]; then
				echo "magcheck_minimal_positions_covered=$(prop 'magcheck_minimal_positions_covered')" >> conf.properties
			fi
			if [ ! -z "$(prop 'magcheck_minimal_average_coverage_to_consider')" ]; then
				echo "magcheck_minimal_average_coverage_to_consider=$(prop 'magcheck_minimal_average_coverage_to_consider')" >> conf.properties
			fi
			if [ ! -z "$(prop 'magcheck_minimal_ratio_largest_segment_to_consider')" ]; then
				echo "magcheck_minimal_ratio_largest_segment_to_consider=$(prop 'magcheck_minimal_ratio_largest_segment_to_consider')" >> conf.properties
			fi
			if [ ! -z "$(prop 'magcheck_minimal_absolute_size_largest_segment_to_consider')" ]; then
				echo "magcheck_minimal_absolute_size_largest_segment_to_consider=$(prop 'magcheck_minimal_absolute_size_largest_segment_to_consider')" >> conf.properties
			fi
			if [ ! -z "$(prop 'nucmer_minimal_identity_to_consider')" ]; then
				echo "nucmer_minimal_identity_to_consider=$(prop 'nucmer_minimal_identity_to_consider')" >> conf.properties
			fi
			if [ ! -z "$(prop 'nucmer_minimal_query_match_size_to_consider')" ]; then
				echo "nucmer_minimal_query_match_size_to_consider=$(prop 'nucmer_minimal_query_match_size_to_consider')" >> conf.properties
			fi
			if [ ! -z "$(prop 'nucmer_minimal_covered_between_similar_gris_to_consider')" ]; then
				echo "nucmer_minimal_covered_between_similar_gris_to_consider=$(prop 'nucmer_minimal_covered_between_similar_gris_to_consider')" >> conf.properties
			fi
			
			/programs/magset.sh ${output_folder}/08_mag_improve/conf.properties || exit 1
	
			if [ -d "${output_folder}/result_after_reassembly" ]; then
				mv -f ${output_folder}/result_after_reassembly ${output_folder}/result_after_reassembly.$date_string
			fi
	
			mv result ../result_after_reassembly
		fi
	else
		echo "GRIs not found, MAGcheck ignored."
	fi

fi
echo "starting export data/result..." 

cd ${output_folder}

if [ -d "result" ]; then
	mv -f result result.$date_string
fi
mkdir result

java -jar /programs/magset-export.jar ${properties_file} "ALL" || exit 1
echo "MAGset: done!"