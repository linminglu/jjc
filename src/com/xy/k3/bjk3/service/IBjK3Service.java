package com.xy.k3.bjk3.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.k3.bjk3.model.BjK3GaSession;
import com.xy.k3.bjk3.model.BjK3GaTrend;
import com.xy.k3.bjk3.model.dto.BjK3DTO;

public interface IBjK3Service   extends IService {
	/**
	 * 初始化场次
	 */
	public String updateInitSession(String sessionNo1);
	/**
	 * 初始化明天场次
	 */
	public String updateInitTomorrowSession();
	/**
	 * 开奖
	 * @return
	 */
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);
	
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public BjK3GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public BjK3GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public void saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,BjK3GaSession session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<BjK3GaTrend> findBjK3TrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findBjK3GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 更新冷热排行
	 * @param count
	 */
//	public void updateFetchAndOpenTrendResult(Integer count);
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(BjK3GaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(BjK3GaSession session,String openResult);
	/**
	 * 获取投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBjK3GaBetList(String hql,
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
	public List<BjK3DTO> findGaBetDetailById(String string, List<Object> para);
	
	/**
	 * 根据开奖结果转换为历史开奖的key与value。
	 * @param openResult
	 * @return
	 */
	public Map<String, String> judgeCountResult(String openResult);
	
	/**
	 * 根据开奖结果转换为走势图的key与value.
	 * @param openResult
	 * @return
	 */
	public Map<String, String> getTrendResult(String openResult);

	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(BjK3GaSession session);
}
