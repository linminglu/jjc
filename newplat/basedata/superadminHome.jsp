<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ include file ="../common/inc_include.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>城市管理</title>
<%@ include file = "../common/inc_file.jsp"%>
</head>
<body>
<div class="bread">待办事项</div>


<div>
	<div class="supbox">
		<logic:notEmpty name="cityList">
		<table class="tblist">
		<tr >
	      <th colspan="5" align="center">以下城市还没有录入:</th>      
	    </tr>
		<% List list=(ArrayList)request.getAttribute("cityList"); 
			int size = list.size();        	
	    	int count=0;
	    	int les=0;
		%>
		 <logic:iterate id="item" name="cityList" indexId="number">    
		<%
			if((number.intValue() % 5)==0){	
			 count++;	
		%>	
		     <tr class="tr-color-body">
		<%
			}
		%>
		<%
			les++;
		%> 
			<td>	
				<bean:write name="item" property="content" />
	      	</td>
	     <%	
			if(((number.intValue()-4) % 5) ==0){	    
		%>	 
	     </tr>
	     <%
		   }if((count*5>size)&&(number.intValue()+1==size)){
		     for(int i=0;i<(count*5-size);i++){
		   %>
		    <td></td>
		    <%
		    }
		    %>
		<%}
		%> 
		</logic:iterate>
		</table>
		</logic:notEmpty>
		<logic:empty name="cityList">
			<p style="padding: 20px;">暂无</p>
		</logic:empty>
	</div>
</div>
</body>
</html>