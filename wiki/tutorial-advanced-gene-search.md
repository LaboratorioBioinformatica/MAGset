# Tutorial - Using the advanced gene search
<p>In this tutorial you will learn how to use the advanced gene search.</p>

* [Generate the results executing MAGset with GBFF input files](tutorial-gbff-files-as-input.md);
* Open the {output_folder}/result/html/index.html, where {output_folder} is the chosen folder for export the result in the previous step;
* Click on "Advanced gene search" menu item;

<p>This page is divided in three parts:
 
* Filters - Here you can add more filters to results. These filters can:
    * Add (Union) - Acumulate the previous results with the results of the filter you are adding;
    * Remove (Minus) - Remove results from the previous results you got based of your current filter;
    * Intersect - Keep just the results that intersect between the the current filter and the previous results;   
    * Gene filter type:
        * Indicates which field will be used in the filter (locus tag, COG code, RGI, etc). 
    * Type of comparison:
        * Indicates the type of comparison (equals, not equals, empty, etc.).
* Applied filters:
    * List of applied filters.
* Results:
    * All genes that respect all filters applied.

Let's show some examples:
<h4>Example 1 - Select all genes from MAG ZC4RG21.gbff if they are specific genes</h4>

* Select "Add (Union)" as filter type, "genome" as field to be compared, "equals" as type of comparison, fill the field text with "ZC4RG21" and click on "Add filter" button;
    * At this point the result panel will show all genes where the genome is "ZC4RG21"
* Select "Intersect" as filter type, "gene specific" as field to be compared, "equals" as type of comparison, fill the field text with "true" and click on "Add filter" button;
    * At this point the result panel will show all genes where the genome is "ZC4RG21" AND "gene specific" is "true"     
* If you need explore or analyse these results after, it is possible export to a CSV file, this option is available at the result panel bottom.

----------
 
<h4>Example 2 - Select all genes from references where gene has COG annotation and not have CAzy annotation</h4>

* Remove all previous filters applied or reload the page;
* Select "Add (Union)" as filter type, "COG code" as field to be compared, "not empty" as type of comparison and click on "Add filter" button;
* Select "Remove (Minus)" as filter type, "CAZy codes" as field to be compared, "not empty" as type of comparison and click on "Add filter" button;
    * In this case, it is possible use different types of filters to achieve the same result. For example, the last filter can be replaced by:
        * Select "Intersect" as filter type, "CAZy codes" as field to be compared, "empty" as type of comparison and click on "Add filter" button;
* Select "Intersect"  as filter type, "genome" as field to be compared, "not equals" as type of comparison, fill the field text with "ZC4RG21" and click on "Add filter" button;      

----------
 
<h4>Example 3 - Select all genes from references where gene has COG description contains "helicases" or COG code is one of these: "COG3866","COG0438" or "COG0859" </h4>

* Select "Add (Union)" as filter type, "COG description" as field to be compared, "contains" as type of comparison, fill the field text with "helicases" and click on "Add filter" button;
* Select "Add (Union)" as filter type, "COG code" as field to be compared, "in" as type of comparison, fill the field text with "COG3866 COG0438 COG0859" (all the options separated by space) and click on "Add filter" button;

