package com.apps.util;

import java.math.BigDecimal;

public class MathUtil {

	/**
	 * string转double
	 * 
	 * @param str
	 * @param isRet
	 *            true 保留两位小数 false 不做保留操作，直接转
	 * @return
	 */
	public static Float strToFloat(String str, boolean isRet) {
		Float aa = 0.00f;
		try {
			aa = Float.parseFloat(str);
			if (isRet) {
				BigDecimal b = new BigDecimal(aa);
				aa = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return aa;
	}

	/**
	 * 除法
	 * 
	 * @param chu
	 *            除数
	 * @param bchu
	 *            被除数
	 * @return
	 */
	public static Float divide(Float chu, Integer bchu) {
		if (bchu != null && bchu != 0) {
			BigDecimal a = new BigDecimal(chu);
			BigDecimal b = new BigDecimal(bchu);
			BigDecimal c = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);// 四舍五入
			return c.floatValue();
		}
		return 0f;
	}

	/**
	 * 除法
	 * 
	 * @param chu
	 *            除数
	 * @param bchu
	 *            被除数
	 * @return
	 */
	public static Float divide(BigDecimal chu, Integer bchu) {
		if (bchu != null && bchu != 0) {
			BigDecimal b = new BigDecimal(bchu);
			BigDecimal c = chu.divide(b, 2, BigDecimal.ROUND_HALF_UP);// 四舍五入
			return c.floatValue();
		}
		return 0f;
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static BigDecimal mul(BigDecimal b1, int v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2);
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(BigDecimal b1, BigDecimal b2) {
		return b1.add(b2).doubleValue();
	}

	public static void main(String[] args) {
		System.out.println(strToFloat("5", true));
		System.out.println(divide(6f, 3));
	}
	/**
	 * 两数相除向上取整
	 * @param a 除数
	 * @param b 被除数
	 * @return
	 */
	public static int getUpwardInt(int a, int b) {
		return (((double) a / (double) b) > (a / b) ? a / b + 1 : a / b);
	}
}
