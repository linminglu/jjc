package com.framework.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpServletUtil {
	 public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	            
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        return null;
	 }
	 
	 
	 public static String getResultJson(String surl) {
	        String htmlContent = "";
	        try {
	            java.io.InputStream inputStream;
	            java.net.URL url = new java.net.URL(surl);
	            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url
	                    .openConnection();
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            connection.connect();
	            inputStream = connection.getInputStream();
	            byte[] bytes = new byte[1024 * 2000];
	            int index = 0;
	            int count = inputStream.read(bytes, index, 1024 * 2000);
	            while (count != -1) {
	                index += count;
	                count = inputStream.read(bytes, index, 1);
	            }
	            htmlContent = new String(bytes, "UTF-8");
	            connection.disconnect();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return htmlContent.trim();
	    }

}
