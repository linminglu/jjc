package help.base;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.eff.TradeCacheUtil;
import com.apps.model.Param;
import com.apps.model.UserTradeDetail;
import com.apps.service.IBaseDataService;
import com.apps.service.IParamService;
import com.apps.service.ISmsService;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.apps.util.SessionUtil;
import com.apps.util.UploadUtil;
import com.card.model.RechargeWay;
import com.cash.model.UserBankBind;
import com.cash.service.ICashService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.FSO;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaBetDetail;
import com.game.model.GaBetPart;
import com.game.model.GaBetSponsor;
import com.game.model.UserLevel;
import com.game.model.dto.GaBetDetailDTO;
import com.game.model.dto.SpDetailDTO;
import com.game.service.IGaService;
import com.ram.model.IpRecord;
import com.ram.model.NewsInformation;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.util.IPUtil;
import com.ram.util.PasswordUtil;
import com.xy.ssc.cqssc.CqSscConstants;

import help.push.JPushUtil;
import help.push.PushBean;

/**
 * 用户管理类
 * 
 * @author Mr.zang
 * 
 */
public class UserManager extends BaseDispatchAction {
	private final IUserService userService = (IUserService) getService("userService");
	private final IBaseDataService baseDataService = (IBaseDataService) getService("baseDataService");
	private final ISmsService smsService = (ISmsService) getService("smsService");
	private final IParamService paramService = (IParamService) getService("paramService");
	private final ICashService cashService = (ICashService) getService("cashService");
	private final IGaService gaService = (IGaService) getService("gaService");
	
	public void regCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
//		Param  param=paramService.getParamByType(Constants.PARAM_REG_SWITCH);//注册类型如果param 的 value=1 为手机短信验证注册     value=0 为图形验证码验证码注册
		List<Param> list=CacheUtil.getParam();
		String value="";
		if(list!=null&&list.size()>0){
			for(Param param:list){
				if(param.getType().equals(Constants.PARAM_REG_SWITCH)){
					value=param.getValue();
				}
			}
		}
		if(!ParamUtils.chkString(value)){
				value="1";
		}
		if(value.equals(Constants.PUB_STATUS_CLOSE)){
				value="2";
		}

		code = APIConstants.CODE_REQUEST_SUCCESS;
		data.put("regType", value);
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

//	//短信注册
//	public void reg1(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		Map<String, String> deParameter = ParamUtils.deParameter(request);
//		String loginName = ParamUtils
//				.getParameter(deParameter, "loginName", "");//手机号
//		String password = ParamUtils.getParameter(deParameter, "password", "");// 密码
//		String inpcode = ParamUtils.getParameter(deParameter, "inpcode", "");// 用户输入的验证码
//		String inviteCode = ParamUtils.getParameter(deParameter, "inviteCode","");// 邀请码
//		String token = ParamUtils.getParameter(deParameter, "token", "");
//		
//		String userName = ParamUtils.getParameter(deParameter, "userName", "");// 用户昵称
//		String message = "";
//		String smsPhone = (String) request.getSession().getAttribute(
//				APIConstants.SESSION_SMS_PHONE);
//		String mcode = (String) request.getSession().getAttribute(
//				APIConstants.SESSION_SMS_CODE_NAME);
//
////		if(inpcode.equals("000000")){
////			mcode="000000";
////		}
//		
//		log.error("M___________________inpcode>" + inpcode + "___sdCode>"
//				+ mcode + "___loginName>" + loginName + "___smsPhone>"
//				+ smsPhone + "___date>[" + DateTimeUtil.getDateTime()
//				+ "]rec>>>>[user_reg]");
//		if (!ParamUtils.chkString(loginName) || !ParamUtils.chkString(inpcode)
//				|| !ParamUtils.chkString(mcode)
//				|| !ParamUtils.chkString(password)) {
//			message = APIConstants.PARAMS_EMPTY_MSG;
//		}
//		JSONObject map = new JSONObject();// 最外层
//		JSONObject data = new JSONObject();// 返回数据层
//		String code = APIConstants.CODE_REQUEST_ERROR;
////		if (!loginName.equals(smsPhone)) {
////			message = APIConstants.USER_TIPS_CODE_ERROR;
////			code = APIConstants.CODE_REQUEST_ERROR;
////		}
//		String isReg="1"; //短信
//		List<Param> paramList = CacheUtil.getParam();
//		for (Param param : paramList) {
//			String type = param.getType();
//			if(type.equals(Constants.PARAM_REG_SWITCH)){
//				isReg=param.getValue();
//			}
//		}
//		
//		if(!isReg.equals("1")){
//			message="暂停注册";
//		}
//		String ip = IPUtil.getIpAddr(request);
//		if(ParamUtils.chkString(ip)){
//			IpRecord ipRecord = userService.getIpRecordByIp(ip);
//			if(ipRecord != null && ipRecord.getCount() != null){
//				Integer count = ipRecord.getCount();
//				if(count >=3){
//					message="同一ip注册过多，禁止注册";
//				}else{
//					ipRecord.setCount(count+1);
//					userService.saveObject(ipRecord, null);
//				}
//			}else{
//				ipRecord = new IpRecord();
//				ipRecord.setIp(ip);
//				ipRecord.setCount(1);
//				userService.saveObject(ipRecord, null);
//			}
//		}else{
//			log.info("注册解析ip地址失败！");
//		}
//		if (message.equals("")) {
//			// 验证码不正确
//			if (!inpcode.trim().equals(mcode.trim())) {
//				message = APIConstants.USER_TIPS_CODE_ERROR;
//				code = APIConstants.CODE_REQUEST_ERROR;
//			}
//			// else if (!password.trim().equals(repassword.trim())) {// 两次密码不同
//			// message = APIConstants.USER_TIPS_REG_NOT_PWD;
//			// code = APIConstants.CODE_REQUEST_ERROR;
//			// }
//			else {
//				loginName = loginName.trim();
//				User user = userService.getUserByLoginName(loginName);
//				if (user != null) {// 已经注册过了
//					message = APIConstants.USER_TIPS_UN_EXISTS;// 已存在
//					code = APIConstants.CODE_REQUEST_ERROR;
//				} else {
//					User regUser = new User();
//					regUser.setLoginName(loginName); 
//					
//					if (!ParamUtils.chkString(userName)) {
//						userName = loginName;
//					}
//					regUser.setUserName(userName);
//					regUser.setCellPhone(loginName);
//					regUser.setPassword(password);
//					regUser.setUserType(Constants.USER_TYPE_SUER);
//					regUser.setStatus(Constants.PUB_STATUS_OPEN);
//					regUser.setRegistDateTime(new Date());
//					regUser.setLoginTimes(1);
//					regUser.setToken(token);
//					regUser.setLastLoginDate(new Date());
//					regUser.setLastLoginIp(IPUtil.getIpAddr(request));
//					
//					if(ParamUtils.chkString(inviteCode)){
//						try {
//							Integer agentUserId = Integer.valueOf(inviteCode);
//							if(ParamUtils.chkInteger(agentUserId)){
//								User agentUser = userService.getUser(agentUserId);
//								if(agentUser != null){
//									String agentUserName = agentUser.getLoginName();
//									regUser.setAgentName(agentUserName);
//									regUser.setAgentId(agentUserId);
//								}
//							}
//						} catch (Exception e) {
//							log.info("无效代理用户id");
//						}
//					}
//					
//					List<Param> list = CacheUtil.getParam();
//					Param para = null;
//					BigDecimal money = null;//赠送金额
//					for(Param param:list){
//						if(Constants.PARAM_REGISTER_SEND.equals(param.getType())){
//							para = param;
//							break;
//						}
//					}
//					if(para != null){
//						String value = para.getValue();
//						if(ParamUtils.chkString(value)){
//							try{
//								money = new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP);
//							}catch(Exception e){
//								log.info("赠送金额错误");
//							}
//						}
//					}
//					regUser = (User) userService.saveObjectDB(regUser);
//					if(money != null){
////						StringBuffer remark=new StringBuffer();
////						remark.append("注册赠送 金额 ")
////						    .append(money.toString()).append("元");
////						userService.saveTradeDetail(regUser,regUser.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRESENT, money, null, null,remark.toString());
////						//--by.cuisy.20171209
////						userService.updateUserMoney(regUser.getUserId());
//						userService.saveUserRegSendMoney(regUser,money);
//					}
//
//					Integer userId = regUser.getUserId();
//
//					JSONObject obj = new JSONObject();
//					obj.put("uid", regUser.getUserId());
//					obj.put("gender",
//							ParamUtils.chkStringNotNull(regUser.getGender()));// 性别
//																				// 1男
//																				// 2女
//					obj.put("loginName", regUser.getLoginName());
//					obj.put("cellPhone", regUser.getCellPhone());
//					obj.put("userName",
//							ParamUtils.chkStringNotNull(regUser.getUserName()));
//					obj.put("email",
//							ParamUtils.chkStringNotNull(regUser.getUserEmail()));
//					obj.put("password", regUser.getPassword());
//					String logo = regUser.getLogo();
//					String inviteCode2 = regUser.getInvitationCode();// 邀请码
//					if (ParamUtils.chkString(inviteCode2)) {
//						obj.put("inviteCode", inviteCode2);
//					} else {
//						obj.put("inviteCode", "");
//					}
//					
//					if(DateTimeUtil.DateToString(regUser.getLastSignDate()).equals(DateTimeUtil.DateToString(new Date()))){
//						obj.put("signStatus", "1");
//					}else{
//						obj.put("signStatus", "0");
//					}
//					if (ParamUtils.chkString(logo)) {
//						obj.put("logo",
//								StringUtil.httpTohttps(request,
//										StringUtil.checkAPIHttpUrl(logo)));
//					} else {
//						obj.put("logo", "");
//					}
//					String u = Constants.DES_KEY_UID + "=" + userId + "&"
//							+ Constants.DES_KEY_LOGINNAME + "=" + loginName
//							+ "&" + Constants.DES_KEY_PASSWORD + "=" + password;
//					obj.put("u", DesUtils.encrypt(u));
//					data.put("obj", obj);
//					message = APIConstants.USER_TIPS_REG_SUCCESS;
//					code = APIConstants.CODE_REQUEST_SUCCESS;
//					request.getSession().removeAttribute(
//							APIConstants.SESSION_SMS_PHONE);
//					request.getSession().removeAttribute(
//							APIConstants.SESSION_SMS_CODE_NAME);
//				}
//			}
//		}
//		map.put("code", code);
//		map.put("msg", message);
//		map.put("data", data);
//		JsonUtil.AjaxWriter(response, map);
//	}
	
