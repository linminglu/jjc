<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript">

function isValidIP(ip) {
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    return reg.test(ip.trim());
} 
//判断了4 到6位数字
function isValidUid(uid) {
    var reg = /^\d{4,6}$/;
    return reg.test(uid.trim());
}

//查看账单投注详情
function del(id){
	layer.confirm('确定删除该条数据吗?', {icon: 3, offset: '100px', title:'提示'}, function(index){
		layer.close(index);
		$.post("blackListAction.do?method=deleteBalck&bid="+id,
			function(data) {
				var jsonData = JSON.parse(data);
	            var code = jsonData.code;
	            if (code == "200") {
			        layer.alert(jsonData.msg, {icon: 6, offset: '100px'},function(index){
				        location.reload();
			        	layer.close(index)
			        });
		        }else{
			        layer.alert(jsonData.msg, {icon: 5, offset: '100px'});
		        }
		});	
	});	
}


function add(){
	  var html='<div class="layui-form-item" style="margin-top:10px;">'+
	    '<label class="layui-form-label">类型</label>'+
	    '<div class="layui-input-block">'+
	      '<select name="interest" lay-filter="aihao" id="blackType"  style="width:200px;">'+
	      	'<option value="">请选择</option>'+
	        '<option value="1">黑名单用户ID</option>'+
	        '<option value="2">黑名单IP地址</option>'+
	        '<option value="3">白名单IP地址</option>'+
	      	'</select>'+
	    '</div>'+
	  '</div>'+
	  '<div class="layui-form-item layui-form-text">'+
		    '<label class="layui-form-label">值</label>'+
		    '<div class="layui-input-block">'+
		      ' <input type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入值" class="layui-input"  style="width:200px;"  id="blackValue">'+
		    '</div>'+
		'</div>'+	
	    '<div class="layui-input-block">'+
	      '<button class="layui-btn" lay-submit="" lay-filter="demo1" onclick="saveBlack();">提交</button>'+
	      '<button  type="reset" class="layui-btn layui-btn-primary" onclick="resetBlack();">重置</button>'+
	  '</div>';	
	   layer.open({
	     	        title: '添加黑名单',
	     	        area: ['380px','260px'],
	     	        skin: 'layui-layer-lan',
	     	        type: 1,
	     	        content: html
	   });
}
function saveBlack(){
	var blackValue=document.getElementById("blackValue").value;
	var blackType=document.getElementById("blackType").value;
	if(blackType==""){
		 layer.alert("请选择黑名单类型", {icon: 5, offset: '100px'});
		 return;
	}
	if(blackValue==null||(blackValue!=null&&blackValue.trim()=="")){
		 layer.alert("请输入值", {icon: 5, offset: '100px'});
		 return;
	}else{
		if(blackType=="1"){
			if(!isValidUid(blackValue)){
				 layer.alert("请填写合法用户ID", {icon: 5, offset: '100px'});
				 return;
			}
		}else if(blackType=="2" || blackType=="3"){
			if(!isValidIP(blackValue)){
				 layer.alert("请填写合法IP地址", {icon: 5, offset: '100px'});
				 return;
			}
		}
	}
	layer.confirm('确定添加该条数据吗?', {icon: 3, offset: '100px', title:'提示'}, function(index){
		layer.close(index);
		$.post("blackListAction.do?method=saveBlack&blackValue="+blackValue.trim()+"&blackType="+blackType.trim(),
			function(data) {
				var jsonData = JSON.parse(data);
	            var code = jsonData.code;
	            if (code == "200") {
			        layer.alert(jsonData.msg, {icon: 6, offset: '100px'},function(index){
				        location.reload();
			        	layer.close(index)
			        });
		        }else{
			        layer.alert(jsonData.msg, {icon: 5, offset: '100px'});
		        }
		});	
	});	
}

function resetBlack(){
	document.getElementById("blackValue").value="";
	document.getElementById("blackType").value="";
}
</script>

</head>
<body>
<div class="bread">系统管理  &raquo; 黑名单
</div>
<html:form action="/blackListAction.do?method=initBlackList">
<html:hidden property="startIndex" name="blackListForm"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			<span>开始时间：<html:text property="startDate" name="blackListForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endDate" name="blackListForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
    	    <input type="button" class="gbutton" value="添加" onclick="add();"/>
		</td>
	</tr>
</table>
</html:form>
<%
int number=1;
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
%>
<pg:pager 
	url="blackListAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="initUserLog"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>类型</th>
		<th>值</th>
		<th>时间</th>
		<th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	    	<c:if test="${item.type eq '1' }">
	    		黑名单用户id
	    	</c:if>
	    	<c:if test="${item.type eq '2' }">
	    		黑名单IP地址
	    	</c:if>
	    	<c:if test="${item.type eq '3' }">
	    		白名单IP地址
	    	</c:if>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="value" />
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="createTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
		<td align="center">
				 <a href='javascript:void(0);'  onclick="del(<bean:write name="item" property="bid"/>);"  >删除</a>
		</td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="/template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
</body>
</html:html>