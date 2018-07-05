package com.apps;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.apps.model.SMSCache;
import com.framework.common.properties.IReadProperties;
import com.framework.service.IServiceLocator;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.ManageFile;

/**
 * 平台常量类
 * 
 * @author Mr.zang
 * 
 */
public final class Constants {
	
	/**系统平台运行模式**/
	public final static String PLAT_RUN_MODE_ID = "C";//运行模式标识,默认为C
	public final static String PLAT_RUN_MODE_CITY = "C";//城市O2O
	public final static String PLAT_RUN_MODE_COMMUNITY = "CC";//小区O2O
	public final static String PLAT_RUN_MODE_UNIVERSITY = "UN";//学校O2O
	
	public static String SITE_TITLE = "";//站点标题
	public static String SITE_NAME = "";//站点名称
	public static String SITE_DOMAIN = "";//站点域名
	
	/**
	 * 系统平台功能定义默认或未配置都为打开，如配置：0关闭1打开
	 * 可针对模块、功能、页面功能等
	 * 配置文件：func.properties
	 * eg:
	 * system.func.city.add - 添加城市
	 * system.func.info.buy - 供求模块交易功能
	 */
	public static HashMap<String,String> SysFuncConfig;
	
	public static String DEF_PAY_COMPANY = "4";//1.仁信支付2.闪付3.艾米森 4.大面包5.轻易付 6.零点支付
	
	public final static String PARAM_DEF_PAY = "4";//1设置支付收款公司方式
	public final static String PARAM_RECHARGE_ONLINE="5";//在线充值开关类型
	public final static String PARAM_RECHARGE_OFFLINE="6";//线下充值开关类型
	public final static String PARAM_RECHARGE_FAST="7";//快速充值中心开关类型
	public final static String PARAM_RECHARGE_CODE="8";//支付宝/微信/QQ 扫描充值开关类型
	public final static String PARAM_REG_SWITCH="9";//注册类型切换设置 1=手机验证码 0=图形验证码 4=关闭注册 
	public final static String PARAM_OFFICIAL_ACCOUNTS="10";//微信公众好二维码设置
	public final static String PARAM_RECHARGE_SEND="11";//会员每次充值系统固定金额
	public final static String PARAM_RECHARGE_SEND_AGENT="12";//会员充值返水代理设置（废弃）
	public final static String PARAM_BET_SEND_AGENT="13";//会员打码返水代理设置（废弃）
	public final static String PARAM_CHATROOM="14";//聊天室
	public final static String PARAM_REGISTER_SEND="15";//注册会员赠送金额设置
	public final static String PARAM_SIGN_SEND="16";//签到赠送金额设置
	public final static String PARAM_TASK_SEND="17";//任务赚钱设置
	/**每日首次充值赠送比例**/
	public final static String PARAM_DEF_PRESENT = "18";//充值赠送固定比例   11是赠送固定金额
	/**每日首次充值赠送比例**/
	public final static String PARAM_DEF_PRESENT6 = "19";//充值100-999.99金额赠送的比例  表里面有最大赠送金额字段（达到最大额不在多余赠送）
	/**每日首次充值赠送比例**/
	public final static String PARAM_DEF_PRESENT7 = "20";//充值1000-4999.99金额赠送的比例  表里面有最大赠送金额字段（达到最大额不在多余赠送）
	/**每日首次充值赠送比例**/
	public final static String PARAM_DEF_PRESENT8 = "21";//充值5000以上  表里面有最大赠送金额字段（达到最大额不在多余赠送）
	/**限制提现比例**/
	public final static String PARAM_DEF_WITHDRAW = "22";
	/**支付宝收款二维码**/
	public final static String PARAM_DEF_ZHIFUBAO = "23";
	/**微信收款二维码**/
	public final static String PARAM_DEF_WEIXIN = "24";
	/**转账汇款提示内容**/
	public final static String PARAM_DEF_HINT = "25";
	public final static String PARAM_CASH_SEND_MSG="28";//提现短信发送开关设置
	public final static String PARAM_BJ3_OPEN_NUM="29";//三分彩开奖次数控制
	public final static String PARAM_SFPK10_OPEN_NUM="30";//急速赛车开奖次数控制
	public final static String PARAM_JSFT_OPEN_NUM="31";//极速飞艇开奖次数控制
	public final static String PARAM_SFPK102_OPEN_NUM="32";//三分pk10开奖次数控制
	public final static String PARAM_SFLHC_OPEN_NUM="33";//急速六合彩开奖次数控制
	public final static String PARAM_FIVE_OPEN_NUM="34";//五分彩开奖次数控制
	public final static String PARAM_BJSSC_OPEN_NUM="35";//北京时时彩开奖次数控制
	public final static String PARAM_QQ_CUSTOMER_URL="37";// QQ客服地址
	public final static String PARAM_ONLINE_CUSTOMER_URL="47";// 在线客服地址
	public final static String PARAM_QQ_ACCOUNT="43";// QQ客服账号
	public final static String PARAM_DAY_DRAWING_NUMBER="44";//每日提款次数
	public final static String PARAM_DRAWING_FEE="45";//提款手续费（%）
	public final static String PARAM_DRAWING_BET_MONEY="46";//提款限制打码量（%）
	public final static String PARAM_CASH_SWITCH="48";// 提款开关 0关闭  1开启
	public final static String PARAM_LOTTERY_CLOSE_TIP="49";// 彩种关闭提示语
	
	public final static String PARAM_LOTTERY_APP_DOWN_URL="91";//app下载地址
	
	//会员打码返水
	public final static String PARAM_BET_BACK_51="51";
	public final static String PARAM_BET_BACK_52="52";
	public final static String PARAM_BET_BACK_53="53";
	public final static String PARAM_BET_BACK_54="54";
	public final static String PARAM_BET_BACK_55="55";
	//会员打码代理返水
	public final static String PARAM_BET_BACK_AGENT_66="66";
	public final static String PARAM_BET_BACK_AGENT_67="67";
	public final static String PARAM_BET_BACK_AGENT_68="68";
	public final static String PARAM_BET_BACK_AGENT_69="69";
	public final static String PARAM_BET_BACK_AGENT_70="70";
	//会员充值返水
	public final static String PARAM_RECHARGE_BACK_56="56";
	public final static String PARAM_RECHARGE_BACK_57="57";
	public final static String PARAM_RECHARGE_BACK_58="58";
	public final static String PARAM_RECHARGE_BACK_59="59";
	public final static String PARAM_RECHARGE_BACK_60="60";
	//会员充值代理返水
	public final static String PARAM_RECHARGE_AGENT_BACK_61="61";
	public final static String PARAM_RECHARGE_AGENT_BACK_62="62";
	public final static String PARAM_RECHARGE_AGENT_BACK_63="63";
	public final static String PARAM_RECHARGE_AGENT_BACK_64="64";
	public final static String PARAM_RECHARGE_AGENT_BACK_65="65";

	
	/** 各级页面title **/
	public final static String SLOGAN = "官网";
	/** 全局状态-有效 **/
	public final static String PUB_STATUS_OPEN = "1";
	/** 全局状态-无效 **/
	public final static String PUB_STATUS_CLOSE = "0";

	
	/** 用户类型-普通用户 **/
	public final static String USER_TYPE_SUER = "1";
	/** 用户类型-管理员 **/
	public final static String USER_TYPE_ADMIN = "2";
	/** 用户类型-超级管理员 **/
	public final static String USER_TYPE_SUPERADMIN = "4";
	/** 用户类型-测试用户**/
	public final static String USER_TYPE_TEST = "99";
	/** 用户类型-代理用户**/
	public final static String USER_TYPE_AGENT = "12";
	/** 用户类型-代理用户-初级代理**/
	public final static String USER_TYPE_AGENT_ONE = "13";
	/** 用户类型-代理用户-中级代理**/
	public final static String USER_TYPE_AGENT_TWO = "14";
	/** 用户类型-代理用户-高级代理**/
	public final static String USER_TYPE_AGENT_THREE = "15";
	//--by.cuisy.20171220-----------------------
	/** 用户类型  客服人员*/
	public final static String USER_TYPE_CUS_SERVICE = "20";
	/** 用户类型-财务**/
	public final static String USER_TYPE_FINANCE = "21";
	/** 用户类型-隐藏用户**/
	public final static String USER_TYPE_HIDDEN = "0";
	//--by.cuisy.20171220-----------------------
	

	//--------下面是未用到的-------------------
	/** 用户类型-管理员_商家管理账号 **/
	public final static String USER_TYPE_ADMIN_SHANGJIA = "3";
	/** 用户类型-商家-订餐 **/
	public final static String USER_TYPE_SELLER_EAT = "5";
	/** 用户类型-商家-电商 **/
	public final static String USER_TYPE_SELLER_STORE = "6";
	/** 用户类型-商家-团购 **/
	public final static String USER_TYPE_SELLER_BUY = "7";
	/** 用户类型-管理员-商圈 **/
	public final static String USER_TYPE_USER_BAID= "8";
	/** 用户类型-管理员-小区 **/
	public final static String USER_TYPE_USER_CCID= "9";
	/** 用户类型-管理员-物业 **/
	public final static String USER_TYPE_USER_ESTATE= "10";
	/** 用户类型-商家-商品管理员 **/
	public final static String SELLER_USER_RL_TYPE_ADMIN = "4";
	/** 用户类型-商家-商品管理员 **/
	public final static String SELLER_USER_RL_TYPE_PUTONG= "1";
	/**送货类型 自送**/
	public final static String ORDER_SEND_OWNER= "0";
	/**送货类型 三方物流**/
	public final static String ORDER_SEND_EXPRESS= "1";
	//
	
	
	/**订单结算 未结算**/
	public final static String ORDER_CHECKOUT_NOT= "0";
	/**订单结算 已结算**/
	public final static String ORDER_CHECKOUT_SUCCESS= "1";
	
	/**订单结算 未入账**/
	public final static String ORDER_CHECKOUT_PAY_NOT= "0";
	/**订单结算 已入账**/
	public final static String ORDER_CHECKOUT_PAY_SUCCESS= "1";
	
	/**订单状态-未支付**/
	public final static String PAY_STATUS_UNHANDLE = "0";//线下--未处理
	/**订单状态-未支付**/
	public final static String PAY_STATUS_UNPAID = "1";//线下--未支付
	/**订单状态-已支付**/
	public final static String PAY_STATUS_PAID = "2";//线下--未处理 线上--未发货   在线-- 已支付
	/**订单状态-已拒绝**/
	public final static String PAY_STATUS_REJECT = "3";//线下--已拒绝
	/**订单状态-已充值**/
	public final static String PAY_STATUS_RESOLVED = "4";//线下---已充值

	/**奖励类型-转盘**/
	public final static String LOTTERY_SETTING_TURNTABLE= "1";
	/**奖励类型-红包**/
	public final static String LOTTERY_SETTING_REDPACKETS= "2";
	/**奖励类型-打码返水**/
	public final static String LOTTERY_SETTING_RECHARGE_BET_BACK= "3";
	
	
	public final static Integer USER_REG_SEND_POINT = 200;
	/**积分与钱的换算倍数 1元=100积分**/
	public final static Integer USER_MONEY_AND_POINT = 100;
	
	/** 系统默认头像 **/
	public final static String DEFAULT_LOGO = "/images/noimg.png";
	/** 系统默认mini头像 **/
	public final static String DEFAULT_LOGO_MINI = "/images/noimg.png";

	/** web根 **/
	private static String webRootPath = "";
	private static String webappContext = "";
	private static String fileServerFolder = "";
	/** 文件上传到的路径 **/
	private static String fileUploadPath = "";

	private static String serverUrl = "";

	/** 域名 **/
	public static String domainName = "";
	/** 域名-文件，如果是在一个服务器，则相同 **/
	public static String domainNameFile = "";

	/** 分别为商品的图片 **/
	public final static int IMG_WIDTH = 720;
	public final static int IMG_HEIGHT = 450;
	public final static int IMG_MINI_WIDTH = 240;
	public final static int IMG_MINI_HEIGHT = 150;

	/** 分别为商家的图片 **/
	public final static int IMG_WIDTH_SELLER = 640;
	public final static int IMG_HEIGHT_SELLER = 200;

	/** apps设备类型-IOS **/
	public final static String APPS_TYPE_IOS = "1";
	/** apps设备类型-Android **/
	public final static String APPS_TYPE_ANDROID = "2";
	
	/** 广告 **/
	public final static String ADVERTISING_LUNBO = "1";// 轮播
	public final static String ADVERTISING_STAT = "2";// 静态
	public final static String ADVERTISING_TYPE = "3";// 类型下的广告
	
	/**首页轮播广告最大数量限制**/
	public final static Integer ADVERTISING_LUNBO_HOME_MAX = 5;
	
