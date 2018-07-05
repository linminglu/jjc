package com.game;

import com.framework.util.StringUtil;



/**
 * 游戏模块常量类
 * @author Administrator
 */
public final class GameConstants {

	public final static String common = "cpworld";
	
	public final static String STATUS_0 = "0";//全局无效
	public final static String STATUS_1  = "1";//全局有效
	
	public final static Integer DEF_ID = 0;//默认ID 0
	public final static Integer DEF_NUMBER = 0;//默认数字 0
	
	public final static String OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static Integer BJ3_MAX_PART=420; // 最大场次
    public final static Integer BJ3_EVENING_PART=120; // 夜场场次
	public final static Integer BJ3_PAUSE_PART=3; // 游戏暂停时间3小时  早上6点到9点
	public final static String BJ3_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中(正在开奖)  2=已开奖
	public final static Integer BJ3_TIME_INTERVAL=3; // 场次间隔时间
	public final static String BJ3_EVENING_END_TIME=" 06:00:00"; // 夜场结束时间
	public final static String BJ3_NORMAL_START_TIME=" 09:00:00"; // 正常开始时间
	public final static String BJ3_WIN = "1";//1=中奖 2=不中 3=打和
	public final static String BJ3_WIN_NOT = "2";//1=中奖 2=不中 3=打和
	public final static String BJ3_WIN_HE = "3";//1=中奖  2=未中奖   3=打和
	
	public final static String WIN = "1";//1=中奖 2=不中 3=打和
	public final static String WIN_NOT = "2";//1=中奖 2=不中 3=打和
	public final static String WIN_HE = "3";//1=中奖  2=未中奖   3=打和
	public final static String WIN_BACK = "4";//撤单
	
	//---gf------
	public final static String PROCUREMENT_SERVICE = "0";//代购
	public final static String SPONSOR = "1";//合买
//	public final static String PARTICIPATOR = "2";//参与合买
	
	//----------------k10-------------常量配置--------------------
	
	//初始场次号 2015-08-25 230
	//2015230001 - 201523050
	public final static String K10_SESSION_REF_DATE="2015-08-25 00:00:00";
	public final static Integer K10_SESSION_NO_REF = 230;//第增加一天就+1

	
	public final static Integer K3_TIME_ADD=2;//场次提前两分钟
	//配置场次及时间间隔
	public final static Integer K10_TIME_OPENING_FREEZE=65000;//[毫秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static Integer K10_TIME_INTERVAL=15;//场次间隔分钟
	public final static Integer K10_MAX_PART=50;//最大场次
	public final static String K10_START_TIME_END=" 00:00:00";//开始时间后缀
	public final static String K10_END_TIME_END=" 21:30:00";//开始时间后缀
	public final static String K10_START_TIME_S=" 09:00:00";//游戏开始时间点
	public final static String K10_START_TIME_STR=" 09:10:00";//游戏开始时间第一期
	public final static Integer K10_END_TIME_HOUR=21;//游戏游戏结束小时
	public final static Integer K10_END_TIME_MINUTE=50;//游戏结束小时
	public final static Integer K10_START_TIME_NUMBER=90000;//开始时间数字化
	public final static Integer K10_END_TIME_NUMBER=213000;//开始时间数字化
	public final static Integer K10_END_TIME_NUMBER_MAX=235959;//开始时间数字化
	
	public final static Integer K10_MIN_UNIT_POINT = 2;//最小单位购买积分
	
	public final static Integer K10_MAX_NUMBER=10;//最大数字(不含)
	public final static Integer K10_MAX_HOME_BET_SESSIONS=12;//首页最大显示场数
	
	public final static Integer TOTAL_BET_FIVE_THOUSAND=5000;
	public final static Integer BJ3_MAX_BET_POINT=10000;
	public final static String BJ3 = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String BJ3_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String BJ3_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	//------------------------------------k10 end---------------------
	
	//------------------------------------双色球 常量配置--------------------
	public final static Integer SSQ_MAX_PART=153;//最大场次
	
	public final static String SSQ_BET_TYPE_DANSHI = "0";//单式
	public final static String SSQ_BET_TYPE_FUSHI = "1";//复式
	public final static String SSQ_BET_TYPE_DANTUO = "2";//胆拖
	
	public final static String SSQ_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String SSQ_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String SSQ_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static String SSQ_WIN_STATUS_INIT = "0";//0=未开奖 
	public final static String SSQ_WIN_STATUS_YES = "1";//1=中奖  
	public final static String SSQ_WIN_STATUS_NO = "2";//2=未中奖
	
	//------------------------------------双色球 end---------------------
	
	//----------------k3-------------常量配置--------------------
	
	//初始场次号 2015-08-25 230
	//2015230001 - 201523050
	public final static String K3_SESSION_REF_DATE="2015-08-25 00:00:00";
	public final static Integer K3_SESSION_NO_REF = 230;//第增加一天就+1

	//配置场次及时间间隔
	public final static Integer K3_TIME_OPENING_FREEZE=65000;//[毫秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static Integer K3_TIME_INTERVAL=10;//场次间隔分钟
	public final static Integer K3_MAX_PART=80;//最大场次
	public final static String K3_START_TIME_END=" 00:00:00";//开始时间后缀
	public final static String K3_END_TIME_END=" 21:30:00";//开始时间后缀
	public final static String K3_START_TIME_S=" 09:00:00";//游戏开始时间点
	public final static String K3_START_TIME_STR=" 09:10:00";//游戏开始时间第一期
	public final static Integer K3_END_TIME_HOUR=22;//游戏游戏结束小时
	public final static Integer K3_END_TIME_MINUTE=00;//游戏结束小时
	public final static Integer K3_START_TIME_NUMBER=90000;//开始时间数字化
	public final static Integer K3_END_TIME_NUMBER=220000;//开始时间数字化
	public final static Integer K3_END_TIME_NUMBER_MAX=235959;//开始时间数字化
	
