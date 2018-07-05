package com.gf.pick11.sdpick11.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.util.Log;

import com.gf.pick11.gdpick11.GdPick11Constants;
import com.gf.pick11.sdpick11.SdPick11Constants;

public class SdPick11Util {

	
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
		if("0".equals(playType)){//前三直选
			int num=0;
			String[] one = betBall.split("\\*")[0].split(",");
			String[] two = betBall.split("\\*")[1].split(",");
			String[] three = betBall.split("\\*")[2].split(",");
			List<String> list=new ArrayList<String>();
			for(int i=0;i<one.length;i++){
				for(int j=0;j<two.length;j++){
					if(!one[i].equals(two[j])){
						list.add(one[i]+","+two[j]);
					}
				}
			}
			if(list.size()<1){
				Log.info("广东十一选五前三直选投注不正确");
				num=0;
				return -1;
			}else{
				for(int m=0;m<three.length;m++){
					for(int n=0;n<list.size();n++){
						if(list.get(n).indexOf(three[m])==-1){
							num++;
						}
					}
				}
				if(num==0){
					Log.info("广东十一选五前三直选投注不正确");
					return -1;
				}
			}
			totalBet=totalBet+num;
		}else if("1".equals(playType)){//前三组选
			String[] one = betBall.split(",");
			if(one.length<3){
				Log.info("广东十一选五前三组选投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,3);
		}else if("2".equals(playType)){//前二直选
			String[] one = betBall.split("\\*")[0].split(",");
			String[] two = betBall.split("\\*")[1].split(",");
			List<String> list=new ArrayList<String>();
			for(int i=0;i<one.length;i++){
				for(int j=0;j<two.length;j++){
					if(!one[i].equals(two[j])){
						list.add(one[i]+","+two[j]);
					}
				}
			}
			if(list.size()<1){
				Log.info("广东十一选五前二直选投注不正确");
				return -1;
			}
			totalBet = totalBet + list.size();
		}else if("3".equals(playType)){//前二组选
			String[] one = betBall.split(",");
			if(one.length<2){
				Log.info("广东十一选五前二组选投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,2);
		}else if("4".equals(playType)){//前一
			String[] one = betBall.split(",");
			totalBet = totalBet + getBetTotal(one.length,1);
		}else if("5".equals(playType)){//任八
			String[] one = betBall.split(",");
			if(one.length<8){
				Log.info("广东十一选五任八投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,8);
		}else if("6".equals(playType)){//任七
			String[] one = betBall.split(",");
			if(one.length<7){
				Log.info("广东十一选五任七投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,7);
		}else if("7".equals(playType)){//任六
			String[] one = betBall.split(",");
			if(one.length<6){
				Log.info("广东十一选五任六投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,6);
		}else if("8".equals(playType)){//任五
			String[] one = betBall.split(",");
			if(one.length<5){
				Log.info("广东十一选五任五投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,5);
		}else if("9".equals(playType)){//任四
			String[] one = betBall.split(",");
			if(one.length<4){
				Log.info("广东十一选五任四投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,4);
		}else if("10".equals(playType)){//任三
			String[] one = betBall.split(",");
			if(one.length<3){
				Log.info("广东十一选五任三投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,3);
		}else if("11".equals(playType)){//任二
			String[] one = betBall.split(",");
			if(one.length<2){
				Log.info("广东十一选五任二投注不正确");
				return -1;
			}
			totalBet = totalBet + getBetTotal(one.length,2);
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
		if(SdPick11Constants.GAME_TYPE_GF_QIANSANZHI.equals(playType)){//前三直选
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
		}else if(SdPick11Constants.GAME_TYPE_GF_QIANSANZU.equals(playType)){//前三组选
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
		}else if(SdPick11Constants.GAME_TYPE_GF_QIANERZHI.equals(playType)){//前二直选
			String[] one = betBall.split("\\*")[0].split(",");
			String[] two = betBall.split("\\*")[1].split(",");
			int num1=0;
			int num2=0;
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
				winMoney=winMoney.add(betRate);
			}
		}else if(SdPick11Constants.GAME_TYPE_GF_QIANERZU.equals(playType)){//前二组选
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
		}else if(SdPick11Constants.GAME_TYPE_GF_QIANYI.equals(playType)){//前一
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])){
					num++;
				}
			}
			if(num>=1){
				winMoney=winMoney.add(betRate);
			}
		}else if(SdPick11Constants.GAME_TYPE_GF_RENBA.equals(playType)){//任八		
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])||array[i].equals(results[1])||array[i].equals(results[2])||array[i].equals(results[3])
						||array[i].equals(results[4])){
					num++;
				}
			}
			if(num>=5){
				winMoney=winMoney.add(betRate);
			}	
		}else if(SdPick11Constants.GAME_TYPE_GF_RENQI.equals(playType)){//任七
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])||array[i].equals(results[1])||array[i].equals(results[2])||array[i].equals(results[3])
						||array[i].equals(results[4])){
					num++;
				}
			}
			if(num>=5){
				winMoney=winMoney.add(betRate);
			}	
		}else if(SdPick11Constants.GAME_TYPE_GF_RENLIU.equals(playType)){//任六
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])||array[i].equals(results[1])||array[i].equals(results[2])||array[i].equals(results[3])
						||array[i].equals(results[4])){
					num++;
				}
			}
			if(num>=5){
				winMoney=winMoney.add(betRate);
			}
		}else if(SdPick11Constants.GAME_TYPE_GF_RENWU.equals(playType)){//任五
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])||array[i].equals(results[1])||array[i].equals(results[2])||array[i].equals(results[3])
						||array[i].equals(results[4])){
					num++;
				}
			}
			if(num>=5){
				winMoney=winMoney.add(betRate);
			}
		}else if(GdPick11Constants.GAME_TYPE_GF_RENSI.equals(playType)){//任四
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])
						||array[i].equals(results[1])
						||array[i].equals(results[2])
						||array[i].equals(results[3])
						||array[i].equals(results[4])){
					num++;
				}
			}
			if(num>=4){
				int winNum = getBetTotal(num, 4);
				winMoney=new BigDecimal(winNum).multiply(betRate);
			}
		}else if(GdPick11Constants.GAME_TYPE_GF_RENSAN.equals(playType)){//任三
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){//
				if(array[i].equals(results[0])
						||array[i].equals(results[1])
						||array[i].equals(results[2])
						||array[i].equals(results[3])
						||array[i].equals(results[4])){
					num++;
				}
			}
			if(num>=3){
				int winNum = getBetTotal(num, 3);
				winMoney=new BigDecimal(winNum).multiply(betRate);
			}
		}else if(GdPick11Constants.GAME_TYPE_GF_RENER.equals(playType)){//任二
			String array[]=betBall.split(",");
			int num=0;
			for(int i=0;i<array.length;i++){
				if(array[i].equals(results[0])
						||array[i].equals(results[1])
						||array[i].equals(results[2])
						||array[i].equals(results[3])
						||array[i].equals(results[4])){
					num++;
				}
			}
			if(num>=2){
			    int winNum = getBetTotal(num, 2);
				winMoney=new BigDecimal(winNum).multiply(betRate);
			}
		}
		return winMoney;
	}
	
	
	public static void main(String args[]){
		System.out.println(getBetTotal(11,8));
	}
	
	/**
	 * 11任二玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkRENER(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]),){1,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){1,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 10任三玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkRENSAN(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]),){2,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){2,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 9任四玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkRENSI(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]),){3,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){3,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 8任五玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkRENWU(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]),){4,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){4,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 7任六玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkRENLIU(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]),){5,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){5,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 6任七玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkRENQI(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]),){6,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){6,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 5任八玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkRENBA(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]),){7,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){7,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 4前一玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkQIANYI(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]){1},){0,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){0,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 3前二组选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkQIANERZU(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]),){1,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){1,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	/**
	 * 2前二直选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkQIANERZHI(String betBall) {
//		String regEx = "(((0[1-9]|1[0-1]),){1,10}((0[1-9]|1[0-1]){1}){1})\\*(((0[1-9]|1[0-1]),){0,10}((0[1-9]|1[0-1]){1}){1})|(((0[1-9]|1[0-1]),){0,10}((0[1-9]|1[0-1]){1}){1})\\*(((0[1-9]|1[0-1]),){1,10}((0[1-9]|1[0-1]){1}){1})";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(betBall);
//		return matcher.matches();
		String reg = "^[^\\*]+\\*[^\\*]+$";
		Pattern pattern = Pattern.compile(reg);
		if(pattern.matcher(betBall).matches()){
			String tensBall = betBall.split("\\*")[0];
			String unitBall = betBall.split("\\*")[1];
			Pattern unitPattern = Pattern.compile("(?:(0[1-9]|1[0-1]),(?!.*\\1)){0,10}(0[1-9]|1[0-1])");  
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
	 * 1前三组选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkQIANSANZU(String betBall) {
//		String regEx = "((0[1-9]|1[0-1]),){2,10}((0[1-9]|1[0-1]){1}){1}";
		String regEx = "(?:(0[1-9]|1[0-1]),(?!.*\\1)){2,10}(0[1-9]|1[0-1])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(betBall);
		return matcher.matches();
	}
	
	/**
	 * 0前三直选玩法正则
	 * 
	 * @param betBall
	 * @return true 正确  false 错误
	 */
	public static boolean chkQIANSANZHI(String betBall) {
//		String regEx = "(((0[1-9]|1[0-1]),){2,10}((0[1-9]|1[0-1]){1}){1})\\*(((0[1-9]|1[0-1]),){0,10}((0[1-9]|1[0-1]){1}){1})\\*(((0[1-9]|1[0-1]),){0,10}((0[1-9]|1[0-1]){1}){1})|(((0[1-9]|1[0-1]),){0,10}((0[1-9]|1[0-1]){1}){1})\\*(((0[1-9]|1[0-1]),){2,10}((0[1-9]|1[0-1]){1}){1})\\*(((0[1-9]|1[0-1]),){0,10}((0[1-9]|1[0-1]){1}){1})|(((0[1-9]|1[0-1]),){0,10}((0[1-9]|1[0-1]){1}){1})\\*(((0[1-9]|1[0-1]),){0,10}((0[1-9]|1[0-1]){1}){1})\\*(((0[1-9]|1[0-1]),){2,10}((0[1-9]|1[0-1]){1}){1})";
//		Pattern pattern = Pattern.compile(regEx);
//		Matcher matcher = pattern.matcher(betBall);
//		return matcher.matches();
		String reg = "^[^\\*]+\\*[^\\*]+\\*[^\\*]+$";
		Pattern pattern = Pattern.compile(reg);
		if(pattern.matcher(betBall).matches()){
			String precentBall = betBall.split("\\*")[0];
			String tensBall = betBall.split("\\*")[1];
			String unitBall = betBall.split("\\*")[2];
			Pattern unitPattern = Pattern.compile("(?:(0[1-9]|1[0-1]),(?!.*\\1)){0,10}(0[1-9]|1[0-1])");  
	        if(unitPattern.matcher(tensBall).matches()&&unitPattern.matcher(unitBall).matches()
	        		&&unitPattern.matcher(precentBall).matches()){
	        	return true;
	        }else{
	        	return false;
	        }
		}else{
			return false;
		}

	}
	
	
}
