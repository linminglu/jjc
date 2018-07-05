package com.xy.pk10.sfpk10.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.pk10.sfpk10.model.SfPk10GaSession;
import com.xy.pk10.sfpk10.model.SfPk10GaTrend;
import com.xy.pk10.sfpk10.model.dto.SfPk10DTO;


public interface ISfPk10DAO extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public SfPk10GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public SfPk10GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<SfPk10GaTrend> findSfPk10GaTrendList();
	/**
	 * 冷热所有
	 * @return
	 */
	public List<SfPk10GaTrend> findSfPk10GaTrendAllList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findSfPk10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期的开奖数据
	 */
	public PaginationSupport  findSfPk10GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<SfPk10DTO> findGaBetDetailById(String hql, List<Object> para);
	
	/**
	 * 删除SfPk10GaBet表数据
	 * @param string
	 * @param para
	 */
	public void deleteSfPk10GaBet(String string, List<Object> para);

}
