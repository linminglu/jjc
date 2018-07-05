package help.pay;

import help.base.APIConstants;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.eff.PayCacheUtil;
import com.apps.model.PayConfig;
import com.apps.util.HttpRequest;
import com.apps.util.Util;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;

/**
 * 闪付
 * 
 * @author Mr.zang
 * 
 */
public class ShanFuPayUtil {
	protected final static Log log = LogFactory.getLog(ShanFuPayUtil.class);
	public static String partner = "610699";// 商户id
	public static String terminalId = "10699";// 终端id
	public static String secretKey = "jER03veW670UcDKK8RRuWXWe";// 密钥
	public static String version = "4.0";// 版本号
	public static String method = "";// 方法名称
	public static String isshow = "0";// 该参数为支付宝扫码、微信、QQ钱包专用，默认为1，跳转到网关页面进行扫码，如设为0，则网关只返回二维码图片地址供用户自行调用

	
	static {
		reload();
	}
	public static void reload() {
		List<PayConfig> list = PayCacheUtil.getConfigList();
		for (PayConfig config : list) {
			String type = config.getType();
			if("2".equals(type)){
				partner=config.getPar();
				terminalId=config.getCode();
				secretKey=config.getPayKey();
				log.info("_______闪付加载支付参数：：partner："+partner+"__terminalId："+terminalId+"__secretKey："+secretKey);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		PayDto dto = new PayDto();
		dto.setOrderNo("4170612110711107514");
		dto.setCallback(StringUtil
				.checkAPIHttpUrl("/api/recharge_callbackShanfu"));
		dto.setOrderPrice("1.00");
		dto.setPayGateway("5");
		payOrder(dto);

	}

	/**
	 * 下单接口
	 * 
	 * @param map
	 *            payGateway 支付方式 1.支付宝5.微信
	 * @return
	 */
	public static BxsPayBean payOrder(PayDto dto) {
		String orderNo = dto.getOrderNo();// 订单号
		String orderPrice = dto.getOrderPrice();// 支付金额
		String payGateway = dto.getPayGateway();// 支付方式
		String callback = dto.getCallback();
		String payType = "57";// 微信
		if (APIConstants.PAY_TYPE_ALIPAY.equals(payGateway)) {
			payType = "758";
		} else if (APIConstants.PAY_TYPE_WECHAT.equals(payGateway)) {
			payType = "57";
		}

		String format = "yyyyMMddhhmmss";
		String tradeDate = DateTimeUtil.dateToString(new Date(), format);
		String url = "http://gw.sfvipgate.com/v4.aspx";
		String signParam = "{MERCHANTID}|{PAYID}|{TRADEDATE}|{TRANSID}|{ORDERMONEY}|{PAGEURL}|{RETURNURL}|{NOTICETYPE}|{SECRETKEY}";
		signParam = signParam
				.replace("{MERCHANTID}", partner)
				.replace("{PAYID}", payType)
				.replace("{TRADEDATE}", tradeDate)
				.replace("{TRANSID}", orderNo)
				.replace(
						"{ORDERMONEY}",
						ParamUtils
								.BigFormatIntString(new BigDecimal(orderPrice)))
				.replace("{PAGEURL}", callback).replace("{RETURNURL}", callback)
				.replace("{NOTICETYPE}", "1").replace("{SECRETKEY}", secretKey);
		String sign = "";
		try {
			sign = Util.encryption(signParam);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String param = "MemberID={MEMBERID}&TerminalID={TERMINALID}&InterfaceVersion={INTERFACEVERSION}&KeyType=1&PayID={PAYID}&TradeDate={TRADEDATE}&TransID={TRANSID}&OrderMoney={ORDERMONEY}&NoticeType=1&PageUrl={PAGEURL}&ReturnUrl={RETURNURL}&Md5Sign={SIGN}";
		param =param.replace("{MEMBERID}", partner)
		.replace("{TERMINALID}", terminalId)
		.replace("{INTERFACEVERSION}", version)
		.replace("{PAYID}", payType)
		.replace("{TRADEDATE}", tradeDate)
		.replace("{TRANSID}", orderNo)
		.replace(
				"{ORDERMONEY}",
				ParamUtils
						.BigFormatIntString(new BigDecimal(orderPrice)))
		.replace("{PAGEURL}", callback).replace("{RETURNURL}", callback)
		.replace("{SIGN}", sign);
		String result = HttpRequest.sendPost(url, param);

		log.error("_____ShanFupay result::" + result);
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
					String payUrl = obj.getString("qrurl");
					bean.setUrl(payUrl);
					code = "200";
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
