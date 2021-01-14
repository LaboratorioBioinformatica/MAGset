$( document ).ready(function() {
	activeLoading();
	setTimeout( createTable, 100 );
});

function createTable(){
	var table = $('#dataTable');
	var headLine = '<tr><th>Locus tag</th><th>CAZy Codes</th></tr>';
	table.children('thead').append(headLine);	
	table.children('tfoot').append(headLine);	
	var tbody = table.children('tbody');
	var lines = cazyGenes;
	lines.forEach(function (item, indice, array) {
		line = '<tr><td>' + item.locusTag + '</td><td> '+ item.cazyCodes+ '</td></tr>';
		
		tbody.append(line);
	});
	
	
	var dataTable = table.DataTable( {
		 buttons: [{extend: 'csv', text: 'Export results to CSV', className: 'btn btn-light btn-export'}]	
	});
	dataTable.buttons().container().appendTo( '#exportDiv' );
	deactiveLoading();
}