package com.apps.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.GameCacheUtil;
import com.apps.model.Param;
import com.apps.model.dto.AgentDTO;
import com.apps.service.IAgentService;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.apps.web.form.AgentForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ManagerUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaBetOption;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaSessionInfo;
import com.game.model.dto.GaDTO;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.ram.model.dto.UserDTO;

import help.base.APIConstants;

public class AgentAction extends BaseDispatchAction{
	private final IAgentService agentService = (IAgentService) getService("agentService");
	private final IGaService gaService = (IGaService) getService("gaService");
	

	
	/**
	 * 用户报表
	 */
	public ActionForward userReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AgentForm agentForm = (AgentForm) form;
		String startDate = agentForm.getStartDate();
		String endDate = agentForm.getEndDate();
		Integer totalBet = 0; //总投注
		BigDecimal bet =  new BigDecimal(0); //总投注
		BigDecimal win = new BigDecimal(0);//中奖彩派
		BigDecimal totalWin = new BigDecimal(0);//中奖彩派
		BigDecimal totalPayoff = new BigDecimal(0); //盈利
		BigDecimal totalRechage = new BigDecimal(0);//充值
		BigDecimal totalDrawMoney = new BigDecimal(0);//累计提现
		BigDecimal rechageDrawPayoff = new BigDecimal(0);//出入款差
		BigDecimal money = new BigDecimal(0);//账户余额
		BigDecimal decuteMoney=new BigDecimal(0);//后台扣款
		BigDecimal incomeMoney=new BigDecimal(0);//入款总计
		BigDecimal outMoney=new BigDecimal(0);//出款总计
		BigDecimal turnTableMoney = new BigDecimal(0);//转盘
		BigDecimal redPacketsMoney = new BigDecimal(0); //红包
//		BigDecimal fanshui = new BigDecimal(0); //返水
		BigDecimal memberBetReturn = new BigDecimal(0); //返水
		BigDecimal memberRechargeReturn = new BigDecimal(0); //返水
		BigDecimal memberBetReturnSelf = new BigDecimal(0); //返水
		BigDecimal memberReturnSelf = new BigDecimal(0); //充值赠送
		BigDecimal otherDecuteMoney = new BigDecimal(0); //其它扣款
		BigDecimal otherRechage = new BigDecimal(0);//其它加款
		BigDecimal repairDetailAdd = new BigDecimal(0); //修复明细--加款
		BigDecimal repairDetailSub = new BigDecimal(0);//修复明细--扣款
	
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		
		Integer userid=ParamUtils.getIntegerParameter(request, "userId");//传来的id
		Integer loginUserId = loginUser.getUserId();//登录用户id
		String loginUserType = loginUser.getUserType();//登录用户类型
		
		//共用的查询条件
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		if (Constants.USER_TYPE_ADMIN.equals(loginUserType)
				|| Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)) {//管理员登录
		}else if(Constants.USER_TYPE_AGENT_ONE.equals(loginUserType)||
				Constants.USER_TYPE_AGENT_TWO.equals(loginUserType)||
				Constants.USER_TYPE_AGENT_THREE.equals(loginUserType)){//代理用户登录
			   if(!loginUserId.equals(userid)){ //只能查询自己及自己的下级会员
					hqls.append(" and u.agentId = ?  ");
				    para.add(loginUserId);   
			   }
		}
		hqls.append(" and  u.userId= ? ");
		para.add(userid);

		if(!ParamUtils.chkInteger(userid)){
			request.setAttribute("msg", "传入参数为空");
			return mapping.findForward("error");
		}else{
			List<User> userList = userService.findUserList(hqls.toString(), para);
			if(userList != null && userList.size() >0){
				User temp = userList.get(0);
				money=ProductUtil.checkBigDecimal(temp.getMoney()); //用户余额
			}else{
				request.setAttribute("msg", "用户不存在");
				return mapping.findForward("error");
			}
		}

		if (Constants.USER_TYPE_ADMIN.equals(loginUserType)
				|| Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)) {
			loginUserId = 0 ;
		}
		if(!ParamUtils.chkString(startDate)||! ParamUtils.chkString(endDate)){
			startDate = DateTimeUtil.DateToString(new Date());
			endDate = DateTimeUtil.getMonthEndDay();
			agentForm.setStartDate(startDate);
			agentForm.setEndDate(endDate);
		}
		
		
		List<Object> para6 = new ArrayList<Object>();
		StringBuffer hsql6 = new StringBuffer(hqls);
		para6.addAll(para);
		//代理及会员交易记录
		if(ParamUtils.chkString(startDate) && ParamUtils.chkString(endDate)){
			hsql6.append(" and ho.createTime >= ? ");
			para6.add(startDate + " 00:00:00");
			hsql6.append(" and ho.createTime <= ? ");
			para6.add(endDate + " 23:59:59");
		}
		hsql6.append(" and ho.status = ? ");
		para6.add(Constants.PUB_STATUS_OPEN);
		hsql6.append(" group by ho.cashType ");
		List<GaDTO> tradelist = null; //代理及会员交易记录
		tradelist = gaService.findUserTradeDetailList(hsql6.toString(),para6);
		for(GaDTO tradeDto: tradelist){
			String cashType = tradeDto.getCashType();
			BigDecimal paperMoney = tradeDto.getPaperMoney();
			if( cashType != null && paperMoney != null){
				if(Constants.CASH_TYPE_ONLINE.equals(cashType)||Constants.CASH_TYPE_MANAGER_SET.equals(cashType)){//充值= 在线充值+管理员充值
					totalRechage = totalRechage.add(paperMoney);
				}else if(Constants.CASH_TYPE_CASH_OUT.equals(cashType)){//提现
					totalDrawMoney = totalDrawMoney.add(paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN));
				}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN.equals(cashType)){//下级投注返水
					memberBetReturn = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN.equals(cashType)){//下级充值返水
					memberRechargeReturn = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF.equals(cashType)){//自己投注返水
					memberBetReturnSelf = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_MEMBER_RETURN_SELF.equals(cashType)){//充值赠送
					memberReturnSelf = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_PRIZE.equals(cashType)){//中奖彩派
					win = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_TURN_TABLE.equals(cashType)){//转盘
					turnTableMoney = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_RED_PACKETS.equals(cashType)){//红包
					redPacketsMoney = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType)){//投注
					bet = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}else if(Constants.CASH_TYPE_CASH_SYS_CHARGE.equals(cashType)){//后台扣款
					decuteMoney = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}else if(Constants.CASH_TYPE_CASH_RETURN.equals(cashType)){// 提现审核返回
					totalDrawMoney = totalDrawMoney.subtract(paperMoney); //提现减去审核返回金额
				}else if(Constants.CASH_TYPE_CASH_OTHER_SET.equals(cashType)){//其它加款
					otherRechage = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_OTHER_CHARGE.equals(cashType)){//其它扣款
					otherDecuteMoney = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}else if(Constants.CASH_TYPE_CASH_REPAIR_DETAIL_ADD.equals(cashType)){//修复明细--加款
					repairDetailAdd = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_REPAIR_DETAIL_SUB.equals(cashType)){//修复明细--扣款
					repairDetailSub = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}
		}
		incomeMoney = totalRechage.add(memberReturnSelf).add(win).add(turnTableMoney).add(redPacketsMoney)
				.add(memberBetReturn).add(memberBetReturnSelf).add(memberRechargeReturn).add(otherRechage)
				.add(repairDetailAdd);
		outMoney = totalDrawMoney.add(bet).add(decuteMoney).add(otherDecuteMoney).add(repairDetailSub);
		rechageDrawPayoff = incomeMoney.subtract(outMoney);
		
		//查询每一个彩种的投注、中奖、盈亏
		List<Object> para4 = new ArrayList<Object>();
		StringBuffer hqls4 = new StringBuffer(hqls);
		para4.addAll(para);
		
		hqls4.append(" and ho.betTime >= ? ");
		para4.add(startDate+" 00:00:00");
		hqls4.append(" and ho.betTime <= ? ");
		para4.add(endDate+" 23:59:59");
		
		hqls4.append("group by ho.gameType ");
		
		List<AgentDTO> betAndWinList = new ArrayList<AgentDTO>();
		List<String> gameTypeList = new ArrayList<String>();
        Map<String,String> map = new LinkedHashMap<String,String>();
        map.put(Constants.GAME_TYPE_GF_THREE, "北京三分彩");
        map.put(Constants.GAME_TYPE_GF_BJPK10, "北京赛车pk10");
        map.put(Constants.GAME_TYPE_GF_XJPLU28, "新加坡幸运28");
        map.put(Constants.GAME_TYPE_GF_CQSSC, "重庆时时彩");
        map.put(Constants.GAME_TYPE_GF_BJLU28, "pc蛋蛋");
        map.put(Constants.GAME_TYPE_GF_GDK10, "广东快乐10分");
        map.put(Constants.GAME_TYPE_GF_TJSSC, "天津时时彩");
        map.put(Constants.GAME_TYPE_GF_XJSSC, " 新疆时时彩");
        map.put(Constants.GAME_TYPE_GF_POKER, "快乐扑克3");
        map.put(Constants.GAME_TYPE_GF_GDPICK11, "广东11选5");
        map.put(Constants.GAME_TYPE_GF_JSK3, "江苏快乐3");
        map.put(Constants.GAME_TYPE_GF_GXK10, "广西快乐十分");
        map.put(Constants.GAME_TYPE_GF_MARKSIX, "香港六合彩");
