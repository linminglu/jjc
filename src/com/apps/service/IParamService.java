package com.apps.service;

import java.util.List;

import com.apps.model.Param;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;

/**
 * 类型service
 * 
 * @author Mr.zang
 * 
 */
public interface IParamService extends IService {

	/**
	 * 获得全部的类型
	 * 
	 * @return
	 */
	public List<Param> findParamList();
	public List<Param> findParamList(String status);
	public List<Param> findParamList(String status,String readonly);

	public PaginationSupport findList(String string, List<Object> para,
			int startIndex, int pageSize);

	public void updateSort(Integer id, String flag);

	public Param saveParam(Param param);
	public void delParam(Param param) ;
	
	public String getNamebyCode(Integer ParamId) ;
	public String getValueByCode(String code);
	/*
	 * 根据type获取设置。
	 */
	public Param getParamByType(String defPay);
	
	public List<Param> findParamList(String string, List<Object> para);


}
