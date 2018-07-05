package com.apps.web.action;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.model.LotterySetting;
import com.apps.model.LotterySettingRl;
import com.apps.model.Param;
import com.apps.model.dto.SettingDTO;
import com.apps.service.IBaseDataService;
import com.apps.service.IParamService;
import com.apps.util.JsonUtil;
import com.apps.web.form.LotterySettingForm;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.exception.permission.NoFunctionPermissionException;

/**
 * 活动设置
 * 
 * @author Mr.zang
 * 
 */
public class LotterySettingAction extends BaseDispatchAction {
	private final IBaseDataService baseDataService = (IBaseDataService) getService("baseDataService");
	private final IParamService paramService = (IParamService) getService("paramService");

	/**
	 * 转盘
	 */
	public ActionForward zpInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		LotterySetting lotterySetting = baseDataService
				.getLotterySetting(Constants.LOTTERY_SETTING_TURNTABLE);
		LotterySettingForm from = new LotterySettingForm();
		SettingDTO s1 = new SettingDTO();
		SettingDTO s2 = new SettingDTO();
		SettingDTO s3 = new SettingDTO();
		if (lotterySetting != null) {
			Date startTime = lotterySetting.getStartTime();
			Date endTime = lotterySetting.getEndTime();

			String format = "yyyy-MM-dd";
			from.setStartDate(DateTimeUtil.dateToString(startTime, format));
			from.setEndDate(DateTimeUtil.dateToString(endTime, format));

			Integer lsId = lotterySetting.getLsId();
			List<LotterySettingRl> list = baseDataService
					.findLotterySetList(lsId);

			for (int i = 0; i < list.size(); i++) {
				LotterySettingRl rl = list.get(i);
				BigDecimal minMoney = rl.getRechargeMinMoney();
				BigDecimal maxMoney = rl.getRechargeMaxMoney();
				String title = rl.getTitle();
				Integer rlId = rl.getRid();
				if (ParamUtils.chkString(title)) {
					JSONArray arr = new JSONArray(title);
					for (int j = 0; j < arr.length(); j++) {
						JSONObject obj = (JSONObject) arr.get(j);
						String lable = obj.getString("title");
						Double bonustemp = obj.getDouble("bonus");
						String bonus = bonustemp.toString();
						// String bonus=obj.getString("bonus");
						if (i == 0) {
							if (j == 0) {
								s1.setLable1(lable);
								s1.setVal1(bonus);
							} else if (j == 1) {
								s1.setLable2(lable);
								s1.setVal2(bonus);
							} else if (j == 2) {
								s1.setLable3(lable);
								s1.setVal3(bonus);
							} else if (j == 3) {
								s1.setLable4(lable);
								s1.setVal4(bonus);
							} else if (j == 4) {
								s1.setLable5(lable);
								s1.setVal5(bonus);
							} else if (j == 5) {
								s1.setLable6(lable);
								s1.setVal6(bonus);
							} else if (j == 6) {
								s1.setLable7(lable);
								s1.setVal7(bonus);
							} else if (j == 7) {
								s1.setLable8(lable);
								s1.setVal8(bonus);
							} else if (j == 8) {
								s1.setLable9(lable);
								s1.setVal9(bonus);
							}
						} else if (i == 1) {
							if (j == 0) {
								s2.setLable1(lable);
								s2.setVal1(bonus);
							} else if (j == 1) {
								s2.setLable2(lable);
								s2.setVal2(bonus);
							} else if (j == 2) {
								s2.setLable3(lable);
								s2.setVal3(bonus);
							} else if (j == 3) {
								s2.setLable4(lable);
								s2.setVal4(bonus);
							} else if (j == 4) {
								s2.setLable5(lable);
								s2.setVal5(bonus);
							} else if (j == 5) {
								s2.setLable6(lable);
								s2.setVal6(bonus);
							} else if (j == 6) {
								s2.setLable7(lable);
								s2.setVal7(bonus);
							} else if (j == 7) {
								s2.setLable8(lable);
								s2.setVal8(bonus);
							} else if (j == 8) {
								s2.setLable9(lable);
								s2.setVal9(bonus);
							}
						} else if (i == 2) {
							if (j == 0) {
								s3.setLable1(lable);
								s3.setVal1(bonus);
							} else if (j == 1) {
								s3.setLable2(lable);
								s3.setVal2(bonus);
							} else if (j == 2) {
								s3.setLable3(lable);
								s3.setVal3(bonus);
							} else if (j == 3) {
								s3.setLable4(lable);
								s3.setVal4(bonus);
							} else if (j == 4) {
								s3.setLable5(lable);
								s3.setVal5(bonus);
							} else if (j == 5) {
								s3.setLable6(lable);
								s3.setVal6(bonus);
							} else if (j == 6) {
								s3.setLable7(lable);
								s3.setVal7(bonus);
							} else if (j == 7) {
								s3.setLable8(lable);
								s3.setVal8(bonus);
							} else if (j == 8) {
								s3.setLable9(lable);
								s3.setVal9(bonus);
							}
						}
					}
				}

				if (i == 0) {
					s1.setRlId(rlId);
					s1.setMinMoney(minMoney);
					s1.setMaxMoney(maxMoney);
				} else if (i == 1) {
					s2.setRlId(rlId);
					s2.setMinMoney(minMoney);
					s2.setMaxMoney(maxMoney);
				} else if (i == 2) {
					s3.setRlId(rlId);
					s3.setMinMoney(minMoney);
					s3.setMaxMoney(maxMoney);
				}
			}
		}

