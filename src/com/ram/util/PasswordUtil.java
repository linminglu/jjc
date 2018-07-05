package com.ram.util;

import com.framework.util.StringUtil;

public class PasswordUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 产生随机密码
	 * @return
	 */
	public static String genRamdonPassword(){
		return StringUtil.getRandomString(10);
		
	}
	
	/**
	 * 将传入的参数用MD5加密
	 * @return
	 */
	public static String genRamdonPasswordMD5(String password){
		return MD5.exc(password);
	}
}
