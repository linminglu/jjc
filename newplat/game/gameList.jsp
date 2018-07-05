<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
function showOptions(id){
	window.location = "../../game/gameAction.do?method=gameOptionList&infoId="+id;
}
</script>
</head>
<body>
<div class="bread">彩种管理
</div>

<html:form action="/gameAction.do?method=gameList">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			彩票名称
			<html:text property="title" name="gameForm"/>
			彩票类型
			<html:select property="playCate" name="gameForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">官方</html:option>
				<html:option value="2">信用</html:option>
			</html:select>
			显示状态
			<html:select property="status" name="gameForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">有效</html:option>
				<html:option value="0">无效</html:option>
			</html:select>
			投注状态
			<html:select property="betAvoid" name="gameForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">有效</html:option>
				<html:option value="0">无效</html:option>
			</html:select>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>

<%
//分页
String totalCount = request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String title = (String)request.getAttribute("title");
String playCate = (String)request.getAttribute("playCate");
String status = (String)request.getAttribute("status");
String betAvoid = (String)request.getAttribute("betAvoid");
%>
<pg:pager 
	url="gameAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="gameList"/>
	<pg:param name="title" value="<%=title%>"/>
	<pg:param name="playCate" value="<%=playCate%>"/>
	<pg:param name="status" value="<%=status%>"/>
	<pg:param name="betAvoid" value="<%=betAvoid%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>类型</th>
		<th>彩票名称</th>
	    <th>显示状态</th>	   	    
	    <th>投注状态</th>	    
	    <th>操作</th>
<!-- 	    <th>编辑</th> -->
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
		<td align="center">
             <c:if test = "${item.playCate eq '1'}">
		  		官方
		  	</c:if>
		    <c:if test = "${item.playCate eq '2'}">
		  		信用
		  	</c:if>
	    </td>
	    <td align="center">
            <bean:write name="item" property="gameTitle" />
	    </td>
		<td align="center" id="infoId<bean:write name="item" property="infoId"/>">
			<logic:equal name="item" property="status" value="1">
				<font color="green"><strong>有效</strong></font>&nbsp;
				<a class="button" onclick="changeStatus(<bean:write name="item" property="infoId"/>,'1');" href='javascript:void(0);'>关</a>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="status" value="0">
				<font color="red"><strong>无效</strong></font>&nbsp;
				<a class="button" onclick="changeStatus(<bean:write name="item" property="infoId"/>,'0');" href='javascript:void(0);'>开</a>&nbsp;
			</logic:equal>
		</td>
		<td align="center" id="betAvoid<bean:write name="item" property="infoId"/>">
			<logic:equal name="item" property="betAvoid" value="1">
				<font color="green"><strong>有效</strong></font>&nbsp;
				<a class="button" onclick="changeBetAvoid(<bean:write name="item" property="infoId"/>,'1');" href='javascript:void(0);'>关</a>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="betAvoid" value="0">
				<font color="red"><strong>无效</strong></font>&nbsp;
				<a class="button" onclick="changeBetAvoid(<bean:write name="item" property="infoId"/>,'0');" href='javascript:void(0);'>开</a>&nbsp;
			</logic:equal>
		</td>
		<td align="center" id="betAvoid<bean:write name="item" property="infoId"/>">
			<a class="button" onclick="showOptions(<bean:write name="item" property="infoId"/>);" href='javascript:void(0);'>投注项管理</a>&nbsp;
<!-- 			<a class="button"  href='../../game/gameAction.do?method=gameOptionList'>投注项管理</a>&nbsp; -->
		</td>
<!-- 	    <td align="center">
            <a href='gameAction.do?method=preModify&infoId=<bean:write name="item" property="infoId"/>'><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
	    </td>
 -->
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