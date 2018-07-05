<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">

function changeStatus(id,status){
	$.post("gameAction.do?method=changeStatus&infoId="+ id , 
		function(data) {
			data = $.trim(data);
			if (data == "success") {
				if(status=='1'){
					$('#infoId'+id).html('<font color="red"><strong>无效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+id+',\'0\');">开</a>');
				}else{
					$('#infoId'+id).html('<font color="green"><strong>有效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+id+',\'1\');">关</a>');
				}
			}
	});	
}

function changeBetAvoid(id,status){
	$.post("gameAction.do?method=changeBetAvoid&infoId="+ id , 
		function(data) {
			data = $.trim(data);
			if (data == "success") {
				if(status=='1'){
					$('#betAvoid'+id).html('<font color="red"><strong>无效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeBetAvoid('+id+',\'0\');">开</a>');
				}else{
					$('#betAvoid'+id).html('<font color="green"><strong>有效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeBetAvoid('+id+',\'1\');">关</a>');
				}
			}
	});
}
function saveChange(id,th){
	var t = $(this);
	console.log(th);
	var t2 = $(th).parent().parent();
	var inp = t2.find(":input");
	var val = inp.val();
	if(val&&val.length>0&&val>0&&id){
		if(!isNaN(val)){
			$.post("gameAction.do?method=changeBetRate&infoId="+ id +"&rate="+val, 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						alert("修改成功");
					}
			});
		}else{
			alert("请输入正确的赔率");
		}
	}else{
		alert("请输入正确的赔率");
	}
	
}
</script>
</head>
<body>
<div class="bread"><a href="../../game/gameAction.do?method=gameList" >彩种管理</a>-<c:out value="${gameTitle}"/>
</div>
<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String infoId =request.getAttribute("infoId")!=null?(String)request.getAttribute("infoId"):"0";
String title =request.getAttribute("title")!=null?(String)request.getAttribute("title"):"";
List<Object> playTypeStrs =(List<Object>)request.getAttribute("playTypeStrs");
%>
<form name="managerForm" method="post" action="/game/gameAction.do?method=gameOptionList">
	<input type="hidden" value="<%=infoId%>" name="infoId"/>
	<table class="tblist" cellpadding="0" cellspacing="0" border="0">
		<tbody>
			<tr class="moon">
				<td>
					投注项
					<input type="text" name="title" value="<%=title%>"/>
					<input type="submit" class="gbutton" value="查询"/>
				</td>
			</tr>
		</tbody>
	</table>
</form>

<pg:pager 
	url="gameAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="gameOptionList"/>
	<pg:param name="infoId" value="<%=infoId%>"/>
	<pg:param name="title" value="<%=title%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>玩法</th>
		<th>投注项</th>
		<th>倍率</th>
	    <th>描述</th>	   	    
<!-- 	    <th>投注状态</th>	     -->
	    <th>操作</th>
<!-- 	    <th>编辑</th> -->
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
		
		<td align="center">
           	<bean:write name="item" property="playTypeStr" />
	    </td>
	    <td align="center">
            <bean:write name="item" property="title" /> - <bean:write name="item" property="optionTitle" />
	    </td>
	    <td align="center">
	    	<input value="<bean:write name="item" property="betRate"  />" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
	    </td>
	    <td align="center">
            <bean:write name="item" property="hint" />
	    </td>
 	    <td align="center">
 	    	<a href="javascript:void(0)" onclick="saveChange(<bean:write name="item" property="betOptionId" />,this)">保存</a>
<%--             <a href='gameAction.do?method=preModify&infoId=<bean:write name="item" property="infoId"/>'><img class="xtbImg ico-edit" src="../images/modify.png"/></a> --%>
	    </td>
 <!---->
	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html>