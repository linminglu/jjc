package com.xy.pk10.xyft.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.xy.pk10.xyft.model.XyftGaSession;
import com.xy.pk10.xyft.model.XyftGaTrend;
import com.xy.pk10.xyft.model.dto.XyftDTO;
import com.xy.ssc.cqssc.model.CqSscGaSession;
import com.ram.model.User;

public interface IXyftService extends IService {
	
	/**
	 * 初始化期号 days=0 初始化昨天 days=1初始化明天  days=-1初始化昨天
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
	public XyftGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public XyftGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,XyftGaSession gaK10Session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<XyftGaTrend> findXyftGaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findXyftGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期的开奖数据
	 */
	public PaginationSupport  findXyftGaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(XyftGaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(XyftGaSession session,String openResult);
	
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
	public List<XyftDTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(XyftGaSession session);
}
