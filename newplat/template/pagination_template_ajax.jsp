<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:useBean id="currentPageNumber" type="java.lang.Integer" scope="request"/>
<%if(currentPageNumber!=null){%>
<div class="pagerbox">
	<div class="pagerboxw">
			<pg:index export="total=itemCount">
			<pg:first><a onclick='getDate("<c:url value='${pageUrl}'/>")' href="javascript:void(0);"><nobr>[首页]</nobr></a></pg:first>
			<pg:skip pages="<%= -5 %>"><a class="apage" onclick='getDate("<c:url value='${pageUrl}'/>")' href="javascript:void(0);"><nobr>[上五页]</nobr></a></pg:skip>
			<pg:prev><a onclick='getDate("<c:url value='${pageUrl}'/>")' href="javascript:void(0);"><nobr>[上一页]</nobr></a></pg:prev>
			<pg:pages>
			<% 
				if (pageNumber == currentPageNumber) { 
			%>
				<a class="selected"><%= pageNumber %></a>
			<% } else { %>
			   <a class="apage" onclick='getDate("<c:url value='${pageUrl}'/>")' href="javascript:void(0);"><%= pageNumber %></a>
			<% } %>
			</pg:pages>
			<pg:next><a onclick='getDate("<c:url value='${pageUrl}'/>")' href="javascript:void(0);"><nobr>[下一页]</nobr></a></pg:next>
			<pg:skip pages="<%= 5 %>"><a class="apage" onclick='getDate("<c:url value='${pageUrl}'/>")'  href="javascript:void(0);"><nobr>[下五页]</nobr></a></pg:skip>
			<pg:last><a onclick='getDate("<c:url value='${pageUrl}'/>")' href="javascript:void(0);"><nobr>[尾页]</nobr></a></pg:last>
			<pg:page export="first,last">
<%-- 			   共<%= total %>条记录 --%>
			</pg:page>
			</pg:index>
	</div>
</div>
<%}%>