	/** 市区类型-省 */
	public final static String CITY_SHENG = "3";//省
	/** 市区类型-市 */
	public final static String CITY_SHI = "1";
	/** 市区类型-区 */
	public final static String CITY_QU = "2";

	public final static String DES_KEY_OID = "oid";
	public final static String DES_KEY_UID = "uid";
	public final static String DES_KEY_LOGINNAME = "loginName";
	public final static String DES_KEY_PASSWORD = "password";
	public final static String DES_KEY_STATUS = "status";
	public final static String DES_KEY_SESSION = "session";
	

	/** 电商模块 */
	public final static String MODULE_STORE = "1";
	/** 订餐模块 */
	public final static String MODULE_EAT = "2";
	/** 团购模块 */
	public final static String MODULE_GROUP = "3";
	/** 充值模块 */
	public final static String MODULE_RECHARGE = "4";
	/** 礼品卡模块 */
	public final static String MODULE_GIFT_CARD = "5";
	/**物业费模块**/
	public final static String MODULE_ESTATE_FEE = "6";
	
	/**供求信息模块**/
	public final static String MODULE_PUBLISH_INFO= "7";
	
	/**双色球**/
	public final static String MODULE_SSQ= "8";
	/**快3**/
	public final static String MODULE_K3= "9";
	/** 游戏模块 */
	public final static String MODULE_GAME = "10";
	
	
	/**员工类型-普通员工**/
	public final static String EMPLOYEE_TYPE_ORDINARY = "1";
	/**员工类型-派单员工**/
	public final static String EMPLOYEE_TYPE_TASK = "2";
	/**员工类型-外部配送人员(后续实现)**/
	public final static String EMPLOYEE_TYPE_EXPRESS = "3";
	/**员工类型-管理人员**/
	public final static String EMPLOYEE_TYPE_ADMIN = "4";
	
	/**用户资金收支-收入**/
	public final static String TRADE_TYPE_INCOME = "1";
	/**用户资金收支-支出**/
	public final static String TRADE_TYPE_PAY = "2";
	
	/**订单结算-未结算**/
	public final static String ORDER_STATUS_CHECKOUT_NO = "0";
	/**订单结算-已结算**/
	public final static String ORDER_STATUS_CHECKOUT_OVER  = "1";
	
	/**用户提现审核-审核中**/
	public final static String CASH_AUDIT_TYPE_ING= "0";
	/**用户提现审核-通过**/
	public final static String CASH_AUDIT_TYPE_PASS= "1";
	/**用户提现审核-拒绝**/
	public final static String CASH_AUDIT_TYPE_REFUSE= "2";
	
	/**注册用户--默认密码**/
	public final static String USER_PASSWORD_DEFAULT= "888888";

	/**使用的是开彩网Url**/
	public final static String KCW_URL = "1";
	/**使用的是彩票控Url**/
	public final static String CPK_URL = "2";
	
	//----------------------------------------------------------------
	
	//提现类型
	public final static String CASH_MONEY_MIN="21";// 提现最低限额
	public final static String CASH_MONEY_MAX="22";// 提现最高限额
	
	public static String gameTimerOpen = "true";//彩票定时总开关默认开
	
	//资金交易类型
	/**用户资金来源-在线充值**/
	public final static String CASH_TYPE_ONLINE = "1";
	/**用户资金来源-卡充**/
	public final static String CASH_TYPE_CARD = "2";
	/**用户资金来源-线下充值**/
	public final static String CASH_TYPE_OFFLINE = "3";
	/**用户资金来源-分销佣金**/
	public final static String CASH_TYPE_FENXIAO= "4";
	public final static String CASH_TYPE_CASH_OUT ="5";// 提现
	public final static String CASH_TYPE_CASH_RETURN ="6";// 提现审核返回
	public final static String CASH_TYPE_INFO_PUBLISH= "7";//供求交易
	public final static String CASH_TYPE_CASH_CHECKOUT ="8";// 结算
	public final static String CASH_TYPE_CASH_REFUND ="9";// 退款
	public final static String CASH_TYPE_CASH_BUY_LOTO ="10";// 购买彩票
	public final static String CASH_TYPE_CASH_GUARANTEE ="11";// 方案保底 
	public final static String CASH_TYPE_CASH_FROZEN ="12";// 保底 冻结||扣款
	public final static String CASH_TYPE_CASH_DRAW ="13";// 方案撤单
	public final static String CASH_TYPE_CASH_SYSTEM ="14";// 系统充值
	public final static String CASH_TYPE_CASH_SYS_CHARGE ="15";// 系统扣款 
	public final static String CASH_TYPE_CASH_PRESENT ="16";// 注册赠送
	public final static String CASH_TYPE_CASH_GUA_BACK ="17";// 方案退保
	public final static String CASH_TYPE_CASH_PRIZE ="18";// 中奖彩派
	public final static String CASH_TYPE_CASH_AGENCY ="19";// 代理返点
	public final static String CASH_TYPE_CASH_EXCHANGE ="20";// 积分兑换
	public final static String CASH_TYPE_CASH_SPONSOR ="21";// 参与合买
	public final static String CASH_TYPE_MANAGER_SET= "27";// 管理员充值
	public final static String CASH_TYPE_CASH_OTHER_SET= "28";// 其它加款
	public final static String CASH_TYPE_CASH_OTHER_CHARGE= "29";//其他扣款
	public final static String CASH_TYPE_CASH_REPAIR_DETAIL_ADD="30";//修复明细加款
	public final static String CASH_TYPE_CASH_REPAIR_DETAIL_SUB="31";//修复明细扣款
	public final static String CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN ="32";// 会员充值返水给代理
	public final static String CASH_TYPE_CASH_MEMBER_BET_RETURN  ="33";//会员打码（投注）返水给代理
	public final static String CASH_TYPE_CASH_MEMBER_RETURN_SELF  ="34";//会员充值系统赠送
	public final static String CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF  ="35";//会员打码（投注）返水给自己
	public final static String CASH_TYPE_CASH_SIGN ="36";//签到赠送
	public final static String CASH_TYPE_CASH_TURN_TABLE ="37";// 转盘获得
	public final static String CASH_TYPE_CASH_RED_PACKETS ="38";//红包获得
	public final static String CASH_TYPE_CASH_DRAWBACK ="39";// 投注退还
	public final static String CASH_TYPE_RECHARGE_AWARD = "40";// 充值跳级奖励
	public final static String CASH_TYPE_CASH_REVOKE_PRIZE ="41";// 撤回彩派金额
	public final static String CASH_TYPE_CASH_RECHARGE_PRESENT ="56";// 充值赠送
	
	// 竞猜
	public final static String CASH_TYPE_CASH_BUY_JC ="57";// 竞猜投注
	
	
	//-------------------------全局game_type定义-------------------------------
	//分成 赛车类[1xx]、时时彩类[2xx]、综合类[3xx]、11选5类[5xx]、快3类[6xx]。官方信用按规律定义
	
	public final static String GAME_PLAY_CATE_GF = "1";//官方
	public final static String GAME_PLAY_CATE_XY = "2";//信用
	public final static String getGFXYPre(String playCate){
		if(playCate.equals(GAME_PLAY_CATE_GF)) return "gf";
		else if(playCate.equals(GAME_PLAY_CATE_XY)) return "xy";
		return "xy";
	}
	//根据gameType判断是gf或xy
	public final static String getGFXYFlag(String gameType){
		if(gameType==null || gameType.trim().length()==0 || gameType.equals("0")) return "0";//默认
		if(gameType.substring(1,2).equals("0") || gameType.substring(1,2).equals("1")){
			return GAME_PLAY_CATE_GF;
		}else if(gameType.substring(1,2).equals("5") || gameType.substring(1,2).equals("6")){
			return GAME_PLAY_CATE_XY;
		}
		return "0";
	}
	/**
	 * 获取官方信用唯一代码
	 * @param gameCode
	 * @param playCate
	 * @return
	 */
	public final static String getGFXYGameCode(String gameCode,String playCate){
		return getGFXYPre(playCate)+gameCode;
	}
	
	//赛车类
	public final static String GAME_TYPE_GF_BJPK10="101";//北京赛车
	public final static String GAME_TYPE_XY_BJPK10="151";
	public final static String GAME_TYPE_GF_SFPK10="102";//极速赛车
	public final static String GAME_TYPE_XY_SFPK10="152";
	public final static String GAME_TYPE_GF_SFPK102="103";//三分PK拾
	public final static String GAME_TYPE_XY_SFPK102="153";
	public final static String GAME_TYPE_GF_XYFT="104";//幸运飞艇
	public final static String GAME_TYPE_XY_XYFT="154";
	public final static String GAME_TYPE_GF_JSFT="105";//极速飞艇
	public final static String GAME_TYPE_XY_JSFT="155";
	
	//时时彩类
	public final static String GAME_TYPE_GF_CQSSC="201";//重庆时时彩
	public final static String GAME_TYPE_XY_CQSSC="251";
	public final static String GAME_TYPE_GF_XJSSC="202";//新疆时时彩
	public final static String GAME_TYPE_XY_XJSSC="252";
	public final static String GAME_TYPE_GF_TJSSC="203";//天津时时彩
	public final static String GAME_TYPE_XY_TJSSC="253";
	public final static String GAME_TYPE_GF_JXSSC="204";//江西时时彩
	public final static String GAME_TYPE_XY_JXSSC="254";
	public final static String GAME_TYPE_GF_BJSSC="205";//北京时时彩
	public final static String GAME_TYPE_XY_BJSSC="255";
	//public final static String GAME_TYPE_GF_BJ3="206";//三分彩(北京)
	public final static String GAME_TYPE_XY_BJ3="256";
	public final static String GAME_TYPE_GF_THREE="207";//三分彩
	//public final static String GAME_TYPE_XY_THREE="257";
	public final static String GAME_TYPE_GF_FC="208";//五分彩
	public final static String GAME_TYPE_XY_FC="258";
	
	//综合类
	public final static String GAME_TYPE_GF_BJKL8 ="301";//北京快乐8
	public final static String GAME_TYPE_XY_BJKL8 ="351";
	public final static String GAME_TYPE_GF_XJPLU28 ="302";//新加坡幸运28
	public final static String GAME_TYPE_XY_XJPLU28 ="352";
	public final static String GAME_TYPE_GF_BJLU28 ="303";//PC蛋蛋(北京幸运28)
	public final static String GAME_TYPE_XY_BJLU28 ="353";
	public final static String GAME_TYPE_GF_GDK10 ="305";//广东快乐10分
	public final static String GAME_TYPE_XY_GDK10 ="355";
	public final static String GAME_TYPE_GF_GXK10="306";//广西快乐十分
	public final static String GAME_TYPE_XY_GXK10="356";
	public final static String GAME_TYPE_GF_CQK10="307";//重庆快乐十分(幸运农场)
	public final static String GAME_TYPE_XY_CQK10="357";
	public final static String GAME_TYPE_GF_POKER="308";//快乐扑克3
	public final static String GAME_TYPE_XY_POKER="358";
	public final static String GAME_TYPE_GF_MARKSIX="309";//香港六合彩
	public final static String GAME_TYPE_XY_MARKSIX="359";
	public final static String GAME_TYPE_GF_SFLHC="310";//极速六合彩
	public final static String GAME_TYPE_XY_SFLHC="360";
	public final static String GAME_TYPE_GF_DCB="311";//双色球
	public final static String GAME_TYPE_XY_DCB="361";
	
