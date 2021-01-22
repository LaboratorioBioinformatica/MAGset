$( document ).ready(function() {
	activeLoading();
	setTimeout( loadDataAsync, 100 );
});


function loadDataAsync(){
	loadSummaryGeneTable();
	loadSummaryGRITable();
	loadSummaryAnnotations();
	loadSummaryMAGCheck();
	loadSummaryBoxes();
	deactiveLoading();
	
}
function loadSummaryGeneTable(){
	var table = $('#summaryGeneTable');
	var headLine = '<tr><th></th><th>Genes</th><th>Specific</th><th>Shared</th><th>Core</th><th>Nonconsidered</th></tr>';
	table.children('thead').append(headLine);	
	var tbody = table.children('tbody');
	var lines = summaryResults;
	
	if (!annotatedGenomes){
		tbody.append('<tr><td colspan="6">Not available for FASTA input.</td></tr>');
	}else{
		lines.forEach(function (item) {
			var magClass = '';
			if (item.genomeName === magOfInterest){
				magClass = 'table-mag-class';
			}
			line = '<tr class=\'' + magClass + '\'><th class=\'text-left\'>' + item.genomeName + '</th><td>' + item.genesQty + '</td><td>' + item.specificGenesQty + '</td><td>' + item.sharedGenesQty + '</td><td>' + item.coreGenesQty + '</td><td>' + item.discartedGenes + '</td>';
			line = line + '</tr>';
			tbody.append(line);
		});	
	}
}

function loadSummaryGRITable(){
	var table = $('#summaryGRITable');
	var headLine = '<tr><th></th><th>Genome size (bp)</th><th>GRI Qty</th>><th>Sum of GRIs size</th><th>Genes in RGIs</th></tr>';
	table.children('thead').append(headLine);	
	var tbody = table.children('tbody');
	var lines = summaryResults;
	lines.forEach(function (item) {
		var magClass = '';
		if (item.genomeName === magOfInterest){
			magClass = 'table-mag-class';
		}
		var genesInGrisQty = item.genesInGrisQty;
		if (!annotatedGenomes){
			genesInGrisQty = 'N/A';
		}
		line = '<tr class=\'' + magClass + '\'><th class=\'text-left\'>' + item.genomeName + '</th><td>' + item.genomeSize.toLocaleString('en-US') + '</td><td>' + item.grisQty + '</td><td>' + item.sumOfGrisSize.toLocaleString('en-US') + '</td><td>' + genesInGrisQty + '</td>';
		line = line + '</tr>';
		tbody.append(line);
	});	
}

function loadSummaryAnnotations(){
	var table = $('#annotationsTable');
	var headLine = '<tr><th></th><th>CAZy</th><th>COGs</th></tr>';
	table.children('thead').append(headLine);	
	var tbody = table.children('tbody');
	var lines = summaryResults;
	if (!annotatedGenomes){
		tbody.append('<tr><td colspan="3">Not available for FASTA input.</td></tr>');
	}else{
		lines.forEach(function (item) {
			var magClass = '';
			if (item.genomeName === magOfInterest){
				magClass = 'table-mag-class';
			}
			line = '<tr class=\'' + magClass + '\'><th class=\'text-left\'>' + item.genomeName + '</th><td>' + (cazyAnnotations?item.cazyQty:'N/A') + '</td><td>' + item.cogQty + '</td>';
			line = line + '</tr>';
			tbody.append(line);
		});
	}
}

function loadSummaryMAGCheck(){
	var table = $('#summaryMAGCheckTable');
	var headLine = '<tr><th></th><th>GRIs found in raw data</th><th>Sum of GRIs size</th><th>Genes in GRIs</th></tr>';
	table.children('thead').append(headLine);	
	var tbody = table.children('tbody');
	var lines = summaryResults;
	lines.forEach(function (item) {
		if (item.genomeName === magOfInterest){
			return;
		}
		var grisFoundByMAGCheckQty = item.grisFoundByMAGCheckQty;
		var genesInGRIsFoundByMAGCheckQty = item.genesInGRIsFoundByMAGCheckQty;
		var  sumOfGrisFoundByMAGCheckSize = item.sumOfGrisFoundByMAGCheckSize;
		if (!magCheckExecuted){
			grisFoundByMAGCheckQty = 'N/A';
			genesInGRIsFoundByMAGCheckQty = 'N/A';
			sumOfGrisFoundByMAGCheckSize = 'N/A';
			$("#magCheckFileText").hide();
		}
		if (!annotatedGenomes){
			genesInGRIsFoundByMAGCheckQty = 'N/A';
		}
		
		line = '<tr><th class=\'text-left\'>' + item.genomeName + '</th><td>' + grisFoundByMAGCheckQty + '</td><td>' + sumOfGrisFoundByMAGCheckSize.toLocaleString('en-US') + '</td><td>' + genesInGRIsFoundByMAGCheckQty + '</td>';
		line = line + '</tr>';
		tbody.append(line);
	});	
}

function loadSummaryBoxes(){
	$("#genomes-qty").html(genomesName.length);
	var grisTotal = 0;
	var genesTotal = 0;
	var genesInGrisTotal = 0;
	summaryResults.forEach(
			function (item) {
				grisTotal += item.grisQty;
				genesTotal += item.genesQty;
				genesInGrisTotal += item.genesInGrisQty;
			});
	
	$("#gris-qty").html(grisTotal.toLocaleString('en-US'));
	
	if (!annotatedGenomes){
		$("#genes-qty").html("N/A");
		$("#gri-genes-qty").html("N/A");
	}else{
		$("#genes-qty").html(genesTotal.toLocaleString('en-US'));
		$("#gri-genes-qty").html(genesInGrisTotal.toLocaleString('en-US'));
	}
}