package help.pay;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.eff.PayCacheUtil;
import com.apps.model.Param;
import com.apps.model.PayConfig;
import com.apps.util.HttpRequest;
import com.apps.util.MapKeyComparator;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.ram.util.MD5;

/**
 * 艾米森
 * 
 * @author Mr.zang
 * 
 */
public class AmxmyPayUtil {
	protected final static Log log = LogFactory.getLog(AmxmyPayUtil.class);
	public static String mchid = "135596";// 商户号
	public static String srcCode = "AMDaz51497445556F7xke";// 商户唯一标识
	public static String secretKey = "55CE5sXFXp1497445556lCvlwEAGHLN";// 密钥
	public static String version = "";// 版本号
	public static String method = "";// 方法名称
	public static String isshow = "";// 该参数为支付宝扫码、微信、QQ钱包专用，默认为1，跳转到网关页面进行扫码，如设为0，则网关只返回二维码图片地址供用户自行调用

	static {
		reload();
	}
	public static void reload() {
		List<PayConfig> list = PayCacheUtil.getConfigList();
		for (PayConfig config : list) {
			String type = config.getType();
			if("3".equals(type)){
				mchid=config.getPar();
				srcCode=config.getCode();
				secretKey=config.getPayKey();
				log.info("_______艾米森加载支付参数：：mchid："+mchid+"__srcCode："+srcCode+"__secretKey："+secretKey);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
//		PayDto dto = new PayDto();
//		dto.setOrderNo("4170612110711107514");
//		dto.setCallback(StringUtil
//				.checkAPIHttpUrl("/api/recharge_callbackShanfu"));
//		dto.setPageUrl(StringUtil
//				.checkAPIHttpUrl("/pay/amxmyPayResult.jsp"));
//		dto.setPageUrl("http://a.com/pay/amxmyPayResult.jsp?a=1");
//		dto.setOrderPrice("2.00");
//		dto.setPayGateway("5");
//		payOrder(dto);

		String url="http://api.amxmy.top/trade/query";
//		IServiceLocator serviceLocator = ServiceLocatorImpl.getInstance();
//		ICardService cardService = (ICardService) ServiceLocatorImpl.getInstance().getService("cardService");
//		String orderNo = "4170621140502290370";// 商户 ID
		String format = "yyyyMMddhhmmss";
		String startDate = DateTimeUtil.dateToString(DateTimeUtil.getCurrentDayStart(), format);
		String endDate = DateTimeUtil.dateToString(new Date(), format);
		Map<String, String> map=new HashMap<String, String>();
//		map.put("start_time",startDate);
//		map.put("end_time",endDate);
		map.put("page","2");
		map.put("src_code",AmxmyPayUtil.srcCode);
//		map.put("out_trade_no",orderNo);
		String sign=AmxmyPayUtil.sign(map);
		map.put("sign",sign);
		String result = HttpRequest.sendPost(url, map);
		JSONObject obj = new JSONObject(result);
		String code = obj.getString("respcd");
		String msg = obj.getString("respmsg");
		System.out.println(result);
		JSONArray data=obj.getJSONArray("data");
		if (code.equals("0000")) {// 查询成功
			for (int i = 0; i < data.length(); i++) {
				JSONObject obj2=data.getJSONObject(i);
				int status=obj2.getInt("order_status");
				String tradeNo=obj2.getString("trade_no");
				int factMoney=obj2.getInt("total_fee");
				String orderNo=obj2.getString("out_trade_no");
				
				if(3==status){//支付成功
//					CardRechargeOrder order=cardService.getRechargeOrderByOrderNum(orderNo);
//					System.out.println("____orderNo::"+orderNo+"____order::"+order);
//					if(order!=null){
//						BigDecimal payPrice=order.getPayPrice();
//						String s=ParamUtils.BigFormatIntString(payPrice);
//						BigDecimal factPrice=new BigDecimal(factMoney);
//						if(new BigDecimal(s).compareTo(factPrice)==0){
//							order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
//							order.setPayTime(new Date());
//							order.setTradeNo(tradeNo);
//							cardService.updateUserRecharge(order);
//						}
//					}
					
				}
			}
		}
		
		
	}
	
	/**
	 * 使用 Map按key进行排序
	 * @param map
	 * @return
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}
	
	public static String sign(Map<String, String> map){
		Map<String, String> resultMap = sortMapByKey(map);	//按Key进行排序
		StringBuffer signParam=new StringBuffer();
		for (Map.Entry<String, String> entry : resultMap.entrySet()) {
			signParam.append(entry.getKey()+"="+entry.getValue()+"&");
		}
		signParam.append("key="+secretKey);
//		System.out.println(signParam.toString());
		return MD5.exc(signParam.toString());
	}

	/**
	 * 下单接口
	 * 
	 * @param map
	 *            payGateway 支付方式 1.支付宝5.微信
	 * @return
	 */
	public static BxsPayBean payOrder(PayDto dto) {
		String url="http://api.amxmy.top/trade/pay_v2";
		String orderNo = dto.getOrderNo();// 订单号
		String orderPrice = dto.getOrderPrice();// 支付金额
		String payGateway = dto.getPayGateway();// 支付方式
		String callback = dto.getCallback();
		String pageUrl = dto.getPageUrl();
		
		String payType = "50104";// 微信
		if (APIConstants.PAY_TYPE_ALIPAY.equals(payGateway)) {
			payType = "60104";
		} else if (APIConstants.PAY_TYPE_WECHAT.equals(payGateway)) {
			payType = "50104";
		}
		String format = "yyyyMMddhhmmss";
		String tradeDate = DateTimeUtil.dateToString(new Date(), format);
		Map<String, String> map=new HashMap<String, String>();
		map.put("src_code", srcCode);
		map.put("out_trade_no", orderNo);
		map.put("total_fee", ParamUtils
				.BigFormatIntString(new BigDecimal(orderPrice)));
		map.put("time_start", tradeDate);
		map.put("goods_name", "chongzhi" );
		map.put("mchid", mchid);
		map.put("trade_type", payType);
		map.put("finish_url", pageUrl);
		
		String sign = sign(map);
		map.put("sign", sign);
		String result = HttpRequest.sendPost(url, map);

		log.error("_____AmxmyPay result::" + result);
		JSONObject obj;
		String code = "201";
		String msg = "";
		BxsPayBean bean = new BxsPayBean();
		if (ParamUtils.chkString(result) && !result.equals("null")) {
			try {
				obj = new JSONObject(result);
				code = obj.getString("respcd");
				msg = obj.getString("respmsg");
				JSONObject data=obj.getJSONObject("data");
				if (code.equals("0000")) {// 成功
					String payUrl =data.getString("pay_params");
					bean.setUrl("http://pan.baidu.com/share/qrcode?w=150&h=150&url="+payUrl);
					code = "200";
				} else {
					code = "201";
				}
			} catch (JSONException e) {
				System.out.println(e);
			}
		}
		log.error("____________url::"+bean.getUrl());
		bean.setCode(code);
		bean.setMsg(msg);
		return bean;
	}

}
