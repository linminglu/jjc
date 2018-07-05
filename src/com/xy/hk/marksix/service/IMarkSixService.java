package com.xy.hk.marksix.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.hk.marksix.model.MarkSixGaSession;
import com.xy.hk.marksix.model.MarkSixGaTrend;
import com.xy.hk.marksix.model.dto.MarkSixDTO;
import com.xy.ssc.cqssc.model.CqSscGaSession;

public interface IMarkSixService   extends IService {
	/**
	 * 初始化场次
	 */
	public String updateInitSession();
	
	/**
	 * 开奖
	 * @return
	 */
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);
	
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public MarkSixGaSession getCurrentSession(Date now);
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public MarkSixGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,MarkSixGaSession session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<MarkSixGaTrend> findMarkSixTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findMarkSixGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);

	
	/**
	 * 更新冷热排行
	 * @param count
	 */
//	public void updateFetchAndOpenTrendResult(Integer count);
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(MarkSixGaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(MarkSixGaSession session,String openResult);
	/**
	 * 获取投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findMarkSixGaBetList(String hql,
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
	public List<MarkSixDTO> findGaBetDetailById(String string, List<Object> para);

	/**
	 * 修改结束时间。自动根据当前期修改的时间修改剩下期号的时间。
	 * @param session
	 * @param endTime
	 * @return
	 */
	public boolean modifyDate(MarkSixGaSession session, String endTime);

	/**
	 * 根据当前时间获取上一期
	 * @param now
	 * @return
	 */
	public MarkSixGaSession getPreSession(Date now);
	
	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(MarkSixGaSession session);

}
