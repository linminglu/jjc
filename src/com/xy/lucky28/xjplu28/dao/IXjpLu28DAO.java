package com.xy.lucky28.xjplu28.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.lucky28.xjplu28.model.XjpLu28GaSession;
import com.xy.lucky28.xjplu28.model.XjpLu28GaTrend;
import com.xy.lucky28.xjplu28.model.dto.XjpLu28DTO;

public interface IXjpLu28DAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public XjpLu28GaSession getCurrentSession();
	/**
	 * 获取当前场次 开奖或者未开奖的
	 * @return
	 */
	public XjpLu28GaSession getTempCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public XjpLu28GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<XjpLu28GaTrend> findXjpLu28GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findXjpLu28GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 冷热所有
	 * @return
	 */
	public List<XjpLu28GaTrend> findXjpLu28GaTrendAllList();
	/**
	 * 查询投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findXjpLu28GaBetList(String hql, List<Object> para,
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
	 * 根据id查询投注详情
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<XjpLu28DTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除XjpLu28GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteXjpLu28GaBet(String hql, List<Object> para);
}
