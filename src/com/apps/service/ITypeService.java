package com.apps.service;

import java.util.List;

import com.apps.model.SellerTypeRl;
import com.apps.model.Type;
import com.apps.model.TypeCate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;


/**
 * 类型service
 * 
 * @author Mr.zang
 * 
 */
public interface ITypeService extends IService {

	/**
	 * 获得全部的类型
	 * 
	 * @return
	 */
	public List<Type> findTypeList();

	public PaginationSupport findList(String string, List<Object> para,
			int startIndex, int pageSize);
	
	public PaginationSupport findTypeList(String string, List<Object> para,
			int startIndex, int pageSize);

	public void updateSort(Integer id, String flag);

	public Type saveType(Type type);

	/**
	 * 根据栏目类型获得一级栏目
	 * @param 模块类型  1.电商模块2.订餐模块3.团购模块
	 * @return
	 */
	public List<Type> findTypeList(String type);
	public SellerTypeRl findSellerTypeRlBySid(Integer sid);
	public List<Type> findGRTypeList();

	/**
	 * 获得商家所在的栏目
	 * @param sid
	 * @return
	 */
	public Type getTypeBySid(String sid);

	/**
	 * 获得二级/三级栏目
	 * @param tid
	 * @return
	 */
	public List<Type> findTypeList(Integer tid);
	
	/**
	 * 获得所有有效的栏目类型
	 * @return List<TypeCate>
	 */
	public List<TypeCate> findTypeCate();
	
	/**
	 * 删除栏目
	 * @param tid
	 * @return 200=ok 201=fail
	 */
	public String delType(Integer tid);

}