	//11选5类
	public final static String GAME_TYPE_GF_GDPICK11="501";// 广东11选5
	public final static String GAME_TYPE_XY_GDPICK11="551";
	public final static String GAME_TYPE_GF_JXPICK11="502";//江西11选5
	public final static String GAME_TYPE_XY_JXPICK11="552";
	public final static String GAME_TYPE_GF_SDPICK11="503";//山东11选5
	public final static String GAME_TYPE_XY_SDPICK11="553";
	public final static String GAME_TYPE_GF_SXPICK11="504";//山西11选5
	public final static String GAME_TYPE_XY_SXPICK11="554";
	public final static String GAME_TYPE_GF_BJPICK11="505";//北京11选5
	public final static String GAME_TYPE_XY_BJPICK11="555";
	public final static String GAME_TYPE_GF_TJPICK11="506";//天津11选5
	public final static String GAME_TYPE_XY_TJPICK11="556";
	public final static String GAME_TYPE_GF_HEBPICK11="507";//河北11选5
	public final static String GAME_TYPE_XY_HEBPICK11="557";
	public final static String GAME_TYPE_GF_NMGPICK11="508";//内蒙古11选5
	public final static String GAME_TYPE_XY_NMGPICK11="558";
	public final static String GAME_TYPE_GF_LNPICK11="509";//辽宁11选5
	public final static String GAME_TYPE_XY_LNPICK11="559";
	public final static String GAME_TYPE_GF_JLPICK11="510";//吉林11选5
	public final static String GAME_TYPE_XY_JLPICK11="560";
	public final static String GAME_TYPE_GF_HLJPICK11="511";//黑龙江11选5
	public final static String GAME_TYPE_XY_HLJPICK11="561";
	public final static String GAME_TYPE_GF_SHPICK11="512";//上海11选5
	public final static String GAME_TYPE_XY_SHPICK11="562";
	public final static String GAME_TYPE_GF_JSPICK11="513";//江苏11选5
	public final static String GAME_TYPE_XY_JSPICK11="563";
	public final static String GAME_TYPE_GF_ZJPICK11="514";//浙江11选5
	public final static String GAME_TYPE_XY_ZJPICK11="564";
	public final static String GAME_TYPE_GF_AHPICK11="515";//安徽11选5
	public final static String GAME_TYPE_XY_AHPICK11="565";
	public final static String GAME_TYPE_GF_FJPICK11="516";//福建11选5
	public final static String GAME_TYPE_XY_FJPICK11="566";
	public final static String GAME_TYPE_GF_HNPICK11="517";//河南11选5
	public final static String GAME_TYPE_XY_HNPICK11="567";
	public final static String GAME_TYPE_GF_HUBPICK11="518";//湖北11选5
	public final static String GAME_TYPE_XY_HUBPICK11="568";
	public final static String GAME_TYPE_GF_GXPICK11="519";//广西11选5
	public final static String GAME_TYPE_XY_GXPICK11="569";
	public final static String GAME_TYPE_GF_GZPICK11="520";//贵州11选5
	public final static String GAME_TYPE_XY_GZPICK11="570";
	public final static String GAME_TYPE_GF_SHXPICK11="521";//陕西11选5
	public final static String GAME_TYPE_XY_SHXPICK11="571";
	public final static String GAME_TYPE_GF_GSPICK11="522";//甘肃11选5
	public final static String GAME_TYPE_XY_GSPICK11="572";
	public final static String GAME_TYPE_GF_XJPICK11="523";//新疆11选5
	public final static String GAME_TYPE_XY_XJPICK11="573";
	public final static String GAME_TYPE_GF_YNPICK11="524";//云南11选5
	public final static String GAME_TYPE_XY_YNPICK11="574";
	
	//快3类
	public final static String GAME_TYPE_GF_JSK3 ="601";//江苏快3
	public final static String GAME_TYPE_XY_JSK3="651";
	public final static String GAME_TYPE_GF_AHK3="602";//安徽快3
	public final static String GAME_TYPE_XY_AHK3="652";
	public final static String GAME_TYPE_GF_BJK3="603";//北京快3
	public final static String GAME_TYPE_XY_BJK3="653";
	public final static String GAME_TYPE_GF_FJK3="604";//福建快3
	public final static String GAME_TYPE_XY_FJK3="654";
	public final static String GAME_TYPE_GF_GZK3="605";//贵州快3
	public final static String GAME_TYPE_XY_GZK3="655";
	public final static String GAME_TYPE_GF_GXK3="606";//广西快3
	public final static String GAME_TYPE_XY_GXK3="656";
	public final static String GAME_TYPE_GF_GSK3="607";//甘肃快3
	public final static String GAME_TYPE_XY_GSK3="657";
	public final static String GAME_TYPE_GF_HUBK3="608";//湖北快3
	public final static String GAME_TYPE_XY_HUBK3="658";
	public final static String GAME_TYPE_GF_HEBK3="609";//河北快3
	public final static String GAME_TYPE_XY_HEBK3="659";
	public final static String GAME_TYPE_GF_HNK3="610";//河南快3
	public final static String GAME_TYPE_XY_HNK3="660";
	public final static String GAME_TYPE_GF_JXK3="611";//江西快3
	public final static String GAME_TYPE_XY_JXK3="661";
	public final static String GAME_TYPE_GF_JLK3="612";//吉林快3
	public final static String GAME_TYPE_XY_JLK3="662";
	public final static String GAME_TYPE_GF_NMGK3="613";//内蒙古快3
	public final static String GAME_TYPE_XY_NMGK3="663";
	public final static String GAME_TYPE_GF_SHK3="614";//上海快3
	public final static String GAME_TYPE_XY_SHK3="654";
	
	
	// 竞猜
	public final static String GAME_TYPE_JC= "666";
	
	
	//--------------------------------------------------------------------
	
	
	//彩种时间定义 hzb 20171128------------------------------------
	public final static String GAME_GDPICK11_BEGIN_TIME = "09:00:00";// 广东11选5开始时间
	public final static String GAME_GDPICK11_END_TIME = "23:00:00";// 广东11选5结束时间
	public final static String GAME_JXPICK11_BEGIN_TIME = "09:00:00";// 江西11选5开始时间
	public final static String GAME_JXPICK11_END_TIME  = "23:00:00";// 江西11选5结束时间
	public final static String GAME_SDPICK11_BEGIN_TIME = "08:25:00";// 山东11选5开始时间
	public final static String GAME_SDPICK11_END_TIME = "22:55:00";// 山东11选5结束时间
	public final static String GAME_SXPICK11_BEGIN_TIME= "08:15:00";//山西11选5开始时间
	public final static String GAME_SXPICK11_END_TIME = "23:55:00";//山西11选5结束时间
	public final static String GAME_BJPICK11_BEGIN_TIME = "08:50:00";//北京11选5开始时间
	public final static String GAME_BJPICK11_END_TIME = "23:00:00";//北京11选5结束时间
	public final static String GAME_TJPICK11_BEGIN_TIME = "09:00:00";//天津11选5开始时间
	public final static String GAME_TJPICK11_END_TIME = "23:50:00";//天津11选5结束时间
	public final static String GAME_HEBPICK11_BEGIN_TIME = "08:20:00";//河北11选5开始时间
	public final static String GAME_HEBPICK11_END_TIME = "22:30:00";//河北11选5结束时间
	public final static String GAME_NMGPICK11_BEGIN_TIME = "08:56:00";//内蒙古11选5开始时间
	public final static String GAME_NMGPICK11_END_TIME = "23:06:00";//内蒙古11选5结束时间
	public final static String GAME_LNPICK11_BEGIN_TIME = "08:40:00";//辽宁11选5开始时间
	public final static String GAME_LNPICK11_END_TIME = "22:30:00";//辽宁11选5结束时间
	public final static String GAME_JLPICK11_BEGIN_TIME = "08:20:00";//吉林11选5开始时间
	public final static String GAME_JLPICK11_END_TIME = "21:30:00";//吉林11选5结束时间
	public final static String GAME_HLJPICK11_BEGIN_TIME = "07:55:00";//黑龙江11选5开始时间
	public final static String GAME_HLJPICK11_END_TIME = "22:35:00";//黑龙江11选5结束时间
	public final static String GAME_SHPICK11_BEGIN_TIME = "09:00:00";//上海11选5开始时间
	public final static String GAME_SHPICK11_END_TIME = "23:50:00";//上海11选5结束时间
	public final static String GAME_JSPICK11_BEGIN_TIME = "08:25:00";//江苏11选5开始时间
	public final static String GAME_JSPICK11_END_TIME = "22:05:00";//江苏11选5结束时间
	public final static String GAME_ZJPICK11_BEGIN_TIME = "08:20:00";//浙江11选5开始时间
	public final static String GAME_ZJPICK11_END_TIME = "23:30:00";//浙江11选5结束时间
	public final static String GAME_AHPICK11_BEGIN_TIME = "08:30:00";//安徽11选5开始时间
	public final static String GAME_AHPICK11_END_TIME = "22:00:00";//安徽11选5结束时间
	public final static String GAME_FJPICK11_BEGIN_TIME = "08:00:00";//福建11选5开始时间
	public final static String GAME_FJPICK11_END_TIME = "23:00:00";//福建11选5结束时间
	public final static String GAME_HNPICK11_BEGIN_TIME = "08:20:00";//河南11选5开始时间
	public final static String GAME_HNPICK11_END_TIME = "23:00:00";//河南11选5结束时间
	public final static String GAME_HUBPICK11_BEGIN_TIME = "08:25:00";//湖北11选5开始时间
	public final static String GAME_HUBPICK11_END_TIME = "21:55:00";//湖北11选5结束时间
	public final static String GAME_GXPICK11_BEGIN_TIME = "08:50:00";//广西11选5开始时间
	public final static String GAME_GXPICK11_END_TIME = "23:50:00";//广西11选5结束时间
	public final static String GAME_SCPICK11_BEGIN_TIME = "08:50:00";//四川11选5开始时间
	public final static String GAME_SCPICK11_END_TIME = "23:50:00";//四川11选5结束时间
	public final static String GAME_GZPICK11_BEGIN_TIME = "08:50:00";//贵州11选5开始时间
	public final static String GAME_GZPICK11_END_TIME = "22:10:00";//贵州11选5结束时间
	public final static String GAME_SHXPICK11_BEGIN_TIME = "08:20:00";//陕西11选5开始时间
	public final static String GAME_SHXPICK11_END_TIME = "23:00:00";//陕西11选5结束时间
	public final static String GAME_GSPICK11_BEGIN_TIME = "08:50:00";//甘肃11选5开始时间
	public final static String GAME_GSPICK11_END_TIME = "23:10:00";//甘肃11选5结束时间
	public final static String GAME_XJPICK11_BEGIN_TIME = "10:00:00";//新疆11选5开始时间
	public final static String GAME_XJPICK11_END_TIME = "24:00:00";//新疆11选5结束时间
	public final static String GAME_YNPICK11_BEGIN_TIME = "08:50:00";//云南11选5开始时间
	public final static String GAME_YNPICK11_END_TIME = "23:00:00";//云南11选5结束时间
	
	public final static String GAME_JSK3_BEGIN_TIME ="08:30:00";// 江苏快乐3开始时间
	public final static String GAME_JSK3_END_TIME ="22:10:00";// 江苏快乐3结束时间
	public final static String GAME_AHK3_BEGIN_TIME = "08:40:00";//安徽快3开始时间
	public final static String GAME_AHK3_END_TIME = "22:00:00";//安徽快3结束时间
	public final static String GAME_BJK3_BEGIN_TIME = "09:00:00";//北京快3开始时间
	public final static String GAME_BJK3_END_TIME = "23:50:00";//北京快3结束时间
	public final static String GAME_FJK3_BEGIN_TIME = "09:00:00";//福建快3开始时间
	public final static String GAME_FJK3_END_TIME = "22:00:00";//福建快3结束时间
	public final static String GAME_GZK3_BEGIN_TIME = "09:00:00";//贵州快3开始时间
	public final static String GAME_GZK3_END_TIME = "22:00:00";//贵州快3结束时间
	public final static String GAME_GXK3_BEGIN_TIME = "09:27:00";//广西快3开始时间
	public final static String GAME_GXK3_END_TIME = "22:27:00";//广西快3结束时间
	public final static String GAME_GSK3_BEGIN_TIME = "10:00:00";//甘肃快3开始时间
	public final static String GAME_GSK3_END_TIME = "21:00:00";//甘肃快3结束时间
	public final static String GAME_HUBK3_BEGIN_TIME = "09:00:00";//湖北快3开始时间
	public final static String GAME_HUBK3_END_TIME = "22:00:00";//湖北快3结束时间
	public final static String GAME_HEBK3_BEGIN_TIME = "08:30:00";//河北快3开始时间
	public final static String GAME_HEBK3_END_TIME = "22:00:00";//河北快3结束时间
	public final static String GAME_HNK3_BEGIN_TIME = "08:30:00";//河南快3开始时间
	public final static String GAME_HNK3_END_TIME = "22:30:00";//河南快3结束时间
	public final static String GAME_JXK3_BEGIN_TIME = "08:55:00";//江西快3开始时间
	public final static String GAME_JXK3_END_TIME = "22:55:00";//江西快3结束时间
	public final static String GAME_JLK3_BEGIN_TIME = "08:20:00";//吉林快3开始时间
	public final static String GAME_JLK3_END_TIME = "22:00:00";//吉林快3结束时间
	public final static String GAME_NMGK3_BEGIN_TIME = "09:40:00";//内蒙古快3开始时间
	public final static String GAME_NMGK3_END_TIME = "21:40:00";//内蒙古快3结束时间
	public final static String GAME_SHK3_BEGIN_TIME = "08:48:00";//上海快3开始时间
	public final static String GAME_SHK3_END_TIME = "22:28:00";//上海快3结束时间

	
	
	/**方案类型 -代购**/
	public final static String PROCUREMENT_SERVICE = "0";//代购
	/**方案类型 -合买**/
	public final static String SPONSOR = "1";//合买
	/**购买类型 -发起购买**/
	public final static String ORIGINATE = "0";//发起购买
	/**购买类型 -参与购买**/
	public final static String PARTICIPATOR = "1";//参与购买
	
	
	/**充值方式类型-通道**/
	public final static String RECHARGE_WAY_1 = "1";
	/**充值方式类型-渠道**/
	public final static String RECHARGE_WAY_2 = "2";
	/**充值方式类型-充值方式**/
	public final static String RECHARGE_WAY_3 = "3";
	
