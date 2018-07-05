package com.xy.k10.cqk10.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.k10.cqk10.model.CqK10GaSession;
import com.xy.k10.cqk10.model.CqK10GaTrend;
import com.xy.k10.cqk10.model.dto.CqK10DTO;

public interface ICqK10DAO extends IDAO {

	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public CqK10GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public CqK10GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<CqK10GaTrend> findCqK10GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findCqK10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findCqK10GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<CqK10DTO> findGaBetDetailById(String hql, List<Object> para);
	
	/**
	 * 删除CqK10GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteCqK10GaBet(String hql, List<Object> para);

}
