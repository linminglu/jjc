package com.xy.k10.gxk10.dao;

import java.util.Date;
import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.k10.gxk10.model.GxK10GaSession;
import com.xy.k10.gxk10.model.GxK10GaTrend;
import com.xy.k10.gxk10.model.dto.GxK10DTO;


public interface IGxK10DAO extends IDAO {

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
	 * 冷热排行列表
	 * @return
	 */
	public List<GxK10GaTrend> findGxK10GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findGxK10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findGxK10GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
	/**
	 * 投注详细信息
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize);
	
	/**
	 * 根据id查询投注详情
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<GxK10DTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 根据指定时间获取上一期期号
	 * @param startDate
	 * @return
	 */
	public GxK10GaSession getPreSession(Date startDate);
	/**
	 * 删除CqSscGaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteGxK10GaBet(String hql, List<Object> para);
}
