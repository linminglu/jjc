package com.apps.util;

import help.base.APIConstants;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.jpush.api.common.resp.APIConnectionException;

/**
 * 
 * @author Mr.zang
 */
public class JsonUtil {
	// static Log log = LogFactory.getLog(getClass());
	// PropertyConfigurator.configure(Log4j.class.getResource("/conf/log4j.properties"));

	public static <T> String ObjectToJsonString(Object object) {
		JSONObject obj = new JSONObject(object);
		return (obj.toString());
	}

	public static <T> String ArrayToJsonString(Object object) {
		JSONArray array = new JSONArray(object);
		return array.toString();
	}

	/**
	 * @Title: StringToObject
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param jsonString 设定文件
	 * @return void 返回类型 json.getString("b");
	 * @throws
	 */
	public static JSONObject StringToObject(String jsonString) {
		JSONObject json = new JSONObject(jsonString);
		return json;
	}

	public static JSONArray StringToArray(String jsonString) {
		JSONArray json = new JSONArray(jsonString);
		return json;
	}

	public static String getParam(JSONObject json, String key,
			String not_found_return_vlaue) {
		if (key == null || !json.has(key))
			return not_found_return_vlaue;
		return json.getString(key);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject json = JsonUtil
				.StringToObject("{'a':'1','b':'2',c:{'c1':'99','c2':'98'}}");
		System.out.println(json.getString("a"));

		JSONObject jsonc = json.getJSONObject("c");
		System.out.println(jsonc.getString("c1"));

	}

	/**
	 * ajax输出流
	 * 
	 * @param response
	 * @param info
	 */
	public static void AjaxWriter(HttpServletResponse response, Object obj) {
		PrintWriter out;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.println(obj);
			out.flush();// 清空缓冲区
			out.close();// 关闭输入流
		} catch (IOException e) {
			// log.error(e.toString());
		}
	}
	
	
	public static void AjaxWriter(HttpServletResponse response, String code,
			String message, JSONObject data) {
		JSONObject map = new JSONObject();// 最外层
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		PrintWriter out;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.println(map);
			out.flush();// 清空缓冲区
			out.close();// 关闭输入流
		} catch (IOException e) {
			// log.error(e.toString());
		}
	}
	public static void AjaxWriterError(HttpServletResponse response,org.json.JSONObject map, String errMsg){
		map.put("code", APIConstants.CODE_REQUEST_ERROR);
		map.put("msg", errMsg);
		map.put("data", new org.json.JSONObject());
		AjaxWriter(response,map);
	}

}
