package com.gf.xyft;

public class XyFtConstants {

	public final static String XY_FT_START_TIME_STR=" 13:04:00";//游戏开始时间第一期
	public final static Integer XY_FT_MAX_PART=180;//最大场次
	public final static Integer XY_FT_TIME_INTERVAL=300;//场次间隔180秒
	
	public final static Integer XY_FT_TIME_OPENING_FREEZE = 18;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static String XY_FT_OPEN_STATUS_INIT = "0";//0=未开奖 
	public final static String XY_FT_OPEN_STATUS_OPENING = "1";//1=开奖中
	public final static String XY_FT_OPEN_STATUS_OPENED = "2";//2=已开奖
	
	public final static Integer XY_FT_MAX_BET_POINT = 100000; //单注投注最大值1万。
	public final static Integer TOTAL_MAX_BET_POINT=50000; //单期投注总值最大为5万。
	
	
	/** 游戏玩法0-单双(季军) **/
	public final static String GAME_TYPE_GF_DAN_SHUANG_JI = "0";
	/** 游戏玩法1-单双(亚军) **/
	public final static String GAME_TYPE_GF_DAN_SHUANG_YA = "1";
	/** 游戏玩法2-单双(冠军) **/
	public final static String GAME_TYPE_GF_DAN_SHUANG_GUAN = "2";
	/** 游戏玩法3-大小(季军) **/
	public final static String GAME_TYPE_GF_DAXI_AO_JI = "3";
	/** 游戏玩法4-大小(亚军) **/
	public final static String GAME_TYPE_GF_DAXI_AO_YA = "4";
	/** 游戏玩法5-大小(冠军) **/
	public final static String GAME_TYPE_GF_DAXI_AO_GUAN = "5";
	/** 游戏玩法6-定位胆(6-10名) **/
	public final static String GAME_TYPE_GF_DING_WEI_DAN_6_10 = "6";
	/** 游戏玩法7-定位胆(1-5名) **/
	public final static String GAME_TYPE_GF_DING_WEI_DAN_1_5 = "7";
	/** 游戏玩法8-前三 **/
	public final static String GAME_TYPE_GF_QIAN_SAN = "8";
	/** 游戏玩法9-前二 **/
	public final static String GAME_TYPE_GF_QIAN_ER = "9";
	/** 游戏玩法10-前一 **/
	public final static String GAME_TYPE_GF_QIAN_YI = "10";

}
