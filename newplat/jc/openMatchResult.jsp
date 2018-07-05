<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include file = "../common/inc_weditor.jsp"%>
<script language="javascript">

function getRate(oId){
	$.post("fieldAction.do?method=getRateByOId&oId="+oId,
			function(data) {
				if(data!=null) {
					$("#rate").val(data);
				}
		});
}

function getOptions(ptId){
	$("#oId").empty();
	var html = '';
	html +="<option>请选择</option>";
	$.post("fieldAction.do?method=getOptions",{'ptId':ptId},function(result){
		if(result!=null && result.length>0){
			var optionList = JSON.parse(result);
			 $.each(optionList,function(i){
				html +="<option value="+optionList[i].oId+">"+optionList[i].title+"</option>";
			});
			$(html).appendTo("#oId");
		}
	})
}

function getPlayTypes(fId){
	$("#pTId").empty();
	var html = '';
	html +="<option>请选择</option>";
	$.post("matchAction.do?method=getPlayTypes",{'fId':fId},function(result){
		if(result!=null && result.length>0){
			var playList = JSON.parse(result);
			 $.each(playList,function(i){
				html +="<option value="+playList[i].pTId+">"+playList[i].title+"</option>";
			});
			$(html).appendTo("#pTId");
		}
	})
}
</script>
<style type="text/css">
#editor-trigger{height: 300px}
</style>
</head>
<body>
<div class="bread">赛事管理  &raquo; 开奖 &raquo; 
</div>
<html:form action="/matchAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="matchForm" property="match.mId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">赛事名称</th>
      	<td>
        	<html:text property="match.title" name="matchForm" maxlength="20" style="width: 540px;" styleId="title" disabled="true"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">队伍1</th>
      	<td>
        	<html:select property="match.team1Name" styleClass="selectbox" style="width: 540px;" name="matchForm" styleId="team1Name" disabled="true">
                <html:option value="">请选择</html:option>
			       <c:if test="${not empty teamList}">
			          <html:options collection="teamList" property="id" labelProperty="title" />
			       </c:if>
			</html:select>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">队伍2</th>
      	<td>
        	<html:select property="match.team2Name" styleClass="selectbox" style="width: 540px;" name="matchForm" styleId="team2Name" disabled="true">
                <html:option value="">请选择</html:option>
			       <c:if test="${not empty teamList}">
			          <html:options collection="teamList" property="id" labelProperty="title" />
			       </c:if>
			</html:select>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">请选择要开奖的比赛局</th>
      	<td>
        	<html:select property="field.fId" name="matchForm" style="width: 540px;" styleId="fId" onchange="getPlayTypes(this.value)">
        		<html:option value="">请选择</html:option>
			       <c:if test="${not empty fieldList}">
			          <html:options collection="fieldList" property="fId" labelProperty="title"/>
			       </c:if>
        	</html:select>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">请选择要开奖的玩法</th>
      	<td>
        	<html:select property="playType.pTId" name="matchForm" style="width: 540px;" styleId="pTId" onchange="getOptions(this.value)">
        		<html:option value="">请选择</html:option>
			       <c:if test="${not empty playList}">
			          <html:options collection="playList" property="pTId" labelProperty="title"/>
			       </c:if>
        	</html:select>
      	</td>
    </tr>
    
   	<tr class="tr-color-body">
      	<th align="right">请选择开奖结果</th>
      	<td>
        	<html:select property="option.oId" name="matchForm" style="width: 540px;" styleId="oId" onchange="getRate(this.value)">
        		<html:option value="">请选择</html:option>
        		<c:if test="${not empty optionList}">
			          <html:options collection="optionList" property="oId" labelProperty="title"/>
			       </c:if>
        	</html:select>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">赔率</th>
      	<td>
        	<html:text property="option.rate" name="matchForm" maxlength="20" style="width: 540px;" styleId="rate" readonly="true"/>
      	</td>
    </tr>
    
	
</table>
<div class="div-bottom-bar">
	<input type="button" class="gbutton" id="submit" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</html:form>
</body>
<script type="text/javascript">
$("#submit").click(function(){
	var fId = $("#fId").val();
	if(!fId){
		layer.msg('请选择要开奖的局数或场次',{time: 800});
		return false;
	}
	
	var ptId = $("#pTId").val();
	if(!ptId){
		layer.msg('请选择要开奖的玩法',{time: 800});
		return false;
	}
	
	var oId = $("#oId").val();
	if(!oId){
		layer.msg('请选择开奖结果',{time: 800});
		return false;
	}
	
	$.post("fieldAction.do?method=saveOpen", 
			{'oId':oId},
			function(data){
				var code=data.code;
				if(code=='200'){
					layer.open({
						  content: data.msg,
						  yes: function(index, layero){
							  location = "matchAction.do?method=initMatch";
						  }
						});
				}else{
					layer.msg('开奖失败',{time: 800});
				}
		}, "json");
})
</script>
</html:html>