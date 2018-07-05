package com.xy.hk.marksix.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apps.eff.CacheUtil;
import com.apps.util.Combination;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;

/**
 * 六合彩工具类
 * 
 * @author Mr.zang
 * 
 */
public class MarkSixUtil {

	/**
	 * 玩法一共有种 0=特码A 1=特码B 2=正码 3=正1特 4=正2特 5=正3特 6=正4特 7=正5特 8=正6特 9=正码1-6 10=过关
	 * 11=二全中 12=二中特 13=特串 14=三全中 15=三中二 16=四全中 17=半波 18=一肖 19=尾数 20=特码生肖 21=二肖
	 * 22=三肖 23=四肖 24=五肖 25=六肖 26=七肖 27=八肖 28=九肖 29=十肖 30=十一肖 31=二肖连中 32=三肖连中
	 * 33=四肖连中 34=五肖连中 35=二肖连不中 36=三肖连不中 37=四肖连不中 38=二尾连中 39=三尾连中 40=四尾连中
	 * 41=二尾连不中 42=三尾连不中 43=四尾连不中 44=五不中 45=六不中 46=七不中 47=八不中 48=九不中 49=十不中
	 * 50=十一不中 51=十二不中
	 */

	/**
	 * 特码开奖结果
	 */
	public static Map<String, Boolean> getTeResult(String result,Date startTime) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		String code = result.split("\\+")[1];
		map.put(code, true);
		int codeValue = Integer.parseInt(code);
		if (codeValue >= 1 && codeValue <= 10) {
			map.put("1-10", true);
		} else if (codeValue >= 11 && codeValue <= 20) {
			map.put("11-20", true);
		} else if (codeValue >= 21 && codeValue <= 30) {
			map.put("21-30", true);
		} else if (codeValue >= 31 && codeValue <= 40) {
			map.put("31-40", true);
		} else if (codeValue >= 41 && codeValue <= 49) {
			map.put("41-49", true);
		}
		if (codeValue >= 25 && codeValue != 49) {
			map.put("大", true);
		} else if (codeValue <= 24) {
			map.put("小", true);
		}

		if (codeValue >= 25 && codeValue != 49) {
			if (codeValue % 2 == 0) {
				map.put("大双", true);
			} else if (codeValue % 2 != 0 && codeValue != 49) {
				map.put("大单", true);
			}
		} else if (codeValue <= 24) {
			if (codeValue % 2 == 0) {
				map.put("小双", true);
			} else if (codeValue % 2 != 0) {
				map.put("小单", true);
			}
		}

		String zodiac = getZodiac(result,startTime);
		if ("牛、马、羊、鸡、狗、猪".indexOf(zodiac) > -1) {
			map.put("家禽", true);
		} else {
			map.put("野兽", true);
		}

