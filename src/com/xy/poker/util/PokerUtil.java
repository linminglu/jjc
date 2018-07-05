package com.xy.poker.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.framework.util.ParamUtils;

/**
 * 快乐扑克工具类
 * 
 * @author Mr.zang
 * 
 */
public class PokerUtil {

	/**
	 * 计算快乐扑克3 花色
	 * 
	 * @param result
	 *            开奖结果 "110,202,208" 共三个值，英文半角逗号分割，
	 *            每个值三位数，第一位代表花色（1.黑桃2.红桃3.梅花4.方片），后两位代表值
	 * 
	 * @return null代表开奖结果有误 ""空代表什么都不是 1.对子2.同花3.顺子4.同花顺5.豹子
	 */
	public static String chkHuase(String result) {
		if (!ParamUtils.chkString(result)) {
			return null;
		}
		String[] split = result.split(",");
		int length = split.length;
		if (length == 3) {
			// 分出花色和结果
			int[] huaseArray = new int[3];// 花色集合
			int[] zhiArray = new int[3];// 开奖值集合
			for (int i = 0; i < split.length; i++) {
				String single = split[i];
				int singleLength = single.length();
				if (singleLength == 3) {
					String singleHuase = single.substring(0, 1);// 单值的花色
					String singleZhi = single.substring(1, 3);// 单值的花色
					huaseArray[i] = Integer.valueOf(singleHuase);
					zhiArray[i] = Integer.valueOf(singleZhi);
				} else {
					return null;
				}
			}

			Arrays.sort(huaseArray);
			Arrays.sort(zhiArray);

			// 判断豹子
			if (chkBaozi(zhiArray)) {
				return "5";
			}
			// 判断对子
			if (chkDuizi(zhiArray)) {
				return "1";
			}
			// 判断同花顺
			if (chkShunzi(zhiArray) && chkTonghua(huaseArray)) {
				return "4";
			}
			// 判断同花
			if (chkTonghua(huaseArray)) {
				return "2";
			}
			// 判断顺子
			if (chkShunzi(zhiArray)) {
				return "3";
			}
		}
		return "";
	}
	/**
	 * 计算快乐扑克3 花色
	 * 
	 * @param result
	 *            开奖结果 "110,202,208" 共三个值，英文半角逗号分割，
	 *            每个值三位数，第一位代表花色（1.黑桃2.红桃3.梅花4.方片），后两位代表值
	 * 
	 * @return 开奖结果数值，前三位为实际开奖值，第四位为和，第五位为花色（0.散牌1.对子2.同花3.顺子4.同花顺5.豹子）
	 */
	public static String openVer(String result) {
		if (!ParamUtils.chkString(result)) {
			return null;
		}
		StringBuffer stringB=new StringBuffer();
		String[] split = result.split(",");
		int length = split.length;
		if (length == 3) {
			// 分出花色和结果
			int[] huaseArray = new int[3];// 花色集合
			int[] zhiArray = new int[3];// 开奖值集合
			for (int i = 0; i < split.length; i++) {
				String single = split[i];
				int singleLength = single.length();
				if (singleLength == 3) {
					String singleHuase = single.substring(0, 1);// 单值的花色
					String singleZhi = single.substring(1, 3);// 单值的花色
					huaseArray[i] = Integer.valueOf(singleHuase);
					zhiArray[i] = Integer.valueOf(singleZhi);
				} else {
					return null;
				}
			}
			int he=0;
			for (int i : zhiArray) {
				stringB.append(i+",");
				he=he+i;
			}
			he=he%10;//和取余
			
			stringB.append(he+",");

			Arrays.sort(huaseArray);
			Arrays.sort(zhiArray);
//			System.out.println("___花色：" + Arrays.toString(huaseArray));
//			System.out.println("___值值：" + Arrays.toString(zhiArray));

			String huase="0";
			// 判断豹子
			if (chkBaozi(zhiArray)) {
				huase="5";
			}
			// 判断对子
			if (chkDuizi(zhiArray)) {
				huase="1";
			}
			// 判断同花顺
			if (chkShunzi(zhiArray) && chkTonghua(huaseArray)) {
				huase="4";
			}
			// 判断同花
			if (chkTonghua(huaseArray)) {
				huase="2";
			}
			// 判断顺子
			if (chkShunzi(zhiArray)) {
				huase="3";
			}
			stringB.append(huase+",");
		}
		return stringB.substring(0, stringB.length()-1);
	}

	/**
	 * 判断是否是顺子
	 * 
	 * @param zhiArray
	 * @return
	 */
	public static boolean chkShunzi(int[] zhiArray) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("1,2,3", "1,2,3");
		map.put("2,3,4", "2,3,4");
		map.put("3,4,5", "3,4,5");
		map.put("4,5,6", "4,5,6");
		map.put("5,6,7", "5,6,7");
		map.put("6,7,8", "6,7,8");
		map.put("7,8,9", "7,8,9");
		map.put("8,9,10", "8,9,10");
		map.put("9,10,11", "9,10,11");
		map.put("10,11,12", "10,11,12");
		map.put("11,12,13", "11,12,13");
		map.put("1,12,13", "1,12,13");

		StringBuffer strB = new StringBuffer();
		for (int i = 0; i < zhiArray.length; i++) {
			strB.append(zhiArray[i] + ",");
		}
		String zhi = strB.substring(0, strB.length() - 1);
		String zhi2 = map.get(zhi);
		if (zhi.equals(zhi2)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是同花
	 * 
	 * @param huaseArray
	 * @return
	 */
	public static boolean chkTonghua(int[] huaseArray) {
		if (huaseArray[0] == huaseArray[1] && huaseArray[0] == huaseArray[2]
				&& huaseArray[1] == huaseArray[2]) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是豹子
	 * 
	 * @param zhiArray
	 * @return
	 */
	public static boolean chkBaozi(int[] zhiArray) {
		if (zhiArray[0] == zhiArray[1] && zhiArray[1] == zhiArray[2]
				&& zhiArray[0] == zhiArray[2]) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否对子
	 * 
	 * @param zhiArray
	 * @return
	 */
	public static boolean chkDuizi(int[] zhiArray) {
		if (zhiArray[0] == zhiArray[1] || zhiArray[0] == zhiArray[2]
				|| zhiArray[1] == zhiArray[2]) {
			return true;
		}
		return false;
	}

	// 1.对子2.同花3.顺子4.同花顺5.豹子
	public static void main(String[] args) {
		String result = "105,205,205";
		String chkHuase = chkHuase(result);
		if (chkHuase != null) {
			if ("1".equals(chkHuase)) {
				System.out.println("对子");
			} else if ("2".equals(chkHuase)) {
				System.out.println("同花");
			} else if ("3".equals(chkHuase)) {
				System.out.println("顺子");
			} else if ("4".equals(chkHuase)) {
				System.out.println("同花顺");
			} else if ("5".equals(chkHuase)) {
				System.out.println("豹子");
			} else {
				System.out.println("什么都不是");
			}
		} else {
			System.out.println("开奖结果有误");
		}
	}

}
