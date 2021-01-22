$( document ).ready(function() {
	loadAniTable();
});

function loadAniTable(){
	var table = $('#aniTable');
	var headLine = '<tr><th></th>';
	genomesName.forEach(function (item, indice, array) {
		headLine = headLine + '<th>' + item + '</th>';
	});
	
	headLine = headLine + '</tr>';
	table.children('thead').append(headLine);	
	var tbody = table.children('tbody');
	var lines = aniResults;
	lines.forEach(function (item, indice, array) {
		var magClass = '';
		if (item.genomeName === magOfInterest){
			magClass = 'table-mag-class';
		}
		
		line = '<tr class=\'' + magClass + '\'><th class=\'text-left\'>' + item.genomeName + '</th>';
		
		item.results.forEach(function (subitem, subindice, subarray) {
			var result = parseFloat(subitem).toFixed(2);
			var style = '';
			if (indice == subindice){
                style='background-color: #f8f9fc;color: #bebebf;';
			}
			line = line + '<td style=\'' + style + '\'>' + result + '%</td>';
		});
		line = line + '</tr>';
		tbody.append(line);
	});
}