		getSingleCode(code, map);
		return map;
	}

	/**
	 * * 正码开奖结果
	 */

	public static Map<String, Boolean> getZhengMaResult(String result) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		String code = result.split("\\+")[0];
		String code1 = result.split("\\+")[1];
		String array[] = code.split(",");
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			map.put(array[i], true);
			sum = Integer.parseInt(array[i]) + sum;
		}
		sum = Integer.parseInt(code1) + sum;
		if (sum >= 175) {
			map.put("总大", true);
		} else {
			map.put("总小", true);
		}
		if (sum % 2 == 0) {
			map.put("总双", true);
		} else {
			map.put("总单", true);
		}
		if (sum % 100 % 10 >= 5) {
			map.put("总尾大", true);
		} else {
			map.put("总尾小", true);
		}
		if (Integer.parseInt(array[0]) > Integer.parseInt(code1)) {
			map.put("龙", true);
		} else {
			map.put("虎", true);
		}
		return map;
	}

	/**
	 * 
	 * 正码特开奖结果 type指的是正几特 result 为本期开奖结果
	 */
	public static Map<String, Map<String, Boolean>> getZhengMaTeResult(
			String type, String result) {
		Map<String, Map<String, Boolean>> map = new HashMap<String, Map<String, Boolean>>();
		String code = result.split("\\+")[0];
		String array[] = code.split(",");
		if (type.equals("正1特")) {
			Map<String, Boolean> map1 = new HashMap<String, Boolean>();
			map1.put(array[0], true);
			map.put(type, map1);
		} else if (type.equals("正2特")) {
			Map<String, Boolean> map2 = new HashMap<String, Boolean>();
			map2.put(array[1], true);
			map.put(type, map2);
		} else if (type.equals("正3特")) {
			Map<String, Boolean> map3 = new HashMap<String, Boolean>();
			map3.put(array[2], true);
			map.put(type, map3);
		} else if (type.equals("正4特")) {
			Map<String, Boolean> map4 = new HashMap<String, Boolean>();
			map4.put(array[3], true);
			map.put(type, map4);
		} else if (type.equals("正5特")) {
			Map<String, Boolean> map5 = new HashMap<String, Boolean>();
			map5.put(array[4], true);
			map.put(type, map5);
		} else if (type.equals("正6特")) {
			Map<String, Boolean> map6 = new HashMap<String, Boolean>();
			map6.put(array[5], true);
			map.put(type, map6);
		}
		return map;
	}

	/**
	 * * 正码1-6开奖结果 type为投注项名字 ， result 为本期开奖结果
	 */

	public static Map<String, Map<String, Boolean>> getZhengMa16Result(
			String type, String result) {
		Map<String, Map<String, Boolean>> map = new HashMap<String, Map<String, Boolean>>();
		String code = result.split("\\+")[0];
		String array[] = code.split(",");
		if (type.equals("正码1")) {
			Map<String, Boolean> map1 = new HashMap<String, Boolean>();
			getSingleCode(array[0], map1);
			map.put(type, map1);
		} else if (type.equals("正码2")) {
			Map<String, Boolean> map2 = new HashMap<String, Boolean>();
			getSingleCode(array[1], map2);
			map.put(type, map2);
		} else if (type.equals("正码3")) {
			Map<String, Boolean> map3 = new HashMap<String, Boolean>();
			getSingleCode(array[2], map3);
			map.put(type, map3);
		} else if (type.equals("正码4")) {
			Map<String, Boolean> map4 = new HashMap<String, Boolean>();
			getSingleCode(array[3], map4);
			map.put(type, map4);
		} else if (type.equals("正码5")) {
			Map<String, Boolean> map5 = new HashMap<String, Boolean>();
			getSingleCode(array[4], map5);
			map.put(type, map5);
		} else if (type.equals("正码6")) {
			Map<String, Boolean> map6 = new HashMap<String, Boolean>();
			getSingleCode(array[5], map6);
			map.put(type, map6);
		}
		return map;
	}

	/**
	 * optionTitle 为投注项组合内容 如：正码1,大;正码1,红波;正码2,蓝波;正码3,单 result为开奖结果 过关开奖结果计算
	 * 如果中奖就返回 1&中奖赔率 ，如果不中奖就返回 状态2
	 */
	public static String getGuoguanResult(String optionTitle, String result) {
		String resultValue = "";
		Map<String, Map<String, Boolean>> map = new HashMap<String, Map<String, Boolean>>();
		BigDecimal rate = new BigDecimal(1);
		String code = result.split("\\+")[0];
		String array[] = code.split(",");
		String array1[] = optionTitle.split(";");
		int k = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals("49")) {// 开出来的有49
				k = i;
			}
		}
		if (k != -1) {
			if (k == 0) {// 第1个号是49
				for (int j = 0; j < array1.length; j++) {
					String type = array1[j].split(",")[0];
					String betTitle = array1[j].split(",")[1];
					if (type.equals("正码1")) {
						Map<String, Boolean> map1 = null;
						if (map.get(type) != null) {
							map1 = map.get(type);
						} else {
							map1 = new HashMap<String, Boolean>();
							getSingleCodeResult(array[0], map1);// 单个号码的大小单双波色结果
							map.put(type, map1);
						}
						if (betTitle.indexOf("波") > -1) {//
							if (betTitle.equals("绿波")) {
								rate = rate.multiply(new BigDecimal(2.875));
							} else {
								resultValue = "2";
								return resultValue;
							}
						} else {
							rate = rate.multiply(new BigDecimal(1));
						}
					} else if (type.equals("正码2")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[1], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码3")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[2], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码4")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[3], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码5")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[4], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码6")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[5], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					}
				}
				resultValue = "1&"
						+ rate.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
			} else if (k == 1) {// 第2个号是49
				for (int j = 0; j < array1.length; j++) {
					String type = array1[j].split(",")[0];
					String betTitle = array1[j].split(",")[1];
					if (type.equals("正码2")) {
						Map<String, Boolean> map1 = null;
						if (map.get(type) != null) {
							map1 = map.get(type);
						} else {
							map1 = new HashMap<String, Boolean>();
							getSingleCodeResult(array[1], map1);// 单个号码的大小单双波色结果
							map.put(type, map1);
						}
						if (betTitle.indexOf("波") > -1) {//
							if (betTitle.equals("绿波")) {
								rate = rate.multiply(new BigDecimal(2.875));
							} else {
								resultValue = "2";
								return resultValue;
							}
						} else {
							rate = rate.multiply(new BigDecimal(1));
						}
					} else if (type.equals("正码1")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[0], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码3")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[2], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码4")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[3], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码5")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[4], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码6")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[5], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					}
				}
				resultValue = "1&"
						+ rate.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
			} else if (k == 2) {// 第3个号是49
				for (int j = 0; j < array1.length; j++) {
					String type = array1[j].split(",")[0];
					String betTitle = array1[j].split(",")[1];
					if (type.equals("正码3")) {
						Map<String, Boolean> map1 = null;
						if (map.get(type) != null) {
							map1 = map.get(type);
						} else {
							map1 = new HashMap<String, Boolean>();
							getSingleCodeResult(array[2], map1);// 单个号码的大小单双波色结果
							map.put(type, map1);
						}
						if (betTitle.indexOf("波") > -1) {//
							if (betTitle.equals("绿波")) {
								rate = rate.multiply(new BigDecimal(2.875));
							} else {
								resultValue = "2";
								return resultValue;
							}
						} else {
							rate = rate.multiply(new BigDecimal(1));
						}
					} else if (type.equals("正码2")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[1], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码1")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[0], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码4")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[3], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码5")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[4], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码6")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[5], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					}
				}
				resultValue = "1&"
						+ rate.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
			} else if (k == 3) {// 第4个号是49
				for (int j = 0; j < array1.length; j++) {
					String type = array1[j].split(",")[0];
					String betTitle = array1[j].split(",")[1];
					if (type.equals("正码4")) {
						Map<String, Boolean> map1 = null;
						if (map.get(type) != null) {
							map1 = map.get(type);
						} else {
							map1 = new HashMap<String, Boolean>();
							getSingleCodeResult(array[3], map1);// 单个号码的大小单双波色结果
							map.put(type, map1);
						}
						if (betTitle.indexOf("波") > -1) {//
							if (betTitle.equals("绿波")) {
								rate = rate.multiply(new BigDecimal(2.875));
							} else {
								resultValue = "2";
								return resultValue;
							}
						} else {
							rate = rate.multiply(new BigDecimal(1));
						}
					} else if (type.equals("正码2")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[1], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码3")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[2], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码1")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[0], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码5")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[4], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码6")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[5], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					}
				}
				resultValue = "1&"
						+ rate.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
			} else if (k == 4) {// 第5个号是49
				for (int j = 0; j < array1.length; j++) {
					String type = array1[j].split(",")[0];
					String betTitle = array1[j].split(",")[1];
					if (type.equals("正码5")) {
						Map<String, Boolean> map1 = null;
						if (map.get(type) != null) {
							map1 = map.get(type);
						} else {
							map1 = new HashMap<String, Boolean>();
							getSingleCodeResult(array[4], map1);// 单个号码的大小单双波色结果
							map.put(type, map1);
						}
						if (betTitle.indexOf("波") > -1) {//
							if (betTitle.equals("绿波")) {
								rate = rate.multiply(new BigDecimal(2.875));
							} else {
								resultValue = "2";
								return resultValue;
							}
						} else {
							rate = rate.multiply(new BigDecimal(1));
						}
					} else if (type.equals("正码2")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[1], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码3")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[2], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码4")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[3], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码1")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[0], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码6")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[5], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					}
				}
				resultValue = "1&"
						+ rate.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
			} else if (k == 5) {// 第6个号是49
				for (int j = 0; j < array1.length; j++) {
					String type = array1[j].split(",")[0];
					String betTitle = array1[j].split(",")[1];
					if (type.equals("正码6")) {
						Map<String, Boolean> map1 = null;
						if (map.get(type) != null) {
							map1 = map.get(type);
						} else {
							map1 = new HashMap<String, Boolean>();
							getSingleCodeResult(array[5], map1);// 单个号码的大小单双波色结果
							map.put(type, map1);
						}
						if (betTitle.indexOf("波") > -1) {//
							if (betTitle.equals("绿波")) {
								rate = rate.multiply(new BigDecimal(2.875));
							} else {
								resultValue = "2";
								return resultValue;
							}
						} else {
							rate = rate.multiply(new BigDecimal(1));
						}
					} else if (type.equals("正码2")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[1], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码3")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[2], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码4")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[3], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码5")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[4], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					} else if (type.equals("正码1")) {
						String resultMap1 = win(type, betTitle, map, rate,
								array[0], resultValue);
						if (resultMap1 != null) {
							return resultMap1;
						}
					}
				}
				resultValue = "1&"
						+ rate.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
			}
		} else {// 没有号码是49，只需要判断用户是否中奖
			for (int j = 0; j < array1.length; j++) {
				String type = array1[j].split(",")[0];
				String betTitle = array1[j].split(",")[1];
				if (type.equals("正码1")) {
					String resultMap1 = win(type, betTitle, map, rate,
							array[0], resultValue);
					if (resultMap1 != null) {
						return resultMap1;
					}
				} else if (type.equals("正码2")) {
					String resultMap1 = win(type, betTitle, map, rate,
							array[1], resultValue);
					if (resultMap1 != null) {
						return resultMap1;
					}
				} else if (type.equals("正码3")) {
					String resultMap1 = win(type, betTitle, map, rate,
							array[2], resultValue);
					if (resultMap1 != null) {
						return resultMap1;
					}
				} else if (type.equals("正码4")) {
					String resultMap1 = win(type, betTitle, map, rate,
							array[3], resultValue);
					if (resultMap1 != null) {
						return resultMap1;
					}
				} else if (type.equals("正码5")) {
					String resultMap1 = win(type, betTitle, map, rate,
							array[4], resultValue);
					if (resultMap1 != null) {
						return resultMap1;
					}
				} else if (type.equals("正码6")) {
					String resultMap1 = win(type, betTitle, map, rate,
							array[5], resultValue);
					if (resultMap1 != null) {
						return resultMap1;
					}
				}
			}
			resultValue = "1&"
					+ rate.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
		}
		return resultValue;
	}

	/**
	 * 计算是否中奖 若果不中奖 直接返回状态2 中奖的话返回空，里面的赔率做乘积
	 * 
	 * @param type
	 * @param betTitle
	 * @param map
	 * @param rate
	 * @param singleCode
	 * @param resultMap
	 * @return
	 */

	public static String win(String type, String betTitle,
			Map<String, Map<String, Boolean>> map, BigDecimal rate,
			String singleCode, String resultValue) {
		Map<String, Boolean> map2 = null;
		if (map.get(type) != null) {
			map2 = map.get(type);
		} else {
			map2 = new HashMap<String, Boolean>();
			getSingleCodeResult(singleCode, map2);
			map.put(type, map2);
		}
		if (map2.get(betTitle) != null && map2.get(betTitle) == true) {
			if (betTitle.indexOf("波") > -1) {//
				if (betTitle.equals("红波")) {
					rate = rate.multiply(new BigDecimal(2.7));
				} else {
					rate = rate.multiply(new BigDecimal(2.85));
				}
			} else {
				rate = rate.multiply(new BigDecimal(1.9));
			}
			return null;
		} else {
			resultValue = "2";// 不中奖
			return resultValue;
		}
	}

	/**
	 * 二全中开奖 返回是否中奖，中奖几注一级中奖结果 2=不中奖 1=中奖 中奖返回的格式 ： 1&中奖数量&中奖内容
	 */
	public static String getTwoQuanZhong(Integer money,String optionTitle, String result) {
		String code = result.split("\\+")[0];
		List<String> list = new ArrayList<String>();
		String array[] = code.split(",");
		String array1[] = optionTitle.split(",");
		// 单注投注金额
		int singleMoney = money / (getTotal(array1.length, 2));
		for (int i = 0; i < array1.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (Integer.parseInt(array[j]) == Integer.parseInt(array1[i])) {
					list.add(array1[i]);
					break;
				}
			}
		}
		if (list.size() < 2) {// 少于两个说明未中奖
			return "2";
		} else {
			String winResult = "";
			int total = getTotal(list.size(), 2);// 总共中奖多少注
			List<String> resultList = combine(
					(String[]) list.toArray(new String[list.size()]), 2);
			for (String str : resultList) {
				winResult = winResult + str + ";";
			}
			winResult = winResult.substring(0, winResult.length() - 1);
			return "1&" + total + "&" + winResult+"&"+singleMoney;
		}
	}

	/**
	 * 二中特开奖 返回是否中奖，中奖几注以及中奖赔率和结果 中奖返回的格式 ： 1_中奖数量&赔率&单注投注金额&内容_
	 * 中奖数量&赔率&单注投注金额&内容 不中奖直接返回2 optionTitle是投注的内容 result 开奖结果
	 */
	public static String getTwoZhongTe(Integer money, String optionTitle,
			String result) {

		String code = result.split("\\+")[0];
		String code1 = result.split("\\+")[1];
		List<String> list = new ArrayList<String>();
		String array[] = code.split(",");
		String array1[] = optionTitle.split(",");
		// 单注投注金额
		int singleMoney = money / (getTotal(array1.length, 2));
		// Map<String,String> map=new HashMap<String,String>();//正码map
		Map<String, String> map1 = new HashMap<String, String>();// 特码map

		boolean flag = false;
		map1.put(Integer.parseInt(code1) + "", Integer.parseInt(code1) + "");
		for (int i = 0; i < array1.length; i++) {
			if (map1.get(Integer.parseInt(array1[i]) + "") != null) {
				flag = true;
			}
			// map.put(Integer.parseInt(array[i])+"",
			// Integer.parseInt(array[i])+"");
			for (int j = 0; j < array.length; j++) {
				if (Integer.parseInt(array[j]) == Integer.parseInt(array1[i])) {
					list.add(array1[i]);
					break;
				}
			}
		}

		if (list.size() < 1) {// 小于一个不中奖
			return "2";
		} else {
			if (list.size() == 1) {
				if (flag) {
					return "1_1&50&" + singleMoney + "&" + code1 + ","
							+ list.get(0);// 赔率是50
				} else {
					return "2";
				}
			} else {
				if (flag) {
					String result1 = "";
					List<String> resultList = combine(
							(String[]) list.toArray(new String[list.size()]), 2);
					String winResult = "";
					for (String str : resultList) {
						winResult = winResult + str + ";";
					}
					winResult = winResult.substring(0, winResult.length() - 1);
					result1 = "1_" + resultList.size() + "&25&" + singleMoney
							+ "&" + winResult;// 赔率是25
					String val = "";
					for (String str : list) {
						val = val + code1 + "," + str + ";";
					}
					val = val.substring(0, val.length() - 1);
					result1 = result1 + "_" + list.size() + "&50&"
							+ singleMoney + "&" + val;// 赔率是50
					return result1;// 赔率是50
				} else {
					List<String> resultList = combine(
							(String[]) list.toArray(new String[list.size()]), 2);
					String winResult = "";
					for (String str : resultList) {
						winResult = winResult + str + ";";
					}
					winResult = winResult.substring(0, winResult.length() - 1);
					return "1_" + resultList.size() + "&25&" + singleMoney
							+ "&" + winResult;// 赔率是25
				}
			}
		}

	}

	/**
	 * 特串开奖 返回是否中奖，中奖几注以及中奖赔率和结果 中奖返回的格式 ： 1&中奖数量&单注投注金额&内容 不中奖直接返回2
	 * optionTitle是投注的内容 result 开奖结果
	 */
	public static String getTeChuan(Integer money, String optionTitle,
			String result) {

		String code = result.split("\\+")[0];
		String code1 = result.split("\\+")[1];
		List<String> list = new ArrayList<String>();
		String array[] = code.split(",");
		String array1[] = optionTitle.split(",");
		Map<String, String> map1 = new HashMap<String, String>();// 特码map
		int singleMoney = money / (getTotal(array1.length, 2));

		boolean flag = false;
		map1.put(Integer.parseInt(code1) + "", Integer.parseInt(code1) + "");
		for (int i = 0; i < array1.length; i++) {
			if (map1.get(Integer.parseInt(array1[i]) + "") != null) {
				flag = true;
			}
			for (int j = 0; j < array.length; j++) {
				if (Integer.parseInt(array[j]) == Integer.parseInt(array1[i])) {
					list.add(array1[i]);
					break;
				}
			}
		}

		if (list.size() < 1) {// 少于1个不中奖
			return "2";
		} else {
			if (list.size() == 1) {
				if (flag) {
					return "1&1&" + singleMoney + "&" + code1 + ","
							+ list.get(0);
				} else {
					return "2";
				}
			} else {
				if (flag) {
					String val = "";
					for (String str : list) {
						val = val + code1 + "," + str + ";";
					}
					val = val.substring(0, val.length() - 1);
					val = "1&" + list.size() + "&" + singleMoney + "&" + val;
					return val;
				} else {
					return "2";
				}
			}
		}
	}

	/**
	 * 三全中开奖 返回是否中奖，中奖几注一级中奖结果 2=不中奖 1=中奖 中奖返回的格式 ： 1&中奖数量&单注投注金额&中奖内容 不中奖直接返回2
	 */
	public static String getThreeQuanZhong(Integer money, String optionTitle,
			String result) {

		String code = result.split("\\+")[0];
		List<String> list = new ArrayList<String>();
		String array[] = code.split(",");
		String array1[] = optionTitle.split(",");

		int singleMoney = money / (getTotal(array1.length, 3));

		for (int i = 0; i < array1.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (Integer.parseInt(array1[i]) == Integer.parseInt(array[j])) {
					list.add(array1[i]);
					break;
				}
			}
		}
		if (list.size() < 3) {// 少于三个说明未中奖
			return "2";
		} else {
			String winResult = "";
			int total = getTotal(list.size(), 3);// 总共中奖多少注
			List<String> resultList = combine(
					(String[]) list.toArray(new String[list.size()]), 3);
			for (String str : resultList) {
				winResult = winResult + str + ";";
			}
			winResult = winResult.substring(0, winResult.length() - 1);
			return "1&" + total + "&" + singleMoney + "&" + winResult;
		}
	}

	/**
	 * 三中二开奖 返回是否中奖，中奖几注一级中奖结果 2=不中奖 1=中奖 中奖返回的格式 ： 1_中奖数量&赔率&单注投注金额&内容_
	 * 中奖数量&赔率&单注投注金额&内容 不中奖直接返回2
	 */
	public static String getThreeZhongTwo(int money, String optionTitle,
			String result) {

		String code = result.split("\\+")[0];

		List<String> list = new ArrayList<String>();
		List<String> restlist = new ArrayList<String>();
		String array[] = code.split(",");
		String array1[] = optionTitle.split(",");
		int singleMoney = money / (getTotal(array1.length, 3));
		for (int i = 0; i < array1.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (Integer.parseInt(array[j]) == Integer.parseInt(array1[i])) {
					list.add(array1[i]);
					break;
				} else {
					if (j == array.length - 1) {
						restlist.add(array1[i]);
					}
				}
			}
		}

		if (list.size() < 2) {
			return "2";
		} else {
			if (list.size() == 2) {
				String val = "";
				for (String str : restlist) {
					val = val + list.get(0) + "," + list.get(1) + "," + str
							+ ";";
				}
				val = val.substring(0, val.length() - 1);
				return "1_" + restlist.size() + "&20&" + singleMoney + "&"
						+ val;// 赔率是20
			} else {
				List<String> resultList = combine(
						(String[]) list.toArray(new String[list.size()]), 3);
				String val = "1_" + resultList.size() + "&110&" + singleMoney
						+ "&";// 赔率是110
				for (String str : resultList) {
					val = val + str + ";";
				}
				val = val.substring(0, val.length() - 1);
				String betVal = "";
				List<String> resultList1 = combine(
						(String[]) list.toArray(new String[list.size()]), 2);
				for (int i = 0; i < resultList1.size(); i++) {
					for (int j = 0; j < restlist.size(); j++) {
						betVal = betVal + resultList1.get(i) + ","
								+ restlist.get(j) + ";";
					}
				}
				betVal = betVal.substring(0, betVal.length() - 1);
				val = val + "_" + resultList1.size() * restlist.size() + "&20&"
						+ singleMoney + "&" + betVal;
				return val;
			}
		}
	}

	/**
	 * 四全中 返回是否中奖，中奖几注以及中奖赔率和结果 中奖返回的格式 ： 1&中奖数量&单注投注金额&内容,不中奖直接返回2
	 * 
	 */
	public static String getFourQuanZhong(int money, String optionTitle,
			String result) {
		String code = result.split("\\+")[0];
		List<String> list = new ArrayList<String>();
		String array[] = code.split(",");
		String array1[] = optionTitle.split(",");
		int singleMoney = money / (getTotal(array1.length, 4));
		for (int i = 0; i < array1.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (Integer.parseInt(array1[i]) == Integer.parseInt(array[j])) {
					list.add(array1[i]);
					break;
				}
			}
		}
		if (list.size() < 4) {// 少于四个说明未中奖
			return "2";
		} else {
			String winResult = "";
			int total = getTotal(list.size(), 4);// 总共中奖多少注
			List<String> resultList = combine(
					(String[]) list.toArray(new String[list.size()]), 4);
			for (String str : resultList) {
				winResult = winResult + str + ";";
			}
			winResult = winResult.substring(0, winResult.length() - 1);
			return "1&" + total + "&" + singleMoney + "&" + winResult;
		}
	}

	/**
	 * 半波 中奖结果
	 */
	public static Map<String, Boolean> getBanBo(String result) {
		String value = result.split("\\+")[1];
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if (Integer.parseInt(value) == 49) {
			map.put("和", true);
		} else {
			String red = "01,02,07,08,12,13,18,19,23,24,29,30,34,35,40,45,46";// 红波数字
			String blue = "03,04,09,10,14,15,20,25,26,31,36,37,41,42,47,48";// 蓝波数字
			String green = "05,06,11,16,17,21,22,27,28,32,33,38,39,43,44";// 绿波数字
			String val = "";
			if (red.indexOf(value) > -1) {
				val = "红";
			} else if (blue.indexOf(value) > -1) {
				val = "蓝";
			} else if (green.indexOf(value) > -1) {
				val = "绿";
			}
			String val1 = "";
			if (Integer.parseInt(value) >= 25) {
				val1 = "大";
			} else {
				val1 = "小";
			}
			String val2 = "";
			if (Integer.parseInt(value) % 2 == 0) {
				val2 = "双";
			} else {
				val2 = "单";
			}
			String val3 = "";
			int ten = Integer.parseInt(value) / 10;
			int one = Integer.parseInt(value) % 10;
			if ((ten + one) % 2 == 0) {
				val3 = "合双";
			} else {
				val3 = "合单";
			}
			map.put(val + val1, true);
			map.put(val + val2, true);
			map.put(val + val3, true);
		}
		return map;
	}

	/**
	 * 一肖开奖结果 2017年的
	 */
	public static Map<String, Boolean> getOneZodiac(Date startTime ,String result) {
		String value = result.split("\\+")[1];
		String code = result.split("\\+")[0] + "," + value;
		String array[] = code.split(",");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		String ji = "01,13,25,37,49";// 鸡 2017
		String shu = "10,22,34,46";// 鼠 2017
		String hu = "08,20,32,44";// 虎 2017
		String lon = "06,18,30,42";// 龙 2017
		String ma = "04,16,28,40";// 马 2017
		String hou = "02,14,26,38";// 猴 2017
		String gou = "12,24,36,48";// 狗 2017
		String niu = "09,21,33,45";// 牛 2017
		String tu = "07,19,31,43";// 兔 2017
		String she = "05,17,29,41";// 蛇 2017
		String yang = "03,15,27,39";// 羊 2017
		String zhu = "11,23,35,47";// 猪 2017
		
		int year2 = DateTimeUtil.getRightYear(startTime);
		int month = DateTimeUtil.getRightMonth(startTime)-1;
		int day = DateTimeUtil.getRightDay(startTime);
	    Calendar cal=Calendar.getInstance();
	    cal.set(year2, month, day);
	     
	    Lunar lunar=new Lunar(cal);
	    int year = lunar.year;
	    Map<String,String> lumap = CacheUtil.getMarkSixMap(String.valueOf(year), startTime);
		shu = lumap.get("shu");
		zhu = lumap.get("zhu");
		gou = lumap.get("gou");
		ji = lumap.get("ji");
		hou = lumap.get("hou");
		yang = lumap.get("yang");
		ma = lumap.get("ma");
		she = lumap.get("she");
		lon = lumap.get("lon");
		tu = lumap.get("tu");
		hu = lumap.get("hu");
		niu = lumap.get("niu");
		
		for (int i = 0; i < array.length; i++) {
			if (ji.indexOf(array[i]) > -1) {
				map.put("鸡", true);
			} else if (shu.indexOf(array[i]) > -1) {
				map.put("鼠", true);
			} else if (niu.indexOf(array[i]) > -1) {
				map.put("牛", true);
			} else if (ma.indexOf(array[i]) > -1) {
				map.put("马", true);
			} else if (lon.indexOf(array[i]) > -1) {
				map.put("龙", true);
			} else if (she.indexOf(array[i]) > -1) {
				map.put("蛇", true);
			} else if (tu.indexOf(array[i]) > -1) {
				map.put("兔", true);
			} else if (zhu.indexOf(array[i]) > -1) {
				map.put("猪", true);
			} else if (gou.indexOf(array[i]) > -1) {
				map.put("狗", true);
			} else if (yang.indexOf(array[i]) > -1) {
				map.put("羊", true);
			} else if (hu.indexOf(array[i]) > -1) {
				map.put("虎", true);
			} else if (hou.indexOf(array[i]) > -1) {
				map.put("猴", true);
			}
		}
		return map;
	}

	/**
	 * 尾数开奖结果
	 */

	public static Map<String, Boolean> getTailNum(String result) {
		String value = result.split("\\+")[1];
		String code = result.split("\\+")[0] + "," + value;
		String array[] = code.split(",");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		for (int i = 0; i < array.length; i++) {
			if (Integer.parseInt(array[i]) % 10 == 0) {
				map.put("0尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 1) {
				map.put("1尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 2) {
				map.put("2尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 3) {
				map.put("3尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 4) {
				map.put("4尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 5) {
				map.put("5尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 6) {
				map.put("6尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 7) {
				map.put("7尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 8) {
				map.put("8尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 9) {
				map.put("9尾", true);
			}
		}
		return map;
	}

	/**
	 * 特码生肖 开奖结果
	 */
	public static Map<String, Boolean> getailTeZodiac(Date startTime,String result) {
		String value = result.split("\\+")[1];
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		String ji = "01,13,25,37,49";// 鸡 2017
		String shu = "10,22,34,46";// 鼠 2017
		String hu = "	08,20,32,44";// 虎 2017
		String lon = "06,18,30,42";// 龙 2017
		String ma = "04,16,28,40";// 马 2017
		String hou = "02,14,26,38";// 猴 2017
		String gou = "12,24,36,48";// 狗 2017
		String niu = "09,21,33,45";// 牛 2017
		String tu = "07,19,31,43";// 兔 2017
		String she = "05,17,29,41";// 蛇 2017
		String yang = "03,15,27,39";// 羊 2017
		String zhu = "11,23,35,47";// 猪 2017


		int year2 = DateTimeUtil.getRightYear(startTime);
		int month = DateTimeUtil.getRightMonth(startTime)-1;
		int day = DateTimeUtil.getRightDay(startTime);
	    Calendar cal=Calendar.getInstance();
	    cal.set(year2, month, day);
	     
	    Lunar lunar=new Lunar(cal);
	    int year = lunar.year;
	    Map<String,String> lumap = CacheUtil.getMarkSixMap(String.valueOf(year), startTime);
		shu = lumap.get("shu");
		zhu = lumap.get("zhu");
		gou = lumap.get("gou");
		ji = lumap.get("ji");
		hou = lumap.get("hou");
		yang = lumap.get("yang");
		ma = lumap.get("ma");
		she = lumap.get("she");
		lon = lumap.get("lon");
		tu = lumap.get("tu");
		hu = lumap.get("hu");
		niu = lumap.get("niu");

		System.out.println("hu"+hu);
		
		if (ji.indexOf(value) > -1) {
			map.put("鸡", true);
		} else if (shu.indexOf(value) > -1) {
			map.put("鼠", true);
		} else if (niu.indexOf(value) > -1) {
			map.put("牛", true);
		} else if (ma.indexOf(value) > -1) {
			map.put("马", true);
		} else if (lon.indexOf(value) > -1) {
			map.put("龙", true);
		} else if (she.indexOf(value) > -1) {
			map.put("蛇", true);
		} else if (tu.indexOf(value) > -1) {
			map.put("兔", true);
		} else if (zhu.indexOf(value) > -1) {
			map.put("猪", true);
		} else if (gou.indexOf(value) > -1) {
			map.put("狗", true);
		} else if (yang.indexOf(value) > -1) {
			map.put("羊", true);
		} else if (hu.indexOf(value) > -1) {
			map.put("虎", true);
		} else if (hou.indexOf(value) > -1) {
			map.put("猴", true);
		}
		return map;
	}

	/**
	 * 合肖(通用) 开奖结果 1=中奖 2=不中奖 3=打和
	 */
	public static String getHeZodiac(String optionTitle, String result,Date startTime) {
		String value = result.split("\\+")[1];
		String resultValue = "";
		if (value.equals("49")) {// 和
			return "3";
		}
		// Map<String,Boolean> map=new HashMap<String,Boolean>();
		String ji = "01,13,25,37";// 鸡 2017
		String shu = "10,22,34,46";// 鼠 2017
		String hu = "	08,20,32,44";// 虎 2017
		String lon = "06,18,30,42";// 龙 2017
		String ma = "04,16,28,40";// 马 2017
		String hou = "02,14,26,38";// 猴 2017
		String gou = "12,24,36,48";// 狗 2017
		String niu = "09,21,33,45";// 牛 2017
		String tu = "07,19,31,43";// 兔 2017
		String she = "05,17,29,41";// 蛇 2017
		String yang = "03,15,27,39";// 羊 2017
		String zhu = "11,23,35,47";// 猪 2017
		int year2 = DateTimeUtil.getRightYear(startTime);
		int month = DateTimeUtil.getRightMonth(startTime)-1;
		int day = DateTimeUtil.getRightDay(startTime);
	    Calendar cal=Calendar.getInstance();
	    cal.set(year2, month, day);
	     
	    Lunar lunar=new Lunar(cal);
	    int year = lunar.year;
	    Map<String,String> lumap = CacheUtil.getMarkSixMap(String.valueOf(year), startTime);
		shu = lumap.get("shu");
		zhu = lumap.get("zhu");
		gou = lumap.get("gou");
		ji = lumap.get("ji");
		hou = lumap.get("hou");
		yang = lumap.get("yang");
		ma = lumap.get("ma");
		she = lumap.get("she");
		lon = lumap.get("lon");
		tu = lumap.get("tu");
		hu = lumap.get("hu");
		niu = lumap.get("niu");

		if (ji.indexOf(value) > -1) {
			resultValue = "鸡";
		} else if (shu.indexOf(value) > -1) {
			resultValue = "鼠";
		} else if (niu.indexOf(value) > -1) {
			resultValue = "牛";
		} else if (ma.indexOf(value) > -1) {
			resultValue = "马";
		} else if (lon.indexOf(value) > -1) {
			resultValue = "龙";
		} else if (she.indexOf(value) > -1) {
			resultValue = "蛇";
		} else if (tu.indexOf(value) > -1) {
			resultValue = "兔";
		} else if (zhu.indexOf(value) > -1) {
			resultValue = "猪";
		} else if (gou.indexOf(value) > -1) {
			resultValue = "狗";
		} else if (yang.indexOf(value) > -1) {
			resultValue = "羊";
		} else if (hu.indexOf(value) > -1) {
			resultValue = "虎";
		} else if (hou.indexOf(value) > -1) {
			resultValue = "猴";
		}
		if (optionTitle.indexOf(resultValue) > -1) {
			return "1";
		} else {
			return "2";
		}
	}

	/**
	 * 生肖连 二肖连中，三肖连中，四肖连中，五肖连中 投注结果， num表示是投的几肖 optionTitle为投注拼接的字符串（鼠,虎,龙,马）
	 * result是开奖结果
	 * 
	 * 返回1表示中奖 2表示不中奖 中奖之后返回的格式 1&中奖注数&单注投注金额&中奖内容
	 */
	public static String getZodiacLianZhong(int money, int num,
			String optionTitle, String result,Date startTime) {
		String resultValue = "";
		String value = result.split("\\+")[1];
		String code = result.split("\\+")[0] + "," + value;
		String array[] = code.split(",");
		String betArray[] = optionTitle.split(",");

		int singleMoney = money / (getTotal(betArray.length, num));

		Map<String, Boolean> map = new HashMap<String, Boolean>();
		String ji = "01,13,25,37,49";// 鸡 2017
		String shu = "10,22,34,46";// 鼠 2017
		String hu = "	08,20,32,44";// 虎 2017
		String lon = "06,18,30,42";// 龙 2017
		String ma = "04,16,28,40";// 马 2017
		String hou = "02,14,26,38";// 猴 2017
		String gou = "12,24,36,48";// 狗 2017
		String niu = "09,21,33,45";// 牛 2017
		String tu = "07,19,31,43";// 兔 2017
		String she = "05,17,29,41";// 蛇 2017
		String yang = "03,15,27,39";// 羊 2017
		String zhu = "11,23,35,47";// 猪 2017
		
		int year2 = DateTimeUtil.getRightYear(startTime);
		int month = DateTimeUtil.getRightMonth(startTime)-1;
		int day = DateTimeUtil.getRightDay(startTime);
	    Calendar cal=Calendar.getInstance();
	    cal.set(year2, month, day);
	     
	    Lunar lunar=new Lunar(cal);
	    int year = lunar.year;
	    Map<String,String> lumap = CacheUtil.getMarkSixMap(String.valueOf(year), startTime);
		shu = lumap.get("shu");
		zhu = lumap.get("zhu");
		gou = lumap.get("gou");
		ji = lumap.get("ji");
		hou = lumap.get("hou");
		yang = lumap.get("yang");
		ma = lumap.get("ma");
		she = lumap.get("she");
		lon = lumap.get("lon");
		tu = lumap.get("tu");
		hu = lumap.get("hu");
		niu = lumap.get("niu");

		for (int i = 0; i < array.length; i++) {
			if (ji.indexOf(array[i]) > -1) {
				map.put("鸡", true);
			} else if (shu.indexOf(array[i]) > -1) {
				map.put("鼠", true);
			} else if (niu.indexOf(array[i]) > -1) {
				map.put("牛", true);
			} else if (ma.indexOf(array[i]) > -1) {
				map.put("马", true);
			} else if (lon.indexOf(array[i]) > -1) {
				map.put("龙", true);
			} else if (she.indexOf(array[i]) > -1) {
				map.put("蛇", true);
			} else if (tu.indexOf(array[i]) > -1) {
				map.put("兔", true);
			} else if (zhu.indexOf(array[i]) > -1) {
				map.put("猪", true);
			} else if (gou.indexOf(array[i]) > -1) {
				map.put("狗", true);
			} else if (yang.indexOf(array[i]) > -1) {
				map.put("羊", true);
			} else if (hu.indexOf(array[i]) > -1) {
				map.put("虎", true);
			} else if (hou.indexOf(array[i]) > -1) {
				map.put("猴", true);
			}
		}
		List<String> winList = new ArrayList<String>();
		for (int i = 0; i < betArray.length; i++) {// 判断用户投注的选项有没有中奖
			if (map.get(betArray[i]) != null && map.get(betArray[i]) == true) {
				winList.add(betArray[i]);
			}
		}
		if (winList.size() < num) {// 如果中奖的个数小于用户投注的生肖数 表示没有中奖的
			return "2";
		} else {
			int total = getTotal(winList.size(), num);// 总共中奖多少注
			List<String> resultList = combine(
					(String[]) winList.toArray(new String[winList.size()]), num);
			for (String str : resultList) {
				resultValue = resultValue + str + ";";
			}
			resultValue = resultValue.substring(0, resultValue.length() - 1);
			return "1&" + total + "&" + singleMoney + "&" + resultValue;
		}
	}

	/**
	 * 生肖连 二肖连不中，三肖连不中，四肖连不中 投注结果 num表示是投的几肖连不中 optionTitle为投注拼接的字符串（鼠,虎,龙,马）
	 * result是开奖结果
	 * 
	 * 返回值 1表示中奖 2表示不中奖 中奖之后返回的格式：1&中奖注数&单注投注金额&中奖内容 不中奖直接返回2
	 */
	public static String getZodiacLianBuZhong(int money, int num,
			String optionTitle, String result,Date startTime) {
		String resultValue = "";
		String value = result.split("\\+")[1];
		String code = result.split("\\+")[0] + "," + value;
		String array[] = code.split(",");
		String betArray[] = optionTitle.split(",");

		int singleMoney = money / (getTotal(betArray.length, num));

		Map<String, Boolean> map = new HashMap<String, Boolean>();
		String ji = "01,13,25,37,49";// 鸡 2017
		String shu = "10,22,34,46";// 鼠 2017
		String hu = "	08,20,32,44";// 虎 2017
		String lon = "06,18,30,42";// 龙 2017
		String ma = "04,16,28,40";// 马 2017
		String hou = "02,14,26,38";// 猴 2017
		String gou = "12,24,36,48";// 狗 2017
		String niu = "09,21,33,45";// 牛 2017
		String tu = "07,19,31,43";// 兔 2017
		String she = "05,17,29,41";// 蛇 2017
		String yang = "03,15,27,39";// 羊 2017
		String zhu = "11,23,35,47";// 猪 2017
		int year2 = DateTimeUtil.getRightYear(startTime);
		int month = DateTimeUtil.getRightMonth(startTime)-1;
		int day = DateTimeUtil.getRightDay(startTime);
	    Calendar cal=Calendar.getInstance();
	    cal.set(year2, month, day);
	     
	    Lunar lunar=new Lunar(cal);
	    int year = lunar.year;
	    Map<String,String> lumap = CacheUtil.getMarkSixMap(String.valueOf(year), startTime);
		shu = lumap.get("shu");
		zhu = lumap.get("zhu");
		gou = lumap.get("gou");
		ji = lumap.get("ji");
		hou = lumap.get("hou");
		yang = lumap.get("yang");
		ma = lumap.get("ma");
		she = lumap.get("she");
		lon = lumap.get("lon");
		tu = lumap.get("tu");
		hu = lumap.get("hu");
		niu = lumap.get("niu");

		for (int i = 0; i < array.length; i++) {
			if (ji.indexOf(array[i]) > -1) {
				map.put("鸡", true);
			} else if (shu.indexOf(array[i]) > -1) {
				map.put("鼠", true);
			} else if (niu.indexOf(array[i]) > -1) {
				map.put("牛", true);
			} else if (ma.indexOf(array[i]) > -1) {
				map.put("马", true);
			} else if (lon.indexOf(array[i]) > -1) {
				map.put("龙", true);
			} else if (she.indexOf(array[i]) > -1) {
				map.put("蛇", true);
			} else if (tu.indexOf(array[i]) > -1) {
				map.put("兔", true);
			} else if (zhu.indexOf(array[i]) > -1) {
				map.put("猪", true);
			} else if (gou.indexOf(array[i]) > -1) {
				map.put("狗", true);
			} else if (yang.indexOf(array[i]) > -1) {
				map.put("羊", true);
			} else if (hu.indexOf(array[i]) > -1) {
				map.put("虎", true);
			} else if (hou.indexOf(array[i]) > -1) {
				map.put("猴", true);
			}
		}
		List<String> winList = new ArrayList<String>();
		for (int i = 0; i < betArray.length; i++) {
			if (map.get(betArray[i]) != null && map.get(betArray[i]) == true) {
			} else {// 用户投注的不在开奖范围内的为中奖 加入数组
				winList.add(betArray[i]);
			}
		}
		if (winList.size() < num) {// 如果中奖的个数小于用户投注的生肖数 表示没有中奖的
			return "2";
		} else {
			int total = getTotal(winList.size(), num);// 总共中奖多少注
			List<String> resultList = combine(
					(String[]) winList.toArray(new String[winList.size()]), num);
			for (String str : resultList) {
				resultValue = resultValue + str + ";";
			}
			resultValue = resultValue.substring(0, resultValue.length() - 1);
			return "1&" + total + "&" + singleMoney + "&" + resultValue;
		}
	}

	/**
	 * 尾数连 二尾连中，三尾连中，四尾连中 投注结果， num表示是投的几尾 optionTitle为投注拼接的字符串（如：1尾,2尾,5尾）
	 * result是开奖结果
	 * 
	 * 返回1表示中奖 2表示不中奖 中奖之后返回的格式 1&中奖注数&单注投注金额&中奖内容
	 */
	public static String getTailLianZhong(int money, int num,
			String optionTitle, String result) {
		String resultValue = "";
		String value = result.split("\\+")[1];
		String code = result.split("\\+")[0] + "," + value;
		String array[] = code.split(",");

		String betArray[] = optionTitle.split(",");
		int singleMoney = money / (getTotal(betArray.length, num));
		Map<String, Boolean> map = new HashMap<String, Boolean>();

		for (int i = 0; i < array.length; i++) {
			if (Integer.parseInt(array[i]) % 10 == 0) {
				map.put("0尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 1) {
				map.put("1尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 2) {
				map.put("2尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 3) {
				map.put("3尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 4) {
				map.put("4尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 5) {
				map.put("5尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 6) {
				map.put("6尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 7) {
				map.put("7尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 8) {
				map.put("8尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 9) {
				map.put("9尾", true);
			}
		}
		List<String> winList = new ArrayList<String>();
		for (int i = 0; i < betArray.length; i++) {// 判断用户投注的选项有没有中奖
			if (map.get(betArray[i]) != null && map.get(betArray[i]) == true) {
				winList.add(betArray[i]);
			}
		}
		if (winList.size() < num) {// 如果中奖的个数小于用户投注的生肖数 表示没有中奖的
			return "2";
		} else {
			int total = getTotal(winList.size(), num);// 总共中奖多少注
			List<String> resultList = combine(
					(String[]) winList.toArray(new String[winList.size()]), num);
			for (String str : resultList) {
				resultValue = resultValue + str + ";";
			}
			resultValue = resultValue.substring(0, resultValue.length() - 1);
			return "1&" + total + "&" + singleMoney + "&" + resultValue;
		}
	}

	/**
	 * 尾数连 二尾连不中，三尾连不中，四尾连不中 投注结果， num表示是投的几肖 optionTitle为投注拼接的字符串（如：1尾,2尾,5尾）
	 * result是开奖结果
	 * 
	 * 返回1表示中奖 2表示不中奖 中奖之后返回的格式 1&中奖注数&单注投注金额&中奖内容
	 */
	public static String getTailLianBuZhong(int money, int num,
			String optionTitle, String result) {
		String resultValue = "";
		String value = result.split("\\+")[1];
		String code = result.split("\\+")[0] + "," + value;
		String array[] = code.split(",");

		String betArray[] = optionTitle.split(",");
		int singleMoney = money / (getTotal(betArray.length, num));
		Map<String, Boolean> map = new HashMap<String, Boolean>();

		for (int i = 0; i < array.length; i++) {
			if (Integer.parseInt(array[i]) % 10 == 0) {
				map.put("0尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 1) {
				map.put("1尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 2) {
				map.put("2尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 3) {
				map.put("3尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 4) {
				map.put("4尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 5) {
				map.put("5尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 6) {
				map.put("6尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 7) {
				map.put("7尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 8) {
				map.put("8尾", true);
			} else if (Integer.parseInt(array[i]) % 10 == 9) {
				map.put("9尾", true);
			}
		}
		List<String> winList = new ArrayList<String>();
		for (int i = 0; i < betArray.length; i++) {// 判断用户投注的选项有没有中奖
			if (map.get(betArray[i]) != null && map.get(betArray[i]) == true) {
			} else {
				winList.add(betArray[i]);
			}
		}
		if (winList.size() < num) {// 如果中奖的个数小于用户投注的生肖数 表示没有中奖的
			return "2";
		} else {
			int total = getTotal(winList.size(), num);// 总共中奖多少注
			List<String> resultList = combine(
					(String[]) winList.toArray(new String[winList.size()]), num);
			for (String str : resultList) {
				resultValue = resultValue + str + ";";
			}
			resultValue = resultValue.substring(0, resultValue.length() - 1);
			return "1&" + total + "&" + singleMoney + "&" + resultValue;
		}
	}

	/**
	 * 全不中（通用） 开奖结果 num表示是投的几不中（例：九不中则 num=9） optionTitle为投注拼接的字符串（如：11,23,35）
	 * result是开奖结果
	 */
	public static String getAllNotWin(int money, int num, String optionTitle,
			String result) {
		String resultValue = "";
		String value = result.split("\\+")[1];
		String code = result.split("\\+")[0] + "," + value;
		String array[] = code.split(",");

		String betArray[] = optionTitle.split(",");

		int singleMoney = money / (getTotal(betArray.length, num));

		Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();

		for (int i = 0; i < array.length; i++) {
			map.put(Integer.parseInt(array[i]), true);
		}
		List<String> winList = new ArrayList<String>();
		for (int i = 0; i < betArray.length; i++) {// 判断用户投注的选项有没有中奖
			if (map.get(Integer.parseInt(betArray[i])) != null
					&& map.get(Integer.parseInt(betArray[i])) == true) {
			} else {
				winList.add(betArray[i]);
			}
		}
		if (winList.size() < num) {// 如果中奖的个数小于用户投注的生肖数 表示没有中奖的
			return "2";
		} else {
			int total = getTotal(winList.size(), num);// 总共中奖多少注
			List<String> resultList = combine(
					(String[]) winList.toArray(new String[winList.size()]), num);
			for (String str : resultList) {
				resultValue = resultValue + str + ";";
			}
			resultValue = resultValue.substring(0, resultValue.length() - 1);
			return "1&" + total + "&" + singleMoney + "&" + resultValue;
		}
	}

	/**
	 * 根据数字获取生肖 2017年数字代表的生肖
	 * 
	 * 鸡 : 1 , 13 , 25 , 37 , 49 猴 : 2 , 14 , 26 , 38 羊 : 3 , 15 , 27 , 39 马 : 4
	 * , 16 , 28 , 40 蛇 : 5 , 17 , 29 , 41 龙 : 6 , 18 , 30 , 42 兔 : 7 , 19 , 31
	 * , 43 虎 : 8 , 20 , 32 , 44 牛 : 9 , 21 , 33 , 45 鼠 : 10 , 22 , 34 , 46 猪 :
	 * 11 , 23 , 35 , 47 狗 : 12 , 24 , 36 , 48
	 */
	public static String getZodiac(String result,Date startTime) {
		String code = result.split("\\+")[1];
//		int codeValue = Integer.parseInt(code);
		String ji = "01,13,25,37,49";// 鸡 2017
		String shu = "10,22,34,46";// 鼠 2017
		String hu = "	08,20,32,44";// 虎 2017
		String lon = "06,18,30,42";// 龙 2017
		String ma = "04,16,28,40";// 马 2017
		String hou = "02,14,26,38";// 猴 2017
		String gou = "12,24,36,48";// 狗 2017
		String niu = "09,21,33,45";// 牛 2017
		String tu = "07,19,31,43";// 兔 2017
		String she = "05,17,29,41";// 蛇 2017
		String yang = "03,15,27,39";// 羊 2017
		String zhu = "11,23,35,47";// 猪 2017
		int year2 = DateTimeUtil.getRightYear(startTime);
		int month = DateTimeUtil.getRightMonth(startTime)-1;
		int day = DateTimeUtil.getRightDay(startTime);
	    Calendar cal=Calendar.getInstance();
	    cal.set(year2, month, day);
	     
	    Lunar lunar=new Lunar(cal);
	    int year = lunar.year;
	    Map<String,String> lumap = CacheUtil.getMarkSixMap(String.valueOf(year), startTime);
		shu = lumap.get("shu");
		zhu = lumap.get("zhu");
		gou = lumap.get("gou");
		ji = lumap.get("ji");
		hou = lumap.get("hou");
		yang = lumap.get("yang");
		ma = lumap.get("ma");
		she = lumap.get("she");
		lon = lumap.get("lon");
		tu = lumap.get("tu");
		hu = lumap.get("hu");
		niu = lumap.get("niu");

		if (ji.indexOf(code) > -1) {
			return "鸡";
		} else if (shu.indexOf(code) > -1) {
			return "鼠";
		} else if (niu.indexOf(code) > -1) {
			return "牛";
		} else if (ma.indexOf(code) > -1) {
			return "马";
		} else if (lon.indexOf(code) > -1) {
			return "龙";
		} else if (she.indexOf(code) > -1) {
			return "蛇";
		} else if (tu.indexOf(code) > -1) {
			return "兔";
		} else if (zhu.indexOf(code) > -1) {
			return "猪";
		} else if (gou.indexOf(code) > -1) {
			return "狗";
		} else if (yang.indexOf(code) > -1) {
			return "羊";
		} else if (hu.indexOf(code) > -1) {
			return "虎";
		} else if (hou.indexOf(code) > -1) {
			return "猴";
		}

		return "";
	}

	/**
	 * 计算单个号的大小单双合大合小，尾大尾小 value是单个号码的值
	 * 
	 * @param args
	 */

	public static void getSingleCode(String value, Map<String, Boolean> map) {
		int codeValue = Integer.parseInt(value);
		if (codeValue >= 25 && codeValue != 49) {
			map.put("大", true);
		} else if (codeValue <= 24) {
			map.put("小", true);
		}
		if (codeValue == 49) {
			map.put("和", true);
		}
		if (codeValue % 2 == 0) {
			map.put("双", true);
		} else if (codeValue % 2 != 0 && codeValue != 49) {
			map.put("单", true);
		}
		if (codeValue < 10) {
			if (codeValue <= 6) {
				map.put("合小", true);
			} else {
				map.put("合大", true);
			}
			if (codeValue % 2 == 0) {
				map.put("合双", true);
			} else if (codeValue % 2 != 0) {
				map.put("合单", true);
			}
		} else {
			if (codeValue != 49) {
				int one = codeValue % 10;
				int ten = codeValue / 10;
				if (one + ten <= 6) {
					map.put("合小", true);
				} else {
					map.put("合大", true);
				}
				if ((one + ten) % 2 == 0) {
					map.put("合双", true);
				} else {
					map.put("合单", true);
				}
			}
		}
		if (codeValue % 10 <= 4) {
			map.put("尾小", true);
		} else if (codeValue % 10 >= 4 && codeValue != 49) {
			map.put("尾大", true);
		}

		String red = "01,02,07,08,12,13,18,19,23,24,29,30,34,35,40,45,46";// 红波数字
		String blue = "03,04,09,10,14,15,20,25,26,31,36,37,41,42,47,48";// 蓝波数字
		String green = "05,06,11,16,17,21,22,27,28,32,33,38,39,43,44,49";// 绿波数字
		if (red.indexOf(value) > -1) {
			map.put("红波", true);
		} else if (blue.indexOf(value) > -1) {
			map.put("蓝波", true);
		} else if (green.indexOf(value) > -1) {
			map.put("绿波", true);
		}

	}

	/**
	 * 单个号码的大小单双波色结果
	 * 
	 * @param value
	 * @param map
	 */

	public static void getSingleCodeResult(String value,
			Map<String, Boolean> map) {
		int codeValue = Integer.parseInt(value);
		if (codeValue >= 25 && codeValue != 49) {
			map.put("大", true);
		} else if (codeValue <= 24) {
			map.put("小", true);
		}
		if (codeValue == 49) {
			map.put("和", true);
		}
		if (codeValue % 2 == 0) {
			map.put("双", true);
		} else if (codeValue % 2 != 0 && codeValue != 49) {
			map.put("单", true);
		}
		String red = "01,02,07,08,12,13,18,19,23,24,29,30,34,35,40,45,46";// 红波数字
		String blue = "03,04,09,10,14,15,20,25,26,31,36,37,41,42,47,48";// 蓝波数字
		String green = "05,06,11,16,17,21,22,27,28,32,33,38,39,43,44,49";// 绿波数字
		if (red.indexOf(value) > -1) {
			map.put("红波", true);
		} else if (blue.indexOf(value) > -1) {
			map.put("蓝波", true);
		} else if (green.indexOf(value) > -1) {
			map.put("绿波", true);
		}

	}

	/**
	 * 计算组合数 all是所有数量 min是最少几个为一组 即all里面选min个
	 * 
	 * @param all
	 * @param min
	 * @return
	 */
	public static int getTotal(int all, int min) {
		// int mTotal=1;
		// int nTotal=1;
		// for(int i=all;i>=(all-min+1);i--){
		// mTotal=mTotal*i;
		// }
		// for(int j=1;j<=min;j++){
		// nTotal=nTotal*j;
		// }
		Combination combination = new Combination();
		ArrayList<String> coms = new ArrayList<String>();
		try {
			coms = combination.getCombinations(all, min);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coms.size();
	}

	/**
	 * 实现的算法
	 * 
	 * @param a
	 *            数据数组
	 * @param num
	 *            M选N中 N的个数
	 * @return
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
		System.out.println("数据长度 " + list.size());
		return list;

	}

	public static void main(String args[]) {
		int i = 32;
//		System.out.println("key:"+key+" value"+map.get(key));
		 System.out.println(i%100%10);
		String red = "01,02,07,08,12,13,18,19,23,24,29,30,34,35,40,45,46";
		// System.out.println(red.indexOf("14"));
		// BigDecimal rate=new BigDecimal(3.14567);
		// System.out.println(rate.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

		// Class targetclass = JPEGImageEncoder.class;
		// String className = targetclass.getName();
		// className = className.replace('.', '/');
		// String resource = "/" + className + ".class";
		// URL url = targetclass.getResource(resource);
		// System.out.println(url.getFile());
		// 输出结果：
		// String result="01,13,25,37,49,23+15";
		// String value=result.split("\\+")[1];
		// String array[]=result.split("\\+")[0].split(",");
		// Map<Integer,Boolean> map=new HashMap<Integer,Boolean>();
		// for(int j=0;j<array.length;j++){
		// map.put(Integer.parseInt(array[j]), true);
		// }
		// System.out.println(map.get(Integer.parseInt("13")));
		// List<String> list=new ArrayList<String>(Arrays.asList(array));
		// System.out.println(list.size());

		// String type="正_1特".substring(1, 2);
		// String type1="正_1特".split("_")[1];
//		System.out.println(getTotal(13, 12));
		
		String ji = "01,13,25,37,49";// 鸡 2017
		String shu = "10,22,34,46";// 鼠 2017
		String hu = "08,20,32,44";// 虎 2017
		String lon = "06,18,30,42";// 龙 2017
		String ma = "04,16,28,40";// 马 2017
		String hou = "02,14,26,38";// 猴 2017
		String gou = "12,24,36,48";// 狗 2017
		String niu = "09,21,33,45";// 牛 2017
		String tu = "07,19,31,43";// 兔 2017
		String she = "05,17,29,41";// 蛇 2017
		String yang = "03,15,27,39";// 羊 2017
		String zhu = "11,23,35,47";// 猪 2017
		
		Map<String,String> map=new HashMap<String,String>();
		
		int year2 = DateTimeUtil.getRightYear(new Date());
		int month = DateTimeUtil.getRightMonth(new Date())-1;
		int day = DateTimeUtil.getRightDay(new Date());
	    Calendar cal=Calendar.getInstance();
	    cal.set(year2, month, day);
	    Lunar lunar=new Lunar(cal);
	    int year = lunar.year;
		if(ParamUtils.chkInteger(year)){
            if(((year-4)%12)==0){
        		shu = "01,13,25,37,49";
        		zhu = "02,14,26,38";
        		gou = "03,15,27,39";
        		ji = "04,16,28,40";
        		hou = "05,17,29,41";
        		yang = "06,18,30,42";
        		ma = "07,19,31,43";
        		she = "08,20,32,44";
        		lon = "09,21,33,45";
        		tu = "10,22,34,46";
        		hu = "11,23,35,47";
        		niu = "12,24,36,48";            
        	}else if(((year-4)%12)==1){
        		shu = "02,14,26,38";
        		zhu = "03,15,27,39";
        		gou = "04,16,28,40";
        		ji = "05,17,29,41";
        		hou = "06,18,30,42";
        		yang = "07,19,31,43";
        		ma = "08,20,32,44";
        		she = "09,21,33,45";
        		lon = "10,22,34,46";
        		tu = "11,23,35,47";
        		hu = "12,24,36,48";
        		niu = "01,13,25,37,49";
            }else if(((year-4)%12)==2){
        		shu = "03,15,27,39";
        		zhu = "04,16,28,40";
        		gou = "05,17,29,41";
        		ji = "06,18,30,42";
        		hou = "07,19,31,43";
        		yang = "08,20,32,44";
        		ma = "09,21,33,45";
        		she = "10,22,34,46";
        		lon = "11,23,35,47";
        		tu = "12,24,36,48";
        		hu = "01,13,25,37,49";
        		niu = "02,14,26,38";
            }else if(((year-4)%12)==3){
        		shu = "04,16,28,40";
        		zhu = "05,17,29,41";
        		gou = "06,18,30,42";
        		ji = "07,19,31,43";
        		hou = "08,20,32,44";
        		yang = "09,21,33,45";
        		ma = "10,22,34,46";
        		she = "11,23,35,47";
        		lon = "12,24,36,48";
        		tu = "01,13,25,37,49";
        		hu = "02,14,26,38";
        		niu = "03,15,27,39";
            }else if(((year-4)%12)==4){
        		shu = "05,17,29,41";
        		zhu = "06,18,30,42";
        		gou = "07,19,31,43";
        		ji = "08,20,32,44";
        		hou = "09,21,33,45";
        		yang = "10,22,34,46";
        		ma = "11,23,35,47";
        		she = "12,24,36,48";
        		lon = "01,13,25,37,49";
        		tu = "02,14,26,38";
        		hu = "03,15,27,39";
        		niu = "04,16,28,40";
            }else if(((year-4)%12)==5){
        		shu = "06,18,30,42";
        		zhu = "07,19,31,43";
        		gou = "08,20,32,44";
        		ji = "09,21,33,45";
        		hou = "10,22,34,46";
        		yang = "11,23,35,47";
        		ma = "12,24,36,48";
        		she = "01,13,25,37,49";
        		lon = "02,14,26,38";
        		tu = "03,15,27,39";
        		hu = "04,16,28,40";
        		niu = "05,17,29,41";
            }else if(((year-4)%12)==6){
        		shu = "07,19,31,43";
        		zhu = "08,20,32,44";
        		gou = "09,21,33,45";
        		ji = "10,22,34,46";
        		hou = "11,23,35,47";
        		yang = "12,24,36,48";
        		ma = "01,13,25,37,49";
        		she = "02,14,26,38";
        		lon = "03,15,27,39";
        		tu = "04,16,28,40";
        		hu = "05,17,29,41";
        		niu = "06,18,30,42";
            }else if(((year-4)%12)==7){
        		shu = "08,20,32,44";
        		zhu = "09,21,33,45";
        		gou = "10,22,34,46";
        		ji = "11,23,35,47";
        		hou = "12,24,36,48";
        		yang = "01,13,25,37,49";
        		ma = "02,14,26,38";
        		she = "03,15,27,39";
        		lon = "04,16,28,40";
        		tu = "05,17,29,41";
        		hu = "06,18,30,42";
        		niu = "07,19,31,43";
            }else if(((year-4)%12)==8){
        		shu = "09,21,33,45";
        		zhu = "10,22,34,46";
        		gou = "11,23,35,47";
        		ji = "12,24,36,48";
        		hou = "01,13,25,37,49";
        		yang = "02,14,26,38";
        		ma = "03,15,27,39";
        		she = "04,16,28,40";
        		lon = "05,17,29,41";
        		tu = "06,18,30,42";
        		hu = "07,19,31,43";
        		niu = "08,20,32,44";
            }else if(((year-4)%12)==9){ //2017年为鸡年
        		shu = "10,22,34,46";
        		zhu = "11,23,35,47";
        		gou = "12,24,36,48";
        		ji = "01,13,25,37,49";
        		hou = "02,14,26,38";
        		yang = "03,15,27,39";
        		ma = "04,16,28,40";
        		she = "05,17,29,41";
        		lon = "06,18,30,42";
        		tu = "07,19,31,43";
        		hu = "08,20,32,44";
        		niu = "09,21,33,45";
            }else if(((year-4)%12)==10){
        		shu = "11,23,35,47";
        		zhu = "12,24,36,48";
        		gou = "01,13,25,37,49";
        		ji = "02,14,26,38";
        		hou = "03,15,27,39";
        		yang = "04,16,28,40";
        		ma = "05,17,29,41";
        		she = "06,18,30,42";
        		lon = "07,19,31,43";
        		tu = "08,20,32,44";
        		hu = "09,21,33,45";
        		niu = "10,22,34,46";
            }else if(((year-4)%12)==11){
        		shu = "12,24,36,48";
        		zhu = "01,13,25,37,49";
        		gou = "02,14,26,38";
        		ji = "03,15,27,39";
        		hou = "04,16,28,40";
        		yang = "05,17,29,41";
        		ma = "06,18,30,42";
        		she = "07,19,31,43";
        		lon = "08,20,32,44";
        		tu = "09,21,33,45";
        		hu = "10,22,34,46";
        		niu = "11,23,35,47";
            }
		}
		map.put("shu", shu);
		map.put("zhu", zhu);
		map.put("gou", gou);
		map.put("ji", ji);
		map.put("hou", hou);
		map.put("yang", yang);
		map.put("ma", ma);
		map.put("she", she);
		map.put("lon", lon);
		map.put("tu", tu);
		map.put("hu", hu);
		map.put("niu", niu);
		System.out.println(map.size());
		
		
//		Map<String, Boolean> map1=getailTeZodiac(map,new Date(),"04,37,42,39,30,03+32");
		
//		for(String key:map1.keySet()){		
//			System.out.println("key:"+key+" value"+map1.get(key));
//		}
		// list.add(value);
		// for(String str:list){
		// System.out.println(str);
		// }
	}

}
