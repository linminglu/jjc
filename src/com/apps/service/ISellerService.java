package com.apps.service;

import java.util.Date;
import java.util.List;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.ram.model.User;
import com.ram.model.dto.UserDTO;
import com.apps.model.Promotion;
import com.apps.model.PromotionRule;
import com.apps.model.Seller;
import com.apps.model.SellerUserRl;
import com.apps.model.Type;
import com.apps.web.form.SellerForm;

/**
 * 商家service
 * 
 * @author Mr.zang
 * 
 */
public interface ISellerService extends IService {

	public PaginationSupport search(String string, List<Object> para,
			int pageIndex, int pageSize);

	/**
	 * 获得商家列表
	 * 
	 * @return
	 */
	public PaginationSupport findSellerList(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 保存商家信息
	 * 
	 * @param seller
	 * @return
	 */
	public Seller saveSeller(Seller seller, SellerForm form);

	/**
	 * 获得商家的类型
	 * 
	 * @param sid
	 * @return
	 */
	public List<Type> findSellerType(Integer sid);

	/**
	 * 获得全部的商家信息
	 * 
	 * @param status
	 * @return
	 */
	public List<Seller> findAllList(String status);

	public List<Seller> findAllListByColumnId(Integer columnId);

	public List<Seller> findAllListByTid(String status, Integer tid);

	/**
	 * 查询某种类型下的所有栏目下的商家
	 * 
	 * @param status
	 * @param tid
	 * @return
	 */
	public List<Seller> findAllListByTids(String status, String tids);

	/**
	 * 获得商家用户关系
	 * 
	 * @param userId
	 * @return
	 */
	public SellerUserRl getSellerUser(Integer userId);

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
	public PaginationSupport findSellerCommStore(String hqls,
			List<Object> para, int statIndex, int pageSize);

	/**
	 * 商家促销
	 * 
	 * @return
	 */
	public PaginationSupport findPromotionList(String string,
			List<Object> para, int startIndex, int pageSize);

	/**
	 * 获得促销规则
	 * 
	 * @param id
	 * @return
	 */
	public List<PromotionRule> findPromotionRule(Integer id);

	/**
	 * 保存优惠
	 * 
	 * @param promotion
	 * @param minusPrices
	 * @param fullPrices
	 * @return
	 */
	public Promotion savePromotion(Promotion promotion, String[] fullPrices,
			String[] minusPrices);

	/**
	 * 计算商家满减状态
	 * 
	 * @param nowDate
	 */
	public void updateSellerMinusStatus(String nowDate);

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

	/**
	 * 获得商家所在的类型
	 * 
	 * @param sid
	 * @return
	 */
	public List<Type> findTypeBySid(Integer sid);

	/**
	 * 获得商家列表（sql）
	 * 
	 * @param sqls
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @param longAlt
	 * @return
	 */
	public PaginationSupport findSellerListSql(String sqls, List<Object> para,
			int statIndex, int pageSize, String longAlt);

	/**
	 * 获得热卖的商家
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
	 * 根据商家id
	 * 
	 * @param sid
	 * @return
	 */
	public List<User> findSellerUserBySid(Integer sid);
	
	/**
	 * 获得城市下的商家
	 * 
	 * @param cid
	 * @return
	 */
	public List<Seller> findSellerByCid(Integer cid);

	/**
	 * 获得类型下的商家数量
	 * 
	 * @param tid
	 * @return
	 */
	public int getSellerCountByTid(Integer tid);

	/**
	 * 团购商家列表
	 * 
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
	 * 团购商家的评价
	 * 
	 * @return
	 */
	public PaginationSupport findSellerCommBuy(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 检测员工是否是派单人员
	 * @param userId
	 * @param type
	 * @return
	 */
	public boolean chkSellerUserTask(Integer userId, String type);

	/**
	 * 获得商家的人员
	 * @param sid
	 * @return
	 */
	public List<UserDTO> findSellerUser(Integer sid);

	public List<User> findSellerUserBySid(Integer sid, String employeeTypeTask);
	/**
	 * 获得商家用户关系
	 * 
	 * @param userId
	 * @return
	 */
	public SellerUserRl getSellerUser(Integer sid,String type);
	/**
	 * 获得商家商家用户的类型
	 * @param userId
	 * @return
	 */
	public SellerUserRl getSellerUserType(Integer userId);
	/**
	 * 删除商家
	 * @param sid
	 * @return 200=ok 201=fail
	 */
	public String delSeller(Integer sid,String type);
}
