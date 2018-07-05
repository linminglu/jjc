package com.apps.util;

import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.framework.util.HttpServletUtil;
import com.framework.util.ParamUtils;

/**
 * google 地理编译工具类
 * 
 * @author Mr.zang
 * 
 */
public class GoogleGeocoder {

	public static void main(String[] args) {
//		3.149697, 101.695626
//		101.695626,3.149697
//		116.339267,39.856027
//		System.out.println(getGLGeocoderCityCode("Beijing"));
//			System.out.println(getGLGeocoderCityName("101.6082551,3.1379835")+">>>>");
//		for (int i = 0; i < 5000; i++) {
////			System.out.println(getGLGeocoderCityName("116.339267,39.856027")+">>>>"+i);
//		}
		
	}

	/**
	 * 利用google反地理编译获得市级名
	 * 
	 * @param longAlt
	 *            经纬度，格式为： 经度,纬度
	 */
//	public static String getGLGeocoderCityName(String longAlt) {
//		String cityName = null;
//		String[] split = longAlt.split(",");
//		if (split != null && split.length == 2) {
//			try {
//				String longitude = split[0];// 经度
//				String latitude = split[1];// 经度
//
//				String result = HttpServletUtil
//						.getResultJson("https://maps.googleapis.com/maps/api/geocode/json?address="
//								+ latitude + "," + longitude + "&language=en");
////				System.out.println(result);
//				if (result != null) {
//					result = result.replaceAll("\\s*", "");
//					JSONObject jsonObj = JsonUtil.StringToObject(result);
//					String status=(String) jsonObj.get("status");
//					if (status.equals("OK")) {
//						JSONArray jsonArray = jsonObj.getJSONArray("results");
//						JSONObject s = null;
//						boolean isbreak=false;
//						for (int i = 0; i < jsonArray.size(); i++) {
//							JSONObject obj = (JSONObject) jsonArray.get(i);
//							JSONArray types = (JSONArray) obj.get("types");
//							for (Object type : types) {
//								if (type.equals("locality")) {
//									// 获得到达市级别的地址，例如：山东省济南市
//									s = (JSONObject) jsonArray.get(i);
//									isbreak=true;
//									break;
//								}
//							}
//							if(isbreak){
//								break;
//							}
//						}
//						JSONArray components = (JSONArray) s
//								.get("address_components");
//						for (Object object : components) {
//							JSONObject obj = (JSONObject) object;
//							JSONArray types = (JSONArray) obj.get("types");
//							for (Object type : types) {
//								if (type.equals("locality")) {
//									cityName = obj.getString("long_name");// short_name
//									System.out.println(">>>>"+cityName);
//								}
//							}
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return cityName;
//	}

	/**
	 * 利用google地理编译获得城市code，因为google没有带code，所以拿google返回的市级名代表code <br/>
	 * 这个地方做转换的原因是因为管理员输入的城市名和google返回的不一定一样，所以统一转换为google的
	 * 
	 * @param cityName
	 *            城市名
	 */
	/*public static String getGLGeocoderCityCode(String cityName) {
		if (ParamUtils.chkString(cityName)) {
			cityName = cityName.replaceAll("\\s*", "");
			try {
//				System.out.println(cityName);
				String inputUrl="https://maps.googleapis.com/maps/api/geocode/json?address="
				+ cityName + "&language=en";
				String result = HttpServletUtil
						.getResultJson(inputUrl);
				if (result != null) {
					result = result.replaceAll("\\s*", "");
//					System.out.println(result);
					JSONObject jsonObj = JsonUtil.StringToObject(result);
					String status=(String) jsonObj.get("status");
//					System.out.println(status);
					if (status.equals("OK")) {
						JSONArray jsonArray = jsonObj.getJSONArray("results");
						JSONObject s = null;
						boolean isbreak=false;
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject obj = (JSONObject) jsonArray.get(i);
							JSONArray types = (JSONArray) obj.get("types");
							for (Object type : types) {
								// System.out.println(type);
								if (type.equals("locality")) {
									// 获得到达市级别的地址，例如：山东省济南市
									s = (JSONObject) jsonArray.get(i);
									isbreak=true;
									break;
								}
							}
							if(isbreak){
								break;
							}
						}
						JSONArray components = (JSONArray) s
								.get("address_components");
						for (Object object : components) {
							JSONObject obj = (JSONObject) object;
							JSONArray types = (JSONArray) obj.get("types");
							for (Object type : types) {
								if (type.equals("locality")) {
									cityName = obj.getString("long_name");// short_name
									System.out.println(">>>>2"+cityName);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cityName;
	}*/

}
