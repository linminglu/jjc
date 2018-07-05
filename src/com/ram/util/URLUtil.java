package com.ram.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * URLUtil，工具类，用于处理URL
 * 
 * @author 胡磊
 * 
 */
public class URLUtil {

	/**
	 * 判断一个URL是否存在
	 * 
	 * @param url
	 * @return
	 */
	public static boolean URLIsExist(String url) {
		boolean isExist = false;
		try {
			URL destUrl = new URL(url);
			HttpURLConnection urlConn = (HttpURLConnection) destUrl
					.openConnection();
			if(urlConn.getResponseCode()== urlConn.HTTP_NOT_FOUND){
				isExist=false;
			}
			if(urlConn.getResponseCode()==urlConn.HTTP_OK){
				isExist=true;
			}else{
				isExist=false;
			}


		} catch (MalformedURLException e) {
			System.err.println("Error in URLUtil when quest url:" + url);
			System.err.println("The error message is:" + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error in URLUtil when quest url:" + url);
			System.err.println("The error message is:" + e.getMessage());
		}

		return isExist;

	}
	//private static String HttpRequest(String url, String query)
		public static String HttpRequestUTF8(String url)
		{
			HttpURLConnection connection = null;
			StringBuilder out = new StringBuilder();
			String returns = "";
			try
			{
				URL urlobj = new URL(url);
				connection = (HttpURLConnection)urlobj.openConnection();
				connection.setRequestMethod("POST");
				//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				//connection.setRequestProperty("Content-Length", (new StringBuilder()).append(query.length()).toString());
				connection.setRequestProperty("charset", "utf-8");
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				//wr.writeBytes(query.toString());
				wr.flush();
				wr.close();
				//System.out.println(connection.getResponseCode());
				InputStream is = connection.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is,"utf-8"));
				String line;
				while ((line = rd.readLine()) != null) 
				{
					out.append(line);
					out.append('\r');
				}
				//returns = new String(out.toString().getBytes(),"iso-8859-1");
				///returns = new String(out.toString().getBytes(),"utf-8");
				//System.out.println("==========:"+out.toString());
				//returns = new String(out.toString().getBytes(),"utf-8");
//				System.out.println("==========:"+returns);
//				returns = new String(returns.getBytes("gbk"),"utf-8");
//				System.out.println("==========:"+returns);
				returns = out.toString();
				rd.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (connection != null)
				connection.disconnect();
			return returns;
		}
		
		public static String HttpRequestUTF8(String url, String query)
		{
			HttpURLConnection connection = null;
			StringBuilder out = new StringBuilder();
			String returns = "";
			try
			{
				URL urlobj = new URL(url);
				connection = (HttpURLConnection)urlobj.openConnection();
				connection.setRequestMethod("POST");
				//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				//connection.setRequestProperty("Content-Length", (new StringBuilder()).append(query.length()).toString());
				connection.setRequestProperty("charset", "utf-8");
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(query.toString());
				wr.flush();
				wr.close();
				//System.out.println(connection.getResponseCode());
				InputStream is = connection.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is,"utf-8"));
				String line;
				while ((line = rd.readLine()) != null) 
				{
					out.append(line);
					out.append('\r');
				}
				//returns = new String(out.toString().getBytes(),"iso-8859-1");
				///returns = new String(out.toString().getBytes(),"utf-8");
				//System.out.println("==========:"+out.toString());
				//returns = new String(out.toString().getBytes(),"utf-8");
//				System.out.println("==========:"+returns);
//				returns = new String(returns.getBytes("gbk"),"utf-8");
//				System.out.println("==========:"+returns);
				returns = out.toString();
				rd.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (connection != null)
				connection.disconnect();
			return returns;
		}


}
