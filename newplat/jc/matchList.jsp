<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script type="text/javascript">
function modify(id){
	document.forms[0].action = "matchAction.do?method=modify&mId="+id;
	document.forms[0].submit();
}

function del(id){
	if(confirm("删除战队可能会影响该战队相关的比赛，确定删除？")){
		location = "matchAction.do?method=delete&mId="+id;
	}
}
</script>
</head>
<body>
<div class="bread">竞猜管理  &raquo;赛事列表
</div>
<html:form action="/matchAction.do?method=init">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
		赛事名称：<html:text property="match.title" name="matchForm"/>
		&nbsp;队伍名称：<html:text property="match.title" name="matchForm"/>
		<span>比赛时间：<html:text property="startDate" name="matchForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/>
			——&nbsp;&nbsp;<html:text property="endDate" name="matchForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
    		&nbsp;<a href="matchAction.do?method=preAdd" class="gbutton">新增</a>
		</td>
	</tr>
</table>
</html:form>
<%
int number=1;
//分页
String totalCount =request.getAttribute("totalCount")!=null?(String)request.getAttribute("totalCount"):"0";
String title = (String)request.getAttribute("title");
%>
<pg:pager 
	url="matchAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="init"/>
	<pg:param name="title" value="<%=title%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<form class="layui-form" action="#" lay-filter="reform">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>赛事名称</th>
		<th>队伍1</th>
		<th>队伍2</th>
		<th>副标题</th>
		<th>是否推荐</th>
		<th>开奖时间</th>
		<th>比赛时间</th>
		<th>总场次</th>
		<th>红方</th>
		<th>蓝方</th>
		<th>类型</th>
		<th>一级</th>
		<th>二级</th>
		<th>抽水比例</th>
		<th>总金额</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<bean:write name="item" property="title"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="team1Name"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="team2Name"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="subTitle"/>
	    </td>
	    <td align="center">
	     	<c:if test="${item.isRecommend == '1'}">
				<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="1" data="<bean:write name="item" property="mId"/>" id="status<bean:write name="item" property="mId"/>"/>
			</c:if>
			<c:if test="${item.isRecommend == '0'}">
				<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="0" data="<bean:write name="item" property="mId"/>" id="status<bean:write name="item" property="mId"/>"/>
			</c:if>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="openTime" format="yyyy-MM-dd HH:mm"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="matchTime" format="yyyy-MM-dd HH:mm"/>
	    </td>
	    <td align="center">
	     	<c:if test="${item.boNum==1}">BO1</c:if>
	     	<c:if test="${item.boNum==3}">BO3</c:if>
	     	<c:if test="${item.boNum==5}">BO5</c:if>
	     	<c:if test="${item.boNum==7}">BO7</c:if>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="red"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="blue"/>
	    </td>
	    <td align="center">
	     	<c:if test="${item.type==1}">对战</c:if>
	     	<c:if test="${item.type==2}">单</c:if>
	    </td>
	    <td align="center">
	     	<c:if test="${not empty type1List}">
				<c:forEach var="type" items="${type1List}">
					<c:if test="${item.tId1 eq type.tid}">
						<c:out value="${type.title}" />
					</c:if>
				</c:forEach>
			</c:if>
	    </td>
	    <td align="center">
	     	<c:if test="${not empty type2List}">
				<c:forEach var="type" items="${type2List}">
					<c:if test="${item.tId2 eq type.tid}">
						<c:out value="${type.title}" />
					</c:if>
				</c:forEach>
			</c:if>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="rercentage"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="totalPrice"/>
	    </td>
		<td align="center">
			<a class="link" href='updateMatch.jsp?mId=<bean:write name="item" property="mId"/>'>编辑</a>
			<%-- |<a class="link" href='modifyMatch.jsp?mId=<bean:write name="item" property="mId"/>'>比赛局</a> --%>
			<c:if test="${loginUser.userType eq '2' || loginUser.userType eq '4'}">
				|<a class="link" href='javascript:void(0);' onclick="del(<bean:write name="item" property="mId"/>);">删除</a>
			</c:if>
			|<a class="link" href='openMatch.jsp?mId=<bean:write name="item" property="mId"/>'>开奖</a>
		</td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
</form>
</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="/template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
<script type="text/javascript">
layui.use('element', function(){
});
layui.use('form', function(){
	var form = layui.form;
	
	form.on('switch(statusSwitch)', function(data){
		var loadObj = layer.load();
		var obj=data.elem;
		var id=$(obj).attr('data');
		var checked=data.elem.checked;		
		var val='0';
		if(checked){
			val='1';
		}
		$.post("matchAction.do?method=changeStatus", 
			{'mId':id},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					layer.close(loadObj);
					$('#status'+id).val(val);
				}else{
					layer.close(loadObj);
					//失败的话
					if(val=='1'){
						$('#status'+id).val('0');
						$('#status'+id).removeAttr('checked');
					}else{
						$('#status'+id).val('1');
						$('#status'+id).attr('checked',true);
					}
					form.render('checkbox');
					layer.alert(data.msg);
				}
		}, "json");
	});  
});
</script>
</body>
</html:html>