package help.pay.lingdian.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import help.pay.lingdian.Config.MerConfig;

public class SignUtil {
	/*
	 * 获取HttpPost的所有参数
	 */
	public static Map<String, String> parseParam(HttpServletRequest request, String charset) throws UnsupportedEncodingException {
		if (request == null || charset == null)
			return null;
		Map<String, String> reqMap = new LinkedHashMap<String, String>();
		Enumeration enum1 = request.getParameterNames();
		while (enum1.hasMoreElements()) {
			String paramName = (String) enum1.nextElement();
			String paramValue = request.getParameter(paramName);
			// 形成键值对应的map
			reqMap.put(paramName, URLDecoder.decode(paramValue, charset));
		}
		return reqMap;
	}

	public static Map<String, String> parseResponse(String respData, String[] base64Keys, String urlParamConnectFlag, String charset)
			throws UnsupportedEncodingException {
		Map<String, String> map = new LinkedHashMap<String, String>();
		String[] str = respData.split(urlParamConnectFlag);
		for (int i = 0; i < str.length; i++) {
			int z = str[i].indexOf("=");
			map.put(str[i].substring(0, z), URLDecoder.decode(str[i].substring(z + 1, str[i].length()), charset));
		}

		System.out.println("URL解码后返回数据:" + getURLParam(map, urlParamConnectFlag, true, null));
		SignUtil.convertRespData(map, base64Keys);
		System.out.println("转换返回数据中Base64的值:" + getURLParam(map, urlParamConnectFlag, true, null));
		return map;
	}

	public static String getSignMsg(Map<String, String> map, String urlParamConnectFlag, Set<String> removeKey) {
		return getURLParam(map, urlParamConnectFlag, true, removeKey);
	}

