function resizeDialogHeightByPercent(id, percent) {
	var htmlTag = document.getElementById(id);
	htmlTag.style.height = Math.floor(window.innerHeight*(percent/100))+"px";
}
