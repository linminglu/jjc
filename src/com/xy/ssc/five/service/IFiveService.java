package com.xy.ssc.five.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.ssc.five.model.FiveGaSession;
import com.xy.ssc.five.model.FiveGaTrend;
import com.xy.ssc.five.model.dto.FiveDTO;

public interface IFiveService   extends IService {
	/**
	 * 初始化场次 days=0 初始化今天 days=1初始化明天 days=-1初始化昨天 以此类推
	 */
	public String updateInitSession(int days);
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);
//	public void updateFetchAndOpenTrend(Integer count);
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public FiveGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public FiveGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<String,Integer> betMap,List<GaBetOption> list,FiveGaSession gaK10Session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<FiveGaTrend> findFiveGaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findFiveGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(FiveGaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(FiveGaSession session,String openResult);
	/**
	 * 统计每期数据
	 * @return
	 */
	public PaginationSupport  findFiveGaBetList(String hql, List<Object> para,int pageNum,int pageSize);

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
	 * 根据id查询投注详情。
	 * @param string
	 * @param para
	 * @return
	 */
	public List<FiveDTO> findGaBetDetailById(String string, List<Object> para);
	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(FiveGaSession session);
}
