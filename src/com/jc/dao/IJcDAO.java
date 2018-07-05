package com.jc.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.jc.model.JcField;
import com.jc.model.JcOption;
import com.jc.model.JcPlayType;
import com.jc.model.JcTeam;
import com.jc.model.Type;

public interface IJcDAO extends IDAO {

	public List<Type> getTypeList(String type);
	
	public List<JcTeam> getTeamList();
	
	public JcTeam getTeamByTitle(String title);
	
	/**
	 * 精确查询，非模糊 
	 */
	public JcPlayType getJcPlayTypeByTitle(String title);
	
	public List<JcPlayType> getJcPlayTypesByFid(Integer fId, String status);
	
	public List<JcOption> getJcOptionsByPtId(Integer ptId);
	
	public List<JcField> getJcFieldByMid(Integer mid,String status);
	
	public List<JcPlayType> getJcPlayNotOpen(Integer fId);
	
	/**
	 * 查询比赛下的所有投注项(开奖界面用)
	 * @return
	 */
	public PaginationSupport findJcOptionList(String hql, List<Object> para,int statIndex,int pageNum);
}
