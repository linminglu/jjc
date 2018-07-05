package help.pay.lingdian.Config;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import help.pay.lingdian.Utils.HttpClientUtil;
import help.pay.lingdian.Utils.SignUtil;

/**
 * 商户配置信息
 * @author Administrator
 *
 */
public class MerConfig{
	//参数分隔符
	public static final String URL_PARAM_CONNECT_FLAG = "&";
	//字符编码
	public static final String CHARSET="UTF-8";
	//签名类型
	public static final String SIGNMETHOD="MD5";
	// 需做Base64加密
	public static final String[] base64Keys = 
			new String[] { "CodeUrl", "ImgUrl", "Token_Id","PayInfo","sPayUrl","PayUrl","NotifyUrl","ReturnUrl"};
	/*
	//密钥
	public static final String MD5KEY="aeac69bb25d9b1a87754b32701f4ff77";
	//商户号
	public static final String MERNO="00330001";
	//交易成功异步通知地址  商户自己设置异步URL地址
	public static final String NOTIFYURL="http://XXX.com.cn:1001";
	//请求地址
	public static final String REQURL = "http://test.1yigou.com.cn:8811/merchant-trade-api/command";
	*/
	//密钥
	public static final String MD5KEY="4006c2bb70232aa1fc3c4c2e154ff841";
	//商户号
	public static final String MERNO="88320049";
	//交易成功异步通知地址  商户自己设置异步URL地址
	public static final String NOTIFYURL="http://api.1yigou.com.cn:1001";
	//请求地址
	public static final String REQURL = "http://api.1yigou.com.cn:8881/merchant-trade-api/command";

	/**
	 * 设置签名
	 * 
	 * @param paramMap
	 */
	public static void setSignature(Map<String, String> paramMap) {
		String signMethod = paramMap.get("SignMethod");
		Set<String> removeKey = new HashSet<String>();
		removeKey.add("SignMethod");
		removeKey.add("Signature");
		String signMsg = SignUtil.getSignMsg(paramMap,MerConfig.URL_PARAM_CONNECT_FLAG, removeKey);
		System.out.println("需签名报文末尾未加密钥串:"+signMsg);
		
		String signature = SignUtil.sign(signMethod, signMsg,MerConfig.MD5KEY,MerConfig.CHARSET);
		System.out.println("生成的签名:"+signature);
//		若签名有时能过有时不能过,请把+号做转义后再测试
//		+号做转义
//		if(signature.indexOf("+")!=-1){
//			signature = signature.replaceAll("\\+", "%2B");
//		}
		
		paramMap.put("Signature", signature);
		
	}
	
	/**
	 * 往渠道发送数据
	 * 
	 * @param url
	 *            通讯地址
	 * @param paramMap
	 *            发送参数
	 * @return 应答消息
	 */
	public static String sendMsg(String url, Map<String, String> paramMap) {
		try {
			// 转换参数格式
			String requestMsg = SignUtil.getWebForm(paramMap,MerConfig.base64Keys,MerConfig.CHARSET, MerConfig.URL_PARAM_CONNECT_FLAG);
			System.out.println("通过URLEncode处理后发送的报文:"+requestMsg);
			String rspMsg = HttpClientUtil.sendHttpMsg(url,"POST", requestMsg);
			System.out.println("返回数据:"+rspMsg);
			return rspMsg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 验签
	 * 
	 * @param paramMap
	 * @return
	 */
	public static boolean verifySign(Map<String,String> paramMap) {
		// 计算签名
		Set<String> removeKey = new HashSet<String>();
		removeKey.add("SignMethod");
		removeKey.add("Signature");
		String signedMsg = SignUtil.getSignMsg(paramMap,MerConfig.URL_PARAM_CONNECT_FLAG, removeKey);
		String signMethod = (String) paramMap.get("SignMethod");
		String signature = (String) paramMap.get("Signature");
		System.out.println("签名字符串:"+signedMsg);
		return SignUtil.verifySign(signMethod, signedMsg, signature, MerConfig.MD5KEY,MerConfig.CHARSET);
	}
	
	
	/**
	 * 验签
	 * 
	 * @param paramMap
	 * @return
	 */
	public static boolean verifyAsynNotifySign(Map<String,String> paramMap) {
		// 计算签名
		Set<String> removeKey = new HashSet<String>();
		removeKey.add("SignMethod");
		removeKey.add("Signature");
		removeKey.add("method");
		String signedMsg = SignUtil.getAsynNotifySignMsg(paramMap,MerConfig.URL_PARAM_CONNECT_FLAG, removeKey);
		String signMethod = (String) paramMap.get("SignMethod");
		String signature = (String) paramMap.get("Signature");
		System.out.println("签名字符串:"+signedMsg);
		return SignUtil.verifySign(signMethod, signedMsg, signature, MerConfig.MD5KEY,MerConfig.CHARSET);
	}
	
	public static void main(String[] args) {
		Map<String,String> paramMap = new HashMap<String,String>();	
		paramMap.put("TxCode", "230000");
		paramMap.put("MerNo", "88570021");
		paramMap.put("TxSN", "20170921114511154627");
		paramMap.put("Amount", "100");
		paramMap.put("PdtName", "CZ-1");
		paramMap.put("Remark", "b");
		paramMap.put("Status", "1");
		paramMap.put("PlatTxSN", "S602117092100000264");
//		paramMap.put("PlatTxMsg", "%E6%88%90%E5%8A%9F");
		paramMap.put("PlatTxMsg", "成功");
		paramMap.put("BankTxSN", "03375009201709210000000020");
		paramMap.put("TxTime", "20170921114545");
		paramMap.put("PayFee", "1");
		paramMap.put("SignMethod", "MD5");
		paramMap.put("method", "callbackLingdian");
		paramMap.put("Signature", "8aa3941f9398ecd4d142a2e8c9221d21");
		
//		Map<String,String> paramMap2 = new HashMap<String,String>();	
//		paramMap2.put("TxCode", "230000");
//		paramMap2.put("MerNo", "88570021");
//		paramMap2.put("TxSN", "20170920175305737348");
//		paramMap2.put("Amount", "1000");
//		paramMap2.put("PdtName", "CZ-10");
//		paramMap2.put("Remark", "b");
//		paramMap2.put("Status", "1");
//		paramMap2.put("PlatTxSN", "S602117092000000137");
//		paramMap2.put("PlatTxMsg", "%E6%88%90%E5%8A%9F");
//		paramMap2.put("BankTxSN", "03375009201709200000000010");
//		paramMap2.put("TxTime", "20170920175323");
//		paramMap2.put("PayFee", "100");
//		paramMap2.put("SignMethod", "MD5");
//		paramMap2.put("Signature", "23b09041788a86e627e38ccc42e0b2c6");
		
		System.out.println(verifyAsynNotifySign(paramMap));
//		System.out.println(verifyAsynNotifySign(paramMap2));
	}
}