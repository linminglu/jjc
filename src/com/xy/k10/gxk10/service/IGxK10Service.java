package com.xy.k10.gxk10.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.k10.gxk10.model.GxK10GaSession;
import com.xy.k10.gxk10.model.GxK10GaTrend;
import com.xy.k10.gxk10.model.dto.GxK10DTO;
import com.xy.ssc.cqssc.model.CqSscGaSession;

public interface IGxK10Service extends IService {

	/**
	 * 开奖
	 * @return
	 */
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);
	
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GxK10GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GxK10GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,GxK10GaSession gaK10Session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GxK10GaTrend> findGxK10GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findGxK10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 更新冷热排行榜
	 */
//	public void updateGaTrend();
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(GxK10GaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(GxK10GaSession session,String openResult);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findGxK10GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
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
	public List<GxK10DTO> findGaBetDetailById(String string, List<Object> para);

	/**
	 * 初始化化期号，day=0 初始化今天 day=1初始化明天,day=-1初始化昨天
	 * @param days
	 */
	public String updateInitSession(int days);

	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(GxK10GaSession session);
}
