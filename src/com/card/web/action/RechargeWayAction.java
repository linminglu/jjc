package com.card.web.action;

import help.base.APIConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.model.Type;
import com.apps.util.JsonUtil;
import com.apps.util.UploadUtil;
import com.apps.web.form.TypeForm;
import com.card.model.Card;
import com.card.model.RechargeWay;
import com.card.service.ICardService;
import com.card.service.IRechargeWayService;
import com.card.service.IUserCardService;
import com.card.web.form.CardForm;
import com.card.web.form.RechargeWayForm;
import com.card.web.form.UserCardForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.exception.BusinessException;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

/**
 * 充值方式
 */
public class RechargeWayAction extends BaseDispatchAction{
	private final IRechargeWayService rechargeWayService = (IRechargeWayService)getService("rechargeWayService");
	
	/**
	 * 充值方式列表
	 */
	public ActionForward initRechargeWay(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		String passageWayId = ParamUtils.getParameter(request, "passageWayId");// 通道id
		String channelId = ParamUtils.getParameter(request, "channelId");// 渠道id
		RechargeWayForm rechargeWayForm = (RechargeWayForm) form;
		rechargeWayForm.setPassageWayId(Integer.valueOf(passageWayId));
		HQUtils hq = new HQUtils("from RechargeWay rw where rw.parentId=? and rw.type=?");
		hq.addPars(passageWayId);
		hq.addPars(Constants.RECHARGE_WAY_2);
		List<Object> rwList = rechargeWayService.findObjects(hq);
		List<Object> rwList2 = new ArrayList<Object>();
		Integer id = 0;
		if (ParamUtils.chkString(channelId)) {
			id = Integer.valueOf(channelId);
			rechargeWayForm.setChannelId(id);
		} else {
			if (rwList.size() > 0) {
				RechargeWay rw = (RechargeWay) rwList.get(0);
				id = rw.getId();
			}
		}
		if (ParamUtils.chkInteger(id)) {
			HQUtils hq2 = new HQUtils("from RechargeWay rw where rw.parentId=? and rw.type=?");
			hq2.addPars(id);
			hq2.addPars(Constants.RECHARGE_WAY_3);
			rwList2 = rechargeWayService.findObjects(hq2);
		}
		request.setAttribute("rwList", rwList);
		request.setAttribute("rwList2", rwList2);
		request.setAttribute("rechargeWayForm", rechargeWayForm);
		return mapping.findForward("initRechargeWay");
	}
	
	
	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		RechargeWay rechargeWay = (RechargeWay) rechargeWayService.getObject(RechargeWay.class, id);
		String status = rechargeWay.getStatus();
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				rechargeWay.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				rechargeWay.setStatus(Constants.PUB_STATUS_OPEN);
			}
			rechargeWayService.saveObject(rechargeWay, null);
			code=APIConstants.CODE_REQUEST_SUCCESS;
			message="切换状态成功！";
		} catch (Exception e) {
			message="切换状态失败！";
			e.printStackTrace();
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}
	
	
	/**
	 * 创建和修改充值方式
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		RechargeWayForm rechargeWayForm = (RechargeWayForm) form;
		String passageWayId = ParamUtils.getParameter(request, "passageWayId");// 通道id
		String wayId = ParamUtils.getParameter(request, "wayId");// 方法id
		
		if (ParamUtils.chkString(wayId)) {
			RechargeWay rechargeWay = (RechargeWay) rechargeWayService
					.getObject(RechargeWay.class, Integer.valueOf(wayId));
			rechargeWayForm.setRechargeWay(rechargeWay);
		}
		rechargeWayForm.setPassageWayId(Integer.valueOf(passageWayId));
		HQUtils hq = new HQUtils("from RechargeWay rw where rw.parentId=? and rw.type=?");
		hq.addPars(passageWayId);
		hq.addPars(Constants.RECHARGE_WAY_2);
		List<Object> rwList = rechargeWayService.findObjects(hq);
		request.setAttribute("rwList", rwList);
		request.setAttribute("rechargeWayForm", rechargeWayForm);
		return mapping.findForward("preModify");
	}
	
	/**
	 * 保存充值方式
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		RechargeWayForm rechargeWayForm = (RechargeWayForm) form;
		RechargeWay rechargeWay = rechargeWayForm.getRechargeWay();
		Integer passageWayId = rechargeWayForm.getPassageWayId();
		FormFile file = rechargeWayForm.getFile();
		Integer id = rechargeWay.getId();
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/rechargeWay";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
		}
		if(ParamUtils.chkInteger(id)){// 修改
			RechargeWay old = (RechargeWay) rechargeWayService
						.getObject(RechargeWay.class, id);
			old.setTitle(rechargeWay.getTitle());
			old.setParentId(rechargeWay.getParentId());
			old.setBankName(rechargeWay.getBankName());
			old.setBankAccount(rechargeWay.getBankAccount());
			old.setUserName(rechargeWay.getUserName());
			old.setDes(rechargeWay.getDes());
			old.setUrl(rechargeWay.getUrl());
			if (ParamUtils.chkString(imgUrl)) {
				old.setImg(imgUrl);
			}
			rechargeWayService.saveObject(old, null);
		} else {
			RechargeWay newRw = new RechargeWay();
			newRw.setStatus(Constants.PUB_STATUS_OPEN);
			newRw.setType(Constants.RECHARGE_WAY_3);
			newRw.setTitle(rechargeWay.getTitle());
			newRw.setParentId(rechargeWay.getParentId());
			newRw.setBankName(rechargeWay.getBankName());
			newRw.setBankAccount(rechargeWay.getBankAccount());
			newRw.setUserName(rechargeWay.getUserName());
			newRw.setDes(rechargeWay.getDes());
			newRw.setUrl(rechargeWay.getUrl());
			if (ParamUtils.chkString(imgUrl)) {
				newRw.setImg(imgUrl);
			}
			rechargeWayService.saveObjectDB(newRw);
		}

		ActionForward forward = mapping.findForward("save");			
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&passageWayId=" + passageWayId);
		return new ActionForward(forward.getName(), path.toString(), true);
	}
	
	
	/**
	 * 删除充值方式
	 */
	public void del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		JSONObject jo = new JSONObject();
		String code = APIConstants.CODE_REQUEST_ERROR;
		if(ParamUtils.chkInteger(id)){
			 rechargeWayService.deleteObject(RechargeWay.class, id, null);
			 code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		jo.put("code", code);
		JsonUtil.AjaxWriter(response, jo);
	}
}