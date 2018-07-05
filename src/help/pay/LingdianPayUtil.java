package help.pay;

import help.pay.lingdian.Config.MerConfig;
import help.pay.lingdian.Utils.SignUtil;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
public class LingdianPayUtil {

	private static final String TxCode = "210110";// 210110-机构统一下单

	/**
	 * 支付方法 dto.payGateway 支付方式 1.支付宝5.微信
	 * 
	 * @return
	 */
	public static BxsPayBean payOrder(PayDto dto) {
		String productId = "";
		String payGateway = dto.getPayGateway();
		if (payGateway.equals("1")) {// 支付宝
			productId = "0602";
		} else if (payGateway.equals("5")) {// 微信
			productId = "0601";
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("Version", "2.0"); // 版本号
		map.put("SignMethod", MerConfig.SIGNMETHOD); // 签名类型
		map.put("TxCode", TxCode); // 交易编码
		map.put("MerNo", MerConfig.MERNO); // 商户号-测试环境统一商户号
		map.put("ProductId", productId);// 产品类型:0602-支付宝扫码

		map.put("TxSN", dto.getOrderNo());// 商户交易流水号(唯一)
		String orderPrice = dto.getOrderPrice();
		orderPrice = ParamUtils.BigFormatIntString(new BigDecimal(orderPrice));
		
		map.put("Amount", orderPrice);// 金额:单位:分
		map.put("PdtName", dto.getTitle());// 商品名称
		map.put("Remark", "b");// 备注
		map.put("StoreId", "01");// 门店号
		map.put("TerminalId", "001");// 终端号
		map.put("ReturnUrl", dto.getPageUrl());// 同步通知URL
		map.put("NotifyUrl", dto.getCallback());// 异步通知URL
		// 设置签名
		MerConfig.setSignature(map);
		// 报文明文
		String plain = SignUtil.getURLParam(map,
				MerConfig.URL_PARAM_CONNECT_FLAG, true, null);
		// 测试地址
		String result = MerConfig.sendMsg(MerConfig.REQURL, map);
		BxsPayBean bean = new BxsPayBean();
		if (null == result) {
//			System.out.println("报文发送失败或应答消息为空");
		} else {
			Map<String, String> resMap;
			try {
				resMap = SignUtil.parseResponse(result,
						MerConfig.base64Keys, MerConfig.URL_PARAM_CONNECT_FLAG,
						MerConfig.CHARSET);
//				System.out.println("URLDecoder处理后返回数据:"
//						+ SignUtil.getURLParam(resMap,
//								MerConfig.URL_PARAM_CONNECT_FLAG, true, null));
				if (MerConfig.verifySign(resMap)) {
//					System.out.println("签名验证结果成功");
					if ("00000".equalsIgnoreCase(resMap.get("RspCod"))
							|| "1".equalsIgnoreCase(resMap.get("Status"))) {
						// 请求成功进行处理
						String ImgUrl = resMap.get("ImgUrl");
						String CodeUrl = resMap.get("CodeUrl");
						System.out.println("二维码图片地址:" + ImgUrl + "_____CodeUrl::"
								+ CodeUrl);

						bean.setCode("200");
						bean.setMsg("");
						bean.setUrl(CodeUrl);
						bean.setQrUrl(ImgUrl);
					} else {
						String msg = resMap.get("RspMsg");
						bean.setCode("201");
						bean.setMsg(msg);
					}
				} else {
					System.out.println("签名验证结果失败");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bean;
	}

}
