package com.xy.pk10.bjpk10.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.pk10.bjpk10.model.BjPk10GaSession;
import com.xy.pk10.bjpk10.model.BjPk10GaTrend;
import com.xy.pk10.bjpk10.model.dto.BjPk10DTO;

public interface IBjPk10DAO extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public BjPk10GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public BjPk10GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<BjPk10GaTrend> findBjPk10GaTrendList();
	/**
	 * 冷热所有
	 * @return
	 */
	public List<BjPk10GaTrend> findBjPk10GaTrendAllList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findBjPk10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期的开奖数据
	 */
	public PaginationSupport  findBjPk10GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<BjPk10DTO> findGaBetDetailById(String hql, List<Object> para);
	
	/**
	 * 删除BjPk10GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteBjPk10GaBet(String hql, List<Object> para);

}
