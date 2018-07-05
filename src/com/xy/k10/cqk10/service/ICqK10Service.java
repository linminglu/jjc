package com.xy.k10.cqk10.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.k10.cqk10.model.CqK10GaSession;
import com.xy.k10.cqk10.model.CqK10GaTrend;
import com.xy.k10.cqk10.model.dto.CqK10DTO;

public interface ICqK10Service extends IService {

	/**
	 * 更新场次
	 * @return
	 */
	public String updateInitSession(int days);
	/**
	 * 开奖
	 * @return
	 */
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);
	
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public CqK10GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public CqK10GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,CqK10GaSession gaK10Session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<CqK10GaTrend> findCqK10GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findCqK10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 更新冷热排行榜
	 */
//	public void updateGaTrend();
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(CqK10GaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(CqK10GaSession session,String openResult);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findCqK10GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
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
	 * 根据id查询投注详情
	 * @param string
	 * @param para
	 * @return
	 */
	public List<CqK10DTO> findGaBetDetailById(String string, List<Object> para);

	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(CqK10GaSession session);
}
