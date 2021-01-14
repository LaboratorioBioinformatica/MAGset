$( document ).ready(function() {
	activeLoading();
	setTimeout( createTable, 100 );
});

function createTable(){
	var table = $('#dataTable');
	var headLine = '<tr><th>GRI</th><th>Genome</th><th>Type</th><th>Locus tag</th><th>Product</th><th>Protein ID</th><th>Start position</th><th>End position</th><th>Strand</th><th>Parent</th></tr>';
	table.children('thead').append(headLine);	
	table.children('tfoot').append(headLine);	
	var tbody = table.children('tbody');
	var lines = griGenes;
	lines.forEach(function (item, indice, array) {
		var magClass = '';
		if (item.genomeName === magOfInterest){
			magClass = 'table-mag-class';
		}
		line = '<tr><td>' + item.griName + '</td><td class=\'' + magClass + '\'>' + item.genomeName + '</td><td>' + item.type + '</td><td>' + item.locusTag + '</td><td>' + item.product + '</td><td>' + item.proteinId + '</td><td>' + item.min + '</td><td>' + item.max + '</td><td>'+ item.strand + '</td><td>'+ item.parent + '</td></tr>';
		
		tbody.append(line);
	});
	
	
	var dataTable = table.DataTable( {
		 buttons: [{extend: 'csv', text: 'Export results to CSV', className: 'btn btn-light btn-export'}]	
	});
	dataTable.buttons().container().appendTo( '#exportDiv' );
	deactiveLoading();
}