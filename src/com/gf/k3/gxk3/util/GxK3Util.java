package com.gf.k3.gxk3.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.util.Log;

import com.gf.k3.gxk3.GxK3Constants;

public class GxK3Util {

	/**
	 * 计算投注总注数
	 * @param bets  投注字符串例如  2+2,4,6,7,32,13,22||2,15 玩法+投注号码
	 * @return
	 */
	public static int getTotalBetNum(List<String> betBallList){
		int totalBet = 0;
		for(String betOption : betBallList){
			String playType = betOption.split("\\+")[0];
			String betBall = betOption.split("\\+")[1];
			totalBet += getTotalBetNum(playType,betBall);
		}
		return totalBet;
	}
	public static int getTotalBetNum(String playType,String betBall){
		int totalBet =0;
		if("0".equals(playType)){//猜一个就中奖
			totalBet=21;//固定21注
		}else if("1".equals(playType)){//二不同号
			String[] one = betBall.split(",");
			if(one.length<2){
				Log.info("江苏快3二不同号投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,2);
		}else if("2".equals(playType)){//二同号单选
			String[] one = betBall.split("\\*")[0].split(",");
			String[] two = betBall.split("\\*")[1].split(",");
			List<String> list=new ArrayList<String>();
			for(int i=0;i<one.length;i++){
				for(int j=0;j<two.length;j++){
					if(Integer.parseInt(one[i].substring(0, 1))<Integer.parseInt(two[j])){
						list.add(one[i]+","+two[j]);
					}else{
						list.add(two[j]+","+one[i]);
					}
				}
			}
			if(list.size()<1){
				Log.info("江苏快3二同号单选投注不正确");
				return -1;
			}
			totalBet = totalBet + list.size();
		}else if("3".equals(playType)){//二同号复选
			if(betBall.length()<3){
				Log.info("江苏快3二同号复选投注不正确");
				return -1;
			}
			totalBet = totalBet + 1;
		}else if("4".equals(playType)){//三连号通选
			if(!"三连号通选".equals(betBall)){
				Log.info("江苏快3三连号通选投注不正确");
				return -1;
			}
			totalBet = totalBet + 1;//因为一个号只有一注
		}else if("5".equals(playType)){//三不同号
			String[] one = betBall.split(",");
			if(one.length<3){
				Log.info("江苏快3三不同号投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,3);
		}else if("6".equals(playType)){//三同号单选
			if(betBall.length()<3){
				Log.info("江苏快3三同号单选投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(betBall.length(),3);
		}else if("7".equals(playType)){//三同号通选
			if(!"三同号通选".equals(betBall)){
				Log.info("江苏快3三同号通选投注不正确");
				return -1;
			}
			totalBet = totalBet + 1;//因为一个号只有一注
		}else if("8".equals(playType)){//和值
			String[] one = betBall.split(",");
			if(one.length<1){
				Log.info("江苏快3和值投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,1);
		}
		return totalBet;
	}


	
	/**
	 * 
	 * 比较小的数据排列组合
	 */
	public static int  getBetTotal(int m,int n) { 
		int fenzi=1;
		int fenmu=1;
		for(int k=m;k>=(m-n+1);k--){
			fenzi=fenzi*k;
		}
		for(int k=1;k<=n;k++){
			fenmu=fenmu*k;
		}
		return fenzi/fenmu;
    }
	
	
	
	public static BigDecimal judgeWinMoney(String playType, String betBall,
			BigDecimal betRate, String result) {
		String[] results = result.split(",");
		BigDecimal winMoney = new BigDecimal(0);
		if(GxK3Constants.GAME_TYPE_GF_YIGEHAO.equals(playType)){//猜一个号就中奖
//			String resultVal=results[0]+","+results[1]+","+results[2];
			String[] one = betBall.split("\\*")[0].split(",");
			String[] two = betBall.split("\\*")[1].split(",");
			String[] three = betBall.split("\\*")[2].split(",");
			int num1=0;
			int num2=0;
			int num3=0;
			for(int i=0;i<one.length;i++){
				if(one[i].equals(results[0])){
					num1=1;
				}
			}
			if(num1==1){
				for(int m=0;m<two.length;m++){
					if(two[m].equals(results[1])){
						num2=1;
					}
				}
			}
			if(num2==1){
				for(int m=0;m<three.length;m++){
					if(three[m].equals(results[2])){
						num3=1;
					}
				}
			}
			if(num3==1){
				winMoney=winMoney.add(betRate);
			}
		}else if(GxK3Constants.GAME_TYPE_GF_ERBUTONG.equals(playType)){//二不同号
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])||array[i].equals(results[1])){
					num++;
				}
			}
			if(num>=2){
				winMoney=winMoney.add(betRate);
			}
		}else if(GxK3Constants.GAME_TYPE_GF_ERTONGDAN.equals(playType)){//二同号单选
			String[] one = betBall.split("\\*")[0].split(",");
			String[] two = betBall.split("\\*")[1].split(",");
			List<String> list=new ArrayList<String>();
			String value = results[0]+results[1]+results[2];//开奖结果去掉逗号
			String bet = "";//投的号
			for(int i=0;i<one.length;i++){
				for(int j=0;j<two.length;j++){
					if(Integer.parseInt(one[i].substring(0, 1))<Integer.parseInt(two[j])){
						bet = one[i]+two[j];
					}else{
						bet = two[i]+one[j];
					}
					if (value.equals(bet)) {//中奖
						winMoney=winMoney.add(betRate);
					}
				}
			}
		}else if(GxK3Constants.GAME_TYPE_GF_ERTONGFU.equals(playType)){//二同号复选
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])||array[i].equals(results[1])){
					num++;
				}
			}
			if(num>=2){
				winMoney=winMoney.add(betRate);
			}
		}else if(GxK3Constants.GAME_TYPE_GF_SANLIANTONG.equals(playType)){//三连号通选
			String value = results[0]+results[1]+results[2];
			if ("123".equals(value) || "234".equals(value)|| "345".equals(value)
					|| "456".equals(value)) {
				winMoney=winMoney.add(betRate);
			}
		}else if(GxK3Constants.GAME_TYPE_GF_SANBUTONG.equals(playType)){//三不同号	
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])||array[i].equals(results[1])||array[i].equals(results[2])){
					num++;
				}
			}
			if(num>=3){
				winMoney=winMoney.add(betRate);
			}	
		}else if(GxK3Constants.GAME_TYPE_GF_SANTONGDAN.equals(playType)){//三同号单选
			String value = results[0]+results[1]+results[2];
			if (value.equals(betBall)) {
				winMoney=winMoney.add(betRate);
			}
		}else if(GxK3Constants.GAME_TYPE_GF_SANTONGTONG.equals(playType)){//三同号通选
			String value = results[0]+results[1]+results[2];
			if ("111".equals(value) || "222".equals(value)|| "333".equals(value)
					|| "444".equals(value)|| "555".equals(value)|| "666".equals(value)) {
				winMoney=winMoney.add(betRate);
			}
		}else if(GxK3Constants.GAME_TYPE_GF_HEZHI.equals(playType)){//和值
			Integer hezhi = Integer.valueOf(results[0])+Integer.valueOf(results[1])
					+Integer.valueOf(results[2]);//计算开奖的和值
			String array[]=betBall.split(",");
			for(int i=0;i<array.length;i++){
				if (hezhi == Integer.valueOf(array[i])) {
					winMoney=winMoney.add(betRate);
				}
			}
		}
		return winMoney;
	}
	
	
	/**
	 * 和值玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkHEZHI(String betBall) {
		//String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){7,10}(0[1-9]|1[0-1])";
		String regEx = "(([4-9]|1[0-7]),?){1,14}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	/**
	 * 三同号通选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkSANTONGTONG(String betBall) {
		if ("三同号通选".equals(betBall)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 三同号单选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkSANTONGDAN(String betBall) {
		String regEx = "([1-6]{3},?){1,6}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	
	/**
	 * 三不同号玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkSANBUTONG(String betBall) {
		String regEx = "(?:([1-6]),(?!.*\\1)){2,5}([1-6])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	/**
	 * 三连号通选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkSANLIANTONG(String betBall) {
		if ("三连号通选".equals(betBall)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 二同号复选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkERTONGFU(String betBall) {
		String regEx = "([1-6]{2}\\*{1},?){1,6}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	
	/**
	 * 二同号单选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkERTONGDAN(String betBall) {
		String regEx = "(([1-6]{2},?){1,6}\\*{1}([1-6]{1},?){1,6})";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	/**
	 * 二不同号玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkERBUTONG(String betBall) {
		String regEx = "(?:([1-6]),(?!.*\\1)){1,5}([1-6])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	/**
	 * 猜一个号就中奖玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkYIGEHAO(String betBall) {
//		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){1,10}(0[1-9]|1[0-1])";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(betBall);
//		return matcher.matches();
		return true;
	}
}
