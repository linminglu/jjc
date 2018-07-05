package help.pay;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.util.HttpRequest;
import com.framework.util.ParamUtils;
import com.ram.util.MD5;

public class BxsPayUtil {
	protected final static Log log = LogFactory.getLog(BxsPayUtil.class);
	public static String mchId = "10108168";// 商户号
	public static String secretKey = "7pv35297rcn9et5f295c5d5qc899789k";// 密钥
	public static String ver = "1.0";// 版本号

	
	static {
		reload();
	}
	public static void reload() {
//		List<PayConfig> list = PayCacheUtil.getConfigList();
//		for (PayConfig config : list) {
//			String type = config.getType();
//			if("4".equals(type)){
//				mchId=config.getPar();
//				secretKey=config.getPayKey();
//				log.info("_______大面包加载支付参数：：mchid："+mchId+"__secretKey："+secretKey);
//			}
//		}
	}
	
	/**
	 * 
	 * @param map
	 *            payGateway 支付方式 1.微信支付 2.支付宝
	 * 
	 * @return
	 */
	public static BxsPayBean payOrder(PayDto dto) {
		String url = "http://mqpay.bigmianbao.com/mqpay/api/pay_order";
		String orderNo = dto.getOrderNo();// 订单号
		String orderPrice = dto.getOrderPrice();// 支付金额
		String orderStamp = dto.getOrderStamp();// 时间戳
		String payGateway = dto.getPayGateway();// 支付方式
		String sign = getSignPay(dto);
		String payMode = dto.getPayMode();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ver", ver);
		paramMap.put("merchantNo", mchId);
		paramMap.put("payGateway", payGateway);
		paramMap.put("payMode", payMode);
		paramMap.put("orderNo", orderNo);
		paramMap.put("orderPrice", orderPrice);
		paramMap.put("orderStamp", orderStamp);
		paramMap.put("sign", sign);
		paramMap.put("notifyUrl", dto.getCallback());
		paramMap.put("scene", dto.getScene());//应用场景 1.pc 2.app 3.wap 4.wechat
		paramMap.put("returnUrl", dto.getReturnUrl());
		// String result = HttpRequest.sendPost(url, paramMap);
		String result = HttpRequest.doGet(url, paramMap);
		log.error("_____Bxspay result::" + result);
		JSONObject obj;
		String code = "201";
		String msg = "";
		BxsPayBean bean = new BxsPayBean();
		if (ParamUtils.chkString(result) && !result.equals("null")) {
			try {
				obj = new JSONObject(result);
				code = obj.getString("code");
				msg = obj.getString("msg");
				if (code.equals("200")) {// 成功
					JSONObject data = obj.getJSONObject("data");
					String payUrl = data.getString("payUrl");
					bean.setUrl(payUrl);
				} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		bean.setCode(code);
		bean.setMsg(msg);
		return bean;
	}

	/**
	 * 获得支付签名
	 * 
	 * @param dto
	 * @return
	 */
	public static String getSignPay(PayDto dto) {
		String sginUrl = "ver=VER&merchantNo=MERCHANTNO&payGateway=PAYGATEWAY&payMode=PAYMODE&orderNo=ORDERNO&orderPrice=ORDERPRICE&orderStamp=ORDERSTAMP&secretKey=SECRETKEY";
		String orderNo = dto.getOrderNo();// 订单号
		String orderPrice = dto.getOrderPrice();// 支付金额
		String orderStamp = dto.getOrderStamp();// 时间戳
		String payGateway = dto.getPayGateway();// 支付方式
		String payMode = dto.getPayMode();
		sginUrl = sginUrl.replace("VER", ver).replace("MERCHANTNO", mchId)
				.replace("PAYGATEWAY", payGateway).replace("PAYMODE", payMode)
				.replace("ORDERNO", orderNo).replace("ORDERPRICE", orderPrice)
				.replace("ORDERSTAMP", orderStamp)
				.replace("SECRETKEY", secretKey);
		return MD5.exc(sginUrl).toLowerCase();
	}
}
