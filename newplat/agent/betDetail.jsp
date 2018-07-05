<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>海豚彩票</title>
<%@ include file = "../common/inc_file.jsp"%>

</head>
<body>
<div class="bread">投注详情  &raquo; 
</div>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
    	<th align="right">发起人</th>
		<td>
		    <bean:write name="dto" property="sponsorName" />
      	</td>
    </tr>
    <tr class="tr-color-body">
    	<th align="right">订单号</th>
		<td>
		    <bean:write name="dto" property="gaBetSponsor.orderNum" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">期号</th>
		<td>
			<bean:write name="dto" property="gaBetSponsor.sessionNo" />
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">彩种</th>
      	<td>
      		<bean:write name="dto" property="gaBetSponsor.gameName"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">订单总份数</th>
      	<td>
        	<bean:write name="dto" property="gaBetSponsor.num"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">订单总金额</th>
      	<td>
        	<font color="red"><strong><bean:write name="dto" property="gaBetSponsor.money"/></strong></font>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">倍数</th>
      	<td>
        	<bean:write name="dto" property="gaBetSponsor.multiple"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">开奖结果</th>
      	<td>
        	<bean:write name="dto" property="gaBetSponsor.openResult"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">订单状态</th>
      	<td>
     		<logic:equal name="dto" property="gaBetSponsor.winResult" value="0">
     			<ord-notpay><font color ="blue">未完成</font></ord-notpay>
			</logic:equal>
     		<logic:equal name="dto" property="gaBetSponsor.winResult" value="1">
     			<ord-pay>无效</ord-pay>
			</logic:equal>
     		<logic:equal name="dto" property="gaBetSponsor.winResult" value="2">
     			<ord-send><font color ="green">未开奖</font></ord-send>
			</logic:equal>
     		<logic:equal name="dto" property="gaBetSponsor.winResult" value="4">
     			<font color="red"><strong><ord-complete>已中奖 </ord-complete></strong></font>
			</logic:equal>
     		<logic:equal name="dto" property="gaBetSponsor.winResult" value="5">
     			<ord-closed><font color ="purple">未中奖</font></ord-closed>
			</logic:equal>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">中奖总金额</th>
      	<td>
        	<font color="red"><strong><bean:write name="dto" property="gaBetSponsor.winCash"/></strong></font>
      	</td>
    </tr>
    
   <tr class="tr-color-body">
		<th align="right">发起投注时间</th>
		<td>
			<bean:write name="dto" property="gaBetSponsor.betTime" format="yyyy-MM-dd HH:mm:ss"/>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
    	<th align="right">购买人</th>
		<td>
		    <bean:write name="dto" property="userName" />
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">购买份数</th>
      	<td>
        	<bean:write name="dto" property="gaBetPart.buyNum"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">购买金额</th>
      	<td>
        	<font color="red"><strong><bean:write name="dto" property="gaBetPart.betMoney"/></strong></font>
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">投注时间</th>
		<td>
			<bean:write name="dto" property="gaBetPart.buyTime" format="yyyy-MM-dd HH:mm:ss"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">中奖金额</th>
      	<td>
        	<font color="red"><strong><bean:write name="dto" property="gaBetPart.winCash"/></strong></font>
      	</td>
    </tr>
       <tr class="tr-color-body">
      	<th align="right">中奖积分</th>
      	<td>
        	<font color="red"><strong><bean:write name="dto" property="gaBetPart.winPoint"/></strong></font>
      	</td>
    </tr>
    
</table>
<table class="tblist" cellspacing="0" cellpadding="0" border="0">
   <tr class="tr-color-body">
		<td align="right">投注明细</td>
    </tr>

<logic:notEmpty name="list">
		<logic:iterate id="item" name="list" indexId="num">
			<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
				<td>
					<bean:write name="item" property="title"/>:&nbsp;
					<bean:write name="item" property="optionTitle"/>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
<div class="div-bottom-bar">
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</body>
</html:html>