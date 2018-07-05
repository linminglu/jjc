/*
 * Created on 2005-7-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.service.system.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;
import org.hibernate.criterion.DetachedCriteria;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.ram.dao.system.ISystemTutorCenterDAO;
import com.ram.model.RecursionBean;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.service.system.ISystemTutorCenterService;

/**
 * @author yangjy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SystemTutorCenterServiceImpl 
	extends BaseService 
	implements ISystemTutorCenterService {
	
	private ISystemTutorCenterDAO systemTutorCenterDAO;
	private List tcTree;
	private List subTcId;
	
	/**
	 * IOC方式注入DAO
	 * @param DAO
	 */
	public void setSystemTutorCenterDAO(ISystemTutorCenterDAO systemTutorCenterDAO){
		this.systemTutorCenterDAO = systemTutorCenterDAO;		
	};	

	/**
	 * 保存课程考核信息
	 * @param courseExamSet
	 * @return
	 */
	public void saveSystemTutorCenter(TutorCenter tutorCenter, User user){
		systemTutorCenterDAO.saveSystemTutorCenter(tutorCenter, user);
	};

	/**
	 * 通过课程考核信息主键进行查找
	 * @param id
	 */
	public TutorCenter getSystemTutorCenter(Integer id){
		TutorCenter tutorCenter = systemTutorCenterDAO.getSystemTutorCenter(id);
		return tutorCenter;
	};

	/**
	 * 通过持久化对象找到全部列表
	 * @return
	 */
	public List findSystemTutorCenters(){
		List list = systemTutorCenterDAO.findSystemTutorCenters();
		return list;
	};

	public void removeSystemTutorCenter(Integer id, User user) {
		systemTutorCenterDAO.removeSystemTutorCenter(id, user);
	}
	
	public void updateSystemTutorCenter (TutorCenter tutorCenter,User user){
		systemTutorCenterDAO.updateSystemTutorCenter(tutorCenter, user);
	}
	/**
	 * 获得指定学习中心ID的所有下级学习中心
	 * @param id
	 */
	public List findSubTutorCenterByTcId (Integer tcId){
		return systemTutorCenterDAO.findSubTutorCenterByTcId(tcId);
	}

	public void deleteSystemTutorCenter(Integer id, User user) {
		systemTutorCenterDAO.deleteSystemTutorCenter(id, user);		
	}
	
	/**
	 * 得到指定学习中心的所有上级学习中心 
	 * 
	 */
	public List findParentTutorCentersByTcLevel(int tcLevel){
		return systemTutorCenterDAO.findParentTutorCentersByTcLevel(tcLevel);
	}

	/**
	 * 递归得到所有的学习中心
	 * 
	 */
	public List findAllSubTutorCenters(int tcId, List list) {
		return systemTutorCenterDAO.getTutorCenterList(tcId, list);		
	}

	public int getTcLevelByParentTcId(int parentTcId) {		
		return systemTutorCenterDAO.getTcLevelByParentTcId(parentTcId);
	}
	
	public TutorCenter getHeadQuarterTutorCenterID(){
		return systemTutorCenterDAO.getHeadQuarterTutorCenter();
	}

	/**
	 * 得到学习中心的所有下级学习中心的树状结构
	 * 提供给下拉框使用
	 * <html:select name="systemTutorCenterSetForm" property="tutorCenter.parentTcId">
	 *    	<html:options collection="tutorCenterCollection" property="value" labelProperty="label"/>
	 * </html:select>	
	 */
	public List getTutorCenterTree(int parentTcId) {
//		this.tcTree = new ArrayList();
//		List list = this.systemTutorCenterDAO.getTutorCenter();
//		rcs("", parentTcId, list);
//
//		return this.tcTree;
		List list = new ArrayList();
		
		List tutorCenterList = this.findAllSubTutorCenters(parentTcId, list);
		List tutorCenterCollection = new ArrayList();
		TutorCenter tutorCenter = null;
		
		//if(parentTcId==0){
		//	tutorCenterCollection.add(new LabelValueBean("无", "0"));
		//}
		for(int i = 0; i < tutorCenterList.size(); i++){
			tutorCenter = new TutorCenter();
			tutorCenter = (TutorCenter)tutorCenterList.get(i);
			String tcTitle = tutorCenter.getTcTitle();
			int level = tutorCenter.getTcLevel().intValue();
			for(int j = 0; j < level; j++){
				tcTitle = "— " + tcTitle;
			}			
			tutorCenterCollection.add(new LabelValueBean(tcTitle, (tutorCenter.getTcId()).toString()));
		}
		return tutorCenterCollection;
	}
	
	public Integer[] getSubTcId(int cid) {
		this.subTcId = new ArrayList();
		List list = this.systemTutorCenterDAO.getTutorCenter();
		rcs(cid, list);
		Integer[] tcId = new Integer[subTcId.size()+1];
		tcId[0] = new Integer(cid);
		Iterator it = subTcId.iterator();
		int i = 1;
		while(it.hasNext()){
			tcId[i++] = (Integer)it.next();
		}
		return tcId;
	}

	private void rcs(String prefix, int cid, List list){
		Iterator it = list.iterator();
		while(it.hasNext()){
			RecursionBean bean = (RecursionBean)it.next();
			if(bean.getCid()==null){
				log.info(bean.getTitle()+"的父Id为空");
			}else if(bean.getCid().intValue() == cid){
				this.tcTree.add(new LabelValueBean(prefix+bean.getTitle(), bean.getId().toString()));
				rcs(prefix + "——", bean.getId().intValue(), list);
			}
		}
	}

	private void rcs(int cid, List list){
		Iterator it = list.iterator();
		while(it.hasNext()){
			RecursionBean bean = (RecursionBean)it.next();
			if(bean.getCid()==null){
				log.info(bean.getTitle()+"的父Id为空");
			}else if(bean.getCid().intValue() == cid){
				this.subTcId.add(bean.getId());
				rcs(bean.getId().intValue(), list);
			}
		}
	}
	
	public PaginationSupport findALLTutorCenterPage(int startIndex, int pageSize) {
		List l = systemTutorCenterDAO.findALLTutorCenters(startIndex, pageSize);
		int num = systemTutorCenterDAO.findALLTutorCenterNum();
		PaginationSupport ps = new PaginationSupport(l, num, pageSize,
				startIndex);
		return ps;
	}
	
	public PaginationSupport findTutorCenterByCriteria(
			DetachedCriteria detachedCriteria, int pageSize, int startIndex) {
		return systemTutorCenterDAO.findElTutorCenterByCriteria(detachedCriteria, pageSize,
				startIndex);
	}
	
	
	public TutorCenter getTutorCenterByTcCode(String tcCode){
		return systemTutorCenterDAO.getTcByTcCode(tcCode);
		
	}
	
	public TutorCenter getTutorCenterByUserId(Integer userId){
		return systemTutorCenterDAO.getTutorCenterByUserId(userId);
	}
	
	public TutorCenter getUserTutorCenter(Integer userId){
		TutorCenter tc = null;
		tc = systemTutorCenterDAO.getLearnerTutotCenter(userId);
		if(tc == null){
			tc = systemTutorCenterDAO.getTeacherTutotCenter(userId);
		}
		if(tc == null){
			tc = systemTutorCenterDAO.getManagerTutotCenter(userId);
		}
		return tc;
	}
	
	public TutorCenter getTutorCenter(Integer tcId){
		return (TutorCenter)systemTutorCenterDAO.getObject(TutorCenter.class,tcId);
	}
	
	public List findTutorCenterForEduBulletin(int tcId){
		List list = new ArrayList();
		if(tcId == 1){//总部
			list = systemTutorCenterDAO.findTutorCenterForEduBulletin();
		}else{
			list = systemTutorCenterDAO.findTutorCenterForEduBulletinByParTcId(tcId);
			if(list != null && list.size() > 0){//直属中心
//				list = list;
			}else{//地方中心
				list.add(systemTutorCenterDAO.getObject(TutorCenter.class,new Integer(tcId)));
			}
		}
		return list;
	}
	
	public List findNotInTutorCenterForEduBulletin(int tcId,int eduBulletinId){
		List list = new ArrayList();
		if(tcId == 1){//总部
			if(eduBulletinId > 0){//修改
				list = systemTutorCenterDAO.findNotInTutorCenterForEduBulletin(eduBulletinId);
			}else{//创建
				list = systemTutorCenterDAO.findTutorCenterForEduBulletin();
			}
		}else{
			list = systemTutorCenterDAO.findTutorCenterForEduBulletinByParTcId(tcId);
			if(list != null && list.size() > 0){//直属中心
				if(eduBulletinId > 0){//修改
					list = systemTutorCenterDAO.findNotInTutorCenterForEduBulletinByParTcId(tcId,eduBulletinId);
				}else{//创建
				}
			}else{//地方中心
				if(eduBulletinId > 0){//修改
				}else{//创建
					list.add(systemTutorCenterDAO.getObject(TutorCenter.class,new Integer(tcId)));
				}
			}
		}
		return list;
	}
	
	public List findInTutorCenterForEduBulletin(int eduBulletinId){
		return systemTutorCenterDAO.findInTutorCenterForEduBulletin(eduBulletinId);
	}
	
	public List findSubTutorCenter(Integer tcId){
		return systemTutorCenterDAO.findSubTutorCenter(tcId);
	}

	public Integer tcIdInMyScope(Integer tcId, Integer selfTcId){
		if(selfTcId==null)return new Integer(0);
		if(tcId==null)return selfTcId.intValue()==1?new Integer(0):selfTcId;
		Integer pTcId = systemTutorCenterDAO.getParentTcId(tcId);
		while(true){
			if(pTcId == null)break;
			if(pTcId.intValue() == selfTcId.intValue())return tcId;
			pTcId = systemTutorCenterDAO.getParentTcId(pTcId);
		}
		return selfTcId;
	}
	
	public List getAllTc(){
		return systemTutorCenterDAO.getAllTc();
	}
}
