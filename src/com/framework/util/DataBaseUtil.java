package com.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataBaseUtil {

	public String bakMySqlToFile() { 
	    String outStr = ""; 
	    try { 
	        Runtime rt = Runtime.getRuntime(); 
	            Process child = rt.exec("mysqldump  -uroot -pmysql dbname");// 设置导出编码为utf8。这里必须是utf8在此要注意，有时会发生一个mysqldump: Got error: 1045的错误，此时mysqldump必须加上你要备份的数据库的IP地址，即mysqldump -h192.168.0.1 -uroot -pmysql dbname，今天我就遇到了这样的问题，呵呵	            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行 
	            InputStream in = child.getInputStream();// 控制台的输出信息作为输入流 
	            InputStreamReader xx = new InputStreamReader(in, "utf8");// 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码 
	            String inStr; 
	            StringBuffer sb = new StringBuffer(""); 
	            // 组合控制台输出信息字符串 
	            BufferedReader br = new BufferedReader(xx); 
	            while ((inStr = br.readLine()) != null) { 
	                sb.append(inStr + "\r\n"); 
	            } 
	            outStr = sb.toString(); 
	            in.close(); 
	            xx.close(); 
	            br.close(); 

	        } catch (Exception e) { 
	            e.printStackTrace(); 
	        } 
	        return outStr; 
	    } 
	
	
	
}
