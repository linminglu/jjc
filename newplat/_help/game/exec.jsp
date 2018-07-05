<%@page import="com.game.service.IGaService"%>
<%@page import="com.mchange.v2.resourcepool.ResourcePool.Manager"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.game.model.GaBetOption"%>
<%@page import="com.ram.model.User"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.card.model.RechargeWay"%>
<%@page import="com.framework.util.DateTimeUtil"%>
<%@page import="help.pay.lingdian.Utils.Md5Util"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.apps.model.Param"%>
<%@page import="com.apps.eff.CacheUtil"%>
<%@page import="com.apps.eff.GameHelpUtil"%>
<%@page import="com.game.model.GaSessionInfo"%>
<%@page import="com.apps.Constants"%>
<%@page import="com.framework.util.ParamUtils"%>
<%@page import="com.framework.util.ManageFile"%>
<%@page import="com.framework.util.FSO"%>
<%@page import="com.apps.model.Advertising"%>
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
</head>
<body>
<%!
IServiceLocator serviceLocator = ServiceLocatorImpl.getInstance();
IUserService userService = (IUserService)ServiceLocatorImpl.getInstance().getService("userService");
IGaService gaService = (IGaService)ServiceLocatorImpl.getInstance().getService("gaService");

HQUtils hq = null;
List<Object> list = null;

