<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ include file = "/common/inc_include.jsp"%>
<%@ include file = "/common/inc_pageSetting.jsp"%>
 
<html> 
	<head>
<link type="text/css" rel="stylesheet" href="../style/base.css" media="all">
<link type="text/css" rel="stylesheet" href="../style/navigation.css" media="all">
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
function pageInit(){
	updateLink();
}
function updateLink(){
	var as = document.getElementsByTagName("a");
	if(as){
		var tt = TLT.getTime();
		for(var i=0;i<as.length;i++){
			if(as[i].href && as[i].href.length>5){
				var href = as[i].href;
				as[i].href = href + "&t="+tt;
			}
		}
	}
}

window.onload = pageInit;
</script>

	</head>
 <%
	Integer total=request.getAttribute("total")!=null?(Integer)request.getAttribute("total"):new Integer(0);
	int totalCount = 0;	
	if(total != null)totalCount=total.intValue();	
	String count = String.valueOf(totalCount);
%> 
<body>
<pg:pager 
		url="betSessionAction.do" 
	    index="<%=index%>"
	    items="<%=totalCount%>"
	    maxPageItems="<%= maxPageItems %>"
	    maxIndexPages="<%= maxIndexPages %>"
	    isOffset="<%= isOffset %>"
	    export="offset,currentPageNumber=pageNumber" scope="request"> 
	    <pg:param name="method" value="getBetSessionInfo"/>
	    <pg:param name="bsId" value="${bsId}"/>	

<div id="sub-nav">
	<ul id="userFeedList">
		<li><a href="../xiaonei/p.do?method=init&userId=<bean:write name="userId"/>">璇嶉湼</a></li>
		<li><a href="../xiaonei/p.do?method=more&userId=<bean:write name="userId"/>">宸茬粌杩�</a></li>
		<li><a href="../pce/betSessionAction.do?method=getBetSessionList&userId=<bean:write name="userId"/>">骞歌繍娓告垙</a></li>
		<li class="selected"><a href="#">鑾峰鍚嶅崟</a></li>
		<li><a target="_blank" href="../xiaonei/help/test.htm?" class="help">璇嶉湼璇存槑</a></li>
		<li><a target="_blank" href="../pce/help/game.htm?" class="help">娓告垙璇存槑</a></li>
	</ul>
	
</div>

<div id="single-column">
<form action="">
<input type="hidden" id="userId" value="<bean:write name="userId"/>"/>
<TABLE align="center" border="0" cellpadding="0" cellspacing="0" class="table-list">
	  <TBODY>
	  <tr> <TD colspan="3" align=left height=25><STRONG class=style1>绗�${bsId }鏈熻幏濂栧悕鍗�</STRONG></TD></tr>
	 
	   <TR >
	  
			<TD width="10%" background=<%=request.getContextPath()%>/images/bg.gif height=28>鐢ㄦ埛ID</TD>
			<TD width="15%" background=<%=request.getContextPath()%>/images/bg.gif>鎶曟敞閲戦</TD>
			<TD width="20%" background=<%=request.getContextPath()%>/images/bg.gif>鑾峰緱閲戣泲</TD>

	  </TR>
		
	<logic:iterate id="bs" name="list">	
	<pg:item>
	  <TR  bgColor=#ffffff>
		<TD bgColor=#fffac7 height=28>${bs.userId }</TD>
		<TD bgColor=#ffffff height=28>${bs.totalNumber }<IMG src="<%=request.getContextPath()%>/images/egg.gif"></TD>
		<TD bgColor=#ffffff>${bs.betNumber}<IMG src="<%=request.getContextPath()%>/images/egg.gif"></TD>
	  </TR>
	</pg:item>  
	  </logic:iterate>
		<tr bgColor=#ffffff>
			<td colspan="3">
				<jsp:include page="/template/pagination_template.jsp" flush="true"/>  
			</td>
		</tr>	  
	  </TBODY>
</TABLE>

</form>
</div>

</pg:pager>
</body>
</html>

