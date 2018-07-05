package com.xy.poker.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.poker.model.PokerGaSession;
import com.xy.poker.model.PokerGaTrend;
import com.xy.poker.model.dto.PokerDTO;
import com.xy.ssc.cqssc.model.CqSscGaSession;

public interface IPokerService   extends IService {
	/**
	 * 初始化场次
	 */
	public String updateInitSession();
	
	public String updateInitTodaySession();
	
	/**
	 * 开奖
	 * @return
	 */
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);
	
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public PokerGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public PokerGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,PokerGaSession session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<PokerGaTrend> findPokerTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findPokerGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);

	
	/**
	 * 开奖结果及波色
	 * @param openResult
	 * @return
	 */
	public Map<String,String>  openCountResult(String openResult);
	/**
	 * 混合结果判断
	 * @param openResult
	 * @return
	 */
//	public Map<String,String>  judgeResult(String openResult);
	public Map<String,String>  judgeCountResult(String openResult);
	/**
	 * 更新冷热排行
	 * @param count
	 */
//	public void updateFetchAndOpenTrendResult(Integer count);
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(PokerGaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(PokerGaSession session,String openResult);
	/**
	 * 获取投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findPokerGaBetList(String hql,
			List<Object> para, int pageNum,int pageSize);

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
	public List<PokerDTO> findGaBetDetailById(String string, List<Object> para);

	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(PokerGaSession session);
}