	//系统定时器配置
	private final static Map<String,String> TIMER_CONFIG_MAP=new HashMap<String, String>();//定时配置
	private final static void loadTimerConfig(){
		//配置详情
		String timerData = ManageFile.loadTextFileUTF8(webRootPath+"/WEB-INF/classes/timer.properties");
		String[] timerArr = timerData.split("\n");
		for(int i=0;i<timerArr.length;i++){
			String confs = timerArr[i];
			if(confs!=null && confs.trim().length()>0 && !confs.startsWith("#")){
				String[] confArr = confs.split("=");
				TIMER_CONFIG_MAP.put(confArr[0].trim(), confArr[1].trim());
			}
		}
	}
	/**
	 * 获取定时开关方法
	 * @param timerCode
	 * @return true or false
	 */
	public final static boolean getTimerOpen(String timerCode){
		String tf = TIMER_CONFIG_MAP.get(timerCode);
		if(tf==null || tf=="") tf = "false";
		return tf.equals("true")?true:false;
	}
	//
	
	/**彩票订单状态-未完成**/
	public final static String UNFINISHED = "0";//0=未完成
	/**彩票订单状态-投注无效。退款（系统撤单）**/
	public final static String INVALID_REFUND = "1";//1=投注无效。退款（系统撤单）
	/**彩票订单状态-未开奖 （已出票）(进行中)**/
	public final static String INIT = "2";//2=未开奖 （已出票）(进行中)
	/**彩票订单状态-开奖中**/
	public final static String OPENING = "3";//3=开奖中
	/**彩票订单状态-中奖**/
	public final static String WIN = "4";//4=中奖
	/**彩票订单状态-未中奖**/
	public final static String WIN_NO = "5";//5=未中奖
	/**彩票订单状态-打和**/
	public final static String WIN_HE = "6";//6=打和
	
	/**不保底**/
	public final static String NOT_GUARANTEE = "0";//不保底
	/**保底**/
	public final static String GUARANTEE = "1";//保底
	/**保底百分比  20% **/
	public final static String GUARANTEE_PERCENTAGE = "0.2";
	/**中奖就停止追号**/
	public final static String WIN_STOP = "1";//中奖就停止追号
	/**中奖继续追号**/
	public final static String WIN_NO_STOP = "0";//中奖继续追号
	/**不追号**/
	public final static String NOT_ADD_NO = "0";//不追号
	/**追号**/
	public final static String ADD_NO = "1";//追号

	/**开奖状态-未开奖**/
	public final static String OPEN_STATUS_INIT = "0";
	/**开奖状态-开奖中**/
	public final static String OPEN_STATUS_OPENING = "1";
	/**开奖状态-已开奖**/
	public final static String OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static String REG_STATUS_OPEN = "1";//允许注册
	public final static String REG_STATUS_CLOSE = "4";//注册关闭
	/**
	 * 苹果审核控制版本
	 */
	public static String IOS_REVIEW_VISION = "";
	
	/** 最多一天发送短信条数 */
	public static String msgMaxCount = "5";
	/** 最短发送间隔时间 */
	public static String msgTime = "10";
	
	
	/**交易类型-在线支付**/
	public final static String TRADE_TYPE_MONEY_ONLINE = "1";
	/**交易类型-积分支付**/
	public final static String TRADE_TYPE_MONEY_POINTS = "2";
	

	
	
	/**游戏类型-双色球**/
	public final static String GAME_TYPE_SSQ = "1";
	/**游戏类型-快三**/
	public final static String GAME_TYPE_K3 = "2";
	/**积分说明**/
	public final static String GAME_POINT_INSTRUCTION= "27";
	
	//积分交易类型
	public final static String POINT_TYPE_ONLINE= "1";//在线充值
	public final static String POINT_TYPE_CARD= "2";//卡充--未用
	public final static String POINT_TYPE_REGISTER= "3";//注册赠送
	public final static String POINT_TYPE_SHARE= "4";//分享--未用
	public final static String POINT_TYPE_INVITE= "5";//邀请--未用
	
	public final static String POINT_TYPE_BET_HHMF= "26";//游戏黑红梅方
	public final static String POINT_TYPE_MANAGER_SET= "27";//管理员充值

	
	
	
	public final static String POINT_TYPE_BET_HBK3= "28";//湖北快三
	public final static String POINT_TYPE_BET_AHK3= "29";//安徽快三
	public final static String POINT_TYPE_BET_GXK3= "30";//广西快三
	public final static String POINT_TYPE_BET_JLK3= "31";//吉林快三
	public final static String POINT_TYPE_BET_JSK3= "32";//江苏快三
	public final static String POINT_TYPE_BET_GXK10= "33";//广西快乐十分
	public final static String POINT_TYPE_BET_GDK10= "34";//广东快乐十分
	public final static String POINT_TYPE_BET_HNK10= "35";//湖南快乐十分
	public final static String POINT_TYPE_BET_CQK10= "36";//重庆快乐十分
	
	public final static String POINT_TYPE_BJ_LUCKY28= "37";//PC蛋蛋(北京幸运28)
	public final static String POINT_TYPE_XJP_LUCKY28= "38";//幸运28(新加坡28)

	
	public final static int POINT_12SHENGXIAO_MAX=5000;//12生肖每次最大下注
	public final static int POINT_HHMF_MAX_HHMF = 100000;//黑红梅方四中花色最大下注 (每个人）
	public final static int POINT_HHMF_MAX_WANG = 20000;//王最大下注 (每个人）
	public final static int POINT_HHMF_MAX_EVERY_BET = 500;//王最大下注 (每个人）
	public final static String GAME_HHMF_HEITAO= "1";//黑桃
	public final static String GAME_HHMF_HONGTAO= "2";//红桃
	public final static String GAME_HHMF_MEIHUA= "3";//梅花
	public final static String GAME_HHMF_FANGKUAI= "4";//方块
	public final static String GAME_HHMF_WANG= "5";//王
	
	public final static String GAME_WIN_STATUS_NOOPEN= "0";//未开奖
	public final static String GAME_WIN_STATUS_WIN= "1";//中奖
	public final static String GAME_WIN_STATUS_NOWIN= "2";//未中奖
	
	public final static String GAME_HHMF_LEVEL_DI= "1";//低
	public final static String GAME_HHMF_LEVEL_ZHONG= "2";//中
	public final static String GAME_HHMF_LEVEL_GAO= "3";//高
	
	/**
	 * 根据gameType获取gameCode
	 * @param gameType
	 * @return
	 */
	public final static String getGameCodeOfGameType(String gameType){
		if(gameType.equals("101")) return "gfbjpk10";
		else if(gameType.equals("151")) return "xybjpk10";
		else if(gameType.equals("102")) return "gfsfpk10";
		else if(gameType.equals("152")) return "xysfpk10";
		else if(gameType.equals("103")) return "gfsfpk102";
		else if(gameType.equals("153")) return "xysfpk102";
		else if(gameType.equals("104")) return "gfxyft";
		else if(gameType.equals("154")) return "xyxyft";
		else if(gameType.equals("105")) return "gfjsft";
		else if(gameType.equals("155")) return "xyjsft";
		else if(gameType.equals("201")) return "gfcqssc";
		else if(gameType.equals("251")) return "xycqssc";
		else if(gameType.equals("202")) return "gfxjssc";
		else if(gameType.equals("252")) return "xyxjssc";
		else if(gameType.equals("203")) return "gftjssc";
		else if(gameType.equals("253")) return "xytjssc";
		else if(gameType.equals("205")) return "gfbjssc";
		else if(gameType.equals("255")) return "xybjssc";
		else if(gameType.equals("256")) return "xybj3";
		else if(gameType.equals("207")) return "gfsfc";
		else if(gameType.equals("208")) return "gfwfc";
		else if(gameType.equals("258")) return "xyfive";
		else if(gameType.equals("352")) return "xyxjplu28";
		else if(gameType.equals("353")) return "xybjlu28";
		else if(gameType.equals("355")) return "xygdk10";
		else if(gameType.equals("356")) return "xygxk10";
		else if(gameType.equals("357")) return "xycqk10";
		else if(gameType.equals("358")) return "xypoker";
		else if(gameType.equals("359")) return "xymarksix";
		else if(gameType.equals("360")) return "xysflhc";
		else if(gameType.equals("501")) return "gfgdpick11";
		else if(gameType.equals("551")) return "xygdpick11";
		else if(gameType.equals("502")) return "gfjxpick11";
		else if(gameType.equals("552")) return "xyjxpick11";
		else if(gameType.equals("503")) return "gfsdpick11";
		else if(gameType.equals("553")) return "xysdpick11";
		else if(gameType.equals("512")) return "gfshpick11";
		else if(gameType.equals("515")) return "gfahpick11";
		else if(gameType.equals("601")) return "gfjsk3";
		else if(gameType.equals("651")) return "xyjsk3";
		else if(gameType.equals("602")) return "gfahk3";
		else if(gameType.equals("603")) return "gfbjk3";
		else if(gameType.equals("653")) return "xybjk3";
		else if(gameType.equals("606")) return "gfgxk3";
		else if(gameType.equals("608")) return "gfhubk3";
		else if(gameType.equals("611")) return "gfjxk3";
		else if(gameType.equals("612")) return "gfjlk3";
		else if(gameType.equals("614")) return "gfshk3";
		return "";
	}
	
