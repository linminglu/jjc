package com.apps.service.impl;

import help.base.APIConstants;

import java.util.List;

import com.apps.Constants;
import com.apps.dao.ITypeDAO;
import com.apps.model.SellerTypeRl;
import com.apps.model.Type;
import com.apps.model.TypeCate;
import com.apps.service.ITypeService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;

public class TypeServiceImpl extends BaseService implements ITypeService {
	private ITypeDAO typeDAO;

	public void setTypeDAO(ITypeDAO typeDAO) {
		this.typeDAO = typeDAO;
		super.dao = typeDAO;
	}

	public List<Type> findTypeList() {
		return typeDAO.findTypeList();
	}

	public PaginationSupport findList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return typeDAO.findList(hqls, para, startIndex, pageSize);
	}
	
	public PaginationSupport findTypeList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		PaginationSupport ps = typeDAO.findTypeList(hqls, para, startIndex, pageSize);
		List<Type> list = ps.getItems();
		//Type type = null;
		if(list != null && list.size() > 0){
			for (Type type : list) {
				Integer parentId = type.getParentId();
				if(ParamUtils.chkInteger(parentId)){
					Type type2 = (Type)typeDAO.getObject(Type.class, parentId);
					type.setPtitle(type2.getTitle());
				}
			}
			/*for (int i = 0; i < list.size(); i++) {
				type = (Type)list.get(i);
				Integer parentId = type.getParentId();
				Type type2 = (Type)typeDAO.getObject(Type.class, parentId);
				type.setPtitle(type2.getTitle());
			}*/
		}
		return ps;
	}

	public void updateSort(Integer id, String flag) {
		typeDAO.updateSort(id, flag);
	}

	public Type saveType(Type type) {
		Integer tid = type.getTid();

		Type temp = null;
		if (ParamUtils.chkInteger(tid)) {
			temp = (Type) typeDAO.getObject(Type.class, tid);
			temp.setType(type.getType());
			temp.setPosition(type.getPosition());
			temp.setImg(type.getImg());
			temp.setTitle(type.getTitle());
			if(ParamUtils.chkString(type.getIsShow())) temp.setIsShow(type.getIsShow());
			if(ParamUtils.chkString(type.getTemplate())) temp.setTemplate(type.getTemplate());
			typeDAO.saveObject(temp);
		} else {
			temp = new Type();
			temp.setType(type.getType());
			temp.setPosition(type.getPosition());
			temp.setImg(type.getImg());
			temp.setSort("1");
			temp.setIsMore("0");
			if(ParamUtils.chkInteger(type.getParentId())){
				temp.setParentId(type.getParentId());
			}else{
				temp.setParentId(0);
			}
			//temp.setType(Constants.MODULE_GROUP);
			temp.setStatus(Constants.PUB_STATUS_OPEN);
			temp.setTitle(type.getTitle());
			if(ParamUtils.chkString(type.getIsShow())) temp.setIsShow(type.getIsShow());
			if(ParamUtils.chkString(type.getTemplate())) temp.setTemplate(type.getTemplate());
			type = (Type) typeDAO.saveObjectDB(temp);
		}
		return type;
	}

	@Override
	public List<Type> findTypeList(String type) {
		return typeDAO.findTypeList(type);
	}
	public SellerTypeRl findSellerTypeRlBySid(Integer sid){
		return typeDAO.findSellerTypeRlBySid(sid);
	}
	public List<Type> findGRTypeList(){
		return typeDAO.findGRTypeList();
	}
	@Override
	public Type getTypeBySid(String sid) {
		return typeDAO.getTypeBySid(sid);
	}

	@Override
	public List<Type> findTypeList(Integer tid) {
		return typeDAO.findTypeList(tid);
	}
	
	public List<TypeCate> findTypeCate(){
		return typeDAO.findTypeCate();
	}
	
	public String delType(Integer tid) {
		HQUtils hq = null;
		Integer count = 0;
		
		//1.检查是否有子栏目
		hq = new HQUtils("from Type t where t.parentId=?");
		hq.addPars(tid);
		count = typeDAO.countObjects(hq);
		
		//2.检查是否有商家关联
		if(count==0){
			hq = new HQUtils("from SellerTypeRl rl where rl.tid=?");
			hq.addPars(tid);
			count = typeDAO.countObjects(hq);
		}
		
		if(count==0){
			typeDAO.deleteObject(Type.class, tid, null);
			return APIConstants.CODE_REQUEST_SUCCESS;
		}
		return APIConstants.CODE_REQUEST_ERROR;
	}

}
