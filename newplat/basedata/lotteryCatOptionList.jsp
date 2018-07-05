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
function del(){
	var frm = document.managerForm;
	var bool = false;
	var sel = frm["selectIndex"];
	for(var i=0;i<sel.length;i++ ){
		if(sel[i].checked){
			bool = true;
		}
	}
	if( !bool ){
		alert( "请选择删除对象" );
		return false;
	}
	if( confirm( "你确认要删除吗?" )){
		return true;
	}
	return false;
}

function delUser(url,loginName){
	if(loginName && (loginName=="admin" || loginName=="cuisy" || loginName=="toefladmin" || loginName=="genadmin" 
		 	|| loginName=="testadmin" || loginName=="examadmin"
			|| loginName=="satadmin" || loginName=="ieltsadmin" || loginName=="greadmin" || loginName=="gmatadmin")){
		alert("不能删除此管理账号！");
		return false;
	}
	if(confirm("确认删除用户?")){
		location.href = url;
	}
	
}
function selectAll(flag){
	var selAll = document.managerForm;
	for( var i=0;i<selAll.elements.length;i++ ){
		if( selAll.elements[i].name == 'selectIndex' ){
			selAll.elements[i].checked = flag;
		}
	}
}
// function addOption(){
// 	var gasInfoId = document.getElementById("gasInfoId");
// 	location='baseDataAction.do?method=lotteryCatOptionPreAdd&gasInfoId='+gasInfoId.value;
// }
function delItem(id){
	if(confirm('确实要删除吗？')){
		$.post("baseDataAction.do?method=lotteryCatOptionDel&id="+id,
			function(data) {
				data = $.parseJSON(data);
				if(data && data.code=='200') {
					location.reload();
					alert("删除成功！");
					var frm = document.baseDataForm[0];
					var infoId = frm['gasInfo.infoId'].value;
					location='baseDataAction.do?method=lotteryCatOptionList&infoId='+infoId;
				}else{
// 					alert("删除失败！\n\n提示：数据已应用则不能删除！");
					alert(data.msg);
				}
		});	
	}
}
function edit(id){
	var frm = document.baseDataForm[0];
	var infoId = frm['gasInfo.infoId'].value;
	location='baseDataAction.do?method=lotteryCatOptionPreAdd&betOptionId='+id+"&infoId="+infoId;
}

</script>
</head>
<body>
<div class="bread"><c:out value='${gasTitle}'/> &raquo;玩法</div>
<html:form action="/baseDataAction.do?method=lotteryCatOptionList" method="post">
<html:hidden name="baseDataForm" property="gasInfo.infoId"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			玩法名称
			<html:text property="userName" name="baseDataForm"/>
			<input type="submit" class="gbutton" value="查询" />
			<input type="button" class="gbutton" value="添加" onclick="location='baseDataAction.do?method=lotteryCatOptionPreAdd&infoId=<c:out value='${gasInfoId}'/>';"/>
		</td>
	</tr>
</table>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userType =request.getAttribute("userType")!=null?(String)request.getAttribute("userType"):"";
String category =request.getAttribute("category")!=null?(String)request.getAttribute("category"):"";
String agentId =request.getAttribute("agentId")!=null?(String)request.getAttribute("agentId"):"0";
%>
<pg:pager 
	url="baseDataAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="lotteryCatOptionList"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<html:form action="/baseDataAction.do?method=delete" onsubmit="return del();">
<bean:size id="length" name="lotteryCatOptionList"/>
<logic:notEmpty name="lotteryCatOptionList">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
<!-- 		<th>ID</th> -->
		<th>名称</th>
	    <th>下注类型</th>
	    <th>赔率</th>	   	    
	    <th>游戏类型</th>
    	<th>玩法类型</th>
    	<th>名称</th>
    	<th>积分倍数</th>	
    	<th>操作</th>	
    	
	</tr>
	<logic:iterate id="item" name="lotteryCatOptionList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="optionTitle" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="optionType"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="betRate"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="gameType"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="playType"/>
	    </td>
<!-- 		<td align="center"> -->
<%-- 	      	<bean:write name="item" property="endTime" format="yyyy-MM-dd HH:mm:ss"/> --%>
<!-- 		</td> -->
<!-- 	    <td align="center"> -->
<%-- 	        <img style="width:30%;" src="<bean:write name="item" property="img"/>" alt="" /> --%>
<!-- 	    </td> -->
	    <td align="center">
	        <bean:write name="item" property="title"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="pointMultiple"/>
	    </td>
	    <td align="center">
	     	<a class="button" href="javascript:void(0)" onclick="delItem(<bean:write name="item" property="betOptionId"/>)">删除</a>
	        <a class="button" href="javascript:void(0)" onclick="edit(<bean:write name="item" property="betOptionId"/>)">修改</a>
	    </td>
<!-- 		<td align="center"> -->
<%-- 	      	<bean:write name="item" property="registDateTime" format="yyyy-MM-dd"/> --%>
<!-- 		</td> -->

	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="lotteryCatOptionList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

</html:form>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html>