//        map.put(Constants.GAME_TYPE_GF_SFPK10, "极速赛车");

        List<AgentDTO> gameBetAndWinList;
		gameBetAndWinList = agentService.findGameBetAndWin(hqls4.toString(), para4);
		
		for(String key:map.keySet()) {
			AgentDTO dto = null;
			if(gameBetAndWinList != null && gameBetAndWinList.size()>0){
		        for(int i = 0;i<gameBetAndWinList.size();i++){
		        	dto = gameBetAndWinList.get(i);
		        	if(key.equals(dto.getGameType())){
						betAndWinList.add(dto);
						gameTypeList.add(key);
						break;
		        	}
		        }
			}
	        if(!gameTypeList.contains(key)){
				dto = new AgentDTO(map.get(key),key, 0, new BigDecimal(0), new BigDecimal(0));
				betAndWinList.add(dto);
	        }
			totalBet=totalBet+dto.getBetMoney();
			totalWin=totalWin.add(ProductUtil.checkBigDecimal(dto.getWinMoney()));
			totalPayoff=totalPayoff.add(ProductUtil.checkBigDecimal(dto.getPayoff()));
		}

		AgentDTO dto = new AgentDTO("合计","合计", totalBet, totalWin, totalPayoff);
		betAndWinList.add(dto);

		request.setAttribute("totalRechage", totalRechage);//充值
		request.setAttribute("totalDrawMoney", totalDrawMoney);//提现
//		request.setAttribute("fanshui", fanshui);//返水
		request.setAttribute("memberBetReturn", memberBetReturn);//返水
		request.setAttribute("memberRechargeReturn", memberRechargeReturn);//返水
		request.setAttribute("memberBetReturnSelf", memberBetReturnSelf);//返水
		request.setAttribute("memberReturnSelf", memberReturnSelf);//充值赠送
		request.setAttribute("win", win);//彩派
		request.setAttribute("turnTableMoney", turnTableMoney);//转盘
		request.setAttribute("redPacketsMoney", redPacketsMoney);//红包
		request.setAttribute("money", money);//总余额
		request.setAttribute("outMoney", outMoney);	//出款
		request.setAttribute("incomeMoney", incomeMoney);//入款
		request.setAttribute("rechageDrawPayoff", rechageDrawPayoff);//出入差
		request.setAttribute("otherRechage", otherRechage);//其它加款
		request.setAttribute("otherDecuteMoney", otherDecuteMoney);//其它扣款
