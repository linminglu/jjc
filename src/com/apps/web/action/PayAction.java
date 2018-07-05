package com.apps.web.action;

import help.base.APIConstants;
import help.pay.AmxmyPayUtil;
import help.pay.BxsPayBean;
import help.pay.LingdianPayUtil;
import help.pay.PayDto;
import help.pay.QYFPayUtil;
import help.pay.RenXinPayUtil;
import help.pay.ShanFuPayUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.apps.util.Util;
import com.card.CardConstants;
import com.card.model.CardRechargeOrder;
import com.card.service.ICardService;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.ram.exception.permission.NoFunctionPermissionException;

/**
 * 支付
 * 
 * @author Mr.zang
 */
public class PayAction extends BaseDispatchAction {
	private final ICardService cardService = (ICardService) ServiceLocatorImpl
			.getInstance().getService("cardService");

	/**
	 * 列表
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String orderNum = ParamUtils.getParameter(request, "a");// 订单号
		String t = ParamUtils.getParameter(request, "t");// 支付类型 同Constants.DEF_PAY_COMPANY
		String g = "";
		CardRechargeOrder oreder = cardService.getRechargeOrderByOrderNum(orderNum);
		String QRcodeURL = "";
		String code = "201";
		String msg = "";
		String p="";
		if (oreder != null) {
			g = oreder.getPayType();
			QRcodeURL = oreder.getQRcodeURL();
			String status = oreder.getPayStatus();
			BigDecimal payPrice = oreder.getPayPrice();
			
			p=ProductUtil.BigFormat(payPrice);
			if (CardConstants.ORDER_PAY_NOT.equals(status)) {
				if (!ParamUtils.chkString(QRcodeURL)) {// 如果没有，证明这个是新订单
					// 这个地方就是对接第三方的支付工具
					// 需要走支付
					BxsPayBean payBean =new BxsPayBean();
					if(Constants.DEF_PAY_COMPANY.equals("1")){
						PayDto dto = new PayDto();
						dto.setCallback(StringUtil
								.checkAPIHttpUrl("/api/recharge_callbackRenXin"));
						dto.setOrderNo(orderNum);
						dto.setOrderPrice(ProductUtil.BigFormat(payPrice));
						dto.setPayGateway(g);
						payBean = RenXinPayUtil.payOrder(dto);
					}else if(Constants.DEF_PAY_COMPANY.equals("3")){
						PayDto dto = new PayDto();
						dto.setPageUrl(StringUtil
								.checkAPIHttpUrl("/pay/amxmyPayResult.jsp?a="+orderNum));
						dto.setCallback(StringUtil
								.checkAPIHttpUrl("/api/recharge_callbackAmxmy"));
						dto.setOrderNo(orderNum);
						dto.setOrderPrice(ProductUtil.BigFormat(payPrice));
						dto.setPayGateway(g);
						payBean =AmxmyPayUtil.payOrder(dto);
					}else if(Constants.DEF_PAY_COMPANY.equals("5")){//轻易付
						PayDto dto = new PayDto();
						dto.setPageUrl(StringUtil
								.checkAPIHttpUrl("/user/finance/"));
						dto.setCallback(StringUtil
								.checkAPIHttpUrl("/api/recharge_callbackQYF"));
						dto.setOrderNo(orderNum);
						dto.setOrderPrice(ProductUtil.BigFormat(payPrice));
						dto.setPayGateway(g);
						dto.setTitle("充值-"+ProductUtil.BigFormatJud(payPrice));
						
						payBean =QYFPayUtil.payOrder(dto);
					}else if(Constants.DEF_PAY_COMPANY.equals("6")){//零点支付
						
						PayDto dto = new PayDto();
						dto.setPageUrl(StringUtil
								.checkAPIHttpUrl("/user/finance/"));
						dto.setCallback(StringUtil
								.checkAPIHttpUrl("/api/recharge_callbackLingdian"));
						dto.setOrderNo(orderNum);
						dto.setOrderPrice(ProductUtil.BigFormat(payPrice));
						dto.setPayGateway(g);
						dto.setTitle("CZ-"+ProductUtil.BigFormatJud(payPrice));
						
						payBean =LingdianPayUtil.payOrder(dto);
						
					}
					QRcodeURL = payBean.getQrUrl();
					code = payBean.getCode();
					msg = payBean.getMsg();
					oreder.setQRcodeURL(StringUtil.formatImgHttpUrlToRelative(QRcodeURL));
					cardService.saveObject(oreder, null);
				}
			}
		}
		String payurl = "";
		if(Constants.DEF_PAY_COMPANY.equals("1")){
			if (ParamUtils.chkString(QRcodeURL)) {
				Map<String, String> map = ParamUtils.toMap(QRcodeURL);
				if (map != null) {
					payurl = map.get("data");
					payurl = URLDecoder.decode(payurl, "UTF-8");
				}
			}
		}
		request.setAttribute("QRcodeURL",
				ParamUtils.chkStringNotNull(QRcodeURL));// 支付需要的二维码图片地址
		request.setAttribute("payurl", ParamUtils.chkStringNotNull(payurl));// 支付地址
		
		request.setAttribute("p",p );// 支付钱
		request.setAttribute("a", orderNum);// 订单号
		request.setAttribute("s", "2");// 支付场景1.pc2.app
		request.setAttribute("g", g);// 支付类型5.微信1.支付宝
		request.setAttribute("t", "180");// 倒计时秒
		request.setAttribute("code", code);//
		request.setAttribute("msg", msg);//
		return mapping.findForward("init");
	}

	
	
	
	/**
	 * 获得支付状态
	 */
	public void getStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String orderNum = ParamUtils.getParameter(request, "a");// 订单号
		String message = "";
		if (!ParamUtils.chkString(orderNum)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			CardRechargeOrder oreder = cardService
					.getRechargeOrderByOrderNum(orderNum);
			if (oreder != null) {
				String payStatus = oreder.getPayStatus();// 1.待支付 2.支付成功
				data.put("status", payStatus);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 失败
	 */
	public ActionForward fail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		return mapping.findForward("fail");
	}

	/**
	 * 成功
	 */
	public ActionForward success(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		return mapping.findForward("success");
	}

	/**
	 * 闪付初始化
	 */
	public ActionForward shanfuInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String orderNum = ParamUtils.getParameter(request, "a");// 订单号
		String g = "";
		CardRechargeOrder oreder = cardService
				.getRechargeOrderByOrderNum(orderNum);
		String QRcodeURL = "";
		String code = "201";
		String msg = "";
		if (oreder != null) {
			g = oreder.getPayType();
			QRcodeURL = oreder.getQRcodeURL();
			String status = oreder.getPayStatus();
			BigDecimal payPrice = oreder.getPayPrice();

			if (CardConstants.ORDER_PAY_NOT.equals(status)) {
				PayDto dto = new PayDto();
				dto.setCallback(StringUtil
						.checkAPIHttpUrl("/api/recharge_callbackShanfu"));
				dto.setOrderNo(orderNum);
				dto.setOrderPrice(ProductUtil.BigFormat(payPrice));
				dto.setPayGateway(g);

				String orderNo = dto.getOrderNo();// 订单号
				String orderPrice = dto.getOrderPrice();// 支付金额
				String payGateway = dto.getPayGateway();// 支付方式
				String callback = dto.getCallback();
				String pageback = StringUtil
				.checkAPIHttpUrl("/pay/shanfuResult.jsp");
				String payType = "57";// 微信
				if (APIConstants.PAY_TYPE_ALIPAY.equals(payGateway)) {
					payType = "758";
				} else if (APIConstants.PAY_TYPE_WECHAT.equals(payGateway)) {
					payType = "57";
				}

				String format = "yyyyMMddhhmmss";
				String tradeDate = DateTimeUtil
						.dateToString(new Date(), format);
				String url = "http://gw.sfvipgate.com/v4.aspx";
				String signParam = "{MERCHANTID}|{PAYID}|{TRADEDATE}|{TRANSID}|{ORDERMONEY}|{PAGEURL}|{RETURNURL}|{NOTICETYPE}|{SECRETKEY}";
				signParam = signParam
						.replace("{MERCHANTID}", ShanFuPayUtil.partner)
						.replace("{PAYID}", payType)
						.replace("{TRADEDATE}", tradeDate)
						.replace("{TRANSID}", orderNo)
						.replace(
								"{ORDERMONEY}",
								ParamUtils.BigFormatIntString(new BigDecimal(
										orderPrice)))
						.replace("{PAGEURL}", pageback)
						.replace("{RETURNURL}", callback)
						.replace("{NOTICETYPE}", "1")
						.replace("{SECRETKEY}", ShanFuPayUtil.secretKey);
				String sign = "";
				try {
					sign = Util.encryption(signParam);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
//				String param = "MemberID={MEMBERID}&TerminalID={TERMINALID}&InterfaceVersion={INTERFACEVERSION}&KeyType=1&PayID={PAYID}&TradeDate={TRADEDATE}&TransID={TRANSID}&OrderMoney={ORDERMONEY}&NoticeType=1&PageUrl={PAGEURL}&ReturnUrl={RETURNURL}&Md5Sign={SIGN}";
//				param = param
//						.replace("{MEMBERID}", ShanFuPayUtil.partner)
//						.replace("{TERMINALID}", ShanFuPayUtil.terminalId)
//						.replace("{INTERFACEVERSION}", ShanFuPayUtil.version)
//						.replace("{PAYID}", payType)
//						.replace("{TRADEDATE}", tradeDate)
//						.replace("{TRANSID}", orderNo)
//						.replace(
//								"{ORDERMONEY}",
//								ParamUtils.BigFormatIntString(new BigDecimal(
//										orderPrice)))
//						.replace("{PAGEURL}", callback)
//						.replace("{RETURNURL}", callback)
//						.replace("{SIGN}", sign);

				request.setAttribute("MemberID", ShanFuPayUtil.partner);// 商户 ID
				request.setAttribute("TerminalID", ShanFuPayUtil.terminalId);//终端ID
				request.setAttribute("InterfaceVersion", ShanFuPayUtil.version);// 接口版本
				request.setAttribute("KeyType", "1");// 加密类型
				request.setAttribute("PayID", payType);// 
				request.setAttribute("TradeDate", tradeDate);//订单日期
				request.setAttribute("TransID", orderNo);//订单号
				request.setAttribute("OrderMoney",ParamUtils.BigFormatIntString(new BigDecimal(
						orderPrice)));// 订单金额
				request.setAttribute("NoticeType", "1");//订单号
				request.setAttribute("PageUrl", pageback);//同步回调页面
				request.setAttribute("ReturnUrl", callback);//callback
				request.setAttribute("Md5Sign", sign);//sign
				code="200";
			}
		}
		request.setAttribute("code", code);// 
		request.setAttribute("msg", "");// 商户 ID
		return mapping.findForward("shanfuInit");
	}
	
	public ActionForward gtPay(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String orderNum = ParamUtils.getParameter(request, "a");// 订单号
		String t = ParamUtils.getParameter(request, "t");// 支付类型 同Constants.DEF_PAY_COMPANY
		String g = "";
		CardRechargeOrder oreder = cardService.getRechargeOrderByOrderNum(orderNum);
		String QRcodeURL = "";
		String code = "201";
		if (oreder != null) {
			g = oreder.getPayType();
			String status = oreder.getPayStatus();
			BigDecimal payPrice = oreder.getPayPrice();
			String orderPrice=ProductUtil.BigFormat(payPrice);
			if (CardConstants.ORDER_PAY_NOT.equals(status)) {
				String threepayType = oreder.getThreepayType();
				request.setAttribute("payGateway", g);//
				request.setAttribute("orderNo", orderNum);//
				request.setAttribute("callback", StringUtil.checkAPIHttpUrl("/api/recharge_callbackGT"));//
				request.setAttribute("pageUrl", StringUtil.checkAPIHttpUrl(""));//
				request.setAttribute("orderPrice", orderPrice);//
				request.setAttribute("threepayType", threepayType);//
			}
		}
		return mapping.findForward("gtPay");
	}
}
