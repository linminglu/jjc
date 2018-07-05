package com.apps.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.model.NewsCategory;
import com.ram.model.NewsInformation;
import com.ram.model.NewsType;

public interface INewsDAO extends IDAO {

	/**
	 * 查询新闻资讯列表
	 */
	public PaginationSupport findNewsInformationList(String hqls, List<Object> para,
			int startIndex, int pageSize);

	public List<NewsType> findNewsTypeList();

	public List<NewsCategory> findNewsCategoryList();

	public List<NewsCategory> findNewsCategoryListByNid(Integer nid);

	public void deleteNewsCategoryRl(Integer nid);

	public NewsInformation getPrevNewsInformation();

}
