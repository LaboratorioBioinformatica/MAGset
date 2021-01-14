$( document ).ready(function() {
	activeLoading();
	setTimeout( createTable, 100 );
});

function createTable(){
	var table = $('#dataTable');
	var headLine = '<tr><th>Locus tag</th><th>Type</th><th>Product</th><th>Protein ID</th><th>Start position</th><th>End position</th><th>Strand</th><th>Parent</th><th>Core</th><th>Shared</th><th>Specific</th><th>RGI name</th><th>COG ID</th><th>COG Description</th><th>CAZy Codes</th></tr>';
	table.children('thead').append(headLine);	
	table.children('tfoot').append(headLine);	
	var tbody = table.children('tbody');
	var lines = matrixGenes;
	lines.forEach(function (item, indice, array) {
		var tr = '<tr>';
		if (item.magCheckAlert){
			tr ='<tr class="table-warning" data-toggle="tooltip" title="MAGCheck found this GRI (genomic region of interest) in raw data." >'; 
		}
		line = tr +	'<td>' 
			+ item.locusTag + '</td><td>' 
			+ item.type + '</td><td>' 
			+ item.product + '</td><td>' 
			+ item.proteinId + '</td><td>' 
			+ item.min + '</td><td>' 
			+ item.max + '</td><td>'
			+ item.strand + '</td><td>'
			+ item.parent + '</td><td>'
			+ item.core + '</td><td>'
			+ item.shared + '</td><td>'
			+ item.specific + '</td><td>'
			+ item.rgiName + '</td><td>'
			+ item.cogId+ '</td><td>'
			+ item.cogName + '</td><td>'
			+ item.cazyCodes + '</td></tr>';
		
		tbody.append(line);
	});
	
	
	var dataTable = table.DataTable( {
		 buttons: [{extend: 'csv', text: 'Export results to CSV', className: 'btn btn-light btn-export'}]	
	});
	dataTable.buttons().container().appendTo( '#exportDiv' );
	deactiveLoading();
}
