package com.gf.ssc.cqssc.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.apps.util.Combination;
import com.framework.util.ParamUtils;
import com.gf.ssc.cqssc.CqSscConstants;

/**
 * 双色球工具类
 * 
 * @author admin
 * 
 */
public class CqSscUtil {

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
	 * 
	 * @param bets
	 *            投注字符串例如 2+2,4,6,7,32,13,22||2,15 玩法+投注号码
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
		int totalBet = 0;
		if (CqSscConstants.PLAY_TYPE_ZERO.equals(playType)) {// 大小单双
			char[] tents = betBall.split(",")[0].toCharArray();
			char[] unit = betBall.split(",")[1].toCharArray();
			int tentsNum = tents.length;
			int unitNum = unit.length;
			totalBet = totalBet + tentsNum * unitNum;
		} else if (CqSscConstants.PLAY_TYPE_ONE.equals(playType)) {// 一星
			char[] unit = betBall.toCharArray();
			int unitNum = unit.length;
			totalBet = totalBet + unitNum;
		} else if (CqSscConstants.PLAY_TYPE_TWO.equals(playType)) {// 二星组选
			String[] grou = betBall.split(",");
			int unitNum = CqSscUtil.getTotal(grou.length, 2);
			totalBet = totalBet + unitNum;
		} else if (CqSscConstants.PLAY_TYPE_THREE.equals(playType)) {// 二星直选
			char[] tents = betBall.split(",")[0].toCharArray();
			char[] unit = betBall.split(",")[1].toCharArray();
			int tentsNum = tents.length;
			int unitNum = unit.length;
			totalBet = totalBet + tentsNum * unitNum;
		} else if (CqSscConstants.PLAY_TYPE_FOUR.equals(playType)) {// 三星组六
			String[] grou = betBall.split(",");
			int unitNum = CqSscUtil.getTotal(grou.length, 3);
			totalBet = totalBet + unitNum;
		} else if (CqSscConstants.PLAY_TYPE_FIVE.equals(playType)) {// 三星组三
			String[] grou = betBall.split(",");
			int unitNum = CqSscUtil.getTotal(grou.length, 2);
			totalBet = totalBet + unitNum * 2;
		} else if (CqSscConstants.PLAY_TYPE_SIX.equals(playType)) {// 三星直选
			char[] tents = betBall.split(",")[0].toCharArray();
			char[] unit = betBall.split(",")[1].toCharArray();
			char[] percent = betBall.split(",")[2].toCharArray();
			int tentsNum = tents.length;
			int unitNum = unit.length;
			int percentNum = percent.length;
			totalBet = totalBet + tentsNum * unitNum * percentNum;
		} else if (CqSscConstants.PLAY_TYPE_SEVEN.equals(playType)) {// 五星直选
			char[] tents = betBall.split(",")[0].toCharArray();
			char[] unit = betBall.split(",")[1].toCharArray();
			char[] percent = betBall.split(",")[2].toCharArray();
			char[] thousand = betBall.split(",")[3].toCharArray();
			char[] extreme = betBall.split(",")[4].toCharArray();
			int tentsNum = tents.length;
			int unitNum = unit.length;
			int percentNum = percent.length;
			int thousandNum = thousand.length;
			int extremeNum = extreme.length;
			totalBet = totalBet + tentsNum * unitNum * percentNum
					* thousandNum * extremeNum;
		} else if (CqSscConstants.PLAY_TYPE_EIGHT.equals(playType)) {// 三星组三
			char[] tents = betBall.split(",")[0].toCharArray();
			char[] unit = betBall.split(",")[1].toCharArray();
			char[] percent = betBall.split(",")[2].toCharArray();
			char[] thousand = betBall.split(",")[3].toCharArray();
			char[] extreme = betBall.split(",")[4].toCharArray();
			int tentsNum = tents.length;
			int unitNum = unit.length;
			int percentNum = percent.length;
			int thousandNum = thousand.length;
			int extremeNum = extreme.length;
			totalBet = totalBet + tentsNum * unitNum * percentNum
					* thousandNum * extremeNum;
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
			list.add(sb.toString().substring(0, sb.toString().length() - 1));

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
		// System.out.println("数据长度 " + list.size());
		return list;

	}

