package com.apps.util;

import java.util.Random;

/**
 * 生成自己的session
 * 
 * @author Mr.zang
 * 
 */
public class SessionUtil {

	public static String random() {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < 8; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	public static void main(String[] args) {
		System.out.println(SessionUtil.random());
	}
	
}