String checkPath(String pagePath,String key){
	pagePath = pagePath.replace("/_template", "");
	String createPath = pagePath.replace(key, "");
	try {//检查并创建对应的目录
		new FSO().createFolder(createPath);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return pagePath;
}
String getBetHomeHtml(String playCate){
	StringBuffer sb = new StringBuffer();
	hq = new HQUtils("from GaSessionInfo a where a.status=1 and a.playCate=? order by a.sort");
	hq.addPars(playCate);
	list = userService.findObjects(hq);
	
	for(Object obj:list){
		GaSessionInfo gsi = (GaSessionInfo)obj;
		sb.append("<li class=\"mui-table-view-cell\" data=\""+gsi.getGameCode()+","+gsi.getGameType()+"\">");
		sb.append("<a href=\"/game/"+(playCate.equals("2")?"xy":"gf")+"/"+gsi.getGameCode()+"\" class=\"mui-navigate-right\">");
		sb.append("<div class=\"logo-box\">");
		sb.append("<img class=\"lazy\" data-original=\""+gsi.getImg()+"\" src=\""+gsi.getImg()+"\">");
		sb.append("</div>");
		sb.append("<div class=\"title-box\"><div class=\"title\">"+gsi.getGameTitle()+"</div><div class=\"subtitle\" id=\"sbtitle_"+gsi.getGameType()+"\">第"+gsi.getLatestSessionNo()+"期</div><div id=\"openNum_"+gsi.getGameType()+"\"></div></div>");
		sb.append("<span class=\"timer-show\" id=\"timerShow_"+gsi.getGameType()+"\"></span>");
		sb.append("</a>");
		sb.append("</li>");
	}
	
	return sb.toString();
}
String getHomeHtml(String playCate){
	StringBuffer sb = new StringBuffer();
	hq = new HQUtils("from GaSessionInfo a where a.status=1 and a.playCate=? order by a.sort");
	hq.addPars(playCate);
	list = userService.findObjects(hq);
	int siz = list.size();
	for(Object obj:list){
		GaSessionInfo gsi = (GaSessionInfo)obj;
		sb.append("<li class=\"caizhong-cell\">");
		sb.append("<a href=\"/game/"+(playCate.equals("2")?"xy":"gf")+"/"+gsi.getGameCode()+"\">");
		sb.append("<div class=\"ico\">");
		sb.append("<img class=\"lazy\" data-original=\""+gsi.getImg()+"\">");
		sb.append("</div>");
		sb.append("<div class=\"name-box\"><div class=\"title\">"+gsi.getGameTitle()+"</div><div class=\"subtitle\" id=\"subtitle_"+gsi.getGameType()+"\"></div></div>");
		sb.append("</a>");
		sb.append("</li>");
	}
	//补齐
	if(siz%3>0){
		int looa = 3-siz%3;
		for(int i=0;i<looa;i++){
			sb.append("<li class=\"caizhong-cell\">");
			sb.append("<a href=\"javascript:void(0)\">");
			sb.append("<div class=\"ico\">");
			sb.append("<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFMDk4MzVGQjBCMUYxMUU4QTBBQTkyRjM0N0VEOTU2MSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFMDk4MzVGQzBCMUYxMUU4QTBBQTkyRjM0N0VEOTU2MSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkUwOTgzNUY5MEIxRjExRThBMEFBOTJGMzQ3RUQ5NTYxIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkUwOTgzNUZBMEIxRjExRThBMEFBOTJGMzQ3RUQ5NTYxIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+bknQowAAABBJREFUeNpi/v//PwNAgAEACQsDAUdpTjcAAAAASUVORK5CYII=\">");
			sb.append("</div>");
			sb.append("<div class=\"name-box\"><div class=\"title\">&nbsp;</div><div class=\"subtitle\">&nbsp;</div></div>");
			sb.append("</a>");
			sb.append("</li>");
		}
	}
	return sb.toString();
}
%>

<%

	String action = ParamUtils.getParameter(request,"action","noaction");

	long t1 = System.currentTimeMillis();
	
	int count = 0;
	int updateCount = 0;//更新记录数
	StringBuffer outSb = new StringBuffer();
	outSb.append(action).append("<br>");
	
	if(action.equals("updateStaticHtml")){
		
		String _htmlData = "";//模板html
		
		String rootPath = Constants.getWebRootPath();
		String htmlPath = rootPath+"/game/_template";
		
		String pagePath = "";//待生成页面
		
		//更新首页
		pagePath = htmlPath+"/index.html";
		_htmlData = ManageFile.loadTextFileUTF8(pagePath);
		_htmlData = _htmlData.replace("{#xyList#}", getHomeHtml("2"));
		_htmlData = _htmlData.replace("{#gfList#}", getHomeHtml("1"));
		ManageFile.writeUTF8File(checkPath(pagePath,"index.html"),_htmlData);//更新
		updateCount++;
		
		//更新下注页面
		pagePath = htmlPath+"/xiazhu.html";
		_htmlData = ManageFile.loadTextFileUTF8(pagePath);
		_htmlData = _htmlData.replace("{#xyList#}", getBetHomeHtml("2"));
		_htmlData = _htmlData.replace("{#gfList#}", getBetHomeHtml("1"));
		ManageFile.writeUTF8File(checkPath(pagePath,"xiazhu.html"),_htmlData);//更新
		updateCount++;
	}
	else if(action.equals("setGameHelpUtil.logLevel")){
		String key = ParamUtils.getParameter(request,"key","");
		String value = ParamUtils.getParameter(request,"value","");
		if(key.length()>0 && value.length()>0){
			GameHelpUtil.setLogLevel(key,value);
		}
		outSb.append("setGameHelpUtil.logLevel>"+key+"="+GameHelpUtil.getLogLevel(key)).append("<br>");
		for(String k:GameHelpUtil.logLeveLMap.keySet()){
			outSb.append(k+"="+GameHelpUtil.logLeveLMap.get(k)).append("<br>");
		}
	}else if(action.equals("setGameHelpUtil.keyFilter")){
		String key = ParamUtils.getParameter(request,"key","");
		if(key.length()>0){
			GameHelpUtil.KEY_FILTER = key;
		}else{
			GameHelpUtil.KEY_FILTER = "*";
		}
		outSb.append("setGameHelpUtil.keyFilter>"+GameHelpUtil.KEY_FILTER).append("<br>");
		
	}else if(action.equals("buildBetOptionJSON")){
		HQUtils hq = new HQUtils();
		hq.addHsql(" from GaBetOption a where a.gameType>100 order by a.gameType,a.betOptionId");
		List<Object> list = userService.findObjects(hq);
		JSONArray optArr = new JSONArray();
		for(Object obj:list){
			GaBetOption opt = (GaBetOption)obj;
			JSONObject jo = new JSONObject();
			jo.put("id",opt.getBetOptionId());
			jo.put("gt",opt.getGameType());
			jo.put("pt",opt.getPlayType());
			jo.put("oti",opt.getOptionTitle());
			jo.put("ti",opt.getTitle());
			optArr.put(jo);
			count++;
		}
		ManageFile.writeUTF8File("D:/gfxy.betOption.json",optArr.toString());
		outSb.append("buildBetOptionJSON>"+count).append("<br>");
			
	}else if(action.equals("getParamType")){
		String key = ParamUtils.getParameter(request,"key","");
		if(key.length()>0){
			String paraVal = CacheUtil.getParamValue(key);
			outSb.append("setGameHelpUtil.getParamType>"+key+"="+paraVal).append("<br>");
		}else{
			outSb.append("setGameHelpUtil.getParamType>"+key+"=").append("<br>");
		}
	}else if(action.equals("showOpening")){
		
		outSb.append("GameHelpUtil.opening>"+GameHelpUtil.checkOpening("152","20180507691")).append("<br>");
		
	}else if(action.equals("updateIndexCSS")){
		String key = ParamUtils.getParameter(request,"v","");
		if(key.length()>0){
			
		}
	} else if (action.equals("buildGuestUser")) {// 生成试玩帐号
		User user = userService.getUserByLoginName("guest1");
		if (user == null) {
			User regUser = null;
			for (int i = 0;i < 10;i++) {
				regUser = new User();
				regUser.setLoginName("guest"+(i + 1)); 
				regUser.setUserName("guest"+(i + 1));
				regUser.setPassword(Md5Util.md5("123456", "utf-8").toUpperCase());
				regUser.setUserType(Constants.USER_TYPE_TEST);
				regUser.setStatus(Constants.PUB_STATUS_OPEN);
				regUser.setIsBetBack(Constants.PUB_STATUS_CLOSE);
				regUser.setRegistDateTime(DateTimeUtil.getJavaUtilDateNow());
				regUser.setLoginTimes(1);//注册完跳转登录1次
				regUser.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
				regUser.setLimitBetBack(new BigDecimal(0));
				regUser.setLimitRecharge(new BigDecimal(0));
				
				// 设置用户的充值通道，默认第一个
				HQUtils hq = new HQUtils();
				hq.addHsql("from RechargeWay rw where rw.type=? ");
				hq.addPars(Constants.RECHARGE_WAY_1);
				List<Object> rwList = userService.findObjects(hq);
				if (rwList != null && rwList.size() > 0) {
					RechargeWay r = (RechargeWay)rwList.get(0);
					regUser.setRechargeWay(r.getId());
				}
				regUser.setMoney(new BigDecimal(0));//余额
				regUser.setUserpoints(new BigDecimal(0));//积分
				userService.saveObject(regUser, null);
				userService.saveUserRegSendMoney(regUser, new BigDecimal(2000));
			}
			outSb.append("试玩用户生成成功！");
		} else {
			outSb.append("请勿重复生成！");
		}
	}else if(action.equals("updateMarkSixOption.PlayAlias")){//更新六合彩类 玩法别名，用于缓存名称
		hq = new HQUtils();
		hq.addHsql(" from GaBetOption gbo where gbo.gameType=359");
		List<Object> list = userService.findObjects(hq);
		outSb.append("size="+list.size()).append("<br>");
		for(Object obj:list){
			GaBetOption gbo = (GaBetOption)obj;
			//gbo.setPlayAlias("markSix"+gbo.getPlayType());
		}
		//userService.updateObjectList(list,null);
	}else if(action.equals("dayBetCount.test")){
		String statDay = ParamUtils.getParameter(request,"statDay");
		if(ParamUtils.chkString(statDay)){
			gaService.updateDayBetCount(statDay);
		}
		outSb.append("dayBetCount="+statDay).append("<br>");
		
	}else if(action.equals("dayUserBetCount.test")){
		String statDay = ParamUtils.getParameter(request,"statDay");
		if(ParamUtils.chkString(statDay)){
			gaService.updateUserBetCount(statDay);
		}
		outSb.append("dayUserBetCount="+statDay).append("<br>");
		
	}else if(action.equals("updateDailyStatistics.test")){
		String statDay = ParamUtils.getParameter(request,"statDay");
		if(ParamUtils.chkString(statDay)){
			gaService.updateDailyStatistics(statDay);
		}else{
			gaService.updateDailyStatistics(null);
		}
		outSb.append("updateDailyStatistics="+statDay).append("<br>");
	}

	else if(action.equals("test")){
		//gaService.updateUserGains();
	}
	
	long t2 = System.currentTimeMillis();
	
	//String returns = "__________finshed____>"+((t2-t1)/1000)+"s________共更新："+updateCount+"________________________";
	out.println(outSb.toString());
%>

</body>
</html>