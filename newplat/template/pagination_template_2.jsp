<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:useBean id="currentPageNumber" type="java.lang.Integer" scope="request"/>

<%if(currentPageNumber!=null){%>
<div class="pagerbox">
	<div class="pagerboxw">
		<pg:index export="total=itemCount">
			<pg:first><a href="<%= response.encodeURL(pageUrl) %>">首页</a></pg:first>
			<pg:skip pages="<%= -5 %>"><a href="<%= response.encodeURL(pageUrl) %>">上五页</a></pg:skip>
			<pg:prev><a href="<%= response.encodeURL(pageUrl) %>">上一页</a></pg:prev>
			<pg:pages>
			<% 
				if (pageNumber == currentPageNumber){ 
			%>
				<a class="selected"><%= pageNumber %></a>
			<% } else { %>
			   <a href="<%= response.encodeURL(pageUrl) %>"><%= pageNumber %></a>
			<% } %>
			</pg:pages>
			<pg:next><a href="<%= response.encodeURL(pageUrl)%>">下一页</a></pg:next>
			<pg:skip pages="<%= 5 %>"><a href="<%= response.encodeURL(pageUrl) %>">下五页</a></pg:skip>
			<pg:last><a href="<%=response.encodeURL(pageUrl) %>">尾页</a></pg:last>
			<pg:page export="first,last"><%-- 共<%= total %>条记录 --%></pg:page>
		</pg:index>
	</div>
</div>

<%}%>
