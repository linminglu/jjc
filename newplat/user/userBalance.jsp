<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
<%@ include file = "../common/inc_file.jsp"%>
<script type="text/javascript">
//查看账单投注详情
function view(id){
	$.post("managerAction.do?method=view&tradeDetailId="+id, 
		function(data) {
            var jsonData = JSON.parse(data);
            var code = jsonData.code;
	        if (code == "200") {
				var data=jsonData.data;
				var items=data.items;
				var length=items.length;
				var totalMoney=data.totalMoney;
				var gfxyType=data.gfxyType;
	          	var html='';
	        	var obj0=items[0];

	        	html=html+'<table class="layui-table"><colgroup><col width="10"><col width="20"><col></colgroup><thead><tr><th>订单号</th><th>玩法</th><th>投注项</th><th>投注额</th><th>中奖状态</th><th>奖金</th></tr></thead><tbody>'
	        	for(var i = 0; i < length; i++){
	        		var obj=items[i];
					var sessionNo=obj.sessionNo;
					var playName=obj.playName;
					var betName=obj.betName;
					var optionTitle=obj.optionTitle;
					var betMoney=obj.betMoney;
					var winCash=obj.winCash;
					var winResult=obj.winResult;
					var num=i+1;
					if(gfxyType==1){// 官方
						if(winResult=="2"){
							winResult="未开奖";
						}else if(winResult=="4"){
							winResult="<span class='red'>中奖</span>";
						}else if(winResult=="5"){
							winResult="<span class='green'>未中奖</span>";
						}
					}else{// 信用
						if(winResult=="0"){
							winResult="未开奖";
						}else if(winResult=="1"){
							winResult="<span class='red'>中奖</span>";
						}else if(winResult=="2"){
							winResult="<span class='green'>未中奖</span>";
						}else if(winResult=="3"){
							winResult="打和";
						}
					}
	        	    html=html+'<tr><td>'+obj.betDetailId+'</td><td>'+betName+'</td><td style="word-break: break-all;">'+optionTitle+
	        		'</td><td>'+betMoney+'</td><td>'+winResult+'</td><td>'+winCash+'</td></tr>'
	        	}
	          	html=html+'</tbody></table><div class="layui-field-box" style="text-align:center;color:Orange;">';
	          	html=html+"合计投注"+totalMoney+"元"+'</div>';

	     	    layer.open({
	     	        title: '明细详情',
	     	        btn: ['关闭'] ,
	     	        offset: '100px',
	     	        area: ['700px','400px'],
	     	        skin: 'layui-layer-lan',
	     	        type: 1,
	     	        content: html
	      	    });
	        }else{
		        layer.alert(jsonData.msg, {icon: 5, offset: '100px'});
	        }
	})
}
</script>
</head>
<body>
<div class="bread">快捷管理&raquo;资金明细</div>
<html:form action="/managerAction.do?method=balance">
<html:hidden property="startIndex" name="managerForm"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			<span>用户ID：<html:text property="userId" name="managerForm" style="width:80px;"/></span>
    		<span>用户名：<html:text property="loginName" name="managerForm"/></span>
    		<span>创建时间：<html:text property="startDate" name="managerForm" styleClass="Wdate" styleId="startTime" onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"/>-
    		<html:text property="endDate" name="managerForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"/></span>
		</td>
	</tr>
	<tr class="moon">
		<td>
		<span>订单号：<html:text property="betDetailId" name="managerForm" style="width:170px;"/></span>
   		
			<span>
			账变类型：
			<html:select property="cashType" name="managerForm" styleClass="sele select">
				<html:option value="">全部</html:option>
				<html:option value="18">中奖彩派</html:option>
				<html:option value="27">管理员充值</html:option>
				<html:option value="32">会员充值返水给代理</html:option>
				<html:option value="34">会员充值系统赠送</html:option>
				<html:option value="40">充值跳级奖励</html:option>
				<html:option value="57">竞猜投注</html:option>
			</html:select>
			</span>
			<input type="submit" value="查询" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>
<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userId =request.getAttribute("userId")!=null?(String)request.getAttribute("userId"):"";
String loginName =request.getAttribute("loginName")!=null?(String)request.getAttribute("loginName"):"";
String startDate =request.getAttribute("startDate")!=null?(String)request.getAttribute("startDate"):"";
String endDate =request.getAttribute("endDate")!=null?(String)request.getAttribute("endDate"):"";
String gameType =request.getAttribute("gameType")!=null?(String)request.getAttribute("gameType"):"";
String cashType =request.getAttribute("cashType")!=null?(String)request.getAttribute("cashType"):"";
String betDetailId =request.getAttribute("betDetailId")!=null?(String)request.getAttribute("betDetailId"):"";
%>
<pg:pager 
	url="managerAction.do"
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="balance"/>
	<pg:param name="userId" value="<%=userId%>"/>
	<pg:param name="loginName" value="<%=loginName%>"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="gameType" value="<%=gameType%>"/>
    <pg:param name="cashType" value="<%=cashType%>"/>
     <pg:param name="betDetailId" value="<%=betDetailId%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th width="60">UID</th>
		<th>用户名</th>
		<th width="120">时间</th>
		<th>账变类型</th>
		<th>类型</th>
		<th width="80">金额变动</th>
		<th width="80">余额</th>
		<th width="40">操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><c:out value='${item.tradeDetailId}'/><input type="hidden" name="ids" value="<c:out value='${item.tradeDetailId}'/>"></td>
	    <td align="center">
	        <bean:write name="item" property="userId"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="loginName"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="createTime" format="yyyy-MM-dd HH:mm"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="cashType"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="modelType"/>
	    </td>
	    <td align="center" class="tdprice tdright">
	        <c:if test="${item.tradeType eq '2'}">
	          <font color ="green"><bean:write name="item" property="cashMoney" format="0.00"/></font>
	        </c:if>
	        <c:if test="${item.tradeType eq '1'}">
	          <font color ="red"><bean:write name="item" property="cashMoney" format="0.00"/></font>
	        </c:if>
	        
	    </td>
	     <td align="center" class="tdprice tdright">
	        <bean:write name="item" property="userMoney" format="0.00"/>
	    </td>
	   <td align="center">
			<c:if test="${item.cashType eq '购买彩票' || item.cashType eq '中奖彩派'}">
	      	 <a href='javascript:void(0);' onclick="view(<bean:write name="item" property="tradeDetailId"/>);"  ><img class="viewImg" src="../images/view.png"/></a>
	      	</c:if>
	    </td> 
	</tr>
	</pg:item>
	</logic:iterate>
</table>
</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="/template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
</body>
</html:html>