	/**
	 * 返回彩种名称
	 * @param gameType 彩种类型
	 * @return
	 */
	public final static String getGameName(String gameType){
		if (Constants.GAME_TYPE_XY_BJPK10.equals(gameType)) return "信用北京赛车";
		else if(Constants.GAME_TYPE_XY_SFPK10.equals(gameType)) return "信用极速赛车";
		else if(Constants.GAME_TYPE_XY_SFPK102.equals(gameType)) return "信用三分PK拾";
		else if(Constants.GAME_TYPE_XY_XYFT.equals(gameType)) return "信用幸运飞艇";
		else if(Constants.GAME_TYPE_XY_JSFT.equals(gameType)) return "信用极速飞艇";
		else if(Constants.GAME_TYPE_XY_CQSSC.equals(gameType)) return "信用重庆时时彩";
		else if(Constants.GAME_TYPE_XY_XJSSC.equals(gameType)) return "信用新疆时时彩";
		else if(Constants.GAME_TYPE_XY_TJSSC.equals(gameType)) return "信用天津时时彩";
		else if(Constants.GAME_TYPE_XY_JXSSC.equals(gameType)) return "信用江西时时彩";
		else if(Constants.GAME_TYPE_XY_BJSSC.equals(gameType)) return "信用北京时时彩";
		else if(Constants.GAME_TYPE_XY_BJ3.equals(gameType)) return "信用三分彩(北京)";
		//else if(Constants.GAME_TYPE_XY_THREE.equals(gameType)) return "信用三分彩";
		else if(Constants.GAME_TYPE_XY_FC.equals(gameType)) return "信用五分彩";
		else if(Constants.GAME_TYPE_XY_BJKL8.equals(gameType)) return "信用北京快乐8";
		else if(Constants.GAME_TYPE_XY_XJPLU28.equals(gameType)) return "信用新加坡幸运28";
		else if(Constants.GAME_TYPE_XY_BJLU28.equals(gameType)) return "信用PC蛋蛋";
		else if(Constants.GAME_TYPE_XY_GDK10.equals(gameType)) return "信用广东快乐10分";
		else if(Constants.GAME_TYPE_XY_GXK10.equals(gameType)) return "信用广西快乐十分";
		else if(Constants.GAME_TYPE_XY_CQK10.equals(gameType)) return "信用幸运农场";
		else if(Constants.GAME_TYPE_XY_POKER.equals(gameType)) return "信用快乐扑克3";
		else if(Constants.GAME_TYPE_XY_MARKSIX.equals(gameType)) return "信用香港六合彩";
		else if(Constants.GAME_TYPE_XY_SFLHC.equals(gameType)) return "信用极速六合彩";
		else if(Constants.GAME_TYPE_XY_DCB.equals(gameType)) return "信用双色球";
		else if(Constants.GAME_TYPE_XY_GDPICK11.equals(gameType)) return "信用广东11选5";
		else if(Constants.GAME_TYPE_XY_JXPICK11.equals(gameType)) return "信用江西11选5";
		else if(Constants.GAME_TYPE_XY_SDPICK11.equals(gameType)) return "信用山东11选5";
		else if(Constants.GAME_TYPE_XY_SXPICK11.equals(gameType)) return "信用山西11选5";
		else if(Constants.GAME_TYPE_XY_BJPICK11.equals(gameType)) return "信用北京11选5";
		else if(Constants.GAME_TYPE_XY_TJPICK11.equals(gameType)) return "信用天津11选5";
		else if(Constants.GAME_TYPE_XY_HEBPICK11.equals(gameType)) return "信用河北11选5";
		else if(Constants.GAME_TYPE_XY_NMGPICK11.equals(gameType)) return "信用内蒙古11选5";
		else if(Constants.GAME_TYPE_XY_LNPICK11.equals(gameType)) return "信用辽宁11选5";
		else if(Constants.GAME_TYPE_XY_JLPICK11.equals(gameType)) return "信用吉林11选5";
		else if(Constants.GAME_TYPE_XY_HLJPICK11.equals(gameType)) return "信用黑龙江11选5";
		else if(Constants.GAME_TYPE_XY_SHPICK11.equals(gameType)) return "信用上海11选5";
		else if(Constants.GAME_TYPE_XY_JSPICK11.equals(gameType)) return "信用江苏11选5";
		else if(Constants.GAME_TYPE_XY_ZJPICK11.equals(gameType)) return "信用浙江11选5";
		else if(Constants.GAME_TYPE_XY_AHPICK11.equals(gameType)) return "信用安徽11选5";
		else if(Constants.GAME_TYPE_XY_FJPICK11.equals(gameType)) return "信用福建11选5";
		else if(Constants.GAME_TYPE_XY_HNPICK11.equals(gameType)) return "信用河南11选5";
		else if(Constants.GAME_TYPE_XY_HUBPICK11.equals(gameType)) return "信用湖北11选5";
		else if(Constants.GAME_TYPE_XY_GXPICK11.equals(gameType)) return "信用广西11选5";
		else if(Constants.GAME_TYPE_XY_GZPICK11.equals(gameType)) return "信用贵州11选5";
		else if(Constants.GAME_TYPE_XY_SHXPICK11.equals(gameType)) return "信用陕西11选5";
		else if(Constants.GAME_TYPE_XY_GSPICK11.equals(gameType)) return "信用甘肃11选5";
		else if(Constants.GAME_TYPE_XY_XJPICK11.equals(gameType)) return "信用新疆11选5";
		else if(Constants.GAME_TYPE_XY_YNPICK11.equals(gameType)) return "信用云南11选5";
		else if(Constants.GAME_TYPE_XY_JSK3.equals(gameType)) return "信用江苏快3";
		else if(Constants.GAME_TYPE_XY_AHK3.equals(gameType)) return "信用安徽快3";
		else if(Constants.GAME_TYPE_XY_BJK3.equals(gameType)) return "信用北京快3";
		else if(Constants.GAME_TYPE_XY_FJK3.equals(gameType)) return "信用福建快3";
		else if(Constants.GAME_TYPE_XY_GZK3.equals(gameType)) return "信用贵州快3";
		else if(Constants.GAME_TYPE_XY_GXK3.equals(gameType)) return "信用广西快3";
		else if(Constants.GAME_TYPE_XY_GSK3.equals(gameType)) return "信用甘肃快3";
		else if(Constants.GAME_TYPE_XY_HUBK3.equals(gameType)) return "信用湖北快3";
		else if(Constants.GAME_TYPE_XY_HEBK3.equals(gameType)) return "信用河北快3";
		else if(Constants.GAME_TYPE_XY_HNK3.equals(gameType)) return "信用河南快3";
		else if(Constants.GAME_TYPE_XY_JXK3.equals(gameType)) return "信用江西快3";
		else if(Constants.GAME_TYPE_XY_JLK3.equals(gameType)) return "信用吉林快3";
		else if(Constants.GAME_TYPE_XY_NMGK3.equals(gameType)) return "信用内蒙古快3";
		else if(Constants.GAME_TYPE_XY_SHK3.equals(gameType)) return "信用上海快3";
		else if(Constants.GAME_TYPE_GF_DCB.equals(gameType)) return "官方双色球";
		else if(Constants.GAME_TYPE_GF_FC.equals(gameType)) return "官方五分彩";
		else if(Constants.GAME_TYPE_GF_THREE.equals(gameType)) return "官方三分彩";
		else if(Constants.GAME_TYPE_GF_JXPICK11.equals(gameType)) return "官方江西11选5";
		else if(Constants.GAME_TYPE_GF_CQSSC.equals(gameType)) return "官方重庆时时彩";
		else if(Constants.GAME_TYPE_GF_TJSSC.equals(gameType)) return "官方天津时时彩";
		else if(Constants.GAME_TYPE_GF_XJSSC.equals(gameType)) return "官方新疆时时彩";
		else if(Constants.GAME_TYPE_GF_JXSSC.equals(gameType)) return "官方江西时时彩";
		else if(Constants.GAME_TYPE_GF_GDPICK11.equals(gameType)) return "官方广东11选5";
		else if(Constants.GAME_TYPE_GF_JXPICK11.equals(gameType)) return "官方江西11选5";
		else if(Constants.GAME_TYPE_GF_SDPICK11.equals(gameType)) return "官方山东11选5";
		else if(Constants.GAME_TYPE_GF_SXPICK11.equals(gameType)) return "官方山西11选5";
		else if(Constants.GAME_TYPE_GF_BJPICK11.equals(gameType)) return "官方北京11选5";
		else if(Constants.GAME_TYPE_GF_TJPICK11.equals(gameType)) return "官方天津11选5";
		else if(Constants.GAME_TYPE_GF_HEBPICK11.equals(gameType)) return "官方河北11选5";
		else if(Constants.GAME_TYPE_GF_NMGPICK11.equals(gameType)) return "官方内蒙古11选5";
		else if(Constants.GAME_TYPE_GF_LNPICK11.equals(gameType)) return "官方辽宁11选5";
		else if(Constants.GAME_TYPE_GF_JLPICK11.equals(gameType)) return "官方吉林11选5";
		else if(Constants.GAME_TYPE_GF_HLJPICK11.equals(gameType)) return "官方黑龙江11选5";
		else if(Constants.GAME_TYPE_GF_SHPICK11.equals(gameType)) return "官方上海11选5";
		else if(Constants.GAME_TYPE_GF_JSPICK11.equals(gameType)) return "官方江苏11选5";
		else if(Constants.GAME_TYPE_GF_ZJPICK11.equals(gameType)) return "官方浙江11选5";
		else if(Constants.GAME_TYPE_GF_AHPICK11.equals(gameType)) return "官方安徽11选5";
		else if(Constants.GAME_TYPE_GF_FJPICK11.equals(gameType)) return "官方福建11选5";
		else if(Constants.GAME_TYPE_GF_HNPICK11.equals(gameType)) return "官方河南11选5";
		else if(Constants.GAME_TYPE_GF_HUBPICK11.equals(gameType)) return "官方湖北11选5";
		else if(Constants.GAME_TYPE_GF_GXPICK11.equals(gameType)) return "官方广西11选5";
		else if(Constants.GAME_TYPE_GF_GZPICK11.equals(gameType)) return "官方贵州11选5";
		else if(Constants.GAME_TYPE_GF_SHXPICK11.equals(gameType)) return "官方陕西11选5";
		else if(Constants.GAME_TYPE_GF_GSPICK11.equals(gameType)) return "官方甘肃11选5";
		else if(Constants.GAME_TYPE_GF_XJPICK11.equals(gameType)) return "官方新疆11选5";
		else if(Constants.GAME_TYPE_GF_YNPICK11.equals(gameType)) return "官方云南11选5";
		else if(Constants.GAME_TYPE_GF_JSK3.equals(gameType)) return "官方江苏快3";
		else if(Constants.GAME_TYPE_GF_AHK3.equals(gameType)) return "官方安徽快3";
		else if(Constants.GAME_TYPE_GF_BJK3.equals(gameType)) return "官方北京快3";
		else if(Constants.GAME_TYPE_GF_FJK3.equals(gameType)) return "官方福建快3";
		else if(Constants.GAME_TYPE_GF_GZK3.equals(gameType)) return "官方贵州快3";
		else if(Constants.GAME_TYPE_GF_GXK3.equals(gameType)) return "官方广西快3";
		else if(Constants.GAME_TYPE_GF_GSK3.equals(gameType)) return "官方甘肃快3";
		else if(Constants.GAME_TYPE_GF_HUBK3.equals(gameType)) return "官方湖北快3";
		else if(Constants.GAME_TYPE_GF_HEBK3.equals(gameType)) return "官方河北快3";
		else if(Constants.GAME_TYPE_GF_HNK3.equals(gameType)) return "官方河南快3";
		else if(Constants.GAME_TYPE_GF_JXK3.equals(gameType)) return "官方江西快3";
		else if(Constants.GAME_TYPE_GF_JLK3.equals(gameType)) return "官方吉林快3";
		else if(Constants.GAME_TYPE_GF_NMGK3.equals(gameType)) return "官方内蒙古快3";
		else if(Constants.GAME_TYPE_GF_SHK3.equals(gameType)) return "官方上海快3";
		else if(Constants.GAME_TYPE_GF_GDK10.equals(gameType)) return "官方广东快乐10分";
		else if(Constants.GAME_TYPE_GF_GXK10.equals(gameType)) return "官方广西快乐10分";
		else if(Constants.GAME_TYPE_GF_BJPK10.equals(gameType)) return "官方北京赛车";
		else if(Constants.GAME_TYPE_GF_SFPK10.equals(gameType)) return "官方极速赛车";
		else if(Constants.GAME_TYPE_GF_SFPK102.equals(gameType)) return "官方三分PK拾";
		else if(Constants.GAME_TYPE_GF_XYFT.equals(gameType)) return "官方幸运飞艇";
		else if(Constants.GAME_TYPE_GF_JSFT.equals(gameType)) return "官方极速飞艇";
		else if(Constants.GAME_TYPE_GF_THREE.equals(gameType)) return "官方北京三分彩";
		else if(Constants.GAME_TYPE_GF_BJKL8.equals(gameType)) return "官方北京快乐8";
		else if(Constants.GAME_TYPE_JC.equals(gameType)) return "竞猜";
		else {return "其他";}
	}
	
