package com.ram.service.permission.impl;

import java.util.List;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.ram.dao.permission.IFunctionDAO;
import com.ram.model.Function;
import com.ram.model.User;
import com.ram.service.permission.IFunctionService;

public class FunctionServiceImpl extends BaseService implements
		IFunctionService {

	private IFunctionDAO functionDAO;

	public FunctionServiceImpl() {
		
	}

	/**
	 * 用于spring将FunctionDAO的实例注入本类
	 * 
	 * @param functionDAO
	 */
	public void setFunctionDAO(IFunctionDAO functionDAO) {
		this.functionDAO = functionDAO;
	}

	/**
	 * 获取所有Function列表
	 * 
	 * @return
	 */
	public PaginationSupport findAllFunctions(int startIndex) {
		this.checking();
		List list = functionDAO.findAllFunctions(startIndex);
		int num = functionDAO.findAllFunctions_Num();
		PaginationSupport ps = new PaginationSupport(list, num, startIndex);
		return ps;

	}

	/**
	 * 分页查找当前功能的子功能的列表
	 * 
	 * @param curFunctionID
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findALLSubFunctionsPage(int curFunctionId,
			int startIndex) {
		List list = functionDAO.findSubFunctionByCurrentFunction(curFunctionId,
				startIndex);
		int num = functionDAO
				.findSubFunctionByCurrentFunction_Num(curFunctionId);
		PaginationSupport ps = new PaginationSupport(list, num, startIndex);

		return ps;
	}

	/**
	 * 分页查询顶层功能列表
	 * 
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findAllTopFunctionsPage(int startIndex) {
		int curFunctionID = 0;
		return this.findALLSubFunctionsPage(curFunctionID, startIndex);
	}

	/**
	 * 保存Function对象
	 * 
	 * @param function
	 * @param user
	 */
	public void saveFunction(Function function,User user) {
		String functionActionPath=function.getFunctionLinkAddress();
		//将地址前面的../../变成/
		if(functionActionPath!=null && functionActionPath.length()>0){
			functionActionPath=this.cutFunctionActionURL(functionActionPath);
		}else{
			functionActionPath="";
		}
		function.setFunctionActionPath(functionActionPath);
		functionDAO.saveFunction(function,user);
	}

	
	/**
	 * 剪切处理访问功能的Action的URL
	 * @param functionActionPath
	 * @return
	 */
	  private String cutFunctionActionURL(String functionActionPath){
		    String tmp=functionActionPath;
		    if(tmp.indexOf("&")>0){
		    	tmp=tmp.substring(0,tmp.indexOf("&"));
		    }
		 
		    if(tmp!=null && tmp.trim().length()>0){
		      if(tmp.indexOf(".do")>0){
		        String tt = tmp.substring(functionActionPath.indexOf(".do")+3);
		        tmp = tmp.substring(0, functionActionPath.indexOf(".do"));
		        tmp = tmp.replace('.', '/');
		        tmp = tmp.replaceAll("///", "/");
		        tmp = tmp.replaceAll("//", "/");
		        tmp = tmp + ".do";
		        tmp = tmp + tt;
		      }else if(tmp.indexOf(".jsp")>0){
		        String tt = tmp.substring(functionActionPath.indexOf(".jsp")+4);
		        tmp = tmp.substring(0, functionActionPath.indexOf(".jsp"));
		        tmp = tmp.replace('.', '/');
		        tmp = tmp.replaceAll("///", "/");
		        tmp = tmp.replaceAll("//", "/");
		        tmp = tmp + ".jsp";
		        tmp = tmp + tt;
		      }else{
		        tmp="";
		      }
		    }else{
		      tmp="";
		    }
		  
		    return tmp;
		  }
	  
	/**
	 * 获取Function对象
	 * 
	 * @param id
	 * @return
	 */
	public Function getFunction(Integer id) {
		return functionDAO.getFunction(id);
	}

	/**
	 * 设置某个Function为无效状态
	 * 
	 * @param function
	 */
	public void removeFunction(Function function,User user) {
		//获得当前删除功能的父功能id
		Integer functionParentId=function.getParentId();
		//将当前功能删除
		functionDAO.removeFunction(function.getFunctionId(),user);
		//判断当前功能的父功能下是否还有子功能
		int subFunctions=functionDAO.findSubFunctionByCurrentFunction_Num(functionParentId.intValue());
		if(subFunctions==0){
			//如果该父功能下不再有子功能，则设置该父功能的havesubfunction字段为0
			Function functionParent=(Function)functionDAO.getFunction(functionParentId);
			functionParent.setSubFunctionNumber(new Integer(0));
			//并保存到数据库中
			functionDAO.saveFunction(functionParent,user);
		}
		
	}

	public List findAllSubFunctions(int parentFunctionId) {
		return functionDAO.findSubFunctions(parentFunctionId);
	}

	/**
	 * 找到所有第一级的功能
	 */
	public List findAllTopFunctions() {
		return functionDAO.findAllParentIdIsZero();
	}

	public List findAllSubFunctionsNotBelongGroup(int parentFunctionId, int groupId) {
		return functionDAO.findAllSubFunctionsNotBelongGroup(parentFunctionId,groupId);
	}
	public List findAllSubFunctionsBelongGroup(int parentFunctionId, int groupId) {
		return functionDAO.findAllSubFunctionsBelongGroup(parentFunctionId,groupId);
	}

	public void saveOrderDown(int functionId,User user) {
		Function f=this.getFunction(new Integer(functionId));
		if(f!=null){
			if(f.getOrderSn()==null)f.setOrderSn(new Integer(0));
			f.setOrderSn(new Integer(f.getOrderSn().intValue() - 1));
			functionDAO.saveFunction(f,user);
		}else{
			log.error("调整功能顺序的时候出错：功能ID=" + functionId + "的功能不存在");
		}
	}

	public void saveOrderUp(int functionId,User user) {
		Function f=this.getFunction(new Integer(functionId));
		log.info("-------f.title=" + f.getFunctionTitle());
		log.info("-------f.ordersn=" + f.getOrderSn().intValue());
		if(f!=null){
			if(f.getOrderSn()==null)f.setOrderSn(new Integer(0));
			f.setOrderSn(new Integer(f.getOrderSn().intValue() + 1));
			functionDAO.saveFunction(f,user);
		}else{
			log.error("调整功能顺序的时候出错：功能ID=" + functionId + "的功能不存在");
		}
	}
	
	
}