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
function saveChange(id,th){
	var t = $(this);
// 	console.log(th);
	var t2 = $(th).parent().parent();
	var inp = t2.find(":input");
	var val = inp.val();
	if(val&&val.length>0&&val>0&&id){
		if(!isNaN(val)){
			$.post("baseDataAction.do?method=changeBetRate&infoId="+ id +"&rate="+val, 
				function(data) {
					var result = JSON.parse(data);
					var code = result.code;
					if(code==200){
						$("input[data='"+result.id+"']").val(result.rate);
						alert("修改成功");
					}else{
						alert("网络错误，请重试");
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
<div class="bread"><a href="baseDataAction.do?method=lotteryCatList" >彩种管理</a>-<c:out value="${gameTitle}"/>
</div>
<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String infoId =request.getAttribute("infoId")!=null?(String)request.getAttribute("infoId"):"0";
String title =request.getAttribute("title")!=null?(String)request.getAttribute("title"):"";
List<Object> playTypeStrs =(List<Object>)request.getAttribute("playTypeStrs");
%>
<form name="managerForm" method="post" action="baseDataAction.do?method=gameOptionList">
	<input type="hidden" value="<%=infoId%>" name="infoId"/>
	<table class="tblist" cellpadding="0" cellspacing="0" border="0">
		<tbody>
			<tr class="moon">
				<td>
					玩法
					<select id="playType" name="playType">
						<option value="">全部</option>
						<c:forEach items="${playTypeStrs}" var="tq" varStatus="status">
							<c:if test="${cuPlayType==status.count-1 }">
								<option selected="selected" value="<c:out value="${ status.count -1 }" />"><c:out value="${tq}" /> </option>
							</c:if>
							<c:if test="${cuPlayType != status.count-1}">
								<option value="<c:out value="${ status.count -1 }" />"><c:out value="${tq}" /> </option>
							</c:if>
						</c:forEach>
					</select>
					投注项
					<input type="text" name="title" value="<%=title%>"/>
					<input type="submit" class="gbutton" value="查询"/>
					<input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
				</td>
			</tr>
		</tbody>
	</table>
</form>
	
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>玩法</th>
		<th>投注项</th>
		<th>倍率</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
		
		<td align="center">
           	<bean:write name="item" property="playTypeStr" />
	    </td>
	    <td align="center">
            <bean:write name="item" property="title" /> - <bean:write name="item" property="optionTitle" />
	    </td>
	    <td align="center">
	    	<input data="<bean:write name="item" property="betOptionId" />" value="<bean:write name="item" property="betRate"  />" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
	    </td>
 	    <td align="center">
 	    	<a href="javascript:void(0)" onclick="saveChange(<bean:write name="item" property="betOptionId" />,this)">保存</a>
	    </td>
	</tr>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<div class="div-bottom-bar">
<!-- 	<input type="submit" class="gbutton" value="确定"/> -->
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</body>
</html>