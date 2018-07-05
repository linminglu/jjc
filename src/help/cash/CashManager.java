package help.cash;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.BlackListCacheUtil;
import com.apps.eff.CacheUtil;
import com.apps.eff.GameHelpUtil;
import com.apps.eff.TradeCacheUtil;
import com.apps.model.Param;
import com.apps.model.UserTradeDetail;
import com.apps.service.IBaseDataService;
import com.apps.service.IParamService;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.cash.model.SysBank;
import com.cash.model.UserApplyCash;
import com.cash.model.UserBankBind;
import com.cash.service.ICashService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.model.User;
import com.ram.model.UserLimit;
import com.ram.model.UserLog;
import com.ram.service.user.IUserService;


public class CashManager extends BaseDispatchAction {

	private final ICashService cashService = (ICashService)getService("cashService");
	protected IUserService userService = (IUserService) getService("userService");
	private final IBaseDataService baseDataService = (IBaseDataService) getService("baseDataService");
	private final IParamService paramService = (IParamService) getService("paramService");
	private final IGaService gaService = (IGaService) getService("gaService");
	/*
	 * 绑定银行卡
	 */
	public void bindSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		String bankName= ParamUtils.getParameter(deParameter, "bankName");
		String bindAccount= ParamUtils.getParameter(deParameter, "bindAccount");
		String bindName= ParamUtils.getParameter(deParameter, "bindName");
		Integer bindId= ParamUtils.getIntegerParameter(deParameter, "bindId");
		Integer bankId= ParamUtils.getIntegerParameter(deParameter, "bankId");
		String message = "";
		if ( !ParamUtils.chkString(u)|| !ParamUtils.chkString(bindAccount)
				|| !ParamUtils.chkString(bindName)|| !ParamUtils.chkInteger(bankId)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
//		User user =null;
		Integer uid =0;
		Map<String, String> decryptMap = DesUtils.decryptMap(u);
		if(decryptMap!=null){
			uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
//			user = userService.getUser(uid);
		}
		
		
		if (message.equals("")) {
			if(ParamUtils.chkInteger(bindId)){
				List<UserBankBind> list=cashService.findBankBindListByUid(uid);
				List<UserBankBind> newList=new ArrayList<UserBankBind>();
				for(UserBankBind bank:list){
					bank.setIsDef("0");
					newList.add(bank);
				}
				cashService.updateObjectList(newList, null);
				UserBankBind bank=(UserBankBind) cashService.getObject(UserBankBind.class, bindId);
				bank.setIsDef("1");
				cashService.saveObject(bank,null);
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				UserBankBind bank=cashService.getUserBankBindByBankAccount(bindAccount);
				if(bank!=null){
					code = APIConstants.CODE_REQUEST_ERROR;
					message="该卡号已经存在";
					map.put("msg", message);
					map.put("code", code);
					JsonUtil.AjaxWriter(response, map);
					return;
				}
				List<UserBankBind> list=cashService.findBankBindListByUid(uid);
				if(list!=null&&list.size()>0){
					bank = list.get(0);
					String bindName2 = bank.getBindName();
					if(ParamUtils.chkString(bindName2)){
						code = APIConstants.CODE_REQUEST_ERROR;
						message="您已绑定了银行卡";
					}
				}
				
				if (message.equals("")) {
//					List<UserBankBind> list=cashService.findBankBindListByUid(uid);					
//					if(list!=null&list.size()>0){
//						List<UserBankBind> newList=new ArrayList<UserBankBind>();
//						for(UserBankBind bank1:list){
//							bank1.setIsDef("0");
//							newList.add(bank1);
//						}
//						cashService.updateObjectList(newList, null);
//					}
					bank=new UserBankBind();
//					bank.setBankId(bankId);
//					SysBank sysBank=(SysBank) cashService.getObject(SysBank.class, bankId);
					bank.setBankName(bankName);
					bank.setBindName(bindName);
					bank.setBindAccount(bindAccount);
					bank.setBindType("1");//银行卡
					bank.setIsDef("1");//默认
					bank.setUserId(uid);
//					bank.setBranch(branch);
//					bank.setCellPhone(telephone);
					
					cashService.saveBank(bank,uid);
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message="绑定成功";
				}
			}
		}
		map.put("msg", message);
		map.put("code", code);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 绑定账号列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void bindList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		String message = "";
		if ( !ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer.valueOf(decryptMap
					.get(Constants.DES_KEY_UID));
			JSONArray items = new JSONArray();// 数据集合
			List<UserBankBind> list=cashService.findBankBindListByUid(uid);
			if(list!=null&list.size()>0){
				for(UserBankBind bank:list){
					JSONObject obj = new JSONObject();
					obj.put("isDef", bank.getIsDef()==null?"":bank.getIsDef());
					obj.put("bindId", bank.getBankBindId());
					obj.put("bankName", bank.getBankName());
					obj.put("bindName", bank.getBindName());
					obj.put("bindAccount", bank.getBindAccount().substring(0,bank.getBindAccount().length()-4).replaceAll("[0-9]", "*")+bank.getBindAccount().substring(bank.getBindAccount().length()-4,bank.getBindAccount().length()));
					obj.put("bindType", bank.getBindType());
					items.put(obj);
				}
				data.put("items", items);
			}			
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 删除 账号
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void bindDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		Integer bindId= ParamUtils.getIntegerParameter(deParameter, "bindId");
		String message = "";
		if ( !ParamUtils.chkInteger(bindId)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
//		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer.valueOf(decryptMap
					.get(Constants.DES_KEY_UID));
			UserBankBind bank=(UserBankBind) cashService.getObject(UserBankBind.class, bindId);
			if(bank!=null){
				Integer userId = bank.getUserId();
				if (userId.intValue() == uid) {
					// 删除
					cashService.deleteObject(UserBankBind.class, bindId, null);
					message = "删除成功";
					code = APIConstants.CODE_REQUEST_SUCCESS;
				} else {
					message = "不是本人登录无法删除";
					code = APIConstants.CODE_REQUEST_ERROR;
				}				
			}
		}
		map.put("msg", message);
		map.put("code", code);
		JsonUtil.AjaxWriter(response, map);
	}
	/**
	 * 
	 * 提现保存
	 */
	public synchronized void submit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		String bankName= ParamUtils.getParameter(deParameter, "bankName");//银行名字
		String cashMoney= ParamUtils.getParameter(deParameter, "cashMoney");//提现金额
		String accountNo= ParamUtils.getParameter(deParameter, "accountNo");//银行账号
		String userName= ParamUtils.getParameter(deParameter, "userName");//姓名
		String cashPassword= ParamUtils.getParameter(deParameter, "cashPassword");//提现密码
		Integer cid=ParamUtils.getIntegerParameter(deParameter, "cid");//小区id
		String  type=ParamUtils.getParameter(deParameter, "type");//提现密码
		String cityName = ParamUtils.getParameter(deParameter, "cityName");// 
		String inpcode = ParamUtils.getParameter(deParameter, "inpcode", "");// 用户输入的验证码
		
