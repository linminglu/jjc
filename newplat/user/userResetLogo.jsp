<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.ram.model.User"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf8" />
<title>用户中心 - <c:out value="${slogan}"/></title>
<c:import url="../common/inc_style.jsp"/>
<script src="../uploadMt/xiuxui_gai_mini.js" type="text/javascript"></script>
<script type="text/javascript">
	window.onload = function() {
		xiuxiu.setLaunchVars ("maxFinalWidth", 100);
		xiuxiu.setLaunchVars ("maxFinalHeight", 100);
		xiuxiu.setLaunchVars ("scrollTrap", 1);
		xiuxiu.params.wmode = "transparent";
		
		xiuxiu.embedSWF("altContent", 5, "100%", "100%");
		/*第1个参数是加载编辑器div容器，第2个参数是编辑器类型，第3个参数是div容器宽，第4个参数是div容器高*/
		xiuxiu.setUploadType(2);
		xiuxiu.setUploadURL("<%=basePath%>user/userLogoUpload.jsp");//修改为您自己的上传接收图片程序
		
		xiuxiu.onBeforeUpload = function(data, id) {
			xiuxiu.setUploadArgs({
				filetype : data.type,
				type : "image",
				filename : data.name
			});
		};
		xiuxiu.onInit = function() {
<%-- 			xiuxiu.loadPhoto("<%=basePath%>/uploadMt/exa.jpg"); --%>
		};
		xiuxiu.onUploadResponse = function(data) {
			data=$.trim(data);
			if(data=="true"){
				$.dialog({
					title : '提示',
					content : "修改成功",
					zIndex: 9999,
					ok:function(){
						document.location.reload();// 刷新当前页面
					}
				});
			}else{
				$.dialog({
					title : '提示',
					content : "修改失败，请重新修改",
					zIndex: 9999,
					ok:function(){
						document.location.reload();// 刷新当前页面
					}
				});
			}
		};
	}
	$(document).ready(function(){
		var err = $("#errors");
		if(err && err.val()!=""){
			
		}
	});
</script>
</head>
<body>
<input type="hidden" name="errors" id="errors" value="<c:out value="${error}"/>" />
<div class="hdover">
	<div class="topline"></div>
	<c:import url="../common/inc_naver.jsp">
			<c:param name="mpath" value=""></c:param>
	</c:import>
</div>


<div class="content">
	<div class="signup">
		<div class="n-left-menu">
			<h3></h3>
			<ul>
				<li >
					<a href="/myhome/index.html">个人信息</a>
				</li>
				<li class="selected">
					<a href="/myhome/resetLogo.html">修改头像</a>
				</li>
				<li >
					<a href="/myhome/resetPassword.html">修改密码</a>
				</li>
			</ul>
		</div>
		<div class="n-right">
			<div class="title_1">
				<a class="selected" href="#">修改头像</a>
			</div>
			<div class="m1L" style="position:relative;">
				<div class="logoshow">
					<span>您现在的头像：</span>
					<span><img src="<c:out value='${loginUser.logo}'/>"/></span> 
					<span><img src="<c:out value='${loginUser.logoMini}'/>"/></span> 
				</div>
				<div style="width: 680px; height: 441px; z-index: 1;">
					<div id="altContent">
						<h1>果子联盟</h1>
					</div>
				</div>
				<div id="zhedang">
				</div>
			</div>
		</div>
	</div>
</div>
<c:import url="../common/inc_footer.jsp"/>
</body>
</html>
