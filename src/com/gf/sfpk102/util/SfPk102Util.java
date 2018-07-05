package com.gf.sfpk102.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.util.Log;

import com.apps.eff.GameHelpUtil;
import com.gf.bjpk10.BjPk10Constants;
import com.gf.sfpk102.SfPk102Constants;

public class SfPk102Util {

	/**
	 * 计算投注总注数
	 * @param 
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
		if(SfPk102Constants.GAME_TYPE_GF_DAN_SHUANG_JI.equals(playType) 
				|| SfPk102Constants.GAME_TYPE_GF_DAN_SHUANG_YA.equals(playType)
				|| SfPk102Constants.GAME_TYPE_GF_DAN_SHUANG_GUAN.equals(playType) 
				|| SfPk102Constants.GAME_TYPE_GF_DAXI_AO_JI.equals(playType)
				|| SfPk102Constants.GAME_TYPE_GF_DAXI_AO_YA.equals(playType) 
				|| SfPk102Constants.GAME_TYPE_GF_DAXI_AO_GUAN.equals(playType)){// 大小单双共6种玩法
			String[] arr = betBall.split(",");
			totalBet = totalBet + arr.length;
		}else if(SfPk102Constants.GAME_TYPE_GF_DING_WEI_DAN_1_5.equals(playType)
				|| SfPk102Constants.GAME_TYPE_GF_DING_WEI_DAN_6_10.equals(playType)){// 定位胆
			String[] arr = betBall.split("\\|");
			totalBet = totalBet + getDingWeiDanBetTotal(arr);
		}else if(SfPk102Constants.GAME_TYPE_GF_QIAN_SAN.equals(playType)){// 前三
			String[] arr = betBall.split("\\|");
			if(arr.length != 3){
				Log.info("前三不正确");
				return 0;
			}
			totalBet = totalBet + getQianSanBetTotal(arr);
		}else if(SfPk102Constants.GAME_TYPE_GF_QIAN_ER.equals(playType)){// 前二
			String[] arr = betBall.split("\\|");
			if(arr.length != 2){
				Log.info("前二不正确");
				return 0;
			}
			totalBet = totalBet + getQianErBetTotal(arr);
		}else if(SfPk102Constants.GAME_TYPE_GF_QIAN_YI.equals(playType)){// 前一
			if (!"".equals(betBall)) {
				String[] arr = betBall.split(",");
				totalBet = totalBet + arr.length;
			} else {
				return 0;
			}
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
	
	/**
	 * 计算定位胆玩法总投注数
	 * @param arr
	 * @return
	 */
	public static int getDingWeiDanBetTotal(String[] arr) {
		int total = 0;
		int length = arr.length;
		for (int i = 0;i < length;i++) {
			String str = arr[i];
			if (!"".equals(str)) {
				total = total + str.split(",").length;
			}
		}
		return total;
	}
	
	/**
	 * 计算前三玩法总投注数
	 * @param arr
	 * @return
	 */
	public static int getQianSanBetTotal(String[] arr) {
		int total = 0;
		int length = arr.length;
		if (length != 3) {
			return 0;
		}
		String[] diyi = arr[0].split(",");
		String[] dier = arr[1].split(",");
		String[] disan = arr[2].split(",");
		if (diyi != null && dier != null && disan != null) {
			for (int i = 0;i < diyi.length;i++) {
				String yi = diyi[i];
				for (int j = 0;j < dier.length;j++) {
					String er = dier[j];
					for (int k = 0;k < disan.length;k++) {
						String san = disan[k];
						if (!yi.equals(er) && !yi.equals(san) && !er.equals(san)) {
							total = ++total;
						}
					}
				}
			}
		} else {
			return 0;
		}
		return total;
	}
	
