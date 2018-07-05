package help.pay;

import help.base.APIConstants;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.eff.PayCacheUtil;
import com.apps.model.PayConfig;
import com.apps.util.HttpRequest;
import com.apps.util.Util;
import com.framework.util.ParamUtils;

public class RenXinPayUtil {
	protected final static Log log = LogFactory.getLog(RenXinPayUtil.class);
	public static String partner = "16970";// 商户id
	public static String secretKey = "e446ca97c816e18f3b0b769d1bd440df";// 密钥
//	public static String partner = "16960";// 商户id
//	public static String secretKey = "270bbae38500459a90b7b2f49a9aa6ba";// 密钥
	public static String version = "3.0";// 版本号
	public static String method = "Rx.online.pay";// 方法名称
	public static String isshow = "0";// 该参数为支付宝扫码、微信、QQ钱包专用，默认为1，跳转到网关页面进行扫码，如设为0，则网关只返回二维码图片地址供用户自行调用

	static {
		reload();
	}
	public static void reload() {
		List<PayConfig> list = PayCacheUtil.getConfigList();
		for (PayConfig config : list) {
			String type = config.getType();
			if("1".equals(type)){
				partner=config.getPar();
				secretKey=config.getPayKey();
				log.info("_______仁信加载支付参数：：partner："+partner+"__secretKey："+secretKey);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
//		String url = "http://dpos.qqjun.cn/Online/GateWay";
//		String chuli = "version=3.0&method=Rx.online.pay&partner=16960&banktype=WEIXIN&paymoney=2.00&ordernumber=20161110161947427&callbackurl=http://www.youdomain.com/callback.aspx270bbae38500459a90b7b2f49a9aa6ba";
//		String sign1 = Util.encryption(chuli);
//		String param = "partner=16960&version=3.0&method=Rx.online.pay&banktype=WEIXIN&paymoney=2.00&ordernumber=20161110161947427&callbackurl=http://www.youdomain.com/callback.aspx&attach=mygod&isshow=0&";
//		param = param + "sign=" + sign1;
//		String result = HttpRequest.sendPost(url, param);
//		System.out.println(result);
//		System.out.println(sign1);
//		String jiami = "version=3.0&method=Rx.online.pay&partner=16960&banktype=ALIPAY&paymoney=2.00&ordernumber=20161110161947421&callbackurl=http://www.youdomain.com/callback.aspx270bbae38500459a90b7b2f49a9aa6ba";
//		System.out.println(Util.encryption(jiami));
//		System.out.println(Util.getMD5Str(jiami));
//		System.out.println("---------------------");
//		String sign = "1234567890abcdefghijklmnopqrstuvwxyz";
//		System.out.println(Util.encryption(sign));
//		System.out.println("928f7bcdcd08869cc44c1bf24e7abec6");
		
		PayDto dto=new PayDto();
dto.setOrderNo("4170608190207143619");
dto.setCallback("/api/a");
dto.setOrderPrice("1.00");
dto.setPayGateway("5");
		payOrder(dto);
		
	}

	/**
	 * 下单接口
	 * @param map
	 *            payGateway 支付方式 1.支付宝5.微信
	 * @return
	 */
	public static BxsPayBean payOrder(PayDto dto) {
		String orderNo = dto.getOrderNo();// 订单号
		String orderPrice = dto.getOrderPrice();// 支付金额
		String payGateway = dto.getPayGateway();// 支付方式
		String callback = dto.getCallback();
		String payType = "WEIXIN";
		if (APIConstants.PAY_TYPE_ALIPAY.equals(payGateway)) {
			payType = "ALIPAY";
		} else if (APIConstants.PAY_TYPE_WECHAT.equals(payGateway)) {
			payType = "WEIXIN";
		}

		String url = "http://dpos.qqjun.cn/Online/GateWay";
		String param = "version={VERSION}&method={METHOD}&partner={PARTNER}&banktype={BANKTYPE}&paymoney={PAYMONEY}&ordernumber={ORDERNUMBER}&callbackurl={CALLBACKURL}";
		param = param.replace("{VERSION}", version)
				.replace("{METHOD}", method).replace("{PARTNER}", partner)
				.replace("{BANKTYPE}", payType)
				.replace("{PAYMONEY}", orderPrice)
				.replace("{ORDERNUMBER}", orderNo)
				.replace("{CALLBACKURL}", callback);
		String sign="";
		try {
			sign = Util.encryption(param+secretKey);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		System.out.println(param);
		param = param + "&attach=mygod&isshow={ISSHOW}&sign={SIGN}";
		param=param.replace("{ISSHOW}", isshow).replace("{SIGN}", sign);
		String result = HttpRequest.sendPost(url, param);
		
		log.error("_____RenXinpay result::" + result);
		JSONObject obj;
		String code = "201";
		String msg = "";
		BxsPayBean bean = new BxsPayBean();
		if (ParamUtils.chkString(result) && !result.equals("null")) {
			try {
				obj = new JSONObject(result);
				code = obj.getString("status");
				msg = obj.getString("message");
				if (code.equals("1")) {// 成功
					String payUrl=obj.getString("qrurl");
					bean.setUrl(payUrl);
					code="200";
				} else {
				}
			} catch (JSONException e) {
			}
		}
		bean.setCode(code);
		bean.setMsg(msg);
		return bean;
	}

}
