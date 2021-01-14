$( document ).ready(function() {
	setTitle();
	$("#menu").removeClass('d-none');
	
	if (!annotatedGenomes){
		$(".annotated-genomes").hide();
	}
	if (!cazyAnnotations){
		$(".cazy-annotations").hide();
	}

});

$(function () {
  $('[data-toggle="tooltip"]').tooltip();
})

function setTitle(){
	$("#title").text(title);
}

function activeLoading(){
	$("#spinner").removeClass("invisible");
	$("#addFilterButton").attr("disabled", true);
}

function deactiveLoading(){
	$("#spinner").addClass("invisible");
	$("#addFilterButton").attr("disabled", false);
}