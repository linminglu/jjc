package com.apps.service.impl;

import java.util.List;

import com.apps.Constants;
import com.apps.dao.IParamDAO;
import com.apps.eff.CacheUtil;
import com.apps.model.Param;
import com.apps.service.IParamService;
import com.apps.util.DeleteFolder;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.ParamUtils;

public class ParamServiceImpl extends BaseService implements
		IParamService {
	private IParamDAO paramDAO;

	public void setParamDAO(IParamDAO paramDAO) {
		this.paramDAO = paramDAO;
		super.dao = paramDAO;
	}

	public List<Param> findParamList() {
		return paramDAO.findParamList();
	}

	public List<Param> findParamList( String status) {
		return paramDAO.findParamList( status);
	}

	public PaginationSupport findList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return paramDAO.findList(hqls, para, startIndex, pageSize);
	}

	public void updateSort(Integer id, String flag) {
		paramDAO.updateSort(id, flag);
	}

	public Param saveParam(Param param) {
		Integer tid = param.getParamId();

		Param temp = null;
		if (ParamUtils.chkInteger(tid)) {
			temp = (Param) paramDAO.getObject(Param.class, tid);
			
			String title = param.getTitle();
			if(param.getType().equals(Constants.PARAM_RECHARGE_FAST)||param.getType().equals(Constants.PARAM_RECHARGE_CODE)){
				if(ParamUtils.chkString(title)){
					temp.setTitle(title);
				}
			}
//			temp.setType(param.getType());
//			temp.setReadonly("0");
			temp.setValue(param.getValue());
			paramDAO.saveObject(temp);
		} else {
			temp = new Param();
			temp.setValue(param.getValue());
//			temp.setSort("1");
//			temp.setStatus(Constants.PUB_STATUS_OPEN);
//			temp.setTitle(param.getTitle());
//			temp.setType(param.getType());
//			temp.setReadonly("0");
			param = (Param) paramDAO.saveObjectDB(temp);
		}
		CacheUtil.delParam();
		CacheUtil.getParam();
		return param;
	}

	public List<Param> findParamList( String status,
			String readonly) {
		return paramDAO.findParamList( status, readonly);
	}

	public void delParam(Param param) {
		paramDAO.deleteObject(Param.class, param.getParamId(), null);

	}

	public String getNamebyCode(Integer cid) {
		return paramDAO.getNamebyCode(cid);
	}

	@Override
	public String getValueByCode(String code) {
		return paramDAO.getValueByCode(code);
	}

	@Override
	public Param getParamByType(String defPay) {
		return paramDAO.getParamByType(defPay);
	}
	public List<Param> findParamList(String string, List<Object> para){
		return paramDAO.findParamList( string, para);
	}
}
