$( document ).ready(function() {
	var table = $('#dataTable');
	var lines = filteredGenes;
	
	var dataTable = table.DataTable( {
		dom: "<'row'<'col-sm-12 col-md-6'l><'col-sm-12 col-md-6'f>>" +
			"<'row'<'col-sm-12 col-md-12'B>>" +
			"<'row'<'col-sm-12'tr>>" +
			"<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>>",
        buttons: [
			{extend: 'colvis', text: 'Selected columns', className: 'mb-2 btn btn-light  btn-export'}
        ],
		data: lines,
		columns: [
		    { title: "Genome",data: "genomeName" },
		    { title: "Locus tag", data: "locusTag" },
		    { title: "Type", data: "type" },
		    { title: "Product", data: "product" },
		    { title: "Protein ID", data: "proteinId" },
		    { title: "Parent", data: "parent" },
		    { title: "Start position", data: "min" },
			{ title: "End position", data: "max" },
			{ title: "Strand", data: "strand" },
			{ title: "Core", data: "core" },
			{ title: "Shared", data: "shared" },
			{ title: "Specific", data: "specific" },
			{ title: "GRI ID", data: "rgiName" },
			{ title: "COG dode", data: "cogId" },
			{ title: "COG description", data: "cogName" },
			{ title: "CAZy codes", data: "cazyCodes" },
			{ title: "GRI found by MAGcheck", data: "foundByMAGcheck" }
		],
		 "createdRow": function ( row, data, index ) {
			 if(data.foundByMAGcheck == 'true'){
               $(row).addClass('table-warning');
               $(row).attr('data-toggle','tooltip');
               $(row).attr('title','MAGCheck found this GRI (genomic region of interest) in raw data.');
			 }
	        }
		}
	);
	
	 new $.fn.dataTable.Buttons( dataTable, {
        buttons: [
            {extend: 'csv', text: 'Export results to CSV', className: 'btn btn-light btn-export'}
        ]
    } );
	dataTable.buttons( 0, null ).container().appendTo('#columnSelectorDiv');
	dataTable.buttons( 1, null ).container().appendTo('#exportDiv');
});

function createTable(){
	var table = $('#dataTable');
	var lines = filteredGenes;
	var dataTable = table.DataTable();
	dataTable.clear();
    dataTable.rows.add(lines);
    dataTable.draw();
    $('[data-toggle="tooltip"]').tooltip();
}


var filters = [];
var filteredGenes = [];
var subFilteredGenes = [];

function addFilter(){
	activeLoading();
	filters.push(new Filter($("#filterType").val(), $("#filterField").val(), $("#filterComparatorType").val(), $("#filterContent").val()));
	setTimeout( updateFilterAsync, 100 );
}

function removeFilter(index){
	activeLoading();
	filters.splice(index, 1);
	setTimeout( updateFilterAsync, 100 );
}

function updateFilterAsync(){
	$("#appliedFilters").empty();
	filters.forEach(function (item, indice, array) {
		
		$("#appliedFilters").append('<div class="alert alert-secondary bg-light" role="alert">' + 
				generateFilterDescription(item) +
				'<button type="button" class="close" onclick="removeFilter(' + indice + ')">' + 
		        '<span aria-hidden="true">&times;</span>' + 
		        '</button>' +
				'</div>');	
	});
	
	executeFilters();
	createTable();
	$("#filterContent").val("");
	deactiveLoading();	
}	

function generateFilterDescription(item){
	return filterTypeDescription[item.filterType] +  
	' genes where ' + 
	fieldsDescription[item.filterField] +  
	comparatorDescription[item.filterComparatorType]  +   
	item.filterContent;
}

function configureFilterContentField(){
	var filterComparatorType = $("#filterComparatorType").val();

	var hideFilterContentTypes = ["empty","notEmpty"];
	
	if (hideFilterContentTypes.includes(filterComparatorType)){
		$("#filterContent").hide();
	}else{
		$("#filterContent").show();
	}
}

