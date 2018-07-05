package com.apps.eff;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.framework.service.impl.ServiceLocatorImpl;
import com.ram.model.User;
import com.ram.service.user.IUserService;

/**
 * 用户资金缓存(用于更新用户及明细表UserTradeDetail) userService.saveTradeDetail()
 * 保存每个用户的最新余额，当资金明细发生变化时第一时间更新
 * 【注意】在调用 userService.saveTradeDetail()后：
 * 如单个用户需要在之后调用userService.updateUserMoney(userId)
 * 如批量用户需要在上层方法中调用 userService.updateUserMoney(userIds)
 */
public class TradeCacheUtil {
	public static Logger log = Logger.getLogger(PayCacheUtil.class);
	private final static IUserService userService = (IUserService) ServiceLocatorImpl
			.getInstance().getService("userService");

	private final static int DURING_TIME = 10000;
	/** 
	 * 用户最新余额缓存 <key=M+用户ID,value=余额>
	 * 此为用户最新余额缓存，可用于计算余额的地方，正常用户余额还是通过user.getMoney()来获取
	 **/
	private static Map<String,BigDecimal> moneyMap = new HashMap<String,BigDecimal>();
	
	/**
	 * 外部更新用户加减余额缓存并返回最新余额
	 * @param userId
	 * @param money
	 * @return
	 */
	public static BigDecimal updateUserMoney(Integer userId,BigDecimal cashMoney){
		BigDecimal money = getUserMoneyInner(userId,true);
		setUserMoneyInner(userId,money,cashMoney,true);//将缓存余额加上收支资金并更新缓存
		BigDecimal moneyNew = getUserMoneyInner(userId,true);//再次从缓存中读取
		
		log.info("[cache.updateUserMoney]>>> userId="+userId+",cache.money1="+money+",cashMoney="+cashMoney+",cache.money2="+moneyNew);
		
		return moneyNew;
	}
	
	/**
	 * 外部更新用户加减积分缓存并返回最新积分
	 * @param userId
	 * @param money
	 * @return
	 */
	public static BigDecimal updateUserPoints(Integer userId,BigDecimal cashMoney){
		BigDecimal money = getUserPointsInner(userId,true);
		setUserPointsInner(userId,money,cashMoney,true);//将缓存余额加上收支资金并更新缓存
		BigDecimal moneyNew = getUserPointsInner(userId,true);//再次从缓存中读取
		
		log.info("[cache.updateUserPoints]>>> userId="+userId+",cache.point1="+money+",cashPoint="+cashMoney+",cache.point2="+moneyNew);
		
		return moneyNew;
	}
	
	/**
	 * 外部获取用户最新余额
	 * @param userId
	 */
	public static BigDecimal getUserMoney(Integer userId){
		BigDecimal money = getUserMoneyInner(userId,true);
		
		log.info("[cache.getUserMoney]>>> userId="+userId+",cache.money="+money);
		
		return money;
	}
	
	/**
	 * 外部获取用户最新积分
	 * @param userId
	 */
	public static BigDecimal getUserPoints(Integer userId){
		BigDecimal points = getUserPointsInner(userId,true);
		log.info("[cache.getUserPoints]>>> userId="+userId+",cache.points="+points);
		return points;
	}
	
	/**
	 * 内部读取
	 * @param userId
	 * @param isPrint
	 * @return
	 */
	private static BigDecimal getUserMoneyInner(Integer userId,boolean isPrint){
		BigDecimal money = moneyMap.get("M"+userId);
		BigDecimal dtLong = moneyMap.get("MDT"+userId);
		BigDecimal dtNow = new BigDecimal(System.currentTimeMillis());//当前时间
		if(dtLong==null) dtLong = dtNow;
		double diff = dtNow.subtract(dtLong).doubleValue();//时间差
		//log.info("[cache.getUserMoney.diff]<<< diff="+diff+",DURING_TIME="+DURING_TIME);
		if(money==null || diff>DURING_TIME){//需要初始化用户余额 为空或更新时间差
			userService.updateUserMoney(userId);//统计用户余额
			User user = userService.getUser(userId);//再次读取
			money = user.getMoney();
			if(money==null) money = new BigDecimal(0);//确实没有余额为0
			log.info("[cache.getUserMoney.load from db]<<< userId="+userId+",user.money="+money);
			setUserMoneyInner(userId, money,null,true);//更新缓存
			
		}
		//if(isPrint) log.info("[cache.getUserMoney]<<< userId="+userId+",cache.money="+money);
		
		return moneyMap.get("M"+userId);
	}
	
	
	/**
	 * 内部读取
	 * @param userId
	 * @param isPrint
	 * @return
	 */
	private static BigDecimal getUserPointsInner(Integer userId,boolean isPrint){
		BigDecimal points = moneyMap.get("P"+userId);
		BigDecimal dtLong = moneyMap.get("PDT"+userId);
		BigDecimal dtNow = new BigDecimal(System.currentTimeMillis());//当前时间
		if(dtLong==null) dtLong = dtNow;
		double diff = dtNow.subtract(dtLong).doubleValue();//时间差
		if(points==null || diff>DURING_TIME){//需要初始化用户余额 为空或更新时间差
			userService.updateUserPoints(userId);//统计用户余额
			User user = userService.getUser(userId);//再次读取
			points = user.getUserpoints();
			if(points==null) points = new BigDecimal(0);//确实没有余额为0
			log.info("[cache.getUserPoints.load from db]<<< userId="+userId+",user.points="+points);
			setUserPointsInner(userId, points,null,true);//更新缓存
			
		}
		//if(isPrint) log.info("[cache.getUserPoints]<<< userId="+userId+",cache.points="+points);
		
		return moneyMap.get("P"+userId);
	}
	
	/**
	 * 重置用户余额缓存
	 */
	public static void clear(){
		//System.gc();//java垃圾回收
		moneyMap = new HashMap<String, BigDecimal>();
		log.info("[cache.TradeCacheUtil.clear()]..........................");
	}
	
	/**
	 * 内部更新用户余额缓存
	 * @param userId
	 * @param money
	 * @param cashMoney 收支金额 无则为0
	 * @return
	 */
	private static void setUserMoneyInner(Integer userId,BigDecimal money,BigDecimal cashMoney,boolean isPrint){
		if(cashMoney==null) cashMoney = new BigDecimal(0);
		moneyMap.put("M"+userId, money.add(cashMoney));//缓存余额
		moneyMap.put("MDT"+userId, new BigDecimal(System.currentTimeMillis()));//更新时间
		//if(isPrint) log.info("[cache.setUserMoney]>>> userId="+userId+",money="+money+",cashMoney="+cashMoney+",cache.money="+moneyMap.get("M"+userId));
	}
	/**
	 * 内部更新用户积分缓存
	 * @param userId
	 * @param points
	 * @param cashMoney 收支积分 无则为0
	 * @return
	 */
	private static void setUserPointsInner(Integer userId,BigDecimal points,BigDecimal cashMoney,boolean isPrint){
		if(cashMoney==null) cashMoney = new BigDecimal(0);
		moneyMap.put("P"+userId, points.add(cashMoney));//缓存余额
		moneyMap.put("PDT"+userId, new BigDecimal(System.currentTimeMillis()));//更新时间
		//if(isPrint) log.info("[cache.setUserMoney]>>> userId="+userId+",money="+money+",cashMoney="+cashMoney+",cache.money="+moneyMap.get("M"+userId));
	}
}
