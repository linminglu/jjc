<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="/common/inc_include.jsp"%>  
<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ page  import="java.util.*"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ page import="com.ram.model.UserGroup"%>
<%@ page import="com.card.model.Card"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>礼品卡列表</title>
        <%@ include file = "../common/inc_file.jsp"%>
	
</head>
<%
UserGroup userGroup=(UserGroup)session.getAttribute("loginUserGroup");
Card cardQuery=(Card)request.getAttribute("card");
int totalCount = ((Integer)request.getAttribute("count")).intValue();
String count = String.valueOf(totalCount);
String cardStatus = cardQuery==null?"":cardQuery.getCardStatus();
String cardType = cardQuery==null?"":cardQuery.getCardType();
String cardCode = cardQuery==null?"":cardQuery.getCardCode();
String protocolCode = cardQuery==null?"":cardQuery.getProtocolCode();
String batchCode = cardQuery==null?"":cardQuery.getBatchCode();
String startDate = (String)request.getAttribute("startDate");
String endDate = (String)request.getAttribute("endDate");

 %>
<body onload1="setListDivEvent();"> 
<!-- 
<H1>
	<SPAN class=action-span><A href="cardAction.do?method=cardGenerate">生成学习卡</A> </SPAN>
	<SPAN class=action-span1><A href="#"> </A> </SPAN><SPAN
		class=action-span1 id=search_id>- </SPAN>
	<DIV style="CLEAR: both"></DIV>
</H1> -->
<div class="bread">管理中心  &raquo; 礼品卡列表</div>
<html:form action="cardAction.do?method=initCardGift" method="post">
	<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon"><td>
		协议号：<html:text property="card.protocolCode" maxlength="6" size="10"/>
		批次号：<html:text property="card.batchCode" maxlength="6" size="10"/>
			&nbsp;&nbsp;
			状态：<html:select property="card.cardStatus">
					<html:option value="">全部</html:option>
					<html:option value="1">未激活</html:option>
					<html:option value="2">已激活</html:option>
					<html:option value="0">已注销</html:option>
			    </html:select>
			&nbsp;&nbsp;
		  套餐： <html:select property="itemId" name="cardForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:options collection="cardItemList" property="itemId" labelProperty="title"/>
			</html:select>
			&nbsp;&nbsp;
			卡号：<html:text property="card.cardCode"/>
			截止有效期查询：<input type="text" name="startDate"  size="15"
								onclick="if(self.gfPop)gfPop.fPopCalendar(document.cardForm['startDate']);return false;"
								value="<bean:write name="cardForm" property="startDate" format="yyyy-MM-dd"/>" />
								—
							<input type="text" name="endDate"  size="15"
								onclick="if(self.gfPop)gfPop.fPopCalendar(document.cardForm['endDate']);return false;"
								value="<bean:write name="cardForm" property="endDate" format="yyyy-MM-dd"/>" />

		<input type="button" value="查询" class="gbutton" onclick="_query()">
		<input type="button" class="gbutton" value="生成礼品卡" onclick="location='cardAction.do?method=cardGenerate';"/>
        <input type="button" value="注销" class="gbutton" onclick="removeCard(this.form)">
		<a href="#" onclick=" exportExcel()" class="gbutton">导出excel</a>	
				
				
			
			
	</tr></table>
