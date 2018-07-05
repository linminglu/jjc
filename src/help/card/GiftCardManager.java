package help.card;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.apps.Constants;
import com.apps.model.Address;
import com.apps.model.dto.OrderDTO;
import com.apps.pay.unionpay.Form_6_2_AppConsume;
import com.apps.pay.wechat.ClientCustomSSL;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.card.CardConstants;
import com.card.model.CardItem;
import com.card.model.CardItemOrder;
import com.card.model.dto.CardDTO;
import com.card.service.ICardService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.HQUtils;
import com.framework.util.ManageFile;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.util.xml.XmlUtil;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.model.User;
import com.ram.service.user.IUserService;

/**
 * 礼品卡处理类
 * 列表，订单接品放在一起
 * @author cuisy
 */
public class GiftCardManager extends BaseDispatchAction {
	private final IUserService userService = (IUserService) ServiceLocatorImpl.getInstance().getService("userService");
	private final ICardService cardService = (ICardService) ServiceLocatorImpl.getInstance().getService("cardService");

	/**
	 * 充值卡套餐列表
	 */
	public void list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		
		if(!ParamUtils.chkString(message)) {
			//查询所有套餐
			List<Object> list = cardService.findObjects(new HQUtils("from CardItem ci where 1=1 and ci.status = '1' order by ci.itemId"));
			JSONArray items = new JSONArray();//数据集合
			JSONObject jo = null;
			for(Object obj:list){
				CardItem item = (CardItem)obj;
				jo = new JSONObject();
				jo.put("gid", item.getItemId());
				jo.put("title", item.getTitle());
				jo.put("parValue", ProductUtil.BigFormatJud(item.getParValue()));
				jo.put("price", ProductUtil.BigFormatJud(item.getPrice()));//售价
				jo.put("img", Constants.getDomainName()+item.getImgUrl());
				items.put(jo);
			}
			data.put("items", items);
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 我的礼品卡
	 */
	public void myList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", RamConstants.MAXPAGEITEMS);
		String u = ParamUtils.getParameter(deParameter, "u", "");
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		
		if(!ParamUtils.chkString(message)) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			User user = userService.getUser(uid);
			if(user!=null){
				//查询所有套餐
				HQUtils hq = new HQUtils();
				hq.setSelect("select new com.card.model.dto.CardDTO(ci,cio) ");
				hq.addHsql("from CardItem ci,CardItemOrder cio where ci.itemId=cio.itemId and cio.userId=? ");
				hq.addPars(uid);
				hq.setOrderby("order by cio.orderId desc");
				hq.setPageSize(pageSize);
				hq.setStartIndex(pageIndex*pageSize);
				
				PaginationSupport ps = cardService.findObjectPage(hq);
				
				@SuppressWarnings("unchecked")
				List<Object> list = ps.getItems();
				Integer pageNum = list.size();
				JSONArray items = new JSONArray();//数据集合
				JSONObject jo = null;
				for(Object obj:list){
					CardDTO dto = (CardDTO)obj;
					CardItem item = dto.getCardItem();
					CardItemOrder order = dto.getCardItemOrder();
					jo = new JSONObject();
					jo.put("oid", order.getOrderId());
					jo.put("title", item.getTitle());
					jo.put("orderNum", order.getOrderNum());
					jo.put("totalPrice", ProductUtil.BigFormatJud(order.getTotalMoney()));
					jo.put("num", order.getNum());
					jo.put("status", order.getPayStatus());
					jo.put("img", Constants.getDomainName()+item.getImgUrl());
					items.put(jo);
				}
				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
				
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		
		//map = new JSONObject(ManageFile.loadTextFileGBK(Constants.getWebRootPath()+"/card/giftCard_myList.js"));
		
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 订单详情 
	 */
	public void orderView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u", "");
		int oid = ParamUtils.getIntParameter(deParameter, "oid", 0);
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		
		if (!ParamUtils.chkString(u) || !ParamUtils.chkInteger(oid)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			User user = userService.getUser(uid);
			if(user != null) {
				HQUtils hq = new HQUtils();
				hq.setSelect("select new com.card.model.dto.CardDTO(ci,cio) ");
				hq.addHsql("from CardItem ci,CardItemOrder cio where ci.itemId=cio.itemId and cio.userId=? and cio.orderId=? ");
				hq.addPars(uid);
				hq.addPars(oid);
				Object obj = cardService.getObject(hq);
				if(obj!=null){
					CardDTO dto = (CardDTO)obj;
					CardItem item = dto.getCardItem();
					CardItemOrder order = dto.getCardItemOrder();
					JSONObject jo = new JSONObject();
					jo.put("oid", order.getOrderId());
					jo.put("title", item.getTitle());
					jo.put("orderNum", order.getOrderNum());
					jo.put("totalPrice", ProductUtil.BigFormatJud(order.getTotalMoney()));
					jo.put("price", ProductUtil.BigFormatJud(item.getPrice()));
					jo.put("num", order.getNum());
					jo.put("status", order.getPayStatus());
					jo.put("createDate", DateTimeUtil.DateToStringHHMM(order.getCreateTime()));
					jo.put("userName", ParamUtils.chkString(order.getUserName())?order.getUserName():"");
					jo.put("cellPhone", ParamUtils.chkString(order.getCellPhone())?order.getCellPhone():"");
					jo.put("address", ParamUtils.chkString(order.getAddress())?order.getAddress():"");
					data.put("obj", jo);
				}
				
				//data = new JSONObject(ManageFile.loadTextFileGBK(Constants.getWebRootPath()+"/card/giftCard_orderView.js"));
				
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
		
	}
	
	/**
	 * 提交订单并支付
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		int gid = ParamUtils.getIntParameter(deParameter, "gid", 0);// 套餐id
		int aid = ParamUtils.getIntParameter(deParameter, "aid", 0);//地址信息
		int num = ParamUtils.getIntParameter(deParameter, "num", 0);// 商品数量

		String payType = ParamUtils.getParameter(deParameter, "payType", "");// 在线支付方式1.支付宝2.银联5.微信
		String u = ParamUtils.getParameter(deParameter, "u", "");//

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		if (!ParamUtils.chkInteger(gid) || !ParamUtils.chkString(u) || !ParamUtils.chkString(payType) || !ParamUtils.chkInteger(num)
				|| !ParamUtils.chkInteger(aid)
		){
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		Integer uid = 0;
		BigDecimal totalPrice = new BigDecimal(0);// 总价格
		
		CardItemOrder order = new CardItemOrder();
		CardItem product = (CardItem) cardService.getObject(CardItem.class, gid);//套餐
		
		// 收货信息
		Address address = (Address) cardService.getObject(Address.class, aid);
		if(address==null){
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		
		// 判断1
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			uid = Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			BigDecimal price = product.getPrice();
			totalPrice = price.multiply(new BigDecimal(num));//总价格
		
			order.setUserId(uid);
			order.setPayType(payType);// 在线支付方式
			order.setTotalMoney(totalPrice);
			
			order.setItemId(gid);
			order.setNum(num);
			order.setOrderNum(ProductUtil.createOrderNum(Constants.MODULE_GIFT_CARD));
			order.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
			order.setPayStatus(CardConstants.ORDER_PAY_NOT);
			
			order.setUserName(address.getUserName());
			order.setCellPhone(address.getCellPhone());
			order.setAddress(address.getAddress());
			
			cardService.saveObject(order,null);
			
			// 普通订单信息
			JSONObject obj = new JSONObject();
			obj.put("title", product.getTitle());
			Integer oid = order.getOrderId();
			String key = Constants.DES_KEY_OID + "=" + oid;

			obj.put("key", DesUtils.encrypt(key));
			obj.put("oid", order.getOrderId());
			obj.put("totalPrice",ProductUtil.BigFormatJud(order.getTotalMoney()));
			data.put("obj", obj);

			if (payType.equals(APIConstants.PAY_TYPE_ALIPAY)) {
				// 支付宝信息
				JSONObject alipayObj = new JSONObject();
				alipayObj.put("partner", Constants.getAlipayPartner());
				alipayObj.put("rsa_private", Constants.getAlipayRsaPrivate());
				alipayObj.put("_input_charset", Constants.PAY_ALIPAY_CHARSET);
				alipayObj.put("notify_url", Constants.getAlipayCallBackGiftCard());
				alipayObj.put("out_trade_no", order.getOrderNum());
				alipayObj.put("subject", product.getTitle());
				alipayObj
						.put("payment_type", Constants.PAY_ALIPAY_PAYMENT_TYPE);
				alipayObj.put("seller_id", Constants.getAlipaySellerId());
				alipayObj.put("total_fee",ProductUtil.BigFormatJud(order.getTotalMoney()));
				alipayObj.put("body", product.getTitle());

				data.put("alipayObj", alipayObj);
			} else if (payType.equals(APIConstants.PAY_TYPE_UNIONPAY)) {// 银联
				OrderDTO dto = new OrderDTO();
				dto.setCallback(Constants.getUnionCallBackGiftCard());
				dto.setOrderNum(order.getOrderNum());
				dto.setTotalPrice(order.getTotalMoney());
				Map<String, String> resmap = Form_6_2_AppConsume.resmap(dto);
				JSONObject unionpayObj = new JSONObject();
				unionpayObj.put("respCode", resmap.get("respCode"));// 银联生成交易流水号的返回码
				String tn = resmap.get("tn");
				unionpayObj.put("tn", tn);// 银联生成的流水号
				unionpayObj.put("orderId", resmap.get("orderId"));// 银联生成的订单编号
				unionpayObj.put("txnTime", resmap.get("txnTime"));// 银联生成的交易时间

//				order.setTradeNo(tn);
//				orderService.saveObject(order, null);
				data.put("unionpayObj", unionpayObj);
			} else if (payType.equals(APIConstants.PAY_TYPE_WECHAT)) {// 微信
				JSONObject weixinObj = new JSONObject();
				HashMap<String, String> resmap = ClientCustomSSL.payOrder(
						order.getOrderNum(), product.getTitle(),
						ParamUtils.BigFormatIntString(order.getTotalMoney()),
						Constants.getWeChatCallBackGiftCard());
				weixinObj.put("prepayid", resmap.get("prepayid"));// 生成交易流水号的返回码
				weixinObj.put("appid", resmap.get("appid"));// 商户app号
				weixinObj.put("noncestr", resmap.get("noncestr"));// 随机安全码
				weixinObj.put("package", resmap.get("package"));// 包名
				weixinObj.put("partnerid", resmap.get("partnerid"));// 商户使用ID
				weixinObj.put("timestamp", resmap.get("timestamp"));// 时间戳
				weixinObj.put("sign", resmap.get("sign"));// 参数签名
				data.put("weChatObj", weixinObj);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}

		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * app同步回调
	 */
	public void pay(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String type = ParamUtils.getParameter(request, "type"); // 1.支付宝 2.银联 // 5.微信
		String u = ParamUtils.getParameter(request, "u");
		String key = ParamUtils.getParameter(request, "key");
		String status = ParamUtils.getParameter(request, "status");// 1.成功 0.失败

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		if (!ParamUtils.chkString(u) || !ParamUtils.chkString(type)
				|| !ParamUtils.chkString(key) || !ParamUtils.chkString(status)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("") && status.equals(Constants.PUB_STATUS_OPEN)) {// 支付成功
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			User user = userService.getUser(uid);
			if (user != null) {

				String oid = DesUtils.decrypt(key);// 解析订单id，格式：oid=xxx add by cuisy
				if (ParamUtils.chkString(oid) && oid.indexOf("=")>-1) oid = oid.split("=")[1];

				CardItemOrder order = (CardItemOrder) cardService.getObject(CardItemOrder.class, Integer.valueOf(oid));
				
				if (order != null){
					Integer userId = order.getUserId();
					if (uid.intValue() == userId.intValue()) {// 是否是登录用户的订单
						if(order.getPayStatus().equals(CardConstants.ORDER_PAY_NOT)){
							order.setPayType(type);
							order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
							order.setPayTime(DateTimeUtil.getJavaUtilDateNow());
							cardService.saveObject(order,user);
						}
						code = APIConstants.CODE_REQUEST_SUCCESS;
					} else {
						code = APIConstants.CODE_NOT_FOUND;
						message = APIConstants.TIPS_NOT_DATA;
					}
				} else {
					code = APIConstants.CODE_NOT_FOUND;
					message = APIConstants.TIPS_NOT_DATA;
				}
			} else {
				code = APIConstants.CODE_NOT_FOUND;
				message = APIConstants.TIPS_NOT_DATA;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
		
	}
	
	/**
	 * 异步回调 - 支付宝
	 */
	public void callback(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String tradeNo = ParamUtils.getParameter(request, "trade_no"); // 交易号
		String status = ParamUtils.getParameter(request, "trade_status"); // 交易状态
																			// TRADE_SUCCESS
		String payDate = ParamUtils.getParameter(request, "gmt_payment"); // 付款时间

		// 此处的oid有两种方式，如果是合并订单则返回的是OrderMerge的 id
		// 如果是单独订单支付的话，返回的是 Order 的orderNum 订单号
		String orderNum = ParamUtils.getParameter(request, "out_trade_no");

		String message = "";
		System.out.println("____[recharge_callback][status="+status+"][tradeNo="+tradeNo+"][orderNum="+orderNum+"]");
		
		if (!ParamUtils.chkString(orderNum) || !ParamUtils.chkString(tradeNo)
				|| !ParamUtils.chkString(status)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {// 支付成功
			if (status.equals("TRADE_SUCCESS")
					|| status.equals("TRADE_FINISHED")) {
				CardItemOrder order = cardService.getGiftCardOrderByOrderNum(orderNum);
				if (status.equals("TRADE_SUCCESS") && order.getPayStatus().equals(CardConstants.ORDER_PAY_NOT)) {// 支付成功
					order.setPayType(APIConstants.PAY_TYPE_ALIPAY);
					order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
					order.setTradeNo(tradeNo);
					order.setPayTime(DateTimeUtil.parse(payDate));
				}
				cardService.saveObject(order,null);
				message = "success";// 通知支付宝回调成功
			}
		}
		JsonUtil.AjaxWriter(response, message);
	}

	/**
	 * 微信异步回调
	 */
	public void callbackWechat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(request.getInputStream());

		String returnCode = XmlUtil.getNodeTextValue(XmlUtil.selectNode(doc,
				"//return_code"));// 返回值
		String tradeNo = XmlUtil.getNodeTextValue(XmlUtil.selectNode(doc,
				"//transaction_id"));// 交易号
		String orderNum = XmlUtil.getNodeTextValue(XmlUtil.selectNode(doc,
				"//out_trade_no"));// 订单号

		log.fatal("___[异步回调 ____weixin][tradeNo=" + tradeNo + "][returnCode="
				+ returnCode + "][orderNum=" + orderNum + "]");
		String message = "";
		if (ParamUtils.chkString(tradeNo) && ParamUtils.chkString(returnCode)
				&& ParamUtils.chkString(orderNum)) {
			if (returnCode.equals("SUCCESS")) {// 支付成功
				// 订单业务保存 【注意】异步回调会多次调用，要防止订单被重复处理
				CardItemOrder order = cardService.getGiftCardOrderByOrderNum(orderNum);
				String status = order.getPayStatus();
				if (status.equals(CardConstants.ORDER_PAY_NOT)) {
					order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
					order.setPayType(APIConstants.PAY_TYPE_WECHAT);
					order.setTradeNo(tradeNo);
					order.setPayTime(DateTimeUtil.getJavaUtilDateNow());
					cardService.saveObject(order,null);
				}
				// 返回给微信xml数据
				message = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
			} else {
				message = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[NOT OK]]></return_msg></xml>";
			}
		} else {
			message = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[ERROR]]></return_msg></xml>";
		}

		JsonUtil.AjaxWriter(response, message);
	}

	/**
	 * 微信异步回调
	 */
	public void callbackUnion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String orderId = ParamUtils.getParameter(request, "orderId");// 商户订单号
		String txnTime = ParamUtils.getParameter(request, "txnTime");// 交易时间
		String respCode = ParamUtils.getParameter(request, "respCode");// 相应代码
		String message = "";
		if (ParamUtils.chkString(orderId) && ParamUtils.chkString(respCode)
				&& ParamUtils.chkString(txnTime)) {
			if (respCode.equals("00")) {// 交易成功
				CardItemOrder order = cardService.getGiftCardOrderByOrderNum(orderId);
				String status = order.getPayStatus();

				order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
				if (status.equals(CardConstants.ORDER_PAY_NOT)) {
					order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
					order.setPayType(APIConstants.PAY_TYPE_UNIONPAY);
					order.setPayTime(new Date());
					cardService.saveObject(order,null);
				}
				message = "success";
			} else {
			}
		} else {
		}

		JsonUtil.AjaxWriter(response, message);
	}
}