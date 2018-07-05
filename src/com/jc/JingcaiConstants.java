package com.jc;

import java.math.BigDecimal;

/**
 * 竞猜模块常量
 * @author zzw
 *
 */
public class JingcaiConstants {
	
	/** 全名竞猜投注计算赔率基数*/
	public final static Integer JINGCAI_QUIZ_BASE_NUMMBER = 1000; // 如需修改在此处修改即可
	/** 全名竞猜投注抽税税率*/
	public final static BigDecimal JINGCAI_QUIZ_TAX_RATE = new BigDecimal("0.1"); // 如需修改在此处修改即可
	
	//用来页面分页用，表示每页显示的最大条数
	public final static int  MAXPAGEITEMS=20;
	
	/** 分类状态：0=无效*/
	public final static String TYPE_STATUS_YES = "1";
	/** 分类状态：1=有效*/
	public final static String TYPE_STATUS_NO = "0";
	
	/** 分类等级：1=一级*/
	public final static String TYPE_FIRST = "1";
	/** 分类等级：2=二级*/
	public final static String TYPE_SECOND = "2";
	
	public final static Integer JINGCAI_TIME_OPENING_FREEZE = 150;//投注结束150前不可在投注
	
	public final static String GAME_TYPE = "竞猜";
	
	/** 竞猜开奖状态：0=未开奖*/
	public final static String QUIZ_STATUS_NOT_OPEN = "0";
	/** 竞猜开奖状态：1=已开奖*/
	public final static String QUIZ_STATUS_OPENING = "1";
	
	
	/** 状态：1=有效*/
	public final static String STATUS_YES = "1";
	/** 状态：0=无效*/
	public final static String STATUS_NO = "0";
	
	/** 比赛场次：BO1=1*/
	public final static Integer BO1 = 1;
	/** 比赛场次：BO3=3*/
	public final static Integer BO3 = 3;
	/** 比赛场次：BO5=5*/
	public final static Integer BO5 = 5;
	/** 比赛场次：BO7=7*/
	public final static Integer BO7 = 7;
	
	/** 热门赛事是否推荐：1=是*/
	public final static String IS_RECOMMAND_YES = "1";
	/** 热门赛事是否推荐：0=否*/
	public final static String IS_RECOMMAND_NO = "0";
	
	
	
	public final static String getBoName(Integer boType){
		if(BO1.equals(boType)) return "BO1";
		else if(BO3.equals(boType)) return "BO3";
		else if(BO5.equals(boType)) return "BO5";
		else if(BO7.equals(boType)) return "BO7";
		else {return "";}
	}
	
}