<bean:define id="totalLength" value="<%=count%>"/>
<input type="hidden" name="userId" value="<%=request.getAttribute("userId")%>"> 
<center>  
<pg:pager
    	url="cardAction.do"
	    items="<%=totalCount%>"
	    index="<%=index%>"
	    maxPageItems="<%=maxPageItems%>"
	    maxIndexPages="<%=maxIndexPages%>"
	    isOffset="<%= isOffset %>"
	    export="offset,currentPageNumber=pageNumber"
	    scope="request">
	<pg:param name="method" value="initCardGift"/>
	<pg:param name="card.cardStatus" value="<%=cardStatus %>"/>
	<pg:param name="card.cardType" value="<%=cardType %>"/>
	<pg:param name="card.cardCode" value="<%=cardCode %>"/>
	<pg:param name="card.protocolCode" value="<%=protocolCode %>"/>
	<pg:param name="card.batchCode" value="<%=batchCode %>"/>

	<pg:param name="startDate" value="<%=startDate %>"/>
	<pg:param name="endDate" value="<%=endDate %>"/>
		<table class="tblist" cellSpacing="1" cellPadding="2" width="100%">
			<tr >
			    <th width="5%"><input type="checkbox" name="all" onclick="checkAll(this, 'cardIds')"/>全选</th> 
				<th>卡号</th>
				<th>密码</th>
				<th>面额</th> 
				<th>起始有效期</th>
				<th>截止有效期</th>
				<th>卡状态</th>
			
			</tr>
		
			<logic:present name="cardList">
			
			   <logic:iterate id="itemInfo" name="cardList" indexId="num">
					<tr  onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
					<td align="center"> 
					   <c:if test="${itemInfo.cardStatus=='1'}"> <input type="checkbox" name="cardIds" onclick="check(this)"  value="<c:out value="${itemInfo.cardId}"/>"/></c:if>
					   <c:if test="${itemInfo.cardStatus=='0' || card.cardStatus=='2'}"> <input type="checkbox" name="cardIds" onclick="check(this)" disabled="disabled" value="<c:out value="${card.cardId}"/>"/></c:if>					    
					    </td>
						<td  align="center"><a href="cardAction.do?method=cardDetail&cardCode=<bean:write name="itemInfo" property="cardCode"/>" ><bean:write name="itemInfo" property="cardCode"/></a></td> 
						<td  align="center"><bean:write name="itemInfo" property="cardPwd" /></td>
						<td  align="center"><bean:write name="itemInfo" property="cardAmount" /></td>
						<!--  <td  align="center">
						   <logic:equal value="1" property="cardType" name="itemInfo">课程学习卡</logic:equal>
						   <logic:equal value="2" property="cardType" name="itemInfo">礼品学习卡</logic:equal>
						</td>-->
						<td  align="center"><bean:write name="itemInfo" property="startDate" format="yyyy-MM-dd"/></td>
						<td  align="center"><bean:write name="itemInfo" property="endDate" format="yyyy-MM-dd"/></td>
						
						<td  align="center">
						   <logic:equal value="0" property="cardStatus" name="itemInfo"><font color="gray">已注销</font></logic:equal>
						   <logic:equal value="1" property="cardStatus" name="itemInfo"><font color="red">未激活</font></logic:equal>
						   <logic:equal value="2" property="cardStatus" name="itemInfo"><font color="green">已激活</font></logic:equal>
						</td>
						<c:if test="${card.cardType == '2'}">
						<td><bean:write name="itemInfo" property="cardAmount"/></td>
						</c:if>
					</tr>
				</logic:iterate> 
			</logic:present>
			
			
		
			
		    <logic:greaterThan name="totalLength" value='<%=maxPageItems + ""%>'>
		    	<jsp:include page="../template/pagination_template.jsp" flush="true"/>
			</logic:greaterThan>
		</table>
		
	<div id="shouquan" style="display:none" >
		<table id=general-table width="90%" align=center>
			<tr class="tr-color-body">
				<td class=label>请输入授权码:</td><td align="left"><input type="password" id="key" name="key" value=""/>&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="gbutton" onclick="checkRight()" value="确定"></td>
			</tr>
		</table>
	</div>
</pg:pager>
</center>
<iframe width="174" height="189" name="gToday:normal:agenda.js"
					id="gToday:normal:agenda.js" src="<c:url value="/js/ipopeng.htm"/>"
					scrolling="no" frameborder="0"
					style="visibility:visible; z-index:999; position:absolute; left:-500px; top:0px;"></iframe>
</html:form>

<script type="text/javascript">
 var flag=false;

 function checkAll(e, itemName){  
    var aa = document.getElementsByName(itemName);
    for (var i=0; i<aa.length; i++)  
     aa[i].checked = e.checked;
     flag=true;
 }  
 
 function check(e){
    if(e.checked){
     flag=true;
    }else{
     flag=false;
    }
 }
 function _query(){
 	document.forms[0].action = "cardAction.do?method=initCardGift";
    document.forms[0].submit();
 }
 function cardDetail(cardCode){
 	window.open('cardAction.do?method=cardDetail&cardCode='+cardCode);
 }
 function removeCard(thisForm){
 	if(!checkAtLeast()){
 		alert("请至少选择一项");
 		return false;
 	}
 	if(confirm("学习卡一旦注销后将不能再使用，您确定继续该操作吗？")){
	  thisForm.action = 'cardAction.do?method=remove';
	  thisForm.submit();
 	}
 }
 function checkAtLeast(){
	 var items = document.getElementsByName("cardIds");
	 for(var i=0;i<items.length;i++){
		 if(items[i].checked==true){
			 return true;
		 }
	 }
	 return false;
 }
 function toCheckRight(){
	document.getElementById("shouquan").style.display="";
	document.getElementById("listDiv").style.display="none";
	document.getElementById("form-div_id").style.display="none";
 }
 function exportExcel(){
	document.forms[0].action = "cardAction.do?method=cardExport";
	document.forms[0].submit();
 }
 	function checkRight() {  
        var key = document.getElementById("key").value;
	 	if(key == null || key == ""){
	 		alert("请输入授权码。");
	 		document.getElementById("key").focus();
	 		return false;
	 	}
//	    var url = "cardAction.do?";    
//	    var mailAjax = new Ajax.Request(    
//	         url,
//	         {    
//	             method: 'get',
//	             dataType:"json",
//	             parameters:'method=checkRight&key='+key, 
//	             onComplete: jsonResponse    
//	         });
	    $.post("cardAction.do?method=checkRight&key="+key+"&t="+new Date().getTime(), 
	       		function(data){
	    			jsonResponse(data);
	       		});
		}
				   
	function jsonResponse(data) {
	     var obj = jQuery.parseJSON(data);//eval(request.responseText);
	    if(obj.message=='dui'){
	         document.getElementById("shouquan").style.display='none';
			 document.getElementById("listDiv").style.display="";
			 document.getElementById("form-div_id").style.display="";
			 exportExcel();
	    }else if(obj.message == 'cuo'){
	         alert("授权码错误，请重新输入");
	    }else if(obj.message=='noAdmin'){
	          alert("您没有管理员权限");
	    }else{
	       alert("提交失败，请重试");
	    }
	}
 
</script>
</body>
</html:html>