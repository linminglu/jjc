<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
function del(){
	var frm = document.managerForm;
	var bool = false;
	var sel = frm["selectIndex"];
	for(var i=0;i<sel.length;i++ ){
		if(sel[i].checked){
			bool = true;
		}
	}
	if( !bool ){
		alert( "请选择删除对象" );
		return false;
	}
	if( confirm( "你确认要删除吗?" )){
		return true;
	}
	return false;
}

function delUser(url,loginName){
	if(loginName && (loginName=="admin" || loginName=="cuisy" || loginName=="toefladmin" || loginName=="genadmin" 
		 	|| loginName=="testadmin" || loginName=="examadmin"
			|| loginName=="satadmin" || loginName=="ieltsadmin" || loginName=="greadmin" || loginName=="gmatadmin")){
		alert("不能删除此管理账号！");
		return false;
	}
	if(confirm("确认删除用户?")){
		location.href = url;
	}
	
}

function selectAll(flag){
	var selAll = document.managerForm;
	for( var i=0;i<selAll.elements.length;i++ ){
		if( selAll.elements[i].name == 'selectIndex' ){
			selAll.elements[i].checked = flag;
		}
	}
}
function delItem(id){
	if(confirm('确实要删除吗？')){
		$.post("baseDataAction.do?method=lotteryCatDel&id="+id,
			function(data) {
				data = $.parseJSON(data);
				if(data && data.code=='200') {
					location.reload();
					alert("删除成功！");
					location='baseDataAction.do?method=lotteryCatList';
				}else{
// 					alert("删除失败！\n\n提示：数据已应用则不能删除！");
					alert(data.msg);
				}
		});	
	}
}

function showOptions(id){
	window.location = "baseDataAction.do?method=gameOptionList&infoId="+id;
}
</script>
</head>
<body>
<div class="bread">彩种管理</div>
<html:form action="/baseDataAction.do?method=lotteryCatList" method="post">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			彩票名称
			<html:text property="title" name="baseDataForm"/>
			彩票类型
			<html:select property="playCate" name="baseDataForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">官方</html:option>
				<html:option value="2">信用</html:option>
			</html:select>
			显示状态
			<html:select property="status" name="baseDataForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">有效</html:option>
				<html:option value="0">无效</html:option>
			</html:select>
			投注状态
			<html:select property="betAvoid" name="baseDataForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">有效</html:option>
				<html:option value="0">无效</html:option>
			</html:select>
			<input type="submit" class="gbutton" value="查询" />
			<input type="button" class="gbutton" value="添加" onclick="location='baseDataAction.do?method=lotteryCatPreAdd';"/>
		</td>
	</tr>
</table>
</html:form>	

<%
//分页
String totalCount = request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userType = request.getAttribute("userType")!=null?(String)request.getAttribute("userType"):"";
String category = request.getAttribute("category")!=null?(String)request.getAttribute("category"):"";
String agentId = request.getAttribute("agentId")!=null?(String)request.getAttribute("agentId"):"0";
String title = (String)request.getAttribute("title");
String playCate = (String)request.getAttribute("playCate");
String status = (String)request.getAttribute("status");
String betAvoid = (String)request.getAttribute("betAvoid");
%>
<pg:pager 
	url="baseDataAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="lotteryCatList"/>
	<pg:param name="title" value="<%=title%>"/>
	<pg:param name="playCate" value="<%=playCate%>"/>
	<pg:param name="status" value="<%=status%>"/>
	<pg:param name="betAvoid" value="<%=betAvoid%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<html:form action="/baseDataAction.do?method=delete" onsubmit="return del();" styleClass="layui-form">