	//注册统一接口
	public void reg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		
		String loginName = ParamUtils.getParameter(deParameter, "loginName", "").trim();//手机号
		String password = ParamUtils.getParameter(deParameter, "password", "");// 密码
		String repassword = ParamUtils.getParameter(deParameter, "repassword", "");// 密码
		String inpcode = ParamUtils.getParameter(deParameter, "inpcode", "").trim();// 验证码(lengh=4图形,lengh=6短信)
		String inviteCode = ParamUtils.getParameter(deParameter, "inviteCode","");// 邀请码(可选)
		String userName = ParamUtils.getParameter(deParameter, "userName", "");//用户昵称(可选)
		String token = ParamUtils.getParameter(deParameter, "token", "");//设备token(可选)
		
		JSONObject map = new JSONObject();//最外层

		String smsPhone = (String) request.getSession().getAttribute(APIConstants.SESSION_SMS_PHONE);//短信验证
		String mcode = (String) request.getSession().getAttribute(APIConstants.SESSION_CHECK_CODE);//图形验证
		
		//注册开关
		String isReg = CacheUtil.getParamValue(Constants.PARAM_REG_SWITCH);
		if(isReg.equals(Constants.REG_STATUS_CLOSE)){
			JsonUtil.AjaxWriterError(response,map,"暂停注册");
			return;
		}
		
		log.info("[REG] loginName="+loginName);
		log.info("[REG] inpcode="+inpcode);
		log.info("[REG] mcode="+mcode);
		log.info("[REG] password="+password);
		log.info("[REG] repassword="+repassword);
		//参数检查
		if (!ParamUtils.chkString(loginName) 
				|| !ParamUtils.chkString(inpcode)
				|| !ParamUtils.chkString(mcode)
				|| !ParamUtils.chkString(password) 
				|| !ParamUtils.chkString(repassword)) {
			JsonUtil.AjaxWriterError(response,map,"无效参数");
			return;
		}
		if(!inpcode.equals(mcode)){
			JsonUtil.AjaxWriterError(response,map,"验证码错误");
			return;
		}else if(!password.trim().equals(repassword.trim())){
			JsonUtil.AjaxWriterError(response,map,"两 次密码不匹配");
			return;
		}else if(loginName.length()!=11){
			JsonUtil.AjaxWriterError(response,map,"手机号不正确");
			return;
		}

		//IP重复注册
		String ip = IPUtil.getIpAddr(request);
		IpRecord ipRecord = null;
		if(ParamUtils.chkString(ip)){
			ipRecord = userService.getIpRecordByIp(ip);
			if(ipRecord != null && ipRecord.getCount() != null){
				Integer count = ipRecord.getCount();
				Integer maxRegIpCount = 10;//CacheUtil.getParamValue(Constants.PARAM_REGISTER_MAX_IP_REG_COUNT)
				if(count>=maxRegIpCount){
					JsonUtil.AjaxWriterError(response,map,"禁止IP重复注册");
					return;
				}else{
					ipRecord.setCount(count+1);
					userService.saveObject(ipRecord, null);
				}
			}else{
				ipRecord = new IpRecord();
				ipRecord.setIp(ip);
				ipRecord.setCount(1);
				userService.saveObject(ipRecord, null);
			}
		}else{
			log.error("[REG] Error 注册解析IP地址失败");
		}

		JSONObject data = new JSONObject();//返回数据层
		
		//登录名检查
		User user = userService.getUserByLoginName(loginName);
		if (user != null) {
			JsonUtil.AjaxWriterError(response,map,"手机号已注册");
			return;
		}
		
		//注册新用户
		User regUser = new User();
		regUser.setLoginName(loginName); 
		regUser.setUserName(ParamUtils.chkString(userName)?userName:loginName);
		regUser.setCellPhone(loginName);//手机号
		regUser.setPassword(password);
		regUser.setUserType(Constants.USER_TYPE_SUER);
		regUser.setStatus(Constants.PUB_STATUS_OPEN);
		regUser.setIsBetBack(Constants.PUB_STATUS_OPEN);
		regUser.setRegistDateTime(DateTimeUtil.getJavaUtilDateNow());
		regUser.setLoginTimes(1);//注册完跳转登录1次
		if(ParamUtils.chkString(token)) regUser.setToken(token);
		regUser.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
		regUser.setLastLoginIp(ip);
		regUser.setRegIp(ip);
		regUser.setLimitBetBack(new BigDecimal(0));
		regUser.setLimitRecharge(new BigDecimal(0));
		
		// 设置用户的充值通道，默认第一个
		HQUtils hq = new HQUtils();
		hq.addHsql("from RechargeWay rw where rw.type=? ");
		hq.addPars(Constants.RECHARGE_WAY_1);
		List<Object> rwList = userService.findObjects(hq);
		if (rwList != null) {
			RechargeWay r = (RechargeWay)rwList.get(0);
			regUser.setRechargeWay(r.getId());
		}
		