		String mcode = (String) request.getSession().getAttribute(
				APIConstants.SESSION_SMS_CODE_NAME);
		
		String smsPhone = (String) request.getSession().getAttribute(
				APIConstants.SESSION_SMS_PHONE);
		
		log.info("M___________________inpcode>" + inpcode + "___sdCode>"
				+ mcode + "___smsPhone>"+ smsPhone + "___date>[" + DateTimeUtil.getDateTime()+ "]rec>>>>[cash_submit]");
		
		String message = "";
		if ( !ParamUtils.chkString(u)|| !ParamUtils.chkString(bankName)|| !ParamUtils.chkString(cashMoney)
				|| !ParamUtils.chkString(accountNo)|| !ParamUtils.chkString(userName)|| !ParamUtils.chkString(cashPassword)
				|| !ParamUtils.chkString(type)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		
		Param param=paramService.getParamByType(Constants.PARAM_CASH_SEND_MSG);
		if(param!=null&&param.getValue().equals("1")){
			if (ParamUtils.chkString(inpcode)) {
				if(!inpcode.trim().equals(mcode)){
					message = APIConstants.USER_TIPS_CODE_ERROR;
					code = APIConstants.CODE_REQUEST_ERROR;
				}	
			}else{
				message = "输入短信验证码";
				code = APIConstants.CODE_REQUEST_ERROR;
			}
		}
		
		if (message.equals("")) {
			boolean isBlacklist = BlackListCacheUtil.isBlacklist(request);
			if(isBlacklist){
				message=APIConstants.TIPS_BLACKLIST;
			}
		}
		HQUtils hq = new HQUtils();
		hq.addHsql(" from Param p where p.type=? ");
		hq.addPars(Constants.PARAM_CASH_SWITCH);
		Param cashSwitch = (Param) userService.getObject(hq);
		if(cashSwitch.getValue().equals("0")){
			// 关闭提款功能
			message = "后台维护，提款功能暂时关闭，请联系客服。";
			code = APIConstants.CODE_REQUEST_ERROR;
		}else{
			if (message.equals("")) {
				Map<String, String> decryptMap = DesUtils.decryptMap(u);
				Integer uid = Integer.valueOf(decryptMap
						.get(Constants.DES_KEY_UID));
				User user = userService.getValidUser(uid);
				if(user!=null){
					if (Constants.USER_TYPE_TEST.equals(user.getUserType())) {
						message="试玩用户不可提现哦";
					} else {
						BigDecimal money = TradeCacheUtil.getUserMoney(uid);
						BigDecimal cmoney=new BigDecimal(cashMoney);
						if(cmoney.compareTo(money)<=0){
							if(cmoney.compareTo(new BigDecimal(100))==-1){
								message="最低取现额100元";
							}	
							
//							BigDecimal LimitBetBack = user.getLimitBetBack();
							
							if(ParamUtils.chkString(user.getCashPassword())){
								if(!cashPassword.equals(user.getCashPassword())){
									message="取款密码有误";
								}
							}else{
								message="您还没有设置取款密码呢！";
							}
							int daynumber=0;//每日提款次数
							int fee=0;//提款手续费（%）
							int betMoney=0;//提款限制打码量（%）
							try {
								Param param34 = CacheUtil.getParam(Constants.PARAM_DAY_DRAWING_NUMBER);
								if(param!=null){
									daynumber=Integer.valueOf(param34.getValue());
								}
								
								Param param35 = CacheUtil.getParam(Constants.PARAM_DRAWING_FEE);
								if(param!=null){
									fee=Integer.valueOf(param35.getValue());
								}
								
								Param param36 = CacheUtil.getParam(Constants.PARAM_DRAWING_BET_MONEY);
								if(param!=null){
									betMoney=Integer.valueOf(param36.getValue());
								}
							} catch (Exception e) {
							}
							
							if(daynumber>0){
								int dayDrNum=cashService.getDayDrawingNum(uid);
								if(dayDrNum>=daynumber){
									//message="每日可提款"+daynumber+"次，超出可提次数联系在线客服，每次加收手续费"+fee+"%";
									message="提款次数超出要求，请明天再提款。";
								}
							}
							
						//提款手续费限制
						if(fee>0){
							//计算手续费
							BigDecimal serviceCharge = cmoney.multiply(new BigDecimal(fee)).divide(new BigDecimal(100));
							BigDecimal allMoney = cmoney.add(serviceCharge);//总扣钱数
							if(allMoney.compareTo(money)>0){
								message="余额不足";
							}
						}
						// 从上次提款开始计数的打码量
						BigDecimal betMoneyFromLastCash = userService.getUserBetMoneyFromLastCash(user.getUserId()).abs();
						// 从上次提款开始计数的充值量
						BigDecimal rechargeFromLastCash = userService.getUserRechargeFromLastCash(user.getUserId()).abs();
						
						GameHelpUtil.log("_cash",user.getUserId()+",倍="+betMoney+",码量="+betMoneyFromLastCash+",充值="+rechargeFromLastCash);
						
						if(betMoney>0){
							// 全站打码量限制
							BigDecimal bei=new BigDecimal(betMoney).divide(new BigDecimal(100));
							//打码量小于充值*打码倍数
							if(betMoneyFromLastCash.compareTo(rechargeFromLastCash.multiply(bei))<0){
								//message="打码量不足 "+betMoney+"%";
								message = "打码量未达到提款要求,请继续打码。";
							}
						}
						
						if(message.equals("")){
							// 单个用户限制
							UserLimit userLimit = userService.findUserLimitByUid(user.getUserId());
							if(userLimit!=null && userLimit.getBetMoney()!=null){
								// 限制提现，具体看user.java里的注释
								if (userLimit.getBetMoney().compareTo(new BigDecimal(0)) > 0) {
									// 统计今日打码量									
//								BigDecimal todayBetMoney = userService.getDayBetMoney(user.getUserId());
									BigDecimal bei=userLimit.getBetMoney().divide(new BigDecimal(100));
									if(betMoneyFromLastCash.compareTo(rechargeFromLastCash.multiply(bei))<0){
										if(userLimit.getCashTip()!=null&&!userLimit.getCashTip().equals("")){
											message = userLimit.getCashTip();
										}else{
											//message = "打码量不足";
											message = "打码量未达到提款要求,请继续打码！";
										}
									}else{
										// 打码量足够，解除限制
//									userLimit.setBetMoney(new BigDecimal(0));
										userLimit.setBetMoneyStatus("0");
										userService.saveObject(userLimit, null);
										UserLog log = new UserLog();
										log.setUserId(uid);
										log.setDateTime(new Date());
										log.setActionText("打码量达到"+betMoneyFromLastCash+",自动解除限制");
										userService.saveObject(log, null);
									}
								}
							}
						}
							
							//					City city=(City) cashService.getObject(City.class, cid);
							//					if(city==null||(city!=null&&!city.getType().equals("2"))){
							//						message="开户行所在地有误";
							//					}else{
							//						City city1=(City) cashService.getObject(City.class, city.getParentId());
							//						City city2=(City) cashService.getObject(City.class, city1.getParentId());
							//						cityName=city.getTitle()+"-"+city1.getTitle()+"-"+city2.getTitle();
							//					}
							if(message.equals("")){						
								UserApplyCash cash=new UserApplyCash();
								cash.setUserId(uid);
								cash.setAuditStatus("0");
								cash.setCid(cid);
								cash.setAccountNo(accountNo);
								cash.setBankName(bankName);
								cash.setUserName(userName);
								cash.setCashPassword(cashPassword);
								cash.setCityName(cityName);
								cash.setType(type);
								cash.setCreateTime(new Date());
								cash.setCashMoney(new BigDecimal(cashMoney));
								
								user.setBindName(userName);
								user.setBankName(bankName);
								user.setAccountNo(accountNo);
								user.setCityName(cityName);
//						userService.saveUser(user); // 把绑定的银行卡号存进user表。
								cashService.updateCash(user,cash);
								code = APIConstants.CODE_REQUEST_SUCCESS;
								message = "提现申请成功";
							}
						}else{
							code = APIConstants.CODE_REQUEST_ERROR;
							message = "提现金额大于用户最大余额";
						}
					}
				}else{
					code = APIConstants.CODE_REQUEST_ERROR;
					message = "用户不存在";
				}
			}
		}
		
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	/**
	 * 提现记录
	 */
	public void list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");	
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize",
				RamConstants.MAXPAGEITEMS);
		String message = "";
		if ( !ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
//		User user =null;
		Integer uid =0;
		Map<String, String> decryptMap = DesUtils.decryptMap(u);
		if(decryptMap!=null){
			uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
//			user = userService.getUser(uid);
		}
		
		if (message.equals("")) {
			int pageNum = 0;
			int statIndex = pageIndex * pageSize;// 计算开始的条数
			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and u.userId=? ");
			para.add(uid);
			PaginationSupport ps=cashService.findUserApplyCashList(hqls.toString(),
					para, statIndex, pageSize);
			List<UserApplyCash> list=ps.getItems();
			JSONArray items = new JSONArray();// 数据集合
			if(list!=null&list.size()>0){
				pageNum=list.size();
				for(UserApplyCash cash:list){
					JSONObject obj = new JSONObject();
					obj.put("cashId", cash.getApplyCashId());
					obj.put("createTime", DateTimeUtil.DateToString(cash.getCreateTime()));
					obj.put("auditStatus", cash.getAuditStatus());
					obj.put("cashMoney", cash.getCashMoney().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					int length=cash.getAccountNo().length();
					if(length>4){
						obj.put("accountNo", "***********"+cash.getAccountNo().substring(cash.getAccountNo().length()-4));
					}else{
						obj.put("accountNo", cash.getAccountNo());
					}
					items.put(obj);
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				code = APIConstants.CODE_NOT_FOUND;
			}
			data.put("items", items);
			data.put("total", ps.getTotalCount());// 总条数
			data.put("pageIndex", pageIndex);// 页码
			data.put("pageSize", pageSize);// 每页条数
			data.put("pageNum", pageNum);// 当前页数量
		}
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	/**
	 * 资金明细||账户明细
	 */
	public void tradeList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");	
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize",
				RamConstants.MAXPAGEITEMS);
		String cashType = ParamUtils.getParameter(deParameter, "cashType");//资金来源
		String message = "";
		if ( !ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		
//		User user =null;
		Integer uid =0;
		Map<String, String> decryptMap = DesUtils.decryptMap(u);
		if(decryptMap!=null){
			uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
//			user = userService.getUser(uid);
		}
		
		if (message.equals("")) {
			int pageNum = 0;
			int statIndex = pageIndex * pageSize;// 计算开始的条数
			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and ho.userId=? ");
			para.add(uid);
			PaginationSupport ps=cashService.findUserTradeDetailList(hqls.toString(),
					para, statIndex, pageSize);
			List<UserTradeDetail> list=ps.getItems();		
			JSONArray items = new JSONArray();// 数据集合
			if(list!=null&list.size()>0){
				pageNum = list.size();
				for(UserTradeDetail trade:list){
					JSONObject obj = new JSONObject();
//					obj.put("tradeId", trade.getTradeDetailId());
					obj.put("tradeTime", DateTimeUtil.DateToStringMMddHHmm(trade.getCreateTime()));
					obj.put("tradeContent", Constants.getCashTradeTypeNameZh(trade.getCashType()));
//					if(trade.getTradeType().equals(Constants.TRADE_TYPE_INCOME)){
						obj.put("tradeMoney", trade.getCashMoney().setScale(2,BigDecimal.ROUND_HALF_UP).toString());			
//					}else if(trade.getTradeType().equals(Constants.TRADE_TYPE_PAY)){
//						obj.put("tradeMoney", "-"+trade.getCashMoney().setScale(1,BigDecimal.ROUND_HALF_UP).toString());			
//					}
					items.put(obj);
				}
				data.put("items", items);
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				code = APIConstants.CODE_NOT_FOUND;
				message=APIConstants.TIPS_DATA_NOT;
			}
			data.put("total", ps.getTotalCount());// 总条数
			data.put("pageIndex", pageIndex);// 页码
			data.put("pageSize", pageSize);// 每页条数
			data.put("pageNum", pageNum);// 当前页数量	
		}
		
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
//	/**
//	 * 银行卡列表
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @throws Exception
//	 */
//	public void bankList1(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		Map<String, String> deParameter = ParamUtils.deParameter(request);
////		String u = ParamUtils.getParameter(deParameter, "u");
//		String message = "";
////		if ( !ParamUtils.chkString(u)) {
////			message = APIConstants.PARAMS_EMPTY_MSG;
////		}
//		JSONObject map = new JSONObject();// 最外层
//		JSONObject data = new JSONObject();// 返回数据层
//		String code = APIConstants.CODE_REQUEST_ERROR;
//		if (message.equals("")) {
////			Map<String, String> decryptMap = DesUtils.decryptMap(u);
////			Integer uid = Integer.valueOf(decryptMap
////					.get(Constants.DES_KEY_UID));
//			JSONArray items = new JSONArray();// 数据集合
//			List<SysBank> list=cashService.findSysBankList();
//			if(list!=null&list.size()>0){
//				for(SysBank bank:list){
//					JSONObject obj = new JSONObject();
//					obj.put("bankId", bank.getBankId());
//					obj.put("bankName", bank.getBankName());
//					items.put(obj);
//				}
//				data.put("items", items);
//			}			
//			code = APIConstants.CODE_REQUEST_SUCCESS;
//		}
//		map.put("msg", message);
//		map.put("code", code);
//		map.put("data", data);
//		JsonUtil.AjaxWriter(response, map);
//	}
	
	

	public void bankList1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
//		String u = ParamUtils.getParameter(deParameter, "u");
		String message = "";
//		if ( !ParamUtils.chkString(u)) {
//			message = APIConstants.PARAMS_EMPTY_MSG;
//		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
//			Map<String, String> decryptMap = DesUtils.decryptMap(u);
//			Integer uid = Integer.valueOf(decryptMap
//					.get(Constants.DES_KEY_UID));
			JSONArray items = new JSONArray();// 数据集合
			List<SysBank> list=cashService.findSysBankList();
			if(list!=null&list.size()>0){
				for(SysBank bank:list){
					JSONObject obj = new JSONObject();
					obj.put("bankId", bank.getBankId());
					obj.put("bankName", bank.getBankName());
					items.put(obj);
				}
				data.put("items", items);
			}			
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	
	/**
	 * 银行卡列表
	 */
	public void bankList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String type = ParamUtils.getParameter(deParameter, "type");
		String u = ParamUtils.getParameter(deParameter, "u");
		String message = "";
//		if ( !ParamUtils.chkString(u)) {
//			message = APIConstants.PARAMS_EMPTY_MSG;
//		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			
			String bankName = null;// 银行名字
			String accountNo = null;// 银行账号
			String userName =null; 
			String cityName =null;
			
			if(ParamUtils.chkString(u)){
				Map<String, String> decryptMap = DesUtils.decryptMap(u);
				Integer uid = Integer
						.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				User user = userService.getUser(uid);
				bankName=user.getBankName();
				System.out.println("^^^^^^^^^^^^^^"+bankName);
				accountNo=user.getAccountNo();
				userName = user.getBindName();
				cityName = user.getCityName();
			}
			
			JSONArray items = new JSONArray();// 数据集合
//			List<SysBank> list=cashService.findSysBankList();
//			if(list!=null&list.size()>0){
//				for(SysBank bank:list){
					JSONObject obj = new JSONObject();
					
					obj.put("cashType", "银行");
					JSONArray subitems = new JSONArray();
					subitems.put("中国工商银行");
					subitems.put("中国建设银行");
					subitems.put("中国银行");
					subitems.put("中国农业银行");
					subitems.put("中国交通银行");
					subitems.put("中国光大银行");
					subitems.put("中国民生银行");
					subitems.put("北京银行");
					subitems.put("北京农村商业银行");
					subitems.put("渤海银行");
					subitems.put("东亚银行");
					subitems.put("南京银行");
					subitems.put("邮政储蓄银行");
					subitems.put("招商银行");
					subitems.put("平安银行");
					subitems.put("华夏银行");
					subitems.put("中信银行");
					subitems.put("兴业银行");
					subitems.put("杭州银行");
					subitems.put("宁波银行");
					subitems.put("平安银行");
					subitems.put("上海银行");
					subitems.put("上海浦东发展银行");
					subitems.put("上海农村商业银行");
					subitems.put("广东发展银行");
					subitems.put("广州市农村信用合作社");
					subitems.put("广州市商业银行");
					subitems.put("深圳发展银行");
					
					subitems.put("徽商银行");
					subitems.put("中国银联");
//					subitems.put("支付宝");
//					subitems.put("财付通");
					
					obj.put("subitems", subitems);
					items.put(obj);
//				}
				data.put("items", items);
//			}
			JSONObject obj2 = new JSONObject();
			obj2.put("bankName", ParamUtils.chkStringNotNull(bankName));
			obj2.put("accountNo", ParamUtils.chkStringNotNull(accountNo));
			obj2.put("userName", ParamUtils.chkStringNotNull(userName));
			obj2.put("cityName", ParamUtils.chkStringNotNull(cityName));
			
			String isSMS="0";
			Param  param=paramService.getParamByType(Constants.PARAM_CASH_SEND_MSG);
			if(param!=null){
				String type2 = param.getType();
				if (type2.equals("28")) {
					isSMS = param.getValue();
				}
			}
			
			obj2.put("isSMS", ParamUtils.chkStringNotNull(isSMS));
			
			data.put("obj", obj2);
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}	


	/**
	 * 积分兑换
	 */
	public void pointExchange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");	
		String point = ParamUtils.getParameter(deParameter, "point");//积分
		String message = "";
		if ( !ParamUtils.chkString(u)||!ParamUtils.chkString(point)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer.valueOf(decryptMap
					.get(Constants.DES_KEY_UID));
			User user = (User) userService.getObject(User.class, uid);
			if (user == null) {
			    code = APIConstants.CODE_REQUEST_ERROR;
			    message = "用户不存在";
		    } else {
			    String status = user.getStatus();
			    if(Constants.PUB_STATUS_CLOSE.equals(status)){
				    code = APIConstants.CODE_REQUEST_ERROR;
				    message = "用户无效";
				    map.put("code", code);
				    map.put("msg", message);
				    JsonUtil.AjaxWriter(response, map);
				    return;
			    }
				int points = Integer.parseInt(point);
				int userpoints;
				if(user.getUserpoints()==null){
					userpoints =0;
				}else{
					userpoints = user.getUserpoints().intValue();
				}
				if(points<100){
				    message = "兑换积分必须大于100！";
				    map.put("code", code);
				    map.put("msg", message);
				    JsonUtil.AjaxWriter(response, map);
				    return;
				}
			    if(!ParamUtils.chkInteger(points)){
				    message = "参数错误！";
				    map.put("code", code);
				    map.put("msg", message);
				    JsonUtil.AjaxWriter(response, map);
				    return;
			    }
			    if(points>userpoints){
				    message = "积分不足！";
				    map.put("code", code);
				    map.put("msg", message);
				    JsonUtil.AjaxWriter(response, map);
				    return;	
			    }
			    int points2 = points/100*100;//实际兑换的积分，必须是100的整数倍
				boolean isSuccess = cashService.updatePointExchangeMoney(user,points2);
				if(isSuccess){
					BigDecimal userpoints2 = user.getUserpoints();
					data.put("points", ProductUtil.BigFormatJud(userpoints2));
					message = "兑换成功！";
					code = APIConstants.CODE_REQUEST_SUCCESS;
				}else{
					message = "兑换失败！";
				}
		    }
		}
		
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 统一用户的余额  以目前余额为准， UserTradeDetail 表里面数据统计小于余额 ，则充值一条数据 ，大于扣款一条数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void countMoney(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
//		String u = ParamUtils.getParameter(deParameter, "u");
		String message = "";
//		if ( !ParamUtils.chkString(u)) {
//			message = APIConstants.PARAMS_EMPTY_MSG;
//		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			userService.saveCountUserMoney();
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	

}
