package com.xy.pk10.sfpk10.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.pk10.sfpk10.model.SfPk10GaSession;
import com.xy.pk10.sfpk10.model.SfPk10GaTrend;
import com.xy.pk10.sfpk10.model.dto.SfPk10DTO;

public interface ISfPk10Service extends IService {
	
	public String updateInitSession();
	/**
	 * 初始化今天场次
	 */
	public String updateInitTodaySession();
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);
//	public void updateFetchAndOpenTrendResult(Integer count);
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public SfPk10GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public SfPk10GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,SfPk10GaSession gaK10Session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<SfPk10GaTrend> findSfPk10GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findSfPk10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期的开奖数据
	 */
	public PaginationSupport  findSfPk10GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(SfPk10GaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(SfPk10GaSession session,String openResult);
	
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
	public List<SfPk10DTO> findGaBetDetailById(String hql, List<Object> para);
	
	public boolean openSfPk10SessionOpenResultMethod(SfPk10GaSession session,String result);
	
	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(SfPk10GaSession session);

}
