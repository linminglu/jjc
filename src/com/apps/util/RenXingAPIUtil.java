package com.apps.util;

public class RenXingAPIUtil {
	
	public static String partner="";//商户id
	public static String version="3.0";//版本号
	public static String method="Rx.online.pay";//方法名称
	
	public static String callbackurl="";//异步回调地址
	
	public static String isshow="0";//该参数为支付宝扫码、微信、QQ钱包专用，默认为1，跳转到网关页面进行扫码，如设为0，则网关只返回二维码图片地址供用户自行调用
	

	public static void main(String[] args) throws Exception {
		
		String url="http://dpos.qqjun.cn/Online/GateWay";
		
		String chuli="version=3.0&method=Rx.online.pay&partner=16960&banktype=WEIXIN&paymoney=2.00&ordernumber=20161110161947427&callbackurl=http://www.youdomain.com/callback.aspx270bbae38500459a90b7b2f49a9aa6ba";
		String sign1=Util.encryption(chuli);
		String param="partner=16960&version=3.0&method=Rx.online.pay&banktype=WEIXIN&paymoney=2.00&ordernumber=20161110161947427&callbackurl=http://www.youdomain.com/callback.aspx&attach=mygod&isshow=0&";
		param=param+"sign="+sign1;
		String result=HttpRequest.sendPost(url, param);
		
		
		System.out.println(result);
		System.out.println(sign1);		
		String jiami="version=3.0&method=Rx.online.pay&partner=16960&banktype=ALIPAY&paymoney=2.00&ordernumber=20161110161947421&callbackurl=http://www.youdomain.com/callback.aspx270bbae38500459a90b7b2f49a9aa6ba";
		System.out.println(Util.encryption(jiami));
		System.out.println(Util.getMD5Str(jiami));
		System.out.println("---------------------");
		String  sign="1234567890abcdefghijklmnopqrstuvwxyz";	
		System.out.println(Util.encryption(sign));
		System.out.println("928f7bcdcd08869cc44c1bf24e7abec6");
		
	}
	
}
