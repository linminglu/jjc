package com.xy.pick11.jxpick11.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.pick11.jxpick11.model.JxPick11GaSession;
import com.xy.pick11.jxpick11.model.JxPick11GaTrend;
import com.xy.pick11.jxpick11.model.dto.JxPick11DTO;

public interface IJxPick11DAO extends IDAO {

	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public JxPick11GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public JxPick11GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<JxPick11GaTrend> findJxPick11GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findJxPick11GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findJxPick11GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<JxPick11DTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除JxPick11GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteJxPick11GaBet(String hql, List<Object> para);
}
