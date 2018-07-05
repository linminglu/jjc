<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%
/**
 * 分页:	┬ 只针对 GET 方式的访问 (注意URI编码问题<Connector URIEncoding="UTF-8" />)
 * 		├ 需提供二个request范围的Integer参数(count,rows)
 * 		└ 程序根据 offset 判断当前页数 0:为第一页
 * @param count 总共记录数
 * @param rows 每页显示的记录数 默认为20条
 * @author Lu Congyu
 * @date 06/10/11
 */
%>
<style type="text/css">
.pages {padding:1em 0;margin:.2em 0;clear:left;font-size:12px}
.pages a, .pages span{display:block;float:left;font-size:12px;padding:0.2em 0.5em;margin-right:0.1em;border:1px solid #fff;background:#fff}
.pages span.current{border:1px solid #2e6ab1;font-weight:bold;background:#2e6ab1;color:#fff;font-size:12px}
.pages a{border:1px solid #9aafe5;text-decoration:none;font-size:12px}
.pages a:hover{border-color:#2e6ab1;text-decoration:none;font-size:12px}
.pages a.nextprev{font-weight:bold;font-size:12px}
.pages span.nextprev{border:1px solid #ddd;color:#999;font-size:12px}
</style>

<%
String _url = request.getRequestURL().toString();
String _query = request.getQueryString()==null?"":request.getQueryString().replaceAll("[o|O][f|F][f|F][s|S][e|E][t|T]=[^&]*&?", "");
int _offset = request.getParameter("offset")==null?0:Integer.valueOf(((String)request.getParameter("offset")).replaceAll("[^0-9]","")).intValue();
int _count = request.getAttribute("count")==null?0:((Integer)request.getAttribute("count")).intValue();
int _rows = request.getAttribute("rows")==null?20:((Integer)request.getAttribute("rows")).intValue();
int _pages = _count%_rows>0?_count/_rows+1:_count/_rows;

_offset = _offset<_pages?_offset:0;
_url += _query.equals("")?"?offset=":"?"+_query+(_query.endsWith("&")?"":"&")+"offset=";

int	_end = _offset>5?(_offset+5>_pages?_pages:_offset+5):(10>_pages?_pages:10);
int	_begin = _end-10<0?0:_end-10;
int record_begin=0;
	record_begin=_offset * _rows +1;
int record_end=0;
	if((_offset+1)<_pages)  {
		record_end= (_offset + 1)*_rows ;
	}
	else{
		record_end= _count;
	}
out.print("<table width='100%' cellspacing='0' cellpadding='0' border='0' ><tr><td align=right>");
out.print("<a href=\""+_url+(0)+"\" class=\"nextprev\">[首页]</a>");
if(_pages>1){

	out.print(_offset==0?"<span class=\"nextprev\">[上一页]</span>":"<a href=\""+_url+(_offset-1)+"\" class=\"nextprev\">[上一页]</a>");
	if(_begin>0)out.print("<a href=\""+_url+"0\">[1]</a>");
	if(_begin>1)out.print("<a href=\""+_url+"1\">[2]</a>");
	if(_begin>2)out.print("<span>...</span>");
	for(int _i=_begin;_i<_end;_i++){
		out.print(_offset==_i?"<span class=\"current\">["+(_i+1)+"]</span>":"<a href=\""+_url+_i+"\">["+(_i+1)+"]</a>");
	}
	if(_pages-_end>2)out.print("<span>...</span>");
	if(_pages-_end>1)out.print("<a href=\""+_url+(_pages-2)+"\">["+(_pages-1)+"]</a>");
	if(_pages-_end>0)out.print("<a href=\""+_url+(_pages-1)+"\">["+(_pages)+"]</a>");

	out.print(_offset==_pages-1?"<span class=\"nextprev\">[下一页]</span>":"<a href=\""+_url+(_offset+1)+"\" class=\"nextprev\">[下一页]</a>");

}
out.print("<a href=\""+_url+(_pages-1)+"\" class=\"nextprev\">[尾页]</a>");
out.print("第"+record_begin+"记录-第"+record_end+"记录/共"+_count+"记录");
out.print("</td></tr></table>");
%>
