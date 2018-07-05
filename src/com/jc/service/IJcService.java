package com.jc.service;

import java.math.BigDecimal;
import java.util.List;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.jc.model.JcField;
import com.jc.model.JcOption;
import com.jc.model.JcPlayType;
import com.jc.model.JcTeam;
import com.jc.model.Type;
import com.ram.model.User;

public interface IJcService extends IService {

	public List<Type> getTypeList(String type);
	
	public List<JcTeam> getTeamList();
	
	public JcTeam getTeamByTitle(String title);
	
	public String saveFields(JcField field, String playIds, String playTitles, String playStatus, String optionIds, String optionTitles, String optionPlays, String optionRates);
	
	public List<JcPlayType> getJcPlayTypesByFid(Integer fId, String status);
	
	public List<JcOption> getJcOptionsByPtId(Integer ptId);
	
	public void deletePlayTypeAndOption(Integer playId);
	
	public void deleteField(Integer fId);
	
	public List<JcField> getJcFieldByMid(Integer mid,String status);
	
	/**
	 * 保存用户竞猜投注 
	 */
	public User saveUserBetInfo(JcField field,BigDecimal money,JcOption option, User user);
	
	/**
	 * 计算赔率
	 */
	public void updateJingcaiOdds();
	
	/**
	 * 开奖 
	 */
	public String updateOPenResult(JcPlayType playType, JcOption option);
	
	/**
	 * 查询未开奖的玩法 
	 */
	public List<JcPlayType> getJcPlayNotOpen(Integer fId);
	
	
	/**
	 * 查询比赛下的所有投注项(开奖界面用)
	 * @return
	 */
	public PaginationSupport findJcOptionList(String hql, List<Object> para,int statIndex,int pageNum);
	
	/**
	 * 保存比赛  
	 */
	public String saveMatch(String param);
	
	/**
	 * 删除赛事，同时删除赛事下的比赛局，玩法，投注项
	 * @param mId
	 */
	public void deleteMatch(Integer mId);
	
	public String saveUpdateMatch(String param);
}
