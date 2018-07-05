package com.xy.pk10.jsft.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.pk10.jsft.model.JsftGaSession;
import com.xy.pk10.jsft.model.JsftGaTrend;
import com.xy.pk10.jsft.model.dto.JsftDTO;


public interface IJsftDAO extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public JsftGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public JsftGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<JsftGaTrend> findJsftGaTrendList();
	/**
	 * 冷热所有
	 * @return
	 */
	public List<JsftGaTrend> findJsftGaTrendAllList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findJsftGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期的开奖数据
	 */
	public PaginationSupport  findJsftGaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<JsftDTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除JsftGaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteJsftGaBet(String hql, List<Object> para);
}
