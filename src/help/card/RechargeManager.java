package help.card;

import help.base.APIConstants;
import help.pay.AmxmyPayUtil;
import help.pay.BxsPayBean;
import help.pay.BxsPayUtil;
import help.pay.LingdianPayUtil;
import help.pay.PayDto;
import help.pay.QYFPayUtil;
import help.pay.RenXinPayUtil;
import help.pay.ShanFuPayUtil;
import help.pay.lingdian.Config.MerConfig;
import help.pay.lingdian.Utils.SignUtil;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.Consts;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.model.Param;
import com.apps.model.UserTradeDetail;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.apps.util.Util;
import com.card.CardConstants;
import com.card.model.CardRechargeItem;
import com.card.model.CardRechargeOrder;
import com.card.model.RechargeWay;
import com.card.model.dto.CardDTO;
import com.card.service.ICardService;
import com.cash.model.SysBank;
import com.cash.service.ICashService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.util.xml.XmlUtil;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.model.User;
import com.ram.service.user.IUserService;

/**
 * 充值处理类 列表，订单接品放在一起
 * 
 * @author cuisy
 */
public class RechargeManager extends BaseDispatchAction {
	private final IUserService userService = (IUserService) ServiceLocatorImpl
			.getInstance().getService("userService");
	private final ICardService cardService = (ICardService) ServiceLocatorImpl
			.getInstance().getService("cardService");
	private final ICashService cashService = (ICashService) getService("cashService");
	private String paramsEmptyMsg;

	/**
	 * 充值卡套餐列表
	 */
//	public void list(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		JSONObject map = new JSONObject();// 最外层
//		JSONObject data = new JSONObject();// 返回数据层
//		String code = APIConstants.CODE_REQUEST_ERROR;
//		String message = "";
//
////		List<Param> ParamList = CacheUtil.getParam();
////		for (Param param : ParamList) {
////			String type = param.getType();
////			if (Constants.PARAM_DEF_PAY.equals(type)) {
////				Constants.DEF_PAY_COMPANY = param.getValue();
////			}
////		}
//
//		if (!ParamUtils.chkString(message)) {
//			// 查询所有充值套餐
//			List<Object> list = cardService
//					.findObjects(new HQUtils(
//							"from CardRechargeItem cri where 1=1 and cri.status = '1' order by cri.itemId"));
//			JSONArray items = new JSONArray();// 数据集合
//			JSONObject jo = null;
//			for (Object obj : list) {
//				CardRechargeItem item = (CardRechargeItem) obj;
//				jo = new JSONObject();
//				jo.put("rid", item.getItemId());
//				jo.put("parValue", ProductUtil.BigFormatJud(item.getParValue()));
//				jo.put("price", ProductUtil.BigFormatJud(item.getPrice()));// 售价
//				items.put(jo);
//			}
//			data.put("items", items);
//			code = APIConstants.CODE_REQUEST_SUCCESS;
//		}
//		data.put("pt", Constants.DEF_PAY_COMPANY);
//
//		map.put("code", code);
//		map.put("msg", message);
//		map.put("data", data);
//
//		// demo data add by cuisy
//		// map = new
//		// JSONObject(ManageFile.loadTextFileGBK(Constants.getWebRootPath()+"/card/recharge_list.js"));
//
//		JsonUtil.AjaxWriter(response, map);
//	}

	/**
	 * 充值卡套餐列表
	 */
	public void list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型//0.网页
		String u = ParamUtils.getParameter(deParameter, "u", "");
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";

		System.out.println(u);
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
				// 查询所有充值套餐
				List<Object> list = cardService.findObjects(new HQUtils(
						"from CardRechargeItem cri where 1=1 order by cri.price "));
				JSONArray items = new JSONArray();// 数据集合
				
				Integer passageWayId = 1;// 用户所属的充值通道
				HQUtils hq = new HQUtils("from RechargeWay rw where rw.parentId=? and rw.type=? and rw.status=? and rw.channelType=?");
				hq.addPars(passageWayId);
				hq.addPars(Constants.RECHARGE_WAY_2);
				hq.addPars(Constants.PUB_STATUS_OPEN);
				hq.addPars("1");// 渠道类型1.在线充值
				Object object = userService.getObject(hq);
				
				RechargeWay rw = (RechargeWay)object;
				HQUtils hq2 = new HQUtils("from RechargeWay rw where rw.parentId=? and rw.type=? and rw.status=?");
				hq2.addPars(rw.getId());
				hq2.addPars(Constants.RECHARGE_WAY_3);
				hq2.addPars(Constants.PUB_STATUS_OPEN);
				List<Object> rwList2 = userService.findObjects(hq2);
				if (rwList2 != null && rwList2.size() > 0) {
					JSONObject jObj = null;
					for (Object obj : rwList2) {
						RechargeWay rec = (RechargeWay)obj;
						jObj = new JSONObject();
						jObj.put("title", rec.getTitle());
						jObj.put("type", rec.getDes());
						jObj.put("img",
								StringUtil.checkAPIHttpUrl(rec.getImg()));
						JSONObject jObj2 = null;
						JSONArray arr = new JSONArray();
						for (Object temp : list) {
							CardRechargeItem item = (CardRechargeItem) temp;
							jObj2 = new JSONObject();
							jObj2 = new JSONObject();
							jObj2.put("rid", item.getItemId());
							jObj2.put("parValue",
									ProductUtil.BigFormatJud(item.getParValue()));
							BigDecimal price = item.getPrice();
							jObj2.put("price", ProductUtil.BigFormatJud(price));// 售价
							arr.put(jObj2);
						}
						jObj.put("moneyArr", arr);
						JSONArray bankArr = new JSONArray();// 数据集合
						JSONObject bank1 = new JSONObject();
						bank1.put("bankId", "ICBC");
						bank1.put("title", "中国工商银行");
						bankArr.put(bank1);
						JSONObject bank2 = new JSONObject();
						bank2.put("bankId", "ABC");
						bank2.put("title", "中国农业银行");
						bankArr.put(bank2);
						JSONObject bank3 = new JSONObject();
						bank3.put("bankId", "BOC");
						bank3.put("title", "中国银行");
						bankArr.put(bank3);
						JSONObject bank4 = new JSONObject();
						bank4.put("bankId", "CCB");
						bank4.put("title", "建设银行");
						bankArr.put(bank4);
						JSONObject bank5 = new JSONObject();
						bank5.put("bankId", "CMB");
						bank5.put("title", "招商银行");
						bankArr.put(bank5);
						JSONObject bank18 = new JSONObject();
						bank18.put("bankId", "BCCB");
						bank18.put("title", "北京银行");
						bankArr.put(bank18);
						JSONObject bank8 = new JSONObject();
						bank8.put("bankId", "BOCO");
						bank8.put("title", "交通银行");
						bankArr.put(bank8);
						JSONObject bank14 = new JSONObject();
						bank14.put("bankId", "CIB");
						bank14.put("title", "兴业银行");
						bankArr.put(bank14);
						JSONObject bank19 = new JSONObject();
						bank19.put("bankId", "NJCB");
						bank19.put("title", "南京银行");
						bankArr.put(bank19);
						JSONObject bank11 = new JSONObject();
						bank11.put("bankId", "CMBC");
						bank11.put("title", "民生银行");
						bankArr.put(bank11);
						JSONObject bank12 = new JSONObject();
						bank12.put("bankId", "CEB");
						bank12.put("title", "光大银行");
						bankArr.put(bank12);
						JSONObject bank17 = new JSONObject();
						bank17.put("bankId", "PINGANBANK");
						bank17.put("title", "平安银行");
						bankArr.put(bank17);
						JSONObject bank20 = new JSONObject();
						bank20.put("bankId", "CBHB");
						bank20.put("title", "渤海银行");
						bankArr.put(bank20);
						JSONObject bank21 = new JSONObject();
						bank21.put("bankId", "HKBEA");
						bank21.put("title", "东亚银行");
						bankArr.put(bank21);
						JSONObject bank22 = new JSONObject();
						bank22.put("bankId", "NBCB");
						bank22.put("title", "宁波银行");
						bankArr.put(bank22);
						JSONObject bank10 = new JSONObject();
						bank10.put("bankId", "CTTIC");
						bank10.put("title", "中信银行");
						bankArr.put(bank10);
						JSONObject bank7 = new JSONObject();
						bank7.put("bankId", "GDB");
						bank7.put("title", "广发银行");
						bankArr.put(bank7);
						JSONObject bank15 = new JSONObject();
						bank15.put("bankId", "SHB");
						bank15.put("title", "上海银行");
						bankArr.put(bank15);
						JSONObject bank6 = new JSONObject();
						bank6.put("bankId", "SPDB");
						bank6.put("title", "浦发银行");
						bankArr.put(bank6);
						JSONObject bank9 = new JSONObject();
						bank9.put("bankId", "PSBC");
						bank9.put("title", "中国邮政");
						bankArr.put(bank9);
						JSONObject bank13 = new JSONObject();
						bank13.put("bankId", "HXB");
						bank13.put("title", "华夏银行");
						bankArr.put(bank13);
						JSONObject bank23 = new JSONObject();
						bank23.put("bankId", "BJRCB");
						bank23.put("title", "北京农村商业银行");
						bankArr.put(bank23);
						JSONObject bank16 = new JSONObject();
						bank16.put("bankId", "SRCB");
						bank16.put("title", "上海农商");
						bankArr.put(bank16);
						JSONObject bank24 = new JSONObject();
						bank24.put("bankId", "SDB");
						bank24.put("title", "深圳发展银行");
						bankArr.put(bank24);
						JSONObject bank25 = new JSONObject();
						bank25.put("bankId", "CZB");
						bank25.put("title", "浙江稠州商业银行");
						bankArr.put(bank24);
						jObj.put("bankArr", bankArr);
						items.put(jObj);
					}
				}
				data.put("items", items);
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}
			
