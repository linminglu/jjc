package com.gf.pick11.shpick11;

public class ShPick11Constants {

	public final static String SH_PICK11_START_TIME_STR=" 09:00:00";//游戏开始时间第一期
	public final static Integer SH_PICK11_MAX_PART=84;//最大场次
	public final static Integer SH_PICK11_TIME_INTERVAL=10;//场次间隔分钟
	
	public final static Integer SH_PICK11_TIME_OPENING_FREEZE=60;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖
	
	public final static String SH_PICK11_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String SH_PICK11_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String SH_PICK11_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	
	public final static String SH_PICK11_WIN_INIT = "0";//0=未开奖 1=中奖  2=未中奖
	public final static String SH_PICK11_WIN = "1";//0=未开奖 1=中奖  2=未中奖
	public final static String SH_PICK11_WIN_NOT = "2";//0=未开奖 1=中奖  2=未中奖
	public final static String SH_PICK11_WIN_HE = "3";//3=和
	
	public final static Integer SHPICK11_MAX_BET_POINT = 10000; // 单注投注最大值
	public final static Integer TOTAL_MAX_BET_POINT=50000; //单期投注总值最大为5万。
	
	
	/**游戏玩法0-前三直选**/
	public final static String GAME_TYPE_GF_QIANSANZHI="0";
	/**游戏玩法1-前三组选**/
	public final static String GAME_TYPE_GF_QIANSANZU="1";
	/**游戏玩法2-前二直选**/
	public final static String GAME_TYPE_GF_QIANERZHI="2";
	/**游戏玩法3-前二组选**/
	public final static String GAME_TYPE_GF_QIANERZU="3";
	/**游戏玩法4-前一**/
	public final static String GAME_TYPE_GF_QIANYI="4";
	/**游戏玩法5-任八**/
	public final static String GAME_TYPE_GF_RENBA="5";
	/**游戏玩法6-任七**/
	public final static String GAME_TYPE_GF_RENQI="6";
	/**游戏玩法7-任六**/
	public final static String GAME_TYPE_GF_RENLIU="7";
	/**游戏玩法8-任五**/
	public final static String GAME_TYPE_GF_RENWU="8";
	/**游戏玩法9-任四**/
	public final static String GAME_TYPE_GF_RENSI="9";
	/**游戏玩法10-任三**/
	public final static String GAME_TYPE_GF_RENSAN="10";
	/**游戏玩法11-任二**/
	public final static String GAME_TYPE_GF_RENER="11";
	
	

}
