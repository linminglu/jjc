<%@page import="com.framework.util.ParamUtils"%>
<%
String index = ParamUtils.getParameter(request,"index","center");
int maxIndexPages = ParamUtils.getIntParameter(request,"maxIndexPages",20);
boolean isOffset = ParamUtils.getBooleanParameter(request,"isOffset",true);
int maxPageItems = ParamUtils.getIntParameter(request,"maxPageItems",20);
%>
