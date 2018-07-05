package com.apps.dao;

import java.util.List;

import com.apps.model.Promotion;
import com.apps.model.PromotionRule;
import com.apps.model.Seller;
import com.apps.model.SellerUserRl;
import com.apps.model.Type;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.model.User;
import com.ram.model.dto.UserDTO;

public interface ISellerDAO extends IDAO {

	/**
	 * 获得商家列表
	 * 
	 * @return
	 */
	public PaginationSupport findSellerList(String hqls, List<Object> para,
			int pageIndex, int pageSize);

	/**
	 * 删除商家的类型
	 * 
	 * @param sid
	 */
	public void delSellerType(Integer sid);

	/**
	 * 获得商家的一级类型
	 * 
	 * @param sid
	 * @return
	 */
	public List<Type> findSellerType(Integer sid);

	/**
	 * 获得商家信息
	 * 
	 * @param status
	 * @return
	 */
	public List<Seller> findAllList(String status);
	public List<Seller> findAllListByTid(String status,Integer tid);
	public List<Seller> findAllListByColumnId(Integer columnId);
	/**
	 * 查询一种类型下的所有栏目下的商家
	 * @param status
	 * @param tids
	 * @return
	 */
	public List<Seller> findAllListByTids(String status,String tids);
	/**
	 * 获得商家用户关系
	 * 
	 * @param userId
	 * @return
	 */
	public SellerUserRl getSellerUser(Integer userId);
	/**
	 * 获得商家用户关系
	 * 
	 * @param userId
	 * @return
	 */
	public SellerUserRl getSellerUser(Integer sid,String type);
	/**
	 * 获得商家下的评论
	 * 
	 * @return
	 */
	public PaginationSupport findSellerCommEat(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 获得商家下的评论
	 * 
	 * @return
	 */
	public PaginationSupport findSellerCommStore(String hqls, List<Object> para,
			int statIndex, int pageSize);
	/**
	 * 获得商家促销
	 * 
	 * @return
	 */
	public PaginationSupport findPromotionList(String hqls, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 获得促销规则
	 * 
	 * @param id
	 * @return
	 */
	public List<PromotionRule> findPromotionRule(Integer id);

	/**
	 * 删除促销规则
	 * 
	 * @param id
	 */
	public void delPromotionRule(Integer id);

	/**
	 * 获得有优惠活动
	 * 
	 * @param nowDate
	 * @return
	 */
	public List<Promotion> findPromotion(String nowDate);

	/**
	 * 把商家 改为满减状态
	 * 
	 * @param sids
	 */
	public void updateSellerMinusStatus(List<Integer> sids, String status);

	/**
	 * 获得商家有效的促销信息
	 * 
	 * @param sid
	 *            商家id
	 * @param nowDate
	 *            当前时间
	 * @return
	 */
	public List<PromotionRule> findSellerPromotionRule(Integer sid,
			String nowDate);

	/**
	 * 获得用户所属的商家信息
	 * 
	 * @param uid
	 * @return
	 */
	public Seller getSellerByUserId(Integer uid);

	public PaginationSupport search(String hqls, List<Object> para,
			int pageIndex, int pageSize);

	/**
	 * 失效的优惠活动
	 * 
	 * @param nowDate
	 * @return
	 */
	public List<Promotion> findPromotionFail(String nowDate);

	/**
	 * 获得商家所在的分类
	 * 
	 * @param sid
	 * @return
	 */
	public List<Type> findTypeBySid(Integer sid);

	public PaginationSupport findSellerListSql(String sqls, List<Object> para,
			int statIndex, int pageSize, String longAlt);

	/**
	 * 获得热卖商家
	 * 
	 * @param hqls
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findSellerHot(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 根据商家id获得商家用户
	 * 
	 * @param sid
	 * @return
	 */
	public List<User> findSellerUserBySid(Integer sid);
	
	/**
	 * 获得城市下的商家
	 * @param cid
	 * @return
	 */
	public List<Seller> findSellerByCid(Integer cid);

	/**
	 * 获得类型下的商家数量
	 * @param tid
	 * @return
	 */
	public int getSellerCountByTid(Integer tid);

	/**
	 * 获得团购商家列表
	 * @param hqls
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @param longAlt
	 * @return
	 */
	public PaginationSupport findSellerListSqlBuy(String hqls,
			List<Object> para, int statIndex, int pageSize, String longAlt);

	/**
	 * 获得团购商家的评论
	 * @param hqls
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findSellerCommBuy(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 检测是否是送单人员
	 * @param userId
	 * @param type
	 * @return
	 */
	public boolean chkSellerUserTask(Integer userId, String type);

	/**
	 * 获得商家的员工
	 * @param sid
	 * @return
	 */
	public List<UserDTO> findSellerUser(Integer sid);

	/**
	 * 获得商家员工
	 * @param sid
	 * @param type  1.普通员工 2.派单人员 3.送货人员
	 * @return
	 */
	public List<User> findSellerUserBySid(Integer sid, String type);
	/**
	 * 获得商家用户的类型
	 * @param userId
	 * @return
	 */
	public SellerUserRl getSellerUserType(Integer userId);
	/**
	 * 删除商家用户关联表
	 */
	public void delSellerUserRl(Integer sid);
}
