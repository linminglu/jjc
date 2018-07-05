package com.apps.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.util.JsonUtil;
import com.apps.web.form.GameForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.model.dto.GaBetOptionDto;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;

/**
 * 基本功能
 * 
 * @author Mr.zang
 * 
 */
public class GameAction extends BaseDispatchAction {
	private final IGaService gaService = (IGaService) getService("gaService");
	/**
	 * 彩种管理
	 */
	@SuppressWarnings("unchecked")
	public ActionForward gameList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		GameForm gameForm = (GameForm) form;
		gameForm.setStartIndex(String.valueOf(startIndex));
		String title = gameForm.getTitle();// 彩票名称
		String playCate = gameForm.getPlayCate();// 彩种类型  1=官方   2=信用
		String status = gameForm.getStatus(); //状态。默认为1
		String betAvoid = gameForm.getBetAvoid(); //是否能投注
		
		
		if (ParamUtils.chkString(title)) {
			hqls.append(" and ho.gameTitle like ?");
			para.add("%" + title.trim() + "%");
		}
		
		if (ParamUtils.chkString(playCate)) {
			hqls.append(" and ho.playCate = ?");
			para.add(playCate);
		}
		
		if (ParamUtils.chkString(status)) {
			hqls.append(" and ho.status = ?");
			para.add(status);
		}
		
		if (ParamUtils.chkString(betAvoid)) {
			hqls.append(" and ho.betAvoid = ?");
			para.add(betAvoid);
		}
		
