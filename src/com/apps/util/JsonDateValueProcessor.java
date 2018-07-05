package com.apps.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * 将实体中date类型转换为yyyy-MM-dd 的json格式
 * 
 * @author zang
 * 
 *         使用方法:
 * 
 *        <p> JSONObject fromObject = null;</p>
 *        <p> JsonConfig jsonConfig = new JsonConfig(); //设置javabean中日期转换时的格式</p>
 *        <p> jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm")); </p>
 *        <p> fromObject =JSONObject.fromObject(object,jsonConfig);</p>
 * 
 */
public class JsonDateValueProcessor implements JsonValueProcessor {

	private String datePattern = "yyyy-MM-dd";

	public JsonDateValueProcessor() {
		super();
	}

	public JsonDateValueProcessor(String format) {
		super();
		this.datePattern = format;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		try {
			if (value instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat(datePattern,
						Locale.UK);
				return sdf.format((Date) value);
			}
			return value == null ? "" : value.toString();
		} catch (Exception e) {
			return "";
		}

	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String pDatePattern) {
		datePattern = pDatePattern;
	}

}
