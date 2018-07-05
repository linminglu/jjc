package com.apps.dao;

import java.util.List;

import com.apps.model.Param;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

/**
 * 类型dao
 * @author Mr.zang
 *
 */
public interface IParamDAO extends IDAO {

	public List<Param> findParamList();
	public List<Param> findParamList(String status);
	public List<Param> findParamList(String status,String readonly);

	public PaginationSupport findList(String hqls, List<Object> para,
			int startIndex, int pageSize);

	public void updateSort(Integer id, String flag);
	public void delParam(Integer paramId) ;
	public String getNamebyCode(Integer paramId) ;
	public String getValueByCode(String code);
	public Param getParamByType(String defPay);
	
	/*
	 * 获取配置信息
	 */
	public Param getParam(String string, List<Object> para);
	public List<Param> findParamList(String string, List<Object> para);

}
