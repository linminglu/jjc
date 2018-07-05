package com.apps.eff;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.apps.Constants;
import com.apps.eff.dto.SessionItem;
import com.apps.eff.dto.ValueConfig;
import com.apps.eff.dto.ValueItem;
import com.framework.util.DateTimeUtil;
import com.framework.util.ManageFile;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.util.xml.XmlUtil;
import com.game.model.GaSessionInfo;
import com.gf.ssc.bjssc.util.BjSscUtil;
import com.ram.util.URLUtil;
import com.xy.bj3.service.impl.Bj3OpenResult;
import com.xy.hk.sflhc.util.SflhcUtil;
import com.xy.pk10.sfpk10.util.SfPk10Util;

/**
 * Game模块辅助类
 * 开奖接口请求，独立日志保存
 */
public class GameHelpUtil {
	//public static Logger log = Logger.getLogger(GameHelpUtil.class);
//	private final static IUserService userService = (IUserService) ServiceLocatorImpl.getInstance().getService("userService");

//	/**
//	 * 已开奖最新期号(用于系统开奖的彩种)
//	 */
//	public static Map<String,String> openMap = new HashMap<String, String>();
//	public static void setOpenSessionNo(String gameType,String sessionNo){
//		openMap.put("opened_"+gameType, sessionNo);
//	}
//	public static String getOpenSessionNo(String gameType){
//		return openMap.get("opened_"+gameType);
//	}
	
	/**
	 * 检查彩种指定期号是否正开奖中[true=开奖中][false=未开奖]
	 * @param gameType
	 * @param sessionNo
	 * @return true/false
	 */
	public static Map<String,Boolean> openMap = new HashMap<String, Boolean>();
	public static void setOpening(String gameType,String sessionNo){
		//openMap.put(gameType, sessionNo+"-opening");
		openMap.put(gameType+"_"+sessionNo,true);
		//GameHelpUtil.log(Constants.getGameCodeOfGameType(gameType),"opening=true "+gameType+","+sessionNo);
	}
	public static boolean checkOpening(String gameType,String sessionNo){
		Boolean val = openMap.get(gameType+"_"+sessionNo);
		if(val==null) val = false;
		//GameHelpUtil.log(Constants.getGameCodeOfGameType(gameType),"checking ..... ["+sessionNo+"],opening="+val);
		return val;
	}
	public static void removeOpening(String gameType,String sessionNo){
		openMap.remove(gameType+"_"+sessionNo);
		GameHelpUtil.log(Constants.getGameCodeOfGameType(gameType),"removing opening flag ..... ["+sessionNo+"]");
	}
	
	/**
	 * 日志调用级别设置
	 * Map<String,String><gameAlias,level>
	 * gameAlias=gameType 
	 * level=[P=打印 W=写日志 PW=二者]
	 */
	public static Map<String,String>_logLeveLMap = new HashMap<String, String>();
	public static String getLogLevel(String gameCode){
		String level = _logLeveLMap.get("key_"+gameCode);
		return level!=null?level:"PW";
	}
	public static void setLogLevel(String gameAlias,String level){
		_logLeveLMap.put("key_"+gameAlias, level);
	}
	
