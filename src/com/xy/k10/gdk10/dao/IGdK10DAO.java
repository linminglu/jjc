package com.xy.k10.gdk10.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.bj3.model.dto.Bj3DTO;
import com.xy.k10.gdk10.model.GdK10GaSession;
import com.xy.k10.gdk10.model.GdK10GaTrend;
import com.xy.k10.gdk10.model.dto.GdK10DTO;

public interface IGdK10DAO extends IDAO {

	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GdK10GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GdK10GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GdK10GaTrend> findGdK10GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findGdK10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findGdK10GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<GdK10DTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除GdK10GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteGdK10GaBet(String hql, List<Object> para);
}
