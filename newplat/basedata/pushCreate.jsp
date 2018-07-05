<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
function formSubmit(){
	var frm = document.baseDataForm;
	if(frm["push.deviceType"].value.trim().length==0){
		alert("请选择设备类型");
		frm["push.deviceType"].focus();
		return false;
	}
	var type=frm["push.type"].value;
	
	if(frm["push.type"].value.trim().length==0){
		alert("请选择消息类型");
		frm["push.type"].focus();
		return false;
	}
	if(type=='2'){
		if(frm["push.content"].value.trim().length==0){
			alert("请填写网址");
			frm["push.content"].focus();
			return false;
		}
	}
	if(type=='3'){
		if(frm["push.infoId"].value.trim().length==0){
			alert("请选择商家");
			frm["push.infoId"].focus();
			return false;
		}
	}
	if(frm["push.title"].value.trim().length==0){
		alert("请输入标题");
		frm["push.title"].focus();
		return false;
	}
	return true;
}
function selectType(val){
	if(val=='1'||val=='2'){
	      if(val=='2'){
	      $('#contentid').show();
	      $('#sellerid').hide();
	      }
	      if(val=='1'){
	      	$('#sellerid').hide();
	      	$('#contentid').hide();
	      }
	}else{
		$('#sellerid').show();
		$('#contentid').hide();
	}
}

</script>
</head>
<body>
<div class="bread">推送管理  &raquo; 
<logic:empty name="baseDataForm" property="push.pushId">新增</logic:empty>
<logic:notEmpty name="baseDataForm" property="push.pushId">修改</logic:notEmpty>
</div>
<html:form action="/baseDataAction.do?method=savePush" onsubmit="return formSubmit();" >
<html:hidden name="baseDataForm" property="push.pushId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">设备类型</th>
		<td>
        	<html:select property="push.deviceType" name="baseDataForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">IOS+Android</html:option>
				<html:option value="2">Android</html:option>
				<html:option value="3">IOS</html:option>
			</html:select>
      	</td>
    </tr>
	<tr class="tr-color-body">
		<th align="right">消息类型</th>
		<td>
        	<html:select property="push.type" name="baseDataForm" onchange="selectType(this.value);">
				<html:option value="">请选择</html:option>
				<html:option value="1">普通消息</html:option>
				<html:option value="2">网页</html:option>
<%-- 				<html:option value="3">商家主页</html:option> --%>
			</html:select>
      	</td>
    </tr>
	<tr class="tr-color-body" id="sellerid" style="display: none;">
		<th align="right">商家</th>
		<td>
        	<html:select property="push.infoId" name="baseDataForm">
				<html:option value="">请选择</html:option>
	      	<logic:notEmpty name="sellerList">
      			<html:options  collection="sellerList"  property="sid" labelProperty="title"/>
	      	</logic:notEmpty>
      		</html:select>
      	</td>
    </tr>
	<tr class="tr-color-body" id="contentid" style="display: none;">
		<th align="right">网 址</th>
		<td>
		  <html:text name="baseDataForm" property="push.url" />
      	</td>
    </tr>
	<tr class="tr-color-body">
		<th align="right">标题</th>
		<td>
        	<html:text name="baseDataForm" property="push.title" maxlength="20"/>
      	</td>
    </tr>
	<tr class="tr-color-body">
		<th align="right">内容</th>
		<td>
			<html:textarea name="baseDataForm" property="push.content" rows="3" cols="60" ></html:textarea>
      	</td>
    </tr>
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</html:form>
</body>
</html:html>