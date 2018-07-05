<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- Baidu Button BEGIN -->
<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=0"  ></script>
<script type="text/javascript" id="bdshell_js"></script>
<script type="text/javascript">
document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000);
</script>
<!-- Baidu Button END -->


<%-- 
大图标
<div id="bdshare" class="bdshare_t bds_tools_32 get-codes-bdshare"  data="{'text':'要分享的文字'}" style="float: right; margin: -10px 0px 0 10px;">
	<a class="bds_tsina"></a>
	<a class="bds_tqq"></a>
</div>

 --%>
 
 <%-- 
 小图标
 <div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare"  data="{'text':'【<c:out value="${succ.title}"/>】<c:out value="${item.commContent}"/>'}" style="float: right; margin: 0px 0px 0 10px;">
	<a class="bds_tsina"></a>
	<a class="bds_tqq"></a>
</div>
 
  --%>
  
  <%-- 
  带图片
  <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

  data="{'pic':'<%= basePath%>..<c:out value="${succ.cover}"/>'}" 
   --%>