package com.ram.util;

import java.io.InputStream;

import com.framework.util.DateTimeUtil;

public class PingUtil {

	public PingUtil() {
	}

	public void ping(String remoteIp){
		Runtime rt = Runtime.getRuntime();
		try {
			/*
			 * 以下代码使用Runtime类在一个subprocess中运行所在平台 的native命令。
			 */
			Process ps = rt.exec("ping " + remoteIp);

			/*
			 * ping命令的output会定向到subprocess的InputStream。 得到此InputStream,
			 * 然后输出到你希望的地方。
			 */
			InputStream is = ps.getInputStream();
			int t;
			System.out.println(DateTimeUtil.getDateTime());
			while ((t = is.read()) != -1) {
				System.out.print( (char) t);
			}
		} catch (Exception se) {
		}		
	}
	
	public static void main(String[] args) {
		PingUtil pu=new PingUtil();
		pu.ping("oracle.ramonline.com");


	}
}