	public static String sign(String signMethod, String signedMsg, String key, String charSet) {
		try {
			// byte[] data = (signedMsg + key).getBytes(charSet);
			String[] algArray = { "MD5", "SHA1", "SHA256", "SHA512" };
			String algorithm = null;
			for (int i = 0; i < algArray.length; i++) {
				if (algArray[i].equalsIgnoreCase(signMethod)) {
					algorithm = algArray[i];
					break;
				}
			}

			if (algorithm == null) {
				// 签名方法错误
				return null;
			}
			return Md5Util.md5(signedMsg + key, charSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean verifySign(String signMethod, String signedMsg, String hmac, String key, String charSet) {
		try {
			if (null == hmac || null == signedMsg) {
				return false;
			}
			return hmac.equalsIgnoreCase(sign(signMethod, signedMsg, key, charSet));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getURLParam(Map<String, String> map, String urlParamConnectFlag, boolean isSort, Set<String> removeKey) {
		StringBuffer param = new StringBuffer();
		List<String> msgList = new ArrayList<String>();
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			String value = (String) map.get(key);
			if ((removeKey == null) || (!removeKey.contains(key))) {
				msgList.add(key + "=" + toEmpty(value));
			}
		}

		if (isSort) {
			Collections.sort(msgList);
		}

		for (int i = 0; i < msgList.size(); i++) {
			String msg = (String) msgList.get(i);
			if (i > 0) {
				param.append(urlParamConnectFlag);
			}
			param.append(msg);
		}

		return param.toString();
	}

	public static String getAsynNotifySignMsg(Map<String, String> map, String urlParamConnectFlag, Set<String> removeKey) {
		String msg = null;
		try {
			msg = getAsynNotifyURLParam(map, urlParamConnectFlag, true, removeKey);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static String getAsynNotifyURLParam(Map<String, String> map, String urlParamConnectFlag, boolean isSort, Set<String> removeKey)
			throws UnsupportedEncodingException {
		StringBuffer param = new StringBuffer();
		List<String> msgList = new ArrayList<String>();
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			String value = (String) map.get(key);
			if ((removeKey == null) || (!removeKey.contains(key))) {
				if (key.equals("PlatTxMsg")) {
					msgList.add(key + "=" + URLDecoder.decode(toEmpty(value), "utf-8"));
				} else {
					msgList.add(key + "=" + toEmpty(value));
				}
			}
		}

		if (isSort) {
			Collections.sort(msgList);
		}

		for (int i = 0; i < msgList.size(); i++) {
			String msg = (String) msgList.get(i);
			if (i > 0) {
				param.append(urlParamConnectFlag);
			}
			param.append(msg);
		}

		return param.toString();
	}

	public static String toEmpty(String aStr) {
		return aStr == null ? "" : aStr;
	}

	/**
	 * 将map转化为形如key1=value1&key2=value2...
	 * 
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getWebForm(Map<String, String> map, String[] base64Keys, String charset, String urlParamConnectFlag)
			throws UnsupportedEncodingException {
		if (null == map || map.keySet().size() == 0) {
			return null;
		}
		if (charset == null || urlParamConnectFlag == null)
			return null;
		// 转换请求数据中特殊字段的值为Base64

		SignUtil.convertReqData(map, base64Keys, charset);
		System.out.println("转换请求数据中特殊字段的值为Base64的报文:" + getURLParam(map, urlParamConnectFlag, true, null));
		StringBuffer url = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String value = entry.getValue();
			String str = (value != null ? value : "");
			str = URLEncoder.encode(str, "UTF-8");
			url.append(entry.getKey()).append("=").append(str).append(urlParamConnectFlag);
		}

		// 最后一个键值对后面的“&”需要去掉。
		String strURL = null;
		strURL = url.toString();
		if (strURL != null && urlParamConnectFlag.equals(String.valueOf(strURL.charAt(strURL.length() - 1)))) {
			strURL = strURL.substring(0, strURL.length() - 1);
		}
		return strURL;
	}

	/**
	 * 转换特殊请求字段
	 * 
	 * @param paramMap
	 */
	public static void convertReqData(Map<String, String> paramMap, String[] base64Keys, String charset) {
		if (base64Keys == null)
			return;
		for (int i = 0; i < base64Keys.length; i++) {
			String key = base64Keys[i];
			String value = (String) paramMap.get(key);
			if (value != null) {
				try {
					String text = Base64.encode(value.getBytes(charset)).replace("+", "%2b");
					// 更新请求参数
					paramMap.put(key, text);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 转换特殊返回字段
	 * 
	 * @param paramMap
	 */
	public static void convertRespData(Map<String, String> paramMap, String[] base64Keys) {
		if (base64Keys == null)
			return;
		for (int i = 0; i < base64Keys.length; i++) {
			String key = base64Keys[i];
			String value = (String) paramMap.get(key);
			if (value != null) {
				try {
					String text = new String(Base64.decode(value.replace("%2b", "+")));
					// 更新请求参数
					paramMap.put(key, text);
				} catch (Exception e) {
				}
			}
		}
	}

	/*
	 * 获取请求地址
	 */
	public static String parseUrlFromGetReqData(String reqdata) {
		if (reqdata == null)
			return null;
		int s = reqdata.indexOf("?");
		if (s == -1) {
			return null;
		}
		return reqdata.substring(0, s);
	}

	/*
	 * 获取请求参数
	 */
	public static Map<String, String> parseReqMapFromGetReqData(String reqdata) {
		if (reqdata == null)
			return null;
		int s = reqdata.indexOf("?");
		if (s == -1) {
			return null;
		}
		String data = reqdata.substring(s + 1, reqdata.length());
		if (data == null)
			return null;
		String[] dataMap = data.split("&");
		if (dataMap == null || dataMap.length < 1)
			return null;
		int index = -1;
		String key = null;
		String value = null;

		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < dataMap.length; i++) {
			index = dataMap[i].indexOf("=");
			key = dataMap[i].substring(0, index);
			value = dataMap[i].substring(index + 1, dataMap[i].length());
			map.put(key, value);
		}
		return map;
	}
}