package com.ram.web.permission.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.ram.model.Function;
import com.ram.model.User;
import com.ram.service.permission.IFunctionService;
import com.ram.web.permission.form.FunctionForm;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class FunctionModifyAction extends BaseDispatchAction {

	private IFunctionService functionService = (IFunctionService) getService("functionService");

	public final ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		Integer IntegerFunctionId = (Integer) request
				.getAttribute("functionId");

		int functionId = IntegerFunctionId.intValue();

		Function function = (Function) functionService.getFunction(new Integer(
				functionId));
		FunctionForm functionForm = (FunctionForm) form;
		functionForm.setFunction(function);
		functionForm.setCurrentFunctionID(function.getFunctionId().intValue());
		functionForm.setParentFunctionID(function.getParentId().intValue());

		request.setAttribute("functionId", function.getFunctionId());

		List pfLabelValueBeanList = null;

		if (function.getParentId().intValue() == 0) {
			// 如果当前是第一级目录，则在第一级前加根
			LabelValueBean parentLevelFunction = new LabelValueBean();
			parentLevelFunction.setLabel("根");
			parentLevelFunction.setValue("0");
			pfLabelValueBeanList = new ArrayList();
			pfLabelValueBeanList.add(parentLevelFunction);
			// 加根后，在增加第一级的菜单

			List parentLevelFunctionList = functionService
					.findAllSubFunctions(0);

			for (int i = 0; i < parentLevelFunctionList.size(); i++) {

				Function pFunction = (Function) parentLevelFunctionList.get(i);
				parentLevelFunction = new LabelValueBean();
				parentLevelFunction.setLabel(pFunction.getFunctionTitle());
				parentLevelFunction.setValue(pFunction.getFunctionId()
						.toString());
				pfLabelValueBeanList.add(parentLevelFunction);
			}
		} else {

			Function parentFunction = (Function) functionService
					.getFunction(function.getParentId());
			if (parentFunction != null) {

				List parentLevelFunctionList = functionService
						.findAllSubFunctions(parentFunction.getParentId()
								.intValue());
				LabelValueBean parentLevelFunction = null;
				pfLabelValueBeanList = new ArrayList();
				// /
				parentLevelFunction = new LabelValueBean();
				parentLevelFunction.setLabel("..");
				parentLevelFunction.setValue(String.valueOf(parentFunction
						.getParentId().intValue()));
				pfLabelValueBeanList.add(parentLevelFunction);
				//
				for (int i = 0; i < parentLevelFunctionList.size(); i++) {

					Function f = (Function) parentLevelFunctionList.get(i);

					if (f != null) {
						// 如果功能不为空，那么加到下拉列表中
						parentLevelFunction = new LabelValueBean();
						parentLevelFunction.setLabel(f.getFunctionTitle());
						parentLevelFunction.setValue(String.valueOf(f
								.getFunctionId().intValue()));
						pfLabelValueBeanList.add(parentLevelFunction);

						// 下面得到该功能的下级菜单列表
						List subFunctionList = functionService
								.findAllSubFunctions(f.getFunctionId()
										.intValue());
						// 把当前菜单的下级子菜单也加入到下拉列表

						for (int k = 0; k < subFunctionList.size(); k++) {
							Function subFunction = (Function) subFunctionList
									.get(k);

							parentLevelFunction = new LabelValueBean();
							parentLevelFunction.setLabel("——"
									+ subFunction.getFunctionTitle());
							parentLevelFunction.setValue(String
									.valueOf(subFunction.getFunctionId()
											.intValue()));
							pfLabelValueBeanList.add(parentLevelFunction);
						}
					}
				}
			} else {

				LabelValueBean parentLevelFunction = new LabelValueBean();
				parentLevelFunction.setLabel("根");
				parentLevelFunction.setValue("0");
				pfLabelValueBeanList = new ArrayList();
				pfLabelValueBeanList.add(parentLevelFunction);
			}
		}
		request.setAttribute("pfLabelValueBeanList", pfLabelValueBeanList);
		return mapping.findForward("init");
	}

	/**
	 * 保存Post过来的功能修改后的数据
	 * 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		log.info("开始修改一个function");
		// 获取正在修改的function的id值
//		log.info("---111");
		int functionId = ParamUtils.getIntParameter(request, "functionId", 0);
		Function function = functionService
				.getFunction(new Integer(functionId));
		//获得当前修改的function的父functionid
		int pFunctionIdOfThisFunction=function.getFunctionId().intValue();
//		log.info("---222");
		// 从form中获取function
		FunctionForm functionForm = (FunctionForm) form;
		Function pageFunction = functionForm.getFunction();
		// 将get出来的对象设置新值
//		log.info("---333");
		function.setFunctionLinkAddress(pageFunction.getFunctionLinkAddress());
		function.setFunctionTitle(pageFunction.getFunctionTitle());
		function.setParentId(pageFunction.getParentId());
		function.setOrderSn(pageFunction.getOrderSn());
//		log.info("---444");
		if (function.getParentId().intValue() == 0) {
			function.setRootId(new Integer(0));
//			log.info("---555");
		} else {
			Function pFunction = functionService.getFunction(function
					.getParentId());
			function.setRootId(new Integer(this.getRootId(function
					.getFunctionId().intValue(), function.getParentId()
					.intValue())));
//			log.info("---666");
		}
		//判断一下所修改的这个function的父function下是否还有子function
//		log.info("---777");
		List subFuncOfThisFunction=functionService.findAllSubFunctions(pFunctionIdOfThisFunction);
		if(subFuncOfThisFunction.size()==0){
//			log.info("---888");
			//如果没有子function了，那么把SubFunctionNumber字段设置为0
			function.setSubFunctionNumber(new Integer(0));
		}else{
//			log.info("---999");
			function.setSubFunctionNumber(new Integer(subFuncOfThisFunction.size()));
		}
		// 保存对象
//		log.info("---000");
		functionService.saveFunction(function, user);
//		log.info("---abba");
		request.setAttribute("parentFunctionId", String.valueOf(function
				.getParentId()));
		userLogService.saveLog(user, "开始修改一个功能：" + function.getFunctionTitle());
		return mapping.findForward("save");
	}

	/**
	 * 递归获取当前功能的rootId
	 * 
	 * @param functionId
	 * @param parentId
	 * @return
	 */
	private int getRootId(int functionId, int parentId) {
		if (parentId == 0) {
			return 0;
		} else {
			Function f = functionService.getFunction(new Integer(functionId));
			if (f.getParentId().intValue() == 0) {
				return f.getFunctionId().intValue();
			} else {
				Function pf = functionService.getFunction(f.getParentId());
				return getRootId(pf.getFunctionId().intValue(), parentId);
			}
		}

	}
}
