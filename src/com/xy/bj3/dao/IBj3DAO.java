package com.xy.bj3.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.bj3.model.Bj3GaSession;
import com.xy.bj3.model.Bj3GaTrend;
import com.xy.bj3.model.dto.Bj3DTO;

public interface IBj3DAO  extends IDAO {

	/**
	 * 根据系统当前时间获取当前场次
	 * @return
	 */
	public Bj3GaSession getCurrentSession();

	/**
	 * 根据期号获取场次相关信息
	 * @param sessionNo
	 * @return
	 */
	public Bj3GaSession getPreviousSessionBySessionNo(String sessionNo);

	/**
	 * 冷热排行榜列表
	 * @return
	 */
	public List<Bj3GaTrend> findBj3GaTrendList();
	
	/**
	 * 冷热排行所有数据
	 * @return
	 */
	public List<Bj3GaTrend> findBj3GaTrendAllList();

	/**
	 * 
	 * @return
	 */
	public PaginationSupport findBj3GaSessionList(String hsql,List<Object> para,int pageNum, int pageSize);

	public List<Bj3GaSession> getCurrentSession(int start, int end);
	
	/**
	 * 查询北京3分彩投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBj3GaBetList(String hql, List<Object> para,
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
	public List<Bj3DTO> findGaBetDetailById(String hql, List<Object> para);
	
	/**
	 * 删除Bj3GaBet表数据
	 * @param string
	 * @param para
	 */
	public void deleteBj3GaBet(String string, List<Object> para);
	
}
