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
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="../js/js_z/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/js_z/dialog.js"></script>
<script type="text/javascript">
function justify(id){
	var value= $("#openresult").val();
	if(value!=null&&value!=""&&value.trim()!=""){
		 if(confirm('确实该开奖结果吗？')){		
				$.post("pokerAction.do?method=saveOpenResult&sessionId="+id+"&openResult="+value, 
						function(data) {
							data = $.trim(data);					
							if (data == "success") {
								location.reload();
								alert("保存成功！");	
							}else{
								alert("保存失败！");	
							}
				});
		}
	}else{
		alert("请输入开奖结果");
	}
}

function open(id){
	var sOne= $("#suitOne" + id + " option:selected").val().trim();
	var nOne = $("#numOne" + id).val().trim();
	var sTwo= $("#suitTwo" + id + " option:selected").val().trim();
	var nTwo = $("#numTwo" + id).val().trim();
	var sThree= $("#suitThree" + id + " option:selected").val().trim();
	var nThree = $("#numThree" + id).val().trim();
	var oneT= $("#suitOne" + id + " option:selected").text().trim();
	var twoT = $("#suitTwo" + id +" option:selected").text().trim();
	var threeT= $("#suitThree" + id + " option:selected").text().trim();

	if(sOne == null || sOne == "" || sTwo == null || sTwo == "" || sThree == null || sThree == "" ||
			nOne == null || nOne == "" || nTwo == null || nTwo == "" || nThree == null || nThree == ""){
		alert("开奖结果没有输入完整");	
	}else{
		var value = sOne+"_"+nOne+","+sTwo+"_"+nTwo+","+sThree+"_"+nThree;
		var value2 = oneT+nOne+","+twoT+nTwo+","+threeT+nThree;
		if(value!=null&&value!=""){
			 if(confirm('确实要开奖吗？开奖号码：'+value2)){		
					$.post("pokerAction.do?method=openResult&sessionId="+id+"&openResult="+value, 
							function(data) {
								data = $.trim(data);
								if (data == "success") {
									location.reload();
									alert("开奖成功！");	
								}else{
									alert("开奖失败！");	
								}
					});
			}
		}else{
			alert("请输入开奖结果");
		}

	}
}

function drawback(id){
	 if(confirm('确实要退还本期投注吗？')){
			$.post("pokerAction.do?method=drawback&sessionId="+id, 
					function(data) {
						data = $.trim(data);
						if (data == "success") {
							location.reload();
							alert("退还成功！");	
						}else{
							alert("退还失败！");	
						}
			});
	}
}
//撤回已经派彩的金额
function revokePrize(id){
	layer.confirm('确实要撤回本期已经派彩的金额吗？', {icon: 3, offset: '100px', title:'提示'}, function(index){
		layer.close(index);
		$.post("pokerAction.do?method=revokePrize&sessionId="+id, 
			function(data) {
                var jsonData = JSON.parse(data);
                var code = jsonData.code;
		        if (code == "200") {
			        layer.alert(jsonData.msg, {icon: 6, offset: '100px'},function(index){
				        location.reload();
			        	layer.close(index)
			        });
		        }else{
			        layer.alert(jsonData.msg, {icon: 5, offset: '100px'});
		        }
		});
	});
}
</script>
</head>
<body>
<html:form action="/pokerAction.do?method=init">
<html:hidden property="startIndex" name="pokerForm"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
	</tr>
</table>
</html:form>
<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
%>
<pg:pager 
	url="cqSscAction.do"
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="init"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>期号</th>
		<th>开始时间</th>
		<th>结束时间</th>
		<th>结果</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<bean:write name="item" property="sessionNo"/>
	    </td>
	     <td align="center">
			<bean:write name="item" property="startTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="endTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    <td align="center">
	        <select id="suitOne<bean:write name="item" property="sessionId"/>" name="" >
				<option value="">请选择</option>
				<option value="1">黑桃</option>
				<option value="2">红心</option>
				<option value="3">梅花</option>
				<option value="4">方块</option>
			</select>
			<input  id="numOne<bean:write name="item" property="sessionId"/>" value=""  size="5" />&nbsp;&nbsp;
	        <select id="suitTwo<bean:write name="item" property="sessionId"/>" name="" >
				<option value="">请选择</option>
				<option value="1">黑桃</option>
				<option value="2">红心</option>
				<option value="3">梅花</option>
				<option value="4">方块</option>
			</select>
			<input  id="numTwo<bean:write name="item" property="sessionId"/>" value=""  size="5" />&nbsp;&nbsp;
	        <select id="suitThree<bean:write name="item" property="sessionId"/>" name="" >
				<option value="">请选择</option>
				<option value="1">黑桃</option>
				<option value="2">红心</option>
				<option value="3">梅花</option>
				<option value="4">方块</option>
			</select>
			<input  id="numThree<bean:write name="item" property="sessionId"/>" value=""  size="5" />&nbsp;&nbsp;
			<p class="red">从左往右分别为1到3球开奖结果，记得每一球都要选择花色，都要输入开奖值。开奖值可以输入：1～10、J、Q、K</p>
	    </td>
		<td align="center">
			<c:if test="${item.openStatus !='2'}">
				<a class="link" href="javascript:open(<bean:write name="item" property="sessionId"/>);">保存并开奖</a>
				/<a class="link" href="javascript:drawback(<bean:write name="item" property="sessionId"/>);">投注退款</a>
			</c:if>
			<c:if test="${item.openStatus =='2'}">
				<a class="link" href="javascript:revokePrize(<bean:write name="item" property="sessionId"/>);">撤回派彩</a>
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