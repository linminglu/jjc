package com.apps.service;

import java.util.List;


import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.ram.model.NewsCategory;
import com.ram.model.NewsInformation;
import com.ram.model.NewsType;

public interface INewsService extends IService {

	/**
	 * 查询新闻资讯列表
	 */
	public PaginationSupport findNewsInformationList(String string, List<Object> para,
			int startIndex, int pageSize);

	public List<NewsType> findNewsTypeList();

	public List<NewsCategory> findNewsCategoryList();

	public List<NewsCategory> findNewsCategoryListByNid(Integer nid);

	public void updateNewsInformation(NewsInformation newsInfo, int[] cids);

	public void saveNewsInformation(NewsInformation newsInfo, int[] cids);




}
