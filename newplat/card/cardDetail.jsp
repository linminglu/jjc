<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/inc_datepicker.jsp"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>充值卡密码详情</title>
<%@ include file="../common/inc_file.jsp"%>
<%@ include file="../common/inc_ueditor.jsp"%>
</head>
<body>
	<div class="bread">商品管理 &raquo; 充值密码&raquo; 充值密码详情</div>
	<html:form action="cardAction.do?method=generateCardMain">
		<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">

			<tr class="tr-color-body">
				<th align="right">协议号：</th>
				<td><bean:write name="cardDTO" property="card.protocolCode" />
				</td>
			</tr>
			<tr class="tr-color-body">
				<th align="right">批次号：</th>
				<td><bean:write name="cardDTO" property="card.batchCode" />
				</td>
			</tr>

			<tr class="tr-color-body" id="ttype">
				<th align="right">面额：</th>
				<td><bean:write name="cardDTO" property="card.cardAmount" />元

				</td>
			</tr>
			<tr class="tr-color-body" id="ttype">
				<th align="right">售价：</th>
				<td><bean:write name="cardForm" property="price"  format="0"/>元

				</td>
			</tr>
			<tr class="tr-color-body">
				<th align="right">起始有效期：</th>
				<td><bean:write name="cardDTO" property="card.startDate"
						format="yyyy-MM-dd" /></td>
			</tr>
			<tr class="tr-color-body">
				<th align="right">截止有效期：</th>
				<td><bean:write name="cardDTO" property="card.endDate"
						format="yyyy-MM-dd" />
				</td>
			</tr>
			<tr class="tr-color-body">
				<th align="right">状态：</th>
				<td><c:if test="${cardDTO.card.cardStatus == '0'}">注销</c:if> <c:if
						test="${cardDTO.card.cardStatus == '1'}">未激活</c:if> <c:if
						test="${cardDTO.card.cardStatus == '2'}">已激活</c:if>
				</td>
			</tr>
			<tr class="tr-color-body">
				<th align="right">创建人：</th>
				<td><bean:write name="cardDTO" property="createUser.loginName" />
				</td>
			</tr>
			<tr class="tr-color-body">
				<th align="right">创建时间：</th>
				<td><bean:write name="cardDTO" property="card.createDate"
						format="yyyy-MM-dd" />
				</td>
			</tr>
			<tr class="tr-color-body">
				<th align="right">备注：</th>
				<td><html:textarea property="cardGeneLog.remark" rows="5"
						cols="60" readonly="true" />
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>