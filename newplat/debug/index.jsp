<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="tools.debug.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Debug</title>
</head>
<link rel="stylesheet" href="debug.css" type="text/css" />
<script type="text/javascript" src="debug.js"></script>
<script>
function setPara(){

 var mode = document.getElementById("mode").value;
 var key = document.getElementById("key").value;
 var device = document.getElementById("device").value;
 var ip = document.getElementById("ip").value;
 var filter = document.getElementById("filter").value;
 window.location="?action=set&mode="+mode+"&key="+key+"&device="+device+"&ip="+ip+"&filter="+filter;
}
function clearLog(){
 window.location="?action=clear";
}
function init(){
document.getElementById("mode").value="<%=DebugConstants.debugMode%>";
document.getElementById("key").value="<%=DebugConstants.debugKey%>";
document.getElementById("device").value="<%=DebugConstants.debugDevice%>";
document.getElementById("ip").value="<%=DebugConstants.debugIP%>";

}
</script>
<body style="padding:0;margin:0;" onload="init();">
<%
	String action = request.getParameter("action");

out.println("<div>debugMode:"+DebugConstants.debugMode+"</div>");
out.println("<div>debugDevice:"+DebugConstants.debugDevice+"</div>");
out.println("<div>debugIP:"+DebugConstants.debugIP+"</div>");
out.println("<div>key:"+DebugConstants.debugKey+"</div>");
out.println("<div>deviceIp:"+request.getHeader("x-forwarded-for")+"</div>");
out.println("<div>agent:"+request.getHeader("User-Agent")+"</div>");

out.println("<div>--------------------------------------</div>");
if(action!=null && action.equals("clear")){
	DebugUtil.writeTextToFile("",DebugConstants.getWebRootPath()+"/debug/d.log",false);
	response.sendRedirect("../debug");
}else if(action!=null && action.equals("set")){
	String debugMode = request.getParameter("mode");
    //request.setAttribute("mode",debugMode);
	if(debugMode!=null) DebugConstants.debugMode = debugMode.equals("true")?true:false;
	String debugKey = request.getParameter("key"); 
	if(debugKey!=null) DebugConstants.debugKey = debugKey;
	String ip = request.getParameter("ip"); 
	if(ip!=null) DebugConstants.debugIP = ip;
	String device = request.getParameter("device");
	if(device!=null) DebugConstants.debugDevice = device;
	String filter = request.getParameter("filter");
	if(filter!=null) DebugConstants.debugFilter = filter;
	response.sendRedirect("../debug");
}
%>
  <fieldset>
    <legend>精确条件</legend>
  日志开启:<select name="mode" id="mode">
		<option value="false">关闭</option>
		<option value="true">打开</option>
        </select>
   设备:<select name="device" id="device">
		<option value="All">All</option>
		<option value="Android">Android</option>
		<option value="iPhone">iPhone</option>
		<option value="Others">Others</option>
		
	</select>
    关键字：<input type="text" name="key" id="key"/>
    指定IP：<input type="text" name="ip" id="ip"/>
    </br>需要过滤的请求:
    <textarea name="filter" id="filter" rows="1" cols="100%"><%=DebugConstants.debugFilter %></textarea>
  </fieldset>
<input type="button" value="保存设置" onclick="setPara();">
<input type="button" value="清除日志" onclick="clearLog()">
<table  border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<th>请求时间</th>
		<th>设备</th>
		<th>地址</th>
		<th>ip地址</th>
	</tr>
    <%
     String logFilePath = DebugConstants.getWebRootPath()+"/debug/d.log";
     File logFile = new File(logFilePath);
     if(!logFile.exists()){
    	 DebugUtil.writeTextToFile("",logFilePath,false);
     }
     InputStreamReader read = new InputStreamReader (new FileInputStream(logFilePath),"gbk");
	 BufferedReader br=new BufferedReader(read);
        String[] arrs=null;
        String line="";
        int i=0;
        while ((line=br.readLine())!=null) {
            arrs=line.split("##");
            System.out.println(arrs.length);
             if(arrs.length==7){
            %>
            
             <tr>
		        <td><%=arrs[0] %></td>
				<td><%=arrs[1] %></td>
		        <td>
		        	<%=arrs[2]%>?<%=arrs[3]%>
		        	
		        	<a href="javascript:showOrHide('div<%=i %>','RawJson<%=i%>','Canvas<%=i %>')">returns</a>|<a href="<%=arrs[2]%>?<%=arrs[3]%>" target="_blank">browse</a>
		        	
		        </td>
		        <td><%=arrs[5] %></td>
		    </tr>
		    <tr id="div<%=i %>" style="display:none;">
		        <td colspan="4" width="760" height="60" bgcolor="white">
		        <textarea id="RawJson<%=i %>" style="display:none;" cols="100" rows="18"><%=arrs[6] %></textarea><BR/>
                <div style="background:white; color:blue" >设备信息:</div>
               <div style="background:white; color:red" ><%=arrs[4] %></div>
               <div style="background:white; color:blue" >请求参数:</div>
               <div style="background:white; color:red" ><%=arrs[3] %></div>
               <div style="background:white; color:blue" >返回结果:</div>
                <div id="Canvas<%=i %>" class="Canvas" style="display:none">></div>
		        </td>
		    </tr>
            <%
            i++;
            }else{
            %>
            <tr><td colspan="4"><%=line %></td></tr>
            <%
            }
        }
        br.close();
        read.close();
     %>
</table>
</body>
</html>