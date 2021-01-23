## Supported input files - Genome files
MAGset supports two file types as input: FASTA files and Genbank files (.gbff/.gbk). <br/>
All genome files must be of the same type per execution, and the chose type must be defined at conf.properties, property "input_type".

## Supported input files - Raw reads (MAGcheck)
If you want to execute the MAGcheck module, MAGset supports .fastq files as input using paired end and/or unpaired sequences files. These files can be compressed (tar.gz). If possible, use "cleaned" reads (after running quality filters) to get better results.<br/>
For more information about the parameters, please see the page [Parameters](Parameters.md)
 
