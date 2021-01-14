# Output files - results
MAGset returns two types of final results: HTML pages and CSV files.

## HTML pages
The generated html pages are available in {output_folder}/result/html:<br/>
* Index with ANI results, pangenome summary (\*), genomic regions of interest summary, annotations summary (\*), MAGcheck summary
* Advanced gene search (*)
* Pangenome charts (*)
* Pangenome genes list (*)
* Genomic regions of interest
* Annotations (*)

*These data/pages are available just if the input are Genbank files.

## CSV files
The html result has more information/details, but some results are also in CSV format. The CSV files are available in {output_folder}/result/csv:
* summary.csv, with the columns: <br/>
  * genome_name
  * pangenome_genes_qty (*)
  * pangenome_specific_genes (*)
  * pangenome_shared_genes (*)
  * pangenome_core_genes (*)
  * pangenome_discarted_genes (*)
  * gris
  * genes_in_gris (*)
  * gris_found_by_magcheck
  * genes_in_gris_found_by_magcheck (*)
  * cog_annotations (*)
  * cazy_annotations (*)
* gri_list.csv, with the list of found gris

*These data are available just if the input are Genbank files.

## More result files
All intermediated files are available in {output_folder}. If you need the .fasta sequence files of the GRIs (genomic region of interest), they can be found in {output_folder}/03_gris/clustered/.


