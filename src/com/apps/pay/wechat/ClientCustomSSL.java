/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.apps.pay.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;

import com.apps.Constants;
import com.framework.util.StringUtil;
import com.framework.util.xml.XmlUtil;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class ClientCustomSSL {

	/**
	 * 
	 * @param orderNum
	 *            订单编号
	 * @param body
	 *            商品标题
	 * @param totalFee
	 *            支付价格 以分为单位计算
	 * @param callBack
	 *            异步回调地址
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> payOrder(String orderNum,
			String body, String totalFee, String callBack) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpclient = httpClientBuilder.build();
		try {
			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			HttpPost httpPost = new HttpPost(url);
			String str = genProductArgs(orderNum, body, totalFee, callBack);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(new StringEntity(str));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			String prepayId = "";
			try {
				HttpEntity entity = response.getEntity();
				StringBuffer result = new StringBuffer();
				if (entity != null) {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent()));
					String text = "";
					while ((text = bufferedReader.readLine()) != null) {
						result.append(text);
					}
					Document xmlDoc = XmlUtil.getDOMDocumentFromString(result
							.toString());
					prepayId = XmlUtil.getNodeTextValue(XmlUtil.selectNode(
							xmlDoc, "//prepay_id"));
				}
				EntityUtils.consume(entity);
				map = genOnceMoreProductArgs(prepayId);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		httpclient.close();
		return map;
	}

	/**
	 * 依据prepayId再次签名
	 * 
	 * @param prepayId
	 * @return
	 */
	private static HashMap genOnceMoreProductArgs(String prepayId) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			String s32 = StringUtil.getRandomString(32);
			long tamp = System.currentTimeMillis() / 1000;
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants
					.getWeChatAppid()));// 公众号
			packageParams.add(new BasicNameValuePair("noncestr", s32));// 随机安全字串32
			packageParams.add(new BasicNameValuePair("package", "Sign=WXPay"));//
			packageParams.add(new BasicNameValuePair("partnerid", Constants
					.getWeChatMchid()));// 商户id
			packageParams.add(new BasicNameValuePair("prepayid", prepayId));// 预支付订单
			packageParams.add(new BasicNameValuePair("timestamp", tamp + ""));
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			map.put("appid", Constants.getWeChatAppid());
			map.put("noncestr", s32);
			map.put("package", "Sign=WXPay");
			map.put("partnerid", Constants.getWeChatMchid());
			map.put("timestamp", tamp + "");
			map.put("sign", sign);
			map.put("prepayid", prepayId);
		} catch (Exception e) {
		}
		return map;
	}

	/**
	 * 
	 * @param orderNum
	 *            订单号
	 * @param body
	 *            订单内容
	 * @param totalFee
	 *            支付费用
	 * @param callBack
	 *            异步回调地址
	 * @return
	 */

	private static String genProductArgs(String orderNum, String body,
			String totalFee, String callBack) {
		try {
			// 参数顺序不要变动
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants
					.getWeChatAppid()));// 公众号
			packageParams.add(new BasicNameValuePair("body", body));// 商品名称
			packageParams.add(new BasicNameValuePair("mch_id", Constants
					.getWeChatMchid()));// 商户号
			packageParams.add(new BasicNameValuePair("nonce_str", StringUtil
					.getRandomString(32)));// 随机安全字串32
			packageParams.add(new BasicNameValuePair("notify_url", callBack));// 异步回调地址
			packageParams.add(new BasicNameValuePair("out_trade_no", orderNum));// 商户订单号
			packageParams.add(new BasicNameValuePair("total_fee", totalFee));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = toXml(packageParams);
			return new String(xmlstring.getBytes("UTF-8"), "ISO8859-1");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 生成签名
	 */
	private static String genPackageSign(List<NameValuePair> params)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.getWeChatKey());
		String packageSign = getMessageDigest(sb.toString().getBytes("UTF-8"))
				.toUpperCase();
		return packageSign;
	}

	private static String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");
			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) throws IOException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException,
			KeyManagementException, UnrecoverableKeyException {
		// 验证证书，可以不要
		// KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// FileInputStream instream = new FileInputStream(new
		// File(RamConstants.getWebRootPath()+"/wPay/apiclient_cert.p12"));
		// try {
		// keyStore.load(instream, "1249613601".toCharArray());
		// } finally {
		// instream.close();
		// }
		// SSLContext sslcontext = SSLContexts.custom()
		// .loadKeyMaterial(keyStore, "1249613601".toCharArray()).build();
		// SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		// sslcontext,
		// new String[] { "TLSv1" },
		// null,
		// SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		// CloseableHttpClient httpclient =
		// HttpClients.custom().setSSLSocketFactory(sslsf).build();

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpclient = httpClientBuilder.build();
		try {
			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			HttpPost httpPost = new HttpPost(url);
			String str = genProductArgs("12", "测试", "100",
					Constants.getWeChatCallBackEat());
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(new StringEntity(str));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			String prepayId = "";
			try {
				HttpEntity entity = response.getEntity();
				StringBuffer result = new StringBuffer();
				if (entity != null) {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent()));
					String text = "";
					while ((text = bufferedReader.readLine()) != null) {
						result.append(text);
					}
					System.out.println(result.toString());
					Document xmlDoc = XmlUtil.getDOMDocumentFromString(result
							.toString());
					prepayId = XmlUtil.getNodeTextValue(XmlUtil.selectNode(
							xmlDoc, "//prepay_id"));
				}
				EntityUtils.consume(entity);
				HashMap map = genOnceMoreProductArgs(prepayId);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		httpclient.close();
	}
}
