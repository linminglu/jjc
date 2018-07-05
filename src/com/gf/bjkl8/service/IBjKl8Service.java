package com.gf.bjkl8.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.gf.bjkl8.model.GfBjKl8GaSession;
import com.gf.bjkl8.model.GfBjKl8GaTrend;
import com.gf.bjkl8.model.dto.GfBjKl8DTO;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;

public interface IBjKl8Service   extends IService {
	/**
	 * 初始化场次
	 */
	public String updateInitSession();
	/**
	 * 初始化场次
	 */
	public String updateInitTodaySession(String sessionNo1);
	/**
	 * 开奖
	 * @return
	 */
	public void updateFetchAndOpenResult();
	
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GfBjKl8GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GfBjKl8GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,GfBjKl8GaSession session,User user,BigDecimal betAll);
	
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GfBjKl8GaTrend> findBjKl8TrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findBjKl8GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 开奖结果及波色
	 * @param openResult
	 * @return
	 */
	public Map<String,String>  openResult(String openResult);
	
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
	public Map<String,String>  judgeResult(String openResult);
	public Map<String,String>  judgeCountResult(String openResult);
	/**
	 * 更新冷热排行
	 * @param count
	 */
	public void updateFetchAndOpenTrendResult(Integer count);
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(GfBjKl8GaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(GfBjKl8GaSession session,String openResult);
	/**
	 * 获取投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBjKl8GaBetList(String hql,
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
	public List<GfBjKl8DTO> findGaBetDetailById(String string, List<Object> para);


}
