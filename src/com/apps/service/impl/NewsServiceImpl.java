package com.apps.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.apps.dao.INewsDAO;
import com.apps.service.INewsService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.ParamUtils;
import com.ram.model.NewsCategory;
import com.ram.model.NewsCategoryRl;
import com.ram.model.NewsInformation;
import com.ram.model.NewsType;

public class NewsServiceImpl extends BaseService implements INewsService {
	private INewsDAO newsDAO;

	
	public INewsDAO getNewsDAO() {
		return newsDAO;
	}

	public void setNewsDAO(INewsDAO newsDAO) {
		this.newsDAO = newsDAO;
		super.dao = newsDAO;
	}
	public PaginationSupport findNewsInformationList(String hqls,
			List<Object> para, int startIndex, int pageSize) {
		return newsDAO.findNewsInformationList(hqls, para, startIndex, pageSize);
	}

	public List<NewsType> findNewsTypeList() {
		return newsDAO.findNewsTypeList();
	}


	public List<NewsCategory> findNewsCategoryList() {
		return newsDAO.findNewsCategoryList();
	}

	public List<NewsCategory> findNewsCategoryListByNid(Integer nid) {
		return newsDAO.findNewsCategoryListByNid(nid);
	}


	public void updateNewsInformation(NewsInformation newsInfo, int[] cids) {
		if(cids.length>0){
			List<NewsCategoryRl> saveList=new ArrayList<NewsCategoryRl>();
			newsDAO.deleteNewsCategoryRl(newsInfo.getNid());	
			for(int i=0;i<cids.length;i++){
				NewsCategoryRl rl=new NewsCategoryRl();
				rl.setCid(cids[i]);
				rl.setNid(newsInfo.getNid());
				saveList.add(rl);
			}
			newsDAO.updateObjectList(saveList, null);	
		}
		if(ParamUtils.chkInteger(newsInfo.getNid())){
			NewsInformation temp=(NewsInformation) newsDAO.getObject(NewsInformation.class, newsInfo.getNid());
			temp.setTid(newsInfo.getTid());
			temp.setType(newsInfo.getType());
			temp.setContent(newsInfo.getContent());
			temp.setCompany(newsInfo.getCompany());
			temp.setAuthor(newsInfo.getAuthor());
			if(ParamUtils.chkString(newsInfo.getSubTitle())){
				temp.setSubTitle(newsInfo.getSubTitle());
			}
			temp.setTitle(newsInfo.getTitle());	
			newsDAO.updateObject(temp,null);
		}
		
	}


	public void saveNewsInformation(NewsInformation newsInfo, int[] cids) {
//		NewsInformation prevInfo=newsDAO.getPrevNewsInformation();
//		newsInfo.setPrevId(prevInfo.getNid());
		newsInfo=(NewsInformation) newsDAO.saveObjectDB(newsInfo);
		if(cids.length>0){
			List<NewsCategoryRl> saveList=new ArrayList<NewsCategoryRl>();
			for(int i=0;i<cids.length;i++){
				NewsCategoryRl rl=new NewsCategoryRl();
				rl.setCid(cids[i]);
				rl.setNid(newsInfo.getNid());
				saveList.add(rl);
			}
			newsDAO.updateObjectList(saveList, null);
		}

	}

}