//		request.setAttribute("userName", userName);
		request.setAttribute("gameBetAndWinList", betAndWinList);
		request.setAttribute("agentForm", agentForm);
		request.setAttribute("userId", String.valueOf(userid));
		return mapping.findForward("userReport");
	}
	
	/**
	 * 代理报表
	 */
	public ActionForward agentReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		
		AgentForm agentForm = (AgentForm) form;
		String startDate = agentForm.getStartDate();
		String endDate = agentForm.getEndDate();
		Integer totalBet = 0; //总投注
		BigDecimal bet =  new BigDecimal(0); //总投注
		BigDecimal win = new BigDecimal(0);//中奖彩派
		BigDecimal totalWin = new BigDecimal(0);//中奖彩派
		BigDecimal totalPayoff = new BigDecimal(0); //盈利
		BigDecimal totalRechage = new BigDecimal(0);//充值
		BigDecimal totalDrawMoney = new BigDecimal(0);//累计提现
		BigDecimal rechageDrawPayoff = new BigDecimal(0);//出入款差
		BigDecimal money = new BigDecimal(0);//账户余额
		BigDecimal decuteMoney=new BigDecimal(0);//后台扣款
		BigDecimal incomeMoney=new BigDecimal(0);//入款总计
		BigDecimal outMoney=new BigDecimal(0);//出款总计
		BigDecimal turnTableMoney = new BigDecimal(0);//转盘
		BigDecimal redPacketsMoney = new BigDecimal(0); //红包
		BigDecimal fanshui = new BigDecimal(0); //总返水
		BigDecimal memberBetReturn = new BigDecimal(0); //下级投注返水
		BigDecimal memberRechargeReturn = new BigDecimal(0); //下级充值返水
		BigDecimal memberBetReturnSelf = new BigDecimal(0); //自己投注返水
		BigDecimal memberReturnSelf = new BigDecimal(0); //自己充值赠送
		BigDecimal luckyMoney = new BigDecimal(0);//红包+转盘
		BigDecimal otherDecuteMoney = new BigDecimal(0); //其它扣款
		BigDecimal otherRechage = new BigDecimal(0);//其它加款
		Integer agentNum = 0;//下级代理数量
		Integer memberNum = 0; //下级会员数量
		BigDecimal repairDetailAdd = new BigDecimal(0); //修复明细--加款
		BigDecimal repairDetailSub = new BigDecimal(0);//修复明细--扣款
		
		BigDecimal rechargePresent = new BigDecimal(0); // 充值赠送金额总计
		BigDecimal totalwin2 = new BigDecimal(0); // 彩金派送总计

		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		
		Integer userid=ParamUtils.getIntegerParameter(request, "userId");//传来的id
		if(!ParamUtils.chkInteger(userid)){
			userid = Integer.valueOf(agentForm.getUserName());
		}
		Integer loginUserId = loginUser.getUserId();//登录用户id
		String loginUserType = loginUser.getUserType();//登录用户类型
		
		//共用的查询条件
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		
		if (Constants.USER_TYPE_ADMIN.equals(loginUserType)
				|| Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)) {//管理员登录
			hqls.append(" and (u.agentId = ? or u.userId = ?) ");
		    para.add(userid);
		    para.add(userid);
		}else if(Constants.USER_TYPE_AGENT_ONE.equals(loginUserType)||
				Constants.USER_TYPE_AGENT_TWO.equals(loginUserType)||
				Constants.USER_TYPE_AGENT_THREE.equals(loginUserType)){//代理用户登录
			hqls.append(" and (u.agentId = ? or u.userId = ?) ");
		    para.add(loginUserId); //只能统计自己及自己下级会员的
		    para.add(loginUserId);
		}
		
		if(!ParamUtils.chkInteger(userid)){
			request.setAttribute("msg", "传入参数为空");
			return mapping.findForward("error");
		}else{
			List<User> userList = userService.findUserList(hqls.toString(), para);
			for(User user:userList){
				money = money.add(ProductUtil.checkBigDecimal(user.getMoney()));
			}
		}
		agentNum=0;//统计直系代理数量
		memberNum=agentService.CountMemberNum(userid);//统计直系会员数量||或者师徒

		if(!ParamUtils.chkString(startDate)||! ParamUtils.chkString(endDate)){
			startDate = DateTimeUtil.getMonthFirstDay();
			endDate = DateTimeUtil.DateToString(new Date());
			agentForm.setStartDate(startDate);
			agentForm.setEndDate(endDate);
		}
		
		
		List<Object> para6 = new ArrayList<Object>();
		StringBuffer hsql6 = new StringBuffer(hqls);
		para6.addAll(para);
		//代理及会员交易记录
		if(ParamUtils.chkString(startDate) && ParamUtils.chkString(endDate)){
			hsql6.append(" and ho.createTime >= ? ");
			para6.add(startDate + " 00:00:00");
			hsql6.append(" and ho.createTime <= ? ");
			para6.add(endDate + " 23:59:59");
		}
		String t = Constants.CASH_TYPE_CASH_RECHARGE_PRESENT;
		hsql6.append(" and ho.status = ? ");
		para6.add(Constants.PUB_STATUS_OPEN);
		hsql6.append(" group by ho.cashType ");
		List<GaDTO> tradelist = null; //代理及会员交易记录
		tradelist = gaService.findUserTradeDetailList(hsql6.toString(),para6);
		for(GaDTO tradeDto: tradelist){
			String cashType = tradeDto.getCashType();
			BigDecimal paperMoney = tradeDto.getPaperMoney();
			if( cashType != null && paperMoney != null){
				if(Constants.CASH_TYPE_ONLINE.equals(cashType)||Constants.CASH_TYPE_MANAGER_SET.equals(cashType)){//充值= 在线充值+管理员充值
					totalRechage = totalRechage.add(paperMoney);
				}else if(Constants.CASH_TYPE_CASH_OUT.equals(cashType)){//提现
					totalDrawMoney = totalDrawMoney.add(paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN));
				}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN.equals(cashType)){//下级投注返水
					memberBetReturn = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN.equals(cashType)){//下级充值返水
					memberRechargeReturn = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF.equals(cashType)){//自己投注返水
					memberBetReturnSelf = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_MEMBER_RETURN_SELF.equals(cashType)){//充值赠送
					memberReturnSelf = paperMoney;
					rechargePresent = rechargePresent.add(paperMoney);
				}else if(Constants.CASH_TYPE_CASH_PRIZE.equals(cashType)){//中奖彩派
					win = paperMoney;
					totalwin2 = totalwin2.add(paperMoney);
				}else if(Constants.CASH_TYPE_CASH_TURN_TABLE.equals(cashType)){//转盘
					turnTableMoney = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_RED_PACKETS.equals(cashType)){//红包
					redPacketsMoney = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType)){//投注
					bet = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}else if(Constants.CASH_TYPE_CASH_SYS_CHARGE.equals(cashType)){//后台扣款
					decuteMoney = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}else if (Constants.CASH_TYPE_CASH_RETURN.equals(cashType)){//提现审核返回
					totalDrawMoney = totalDrawMoney.subtract(paperMoney);//提现-审核返回
				}else if (Constants.CASH_TYPE_CASH_OTHER_SET.equals(cashType)){//其它加款
					otherRechage = paperMoney;
				}else if (Constants.CASH_TYPE_CASH_OTHER_CHARGE.equals(cashType)){//其它扣款
					otherDecuteMoney = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}else if(Constants.CASH_TYPE_CASH_REPAIR_DETAIL_ADD.equals(cashType)){//修复明细--加款
					repairDetailAdd = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_REPAIR_DETAIL_SUB.equals(cashType)){//修复明细--扣款
					repairDetailSub = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}
		}
		incomeMoney = totalRechage.add(memberReturnSelf).add(win).add(turnTableMoney).add(redPacketsMoney)
				.add(memberBetReturn).add(memberBetReturnSelf).add(memberRechargeReturn).add(otherRechage)
				.add(repairDetailAdd);
		outMoney = totalDrawMoney.add(bet).add(decuteMoney).add(otherDecuteMoney).add(repairDetailSub);
		rechageDrawPayoff = incomeMoney.subtract(outMoney);
		fanshui = memberBetReturn.add(memberRechargeReturn).add(memberBetReturnSelf).add(memberReturnSelf);
		luckyMoney = turnTableMoney.add(redPacketsMoney);
		
		//查询每一个彩种的投注、中奖、盈亏
		List<Object> para4 = new ArrayList<Object>();
		StringBuffer hqls4 = new StringBuffer(" and ho.userId = ?");
		para4.add(userid);
		
		hqls4.append(" and ho.createTime >= ? ");
		para4.add(startDate+" 00:00:00");
		hqls4.append(" and ho.createTime <= ? ");
		para4.add(endDate+" 23:59:59");
		
		hqls4.append("group by ho.modelType ");
		
		List<AgentDTO> betAndWinList = new ArrayList<AgentDTO>();
		List<String> gameTypeList = new ArrayList<String>();
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put(Constants.GAME_TYPE_XY_BJPK10, "信用北京赛车");
		map.put(Constants.GAME_TYPE_XY_SFPK10, "信用急速赛车");
		map.put(Constants.GAME_TYPE_XY_SFPK102, "信用三分PK拾");
		map.put(Constants.GAME_TYPE_XY_XYFT, "信用幸运飞艇");
		map.put(Constants.GAME_TYPE_XY_JSFT, "信用极速飞艇");
		map.put(Constants.GAME_TYPE_XY_CQSSC, "信用重庆时时彩");
		map.put(Constants.GAME_TYPE_XY_XJSSC, "信用新疆时时彩");
		map.put(Constants.GAME_TYPE_XY_TJSSC, "信用天津时时彩");
		map.put(Constants.GAME_TYPE_XY_BJSSC, "信用北京时时彩");
		map.put(Constants.GAME_TYPE_XY_BJ3, "信用三分彩");
		map.put(Constants.GAME_TYPE_XY_FC, "信用五分彩");
		map.put(Constants.GAME_TYPE_XY_XJPLU28, "信用幸运28");
		map.put(Constants.GAME_TYPE_XY_BJLU28, "信用PC蛋蛋");
		map.put(Constants.GAME_TYPE_XY_GDK10, "信用广东快乐十分");
		map.put(Constants.GAME_TYPE_XY_GXK10, "信用广西快乐十分");
		map.put(Constants.GAME_TYPE_XY_CQK10, "信用重庆幸运农场");
		map.put(Constants.GAME_TYPE_XY_POKER, "信用快乐扑克3");
		map.put(Constants.GAME_TYPE_XY_MARKSIX, "信用香港六合彩");
		map.put(Constants.GAME_TYPE_XY_SFLHC, "信用极速六合彩");
		map.put(Constants.GAME_TYPE_XY_GDPICK11, "信用广东11选5");
		map.put(Constants.GAME_TYPE_XY_JXPICK11, "信用江西11选5");
		map.put(Constants.GAME_TYPE_XY_SDPICK11, "信用山东11选5");
		map.put(Constants.GAME_TYPE_XY_JSK3, "信用江苏快3");
		map.put(Constants.GAME_TYPE_XY_BJK3, "信用北京快3");

		map.put(Constants.GAME_TYPE_GF_BJPK10, "官方北京赛车");
		map.put(Constants.GAME_TYPE_GF_SFPK10, "官方急速赛车");
		map.put(Constants.GAME_TYPE_GF_SFPK102, "官方三分PK拾");
		map.put(Constants.GAME_TYPE_GF_XYFT, "官方幸运飞艇");
		map.put(Constants.GAME_TYPE_GF_JSFT, "官方极速飞艇");
		map.put(Constants.GAME_TYPE_GF_CQSSC, "官方重庆时时彩");
		map.put(Constants.GAME_TYPE_GF_XJSSC, "官方新疆时时彩");
		map.put(Constants.GAME_TYPE_GF_TJSSC, "官方天津时时彩");
		map.put(Constants.GAME_TYPE_GF_BJSSC, "官方北京时时彩");
		map.put(Constants.GAME_TYPE_GF_THREE, "官方三分彩");
		map.put(Constants.GAME_TYPE_GF_FC, "官方五分彩");
		map.put(Constants.GAME_TYPE_GF_GDPICK11, "官方广东11选5");
		map.put(Constants.GAME_TYPE_GF_JXPICK11, "官方江西11选5");
		map.put(Constants.GAME_TYPE_GF_SDPICK11, "官方山东11选5");
		map.put(Constants.GAME_TYPE_GF_SHPICK11, "官方上海11选5");
		map.put(Constants.GAME_TYPE_GF_AHPICK11, "官方安徽11选5");
		map.put(Constants.GAME_TYPE_GF_JSK3, "官方江苏快3");
		map.put(Constants.GAME_TYPE_GF_BJK3, "官方北京快3");
		map.put(Constants.GAME_TYPE_GF_GXK3, "官方广西快3");
		map.put(Constants.GAME_TYPE_GF_HUBK3, "官方湖北快3");
		map.put(Constants.GAME_TYPE_GF_JXK3, "官方江西快3");
		map.put(Constants.GAME_TYPE_GF_JLK3, "官方吉林快3");
		map.put(Constants.GAME_TYPE_GF_SHK3, "官方上海快3");
		
		List<AgentDTO> gameBetAndWinList;