	/**
	 * 返回彩种订单名称
	 * @param gameType 彩种类型
	 * @return
	 */
	public final static String getGameOrderName(String gameType){
		if (Constants.GAME_TYPE_XY_BJ3.equals(gameType)) return "XYBJSFC";
		else if(Constants.GAME_TYPE_XY_BJPK10.equals(gameType)) return "XYBJSC";
		else if(Constants.GAME_TYPE_XY_XJPLU28.equals(gameType)) return "XYXYEB";
		else if(Constants.GAME_TYPE_XY_CQSSC.equals(gameType)) return "XYCQSSC";
		else if(Constants.GAME_TYPE_XY_BJLU28.equals(gameType)) return "XYPCDD";
		else if(Constants.GAME_TYPE_XY_GDK10.equals(gameType)) return "XYGDKLSF";
		else if(Constants.GAME_TYPE_XY_TJSSC.equals(gameType)) return "XYTJSSC";
		else if(Constants.GAME_TYPE_XY_XJSSC.equals(gameType)) return "XYXJSSC";
		else if(Constants.GAME_TYPE_XY_POKER.equals(gameType)) return "XYKLPKS";
		else if(Constants.GAME_TYPE_XY_GDPICK11.equals(gameType)) return "XYGDSYXW";
		else if(Constants.GAME_TYPE_XY_JSK3.equals(gameType)) return "XYJSKLS";
		else if(Constants.GAME_TYPE_XY_GXK10.equals(gameType)) return "XYGXKLSF";
		else if(Constants.GAME_TYPE_XY_MARKSIX.equals(gameType)) return "XYXGLHC";
		else if(Constants.GAME_TYPE_GF_DCB.equals(gameType)) return "GFSSQ";
		else if(Constants.GAME_TYPE_GF_FC.equals(gameType)) return "GFWFC";
		else if(Constants.GAME_TYPE_GF_THREE.equals(gameType)) return "GFSFC";
		else if(Constants.GAME_TYPE_GF_JXPICK11.equals(gameType)) return "GFJXSYXW";
		else if(Constants.GAME_TYPE_GF_CQSSC.equals(gameType)) return "GFCQSSC";
		else if(Constants.GAME_TYPE_GF_TJSSC.equals(gameType)) return "GFTJSSC";
		else if(Constants.GAME_TYPE_GF_XJSSC.equals(gameType)) return "GFXJSSC";
		else if(Constants.GAME_TYPE_GF_JXSSC.equals(gameType)) return "GFJXSSC";
		else if(Constants.GAME_TYPE_GF_GDPICK11.equals(gameType)) return "GFGDSYXW";
		else if(Constants.GAME_TYPE_GF_JXPICK11.equals(gameType)) return "GFJXSYXW";
		else if(Constants.GAME_TYPE_GF_SDPICK11.equals(gameType)) return "GFSDSYXW";
		else if(Constants.GAME_TYPE_GF_SXPICK11.equals(gameType)) return "GFSXSYXW";
		else if(Constants.GAME_TYPE_GF_BJPICK11.equals(gameType)) return "GFBJSYXW";
		else if(Constants.GAME_TYPE_GF_TJPICK11.equals(gameType)) return "GFTJSYXW";
		else if(Constants.GAME_TYPE_GF_HEBPICK11.equals(gameType)) return "GFHEBSYXW";
		else if(Constants.GAME_TYPE_GF_NMGPICK11.equals(gameType)) return "GFNMGSYXW";
		else if(Constants.GAME_TYPE_GF_LNPICK11.equals(gameType)) return "GFLNSYXW";
		else if(Constants.GAME_TYPE_GF_JLPICK11.equals(gameType)) return "GFJLSYXW";
		else if(Constants.GAME_TYPE_GF_HLJPICK11.equals(gameType)) return "GFHLJSYXW";
		else if(Constants.GAME_TYPE_GF_SHPICK11.equals(gameType)) return "GFSHSYXW";
		else if(Constants.GAME_TYPE_GF_JSPICK11.equals(gameType)) return "GFJSSYXW";
		else if(Constants.GAME_TYPE_GF_ZJPICK11.equals(gameType)) return "GFZJSYXW";
		else if(Constants.GAME_TYPE_GF_AHPICK11.equals(gameType)) return "GFAHSYXW";
		else if(Constants.GAME_TYPE_GF_FJPICK11.equals(gameType)) return "GFFJSYXW";
		else if(Constants.GAME_TYPE_GF_HNPICK11.equals(gameType)) return "GFHNSYXW";
		else if(Constants.GAME_TYPE_GF_HUBPICK11.equals(gameType)) return "GFHUBSYXW";
		else if(Constants.GAME_TYPE_GF_GXPICK11.equals(gameType)) return "GFGXSYXW";
		else if(Constants.GAME_TYPE_GF_GZPICK11.equals(gameType)) return "GFGZSYXW";
		else if(Constants.GAME_TYPE_GF_SHXPICK11.equals(gameType)) return "GFSHXSYXW";
		else if(Constants.GAME_TYPE_GF_GSPICK11.equals(gameType)) return "GFGSSYXW";
		else if(Constants.GAME_TYPE_GF_XJPICK11.equals(gameType)) return "GFXJSYXW";
		else if(Constants.GAME_TYPE_GF_YNPICK11.equals(gameType)) return "GFYNSYXW";
		else if(Constants.GAME_TYPE_GF_AHK3.equals(gameType)) return "GFAHKS";
		else if(Constants.GAME_TYPE_GF_BJK3.equals(gameType)) return "GFBJKS";
		else if(Constants.GAME_TYPE_GF_FJK3.equals(gameType)) return "GFFJKS";
		else if(Constants.GAME_TYPE_GF_GZK3.equals(gameType)) return "GFGZKS";
		else if(Constants.GAME_TYPE_GF_GXK3.equals(gameType)) return "GFGXKS";
		else if(Constants.GAME_TYPE_GF_GSK3.equals(gameType)) return "GFGSKS";
		else if(Constants.GAME_TYPE_GF_HUBK3.equals(gameType)) return "GFHUBKS";
		else if(Constants.GAME_TYPE_GF_HEBK3.equals(gameType)) return "GFHEBKS";
		else if(Constants.GAME_TYPE_GF_HNK3.equals(gameType)) return "GFHNKS";
		else if(Constants.GAME_TYPE_GF_JXK3.equals(gameType)) return "GFJXKS";
		else if(Constants.GAME_TYPE_GF_JLK3.equals(gameType)) return "GFJLKS";
		else if(Constants.GAME_TYPE_GF_NMGK3.equals(gameType)) return "GFNMGKS";
		else if(Constants.GAME_TYPE_GF_SHK3.equals(gameType)) return "GFSHKS";
		else if(Constants.GAME_TYPE_GF_JSK3.equals(gameType)) return "GFJSKS";
		else if(Constants.GAME_TYPE_GF_GDK10.equals(gameType)) return "GFGDK10";
		else if(Constants.GAME_TYPE_GF_GXK10.equals(gameType)) return "GFGXK10";
		else if(Constants.GAME_TYPE_GF_BJPK10.equals(gameType)) return "GFBJPK10";
		else if(Constants.GAME_TYPE_GF_SFPK10.equals(gameType)) return "GFSFPK10";
		else if(Constants.GAME_TYPE_GF_SFPK102.equals(gameType)) return "GFSFPK102";
		else if(Constants.GAME_TYPE_GF_XYFT.equals(gameType)) return "GFXYFT";
		else if(Constants.GAME_TYPE_GF_JSFT.equals(gameType)) return "GFJSFT";
		else if(Constants.GAME_TYPE_GF_THREE.equals(gameType)) return "GFBJ3";
		else if(Constants.GAME_TYPE_GF_BJKL8.equals(gameType)) return "GFBJKL8";
		else {return "其他";}
	}
	
	/**
	 *返回开奖结果中文
	 * @param winResult
	 * @return
	 */
	public final static String getWinResultNameZh(String winResult) {
		if(Constants.UNFINISHED.equals(winResult)) return "未完成";
		else if(Constants.INVALID_REFUND.equals(winResult)) return "系统撤单";
		else if(Constants.INIT.equals(winResult)) return "未开奖";
		else if(Constants.OPENING.equals(winResult)) return "开奖中";
		else if(Constants.WIN.equals(winResult)) return "已中奖";
		else if(Constants.WIN_NO.equals(winResult)) return "未中奖";
		else if(Constants.WIN_HE.equals(winResult)) return "打和";
		else return "其他";
	}
	
	/**
	 *返回充值类型中文  1.支付宝 2.银联 5.微信 6.直充
	 * @param type
	 * @return
	 */
	public final static String getRechargeTypeNameZh(String type) {
//		if(Constants.CASH_TYPE_ONLINE.equals(type)) return "在线充值";	
		if(type.equals("1")||type.equals("2")||type.equals("5"))return "在线充值";	
		else if(Constants.CASH_TYPE_OFFLINE.equals(type)) return "线下充值";
		else if(Constants.CASH_TYPE_CASH_SYSTEM.equals(type)) return "系统充值";
		else if(Constants.CASH_TYPE_CASH_RECHARGE_PRESENT.equals(type)) return "充值赠送";
		else return "其他";
	}
	
