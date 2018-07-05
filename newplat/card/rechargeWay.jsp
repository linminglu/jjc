<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include file = "../common/inc_file.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<script type="text/javascript">
function initLiMenu(){
	$(".layui-tab-title .way-line").bind("click", function(){
		$(".layui-tab-title .way-line").removeClass('layui-this');
		$(this).addClass("layui-this");
		var channelId=$(this).attr("id");
		var passageWayId=$("#passageWayId").val();
		document.forms[0].action = "rechargeWayAction.do?method=initRechargeWay&passageWayId="+passageWayId+"&channelId="+channelId;
		document.forms[0].submit();
	});
}
function changeStatus2(status){// 更改渠道的状态
	var id=$(".layui-this").attr("id");
	$.post("rechargeWayAction.do?method=changeStatus&id="+ id , 
		function(data) {
			data = $.trim(data);
			if (data == "success") {
				if(status=='1'){
					$(".valid").hide();
					$(".invalid").show();
				}else{
					$(".valid").show();
					$(".invalid").hide();
				}
			}
	});	
}
function add(){
	var passageWayId=$("#passageWayId").val();
	document.forms[0].action = "rechargeWayAction.do?method=preModify&passageWayId="+passageWayId;
	document.forms[0].submit();
}
function preModify(id){
	var passageWayId=$("#passageWayId").val();
	document.forms[0].action = "rechargeWayAction.do?method=preModify&passageWayId="+passageWayId+"&wayId="+id;
	document.forms[0].submit();
}
function del(id){
	if(confirm('确实要删除吗？')){
		$.post("rechargeWayAction.do?method=del&id="+id, 
			function(data) {
				data = $.parseJSON(data);
				if(data && data.code=='200') {
					location.reload();
					alert("删除成功！");
				}else{
					alert("删除失败！\n\n提示：数据已应用则不能删除！");
				}
		});	
	}
}
window.onload = function(){
	initLiMenu();
	var status=$(".layui-this").attr("status");
	if(status==1){
		$(".valid").show();
	}else{
		$(".invalid").show();
	}
}
</script>
<%@ include file = "../common/inc_file.jsp"%>
<style type="text/css">
	.layui-tab-brief{height: 80px;}
	.layui-tab-title li{position: relative;}
	.way-status{position: absolute;bottom: -35px;left: 20px;}
</style>
</head>
<body>
<div class="bread">充值/提现管理 &raquo;充值方式管理 </div>
<%-- <html:form action="/rechargeWayAction.do?method=initRechargeWay" method="post"> --%>
<form class="layui-form" action="/rechargeWayAction.do?method=initRechargeWay" method="post" lay-filter="reform">
<html:hidden name="rechargeWayForm" property="passageWayId" styleId="passageWayId"/>
	<div class="layui-tab-brief ">
		<ul class="layui-tab-title layui-tab-more" style="padding-right:0;">
			<logic:iterate id="way" name="rwList" indexId="num">
				<c:if test="${rechargeWayForm.channelId==null}">
					<logic:equal name="num" value="0">
						<li class="way-line layui-this" status="<bean:write name="way" property="status"/>" id="<bean:write name="way" property="id"/>">
							<bean:write name="way" property="title"/>
							<span class="way-status">
								<c:if test="${way.status == '1'}">
									<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="1" data="<bean:write name="way" property="id"/>" id="status<bean:write name="way" property="id"/>"/>
								</c:if>
								<c:if test="${way.status == '0'}">
									<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="0" data="<bean:write name="way" property="id"/>" id="status<bean:write name="way" property="id"/>"/>
								</c:if>
							</span>
						</li>
					</logic:equal>
					<logic:notEqual name="num" value="0">
						<li class="way-line" status="<bean:write name="way" property="status"/>" id="<bean:write name="way" property="id"/>">
							<bean:write name="way" property="title" />
							<span class="way-status">
								<c:if test="${way.status == '1'}">
									<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="1" data="<bean:write name="way" property="id"/>" id="status<bean:write name="way" property="id"/>"/>
								</c:if>
								<c:if test="${way.status == '0'}">
									<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="0" data="<bean:write name="way" property="id"/>" id="status<bean:write name="way" property="id"/>"/>
								</c:if>
							</span>
						</li>
					</logic:notEqual>
				</c:if>
				<c:if test="${rechargeWayForm.channelId!=null}">
					<c:if test="${rechargeWayForm.channelId==way.id}">
						<li class="way-line layui-this" status="<bean:write name="way" property="status"/>" id="<bean:write name="way" property="id"/>">
							<bean:write name="way" property="title"/>
							<span class="way-status">
								<c:if test="${way.status == '1'}">
									<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="1" data="<bean:write name="way" property="id"/>" id="status<bean:write name="way" property="id"/>"/>
								</c:if>
								<c:if test="${way.status == '0'}">
									<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="0" data="<bean:write name="way" property="id"/>" id="status<bean:write name="way" property="id"/>"/>
								</c:if>
							</span>
						</li>
					</c:if>
					<c:if test="${rechargeWayForm.channelId!=way.id}">
						<li class="way-line" status="<bean:write name="way" property="status"/>" id="<bean:write name="way" property="id"/>">
							<bean:write name="way" property="title" />
							<span class="way-status">
								<c:if test="${way.status == '1'}">
									<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="1" data="<bean:write name="way" property="id"/>" id="status<bean:write name="way" property="id"/>"/>
								</c:if>
								<c:if test="${way.status == '0'}">
									<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="0" data="<bean:write name="way" property="id"/>" id="status<bean:write name="way" property="id"/>"/>
								</c:if>
							</span>
						</li>
					</c:if>
				</c:if>
			</logic:iterate> 
			<li>
				<input type="button" class="gbutton" value="添加方式" onclick="add()"/>
			</li>
		</ul>
	</div>
