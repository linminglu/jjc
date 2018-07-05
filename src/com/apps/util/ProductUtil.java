package com.apps.util;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import com.apps.eff.GameHelpUtil;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;

/**
 * 商品相关的
 * 
 * @author Mr.zang
 * 
 */
public class ProductUtil {
	/**
	 * 生成一个消费码
	 * 
	 * @return
	 */
	public static String createXFCode() {
//		String uuid = StringUtil.getUUID(12);
//		return uuid.toUpperCase();
		String rasc = "";
		int number = 0;
		Random random = new Random();
		int count=12;
		for (int i = 0; i < count; i++) {
			number = random.nextInt(30);
			rasc = rasc + StringUtil.getNumber(number);
		}
		return rasc;
		
	}

	/**
	 * 生成订单号 19位
	 * 
	 * @param type
	 *            1.订餐 2.电商3.团购
	 * @return
	 */
	public static String createOrderNum(String type) {
		SimpleDateFormat dateform = new SimpleDateFormat("yyMMddHHmmhhSSS");// notice
		// here
		GregorianCalendar calendar = new GregorianCalendar();
		String s = dateform.format(calendar.getTime());
		Random r = new Random();
		Integer randint = r.nextInt(900) + 100;
		return type + s + randint;
	}
	/**
	 * 生成订单号 19位
	 * 
	 * @return
	 */
	public static String createOrderNum2() {
		SimpleDateFormat dateform = new SimpleDateFormat("yyyyMMddHHmmhhSSS");// notice
		// here
		GregorianCalendar calendar = new GregorianCalendar();
		String s = dateform.format(calendar.getTime());
		Random r = new Random();
		Integer randint = r.nextInt(900) + 100;
		return  s + randint;
	}

	/**
	 * 生成订单号 18位
	 * 
	 * @return
	 */
	public static String createOrderNum() {
		SimpleDateFormat dateform = new SimpleDateFormat("yyMMddHHmmhhSSS");// notice
																			// here
		GregorianCalendar calendar = new GregorianCalendar();
		String s = dateform.format(calendar.getTime());
		Random r = new Random();
		Integer randint = r.nextInt(900) + 100;
		return s + randint;
	}

	/**
	 * 提交订单时的送餐时间段
	 * 
	 * @param hBegin
	 *            开始小时
	 * @param mBegin
	 *            开始分钟
	 * @param hEnd
	 *            结束小时
	 * @param mEnd
	 *            结束分钟
	 * @param step
	 *            步
	 * @return
	 */
	public static String[] getDates(int hBegin, int mBegin, int hEnd, int mEnd,
			int step) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("立即送货");
		while (true) {
			DecimalFormat df = new DecimalFormat("#00");
			list.add(String.valueOf(hBegin) + ":" + df.format(mBegin));
			mBegin += step;
			if (mBegin >= 60) {
				mBegin = mBegin - 60;
				hBegin++;
			}
			if (hBegin > hEnd || (hBegin == hEnd && mBegin > mEnd)) {
				break;
			}
		}
		int size = list.size();
		String[] arr = (String[]) list.toArray(new String[size]);
		return arr;
	}

	/**
	 * 判断是合并订单id还是订单号 <br/>
	 * 在支付回调时用到
	 * 
	 * @return <b>true</b> omId <br/>
	 *         <b>false</b> orderNum
	 */
	public static boolean chkOmIdOrOrderNum(String str) {
		if (str.length() < 10) {// omId不会大于10位的
			return true;
		}
		return false;
	}

	/**
	 * 保留小数点后2位
	 * 
	 * @param obj
	 * @return
	 */
	public static String BigFormat(Object obj) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(obj);
	}

	/**
	 * 整数，小数点一位
	 * 
	 * @param obj
	 * @return
	 */
	public static String BigFormat1(Object obj) {
		DecimalFormat df = new DecimalFormat("#0.0");
		return df.format(obj);
	}

	/**
	 * 整数，不要小数点
	 * 
	 * @param obj
	 * @return
	 */
	public static String BigFormat2(Object obj) {
		DecimalFormat df = new DecimalFormat("#0");
		return df.format(obj);
	}

	/**
	 * a与b比较大小
	 * 
	 * @param a
	 * @param b
	 * @return -1 小于 0 等于 1 大于
	 */
	public static int compareTo(BigDecimal a, BigDecimal b) {
		return a.compareTo(b);
	}

	/**
	 * 判断小数点后有几位有效小数，再保留位数
	 * 
	 * @param obj
	 * @return
	 */
	public static String BigFormatJud(Object obj) {
		if(obj==null) return "";
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			return df.format(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 检查BigDecimal不为空，为空返回0
	 * @param big
	 * @return
	 */
	public static BigDecimal checkBigDecimal(BigDecimal big) {
		if(big==null) return new BigDecimal(0);
		try {
			//big.doubleValue();
			big = GameHelpUtil.round(big);
		} catch (Exception e) {
			e.printStackTrace();
			return new BigDecimal(0);
		}
		return big;
	}

	/**
	 * 隐藏手机号中间4位
	 * 
	 * @param phone
	 * @return
	 */
	public static String hidePhone(String phone) {
		String temp = "";
		if (StringUtil.isMobileNO(phone)) {
			temp = phone.substring(0, 3) + "****"
					+ phone.substring(7, phone.length());
		} else {
			temp = phone;
		}
		return temp;
	}


	
	/**
	 * 获取支付别名短语
	 * [支付别名: 支付宝/微信/银联/余额/货到付款]
	 * @param type 支付方式
	 * @param payType 支付类型
	 * @return String
	 */
	public static String getPayAlias(String type,String payType){
		if(type.equals(APIConstants.PAY_LINE)){//在线支付
			if(ParamUtils.chkString(payType)){
				if(payType.equals(APIConstants.PAY_TYPE_ALIPAY)){
					return APIConstants.PAY_TYPE_ALIPAY_ALIAS;
				}else if(payType.equals(APIConstants.PAY_TYPE_WECHAT)){
					return APIConstants.PAY_TYPE_WECHAT_ALIAS;
				}else if(payType.equals(APIConstants.PAY_TYPE_WECHAT_JS)){
					return APIConstants.PAY_TYPE_WECHAT_JS_ALIAS;
				}else if(payType.equals(APIConstants.PAY_TYPE_UNIONPAY)){
					return APIConstants.PAY_TYPE_UNIONPAY_ALIAS;
				}
			}
		}else if(type.equals(APIConstants.PAY_YUE)){//余额支付
			return APIConstants.PAY_YUE_ALIAS;
		}else if(type.equals(APIConstants.PAY_NOT_PAY)){//货到付款
			return APIConstants.PAY_NOT_PAY_ALIAS;
		}
		return "";
	}

}
