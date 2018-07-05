<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include file = "../common/inc_ueditor.jsp"%> 
<!-- <script language="javascript" src="../js/validate.js"></script> -->
<!-- <script language="javascript" src="../js/show.js"></script> -->
<script type="text/javascript" src="<%=webContext %>/js/js_z/md5.js"></script>
<script language="javascript">
function xmlHttpRequest(){
	if( window.ActiveXObject ){
		return new ActiveXObject("Microsoft.xmlhttp");
	}else if( window.XMLHttpRequest ){
		return new XMLHttpRequest();
	}
}

function formSubmit(){
	var frm = document.newsForm;
	if(frm["newsInfo.title"].value.trim().length==0){
		alert("请输入标题");
		frm["newsInfo.title"].focus();
		return false;
	}
	
 	if(frm["newsInfo.author"].value.length==0){
 		alert("请输入作者");
 		frm["newsInfo.author"].focus();
 		return false;
 	}
	
 	if(frm["newsInfo.company"].value.length==0){
 		alert("请选择机构");
 		return false;
 	}
 	
	var check=document.getElementsByName("ids"); 
	var temp=false;
	for(var i=0;i <check.length;i++){ 
		if(check[i].checked==true){ 
			temp=true; 
		} 
	}
 
	 if(frm["tid"].value.length==0){
	 	alert("请选择新闻类别");
	 	frm["tid"].focus();
	 	return false;
	 }

	var flag=false;
	$('input[name="ids"]').each(function(){    
		if($("input[type='checkbox']").is(':checked')){
			flag=true;
		}
	});
	 if(!flag){
		 	alert("请选择资讯种类");
		 	return false;
	 }
	
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}

$(document).ready(function(){
	var pids=$('#pids').val();
	if(pids!=null&&pids!=""){
		var array=pids.split(",");
		for(var i=0;i<array.length;i++){
			$('input[name="ids"]').each(function(){    
				var id=$(this).val();
				if(id==array[i]){
					$(this).attr("checked", true);
				}
			});
		}
	}
});
</script>
</head>
<body>
<div class="bread">新闻资讯  &raquo; 
	<c:if test="${action eq 'add'}">新增</c:if>
	<c:if test="${action eq 'modify'}">修改</c:if>
</div>
<html:form action="/newsAction.do?method=saveNews" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="newsForm" property="newsInfo.nid"/>
<input type="hidden" name="pids" id="pids" value="<c:out value='${pids}'/>"></input>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">标题</th>
		<td>
        	<html:text name="newsForm" property="newsInfo.title" />
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">副标题</th>
      	<td>
      		<html:text name="newsForm" property="newsInfo.subTitle" />
      		<p class="red">可不填</p>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">前缀分类</th>
      	<td>
       		<html:text name="newsForm" property="newsInfo.type" maxlength="20"/>
       		<p class="red">建议两个字，例如：新闻，中奖，推荐，分析，技巧，预测等</p>
      	</td>
    </tr>

	<tr class="tr-color-body">
      	<th align="right">作者</th>
      	<td>
       		<html:text name="newsForm" property="newsInfo.author" maxlength="50"/>
       		<p class="red">如无可以写：admin</p>
      	</td>
    </tr>
   
   	<tr class="tr-color-body">
      	<th align="right">发布机构</th>
      	<td>
       		<html:text name="newsForm" property="newsInfo.company" maxlength="50"/>
       		<p class="red">可以写xx机构，或者公司名字（为了排版建议不要超过6个字）</p>
      	</td>
    </tr>
    
   <tr class="tr-color-body">
      	<th align="right">新闻类别</th>
      	<td>
      		<html:select property="tid" name="newsForm" >
      		<html:option value="">请选择</html:option>
      			<logic:notEmpty name="typeList">
      				<html:options property="tid" collection="typeList" labelProperty="title"/>
      			</logic:notEmpty>
      		</html:select>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">资讯种类</th>
      	<td id="typeList">
      	<logic:notEmpty name="cateList">
      		<table align="center" border="0" cellpadding="0"  class="table-color" >
      			<%List list = (ArrayList)request.getAttribute("cateList"); 
			    	int size = list.size();        	
			    	int count=0;
			    %>
			    <logic:iterate id="item" name="cateList" indexId="number" >  
				<%
					if((number.intValue() % 3)==0){	
					 count++;	
				%>    
				     <tr class="tr-color-body">
				<%
				}
				%>
				      <td>
			           		<input type="checkbox"  name="ids" value="<bean:write name='item' property='cid'/>"   id="id<bean:write name='item' property='cid'/>"/>
					        <label for="id<bean:write name='item' property='cid'/>" class="cursor"> <bean:write name="item" property="title"/></label> 
				      </td>
				<%	
					if(((number.intValue()-2) % 3) ==0){	    
				%>
				  </tr>
				<%
				   }if((count*3>size)&&(number.intValue()+1==size)){
				     for(int i=0;i<(count*3-size);i++){
				   %>
				    <td></td>
				    <%
				    }
				    %>
				</tr>
				<%}
				%> 
			 </logic:iterate>
      		</table>
      		</logic:notEmpty>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">内容&nbsp;</th>
	      	<td>
				<textarea id="newsInfo.content" name="newsInfo.content" style="width:650px;" class="editortextareas"><bean:write name="newsForm" property="newsInfo.content"/></textarea>
				<script type="text/javascript">UECreator.def("newsInfo.content",0,350,false);</script>
	      	</td>
    </tr>
    
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>


</html:form>
</body>
</html:html>