//			String gtUnionPay = "1";// 聚财银联
//			String gtAlipay = "1";//
//			String gtAlipayWap = "1";//
//			String gtWechat = "1";//
//			String gtWechatWap = "1";//
//			String gtQQ = "1";//
//			String gtQQWap = "1";//
//
//			String gtUnionPayQRcode = "1";// 银联扫码
//
//			List<Param> ParamList = CacheUtil.getParam();
//			for (Param param : ParamList) {
//				String type = param.getType();
//				if (type.equals("20")) {
//					gtAlipay = param.getValue();
//				} else if (type.equals("21")) {
//					gtWechat = param.getValue();
//				} else if (type.equals("22")) {
//					gtUnionPay = param.getValue();
//				} else if (type.equals("23")) {
//					gtQQ = param.getValue();
//				} else if (type.equals("24")) {
//					gtWechatWap = param.getValue();
//				} else if (type.equals("25")) {
//					gtAlipayWap = param.getValue();
//				} else if (type.equals("26")) {
//					gtQQWap = param.getValue();
//				} else if (type.equals("27")) {
//					gtUnionPayQRcode = param.getValue();
//				}
//			}
//
//			JSONObject obj27 = new JSONObject();
//			obj27.put("title", "银联扫码");
//			obj27.put("type", "UNIONPAY");
//			obj27.put("img",
//					StringUtil.checkAPIHttpUrl("/images/ico-pay-unionpay.png"));
//			JSONArray moneyArr27 = new JSONArray();// 数据集合
//			JSONObject jo27 = null;
//			for (Object temp : list) {
//				CardRechargeItem item = (CardRechargeItem) temp;
//				jo27 = new JSONObject();
//				jo27.put("rid", item.getItemId());
//				jo27.put("parValue",
//						ProductUtil.BigFormatJud(item.getParValue()));
//				BigDecimal price = item.getPrice();
//				jo27.put("price", ProductUtil.BigFormatJud(price));// 售价
//				// if (price.compareTo(new BigDecimal(3000)) == -1
//				// || price.compareTo(new BigDecimal(3000)) == 0) {
//				moneyArr27.put(jo27);
//				// }
//			}
//			JSONArray bankArr27 = new JSONArray();// 数据集合
//			obj27.put("bankArr", bankArr27);
//			obj27.put("moneyArr", moneyArr27);
//			if (gtUnionPayQRcode.equals("1")) {
//				items.put(obj27);
//			}
//
//			JSONObject obj26 = new JSONObject();
//			obj26.put("title", "QQ钱包wap");
//			obj26.put("type", "QQPAYWAP");
//			obj26.put("img",
//					StringUtil.checkAPIHttpUrl("/images/ico-pay-qqpay.png"));
//			JSONArray moneyArr26 = new JSONArray();// 数据集合
//			JSONObject jo26 = null;
//			for (Object temp : list) {
//				CardRechargeItem item = (CardRechargeItem) temp;
//				jo26 = new JSONObject();
//				jo26.put("rid", item.getItemId());
//				jo26.put("parValue",
//						ProductUtil.BigFormatJud(item.getParValue()));
//				BigDecimal price = item.getPrice();
//				jo26.put("price", ProductUtil.BigFormatJud(price));// 售价
//				// if (price.compareTo(new BigDecimal(3000)) == -1
//				// || price.compareTo(new BigDecimal(3000)) == 0) {
//				moneyArr26.put(jo26);
//				// }
//			}
//			JSONArray bankArr26 = new JSONArray();// 数据集合
//			obj26.put("bankArr", bankArr26);
//			obj26.put("moneyArr", moneyArr26);
//			if (gtQQWap.equals("1")) {
//				items.put(obj26);
//			}
//
//			JSONObject obj7 = new JSONObject();
//			obj7.put("title", "QQ钱包");
//			obj7.put("type", "QQPAY");
//			obj7.put("img",
//					StringUtil.checkAPIHttpUrl("/images/ico-pay-qqpay.png"));
//			JSONArray moneyArr7 = new JSONArray();// 数据集合
//			JSONObject jo7 = null;
//			for (Object temp : list) {
//				CardRechargeItem item = (CardRechargeItem) temp;
//				jo7 = new JSONObject();
//				jo7.put("rid", item.getItemId());
//				jo7.put("parValue",
//						ProductUtil.BigFormatJud(item.getParValue()));
//				BigDecimal price = item.getPrice();
//				jo7.put("price", ProductUtil.BigFormatJud(price));// 售价
//				// if (price.compareTo(new BigDecimal(3000)) == -1
//				// || price.compareTo(new BigDecimal(3000)) == 0) {
//				moneyArr7.put(jo7);
//				// }
//			}
//			JSONArray bankArr7 = new JSONArray();// 数据集合
//			obj7.put("bankArr", bankArr7);
//			obj7.put("moneyArr", moneyArr7);
//			if (gtQQ.equals("1")) {
//				items.put(obj7);
//			}
//
//			JSONObject obj = new JSONObject();
//			obj.put("title", "支付宝");
//			obj.put("type", "ALIPAY");
//			obj.put("img",
//					StringUtil.checkAPIHttpUrl("/images/ico-pay-alipay.png"));
//			JSONArray moneyArr = new JSONArray();// 数据集合
//			JSONObject jo = null;
//			for (Object temp : list) {
//				CardRechargeItem item = (CardRechargeItem) temp;
//				jo = new JSONObject();
//				jo.put("rid", item.getItemId());
//				jo.put("parValue", ProductUtil.BigFormatJud(item.getParValue()));
//				BigDecimal price = item.getPrice();
//				jo.put("price", ProductUtil.BigFormatJud(price));// 售价
//				// if (price.compareTo(new BigDecimal(3000)) == -1
//				// || price.compareTo(new BigDecimal(3000)) == 0) {
//				moneyArr.put(jo);
//				// }
//			}
//			JSONArray bankArr = new JSONArray();// 数据集合
//			obj.put("bankArr", bankArr);
//			obj.put("moneyArr", moneyArr);
//			if (gtAlipay.equals("1")) {
//				items.put(obj);
//			}
//
//			JSONObject obj9 = new JSONObject();
//			obj9.put("title", "支付宝wap");
//			obj9.put("type", "ALIPAYWAP");
//			obj9.put("img",
//					StringUtil.checkAPIHttpUrl("/images/ico-pay-alipay.png"));
//			JSONArray moneyArr9 = new JSONArray();// 数据集合
//			JSONObject jo9 = null;
//			for (Object temp : list) {
//				CardRechargeItem item = (CardRechargeItem) temp;
//				jo9 = new JSONObject();
//				jo9.put("rid", item.getItemId());
//				jo9.put("parValue",
//						ProductUtil.BigFormatJud(item.getParValue()));
//				BigDecimal price = item.getPrice();
//				jo9.put("price", ProductUtil.BigFormatJud(price));// 售价
//				// if (price.compareTo(new BigDecimal(3000)) == -1
//				// || price.compareTo(new BigDecimal(3000)) == 0) {
//				moneyArr9.put(jo9);
//				// }
//			}
//			JSONArray bankArr9 = new JSONArray();// 数据集合
//			obj9.put("bankArr", bankArr9);
//			obj9.put("moneyArr", moneyArr9);
//			if (gtAlipayWap.equals("1")) {
//				items.put(obj9);
//			}
//
//			JSONObject obj8 = new JSONObject();
//			obj8.put("title", "微信wap");
//			obj8.put("type", "WEIXINWAP");
//			obj8.put("img",
//					StringUtil.checkAPIHttpUrl("/images/ico-pay-wechat.png"));
//			JSONArray moneyArr8 = new JSONArray();// 数据集合
//			JSONObject jo8 = null;
//			for (Object temp : list) {
//				CardRechargeItem item = (CardRechargeItem) temp;
//				jo8 = new JSONObject();
//				jo8.put("rid", item.getItemId());
//				jo8.put("parValue",
//						ProductUtil.BigFormatJud(item.getParValue()));
//				BigDecimal price = item.getPrice();
//				jo8.put("price", ProductUtil.BigFormatJud(price));// 售价
//				if (price.compareTo(new BigDecimal(3000)) == -1
//						|| price.compareTo(new BigDecimal(3000)) == 0) {
//					moneyArr8.put(jo8);
//				}
//			}
//			JSONArray bankArr8 = new JSONArray();// 数据集合
//			obj8.put("bankArr", bankArr8);
//			obj8.put("moneyArr", moneyArr8);
//			if (gtWechatWap.equals("1")) {
//				items.put(obj8);
//			}
//
//			JSONObject obj2 = new JSONObject();
//			obj2.put("title", "微信");
//			obj2.put("type", "WEIXIN");
//			obj2.put("img",
//					StringUtil.checkAPIHttpUrl("/images/ico-pay-wechat.png"));
//			JSONArray moneyArr2 = new JSONArray();// 数据集合
//			JSONObject jo2 = null;
//			for (Object temp : list) {
//				CardRechargeItem item = (CardRechargeItem) temp;
//				jo2 = new JSONObject();
//				jo2.put("rid", item.getItemId());
//				jo2.put("parValue",
//						ProductUtil.BigFormatJud(item.getParValue()));
//				BigDecimal price = item.getPrice();
//				jo2.put("price", ProductUtil.BigFormatJud(price));// 售价
//				if (price.compareTo(new BigDecimal(3000)) == -1
//						|| price.compareTo(new BigDecimal(3000)) == 0) {
//					moneyArr2.put(jo2);
//				}
//			}
//			JSONArray bankArr2 = new JSONArray();// 数据集合
//			obj2.put("bankArr", bankArr2);
//			obj2.put("moneyArr", moneyArr2);
//			if (gtWechat.equals("1")) {
//				items.put(obj2);
//			}
//
//			JSONObject obj3 = new JSONObject();
//			obj3.put("title", "银联");
//			obj3.put("type", "bank");
//			obj3.put("img",
//					StringUtil.checkAPIHttpUrl("/images/ico-pay-unionpay.png"));
//			JSONArray moneyArr3 = new JSONArray();// 数据集合
//			JSONObject jo3 = null;
//			for (Object temp : list) {
//				CardRechargeItem item = (CardRechargeItem) temp;
//				jo3 = new JSONObject();
//				jo3.put("rid", item.getItemId());
//				jo3.put("parValue",
//						ProductUtil.BigFormatJud(item.getParValue()));
//				BigDecimal price = item.getPrice();
//				jo3.put("price", ProductUtil.BigFormatJud(price));// 售价
//				if (price.compareTo(new BigDecimal(3000)) == -1
//						|| price.compareTo(new BigDecimal(3000)) == 0) {
//					moneyArr3.put(jo3);
//				}
//			}
//			JSONArray bankArr3 = new JSONArray();// 数据集合
//			JSONObject bank1 = new JSONObject();
//			bank1.put("bankId", "ICBC");
//			bank1.put("title", "中国工商银行");
//			bankArr3.put(bank1);
//			JSONObject bank2 = new JSONObject();
//			bank2.put("bankId", "ABC");
//			bank2.put("title", "中国农业银行");
//			bankArr3.put(bank2);
//			JSONObject bank3 = new JSONObject();
//			bank3.put("bankId", "BOC");
//			bank3.put("title", "中国银行");
//			bankArr3.put(bank3);
//			JSONObject bank4 = new JSONObject();
//			bank4.put("bankId", "CCB");
//			bank4.put("title", "建设银行");
//			bankArr3.put(bank4);
//			JSONObject bank5 = new JSONObject();
//			bank5.put("bankId", "CMB");
//			bank5.put("title", "招商银行");
//			bankArr3.put(bank5);
//			JSONObject bank18 = new JSONObject();
//			bank18.put("bankId", "BCCB");
//			bank18.put("title", "北京银行");
//			bankArr3.put(bank18);
//			JSONObject bank8 = new JSONObject();
//			bank8.put("bankId", "BOCO");
//			bank8.put("title", "交通银行");
//			bankArr3.put(bank8);
//			JSONObject bank14 = new JSONObject();
//			bank14.put("bankId", "CIB");
//			bank14.put("title", "兴业银行");
//			bankArr3.put(bank14);
//			JSONObject bank19 = new JSONObject();
//			bank19.put("bankId", "NJCB");
//			bank19.put("title", "南京银行");
//			bankArr3.put(bank19);
//			JSONObject bank11 = new JSONObject();
//			bank11.put("bankId", "CMBC");
//			bank11.put("title", "民生银行");
//			bankArr3.put(bank11);
//			JSONObject bank12 = new JSONObject();
//			bank12.put("bankId", "CEB");
//			bank12.put("title", "光大银行");
//			bankArr3.put(bank12);
//			JSONObject bank17 = new JSONObject();
//			bank17.put("bankId", "PINGANBANK");
//			bank17.put("title", "平安银行");
//			bankArr3.put(bank17);
//			JSONObject bank20 = new JSONObject();
//			bank20.put("bankId", "CBHB");
//			bank20.put("title", "渤海银行");
//			bankArr3.put(bank20);
//			JSONObject bank21 = new JSONObject();
//			bank21.put("bankId", "HKBEA");
//			bank21.put("title", "东亚银行");
//			bankArr3.put(bank21);
//			JSONObject bank22 = new JSONObject();
//			bank22.put("bankId", "NBCB");
//			bank22.put("title", "宁波银行");
//			bankArr3.put(bank22);
//			JSONObject bank10 = new JSONObject();
//			bank10.put("bankId", "CTTIC");
//			bank10.put("title", "中信银行");
//			bankArr3.put(bank10);
//			JSONObject bank7 = new JSONObject();
//			bank7.put("bankId", "GDB");
//			bank7.put("title", "广发银行");
//			bankArr3.put(bank7);
//			JSONObject bank15 = new JSONObject();
//			bank15.put("bankId", "SHB");
//			bank15.put("title", "上海银行");
//			bankArr3.put(bank15);
//			JSONObject bank6 = new JSONObject();
//			bank6.put("bankId", "SPDB");
//			bank6.put("title", "浦发银行");
//			bankArr3.put(bank6);
//			JSONObject bank9 = new JSONObject();
//			bank9.put("bankId", "PSBC");
//			bank9.put("title", "中国邮政");
//			bankArr3.put(bank9);
//			JSONObject bank13 = new JSONObject();
//			bank13.put("bankId", "HXB");
//			bank13.put("title", "华夏银行");
//			bankArr3.put(bank13);
//			JSONObject bank23 = new JSONObject();
//			bank23.put("bankId", "BJRCB");
//			bank23.put("title", "北京农村商业银行");
//			bankArr3.put(bank23);
//			JSONObject bank16 = new JSONObject();
//			bank16.put("bankId", "SRCB");
//			bank16.put("title", "上海农商");
//			bankArr3.put(bank16);
//			JSONObject bank24 = new JSONObject();
//			bank24.put("bankId", "SDB");
//			bank24.put("title", "深圳发展银行");
//			bankArr3.put(bank24);
//			JSONObject bank25 = new JSONObject();
//			bank25.put("bankId", "CZB");
//			bank25.put("title", "浙江稠州商业银行");
//			bankArr3.put(bank24);
//
//			obj3.put("bankArr", bankArr3);
//			obj3.put("moneyArr", moneyArr3);
//			if (gtUnionPay.equals("1")) {
//				items.put(obj3);
//			}
		}
		data.put("pt", Constants.DEF_PAY_COMPANY);

		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);

		// demo data add by cuisy
		// map = new
		// JSONObject(ManageFile.loadTextFileGBK(Constants.getWebRootPath()+"/card/recharge_list.js"));

		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 充值卡套餐列表
	 */
	public void list2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";

		
		String damianbaoAlipay="1";//大面包支付宝开关
		String damianbaoWechat="0";//大面包微信开关
		String lingdianAlipay="1";//零点支付宝开关
		String lingdianWechat="1";//零点微信开关
		
		List<Param> ParamList = CacheUtil.getParam();
		for (Param param : ParamList) {
			String type = param.getType();
			if(type.equals("33")){
				damianbaoAlipay=param.getValue();
			}else if(type.equals("34")){
				damianbaoWechat=param.getValue();
			}else if(type.equals("35")){
				lingdianAlipay=param.getValue();
			}else if(type.equals("36")){
				lingdianWechat=param.getValue();
			}
			
//			if (Constants.PARAM_DEF_PAY.equals(type)) {
//				Constants.DEF_PAY_COMPANY = param.getValue();
//			}
		}
		if (!ParamUtils.chkString(message)) {
			// 查询所有充值套餐
			List<Object> list = cardService
					.findObjects(new HQUtils(
							"from CardRechargeItem cri where 1=1 and cri.status = '1' order by cri.itemId"));
			
			JSONArray items = new JSONArray();// 数据集合
			JSONObject obj=new JSONObject();
			obj.put("title", "支付宝");//大面包支付宝
			obj.put("type", "1");
			obj.put("img", StringUtil.checkAPIHttpUrl("/images/ico-pay-alipay.png"));
			JSONArray moneyArr = new JSONArray();// 数据集合
			JSONObject jo = null;
			for (Object temp : list) {
				CardRechargeItem item = (CardRechargeItem) temp;
				jo = new JSONObject();
				jo.put("rid", item.getItemId());
				jo.put("parValue", ProductUtil.BigFormatJud(item.getParValue()));
				jo.put("price", ProductUtil.BigFormatJud(item.getPrice()));// 售价
				moneyArr.put(jo);
			}
			obj.put("moneyArr", moneyArr);
			if(damianbaoAlipay.equals("1")){
				items.put(obj);
			}
			
			JSONObject obj2=new JSONObject();
			obj2.put("title", "零点支付宝");//大面包支付宝
			obj2.put("type", "11");
			obj2.put("img", StringUtil.checkAPIHttpUrl("/images/ico-pay-alipay.png"));
			JSONArray moneyArr2 = new JSONArray();// 数据集合
			JSONObject jo2 = null;
			for (Object temp : list) {
				CardRechargeItem item = (CardRechargeItem) temp;
				jo2 = new JSONObject();
				jo2.put("rid", item.getItemId());
				jo2.put("parValue", ProductUtil.BigFormatJud(item.getParValue()));
				BigDecimal price = item.getPrice();
				jo2.put("price", ProductUtil.BigFormatJud(item.getPrice()));// 售价
				if(price.compareTo(new BigDecimal(3000))==-1||price.compareTo(new BigDecimal(3000))==0){
					moneyArr2.put(jo2);
				}
			}
			obj2.put("moneyArr", moneyArr2);
			if(lingdianAlipay.equals("1")){
				items.put(obj2);
			}
			
			JSONObject obj3=new JSONObject();
			obj3.put("title", "零点微信");
			obj3.put("type", "51");
			obj3.put("img", StringUtil.checkAPIHttpUrl("/images/ico-pay-wechat.png"));
			JSONArray moneyArr3 = new JSONArray();// 数据集合
			JSONObject jo3 = null;
			for (Object temp : list) {
				CardRechargeItem item = (CardRechargeItem) temp;
				jo3 = new JSONObject();
				jo3.put("rid", item.getItemId());
				jo3.put("parValue", ProductUtil.BigFormatJud(item.getParValue()));
				BigDecimal price = item.getPrice();
				jo3.put("price", ProductUtil.BigFormatJud(item.getPrice()));// 售价
				if(price.compareTo(new BigDecimal(3000))==-1||price.compareTo(new BigDecimal(3000))==0){
					moneyArr3.put(jo3);
				}
			}
			obj3.put("moneyArr", moneyArr3);
			if(lingdianWechat.equals("1")){
				items.put(obj3);
			}
			
			data.put("items", items);
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		data.put("pt", Constants.DEF_PAY_COMPANY);

		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);

		// demo data add by cuisy
		// map = new
		// JSONObject(ManageFile.loadTextFileGBK(Constants.getWebRootPath()+"/card/recharge_list.js"));

		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 充值明细
	 */
	public void detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String cashType = ParamUtils.getParameter(deParameter, "cashType");// 用户资金来源类型---充值类型
																			// 1=在线充值
																			// 3=线下充值
																			// 14
																			// =系统充值
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize",
				RamConstants.MAXPAGEITEMS);

		String u = ParamUtils.getParameter(deParameter, "u", "");

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";

		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
				HQUtils hq = new HQUtils();
				// hq.addHsql("from UserPointDetail utd where utd.userId=? and (utd.cashType=? or utd.cashType=?) ");
				// hq.addHsql("from UserTradeDetail utd where utd.userId=? and utd.cashType='1' ");
				// hq.addPars(uid);
				// hq.addPars(Constants.POINT_TYPE_ONLINE);//只查询在线充值和卡充的明细
				// hq.addPars(Constants.POINT_TYPE_CARD);//
				// hq.setOrderby("order by utd.tradeDetailId desc ");
				hq.addHsql("from CardRechargeOrder order where order.userId=? ");
				hq.addPars(uid);
				hq.setPageSize(pageSize);
				hq.setStartIndex(pageIndex * pageSize);
				
				if(ParamUtils.chkString(cashType)){
					hq.addHsql(" and order.payType = ? ");
					hq.addPars(cashType);
				}
				hq.setOrderby("order by   order.orderId  desc");

				PaginationSupport ps = cardService.findObjectPage(hq);
				@SuppressWarnings("unchecked")
				List<CardRechargeOrder> list = ps.getItems();// 明细分页数据
				JSONArray items = new JSONArray();// 数据集合
				JSONObject jo = null;
				Integer pageNum = 0;
				if (list != null && list.size() > 0) {
					pageNum = list.size();
					for (CardRechargeOrder order : list) {
						jo = new JSONObject();
						jo.put("did", order.getOrderId());
						if (order.getPayType().equals("3")) {
							jo.put("title", "线下充值");
						} else if (order.getPayType().equals("1")) {
							jo.put("title", "支付宝");
						} else if (order.getPayType().equals("5")) {
							jo.put("title", "微信");
						} else {
							jo.put("title", "");
						}
						jo.put("money",
								ProductUtil.BigFormatJud(order.getTotalMoney()));
						jo.put("createDate", DateTimeUtil
								.DateToStringAll(order.getCreateTime()));
						String status = order.getPayStatus();
						if (status == null || "0".equals(status)) {
							status = "0";
						} else if (status.equals("1")) {
							status = "2";
						} else if (status.equals("2")) {
							status = "1";
						}
						jo.put("status", status);
						items.put(jo);
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
				} else {
					code = APIConstants.CODE_NOT_FOUND;
				}
				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 提交订单
	 */
	public void submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		int rid = ParamUtils.getIntParameter(deParameter, "rid", 0);// 套餐id
		String payType = ParamUtils.getParameter(deParameter, "payType", "");// 在线支付方式1.支付宝2.银联5.微信
		String u = ParamUtils.getParameter(deParameter, "u", "");//
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型//0.网页
																		// 1.IOS2.Android
		String bankId = ParamUtils.getParameter(deParameter, "bankId", "");//
		if (!device.equals("0")) {
			device = "1";
		}

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		if (!ParamUtils.chkInteger(rid) || !ParamUtils.chkString(u)
				|| !ParamUtils.chkString(payType)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		BigDecimal totalPrice = new BigDecimal(0);// 总价格

		CardRechargeOrder order = new CardRechargeOrder();
		CardRechargeItem product = (CardRechargeItem) cardService.getObject(
				CardRechargeItem.class, rid);// 套餐

		BigDecimal parValue = product.getParValue();
		User user =null;
		Integer uid =0;
		Map<String, String> decryptMap = DesUtils.decryptMap(u);
		if(decryptMap!=null){
			uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			user = userService.getValidUser(uid);
		}
		
		
		// 判断1
		if (message.equals("")&&user!=null) {

			if (payType.equals("ALIPAY") || payType.equals("ALIPAYWAP")) {
				order.setThreepayType(payType);
				payType = "1";
			} else if (payType.equals("WEIXIN") || payType.equals("WEIXINWAP")) {
				order.setThreepayType(payType);
				payType = "5";
			} else if (payType.equals("bank") || payType.equals("UNIONPAY")) {
				payType = "2";
			} else if (payType.equals("QQPAY") || payType.equals("QQPAYWAP")) {
				order.setThreepayType(payType);
				payType = "4";
			}

			// BigDecimal price = product.getPrice();

			totalPrice = parValue;

			order.setUserId(uid);
			order.setPayType(payType);// 在线支付方式
			order.setTotalMoney(totalPrice);
			order.setPayPrice(totalPrice);
			order.setItemId(rid);
			String orderNum = ProductUtil
					.createOrderNum(Constants.MODULE_RECHARGE);
			order.setOrderNum(orderNum);
			order.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
			order.setPayStatus(CardConstants.ORDER_PAY_NOT);

			if (ParamUtils.chkString(bankId)) {
				order.setThreepayType(bankId);
			}
			order = (CardRechargeOrder) cardService.saveObjectDB(order);

			// 普通订单信息
			JSONObject obj = new JSONObject();
			obj.put("title", product.getTitle());
			Integer oid = order.getOrderId();
			String key = Constants.DES_KEY_OID + "=" + oid;

			obj.put("key", DesUtils.encrypt(key));
			obj.put("orderNum", orderNum);
			obj.put("payPrice", ProductUtil.BigFormatJud(order.getPayPrice()));
			obj.put("totalPrice",
					ProductUtil.BigFormatJud(order.getTotalMoney()));
			data.put("obj", obj);
			JSONObject bxsPayObj = new JSONObject();
			String payUrl = "";

			payUrl = StringUtil.checkAPIHttpUrl("/pay/gtPay/" + orderNum);
			code = APIConstants.CODE_REQUEST_SUCCESS;

			bxsPayObj.put("payUrl", payUrl);
			bxsPayObj.put("isWeb", "1");
			data.put("bxsPayObj", bxsPayObj);
		} else {
			message = APIConstants.TIPS_NOT_USER;
			code = APIConstants.CODE_NOT_FOUND;
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
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String type = ParamUtils.getParameter(request, "type"); // 1.支付宝 2.银联 //
																// 5.微信
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
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			User user = userService.getUser(uid);
			if (user != null) {

				String oid = DesUtils.decrypt(key);// 解析订单id，格式：oid=xxx add by
													// cuisy
				if (ParamUtils.chkString(oid) && oid.indexOf("=") > -1)
					oid = oid.split("=")[1];

				CardRechargeOrder order = (CardRechargeOrder) cardService
						.getObject(CardRechargeOrder.class,
								Integer.valueOf(oid));

				if (order != null) {
					Integer userId = order.getUserId();
					if (uid.intValue() == userId.intValue()) {// 是否是登录用户的订单
						if (order.getPayStatus().equals(
								CardConstants.ORDER_PAY_NOT)) {
							order.setPayType(type);
							order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
							order.setPayTime(DateTimeUtil.getJavaUtilDateNow());
							cardService.updateUserRecharge(order);
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
		String payGateway = ParamUtils.getParameter(request, "payGateway");// 支付网关类型
																			// 1=微信支付
																			// 2=支付宝
		String tradeNo = ParamUtils.getParameter(request, "tradeNo"); // 交易号
		String status = ParamUtils.getParameter(request, "tradeStatus"); // 交易状态
																			// 1.成功2.失败0.等待
		String payDate = ParamUtils.getParameter(request, "gmtPayment"); // 付款时间
		String orderNum = ParamUtils.getParameter(request, "orderNo");

		String payType = "";
		if (payGateway.equals("2")) {
			payType = APIConstants.PAY_TYPE_ALIPAY;
		} else if (payGateway.equals("1")) {// 微信
			payType = APIConstants.PAY_TYPE_WECHAT;
		}
		String message = "";
		System.out.println("____[recharge_callback][status=" + status
				+ "][tradeNo=" + tradeNo + "][orderNum=" + orderNum + "]");
		// if (!ParamUtils.chkString(orderNum) || !ParamUtils.chkString(tradeNo)
		// || !ParamUtils.chkString(status)) {
		// message = APIConstants.PARAMS_EMPTY_MSG;
		// }
		if (!ParamUtils.chkString(orderNum)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {// 支付成功
			if (status.equals("1")) {
				CardRechargeOrder order = cardService
						.getRechargeOrderByOrderNum(orderNum);
				if (order.getPayStatus().equals(CardConstants.ORDER_PAY_NOT)) {// 支付成功
					order.setPayType(payType);
					order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
					order.setTradeNo(tradeNo);
					order.setPayTime(DateTimeUtil.parse(payDate));
					cardService.updateUserRecharge(order);
				}
				message = "SUCCESS";// 通知回调成功
			}
		}
		JsonUtil.AjaxWriter(response, message);
	}

	/**
	 * 异步回调 - 零点支付
	 */
	public void callbackLingdian(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding(MerConfig.CHARSET);
		response.setCharacterEncoding(MerConfig.CHARSET);
		response.setContentType("text/html; charset="+MerConfig.CHARSET);
		Map<String, String> resMap = SignUtil.parseParam(request,MerConfig.CHARSET);
		for(Entry<String, String> e :resMap.entrySet()){
			System.out.println("key:"+e.getKey()+",value:"+e.getValue());
		}
		System.out.println("异步通知接收数据:"+SignUtil.getURLParam(resMap,MerConfig.URL_PARAM_CONNECT_FLAG,true, null));		
		if (MerConfig.verifyAsynNotifySign(resMap)){
			System.out.println("签名验证结果成功");
			if ("00000".equalsIgnoreCase(resMap.get("RspCod"))
				|| "1".equalsIgnoreCase(resMap.get("Status"))){
				//支付成功进行处理
				String orderNum=resMap.get("TxSN");
				String tradeNo=resMap.get("PlatTxSN");
				String TxTime=resMap.get("TxTime");
				CardRechargeOrder order = cardService.getRechargeOrderByOrderNum(orderNum);
				if (order.getPayStatus()
						.equals(CardConstants.ORDER_PAY_NOT)) {// 支付成功
//					order.setPayType(payType);
					order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
					 order.setTradeNo(tradeNo);
					String format = "yyyyMMddHHmmss";
					order.setPayTime(DateTimeUtil.parse(TxTime, format));
					cardService.updateUserRecharge(order);
				}
			}
			else {
				//支付失败进行处理
			}
		}else {
			System.out.println("签名验证结果失败");
			//签名异常处理
		}
		PrintWriter out = response.getWriter();
		response.setStatus(200);
		out.print("success");
		out.flush();
		out.close();	
	}
	/**
	 * 异步回调 - 轻易付
	 */
	public void callbackQYF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String message = "";
		String data = request.getParameter("data");
		JSONObject jsonObj = new JSONObject(data);
		String orderNum = jsonObj.getString("orderNum");
		
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		
		if (!ParamUtils.chkString(orderNum)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {// 支付成功
			String status = jsonObj.getString("payResult");// 00表示成功
			String netway = jsonObj.getString("netway");// 支付网关(支付宝填写ZFB,微信填写WX,支付宝WAP填写：ZFB_WAP，QQ钱包填写：QQ，QQ钱包WAP填写：QQ_WAP)
			String payType = "";
			if (netway.equals("ZFB") || netway.equals("ZFB_WAP")) {//
				payType = APIConstants.PAY_TYPE_ALIPAY;
			} else if (netway.equals("WX")) {
				payType = APIConstants.PAY_TYPE_WECHAT;
			}
			String payDate = jsonObj.getString("payDate");
			
			metaSignMap.put("merNo", jsonObj.getString("merNo"));
			metaSignMap.put("netway", jsonObj.getString("netway"));
			metaSignMap.put("orderNum", orderNum);
			metaSignMap.put("amount", jsonObj.getString("amount"));
			metaSignMap.put("goodsName", jsonObj.getString("goodsName"));
			metaSignMap.put("payResult", status);// 支付状态
			metaSignMap.put("payDate", jsonObj.getString("payDate"));// yyyyMMddHHmmss
			String jsonStr = QYFPayUtil.mapToJson(metaSignMap);
			String sign = QYFPayUtil.MD5(jsonStr.toString() + QYFPayUtil.key,
			"UTF-8");
			// 验证签名
			if (sign.equals(jsonObj.getString("sign"))) {
				if (status.equals("00")) {
					CardRechargeOrder order = cardService
					.getRechargeOrderByOrderNum(orderNum);
					if (order.getPayStatus()
							.equals(CardConstants.ORDER_PAY_NOT)) {// 支付成功
						order.setPayType(payType);
						order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
						// order.setTradeNo(tradeNo);
						String format = "yyyyMMddHHmmss";
						order.setPayTime(DateTimeUtil.parse(payDate, format));
						cardService.updateUserRecharge(order);
					}
					message = "0";// 通知回调成功
				}
				
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
				CardRechargeOrder order = cardService
						.getRechargeOrderByOrderNum(orderNum);
				String status = order.getPayStatus();
				if (status.equals(CardConstants.ORDER_PAY_NOT)) {
					order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
					order.setPayType(APIConstants.PAY_TYPE_WECHAT);
					order.setTradeNo(tradeNo);
					order.setPayTime(DateTimeUtil.getJavaUtilDateNow());
					cardService.updateUserRecharge(order);
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
	 * 仁信异步回调
	 */
	public void callbackRenXin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "fail";
		String partner = ParamUtils.getParameter(request, "partner");// 商户号
		String orderNum = ParamUtils.getParameter(request, "ordernumber");// 订单号
		String status = ParamUtils.getParameter(request, "orderstatus");// 订单状态
		String money = ParamUtils.getParameter(request, "paymoney");// 支付金额
		String tradeNo = ParamUtils.getParameter(request, "sysnumber");// 交易号
		String sign = ParamUtils.getParameter(request, "sign");// 签名
		// 先做签名校验
		String singPara = "partner={PARTNER}&ordernumber={ORDERNUMBER}&orderstatus={ORDERSTATUS}&paymoney={PAYMONEY}"
				+ RenXinPayUtil.secretKey;
		singPara = singPara.replace("{PARTNER}", partner)
				.replace("{ORDERNUMBER}", orderNum)
				.replace("{ORDERSTATUS}", status).replace("{PAYMONEY}", money);
		String sign2 = Util.encryption(singPara);

		if (sign2.equals(sign)) {// 签名相同
			CardRechargeOrder order = cardService
					.getRechargeOrderByOrderNum(orderNum);
			if (order != null) {
				String payStatus = order.getPayStatus();
				if (CardConstants.ORDER_PAY_NOT.equals(payStatus)) {// 待支付
					order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
					order.setPayTime(new Date());
					order.setTradeNo(tradeNo);
					cardService.updateUserRecharge(order);
					message = "ok";// 告诉仁信支付成功
				}
			}
		}
		JsonUtil.AjaxWriter(response, message);
	}

	/**
	 * 闪付异步回调
	 */
	public void callbackShanfu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "fail";
		String memberID = ParamUtils.getParameter(request, "MemberID");// 商户 ID
		String terminalID = ParamUtils.getParameter(request, "TerminalID");// 终端
																			// ID
		String orderNum = ParamUtils.getParameter(request, "TransID");// 订单 ID
		String result = ParamUtils.getParameter(request, "Result");// 1：成功 0：失败
		String resultDesc = ParamUtils.getParameter(request, "ResultDesc");// 订单结果
		String factMoney = ParamUtils.getParameter(request, "FactMoney");// 成功金额
		String additionalInfo = ParamUtils.getParameter(request,
				"AdditionalInfo");// 订单附加信息
		String succTime = ParamUtils.getParameter(request, "SuccTime");// 交易成功时间
		String sign = ParamUtils.getParameter(request, "Md5Sign");// 签名
		// 先做签名校验
		String singPara = "MemberID={MEMBERID}|TerminalID={TERMINALID}|TransID={TRANSID}|Result={RESULT}|ResultDesc={RESULTDESC}|FactMoney={FACTMONEY}|AdditionalInfo={ADDITIONALINFO}|SuccTime={SUCCTIME}|Md5Sign={SIGN}";
		singPara = singPara.replace("{MEMBERID}", ShanFuPayUtil.partner)
				.replace("{TERMINALID}", ShanFuPayUtil.terminalId)
				.replace("{TRANSID}", orderNum).replace("{RESULT}", result)
				.replace("{RESULTDESC}", resultDesc)
				.replace("{FACTMONEY}", factMoney)
				.replace("{ADDITIONALINFO}", additionalInfo)
				.replace("{SUCCTIME}", succTime)
				.replace("{sign}", ShanFuPayUtil.secretKey);
		String sign2 = Util.encryption(singPara);
		if (sign2.equals(sign)) {// 签名相同
			CardRechargeOrder order = cardService
					.getRechargeOrderByOrderNum(orderNum);
			if (order != null) {
				if ("1".equals(request)) {// 支付成功
					BigDecimal payPrice = order.getPayPrice();
					BigDecimal factPrice = new BigDecimal(
							Integer.valueOf(factMoney));
					if (payPrice.compareTo(factPrice) == 0) {
						String payStatus = order.getPayStatus();
						if (CardConstants.ORDER_PAY_NOT.equals(payStatus)) {// 待支付
							order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
							order.setPayTime(new Date());
							// order.setTradeNo(tradeNo);
							cardService.updateUserRecharge(order);
							message = "OK";// 告诉闪付支付成功
						}
					}
				}
			}
		}
		JsonUtil.AjaxWriter(response, message);
	}

	/**
	 * 艾米森异步回调
	 */
	public void callbackAmxmy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String src_code = ParamUtils.getParameter(request, "src_code");
		String tradeNo = ParamUtils.getParameter(request, "trade_no");
		String orderNo = ParamUtils.getParameter(request, "out_trade_no");// 订单号
		String time_start = ParamUtils.getParameter(request, "time_start");
		String pay_time = ParamUtils.getParameter(request, "pay_time");
		String factMoney = ParamUtils.getParameter(request, "total_fee");

		String tradeType = ParamUtils.getParameter(request, "trade_type");// 支付类型微信公众号:50103;微信扫码:50104;微信刷卡:50101
		String feeType = ParamUtils.getParameter(request, "fee_type");// 货币类型（默认CNY）
		String goods_name = ParamUtils.getParameter(request, "goods_name");// 商品名称
		String goods_detail = ParamUtils.getParameter(request, "goods_detail");// 商品名称
		String status = ParamUtils.getParameter(request, "order_status");// 订单状态,未支付:1;支付中:2;已支付:3;支付失败:4
		String orderType = ParamUtils.getParameter(request, "order_type");// 订单类型,有效:1;关闭:2
		String cancel = ParamUtils.getParameter(request, "cancel");// 是否已退款,无退款:1;已退款:2
		String outMchid = ParamUtils.getParameter(request, "out_mchid");// 接入方商户号
		String mchid = ParamUtils.getParameter(request, "mchid");// 商户号
		String orig_trade_no = ParamUtils
				.getParameter(request, "orig_trade_no");// 商户号
		String time_expire = ParamUtils.getParameter(request, "time_expire");// 商户号
		String sign = ParamUtils.getParameter(request, "sign");// 签名

		String message = "fail";
		Map<String, String> map = new HashMap<String, String>();
		map.put("src_code", AmxmyPayUtil.srcCode);
		if (ParamUtils.chkString(tradeNo)) {
			map.put("trade_no", tradeNo);
		}
		if (ParamUtils.chkString(orderNo)) {
			map.put("out_trade_no", orderNo);
		}
		if (ParamUtils.chkString(time_start)) {
			map.put("time_start", time_start);
		}
		if (ParamUtils.chkString(pay_time)) {
			map.put("pay_time", pay_time);
		}
		if (ParamUtils.chkString(factMoney)) {
			map.put("total_fee", factMoney);
		}
		if (ParamUtils.chkString(tradeType)) {
			map.put("trade_type", tradeType);
		}
		if (ParamUtils.chkString(feeType)) {
			map.put("fee_type", feeType);
		}
		if (ParamUtils.chkString(goods_name)) {
			map.put("goods_name", goods_name);
		}
		if (ParamUtils.chkString(goods_detail)) {
			map.put("goods_detail", goods_detail);
		}
		if (ParamUtils.chkString(status)) {
			map.put("order_status", status);
		}
		if (ParamUtils.chkString(orderType)) {
			map.put("order_type", orderType);
		}
		if (ParamUtils.chkString(cancel)) {
			map.put("cancel", cancel);
		}
		if (ParamUtils.chkString(outMchid)) {
			map.put("out_mchid", outMchid);
		}
		if (ParamUtils.chkString(mchid)) {
			map.put("mchid", mchid);
		}
		if (ParamUtils.chkString(orig_trade_no)) {
			map.put("orig_trade_no", orig_trade_no);
		}
		if (ParamUtils.chkString(time_expire)) {
			map.put("time_expire", time_expire);
		}

		String sign2 = AmxmyPayUtil.sign(map);
		log.error("____________amxmypay sign2::" + sign2 + "____sign::" + sign);
		if (sign2.equals(sign)) {// 查询成功
			if ("3".equals(status)) {// 支付成功
				CardRechargeOrder order = cardService
						.getRechargeOrderByOrderNum(orderNo);
				BigDecimal payPrice = order.getPayPrice();
				String s = ParamUtils.BigFormatIntString(payPrice);
				BigDecimal factPrice = new BigDecimal(
						Integer.valueOf(factMoney));

				String payStatus = order.getPayStatus();
				if (CardConstants.ORDER_PAY_NOT.equals(payStatus)) {
					if (new BigDecimal(s).compareTo(factPrice) == 0) {
						order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
						order.setPayTime(new Date());
						order.setTradeNo(tradeNo);
						cardService.updateUserRecharge(order);
						message = "SUCCESS";
					}
				}
			}
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
				CardRechargeOrder order = cardService
						.getRechargeOrderByOrderNum(orderId);
				String status = order.getPayStatus();

				order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
				if (status.equals(CardConstants.ORDER_PAY_NOT)) {
					order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
					order.setPayType(APIConstants.PAY_TYPE_UNIONPAY);
					order.setPayTime(new Date());
					cardService.updateUserRecharge(order);
				}
				message = "success";
			} else {
			}
		} else {
		}

		JsonUtil.AjaxWriter(response, message);
	}

	/**
	 * 线下充值
	 */
	public void offlineSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String payType = ParamUtils.getParameter(deParameter, "payType", "3");// 在线支付方式1.支付宝2.银联5.微信
																				// 3线下
		String u = ParamUtils.getParameter(deParameter, "u", "");//
		String receiveBankName = ParamUtils.getParameter(deParameter,
				"receiveBankName", "");
		String totalMoney = ParamUtils.getParameter(deParameter, "money");
		String receiveBankAccount = ParamUtils.getParameter(deParameter,
				"receiveBankAccount", "");
		String receiveUserName = ParamUtils.getParameter(deParameter,
				"receiveUserName", "");
		String receiveCountry = ParamUtils.getParameter(deParameter,
				"receiveCountry", "");
		String depositorBankName = ParamUtils.getParameter(deParameter,
				"depositorBankName", "");
		String depositorUserName = ParamUtils.getParameter(deParameter,
				"depositorUserName", "");
		String depositorBankAccount = ParamUtils.getParameter(deParameter,
				"depositorBankAccount", "");
		String depositType = ParamUtils.getParameter(deParameter,
				"depositType", "");

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		if (!ParamUtils.chkString(u) || !ParamUtils.chkString(receiveBankName)
				|| !ParamUtils.chkString(receiveBankAccount)
				|| !ParamUtils.chkString(receiveUserName)
				|| !ParamUtils.chkString(receiveCountry)
//				|| !ParamUtils.chkString(depositorBankName)
				|| !ParamUtils.chkString(depositorUserName)
//				|| !ParamUtils.chkString(depositType)
				|| !ParamUtils.chkString(totalMoney)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		Integer uid = 0;

		CardRechargeOrder order = new CardRechargeOrder();

		if (payType.equals(APIConstants.PAY_TYPE_ALIPAY)) {
			message = "暂不支持支付宝支付，请选择其他支付方式";
		}
		// 判断1
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			uid = Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			BigDecimal totalPrice = new BigDecimal(totalMoney);// 总价格
			User user = userService.getUser(uid);
			if (user == null) {
				message = "用户不存在";
			} else {
				order.setUserId(uid);
				order.setPayType(payType);// 在线支付方式
				order.setTotalMoney(totalPrice);
				// order.setPayPrice(totalPrice);
				order.setDepositorBankAccount(depositorBankAccount);
				order.setDepositorBankName(depositorBankName);
				order.setDepositType(depositType);
				order.setDepositorUserName(depositorUserName);
				order.setReceiveBankAccount(receiveBankAccount);
				order.setReceiveBankName(receiveBankName);
				order.setReceiveCountry(receiveCountry);
				order.setReceiveUserName(receiveUserName);

				String orderNum = ProductUtil
						.createOrderNum(Constants.MODULE_RECHARGE);
				order.setOrderNum(orderNum);
				order.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
				order.setPayStatus("0");
				order = (CardRechargeOrder) cardService.saveObjectDB(order);
				code = APIConstants.CODE_REQUEST_SUCCESS;
				message = "提交成功，请等待确认";
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 线下充值 || 银行转账充值---海豚彩票
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void offLineRecharge(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		Integer bankId = ParamUtils.getIntegerParameter(deParameter, "bankId");// 充值银行id
		String message = "";
		if (!ParamUtils.chkString(u) || !ParamUtils.chkInteger(bankId)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			SysBank bank = (SysBank) cashService.getObject(SysBank.class,
					bankId);
			if (bank == null) {
				message = "参数错误！";
				code = APIConstants.CODE_REQUEST_ERROR;
				map.put("msg", message);
				map.put("code", code);
				map.put("data", data);
				JsonUtil.AjaxWriter(response, map);
			} else {
				data.put("bankName",
						ParamUtils.chkStringNotNull(bank.getBankName()));
				data.put("userName",
						ParamUtils.chkStringNotNull(bank.getUserName()));
				data.put("bankAccount",
						ParamUtils.chkStringNotNull(bank.getBankAccount()));
				data.put("bankBranch",
						ParamUtils.chkStringNotNull(bank.getBankBranch()));
			}

			CardRechargeOrder order = new CardRechargeOrder();
			order.setUserId(uid);
			order.setPayType(Constants.CASH_TYPE_OFFLINE);// 线下充值
			String orderNum = ProductUtil
					.createOrderNum(Constants.MODULE_RECHARGE);
			order.setOrderNum(orderNum);
			order.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
			order.setPayStatus(Constants.PUB_STATUS_CLOSE); // 线下生成订单号，未支付
			order.setBankId(bankId);
			cardService.updateUserRechargeOffLine(order);

			data.put("orderNum", ParamUtils.chkStringNotNull(orderNum));// 订单号
			message = APIConstants.TIPS_OPERATION_SUCC;
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 线下提交订单 ---海豚彩票
	 */
	public void offLineSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String orderNum = ParamUtils.getParameter(deParameter, "orderNum");// 订单编号
		String payTime = ParamUtils.getParameter(deParameter, "payTime");// 支付时间
		String totalMoney = ParamUtils.getParameter(deParameter, "totalMoney");// 支付金额
		String depositorUserName = ParamUtils.getParameter(deParameter,
				"userName");// 存款人
		String u = ParamUtils.getParameter(deParameter, "u");
		String depositType = ParamUtils
				.getParameter(deParameter, "depositType");// 打款方式
		// 0=网银转账 1=ATM自动柜员机 2=ATM现金入款 3= 银行柜台转账 4= 手机银行转账 5=其他

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		if (!ParamUtils.chkString(orderNum) || !ParamUtils.chkString(payTime)
				|| !ParamUtils.chkString(totalMoney)
				|| !ParamUtils.chkString(depositorUserName)
				|| !ParamUtils.chkString(u)
				|| !ParamUtils.chkString(depositType)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}

		// 判断1
		if (message.equals("")) {
			BigDecimal money = new BigDecimal(totalMoney);
			if (money == null || money.compareTo(new BigDecimal(0)) <= 0) {
				code = APIConstants.CODE_REQUEST_ERROR;
				message = "参数错误！";
				map.put("code", code);
				map.put("msg", message);
				map.put("data", data);
				JsonUtil.AjaxWriter(response, map);
			}
			CardRechargeOrder order = cardService
					.getRechargeOrderByOrderNum(orderNum);
			if (order == null) {
				code = APIConstants.CODE_REQUEST_ERROR;
				message = "参数错误！";
				map.put("code", code);
				map.put("msg", message);
				map.put("data", data);
				JsonUtil.AjaxWriter(response, map);
			}
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));

			Integer userId = order.getUserId();
			if (uid.intValue() != userId.intValue()) {
				code = APIConstants.CODE_REQUEST_ERROR;
				message = "参数错误！";
				map.put("code", code);
				map.put("msg", message);
				map.put("data", data);
				JsonUtil.AjaxWriter(response, map);
			}

			Date time = DateTimeUtil.StringToDate(payTime); // 格式yyyy-MM-dd
			if (time == null || time.compareTo(new Date()) == 1) {
				code = APIConstants.CODE_REQUEST_ERROR;
				message = "参数错误！";
				map.put("code", code);
				map.put("msg", message);
				map.put("data", data);
				JsonUtil.AjaxWriter(response, map);
			}
			// int type = Integer.valueOf(depositType).intValue();
			// if(type >= 6){
			// code = APIConstants.CODE_REQUEST_ERROR;
			// message = "参数错误！";
			// map.put("code", code);
			// map.put("msg", message);
			// map.put("data", data);
			// JsonUtil.AjaxWriter(response, map);
			// }
			order.setPayTime(time);
			order.setTotalMoney(money);
			order.setDepositorUserName(depositorUserName);
			order.setDepositType(depositType);
			order.setPayStatus("2"); // 未发货
			cardService.updateUserRechargeOffLine(order);
			code = APIConstants.CODE_REQUEST_SUCCESS;
			message = APIConstants.TIPS_OPERATION_SUCC;
		}

		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 银行卡信息
	 */
	public void bankPayInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", Constants.getDomainName() + "/help/deposit.html");
		data.put("alipayUrl", Constants.getDomainName()
				+ "/help/depositAlipay.html");
		data.put("wecahtUrl", Constants.getDomainName()
				+ "/help/depositWechat.html");
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 支付宝/微信/QQ 扫码支付提交
	 */
	public void codeRechargeSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String payType = ParamUtils.getParameter(deParameter, "payType", "7");// 在线支付方式1.支付宝2.银联5.微信
																				// 3线下
		String money = ParamUtils.getParameter(deParameter, "money", "");//
		String ID = ParamUtils.getParameter(deParameter, "ID", "");//

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		if (!ParamUtils.chkString(ID) || !ParamUtils.chkString(money)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}

		CardRechargeOrder order = new CardRechargeOrder();

		if (payType.equals(APIConstants.PAY_TYPE_ALIPAY)) {
			message = "暂不支持支付宝支付，请选择其他支付方式";
		}
		// 判断1
		if (message.equals("")) {

			User user = userService.getUser(Integer.parseInt(ID));
			if (user == null) {
				message = "用户不存在";
			} else {
				order.setUserId(Integer.parseInt(ID));
				order.setPayType(payType);// 在线支付方式
				order.setTotalMoney(new BigDecimal(money));
				String orderNum = ProductUtil
						.createOrderNum(Constants.MODULE_RECHARGE);
				order.setOrderNum(orderNum);
				order.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
				order.setPayStatus("0");
				order = (CardRechargeOrder) cardService.saveObjectDB(order);
				code = APIConstants.CODE_REQUEST_SUCCESS;
				message = "提交成功，请等待确认";
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	
	/**
	 * 用户的充值方式
	 */
	public void userRechargeWay(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u", "");
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";

		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
				Integer passageWayId = user.getRechargeWay();// 用户所属的充值通道
				if (ParamUtils.chkInteger(passageWayId)) {
					HQUtils hq = new HQUtils();
					hq.addHsql("from RechargeWay rw where rw.parentId=? and rw.type=? and rw.status=?");
					hq.addPars(passageWayId);
					hq.addPars(Constants.RECHARGE_WAY_2);
					hq.addPars(Constants.PUB_STATUS_OPEN);
					List<Object> rwList = userService.findObjects(hq);
					JSONArray items = new JSONArray();// 数据集合
					JSONObject obj = null;
					if (rwList != null && rwList.size() > 0) {
						for (int i = 0;i < rwList.size();i++) {
							RechargeWay rw = (RechargeWay) rwList.get(i);
							obj = new JSONObject();
							obj.put("title", rw.getTitle());
							obj.put("channelType", rw.getChannelType());
							Integer id = rw.getId();
							
							HQUtils hq2 = new HQUtils("from RechargeWay rw where rw.parentId=? and rw.type=? and rw.status=?");
							hq2.addPars(id);
							hq2.addPars(Constants.RECHARGE_WAY_3);
							hq2.addPars(Constants.PUB_STATUS_OPEN);
							List<Object> rwList2 = userService.findObjects(hq2);
							
							JSONArray wayArr = new JSONArray();// 充值方式
							JSONObject obj2 = null;
							if (rwList2 != null && rwList2.size() > 0) {
								for (int j = 0;j < rwList2.size();j++) {
									RechargeWay rw2 = (RechargeWay) rwList2.get(j);
									obj2 = new JSONObject();
									obj2.put("id", rw2.getId());
									obj2.put("title", rw2.getTitle());
									obj2.put("bankName", rw2.getBankName());
									obj2.put("bankAccount", rw2.getBankAccount());
									obj2.put("userName", rw2.getUserName());
									obj2.put("des", rw2.getDes());
									obj2.put("img", StringUtil.checkAPIHttpUrl(rw2.getImg()));
									obj2.put("url", rw2.getUrl());
									wayArr.put(obj2);
								}
							}
							obj.put("wayArr", wayArr);
							items.put(obj);
						}
						code = APIConstants.CODE_REQUEST_SUCCESS;
					} else {
						code = APIConstants.CODE_NOT_FOUND;
					}
					data.put("items", items);
				} else {
					message = "此用户不能充值";
					code = APIConstants.CODE_NOT_FOUND;
				}
			} else {
				message = APIConstants.TIPS_NOT_USER;
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
}