//		gameBetAndWinList = agentService.findGameBetAndWin(hqls4.toString(), para4);
		gameBetAndWinList = agentService.findGameBetAndWinFromUserTradeDetail(hqls4.toString(), para4);
		
		for(String key:map.keySet()) {
			AgentDTO dto = null;
			if(gameBetAndWinList != null && gameBetAndWinList.size()>0){
				for(int i = 0;i<gameBetAndWinList.size();i++){
					dto = gameBetAndWinList.get(i);
					if(key.equals(dto.getGameType())){
						dto.setGameName(map.get(key));
						betAndWinList.add(dto);
						gameTypeList.add(key);
						break;
					}
				}
			}
			if(!gameTypeList.contains(key)){
				dto = new AgentDTO(map.get(key),key, 0, new BigDecimal(0), new BigDecimal(0));
				betAndWinList.add(dto);
			}
//			totalBet=totalBet+dto.getBetMoney();
//			totalWin=totalWin.add(ProductUtil.checkBigDecimal(dto.getWinMoney()));
//			totalPayoff=totalPayoff.add(ProductUtil.checkBigDecimal(dto.getPayoff()));
		}
		AgentDTO totalSumDto = gameBetAndWinList.get(gameBetAndWinList.size()-1);
		AgentDTO dto = new AgentDTO("合计","合计",totalSumDto.getBetMoney2(), totalSumDto.getWinMoney(),totalSumDto.getPayoff());
//		AgentDTO dto = new AgentDTO("合计","合计", totalBet, totalWin, totalPayoff);
//		betAndWinList.add(dto);

		request.setAttribute("totalSumDto", dto);
		request.setAttribute("agentNum", agentNum);
		request.setAttribute("memberNum", memberNum);
		request.setAttribute("totalRechage", totalRechage);//充值
		request.setAttribute("totalDrawMoney", totalDrawMoney);//提现
		request.setAttribute("fanshui", fanshui);//返水
		request.setAttribute("luckyMoney", luckyMoney);//红包+转盘
		request.setAttribute("memberBetReturn", memberBetReturn);//下级投注返水
		request.setAttribute("memberRechargeReturn", memberRechargeReturn);//下级充值返水
		request.setAttribute("memberBetReturnSelf", memberBetReturnSelf);//自己投注返水
		request.setAttribute("memberReturnSelf", memberReturnSelf);//自己充值赠送
		request.setAttribute("win", win);//彩派
		request.setAttribute("turnTableMoney", turnTableMoney);//转盘
		request.setAttribute("redPacketsMoney", redPacketsMoney);//红包
		request.setAttribute("money", money);//总余额
		request.setAttribute("outMoney", outMoney);	//出款
		request.setAttribute("incomeMoney", incomeMoney);//入款
		request.setAttribute("rechageDrawPayoff", rechageDrawPayoff);//出入差
		request.setAttribute("otherRechage", otherRechage);//其它加款
		request.setAttribute("otherDecuteMoney", otherDecuteMoney);//其它扣款
		request.setAttribute("totalWin2", totalwin2);//派彩总计
		request.setAttribute("rechargePresent", rechargePresent);//充值赠送
