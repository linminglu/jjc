<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/pow2webgui.tld" prefix="gui" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import="com.ram.RamConstants"%>

<%@page import="com.apps.Constants"%>
<%
	request.getSession().setAttribute("slogan",Constants.SLOGAN);
	String webContext = RamConstants.getWebappContext();
	String domainName = Constants.getDomainName();
%>