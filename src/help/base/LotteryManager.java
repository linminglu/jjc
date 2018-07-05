package help.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.eff.TradeCacheUtil;
import com.apps.model.LotterySetting;
import com.apps.model.LotterySettingRl;
import com.apps.model.Promotion;
import com.apps.model.dto.BaseDataDTO;
import com.apps.service.IBaseDataService;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DateUtil;
import com.framework.util.DesUtils;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.UserLevel;
import com.ram.model.User;
import com.ram.service.user.IUserService;

/**
 * 抽奖红包 api 类
 * 
 * @author admin
 * 
 */
public class LotteryManager extends BaseDispatchAction {
	private final IBaseDataService baseDataService = (IBaseDataService) getService("baseDataService");
	private final IUserService userService = (IUserService) getService("userService");

	public void turnTableInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		if (ParamUtils.chkString(u)) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				hqls.append(" and ls.type=? ");
				para.add(Constants.LOTTERY_SETTING_TURNTABLE);
				List<BaseDataDTO> list = baseDataService.findLotterySetList(
						hqls.toString(), para);
				BigDecimal money = user.getMoney();
				if (money == null) {
					money = new BigDecimal(0);
				}
				if (user.getDayRecharge() != null) {
					if (list != null && list.size() > 0) {
						BaseDataDTO dto0 = list.get(0);
						LotterySetting ls0 = dto0.getLs();
						// if(money.compareTo(ls0.getMoney())<0){
						// map.put("code", APIConstants.CODE_REQUEST_ERROR);
						// map.put("msg",
						// "随身余额大于"+ls0.getMoney().toString()+"才能抽奖");
						// map.put("data", data);
						// }else{
						if (list.size() == 1) {
							LotterySettingRl rl0 = dto0.getLsrl();
							String value = rl0.getTitle();
							JSONArray array = new JSONArray(value);
							// JSONArray items=new JSONArray();
							// for(int i=0;i<array.length();i++){
							// JSONObject obj=new JSONObject();
							// JSONObject jsonObj = (JSONObject) array.get(i);
							// if(jsonObj.get("betType")!=null&&jsonObj.get("betContent")!=null){
							//
							// }
							// }
							data.put("items", array);// [{title:66大顺,bonus:66}]
						} else if (list.size() == 2) {
							BaseDataDTO dto1 = list.get(1);
							LotterySettingRl rl0 = dto0.getLsrl();
							LotterySettingRl rl1 = dto1.getLsrl();

							String value0 = rl0.getTitle();
							JSONArray array0 = new JSONArray(value0);

							String value1 = rl1.getTitle();
							JSONArray array1 = new JSONArray(value1);
							if (rl1.getRechargeMinMoney().compareTo(
									user.getDayRecharge()) > 0) {
								data.put("items", array0);// [{title:66大顺,bonus:66}]
							} else {
								data.put("items", array1);// [{title:66大顺,bonus:66}]
							}

						} else if (list.size() == 3) {

							BaseDataDTO dto1 = list.get(1);
							BaseDataDTO dto2 = list.get(2);
							LotterySettingRl rl0 = dto0.getLsrl();
							LotterySettingRl rl1 = dto1.getLsrl();
							LotterySettingRl rl2 = dto2.getLsrl();

							String value0 = rl0.getTitle();
							JSONArray array0 = new JSONArray(value0);

							String value1 = rl1.getTitle();
							JSONArray array1 = new JSONArray(value1);

							String value2 = rl2.getTitle();
							JSONArray array2 = new JSONArray(value2);
							if (rl2.getRechargeMinMoney().compareTo(
									user.getDayRecharge()) < 1) {
								data.put("items", array2);// [{title:66大顺,bonus:66}]
							} else if (rl1.getRechargeMinMoney().compareTo(
									user.getDayRecharge()) < 1) {
								data.put("items", array1);// [{title:66大顺,bonus:66}]
							} else {
								data.put("items", array0);// [{title:66大顺,bonus:66}]
							}
						}
						map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
						map.put("msg", "");
						map.put("data", data);
					}

				} else {
					BaseDataDTO dto0 = list.get(0);
					LotterySettingRl rl0 = dto0.getLsrl();
					String value0 = rl0.getTitle();
					JSONArray array0 = new JSONArray(value0);
					data.put("items", array0);// [{title:66大顺,bonus:66}]
					map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
					map.put("msg", "");
					map.put("data", data);
				}

			} else {
				map.put("code", APIConstants.CODE_REQUEST_ERROR);
				map.put("msg", "参数错误");
				map.put("data", data);
			}
		} else {
			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and ls.type=? ");
			para.add(Constants.LOTTERY_SETTING_TURNTABLE);
			List<BaseDataDTO> list = baseDataService.findLotterySetList(
					hqls.toString(), para);
			BaseDataDTO dto0 = list.get(0);
			LotterySettingRl rl0 = dto0.getLsrl();
			String value0 = rl0.getTitle();
			JSONArray array0 = new JSONArray(value0);
			data.put("items", array0);// [{title:66大顺,bonus:66}]
			map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
			map.put("msg", "");
			map.put("data", data);
		}

		JsonUtil.AjaxWriter(response, map);
	}

	// /**
	// * 转盘抽奖
	// */
	// public void turnTable(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response)
	// throws Exception {
	// Map<String, String> deParameter = ParamUtils.deParameter(request);
	// String u = ParamUtils.getParameter(deParameter, "u");
	// JSONObject map = new JSONObject();// 最外层
	// JSONObject data = new JSONObject();// 返回数据层
	//
	// if(ParamUtils.chkString(u)){
	// Map<String, String> decryptMap = DesUtils.decryptMap(u);
	// Integer uid = Integer.valueOf(decryptMap
	// .get(Constants.DES_KEY_UID));
	// User user = (User) userService.getObject(User.class, uid);
	// if (user != null) {
	// List<Object> para = new ArrayList<Object>();
	// StringBuffer hqls = new StringBuffer();
	// hqls.append(" and ls.type=? ");
	// para.add(Constants.LOTTERY_SETTING_TURNTABLE);
	// List<BaseDataDTO>
	// list=baseDataService.findLotterySetList(hqls.toString(),para);
	// BigDecimal money=user.getMoney();
	// if(money==null){
	// money=new BigDecimal(0);
	// }
	// if(user.getDayRecharge()!=null){
	// if(list!=null&&list.size()>0){
	// BaseDataDTO dto0=list.get(0);
	// LotterySetting ls0=dto0.getLs();
	// LotterySettingRl rl0=dto0.getLsrl();
	// if(ls0.getStartTime().after(new Date())||ls0.getEndTime().before(new
	// Date())){
	// map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// map.put("msg", "现在不是转盘活动时间");
	// map.put("data", data);
	// }else{
	// if(money.compareTo(ls0.getMoney())<0){
	// map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// map.put("msg", "随身余额大于"+ls0.getMoney().toString()+"才能抽奖");
	// map.put("data", data);
	// }else{
	// if(ParamUtils.chkInteger(user.getTurnTableNum())){
	// if(rl0.getRechargeMinMoney().compareTo(user.getDayRecharge())>0){
	// map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// map.put("msg", "当日充值金额大于"+rl0.getRechargeMinMoney().toString()+"才能抽奖");
	// map.put("data", data);
	// }else{
	// if(list.size()==1){
	// String value=rl0.getTitle();
	// JSONArray array=new JSONArray(value);
	// // if(rl0.getNum()<=0){
	// // map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// // map.put("msg", "转盘抽奖次数已用完");
	// // map.put("data", data);
	// // }else{
	// Random r=new Random();
	// int index=r.nextInt(array.length());
	// JSONObject jsonObj = (JSONObject) array.get(index);
	// if(jsonObj.get("bonus")!=null){
	// rl0.setNum(rl0.getNum()-1);
	// BigDecimal drawMoney=new BigDecimal(jsonObj.get("bonus").toString());
	// user=userService.updateUserTurnTable(user,rl0,drawMoney);
	// data.put("title", jsonObj.get("title").toString());
	// data.put("bonus", jsonObj.get("bonus").toString());
	// data.put("money", user.getMoney().setScale(2,
	// BigDecimal.ROUND_HALF_UP).toString());
	// map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
	// map.put("msg", "恭喜您抽中"+jsonObj.get("bonus").toString()+"元奖励");
	// map.put("data", data);
	// // }
	// }
	// }else if(list.size()==2){
	// BaseDataDTO dto1=list.get(1);
	// LotterySettingRl rl1=dto1.getLsrl();
	//
	// String value0=rl0.getTitle();
	// JSONArray array0=new JSONArray(value0);
	//
	// String value1=rl1.getTitle();
	// JSONArray array1=new JSONArray(value1);
	//
	// if(rl1.getRechargeMinMoney().compareTo(user.getDayRecharge())>=0){
	// // if(rl1.getNum()<=0){
	// // map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// // map.put("msg", "转盘抽奖次数已用完");
	// // map.put("data", data);
	// // }else{
	// Random r=new Random();
	// int index=r.nextInt(array1.length());
	// JSONObject jsonObj = (JSONObject) array1.get(index);
	// if(jsonObj.get("bonus")!=null){
	// BigDecimal drawMoney=new BigDecimal(jsonObj.get("bonus").toString());
	// user=userService.updateUserTurnTable(user,rl1,drawMoney);
	// data.put("title", jsonObj.get("title").toString());
	// data.put("bonus", jsonObj.get("bonus").toString());
	// map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
	// data.put("money", user.getMoney().setScale(2,
	// BigDecimal.ROUND_HALF_UP).toString());
	// map.put("msg", "恭喜您抽中"+jsonObj.get("bonus").toString()+"元奖励");
	// map.put("data", data);
	//
	// // }
	// }
	// }else if(rl0.getRechargeMinMoney().compareTo(user.getDayRecharge())>=0){
	// // if(rl0.getNum()<=0){
	// // map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// // map.put("msg", "转盘抽奖次数已用完");
	// // map.put("data", data);
	// // }else{
	// Random r=new Random();
	// int index=r.nextInt(array0.length());
	// JSONObject jsonObj = (JSONObject) array0.get(index);
	// if(jsonObj.get("bonus")!=null){
	// BigDecimal drawMoney=new BigDecimal(jsonObj.get("bonus").toString());
	// user=userService.updateUserTurnTable(user,rl0,drawMoney);
	// data.put("title", jsonObj.get("title").toString());
	// data.put("bonus", jsonObj.get("bonus").toString());
	// data.put("money", user.getMoney().setScale(2,
	// BigDecimal.ROUND_HALF_UP).toString());
	// map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
	// map.put("msg", "恭喜您抽中"+jsonObj.get("bonus").toString()+"元奖励");
	// map.put("data", data);
	// // }
	// }
	// }
	// }else if(list.size()==3){
	// BaseDataDTO dto1=list.get(1);
	// BaseDataDTO dto2=list.get(2);
	// LotterySettingRl rl1=dto1.getLsrl();
	// LotterySettingRl rl2=dto2.getLsrl();
	//
	// String value0=rl0.getTitle();
	// JSONArray array0=new JSONArray(value0);
	//
	// String value1=rl1.getTitle();
	// JSONArray array1=new JSONArray(value1);
	//
	// String value2=rl2.getTitle();
	// JSONArray array2=new JSONArray(value2);
	// if(rl2.getRechargeMinMoney().compareTo(user.getDayRecharge())>-1){
	// // if(rl2.getNum()<=0){
	// // map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// // map.put("msg", "转盘抽奖次数已用完");
	// // map.put("data", data);
	// // }else{
	// Random r=new Random();
	// int index=r.nextInt(array2.length());
	// JSONObject jsonObj = (JSONObject) array2.get(index);
	// if(jsonObj.get("bonus")!=null){
	// BigDecimal drawMoney=new BigDecimal(jsonObj.get("bonus").toString());
	// user=userService.updateUserTurnTable(user,rl2,drawMoney);
	// data.put("title", jsonObj.get("title").toString());
	// data.put("bonus", jsonObj.get("bonus").toString());
	// data.put("money", user.getMoney().setScale(2,
	// BigDecimal.ROUND_HALF_UP).toString());
	// map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
	// map.put("msg", "恭喜您抽中"+jsonObj.get("bonus").toString()+"元奖励");
	// map.put("data", data);
	// // }
	// }
	// }else if(rl1.getRechargeMinMoney().compareTo(user.getDayRecharge())>-1){
	// // if(rl1.getNum()<=0){
	// // map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// // map.put("msg", "转盘抽奖次数已用完");
	// // map.put("data", data);
	// // }else{
	// Random r=new Random();
	// int index=r.nextInt(array1.length());
	// JSONObject jsonObj = (JSONObject) array1.get(index);
	// if(jsonObj.get("bonus")!=null){
	// BigDecimal drawMoney=new BigDecimal(jsonObj.get("bonus").toString());
	// user=userService.updateUserTurnTable(user,rl1,drawMoney);
	// data.put("title", jsonObj.get("title").toString());
	// data.put("bonus", jsonObj.get("bonus").toString());
	// data.put("money", user.getMoney().setScale(2,
	// BigDecimal.ROUND_HALF_UP).toString());
	// map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
	// map.put("msg", "恭喜您抽中"+jsonObj.get("bonus").toString()+"元奖励");
	// map.put("data", data);
	// // }
	// }
	// }else{
	// // if(rl0.getNum()<=0){
	// // map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// // map.put("msg", "转盘抽奖次数已用完");
	// // map.put("data", data);
	// // }else{
	// Random r=new Random();
	// int index=r.nextInt(array0.length());
	// JSONObject jsonObj = (JSONObject) array0.get(index);
	// if(jsonObj.get("bonus")!=null){
	// BigDecimal drawMoney=new BigDecimal(jsonObj.get("bonus").toString());
	// user=userService.updateUserTurnTable(user,rl0,drawMoney);
	// data.put("money", user.getMoney().setScale(2,
	// BigDecimal.ROUND_HALF_UP).toString());
	// data.put("title", jsonObj.get("title").toString());
	// data.put("bonus", jsonObj.get("bonus").toString());
	// map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
	// map.put("msg", "恭喜您抽中"+jsonObj.get("bonus").toString()+"元奖励");
	// map.put("data", data);
	// // }
	// }
	// }
	// }
	// }
	// }else{
	// map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// map.put("msg", "您今日转盘次数已用完");
	// map.put("data", data);
	// }
	// }
	// }
	// }
	// }else{
	// BaseDataDTO dto0=list.get(0);
	// LotterySettingRl rl0=dto0.getLsrl();
	// map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// map.put("msg", "当日充值金额大于"+rl0.getRechargeMinMoney()+"才能抽奖");
	// map.put("data", data);
	// }
	// }else{
	// map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// map.put("msg", "参数错误");
	// map.put("data", data);
	// }
	// } else {
	// map.put("code", APIConstants.CODE_REQUEST_ERROR);
	// map.put("msg", "请先登录");
	// map.put("data", data);
	// }
	// JsonUtil.AjaxWriter(response, map);
	// }

	/**
	 * 转盘抽奖
	 */
	public void turnTable(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		if(ParamUtils.chkString(u)){
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
//				LotterySetting lotterySetting=baseDataService.getLotterySetting(Constants.LOTTERY_SETTING_TURNTABLE);
				LotterySetting lotterySetting=CacheUtil.getLotterySetting("turntable");
				String status = lotterySetting.getStatus();
				if(Constants.PUB_STATUS_OPEN.equals(status)){
					BigDecimal money=TradeCacheUtil.getUserMoney(uid);
					if(money==null){
						money=new BigDecimal(0);
					}
					if(lotterySetting.getStartTime().after(new Date())||lotterySetting.getEndTime().before(new Date())){
						message="现在不是转盘活动时间";
					}else{
						if(money.compareTo(lotterySetting.getMoney())<0){
							message="随身余额大于"+lotterySetting.getMoney().toString()+"才能抽奖";
						}else{
							
							//判断今天是否有充值次数
							if(ParamUtils.chkInteger(user.getTurnTableNum())){
//								Integer lsId = lotterySetting.getLsId();
//								List<LotterySettingRl> findLotterySetList = baseDataService.findLotterySetList(lsId);
								List<LotterySettingRl> findLotterySetList=CacheUtil.getLotterySettingRl("turntable");
								BigDecimal dayRecharge = user.getDayRecharge();//当日充值钱数
								int size = findLotterySetList.size();
								
								for (int i = 0; i < findLotterySetList.size(); i++) {
									LotterySettingRl rl = findLotterySetList.get(i);
									
									log.info("dayRecharge:"+dayRecharge);
									log.info("i:"+rl.getRechargeMinMoney());
									log.info("bijiao:"+dayRecharge.compareTo(rl.getRechargeMinMoney()));
									if(dayRecharge.compareTo(rl.getRechargeMinMoney())==-1){//当日充值小于最小充值限制
										message="当日充值不足"+rl.getRechargeMinMoney().toString()+"，不能抽奖";
										break;
									}else if(dayRecharge.compareTo(rl.getRechargeMaxMoney())==-1||dayRecharge.compareTo(rl.getRechargeMaxMoney())==0){
										//可以抽奖
										String value=rl.getTitle();
										JSONArray array=new JSONArray(value);
										Random r=new Random();
										int index=r.nextInt(array.length());
										JSONObject jsonObj = (JSONObject) array.get(index);
										if(jsonObj.get("bonus")!=null){
//											rl.setNum(rl.getNum()-1);
											BigDecimal  drawMoney=new BigDecimal(jsonObj.get("bonus").toString());
											user=userService.updateUserTurnTable(user,rl,drawMoney);
											data.put("title", jsonObj.get("title").toString());
											data.put("bonus", jsonObj.get("bonus").toString());
											data.put("money", user.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
											message="恭喜您抽中"+jsonObj.get("bonus").toString()+"元奖励";
											break;
										}
									}else{
										//可以抽奖。上不封顶
										if(size==(i+1)){//最后一个
											String value=rl.getTitle();
											JSONArray array=new JSONArray(value);
											Random r=new Random();
											int index=r.nextInt(array.length());
											JSONObject jsonObj = (JSONObject) array.get(index);
											if(jsonObj.get("bonus")!=null){
//												rl.setNum(rl.getNum()-1);
												BigDecimal  drawMoney=new BigDecimal(jsonObj.get("bonus").toString());
												if(drawMoney.compareTo(new BigDecimal("0"))==0){
													data.put("title", jsonObj.get("title").toString());
													data.put("bonus", jsonObj.get("bonus").toString());
													data.put("money", user.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
													message=jsonObj.get("title").toString();
												}else{
													user=userService.updateUserTurnTable(user,rl,drawMoney);
													data.put("title", jsonObj.get("title").toString());
													data.put("bonus", jsonObj.get("bonus").toString());
													data.put("money", user.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
													message="恭喜您抽中"+jsonObj.get("bonus").toString()+"元奖励";	
												}
											}
											
										}else{
											continue;
										}
									}
								}
								code=APIConstants.CODE_REQUEST_SUCCESS;
							}else{
								message="您不能抽奖，充值会增加抽奖次数哦";
							}
						}
					}
				}else{
					message="红包活动已关闭";
				}
			}else{
				message="无此用户";
			}
		}else{
			message="登录后才可以抽奖";
		}
		
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 抽红包
	 */
	public void drawRedPackets(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		if (ParamUtils.chkString(u)) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
				Integer redPacketsNum = user.getRedPacketsNum();
				if(redPacketsNum==null){
					redPacketsNum=0;
				}
				if (redPacketsNum.intValue() <= 0) {
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
					map.put("msg", "您现在没有抽红包资格!");
					map.put("data", data);
					JsonUtil.AjaxWriter(response, map);
					return;
				}
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				Date now = DateTimeUtil.getCurrentDate();
				hqls.append(" and ls.status=? ");
				para.add(Constants.PUB_STATUS_OPEN);// 活动有效
				hqls.append(" and ls.type=? ");
				para.add(Constants.LOTTERY_SETTING_REDPACKETS);// 红包
				hqls.append(" and ls.startTime<=? ");
				para.add(now);// 开始时间小于现在
				hqls.append(" and ls.endTime>=? ");
				para.add(now);// 结束时间大于现在
				hqls.append(" order by rl.rechargeMinMoney desc");
				
				List<BaseDataDTO> list = baseDataService.findLotterySetList(
						hqls.toString(), para);
				if (list != null && list.size() > 0) {
					LotterySetting ls = list.get(0).getLs();
					BigDecimal limitMoney = ls.getMoney();// 要求用户身上有多少余额
					BigDecimal money = ProductUtil.checkBigDecimal(user
							.getMoney());// 用户余额
					if (limitMoney.compareTo(new BigDecimal(0)) == 1) {// 要求用户余额大于0
						if (limitMoney.compareTo(money) == 1) { // 要求用户余额大于等于限制金额余额
							map.put("code", APIConstants.CODE_REQUEST_ERROR);
							map.put("msg", "您的余额太少，不能参与抽红包");
							map.put("data", data);
							JsonUtil.AjaxWriter(response, map);
							return;
						}
					}
					BigDecimal userDayRecharge = ProductUtil
							.checkBigDecimal(user.getDayRecharge());// 用户每日充值
					LotterySettingRl lsrl = null;
					boolean isTrue = false;
					for (BaseDataDTO dto : list) {
						lsrl = dto.getLsrl();
						BigDecimal rechargeMinMoney = lsrl
								.getRechargeMinMoney();
						BigDecimal rechargeMaxMoney = lsrl
								.getRechargeMaxMoney();
						if (rechargeMinMoney.compareTo(userDayRecharge) < 1
//								&& rechargeMaxMoney.compareTo(rechargeMaxMoney) >= 0
								){
							isTrue = true;
							break;
						}
							
					}
					
					if (!isTrue) {
						map.put("code", APIConstants.CODE_REQUEST_ERROR);
						map.put("msg", "您的今日充值太少，不能参与抽红包");
						map.put("data", data);
						JsonUtil.AjaxWriter(response, map);
						return;
					}
					Integer surplusNum = lsrl.getNum();// 剩余红包数量
					if (surplusNum.intValue() <= 0) {
						map.put("code", APIConstants.CODE_REQUEST_ERROR);
						map.put("msg", "对不起！没有红包了！");
						map.put("data", data);
						JsonUtil.AjaxWriter(response, map);
						return;
					}
					String redPacketsType = lsrl.getType();
					BigDecimal fixedMoney = null;// 红包金额
					if ("0".equals(redPacketsType)) {// 固定值
						fixedMoney = ProductUtil.checkBigDecimal(lsrl
								.getFixedMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
					} else if ("1".equals(redPacketsType)) {// 随机值
						BigDecimal minMoney = ProductUtil.checkBigDecimal(lsrl
								.getMinMoney());
						BigDecimal maxMoney = ProductUtil.checkBigDecimal(lsrl
								.getMaxMoney());
						fixedMoney = new BigDecimal(Math.random())
								.multiply(maxMoney.subtract(minMoney))
								.add(minMoney)
								.setScale(2, BigDecimal.ROUND_HALF_UP);// 保留2位小数并四舍五入
					}
					user = userService.updateUserRedPackets(user, lsrl,
							fixedMoney);
					data.put("newMoney", user.getMoney());// 最新余额
					data.put("fixedMoney", fixedMoney.toString());// 红包金额
					map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
					map.put("msg", "");
					map.put("data", data);
				} else {
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
					map.put("msg", "对不起！活动已结束！");
					map.put("data", data);
				}
			}
		}
		JsonUtil.AjaxWriter(response, map);
	}

	
	/**
	 * 优惠活动
	 */
	public void promotionList(ActionMapping mapping, ActionForm form,
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

		HQUtils hq = new HQUtils();
		hq.setStartIndex(pageIndex);
		hq.setPageSize(pageSize);
		hq.setStartIndex(pageIndex * pageSize);
		hq.addHsql(" from Promotion p where 1=1 ");
		hq.addHsql(" and p.status=?");
		hq.addPars(Constants.PUB_STATUS_OPEN);
		hq.addHsql(" order by p.sort desc,p.id desc ");
		PaginationSupport ps = baseDataService.findObjectPage(hq);
		List list = ps.getItems();
		JSONArray items = new JSONArray();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			for (int i = 0; i < list.size(); i++) {
				Promotion promotion = (Promotion) list.get(i);
				JSONObject it = new JSONObject();
				it.put("title", promotion.getTitle());
				it.put("startDate", DateTimeUtil.DateToString(
						promotion.getStartDate()));
				it.put("img", ParamUtils.chkStringNotNull(StringUtil
						.checkAPIHttpUrl(promotion.getImg())));
				it.put("content", promotion.getContent());
				it.put("id", promotion.getId());
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
	
	public void promotionDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		Integer pId = ParamUtils.getIntegerParameter(deParameter, "id");
		
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		if (!ParamUtils.chkInteger(pId)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		
		if (message.equals("")) {
			Promotion promotion = (Promotion) baseDataService.getObject(Promotion.class, pId);
			if(promotion!=null){
				JSONObject obj = new JSONObject();
				obj.put("title", promotion.getTitle());
				obj.put("startDate", DateTimeUtil.DateToStringAll(promotion.getStartDate()));
				obj.put("img", promotion.getImg());
				obj.put("content", promotion.getContent());
				
				data.put("obj", obj);
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				code = APIConstants.CODE_REQUEST_ERROR;
				message = APIConstants.TIPS_SERVER_ERROR;
			}
		}
		
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
}