function executeFilters(){
	filteredGenes = [];
	subFilteredGenes = [];
	filters.forEach(function (item, indice, array) {

		if (item.filterComparatorType === 'equals'){
			subFilteredGenes = matrixGenes.filter(function(subitem) {
				if (subitem[item.filterField].toLowerCase() === item.filterContent.toLowerCase()){
					return true;
				}
				return false;
			});
		}
		
		if (item.filterComparatorType === 'notEquals'){
			subFilteredGenes = matrixGenes.filter(function(subitem) {
				if (!(subitem[item.filterField].toLowerCase() === item.filterContent.toLowerCase())){
					return true;
				}
				return false;
			});
		}
		
		if (item.filterComparatorType === 'in'){
			console.log('in filter');
			subFilteredGenes = matrixGenes.filter(function(subitem) {
				if (item.filterContent.toLowerCase().indexOf(subitem[item.filterField].toLowerCase()) != -1 && !(subitem[item.filterField] === '')){
					return true;
				}
				return false;
			});
		}
		
		if (item.filterComparatorType === 'notIn'){
			console.log('notIn filter');
			subFilteredGenes = matrixGenes.filter(function(subitem) {
				if (item.filterContent.toLowerCase().indexOf(subitem[item.filterField].toLowerCase()) === -1 || subitem[item.filterField] === ''){
					return true;
				}
				return false;
			});
		}
		
		if (item.filterComparatorType === 'contains'){
			console.log('contains filter');
			subFilteredGenes = matrixGenes.filter(function(subitem) {
				if (subitem[item.filterField].toLowerCase().indexOf(item.filterContent.toLowerCase()) != -1){
					return true;
				}
				return false;
			});
		}
		
		if (item.filterComparatorType === 'notContains'){
			console.log('notContains filter');
			subFilteredGenes = matrixGenes.filter(function(subitem) {
				if (subitem[item.filterField].toLowerCase().indexOf(item.filterContent.toLowerCase()) === -1){
					return true;
				}
				return false;
			});
		}
		
		if (item.filterComparatorType === 'empty'){
			console.log('isEmpty filter');
			subFilteredGenes = matrixGenes.filter(function(subitem) {
				if (subitem[item.filterField] === ''){
					return true;
				}
				return false;
			});
		}
		
		if (item.filterComparatorType === 'notEmpty'){
			subFilteredGenes = matrixGenes.filter(function(subitem) {
				if (!(subitem[item.filterField] === '')){
					return true;
				}
				return false;
			});
		}
		
		if (item.filterType === 'add'){
			filteredGenes = filteredGenes.concat(subFilteredGenes.filter(function(i) {
				  return filteredGenes.indexOf(i) == -1;
			}));
		} else if (item.filterType === 'remove'){			
			filteredGenes = filteredGenes.filter(function(i) {
				  return subFilteredGenes.indexOf(i) == -1;
			});
		} else if (item.filterType === 'intersect'){			
			filteredGenes = filteredGenes.filter(function(i) {
				  return subFilteredGenes.indexOf(i) > -1;
			});
		}
		
	});	
}


var filterTypeDescription = { 
		add:'Added',
		remove:'Removed',
		intersect:'Intersected'
};

var fieldsDescription = { 
		genomeName:'genome',
		locusTag:'locus tag',
		proteinId:'protein ID',
		product:'product',
		strand:'strand',
		core:'gene core',
		shared:'gene shared',
		specific:'gene specific',
		rgiName:'GRI ID',
		cogId:'COG code',
		cogName:'COG description',
		cazyCodes:'cazy codes',
		foundByMAGcheck:'GRI found by MAGcheck'
};

var comparatorDescription = { 
		equals: ' is equals: ',
		notEquals: ' is not equals: ',
		in: ' in: ',
		notIn: ' not in: ',
		empty: ' is empty.',
		notEmpty: ' is not empty.',
		contains: ' contains: ',
		notContains: ' not contains: '
}