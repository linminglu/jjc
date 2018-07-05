package com.gf.fivecolor;

public class FiveConstants {


//	public final static String common = "cpworld";
//		
//	public final static String FC_SESSION_REF_DATE="2017-01-01 00:00:00";
//	public final static Integer FC_SESSION_NO_REF = 0;//没增加一天就+1

	//配置场次及时间间隔
	public final static Integer FC_TIME_OPENING_FREEZE=65000;//[毫秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static Integer FC_TIME_INTERVAL=5*60;//场次间隔分钟
	public final static Integer FC_MAX_PART=288;//最大场次
//	public final static String FC_START_TIME_END=" 00:00:00";//开始时间后缀
//	public final static String FC_END_TIME_END=" 21:30:00";//开始时间后缀
//	public final static String FC_START_TIME_S=" 08:22:00";//游戏开始时间点
//	public final static String FC_START_TIME_STR=" 09:05:00";//游戏开始时间第一期
//	public final static Integer FC_END_TIME_HOUR=21;//游戏游戏结束小时
//	public final static Integer FC_END_TIME_MINUTE=50;//游戏结束小时
	
	

	public final static String FC_OPEN_STATUS_INIT = "0";
	public final static String FC_OPEN_STATUS_OPENING = "1";
	public final static String FC_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖

	
	public final static String FC_WIN = "1";//1=中奖
	public final static String FC_WIN_NO = "2";//2=未中奖
	public final static String FC_WIN_HE = "3";//3=打和
	public final static String UNFINISHED = "4";//未完成
	public final static String FC_INIT = "5";//5=未开奖 
	public final static String FC_OPENING = "6";//6=开奖中
	public final static String INVALID_REFUND = "7";//投注无效。退款

	
	public final static String PROCUREMENT_SERVICE = "0";//代购
	public final static String SPONSOR = "1";//合买
//	public final static String PARTICIPATOR = "2";//参与合买
	
	public final static String NOT_ADD_NO = "0";//不追号
	public final static String ADD_NO = "1";//追号
	
	public final static String NOT_GUARANTEE = "0";//不保底
	public final static String GUARANTEE = "1";//保底
	
	public final static String WIN_STOP = "0";//中奖就停止追号
	public final static String WIN_NO_STOP = "1";//中奖继续追号
	
//	public final static String RED_BALL = "0";//红球
//	public final static String BLUE_BALL = "1";//蓝球
//	
//	public final static Integer SIX_PRICE = 5;//六等奖赔率
//	public final static Integer FIVE_PRICE = 10;//五等奖赔率
//	public final static Integer FOUR_PRICE = 200;//四等奖赔率
//	public final static Integer THREE_PRICE = 1000;//三等奖赔率
//	public final static Integer TWO_PRICE = 10000;//二等奖赔率
//	public final static Integer ONE_PRICE = 100000;//一等奖赔率
//	
//	public final static Integer SIX = 6;//六等奖
//	public final static Integer FIVE = 5;//五等奖
//	public final static Integer FOUR = 4;//四等奖
//	public final static Integer THREE = 3;//三等奖
//	public final static Integer TWO = 2;//二等奖
//	public final static Integer ONE = 1;//一等奖
	public final static String PLAY_TYPE_ZERO = "0";//大小单双
	public final static String PLAY_TYPE_ONE = "1";//一星
	public final static String PLAY_TYPE_TWO = "2";//二星组选
	public final static String PLAY_TYPE_THREE = "3";//二星直选
	public final static String PLAY_TYPE_FOUR = "4";//三星组六
	public final static String PLAY_TYPE_FIVE = "5";//三星组三
	public final static String PLAY_TYPE_SIX = "6";//三星直选
	public final static String PLAY_TYPE_SEVEN = "7";//五星直选
	public final static String PLAY_TYPE_EIGHT = "8";//五星通选

	public final static long FC_BET_END_INTERVAL = 60;//投注截止时间到开奖时间间隔58秒。
	
	public final static Integer FC_MAX_BET_POINT = 10000; //单注投注最大值
	public final static Integer FC_MAX_TOTAL_BET_POINT = 50000; //单期投注最大值五万
	
	public final static String GAME_NAME="五分彩";

}
