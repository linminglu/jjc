package com.apps.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.model.Param;
import com.apps.service.IParamService;
import com.apps.util.JsonUtil;
import com.apps.util.UploadUtil;
import com.apps.web.form.ParamForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

import help.base.APIConstants;

/**
 * 类型
 * 
 * @author Mr.zang
 * 
 */
public class ParamAction extends BaseDispatchAction {
	private final IParamService paramService = (IParamService) getService("paramService");

	/**
	 * 列表
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		String title = paramForm.getTitle();
		String type = ParamUtils.getParameter(request, "type");
		if (ParamUtils.chkString(title)) {
			hqls.append(" and t.title like ?");
			para.add("%"+title+"%");
		}
		if (ParamUtils.chkString(type)) {
			hqls.append(" and t.type = ?");
			para.add(type);
			request.setAttribute("type", type);
		}else{
			hqls.append(" and (t.type='20' or t.type='21' or t.type='22' or t.type='23' or t.type='24' or t.type='25' or t.type='26' or t.type='27'  or  t.type='28'  )");
			
		}
//		hqls.append(" and t.status='1' ");
		
		
		hqls.append(" order by t.sort desc");
		PaginationSupport ps = paramService.findList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("init");
	}

	/**
	 * 创建
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("create");
	}

	/**
	 * 修改
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Integer id = ParamUtils.getIntegerParameter(request, "paramId");
		Param param = (Param) paramService.getObject(
				Param.class, id);
		paramForm.setParam(param);
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preModify");
	}

	/**
	 * 保存
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		paramService.saveParam(param);
//		return init(mapping, form, request, response);
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("save");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		path.append("&type=" + param.getType());
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "paramId");
		Param param = (Param) paramService.getObject(
				Param.class, id);
		String status = param.getStatus();
		String flag = "success";
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				param.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				param.setStatus(Constants.PUB_STATUS_OPEN);
			}
			paramService.saveObject(param, null);
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}

	/**
	 * 更改状态
	 */
	public void changeValue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "paramId");
		Param param = (Param) paramService.getObject(
				Param.class, id);
		String value = param.getValue();
		String flag = "success";
		try {
			if (value.equals(Constants.PUB_STATUS_OPEN)) {
				param.setValue(Constants.PUB_STATUS_CLOSE);
			} else {
				param.setValue(Constants.PUB_STATUS_OPEN);
			}
			paramService.saveObject(param, null);
			CacheUtil.delParam();
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
	
	/**
	 * 排序
	 */
	public ActionForward sort(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String flag = ParamUtils.getParameter(request, "flag");// 1升序 0降序
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		paramService.updateSort(id, flag);
		return init(mapping, form, request, response);
	}
	/**
	 * 删除
	 */
	public ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Param param = (Param) paramService.getObject(
				Param.class, id);	
		ParamForm paramForm = (ParamForm) form;
		paramService.delParam(param);
		String startIndex = paramForm.getStartIndex();
		return init(mapping, form, request, response);

	}

	/**
	 * 注册方式切换
	 */
	public ActionForward regSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_REG_SWITCH);
//		paramForm.setParam(param);
		request.setAttribute("param", param);
		return mapping.findForward("regSet");
	}
	
	
	/**
	 * 在线充值开关设置
	 */
	public ActionForward onlineSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_RECHARGE_ONLINE);
//		paramForm.setParam(param);
		request.setAttribute("param", param);
		return mapping.findForward("onlineSet");
	}
	/**
	 * 线下充值开关设置
	 */
	public ActionForward offlineSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_RECHARGE_OFFLINE);