		from.setLotterySetting(lotterySetting);
		from.setSetting1(s1);
		from.setSetting2(s2);
		from.setSetting3(s3);
		request.setAttribute("lotterySettingForm", from);
		return mapping.findForward("zpInit");
	}

	/**
	 * 保存转盘
	 */
	public ActionForward savezp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		LotterySettingForm lotterySettingForm = (LotterySettingForm) form;
		String startDate = lotterySettingForm.getStartDate();
		String endDate = lotterySettingForm.getEndDate();
		
		LotterySetting lotterySetting = lotterySettingForm.getLotterySetting();
		lotterySetting.setLsId(1);
		lotterySetting.setType(Constants.LOTTERY_SETTING_TURNTABLE);
		String format="yyyy-MM-dd HH:mm:ss";
		lotterySetting.setStartTime(DateTimeUtil.stringToDate(startDate+" 00:00:00",format));
		lotterySetting.setEndTime(DateTimeUtil.stringToDate(endDate+" 23:59:59",format));
		
		SettingDTO setting1 = lotterySettingForm.getSetting1();
		SettingDTO setting2 = lotterySettingForm.getSetting2();
		SettingDTO setting3 = lotterySettingForm.getSetting3();
		
		List<LotterySettingRl> rlList=new ArrayList<LotterySettingRl>();
		
		Integer rlId1 = setting1.getRlId();
		BigDecimal maxMoney1 = setting1.getMaxMoney();
		BigDecimal minMoney1 = setting1.getMinMoney();
		String lable1 = setting1.getLable1();
		String val1 = setting1.getVal1();
		String lable2 = setting1.getLable2();
		String val2 = setting1.getVal2();
		String lable3 = setting1.getLable3();
		String val3 = setting1.getVal3();
		String lable4 = setting1.getLable4();
		String val4 = setting1.getVal4();
		String lable5 = setting1.getLable5();
		String val5 = setting1.getVal5();
		String lable6 = setting1.getLable6();
		String val6 = setting1.getVal6();
		JSONArray arr1=new JSONArray();
		JSONObject obj11=new JSONObject();
		obj11.put("title", lable1);
		obj11.put("bonus", val1);
		arr1.put(obj11);
		JSONObject obj12=new JSONObject();
		obj12.put("title", lable2);
		obj12.put("bonus", val2);
		arr1.put(obj12);
		JSONObject obj13=new JSONObject();
		obj13.put("title", lable3);
		obj13.put("bonus", val3);
		arr1.put(obj13);
		JSONObject obj14=new JSONObject();
		obj14.put("title", lable4);
		obj14.put("bonus", val4);
		arr1.put(obj14);
		JSONObject obj15=new JSONObject();
		obj15.put("title", lable5);
		obj15.put("bonus", val5);
		arr1.put(obj15);
		JSONObject obj16=new JSONObject();
		obj16.put("title", lable6);
		obj16.put("bonus", val6);
		arr1.put(obj16);
		
		LotterySettingRl rl1=new LotterySettingRl();
		rl1.setLsId(1);
		rl1.setRid(rlId1);
		rl1.setType(Constants.LOTTERY_SETTING_TURNTABLE);
		rl1.setTitle(arr1.toString());
		rl1.setRechargeMaxMoney(maxMoney1);
		rl1.setRechargeMinMoney(minMoney1);
		rlList.add(rl1);
		
		Integer rlId2 = setting2.getRlId();
		BigDecimal maxMoney2 = setting2.getMaxMoney();
		BigDecimal minMoney2 = setting2.getMinMoney();
		String lable21 = setting2.getLable1();
		String val21 = setting2.getVal1();
		String lable22 = setting2.getLable2();
		String val22 = setting2.getVal2();
		String lable23 = setting2.getLable3();
		String val23 = setting2.getVal3();
		String lable24 = setting2.getLable4();
		String val24 = setting2.getVal4();
		String lable25 = setting2.getLable5();
		String val25 = setting2.getVal5();
		String lable26 = setting2.getLable6();
		String val26 = setting2.getVal6();
		JSONArray arr2=new JSONArray();
		JSONObject obj21=new JSONObject();
		obj21.put("title", lable21);
		obj21.put("bonus", val21);
		arr2.put(obj21);
		JSONObject obj22=new JSONObject();
		obj22.put("title", lable22);
		obj22.put("bonus", val22);
		arr2.put(obj22);
		JSONObject obj23=new JSONObject();
		obj23.put("title", lable23);
		obj23.put("bonus", val23);
		arr2.put(obj23);
		JSONObject obj24=new JSONObject();
		obj24.put("title", lable24);
		obj24.put("bonus", val24);
		arr2.put(obj24);
		JSONObject obj25=new JSONObject();
		obj25.put("title", lable25);
		obj25.put("bonus", val25);
		arr2.put(obj25);
		JSONObject obj26=new JSONObject();
		obj26.put("title", lable26);
		obj26.put("bonus", val26);
		arr2.put(obj26);
		
		LotterySettingRl rl2=new LotterySettingRl();
		rl2.setRid(rlId2);
		rl2.setLsId(1);
		rl2.setType(Constants.LOTTERY_SETTING_TURNTABLE);
		rl2.setTitle(arr2.toString());
		rl2.setRechargeMaxMoney(maxMoney2);
		rl2.setRechargeMinMoney(minMoney2);
		rlList.add(rl2);
		
		Integer rlId3 = setting3.getRlId();
		BigDecimal maxMoney3 = setting3.getMaxMoney();
		BigDecimal minMoney3 = setting3.getMinMoney();
		String lable31 = setting3.getLable1();
		String val31 = setting3.getVal1();
		String lable32 = setting3.getLable2();
		String val32 = setting3.getVal2();
		String lable33 = setting3.getLable3();
		String val33 = setting3.getVal3();
		String lable34 = setting3.getLable4();
		String val34 = setting3.getVal4();
		String lable35 = setting3.getLable5();
		String val35 = setting3.getVal5();
		String lable36 = setting3.getLable6();
		String val36 = setting3.getVal6();
		JSONArray arr3=new JSONArray();
		JSONObject obj31=new JSONObject();
		obj31.put("title", lable31);
		obj31.put("bonus", val31);
		arr3.put(obj31);
		JSONObject obj32=new JSONObject();
		obj32.put("title", lable32);
		obj32.put("bonus", val32);
		arr3.put(obj32);
		JSONObject obj33=new JSONObject();
		obj33.put("title", lable33);
		obj33.put("bonus", val33);
		arr3.put(obj33);
		JSONObject obj34=new JSONObject();
		obj34.put("title", lable34);
		obj34.put("bonus", val34);
		arr3.put(obj34);
		JSONObject obj35=new JSONObject();
		obj35.put("title", lable35);
		obj35.put("bonus", val35);
		arr3.put(obj35);
		JSONObject obj36=new JSONObject();
		obj36.put("title", lable36);
		obj36.put("bonus", val36);
		arr3.put(obj36);
		
		LotterySettingRl rl3=new LotterySettingRl();
		rl3.setRid(rlId3);
		rl3.setLsId(1);
		rl3.setType(Constants.LOTTERY_SETTING_TURNTABLE);
		rl3.setTitle(arr3.toString());
		rl3.setRechargeMaxMoney(maxMoney3);
		rl3.setRechargeMinMoney(minMoney3);
		rlList.add(rl3);
		
		baseDataService.saveObject(lotterySetting, null);
		baseDataService.updateObjectList(rlList, null);
		
		CacheUtil.updateTurnTableList();
		
		request.setAttribute("msg", "保存成功");
		return mapping.findForward("savezp");
	}

	

	/**
	 * 红包
	 */
	public ActionForward hbInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		LotterySetting lotterySetting = baseDataService
				.getLotterySetting(Constants.LOTTERY_SETTING_REDPACKETS);
		LotterySettingForm from = new LotterySettingForm();
		if (lotterySetting != null) {
			Date startTime = lotterySetting.getStartTime();
			Date endTime = lotterySetting.getEndTime();

			String format = "yyyy-MM-dd";
			from.setStartDate(DateTimeUtil.dateToString(startTime, format));
			from.setEndDate(DateTimeUtil.dateToString(endTime, format));

			Integer lsId = lotterySetting.getLsId();
			List<LotterySettingRl> list = baseDataService
					.findLotterySetList(lsId);

			for (int i = 0; i < list.size(); i++) {
				LotterySettingRl rl = list.get(i);
				if(i==0){
					from.setRl1(rl);
				}else if(i==1){
					from.setRl2(rl);
				}else if(i==2){
					from.setRl3(rl);
				}
			}
		}
		from.setLotterySetting(lotterySetting);
		request.setAttribute("lotterySettingForm", from);
		return mapping.findForward("hbInit");
	}
	/**
	 * 保存红包
	 */
	public ActionForward savehb(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		LotterySettingForm lotterySettingForm = (LotterySettingForm) form;
		String startDate = lotterySettingForm.getStartDate();
		String endDate = lotterySettingForm.getEndDate();
		LotterySetting lotterySetting = lotterySettingForm.getLotterySetting();
		lotterySetting.setLsId(2);
		lotterySetting.setType(Constants.LOTTERY_SETTING_REDPACKETS);
		String format="yyyy-MM-dd HH:mm:ss";
		lotterySetting.setStartTime(DateTimeUtil.stringToDate(startDate+" 00:00:00",format));
		lotterySetting.setEndTime(DateTimeUtil.stringToDate(endDate+" 23:59:59",format));
		
		LotterySettingRl rl1 = lotterySettingForm.getRl1();
		LotterySettingRl rl2 = lotterySettingForm.getRl2();
		LotterySettingRl rl3 = lotterySettingForm.getRl3();
		List<LotterySettingRl> rlList=new ArrayList<LotterySettingRl>();
		rlList.add(rl1);
		rlList.add(rl2);
		rlList.add(rl3);
		
		
		baseDataService.saveObject(lotterySetting, null);
		baseDataService.updateObjectList(rlList, null);
		CacheUtil.updateRedPacketsList();
		request.setAttribute("msg", "保存成功");
		return mapping.findForward("savehb");
	}
	/**
	 * 打码返水
	 */
