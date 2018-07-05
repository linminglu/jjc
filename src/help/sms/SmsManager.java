package help.sms;

import help.base.APIConstants;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.model.MessageCount;
import com.apps.model.SMSCache;
import com.apps.service.ISmsService;
import com.apps.util.JsonUtil;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.util.IPUtil;

/**
 * 发送短信管理类
 * 
 * @author Mr.zang
 * 
 */
public class SmsManager extends BaseDispatchAction {
	protected IUserService userService = (IUserService) ServiceLocatorImpl
			.getInstance().getService("userService");
	private final ISmsService smsService = (ISmsService) getService("smsService");

	/**
	 * 发送短信
	 */
	public synchronized void send(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u", "");
		String mobile = ParamUtils.getParameter(deParameter, "mobile", "");
		String type = ParamUtils.getParameter(deParameter, "type");// 1.注册
																// 2.找回密码3.绑定手机号4.提现发送验证码
//		String chkCode = ParamUtils.getParameter(deParameter, "chkCode");// 图形码

		String message = "";
		if(type.equals("1")||type.equals("2")||type.equals("3")){
			if (!ParamUtils.chkString(mobile) || !ParamUtils.chkString(type)) {
				message = APIConstants.PARAMS_EMPTY_MSG;
			}
		}else if(type.equals("4")){
			if (!ParamUtils.chkString(u) || !ParamUtils.chkString(type)) {
				message = APIConstants.PARAMS_EMPTY_MSG;
			}else{
				Map<String, String> decryptMap = DesUtils.decryptMap(u);
				Integer uid = Integer
						.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				User user = userService.getUser(uid);
				mobile = user.getLoginName();
			}
		}
		
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		Map<String, SMSCache> datelist = Constants.getIPlist();
		String ipAddr = IPUtil.getIpAddr(request);
		log.error("_____IP__::"+ipAddr);
		if (ParamUtils.chkString(ipAddr)) {
			String dateTime = DateTimeUtil.getDateTime();
			SMSCache cache = datelist.get(ipAddr);
			if (cache != null) {
				String iptime = cache.getDateTime();
				String cellPhone = cache.getCellPhone();
				String format = "yyyy-MM-dd HH:mm:ss";
				long dateDiffToSec = DateTimeUtil.dateDiffToSec(iptime,
						dateTime, format);
				// 时间间隔90秒
//				System.out.println(dateDiffToSec+"<<<<<<秒");
//				if (dateDiffToSec < 90 && mobile.equals(cellPhone)) {
				if (dateDiffToSec < 60) {
//					if(mobile.equals(cellPhone)){
						message = "短信发送频率太高，请稍后再试[-1]";
						log.error("______________________" + message);
//					}else{
//						cache.setDateTime(dateTime);
//						cache.setCellPhone(mobile);
//						datelist.put(ipAddr, cache);
//					}
				}else{
					cache.setDateTime(dateTime);
					datelist.put(ipAddr, cache);
				}
			} else {
				cache = new SMSCache(mobile, dateTime);
				datelist.put(ipAddr, cache);
			}
		}

//		System.out.println("____________[sysCode:"
//				+ request.getSession().getAttribute("chkCode") + "][chkCode:"
//				+ chkCode + "]");

//		if (ParamUtils.chkString(chkCode)) {
//			if (!chkCode.equals(request.getSession().getAttribute("chkCode"))) {
//				message = "图形码不正确";
//			}
//		}

		if (message.equals("")) {
			int num = smsService.getMobileCountDay(mobile);
			if (!ParamUtils.chkInteger(num)) {
				num = 0;
			}
			if (num >= Integer.parseInt(Constants.getMsgMaxCount())) {
				message = "短信发送频率太高，请稍后再试[-2]";
			}
			int num2 = smsService.getIPCountDay(IPUtil.getIpAddr(request));
			if (!ParamUtils.chkInteger(num2)) {
				num2 = 0;
			}
			if (num2 >= Integer.parseInt(Constants.getMsgMaxCount())) {
				message = "短信发送频率太高，请稍后再试[-3]";
			}

			MessageCount msgcount = smsService.getMessageLately(mobile);
			if (msgcount != null) {
				Date sendTime = msgcount.getSendTime();
				long sec = DateTimeUtil.dateDiffToSec(sendTime, new Date());// 相差秒数
				String msgTime = Constants.getMsgTime();
				int min = Integer.parseInt(msgTime);
				if (sec < min * 60) {
					message = "短信发送频率太高，请稍后再试[-4]";
				}
			}
		}
		
		
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (!message.equals("")) {
			code = APIConstants.CODE_REQUEST_ERROR;
		} else if (StringUtil.isMobileNO(mobile)) {
			String smsCode = String.valueOf(StringUtil.getRandomInt(111111,
					999999));// 生成验证码
			request.getSession().setAttribute(
					APIConstants.SESSION_SMS_CODE_NAME, smsCode);// 短信验证码放到session里
			request.getSession().setAttribute(APIConstants.SESSION_SMS_PHONE,
					mobile);// 短信验证码放到session里
			User user = userService.findUserByLoginName(mobile);
			if (type.equals("1")) {// 注册
				if (user != null) {
					message = APIConstants.USER_TIPS_UN_EXISTS;// 用户存在
				} else {
					String smsContent = APIConstants.SMS_REG_TEXT;
					smsContent = smsContent.replace("{**}", smsCode);
					data.put("mobile", mobile);
					data.put("mcode", "");
					code = APIConstants.CODE_REQUEST_SUCCESS;
//					message=smsContent;
//					boolean isSend = SmsSendUtil.sendSms(mobile,
//							smsContent,null);// 正式使用时请打开注掉
					boolean isSend = true;// 正式使用时请打开注掉
					
					if (isSend) {
						// 记录短信条数
						MessageCount sms = new MessageCount(mobile, new Date(),
								IPUtil.getIpAddr(request), smsContent);
						smsService.saveSms(sms);
					}
				}
			} else if (type.equals("2")) {// 找回密码
				if (user != null) {// 用户存在
					String smsContent = APIConstants.SMS_REG_TEXT;
					smsContent = smsContent.replace("{**}", smsCode);
					data.put("mobile", mobile);
					data.put("mcode", smsCode);
					code = APIConstants.CODE_REQUEST_SUCCESS;
//					message=smsContent;
					boolean isSend = SmsSendUtil.sendSms(mobile,
							smsContent,null);
					if (isSend) {
						// 记录短信条数
						MessageCount sms = new MessageCount(mobile, new Date(),
								IPUtil.getIpAddr(request), smsContent);
						smsService.saveSms(sms);
					}
				} else {
					message = APIConstants.TIPS_NOT_USER;
				}
			} else if (type.equals("3")) {
				String smsContent = APIConstants.SMS_REG_TEXT;
				smsContent = smsContent.replace("{**}", smsCode);
				data.put("mobile", mobile);
				data.put("mcode", "");
				code = APIConstants.CODE_REQUEST_SUCCESS;
//				message=smsContent;
				boolean isSend = SmsSendUtil.sendSms(mobile, smsContent,null);
				if (isSend) {
					// 记录短信条数
					MessageCount sms = new MessageCount(mobile, new Date(),
							IPUtil.getIpAddr(request), smsContent);
					smsService.saveSms(sms);
				}
			} else if (type.equals("4")) {
				String smsContent = APIConstants.SMS_REG_TEXT;
				smsContent = smsContent.replace("{**}", smsCode);
				data.put("mobile", mobile);
				data.put("mcode", "");
				code = APIConstants.CODE_REQUEST_SUCCESS;
//				message=smsContent;
				boolean isSend = SmsSendUtil.sendSms(mobile, smsContent,null);
				if (isSend) {
					// 记录短信条数
					MessageCount sms = new MessageCount(mobile, new Date(),
							IPUtil.getIpAddr(request), smsContent);
					smsService.saveSms(sms);
				}
			}
		} else {
			message = APIConstants.MOBILE_NOT;// 用户存在
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

}