	/**
	 * 
	 * @param openRedBall
	 *            开奖红球
	 * @param betRedBall
	 *            投注红球
	 * @return 返回相同的红球和不相同的红球
	 */
	public static List<List<String>> same(String[] openRedBall,
			String[] betRedBall) {
		List<List<String>> newList = new ArrayList<List<String>>();
		List<String> sameList = new ArrayList<String>();
		List<String> notList = new ArrayList<String>();
		for (int i = 0; i < betRedBall.length; i++) {
			for (int j = 0; j < betRedBall.length; j++) {
				if (betRedBall[i].equals(openRedBall[j])) {
					sameList.add(betRedBall[i]);
				} else {
					notList.add(betRedBall[i]);
				}
			}
		}
		newList.add(sameList);
		newList.add(notList);
		return newList;
	}

	/**
	 * 生成随机两位数
	 * 
	 * @return
	 */
	public static String getTwo() {
		Random rad = new Random();

		String result = String.format("%02d", rad.nextInt(100));
		return result;
	}

	/**
	 * 根据号码返回大小单双
	 * 
	 * @param number
	 * @return
	 */
	public static String getOddAndEven(String number) {
		StringBuilder sb = new StringBuilder();
		int num = Integer.parseInt(number);
		if (ParamUtils.chkInteger(num)) {
			if (num >= 5) {
				sb.append("大");
			} else {
				sb.append("小");
			}
			if (num % 2 == 0) {
				sb.append("双");
			} else {
				sb.append("单");
			}
		} else {// 如果num=0
			sb.append("小双");
		}
		return sb.toString();
	}

	public static String getAfterCqSsc(String openResult) {
		String[] re = openResult.split(",");
		if (re[2].equals(re[3]) || re[2].equals(re[4]) || re[3].equals(re[4])) {
			return "组三";
		} else {
			return "组六";
		}
	}