//		paramForm.setParam(param);
		request.setAttribute("param", param);
		return mapping.findForward("offlineSet");
	}
	
	/**
	 * 快速充值开关设置
	 */
	public ActionForward fastRechargeSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_RECHARGE_FAST);
		request.setAttribute("param", param);
		return mapping.findForward("fastRechargeSet");
	}
	
	
	/**
	 * 修改
	 */
	public ActionForward preFastRechargeSetModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Integer id = ParamUtils.getIntegerParameter(request, "paramId");
		Param param = (Param) paramService.getObject(
				Param.class, id);
		paramForm.setParam(param);
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preFastRechargeSetModify");
	}

	/**
	 * 保存
	 */
	public ActionForward saveFastRechargeSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		paramService.saveParam(param);
		return fastRechargeSet(mapping, form, request, response);
	}
	
	/**
	 * 支付宝/微信/QQ  充值开关设置
	 */
	public ActionForward codeRechargeSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_RECHARGE_CODE);
		request.setAttribute("param", param);
		return mapping.findForward("codeRechargeSet");
	}
	
	/**
	 * 修改
	 */
	public ActionForward preCodeRechargeSetModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Integer id = ParamUtils.getIntegerParameter(request, "paramId");
		Param param = (Param) paramService.getObject(
				Param.class, id);
		paramForm.setParam(param);
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preCodeRechargeSetModify");
	}

	/**
	 * 保存
	 */
	public ActionForward saveCodeRechargeSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		FormFile file = paramForm.getFile();
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/code";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			param.setValue(imgUrl);
		}
		paramService.saveParam(param);
		return codeRechargeSet(mapping, form, request, response);
	}
	
	
	/**
	 * 微信公众号设置
	 */
	public ActionForward officialAccountsSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_OFFICIAL_ACCOUNTS);
		request.setAttribute("param", param);
		return mapping.findForward("officialAccountsSet");
	}
	
	
	/**
	 * 微信公众号二维码设置
	 */
	public ActionForward preOfficialAccountsSetModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_OFFICIAL_ACCOUNTS);			
		paramForm.setParam(param);
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preOfficialAccountsSetModify");
	}
	/**
	 * 保存
	 */
	public ActionForward saveOfficialAccountsSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		FormFile file = paramForm.getFile();
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/code";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			param.setValue(imgUrl);
		}
		paramService.saveParam(param);
		return officialAccountsSet(mapping, form, request, response);
	}
	
	
	/**
	 * 会员每次充值系统赠送设置
	 */
	public ActionForward rechargeSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_RECHARGE_SEND);
		request.setAttribute("param", param);
		return mapping.findForward("rechargeSendSet");
	}
	
	
	/**
	 * 查看会员每次充值系统赠送设置
	 */
	public ActionForward preRechargeSendSetModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_RECHARGE_SEND);			
		paramForm.setParam(param);
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preRechargeSendSetModify");
	}
	/**
	 * 保存会员每次充值系统赠送设置
	 */
	public ActionForward saveRechargeSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		paramService.saveParam(param);
		return rechargeSendSet(mapping, form, request, response);
	}
	
	/**
	 * 会员每次充值返水代理设置
	 */
	public ActionForward sendAgentSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String type = ParamUtils.getParameter(request, "type");
		ParamForm paramForm = (ParamForm) form;
		Param param =null;
		if(type.equals("1")){
			param= paramService.getParamByType(Constants.PARAM_RECHARGE_SEND_AGENT);
		}else if(type.equals("2")){
			param=paramService.getParamByType(Constants.PARAM_BET_SEND_AGENT);
		}
		paramForm.setType(type);
		String value=param.getValue();
		if(type.equals("1")){
			paramForm.setValue(value);
		}else if(type.equals("2")){
			paramForm.setTime(value.split("&")[0]);
			paramForm.setValue(value.split("&")[1]);
		}
	
		request.setAttribute("param", param);
		request.setAttribute("paramForm", paramForm);
		return mapping.findForward("sendAgentSet");
	}
	
	
	/**
	 * 查看会员每次充值返水代理   打码返水代理
	 */
	public ActionForward preSendAgentSetModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		String type = ParamUtils.getParameter(request, "type");
		Param param =null;
		if(type.equals("1")){
			param= paramService.getParamByType(Constants.PARAM_RECHARGE_SEND_AGENT);
		}else if(type.equals("2")){
			param=paramService.getParamByType(Constants.PARAM_BET_SEND_AGENT);
		}
		paramForm.setType(type);
		paramForm.setParam(param);
		String value=param.getValue();
		if(type.equals("1")){
	//		paramForm.setTime(value.split("&")[0]);
			paramForm.setValue(value);
		}else if(type.equals("2")){
			paramForm.setTime(value.split("&")[0]);
			paramForm.setValue(value.split("&")[1]);
		}
	
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preSendAgentSetModify");
	}
	/**
	 * 保存会员每次充值返水代理   打码返水代理
	 */
	public ActionForward saveSendAgentSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		String type=paramForm.getType();
		String time = paramForm.getTime();
		String value = paramForm.getValue();
		
		if(type.equals("1")){
			param.setValue(value);
		}else if(type.equals("2")){
			param.setValue(time+"&"+value);
		}
		paramService.saveParam(param);
		if(type.equals("2")){
			CacheUtil.updateAgentBetBack();//更新缓存
		}
		ActionForward forward = mapping.findForward("saveSendAgentSet");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&type=" + type);
		return new ActionForward(forward.getName(), path.toString(), true);
	}
	
	/**
	 * 注册会员赠送金额设置
	 */
	public ActionForward registerSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_REGISTER_SEND);
		request.setAttribute("param", param);
		return mapping.findForward("registerSendSet");
	}
	
	/**
	 * 注册会员赠送金额设置
	 */
	public ActionForward preRegisterSendSetModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_REGISTER_SEND);			
		paramForm.setParam(param);
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preRegisterSendSetModify");
	}
	
	/**
	 * 保存注册会员系统赠送设置
	 */
	public ActionForward saveRegisterSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		paramService.saveParam(param);
		return registerSendSet(mapping, form, request, response);
	}
	
	/**
	 * 签到赠送金额设置
	 */
	public ActionForward signSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_SIGN_SEND);
		request.setAttribute("param", param);
		return mapping.findForward("signSendSet");
	}
	
	/**
	 * 签到赠送金额设置
	 */
	public ActionForward preSignSendSetModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_SIGN_SEND);			
		paramForm.setParam(param);
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preSignSendSetModify");
	}
	
	/**
	 * 保存签到系统赠送设置
	 */
	public ActionForward saveSignSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		paramService.saveParam(param);
		return signSendSet(mapping, form, request, response);
	}
	
	/**
	 * 任务赚钱设置
	 */
	public ActionForward taskSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_TASK_SEND);
		request.setAttribute("param", param);
		return mapping.findForward("taskSendSet");
	}
	
	
	/**
	 * 任务赚钱设置
	 */
	public ActionForward preTaskSendSetModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_TASK_SEND);			
		paramForm.setParam(param);
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preTaskSendSetModify");
	}
	
	
	/**
	 * 保存任务赚钱设置
	 */
	public ActionForward saveTaskSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		paramService.saveParam(param);
		return taskSendSet(mapping, form, request, response);
	}
	
	/**
	 * qq客服地址设置
	 */
	public ActionForward qqSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_QQ_CUSTOMER_URL);
		request.setAttribute("param", param);
		return mapping.findForward("qqSendSet");
	}
	
	
	/**
	 * qq客服地址设置
	 */
	public ActionForward preQQSendSetModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramService.getParamByType(Constants.PARAM_QQ_CUSTOMER_URL);			
		paramForm.setParam(param);
		request.setAttribute("paramForm", paramForm);
		request.setAttribute("startIndex", ParamUtils.getIntParameter(request,
				"pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preQQSendSetModify");
	}
	
	
	/**
	 * 保存qq客服地址设置
	 */
	public ActionForward saveQQSendSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		ParamForm paramForm = (ParamForm) form;
		Param param = paramForm.getParam();
		paramService.saveParam(param);
		return qqSendSet(mapping, form, request, response);
	}
	
	/**
	 * 设置游戏开奖
	 */
	public ActionForward gameOpenSetting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception, NoFunctionPermissionException {
//		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
//		String loginUserType = loginUser.getUserType();//登录用户类型
//		//管理员和超级管理员才能查询
//		if(Constants.USER_TYPE_ADMIN.equals(loginUserType)
//				||Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)){
			return mapping.findForward("gameOpenSetting");
//		}else{
//			request.setAttribute("msg", "无权限！请联系管理员！");
//			return mapping.findForward("error");
//		}
	}
	public void saveParam(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		Integer paramId = ParamUtils.getIntParameter(request, "paramId", 0);
		String type = ParamUtils.getParameter(request, "type");
		String value = ParamUtils.getParameter(request, "value");
		String title = ParamUtils.getParameter(request, "title");
		
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		
		if (!ParamUtils.chkInteger(paramId) || !ParamUtils.chkString(type)
				|| !ParamUtils.chkString(value)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		
		if (message.equals("")) {
			User loginUser = this.getUser(request);
			String loginUserType = loginUser.getUserType();
			
			if(loginUserType.equals(Constants.USER_TYPE_ADMIN)||loginUserType.equals(Constants.USER_TYPE_SUPERADMIN)){
				Param param = CacheUtil.getParam(type);
				if(param!=null){
					Integer paramId2 = param.getParamId();
					if(paramId.intValue()==paramId2.intValue()){
						StringBuffer loginText = new StringBuffer();
						String remark="";
						if(!ParamUtils.chkString(title)){
							title=param.getTitle();
						}
						
						if(type.equals(Constants.PARAM_BJ3_OPEN_NUM)){
							remark="["+title+"]，原次数["+param.getValue()+"]，现次数["+value+"]";
						}else if(type.equals(Constants.PARAM_SFPK10_OPEN_NUM)){
							remark="["+title+"]，原次数["+param.getValue()+"]，现次数["+value+"]";	
						}else if(type.equals(Constants.PARAM_JSFT_OPEN_NUM)){
							remark="["+title+"]，原次数["+param.getValue()+"]，现次数["+value+"]";		
						}else if(type.equals(Constants.PARAM_RECHARGE_ONLINE)||type.equals(Constants.PARAM_RECHARGE_OFFLINE)||
								type.equals(Constants.PARAM_CASH_SEND_MSG)||type.equals("20")||type.equals("21")||type.equals("22")||type.equals("23")||
								type.equals("24")||type.equals("25")||type.equals("26")||type.equals("27")){
							if("1".equals(param.getValue())){
								remark="["+title+"]切换，状态[开变关]";	
							}else{
								remark="["+title+"]切换，状态[关变开]";	
							}
						}else if(type.equals(Constants.PARAM_REG_SWITCH)){
							if("1".equals(param.getValue())){
								remark="["+title+"]切换，状态[验证码变图形码]";
							}else{
								remark="["+title+"]切换，状态[图形码变验证码]";	
							}
						}else if(type.equals(Constants.PARAM_CHATROOM)){
							remark="["+title+"]，原链接["+param.getValue()+"]，现链接["+value+"]";
						}else if(type.equals(Constants.PARAM_TASK_SEND)){
							remark="["+title+"]，原链接["+param.getValue()+"]，现链接["+value+"]";
						}
						if(ParamUtils.chkString(title)){
							param.setTitle(title);
						}
						param.setValue(value);
						
						paramService.saveParam(param);
						
					
						loginText.append(remark+"，操作人登录名[");
						loginText.append(loginUser.getLoginName());
						loginText.append("]，操作人ID[");
						loginText.append(loginUser.getUserId());
						loginText.append("]");
						userService.updateUserLog(request,loginUser,loginText.toString());
						
						code=APIConstants.CODE_REQUEST_SUCCESS;
					}else{
						message="出错了";
					}
				}else{
					message="出错了";
				}
			}else{
				message="无此权限";
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response,map);
	}
	
	public void loadParam(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONArray items = new JSONArray();// 数据集合
		JSONObject obj = null;
		if (message.equals("")) {
			List<Param> list = CacheUtil.getParam();
			if (list != null && list.size() > 0) {
				for (Param para : list) {
					obj = new JSONObject();
					Integer paramId = para.getParamId();
					String title = para.getTitle();
					String value = para.getValue();
					String type = para.getType();
					
					obj.put("paramId", paramId);
					obj.put("type", ParamUtils.chkStringNotNull(type));
					obj.put("title", ParamUtils.chkStringNotNull(title));
					obj.put("value", ParamUtils.chkStringNotNull(value));
					obj.put("status", ParamUtils.chkStringNotNull(para.getStatus()));
					obj.put("beizhu", ParamUtils.chkStringNotNull(para.getBeizhu()));
					items.put(obj);
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				code = APIConstants.CODE_NOT_FOUND;
			}
			data.put("items", items);
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
}