<bean:size id="length" name="lotteryCatList"/>
<logic:notEmpty name="lotteryCatList">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th width="30">类型</th>
		<th>游戏名称</th>
	    <!-- <th>游戏类型</th> -->
	    <th width="80">最新未开奖期号</th>	   	    
	    <th width="80">最新已开奖期号</th>
    	<th>最新开奖结果</th>
    	<th width="140">开奖时间</th>
	    <th>图片</th>
    	<!-- <th>描述</th>	
    	<th>每日开奖期数说明</th> -->
    	<th width="60">显示状态</th>	   	    
	    <th width="60">投注状态</th>
	    <!-- <th width="120">是否显示历史开奖</th> -->
	    <th width="130">操作</th>

	</tr>
	<logic:iterate id="item" name="lotteryCatList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
		<td align="center">
            <c:if test = "${item.playCate eq '1'}">
		  		官方
		  	</c:if>
		    <c:if test = "${item.playCate eq '2'}">
		  		信用
		  	</c:if>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="gameTitle" />
	    </td>
	   <%--  <td align="center">
	        <bean:write name="item" property="gameType"/>
	    </td> --%>
	    <td align="center">
	        <bean:write name="item" property="latestSessionNo"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="openSessionNo"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="openResult"/>
	    </td>
		<td align="center">
	      	<bean:write name="item" property="endTime" format="yyyy-MM-dd HH:mm:ss"/>
		</td>
	    <td align="center">
	        <img style="width:30px;" src="<bean:write name="item" property="img"/>" alt="" />
	    </td>
	    <%-- <td align="center">
	        <bean:write name="item" property="des"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="exp"/>
	    </td> --%>
	    <td align="center">
 	    	<c:if test="${item.status == '1'}">
				<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="changeStatus" value="1" data="<bean:write name="item" property="infoId"/>" id="status<bean:write name="item" property="infoId"/>"/>
			</c:if>
			<c:if test="${item.status == '0'}">
				<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="changeStatus" value="0" data="<bean:write name="item" property="infoId"/>" id="status<bean:write name="item" property="infoId"/>"/>
			</c:if>
	    </td>
	    <td align="center">
 	    	<c:if test="${item.betAvoid == '1'}">
				<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="changeBetAvoid" value="1" data="<bean:write name="item" property="infoId"/>" id="betAvoid<bean:write name="item" property="infoId"/>"/>
			</c:if>
			<c:if test="${item.betAvoid == '0'}">
				<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="changeBetAvoid" value="0" data="<bean:write name="item" property="infoId"/>" id="betAvoid<bean:write name="item" property="infoId"/>"/>
			</c:if>
	    </td>
	   <%--  <td align="center">
 	    	<c:if test="${item.isShowHistoryOpen == '1'}">
				<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="isShowHistoryOpen" value="1" data="<bean:write name="item" property="infoId"/>" id="isShowHistoryOpen<bean:write name="item" property="infoId"/>"/>
			</c:if>
			<c:if test="${item.isShowHistoryOpen == '0'}">
				<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="isShowHistoryOpen" value="0" data="<bean:write name="item" property="infoId"/>" id="isShowHistoryOpen<bean:write name="item" property="infoId"/>"/>
			</c:if>
	    </td> --%>
		<%-- <td align="center" id="isShowHistoryOpen<bean:write name="item" property="infoId"/>">
			<logic:equal name="item" property="isShowHistoryOpen" value="1">
				<font color="green"><strong>有效</strong></font>&nbsp;
				<a class="button" onclick="changeIsShowHistoryOpen(<bean:write name="item" property="infoId"/>,'1');" href='javascript:void(0);'>关</a>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="isShowHistoryOpen" value="0">
				<font color="red"><strong>无效</strong></font>&nbsp;
				<a class="button" onclick="changeIsShowHistoryOpen(<bean:write name="item" property="infoId"/>,'0');" href='javascript:void(0);'>开</a>&nbsp;
			</logic:equal>
		</td> --%>
	    <td align="center">
<%-- 	     	<a class="link" href="javascript:void(0)" onclick="delItem(<bean:write name="item" property="infoId"/>)">删除</a> --%>
	        <a class="link" href="baseDataAction.do?method=lotteryCatPreAdd&id=<bean:write name="item" property="infoId"/>">修改</a>
	        <%-- /<a class="link" href="baseDataAction.do?method=lotteryCatOptionList&infoId=<bean:write name="item" property="infoId"/>">玩法</a> --%>
	 		/<a class="link" onclick="showOptions(<bean:write name="item" property="infoId"/>);" href='javascript:void(0);'>赔率</a>&nbsp;
	    </td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="lotteryCatList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

</html:form>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
<script type="text/javascript">
layui.use('element', function(){
});
layui.use('form', function(){
	var form = layui.form;
	form.on('switch(changeStatus)', function(data){
		var loadObj = layer.load();
		var obj=data.elem;
		var id=$(obj).attr('data');
		var checked=data.elem.checked;		
		var val='0';
		if(checked){
			val='1';
		}
		$.post("baseDataAction.do?method=changeStatus", 
			{'infoId':id},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					$('#status'+id).val(val);
				}else{
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
layui.use('form', function(){
	var form = layui.form;
	form.on('switch(changeBetAvoid)', function(data){
		var loadObj = layer.load();
		var obj=data.elem;
		var id=$(obj).attr('data');
		var checked=data.elem.checked;		
		var val='0';
		if(checked){
			val='1';
		}
		$.post("baseDataAction.do?method=changeBetAvoid", 
			{'infoId':id},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					$('#betAvoid'+id).val(val);
				}else{
					//失败的话
					if(val=='1'){
						$('#betAvoid'+id).val('0');
						$('#betAvoid'+id).removeAttr('checked');
					}else{
						$('#betAvoid'+id).val('1');
						$('#betAvoid'+id).attr('checked',true);
					}
					form.render('checkbox');
					layer.alert(data.msg);
				}
		}, "json");
	});  
});
layui.use('form', function(){
	var form = layui.form;
	form.on('switch(isShowHistoryOpen)', function(data){
		var loadObj = layer.load();
		var obj=data.elem;
		var id=$(obj).attr('data');
		var checked=data.elem.checked;		
		var val='0';
		if(checked){
			val='1';
		}
		$.post("baseDataAction.do?method=changeIsShowHistoryOpen", 
			{'infoId':id},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					$('#isShowHistoryOpen'+id).val(val);
				}else{
					//失败的话
					if(val=='1'){
						$('#isShowHistoryOpen'+id).val('0');
						$('#isShowHistoryOpen'+id).removeAttr('checked');
					}else{
						$('#isShowHistoryOpen'+id).val('1');
						$('#isShowHistoryOpen'+id).attr('checked',true);
					}
					form.render('checkbox');
					layer.alert(data.msg);
				}
		}, "json");
	});  
});
</script>
</body>
</html>