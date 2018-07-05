<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:useBean id="currentPageNumber" type="java.lang.Integer" scope="request"/>

<%if(currentPageNumber!=null){%>
<table class="tblpage" border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td align="right" nowrap="nowrap">
			<pg:index export="total=itemCount">
			<pg:first><a class="apage" href="<%= pageUrl %>"><nobr>[首页]</nobr></a></pg:first>
			<pg:skip pages="<%= -5 %>"><a class="apage" href="<%= pageUrl %>"><nobr>[上五页]</nobr></a></pg:skip>
			<pg:prev><a class="apage" href="<%= pageUrl %>"><nobr>[上一页]</nobr></a></pg:prev>
			<pg:pages>
			<% 
				if (pageNumber == currentPageNumber) { 
			%>
				[<%= pageNumber %>]
			<% } else { %>
			   <a class="apage" href="<%= pageUrl %>"><%= pageNumber %></a>
			<% } %>
			</pg:pages>
			<pg:next><a class="apage" href="<%= pageUrl %>"><nobr>[下一页]</nobr></a></pg:next>
			<pg:skip pages="<%= 5 %>"><a class="apage" href="<%= pageUrl %>"><nobr>[下五页]</nobr></a></pg:skip>
			<pg:last><a class="apage" href="<%= pageUrl %>"><nobr>[尾页]</nobr></a></pg:last>
			<pg:page export="first,last">
			   共<%= total %>条记录
			</pg:page>
			</pg:index>
		</td>
	</tr>
</table>

<%}%>
<br/>

