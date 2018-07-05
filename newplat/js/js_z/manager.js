/**
 * 推送消息初始化
 * 
 * @param column
 *            栏目
 * @param itemId
 *            id
 */
function pushInit(column, itemId) {
	$("#results").html("");
	$.ajax({
		url : "../home/homeAction.do?method=pushInit&column=" + column
				+ "&itemId=" + itemId,
		cache : false,
		success : function(html) {
			$("#results").append(html);
			var content = $.trim($("#contentid").text());
			var length = 0;
			var maxlength = 0;
			if (content != '') {
				length = content.length;
				if (length > 50) {
					maxlength = 50;
				} else {
					maxlength = length;
				}
			}
			var newCon = content.substring(0, maxlength);
			$('#pushCon').html(newCon);
			showDiv('thickdiv', 'contents', 390, 350);
		}
	});
}
function push() {
	$.post("../home/homeAction.do?method=push", $("#pushForm")
			.serialize(),// 发送表单数据
	function(data) {
		data = $.trim(data);
//		if (data == "true") {
//			alert('推送消息发送成功');
//		}else{
//			alert('推送消息发送失败');
//		}
	});
	alert('已发送');
	closeCover('thickdiv', 'contents');//关闭弹出层
}