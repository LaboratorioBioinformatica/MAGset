$( document ).ready(function() {
	activeLoading();
	setTimeout( createTable, 100 );
});

function createTable(){
	var table = $('#dataTable');
	var headLine = '<tr><th>Gene name</th><th>N. isolates</th>';
	var magOfInterestIndex = -1;
	genomesName.sort().forEach(function (item, indice, array) {
		var magClass = 'teste';
		if (item === magOfInterest){
			magClass = 'table-mag-class';
			magOfInterestIndex = indice;
		}
		headLine = headLine + '<th class=\'' + magClass + '\'>' + item + '</th>';
	});
	
	headLine = headLine + '</tr>';
	table.children('thead').append(headLine);	
	table.children('tfoot').append(headLine);	
	var tbody = table.children('tbody');
	var lines = pangenomeGenes;
	lines.forEach(function (item, indice, array) {
		line = '<tr><td>' + item.geneName + '</td><td>' + item.numberOfIsolates + '</td>';
		
		item.genomeGeneNames.forEach(function (subitem, subindice, subarray) {
			var magClass = 'teste';
			if (magOfInterestIndex === subindice){
				magClass = 'table-mag-class';
			}
			line = line + '<td class=\'text-center ' + magClass + '\'>' + subitem + '</td>';
		});
		
		if (item.genomeGeneNames.length != genomesName.length){
			for(var i = 0; i < (genomesName.length - item.genomeGeneNames.length);i++){
				line = line + '<td></td>';
			}
		}
		
		
		line = line + '</tr>';
		tbody.append(line);
	});
	
	
	var dataTable = table.DataTable( {
		 buttons: [{extend: 'csv', text: 'Export results to CSV', className: 'btn btn-light btn-export'}]	
	});
	dataTable.buttons().container().appendTo( '#exportDiv' );
	deactiveLoading();
}
