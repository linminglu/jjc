<%@page import="com.ram.model.User"%>
<%@page import="java.util.Date"%>
<%@page import="com.framework.util.DateTimeUtil"%>
<%@page import="com.apps.util.ProductUtil"%>
<%@page import="com.apps.Constants"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.apps.eff.TradeCacheUtil"%>
<%@page import="com.game.model.GaBetDetail"%>
<%@page import="com.framework.util.ParamUtils"%>
<%@page import="com.game.model.GaSessionInfo"%>
<%@page import="com.framework.util.FSO"%>
<%@page import="java.util.List"%>
<%@page import="com.framework.util.HQUtils"%>
<%@page import="com.framework.service.impl.ServiceLocatorImpl"%>
<%@page import="com.ram.service.user.IUserService"%>
<%@page import="com.framework.service.IServiceLocator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Exec - help</title>
<style type="text/css">
red{color:red;}
</style>
</head>
<body>
<%!
IServiceLocator serviceLocator = ServiceLocatorImpl.getInstance();
IUserService userService = (IUserService)ServiceLocatorImpl.getInstance().getService("userService");

HQUtils hq = null;
List<Object> list = null;

%>

<%

//浏览器打开地址
//http://app7878.com/_help/game/repeatFix.jsp?action=check&cmd=&gameType=13&start=2018-03-16 14:00:00&end=2018-03-17 23:59:59

	String action = ParamUtils.getParameter(request,"action","noaction");

	long t1 = System.currentTimeMillis();
	
	int count = 0;
	int updateCount = 0;//更新记录数
	StringBuffer outSb = new StringBuffer();
	
	if(action.equals("check")){
		String gameType = ParamUtils.getParameter(request,"gameType","");
		String start = ParamUtils.getParameter(request,"start","");//开始时间
		String end = ParamUtils.getParameter(request,"end","");//结束时间
		String cmd = ParamUtils.getParameter(request,"cmd","");//命令
		String cmdDt = ParamUtils.getParameter(request,"cmdDt","0000");//当前时间最小到分钟
		if(ParamUtils.chkString(gameType) && ParamUtils.chkString(start) && ParamUtils.chkString(end)){
			hq = new HQUtils();
			hq.setSelect("select utd.refId,count(*),utd.cashMoney ");
			hq.addHsql("from UserTradeDetail utd where utd.createTime>'"+start+"' and utd.createTime<'"+end+"' and utd.modelType='"+gameType+"' and utd.cashType=18 group by utd.refId having count(*)>1");
			List<Object> list = userService.findObjects(hq);
			outSb.append("gameType="+gameType).append("<br>");
			outSb.append("list.size="+list.size()).append("<br>");
			
			Date now = DateTimeUtil.getJavaUtilDateNow();
			String serverNow = DateTimeUtil.dateToString(now,"yyyyMMddHHmm");
			outSb.append("cmd="+cmd).append("<br>");
			outSb.append("cmdDt="+cmdDt).append("<br>");
			outSb.append("serverNow="+serverNow).append("<br>");
			
			for(Object obj:list){
				Object[] arr = (Object[])obj;
				Integer refId = (Integer)arr[0];
				Integer repeat = (Integer)arr[1];
				BigDecimal cashMoney = (BigDecimal)arr[2];
				GaBetDetail gbd = (GaBetDetail)userService.getObject(GaBetDetail.class,refId);
				Integer userId = gbd.getUserId();
				String loginName = gbd.getLoginName();
				
				BigDecimal userMoney = TradeCacheUtil.getUserMoney(userId);//用户当前余额
				User u = (User)userService.getObject(User.class,userId);
				
				boolean isExec = false;
				boolean isHasMoney = userMoney.doubleValue()>cashMoney.doubleValue()?true:false;
				
				//当cmd==exec和时间分钟相匹配时，执行
				if(cmd.equals("exec") && cmdDt.equals(serverNow) && isHasMoney){
					   userService.saveTradeDetail(u,userId,
							Constants.TRADE_TYPE_PAY,//支出
							Constants.CASH_TYPE_CASH_OTHER_CHARGE,//其他扣款
							cashMoney,//扣除金额
							refId,//引用id
							gameType,
							"其他扣款[处理]",
							"",
							u.getUserType(),
							loginName);  
					isExec = true; 
				}
				
				outSb.append("refId|repeat|uid,un,cashMoney|userMoney <=> "+refId+"|<red>"+repeat+"</red>,"+userId+","+loginName+",<red>"+cashMoney+"</red>|<red>"+userMoney+"</red>[isExec="+isExec+"]>扣款余额="+TradeCacheUtil.getUserMoney(userId)).append(!isHasMoney?"<red>余额不够</red>":"").append("<br>");
				
				
			}
		}else{
			outSb.append("参数不对...");
		}
		
		
	}else{
		outSb.append("nothing...");
	}

	
	long t2 = System.currentTimeMillis();
	
	out.println(outSb.toString());
%>

</body>
</html>