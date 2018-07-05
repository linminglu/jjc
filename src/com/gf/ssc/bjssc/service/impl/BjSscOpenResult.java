package com.gf.ssc.bjssc.service.impl;

import com.framework.util.StringUtil;

public class BjSscOpenResult {
	public final static String[] SOURCE = {
		"0","1","2","3","4","5","6","7","8","9"
	};
	
	/**
	 * 产生一个随机的结果
	 * @return
	 */
	public String getRandomResult(){
		String number1=SOURCE[StringUtil.getRandomInt(10)];
		String number2=SOURCE[StringUtil.getRandomInt(10)];
		String number3=SOURCE[StringUtil.getRandomInt(10)];
		String number4=SOURCE[StringUtil.getRandomInt(10)];
		String number5=SOURCE[StringUtil.getRandomInt(10)];	
		return number1+","+number2+","+number3+","+number4+","+number5;
	}
	
	
	
	
	
	public static void main(String[] args){
		BjSscOpenResult bj3=new BjSscOpenResult();
		for(int i=0;i<50;i++){
			System.out.println(bj3.getRandomResult());
		}
	}
}
