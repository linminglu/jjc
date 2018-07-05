package com.xy.k3.jsk3.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.k3.jsk3.model.JsK3GaSession;
import com.xy.k3.jsk3.model.JsK3GaTrend;
import com.xy.k3.jsk3.model.dto.JsK3DTO;

public interface IJsK3DAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public JsK3GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public JsK3GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<JsK3GaTrend> findJsK3GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findJsK3GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 冷热所有
	 * @return
	 */
	public List<JsK3GaTrend> findJsK3GaTrendAllList();
	/**
	 * 查询投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findJsK3GaBetList(String hql, List<Object> para,
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
	public List<JsK3DTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除JsK3GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteJsK3GaBet(String hql, List<Object> para);
}