		//资金方面先置0
		regUser.setMoney(new BigDecimal(0));//余额
		regUser.setUserpoints(new BigDecimal(0));//积分
		
		//代理
		if(ParamUtils.isNumeric(inviteCode)){//有效邀请码
			Integer agentUserId = Integer.valueOf(inviteCode);
			if(ParamUtils.chkInteger(agentUserId)){
				User agentUser = userService.getUser(agentUserId);
				if(agentUser != null){
					String agentUserName = agentUser.getLoginName();
					regUser.setAgentId(agentUserId);
					regUser.setAgentName(agentUserName);
				}else{
					log.error("[REG] ["+loginName+"] 无效代理用户="+agentUserId);
				}
			}else{
				log.error("[REG] ["+loginName+"] 无效邀请码="+inviteCode);
			}
		}
		
		userService.saveObject(regUser,null);//保存以生成userId
		
		//注册赠送金额
//		String regSendStr = CacheUtil.getParamValue(Constants.PARAM_REGISTER_SEND);
//		BigDecimal regSendMoney = new BigDecimal(0);
//		if(ParamUtils.isNumber(regSendStr)){
//			regSendMoney = new BigDecimal(regSendStr).setScale(2, BigDecimal.ROUND_HALF_UP);
//			if(regSendMoney.doubleValue()>0){
//				userService.saveUserRegSendMoney(regUser, regSendMoney);
//			}
//		}else{
//			log.fatal("[REG] 未设置注册赠送金额 ["+loginName+"]");
//		}
		
		Integer userId = regUser.getUserId();//生成的userId

		JSONObject obj = new JSONObject();
		obj.put("uid", regUser.getUserId());
		obj.put("gender",ParamUtils.chkStringNotNull(regUser.getGender()));// 性别// 1男 // 2女
		//签到
		obj.put("signStatus", DateTimeUtil.checkSameDay(regUser.getLastSignDate(),DateTimeUtil.getJavaSQLDateNow())?"1":"0");
		obj.put("loginName", regUser.getLoginName());
		obj.put("cellPhone", regUser.getCellPhone());
		obj.put("userName",ParamUtils.chkStringNotNull(regUser.getUserName()));
		obj.put("email", ParamUtils.chkStringNotNull(regUser.getUserEmail()));
		obj.put("password", regUser.getPassword());
		obj.put("userType", regUser.getUserType());
		obj.put("money", regUser.getMoney());
		
		obj.put("inviteCode", ParamUtils.chkStringNotNull(regUser.getInvitationCode()));
		obj.put("log", ParamUtils.chkStringNotNull(StringUtil.httpTohttps(request,StringUtil.checkAPIHttpUrl(regUser.getLogo()))));
		
		//用户key
		StringBuffer uKeys = new StringBuffer();
		uKeys.append(Constants.DES_KEY_UID+"="+userId);
		uKeys.append("&"+Constants.DES_KEY_LOGINNAME+"="+loginName);
		uKeys.append("&"+Constants.DES_KEY_PASSWORD+"="+password);
		obj.put("u", DesUtils.encrypt(uKeys.toString()));
		
		data.put("obj", obj);
		
		//清除验证码
		request.getSession().removeAttribute(APIConstants.SESSION_SMS_PHONE);
		request.getSession().removeAttribute(APIConstants.SESSION_SMS_CODE_NAME);
		
		map.put("data", data);
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		map.put("msg", APIConstants.USER_TIPS_REG_SUCCESS);
		JsonUtil.AjaxWriter(response, map);
	}
	
	
	