	public static String KEY_FILTER = "*";
	public static boolean checkLogFilterHas(String logs){
		if(KEY_FILTER!=null && KEY_FILTER.length()>1){
			String[] arr = KEY_FILTER.split(",");
			for(int i=0;i<arr.length;i++){
				if(logs.contains(arr[i])) return true;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 开奖定时器，一个gameType只同时执行一个Timer
	 * 每次设置时先清除上次的
	 */
	public static Map<String,Timer> fetchTimerMap = new HashMap<String, Timer>();
	public static void setFetchTimerMap(String gameType,Timer fetchTimer){
		//先清除之前定时器
		Timer timer = (Timer)fetchTimerMap.get("fetchTimer_"+gameType);
		if(timer!=null){
			timer.cancel();
			timer=null;
			//GameHelpUtil.log(Constants.getGameCodeOfGameType(gameType),"previous fetch timer clear .....");
		}
		//重新设定计时器
		fetchTimerMap.put("fetchTimer_"+gameType, fetchTimer);
	}
	public static Timer getFetchTimerMap(String gameType){
		return (Timer)fetchTimerMap.get("fetchTimer_"+gameType);
	}
//	public static void clearFetchTimerMap(String gameType){
//		Timer timer = (Timer)fetchTimerMap.get("fetchTimer_"+gameType);
//		if(timer!=null){
//			timer.cancel();
//			timer=null;
//			GameHelpUtil.log(Constants.getGameCodeOfGameType(gameType),"previous fetch timer clear .....");
//		}
//	}
	
	/**
	 * 获取彩种最大抓取次数
	 * @param gameType
	 * @return
	 */
	public static int getFetchMaxCount(String gameType){
		if (Constants.GAME_TYPE_GF_XJSSC.equals(gameType)) return 100;
		else if (Constants.GAME_TYPE_GF_JLK3.equals(gameType)) return 200;
		else return 80;
	}
	
	/**
	 * 获取抓取时间间隔(毫秒)
	 * @param gameType
	 * @return
	 */
	public static int getFetchInterval(String gameType){
		return 5000;
	}
	
	/**
	 * 检测是否在运行时间内
	 * 默认超过10分钟后就不执行
	 * @param gameType
	 * @return
	 */
	public static boolean checkTimerRange(String gameType){
		return checkTimerRange(gameType,DateTimeUtil.getJavaUtilDateNow());
	}
	public static boolean checkTimerRange(String gameType,Date now){
		if(now==null) now = DateTimeUtil.getJavaUtilDateNow();
		Integer nowHHmmss = Integer.valueOf(DateTimeUtil.dateToString(now, "HHmmss"));
		if(gameType.equals(Constants.GAME_TYPE_XY_BJPK10) 
				|| gameType.equals(Constants.GAME_TYPE_GF_BJPK10)){
			if(nowHHmmss<Integer.valueOf("090700")){//大于第一期时再开
				return false;
			}
		}else if(gameType.equals(Constants.GAME_TYPE_XY_XYFT) 
				|| gameType.equals(Constants.GAME_TYPE_GF_XYFT)){
			if(nowHHmmss>Integer.valueOf("130000") && nowHHmmss<Integer.valueOf("130900")){
				return false;
			}
		}else if(gameType.equals(Constants.GAME_TYPE_XY_GDPICK11)
				|| gameType.equals(Constants.GAME_TYPE_GF_GDPICK11)
				|| gameType.equals(Constants.GAME_TYPE_XY_JXPICK11)
				|| gameType.equals(Constants.GAME_TYPE_GF_JXPICK11)
			){
			if(nowHHmmss<Integer.valueOf("091000")){//大于第一期时再开
				return false;
			}
		}else if(gameType.equals(Constants.GAME_TYPE_XY_GDK10)
				|| gameType.equals(Constants.GAME_TYPE_GF_GDK10)
			){
			if(nowHHmmss<Integer.valueOf("091000")){//大于第一期时再开
				return false;
			}
		}else if(gameType.equals(Constants.GAME_TYPE_XY_XJSSC)
				|| gameType.equals(Constants.GAME_TYPE_GF_XJSSC)
			){
			if(nowHHmmss>=Integer.valueOf("100000") && nowHHmmss<Integer.valueOf("101000")){//大于第一期时再开
				return false;
			}
		}else if(gameType.equals(Constants.GAME_TYPE_XY_TJSSC)
				|| gameType.equals(Constants.GAME_TYPE_GF_TJSSC)
			){
			if(nowHHmmss<=Integer.valueOf("091200")){//大于第一期时再开
				return false;
			}
		}else if(gameType.equals(Constants.GAME_TYPE_XY_BJK3)
				|| gameType.equals(Constants.GAME_TYPE_GF_BJK3)
			){
			if(nowHHmmss<Integer.valueOf("091000")){//大于第一期时再开
				return false;
			}
		}else if(gameType.equals(Constants.GAME_TYPE_XY_GXK3)
				|| gameType.equals(Constants.GAME_TYPE_GF_GXK3)
			){
			if(nowHHmmss<Integer.valueOf("093700") || nowHHmmss>Integer.valueOf("233700")){
				return false;
			}
		}else if(gameType.equals(Constants.GAME_TYPE_XY_BJK3)
				|| gameType.equals(Constants.GAME_TYPE_GF_BJK3)
			){
			if(nowHHmmss<Integer.valueOf("091000")){//大于第一期时再开
				return false;
			}
		}else if(gameType.equals(Constants.GAME_TYPE_XY_BJK3)
				|| gameType.equals(Constants.GAME_TYPE_GF_BJK3)
			){
			if(nowHHmmss>Integer.valueOf("223800")){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 延迟开奖秒数
	 * @param gameType
	 * @return
	 */
	public static int getDelayOpenTime(String gameType){
		if(gameType.equals(Constants.GAME_TYPE_XY_SFPK10)){
			//return SfPk10Util.getRandomSecond();
			return StringUtil.getRandomInt(5, 10);
		}else if(gameType.equals(Constants.GAME_TYPE_XY_BJ3)){
			return StringUtil.getRandomInt(5, 10);
		}
		return StringUtil.getRandomInt(10, 30);
	}
	
	public static Map<String, SessionItem> getOpenResult(GaSessionInfo gsi,String curSessionNo,Date openTime,int delayOpenTime){
		return null;
	}
	
	/**
	 * 抓取开奖结果
	 * @param gsi
	 * @param openSessionNo
	 * @return
	 */
	public static Map<String, SessionItem> getOpenResultThird(GaSessionInfo gsi,String openSessionNo){
		if(gsi==null) return null;
		String gameCode = Constants.getGFXYGameCode(gsi.getGameCode(), gsi.getPlayCate());
		
		Map<String, SessionItem> sessionNoMap = new HashMap<String, SessionItem>();//接口期号
		
		String urlSwitch = gsi.getUrlSwitch();//三方开奖接口
		String officalURL = urlSwitch.equals("1")?gsi.getKaicaiUrl():gsi.getCaipiaoUrl();// 1=开彩网 2=彩票控
		
		String openSNo = openSessionNo;//当前要开奖的期号
		
		//GameHelpUtil.log(gameCode,officalURL);
		
		String resultXML = URLUtil.HttpRequestUTF8(officalURL);//获取数据
		
		//GameHelpUtil.log(gameCode,resultXML);
		
		if (ParamUtils.chkString(resultXML)) {
			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);

			if (urlSwitch.equals("1")){// 1=开彩网 2=彩票控
				NodeList nList = xmlDoc.getElementsByTagName("row");
				String sessionNo = "";// 场次号
				String result = "";// 开奖结果5组数字
				String time = ""; // 开奖时间
				for (int i = 0; i < nList.getLength(); i++) {
					Node node = nList.item(i);
					// 数据库来源开彩网
					sessionNo = XmlUtil.getElementAttribute((Element) node,"expect");
					result = XmlUtil.getElementAttribute((Element) node,"opencode");
					time = XmlUtil.getElementAttribute((Element) node,"opentime");
					
					if(sessionNo.length()>openSNo.length()){//本地期号年份为yy,接口为yyyy
						sessionNo=sessionNo.substring(2,sessionNo.length());//2017041225 去掉前两位
					}
					if(sessionNo.length()>openSNo.length()){//长度还大，流水保留两位
						sessionNo = sessionNo.substring(0,sessionNo.length()-3)+sessionNo.substring(7,sessionNo.length());
					}
					if(i==0 && !sessionNo.equals(openSNo)){//没有最新开奖结果reutrn null
						return null;
					}
					
					sessionNoMap.put(sessionNo, new SessionItem(result,time));//放入接口所有期号
				}
			} else if (urlSwitch.equals("2")) {// 彩票控
				// 开始解析场次开奖数据、
				String sessionNo = "";// 场次号
				String result = "";// 开奖结果5组数字
				String time = "";
				NodeList nList = xmlDoc.getElementsByTagName("item");
				for (int i = 0; i < nList.getLength(); i++) {
					Node node = nList.item(i);
					sessionNo = XmlUtil.getElementAttribute((Element) node,"id");
					result = XmlUtil.getNodeTextValue(node);
					time = XmlUtil.getElementAttribute((Element) node,"dateline");
					if(sessionNo.length()>openSNo.length()){//本地期号年份为yy,接口为yyyy
						sessionNo=sessionNo.substring(sessionNo.length()-8);//2017041225 去掉前两位
					}
					if(i==0 && !sessionNo.equals(openSNo)){//没有最新开奖结果reutrn null
						return null;
					}
					
					sessionNoMap.put(sessionNo, new SessionItem(result,time));//放入接口所有期号
				}
			}
			
		}
		GameHelpUtil.log(gameCode,"RESULTS  _____ ["+openSNo+"]=>"+((SessionItem)sessionNoMap.get(openSNo)).getResult());
		return sessionNoMap;
	}
	
	/**
	 * 抓取开奖结果
	 * @param sessionInfo
	 * @param curSessionNo 当前期号
	 * @return Map<String, SessionItem>(期号key, 期号dto(openResult,openTime))
	 */
	public static Map<String, SessionItem> getOpenResultTemp(GaSessionInfo gsi,String openSessionNo){
		if(gsi==null) return null;

		String gameCode = Constants.getGFXYGameCode(gsi.getGameCode(), gsi.getPlayCate());
		
		if(openSessionNo==null || openSessionNo.length()<5){
			GameHelpUtil.log(gameCode, "not found curSessionNo[检查期号是否生成与正确]");
		}
		
		Map<String, SessionItem> sessionNoMap = new HashMap<String, SessionItem>();//接口期号
		
		String urlSwitch = gsi.getUrlSwitch();//三方开奖接口
		String officalURL = urlSwitch.equals("1")?gsi.getKaicaiUrl():gsi.getCaipiaoUrl();// 1=开彩网 2=彩票控
		
		String openSNo = openSessionNo;//(Long.parseLong(curSessionNo)-1)+"";//当前要开奖的期号
		
		GameHelpUtil.log(gameCode,officalURL);
		
		String resultXML = URLUtil.HttpRequestUTF8(officalURL);//获取数据
		
		GameHelpUtil.log(gameCode,resultXML);
		
		if (ParamUtils.chkString(resultXML)) {
			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);

			if (urlSwitch.equals("1")){// 1=开彩网 2=彩票控
				NodeList nList = xmlDoc.getElementsByTagName("row");
				String sessionNo = "";// 场次号
				String result = "";// 开奖结果5组数字
				String time = ""; // 开奖时间
				for (int i = 0; i < nList.getLength(); i++) {
					Node node = nList.item(i);
					// 数据库来源开彩网
					sessionNo = XmlUtil.getElementAttribute((Element) node,"expect");
					result = XmlUtil.getElementAttribute((Element) node,"opencode");
					time = XmlUtil.getElementAttribute((Element) node,"opentime");
					
					if(sessionNo.length()>openSNo.length()){//本地期号年份为yy,接口为yyyy
						sessionNo=sessionNo.substring(2,sessionNo.length());//2017041225 去掉前两位
					}
					if(sessionNo.length()>openSNo.length()){//长度还大，流水保留两位
						sessionNo = sessionNo.substring(0,sessionNo.length()-3)+sessionNo.substring(7,sessionNo.length());
					}
					GameHelpUtil.log(gameCode,"当前期号："+openSNo+",开奖接口最新期号："+sessionNo);
					if(i==0 && !sessionNo.equals(openSNo)){//没有最新开奖结果reutrn null
						return null;
					}
					
					sessionNoMap.put(sessionNo, new SessionItem(result,time));//放入接口所有期号
				}
			} else if (urlSwitch.equals("2")) {// 彩票控
				// 开始解析场次开奖数据、
				String sessionNo = "";// 场次号
				String result = "";// 开奖结果5组数字
				String time = "";
				NodeList nList = xmlDoc.getElementsByTagName("item");
				for (int i = 0; i < nList.getLength(); i++) {
					Node node = nList.item(i);
					sessionNo = XmlUtil.getElementAttribute((Element) node,"id");
					result = XmlUtil.getNodeTextValue(node);
					time = XmlUtil.getElementAttribute((Element) node,"dateline");
					if(sessionNo.length()>openSNo.length()){//本地期号年份为yy,接口为yyyy
						sessionNo=sessionNo.substring(sessionNo.length()-8);//2017041225 去掉前两位
					}
					if(i==0 && !sessionNo.equals(openSNo)){//没有最新开奖结果reutrn null
						return null;
					}
					
					sessionNoMap.put(sessionNo, new SessionItem(result,time));//放入接口所有期号
				}
			}
			
		}
		GameHelpUtil.log(gameCode,"RESULTS  _____ ["+openSNo+"]=>"+((SessionItem)sessionNoMap.get(openSNo)).getResult());
		return sessionNoMap;
	}
	
	/**
	 * 系统开奖
	 * @param gsi
	 * @param openSessionNo
	 * @param openTime
	 * @return
	 */
	public static Map<String, SessionItem> getOpenResultSys(GaSessionInfo gsi,String openSessionNo,Date openTime){
		if(gsi==null) return null;

		String gameCode = Constants.getGFXYGameCode(gsi.getGameCode(), gsi.getPlayCate());
		
		Map<String, SessionItem> sessionNoMap = new HashMap<String, SessionItem>();//接口期号
		
		String result = getRandomResult(gsi.getGameType());//随机开奖
		if(result!=null){
			sessionNoMap.put(openSessionNo, new SessionItem(result,DateTimeUtil.DateToStringAll(openTime)));
			GameHelpUtil.log(gameCode,"RESULTS  _____ ["+openSessionNo+"]=>"+result);
			return sessionNoMap;
		}else{
			GameHelpUtil.log(gameCode,"sys open invalid result["+openSessionNo+"]=>"+result+"");
			return null;
		}
		
	}
	
	/**
	 * 随机开奖
	 * @param gameType
	 * @return
	 */
	public static String getRandomResult(String gameType){
		if(gameType.equals(Constants.GAME_TYPE_XY_SFPK10) 
		|| gameType.equals(Constants.GAME_TYPE_GF_SFPK10)
		|| gameType.equals(Constants.GAME_TYPE_XY_SFPK102)
		|| gameType.equals(Constants.GAME_TYPE_GF_SFPK102)
		|| gameType.equals(Constants.GAME_TYPE_XY_JSFT)
		|| gameType.equals(Constants.GAME_TYPE_GF_JSFT)
		){
			
			return SfPk10Util.getRandomResult(1);
			
		}else if(gameType.equals(Constants.GAME_TYPE_XY_BJ3)
		|| gameType.equals(Constants.GAME_TYPE_GF_THREE)
		|| gameType.equals(Constants.GAME_TYPE_XY_FC)
		|| gameType.equals(Constants.GAME_TYPE_GF_FC)
		|| gameType.equals(Constants.GAME_TYPE_GF_THREE)
		){
			return Bj3OpenResult.getRandomResult();
			
		}else if(gameType.equals(Constants.GAME_TYPE_XY_BJSSC)
		|| gameType.equals(Constants.GAME_TYPE_GF_BJSSC)
		){
			
			return BjSscUtil.getRandomResult();
			
		}else if(gameType.equals(Constants.GAME_TYPE_XY_SFLHC)){
			return SflhcUtil.getRandomResult();
		}
		return "";
	}
	
	/**
	 * 日志方法
	 * @param obj
	 * @param gameCode
	 */
	public static void logErr(Object obj){
		log("_error",obj);
	}
	public static void log(Object obj){
		log(null,obj);
	}
	public static void log(Object obj,Exception e){
		log(null,obj);
		e.printStackTrace();
	}
	public static void log(String gameCode,Object obj,Exception e){
		log(gameCode,obj);
		log("_err",gameCode+"|"+obj.toString()+"|"+e.getLocalizedMessage());
		e.printStackTrace();
	}
	public static void log(String method,Object obj,long sTing){
		if(sTing>0){
			log(null,method+" "+obj.toString()+" ["+(System.currentTimeMillis()-sTing)+"ms]");
		}else{
			log(null,method+" "+obj.toString());
		}
	}
	public static void log(String gameCode,Object obj){
		String logLevel = GameHelpUtil.getLogLevel(gameCode);
		if(logLevel==null || logLevel.equals("")) return;
		
		//String gameCode = gameType;//Constants.getGameCode(gameType);
		if(gameCode==null || gameCode.length()==0) gameCode = "ga";
		
		Date now = DateTimeUtil.getJavaUtilDateNow();
		
		String fileName = gameCode+"."+DateTimeUtil.DateToStringYY(now)+".log";
		String logs = obj.toString();
		String logsLimit = "";
		if(logs.contains("<?xml")){
			//logsLimit = "[G]["+DateTimeUtil.getDateTime("MM-dd HH:mm:ss")+"] ["+gameCode+"] "+logs.substring(90, 150)+"...";
			logs = logs.substring(90, 150)+"...";
		}
		logs = "[G]["+DateTimeUtil.getDateTime("MM-dd HH:mm:ss")+"]["+gameCode+"] "+logs;
		if(logLevel.contains("P"))
			if(checkLogFilterHas(logs)) System.out.println(logsLimit.length()>0?logsLimit:logs);
			
		if(logLevel.contains("W")){
			String logPath = Constants.getWebRootPath()+ "/_gamelog/"+DateTimeUtil.DateToStringYY(now);
			ManageFile.createFolder(logPath);
			if(checkLogFilterHas(logs)){
				ManageFile.writeTextToFile(logs+"\r\n",logPath+"/"+fileName, true);
			}
		}
	}
	
	/**
	 * 根据代码获取表名
	 * @param code
	 * @return
	 */
//	public final static Map<String,String> gameTableMap = new HashMap<String, String>();
//	public final static void initGameTable(){
//		gameTableMap.put("bj3","Bj3GaSession");
//		gameTableMap.put("bjpk10","BjPk10GaSession");
//		gameTableMap.put("bjlu28","BjLu28GaSession");
//		gameTableMap.put("xjplu28","XjpKl8GaSession");
//		gameTableMap.put("cqssc","CqSscGaSession");
//		gameTableMap.put("gdk10","GdK10GaSession");
//		gameTableMap.put("tjssc","TjSscGaSession");
//		gameTableMap.put("xjssc","XjSscGaSession");
//		gameTableMap.put("sdklpk3","SdKlpk3GaSession");
//		gameTableMap.put("gdpick11","GdPick11GaSession");
//		gameTableMap.put("jsk3","JsK3GaSession");
//		gameTableMap.put("gxk10","GxK10GaSession");
//		gameTableMap.put("lhc","LhcGaSession");
//		gameTableMap.put("ssq","SsqGaSession");
//		gameTableMap.put("wfc","WfcGaSession");
//		gameTableMap.put("sfc","SfcGaSession");
//		gameTableMap.put("jxpick11","JxPick11GaSession");
//		gameTableMap.put("jxssc","JxSscGaSession");
//		gameTableMap.put("sdpick11","SdPick11GaSession");
//		gameTableMap.put("sxpick11","SxPick11GaSession");
//		gameTableMap.put("bjpick11","BjPick11GaSession");
//		gameTableMap.put("tjpick11","TjPick11GaSession");
//		gameTableMap.put("hebpick11","HebPick11GaSession");
//		gameTableMap.put("nmgpick11","NmgPick11GaSession");
//		gameTableMap.put("lnpick11","LnPick11GaSession");
//		gameTableMap.put("jlpick11","JlPick11GaSession");
//		gameTableMap.put("hljpick11","HljPick11GaSession");
//		gameTableMap.put("shpick11","ShPick11GaSession");
//		gameTableMap.put("jspick11","JsPick11GaSession");
//		gameTableMap.put("zjpick11","ZjPick11GaSession");
//		gameTableMap.put("ahpick11","AhPick11GaSession");
//		gameTableMap.put("fjpick11","FjPick11GaSession");
//		gameTableMap.put("hnpick11","HnPick11GaSession");
//		gameTableMap.put("hubpick11","HubPick11GaSession");
//		gameTableMap.put("gxpick11","GxPick11GaSession");
//		gameTableMap.put("scpick11","ScPick11GaSession");
//		gameTableMap.put("gzpick11","GzPick11GaSession");
//		gameTableMap.put("shxpick11","ShxPick11GaSession");
//		gameTableMap.put("gspick11","GsPick11GaSession");
//		gameTableMap.put("xjpick11","XjPick11GaSession");
//		gameTableMap.put("ynpick11","YnPick11GaSession");
//		gameTableMap.put("ahk3","AhK3GaSession");
//		gameTableMap.put("bjk3","BjK3GaSession");
//		gameTableMap.put("fjk3","FjK3GaSession");
//		gameTableMap.put("gzk3","GzK3GaSession");
//		gameTableMap.put("gxk3","GxK3GaSession");
//		gameTableMap.put("gsk3","GsK3GaSession");
//		gameTableMap.put("hubk3","HubK3GaSession");
//		gameTableMap.put("hebk3","HebK3GaSession");
//		gameTableMap.put("hnk3","HnK3GaSession");
//		gameTableMap.put("jxk3","JxK3GaSession");
//		gameTableMap.put("jlk3","JlK3GaSession");
//		gameTableMap.put("nmgk3","NmgK3GaSession");
//		gameTableMap.put("shk3","ShK3GaSession");
//		gameTableMap.put("bjkl8","BjKl8GaSession");
//		gameTableMap.put("jzdpks","JzdPksGaSession");
//		gameTableMap.put("malft","MalFtGaSession");
//		gameTableMap.put("cqk10","CqK10GaSession");
//		gameTableMap.put("mspks","MsPksGaSession");
//		gameTableMap.put("msft","MsFtGaSession");
//		gameTableMap.put("jspks","JsPksGaSession");
//		gameTableMap.put("jsft","JsFtGaSession");
//	}
	
	/**
	 * 获取表名，官方加前缀Gf
	 * @param code
	 * @param playCate
	 * @return
	 */
//	public final static String getGameTableNameOfCode(String code,String playCate){
//		String tblName = (String)gameTableMap.get(code);
//		return playCate.equals(Constants.GAME_PLAY_CATE_GF)?"Gf"+tblName:tblName;
//	}
	
	/**
	 * 统计保留小数位数
	 */
	public final static BigDecimal round(BigDecimal big){
		return big.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	public final static BigDecimal round(BigDecimal big,int round){
		return big.setScale(round, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 获取备注内容
	 * @param type
	 * @param obj
	 */
	public final static String getRemark(String type,Object obj){
		if(type.equals(Constants.CASH_TYPE_CASH_PRIZE)) return "中奖"+obj.toString();
		else if(type.equals(Constants.CASH_TYPE_CASH_BUY_LOTO)) return "投注"+obj.toString();
		else if(type.equals(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF)) return "返水"+obj.toString();
		return "[0]"+obj.toString();
	}
	
	/**
	 * 获取批次号，用于官方gf
	 * @return
	 */
	public final static String getBatchNum(){
		Random rad=new Random();  
	    String result  = String.format("%02d", rad.nextInt(100));
	    return System.currentTimeMillis()+result;
	}
	
	/**
	 * 计算返水钱(会员或代理)
	 * @param formMoney 原始金额
	 * @param vc 配置对象
	 */
	public final static BigDecimal getBackMoney(BigDecimal formMoney,ValueConfig vc){
		if(vc==null) return round(new BigDecimal(0));//无配置
		if(formMoney==null) return round(new BigDecimal(0));//无金额直接返0
		if(formMoney.compareTo(new BigDecimal(vc.getMin()))<0) return round(new BigDecimal(0));//小于最范围返0
		if(formMoney.compareTo(new BigDecimal(vc.getMin()))==0){//等于最小范围 返 最小比例
			return round(formMoney.multiply(vc.getMinPer().divide(new BigDecimal(100))));
		}
		if(formMoney.compareTo(new BigDecimal(vc.getMax()))>=0){//高于/等于最大范围 返 最大比例
			return round(formMoney.multiply(vc.getMaxPer().divide(new BigDecimal(100))));
		}
		//其他情况
		List<ValueItem> items = vc.getItems();
		for(ValueItem item:items){
			if(formMoney.compareTo(new BigDecimal(item.getMin()))>=0 
					&& formMoney.compareTo(new BigDecimal(item.getMax()))==-1){//在范围中 min<=X<max
				return round(formMoney.multiply(item.getPercent().divide(new BigDecimal(100))));
			}
		}
		return round(new BigDecimal(0));//不在范围中目前返0
	}
}
