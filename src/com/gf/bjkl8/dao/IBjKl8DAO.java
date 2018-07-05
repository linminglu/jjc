package com.gf.bjkl8.dao;

import java.util.List;

import com.gf.bjkl8.model.GfBjKl8GaSession;
import com.gf.bjkl8.model.GfBjKl8GaTrend;
import com.gf.bjkl8.model.dto.GfBjKl8DTO;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

public interface IBjKl8DAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GfBjKl8GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GfBjKl8GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GfBjKl8GaTrend> findBjKl8GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findBjKl8GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 冷热所有
	 * @return
	 */
	public List<GfBjKl8GaTrend> findBjKl8GaTrendAllList();
	/**
	 * 查询投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBjKl8GaBetList(String hql, List<Object> para,
			int pageNum, int pageSize);

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
	public List<GfBjKl8DTO> findGaBetDetailById(String hql, List<Object> para);

}