//	public void reg(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		Map<String, String> deParameter = ParamUtils.deParameter(request);
//		String loginName = ParamUtils
//				.getParameter(deParameter, "loginName", "");
//		String inpcode = ParamUtils.getParameter(deParameter, "inpcode", "");// 用户输入的验证码
//		// String mcode = ParamUtils.getParameter(deParameter, "mcode", "");//
//		// 短信验证码
//		String password = ParamUtils.getParameter(deParameter, "password", "");// 密码
//		String userName = ParamUtils.getParameter(deParameter, "userName", "");// 用户昵称
//		String inviteCode = ParamUtils.getParameter(deParameter, "inviteCode",
//				"");// 邀请码
//
//		String token = ParamUtils.getParameter(deParameter, "token", "");
//		String message = "";
//
//		String smsPhone = (String) request.getSession().getAttribute(
//				APIConstants.SESSION_SMS_PHONE);
////		String mcode = (String) request.getSession().getAttribute(
////				APIConstants.SESSION_SMS_CODE_NAME);
//		String mcode=(String) request.getSession().getAttribute("chkCode");
//		
////		Map<String, String> blacklist = Constants.getBlacklist();
////		String black = blacklist.get(inviteCode);
////		if (ParamUtils.chkString(inviteCode) && inviteCode.equals(black)) {
////			request.getSession()
////					.removeAttribute(APIConstants.SESSION_SMS_PHONE);
////			request.getSession().removeAttribute(
////					APIConstants.SESSION_SMS_CODE_NAME);
////			message = "验证码不正确";
////		}
//
////		String ip = IPUtil.getIpAddr(request);
////		if(ParamUtils.chkString(ip)){
////			IpRecord ipRecord = userService.getIpRecordByIp(ip);
////			if(ipRecord != null && ipRecord.getCount() != null){
////				Integer count = ipRecord.getCount();
////				if(count >=3){
////					message="同一ip注册过多，禁止注册";
////				}else{
////					ipRecord.setCount(count+1);
////					userService.saveObject(ipRecord, null);
////				}
////			}else{
////				ipRecord = new IpRecord();
////				ipRecord.setIp(ip);
////				ipRecord.setCount(1);
////				userService.saveObject(ipRecord, null);
////			}
////		}else{
////			log.info("注册解析ip地址失败！");
////		}
//
//		log.error("M___________________inpcode>" + inpcode + "___sdCode>"
//				+ mcode + "___loginName>" + loginName + "___smsPhone>"
//				+ smsPhone + "___date>[" + DateTimeUtil.getDateTime()
//				+ "]rec>>>>[user_reg]");
//		if (!ParamUtils.chkString(loginName) || !ParamUtils.chkString(inpcode)
//				|| !ParamUtils.chkString(mcode)
//				|| !ParamUtils.chkString(password)) {
//			message = APIConstants.PARAMS_EMPTY_MSG;
//		}
//		JSONObject map = new JSONObject();// 最外层
//		JSONObject data = new JSONObject();// 返回数据层
//		String code = APIConstants.CODE_REQUEST_ERROR;
////		if (!loginName.equals(smsPhone)) {
////			message = APIConstants.USER_TIPS_CODE_ERROR;
////			code = APIConstants.CODE_REQUEST_ERROR;
////		}
//
//		if (message.equals("")) {
//			// 验证码不正确
//			if (!inpcode.trim().equals(mcode.trim())) {
//				message = APIConstants.USER_TIPS_CODE_ERROR;
//				code = APIConstants.CODE_REQUEST_ERROR;
//			}
//			// else if (!password.trim().equals(repassword.trim())) {// 两次密码不同
//			// message = APIConstants.USER_TIPS_REG_NOT_PWD;
//			// code = APIConstants.CODE_REQUEST_ERROR;
//			// }
//			else {
//				loginName = loginName.trim();
//				User user = userService.getUserByLoginName(loginName);
//				if (user != null) {// 已经注册过了
//					message = APIConstants.USER_TIPS_UN_EXISTS;// 已存在
//					code = APIConstants.CODE_REQUEST_ERROR;
//				} else {
//					User regUser = new User();
//					regUser.setLoginName(loginName); 
//					if (!ParamUtils.chkString(userName)) {
//						userName = loginName;
//					}
//					regUser.setUserName(userName);
//					regUser.setCellPhone(loginName);
//					regUser.setPassword(password);
//					regUser.setUserType(Constants.USER_TYPE_SUER);
//					regUser.setStatus(Constants.PUB_STATUS_OPEN);
//					regUser.setRegistDateTime(new Date());
//					regUser.setLoginTimes(0);
//					regUser.setToken(token);
//					
//					if(ParamUtils.chkString(inviteCode)){
//						try {
//							Integer agentUserId = Integer.valueOf(inviteCode);
//							if(ParamUtils.chkInteger(agentUserId)){
//								User agentUser = userService.getUser(agentUserId);
//								if(agentUser != null){
//									String agentUserName = agentUser.getLoginName();
//									regUser.setAgentName(agentUserName);
//									regUser.setAgentId(agentUserId);
//								}	
//							}
//						} catch (Exception e) {
//							log.info("无效代理用户id");
//						}
//					}
//					
//					List<Param> list = CacheUtil.getParam();
//					Param para = null;
//					BigDecimal money = null;//赠送金额
//					for(Param param:list){
//						if(Constants.PARAM_REGISTER_SEND.equals(param.getType())){
//							para = param;
//							break;
//						}
//					}
//					if(para != null){
//						String value = para.getValue();
//						if(ParamUtils.chkString(value)){
//							try{
//								money = new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP);
//							}catch(Exception e){
//								log.info("赠送金额错误");
//							}
//						}
//					}
//					regUser = (User) userService.saveObjectDB(regUser);
//					if(money != null){
//						userService.saveUserRegSendMoney(regUser, money);
//					}
//
//					Integer userId = regUser.getUserId();
//
//					JSONObject obj = new JSONObject();
//					obj.put("uid", regUser.getUserId());
//					obj.put("gender",
//							ParamUtils.chkStringNotNull(regUser.getGender()));// 性别
//																				// 1男
//																				// 2女
//					obj.put("loginName", regUser.getLoginName());
//					obj.put("cellPhone", regUser.getCellPhone());
//					obj.put("userName",
//							ParamUtils.chkStringNotNull(regUser.getUserName()));
//					obj.put("email",
//							ParamUtils.chkStringNotNull(regUser.getUserEmail()));
//					obj.put("password", regUser.getPassword());
//					String logo = regUser.getLogo();
//					String inviteCode2 = regUser.getInvitationCode();// 邀请码
//					if (ParamUtils.chkString(inviteCode2)) {
//						obj.put("inviteCode", inviteCode2);
//					} else {
//						obj.put("inviteCode", "");
//					}
//					
//					if(DateTimeUtil.DateToString(regUser.getLastSignDate()).equals(DateTimeUtil.DateToString(new Date()))){
//						obj.put("signStatus", "1");
//					}else{
//						obj.put("signStatus", "0");
//					}
//					
//					if (ParamUtils.chkString(logo)) {
//						obj.put("logo",
//								StringUtil.httpTohttps(request,
//										StringUtil.checkAPIHttpUrl(logo)));
//					} else {
//						obj.put("logo", "");
//					}
//					String u = Constants.DES_KEY_UID + "=" + userId + "&"
//							+ Constants.DES_KEY_LOGINNAME + "=" + loginName
//							+ "&" + Constants.DES_KEY_PASSWORD + "=" + password;
//					obj.put("u", DesUtils.encrypt(u));
//					data.put("obj", obj);
//					message = APIConstants.USER_TIPS_REG_SUCCESS;
//					code = APIConstants.CODE_REQUEST_SUCCESS;
//					request.getSession().removeAttribute(
//							APIConstants.SESSION_SMS_PHONE);
//					request.getSession().removeAttribute(
//							APIConstants.SESSION_SMS_CODE_NAME);
//				}
//			}
//		}
//		map.put("code", code);
//		map.put("msg", message);
//		map.put("data", data);
//		JsonUtil.AjaxWriter(response, map);
//	}

	
	/**
	 * 注册试玩帐号
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void guestReg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			HQUtils hq = new HQUtils();
			hq.addHsql("from User u where u.userType=? ");
			hq.addPars(Constants.USER_TYPE_TEST);
			List<Object> rwList = userService.findObjects(hq);
			
			User user = null;
			if (rwList != null && rwList.size() > 0) {
				Random ran = new Random();
				int result = ran.nextInt(rwList.size());
				user = (User) rwList.get(result);
			}
			if (user != null) {
				JSONObject obj = new JSONObject();
				obj.put("loginName", user.getLoginName());
				obj.put("password", "123456");
				obj.put("uid", user.getUserId());
				obj.put("cellPhone", user.getCellPhone());
				obj.put("userName", user.getUserName());
				obj.put("userType", user.getUserType());
				BigDecimal money = user.getMoney();
				if(money==null){
					money=new BigDecimal(0);
				}
				BigDecimal userpoints = user.getUserpoints();
				if(userpoints==null){
					userpoints=new BigDecimal(0);
				}
				obj.put("money", ProductUtil.BigFormatJud(money));
				obj.put("point", ProductUtil.BigFormatJud(userpoints));
				String logo = user.getLogo();
				if (ParamUtils.chkString(logo)) {
					obj.put("logo", StringUtil.checkAPIHttpUrl(logo));
				} else {
					obj.put("logo", "");
				}
				String u = Constants.DES_KEY_UID + "=" + user.getUserId() + "&"
						+ Constants.DES_KEY_LOGINNAME + "=" + user.getLoginName() + "&"
						+ Constants.DES_KEY_PASSWORD + "=" + user.getPassword();
				obj.put("u", DesUtils.encrypt(u));
				data.put("obj", obj);
				message = APIConstants.USER_TIPS_REG_SUCCESS;
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				message = "暂时不能免费试玩";
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 是否签到
	 */
	public void signCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u", "");
		String message = "";
		if (!ParamUtils.chkString(u) ) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
				if(DateTimeUtil.DateToString(user.getLastSignDate()).equals(DateTimeUtil.DateToString(new Date()))){
					data.put("signStatus", "1");
				}else{
					data.put("signStatus", "0");
				}
				message = "";
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				message = APIConstants.TIPS_NOT_USER;
				code = APIConstants.CODE_REQUEST_ERROR;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	
	
	/**
	 * 登录
	 */
	public void login(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String loginName = ParamUtils
				.getParameter(deParameter, "loginName", "");
		String password = ParamUtils.getParameter(deParameter, "password", "");
		String machineType = ParamUtils.getParameter(deParameter,
				"machineType", "");// 设备类型 1是安卓 2是IOS
		String token = ParamUtils.getParameter(deParameter, "token", "");
		// String longAlt = ParamUtils.getParameter(deParameter, "longAlt",
		// "");//
		// 经纬度

		String message = "";
		if (!ParamUtils.chkString(loginName) || !ParamUtils.chkString(password)
		// || !ParamUtils.chkString(token)
		) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			User user = userService.getUser(loginName, password);
			if (user != null) {// 登录
				//生成一个session
				String session = SessionUtil.random();
				
				String oldToken = user.getToken();// 原设备码
				String oldMachineType = user.getMachineType();
				Integer userId = user.getUserId();
				user.setMachineType(machineType);
				if (ParamUtils.chkString(token))
					user.setToken(token);
				// if (ParamUtils.chkString(longAlt)) {
				// user.setLongAlt(longAlt);
				// }
				user.setLastLoginDate(new Date());
				Integer loginTimes = ParamUtils
						.chkInteger(user.getLoginTimes()) ? user
						.getLoginTimes() : 0;
				user.setLoginTimes(loginTimes + 1);
				user.setLastLoginIp(IPUtil.getIpAddr(request));
				user.setSession(session);
				userService.saveUser(user);
				JSONObject obj = new JSONObject();
				obj.put("uid", user.getUserId());
				obj.put("gender", ParamUtils.chkStringNotNull(user.getGender()));// 性别 1男 2女
				obj.put("loginName", user.getLoginName());
				obj.put("cellPhone", user.getCellPhone());
				obj.put("userName", user.getUserName());
				obj.put("money", ProductUtil.BigFormat(user.getMoney()));
				obj.put("email",
						ParamUtils.chkStringNotNull(user.getUserEmail()));
				obj.put("password", user.getPassword());
				obj.put("userType", user.getUserType());
				String inviteCode = user.getInvitationCode();// 邀请码
				if (ParamUtils.chkString(inviteCode)) {
					obj.put("inviteCode", inviteCode);
				} else {
					obj.put("inviteCode", "");
				}

				if(user.getUserType().equals(Constants.USER_TYPE_AGENT_ONE)||user.getUserType().equals(Constants.USER_TYPE_AGENT_TWO)
						||user.getUserType().equals(Constants.USER_TYPE_AGENT_THREE)){
					obj.put("isAgent", "1");
				}else{
					obj.put("isAgent", "0");
				}
				
				if(DateTimeUtil.DateToString(user.getLastSignDate()).equals(DateTimeUtil.DateToString(new Date()))){
					obj.put("signStatus", "1");
				}else{
					obj.put("signStatus", "0");
				}
	
				String logo = user.getLogo();
				if (ParamUtils.chkString(logo)) {
					obj.put("logo", StringUtil.checkAPIHttpUrl(logo));
				} else {
					obj.put("logo", "");
				}
				String u = Constants.DES_KEY_UID + "=" + userId + "&"
						+ Constants.DES_KEY_LOGINNAME + "=" + loginName + "&"
						+ Constants.DES_KEY_PASSWORD + "=" + password;
				obj.put("u", DesUtils.encrypt(u));

				data.put("obj", obj);
				message = "";
				code = APIConstants.CODE_REQUEST_SUCCESS;
				
				//log.info("[token]:" + token + " [oldToken]:" + oldToken);
				if (ParamUtils.chkString(token)
						&& ParamUtils.chkString(oldToken)) {
					if (!oldToken.equals(token)) {// 设备码不同
						// 推送
						PushBean pushBean = new PushBean();
						pushBean.setType(JPushUtil.TYPE_SIGN_OUT);
						pushBean.setTitle(APIConstants.PUSH_TIPS_ACCOUNT_LOGIN);
						String description = APIConstants.PUSH_TIPS_SIGN_OUT;
						description = description.replace("{date}",
								DateTimeUtil.getDateTime());
						pushBean.setDescription(description);
						try {
							String [] tokenArray = new String [1];
							tokenArray[0]=oldToken;
							JPushUtil.pushOneDevice(pushBean, oldMachineType,
									tokenArray);
						} catch (Exception e) {
						}
					}
				}
			} else {
				message = APIConstants.LOGIN_TIPS_NOT;
				code = APIConstants.CODE_REQUEST_ERROR;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 保存用户资料
	 */
	public void save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u2 = ParamUtils.getParameter(deParameter, "u", "");
		String userName = ParamUtils.getParameter(deParameter, "userName", "");// 昵称
		String email = ParamUtils.getParameter(deParameter, "email", "");// 邮箱
		String logo = ParamUtils.getParameter(deParameter, "logo", "");// 头像
		String realName = ParamUtils.getParameter(deParameter, "realName", "");// 真实姓名
		String birthday = ParamUtils.getParameter(deParameter, "birthday", "");// 生日
		String gender = ParamUtils.getParameter(deParameter, "gender", "");// 性别
		String cellPhone = ParamUtils.getParameter(deParameter, "cellPhone", "");// 电话
		String qq = ParamUtils.getParameter(deParameter, "qq", "");// qq
		String address = ParamUtils.getParameter(deParameter, "address", "");// 地址

		String message = "";
		if (!ParamUtils.chkString(u2) || !ParamUtils.chkString(email)
				|| !ParamUtils.chkString(realName) || !ParamUtils.chkString(birthday)
				|| !ParamUtils.chkString(gender) || !ParamUtils.chkString(cellPhone)
				|| !ParamUtils.chkString(qq) || !ParamUtils.chkString(address)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u2);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}

			if (user != null) {// 登录
				String loginName = user.getLoginName();
				String password = user.getPassword();
				if (!ParamUtils.chkString(userName)) {
					userName = loginName;
				}
				user.setUserName(userName);
				if (ParamUtils.chkString(logo)) {
					user.setLogo(StringUtil.formatImgHttpUrlToRelative(logo));
				}
				if (ParamUtils.chkString(email)) {
					user.setUserEmail(email);
				}
				if (ParamUtils.chkString(realName)) {
					user.setRealName(realName);
				}
				if (ParamUtils.chkString(birthday)) {
					user.setBirthday(DateTimeUtil.stringToDate(birthday, "yyyy-MM-dd"));
				}
				if (ParamUtils.chkString(gender)) {
					user.setGender(gender);
				}
				if (ParamUtils.chkString(cellPhone)) {
					user.setCellPhone(cellPhone);
				}
				if (ParamUtils.chkString(qq)) {
					user.setQq(qq);
				}
				if (ParamUtils.chkString(address)) {
					user.setAddress(address);
				}
				userService.saveUser(user);
				JSONObject obj = new JSONObject();
				Integer userId = user.getUserId();
				obj.put("uid", userId);
				obj.put("password", user.getPassword());//
				obj.put("gender", ParamUtils.chkStringNotNull(user.getGender()));// 性别
																					// 1男
																					// 2女
				obj.put("loginName", user.getLoginName());
				obj.put("cellPhone", user.getCellPhone());
				obj.put("userName", user.getUserName());
				obj.put("email",
						ParamUtils.chkStringNotNull(user.getUserEmail()));
				String inviteCode = user.getInvitationCode();// 邀请码
				if (ParamUtils.chkString(inviteCode)) {
					obj.put("inviteCode", inviteCode);
				} else {
					obj.put("inviteCode", "");
				}
				if (ParamUtils.chkString(logo)) {
					obj.put("logo", StringUtil.checkAPIHttpUrl(logo));
				} else {
					obj.put("logo", "");
				}
				String u = Constants.DES_KEY_UID + "=" + userId + "&"
						+ Constants.DES_KEY_LOGINNAME + "=" + loginName + "&"
						+ Constants.DES_KEY_PASSWORD + "=" + password;
				obj.put("u", DesUtils.encrypt(u));
				data.put("obj", obj);
				message = "保存成功";
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				message = APIConstants.TIPS_SERVER_ERROR;
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 修改密码
	 */
	public void changePwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u2 = ParamUtils.getParameter(deParameter, "u", "");
		String newpassword = ParamUtils.getParameter(deParameter,
				"newpassword", "");// 新密码
		String repassword = ParamUtils.getParameter(deParameter, "repassword",
				"");// 重复密码
		String oldpassword = ParamUtils.getParameter(deParameter,
				"oldpassword", "");// 原密码

		String message = "";
		if (!ParamUtils.chkString(u2) || !ParamUtils.chkString(newpassword)
				|| !ParamUtils.chkString(oldpassword)
				|| !ParamUtils.chkString(repassword)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;

		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u2);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {// 登录
				if (!Constants.USER_TYPE_TEST.equals(user.getUserType())) {
					if (oldpassword.equals(user.getPassword())) {
						newpassword = newpassword.trim();
						repassword = repassword.trim();
						if (repassword.equals(newpassword)) {
							newpassword = newpassword.trim();
							repassword = repassword.trim();
							if (repassword.equals(newpassword)) {
								user.setPassword(newpassword);
								userService.saveUser(user);
//							EasemobUserUtil.modifyPassword(user);
								
//							String logo = user.getLogo();
								String password = user.getPassword();
								String loginName = user.getLoginName();
								
								JSONObject obj = new JSONObject();
								Integer userId = user.getUserId();
								obj.put("uid", userId);
								obj.put("gender", ParamUtils.chkStringNotNull(user
										.getGender()));// 性别 1男 2女
								obj.put("loginName", user.getLoginName());
								obj.put("password", user.getPassword());
								obj.put("cellPhone", user.getCellPhone());
								obj.put("userName", user.getUserName());
								obj.put("email", ParamUtils.chkStringNotNull(user
										.getUserEmail()));
								String inviteCode = user.getInvitationCode();// 邀请码
								if (ParamUtils.chkString(inviteCode)) {
									obj.put("inviteCode", inviteCode);
								} else {
									obj.put("inviteCode", "");
								}
								if (ParamUtils.chkString(user.getLogo())) {
									obj.put("logo", StringUtil.httpTohttps(request,
											StringUtil.httpTohttps(request,
													StringUtil.checkAPIHttpUrl(user
															.getLogo()))));
								} else {
									obj.put("logo", "");
								}
								String u = Constants.DES_KEY_UID + "=" + userId
										+ "&" + Constants.DES_KEY_LOGINNAME + "="
										+ loginName + "&"
										+ Constants.DES_KEY_PASSWORD + "="
										+ password;
								obj.put("u", DesUtils.encrypt(u));
//							obj.put("shoplink", Constants.getDomainName()+"/help/seller.html?key="+DesUtils.encrypt(u));
//							obj.put("easemobLoginName", EasemobUserUtil.getEasemobUserLoginName(user));
								data.put("obj", obj);
								code = APIConstants.CODE_REQUEST_SUCCESS;
								message="修改成功";
							} else {
								code = APIConstants.CODE_REQUEST_ERROR;
								message = APIConstants.USER_TIPS_REG_NOT_PWD;
							}
						} else {
							code = APIConstants.CODE_REQUEST_ERROR;
							message = APIConstants.USER_TIPS_REG_NOT_PWD;
						}
					} else {
						code = APIConstants.CODE_REQUEST_ERROR;
						message = "原密码不正确";
					}
				} else {
					message = "试玩用户不能修改";
					code = APIConstants.CODE_NOT_FOUND;
				}
			} else {
				message = APIConstants.TIPS_SERVER_ERROR;
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	public void cashPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u2 = ParamUtils.getParameter(deParameter, "u", "");
		String cashPassword = ParamUtils.getParameter(deParameter, "cashPassword",
				"");// 新密码
		String recashPassword = ParamUtils.getParameter(deParameter,
				"recashPassword", "");// 重复密码
		String inpcode = ParamUtils.getParameter(deParameter, "inpcode", "");// 用户输入的验证码

		String password = ParamUtils.getParameter(deParameter, "password", "");// 用户输入的验证码
		String message = "";
		if (!ParamUtils.chkString(u2) || !ParamUtils.chkString(recashPassword)
				|| !ParamUtils.chkString(cashPassword)
				|| !ParamUtils.chkString(password)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;

		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u2);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {// 登录
				if (!Constants.USER_TYPE_TEST.equals(user.getUserType())) {
					if (cashPassword.equals(recashPassword)) {
						if (!user.getPassword().equals(password)) {
							code = APIConstants.CODE_REQUEST_ERROR;
							message = "输入的登录密码有误";
						} else if (recashPassword.equals(user.getCashPassword())) {
							code = APIConstants.CODE_REQUEST_ERROR;
							message = "与原提款密码相同，请重新输入";
						}
						// String mcode = (String)
						// request.getSession().getAttribute(
						// APIConstants.SESSION_SMS_CODE_NAME);
						// if(!inpcode.equals(mcode)){
						// code = APIConstants.CODE_REQUEST_ERROR;
						// message = "短信验证码不正确";
						// }
						if (message.equals("")) {
							user.setCashPassword(recashPassword);
							userService.saveUser(user);
							code = APIConstants.CODE_REQUEST_SUCCESS;
							message="设置成功";
						}
					} else {
						code = APIConstants.CODE_REQUEST_ERROR;
						message = "两次提现密码不一致";
					}
				} else {
					message = "试玩用户不能修改";
					code = APIConstants.CODE_NOT_FOUND;
				}
			} else {
				message = APIConstants.TIPS_SERVER_ERROR;
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}	
	
	/**
	 * 上传头像
	 */
	public void head(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer uid = ParamUtils.getIntegerParameter(request, "uid");
		String loginName = ParamUtils.getParameter(request, "loginName", "");

		// 头像的key是 head
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		// 保存文件
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);
		ServletFileUpload upload = new ServletFileUpload(factory);
		FileItem file = null;
		try {
			List<FileItem> fitems = upload.parseRequest(request);
			for (FileItem fi : fitems) {
				String field = fi.getFieldName();
				String val = fi.getString();
				if (!fi.isFormField()) {// 上传文件
					file = fi;
				} else {
					if (ParamUtils.chkString(val)) {// 从FileItem取参数
						if (field.equals("uid")) {
							uid = Integer.valueOf(val);
						} else if (field.equals("loginName")) {
							loginName = val;
						}
					}
				}
			}
		} catch (Exception e) {
			code = APIConstants.CODE_REQUEST_ERROR;
			message = APIConstants.TIPS_SERVER_ERROR;
		}

//		if (!ParamUtils.chkInteger(uid) || !ParamUtils.chkString(loginName)) {
//			message = APIConstants.PARAMS_EMPTY_MSG;
//		}

		if (message.equals("")) {
			try {
				int maxPostSize = 100 * 1024 * 1024;
				String uploadPath = Constants.getWebRootPath();
				String uploadFolder = Constants.getFileUploadPath();

				upload.setSizeMax(maxPostSize);

				if (file != null && file.getSize() > 0) {
					String fname = file.getName();
					String fileExt = UploadUtil.getFileExt(fname);
					String getUniqueID = null;
					// if (ParamUtils.chkString(loginName)) {
					// getUniqueID = loginName;
					// } else {
					getUniqueID = UploadUtil.GetUniqueID();
					// }
					fname = getUniqueID + fileExt;// 文件名
					String folder = UploadUtil.getFolder2("/head");
					uploadFolder = uploadFolder + folder;
					String upPath = uploadPath + uploadFolder;
					new FSO().createFolder(upPath);
					try {
						File saveFile = new File(upPath + "/" + fname);
						file.write(saveFile);
						data.put(
								"logo",
								StringUtil.httpTohttps(request,
										Constants.getDomainName())
										+ uploadFolder + "/" + fname);
						data.put("uid", uid);
						code = APIConstants.CODE_REQUEST_SUCCESS;
					} catch (Exception e) {
						code = APIConstants.CODE_REQUEST_ERROR;
						message = APIConstants.TIPS_SERVER_ERROR;
						e.printStackTrace();
					}
				} else {
					code = APIConstants.CODE_REQUEST_ERROR;
					message = APIConstants.FILE_TIP_NOT_FILE;
				}

			} catch (Exception e) {
				code = APIConstants.CODE_REQUEST_ERROR;
				message = APIConstants.TIPS_SERVER_ERROR;
				e.printStackTrace();
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 找回密码
	 */
	public void retrievePwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String loginName = ParamUtils.getParameter(deParameter, "loginName", "");
		String inpcode = ParamUtils.getParameter(deParameter, "inpcode", "");// 用户输入的验证码
		// String mcode = ParamUtils.getParameter(deParameter, "mcode", "");// 短信验证码
		String password = ParamUtils.getParameter(deParameter, "password", "");// 密码
		String message = "";
		if (!ParamUtils.chkString(loginName) || !ParamUtils.chkString(inpcode)
		// || !ParamUtils.chkString(mcode)
				|| !ParamUtils.chkString(password)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		String mcode = (String) request.getSession().getAttribute(
				APIConstants.SESSION_SMS_CODE_NAME);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			if (!inpcode.trim().equals(mcode.trim())) {
				message = APIConstants.USER_TIPS_CODE_ERROR;
				code = APIConstants.CODE_REQUEST_ERROR;
			} else {
				loginName = loginName.trim();
				User user = userService.getUserByLoginName(loginName);
				if (user != null) {// 存在
					user.setPassword(password);
					userService.saveUser(user);
					message = APIConstants.TIPS_MODIFY;//
					code = APIConstants.CODE_REQUEST_SUCCESS;
				} else {
					message = APIConstants.TIPS_NOT_USER;//
					code = APIConstants.CODE_REQUEST_ERROR;
				}
			}
		}

		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 退出
	 */
	public void logout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String loginName = ParamUtils.getParameter(deParameter, "loginName", "");
		String password = ParamUtils.getParameter(deParameter, "password", "");// 密码
		String u=ParamUtils.getParameter(deParameter, "u", "");// 密码
		String message = "";
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
//			User user = userService.getUser(loginName, password);
//			if (user != null) {
//				user.setToken("");
//				userService.saveUser(user);
//			}
			HttpSession session = (HttpSession) request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			message = APIConstants.USER_TIPS_LOGOUT;//
			
//			message="禁止登出";
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 推荐受益
	 */
	public void recRevenue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u=ParamUtils.getParameter(deParameter, "u", "");// 密码
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// RamConstants.MAXPAGEITEMS

		String message = "";
		 if (!ParamUtils.chkString(u)) {
			 message = APIConstants.PARAMS_EMPTY_MSG;
		 }
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				int pageNum = 0;
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				hqls.append(" and ho.userId=? ");
				para.add(uid);
				hqls.append(" and (ho.cashType=? or   ho.cashType=? )");
				para.add(Constants.CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN);
				para.add(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN);
				hqls.append(" and ho.status = ? ");
				para.add(Constants.PUB_STATUS_OPEN);
				PaginationSupport ps=cashService.findUserTradeDetailList(hqls.toString(), para, statIndex, pageSize);
				List<UserTradeDetail> list=ps.getItems();
				JSONArray items = new JSONArray();
				if (list != null & list.size() > 0) {
					pageNum = list.size();
					for (int i = 0; i < list.size(); i++) {
						JSONObject obj = new JSONObject();
						UserTradeDetail detail = list.get(i);
						obj.put("createTime", DateTimeUtil.DateToStringAll(detail.getCreateTime()));
						obj.put("remark", detail.getRemark());
						obj.put("money", ProductUtil.BigFormat(detail.getCashMoney()));
						obj.put("tradeContent", Constants.getCashTradeTypeNameZh(detail.getCashType()));
						items.put(obj);
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
				}else{
					code = APIConstants.CODE_NOT_FOUND;
				}
				if(user.getRecRevenue()!=null){
					data.put("recRevenue", ProductUtil.BigFormat(user.getRecRevenue()));
				}else{
					data.put("recRevenue", "0");
				}
				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
				message = "操作成功";
			}else{
				code = APIConstants.CODE_REQUEST_ERROR;
				message = APIConstants.TIPS_NOT_USER;//
			}
	
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 个人信息
	 */
	public void userInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u", "");
		
		String message = "";
		if (!ParamUtils.chkString(u) ) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer.valueOf(decryptMap
					.get(Constants.DES_KEY_UID));
			User user = userService.getUser(uid);
			if (user != null) {// 登录

				Integer userId = user.getUserId();
				JSONObject obj = new JSONObject();
				obj.put("uid", user.getUserId());
				obj.put("gender", ParamUtils.chkStringNotNull(user.getGender()));// 性别 1男 2女
				obj.put("loginName", ParamUtils.chkStringNotNull(user.getLoginName()));
				obj.put("cellPhone", ParamUtils.chkStringNotNull(user.getCellPhone()));
				obj.put("userName", ParamUtils.chkStringNotNull(user.getUserName()));
				obj.put("email", ParamUtils.chkStringNotNull(user.getUserEmail()));
				obj.put("password", ParamUtils.chkStringNotNull(user.getPassword()));
				
				obj.put("realName", ParamUtils.chkStringNotNull(user.getRealName()));
				if(user.getBirthday()!=null){
					obj.put("birthday", ParamUtils.chkStringNotNull(DateTimeUtil.DateToString(user.getBirthday())));
				}else{
					obj.put("birthday", "");
				}				
				obj.put("qq", ParamUtils.chkStringNotNull(user.getQq()));
				obj.put("address", ParamUtils.chkStringNotNull(user.getAddress()));
			    obj.put("logo", "");
			    List<UserBankBind> list=cashService.findBankBindListByUid(uid);
			    if(list!=null&&list.size()>0){
			    	UserBankBind bank=list.get(0);
					obj.put("bindAccount", ParamUtils.chkStringNotNull(bank.getBindAccount()));
					obj.put("bankName", ParamUtils.chkStringNotNull(bank.getBankName()));
					obj.put("bindName", ParamUtils.chkStringNotNull(bank.getBindName()));
					obj.put("branch", ParamUtils.chkStringNotNull(bank.getBranch()));			
				    obj.put("telephone", ParamUtils.chkStringNotNull(bank.getCellPhone()));
			    }else{
			    	obj.put("bindAccount", "");
					obj.put("bankName", "");
					obj.put("bindName", "");
					obj.put("branch", "");
				    obj.put("telephone", "");
			    }
			    BigDecimal money = user.getMoney(); // 用户余额
			    BigDecimal userpoints = user.getUserpoints();// 用户积分
			    BigDecimal aggregateBetMoney = user.getAggregateBetMoney();// 累计投注金额
			    if (money != null) {
			    	obj.put("money", ProductUtil.BigFormatJud(money));
				} else {
					obj.put("money", "0.00");
				}
			    if (userpoints != null) {
			    	obj.put("userpoints", ProductUtil.BigFormat2(userpoints));// 去掉小数
				} else {
					obj.put("userpoints", "0");
				}
			    if (aggregateBetMoney != null) {
			    	obj.put("aggregateBetMoney",
							ProductUtil.BigFormatJud(aggregateBetMoney));
				} else {
					obj.put("aggregateBetMoney", "0.00");
				}
				String u1 = Constants.DES_KEY_UID + "=" + userId + "&"
						+ Constants.DES_KEY_LOGINNAME + "=" + user.getLoginName() + "&"
						+ Constants.DES_KEY_PASSWORD + "=" + user.getPassword();
				obj.put("u", DesUtils.encrypt(u1));
				data.put("obj", obj);
				message = "";
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				message = APIConstants.LOGIN_TIPS_NOT;
				code = APIConstants.CODE_REQUEST_ERROR;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 英雄榜
	 */
	public void heroicList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量
		int pageNum = 0;// 当前页数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		hqls.append(" order by u.addUpRechargeMoney desc");
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		PaginationSupport ps = userService.findHeroicList(hqls.toString(),
				para, statIndex, pageSize);
		List<User> list = ps.getItems();
		JSONArray items = new JSONArray();
		List<UserLevel> levelList = baseDataService.findUserLevel();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			for (int i = 0; i < list.size(); i++) {
				User user = list.get(i);
				JSONObject it = new JSONObject();
				it.put("userId", user.getUserId());
				BigDecimal userPoints = user.getUserpoints();
				it.put("userPoints", userPoints);
				it.put("logo", ParamUtils.chkStringNotNull(StringUtil
						.checkAPIHttpUrl(user.getLogo())));
				Integer level = 0;// 用户等级
				if (levelList != null) {// 根据用户积分判断用户是哪个等级的
					JSONObject ulObject = null;
					boolean isNext = false;
					for (int j=0;j < levelList.size();j++) {
						UserLevel userLevel = levelList.get(j);
						BigDecimal point = userLevel.getPoint();
						String lev = userLevel.getLevel();
						if (userPoints.compareTo(point) >= 0) {
							isNext = true;
							if (levelList.size()-1 == j) {
								level = Integer.valueOf(lev);
							}
						} else {
							if (isNext) {
								level = Integer.valueOf(lev)-1;
							}
							isNext = false;
						}
					}
				}
				it.put("level", level);
				items.put(it);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			message = "操作成功";
		} else {
			code = APIConstants.CODE_NOT_FOUND;
			message = "没有更多了";
		}
		data.put("items", items);
		data.put("total", ps.getTotalCount());
		data.put("pageIndex", pageIndex);
		data.put("pageSize", pageSize);// 每页条数
		data.put("pageNum", pageNum);// 当前页数量
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	
	
	
	
	/**
	 * 竞猜记录
	 */
	public void jcRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String status = ParamUtils.getParameter(deParameter, "status", "");
		String u = ParamUtils.getParameter(deParameter, "u");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);// 每页记录数量
		
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		
		if (!ParamUtils.chkString(u)) {
			message = "必要参数为空";
		}
		
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = (User) userService.getObject(User.class, uid);
			}
			if (user != null) {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				if (ParamUtils.chkString(status)) {
					hqls.append(" and g.winResult=? ");
					para.add(status);
				}
				hqls.append(" and g.userId=? ");
				para.add(user.getUserId());
				hqls.append(" order by j.mId desc,g.sessionId desc,g.betDetailId desc ");
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				int pageNum = 0;
				PaginationSupport ps = gaService.findJcRecordlList(
						hqls.toString(), para, statIndex, pageSize);
				List<GaBetDetailDTO> list = ps.getItems();
				JSONArray items = new JSONArray();// 比赛数组
				if (list != null & list.size() > 0) {
					pageNum = list.size();
					for (GaBetDetailDTO dto : list) {
						Integer itemsLength = items.length();
						Integer matchId = dto.getMatchId();// 比赛id
						Integer sessionId = dto.getSessionId();// 比赛局id
						Date betTime = dto.getBetTime();// 投注时间
						Integer betMoney = dto.getBetMoney();// 投注金额
						String winResult = dto.getWinResult();// 0未开奖  1中奖  2未中奖
						BigDecimal winCash = dto.getWinCash();// 中奖金额
						String playName = dto.getPlayName();// 比赛名称
						String room = dto.getRoom();// 局名称
						String optionTitle = dto.getOptionTitle();// 投注项名称字
						BigDecimal betRate = dto.getBetRate();// 赔率
						if (itemsLength==0) {
							JSONObject macthObj = new JSONObject();// 比赛对象
							macthObj.put("matchId", matchId);
							macthObj.put("playName", ParamUtils.chkStringNotNull(playName));
							macthObj.put("betTime", DateTimeUtil.DateToStringMMddHHmm(betTime));
							macthObj.put("winResult", ParamUtils.chkStringNotNull(winResult));
							
							JSONArray roomArr = new JSONArray();// 场次数组
							JSONObject roomObj = new JSONObject();// 场次对象
							roomObj.put("room", ParamUtils.chkStringNotNull(room));
							roomObj.put("sessionId", sessionId);
							
							JSONArray optionArr = new JSONArray();// 投注项数组
							JSONObject optionObj = new JSONObject();// 投注项对象
							optionObj.put("betMoney", betMoney);
							optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
							optionObj.put("betMoney", ProductUtil.BigFormat(betMoney));
							optionObj.put("betRate", ProductUtil.BigFormat(betRate));
							optionObj.put("winCash", ProductUtil.BigFormat(winCash));
							
							optionArr.put(optionObj);
							roomObj.put("optionArr", optionArr);
							roomArr.put(roomObj);
							macthObj.put("roomArr", roomArr);
							items.put(macthObj);
						} else {
							JSONObject lastMacthObj = items.getJSONObject(itemsLength - 1);// items里的最后一条数据
							Integer lastMatchId = lastMacthObj.getInt("matchId");
							if (matchId.intValue() == lastMatchId.intValue()) {// 属于同一比赛
								JSONArray lastRoomArr = lastMacthObj.getJSONArray("roomArr");// 场次数组
								JSONObject lastRoomObj = lastRoomArr.getJSONObject(lastRoomArr.length() - 1);// 最后一个场次对象
								Integer lastsessionId = lastRoomObj.getInt("sessionId");
								if (sessionId.intValue() == lastsessionId.intValue()) {// 属于同一场
									JSONArray lastOptionArr = lastRoomObj.getJSONArray("optionArr");// 投注项数组
									JSONObject optionObj = new JSONObject();// 投注项对象
									optionObj.put("betMoney", betMoney);
									optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
									optionObj.put("betMoney", ProductUtil.BigFormat(betMoney));
									optionObj.put("betRate", ProductUtil.BigFormat(betRate));
									optionObj.put("winCash", ProductUtil.BigFormat(winCash));
									lastOptionArr.put(optionObj);
								} else {// 不属于同一场
									JSONObject roomObj = new JSONObject();// 场次对象
									roomObj.put("room", ParamUtils.chkStringNotNull(room));
									roomObj.put("sessionId", sessionId);
									
									JSONArray optionArr = new JSONArray();// 投注项数组
									JSONObject optionObj = new JSONObject();// 投注项对象
									optionObj.put("betMoney", betMoney);
									optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
									optionObj.put("betMoney", ProductUtil.BigFormat(betMoney));
									optionObj.put("betRate", ProductUtil.BigFormat(betRate));
									optionObj.put("winCash", ProductUtil.BigFormat(winCash));
									
									optionArr.put(optionObj);
									roomObj.put("optionArr", optionArr);
									lastRoomArr.put(roomObj);
								}
							} else {// 不属于同一比赛
								JSONObject macthObj = new JSONObject();// 比赛对象
								macthObj.put("matchId", matchId);
								macthObj.put("playName", ParamUtils.chkStringNotNull(playName));
								macthObj.put("betTime", DateTimeUtil.DateToStringMMddHHmm(betTime));
								macthObj.put("winResult", ParamUtils.chkStringNotNull(winResult));
								
								JSONArray roomArr = new JSONArray();// 场次数组
								JSONObject roomObj = new JSONObject();// 场次对象
								roomObj.put("room", ParamUtils.chkStringNotNull(room));
								roomObj.put("sessionId", sessionId);
								
								JSONArray optionArr = new JSONArray();// 投注项数组
								JSONObject optionObj = new JSONObject();// 投注项对象
								optionObj.put("betMoney", betMoney);
								optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
								optionObj.put("betMoney", ProductUtil.BigFormat(betMoney));
								optionObj.put("betRate", ProductUtil.BigFormat(betRate));
								optionObj.put("winCash", ProductUtil.BigFormat(winCash));
								
								optionArr.put(optionObj);
								roomObj.put("optionArr", optionArr);
								roomArr.put(roomObj);
								macthObj.put("roomArr", roomArr);
								items.put(macthObj);
							}
						}
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message = "操作成功";
				} else {
					code = APIConstants.CODE_NOT_FOUND;
					message = "没有更多了";
				}
				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
			} else {
				message = "用户不存在";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}
}