//	public ActionForward backInit(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception, NoFunctionPermissionException {
//		LotterySetting lotterySetting = baseDataService
//				.getLotterySetting(Constants.LOTTERY_SETTING_RECHARGE_BET_BACK);
//		LotterySettingForm from = new LotterySettingForm();
//		if (lotterySetting != null) {
//			Date startTime = lotterySetting.getStartTime();//返水时间
//
//			from.setStartDate(DateTimeUtil.dateToString(startTime, "HH:mm:ss"));
//
//			Integer lsId = lotterySetting.getLsId();
//			List<LotterySettingRl> list = baseDataService
//					.findLotterySetList(lsId);
//
//			for (int i = 0; i < list.size(); i++) {
//				LotterySettingRl rl = list.get(i);
//				if(i==0){
//					from.setRl2(rl);
//				}else if(i==1){
//					from.setRl1(rl);
//				}
//			}
//		}
//		from.setLotterySetting(lotterySetting);
//		request.setAttribute("lotterySettingForm", from);
//		return mapping.findForward("backInit");
//	}
	/**
	 * 打码返水
	 */
	public void backInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		LotterySetting lotterySetting = baseDataService
				.getLotterySetting(Constants.LOTTERY_SETTING_RECHARGE_BET_BACK);
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (lotterySetting != null) {
			data.put("startTime", DateTimeUtil.DateToStringHHmmss(lotterySetting.getStartTime()));
			data.put("status", lotterySetting.getStatus());
			Integer lsId = lotterySetting.getLsId();
			List<LotterySettingRl> list = baseDataService
					.findLotterySetList(lsId);
			if (list != null && list.size() > 0) {
				LotterySettingRl rl = list.get(0);
				data.put("minMoney", rl.getRechargeMinMoney());
				data.put("maxMoney", rl.getRechargeMaxMoney());
				data.put("fixedMoney", rl.getFixedMoney());
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				code = APIConstants.CODE_NOT_FOUND;
			}
		} else {
			code = APIConstants.CODE_NOT_FOUND;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 保存打码返水设置
	 */
	public void saveBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		
		String datas = ParamUtils.getParameter(request,
				"map", "");
		
		JSONObject dataObj = new JSONObject(datas);
		String startTime  = "";
		if(dataObj.has("startTime")){
			startTime = dataObj.getString("startTime");
		}
		JSONArray jsonArray = dataObj.getJSONArray("items");
		List<Param> saveList = new ArrayList<Param>();
		for(int i =0;i<jsonArray.length();i++){
			JSONObject obj = (JSONObject) jsonArray.get(i);
			
			Param param =paramService.getParamByType(obj.getString("type"));
			param.setStatus(obj.getString("status"));
			if(startTime.length()>0){
				param.setValue(startTime+"&"+obj.getString("rate"));
			}else{
				param.setValue(obj.getString("rate"));
			}
			param.setBeizhu(obj.getString("minMoney")+","+obj.getString("maxMoney"));
			saveList.add(param);
		}
		
		baseDataService.updateObjectList(saveList, null);
		//更新充值缓存配置
		CacheUtil.rechargeBackConfigUpdate();
		//更新缓存缓存配置
		CacheUtil.betBackConfigUpdate();
		
		code = APIConstants.CODE_REQUEST_SUCCESS;
		
		CacheUtil.updateParam();
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
}
