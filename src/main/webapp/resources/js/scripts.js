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

function applySelectedCell(event) {
	var cell = $(event.target);

	var id = cell[0].parentElement.id;
	if (id == '' && cell[0].children.length > 0) {
		id = cell[0].children[0].id;
	}
	var type = '';

	if (cell.closest('th').length > 0) {
		cell = cell.closest('th');
	} else {
		if (!cell.is('td')) {
			cell = cell.closest('td');
		}
	}
	var row = cell.closest('tr');

	var selectedCell = row.children().index(cell);

	console.log('selectedCell-' + selectedCell);
	if (cell.closest('#lessonModeTable_scrollableTbody').length > 0) {
		type = 'STUDENT_CLASS';
		id = cell[0].children[0].id;
	}
	if (cell.closest('#lessonModeTable_frozenTbody').length > 0) {
		type = 'STUDENT';
	}
	if (cell.closest('#lessonModeTable_scrollableThead').length > 0) {
		type = 'LESSON';
		id = "lessonModeTable";
	}

	console.log("id - " + id);
	$('#selectedClientId').val(id);
	$('#selectedCell').val(selectedCell);
	$('#selectedType').val(type);

	//selectCell();
}