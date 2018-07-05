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
<script type="text/javascript">

/**
 * 修改
 */
function save(){
   var bindId=getRadioBoxValue('bindId');
     
	if(bindId=='undefined'){
		alert("请选择一个账户");
		return ;
	}
	document.forms[0].action = "cashAction.do?method=saveDistribut&id="+bindId;
	document.forms[0].submit();

}


function   getRadioBoxValue(radioName) 
{ 
            var obj = document.getElementsByName(radioName);  //这个是以标签的name来取控件
                 for(i=0; i<obj.length;i++)    {

                  if(obj[i].checked)    { 
                          return   obj[i].value; 
                  } 
              }         
             return "undefined";       
}

function del(id){
	if(confirm('确实要删除吗？')){
		document.forms[0].action = "cashAction.do?method=del&id="+id;
		document.forms[0].submit();
	}
}
</script>
</head>
<body>
<logic:present name="msg">
</logic:present>
<div class="bread">账户管理  &raquo; 银行卡列表</div>


<html:form action="/cashAction.do?method=saveDefBind">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
				<input type="button" class="gbutton" value="添加" onclick="location='cashAction.do?method=preAdd';"/>
		</td>
	</tr>
</table>

<%int number=1;	%>
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
    	<!-- <th style="width:50px;text-align:center;">选择</th> -->
		<th class="sel">序号</th>
		<th>开户行名字</th>
	    <th>账号</th>
	    <th>户主姓名</th>
	    <th>是否默认账户</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">

		<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	   <%-- <td>
		   	<c:if test = "${item.isDef == 1}">
		     	<input name="bindId"  id="bindId" type="radio"  value="<c:out value="${item.bankBindId}"/>"  checked="checked" />
		     </c:if>
		     <c:if test = "${item.isDef != 1}">
		     	<input name="bindId"  id="bindId" type="radio"  value="<c:out value="${item.bankBindId}"/>"  />
		     </c:if>
	   </td> --%>
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
           <c:out value="${item.bankName}"/>
	    </td>
	    <td align="center">
           <c:out value="${item.bindAccount}"/>
	    </td>
	   <td align="center">
           <c:out value="${item.bindName}"/>
	    </td>
	    <td align="center">
	    <c:if test = "${item.isDef == 1}">
		     	是
		 </c:if>
		  <c:if test = "${item.isDef == 0}">
		     	否
		 </c:if>
		</td>
		<td align="center">
			<a href="javascript:del(<bean:write name="item" property="bankBindId"/>);"><img class="xtbImg ico-del" src="../images/trash.png"/></a>
		</td>
	</tr>
	</logic:iterate>
</table>
<!-- <div class="div-bottom-bar">
		<input type="button" class="gbutton" value="保存"  onclick="save();"/>
		<input style="" class="gbutton" onclick="location.href='javascript:history.go(-1);'" value="返回"/>
</div> -->
</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>
</html:form>
</body>
</html:html>