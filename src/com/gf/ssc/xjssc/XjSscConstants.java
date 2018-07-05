package com.gf.ssc.xjssc;

public class XjSscConstants {

	public final static String XJ_SSC_START_TIME_STR=" 10:00:00";//游戏开始时间第一期
	public final static Integer XJ_SSC_MAX_PART=96;//最大场次
	public final static Integer XJ_SSC_TIME_INTERVAL_DAY=10;//场次间隔分钟
	public final static Integer XJ_SSC_TIME_OPENING_FREEZE = 60;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖
	
	public final static String XJ_SSC_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String XJ_SSC_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String XJ_SSC_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static String XJ_SSC_PLAY_TYPE_TWO_FACE = "0";//0=两面盘 1=1-10名  2=冠亚军和
	public final static String XJ_SSC_PLAY_TYPE_ONE_TO_TEN = "1";//0=两面盘 1=1-10名  2=冠亚军和
	public final static String XJ_SSC_PLAY_TYPE_SUM = "2";//0=两面盘 1=1-10名  2=冠亚军和
	
	public final static String XJ_SSC_WIN_INIT = "0";//0=未开奖 1=中奖  2=未中奖
	public final static String XJ_SSC_WIN = "1";//0=未开奖 1=中奖  2=未中奖
	public final static String XJ_SSC_WIN_NOT = "2";//0=未开奖 1=中奖  2=未中奖
	public final static String XJ_SSC_WIN_HE = "3";//0=未开奖 1=中奖  2=未中奖   3=打和
	
	public final static Integer SINGLE_NOTE_MAX_BET_POINT = 10000; //单注投注最大值1万。
	public final static Integer TOTAL_MAX_BET_POINT=50000; //单期投注总值最大为5万。

	public final static String PLAY_TYPE_ZERO = "0";//大小单双
	public final static String PLAY_TYPE_ONE = "1";//一星
	public final static String PLAY_TYPE_TWO = "2";//二星组选
	public final static String PLAY_TYPE_THREE = "3";//二星直选
	public final static String PLAY_TYPE_FOUR = "4";//三星组六
	public final static String PLAY_TYPE_FIVE = "5";//三星组三
	public final static String PLAY_TYPE_SIX = "6";//三星直选
	public final static String PLAY_TYPE_SEVEN = "7";//五星直选
	public final static String PLAY_TYPE_EIGHT = "8";//五星通选

	public final static Integer FC_MAX_BET_POINT = 10000; //单注投注最大值
	public final static Integer FC_MAX_TOTAL_BET_POINT = 50000; //单期投注最大值五万
	
	public final static String GAME_NAME="新疆时时彩";

}