//		request.setAttribute("userName", userName);
		request.setAttribute("gameBetAndWinList", betAndWinList);
		request.setAttribute("userId", String.valueOf(userid));
		agentForm.setUserName(""+userid);
		request.setAttribute("agentForm", agentForm);
		return mapping.findForward("agentReport");
	}
	
	/**
	 * 网站总报表
	 */
	public ActionForward report(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		
		AgentForm agentForm = (AgentForm) form;
		String startDate = agentForm.getStartDate();
		String endDate = agentForm.getEndDate();
		Integer totalBet = 0; //总投注
		BigDecimal bet =  new BigDecimal(0); //总投注
		BigDecimal win = new BigDecimal(0);//中奖彩派
		BigDecimal totalWin = new BigDecimal(0);//中奖彩派
		BigDecimal totalPayoff = new BigDecimal(0); //盈利
		BigDecimal totalRechage = new BigDecimal(0);//充值
		BigDecimal totalDrawMoney = new BigDecimal(0);//累计提现
		BigDecimal rechageDrawPayoff = new BigDecimal(0);//出入款差
		BigDecimal money = new BigDecimal(0);//账户余额
		BigDecimal decuteMoney=new BigDecimal(0);//后台扣款
		BigDecimal incomeMoney=new BigDecimal(0);//入款总计
		BigDecimal outMoney=new BigDecimal(0);//出款总计
		BigDecimal turnTableMoney = new BigDecimal(0);//转盘
		BigDecimal redPacketsMoney = new BigDecimal(0); //红包
		BigDecimal fanshui = new BigDecimal(0); //总返水
		BigDecimal memberBetReturn = new BigDecimal(0); //下级投注返水
		BigDecimal memberRechargeReturn = new BigDecimal(0); //下级充值返水
		BigDecimal memberBetReturnSelf = new BigDecimal(0); //自己投注返水
		BigDecimal memberReturnSelf = new BigDecimal(0); //自己充值赠送
		BigDecimal luckyMoney = new BigDecimal(0);//红包+转盘
		BigDecimal otherDecuteMoney = new BigDecimal(0); //其它扣款
		BigDecimal otherRechage = new BigDecimal(0);//其它加款
		Integer agentNum = 0;//下级代理数量
		Integer memberNum = 0; //下级会员数量
		BigDecimal repairDetailAdd = new BigDecimal(0); //修复明细--加款
		BigDecimal repairDetailSub = new BigDecimal(0);//修复明细--扣款

		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		hqls.append(" and u.userType < ? ");
		para.add(Constants.USER_TYPE_TEST);
		List<User> userList = userService.findUserList(hqls.toString(), para);
		for(User user:userList){
			money = money.add(ProductUtil.checkBigDecimal(user.getMoney()));
		}

		agentNum = userService.countObjects(" select count(u.userId) from User u where u.userType in (13,14,15)");//统计所有代理数量
		memberNum = userList.size();//统计所有用户

		if(!ParamUtils.chkString(startDate)||! ParamUtils.chkString(endDate)){
			startDate = DateTimeUtil.getMonthFirstDay();
			endDate = DateTimeUtil.DateToString(new Date());
			agentForm.setStartDate(startDate);
			agentForm.setEndDate(endDate);
		}
		
		
		List<Object> para6 = new ArrayList<Object>();
		StringBuffer hsql6 = new StringBuffer();
		//代理及会员交易记录
		if(ParamUtils.chkString(startDate) && ParamUtils.chkString(endDate)){
			hsql6.append(" and ho.createTime >= ? ");
			para6.add(startDate + " 00:00:00");
			hsql6.append(" and ho.createTime <= ? ");
			para6.add(endDate + " 23:59:59");
		}
		hsql6.append(" and ho.status = ? ");
		para6.add(Constants.PUB_STATUS_OPEN);
		hsql6.append(" group by ho.cashType ");
		List<GaDTO> tradelist = null; //代理及会员交易记录
		tradelist = gaService.findUserTradeDetailList(hsql6.toString(),para6);
		for(GaDTO tradeDto: tradelist){
			String cashType = tradeDto.getCashType();
			BigDecimal paperMoney = tradeDto.getPaperMoney();
			if( cashType != null && paperMoney != null){
				if(Constants.CASH_TYPE_ONLINE.equals(cashType)||Constants.CASH_TYPE_MANAGER_SET.equals(cashType)){//充值= 在线充值+管理员充值
					totalRechage = totalRechage.add(paperMoney);
				}else if(Constants.CASH_TYPE_CASH_OUT.equals(cashType)){//提现
					totalDrawMoney = totalDrawMoney.add(paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN));
				}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN.equals(cashType)){//下级投注返水
					memberBetReturn = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN.equals(cashType)){//下级充值返水
					memberRechargeReturn = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF.equals(cashType)){//自己投注返水
					memberBetReturnSelf = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_MEMBER_RETURN_SELF.equals(cashType)){//充值赠送
					memberReturnSelf = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_PRIZE.equals(cashType)){//中奖彩派
					win = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_TURN_TABLE.equals(cashType)){//转盘
					turnTableMoney = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_RED_PACKETS.equals(cashType)){//红包
					redPacketsMoney = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType)){//投注
					bet = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}else if(Constants.CASH_TYPE_CASH_SYS_CHARGE.equals(cashType)){//后台扣款
					decuteMoney = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}else if(Constants.CASH_TYPE_CASH_RETURN.equals(cashType)){//提现审核返回
					totalDrawMoney = totalDrawMoney.subtract(paperMoney);
				}else if (Constants.CASH_TYPE_CASH_OTHER_SET.equals(cashType)){//其它加款
					otherRechage = paperMoney;
				}else if (Constants.CASH_TYPE_CASH_OTHER_CHARGE.equals(cashType)){//其它扣款
					otherDecuteMoney = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}else if(Constants.CASH_TYPE_CASH_REPAIR_DETAIL_ADD.equals(cashType)){//修复明细--加款
					repairDetailAdd = paperMoney;
				}else if(Constants.CASH_TYPE_CASH_REPAIR_DETAIL_SUB.equals(cashType)){//修复明细--扣款
					repairDetailSub = paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}

			}
		}
		incomeMoney = totalRechage.add(memberReturnSelf).add(win).add(turnTableMoney).add(redPacketsMoney)
				.add(memberBetReturn).add(memberBetReturnSelf).add(memberRechargeReturn).add(otherRechage)
				.add(repairDetailAdd);
		outMoney = totalDrawMoney.add(bet).add(decuteMoney).add(otherDecuteMoney).add(repairDetailSub);
		rechageDrawPayoff = incomeMoney.subtract(outMoney);
		fanshui = memberBetReturn.add(memberRechargeReturn).add(memberBetReturnSelf).add(memberReturnSelf);
		luckyMoney = turnTableMoney.add(redPacketsMoney);
		
		//查询每一个彩种的投注、中奖、盈亏
		List<Object> para4 = new ArrayList<Object>();
		StringBuffer hqls4 = new StringBuffer();
		
		hqls4.append(" and ho.createTime >= ? ");
		para4.add(startDate+" 00:00:00");
		hqls4.append(" and ho.createTime <= ? ");
		para4.add(endDate+" 23:59:59");
		
		hqls4.append("group by ho.modelType ");
		
		List<AgentDTO> betAndWinList = new ArrayList<AgentDTO>();
		List<String> gameTypeList = new ArrayList<String>();
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put(Constants.GAME_TYPE_XY_BJPK10, "信用北京赛车");
		map.put(Constants.GAME_TYPE_XY_SFPK10, "信用急速赛车");
		map.put(Constants.GAME_TYPE_XY_SFPK102, "信用三分PK拾");
		map.put(Constants.GAME_TYPE_XY_XYFT, "信用幸运飞艇");
		map.put(Constants.GAME_TYPE_XY_JSFT, "信用极速飞艇");
		map.put(Constants.GAME_TYPE_XY_CQSSC, "信用重庆时时彩");
		map.put(Constants.GAME_TYPE_XY_XJSSC, "信用新疆时时彩");
		map.put(Constants.GAME_TYPE_XY_TJSSC, "信用天津时时彩");
		map.put(Constants.GAME_TYPE_XY_BJSSC, "信用北京时时彩");
		map.put(Constants.GAME_TYPE_XY_BJ3, "信用三分彩");
		map.put(Constants.GAME_TYPE_XY_FC, "信用五分彩");
		map.put(Constants.GAME_TYPE_XY_XJPLU28, "信用幸运28");
		map.put(Constants.GAME_TYPE_XY_BJLU28, "信用PC蛋蛋");
		map.put(Constants.GAME_TYPE_XY_GDK10, "信用广东快乐十分");
		map.put(Constants.GAME_TYPE_XY_GXK10, "信用广西快乐十分");
		map.put(Constants.GAME_TYPE_XY_CQK10, "信用重庆幸运农场");
		map.put(Constants.GAME_TYPE_XY_POKER, "信用快乐扑克3");
		map.put(Constants.GAME_TYPE_XY_MARKSIX, "信用香港六合彩");
		map.put(Constants.GAME_TYPE_XY_SFLHC, "信用极速六合彩");
		map.put(Constants.GAME_TYPE_XY_GDPICK11, "信用广东11选5");
		map.put(Constants.GAME_TYPE_XY_JXPICK11, "信用江西11选5");
		map.put(Constants.GAME_TYPE_XY_SDPICK11, "信用山东11选5");
		map.put(Constants.GAME_TYPE_XY_JSK3, "信用江苏快3");
		map.put(Constants.GAME_TYPE_XY_BJK3, "信用北京快3");
		

		map.put(Constants.GAME_TYPE_GF_BJPK10, "官方北京赛车");
		map.put(Constants.GAME_TYPE_GF_SFPK10, "官方急速赛车");
		map.put(Constants.GAME_TYPE_GF_SFPK102, "官方三分PK拾");
		map.put(Constants.GAME_TYPE_GF_XYFT, "官方幸运飞艇");
		map.put(Constants.GAME_TYPE_GF_JSFT, "官方极速飞艇");
		map.put(Constants.GAME_TYPE_GF_CQSSC, "官方重庆时时彩");
		map.put(Constants.GAME_TYPE_GF_XJSSC, "官方新疆时时彩");
		map.put(Constants.GAME_TYPE_GF_TJSSC, "官方天津时时彩");
		map.put(Constants.GAME_TYPE_GF_BJSSC, "官方北京时时彩");
		map.put(Constants.GAME_TYPE_GF_THREE, "官方三分彩");
		map.put(Constants.GAME_TYPE_GF_FC, "官方五分彩");
		map.put(Constants.GAME_TYPE_GF_GDPICK11, "官方广东11选5");
		map.put(Constants.GAME_TYPE_GF_JXPICK11, "官方江西11选5");
		map.put(Constants.GAME_TYPE_GF_SDPICK11, "官方山东11选5");
		map.put(Constants.GAME_TYPE_GF_SHPICK11, "官方上海11选5");
		map.put(Constants.GAME_TYPE_GF_AHPICK11, "官方安徽11选5");
		map.put(Constants.GAME_TYPE_GF_JSK3, "官方江苏快3");
		map.put(Constants.GAME_TYPE_GF_BJK3, "官方北京快3");
		map.put(Constants.GAME_TYPE_GF_GXK3, "官方广西快3");
		map.put(Constants.GAME_TYPE_GF_HUBK3, "官方湖北快3");
		map.put(Constants.GAME_TYPE_GF_JXK3, "官方江西快3");
		map.put(Constants.GAME_TYPE_GF_JLK3, "官方吉林快3");
		map.put(Constants.GAME_TYPE_GF_SHK3, "官方上海快3");
		

		List<AgentDTO> gameBetAndWinList;
//		gameBetAndWinList = agentService.findGameBetAndWin(hqls4.toString(), para4);
		gameBetAndWinList = agentService.findGameBetAndWinFromUserTradeDetail(hqls4.toString(), para4);
		
		for(String key:map.keySet()) {
			AgentDTO dto = null;
			if(gameBetAndWinList != null && gameBetAndWinList.size()>0){
				for(int i = 0;i<gameBetAndWinList.size();i++){
					dto = gameBetAndWinList.get(i);
					if(key.equals(dto.getGameType())){
						dto.setGameName(map.get(key));
						betAndWinList.add(dto);
						gameTypeList.add(key);
						break;
					}
				}
			}
			if(!gameTypeList.contains(key)){
				dto = new AgentDTO(map.get(key),key, 0, new BigDecimal(0), new BigDecimal(0));
				betAndWinList.add(dto);
			}
//			totalBet=totalBet+dto.getBetMoney();
//			totalWin=totalWin.add(ProductUtil.checkBigDecimal(dto.getWinMoney()));
//			totalPayoff=totalPayoff.add(ProductUtil.checkBigDecimal(dto.getPayoff()));
		}
		AgentDTO totalSumDto = gameBetAndWinList.get(gameBetAndWinList.size()-1);
		AgentDTO dto = new AgentDTO("合计","合计",totalSumDto.getBetMoney2(), totalSumDto.getWinMoney(),totalSumDto.getPayoff());
