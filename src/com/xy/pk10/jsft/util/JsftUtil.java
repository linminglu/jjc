package com.xy.pk10.jsft.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 极速赛车   工具类
 * @author admin
 *
 */
public class JsftUtil {
	
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
	
	public static String getRandomResult(int num){
		Map<String,BigDecimal> map=new HashMap<String,BigDecimal>();
		String randomResult="";
		List<String> list=new ArrayList<String>();
		list.add("06");
		list.add("03");
		list.add("01");
		list.add("09");
		list.add("10");
		list.add("02");
		list.add("05");
		list.add("08");
		list.add("04");
		list.add("07");
		for(int i=0;i<num;i++){
			Collections.shuffle(list);
			Iterator ite = list.iterator();
			String result="";
			while(ite.hasNext()){
				result=result+ite.next().toString()+",";
			}
			result=result.substring(0, result.length()-1);
			if(map.get(result)==null){
				map.put(result, new BigDecimal(0));
				randomResult=randomResult+result+"|";
			}else{
				i--;
			}
		}
		return randomResult.substring(0, randomResult.length()-1);
	}
	
	
	public static void main(String[] args) {
		String result=getRandomResult(5);
		System.out.println(result);		
	}
	
}