		hqls.append(" order by ho.sort  asc ");
		PaginationSupport ps = gaService.findGaSessionInfoList(
				hqls.toString(), para, startIndex, pageSize);
		List<GaSessionInfo> list = new ArrayList<GaSessionInfo>();
		list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("title", title);
		request.setAttribute("playCate",playCate);
		request.setAttribute("status", status);
		request.setAttribute("betAvoid", betAvoid);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		return mapping.findForward("gameList");
	}
	
	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer infoId = ParamUtils.getIntegerParameter(request, "infoId");
		GaSessionInfo gaSessionInfo = (GaSessionInfo) gaService.getObject(
				GaSessionInfo.class, infoId);
		String status = gaSessionInfo.getStatus();
		String flag = "success";
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				gaSessionInfo.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				gaSessionInfo.setStatus(Constants.PUB_STATUS_OPEN);
			}
			gaService.saveObject(gaSessionInfo, null);
			CacheUtil.updateGameList();
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
	/**
	 * 更改投注状态
	 */
	public void changeBetAvoid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		Integer infoId = ParamUtils.getIntegerParameter(request, "infoId");
		GaSessionInfo gaSessionInfo = (GaSessionInfo) gaService.getObject(
				GaSessionInfo.class, infoId);
		String betAvoid = gaSessionInfo.getBetAvoid();
		String flag = "success";
		try {
			if (betAvoid.equals(Constants.PUB_STATUS_OPEN)) {
				gaSessionInfo.setBetAvoid(Constants.PUB_STATUS_CLOSE);
			} else {
				gaSessionInfo.setBetAvoid(Constants.PUB_STATUS_OPEN);
			}
			gaService.saveObject(gaSessionInfo, null);
			CacheUtil.updateGameList();
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
	/**
	 * 查询投注项
	 */
	public ActionForward gameOptionList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		Integer infoId = ParamUtils.getIntegerParameter(request, "infoId");
		String title = ParamUtils.getParameter(request, "title");
		
		GaSessionInfo gaSessionInfo = (GaSessionInfo) gaService.getObject(
				GaSessionInfo.class, infoId);
		
		if(ParamUtils.chkInteger(infoId)){
			
		}
		HQUtils hq = new HQUtils();
		hq.addHsql(" from GaBetOption gbo where 1=1 ");
		hq.addHsql(" and gbo.gameType = ?");
		hq.addPars(gaSessionInfo.getGameType());
		if(ParamUtils.chkString(title)){
			hq.addHsql(" and (gbo.title like ? or gbo.optionTitle like ?)");
			hq.addPars("%"+title+"%");
			hq.addPars("%"+title+"%");
		}
		hq.setOrderby(" order by gbo.betOptionId");
		hq.setStartIndex(startIndex);
		hq.setPageSize(pageSize);
		
		List<Object> playTypeStrs = new ArrayList<Object>();
		if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJPK10)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFPK10)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFPK102)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_XYFT)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JSFT)){
			playTypeStrs.add("两面盘");
			playTypeStrs.add("1-10球");
			playTypeStrs.add("冠亚军和");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_CQSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_XJSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_TJSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JXSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJ3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_FC)){
			playTypeStrs.add("两面盘");
			playTypeStrs.add("1-5球");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJKL8)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_XJPLU28)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJLU28)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_POKER)
				){
			playTypeStrs.add("两面盘");
			playTypeStrs.add("特码");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GDK10)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GXK10)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_CQK10)){
			playTypeStrs.add("两面盘");
			playTypeStrs.add("第一球");
			playTypeStrs.add("第二球");
			playTypeStrs.add("第三球");
			playTypeStrs.add("第四球");
			playTypeStrs.add("第五球");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_MARKSIX)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFLHC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_CQK10)){
			playTypeStrs.add("特码A");
			playTypeStrs.add("特码B");
			playTypeStrs.add("正码");
			playTypeStrs.add("正1特");
			playTypeStrs.add("正2特");
			playTypeStrs.add("正3特");
			playTypeStrs.add("正4特");
			playTypeStrs.add("正5特");
			playTypeStrs.add("正6特");
			playTypeStrs.add("正码1-6");
			playTypeStrs.add("过关");
			playTypeStrs.add("二全中");
			playTypeStrs.add("二中特");
			playTypeStrs.add("特串");
			playTypeStrs.add("三全中");
			playTypeStrs.add("三中二");
//			playTypeStrs.add("四全中");
			playTypeStrs.add("半波");
			playTypeStrs.add("一肖");
			playTypeStrs.add("尾数");
			playTypeStrs.add("特码生肖");
			playTypeStrs.add("二肖");
			playTypeStrs.add("三肖");
			playTypeStrs.add("四肖");
			playTypeStrs.add("五肖");
			playTypeStrs.add("六肖");
			playTypeStrs.add("七肖");
			playTypeStrs.add("八肖");
			playTypeStrs.add("九肖");
			playTypeStrs.add("十肖");
			playTypeStrs.add("十一肖");
			playTypeStrs.add("二肖连中");
			playTypeStrs.add("四肖连中");
			playTypeStrs.add("五肖连中");
			playTypeStrs.add("二肖连不中");
			playTypeStrs.add("三肖连不中");
			playTypeStrs.add("四肖连不中");
			playTypeStrs.add("二尾连中");
			playTypeStrs.add("三尾连中");
			playTypeStrs.add("四尾连中");
			playTypeStrs.add("二尾连不中");
			playTypeStrs.add("三尾连不中");
			playTypeStrs.add("四尾连不中");
			playTypeStrs.add("五不中");
			playTypeStrs.add("六不中");
			playTypeStrs.add("七不中");
			playTypeStrs.add("八不中");
			playTypeStrs.add("九不中");
			playTypeStrs.add("十不中");
			playTypeStrs.add("十一不中");
			playTypeStrs.add("十二不中");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GDPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JXPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GXPICK11)){
			playTypeStrs.add("两面盘");
			playTypeStrs.add("1-5球");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JSK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJK3)){
			playTypeStrs.add("两面盘");
			playTypeStrs.add("两连");
		}
		
		PaginationSupport ps = gaService.findObjectPage(hq);
		List<Object> dtoList = new ArrayList<Object>();
		for(Object obj:ps.getItems()){
			GaBetOption gbo = (GaBetOption) obj;
			GaBetOptionDto gboDto = new GaBetOptionDto(gbo);
			gboDto.setPlayTypeStr(""+playTypeStrs.get(Integer.valueOf(gbo.getPlayType())));
			dtoList.add(gboDto);
		}
		request.setAttribute("gameTitle", gaSessionInfo.getGameTitle());
		request.setAttribute("infoId", ""+infoId);
		request.setAttribute("title",title);
		request.setAttribute("playTypeStrs", playTypeStrs);
		request.setAttribute("list", dtoList);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		
		String flag = "success";
		
		return mapping.findForward("gameOptionList");
	}

	/**
	 * 更改赔率
	 */
	public void changeBetRate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		Integer infoId = ParamUtils.getIntegerParameter(request, "infoId");
		String rateStr = ParamUtils.getParameter(request, "rate");
		BigDecimal betRate = new BigDecimal(rateStr);
//		GaSessionInfo gaSessionInfo = (GaSessionInfo) gaService.getObject(
//				GaSessionInfo.class, infoId);
		// 对betrate的验证
		String flag = "success";
		try {
			GaBetOption gbo = (GaBetOption) gaService.getObject(GaBetOption.class, infoId);
			if(gbo!=null){
				gbo.setBetRate(betRate);
			}
			gaService.saveObject(gbo, null);
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}

}