//		betAndWinList.add(dto);

		request.setAttribute("totalSumDto", dto);
		request.setAttribute("agentNum", agentNum);
		request.setAttribute("memberNum", memberNum);
		request.setAttribute("totalRechage", totalRechage);//充值
		request.setAttribute("totalDrawMoney", totalDrawMoney);//提现
		request.setAttribute("fanshui", fanshui);//返水
		request.setAttribute("luckyMoney", luckyMoney);//红包+转盘
		request.setAttribute("memberBetReturn", memberBetReturn);//下级投注返水
		request.setAttribute("memberRechargeReturn", memberRechargeReturn);//下级充值返水
		request.setAttribute("memberBetReturnSelf", memberBetReturnSelf);//自己投注返水
		request.setAttribute("memberReturnSelf", memberReturnSelf);//自己充值赠送
		request.setAttribute("win", win);//彩派
		request.setAttribute("turnTableMoney", turnTableMoney);//转盘
		request.setAttribute("redPacketsMoney", redPacketsMoney);//红包
		request.setAttribute("money", money);//总余额
		request.setAttribute("outMoney", outMoney);	//出款
		request.setAttribute("incomeMoney", incomeMoney);//入款
		request.setAttribute("rechageDrawPayoff", rechageDrawPayoff);//出入差
		request.setAttribute("otherRechage", otherRechage);//其它加款
		request.setAttribute("otherDecuteMoney", otherDecuteMoney);//其它扣款
		request.setAttribute("gameBetAndWinList", betAndWinList);
		request.setAttribute("agentForm", agentForm);
		return mapping.findForward("report");
	}
		
	/**
	 * 投注详情
	 */
	public ActionForward betDetailList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		Integer userId=ParamUtils.getIntegerParameter(request, "userId");
		AgentForm agentForm = (AgentForm) form;
		String startDate = agentForm.getStartDate();
		String endDate = agentForm.getEndDate();
		String sessionNo = agentForm.getSessionNo();
		
		String gameType = agentForm.getGameType();
		String optionTitle = agentForm.getOptionTitle();
		String winResult = agentForm.getWinResult();
		String userName = agentForm.getLoginName();
		
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		
		if(Constants.USER_TYPE_AGENT_ONE.equals(loginUser.getUserType())||
				Constants.USER_TYPE_AGENT_TWO.equals(loginUser.getUserType())||
				Constants.USER_TYPE_AGENT_THREE.equals(loginUser.getUserType())) {
				hqls.append(" and  (u.agentId = ? or u.userId = ?)"); //可以查询自己及自己下级会员的投注
				para.add(loginUser.getUserId());
				para.add(loginUser.getUserId());
		}
		if(ParamUtils.chkString(userName)){
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if(ParamUtils.chkInteger(userId)){
			hqls.append(" and  u.userId = ? ");
			para.add(userId);
			userName = ""+userId;
			agentForm.setLoginName(""+userId);
		}
		

//		if(ParamUtils.chkInteger(userid)){
//			hqls.append(" and upper(u.userId) = ? ");
//			para.add(userid); 
//		}
		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and upper(be.sessionNo) = ? ");
			para.add(sessionNo.trim().toUpperCase());
		}
		
		if(!ParamUtils.chkString(startDate)||! ParamUtils.chkString(endDate)){
			startDate = DateTimeUtil.getMonthFirstDay();
			endDate = DateTimeUtil.getMonthEndDay();
		
			agentForm.setStartDate(startDate);
			agentForm.setEndDate(endDate);
		}
		
		if(ParamUtils.chkString(winResult)){
			hqls.append(" and be.winResult = ? ");
			para.add(winResult);
		}
		if(ParamUtils.chkString(gameType)){
			hqls.append(" and be.gameType = ? ");
			para.add(gameType.trim());
		}
		if(ParamUtils.chkString(optionTitle)){
			
			hqls.append(" and be.betName = ? ");
			para.add(optionTitle);
			
//			String[] option = optionTitle.split("\\-");
//			if(option != null&& option.length == 2){
//				hqls.append(" and be.playName = ? ");
//				para.add(option[0]);
//				hqls.append(" and be.betName = ? ");
//				para.add(option[1]);
//			}
		}
		
		hqls.append(" and be.betTime >= ? ");
		para.add(startDate);
		hqls.append(" and be.betTime <= ? ");
		para.add(endDate);
		
		
		
		hqls.append(" order by be.betTime desc ");
		PaginationSupport ps = userService.findBetList(hqls.toString(), para,
				startIndex, pageSize);
		List<UserDTO> list = ps.getItems();
		
		List<GaSessionInfo> gaXyList = gaService.findGaSessionInfoList("2");
		
		List<UserDTO> secondList = new ArrayList<UserDTO>();
		if(ParamUtils.chkString(gameType)){
			LinkedHashMap<String,GaBetOption> linkedMap = GameCacheUtil.getLotteryBetOption(gameType);
			Iterator<Entry<String, GaBetOption>> iter = linkedMap.entrySet().iterator(); 
			LinkedHashMap<String,String> typeMap=new LinkedHashMap<String,String>();
			
			if(linkedMap.size()>0){
				while (iter.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry entry = (Map.Entry) iter.next(); 
					GaBetOption op = (GaBetOption) entry.getValue(); 
					String playType = op.getPlayType();
					String playTypeZh = op.getPlayTypeZh();
					String title = op.getTitle();
					String optionType = op.getOptionType().toString();
					
					if(typeMap.get(playTypeZh+"-"+title)==null){
//						  typeMap.put(playTypeZh+"-"+title, playTypeZh+"-"+title);
						  typeMap.put(title, title);
					}
				}
				for (Entry<String, String> entry : typeMap.entrySet()) {  
					  
					UserDTO dto = new UserDTO();
					dto.setId(entry.getKey());
					dto.setTitle(entry.getValue());
					secondList.add(dto);
				}
			}
		}
		
		agentForm.setStartIndex(startIndex);
		agentForm.setOptionTitle(optionTitle);
		request.setAttribute("gaXyList", gaXyList);
		request.setAttribute("list", list);
		
		request.setAttribute("sessionNo", sessionNo);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("loginName", userName);
		request.setAttribute("winResult", winResult);
		request.setAttribute("gameType", gameType);
		request.setAttribute("optionTitle", optionTitle);
		
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		request.setAttribute("agentForm", agentForm);
		request.setAttribute("secondList", secondList);
		
		return mapping.findForward("betDetailList");
	}
	
	/**
	 * 官方投注列表
	 */
	public ActionForward gfBetDetailList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");

		AgentForm agentForm = (AgentForm) form;
		String orderNum = agentForm.getOrderNum();
		String startDate = agentForm.getStartDate();
		String endDate = agentForm.getEndDate();
		String winResult = agentForm.getWinResult();
		String userType = agentForm.getUserType();
		String loginName = agentForm.getLoginName();
		String gameType = agentForm.getGameType();
		

		User user = (User) request.getSession(false).getAttribute("loginUser");
		if (Constants.USER_TYPE_ADMIN.equals(user.getUserType())
				|| Constants.USER_TYPE_SUPERADMIN.equals(user.getUserType())) {
//			hqls.append(" and (u.agentId = 0  or u.agentId is null)");
		} else if(Constants.USER_TYPE_AGENT.equals(user.getUserType())) {
			
			Integer agentId = user.getAgentId();
			if(ParamUtils.chkInteger(agentId)){//二级
				hqls.append(" and  u.agentId = ? ");
				para.add(user.getUserId());
			}else{
				hqls.append(" and  u.agentId1 = ? ");
				para.add(user.getUserId());
			}
		}
		