</form>	
<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userType =request.getAttribute("userType")!=null?(String)request.getAttribute("userType"):"";
String category =request.getAttribute("category")!=null?(String)request.getAttribute("category"):"";
String agentId =request.getAttribute("agentId")!=null?(String)request.getAttribute("agentId"):"0";
%>
<pg:pager 
	url="rechargeWayAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="initRechargeWay"/>
	<pg:param name="userType" value="<%=userType%>"/>
	<pg:param name="category" value="<%=category%>"/>
	<pg:param name="agentId" value="<%=agentId%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="rwList2"/>
<logic:notEmpty name="rwList2">
<form class="layui-form" action="#" lay-filter="reform">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>名称</th>
		<th width="60">状态</th>
	    <th width="60">操作</th>
	</tr>
	<logic:iterate id="item" name="rwList2" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="title" />
	    </td>
	    <td align="center">
	    	<c:if test="${item.status == '1'}">
				<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="1" data="<bean:write name="item" property="id"/>" id="status<bean:write name="item" property="id"/>"/>
			</c:if>
			<c:if test="${item.status == '0'}">
				<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="0" data="<bean:write name="item" property="id"/>" id="status<bean:write name="item" property="id"/>"/>
			</c:if>
	    </td>
	   	<td align="center">
	   		<a onclick="preModify(<bean:write name="item" property="id"/>)"><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
	   		<a href="javascript:del(<bean:write name="item" property="id"/>);"><img class="xtbImg ico-del" src="../images/trash.png"/></a>
	   	</td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
</form>
</logic:notEmpty>
<logic:empty name="rwList2">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>
<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
<script type="text/javascript">
layui.use('element', function(){
});
layui.use('form', function(){
	var form = layui.form;
	form.on('switch(statusSwitch)', function(data){
		var loadObj = layer.load();
		var obj=data.elem;
		var id=$(obj).attr('data');
		var checked=data.elem.checked;		
		var val='0';
		if(checked){
			val='1';
		}
		$.post("rechargeWayAction.do?method=changeStatus", 
			{'id':id},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					layer.close(loadObj);
					$('#status'+id).val(val);
				}else{
					layer.close(loadObj);
					//失败的话
					if(val=='1'){
						$('#status'+id).val('0');
						$('#status'+id).removeAttr('checked');
					}else{
						$('#status'+id).val('1');
						$('#status'+id).attr('checked',true);
					}
					form.render('checkbox');
					layer.alert(data.msg);
				}
		}, "json");
	});  
});
</script>
</script>
</body>
</html>