<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
<%@ include file = "../common/inc_file.jsp"%>
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="../js/js_z/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/js_z/dialog.js"></script>
<script>
function justify(id){
	var value= $("#openresult").val();
	if(value!=null&&value!=""&&value.trim()!=""){
		 if(confirm('确实要保存结果吗？结果：'+value)){
				$.post("bj3Action.do?method=saveOpenResult&sessionId="+id+"&openResult="+value, 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
								location.reload();
								alert("保存成功！");	
							}else{
								alert("保存失败！");	
							}
				});
		}
	}else{
		alert("请输入开奖结果");
	}
}

function open(id){
	var value= $("#openresult").val();
	if(value!=null&&value!=""&&value.trim()!=""){
		 if(confirm('确实要开奖吗？')){
				$.post("bj3Action.do?method=openResult&sessionId="+id+"&openResult="+value, 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
								location.reload();
								alert("开奖成功！");	
							}else{
								alert("开奖失败！");	
							}
				});
		}
	}else{
		alert("请输入开奖结果");
	}
}

function search(){
	var start= $("#start").val();
	var end = $("#end").val();
	if(start!=null&&start!=""&&start.trim()!=""&&end!=null&&end!=""&&end.trim()!=""){	
	    $.post("bj3Action.do?method=init&start="+start+"&end="+end, 
			 function(data) {
				 data = $.trim(data);
				 if (data == "success") {
					 location.reload();
					 console.log("查询成功！");	
				 }else{
					 console.log("查询失败！");	
				 }
			 });
	}else{
		alert("请输入查询期数");
	}

}

</script>
</head>
<body>
<html:form action="/bj3Action.do?method=init">
<html:hidden property="startIndex" name="bj3Form"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
    		期号:<html:text property="sessionNo"/>	
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>

<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String sessionNo =(String)request.getAttribute("sessionNo");
%>
<pg:pager 
	url="bj3Action.do"
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="init"/>
	<pg:param name="sessionNo" value="<%=sessionNo%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>

<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>期号</th>
	    <th>开始时间</th>
	    <th>结束时间</th>
	    <th>结果</th>
	    <th>操作</th>
	</tr>
    <logic:iterate id="bj3" name="list" indexId="num">
    	<pg:item>
    <tr>
      <td class="numbers"><%=num.intValue()+1 %></td>
      <td>
        <bean:write name="bj3" property="sessionNo" />
      </td>
      <td>
        <bean:write name="bj3" property="startTime"  format="yyyy-MM-dd HH:mm:ss"/>
      </td>
      <td>
        <bean:write name="bj3" property="endTime"  format="yyyy-MM-dd HH:mm:ss"/>
      </td>
      <td>
        <input  id="openresult<bean:write name="bj3" property="sessionId"/>"  name="resultNo"  value="<bean:write name="bj3" property="openResult"/>"  size="30" /><p class="red">每个数字之间用逗号隔开。 例如：1,2,5,6,8</p>
      </td>
      <td>
        <a href="javascript:justify(<bean:write name="bj3" property="sessionId"/>);"  >保存结果</a>
<%-- 		<c:if test="${bj3.openStatus !='2'}">
				<a href="javascript:open(<bean:write name="bj3" property="sessionId"/>);">开奖</a>
			</c:if>--%>	
      </td>
    </tr>
    	</pg:item>
    </logic:iterate>
</table>
</logic:notEmpty>
<logic:empty name="managerList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="/template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html:html>