//		hqls.append(" and sp.betFlag = ? ");
//		para.add(Constants.PUB_STATUS_OPEN);
		if (ParamUtils.chkString(userType)) {
			String type = null;
			if("1".equals(userType)){//会员用户
				type = Constants.USER_TYPE_SUER;
			}else if("3".equals(userType)){ //代理用户
				type = Constants.USER_TYPE_AGENT;
			}
		    hqls.append(" and u.userType =? ");
		    para.add(type);
	    }else{
	    	 hqls.append(" and u.userType =? ");
			 para.add(Constants.USER_TYPE_SUER);
	    }
		if (ParamUtils.chkString(loginName)){
			hqls.append(" and (upper(u.loginName) = ? OR upper(u.userId) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		}
		if (ParamUtils.chkString(orderNum)) {
			hqls.append(" and upper(ho.orderNum) = ? ");
			para.add(orderNum.trim().toUpperCase());
		}
		if(!ParamUtils.chkString(startDate)||! ParamUtils.chkString(endDate)){
			startDate = DateTimeUtil.getMonthFirstDay();
			endDate = DateTimeUtil.getMonthEndDay();
		
			agentForm.setStartDate(startDate);
			agentForm.setEndDate(endDate);
		}
		
		if(ParamUtils.chkString(winResult)){
			hqls.append(" and ho.winResult = ? ");
			para.add(winResult);
		}
		if(ParamUtils.chkString(gameType)){
			hqls.append(" and ho.gameType = ? ");
			para.add(gameType.trim());
		}
		if(ParamUtils.chkInteger(userId)){
			hqls.append(" and part.userId =? ");
			para.add(userId); 
			loginName = ""+userId;
			agentForm.setLoginName(""+userId);
		}
		hqls.append(" and part.buyTime >= ? ");
		para.add(startDate);
		hqls.append(" and part.buyTime <= ? ");
		para.add(endDate);
		hqls.append(" order by part.buyTime desc ");
		PaginationSupport ps = agentService.findGaBetDetailList(hqls.toString(), para,
				startIndex, pageSize);
		List<AgentDTO> list = ps.getItems();
		
		List<GaSessionInfo> gaGfList = gaService.findGaSessionInfoList("1");
		
		agentForm.setStartIndex(startIndex);
		request.setAttribute("gaGfList", gaGfList);
		request.setAttribute("orderNum", orderNum);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("winResult", winResult);
		request.setAttribute("loginName", loginName);
		request.setAttribute("gameType", gameType);
		request.setAttribute("list", list);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		request.setAttribute("agentForm", agentForm);
		return mapping.findForward("gfBetDetailList");
	}
	
	/**
	 * 官方投注详情
	 */
	public ActionForward gfBetDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		Integer rid = ParamUtils.getIntegerParameter(request, "rid");
		Integer jointId = ParamUtils.getIntegerParameter(request, "jointId");
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		List<GaBetSponsorDetail> list = gaService.findGaBetSponsorDetailListByJointId(jointId);
		
		hqls.append(" and part.rid = ?");
		para.add(rid);
		PaginationSupport ps = agentService.findGaBetDetailList(hqls.toString(), para,
				0, 1);
		List<AgentDTO> dtoList = ps.getItems();
		if(dtoList!= null && dtoList.size()>0){
			AgentDTO dto = dtoList.get(0);
			request.setAttribute("dto", dto);
		}

		request.setAttribute("list", list);
		return mapping.findForward("gfBetDetail");
	}
	
	
	/**
	 * 资金明细
	 */
	public ActionForward tradeDetailList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		Integer userId = this.getUserId(request);
		if (!ParamUtils.chkInteger(userId)) {// 判断是否是正确的用户id
			request.setAttribute("msg", "不正确的用户ID！！");
			return mapping.findForward("error");
		}

		AgentForm agentForm = (AgentForm) form;
		String userName = agentForm.getUserName();
		String startDate = agentForm.getStartDate();
		String endDate = agentForm.getEndDate();
		String cashType1=agentForm.getCashType();

		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		if (Constants.USER_TYPE_ADMIN.equals(loginUser.getUserType())
				|| Constants.USER_TYPE_SUPERADMIN.equals(loginUser.getUserType())) {
		} else if(Constants.USER_TYPE_AGENT_ONE.equals(loginUser.getUserType())||
				Constants.USER_TYPE_AGENT_TWO.equals(loginUser.getUserType())||
				Constants.USER_TYPE_AGENT_THREE.equals(loginUser.getUserType())) {
				hqls.append(" and  (u.agentId = ? or u.userId = ?)");
				para.add(loginUser.getUserId());
				para.add(loginUser.getUserId());
				
		}
		
		if(ParamUtils.chkString(cashType1)){
			 hqls.append(" and de.cashType =? ");
			 para.add(cashType1);
		}
		
		if (ParamUtils.chkString(userName)){
			hqls.append(" and (upper(u.loginName) = ? OR upper(u.userId) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if(!ParamUtils.chkString(startDate)||! ParamUtils.chkString(endDate)){
			startDate = DateTimeUtil.getMonthFirstDay();
			endDate = DateTimeUtil.getMonthEndDay();
			agentForm.setStartDate(startDate);
			agentForm.setEndDate(endDate);
		}
		hqls.append(" and de.createTime >= ? ");
		para.add(startDate);
		hqls.append(" and de.createTime <= ? ");
		para.add(endDate);
		hqls.append(" order by de.createTime desc ");
	
		PaginationSupport ps = userService.findUserTradeDetail(hqls.toString(), para,
				startIndex, pageSize);
		List<UserDTO> list = ps.getItems();

		if(list != null && list.size() >0){
			for(UserDTO detail:list){
				String cashType = detail.getUserTradeDetail().getCashType();
				String cashType2 = Constants.getCashTradeTypeNameZh(cashType);
				detail.getUserTradeDetail().setCashType(cashType2);
			}
		}

		agentForm.setStartIndex(startIndex);
		request.setAttribute("cashType", cashType1);
		request.setAttribute("userName", userName);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		request.setAttribute("list", list);
		request.setAttribute("agentForm", agentForm);

		return mapping.findForward("tradeDetailList");
	}
	
	/**
	 * 下级代理
	 */
	public ActionForward subAgentList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String agentName;

		Integer userId = this.getUserId(request);
		if (!ParamUtils.chkInteger(userId)) {// 判断是否是正确的用户id
			request.setAttribute("msg", "不正确的用户ID！！");
			return mapping.findForward("error");
		}
		User user = (User) userService.getObject(User.class, userId);
		agentName = user.getLoginName();
		
		AgentForm agentForm = (AgentForm) form;
		String userName = agentForm.getUserName();