	/**
	 * 计算投注获奖金额。
	 * 
	 * @param playType
	 * @param betBall
	 * @param betRate
	 * @param result
	 * @return
	 */
	public static BigDecimal judgeWinMoney(String playType, String betBall,
			BigDecimal betRate, String result) {
		String[] results = result.split(",");
		BigDecimal winMoney = new BigDecimal(0);
		if (CqSscConstants.PLAY_TYPE_ZERO.equals(playType)) {
			int ten = Integer.parseInt(results[3]);
			int unit = Integer.parseInt(results[4]);
			if (ten > 4 && betBall.split(",")[0].contains("大")) {
				if (unit > 4 && betBall.split(",")[1].contains("大")) {
					winMoney = winMoney.add(betRate);
				} else if (unit < 4 && betBall.split(",")[1].contains("小")) {
					winMoney = winMoney.add(betRate);
				}
				if (unit % 2 == 0 && betBall.split(",")[1].contains("双")) {
					winMoney = winMoney.add(betRate);
				} else if (unit % 2 != 0 && betBall.split(",")[1].contains("单")) {
					winMoney = winMoney.add(betRate);
				}
			}
			if (ten < 4 && betBall.split(",")[0].contains("小")) {
				if (unit > 4 && betBall.split(",")[1].contains("大")) {
					winMoney = winMoney.add(betRate);
				} else if (unit < 4 && betBall.split(",")[1].contains("小")) {
					winMoney = winMoney.add(betRate);
				}
				if (unit % 2 == 0 && betBall.split(",")[1].contains("双")) {
					winMoney = winMoney.add(betRate);
				} else if (unit % 2 != 0 && betBall.split(",")[1].contains("单")) {
					winMoney = winMoney.add(betRate);
				}
			}
			if (ten % 2 == 0 && betBall.split(",")[0].contains("双")) {
				if (unit > 4 && betBall.split(",")[1].contains("大")) {
					winMoney = winMoney.add(betRate);
				} else if (unit < 4 && betBall.split(",")[1].contains("小")) {
					winMoney = winMoney.add(betRate);
				}
				if (unit % 2 == 0 && betBall.split(",")[1].contains("双")) {
					winMoney = winMoney.add(betRate);
				} else if (unit % 2 != 0 && betBall.split(",")[1].contains("单")) {
					winMoney = winMoney.add(betRate);
				}
			}
			if (ten % 2 != 0 && betBall.split(",")[0].contains("单")) {
				if (unit > 4 && betBall.split(",")[1].contains("大")) {
					winMoney = winMoney.add(betRate);
				} else if (unit < 4 && betBall.split(",")[1].contains("小")) {
					winMoney = winMoney.add(betRate);
				}
				if (unit % 2 == 0 && betBall.split(",")[1].contains("双")) {
					winMoney = winMoney.add(betRate);
				} else if (unit % 2 != 0 && betBall.split(",")[1].contains("单")) {
					winMoney = winMoney.add(betRate);
				}
			}
			return winMoney;
		} else if (CqSscConstants.PLAY_TYPE_ONE.equals(playType)) {
			char ball;// 单个球
			for (int i = 0; i < betBall.length(); i++) {
				ball = betBall.charAt(i);
				if (results[4].equals(String.valueOf(ball))) {
					winMoney = winMoney.add(betRate);
				}
			}
			return winMoney;
		} else if (CqSscConstants.PLAY_TYPE_TWO.equals(playType)) {
			if (!results[3].equals(results[4])) {
				String[] array = betBall.split(",");
				int num = 0;
				boolean is3 = true;
				boolean is4 = true;
				for (int i = 0; i < array.length; i++) {//
					if (is3) {
						if (array[i].equals(results[3])) {
							num++;
							is3 = false;
						}
					}
					if (is4) {
						if (array[i].equals(results[4])) {
							num++;
							is4 = false;
						}
					}

				}
				if (num == 2) {
					winMoney = winMoney.add(betRate);
				}
			}
			return winMoney;
		} else if (CqSscConstants.PLAY_TYPE_THREE.equals(playType)) {
			if (betBall.split(",")[0].contains(results[3])
					&& betBall.split(",")[1].contains(results[4])) {
				winMoney = winMoney.add(betRate);
			}
			return winMoney;
		} else if (CqSscConstants.PLAY_TYPE_FOUR.equals(playType)) {
			if (!results[2].equals(results[3])
					&& !results[2].equals(results[4])
					&& !results[3].equals(results[4])) {
				// if(betBall.contains(results[2])&&betBall.contains(results[3])&&betBall.contains(results[4])){
				// winMoney = winMoney.add(betRate);
				// }
				String[] array = betBall.split(",");
				int num = 0;
				boolean is2 = true;
				boolean is3 = true;
				boolean is4 = true;
				for (int i = 0; i < array.length; i++) {//
					if (is2) {
						if (array[i].equals(results[2])) {
							num++;
							is2 = false;
						}
					}
					if (is3) {
						if (array[i].equals(results[3])) {
							num++;
							is3 = false;
						}
					}
					if (is4) {
						if (array[i].equals(results[4])) {
							num++;
							is4 = false;
						}
					}

				}
				if (num == 3) {
					winMoney = winMoney.add(betRate);
				}

			}
			return winMoney;
		} else if (CqSscConstants.PLAY_TYPE_FIVE.equals(playType)) {
			if (results[2].equals(results[3]) && !results[2].equals(results[4])) {
				String[] array = betBall.split(",");
				int num = 0;
				boolean is2 = true;
				boolean is4 = true;
				for (int i = 0; i < array.length; i++) {//
					if (is4) {
						if (array[i].equals(results[4])) {
							num++;
							is4 = false;
						}
					}
					if (is2) {
						if (array[i].equals(results[2])) {
							num++;
							is2 = false;
						}
					}
				}
				if (num == 2) {
					winMoney = winMoney.add(betRate);
				}
				// if(betBall.contains(results[2])&&betBall.contains(results[4])){
				// winMoney = winMoney.add(betRate);
				// }
			} else if (results[2].equals(results[4])
					&& !results[2].equals(results[3])) {
				String[] array = betBall.split(",");
				int num = 0;
				boolean is2 = true;
				boolean is3 = true;
				for (int i = 0; i < array.length; i++) {//
					if (is3) {
						if (array[i].equals(results[3])) {
							num++;
							is3 = false;
						}
					}
					if (is2) {
						if (array[i].equals(results[2])) {
							num++;
							is2 = false;
						}
					}
				}
				if (num == 2) {
					winMoney = winMoney.add(betRate);
				}
				// if(betBall.contains(results[2])&&betBall.contains(results[3])){
				// winMoney = winMoney.add(betRate);
				// }
			} else if (results[3].equals(results[4])
					&& !results[2].equals(results[3])) {
				String[] array = betBall.split(",");
				int num = 0;
				boolean is2 = true;
				boolean is3 = true;
				for (int i = 0; i < array.length; i++) {//
					if (is3) {
						if (array[i].equals(results[3])) {
							num++;
							is3 = false;
						}
					}
					if (is2) {
						if (array[i].equals(results[2])) {
							num++;
							is2 = false;
						}
					}
				}
				if (num == 2) {
					winMoney = winMoney.add(betRate);
				}
				// if(betBall.contains(results[2])&&betBall.contains(results[3])){
				// winMoney = winMoney.add(betRate);
				// }
			}
			return winMoney;
		} else if (CqSscConstants.PLAY_TYPE_SIX.equals(playType)) {
			if (betBall.split(",")[0].contains(results[2])
					&& betBall.split(",")[1].contains(results[3])
					&& betBall.split(",")[2].contains(results[4])) {
				winMoney = winMoney.add(betRate);
			}
			return winMoney;
		} else if (CqSscConstants.PLAY_TYPE_SEVEN.equals(playType)) {
			if (betBall.split(",")[0].contains(results[0])
					&& betBall.split(",")[1].contains(results[1])
					&& betBall.split(",")[2].contains(results[2])
					&& betBall.split(",")[3].contains(results[3])
					&& betBall.split(",")[4].contains(results[4])) {
				winMoney = winMoney.add(betRate);
			}
			return winMoney;
		} else if (CqSscConstants.PLAY_TYPE_EIGHT.equals(playType)) {
			if (betBall.split(",")[0].contains(results[0])
					&& betBall.split(",")[1].contains(results[1])
					&& betBall.split(",")[2].contains(results[2])
					&& betBall.split(",")[3].contains(results[3])
					&& betBall.split(",")[4].contains(results[4])) {
				// 五星通选一等奖
				winMoney = winMoney.add(betRate);
			} else if ((betBall.split(",")[0].contains(results[0])
					&& betBall.split(",")[1].contains(results[1]) && betBall
					.split(",")[2].contains(results[2]))
					|| (betBall.split(",")[2].contains(results[2])
							&& betBall.split(",")[3].contains(results[3]) && betBall
							.split(",")[4].contains(results[4]))) {
				// "五星通选"二等奖，奖金220元
				winMoney = winMoney.add(new BigDecimal(220));
			} else {
				if (betBall.split(",")[0].contains(results[0])
						&& betBall.split(",")[1].contains(results[1])) {
					winMoney = winMoney.add(new BigDecimal(20));
				}
				if (betBall.split(",")[3].contains(results[3])
						&& betBall.split(",")[4].contains(results[4])) {
					winMoney = winMoney.add(new BigDecimal(20));
				}
			}
			return winMoney;
		}

		return winMoney;
	}

