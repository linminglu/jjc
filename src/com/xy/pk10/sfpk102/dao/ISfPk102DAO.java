package com.xy.pk10.sfpk102.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.pk10.sfpk102.model.SfPk102GaSession;
import com.xy.pk10.sfpk102.model.SfPk102GaTrend;
import com.xy.pk10.sfpk102.model.dto.SfPk102DTO;


public interface ISfPk102DAO extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public SfPk102GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public SfPk102GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<SfPk102GaTrend> findSfPk102GaTrendList();
	/**
	 * 冷热所有
	 * @return
	 */
	public List<SfPk102GaTrend> findSfPk102GaTrendAllList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findSfPk102GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期的开奖数据
	 */
	public PaginationSupport  findSfPk102GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	 * 根据投注id查询投注详情。
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<SfPk102DTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除SfPk102GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteSfPk102GaBet(String hql, List<Object> para);
}
