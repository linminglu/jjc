<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@ taglib uri="/content" prefix="content"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript" src="../js/platjs.js"></script>
<script type="text/javascript">
function del(id,infoType){
	if(confirm("您确定要删除这条数据吗？")){
		document.forms[0].action = "cashAction.do?method=delBank&id="+id;
		document.forms[0].submit();
	}
}
function release(id,stat){
	var info;
	if(stat=='1'){
		info = "您确定要打开吗？";
	}else{
		info = "您确定要关闭吗？";
	}
	if(confirm(info)){
		window.location.href='cashAction.do?method=release&id='+id+"&stat="+stat;
	}
}
function changeSchedule(typeId){
	var sta;
	if(typeId != ""){
		sta = 1
	}
	var url = "cashAction.do?";
	var pars = {method:"findMytType",typeId:typeId,sta:sta,rm:(new Date()).getTime()}
	var par = $H(pars).toQueryString();
	
    var request = new Ajax.Request(url+par,{method:"get",parameters:par,onComplete:changeTypeList});
		
}

function changeTypeList(request){ 
	var shijiSelect = document.all('spTypeId');
	shijiSelect.innerHTML="";
	var obj = eval(request.responseText);
	if(obj.length > 0){
	 	var option0 = document.createElement("OPTION");
	 	 option0.innerHTML = "全部";
	     option0.value = "";
		 shijiSelect.appendChild(option0);
		for(var i=0;i<obj.length;i++){
			var option = document.createElement("OPTION");
	        	option.innerHTML = obj[i].title;
	       		option.value = obj[i].typeId;
	  			shijiSelect.appendChild(option);   
		}
	}
}
/**
 * 排序
 * @param pid 数据id
 * @param sortType 排序类型 1升序 2降序
 * @param typeLe 那种排序，2.商品排序 4.热卖排序
 */
function sort(pid,sortType,typeLe){
	if(confirm('确认此操作?')){
		document.forms[0].action = "cashAction.do?method=orderPx&pid="+pid+"&sortType="+sortType+"&typeLe="+typeLe;
		document.forms[0].submit();
		//导出完毕把action还原
		document.forms[0].action = "cashAction.do?method=initBank"; 
	}
}
</script>
</head>
<body>
<div class="bread">银行管理 &raquo;银行信息 </div>
<html:form method="post" action="/cashAction.do?method=initBank" >
<input type="hidden" name="pager.offset" value="<c:out value="${startIndex}"/>"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			 银行：
			 <html:text property="bankName" name="cashForm"/>
			&nbsp;<input type="submit" class="gbutton" value="查询" />
			&nbsp;<input type="button" class="gbutton" value="添加" onclick="location='cashAction.do?method=preAddBank&action=add';"/>
		</td>
	</tr>
</table>
<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String status=(String)request.getAttribute("status");

%>
<pg:pager 
	url="cashAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="initBank"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="status" value="<%=status%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
<bean:size id="length" name="list"/>
	<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
			<th>银行</th>
			<th>账号</th>
			<th>姓名</th>
			<th>国家</th>
	    	<th>操作</th>
	    </tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	 		<%-- <td><a href='cashAction.do?method=view&id=<c:out value="${item.id}"/>'><content:out maxLength="20"><c:out value="${item.title}"/></content:out></a></td> --%>
 		<td><bean:write name="item" property="bankName"/></td>
 		<td><bean:write name="item" property="bankAccount"/></td>
 		<td><bean:write name="item" property="userName"/></td>
 		<td><bean:write name="item" property="country"/></td>
	   	<td>
	   		<a href='cashAction.do?method=preAddBank&id=<bean:write name="item" property="bankId"/>&action=modif'>
	   			<img class="xtbImg ico-edit" src="../images/modify.png"/>
	   		</a>
	   		<a href='javascript:void(0);' onclick='del(<bean:write name="item" property="bankId"/>);'><img class="xtbImg ico-del" src="../images/trash.png"/></a>
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
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
</html:form>
</body>
</html>