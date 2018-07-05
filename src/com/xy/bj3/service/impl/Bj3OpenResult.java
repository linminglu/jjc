package com.xy.bj3.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.jfree.util.Log;

import com.framework.util.StringUtil;

public class Bj3OpenResult {
	public final static String[] SOURCE = {
		"0","1","2","3","4","5","6","7","8","9"
	};
	
	/**
	 * 产生一个随机的结果
	 * @return
	 */
	public static String getRandomResult(){
		String number1=SOURCE[StringUtil.getRandomInt(10)];
		String number2=SOURCE[StringUtil.getRandomInt(10)];
		String number3=SOURCE[StringUtil.getRandomInt(10)];
		String number4=SOURCE[StringUtil.getRandomInt(10)];
		String number5=SOURCE[StringUtil.getRandomInt(10)];	
		Log.info("jieguo:"+number1+","+number2+","+number3+","+number4+","+number5);
		return number1+","+number2+","+number3+","+number4+","+number5;
	}
	
	public static String getRandomResult(int num){
		Map<String,BigDecimal> map=new HashMap<String,BigDecimal>();
		String  estimateResult="";
		for(int i=0;i<num;i++){			
			String result = getRandomResult();
			if(map.get(result)==null){
				map.put(result, new BigDecimal(0));
				estimateResult=estimateResult+result+"|";
			}else{
				i--;
			}		
		}
		estimateResult=estimateResult.substring(0, estimateResult.length()-1);
		return estimateResult;
	}
	
	
	public static void main(String[] args){
		Bj3OpenResult bj3=new Bj3OpenResult();
		for(int i=0;i<50;i++){
			System.out.println(bj3.getRandomResult());
		}
	}
	
	
	
}