	/**
	 *返回资金类型中文
	 * @param cashType
	 * @return
	 */
	public final static String getCashTradeTypeNameZh(String cashType) {
		if(cashType.equals(Constants.CASH_TYPE_ONLINE)) return "在线充值";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_BUY_LOTO )) return "购买彩票";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_GUARANTEE )) return "方案保底";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_FROZEN )) return "保底冻结";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_DRAW )) return "方案撤单";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_SYS_CHARGE )) return "系统扣款";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_PRESENT )) return "注册赠送";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_GUA_BACK )) return "方案退保";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_PRIZE )) return "中奖彩派";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_AGENCY )) return "代理返点";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_EXCHANGE )) return "积分兑换";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_SPONSOR )) return "参与合买";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_DRAWBACK )) return "投注退还";
		else if(cashType.equals(Constants.CASH_TYPE_CARD)) return "卡充";
		else if(cashType.equals(Constants.CASH_TYPE_OFFLINE)) return "线下充值";
		else if(cashType.equals(Constants.CASH_TYPE_INFO_PUBLISH)) return "供求交易";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_OUT)) return "提现";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_RETURN )) return "提现审核返回";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_CHECKOUT)) return "结算";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_REFUND)) return "退款";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_SYS_CHARGE)) return "系统扣款";
		else if(cashType.equals(Constants.CASH_TYPE_MANAGER_SET)) return "管理员充值";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_PRESENT)) return "注册赠送";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_PRIZE)) return "中奖彩派";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_BUY_LOTO)) return "购买彩票";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN)) return "下级会员充值返水";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN)) return "下级会员打码返水";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_MEMBER_RETURN_SELF)) return "会员充值系统赠送";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF)) return "会员打码每天返水给自己";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_SIGN)) return "签到赠送";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_REPAIR_DETAIL_ADD)) return "修复明细加款";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_REPAIR_DETAIL_SUB)) return "修复明细扣款";
		else if(cashType.equals(Constants.CASH_TYPE_RECHARGE_AWARD)) return "充值跳级奖励";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_REVOKE_PRIZE)) return "撤回彩派金额";
		else if(cashType.equals(Constants.CASH_TYPE_CASH_BUY_JC)) return "竞猜投注";
		else return "其他";
	}
	
	/**
	 * 返回积分类型中文
	 * @param cashType
	 * @return
	 */
	public final static String getPointTradeTypeNameZh(String pointType) {
		if(pointType.equals(Constants.POINT_TYPE_REGISTER)) return "注册赠送";
		else if(pointType.equals(Constants.POINT_TYPE_ONLINE)) return "在线充值";
		else if(pointType.equals(Constants.POINT_TYPE_INVITE)) return "提现";
		else if(pointType.equals(Constants.POINT_TYPE_MANAGER_SET)) return "管理员充值";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_HBK3)) return "湖北快三";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_AHK3)) return "安徽快三";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_GXK3)) return "广西快三";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_JLK3)) return "吉林快三";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_JSK3)) return "江苏快三";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_GXK10)) return "广西快乐十分";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_GDK10)) return "广东快乐十分";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_HNK10)) return "湖南快乐十分";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_CQK10)) return "重庆快乐十分";
		else if(pointType.equals(Constants.CASH_TYPE_CASH_DRAWBACK)) return "投注退还";
		
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_BJ3)) return "三分彩";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_BJPK10)) return "北京赛车PK10";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_XJPLU28)) return "幸运28";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_CQSSC)) return "重庆时时彩";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_BJLU28)) return "PC蛋蛋";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_GDK10)) return "广东快乐10分";		
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_TJSSC)) return "天津时时彩";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_XJSSC)) return "新疆时时彩";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_POKER)) return "快乐扑克3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_GDPICK11)) return "广东11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_JSK3)) return "江苏快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_MARKSIX)) return "香港六合彩";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_SFPK10)) return "极速赛车";
		else return "其他";
	}
	/**
	 * 返回用户类型中文
	 */
	public final static String getUserTypeZh(String cashType) {
		if(cashType.equals(Constants.USER_TYPE_SUER)) return "普通用户";
		else if(cashType.equals(Constants.USER_TYPE_ADMIN)) return "管理员";
		else if(cashType.equals(Constants.USER_TYPE_AGENT_ONE )) return "初级代理";
		else if(cashType.equals(Constants.USER_TYPE_AGENT_TWO )) return "中级代理";
		else if(cashType.equals(Constants.USER_TYPE_AGENT_THREE )) return "高级代理";
		else return "其他";
	}
	/**
	 * 返回员工类型中文
	 */
	public final static String getEmployeeTypeZh(String userType) {
		if(userType.equals(Constants.EMPLOYEE_TYPE_ORDINARY)) return "普通员工";
		else if(userType.equals(Constants.EMPLOYEE_TYPE_TASK)) return "派单员工";
		else if(userType.equals(Constants.EMPLOYEE_TYPE_EXPRESS)) return "外部配送人员";
		else if(userType.equals(Constants.EMPLOYEE_TYPE_ADMIN)) return "管理人员";
		else return "其他";
	}
	
	public static String POINTS_CHARGE_THIRD_PAY_SWITCH = "1";////三方充值积分开关 在线支付充值积分，默认为打开
	public static String POINTS_USER_THIRD_INTRODUCTION_SWITCH = "1";
	public static String POINTS_USER_THIRD_INTRODUCTION_CONTENT="";
	//----------------
	//短信用户名、密码
	public static String SMS_UID = "";
	//短信配置常量 f3
	public static String SMS_SEND_URL = "";
	public static String SMS_ECECCID = "";
	public static String SMS_PASSWORD = "";
	public static String SMS_MSG_TYPE = "5";
	public static String SMS_LONG_CODE = "";
	public static String SMS_CALLBACK_URL="";
	
	//短信配置常量 ipyy
	public static String SMS_SEND_URL_IPYY = "";
	public static String SMS_ECECCID_IPYY = "";
	public static String SMS_PASSWORD_IPYY = "";
	public static String SMS_CALLBACK_URL_IPYY="";
	
	//短信内容定义
	public static String SMS_MSG_SIGNATURE="";
	public static String SMS_MSG_REG="";
	public static String SMS_MSG_REREG="";
	public static String SMS_MSG_COUPON="";
	public static String SMS_MSG_COUPON_EXCHANGE="";
	
	//苹果部署模式[1=开发环境_development][2=生产环境_production]
	public static String IOS_DEPLOY_MODE="1";//默认开发环境
	public static String IOS_DEPLOY_MODE_DEVELOPMENT="1";//开发环境
	public static String IOS_DEPLOY_MODE_PRODUCTION="2";//生产环境
	
	//苹果发布模式[1=企业版][2=商店版]
	public static String IOS_PUBLISH_MODE="1";//默认企业版
	public static String IOS_PUBLISH_MODE_INHOUSE="1";//企业版
	public static String IOS_PUBLISH_MODE_APPSTORE="2";//商店版
	
	//推送配置---------------------------------------------
	//安卓用户版
	public static String BAIDU_PUSH_APIKEY_ANDROID="";
	public static String BAIDU_PUSH_SECRETKEY_ANDROID="";
	//安卓配送版
	public static String BAIDU_PUSH_APIKEY_ANDROID_SEND="";
	public static String BAIDU_PUSH_SECRETKEY_ANDROID_SEND="";
	//苹果用户商店版
	public static String BAIDU_PUSH_APIKEY_IOS_APPSTORE="";
	public static String BAIDU_PUSH_SECRETKEY_IOS_APPSTORE="";
	//苹果用户企业版
	public static String BAIDU_PUSH_APIKEY_IOS_INHOUSE="";
	public static String BAIDU_PUSH_SECRETKEY_IOS_INHOUSE="";
	//苹果配送商店版
	public static String BAIDU_PUSH_APIKEY_IOS_SEND_APPSTORE="";
	public static String BAIDU_PUSH_SECRETKEY_IOS_SEND_APPSTORE="";
	//苹果配送企业版
	public static String BAIDU_PUSH_APIKEY_IOS_SEND_INHOUSE="";
	public static String BAIDU_PUSH_SECRETKEY_IOS_SEND_INHOUSE="";
	//---------------------------

	
	/**极光推送 app key*************************/
	public  static String JPUSH_APP_KEY="";
	/**极光推送 Master Secret *************************/
	public  static String JPUSH_MASTER_SECRET="";
	
	// ---------------------------------------------------------------
	// ####################支付宝支付###########################
	/** 支付宝合作者id **/
	public static String alipayPartner = "";
	/** 私钥 **/
	public static String alipayRsaPrivate = "";
	/** 收款帐号 **/
	public static String alipaySellerId = "";
	/** 异步回调地址-订餐 **/
	public static String alipayCallBackEat = "";
	/** 异步回调地址-电商 **/
	public static String alipayCallBackStore = "";
	/** 异步回调地址-团购 **/
	public static String alipayCallBackBuy = "";
	/** 异步回调地址-在线充值 **/
	public static String alipayCallBackRecharge = "";
	/** 异步回调地址-礼品卡 **/
	public static String alipayCallBackGiftCard = "";
	/** 异步回调地址-物业费 **/
	public static String alipayCallBackEstateFee = "";
	/** 异步回调地址-供求 **/
	public static String alipayCallBackInfoPublish = "";
	/** 异步回调地址-双色球 **/
	public static String alipayCallBackGameSsq = "";
	/** 异步回调地址-快三 **/
	public static String alipayCallBackGameK3 = "";

	/** 支付信息 */
	public final static String PAY_ALIPAY_CHARSET = "utf-8";
	public final static String PAY_ALIPAY_SIGN_TYPE = "RSA";
	public final static String PAY_ALIPAY_PAYMENT_TYPE = "1";

	// ####################微信支付###########################
	/** appid **/
	public static String weChatAppid = "";
	/** 商户号 **/
	public static String weChatMchid = "";
	/** key **/
	public static String weChatKey = "";
	/** 异步回调地址-订餐 **/
	public static String weChatCallBackEat = "";
	/** 异步回调地址-电商 **/
	public static String weChatCallBackStore = "";
	/** 异步回调地址-团购 **/
	public static String weChatCallBackBuy = "";
	/** 异步回调地址-在线充值 **/
	public static String weChatCallBackRecharge = "";
	/** 异步回调地址-礼品卡 **/
	public static String weChatCallBackGiftCard = "";
	/** 异步回调地址-物业费 **/
	public static String weChatCallBackEstateFee = "";
	/** 异步回调地址-供求信息 **/
	public static String weChatCallBackInfoPublish = "";
	/** 异步回调地址-双色球 **/
	public static String weChatCallBackGameSsq = "";
	/** 异步回调地址-快三 **/
	public static String weChatCallBackGameK3 = "";
	
	// ####################银联支付###########################
	/** 合作者id **/
	public static String unionPartnerId = "";
	/** 异步回调地址-订餐 **/
	public static String unionCallBackEat = "";
	/** 异步回调地址-电商 **/
	public static String unionCallBackStore = "";
	/** 异步回调地址-电商 **/
	public static String unionCallBackBuy = "";
	/** 异步回调地址-在线充值 **/
	public static String unionCallBackRecharge = "";
	/** 异步回调地址-礼品卡 **/
	public static String unionCallBackGiftCard = "";
	/** 异步回调地址-物业费 **/
	public static String unionCallBackEstateFee = "";
	/** 异步回调地址-供求信息 **/
	public static String unionCallBackInfoPublish = "";
	
	/** 每天获取优惠次数 **/
	public static String PARAM_COUPONS_COUNT = "0";

	
	public static String gameBj3OpenResultUrl="";// 北京三分彩
	public static String gameBjPk10OpenResultUrl ="";// 北京赛车
	public static String gameXjpLu28OpenResultUrl ="";// 幸运28
	public static String gameCqSscOpenResultUrl ="";// 重庆时时彩
	public static String gameBjLu28OpenResultUrl ="";// PC蛋蛋
	public static String gameGdK10OpenResultUrl ="";// 广东快乐10分
	public static String gameGxK10OpenResultUrl ="";// 广西快乐10分
	public static String gameTjSscOpenResultUrl ="";// 天津时时彩
	public static String gameXjSscOpenResultUrl ="";// 新疆时时彩
	public static String gamePokerOpenResultUrl ="";// 快乐扑克3
	public static String gameGdPick11OpenResultUrl ="";// 广东11选5
	public static String gameJsK3OpenResultUrl ="";// 江苏快3
	public static String gameMarkSixOpenResultUrl ="";// 香港六合彩
	public static String gameSfPk10OpenResultUrl ="";// 极速赛车
	// ----------------------------------------------------------------------------
	/**游戏开奖初始化 是否会定时执行 1=会执行  0=不会执行**/
	public static String gameInitAndOpenSwitch="";
	
	public static String getSmsUid(){
		return SMS_UID;
	}
	public static String getSmsPassword(){
		return SMS_PASSWORD;
	}
	
	public static String getAlipayPartner() {
		return alipayPartner;
	}

	public static String getAlipayCallBackGiftCard() {
		return alipayCallBackGiftCard;
	}

	public static String getWeChatCallBackGiftCard() {
		return weChatCallBackGiftCard;
	}

	public static String getUnionCallBackGiftCard() {
		return unionCallBackGiftCard;
	}

	public static String getAlipayRsaPrivate() {
		return alipayRsaPrivate;
	}

	public static String getAlipaySellerId() {
		return alipaySellerId;
	}

	public static String getAlipayCallBackEat() {
		return alipayCallBackEat;
	}

	public static String getAlipayCallBackStore() {
		return alipayCallBackStore;
	}

	public static String getAlipayCallBackBuy() {
		return alipayCallBackBuy;
	}

	public static String getWeChatAppid() {
		return weChatAppid;
	}

	public static String getWeChatMchid() {
		return weChatMchid;
	}

	public static String getWeChatKey() {
		return weChatKey;
	}

	public static String getWeChatCallBackEat() {
		return weChatCallBackEat;
	}

	public static String getWeChatCallBackStore() {
		return weChatCallBackStore;
	}

	public static String getWeChatCallBackBuy() {
		return weChatCallBackBuy;
	}

	public static String getUnionPartnerId() {
		return unionPartnerId;
	}

	public static String getUnionCallBackEat() {
		return unionCallBackEat;
	}

	public static String getUnionCallBackStore() {
		return unionCallBackStore;
	}

	public static String getUnionCallBackBuy() {
		return unionCallBackBuy;
	}

	public final static String getWebappContext() {
		return webappContext;
	}

	public final static String getWebRootPath() {
		return webRootPath;
	}

	public final static String getFileServerFolder() {
		return fileServerFolder;
	}

	public final static String getFileUploadPath() {
		return fileUploadPath;
	}

	public static String getAlipayCallBackRecharge() {
		return alipayCallBackRecharge;
	}

	public static void setAlipayCallBackRecharge(String alipayCallBackRecharge) {
		Constants.alipayCallBackRecharge = alipayCallBackRecharge;
	}

	public static String getWeChatCallBackRecharge() {
		return weChatCallBackRecharge;
	}

	public static void setWeChatCallBackRecharge(String weChatCallBackRecharge) {
		Constants.weChatCallBackRecharge = weChatCallBackRecharge;
	}

	public static String getUnionCallBackRecharge() {
		return unionCallBackRecharge;
	}

	public static void setUnionCallBackRecharge(String unionCallBackRecharge) {
		Constants.unionCallBackRecharge = unionCallBackRecharge;
	}

	public static String getAlipayCallBackEstateFee() {
		return alipayCallBackEstateFee;
	}

	public static String getWeChatCallBackEstateFee() {
		return weChatCallBackEstateFee;
	}

	public static String getUnionCallBackEstateFee() {
		return unionCallBackEstateFee;
	}

	public static String getAlipayCallBackInfoPublish() {
		return alipayCallBackInfoPublish;
	}

	public static String getWeChatCallBackInfoPublish() {
		return weChatCallBackInfoPublish;
	}

	public static String getUnionCallBackInfoPublish() {
		return unionCallBackInfoPublish;
	}

	public static String getAlipayCallBackGameSsq() {
		return alipayCallBackGameSsq;
	}

	public static String getAlipayCallBackGameK3() {
		return alipayCallBackGameK3;
	}

	public static String getWeChatCallBackGameSsq() {
		return weChatCallBackGameSsq;
	}

	public static String getWeChatCallBackGameK3() {
		return weChatCallBackGameK3;
	}

	
	
	public static String getGameBj3OpenResultUrl() {
		return gameBj3OpenResultUrl;
	}

	public static String getGameBjPk10OpenResultUrl() {
		return gameBjPk10OpenResultUrl;
	}
	public static String getGameSfPk10OpenResultUrl() {
		return gameSfPk10OpenResultUrl;
	}
	

	public static String getGameXjpLu28OpenResultUrl() {
		return gameXjpLu28OpenResultUrl;
	}

	public static String getGameCqSscOpenResultUrl() {
		return gameCqSscOpenResultUrl;
	}

	public static String getGameBjLu28OpenResultUrl() {
		return gameBjLu28OpenResultUrl;
	}

	public static String getGameGdK10OpenResultUrl() {
		return gameGdK10OpenResultUrl;
	}

	public static String getGameGxK10OpenResultUrl() {
		return gameGxK10OpenResultUrl;
	}
	
	public static String getGameTjSscOpenResultUrl() {
		return gameTjSscOpenResultUrl;
	}

	public static String getGameXjSscOpenResultUrl() {
		return gameXjSscOpenResultUrl;
	}
	public static String getGamePokerOpenResultUrl() {
		return gamePokerOpenResultUrl;
	}
	
	public static String getGameGdPick11OpenResultUrl() {
		return gameGdPick11OpenResultUrl;
	}
	public static String getGameJsK3OpenResultUrl() {
		return gameJsK3OpenResultUrl;
	}
	public static String getGameMarkSixOpenResultUrl() {
		return gameMarkSixOpenResultUrl;
	}

	public static String getGameInitAndOpenSwitch() {
		return gameInitAndOpenSwitch;
	}
	
	
	/**
	 * 网站服务器地址
	 */
	public static String getDomainName() {
		return domainName;
	}

	/**
	 * 网站的文件服务器地址，如果没有则和服务器地址相同
	 * 
	 * @return
	 */
	public static String getDomainNameFile() {
		return domainNameFile;
	}

	static {
		reload();
		loadTimerConfig();//定时器配置
	}

	public static void reload() {
		IServiceLocator service = ServiceLocatorImpl.getInstance();
		IReadProperties read = (IReadProperties) service
				.getService("readProperties");
		webappContext = read.getValue("SYSTEM.WEBSERVER.WEBCONTEXT");
		webRootPath = read.getValue("SYSTEM.WEBROOT.PATH");
		fileServerFolder = read.getValue("FILEUPLOAD.FOLDER");
		fileUploadPath = read.getValue("FILEUPLOAD.PATH");
		serverUrl = read.getValue("SYSTEM.SERVER.URL");
		domainName = read.getValue("DOMAIN_NAME");
		domainNameFile = read.getValue("DOMAIN_NAME_FILE");
		alipayPartner = read.getValue("ALIPAY.PARTNER");
		alipayRsaPrivate = read.getValue("ALIPAY.RSA.PRIVATE");
		alipaySellerId = read.getValue("ALIPAY.SELLER.ID");
		alipayCallBackEat = read.getValue("ALIPAY.CALLBACK.EAT");
		alipayCallBackStore = read.getValue("ALIPAY.CALLBACK.STORE");
		alipayCallBackBuy = read.getValue("ALIPAY.CALLBACK.BUY");
		alipayCallBackRecharge = read.getValue("ALIPAY.CALLBACK.RECHARGE");
		alipayCallBackGiftCard = read.getValue("ALIPAY.CALLBACK.GIFTCARD");
		alipayCallBackEstateFee = read.getValue("ALIPAY.CALLBACK.ESTATEFEE");
		alipayCallBackInfoPublish = read.getValue("ALIPAY.CALLBACK.INFOPUBLISH");
		alipayCallBackGameSsq = read.getValue("ALIPAY.CALLBACK.GAMESSQ");
		alipayCallBackGameK3 = read.getValue("ALIPAY.CALLBACK.GAMEK3");
		
		
		weChatAppid = read.getValue("WECHAT.APPID");
		weChatMchid = read.getValue("WECHAT.MCH.ID");
		weChatKey = read.getValue("WECHAT.KEY");
		weChatCallBackEat = read.getValue("WECHAT.CALLBACK.EAT");
		weChatCallBackStore = read.getValue("WECHAT.CALLBACK.STORE");
		weChatCallBackBuy = read.getValue("WECHAT.CALLBACK.BUY");
		weChatCallBackRecharge = read.getValue("WECHAT.CALLBACK.RECHARGE");
		weChatCallBackGiftCard = read.getValue("WECHAT.CALLBACK.GIFTCARD");
		weChatCallBackEstateFee = read.getValue("WECHAT.CALLBACK.ESTATEFEE");
		weChatCallBackInfoPublish = read.getValue("WECHAT.CALLBACK.INFOPUBLISH");
		weChatCallBackGameSsq = read.getValue("WECHAT.CALLBACK.GAMESSQ");
		weChatCallBackGameK3 = read.getValue("WECHAT.CALLBACK.GAMEK3");
		
		unionPartnerId = read.getValue("UNION.PARTNER.ID");
		unionCallBackEat = read.getValue("UNION.CALLBACK.EAT");
		unionCallBackStore = read.getValue("UNION.CALLBACK.STORE");
		unionCallBackBuy = read.getValue("UNION.CALLBACK.BUY");
		unionCallBackRecharge = read.getValue("UNION.CALLBACK.RECHARGE");
		unionCallBackGiftCard = read.getValue("UNION.CALLBACK.GIFTCARD");
		unionCallBackEstateFee = read.getValue("UNION.CALLBACK.ESTATEFEE");
		unionCallBackInfoPublish = read.getValue("UNION.CALLBACK.INFOPUBLISH");
		
		POINTS_CHARGE_THIRD_PAY_SWITCH = read.getValue("POINTS.CHARGE.THIRD.PAY.SWITCH");
		POINTS_USER_THIRD_INTRODUCTION_SWITCH= read.getValue("POINTS.USER.THIRD.INTRODUCTION.SWITCH");
		POINTS_USER_THIRD_INTRODUCTION_CONTENT= read.getValue("POINTS.USER.THIRD.INTRODUCTION.CONTENT");
		
		IOS_REVIEW_VISION = read
				.getValue("ISO.REVIEW.VISION");
		
		gameBj3OpenResultUrl = read.getValue("GAME.BJ3.OPENRESULT.URL");
		gameBjPk10OpenResultUrl= read.getValue("GAME.BJPK10.OPENRESULT.URL");
		gameSfPk10OpenResultUrl= read.getValue("GAME.SFPK10.OPENRESULT.URL");
		gameXjpLu28OpenResultUrl= read.getValue("GAME.XJPLU28.OPENRESULT.URL");
		gameCqSscOpenResultUrl = read.getValue("GAME.CQSSC.OPENRESULT.URL");
		gameBjLu28OpenResultUrl= read.getValue("GAME.BJLU28.OPENRESULT.URL");
		gameGdK10OpenResultUrl= read.getValue("GAME.GDK10.OPENRESULT.URL");
		gameGxK10OpenResultUrl= read.getValue("GAME.GXK10.OPENRESULT.URL");
		
		gameXjSscOpenResultUrl = read.getValue("GAME.XJSSC.OPENRESULT.URL");
		gameTjSscOpenResultUrl = read.getValue("GAME.TJSSC.OPENRESULT.URL");
		gamePokerOpenResultUrl = read.getValue("GAME.POKER.OPENRESULT.URL");
		gameGdPick11OpenResultUrl = read.getValue("GAME.GDPICK11.OPENRESULT.URL");
		gameJsK3OpenResultUrl=read.getValue("GAME.JSK3.OPENRESULT.URL");
		gameMarkSixOpenResultUrl=read.getValue("GAME.MARKSIX.OPENRESULT.URL");
		
		gameInitAndOpenSwitch= read.getValue("GAME.INIT.OPEN.SWITCH");
		
		//加载系统功能配置信息
//		String funcs = ManageFile.loadTextFileConfig(webRootPath+"/WEB-INF/classes/func.properties");
//		if(funcs!=null && funcs.length()>5){
//			System.out.println("funcs:"+funcs);
//			SysFuncConfig = new HashMap<String, String>();
//			String[] confarr = funcs.split(";");
//			for(int a=0;a<confarr.length;a++){
//				String domainstr = confarr[a];
//				if(domainstr!=null && domainstr.indexOf("=")>0){
//					String[] dmainarr = domainstr.split("=");
//					SysFuncConfig.put(dmainarr[0], dmainarr[1]);
//				}
//			}
//		}
		
		SMS_SEND_URL=read.getValue("SMS.SEND.URL");
		SMS_CALLBACK_URL=read.getValue("SMS.CALLBACK.URL");
		SMS_ECECCID=read.getValue("SMS.ECECCID");
		SMS_UID = read.getValue("SMS.UID");
		SMS_PASSWORD=read.getValue("SMS.PASSWORD");
		SMS_LONG_CODE=read.getValue("SMS.LONG.CODE");
		SMS_MSG_TYPE=read.getValue("SMS.MSG.TYPE");
		
		SMS_SEND_URL_IPYY=read.getValue("SMS.SEND.URL.IPYY");
		SMS_ECECCID_IPYY=read.getValue("SMS.ECECCID.IPYY");
		SMS_PASSWORD_IPYY=read.getValue("SMS.PASSWORD.IPYY");
		SMS_CALLBACK_URL_IPYY=read.getValue("SMS.CALLBACK.URL.IPYY");
		
		SMS_MSG_SIGNATURE=read.getValue("SMS.MSG.SIGNATURE");
		SMS_MSG_REG=read.getValue("SMS.MSG.REG");
		SMS_MSG_REREG=read.getValue("SMS.MSG.REREG");
		SMS_MSG_COUPON=read.getValue("SMS.MSG.COUPON");
		SMS_MSG_COUPON_EXCHANGE=read.getValue("SMS.MSG.COUPON.EXCHANGE");
		
//		IOS_DEPLOY_MODE=read.getValue("IOS.DEPLOY.MODE");
//		IOS_PUBLISH_MODE=read.getValue("IOS.PUBLISH.MODE");
		
//		//安卓用户版
//		BAIDU_PUSH_APIKEY_ANDROID=read.getValue("BAIDU.PUSH.APIKEY.ANDROID");
//		BAIDU_PUSH_SECRETKEY_ANDROID=read.getValue("BAIDU.PUSH.SECRETKEY.ANDROID");
//		//安卓配送版
//		BAIDU_PUSH_APIKEY_ANDROID_SEND=read.getValue("BAIDU.PUSH.APIKEY.ANDROID.SEND");
//		BAIDU_PUSH_SECRETKEY_ANDROID_SEND=read.getValue("BAIDU.PUSH.SECRETKEY.ANDROID.SEND");
//		//苹果用户商店版
//		BAIDU_PUSH_APIKEY_IOS_APPSTORE=read.getValue("BAIDU.PUSH.APIKEY.IOS.APPSTORE");
//		BAIDU_PUSH_SECRETKEY_IOS_APPSTORE=read.getValue("BAIDU.PUSH.SECRETKEY.IOS.APPSTORE");
//		//苹果用户企业版
//		BAIDU_PUSH_APIKEY_IOS_INHOUSE=read.getValue("BAIDU.PUSH.APIKEY.IOS.INHOUSE");
//		BAIDU_PUSH_SECRETKEY_IOS_INHOUSE=read.getValue("BAIDU.PUSH.SECRETKEY.IOS.INHOUSE");
//		//苹果配送商店版
//		BAIDU_PUSH_APIKEY_IOS_SEND_APPSTORE=read.getValue("BAIDU.PUSH.APIKEY.IOS.SEND.APPSTORE");
//		BAIDU_PUSH_SECRETKEY_IOS_SEND_APPSTORE=read.getValue("BAIDU.PUSH.SECRETKEY.IOS.SEND.APPSTORE");
//		//苹果配送企业版
//		BAIDU_PUSH_APIKEY_IOS_SEND_INHOUSE=read.getValue("BAIDU.PUSH.APIKEY.IOS.SEND.INHOUSE");
//		BAIDU_PUSH_SECRETKEY_IOS_SEND_INHOUSE=read.getValue("BAIDU.PUSH.SECRETKEY.IOS.SEND.INHOUSE");
//		
//		JPUSH_APP_KEY = read.getValue("JPUSH.APP.KEY");
//		JPUSH_MASTER_SECRET = read.getValue("JPUSH.MASTER.SECRET");
		
		msgTime = read.getValue("MSG.TIME");
		msgMaxCount = read.getValue("MSG.MAX.COUNT");
		
//		String[] SITE_CONFIG = read.getValue("SITE_CONFIG").split(",");
//		SITE_TITLE = SITE_CONFIG[0];
//		SITE_NAME = SITE_CONFIG[1];
//		SITE_DOMAIN = SITE_CONFIG[2];
	}


	public static String getJPUSH_APP_KEY() {
		return JPUSH_APP_KEY;
	}

	public static String getJPUSH_MASTER_SECRET() {
		return JPUSH_MASTER_SECRET;
	}

	
	public static String getGameTimerOpen() {
		return gameTimerOpen;
	}

	public static void setGameTimerOpen(String gameTimerOpen) {
		Constants.gameTimerOpen = gameTimerOpen;
	}

	public static String getServerUrl() {
		return Constants.serverUrl;
	}
	
	public static String getMsgMaxCount() {
		return msgMaxCount;
	}

	public static String getMsgTime() {
		return msgTime;
	}

	public static String getIOS_REVIEW_VISION() {
		return IOS_REVIEW_VISION;
	}
	
	/**
	 * 返回系统定义的功能是否打开或关闭
	 * @param key = system.func.[模块或功能英文名称]
	 * @return Boolean
	 */
	public final static Boolean chkFuncConfig(String key){
		boolean af = false;
		if(SysFuncConfig==null){
			af = true;
		}else{
			af = SysFuncConfig.get(key)!=null && SysFuncConfig.get(key).equals("0")?false:true;
		}
		return af;
	}

	/**
	 * 返回用户默认头像
	 * 
	 * @param gender
	 * @param sizeType
	 * @return
	 */
	public final static String getUserDefaultPicture(String gender,
			String sizeType, String url) {
		if (url != null && url.length() > 1)
			return url;
		if (gender == null || gender.length() <= 0)
			gender = "1";
		String size = "50";
		if (sizeType.equals("picture"))
			size = "50";
		else if (sizeType.equals("logo"))
			size = "120";
		else if (sizeType.equals("logoMini"))
			size = "20";
		else
			size = "50";
		return "/images/ui/p" + size + "_" + gender + ".gif";
	}
	public static Map<String,String> blacklist=new HashMap<String, String>();
	public static Map<String,String> getBlacklist(){		
		return blacklist;
	};
	public static Map<String,SMSCache> datelist=new HashMap<String, SMSCache>();
	public static Map<String,SMSCache> getIPlist(){
		return datelist;
	}
	
	/** 
	* 用户和Session绑定关系 
	*/  
	public static Map<String, HttpSession> USER_SESSION=new HashMap<String, HttpSession>(); 
	public static Map<String,HttpSession> getUserSession(){		
		return USER_SESSION;
	};


	/** 
	* seeionId和用户的绑定关系 
	*/  
	public static   Map<String, String> SESSIONID_USER=new HashMap<String, String>();  
	public static  Map<String,String> getSessionIdUser(){
		return SESSIONID_USER;
	};

}
