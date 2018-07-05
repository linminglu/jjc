package help.pay;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apps.Constants;
import com.apps.util.QRCodeUtil;
import com.apps.util.UploadUtil;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 支付Demo
 * 
 */
public class QYFPayUtil {
	static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	static String merNo = "QYF201709081202";// 商户号
	public static String key = "22CBEEBDDD769F20A1BE915E6697E369";// 密钥,24位
	static String wxreqUrl = "http://wx.qyfpay.com:90/api/pay.action";
	static String alipayreqUrl = "http://zfb.qyfpay.com:90/api/pay.action";
	static String alipayWapReqUrl = "http://zfbwap.qyfpay.com:90/api/pay.action";
	
	static String reqUrl = "http://139.199.195.194:8080/api/pay.action";

	public static void main(String[] args) throws Throwable {
//		payOrder();
	}

	/**
	 * 支付结果处理
	 * @throws Throwable 
	 */
	public static void result(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		String data = request.getParameter("data");
		JSONObject jsonObj = JSONObject.fromObject(data);
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		metaSignMap.put("merNo", jsonObj.getString("merNo"));
		metaSignMap.put("netway", jsonObj.getString("netway"));
		metaSignMap.put("orderNum", jsonObj.getString("orderNum"));
		metaSignMap.put("amount", jsonObj.getString("amount"));
		metaSignMap.put("goodsName", jsonObj.getString("goodsName"));
		metaSignMap.put("payResult", jsonObj.getString("payResult"));// 支付状态
		metaSignMap.put("payDate", jsonObj.getString("payDate"));// yyyyMMddHHmmss
		String jsonStr = mapToJson(metaSignMap);
		String sign = MD5(jsonStr.toString() + key, "UTF-8");
		if(!sign.equals(jsonObj.getString("sign"))){
			return;
		}
		System.out.println("签名校验成功");
		response.getOutputStream().write("0".getBytes());
	}

	/**
	 * 支付方法
	 * dto.payGateway 支付方式 1.支付宝5.微信
	 * @return 
	 */
	public static BxsPayBean payOrder(PayDto dto) {
		String netway="";
		String url="";
		String payGateway = dto.getPayGateway();
		if(payGateway.equals("1")){//支付宝
			netway="ZFB";
			url=alipayreqUrl;
		}else if(payGateway.equals("5")){//微信
			netway="WX";
			url=wxreqUrl;
		}
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		metaSignMap.put("version", "V2.0.0.0");
		metaSignMap.put("merNo", merNo);
		metaSignMap.put("netway", netway);// WX:微信支付,ZFB:支付宝支付 ZFB_WAP:支付宝wap
		metaSignMap.put("random", randomStr(4));// 4位随机数
		String orderNum =dto.getOrderNo(); // 20位
		metaSignMap.put("orderNum", orderNum);
		String orderPrice = dto.getOrderPrice();
		orderPrice = ParamUtils.BigFormatIntString(new BigDecimal(orderPrice));
		
		metaSignMap.put("amount", orderPrice);// 单位:分
		metaSignMap.put("goodsName", dto.getTitle());// 商品名称：20位
		
		metaSignMap.put("callBackUrl", dto.getCallback());// 回调地址
		metaSignMap.put("callBackViewUrl", dto.getPageUrl());// 回显地址
		metaSignMap.put("charset", "UTF-8");//
		String metaSignJsonStr = mapToJson(metaSignMap);
		String sign = MD5(metaSignJsonStr + key, "UTF-8");// 32位
		System.out.println("sign=" + sign); // 英文字母大写
		metaSignMap.put("sign", sign);
		String reqParam = "data=" + mapToJson(metaSignMap);
		String resultJsonStr = request(url, reqParam);

		// 检查状态
		JSONObject resultJsonObj = JSONObject.fromObject(resultJsonStr);
		String stateCode = resultJsonObj.getString("stateCode");
		BxsPayBean bean = new BxsPayBean();
		if (stateCode.equals("00")) {
			String qrcodeUrl = resultJsonObj.getString("qrcodeUrl");
			//生成二维码
			String codeImg="";
			if(ParamUtils.chkString(qrcodeUrl)){
				String QRName = orderNum + ".png";
				String path = Constants.getFileUploadPath()+ UploadUtil.getFolderYYYYMM("/payCode");
				String QRCodePath = Constants.getWebRootPath() + path;
				int width = 300;
				int height = 300;
				String format = "png";
				QRCodeUtil.genQRCodeImg(qrcodeUrl, width, height,format, QRCodePath + "/" + QRName);
				codeImg = path + "/" + QRName;
				System.out.println("QRCodePath::"+QRCodePath);
				System.out.println("QRName"+QRName);
			}
			System.out.println("codeImg"+codeImg);
			bean.setCode("200");
			bean.setMsg("");
			bean.setUrl(StringUtil.checkAPIHttpUrl(codeImg));
		}else{
			String msg = resultJsonObj.getString("msg");
			bean.setCode("201");
			bean.setMsg(msg);
		}
//		String resultSign = resultJsonObj.getString("sign");
//		resultJsonObj.remove("sign");
//		String targetString = MD5(resultJsonObj.toString() + key, "UTF-8");
//		if (targetString.equals(resultSign)) {
//			System.out.println("签名校验成功");
//		}
		return bean;
	}

	private static String getResponseBodyAsString(InputStream in) {
		try {
			BufferedInputStream buf = new BufferedInputStream(in);
			byte[] buffer = new byte[1024];
			StringBuffer data = new StringBuffer();
			int readDataLen;
			while ((readDataLen = buf.read(buffer)) != -1) {
				data.append(new String(buffer, 0, readDataLen, "UTF-8"));
			}
			System.out.println("响应报文=" + data);
			return data.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String request(String url, String params) {
		try {
			System.out.println("请求报文:" + params);
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(1000 * 5);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",
					String.valueOf(params.length()));
			OutputStream outStream = conn.getOutputStream();
			outStream.write(params.toString().getBytes("UTF-8"));
			outStream.flush();
			outStream.close();
			return getResponseBodyAsString(conn.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static String MD5(String s, String encoding) {
		try {
			byte[] btInput = s.getBytes(encoding);
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
				str[k++] = HEX_DIGITS[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String mapToJson(Map<String, String> map) {
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		StringBuffer json = new StringBuffer();
		json.append("{");
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			json.append("\"").append(key).append("\"");
			json.append(":");
			json.append("\"").append(value).append("\"");
			if (it.hasNext()) {
				json.append(",");
			}
		}
		json.append("}");
		System.out.println("mapToJson=" + json.toString());
		return json.toString();
	}

	public static String randomStr(int num) {
		char[] randomMetaData = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
				'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
				'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
				'3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random();
		String tNonceStr = "";
		for (int i = 0; i < num; i++) {
			tNonceStr += (randomMetaData[random
					.nextInt(randomMetaData.length - 1)]);
		}
		return tNonceStr;
	}
}
