function getElementsLikeId(suffix,targetName){
	var elements;
	if (!isNull(targetName)) {
		elements = document.getElementsByTagName(targetName);
	} else {
		elements = document.all;
	}
	if (isNull(elements.length)) {
		return null;
	}
	var result = new Array();
	for (i = 0; i < elements.length; i++) {
		var id = elements[i].id;
		if (isNull(id)) {
			continue;
		}
		if (id.indexOf(suffix) >= 0) {
			result.push(elements[i]);
		}
	}
	return result;
}

function showOrHid(name,imgid) {
	var elements = getElementsLikeId(name);
	for (i = 0; i < elements.length; i++) {
		if (elements[i].style.display != 'none') {
			elements[i].style.display = 'none';
		} else {
			elements[i].style.display = 'block';
		}
	}
	$('#'+imgid).toggleClass("glyphicon-minus");
}

function isNull(obj) {
	if (obj == '' || obj == undefined) {
		return true;
	}
	return false;
}