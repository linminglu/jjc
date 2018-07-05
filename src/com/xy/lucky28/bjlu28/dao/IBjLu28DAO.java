package com.xy.lucky28.bjlu28.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.lucky28.bjlu28.model.BjLu28GaSession;
import com.xy.lucky28.bjlu28.model.BjLu28GaTrend;
import com.xy.lucky28.bjlu28.model.dto.BjLu28DTO;

public interface IBjLu28DAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public BjLu28GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public BjLu28GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<BjLu28GaTrend> findBjLu28GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findBjLu28GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 冷热所有
	 * @return
	 */
	public List<BjLu28GaTrend> findBjLu28GaTrendAllList();
	/**
	 * 查询投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBjLu28GaBetList(String hql, List<Object> para,
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
	public List<BjLu28DTO> findGaBetDetailById(String hql, List<Object> para);

	/**
	 * 删除BjLu28GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteBjLu28GaBet(String hql, List<Object> para);
}
