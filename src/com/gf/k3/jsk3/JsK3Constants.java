package com.gf.k3.jsk3;

public class JsK3Constants {

	public final static String JS_K3_START_TIME_STR=" 08:30:00";//游戏开始时间第一期
	public final static Integer JS_K3_MAX_PART=82;//最大场次
	public final static Integer JS_K3_TIME_INTERVAL=10;//场次间隔分钟
	
	public final static Integer JSK3_TIME_OPENING_FREEZE=60;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖
	
	public final static String JS_K3_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String JS_K3_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String JS_K3_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	
	public final static Integer GDPICK11_MAX_BET_POINT = 10000; // 单注投注最大值
	public final static Integer TOTAL_MAX_BET_POINT=50000; //单期投注总值最大为5万。
	
	
	/** 游戏玩法0-猜一个号就中奖 **/
	public final static String GAME_TYPE_GF_YIGEHAO = "0";
	/** 游戏玩法1-二不同号 **/
	public final static String GAME_TYPE_GF_ERBUTONG = "1";
	/** 游戏玩法2-二同号单选 **/
	public final static String GAME_TYPE_GF_ERTONGDAN = "2";
	/** 游戏玩法3-二同号复选 **/
	public final static String GAME_TYPE_GF_ERTONGFU = "3";
	/** 游戏玩法4-三连号通选 **/
	public final static String GAME_TYPE_GF_SANLIANTONG = "4";
	/** 游戏玩法5-三不同号 **/
	public final static String GAME_TYPE_GF_SANBUTONG = "5";
	/** 游戏玩法6-三同号单选 **/
	public final static String GAME_TYPE_GF_SANTONGDAN = "6";
	/** 游戏玩法7-三同号通选 **/
	public final static String GAME_TYPE_GF_SANTONGTONG = "7";
	/** 游戏玩法8-和值 **/
	public final static String GAME_TYPE_GF_HEZHI = "8";

}
