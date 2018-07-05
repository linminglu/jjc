function showDiv(coverId, coverBox, boxWidth, boxHeight) {
//	var bodyScrollTop = GetPageScroll().Y;
	var windowWidth = document.body.clientWidth;
	var windowHeight = document.body.clientHeight;
//	alert(windowWidth+"vvv"+windowHeight);
//	 var windowWidth = document.documentElement.clientWidth;
//	 var windowHeight = document.documentElement.clientHeight;
	var top=50;
	if(windowHeight>boxHeight){
		top = (windowHeight / 2) - (boxHeight / 2);
	}
	var left = (windowWidth / 2) - (boxWidth / 2);// 这个200是这个弹出框的一半
	var thickdiv = document.getElementById(coverId);
	var thickbox = document.getElementById(coverBox);
	thickbox.style.cssText = "top:" + top + "px;left:" + left + "px;";

	// thickdiv.style.display = "";
	// thickbox.style.display="";//显示

	$("#" + coverId).show();
	$("#" + coverBox).show();
	// thickbox.show();

}
function closeThick(coverId, coverBox) {
	var thickdiv = document.getElementById(coverId);
	var thickbox = document.getElementById(coverBox);
	thickdiv.style.display = "none";
	thickbox.style.display = "none";
}
// 滚动条位置
function GetPageScroll() {
	var x, y;
	if (window.pageYOffset) {// all except IE
		y = window.pageYOffset;
		x = window.pageXOffset;
	} else if (document.documentElement && document.documentElement.scrollTop) {// IE 6
		// Strict
		y = document.documentElement.scrollTop;
		x = document.documentElement.scrollLeft;
	} else if (document.body) {// all other IE
		y = document.body.scrollTop;
		x = document.body.scrollLeft;
	}
	return {
		X : x,
		Y : y
	};
}

/**
 * 关闭弹出层
 */
function closeCover(coverId, contentId) {
	$("#" + coverId).hide();
	$("#" + contentId).hide();
}

