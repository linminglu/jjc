package com.gf.dcb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.apps.util.Combination;
import com.gf.dcb.DcbConstants;

/**
 * 双色球工具类
 * @author admin
 *
 */
public class DcbUtil {
	
	/**
	 * 计算组合数 all是所有数量 min是最少几个为一组 即all里面选min个
	 * 
	 * @param all
	 * @param min
	 * @return
	 */
	public static int getTotal(int all, int min) {
		Combination combination = new Combination();
		ArrayList<String> coms = new ArrayList<String>();
		try {
			coms = combination.getCombinations(all, min);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return coms.size();
	}
	
	/**
	 * 计算投注总注数
	 * @param bets  投注字符串例如  2,4,6,7,32,13,22||2,15
	 * @return
	 */
	public static int getTotalBetNum(List<String> betBallList){
		int totalBet =0;
//		String[] bet = betBallList.split(";");
		for(String betOption : betBallList){
//			String betBall = betOption.split("+")[1];
			String[] redBall = betOption.split("\\|")[0].split(",");
			String[] blueBall = betOption.split("\\|")[1].split(",");
			int redNum = DcbUtil.getTotal(redBall.length,6);
			int blueNum = DcbUtil.getTotal(blueBall.length, 1);
			totalBet = totalBet + redNum*blueNum;
		}
		return totalBet;
	}

	
	/**
	 * 所有组合（红球） a = 红球投注字符串 num=6
	 */
	 public static List<String> combine(String[] a, int num) {
		  List<String> list = new ArrayList<String>();
		  StringBuffer sb = new StringBuffer();
		  String[] b = new String[a.length];
		  for (int i = 0; i < b.length; i++) {
		   if (i < num) {
		    b[i] = "1";
		   } else
		    b[i] = "0";
		  }

		  int point = 0;
		  int nextPoint = 0;
		  int count = 0;
		  int sum = 0;
		  String temp = "1";
		  while (true) {
		   // 判断是否全部移位完毕
		   for (int i = b.length - 1; i >= b.length - num; i--) {
		    if (b[i].equals("1"))
		     sum += 1;
		   }
		   // 根据移位生成数据
		   for (int i = 0; i < b.length; i++) {
		    if (b[i].equals("1")) {
		     point = i;
		     sb.append(a[point]);
		     sb.append(",");
		     count++;
		     if (count == num)
		      break;
		    }
		   }
		   // 往返回值列表添加数据
		   list.add(sb.toString().substring(0, sb.toString().length()-1));

		   // 当数组的最后num位全部为1 退出
		   if (sum == num) {
		    break;
		   }
		   sum = 0;

		   // 修改从左往右第一个10变成01
		   for (int i = 0; i < b.length - 1; i++) {
		    if (b[i].equals("1") && b[i + 1].equals("0")) {
		     point = i;
		     nextPoint = i + 1;
		     b[point] = "0";
		     b[nextPoint] = "1";
		     break;
		    }
		   }
		   // 将 i-point个元素的1往前移动 0往后移动
		   for (int i = 0; i < point - 1; i++)
		    for (int j = i; j < point - 1; j++) {
		     if (b[i].equals("0")) {
		      temp = b[i];
		      b[i] = b[j + 1];
		      b[j + 1] = temp;
		     }
		    }
		   // 清空 StringBuffer
		   sb.setLength(0);
		   count = 0;
		  }
		  // 
//		  System.out.println("数据长度 " + list.size());
		  return list;

		 }


	 /**
	  * 
	  * @param openRedBall  开奖红球
	  * @param betRedBall 投注红球
	  * @return 返回相同的红球和不相同的红球
	  */
		public static List<List<String>> same(String[] openRedBall,String[] betRedBall){
			List<List<String>> newList=new ArrayList<List<String>>();
			List<String> sameList=new ArrayList<String>();
			List<String> notList=new ArrayList<String>();
			for(int i=0;i<betRedBall.length;i++){
				for(int j=0;j<betRedBall.length;j++){
					if(betRedBall[i].equals(openRedBall[j])){
						sameList.add(betRedBall[i]);
					}else{
						notList.add(betRedBall[i]);
					}
				}
			}
			newList.add(sameList);
			newList.add(notList);
			return newList;
		}
	 
	 
	 /**
	  * 
	  * @param betBall 投注球
	  * @param openBall 开奖球
	  * @return 投注球+中奖赔率 例如:1,2,3,4,5,6|7+1000
	  */
	 public static List<String> judgeWin(String betBall,String openBall){
		 String[] betRed = betBall.split("\\|")[0].split(",");//投注红球数组。
		 String[] betBlue = betBall.split("\\|")[1].split(",");//投注蓝球数组。
		 String[] openRed = openBall.split("\\+")[0].split(",");//开奖红球数组。
		 String openBlue = openBall.split("\\+")[1];//开奖蓝球。
		 List<String> winList = new ArrayList<String>();
			if(betBlue != null && betBlue.length>0){
				for(int i=0; i<betBlue.length; i++){
					List<String> betRedList = combine(betRed,6); //红球投注各种组合。
					String win = new String(); //中奖字符串  例如：1,2,3,4,5,6|7+2
					if(betBlue[i].equals(openBlue)){ //如果蓝球相等
						if(betRedList != null && betRedList.size() >0){
							for(int j =0;j<betRedList.size();j++){
								String [] red = betRedList.get(j).split(",");
								List<String> sameList=same(openRed,red).get(0);
								if(sameList.size()<=2){//选6+1中2+1或中1+1或中0+1    六等奖
									win = betRedList.get(j)+"|"+betBlue[i]+"+"+DcbConstants.SIX_PRICE;
									winList.add(win);
								}else if(sameList.size()==3){//中3+1	  五等奖
									win = betRedList.get(j)+"|"+betBlue[i]+"+"+DcbConstants.FIVE_PRICE;
									winList.add(win);
								}else if(sameList.size()==4){//中4+1 四等奖
									win = betRedList.get(j)+"|"+betBlue[i]+"+"+DcbConstants.FOUR_PRICE;
									winList.add(win);
								}else if(sameList.size()==5){//中5+1	  三等奖
									win = betRedList.get(j)+"|"+betBlue[i]+"+"+DcbConstants.THREE_PRICE;
									winList.add(win);
								}else if(sameList.size()==6){//中6+1	  一等奖
									win = betRedList.get(j)+"|"+betBlue[i]+"+"+DcbConstants.ONE_PRICE;
									winList.add(win);
								}
							}
						}
					}else{
						//蓝球不相等
						if(betRedList != null && betRedList.size() >0){
							for(int j =0;j<betRedList.size();j++){
								String [] red = betRedList.get(j).split(",");
								List<String> sameList=same(openRed,red).get(0);
								if(sameList.size()<4){ //不中奖
								}else{
									if(sameList.size()==4){//中4+0  五等奖
										win = betRedList.get(j)+"|"+betBlue[i]+"+"+DcbConstants.FIVE_PRICE;
										winList.add(win);
									}else if(sameList.size()==5){//中5+0 四等奖
										win = betRedList.get(j)+"|"+betBlue[i]+"+"+DcbConstants.FOUR_PRICE;
										winList.add(win);
									}else if(sameList.size()==6){////中6+0 二等奖
										win = betRedList.get(j)+"|"+betBlue[i]+"+"+DcbConstants.TWO_PRICE;
										winList.add(win);
									}
								}
							}
						}
					}
				}
			}
			return winList;
		}
	 
	 
	 /**
	  * 计算单个投注项奖金总额
	  * @param winList 中奖list
	  * @param multiple 倍数
	  * @return
	  */
	 public static int countBonuses(List<String> winList,int multiple){
		 int sum = 0;
		 for(int i =0;i<winList.size();i++){
			 sum = sum + Integer.parseInt(winList.get(i).split("\\+")[1])*multiple;
		 }
		 
		 return sum;
	 }
 
	 /**
	  * 生成随机两位数
	  * @return
	  */
	 public static String getTwo(){  
	    Random rad=new Random();  
	  	  
	    String result  = String.format("%02d", rad.nextInt(100));
	    return result;  
	 }  
	 
	 public static boolean chkBetting(String betBall) {
//		String regEx = "(0[1-9]|[1-2][0-9]|3[0-3])(,(0[1-9]|[1-2][0-9]|3[0-3])){5,20}\\|(0[1-9]|1[0-6])(,(0[1-9]|1[0-6])){0,15}";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(betBall);
//		return matcher.matches();
			String reg = "^[^|]+\\|[^|]+$";
			Pattern pattern = Pattern.compile(reg);
			if(pattern.matcher(betBall).matches()){
				String redBall = betBall.split("\\|")[0];
				String blueBall = betBall.split("\\|")[1];
				Pattern redPattern = Pattern.compile("(?:((0[1-9])|([1|2]\\d)|(3[0-6])),(?!.*\\1)){5,19}((0[1-9])|([1|2]\\d)|(3[0-6]))");  
				Pattern bluePattern = Pattern.compile("(?:((0[1-9])|(1[0-6])),(?!.*\\1)){0,15}((0[1-9])|(1[0-6]))");  
		        if(redPattern.matcher(redBall).matches()&&bluePattern.matcher(blueBall).matches()){
		        	return true;
		        }else{
		        	return false;
		        }
			}else{
				return false;
			}
	}
	 
	 
	 public static void  main(String args[]){
		 String  betOption="01,02,03,04,05,06,07,08,09,10,11,14,15,16,17,18,19,20,21,22|01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16";
		 	String[] redBall = betOption.split("\\|")[0].split(",");
			String[] blueBall = betOption.split("\\|")[1].split(",");
			int redNum = DcbUtil.getTotal(redBall.length,6);
			int blueNum = DcbUtil.getTotal(blueBall.length, 1);
			int totalBet =  redNum*blueNum;
			System.out.println(totalBet);
	 }

}
