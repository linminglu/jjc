package com.gf.three;

public class ThreeConstants {

	//配置场次及时间间隔
	public final static Integer FC_TIME_OPENING_FREEZE=65000;//[毫秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static Integer FC_TIME_INTERVAL=3*60;//场次间隔分钟
	public final static Integer FC_MAX_PART=480;//最大场次


	public final static String PLAY_TYPE_ZERO = "0";//大小单双
	public final static String PLAY_TYPE_ONE = "1";//一星
	public final static String PLAY_TYPE_TWO = "2";//二星组选
	public final static String PLAY_TYPE_THREE = "3";//二星直选
	public final static String PLAY_TYPE_FOUR = "4";//三星组六
	public final static String PLAY_TYPE_FIVE = "5";//三星组三
	public final static String PLAY_TYPE_SIX = "6";//三星直选
	public final static String PLAY_TYPE_SEVEN = "7";//五星直选
	public final static String PLAY_TYPE_EIGHT = "8";//五星通选

	public final static Integer Three_TIME_OPENING_FREEZE=30;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖
	
	public final static Integer FC_MAX_BET_POINT = 10000; //单注投注最大值
	public final static Integer FC_MAX_TOTAL_BET_POINT = 50000; //单期投注最大值五万
	
	public final static String GAME_NAME="三分彩";

}
