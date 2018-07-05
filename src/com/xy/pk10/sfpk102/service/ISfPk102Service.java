package com.xy.pk10.sfpk102.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.pk10.sfpk102.model.SfPk102GaSession;
import com.xy.pk10.sfpk102.model.SfPk102GaTrend;
import com.xy.pk10.sfpk102.model.dto.SfPk102DTO;
import com.xy.ssc.cqssc.model.CqSscGaSession;

public interface ISfPk102Service extends IService {
	
	/**
	 * 初始化期号 days=0初始化今天 days=1c初始化明天 days=-1初始化昨天
	 * @param days
	 * @return
	 */
	public String updateInitSession(int days);
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);
//	public void updateFetchAndOpenTrendResult(Integer count);
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public SfPk102GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public SfPk102GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,SfPk102GaSession gaK10Session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<SfPk102GaTrend> findSfPk102GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findSfPk102GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期的开奖数据
	 */
	public PaginationSupport  findSfPk102GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(SfPk102GaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(SfPk102GaSession session,String openResult);
	
	/**
	 * 获取投注详细信息
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaBetDetail(String hql,
			List<Object> para, int pageNum, int pageSize);
	
	/**
	 * 根据id获取投注详情
	 * @param string
	 * @param para
	 * @return
	 */
	public List<SfPk102DTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(SfPk102GaSession session);
}
