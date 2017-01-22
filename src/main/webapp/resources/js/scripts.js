function resizeDialogHeightByPercent(id, percent) {
	var htmlTag = document.getElementById(id);
	htmlTag.style.height = Math.floor(window.innerHeight*(percent/100))+"px";
}

function resizeDialogWidthByPercent(id, percent) {
	var htmlTag = document.getElementById(id);
	htmlTag.style.width = Math.floor(window.innerWidth*(percent/100))+"px";
}

function resizeDialogByPercent(id, percent) {
	resizeDialogHeightByPercent(id, percent);
	resizeDialogWidthByPercent(id, percent);
}