	public static String getAfterThree(String openResult) {//后三位
		String[] re = openResult.split(",");
		if (re.length >= 4) {
			if (re[2].equals(re[3]) && re[2].equals(re[4])) {
				return "豹子";
			} else if (!re[2].equals(re[3]) && !re[2].equals(re[4])
					&& !re[3].equals(re[4])) {
				return "组六";
			} else {
				return "组三";
			}
		} else {
			return "";
		}
	}

	/**
	 * 大小单双玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkZERO(String betBall) {
//		String regEx = "[大小单双]{1,4},[大小单双]{1,4}";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(betBall);
//		return matcher.matches();
		String reg = "^[^,]+,[^,]+$";
		Pattern pattern = Pattern.compile(reg);
		if(pattern.matcher(betBall).matches()){
			String tensBall = betBall.split(",")[0];
			String unitBall = betBall.split(",")[1];
			Pattern unitPattern = Pattern.compile("(?:([大小单双])(?!.*\\1)){1,4}");  
	        if(unitPattern.matcher(tensBall).matches()&&unitPattern.matcher(unitBall).matches()){
	        	return true;
	        }else{
	        	return false;
	        }
		}else{
			return false;
		}
	}
	/**
	 * 1一星玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkONE(String betBall) {
//		String regEx = "[0-9]{1,10}";
		String regEx = "(?:([0-9])(?!.*\\1)){1,10}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 2二星组选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkTWO(String betBall) {
		//2≤M≤7个号码 0,1,2,3,4,5,6
//		String regEx = "([0-9]{1},){1,6}([0-9]{1}){1}";
		String regEx = "(?:([0-9]),(?!.*\\1)){1,6}[0-9]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 3二星直选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkTHREE(String betBall) {
//		String regEx = "[0-9]{1,10},[0-9]{1,10}";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(betBall);
//		return matcher.matches();
		String reg = "^[^,]+,[^,]+$";
		Pattern pattern = Pattern.compile(reg);
		if(pattern.matcher(betBall).matches()){
			String tensBall = betBall.split(",")[0];
			String unitBall = betBall.split(",")[1];
			Pattern unitPattern = Pattern.compile("(?:([0-9])(?!.*\\1)){1,10}");  
	        if(unitPattern.matcher(tensBall).matches()&&unitPattern.matcher(unitBall).matches()){
	        	return true;
	        }else{
	        	return false;
	        }
		}else{
			return false;
		}
	}
	/**
	 * 4三星组六玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkFOUR(String betBall) {
//		String regEx = "([0-9]{1},){2,9}([0-9]{1}){1}";
		String regEx = "(?:([0-9]),(?!.*\\1)){0,9}[0-9]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 5三星组三玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkFIVE(String betBall) {
//		String regEx = "([0-9]{1},){1,9}([0-9]{1}){1}";
		String regEx = "(?:([0-9]),(?!.*\\1)){0,9}[0-9]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	/**
	 * 6三星直选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkSIX(String betBall) {
//		String regEx = "[0-9]{1,10},[0-9]{1,10},[0-9]{1,10}";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(betBall);
//		return matcher.matches();
		String reg = "^[^,]+,[^,]+,[^,]+$";
		Pattern pattern = Pattern.compile(reg);
		if(pattern.matcher(betBall).matches()){
			String precentBall = betBall.split(",")[0];
			String tensBall = betBall.split(",")[1];
			String unitBall = betBall.split(",")[2];
			Pattern unitPattern = Pattern.compile("(?:([0-9])(?!.*\\1)){1,10}");  
	        if(unitPattern.matcher(tensBall).matches()&&unitPattern.matcher(unitBall).matches()&&unitPattern.matcher(precentBall).matches()){
	        	return true;
	        }else{
	        	return false;
	        }
		}else{
			return false;
		}
	}
	/**
	 * 7五星直选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkSEVEN(String betBall) {
//		String regEx = "[0-9]{1,10},[0-9]{1,10},[0-9]{1,10},[0-9]{1,10},[0-9]{1,10}";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(betBall);
//		return matcher.matches();
		String reg = "^[^,]+,[^,]+,[^,]+,[^,]+,[^,]+$";
		Pattern pattern = Pattern.compile(reg);
		if(pattern.matcher(betBall).matches()){
			String myriabitBall = betBall.split(",")[0];
			String thousandsBall = betBall.split(",")[1];
			String precentBall = betBall.split(",")[2];
			String tensBall = betBall.split(",")[3];
			String unitBall = betBall.split(",")[4];
			Pattern unitPattern = Pattern.compile("(?:([0-9])(?!.*\\1)){1,10}");  
	        if(unitPattern.matcher(tensBall).matches()&&unitPattern.matcher(unitBall).matches()
	        		&&unitPattern.matcher(precentBall).matches()&&unitPattern.matcher(thousandsBall).matches()
	        		&&unitPattern.matcher(myriabitBall).matches()){
	        	return true;
	        }else{
	        	return false;
	        }
		}else{
			return false;
		}
	}
	/**
	 * 8五星通选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkEIGHT(String betBall) {
//		String regEx = "[0-9]{1,10},[0-9]{1,10},[0-9]{1,10},[0-9]{1,10},[0-9]{1,10}";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(betBall);
//		return matcher.matches();
		String reg = "^[^,]+,[^,]+,[^,]+,[^,]+,[^,]+$";
		Pattern pattern = Pattern.compile(reg);
		if(pattern.matcher(betBall).matches()){
			String myriabitBall = betBall.split(",")[0];
			String thousandsBall = betBall.split(",")[1];
			String precentBall = betBall.split(",")[2];
			String tensBall = betBall.split(",")[3];
			String unitBall = betBall.split(",")[4];
			Pattern unitPattern = Pattern.compile("(?:([0-9])(?!.*\\1)){1,10}");  
	        if(unitPattern.matcher(tensBall).matches()&&unitPattern.matcher(unitBall).matches()
	        		&&unitPattern.matcher(precentBall).matches()&&unitPattern.matcher(thousandsBall).matches()
	        		&&unitPattern.matcher(myriabitBall).matches()){
	        	return true;
	        }else{
	        	return false;
	        }
		}else{
			return false;
		}
	}
	
	
	
	

}
