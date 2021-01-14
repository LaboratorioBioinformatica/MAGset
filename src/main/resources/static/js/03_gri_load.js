$( document ).ready(function() {
	activeLoading();
	setTimeout( createTable, 100 );
});

function createTable(){
	var table = $('#dataTable');
	var headLine = '<tr>'
		+ '<th >Id</th>'
		+ '<th >Genome</th>'
		+ '<th >Parent</th>'
		+ '<th >Size</th>'
		+ '<th >Start position</th>'
		+ '<th >End position</th>'
		+ '<th >Genes Qty</th>'
		+ '<th>Covered positions</th>'
		+ '<th>Covered positions (%)</th>'
		+ '<th>Mean depth</th>'
		+ '</tr>';
	table.children('thead').append(headLine);	
	table.children('tfoot').append(headLine);	
	var tbody = table.children('tbody');
	var lines = gris;
	lines.forEach(function (item) {
		var magClass = '';
		if (item.genomeName === magOfInterest){
			magClass = 'table-mag-class';
		}
		var tr = '<tr>';
		if (item.magCheckAlert){
			tr ='<tr class="table-warning" data-toggle="tooltip" title="MAGCheck found this GRI (genomic region of interest) in raw data." >'; 
		}
		
		line = tr + '<td>' + item.id 
		+ '</td><td class=\'' + magClass + '\'>' + item.genomeName
		+ '</td><td>' + item.sequenceName 
		+ '</td><td>' + item.size 
		+ '</td><td>' + item.start 
		+ '</td><td>' + item.end 
		+ '</td><td>' + (annotatedGenomes?item.genesQty:'N/A')
		+ '</td><td>' + (magCheckExecuted?item.coveredPositions:'N/A')
		+ '</td><td>' + (magCheckExecuted?item.coverage.toFixed(2):'N/A')
		+ '</td><td>' + (magCheckExecuted?item.meanDepth.toFixed(2):'N/A')
		+ '</td></tr>';
		
		tbody.append(line);
	});
	
	
	var dataTable = table.DataTable( {
		 buttons: [{extend: 'csv', text: 'Export results to CSV', className: 'btn btn-light btn-export'}]	
	});
	dataTable.buttons().container().appendTo( '#exportDiv' );
	deactiveLoading();
}