	public final static Integer K3_MIN_UNIT_POINT = 2;//最小单位购买积分
	
	public final static Integer K3_MAX_NUMBER=10;//最大数字(不含)
	public final static Integer K3_MAX_HOME_BET_SESSIONS=12;//首页最大显示场数
	
	public final static String K3_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String K3_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String K3_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	

	public final static String K3_WIN_STATUS_INIT = "0";//0=未开奖 
	public final static String K3_WIN_STATUS_YES = "1";//1=中奖 
	public final static String K3_WIN_STATUS_NO = "2";//2=未中奖
	
	//------------------------------------k3 end---------------------
	
	
	
	//----------------hhmf-------------常量配置--------------------
	

	//配置场次及时间间隔
	public final static Integer HHMF_TIME_OPENING_FREEZE=65000;//[毫秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static Integer HHMF_TIME_INTERVAL=25;//秒
	public final static Integer HHMF_MAX_PART=3600*24/GameConstants.HHMF_TIME_INTERVAL;//最大场次
	public final static String HHMF_START_TIME_END=" 00:00:00";//开始时间后缀
	public final static String HHMF_END_TIME_END=" 23:59:59";//开始时间后缀
	public final static String HHMF_START_TIME_S=" 09:00:00";//游戏开始时间点
	public final static String HHMF_START_TIME_STR=" 09:10:00";//游戏开始时间第一期
	public final static Integer HHMF_END_TIME_HOUR=21;//游戏游戏结束小时
	public final static Integer HHMF_END_TIME_MINUTE=50;//游戏结束小时
	public final static Integer HHMF_START_TIME_NUMBER=90000;//开始时间数字化
	public final static Integer HHMF_END_TIME_NUMBER=213000;//开始时间数字化
	public final static Integer HHMF_END_TIME_NUMBER_MAX=235959;//开始时间数字化
	
	public final static Integer HHMF_MIN_UNIT_POINT = 2;//最小单位购买积分
	
	public final static Integer HHMF_MAX_NUMBER=10;//最大数字(不含)
	public final static Integer HHMF_MAX_HOME_BET_SESSIONS=12;//首页最大显示场数
	
	public final static String HHMF_OPEN_STATUS_BET = "0";//已开奖
	public final static String HHMF_OPEN_STATUS_OPENING = "1";//开奖中
	public final static String HHMF_OPEN_STATUS_WAITING = "2";//投注中
	
	public final static Integer HHMF_BET_INTERVAL = 15;//投注时间秒
	public final static Integer HHMF_OPEN_INTERVAL = 5;//开奖等待时间秒
	public final static Integer HHMF_WARIT_INTERVAL = 5;//空闲等待时间秒
	
	
	public final static String HHMF_POKER_COLOR_HONGTAO = "1";//黑桃
	public final static String HHMF_POKER_COLOR_HEITAO = "2";//红桃
	public final static String HHMF_POKER_COLOR_MEIHUA = "3";//梅花
	public final static String HHMF_POKER_COLOR_FANGPIAN = "4";//方片
	public final static String HHMF_POKER_COLOR_WANG = "5";//大小王
	
	//s=spade黑桃 h=heart红桃 c=club梅花 d=diamond方片
	public final static String[] POKERS = {
		"1-2","1-3","1-4","1-5","1-6","1-7","1-8","1-9","1-10","1-J","1-Q","1-K","1-A",//黑桃
		"2-2","2-3","2-4","2-5","2-6","2-7","2-8","2-9","2-10","2-J","2-Q","2-K","2-A",//红桃
		"3-2","3-3","3-4","3-5","3-6","3-7","3-8","3-9","3-10","3-J","3-Q","3-K","3-A",//梅花
		"4-2","4-3","4-4","4-5","4-6","4-7","4-8","4-9","4-10","4-J","4-Q","4-K","4-A",//方片
		"55","5"//大小王
		};
	public final static String getRandomOpenHHMFResult(){
//		String[] singleCards = {"2","3","4","5","6","7","8","9","10","J","Q","K","A","W","WW"};//点数包括两张王 
//	    String[] colorCards = {"s","h","c","d"};
//	    String singles = singleCards[StringUtil.getRandomInt(14)];
//	    String colors = colorCards[StringUtil.getRandomInt(3)];
//	    if(singles.equals("W") || singles.equals("WW")){//大小王时统一返回王
//	    	return "w";
//	    }else{
//	    	return colors+"-"+singles;
//	    }
		return POKERS[StringUtil.getRandomInt(53)];
	}
	
	public final static String[] POKERSVALUE = {
			"2","3","4","5","6","7","8","9","10","J","Q","K","A"
	};
	
	public final static String getRandomOpenHHMFResultValue(){
//		String[] singleCards = {"2","3","4","5","6","7","8","9","10","J","Q","K","A","W","WW"};//点数包括两张王 
//	    String[] colorCards = {"s","h","c","d"};
//	    String singles = singleCards[StringUtil.getRandomInt(14)];
//	    String colors = colorCards[StringUtil.getRandomInt(3)];
//	    if(singles.equals("W") || singles.equals("WW")){//大小王时统一返回王
//	    	return "w";
//	    }else{
//	    	return colors+"-"+singles;
//	    }
		return POKERSVALUE[StringUtil.getRandomInt(13)];
	}
	
	public final static String getHHMFOpenResultType(String result){
		if(result==null && result.length()==0) return "0";
		return result.substring(0,1);
	}
	//------------------------------------hhmf end---------------------
	
}
