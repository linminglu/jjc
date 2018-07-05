//package com.apps;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpSession;
//
//import com.apps.model.SMSCache;
//import com.framework.common.properties.IReadProperties;
//import com.framework.service.IServiceLocator;
//import com.framework.service.impl.ServiceLocatorImpl;
//import com.framework.util.ManageFile;
//
///**
// * 平台常量类
// * 
// * @author Mr.zang
// * 
// */
//public final class Constants11 {
//	
//	/**系统平台运行模式**/
//	public static String PLAT_RUN_MODE_ID = "C";//运行模式标识,默认为C
//	public final static String PLAT_RUN_MODE_CITY = "C";//城市O2O
//	public final static String PLAT_RUN_MODE_COMMUNITY = "CC";//小区O2O
//	public final static String PLAT_RUN_MODE_UNIVERSITY = "UN";//学校O2O
//	
//	/**
//	 * 系统平台功能定义默认或未配置都为打开，如配置：0关闭1打开
//	 * 可针对模块、功能、页面功能等
//	 * 配置文件：func.properties
//	 * eg:
//	 * system.func.city.add - 添加城市
//	 * system.func.info.buy - 供求模块交易功能
//	 */
//	public static HashMap<String,String> SysFuncConfig;
//	
//	public static String DEF_PAY_COMPANY = "6";//1.仁信支付2.闪付3.艾米森 4.大面包5.轻易付 6.零点支付
//	
//	public final static String PARAM_DEF_PAY = "1";//1设置支付收款公司方式
//
//	/**每日首次充值赠送比例**/
//	public final static String PARAM_DEF_PRESENT = "4";
//	/**限制提现比例**/
//	public final static String PARAM_DEF_WITHDRAW = "5";
//	/**每日首次充值赠送比例**/
//	public final static String PARAM_DEF_PRESENT6 = "6";
//	/**每日首次充值赠送比例**/
//	public final static String PARAM_DEF_PRESENT7 = "7";
//	/**每日首次充值赠送比例**/
//	public final static String PARAM_DEF_PRESENT8 = "8";
//	/**支付宝收款二维码**/
//	public final static String PARAM_DEF_ZHIFUBAO = "9";
//	/**微信收款二维码**/
//	public final static String PARAM_DEF_WEIXIN = "10";
//	/**转账汇款提示内容**/
//	public final static String PARAM_DEF_HINT = "11";
//	
//	/** 各级页面title **/
//	public final static String SLOGAN = "官网";
//	/** 全局状态-有效 **/
//	public final static String PUB_STATUS_OPEN = "1";
//	/** 全局状态-无效 **/
//	public final static String PUB_STATUS_CLOSE = "0";
//
//	/** 用户类型-管理员 **/
//	public final static String USER_TYPE_ADMIN = "2";
//	/** 用户类型-超级管理员 **/
//	public final static String USER_TYPE_SUPERADMIN = "4";
//
//	/** 用户类型-测试用户**/
//	public final static String USER_TYPE_TEST = "11";
//	
//	/** 用户类型-代理用户**/
//	public final static String USER_TYPE_AGENT = "12";
//	/** 用户类型-普通用户 **/
//	public final static String USER_TYPE_SUER = "1";
//	/** 用户类型-管理员_商家管理账号 **/
//	public final static String USER_TYPE_ADMIN_SHANGJIA = "3";
//	
//	/** 用户类型-商家-订餐 **/
//	public final static String USER_TYPE_SELLER_EAT = "5";
//	/** 用户类型-商家-电商 **/
//	public final static String USER_TYPE_SELLER_STORE = "6";
//	/** 用户类型-商家-团购 **/
//	public final static String USER_TYPE_SELLER_BUY = "7";
//	/** 用户类型-管理员-商圈 **/
//	public final static String USER_TYPE_USER_BAID= "8";
//	/** 用户类型-管理员-小区 **/
//	public final static String USER_TYPE_USER_CCID= "9";
//	/** 用户类型-管理员-物业 **/
//	public final static String USER_TYPE_USER_ESTATE= "10";
//	
//	/** 用户类型-商家-商品管理员 **/
//	public final static String SELLER_USER_RL_TYPE_ADMIN = "4";
//	/** 用户类型-商家-商品管理员 **/
//	public final static String SELLER_USER_RL_TYPE_PUTONG= "1";
//	/**送货类型 自送**/
//	public final static String ORDER_SEND_OWNER= "0";
//	/**送货类型 三方物流**/
//	public final static String ORDER_SEND_EXPRESS= "1";
//	
//	
//	/**订单结算 未结算**/
//	public final static String ORDER_CHECKOUT_NOT= "0";
//	/**订单结算 已结算**/
//	public final static String ORDER_CHECKOUT_SUCCESS= "1";
//	
//	/**订单结算 未入账**/
//	public final static String ORDER_CHECKOUT_PAY_NOT= "0";
//	/**订单结算 已入账**/
//	public final static String ORDER_CHECKOUT_PAY_SUCCESS= "1";
//	
//	
//	public final static Integer USER_REG_SEND_POINT = 200;
//	/**积分与钱的换算倍数 1元=100积分**/
//	public final static Integer USER_MONEY_AND_POINT = 100;
//	
//	/** 系统默认头像 **/
//	public final static String DEFAULT_LOGO = "/images/noimg.png";
//	/** 系统默认mini头像 **/
//	public final static String DEFAULT_LOGO_MINI = "/images/noimg.png";
//
//	/** web根 **/
//	private static String webRootPath = "";
//	private static String webappContext = "";
//	private static String fileServerFolder = "";
//	/** 文件上传到的路径 **/
//	private static String fileUploadPath = "";
//
//	private static String serverUrl = "";
//
//	/** 域名 **/
//	public static String domainName = "";
//	/** 域名-文件，如果是在一个服务器，则相同 **/
//	public static String domainNameFile = "";
//
//	/** 分别为商品的图片 **/
//	public final static int IMG_WIDTH = 720;
//	public final static int IMG_HEIGHT = 450;
//	public final static int IMG_MINI_WIDTH = 240;
//	public final static int IMG_MINI_HEIGHT = 150;
//
//	/** 分别为商家的图片 **/
//	public final static int IMG_WIDTH_SELLER = 640;
//	public final static int IMG_HEIGHT_SELLER = 200;
//
//	/** apps设备类型-IOS **/
//	public final static String APPS_TYPE_IOS = "1";
//	/** apps设备类型-Android **/
//	public final static String APPS_TYPE_ANDROID = "2";
//	
//	/** 广告 **/
//	public final static String ADVERTISING_LUNBO = "1";// 轮播
//	public final static String ADVERTISING_STAT = "2";// 静态
//	public final static String ADVERTISING_TYPE = "3";// 类型下的广告
//	
//	/**首页轮播广告最大数量限制**/
//	public final static Integer ADVERTISING_LUNBO_HOME_MAX = 5;
//	
//	/** 市区类型-省 */
//	public final static String CITY_SHENG = "3";//省
//	/** 市区类型-市 */
//	public final static String CITY_SHI = "1";
//	/** 市区类型-区 */
//	public final static String CITY_QU = "2";
//
//	public final static String DES_KEY_OID = "oid";
//	public final static String DES_KEY_UID = "uid";
//	public final static String DES_KEY_LOGINNAME = "loginName";
//	public final static String DES_KEY_PASSWORD = "password";
//
//	/** 电商模块 */
//	public final static String MODULE_STORE = "1";
//	/** 订餐模块 */
//	public final static String MODULE_EAT = "2";
//	/** 团购模块 */
//	public final static String MODULE_GROUP = "3";
//	/** 充值模块 */
//	public final static String MODULE_RECHARGE = "4";
//	/** 礼品卡模块 */
//	public final static String MODULE_GIFT_CARD = "5";
//	/**物业费模块**/
//	public final static String MODULE_ESTATE_FEE = "6";
//	
//	/**供求信息模块**/
//	public final static String MODULE_PUBLISH_INFO= "7";
//	
//	/**双色球**/
//	public final static String MODULE_SSQ= "8";
//	/**快3**/
//	public final static String MODULE_K3= "9";
//	
//	
//	
//	/**员工类型-普通员工**/
//	public final static String EMPLOYEE_TYPE_ORDINARY = "1";
//	/**员工类型-派单员工**/
//	public final static String EMPLOYEE_TYPE_TASK = "2";
//	/**员工类型-外部配送人员(后续实现)**/
//	public final static String EMPLOYEE_TYPE_EXPRESS = "3";
//	/**员工类型-管理人员**/
//	public final static String EMPLOYEE_TYPE_ADMIN = "4";
//	
//	/**用户资金收支-收入**/
//	public final static String TRADE_TYPE_INCOME = "1";
//	/**用户资金收支-支出**/
//	public final static String TRADE_TYPE_PAY = "2";
//	
//	/**订单结算-未结算**/
//	public final static String ORDER_STATUS_CHECKOUT_NO = "0";
//	/**订单结算-已结算**/
//	public final static String ORDER_STATUS_CHECKOUT_OVER  = "1";
//	
//	/**用户提现审核-审核中**/
//	public final static String CASH_AUDIT_TYPE_ING= "0";
//	/**用户提现审核-通过**/
//	public final static String CASH_AUDIT_TYPE_PASS= "1";
//	/**用户提现审核-拒绝**/
//	public final static String CASH_AUDIT_TYPE_REFUSE= "2";
//	
//	/**注册用户--默认密码**/
//	public final static String USER_PASSWORD_DEFAULT= "888888";
//	
//	//----------------------------------------------------------------
//	
//	//提现类型
//	public final static String CASH_MONEY_MIN="21";// 提现最低限额
//	public final static String CASH_MONEY_MAX="22";// 提现最高限额
//	
//	//供求信息手续费设置类型
//	public final static String PUBLISH_MONEY_SERVICE_MIN="30";// 供求信息手续费起征额
//	public final static String PUBLISH_MONEY_SERVICE_MAX="32";// 供求信息手续费最大值
//	public final static String PUBLISH_MONEY_SERVICE_RATIO="31";// 供求信息手续费比例
//	
//	//资金交易类型
//	/**用户资金来源-在线充值**/
//	public final static String CASH_TYPE_ONLINE = "1";
//	/**用户资金来源-卡充**/
//	public final static String CASH_TYPE_CARD = "2";
//	/**用户资金来源-线下充值**/
//	public final static String CASH_TYPE_OFFLINE = "3";
//	/**用户资金来源-分销佣金**/
//	public final static String CASH_TYPE_FENXIAO= "4";
//	public final static String CASH_TYPE_INFO_PUBLISH= "7";//供求交易
//	public final static String CASH_TYPE_CASH_OUT ="5";// 提现
//	public final static String CASH_TYPE_CASH_RETURN ="6";// 提现审核返回
//	public final static String CASH_TYPE_CASH_CHECKOUT ="8";// 结算
//	public final static String CASH_TYPE_CASH_REFUND ="9";// 退款
//	public final static String CASH_TYPE_CASH_BUY_LOTO ="10";// 购买彩票
//	public final static String CASH_TYPE_CASH_GUARANTEE ="11";// 方案保底 
//	public final static String CASH_TYPE_CASH_FROZEN ="12";// 保底 冻结||扣款
//	public final static String CASH_TYPE_CASH_DRAW ="13";// 方案撤单
//	public final static String CASH_TYPE_CASH_SYSTEM ="14";// 系统充值
//	public final static String CASH_TYPE_CASH_SYS_CHARGE ="15";// 系统扣款 
//	public final static String CASH_TYPE_CASH_PRESENT ="16";// 注册赠送
//	public final static String CASH_TYPE_CASH_RECHARGE_PRESENT ="56";// 充值赠送
//	public final static String CASH_TYPE_CASH_GUA_BACK ="17";// 方案退保
//	public final static String CASH_TYPE_CASH_PRIZE ="18";// 中奖彩派
//	public final static String CASH_TYPE_CASH_AGENCY ="19";// 代理返点
//	public final static String CASH_TYPE_CASH_EXCHANGE ="20";// 积分兑换
//	public final static String CASH_TYPE_CASH_SPONSOR ="21";// 参与合买
//	public final static String CASH_TYPE_CASH_DRAWBACK ="39";// 投注退还
//	
//	
//	public static String gameTimerOpen = "true";//彩票定时总开关默认开
//
//	//资金类型定义------------------------------------------------
//	public final static String CASH_TYPE_CASH_BET_BJ3="10";// 北京三分彩
//	public final static String CASH_TYPE_CASH_BET_BJPK10 ="11";// 北京赛车
//	public final static String CASH_TYPE_CASH_BET_XJPLU28 ="12";// 幸运28
//	public final static String CASH_TYPE_CASH_BET_CQSSC ="13";// 重庆时时彩
//	public final static String CASH_TYPE_CASH_BET_BJLU28 ="14";// PC蛋蛋
//	public final static String CASH_TYPE_CASH_BET_GDK10 ="15";// 广东快乐10分
//	public final static String CASH_TYPE_CASH_BET_TJSSC ="16";// 天津时时彩
//	public final static String CASH_TYPE_CASH_BET_XJSSC ="17";// 新疆时时彩
//	public final static String CASH_TYPE_CASH_BET_POKER ="18";// 快乐扑克3
//	public final static String CASH_TYPE_CASH_BET_GDPICK11 ="19";// 广东11选5
//	public final static String CASH_TYPE_CASH_BET_JSK3 ="20";// 江苏快乐3
//	public final static String CASH_TYPE_CASH_BET_GXK10 ="21";// 广西快乐10分
//	public final static String CASH_TYPE_CASH_BET_MARKSIX ="22";// 香港六合彩
//	public final static String CASH_TYPE_CASH_BET_DCB ="23";// 双色球
//	public final static String CASH_TYPE_CASH_BET_FC ="24";// 五分彩
//	public final static String CASH_TYPE_CASH_BET_THREE ="25";// 五分彩
//	public final static String CASH_TYPE_CASH_BET_JXPICK11 ="26";// 江西11选5投注
//	public final static String CASH_TYPE_CASH_BET_JXSSC ="27";// 江西时时彩投注
//	public final static String CASH_TYPE_CASH_BET_SDPICK11 ="28";// 山东11选5
//	//add by cuisy 20171021
//	public final static String CASH_TYPE_CASH_BET_SXPICK11="29";//山西11选5
//	public final static String CASH_TYPE_CASH_BET_BJPICK11="30";//北京11选5
//	public final static String CASH_TYPE_CASH_BET_TJPICK11="31";//天津11选5
//	public final static String CASH_TYPE_CASH_BET_HEBPICK11="32";//河北11选5
//	public final static String CASH_TYPE_CASH_BET_NMGPICK11="33";//内蒙古11选5
//	public final static String CASH_TYPE_CASH_BET_LNPICK11="34";//辽宁11选5
//	public final static String CASH_TYPE_CASH_BET_JLPICK11="35";//吉林11选5
//	public final static String CASH_TYPE_CASH_BET_HLJPICK11="36";//黑龙江11选5
//	public final static String CASH_TYPE_CASH_BET_SHPICK11="37";//上海11选5
//	public final static String CASH_TYPE_CASH_BET_JSPICK11="38";//江苏11选5
//	public final static String CASH_TYPE_CASH_BET_ZJPICK11="39";//浙江11选5
//	public final static String CASH_TYPE_CASH_BET_AHPICK11="40";//安徽11选5
//	public final static String CASH_TYPE_CASH_BET_FJPICK11="41";//福建11选5
//	public final static String CASH_TYPE_CASH_BET_HNPICK11="42";//河南11选5
//	public final static String CASH_TYPE_CASH_BET_HUBPICK11="43";//湖北11选5
//	public final static String CASH_TYPE_CASH_BET_GXPICK11="44";//广西11选5
//	public final static String CASH_TYPE_CASH_BET_SCPICK11="45";//四川11选5
//	public final static String CASH_TYPE_CASH_BET_GZPICK11="46";//贵州11选5
//	public final static String CASH_TYPE_CASH_BET_SHXPICK11="47";//陕西11选5
//	public final static String CASH_TYPE_CASH_BET_GSPICK11="48";//甘肃11选5
//	public final static String CASH_TYPE_CASH_BET_XJPICK11="49";//新疆11选5
//	public final static String CASH_TYPE_CASH_BET_YNPICK11="50";//云南11选5
//	public final static String CASH_TYPE_CASH_BET_AHK3="51";//安徽快3
//	public final static String CASH_TYPE_CASH_BET_BJK3="52";//北京快3
//	public final static String CASH_TYPE_CASH_BET_FJK3="53";//福建快3
//	public final static String CASH_TYPE_CASH_BET_GZK3="54";//贵州快3
//	public final static String CASH_TYPE_CASH_BET_GXK3="55";//广西快3
//	public final static String CASH_TYPE_CASH_BET_GSK3="56";//甘肃快3
//	public final static String CASH_TYPE_CASH_BET_HUBK3="57";//湖北快3
//	public final static String CASH_TYPE_CASH_BET_HEBK3="58";//河北快3
//	public final static String CASH_TYPE_CASH_BET_HNK3="59";//河南快3
//	public final static String CASH_TYPE_CASH_BET_JXK3="60";//江西快3
//	public final static String CASH_TYPE_CASH_BET_JLK3="61";//吉林快3
//	public final static String CASH_TYPE_CASH_BET_NMGK3="62";//内蒙古快3
//	public final static String CASH_TYPE_CASH_BET_SHK3="63";//上海快3
//	//~~20171021
//	//------------------------------------------------------------
//	
//	//彩种类型定义-------------------------------------------------
//	public final static String GAME_TYPE_BJ3="0";// 北京三分彩
//	public final static String GAME_TYPE_BJPK10 ="1";// 北京赛车
//	public final static String GAME_TYPE_XJPLU28 ="2";// 幸运28
//	public final static String GAME_TYPE_CQSSC ="3";// 重庆时时彩
//	public final static String GAME_TYPE_BJLU28 ="4";// PC蛋蛋
//	public final static String GAME_TYPE_GDK10 ="5";// 广东快乐10分
//	public final static String GAME_TYPE_TJSSC ="6";// 天津时时彩
//	public final static String GAME_TYPE_XJSSC ="7";// 新疆时时彩
//	public final static String GAME_TYPE_POKER ="8";// 快乐扑克3
//	public final static String GAME_TYPE_GDPICK11 ="9";// 广东11选5
//	public final static String GAME_TYPE_JSK3 ="10";// 江苏快乐3
//	public final static String GAME_TYPE_GXK10="11";//广西快乐十分
//	public final static String GAME_TYPE_MARKSIX="12";//香港六合彩
//	public final static String GAME_TYPE_DCB="13";//双色球
//	public final static String GAME_TYPE_FC="14";//五分彩
//	public final static String GAME_TYPE_THREE="15";//三分彩
//	public final static String GAME_TYPE_JXPICK11 ="16";// 江西11选5
//	public final static String GAME_TYPE_JXSSC ="17";// 江西时时彩
//	public final static String GAME_TYPE_SDPICK11 ="18";// 山东11选5
//	//add by cuisy 20171021
//	public final static String GAME_TYPE_SXPICK11="19";//山西11选5
//	public final static String GAME_TYPE_BJPICK11="20";//北京11选5
//	public final static String GAME_TYPE_TJPICK11="21";//天津11选5
//	public final static String GAME_TYPE_HEBPICK11="22";//河北11选5
//	public final static String GAME_TYPE_NMGPICK11="23";//内蒙古11选5
//	public final static String GAME_TYPE_LNPICK11="24";//辽宁11选5
//	public final static String GAME_TYPE_JLPICK11="25";//吉林11选5
//	public final static String GAME_TYPE_HLJPICK11="26";//黑龙江11选5
//	public final static String GAME_TYPE_SHPICK11="27";//上海11选5
//	public final static String GAME_TYPE_JSPICK11="28";//江苏11选5
//	public final static String GAME_TYPE_ZJPICK11="29";//浙江11选5
//	public final static String GAME_TYPE_AHPICK11="30";//安徽11选5
//	public final static String GAME_TYPE_FJPICK11="31";//福建11选5
//	public final static String GAME_TYPE_HNPICK11="32";//河南11选5
//	public final static String GAME_TYPE_HUBPICK11="33";//湖北11选5
//	public final static String GAME_TYPE_GXPICK11="34";//广西11选5
//	public final static String GAME_TYPE_SCPICK11="35";//四川11选5
//	public final static String GAME_TYPE_GZPICK11="36";//贵州11选5
//	public final static String GAME_TYPE_SHXPICK11="37";//陕西11选5
//	public final static String GAME_TYPE_GSPICK11="38";//甘肃11选5
//	public final static String GAME_TYPE_XJPICK11="39";//新疆11选5
//	public final static String GAME_TYPE_YNPICK11="40";//云南11选5
//	public final static String GAME_TYPE_AHK3="41";//安徽快3
//	public final static String GAME_TYPE_BJK3="42";//北京快3
//	public final static String GAME_TYPE_FJK3="43";//福建快3
//	public final static String GAME_TYPE_GZK3="44";//贵州快3
//	public final static String GAME_TYPE_GXK3="45";//广西快3
//	public final static String GAME_TYPE_GSK3="46";//甘肃快3
//	public final static String GAME_TYPE_HUBK3="47";//湖北快3
//	public final static String GAME_TYPE_HEBK3="48";//河北快3
//	public final static String GAME_TYPE_HNK3="49";//河南快3
//	public final static String GAME_TYPE_JXK3="50";//江西快3
//	public final static String GAME_TYPE_JLK3="51";//吉林快3
//	public final static String GAME_TYPE_NMGK3="52";//内蒙古快3
//	public final static String GAME_TYPE_SHK3="53";//上海快3
//	//~~20171021
//	public final static String GAME_TYPE_BJKL8="80";//北京快乐8
//	//--------------------------------------------------------
//
//	
//	//彩种时间定义 hzb 20171128------------------------------------
//	public final static String GAME_GDPICK11_BEGIN_TIME = "09:00:00";// 广东11选5开始时间
//	public final static String GAME_GDPICK11_END_TIME = "23:00:00";// 广东11选5结束时间
//	public final static String GAME_JXPICK11_BEGIN_TIME = "09:00:00";// 江西11选5开始时间
//	public final static String GAME_JXPICK11_END_TIME  = "23:00:00";// 江西11选5结束时间
//	public final static String GAME_SDPICK11_BEGIN_TIME = "08:25:00";// 山东11选5开始时间
//	public final static String GAME_SDPICK11_END_TIME = "22:55:00";// 山东11选5结束时间
//	public final static String GAME_SXPICK11_BEGIN_TIME= "08:15:00";//山西11选5开始时间
//	public final static String GAME_SXPICK11_END_TIME = "23:55:00";//山西11选5结束时间
//	public final static String GAME_BJPICK11_BEGIN_TIME = "08:50:00";//北京11选5开始时间
//	public final static String GAME_BJPICK11_END_TIME = "23:00:00";//北京11选5结束时间
//	public final static String GAME_TJPICK11_BEGIN_TIME = "09:00:00";//天津11选5开始时间
//	public final static String GAME_TJPICK11_END_TIME = "23:50:00";//天津11选5结束时间
//	public final static String GAME_HEBPICK11_BEGIN_TIME = "08:20:00";//河北11选5开始时间
//	public final static String GAME_HEBPICK11_END_TIME = "22:30:00";//河北11选5结束时间
//	public final static String GAME_NMGPICK11_BEGIN_TIME = "08:56:00";//内蒙古11选5开始时间
//	public final static String GAME_NMGPICK11_END_TIME = "23:06:00";//内蒙古11选5结束时间
//	public final static String GAME_LNPICK11_BEGIN_TIME = "08:40:00";//辽宁11选5开始时间
//	public final static String GAME_LNPICK11_END_TIME = "22:30:00";//辽宁11选5结束时间
//	public final static String GAME_JLPICK11_BEGIN_TIME = "08:20:00";//吉林11选5开始时间
//	public final static String GAME_JLPICK11_END_TIME = "21:30:00";//吉林11选5结束时间
//	public final static String GAME_HLJPICK11_BEGIN_TIME = "07:55:00";//黑龙江11选5开始时间
//	public final static String GAME_HLJPICK11_END_TIME = "22:35:00";//黑龙江11选5结束时间
//	public final static String GAME_SHPICK11_BEGIN_TIME = "09:00:00";//上海11选5开始时间
//	public final static String GAME_SHPICK11_END_TIME = "23:50:00";//上海11选5结束时间
//	public final static String GAME_JSPICK11_BEGIN_TIME = "08:25:00";//江苏11选5开始时间
//	public final static String GAME_JSPICK11_END_TIME = "22:05:00";//江苏11选5结束时间
//	public final static String GAME_ZJPICK11_BEGIN_TIME = "08:20:00";//浙江11选5开始时间
//	public final static String GAME_ZJPICK11_END_TIME = "23:30:00";//浙江11选5结束时间
//	public final static String GAME_AHPICK11_BEGIN_TIME = "08:30:00";//安徽11选5开始时间
//	public final static String GAME_AHPICK11_END_TIME = "22:00:00";//安徽11选5结束时间
//	public final static String GAME_FJPICK11_BEGIN_TIME = "08:00:00";//福建11选5开始时间
//	public final static String GAME_FJPICK11_END_TIME = "23:00:00";//福建11选5结束时间
//	public final static String GAME_HNPICK11_BEGIN_TIME = "08:20:00";//河南11选5开始时间
//	public final static String GAME_HNPICK11_END_TIME = "23:00:00";//河南11选5结束时间
//	public final static String GAME_HUBPICK11_BEGIN_TIME = "08:25:00";//湖北11选5开始时间
//	public final static String GAME_HUBPICK11_END_TIME = "21:55:00";//湖北11选5结束时间
//	public final static String GAME_GXPICK11_BEGIN_TIME = "08:50:00";//广西11选5开始时间
//	public final static String GAME_GXPICK11_END_TIME = "23:50:00";//广西11选5结束时间
//	public final static String GAME_SCPICK11_BEGIN_TIME = "08:50:00";//四川11选5开始时间
//	public final static String GAME_SCPICK11_END_TIME = "23:50:00";//四川11选5结束时间
//	public final static String GAME_GZPICK11_BEGIN_TIME = "08:50:00";//贵州11选5开始时间
//	public final static String GAME_GZPICK11_END_TIME = "22:10:00";//贵州11选5结束时间
//	public final static String GAME_SHXPICK11_BEGIN_TIME = "08:20:00";//陕西11选5开始时间
//	public final static String GAME_SHXPICK11_END_TIME = "23:00:00";//陕西11选5结束时间
//	public final static String GAME_GSPICK11_BEGIN_TIME = "08:50:00";//甘肃11选5开始时间
//	public final static String GAME_GSPICK11_END_TIME = "23:10:00";//甘肃11选5结束时间
//	public final static String GAME_XJPICK11_BEGIN_TIME = "10:00:00";//新疆11选5开始时间
//	public final static String GAME_XJPICK11_END_TIME = "02:00:00";//新疆11选5结束时间
//	public final static String GAME_YNPICK11_BEGIN_TIME = "08:50:00";//云南11选5开始时间
//	public final static String GAME_YNPICK11_END_TIME = "23:00:00";//云南11选5结束时间
//	
//	public final static String GAME_JSK3_BEGIN_TIME ="08:30:00";// 江苏快乐3开始时间
//	public final static String GAME_JSK3_END_TIME ="22:10:00";// 江苏快乐3结束时间
//	public final static String GAME_AHK3_BEGIN_TIME = "08:40:00";//安徽快3开始时间
//	public final static String GAME_AHK3_END_TIME = "22:00:00";//安徽快3结束时间
//	public final static String GAME_BJK3_BEGIN_TIME = "09:00:00";//北京快3开始时间
//	public final static String GAME_BJK3_END_TIME = "23:50:00";//北京快3结束时间
//	public final static String GAME_FJK3_BEGIN_TIME = "09:00:00";//福建快3开始时间
//	public final static String GAME_FJK3_END_TIME = "22:00:00";//福建快3结束时间
//	public final static String GAME_GZK3_BEGIN_TIME = "09:00:00";//贵州快3开始时间
//	public final static String GAME_GZK3_END_TIME = "22:00:00";//贵州快3结束时间
//	public final static String GAME_GXK3_BEGIN_TIME = "09:27:00";//广西快3开始时间
//	public final static String GAME_GXK3_END_TIME = "22:27:00";//广西快3结束时间
//	public final static String GAME_GSK3_BEGIN_TIME = "10:00:00";//甘肃快3开始时间
//	public final static String GAME_GSK3_END_TIME = "21:00:00";//甘肃快3结束时间
//	public final static String GAME_HUBK3_BEGIN_TIME = "09:00:00";//湖北快3开始时间
//	public final static String GAME_HUBK3_END_TIME = "22:00:00";//湖北快3结束时间
//	public final static String GAME_HEBK3_BEGIN_TIME = "08:30:00";//河北快3开始时间
//	public final static String GAME_HEBK3_END_TIME = "22:00:00";//河北快3结束时间
//	public final static String GAME_HNK3_BEGIN_TIME = "08:30:00";//河南快3开始时间
//	public final static String GAME_HNK3_END_TIME = "22:30:00";//河南快3结束时间
//	public final static String GAME_JXK3_BEGIN_TIME = "08:55:00";//江西快3开始时间
//	public final static String GAME_JXK3_END_TIME = "22:55:00";//江西快3结束时间
//	public final static String GAME_JLK3_BEGIN_TIME = "08:20:00";//吉林快3开始时间
//	public final static String GAME_JLK3_END_TIME = "22:00:00";//吉林快3结束时间
//	public final static String GAME_NMGK3_BEGIN_TIME = "09:40:00";//内蒙古快3开始时间
//	public final static String GAME_NMGK3_END_TIME = "21:40:00";//内蒙古快3结束时间
//	public final static String GAME_SHK3_BEGIN_TIME = "08:48:00";//上海快3开始时间
//	public final static String GAME_SHK3_END_TIME = "22:28:00";//上海快3结束时间
//	//--------------------------------------------------------
//	
//	
//	/**方案类型 -代购**/
//	public final static String PROCUREMENT_SERVICE = "0";//代购
//	/**方案类型 -合买**/
//	public final static String SPONSOR = "1";//合买
//	/**购买行文 -发起购买**/
//	public final static String ORIGINATE = "0";//发起购买
//	/**购买行文 -参与购买**/
//	public final static String PARTICIPATOR = "1";//参与购买
//	
//	/**彩票订单状态-未完成**/
//	public final static String UNFINISHED = "0";//未完成
//	/**彩票订单状态-投注无效。退款（系统撤单）**/
//	public final static String INVALID_REFUND = "1";//投注无效。退款（系统撤单）
//	/**彩票订单状态-未开奖 （已出票）(进行中)**/
//	public final static String INIT = "2";//2=未开奖 （已出票）(进行中)
//	/**彩票订单状态-开奖中**/
//	public final static String OPENING = "3";//3=开奖中
//	/**彩票订单状态-中奖**/
//	public final static String WIN = "4";//4=中奖
//	/**彩票订单状态-未中奖**/
//	public final static String WIN_NO = "5";//5=未中奖
//	/**彩票订单状态-打和**/
//	public final static String WIN_HE = "6";//6=打和
//	
//	/**不保底**/
//	public final static String NOT_GUARANTEE = "0";//不保底
//	/**保底**/
//	public final static String GUARANTEE = "1";//保底
//	/**保底百分比  20% **/
//	public final static String GUARANTEE_PERCENTAGE = "0.2";
//	/**中奖就停止追号**/
//	public final static String WIN_STOP = "1";//中奖就停止追号
//	/**中奖继续追号**/
//	public final static String WIN_NO_STOP = "0";//中奖继续追号
//	/**不追号**/
//	public final static String NOT_ADD_NO = "0";//不追号
//	/**追号**/
//	public final static String ADD_NO = "1";//追号
//
//	/**开奖状态-未开奖**/
//	public final static String OPEN_STATUS_INIT = "0";
//	/**开奖状态-开奖中**/
//	public final static String OPEN_STATUS_OPENING = "1";
//	/**开奖状态-已开奖**/
//	public final static String OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
//
//	/**
//	 * 苹果审核控制版本
//	 */
//	public static String IOS_REVIEW_VISION = "";
//	
//	/** 最多一天发送短信条数 */
//	public static String msgMaxCount = "5";
//	/** 最短发送间隔时间 */
//	public static String msgTime = "10";
//	
//	
//	/**交易类型-在线支付**/
//	public final static String TRADE_TYPE_MONEY_ONLINE = "1";
//	/**交易类型-积分支付**/
//	public final static String TRADE_TYPE_MONEY_POINTS = "2";
//	
//	/**订单状态-未支付**/
//	public final static String PAY_STATUS_UNPAID = "1";//线下--未支付
//	/**订单状态-已支付**/
//	public final static String PAY_STATUS_PAID = "2";//线下--未处理 线上--未发货   在线-- 已支付
//	/**订单状态-已拒绝**/
//	public final static String PAY_STATUS_REJECT = "3";//线下--已拒绝
//	/**订单状态-已充值**/
//	public final static String PAY_STATUS_RESOLVED = "4";//线下---已充值
//
//	
//	
//	/**游戏类型-双色球**/
//	public final static String GAME_TYPE_SSQ = "1";
//	/**游戏类型-快三**/
//	public final static String GAME_TYPE_K3 = "2";
//	/**积分说明**/
//	public final static String GAME_POINT_INSTRUCTION= "27";
//	
//	//积分交易类型
//	public final static String POINT_TYPE_ONLINE= "1";//在线充值
//	public final static String POINT_TYPE_CARD= "2";//卡充--未用
//	public final static String POINT_TYPE_REGISTER= "3";//注册赠送
//	public final static String POINT_TYPE_SHARE= "4";//分享--未用
//	public final static String POINT_TYPE_INVITE= "5";//邀请--未用
//
//	public final static String POINT_TYPE_BET_HHMF= "26";//游戏黑红梅方
//	public final static String POINT_TYPE_MANAGER_SET= "27";//管理员充值
//	
////	public final static String POINT_TYPE_BET_HBK3= "28";//湖北快三
////	public final static String POINT_TYPE_BET_AHK3= "29";//安徽快三
////	public final static String POINT_TYPE_BET_GXK3= "30";//广西快三
////	public final static String POINT_TYPE_BET_JLK3= "31";//吉林快三
////	public final static String POINT_TYPE_BET_JSK3= "32";//江苏快三
////	public final static String POINT_TYPE_BET_GXK10= "33";//广西快乐十分
////	public final static String POINT_TYPE_BET_GDK10= "34";//广东快乐十分
////	public final static String POINT_TYPE_BET_HNK10= "35";//湖南快乐十分
////	public final static String POINT_TYPE_BET_CQK10= "36";//重庆快乐十分
////	public final static String POINT_TYPE_BJ_LUCKY28= "37";//PC蛋蛋(北京幸运28)
////	public final static String POINT_TYPE_XJP_LUCKY28= "38";//幸运28(新加坡28)
//
//	
//	public final static int POINT_12SHENGXIAO_MAX=5000;//12生肖每次最大下注
//	public final static int POINT_HHMF_MAX_HHMF = 100000;//黑红梅方四中花色最大下注 (每个人）
//	public final static int POINT_HHMF_MAX_WANG = 20000;//王最大下注 (每个人）
//	public final static int POINT_HHMF_MAX_EVERY_BET = 500;//王最大下注 (每个人）
//	public final static String GAME_HHMF_HEITAO= "1";//黑桃
//	public final static String GAME_HHMF_HONGTAO= "2";//红桃
//	public final static String GAME_HHMF_MEIHUA= "3";//梅花
//	public final static String GAME_HHMF_FANGKUAI= "4";//方块
//	public final static String GAME_HHMF_WANG= "5";//王
//	
//	public final static String GAME_WIN_STATUS_NOOPEN= "0";//未开奖
//	public final static String GAME_WIN_STATUS_WIN= "1";//中奖
//	public final static String GAME_WIN_STATUS_NOWIN= "2";//未中奖
//	
//	public final static String GAME_HHMF_LEVEL_DI= "1";//低
//	public final static String GAME_HHMF_LEVEL_ZHONG= "2";//中
//	public final static String GAME_HHMF_LEVEL_GAO= "3";//高
//	
//	
//	/**
//	 * 返回彩种名称
//	 * @param gameType 彩种类型
//	 * @return
//	 */
//	public final static String getGameName(String gameType){
//		if (Constants.GAME_TYPE_BJ3.equals(gameType)) return "北京三分彩";
//		else if(Constants.GAME_TYPE_BJPK10.equals(gameType)) return "北京赛车";
//		else if(Constants.GAME_TYPE_XJPLU28.equals(gameType)) return "幸运28";
//		else if(Constants.GAME_TYPE_CQSSC.equals(gameType)) return "重庆时时彩";
//		else if(Constants.GAME_TYPE_BJLU28.equals(gameType)) return "PC蛋蛋";
//		else if(Constants.GAME_TYPE_GDK10.equals(gameType)) return "广东快乐10分";
//		else if(Constants.GAME_TYPE_TJSSC.equals(gameType)) return "天津时时彩";
//		else if(Constants.GAME_TYPE_XJSSC.equals(gameType)) return "新疆时时彩";
//		else if(Constants.GAME_TYPE_POKER.equals(gameType)) return "快乐扑克3";
//		else if(Constants.GAME_TYPE_GDPICK11.equals(gameType)) return "广东11选5";
//		else if(Constants.GAME_TYPE_JSK3.equals(gameType)) return "江苏快乐3";
//		else if(Constants.GAME_TYPE_GXK10.equals(gameType)) return "广西快乐十分";
//		else if(Constants.GAME_TYPE_MARKSIX.equals(gameType)) return "香港六合彩";
//		else if(Constants.GAME_TYPE_DCB.equals(gameType)) return "双色球";
//		else if(Constants.GAME_TYPE_FC.equals(gameType)) return "五分彩";
//		else if(Constants.GAME_TYPE_THREE.equals(gameType)) return "三分彩";
//		else if(Constants.GAME_TYPE_JXPICK11.equals(gameType)) return "江西11选5";
//		else if(Constants.GAME_TYPE_JXSSC.equals(gameType)) return "江西时时彩";
//		else if(Constants.GAME_TYPE_SDPICK11.equals(gameType)) return "山东11选5";
//		else if(Constants.GAME_TYPE_SXPICK11.equals(gameType)) return "山西11选5";
//		else if(Constants.GAME_TYPE_BJPICK11.equals(gameType)) return "北京11选5";
//		else if(Constants.GAME_TYPE_TJPICK11.equals(gameType)) return "天津11选5";
//		else if(Constants.GAME_TYPE_HEBPICK11.equals(gameType)) return "河北11选5";
//		else if(Constants.GAME_TYPE_NMGPICK11.equals(gameType)) return "内蒙古11选5";
//		else if(Constants.GAME_TYPE_LNPICK11.equals(gameType)) return "辽宁11选5";
//		else if(Constants.GAME_TYPE_JLPICK11.equals(gameType)) return "吉林11选5";
//		else if(Constants.GAME_TYPE_HLJPICK11.equals(gameType)) return "黑龙江11选5";
//		else if(Constants.GAME_TYPE_SHPICK11.equals(gameType)) return "上海11选5";
//		else if(Constants.GAME_TYPE_JSPICK11.equals(gameType)) return "江苏11选5";
//		else if(Constants.GAME_TYPE_ZJPICK11.equals(gameType)) return "浙江11选5";
//		else if(Constants.GAME_TYPE_AHPICK11.equals(gameType)) return "安徽11选5";
//		else if(Constants.GAME_TYPE_FJPICK11.equals(gameType)) return "福建11选5";
//		else if(Constants.GAME_TYPE_HNPICK11.equals(gameType)) return "河南11选5";
//		else if(Constants.GAME_TYPE_HUBPICK11.equals(gameType)) return "湖北11选5";
//		else if(Constants.GAME_TYPE_GXPICK11.equals(gameType)) return "广西11选5";
//		else if(Constants.GAME_TYPE_SCPICK11.equals(gameType)) return "四川11选5";
//		else if(Constants.GAME_TYPE_GZPICK11.equals(gameType)) return "贵州11选5";
//		else if(Constants.GAME_TYPE_SHXPICK11.equals(gameType)) return "陕西11选5";
//		else if(Constants.GAME_TYPE_GSPICK11.equals(gameType)) return "甘肃11选5";
//		else if(Constants.GAME_TYPE_XJPICK11.equals(gameType)) return "新疆11选5";
//		else if(Constants.GAME_TYPE_YNPICK11.equals(gameType)) return "云南11选5";
//		else if(Constants.GAME_TYPE_AHK3.equals(gameType)) return "安徽快3";
//		else if(Constants.GAME_TYPE_BJK3.equals(gameType)) return "北京快3";
//		else if(Constants.GAME_TYPE_FJK3.equals(gameType)) return "福建快3";
//		else if(Constants.GAME_TYPE_GZK3.equals(gameType)) return "贵州快3";
//		else if(Constants.GAME_TYPE_GXK3.equals(gameType)) return "广西快3";
//		else if(Constants.GAME_TYPE_GSK3.equals(gameType)) return "甘肃快3";
//		else if(Constants.GAME_TYPE_HUBK3.equals(gameType)) return "湖北快3";
//		else if(Constants.GAME_TYPE_HEBK3.equals(gameType)) return "河北快3";
//		else if(Constants.GAME_TYPE_HNK3.equals(gameType)) return "河南快3";
//		else if(Constants.GAME_TYPE_JXK3.equals(gameType)) return "江西快3";
//		else if(Constants.GAME_TYPE_JLK3.equals(gameType)) return "吉林快3";
//		else if(Constants.GAME_TYPE_NMGK3.equals(gameType)) return "内蒙古快3";
//		else if(Constants.GAME_TYPE_SHK3.equals(gameType)) return "上海快3";
//		else {return "其他";}
//	}
//	/**
//	 * 返回彩种订单名称
//	 * @param gameType 彩种类型
//	 * @return
//	 */
//	public final static String getGameOrderName(String gameType){
//		if (Constants.GAME_TYPE_BJ3.equals(gameType)) return "BJSFC";
//		else if(Constants.GAME_TYPE_BJPK10.equals(gameType)) return "BJSC";
//		else if(Constants.GAME_TYPE_XJPLU28.equals(gameType)) return "XYEB";
//		else if(Constants.GAME_TYPE_CQSSC.equals(gameType)) return "CQSSC";
//		else if(Constants.GAME_TYPE_BJLU28.equals(gameType)) return "PCDD";
//		else if(Constants.GAME_TYPE_GDK10.equals(gameType)) return "GDKLSF";
//		else if(Constants.GAME_TYPE_TJSSC.equals(gameType)) return "TJSSC";
//		else if(Constants.GAME_TYPE_XJSSC.equals(gameType)) return "XJSSC";
//		else if(Constants.GAME_TYPE_POKER.equals(gameType)) return "KLPKS";
//		else if(Constants.GAME_TYPE_GDPICK11.equals(gameType)) return "GDSYXW";
//		else if(Constants.GAME_TYPE_JSK3.equals(gameType)) return "JSKLS";
//		else if(Constants.GAME_TYPE_GXK10.equals(gameType)) return "GXKLSF";
//		else if(Constants.GAME_TYPE_MARKSIX.equals(gameType)) return "XGLHC";
//		else if(Constants.GAME_TYPE_DCB.equals(gameType)) return "SEQ";
//		else if(Constants.GAME_TYPE_FC.equals(gameType)) return "WFC";
//		else if(Constants.GAME_TYPE_THREE.equals(gameType)) return "SFC";
//		else if(Constants.GAME_TYPE_JXPICK11.equals(gameType)) return "JXSYXW";
//		else if(Constants.GAME_TYPE_JXSSC.equals(gameType)) return "JXSSC";
//		else if(Constants.GAME_TYPE_SDPICK11.equals(gameType)) return "SDSYXW";
//		else if(Constants.GAME_TYPE_SXPICK11.equals(gameType)) return "SXSYXW";
//		else if(Constants.GAME_TYPE_BJPICK11.equals(gameType)) return "BJSYXW";
//		else if(Constants.GAME_TYPE_TJPICK11.equals(gameType)) return "TJSYXW";
//		else if(Constants.GAME_TYPE_HEBPICK11.equals(gameType)) return "HEBSYXW";
//		else if(Constants.GAME_TYPE_NMGPICK11.equals(gameType)) return "NMGSYXW";
//		else if(Constants.GAME_TYPE_LNPICK11.equals(gameType)) return "LNSYXW";
//		else if(Constants.GAME_TYPE_JLPICK11.equals(gameType)) return "JLSYXW";
//		else if(Constants.GAME_TYPE_HLJPICK11.equals(gameType)) return "HLJSYXW";
//		else if(Constants.GAME_TYPE_SHPICK11.equals(gameType)) return "SHSYXW";
//		else if(Constants.GAME_TYPE_JSPICK11.equals(gameType)) return "JSSYXW";
//		else if(Constants.GAME_TYPE_ZJPICK11.equals(gameType)) return "ZJSYXW";
//		else if(Constants.GAME_TYPE_AHPICK11.equals(gameType)) return "AHSYXW";
//		else if(Constants.GAME_TYPE_FJPICK11.equals(gameType)) return "FJSYXW";
//		else if(Constants.GAME_TYPE_HNPICK11.equals(gameType)) return "HNSYXW";
//		else if(Constants.GAME_TYPE_HUBPICK11.equals(gameType)) return "HUBSYXW";
//		else if(Constants.GAME_TYPE_GXPICK11.equals(gameType)) return "GXSYXW";
//		else if(Constants.GAME_TYPE_SCPICK11.equals(gameType)) return "SCSYXW";
//		else if(Constants.GAME_TYPE_GZPICK11.equals(gameType)) return "GZSYXW";
//		else if(Constants.GAME_TYPE_SHXPICK11.equals(gameType)) return "SHXSYXW";
//		else if(Constants.GAME_TYPE_GSPICK11.equals(gameType)) return "GSSYXW";
//		else if(Constants.GAME_TYPE_XJPICK11.equals(gameType)) return "XJSYXW";
//		else if(Constants.GAME_TYPE_YNPICK11.equals(gameType)) return "YNSYXW";
//		else if(Constants.GAME_TYPE_AHK3.equals(gameType)) return "AHKS";
//		else if(Constants.GAME_TYPE_BJK3.equals(gameType)) return "BJKS";
//		else if(Constants.GAME_TYPE_FJK3.equals(gameType)) return "FJKS";
//		else if(Constants.GAME_TYPE_GZK3.equals(gameType)) return "GZKS";
//		else if(Constants.GAME_TYPE_GXK3.equals(gameType)) return "GXKS";
//		else if(Constants.GAME_TYPE_GSK3.equals(gameType)) return "GSKS";
//		else if(Constants.GAME_TYPE_HUBK3.equals(gameType)) return "HUBKS";
//		else if(Constants.GAME_TYPE_HEBK3.equals(gameType)) return "HEBKS";
//		else if(Constants.GAME_TYPE_HNK3.equals(gameType)) return "HNKS";
//		else if(Constants.GAME_TYPE_JXK3.equals(gameType)) return "JXKS";
//		else if(Constants.GAME_TYPE_JLK3.equals(gameType)) return "JLKS";
//		else if(Constants.GAME_TYPE_NMGK3.equals(gameType)) return "NMGKS";
//		else if(Constants.GAME_TYPE_SHK3.equals(gameType)) return "SHKS";
//		else {return "其他";}
//	}
//	
//	
//	
//	/**
//	 *返回开奖结果中文
//	 * @param winResult
//	 * @return
//	 */
//	public final static String getWinResultNameZh(String winResult) {
//		if(Constants.UNFINISHED.equals(winResult)) return "未完成";
//		else if(Constants.INVALID_REFUND.equals(winResult)) return "系统撤单";
//		else if(Constants.INIT.equals(winResult)) return "未开奖";
//		else if(Constants.OPENING.equals(winResult)) return "开奖中";
//		else if(Constants.WIN.equals(winResult)) return "已中奖";
//		else if(Constants.WIN_NO.equals(winResult)) return "未中奖";
//		else if(Constants.WIN_HE.equals(winResult)) return "打和";
//		else return "其他";
//	}
//	
//	/**
//	 *返回充值类型中文  1.支付宝 2.银联 5.微信 6.直充
//	 * @param type
//	 * @return
//	 */
//	public final static String getRechargeTypeNameZh(String type) {
////		if(Constants.CASH_TYPE_ONLINE.equals(type)) return "在线充值";	
//		if(type.equals("1")||type.equals("2")||type.equals("5"))return "在线充值";	
//		else if(Constants.CASH_TYPE_OFFLINE.equals(type)) return "线下充值";
//		else if(Constants.CASH_TYPE_CASH_SYSTEM.equals(type)) return "系统充值";
//		else if(Constants.CASH_TYPE_CASH_RECHARGE_PRESENT.equals(type)) return "充值赠送";
//		else return "其他";
//	}
//	
//	/**
//	 *返回资金类型中文
//	 * @param cashType
//	 * @return
//	 */
//	public final static String getCashTradeTypeNameZh(String cashType) {
//		if(cashType.equals(Constants.CASH_TYPE_ONLINE)) return "在线充值";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_BUY_LOTO )) return " 购买彩票";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_GUARANTEE )) return "方案保底";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_FROZEN )) return "保底冻结";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_DRAW )) return "方案撤单";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_SYSTEM )) return " 系统充值";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_SYS_CHARGE )) return "系统扣款";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_PRESENT )) return "注册赠送";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_GUA_BACK )) return "方案退保";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_PRIZE )) return "中奖彩派";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_AGENCY )) return "代理返点";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_EXCHANGE )) return "积分兑换";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_SPONSOR )) return "参与合买";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_DRAWBACK )) return "投注退还";
//		else if(cashType.equals(Constants.CASH_TYPE_CARD)) return "卡充";
//		else if(cashType.equals(Constants.CASH_TYPE_OFFLINE)) return "线下充值";
//		else if(cashType.equals(Constants.CASH_TYPE_INFO_PUBLISH)) return "供求交易";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_OUT)) return "提现";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_RETURN )) return "提现审核返回";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_CHECKOUT)) return "结算";
//		else if(cashType.equals(Constants.CASH_TYPE_CASH_REFUND)) return "退款";
//		else return "其他";
//	}
//	
//	/**
//	 * 返回积分类型中文
//	 * @param cashType
//	 * @return
//	 */
//	public final static String getPointTradeTypeNameZh(String pointType) {
//		if(pointType.equals(Constants.POINT_TYPE_REGISTER)) return "注册赠送";
//		else if(pointType.equals(Constants.POINT_TYPE_ONLINE)) return "在线充值";
////		else if(pointType.equals(Constants.POINT_TYPE_CARD)) return "卡充";
////		else if(pointType.equals(Constants.POINT_TYPE_SHARE)) return "分享";
//		else if(pointType.equals(Constants.POINT_TYPE_INVITE)) return "提现";
////		else if(pointType.equals(Constants.POINT_TYPE_INFO_PUBLISH)) return "发布信息";
////		else if(pointType.equals(Constants.POINT_TYPE_ESTATE_REPAIR)) return "物业报修";
////		else if(pointType.equals(Constants.POINT_TYPE_EXCHANGE)) return "积分兑换";
////		else if(pointType.equals(Constants.POINT_TYPE_POINT_PAY)) return "积分支付";
////		else if(pointType.equals(Constants.POINT_TYPE_DOING)) return "谁干活";
////		else if(pointType.equals(Constants.POINT_TYPE_EAT)) return "谁请客";
////		else if(pointType.equals(Constants.POINT_TYPE_DRINK)) return "谁喝酒";
////		else if(pointType.equals(Constants.POINT_TYPE_GAME)) return "好运来";
////		else if(pointType.equals(Constants.POINT_TYPE_GAME_12)) return "益智生肖";
////		else if(pointType.equals(Constants.POINT_TYPE_GAME_ZYM)) return "快乐四方";
////		else if(pointType.equals(Constants.POINT_TYPE_GAME_ZYM)) return "快乐四方";
////		else if(pointType.equals(Constants.POINT_TYPE_GAME_ZYM)) return "快乐四方";
//		else if(pointType.equals(Constants.POINT_TYPE_BET_HHMF)) return "黑红梅方";
//		else if(pointType.equals(Constants.POINT_TYPE_MANAGER_SET)) return "管理员充值";
////		else if(pointType.equals(Constants.POINT_TYPE_BET_HBK3)) return "湖北快三";
////		else if(pointType.equals(Constants.POINT_TYPE_BET_AHK3)) return "安徽快三";
////		else if(pointType.equals(Constants.POINT_TYPE_BET_GXK3)) return "广西快三";
////		else if(pointType.equals(Constants.POINT_TYPE_BET_JLK3)) return "吉林快三";
////		else if(pointType.equals(Constants.POINT_TYPE_BET_JSK3)) return "江苏快三";
////		else if(pointType.equals(Constants.POINT_TYPE_BET_GXK10)) return "广西快乐十分";
////		else if(pointType.equals(Constants.POINT_TYPE_BET_GDK10)) return "广东快乐十分";
////		else if(pointType.equals(Constants.POINT_TYPE_BET_HNK10)) return "湖南快乐十分";
////		else if(pointType.equals(Constants.POINT_TYPE_BET_CQK10)) return "重庆快乐十分";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_DRAWBACK)) return "投注退还";
//		
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
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_DCB)) return "双色球";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_FC)) return "五分彩";
//		
//		//add by cuisy 20171021
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_JXPICK11)) return "江西11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_JXSSC)) return "江西时时彩";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_SDPICK11)) return "山东11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_SXPICK11)) return "山西11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_BJPICK11)) return "北京11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_TJPICK11)) return "天津11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_HEBPICK11)) return "河北11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_NMGPICK11)) return "内蒙古11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_LNPICK11)) return "辽宁11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_JLPICK11)) return "吉林11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_HLJPICK11)) return "黑龙江11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_SHPICK11)) return "上海11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_JSPICK11)) return "江苏11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_ZJPICK11)) return "浙江11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_AHPICK11)) return "安徽11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_FJPICK11)) return "福建11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_HNPICK11)) return "河南11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_HUBPICK11)) return "湖北11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_GXPICK11)) return "广西11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_SCPICK11)) return "四川11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_GZPICK11)) return "贵州11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_SHXPICK11)) return "陕西11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_GSPICK11)) return "甘肃11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_XJPICK11)) return "新疆11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_YNPICK11)) return "云南11选5";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_AHK3)) return "安徽快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_BJK3)) return "北京快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_FJK3)) return "福建快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_GZK3)) return "贵州快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_GXK3)) return "广西快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_GSK3)) return "甘肃快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_HUBK3)) return "湖北快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_HEBK3)) return "河北快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_HNK3)) return "河南快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_JXK3)) return "江西快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_JLK3)) return "吉林快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_NMGK3)) return "内蒙古快3";
//		else if(pointType.equals(Constants.CASH_TYPE_CASH_BET_SHK3)) return "上海快3";
//		//~~20171021
//		
//		else return "其他";
//	}
//	/**
//	 * 返回用户类型中文
//	 */
//	public final static String getUserTypeZh(String cashType) {
//		if(cashType.equals(Constants.USER_TYPE_SUER)) return "普通用户";
//		else if(cashType.equals(Constants.USER_TYPE_ADMIN)) return "管理员";
//		else if(cashType.equals(Constants.USER_TYPE_ADMIN_SHANGJIA)) return "商家管理账号";
//		else if(cashType.equals(Constants.USER_TYPE_SUPERADMIN)) return "超级管理员";
//		else if(cashType.equals(Constants.USER_TYPE_SELLER_EAT)) return "订餐管理员";
//		else if(cashType.equals(Constants.USER_TYPE_SELLER_STORE )) return "电商管理员";
//		else if(cashType.equals(Constants.USER_TYPE_SELLER_BUY )) return "团购管理员";
//		else if(cashType.equals(Constants.USER_TYPE_USER_BAID )) return "商圈管理员";
//		else if(cashType.equals(Constants.USER_TYPE_USER_CCID )) return "小区管理员";
//		else if(cashType.equals(Constants.USER_TYPE_USER_ESTATE )) return "物业管理员";
//		else if(cashType.equals(Constants.USER_TYPE_TEST )) return "测试用户";
//		else if(cashType.equals(Constants.USER_TYPE_AGENT )) return "代理用户";
//		else return "其他";
//	}
//	/**
//	 * 返回员工类型中文
//	 */
//	public final static String getEmployeeTypeZh(String userType) {
//		if(userType.equals(Constants.EMPLOYEE_TYPE_ORDINARY)) return "普通员工";
//		else if(userType.equals(Constants.EMPLOYEE_TYPE_TASK)) return "派单员工";
//		else if(userType.equals(Constants.EMPLOYEE_TYPE_EXPRESS)) return "外部配送人员";
//		else if(userType.equals(Constants.EMPLOYEE_TYPE_ADMIN)) return "管理人员";
//		else return "其他";
//	}
//	
//	public static String POINTS_CHARGE_THIRD_PAY_SWITCH = "1";////三方充值积分开关 在线支付充值积分，默认为打开
//	public static String POINTS_USER_THIRD_INTRODUCTION_SWITCH = "1";
//	public static String POINTS_USER_THIRD_INTRODUCTION_CONTENT="";
//	//----------------
//	//短信配置常量 f3
//	public static String SMS_SEND_URL = "";
//	public static String SMS_ECECCID = "";
//	public static String SMS_PASSWORD = "";
//	public static String SMS_MSG_TYPE = "5";
//	public static String SMS_LONG_CODE = "";
//	public static String SMS_CALLBACK_URL="";
//	
//	//短信配置常量 ipyy
//	public static String SMS_SEND_URL_IPYY = "";
//	public static String SMS_ECECCID_IPYY = "";
//	public static String SMS_PASSWORD_IPYY = "";
//	public static String SMS_CALLBACK_URL_IPYY="";
//	
//	//短信内容定义
//	public static String SMS_MSG_SIGNATURE="";
//	public static String SMS_MSG_REG="";
//	public static String SMS_MSG_REREG="";
//	public static String SMS_MSG_COUPON="";
//	public static String SMS_MSG_COUPON_EXCHANGE="";
//	
//	//苹果部署模式[1=开发环境_development][2=生产环境_production]
//	public static String IOS_DEPLOY_MODE="1";//默认开发环境
//	public static String IOS_DEPLOY_MODE_DEVELOPMENT="1";//开发环境
//	public static String IOS_DEPLOY_MODE_PRODUCTION="2";//生产环境
//	
//	//苹果发布模式[1=企业版][2=商店版]
//	public static String IOS_PUBLISH_MODE="1";//默认企业版
//	public static String IOS_PUBLISH_MODE_INHOUSE="1";//企业版
//	public static String IOS_PUBLISH_MODE_APPSTORE="2";//商店版
//	
//	//推送配置---------------------------------------------
//	//安卓用户版
//	public static String BAIDU_PUSH_APIKEY_ANDROID="";
//	public static String BAIDU_PUSH_SECRETKEY_ANDROID="";
//	//安卓配送版
//	public static String BAIDU_PUSH_APIKEY_ANDROID_SEND="";
//	public static String BAIDU_PUSH_SECRETKEY_ANDROID_SEND="";
//	//苹果用户商店版
//	public static String BAIDU_PUSH_APIKEY_IOS_APPSTORE="";
//	public static String BAIDU_PUSH_SECRETKEY_IOS_APPSTORE="";
//	//苹果用户企业版
//	public static String BAIDU_PUSH_APIKEY_IOS_INHOUSE="";
//	public static String BAIDU_PUSH_SECRETKEY_IOS_INHOUSE="";
//	//苹果配送商店版
//	public static String BAIDU_PUSH_APIKEY_IOS_SEND_APPSTORE="";
//	public static String BAIDU_PUSH_SECRETKEY_IOS_SEND_APPSTORE="";
//	//苹果配送企业版
//	public static String BAIDU_PUSH_APIKEY_IOS_SEND_INHOUSE="";
//	public static String BAIDU_PUSH_SECRETKEY_IOS_SEND_INHOUSE="";
//	//---------------------------
//
//	
//	/**极光推送 app key*************************/
//	public  static String JPUSH_APP_KEY="";
//	/**极光推送 Master Secret *************************/
//	public  static String JPUSH_MASTER_SECRET="";
//	
//	// ---------------------------------------------------------------
//	// ####################支付宝支付###########################
//	/** 支付宝合作者id **/
//	public static String alipayPartner = "";
//	/** 私钥 **/
//	public static String alipayRsaPrivate = "";
//	/** 收款帐号 **/
//	public static String alipaySellerId = "";
//	/** 异步回调地址-订餐 **/
//	public static String alipayCallBackEat = "";
//	/** 异步回调地址-电商 **/
//	public static String alipayCallBackStore = "";
//	/** 异步回调地址-团购 **/
//	public static String alipayCallBackBuy = "";
//	/** 异步回调地址-在线充值 **/
//	public static String alipayCallBackRecharge = "";
//	/** 异步回调地址-礼品卡 **/
//	public static String alipayCallBackGiftCard = "";
//	/** 异步回调地址-物业费 **/
//	public static String alipayCallBackEstateFee = "";
//	/** 异步回调地址-供求 **/
//	public static String alipayCallBackInfoPublish = "";
//	/** 异步回调地址-双色球 **/
//	public static String alipayCallBackGameSsq = "";
//	/** 异步回调地址-快三 **/
//	public static String alipayCallBackGameK3 = "";
//
//	/** 支付信息 */
//	public final static String PAY_ALIPAY_CHARSET = "utf-8";
//	public final static String PAY_ALIPAY_SIGN_TYPE = "RSA";
//	public final static String PAY_ALIPAY_PAYMENT_TYPE = "1";
//
//	// ####################微信支付###########################
//	/** appid **/
//	public static String weChatAppid = "";
//	/** 商户号 **/
//	public static String weChatMchid = "";
//	/** key **/
//	public static String weChatKey = "";
//	/** 异步回调地址-订餐 **/
//	public static String weChatCallBackEat = "";
//	/** 异步回调地址-电商 **/
//	public static String weChatCallBackStore = "";
//	/** 异步回调地址-团购 **/
//	public static String weChatCallBackBuy = "";
//	/** 异步回调地址-在线充值 **/
//	public static String weChatCallBackRecharge = "";
//	/** 异步回调地址-礼品卡 **/
//	public static String weChatCallBackGiftCard = "";
//	/** 异步回调地址-物业费 **/
//	public static String weChatCallBackEstateFee = "";
//	/** 异步回调地址-供求信息 **/
//	public static String weChatCallBackInfoPublish = "";
//	/** 异步回调地址-双色球 **/
//	public static String weChatCallBackGameSsq = "";
//	/** 异步回调地址-快三 **/
//	public static String weChatCallBackGameK3 = "";
//	
//	// ####################银联支付###########################
//	/** 合作者id **/
//	public static String unionPartnerId = "";
//	/** 异步回调地址-订餐 **/
//	public static String unionCallBackEat = "";
//	/** 异步回调地址-电商 **/
//	public static String unionCallBackStore = "";
//	/** 异步回调地址-电商 **/
//	public static String unionCallBackBuy = "";
//	/** 异步回调地址-在线充值 **/
//	public static String unionCallBackRecharge = "";
//	/** 异步回调地址-礼品卡 **/
//	public static String unionCallBackGiftCard = "";
//	/** 异步回调地址-物业费 **/
//	public static String unionCallBackEstateFee = "";
//	/** 异步回调地址-供求信息 **/
//	public static String unionCallBackInfoPublish = "";
//	
//	/** 每天获取优惠次数 **/
//	public static String PARAM_COUPONS_COUNT = "0";
//
//	
//	public static String gameBj3OpenResultUrl="";// 北京三分彩
//	public static String gameBjPk10OpenResultUrl ="";// 北京赛车
//	public static String gameXjpLu28OpenResultUrl ="";// 幸运28
//	public static String gameCqSscOpenResultUrl ="";// 重庆时时彩
//	public static String gameBjLu28OpenResultUrl ="";// PC蛋蛋
//	public static String gameGdK10OpenResultUrl ="";// 广东快乐10分
//	public static String gameGxK10OpenResultUrl ="";// 广西快乐10分
//	public static String gameTjSscOpenResultUrl ="";// 天津时时彩
//	public static String gameXjSscOpenResultUrl ="";// 新疆时时彩
//	public static String gamePokerOpenResultUrl ="";// 快乐扑克3
//	public static String gameMarkSixOpenResultUrl ="";// 香港六合彩
//	public static String gameSsqOpenResultUrl ="";// 双色球
//	public static String gameFcOpenResultUrl ="";// 五分彩
//	public static String gameJxSscOpenResultUrl ="";// 江西时时彩
//	
//	public static String gameAhK3OpenResultUrl ="";//安徽快3
//	public static String gameBjK3OpenResultUrl ="";//北京快3
//	public static String gameFjK3OpenResultUrl ="";//福建快3
//	public static String gameGzK3OpenResultUrl ="";//贵州快3
//	public static String gameGxK3OpenResultUrl ="";//广西快3
//	public static String gameGsK3OpenResultUrl ="";//甘肃快3
//	public static String gameHubK3OpenResultUrl ="";//湖北快3
//	public static String gameHebK3OpenResultUrl ="";//河北快3
//	public static String gameHnK3OpenResultUrl ="";//河南快3
//	public static String gameJsK3OpenResultUrl ="";//江苏快3
//	public static String gameJxK3OpenResultUrl ="";//江西快3
//	public static String gameJlK3OpenResultUrl ="";//吉林快3
//	public static String gameNmgK3OpenResultUrl ="";//内蒙古快3
//	public static String gameShK3OpenResultUrl ="";//上海快3
//	
//	public static String gameBjKl8OpenResultUrl ="";//北京快乐8
//	
//	public static String gameBjPick11OpenResultUrl="";//北京11选5
//	public static String gameTjPick11OpenResultUrl="";//天津11选5
//	public static String gameHebPick11OpenResultUrl="";//河北11选5
//	public static String gameNmgPick11OpenResultUrl="";//内蒙古11选5
//	public static String gameLnPick11OpenResultUrl="";//辽宁11选5
//	public static String gameJlPick11OpenResultUrl="";//吉林11选5
//	public static String gameHljPick11OpenResultUrl="";//黑龙江11选5
//	public static String gameShPick11OpenResultUrl="";//上海11选5
//	public static String gameJsPick11OpenResultUrl="";//江苏11选5
//	public static String gameZjPick11OpenResultUrl="";//浙江11选5
//	public static String gameAhPick11OpenResultUrl="";//安徽11选5
//	public static String gameFjPick11OpenResultUrl="";//福建11选5
//	public static String gameJxPick11OpenResultUrl="";//江西11选5(11选5)
//	public static String gameHnPick11OpenResultUrl="";//河南11选5
//	public static String gameHubPick11OpenResultUrl="";//湖北11选5
//	public static String gameGdPick11OpenResultUrl="";//广东11选5
//	public static String gameGxPick11OpenResultUrl="";//广西11选5
//	public static String gameGzPick11OpenResultUrl="";//贵州11选5
//	public static String gameShxPick11OpenResultUrl="";//陕西11选5
//	public static String gameGsPick11OpenResultUrl="";//甘肃11选5
//	public static String gameXjPick11OpenResultUrl="";//新疆11选5
//	public static String gameSdPick11OpenResultUrl="";//山东11选5(十一运夺金/老11选5)
//	public static String gameSxPick11OpenResultUrl="";//山西11选5
//	public static String gameYnPick11OpenResultUrl="";//云南11选5
//
//
//	// ----------------------------------------------------------------------------
//
//	public static String gameBj3Switch="";// 北京三分彩
//	public static String gameBjPk10Switch ="";// 北京赛车
//	public static String gameXjpLu28Switch ="";// 幸运28
//	public static String gameCqSscSwitch ="";// 重庆时时彩
//	public static String gameBjLu28Switch ="";// PC蛋蛋
//	public static String gameGdK10Switch ="";// 广东快乐10分
//	public static String gameTjSscSwitch ="";// 天津时时彩
//	public static String gameXjSscSwitch ="";// 新疆时时彩
//	public static String gamePokerSwitch ="";// 快乐扑克3
//	public static String gameJsK3Switch= "";//江苏快3
//	public static String gameGxK10Switch = "";
//	public static String gameMarkSixSwitch= "";
//	public static String gameGdPick11Switch= "";//广东11选5
//	public static String gameFiveSwitch= "";//五分彩
//	public static String gameSsqSwitch= "";//双色球
//	public static String gameJxPick11Switch= "";//江西11选5
//	public static String gameJxSscSwitch= "";//江西时时彩
//	public static String gameThreeSwitch= "";//三分彩
//	public static String gameSdPick11Switch= "";//山东11选5
//	
//	public static String getAlipayPartner() {
//		return alipayPartner;
//	}
//
//	public static String getAlipayCallBackGiftCard() {
//		return alipayCallBackGiftCard;
//	}
//
//	public static String getWeChatCallBackGiftCard() {
//		return weChatCallBackGiftCard;
//	}
//
//	public static String getUnionCallBackGiftCard() {
//		return unionCallBackGiftCard;
//	}
//
//	public static String getAlipayRsaPrivate() {
//		return alipayRsaPrivate;
//	}
//
//	public static String getAlipaySellerId() {
//		return alipaySellerId;
//	}
//
//	public static String getAlipayCallBackEat() {
//		return alipayCallBackEat;
//	}
//
//	public static String getAlipayCallBackStore() {
//		return alipayCallBackStore;
//	}
//
//	public static String getAlipayCallBackBuy() {
//		return alipayCallBackBuy;
//	}
//
//	public static String getWeChatAppid() {
//		return weChatAppid;
//	}
//
//	public static String getWeChatMchid() {
//		return weChatMchid;
//	}
//
//	public static String getWeChatKey() {
//		return weChatKey;
//	}
//
//	public static String getWeChatCallBackEat() {
//		return weChatCallBackEat;
//	}
//
//	public static String getWeChatCallBackStore() {
//		return weChatCallBackStore;
//	}
//
//	public static String getWeChatCallBackBuy() {
//		return weChatCallBackBuy;
//	}
//
//	public static String getUnionPartnerId() {
//		return unionPartnerId;
//	}
//
//	public static String getUnionCallBackEat() {
//		return unionCallBackEat;
//	}
//
//	public static String getUnionCallBackStore() {
//		return unionCallBackStore;
//	}
//
//	public static String getUnionCallBackBuy() {
//		return unionCallBackBuy;
//	}
//
//	public final static String getWebappContext() {
//		return webappContext;
//	}
//
//	public final static String getWebRootPath() {
//		return webRootPath;
//	}
//
//	public final static String getFileServerFolder() {
//		return fileServerFolder;
//	}
//
//	public final static String getFileUploadPath() {
//		return fileUploadPath;
//	}
//
//	public static String getAlipayCallBackRecharge() {
//		return alipayCallBackRecharge;
//	}
//
//	public static void setAlipayCallBackRecharge(String alipayCallBackRecharge) {
//		Constants.alipayCallBackRecharge = alipayCallBackRecharge;
//	}
//
//	public static String getWeChatCallBackRecharge() {
//		return weChatCallBackRecharge;
//	}
//
//	public static void setWeChatCallBackRecharge(String weChatCallBackRecharge) {
//		Constants.weChatCallBackRecharge = weChatCallBackRecharge;
//	}
//
//	public static String getUnionCallBackRecharge() {
//		return unionCallBackRecharge;
//	}
//
//	public static void setUnionCallBackRecharge(String unionCallBackRecharge) {
//		Constants.unionCallBackRecharge = unionCallBackRecharge;
//	}
//
//	public static String getAlipayCallBackEstateFee() {
//		return alipayCallBackEstateFee;
//	}
//
//	public static String getWeChatCallBackEstateFee() {
//		return weChatCallBackEstateFee;
//	}
//
//	public static String getUnionCallBackEstateFee() {
//		return unionCallBackEstateFee;
//	}
//
//	public static String getAlipayCallBackInfoPublish() {
//		return alipayCallBackInfoPublish;
//	}
//
//	public static String getWeChatCallBackInfoPublish() {
//		return weChatCallBackInfoPublish;
//	}
//
//	public static String getUnionCallBackInfoPublish() {
//		return unionCallBackInfoPublish;
//	}
//
//	public static String getAlipayCallBackGameSsq() {
//		return alipayCallBackGameSsq;
//	}
//
//	public static String getAlipayCallBackGameK3() {
//		return alipayCallBackGameK3;
//	}
//
//	public static String getWeChatCallBackGameSsq() {
//		return weChatCallBackGameSsq;
//	}
//
//	public static String getWeChatCallBackGameK3() {
//		return weChatCallBackGameK3;
//	}
//
//	/**
//	 * 返回彩种开奖地址
//	 * @param gameType
//	 * @return
//	 */
//	public static String getGameOpenResultUrl(String gameType) {
//		if(gameType.equals(Constants.GAME_TYPE_BJ3))
//			return Constants.gameBj3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_BJPK10))
//				return Constants.gameBjPk10OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_XJPLU28))
//				return Constants.gameXjpLu28OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_BJLU28))
//				return Constants.gameBjLu28OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_SSQ))
//				return Constants.gameSsqOpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_FC))
//				return Constants.gameFcOpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_POKER))
//				return Constants.gamePokerOpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_MARKSIX))
//				return Constants.gameMarkSixOpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_GDK10))
//				return Constants.gameGdK10OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_GXK10))
//				return Constants.gameGxK10OpenResultUrl;
//		
//		//时时彩
//		else if(gameType.equals(Constants.GAME_TYPE_CQSSC))
//			return Constants.gameCqSscOpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_TJSSC))
//			return Constants.gameTjSscOpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_XJSSC))
//			return Constants.gameXjSscOpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_JXSSC))
//			return Constants.gameJxSscOpenResultUrl;
//		
//		//11选5
//		else if(gameType.equals(Constants.GAME_TYPE_BJPICK11))
//			return Constants.gameBjPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_TJPICK11))
//			return Constants.gameTjPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_HEBPICK11))
//			return Constants.gameHebPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_NMGPICK11))
//			return Constants.gameNmgPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_LNPICK11))
//			return Constants.gameLnPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_JLPICK11))
//			return Constants.gameJlPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_HLJPICK11))
//			return Constants.gameHljPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_SHPICK11))
//			return Constants.gameShPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_JSPICK11))
//			return Constants.gameJsPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_ZJPICK11))
//			return Constants.gameZjPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_AHPICK11))
//			return Constants.gameAhPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_FJPICK11))
//			return Constants.gameFjPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_JXPICK11))
//			return Constants.gameJxPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_HNPICK11))
//			return Constants.gameHnPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_HUBPICK11))
//			return Constants.gameHubPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_GDPICK11))
//			return Constants.gameGdPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_GXPICK11))
//			return Constants.gameGxPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_GZPICK11))
//			return Constants.gameGzPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_SHXPICK11))
//			return Constants.gameShxPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_GSPICK11))
//			return Constants.gameGsPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_XJPICK11))
//			return Constants.gameXjPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_SDPICK11))
//			return Constants.gameSdPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_SXPICK11))
//			return Constants.gameSxPick11OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_YNPICK11))
//			return Constants.gameYnPick11OpenResultUrl;
//		
//		
//		//快3
//		else if(gameType.equals(Constants.GAME_TYPE_AHK3))
//			return Constants.gameAhK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_BJK3))
//			return Constants.gameBjK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_FJK3))
//			return Constants.gameFjK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_GZK3))
//			return Constants.gameGzK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_GXK3))
//			return Constants.gameGxK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_GSK3))
//			return Constants.gameGsK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_HUBK3))
//			return Constants.gameHubK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_HEBK3))
//			return Constants.gameHebK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_HNK3))
//			return Constants.gameHnK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_JSK3))
//			return Constants.gameJsK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_JXK3))
//			return Constants.gameJxK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_JLK3))
//			return Constants.gameJlK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_NMGK3))
//			return Constants.gameNmgK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_SHK3))
//			return Constants.gameShK3OpenResultUrl;
//		else if(gameType.equals(Constants.GAME_TYPE_BJKL8))
//			return Constants.gameBjKl8OpenResultUrl;
////		else if(gameType.equals(Constants.GAME_TYPE))
////			return Constants.game;
//		
//		
//		return "";
//	}
//
//	public static String getGameSwitch(String gameType) {
//		if(gameType.equals(Constants.GAME_TYPE_BJ3))
//			return Constants.gameBj3Switch;
//		else if(gameType.equals(Constants.GAME_TYPE_BJPK10))
//				return Constants.gameBjPk10Switch;
//		else if(gameType.equals(Constants.GAME_TYPE_XJPLU28))
//				return Constants.gameXjpLu28Switch;
//		else if(gameType.equals(Constants.GAME_TYPE_BJLU28))
//				return Constants.gameBjLu28Switch;
//		else if(gameType.equals(Constants.GAME_TYPE_SSQ))
//				return Constants.gameSsqSwitch;
//		else if(gameType.equals(Constants.GAME_TYPE_FC))
//				return "";//Constants.gameFcSwitch;
//		else if(gameType.equals(Constants.GAME_TYPE_POKER))
//				return Constants.gamePokerSwitch;
//		else if(gameType.equals(Constants.GAME_TYPE_MARKSIX))
//				return Constants.gameMarkSixSwitch;
//		else if(gameType.equals(Constants.GAME_TYPE_GDK10))
//				return Constants.gameGdK10Switch;
//		else if(gameType.equals(Constants.GAME_TYPE_GXK10))
//				return Constants.gameGxK10Switch;
//		
//		//时时彩
//		else if(gameType.equals(Constants.GAME_TYPE_CQSSC))
//			return Constants.gameCqSscSwitch;
//		else if(gameType.equals(Constants.GAME_TYPE_TJSSC))
//			return Constants.gameTjSscSwitch;
//		else if(gameType.equals(Constants.GAME_TYPE_XJSSC))
//			return Constants.gameXjSscSwitch;
//		else if(gameType.equals(Constants.GAME_TYPE_JXSSC))
//			return Constants.gameJxSscSwitch;
//		
//		//11选5
//		else if(gameType.equals(Constants.GAME_TYPE_GDPICK11))
//			return Constants.gameGdPick11Switch;
//		else if(gameType.equals(Constants.GAME_TYPE_SDPICK11))
//			return Constants.gameSdPick11Switch;
//		else if(gameType.equals(Constants.GAME_TYPE_JXPICK11))
//			return Constants.gameJxPick11Switch;
//		
//		//快3
//		else if(gameType.equals(Constants.GAME_TYPE_JSK3))
//			return Constants.gameJsK3Switch;
//		
////		else if(gameType.equals(Constants.GAME_TYPE))
////			return Constants.game;
//		
//		
//		return "";
//	}
//
//	/**
//	 * 网站服务器地址
//	 */
//	public static String getDomainName() {
//		return domainName;
//	}
//
//	/**
//	 * 网站的文件服务器地址，如果没有则和服务器地址相同
//	 * 
//	 * @return
//	 */
//	public static String getDomainNameFile() {
//		return domainNameFile;
//	}
//
//	static {
//		reload();
//	}
//
//	public static void reload() {
//		IServiceLocator service = ServiceLocatorImpl.getInstance();
//		IReadProperties read = (IReadProperties) service
//				.getService("readProperties");
//		webappContext = read.getValue("SYSTEM.WEBSERVER.WEBCONTEXT");
//		webRootPath = read.getValue("SYSTEM.WEBROOT.PATH");
//		fileServerFolder = read.getValue("FILEUPLOAD.FOLDER");
//		fileUploadPath = read.getValue("FILEUPLOAD.PATH");
//		serverUrl = read.getValue("SYSTEM.SERVER.URL");
//		domainName = read.getValue("DOMAIN_NAME");
//		domainNameFile = read.getValue("DOMAIN_NAME_FILE");
//		alipayPartner = read.getValue("ALIPAY.PARTNER");
//		alipayRsaPrivate = read.getValue("ALIPAY.RSA.PRIVATE");
//		alipaySellerId = read.getValue("ALIPAY.SELLER.ID");
//		alipayCallBackEat = read.getValue("ALIPAY.CALLBACK.EAT");
//		alipayCallBackStore = read.getValue("ALIPAY.CALLBACK.STORE");
//		alipayCallBackBuy = read.getValue("ALIPAY.CALLBACK.BUY");
//		alipayCallBackRecharge = read.getValue("ALIPAY.CALLBACK.RECHARGE");
//		alipayCallBackGiftCard = read.getValue("ALIPAY.CALLBACK.GIFTCARD");
//		alipayCallBackEstateFee = read.getValue("ALIPAY.CALLBACK.ESTATEFEE");
//		alipayCallBackInfoPublish = read.getValue("ALIPAY.CALLBACK.INFOPUBLISH");
//		alipayCallBackGameSsq = read.getValue("ALIPAY.CALLBACK.GAMESSQ");
//		alipayCallBackGameK3 = read.getValue("ALIPAY.CALLBACK.GAMEK3");
//		
//		
//		weChatAppid = read.getValue("WECHAT.APPID");
//		weChatMchid = read.getValue("WECHAT.MCH.ID");
//		weChatKey = read.getValue("WECHAT.KEY");
//		weChatCallBackEat = read.getValue("WECHAT.CALLBACK.EAT");
//		weChatCallBackStore = read.getValue("WECHAT.CALLBACK.STORE");
//		weChatCallBackBuy = read.getValue("WECHAT.CALLBACK.BUY");
//		weChatCallBackRecharge = read.getValue("WECHAT.CALLBACK.RECHARGE");
//		weChatCallBackGiftCard = read.getValue("WECHAT.CALLBACK.GIFTCARD");
//		weChatCallBackEstateFee = read.getValue("WECHAT.CALLBACK.ESTATEFEE");
//		weChatCallBackInfoPublish = read.getValue("WECHAT.CALLBACK.INFOPUBLISH");
//		weChatCallBackGameSsq = read.getValue("WECHAT.CALLBACK.GAMESSQ");
//		weChatCallBackGameK3 = read.getValue("WECHAT.CALLBACK.GAMEK3");
//		
//		unionPartnerId = read.getValue("UNION.PARTNER.ID");
//		unionCallBackEat = read.getValue("UNION.CALLBACK.EAT");
//		unionCallBackStore = read.getValue("UNION.CALLBACK.STORE");
//		unionCallBackBuy = read.getValue("UNION.CALLBACK.BUY");
//		unionCallBackRecharge = read.getValue("UNION.CALLBACK.RECHARGE");
//		unionCallBackGiftCard = read.getValue("UNION.CALLBACK.GIFTCARD");
//		unionCallBackEstateFee = read.getValue("UNION.CALLBACK.ESTATEFEE");
//		unionCallBackInfoPublish = read.getValue("UNION.CALLBACK.INFOPUBLISH");
//		
//		POINTS_CHARGE_THIRD_PAY_SWITCH = read.getValue("POINTS.CHARGE.THIRD.PAY.SWITCH");
//		POINTS_USER_THIRD_INTRODUCTION_SWITCH= read.getValue("POINTS.USER.THIRD.INTRODUCTION.SWITCH");
//		POINTS_USER_THIRD_INTRODUCTION_CONTENT= read.getValue("POINTS.USER.THIRD.INTRODUCTION.CONTENT");
//		
//		//PLAT_RUN_MODE_ID = read.getValue("SYSTEM.PLAT.RUN.MODE");//平台运行模式标识
//		
//		IOS_REVIEW_VISION = read
//				.getValue("ISO.REVIEW.VISION");
//		
//		gameBj3OpenResultUrl = read.getValue("GAME.BJ3.OPENRESULT.URL");
//		gameBjPk10OpenResultUrl= read.getValue("GAME.BJPK10.OPENRESULT.URL");
//		gameXjpLu28OpenResultUrl= read.getValue("GAME.XJPLU28.OPENRESULT.URL");
//		gameCqSscOpenResultUrl = read.getValue("GAME.CQSSC.OPENRESULT.URL");
//		gameBjLu28OpenResultUrl= read.getValue("GAME.BJLU28.OPENRESULT.URL");
//		gameGdK10OpenResultUrl= read.getValue("GAME.GDK10.OPENRESULT.URL");
//		gameGxK10OpenResultUrl= read.getValue("GAME.GXK10.OPENRESULT.URL");
//		
//		gameXjSscOpenResultUrl = read.getValue("GAME.XJSSC.OPENRESULT.URL");
//		gameTjSscOpenResultUrl = read.getValue("GAME.TJSSC.OPENRESULT.URL");
//		gamePokerOpenResultUrl = read.getValue("GAME.POKER.OPENRESULT.URL");
//		gameSsqOpenResultUrl = read.getValue("GAME.SSQ.OPENRESULT.URL");
//		gameMarkSixOpenResultUrl=read.getValue("GAME.MARKSIX.OPENRESULT.URL");
//		gameJxSscOpenResultUrl=read.getValue("GAME.JXSSC.OPENRESULT.URL");
//		
//		gameAhK3OpenResultUrl=read.getValue("GAME.AHK3.OPENRESULT.URL");
//		gameBjK3OpenResultUrl=read.getValue("GAME.BJK3.OPENRESULT.URL");
//		gameFjK3OpenResultUrl=read.getValue("GAME.FJK3.OPENRESULT.URL");
//		gameGzK3OpenResultUrl=read.getValue("GAME.GZK3.OPENRESULT.URL");
//		gameGxK3OpenResultUrl=read.getValue("GAME.GXK3.OPENRESULT.URL");
//		gameGsK3OpenResultUrl=read.getValue("GAME.GSK3.OPENRESULT.URL");
//		gameHubK3OpenResultUrl=read.getValue("GAME.HUBK3.OPENRESULT.URL");
//		gameHebK3OpenResultUrl=read.getValue("GAME.HEBK3.OPENRESULT.URL");
//		gameHnK3OpenResultUrl=read.getValue("GAME.HNK3.OPENRESULT.URL");
//		gameJsK3OpenResultUrl=read.getValue("GAME.JSK3.OPENRESULT.URL");
//		gameJxK3OpenResultUrl=read.getValue("GAME.JXK3.OPENRESULT.URL");
//		gameJlK3OpenResultUrl=read.getValue("GAME.JLK3.OPENRESULT.URL");
//		gameNmgK3OpenResultUrl=read.getValue("GAME.NMGK3.OPENRESULT.URL");
//		gameShK3OpenResultUrl=read.getValue("GAME.SHK3.OPENRESULT.URL");
//		
//		gameBjKl8OpenResultUrl=read.getValue("GAME.BJKL8.OPENRESULT.URL");
//
//		gameBjPick11OpenResultUrl=read.getValue("GAME.BJPICK11.OPENRESULT.URL");
//		gameTjPick11OpenResultUrl=read.getValue("GAME.TJPICK11.OPENRESULT.URL");
//		gameHebPick11OpenResultUrl=read.getValue("GAME.HEBPICK11.OPENRESULT.URL");
//		gameNmgPick11OpenResultUrl=read.getValue("GAME.NMGPICK11.OPENRESULT.URL");
//		gameLnPick11OpenResultUrl=read.getValue("GAME.LNPICK11.OPENRESULT.URL");
//		gameJlPick11OpenResultUrl=read.getValue("GAME.JLPICK11.OPENRESULT.URL");
//		gameHljPick11OpenResultUrl=read.getValue("GAME.HLJPICK11.OPENRESULT.URL");
//		gameShPick11OpenResultUrl=read.getValue("GAME.SHPICK11.OPENRESULT.URL");
//		gameJsPick11OpenResultUrl=read.getValue("GAME.JSPICK11.OPENRESULT.URL");
//		gameZjPick11OpenResultUrl=read.getValue("GAME.ZJPICK11.OPENRESULT.URL");
//		gameAhPick11OpenResultUrl=read.getValue("GAME.AHPICK11.OPENRESULT.URL");
//		gameFjPick11OpenResultUrl=read.getValue("GAME.FJPICK11.OPENRESULT.URL");
//		gameJxPick11OpenResultUrl=read.getValue("GAME.JXPICK11.OPENRESULT.URL");
//		gameHnPick11OpenResultUrl=read.getValue("GAME.HNPICK11.OPENRESULT.URL");
//		gameHubPick11OpenResultUrl=read.getValue("GAME.HUBPICK11.OPENRESULT.URL");
//		gameGdPick11OpenResultUrl=read.getValue("GAME.GDPICK11.OPENRESULT.URL");
//		gameGxPick11OpenResultUrl=read.getValue("GAME.GXPICK11.OPENRESULT.URL");
//		gameGzPick11OpenResultUrl=read.getValue("GAME.GZPICK11.OPENRESULT.URL");
//		gameShxPick11OpenResultUrl=read.getValue("GAME.SHXPICK11.OPENRESULT.URL");
//		gameGsPick11OpenResultUrl=read.getValue("GAME.GSPICK11.OPENRESULT.URL");
//		gameXjPick11OpenResultUrl=read.getValue("GAME.XJPICK11.OPENRESULT.URL");
//		gameSdPick11OpenResultUrl=read.getValue("GAME.SDPICK11.OPENRESULT.URL");
//		gameSxPick11OpenResultUrl=read.getValue("GAME.SXPICK11.OPENRESULT.URL");
//		gameYnPick11OpenResultUrl=read.getValue("GAME.YNPICK11.OPENRESULT.URL");
//		
//		
//		gameBj3Switch= read.getValue("GAME.BJ3.SWITCH");
//		gameBjPk10Switch= read.getValue("GAME.BJPK10.SWITCH");
//		gameXjpLu28Switch= read.getValue("GAME.XJPLU28.SWITCH");
//		gameCqSscSwitch = read.getValue("GAME.CQSSC.SWITCH");
//		gameBjLu28Switch= read.getValue("GAME.BJLU28.SWITCH");
//		gameGdK10Switch= read.getValue("GAME.GDK10.SWITCH");
//		
//		gameXjSscSwitch= read.getValue("GAME.XJSSC.SWITCH");
//		gameTjSscSwitch = read.getValue("GAME.TJSSC.SWITCH");
//		gamePokerSwitch= read.getValue("GAME.POKER.SWITCH");
//		
//		gameJsK3Switch= read.getValue("GAME.JSK3.SWITCH");
//		gameGxK10Switch = read.getValue("GAME.GXK10.SWITCH");
//		gameMarkSixSwitch= read.getValue("GAME.MARKSIX.SWITCH");
//		gameGdPick11Switch= read.getValue("GAME.GDPICK11.SWITCH");
//		gameJxPick11Switch= read.getValue("GAME.JXPICK11.SWITCH");
//		gameFiveSwitch= read.getValue("GAME.FIVE.SWITCH");
//		gameThreeSwitch= read.getValue("GAME.THREE.SWITCH");
//		gameJxSscSwitch = read.getValue("GAME.JXSSC.SWITCH");
//		gameSsqSwitch = read.getValue("GAME.SSQ.SWITCH");
//		gameSdPick11Switch= read.getValue("GAME.SDPICK11.SWITCH");
//		
//		gameTimerOpen = read.getValue("GAME.TIMER.OPEN");
//		
//		//加载系统功能配置信息
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
//		
//		SMS_SEND_URL=read.getValue("SMS.SEND.URL");
//		SMS_CALLBACK_URL=read.getValue("SMS.CALLBACK.URL");
//		SMS_ECECCID=read.getValue("SMS.ECECCID");
//		SMS_PASSWORD=read.getValue("SMS.PASSWORD");
//		SMS_LONG_CODE=read.getValue("SMS.LONG.CODE");
//		SMS_MSG_TYPE=read.getValue("SMS.MSG.TYPE");
//		
//		SMS_SEND_URL_IPYY=read.getValue("SMS.SEND.URL.IPYY");
//		SMS_ECECCID_IPYY=read.getValue("SMS.ECECCID.IPYY");
//		SMS_PASSWORD_IPYY=read.getValue("SMS.PASSWORD.IPYY");
//		SMS_CALLBACK_URL_IPYY=read.getValue("SMS.CALLBACK.URL.IPYY");
//		
//		SMS_MSG_SIGNATURE=read.getValue("SMS.MSG.SIGNATURE");
//		SMS_MSG_REG=read.getValue("SMS.MSG.REG");
//		SMS_MSG_REREG=read.getValue("SMS.MSG.REREG");
//		SMS_MSG_COUPON=read.getValue("SMS.MSG.COUPON");
//		SMS_MSG_COUPON_EXCHANGE=read.getValue("SMS.MSG.COUPON.EXCHANGE");
//		
//		IOS_DEPLOY_MODE=read.getValue("IOS.DEPLOY.MODE");
//		IOS_PUBLISH_MODE=read.getValue("IOS.PUBLISH.MODE");
//		
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
//		
//		msgTime = read.getValue("MSG.TIME");
//		msgMaxCount = read.getValue("MSG.MAX.COUNT");
//	}
//
//
//	public static String getJPUSH_APP_KEY() {
//		return JPUSH_APP_KEY;
//	}
//
//	public static String getJPUSH_MASTER_SECRET() {
//		return JPUSH_MASTER_SECRET;
//	}
//
//	
//	public static String getGameTimerOpen() {
//		return gameTimerOpen;
//	}
//
//	public static void setGameTimerOpen(String gameTimerOpen) {
//		Constants.gameTimerOpen = gameTimerOpen;
//	}
//
//	public static String getServerUrl() {
//		return Constants.serverUrl;
//	}
//	
//	public static String getMsgMaxCount() {
//		return msgMaxCount;
//	}
//
//	public static String getMsgTime() {
//		return msgTime;
//	}
//
//	public static String getIOS_REVIEW_VISION() {
//		return IOS_REVIEW_VISION;
//	}
//	
//	/**
//	 * 返回系统定义的功能是否打开或关闭
//	 * @param key = system.func.[模块或功能英文名称]
//	 * @return Boolean
//	 */
//	public final static Boolean chkFuncConfig(String key){
//		boolean af = false;
//		if(SysFuncConfig==null){
//			af = true;
//		}else{
//			af = SysFuncConfig.get(key)!=null && SysFuncConfig.get(key).equals("0")?false:true;
//		}
//		return af;
//	}
//
//	/**
//	 * 返回用户默认头像
//	 * 
//	 * @param gender
//	 * @param sizeType
//	 * @return
//	 */
//	public final static String getUserDefaultPicture(String gender,
//			String sizeType, String url) {
//		if (url != null && url.length() > 1)
//			return url;
//		if (gender == null || gender.length() <= 0)
//			gender = "1";
//		String size = "50";
//		if (sizeType.equals("picture"))
//			size = "50";
//		else if (sizeType.equals("logo"))
//			size = "120";
//		else if (sizeType.equals("logoMini"))
//			size = "20";
//		else
//			size = "50";
//		return "/images/ui/p" + size + "_" + gender + ".gif";
//	}
//	public static Map<String,String> blacklist=new HashMap<String, String>();
//	public static Map<String,String> getBlacklist(){		
//		return blacklist;
//	};
//	public static Map<String,SMSCache> datelist=new HashMap<String, SMSCache>();
//	public static Map<String,SMSCache> getIPlist(){
//		return datelist;
//	}
//	
///** 
//* 用户和Session绑定关系 
//*/  
//public static   Map<String, HttpSession> USER_SESSION=new HashMap<String, HttpSession>(); 
//public static  Map<String,HttpSession> getUserSession(){		
//	return USER_SESSION;
//};
//
//
///** 
//* seeionId和用户的绑定关系 
//*/  
//public static   Map<String, String> SESSIONID_USER=new HashMap<String, String>();  
//public static  Map<String,String> getSessionIdUser(){
//	return SESSIONID_USER;
//};
//
//
//	//系统定时器配置
//	private final static Map<String,String> TIMER_CONFIG_MAP=new HashMap<String, String>();//定时配置
//	private final static void loadTimerConfig(){
//		//配置详情
//		String timerData = ManageFile.loadTextFileUTF8(webRootPath+"/WEB-INF/classes/timer.properties");
//		String[] timerArr = timerData.split("\n");
//		for(int i=0;i<timerArr.length;i++){
//			String confs = timerArr[i];
//			if(confs!=null && confs.trim().length()>0 && !confs.startsWith("#")){
//				String[] confArr = confs.split("=");
//				TIMER_CONFIG_MAP.put(confArr[0].trim(), confArr[1].trim());
//			}
//		}
//	}
//
//	/**
//	 * 获取定时开关方法
//	 * @param timerCode
//	 * @return true or false
//	 */
//	public final static boolean getTimerOpen(String timerCode){
//		String tf = TIMER_CONFIG_MAP.get(timerCode);
//		if(tf==null || tf=="") tf = "false";
//		return tf.equals("true")?true:false;
//	}
//}
