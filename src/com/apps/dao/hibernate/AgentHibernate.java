package com.apps.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.apps.Constants;
import com.apps.dao.IAgentDAO;
import com.apps.model.dto.AgentDTO;
import com.apps.model.dto.BaseDataDTO;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.game.model.GaBetSponsorDetail;


public class AgentHibernate extends AbstractBaseDAOHibernate implements
		IAgentDAO {
	public Query makeQuerySQL(String hsql, List param) {
		Query q = getSession().createQuery(hsql);
		Iterator<Object> it = param.iterator();
		int i = 0;
		Object obj;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof String) {
				q.setString(i, (String) obj);
			} else if (obj instanceof Date) {
				q.setTimestamp(i, (Date) obj);
			} else if (obj instanceof Integer) {
				q.setInteger(i, ((Integer) obj).intValue());
			} else if (obj instanceof Integer[]) {
				q.setParameterList("ids", (Integer[]) obj);
			}
			i++;
		}
		return q;
	}

	public List<AgentDTO> findGfGameBetAndWin(String hqls, List<Object> para) {
		Query query = makeQuerySQL("select new com.apps.model.dto.AgentDTO(sp.gameName,sp.gameType ,sum(ho.betMoney),sum(ho.winCash),sum(ho.betMoney)-sum(ho.winCash)) from  GaBetPart ho,User u,GaBetSponsor sp where 1=1 and u.userId = ho.userId and sp.jointId = ho.jointId "
				+ hqls, para);
		List<AgentDTO> list = query.list();
		
		return list;
	}
	public List<AgentDTO> findGameBetAndWin(String hqls, List<Object> para) {
		Query query = makeQuerySQL("select new com.apps.model.dto.AgentDTO(ho.gameName,ho.gameType ,sum(ho.betMoney),sum(ho.winCash),sum(ho.betMoney)-sum(ho.winCash)) from  GaBetDetail ho,User u where 1=1 and u.userId = ho.userId "
				+ hqls, para);
		log.info("------"+query.getQueryString());
		for(Object obj:para){
			log.info("===="+obj.toString());
		}
		List<AgentDTO> list = query.list();
		
		return list;
	}

	@Override
	public List<AgentDTO> findGameBetAndWinFromUserTradeDetail(String hqls, List<Object> para) {
		// 从usertradedetail表中查询，cashtype=10 彩票投注 cashtype=18 中奖彩派
		// 查询中将modelType(彩票类型)赋值给cashType
		String hql = " select new com.apps.model.dto.AgentDTO(ho.modelType,sum(ho.cashMoney)) ";
		
		String hqHead = " from UserTradeDetail ho where 1=1 and ho.status='1' and ho.modelType is not null   ";
		// 收入 中奖 奖金
		String param1 = " and ho.tradeType = 1 and ho.cashType = "+Constants.CASH_TYPE_CASH_PRIZE + " ";
		// 支出 投注
		String param2 = " and ho.tradeType = 2 and ho.cashType = "+Constants.CASH_TYPE_CASH_BUY_LOTO+ " ";

		// 收入 奖金
		Query query1 = makeQuerySQL(hql+hqHead + param1+hqls, para);
		List result1 = query1.list();
		
		// 支出  投注
		Query query2 = makeQuerySQL(hql+hqHead + param2+hqls, para);
		List result2 = query2.list();
		
		Map<String, AgentDTO> dtoMap = new HashMap<String, AgentDTO>();
		AgentDTO totalSumDto = new AgentDTO();
		totalSumDto.setWinMoney(new BigDecimal(0));
		totalSumDto.setBetMoney2(new BigDecimal(0));
		totalSumDto.setPayoff(new BigDecimal(0));
		
		for(Object obj:result1){
			AgentDTO tmp = (AgentDTO) obj;
			totalSumDto.setWinMoney(totalSumDto.getWinMoney().add(tmp.getCashMoney()));
			if(dtoMap.get(tmp.getCashType())!=null){
				
			}else{
				tmp.setWinMoney(tmp.getCashMoney());
				tmp.setBetMoney2(new BigDecimal(0).abs());
				tmp.setGameType(tmp.getCashType());
				dtoMap.put(tmp.getCashType(), tmp);
				
			}
		}
		// 
		for(Object obj:result2){
			AgentDTO tmp = (AgentDTO) obj;
			totalSumDto.setBetMoney2(totalSumDto.getBetMoney2().add(tmp.getCashMoney().abs()));
			if(dtoMap.get(tmp.getCashType())!=null){
				AgentDTO savedDto = dtoMap.get(tmp.getCashType());
				savedDto.setBetMoney2(tmp.getCashMoney().abs());
			}else{
				tmp.setWinMoney(new BigDecimal(0));
				tmp.setBetMoney2(tmp.getCashMoney().abs());
				tmp.setGameType(tmp.getCashType());
				dtoMap.put(tmp.getCashType(), tmp);
			}
		}
		List<AgentDTO> result = new ArrayList<AgentDTO>();
		for(AgentDTO dto :dtoMap.values()){
			dto.setPayoff(dto.getBetMoney2().subtract(dto.getWinMoney()));
			result.add(dto);
		}

		totalSumDto.setPayoff(totalSumDto.getBetMoney2().subtract(totalSumDto.getWinMoney()));
		result.add(totalSumDto);
		return result;
	}

	@Override
	public List<AgentDTO> findUserTradeStatistics(String hqls, List<Object> para) {
		Query query = makeQuerySQL("select new com.apps.model.dto.AgentDTO(ho.cashType,sum(ho.cashMoney)) from  UserTradeDetail ho,User u where 1=1 and u.userId = ho.userId  "
				+ hqls, para);
		List<AgentDTO> list = query.list();
		log.info(query.getQueryString());
		return list;
	}

	@Override
	public PaginationSupport findGaBetDetailList(String hql, List<Object> para,
			int startIndex, int pageSize) {
//		String findList = "select new com.apps.model.dto.AgentDTO(part,ho,u.loginName,u2.loginName) from GaBetPart part,GaBetSponsor ho,User u,User u2 where u.userId=part.userId and u2.userId = ho.userId and part.jointId = ho.jointId ";
//		String findCount = "select count(part.id) from GaBetPart part,GaBetSponsor ho,User u where u.userId=part.userId and part.jointId = ho.jointId ";
		
		String findList = "select new com.apps.model.dto.AgentDTO(part,ho,u.loginName,u2.loginName,gsd.optionTitle,gsd.title,gsd.winResult,gsd.winMoney,gsd.betMoney) from GaBetPart part,GaBetSponsor ho,User u,User u2,GaBetSponsorDetail gsd where u.userId=part.userId and u2.userId = ho.userId and part.jointId = ho.jointId and gsd.orderNum = ho.orderNum ";
		String findCount = "select count(part.id) from GaBetPart part,GaBetSponsor ho,User u where u.userId=part.userId and part.jointId = ho.jointId ";
		
		
		Query record = makeQuerySQL(findList + hql, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List<AgentDTO> list = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(list, totalCount.intValue());
	}

	@Override
	public List<AgentDTO> findUserTypeCountList(Integer userId) {
		String hql = " select new com.apps.model.dto.AgentDTO(count(u),u.userType,sum(u.money))from User u where u.agentId=? group By u.userType ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, userId);
		List<AgentDTO> list = query.list();

		return list;
	}


	public Integer CountAgentNum(Integer userId) {
		String hql = "select count(u.userId) from User u where u.agentId=?  and u.userType='12' ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, userId);
		return (Integer) query.uniqueResult();
	}


	public Integer CountMemberNum(Integer userId) {
		String hql = "select count(u.userId) from User u where u.agentId=?  and u.userType='1' ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, userId);
		return (Integer) query.uniqueResult();
	}


	public BigDecimal CountUserMoney(Integer userId) {
		String hql="";
		Query query =null;
		if(ParamUtils.chkInteger(userId)){//代理
			hql = "select sum(u.money) from  User u where (u.agentId=? or u.userId=? )";
			query = getSession().createQuery(hql);
			query.setInteger(0, userId);
			query.setInteger(1, userId);
		}else{//管理员
			hql = "select sum(u.money) from  User u where 1=1  ";
			query = getSession().createQuery(hql);
		}
		List list=query.list();
		if(list!=null&&list.size()>0){
			if(list.get(0)==null){
				return new BigDecimal(0);
			}
			return new BigDecimal(list.get(0).toString());
		}
		return new BigDecimal(0);
	}
	public BigDecimal CountAgentTwoUserMoney(Integer userId){
		String hql="";
		Query query =null;
		hql = "select sum(u.money) from  User u where u.agentId=? and  u.userType='1'  ";
		query = getSession().createQuery(hql);
		query.setInteger(0, userId);
		List list=query.list();
		if(list!=null&&list.size()>0){
			if(list.get(0)==null){
				return new BigDecimal(0);
			}
			return new BigDecimal(list.get(0).toString());
		}
		return new BigDecimal(0);
	}

	@Override
	public List<AgentDTO> findUserTotalRecharge(String hqls, List<Object> para) {
		String hql = " select new com.apps.model.dto.AgentDTO(ho.payType,sum(ho.totalMoney))from User u ,CardRechargeOrder ho where 1=1 and u.userId = ho.userId ";
		Query query = makeQuerySQL(hql + hqls, para);
		List<AgentDTO> list = query.list();
		return list;
	}

	@Override
	public List<AgentDTO> findUserTotalCharge(String hqls, List<Object> para) {
		String hql = " select new com.apps.model.dto.AgentDTO(ho.payType,sum(ho.totalMoney))from User u ,CardChargeOrder ho where 1=1 and u.userId = ho.userId ";
		Query query = makeQuerySQL(hql + hqls, para);
		List<AgentDTO> list = query.list();
		return list;
	}

	@Override
	public List<AgentDTO> findUserTotalApplyCash(String hqls, List<Object> para) {
		String hql = " select new com.apps.model.dto.AgentDTO(ho.auditStatus,sum(ho.cashMoney))from User u ,UserApplyCash ho where 1=1 and u.userId = ho.userId ";
		Query query = makeQuerySQL(hql + hqls, para);
		List<AgentDTO> list = query.list();
		return list;
	}

}
