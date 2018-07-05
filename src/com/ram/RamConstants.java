 package com.ram;

import javax.servlet.http.HttpServletRequest;

import com.framework.common.properties.IReadProperties;
import com.framework.service.IServiceLocator;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.ManageFile;
import com.ram.util.IPUtil;

public final class RamConstants {
	
	//
	public static boolean debugMode = true;//调试模式
	public static boolean debugWrite = true;//写日志文件
	public static boolean debugWriteReturn = false;//写返回结果日志文件
	public static boolean debugPrint = true;//打印
	public static boolean debugPrintReturn = false;//打印返回结果
	public static String debugKey = "no_debug";
	
//	public final static String POINTS_TYPE_GAME ="3";//好运来
//	public final static String POINTS_TYPE_GAME_12 ="4";//益智生肖
//	public final static String POINTS_TYPE_GAME_ZYM ="5";// 快乐四方
	
	public final static String POINTS_TYPE_GAME ="13";//好运来
	public final static String POINTS_TYPE_GAME_12 ="14";//益智生肖
	public final static String POINTS_TYPE_GAME_ZYM ="15";// 快乐四方
	
	public final static Integer USER_ORG_DINGFEIBAO = 1;//定肥宝用户
	public final static Integer USER_ORG_NONGDEBAO = 2;//农得宝用户
	public final static Integer PCE_MIN_UNIT_POINT = 10;//最小单位购买积分
	public final static String PCE_START_TIME_END=" 00:00:00";//开始时间后缀00:00:00
	public final static String PCE_END_TIME_END=" 23:50:00";//开始时间后缀23:50:00
	public final static String PCE_BET_STATUS_INIT="0";//抽奖中"
	public final static String PCE_BET_STATUS_ING="1";//开奖中;
	public final static String PCE_BET_STATUS_OPEND="2";//已开奖;
	public final static String PCE_START_TIME_S=" 09:00:00";//游戏开始时间点
	public final static String PCE_START_TIME_STR=" 09:02:00";//游戏开始时间第一期09:10:00
	public final static Integer PCE_END_TIME_HOUR=23;//游戏游戏结束小时23/24
	public final static Integer PCE_END_TIME_MINUTE=50;//游戏游戏结束小时50/0
	public final static String PCE_BET_OK="1";//已中奖
	public final static String PCE_BET_FAIL="0";//未中奖
	public final static String PCE_BET_EQUAL="2";//平局
	public final static String CARD_STATUS_INIT="1";//未激活 
	public final static String CARD_STATUS_ACTIVED="2";//已激活 
	public final static String CARD_STATUS_INVALID="0";//已注销 
	public final static String NDB_RECHARGE_TYPE_CARD="1";//充值卡充值
	public final static String NDB_RECHARGE_TYPE_GAME="2";//游戏--好运来
	public final static String NDB_RECHARGE_TYPE_LEARN="3";//学习
	public final static String NDB_RECHARGE_TYPE_INVITE="4";//邀请码赠品
	public final static String NDB_RECHARGE_TYPE_GOODS="5";//商品
	public final static String NDB_RECHARGE_TYPE_GAME_12="6";//游戏--12生肖
	public final static String NDB_RECHARGE_TYPE_GAME_ZHUAYUMI="7";//游戏--抓玉米
	public final static Integer COMMENT_PRODUCT_MAX_COUNT_PER_DAY = 3;//每天允许最大评价次数
	// 抽奖常量
	public final static String PCE_UNIT_DAN = "单";
	public final static String PCE_UNIT_SHUANG = "双";
	public final static Integer NDB_BET_RECORD_VIEW_DAY = 7;//最大查看记录的天数
	public final static Integer NDB_POINTS_RECORD_VIEW_DAY = 7;//积分最大查看记录
	public final static Integer NDB_BET_RECORD_MAX = 50;//最多记录条数
	public final static Integer PCE_START_TIME_NUMBER=90000;//开始时间数字化90000
	public final static Integer PCE_END_TIME_NUMBER=235000;//开始时间数字化235000
	public final static Integer PCE_END_TIME_NUMBER_MAX=235959;//开始时间数字化
	public final static String POINTS_TYPE_ADBUY ="10";//在线购买
	public final static String POINTS_TYPE_RECHARGE ="11";//在线充值
	public final static String POINTS_TYPE_INVITE ="12";//邀请
	public final static String POINTS_TYPE_REGISTER ="13";//新用户注册
	public final static String POINTS_TYPE_DAZHUANFEN_TUIGUANG ="16";//推广
	
