<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="com.framework.util.DateTimeUtil"%>
<%@page import="com.apps.model.Param"%>
<%@page import="com.apps.eff.CacheUtil"%>
<%@page import="com.apps.eff.GameHelpUtil"%>
<%@page import="com.game.model.GaSessionInfo"%>
<%@page import="com.apps.Constants"%>
<%@page import="com.framework.util.ParamUtils"%>
<%@page import="com.framework.util.ManageFile"%>
<%@page import="com.framework.util.FSO"%>
<%@page import="com.apps.model.Advertising"%>
<%@page import="java.util.List"%>
<%@page import="com.framework.util.HQUtils"%>
<%@page import="com.framework.service.impl.ServiceLocatorImpl"%>
<%@page import="com.ram.service.user.IUserService"%>
<%@page import="com.framework.service.IServiceLocator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>_gamelog - view</title>
<style type="text/css">
.log-box{}
.log-box p{margin:0;padding:2px 0;font-size:14px;color:#333;}
</style>
</head>
<body>
<%
	String action = ParamUtils.getParameter(request,"action","viewLog");
	String gameCode = ParamUtils.getParameter(request,"code","ga");
	String dtstr = ParamUtils.getParameter(request,"dt");//日期串,如180426
	if(!ParamUtils.chkString(dtstr)) dtstr = DateTimeUtil.getDateTime("yyMMdd");

	StringBuffer outSb = new StringBuffer();//输出
	
	long t1 = System.currentTimeMillis();
	String rootPath = Constants.getWebRootPath()+"/_gamelog/"+dtstr;
	//outSb.append(action);
	if(action.equals("viewLog")){
		String logPath = rootPath+"/"+gameCode+"."+dtstr+".log";
		//outSb.append(logPath);
		try {
			InputStreamReader read = new InputStreamReader (new FileInputStream(logPath),"GBK");
			BufferedReader reader=new BufferedReader(read);
			String rdata = "<div class=\"log-box\">";
			String cdata = "";
			String line;
			while ((line = reader.readLine()) != null) {
				cdata = "<p>"+line+"</p>"+cdata;
			}
			rdata += cdata + "</div>";
			outSb.append(rdata);
			
			reader.close();
			read.close();
		} catch (Exception e) {
		}
	}
		
	out.println(outSb.toString());
%>

</body>
</html>