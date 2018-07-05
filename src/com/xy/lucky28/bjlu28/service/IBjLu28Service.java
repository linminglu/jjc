package com.xy.lucky28.bjlu28.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.lucky28.bjlu28.model.BjLu28GaSession;
import com.xy.lucky28.bjlu28.model.BjLu28GaTrend;
import com.xy.lucky28.bjlu28.model.dto.BjLu28DTO;
import com.xy.ssc.cqssc.model.CqSscGaSession;

public interface IBjLu28Service   extends IService {
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
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);
	
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public BjLu28GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public BjLu28GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,BjLu28GaSession session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<BjLu28GaTrend> findBjLu28TrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findBjLu28GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
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
//	public void updateFetchAndOpenTrendResult(Integer count);
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(BjLu28GaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(BjLu28GaSession session,String openResult);
	/**
	 * 获取投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBjLu28GaBetList(String hql,
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
	public List<BjLu28DTO> findGaBetDetailById(String string, List<Object> para);

	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(BjLu28GaSession session);
}