	public final static String getCharegeTypeName(String chargeType){
		if(chargeType.equals(RamConstants.NDB_RECHARGE_TYPE_CARD)){
			return "充值";
		}else if(chargeType.equals(RamConstants.NDB_RECHARGE_TYPE_GAME)){
			return "好运来";
		}else if(chargeType.equals(RamConstants.NDB_RECHARGE_TYPE_GAME_12)){
			return "益智生肖";
		}else if(chargeType.equals(RamConstants.NDB_RECHARGE_TYPE_GAME_ZHUAYUMI)){
			return "快乐四方";
		}else if(chargeType.equals(RamConstants.NDB_RECHARGE_TYPE_LEARN)){
			return "农科知识";
		}else if(chargeType.equals(RamConstants.NDB_RECHARGE_TYPE_INVITE)){
			return "邀请奖励";
		}else if(chargeType.equals(RamConstants.NDB_RECHARGE_TYPE_GOODS)){
			return "积分商城";
		}else{
			return "其他";
		}
	}
	public final static String getPointTypeName(String pointType){
		if(pointType.equals(RamConstants.POINTS_TYPE_QUESTION)){
			return "答题赚分";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_DOWNLOAD)){
			return "下载赚分";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_GAME)){
			return "好运来";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_GAME_12)){
			return "益智生肖";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_GAME_ZYM)){
			return "快乐四方";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_ZHAOGONG)){
			return "招工发布";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_QIUZHI)){
			return "求职发布";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_ZHONGJIE)){
			return "商家中介";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_RECHANGE)){
			return "积分兑换";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_ADBUY)){
			return "在线购买";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_RECHARGE)){
			return "在线充值";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_INVITE)){
			return "用户邀请";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_REGISTER)){
			return "在线注册";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_TURNTABLE_QINGKE)){
			return "谁请客";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_DAZHUANFEN_TUIGUANG)){
			return "推广";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_TURNTABLE_GANHUO)){
			return "谁干活";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_TURNTABLE_GANHUO)){
			return "谁干活";
		}else if(pointType.equals(RamConstants.POINTS_TYPE_HHMF)){
			return "黑红梅方";
		}else{
			return "其他";
		}
	}
	
	public final static Integer PCE_MAX_NUMBER=10;//最大数字(不含)
	public final static Integer PCE_MAX_HOME_BET_SESSIONS=12;//首页最大显示场数
	public final static Double PCE_BET_EARN_NUMBER = 9.0;//单个数字
	public final static Double PCE_BET_EARN_DAN_OR_SHUAN = 1.9;//单或双
	//配置场次及时间间隔
	public final static Integer PCE_TIME_INTERVAL=2;//场次间隔分钟10/3
	public final static Integer PCE_MAX_PART=445;//最大场次89/300
	public final static Double PCE_BET_EARN_OF_ZHUAYUMI = 1.9;//抓玉米
	public final static Double PCE_BET_EARN_OF_ZHUAYUMI_V2 = 1.95;//抓玉米 >100积分时 
	public final static Double PCE_BET_EARN_OF_12_s1 = 10.0;//十二生肖
	public final static Double PCE_BET_EARN_OF_12_s2 = 5.0;
	public final static Double PCE_BET_EARN_OF_12_s3 = 3.5;
	public final static Double PCE_BET_EARN_OF_12_s4 = 2.5;
	public final static Double PCE_BET_EARN_OF_12_s5 = 2.0;
	public final static Double PCE_BET_EARN_OF_12_s6 = 1.7;
	public final static Double PCE_BET_EARN_OF_12_s7 = 1.5;
	public final static Double PCE_BET_EARN_OF_12_s8 = 1.3;
	public final static Double PCE_BET_EARN_OF_12_s9 = 1.1;
	public final static Double PCE_BET_EARN_OF_12_s10 = 1.0;
	public final static Double PCE_BET_EARN_OF_12_s11 = 0.9;
	public final static Double PCE_BET_EARN_OF_12_s12 = 0.8;
	//-------------系统权限代码定义-------------
	public final static String USER_PERMISSION_LEVEL_ADMIN="admin";//系统管理员
	public final static String USER_PERMISSION_LEVEL_CITY_ADMIN="cityAdmin";//城市代理管理员
	public final static String USER_PERMISSION_LEVEL_SHANGJIA="shangjia";//商家
	public final static String UserTypeIsSuperAdmin4="4";//超级管理员
	public final static String UserTypeIsCityAdmin6="6";//城市代理
	public final static String UserTypeIsSeller3="3";//学生--这里指商家
	public final static String USER_TYPE_Business="3";// 商家 （掌上宜州）
	public final static String POINTS_TYPE_DAZHUANFEN_KOUCHUSHANGJIA ="18";//答题赚分（一次全部扣除商家积分）
	public final static String POINTS_TYPE_QUESTION ="1";///答题赚分
	public final static String POINTS_TYPE_DOWNLOAD ="2";//下载赚分
	public final static String POINTS_TYPE_TURNTABLE_GANHUO ="17";//谁干活（娱乐）
	public final static String POINTS_TYPE_TURNTABLE_QINGKE ="18";//谁请客（娱乐）
	public final static String POINTS_TYPE_RECHANGE ="9";// 积分兑换
	public final static String POINTS_TYPE_HHMF ="26";//谁干活（娱乐）
	
	//短信内容定义
	public static String SMS_MSG_SIGNATURE="";
	public static String SMS_MSG_REG="";
	public static String SMS_MSG_REREG="";
	public static String SMS_MSG_COUPON="";
	public static String SMS_MSG_COUPON_EXCHANGE="";
	
	//
	
	
	
	public final static long mmOfADay=86400000;//一天的毫秒数	
	public final static int DESK_SN_LENGTH=12;
	public final static String DESK_UPDATE_STATUS="1";//可 以升级
	
	public final static String DESK_SN_STATUS_INIT="0";//初始
	public final static String DESK_SN_STATUS_USED="1";//已激活
	public final static String DESK_SN_STATUS_send="2";//已发出
	public final static String DESK_SN_STATUS_FREEZE="3";//已冻结
	public final static String DESK_SN_STATUS_DELETE="4";//已删除
	
	public final static String DESK_REGISTER_SUCCESS="100";//注册成功
	public final static String DESK_REGISTER_PAUSE="110";//暂停注册,注册暂不可用
	public final static String DESK_REGISTER_SUCCESS_ALREADY="101";//已注册成功
	public final static String DESK_SN_INVALID="102";//sn无效，注册无效
	public final static String DESK_MAC_NOT_MATCH="103";//sn已注册mac不匹配
	public final static String DESK_REGISTER_INVALID="104";//未注册这个状态出现在check方法
	public final static String DESK_UPDATE_EXEC="105";//执行升级
	public final static String DESK_DEADLINE_TIP="106";//到期前提示
	public final static String DESK_DEADLINE_EXEC="107";//已到期
	
	public final static String DESK_NO_ACTION="111";//默认状态不处理
	
	public final static String DESK_PARAM_INCORRENT="112";//参数为空
	public final static String DESK_DESTROY_EXEC="113";//摧毁程序
	
	public final static String DESK_REWRIATE_MAC="115";//重写mac地址，解决双网卡的问题
	public final static String DESK_REWRIATE_MAC_FALSE="116";//重新注册认证
	public final static String DESK_EMPTY_MAC_OK="117";//empty mac register 1
	public final static String DESK_STOP_USE="120";//软件停止使用并返回提示信息。
	public final static String DESK_SHOW_MESSAGE="121";//软件停止使用并返回提示信息。
	
	public final static String DESK_SHOW_TIPS="122";//弹出信息通知，不影响软件使用。
	public final static String DESK_SHOW_TIP_URL="123";//弹出信息通知，并转向URL。
	
	public final static String DESK_REGISTER_MAX="199";//超过最大注册数
	
	public final static Integer USER_ORG_NONGDEBAO_PUBLIC= -1;//发布消耗
	//~
	
	/** 全局状态：无效 */
	public final static String STATUS_INVALID = "0";
	/** 全局状态：有效 */
	public final static String STATUS_VALID = "1";
	
	//sat类型
	public final static String USER_CLASS_SAT_TYPE = "5";
	//IELTS类型
	public final static String USER_CLASS_IELTS_TYPE = "6";
	//TOEFL类型
	public final static String USER_CLASS_TOEFL_TYPE = "7";
	//BEC类型
	public final static String USER_CLASS_BEC_TYPE = "8";
	//通用常量--------------------------
	public final static String LOGIN_USER= "loginUser";
	// 数据被删除状态	public final static String DELETE_STATUS = "0";
	// 正常状态（一般为默认状态）
	public final static String VALID_STATUS = "1";
	
	// 用户表的常量-----------------------
	//用户暂停状态	public final static String USER_SUSPEND_STATUS = "2";
	public final static String USER_STATUS_OPEN = "1";//用户开放
	public final static String USER_STATUS_CLOSE = "0";//用户关闭
	
	//用户表的用户类型常量

	public final static String UserTypeIsTutor1="1";//1：教师	public final static String UserTypeIsManager2="2";//管理员	public final static String UserTypeIsLearner3="3";//学生
	public final static String UserTypeIsNewUser4="4";//超级管理员
	public final static String UserTypeIsNewUser6 = "6";//desk tmk用户
	
	public final static String UserTypeIsOriginUser8 = "8";// 机构用户
	
	
	//新闻资讯类别常量
	public final static String NewsCategoryWeb="0";//网站公告
	public final static String NewsCategoryHelp="1";//帮助中心
	public final static String NewsCategoryJiaoDian="2";//焦点新闻
	public final static String NewsCategoryJinCai="3";//竞猜单场
	public final static String NewsCategoryShuZiCai = "4";//数字彩
	public final static String NewsCategoryGoPinCai="5";//焦点新闻
	public final static String NewsCategoryChongZhi="6";//充值指南
	public final static String NewsCategoryCashHelp = "7";//提款帮助
	public final static String NewsCategoryProblems = "8";//常见问题
	public final static String NewsCategoryWebsiteHelp = "9";//网站帮助
	
	
	public final static String USER_ACCOUNT_OPEN = "1";//有效
	public final static String USER_ACCOUNT_CLOSE = "2";//关闭
	//Function功能表的常量-------------------------------
	public final static Integer FUNCTION_SUB_FUNCTION_NUMBER=new Integer(1);
	public final static Integer FUNCTION_NONE_SUB_FUNCTION=new Integer(0);
	public final static Integer FUNCTION_TOP_FUNCTION=new Integer(0);
	
	//用来页面分页用，表示每页显示的最大条数	
	public final static int  MAXPAGEITEMS=20;
	public final static int  MAXPAGEITEMS_MAX=100;
	public final static int  MAXPAGEITEMS_MAX_500=500;
	public final static int  MAXPAGEITEMS_MAX_2000=2000;
	
	public final static String USER_PERMISSION_SUPER_ADMIN="superAdmin";
	public final static String USER_PERMISSION_SYS_ADMIN="sysAdmin";
	public final static String USER_PERMISSION_SYS_TUTOR="sysTutor";
	public final static String USER_PERMISSION_ORG_ADMIN="orgAdmin";
	public final static String USER_PERMISSION_ORG_TUTOR="orgTutor";
	public final static String USER_PERMISSION_LEARNER="learner";
	
	public final static String POINTS_TYPE_UNKNOWN ="0";//未知
	public final static String POINTS_TYPE_ZHAOGONG ="24";// 发布招工
	public final static String POINTS_TYPE_QIUZHI ="25";//发布 求职
	public final static String POINTS_TYPE_ZHONGJIE ="6";//商品中介
	
	/***
	 * add by wl
	 */
	public final static String USER_TYPE_APP="1";//1：APP用户
	public final static double USER_ITCODE_FX_MONEY = 1.0;//邀请码返现金
	public final static double USER_DT_FX_MONEY = 1.0;//看图答题返现金
	public final static double USER_FX_FX_MONEY = 1.0;//分享信息返现金
	public final static double USER_QG_FX_MONEY = 1.0;//抢购返现金
	public final static int RANDOM_YAO_QING_CODE = 6 ;//随机生成邀请码数量
	public final static String RANDOM_YAO_QING_CODE_STATUS_YS = "0" ;//未使用
	public final static String RANDOM_YAO_QING_CODE_STATUS_NO = "1" ;//已使用
	/***
	 * end
	 */
	
	/**
	 * 全局错误提示页面
	 */	
	public final static String GlobalSystemErrorPage="../_error/errorInformation.jsp";
	public final static String GlobalUserWarningPage="../_error/warning.jsp";
	public final static String GlobalFileNotExistPage="../_error/fileNotExist.jsp";
	public final static String GlobalNoPermissionPage="../_error/noPermission.jsp";
	public final static String GlobalResourceNotSetted="../_error/resourceNotSetted.jsp";
	public final static String GlobalAppSessionTimeOut = "../_error/appsessionout.jsp";
	

	private static String appRootURL=""; 
	private static String webappContext="";
	private static String webRootPath="";
	private static String fileServerContext="";
	private static String fileServerPath="";
	private static String fileServerFolder="";
	private static String webServer="";
	

	public static void setAppRootURL(String appRootURL) {
		RamConstants.appRootURL = appRootURL;
	}

	public static String getWebappContext() {
		return webappContext;
	}

	public static void setWebappContext(String webappContext) {
		RamConstants.webappContext = webappContext;
	}

	public static String getWebRootPath() {
		return webRootPath;
	}

	public static void setWebRootPath(String webRootPath) {
		RamConstants.webRootPath = webRootPath;
	}

	public static String getFileServerContext() {
		return fileServerContext;
	}

	public static void setFileServerContext(String fileServerContext) {
		RamConstants.fileServerContext = fileServerContext;
	}

	public static String getFileServerPath() {
		return fileServerPath;
	}

	public static void setFileServerPath(String fileServerPath) {
		RamConstants.fileServerPath = fileServerPath;
	}

	public static String getFileServerFolder() {
		return fileServerFolder;
	}
	
	public static String getWebServer() {
		return webServer;
	}

	public static void setFileServerFolder(String fileServerFolder) {
		RamConstants.fileServerFolder = fileServerFolder;
	}
	

	static {
		reload();
	}
		
	public static void reload() {
	    IServiceLocator service = ServiceLocatorImpl.getInstance();
		IReadProperties read = (IReadProperties) service.getService("readProperties");
		webappContext=read.getValue("SYSTEM.WEBSERVER.WEBCONTEXT");
		webRootPath=read.getValue("SYSTEM.WEBROOT.PATH");			
		fileServerContext=read.getValue("SYSTEM.WEBSERVER.WEBCONTEXT");
		fileServerPath=read.getValue("SYSTEM.WEBROOT.PATH");
		appRootURL = read.getValue("SYSTEM.APPROOT.URL");
		fileServerFolder=read.getValue("FILEUPLOAD.FOLDER");
		webServer = read.getValue("SYSTEM.WEB.SERVER");
		
		SMS_MSG_SIGNATURE=read.getValue("SMS.MSG.SIGNATURE");
		SMS_MSG_REG=read.getValue("SMS.MSG.REG");
		SMS_MSG_REREG=read.getValue("SMS.MSG.REREG");
		SMS_MSG_COUPON=read.getValue("SMS.MSG.COUPON");
		SMS_MSG_COUPON_EXCHANGE=read.getValue("SMS.MSG.COUPON.EXCHANGE");
	}
	
	public final static String getUserDefaultPicture(String gender,String sizeType,String url){
		if(url!=null && url.length()>1) return url;
		if(gender==null || gender.length()<=0) gender = "1";
		String size = "50";
		if(sizeType.equals("picture")) size = "50";
		else if(sizeType.equals("logo")) size = "120";
		else if(sizeType.equals("logoMini")) size = "20";
		else size = "50";
		return "/images/ui/p"+size+"_"+gender+".gif";
	}
	
	public final static boolean checkDemoSupport(){
		return false;
	}
	
	public final static String getSystemUgaidOrgToeflPaperOpen() {
	       return "0";
	}
	
	public final static void checkDebugLogMode(HttpServletRequest request,String logInfo,boolean isReturns){
		if(RamConstants.debugPrint || RamConstants.debugWrite){
			if(logInfo==null || logInfo.length()==0)
				logInfo = "_____[req]["+IPUtil.getRequestIP(request) + "]["+request.getHeader("User-Agent")+"]" + request.getRequestURL()+ "?" + request.getQueryString();
		}
		
		if((!isReturns && RamConstants.debugPrint) || (isReturns && RamConstants.debugPrintReturn)) System.out.println(logInfo);
		
		if((!isReturns && RamConstants.debugWrite) || (isReturns && RamConstants.debugWriteReturn)){
			String ip = request.getHeader("x-forwarded-for");
			if(ip==null) ip = request.getRemoteAddr();
			String userAgent = request.getHeader("User-Agent");
			String debugParams = RamConstants.debugKey;
			
			boolean isWriteLog = false;
			if(debugParams!=null && debugParams.length()>0){
				String[] debugParamsArr = debugParams.split(",");
				for(int i=0;i<debugParamsArr.length;i++){
					if((ip+"_"+userAgent).toLowerCase().indexOf(debugParamsArr[i].toLowerCase())>-1){
						isWriteLog = true;
						break;
					}
				}
			}
			//写log日志
			if(isWriteLog) ManageFile.writeTextToFile("["+DateTimeUtil.getDateTime()+"]"+logInfo+"<br>\n", RamConstants.getWebRootPath()+"/_help_tools/debug/d.log", true);
		}
	}

	/**
	 * 获取配置文件信息
	 * @param name
	 * @return
	 */
	public final static String getConfigInfo(String name) {
		IServiceLocator service = ServiceLocatorImpl.getInstance();
		IReadProperties read = (IReadProperties) service.getService("readProperties");
		return read.getValue(name);
	}
}
