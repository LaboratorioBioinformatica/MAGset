$( document ).ready(function() {
	activeLoading();
	setTimeout( createTable, 100 );
});

function createTable(){
	var table = $('#dataTable');
	var headLine = '<tr><th>Locus tag</th><th>COG ID</th><th>COG Description</th></tr>';
	table.children('thead').append(headLine);	
	table.children('tfoot').append(headLine);	
	var tbody = table.children('tbody');
	var lines = cogGenes;
	lines.forEach(function (item, indice, array) {
		line = '<tr><td>' + item.locusTag + '</td><td> '+ item.cogId+ '</td><td>'+ item.cogDescription + '</td></tr>';
		
		tbody.append(line);
	});
	
	
	var dataTable = table.DataTable( {
		 buttons: [{extend: 'csv', text: 'Export results to CSV', className: 'btn btn-light btn-export'}]	
	});
	dataTable.buttons().container().appendTo( '#exportDiv' );
	deactiveLoading();
}