//		User user = (User) request.getSession(false).getAttribute("loginUser");
		if (Constants.USER_TYPE_ADMIN.equals(user.getUserType())
				|| Constants.USER_TYPE_SUPERADMIN.equals(user.getUserType())) {
			hqls.append(" and  (u.agentId = 0  or u.agentId is null)");
		} else if(Constants.USER_TYPE_AGENT.equals(user.getUserType())) {
			hqls.append(" and  u.agentId = ? ");
			para.add(user.getUserId());
		}

	    hqls.append(" and u.userType =? ");
	    para.add(Constants.USER_TYPE_AGENT);

		if (ParamUtils.chkString(userName)){
			hqls.append(" and (upper(u.loginName) = ? OR upper(u.userId) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}	
		
		PaginationSupport ps = userService.findUser(hqls.toString(), para,
				startIndex, pageSize);
		List<User> list = ps.getItems();

		agentForm.setStartIndex(startIndex);
		request.setAttribute("agentName", agentName);
		request.setAttribute("userName", userName);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		request.setAttribute("list", list);
		request.setAttribute("agentForm", agentForm);

		return mapping.findForward("subAgentList");
	}
	
	/**
	 * 下级会员
	 */
	public ActionForward subMemberList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String agentName;
		
		Integer userId = this.getUserId(request);
		if (!ParamUtils.chkInteger(userId)) {// 判断是否是正确的用户id
			request.setAttribute("msg", "不正确的用户ID！！");
			return mapping.findForward("error");
		}
		User user = (User) userService.getObject(User.class, userId);
		agentName = user.getLoginName();
		
		AgentForm agentForm = (AgentForm) form;
		String userName = agentForm.getUserName();
		String userType = agentForm.getUserType();
		
//		User user = (User) request.getSession(false).getAttribute("loginUser");
		if (Constants.USER_TYPE_ADMIN.equals(user.getUserType())
				|| Constants.USER_TYPE_SUPERADMIN.equals(user.getUserType())) {
			hqls.append(" and  (u.agentId = 0  or u.agentId is null)");
		} else if(Constants.USER_TYPE_AGENT.equals(user.getUserType())) {
			hqls.append(" and  u.agentId = ? ");
			para.add(user.getUserId());
		}

		hqls.append(" and u.userType =? ");
		para.add(Constants.USER_TYPE_SUER);
		
		if (ParamUtils.chkString(userName)){
			hqls.append(" and (upper(u.loginName) = ? OR upper(u.userId) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}	
		if (ParamUtils.chkString(userType)) {
			String type = null;
			if("1".equals(userType)){//会员用户
				type = Constants.USER_TYPE_SUER;
			}else if("3".equals(userType)){ //代理用户
				type = Constants.USER_TYPE_AGENT;
			}
		    hqls.append(" and u.userType =? ");
		    para.add(type);
	    }
		hqls.append(" order by u.userType desc ");
		PaginationSupport ps = userService.findUser(hqls.toString(), para,
				startIndex, pageSize);
		List<User> list = ps.getItems();
		
		agentForm.setStartIndex(startIndex);
		request.setAttribute("agentName", agentName);
		request.setAttribute("userName", userName);
		request.setAttribute("userType", userType);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		request.setAttribute("list", list);
		request.setAttribute("agentForm", agentForm);
		
		return mapping.findForward("subMemberList");
	}
	
	/**
	 * 用户列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward userList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		AgentForm frm = (AgentForm) form;
		String loginName = frm.getLoginName(); //查询的用户名字
		Integer loginId = loginUser.getUserId();//登陆用户id
		
		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		}
		hsql.append(" and (u.agentId = ? or u.userId = ?)");
		para.add(loginId);
		para.add(loginId);
		hsql.append(" order by u.registDateTime desc");
		
		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		List<User> items = ps.getItems();
		request.setAttribute("managerList", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		request.setAttribute("loginUser", loginUser);
		return mapping.findForward("userList");
	}
	
	/**
	 * 下级代理
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward agentList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		AgentForm frm = (AgentForm) form;
		String loginName = frm.getLoginName(); //查询的用户名字
		Integer loginId = loginUser.getUserId();//登陆用户id
		
		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		}
		hsql.append(" and (u.userType = ? or u.userType = ? or u.userType = ? ) ");
		para.add(Constants.USER_TYPE_AGENT_ONE);
		para.add(Constants.USER_TYPE_AGENT_TWO);
		para.add(Constants.USER_TYPE_AGENT_THREE);
		hsql.append(" and u.userId = ? ");
		para.add(loginId);
		
		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		List<User> items = ps.getItems();
		request.setAttribute("managerList", items);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		return mapping.findForward("agentList");
	}
	
	/**
	 * 代理的下级会员列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward subUserList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		Integer agentId = ParamUtils.getIntegerParameter(request, "agentId");
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		Integer loginUserId = loginUser.getUserId();//登录用户id
		hsql.append(" and u.agentId = ? ");
		para.add(loginUserId);

		if(ParamUtils.chkInteger(agentId)){
			hsql.append(" and u.agentId = ? ");
			para.add(agentId);
			hsql.append(" and u.userType = ? ");
			para.add(Constants.USER_TYPE_SUER);
		}
		hsql.append(" order by u.userId desc");
		
		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		List<User> items = ps.getItems();
		request.setAttribute("managerList", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("userList");
	}

	/**
	 * 用户每日统计数据列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward dailyStatisticsList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		AgentForm frm = (AgentForm) form;
		String loginName = frm.getLoginName(); //查询的用户名字
		String startDate = frm.getStartDate();
		String endDate = frm.getEndDate();
		Integer loginId = loginUser.getUserId();//登陆用户id
		PaginationSupport ps =null;
		hsql.append(" and (u.agentId = ? or u.userId = ?)");
		para.add(loginId);
		para.add(loginId);

		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		}
		if(ParamUtils.chkString(startDate) && ParamUtils.chkString(endDate)){
			hsql.append(" and ho.createTime >= ? ");
			para.add(startDate);
			hsql.append(" and ho.createTime <= ? ");
			para.add(endDate);
			
			ps = gaService.findDailyStatisticsList(hsql.toString(), para,
					startIndex, pageSize);
		}else{
			hsql.append(" order by ho.createTime desc");
			ps = gaService.findDailyStatisticsList2(hsql.toString(), para,
					startIndex, pageSize);
		}

		List<GaDTO> items = ps.getItems();
		request.setAttribute("list", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("dailyStatisticsList");
	}
	
	/**
	 * 代理统计数据列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward agentStatisticsList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		List<Object> para3 = new ArrayList<Object>();
		StringBuffer hqls3 = new StringBuffer();
		AgentForm frm = (AgentForm) form;
		String loginName = frm.getLoginName(); //查询的ID
		String startDate = frm.getStartDate();
		String endDate = frm.getEndDate();
		String userType = frm.getUserType();
		PaginationSupport ps =null;
		List<GaDTO> saveList = new ArrayList<GaDTO>();
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		Integer loginId = loginUser.getUserId();//登陆用户id

		hqls3.append(" and (u.userId = ? or u.agentId = ? ) ");
		para3.add(loginId);
		para3.add(loginId);
		if(ParamUtils.chkString(userType)){
			hqls3.append(" and u.userType = ? ");
			para3.add(userType);
		}else{
			hqls3.append(" and (u.userType = ? or u.userType = ? or u.userType = ? )");
			para3.add(Constants.USER_TYPE_AGENT_ONE);
			para3.add(Constants.USER_TYPE_AGENT_TWO);
			para3.add(Constants.USER_TYPE_AGENT_THREE);
		}
		if (ParamUtils.chkString(loginName)) {
			hqls3.append(" and (u.userId = ? or u.loginName = ?) ");
			para3.add(loginName);
			para3.add(loginName);
		}
		ps = userService.findUser(hqls3.toString(), para3, startIndex, pageSize);
		List<User> userList = ps.getItems();
		
		//代理下会员数量
		List<Object> para2 = new ArrayList<Object>();
		StringBuffer hsql2 = new StringBuffer();
		hsql2.append(" and u.agentId = ? ");
		para2.add(loginId);
		hsql2.append(" group by u.agentId ");
		List<GaDTO> agentList = gaService.findUserSubmemberList(hsql2.toString(),para2);

		//代理及会员交易记录
		if(ParamUtils.chkString(startDate) && ParamUtils.chkString(endDate)){
			hsql.append(" and ho.createTime >= ? ");
			para.add(startDate + " 00:00:00");
			hsql.append(" and ho.createTime <= ? ");
			para.add(endDate + " 23:59:59");
		}
		hsql.append(" and (u.userId = ? or u.agentId = ? )");
		hsql.append(" group by ho.cashType ");
		
		GaDTO dto = null;
		List<GaDTO> tradelist = null; //代理及会员交易记录
		for(User user: userList){
			Integer userId = user.getUserId();
			para.add(userId);
			para.add(userId);
			tradelist = gaService.findAgentStatisticsList(hsql.toString(),para);
			para.remove(userId);
			para.remove(userId);

			dto = new GaDTO();
			dto.setUserId(userId);
			dto.setUserName(user.getLoginName());
			dto.setNumber(0);
			dto.setWinCash(new BigDecimal(0));
			dto.setTotalBet(new BigDecimal(0));
			for(GaDTO agent:agentList){
				if(userId.equals(agent.getUserId())){
					dto.setNumber(agent.getNumber());
					break;
				}
			}
			for(GaDTO traDto:tradelist){
				if(userId.equals(traDto.getUserId())){
					String cashType = traDto.getCashType();
					BigDecimal money = traDto.getPaperMoney();
					if(Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType)){
						dto.setTotalBet(money.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN));
					}else if(Constants.CASH_TYPE_CASH_PRIZE.equals(cashType)){
						dto.setWinCash(money);
					}
				}
			}
			dto.setPaperMoney(dto.getWinCash().subtract(dto.getTotalBet()));
			saveList.add(dto);
		}

		request.setAttribute("list", saveList);
		request.setAttribute("count", String.valueOf(saveList.size()));
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		return mapping.findForward("agentStatisticsList");
	}
	
	/**
	 * 查询投注项list
	 */
	public void findSecondGaOptionList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		User loginUser = this.getUser(request);//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		
		//管理员和超级管理员、客服、财务才能查询
		if(ManagerUtils.checkRole(loginUserType)){
			String gameType = ParamUtils.getParameter(request, "gameType");
			if(ParamUtils.chkString(gameType)){
				LinkedHashMap<String,GaBetOption> linkedMap = GameCacheUtil.getLotteryBetOption(gameType);
				Iterator<Entry<String, GaBetOption>> iter = linkedMap.entrySet().iterator(); 
				LinkedHashMap<String,String> typeMap=new LinkedHashMap<String,String>();
				
				if(linkedMap.size()>0){
					while (iter.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map.Entry entry = (Map.Entry) iter.next(); 
						GaBetOption op = (GaBetOption) entry.getValue(); 
						String playType = op.getPlayType();
						String playTypeZh = op.getPlayTypeZh();
						String title = op.getTitle();
						String optionType = op.getOptionType().toString();
						
						if(typeMap.get(playType+"|"+optionType)==null){
//							  typeMap.put(playType+"|"+optionType, playTypeZh+"-"+title);
							typeMap.put(playType+"|"+optionType, title);
						}
					}
					
					org.json.JSONArray jsonArray = new org.json.JSONArray();
					for (Entry<String, String> entry : typeMap.entrySet()) {  
//					    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
						JSONObject obj = new JSONObject();// 返回数据层
						obj.put("title", entry.getValue());
						obj.put("id", entry.getKey());
						jsonArray.put(obj);
					}
					data.put("items", jsonArray);
					log.info("数字长度："+jsonArray.length());
					msg="请求成功";
					code = APIConstants.CODE_REQUEST_SUCCESS;
				}
			}else{
				msg = APIConstants.PARAMS_EMPTY_MSG;
				code = APIConstants.CODE_REQUEST_ERROR;
			}
		}else{
			msg="无权限！请联系管理员！";
			code = APIConstants.CODE_UNAUTHORIZED_ERROR;
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}

}