	/**
	 * 计算前三玩法总投注数
	 * @param arr
	 * @return
	 */
	public static int getQianErBetTotal(String[] arr) {
		int total = 0;
		int length = arr.length;
		if (length != 2) {
			return 0;
		}
		String[] diyi = arr[0].split(",");
		String[] dier = arr[1].split(",");
		if (diyi != null && dier != null) {
			for (int i = 0;i < diyi.length;i++) {
				String yi = diyi[i];
				for (int j = 0;j < dier.length;j++) {
					String er = dier[j];
					if (!yi.equals(er)) {
						total = ++total;
					}
				}
			}
		} else {
			return 0;
		}
		return total;
	}
	
	
	/**
	 * 计算中奖的金额
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
		if (results.length == 10) {
			if(SfPk102Constants.GAME_TYPE_GF_DAN_SHUANG_JI.equals(playType)
					|| SfPk102Constants.GAME_TYPE_GF_DAN_SHUANG_YA.equals(playType)
					|| SfPk102Constants.GAME_TYPE_GF_DAN_SHUANG_GUAN.equals(playType)){// 单双
				String resVal = "";
				if (SfPk102Constants.GAME_TYPE_GF_DAN_SHUANG_JI.equals(playType)) {
					resVal = results[2];// 开奖结果的第三位(季军)
				} else if (SfPk102Constants.GAME_TYPE_GF_DAN_SHUANG_YA.equals(playType)) {
					resVal = results[1];// 开奖结果的第二位(亚军)
				} else if (SfPk102Constants.GAME_TYPE_GF_DAN_SHUANG_GUAN.equals(playType)) {
					resVal = results[0];// 开奖结果的第一位(冠军)
				}
				String danshuang = "";
				if ("01".equals(resVal) || "03".equals(resVal)|| "05".equals(resVal)
						||"07".equals(resVal) || "09".equals(resVal)) {
					danshuang = "单";
				} else if ("02".equals(resVal) || "04".equals(resVal)|| "06".equals(resVal)
						||"08".equals(resVal) || "10".equals(resVal)) {
					danshuang = "双";
				}
				
				String[] betBallArr = betBall.split(",");
				for (int i = 0;i < betBallArr.length;i++) {
					String betBallVal = betBallArr[i];
					if (danshuang.equals(betBallVal)) {
						winMoney = winMoney.add(betRate);
					}
				}
			} else if(SfPk102Constants.GAME_TYPE_GF_DAXI_AO_JI.equals(playType)
					|| SfPk102Constants.GAME_TYPE_GF_DAXI_AO_YA.equals(playType)
					|| SfPk102Constants.GAME_TYPE_GF_DAXI_AO_GUAN.equals(playType)){// 大小
				String resVal = "";
				if (SfPk102Constants.GAME_TYPE_GF_DAXI_AO_JI.equals(playType)) {
					resVal = results[2];// 开奖结果的第三位(季军)
				} else if (SfPk102Constants.GAME_TYPE_GF_DAXI_AO_YA.equals(playType)) {
					resVal = results[1];// 开奖结果的第二位(亚军)
				} else if (SfPk102Constants.GAME_TYPE_GF_DAXI_AO_GUAN.equals(playType)) {
					resVal = results[0];// 开奖结果的第一位(冠军)
				}
				String daxiao = "";
				if ("01".equals(resVal) || "02".equals(resVal)|| "03".equals(resVal)
						||"04".equals(resVal) || "05".equals(resVal)) {
					daxiao = "小";
				} else if ("06".equals(resVal) || "07".equals(resVal)|| "08".equals(resVal)
						||"09".equals(resVal) || "10".equals(resVal)) {
					daxiao = "大";
				}
				
				String[] betBallArr = betBall.split(",");
				for (int i = 0;i < betBallArr.length;i++) {
					String betBallVal = betBallArr[i];
					if (daxiao.equals(betBallVal)) {
						winMoney = winMoney.add(betRate);
					}
				}
			} else if (BjPk10Constants.GAME_TYPE_GF_DING_WEI_DAN_6_10.equals(playType)
					|| BjPk10Constants.GAME_TYPE_GF_DING_WEI_DAN_1_5.equals(playType)) { // 定位胆 投注格式为01,02|05|||
				String diyiRes = "";
				String dierRes = "";
				String disanRes = "";
				String disiRes = "";
				String diwuRes = "";
				if (BjPk10Constants.GAME_TYPE_GF_DING_WEI_DAN_6_10.equals(playType)) {// (6-10名)
					diyiRes = results[5];// 第六名结果
					dierRes = results[6];// 第七名结果
					disanRes = results[7];// 第八名结果
					disiRes = results[8];// 第九名结果
					diwuRes = results[9];// 第十名结果
				} else if (BjPk10Constants.GAME_TYPE_GF_DING_WEI_DAN_1_5.equals(playType)) {// (1-5名)
					diyiRes = results[0];// 第一名结果
					dierRes = results[1];// 第二名结果
					disanRes = results[2];// 第三名结果
					disiRes = results[3];// 第四名结果
					diwuRes = results[4];// 第五名结果
				}
				String[] betBallArr = betBall.split("\\|");
				String diyiBet = "";
				String dierBet = "";
				String disanBet = "";
				String disiBet = "";
				String diwuBet = "";
				if (betBallArr.length == 1) {
					diyiBet = betBallArr[0];
				} else if (betBallArr.length == 2) {
					diyiBet = betBallArr[0];
					dierBet = betBallArr[1];
				} else if (betBallArr.length == 3) {
					diyiBet = betBallArr[0];
					dierBet = betBallArr[1];
					disanBet = betBallArr[2];
				} else if (betBallArr.length == 4) {
					diyiBet = betBallArr[0];
					dierBet = betBallArr[1];
					disanBet = betBallArr[2];
					disiBet = betBallArr[3];
				} else if (betBallArr.length == 5) {
					diyiBet = betBallArr[0];
					dierBet = betBallArr[1];
					disanBet = betBallArr[2];
					disiBet = betBallArr[3];
					diwuBet = betBallArr[4];
				} else {
					GameHelpUtil.log("北京赛车","北京赛车定位胆玩法投注出错，错误的投注为"+betBall);
				}
				if (!"".equals(diyiBet)) {
					String[] diyiArr = diyiBet.split(",");
					for (int i = 0;i < diyiArr.length;i++) {
						if (diyiRes.equals(diyiArr[i])) {
							winMoney = winMoney.add(betRate);
						}
					}
				}
				if (!"".equals(dierBet)) {
					String[] dierArr = dierBet.split(",");
					for (int i = 0;i < dierArr.length;i++) {
						if (dierRes.equals(dierArr[i])) {
							winMoney = winMoney.add(betRate);
						}
					}
				}
				if (!"".equals(disanBet)) {
					String[] disanArr = disanBet.split(",");
					for (int i = 0;i < disanArr.length;i++) {
						if (disanRes.equals(disanArr[i])) {
							winMoney = winMoney.add(betRate);
						}
					}
				}
				if (!"".equals(disiBet)) {
					String[] disiArr = disiBet.split(",");
					for (int i = 0;i < disiArr.length;i++) {
						if (disiRes.equals(disiArr[i])) {
							winMoney = winMoney.add(betRate);
						}
					}
				}
				if (!"".equals(diwuBet)) {
					String[] diwuArr = diwuBet.split(",");
					for (int i = 0;i < diwuArr.length;i++) {
						if (diwuRes.equals(diwuArr[i])) {
							winMoney = winMoney.add(betRate);
						}
					}
				}
			} else if (SfPk102Constants.GAME_TYPE_GF_QIAN_SAN.equals(playType)) { // 前三  格式02,05|06|
				String diyiRes = results[0];// 第一名结果
				String dierRes = results[1];// 第二名结果
				String disanRes = results[2];// 第三名结果
				
				String[] betBallArr = betBall.split("\\|");
				
				String[] diyiArr = betBallArr[0].split(",");
				String[] dierArr = betBallArr[1].split(",");
				String[] disanArr = betBallArr[2].split(",");
				if (diyiArr != null && dierArr != null && disanArr != null) {
					for (int i = 0;i < diyiArr.length;i++) {
						String yi = diyiArr[i];
						for (int j = 0;j < dierArr.length;j++) {
							String er = dierArr[j];
							for (int k = 0;k < disanArr.length;k++) {
								String san = disanArr[k];
								if (!yi.equals(er) && !yi.equals(san) && !er.equals(san)) {// 此时才是有效的投注数
									if (diyiRes.equals(yi) && dierRes.equals(er) && disanRes.equals(san)) {
										winMoney = winMoney.add(betRate);
									}
								}
							}
						}
					}
				}
			} else if (SfPk102Constants.GAME_TYPE_GF_QIAN_ER.equals(playType)) { // 前二 
				String diyiRes = results[0];// 第一名结果
				String dierRes = results[1];// 第二名结果
				
				String[] betBallArr = betBall.split("\\|");
				
				String[] diyiArr = betBallArr[0].split(",");
				String[] dierArr = betBallArr[1].split(",");
				if (diyiArr != null && dierArr != null) {
					for (int i = 0;i < diyiArr.length;i++) {
						String yi = diyiArr[i];
						for (int j = 0;j < dierArr.length;j++) {
							String er = dierArr[j];
							if (!yi.equals(er)) {// 此时才是有效的投注数
								if (diyiRes.equals(yi) && dierRes.equals(er)) {
									winMoney = winMoney.add(betRate);
								}
							}
						}
					}
				}
			} else if (SfPk102Constants.GAME_TYPE_GF_QIAN_YI.equals(playType)) {// 前一 格式   02,05,09,10
				String diyiRes = results[0];// 第一名结果
				String[] betBallArr = betBall.split(",");
				for (int i = 0;i < betBallArr.length;i++) {
					if (diyiRes.equals(betBallArr[i])) {
						winMoney = winMoney.add(betRate);
					}
				}
			}
		} else {
			GameHelpUtil.log("北京赛车","开奖结果出错，错误的结果为"+results.length+"位");
		}
		return winMoney;
	}
	
	
	/**
	 * 单双玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkDAN_SHUANG(String betBall) {
		String regEx = "(单|单,双|双)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	/**
	 * 大小玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkDAXI_AO(String betBall) {
		String regEx = "(大|大,小|小)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 定位胆玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkDING_WEI_DAN(String betBall) {
		String regEx = "((((0[1-9])|10),?){0,10}\\|){4}((((0[1-9])|10),?){0,10}){1}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	/**
	 * 前三玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkQIAN_SAN(String betBall) {
		String regEx = "((((0[1-9])|10),?){0,10}\\|){2}((((0[1-9])|10),?){0,10}){1}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 前二玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkQIAN_ER(String betBall) {
		String regEx = "((((0[1-9])|10),?){0,10}\\|){1}((((0[1-9])|10),?){0,10}){1}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 前一玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkQIAN_YI(String betBall) {
		String regEx = "(((0[1-9])|10),?){1,10}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
}
