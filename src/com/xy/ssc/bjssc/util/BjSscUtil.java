package com.xy.ssc.bjssc.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.framework.util.StringUtil;

/**
 * 
 * @author admin
 *
 */
public class BjSscUtil {
	
	/**
	 * 从10到20获取随机1个数
	 * @return
	 */
	public static int getRandomSecond(){
		int[] second = {10,11,12,13,14,15,16,17,18,19,20};
		
		Random rand = new Random();
		
		int	index = Math.abs(rand.nextInt() % second.length);

		return second[index];
	}
	/**
	 * 获取随机开奖结果
	 * @return
	 */
	public static String getRandomResult(){
		String[] source = {"0","1","2","3","4","5","6","7","8","9"};
		String number1=source[StringUtil.getRandomInt(10)];
		String number2=source[StringUtil.getRandomInt(10)];
		String number3=source[StringUtil.getRandomInt(10)];
		String number4=source[StringUtil.getRandomInt(10)];
		String number5=source[StringUtil.getRandomInt(10)];	
		return number1+","+number2+","+number3+","+number4+","+number5;
	}
	public static String getRandomResult(int num){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<num;i++){
			sb.append(getRandomResult());
			sb.append("|");
		}
		return sb.substring(0, sb.length()-1);
	}
	
	
	public static void main(String[] args) {
		String result=getRandomResult(5);
		System.out.println(result);		
	}
	
}
