package com.xy.pick11.gdpick11.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.pick11.gdpick11.model.GdPick11GaSession;
import com.xy.pick11.gdpick11.model.GdPick11GaTrend;
import com.xy.pick11.gdpick11.model.dto.GdPick11DTO;

public interface IGdPick11DAO extends IDAO {

	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GdPick11GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GdPick11GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GdPick11GaTrend> findGdPick11GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findGdPick11GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findGdPick11GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<GdPick11DTO> findGaBetDetailById(String hql, List<Object> para);

	/**
	 * 删除GdPick11GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteGdPick11GaBet(String hql, List<Object> para);
}
