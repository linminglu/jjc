package com.apps.dao;

import java.util.List;

import com.apps.model.SellerTypeRl;
import com.apps.model.Type;
import com.apps.model.TypeCate;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

/**
 * 类型dao
 * 
 * @author Mr.zang
 * 
 */
public interface ITypeDAO extends IDAO {

	public List<Type> findTypeList();

	public PaginationSupport findList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	
	public PaginationSupport findTypeList(String hqls, List<Object> para,
			int startIndex, int pageSize);

	public void updateSort(Integer id, String flag);

	/**
	 * 根据栏目的类型获得栏目
	 * 
	 * @param type
	 * @return
	 */
	public List<Type> findTypeList(String type);

	public SellerTypeRl findSellerTypeRlBySid(Integer sid);
	
	public List<Type> findGRTypeList();
	
	/**
	 * 获得商家的类型
	 * @param sid
	 * @return
	 */
	public Type getTypeBySid(String sid);

	/**
	 * 二级类型集合
	 * @param tid
	 * @return
	 */
	public List<Type> findTypeList(Integer tid);
	
	/**
	 * 获得所有有效的栏目类型
	 * @return List<TypeCate>
	 */
	public List<TypeCate> findTypeCate();

}
