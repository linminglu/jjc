package com.ram.web.user.action;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ram.model.DeskRegister;
import com.ram.model.DeskSoftware;
import com.ram.model.DeskSoftwareVersion;
import com.ram.model.User;
import com.ram.web.user.form.DeskRegisterForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;

public class DeskRegisterMAction extends BaseDispatchAction{

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		return mapping.findForward("init");
	}
	
	public ActionForward genSerialNumber(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		boolean action = ParamUtils.getBooleanParameter(request,"action",false);
		Integer len = ParamUtils.getIntegerParameter(request, "len");
		String softwareCode = ParamUtils.getParameter(request, "softwareCode");
		String sntype = ParamUtils.getParameter(request, "sntype","1");//默认注册码类型为销售
		if(!ParamUtils.chkInteger(len)) len = 10;
		if(action){
			String returns = userService.createDeskSerialNumber(len,softwareCode,sntype);
			log.fatal("####>"+returns);
		}
		
		return null;
	}
	
	
	
	public ActionForward genSNInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='0'"));
		return mapping.findForward("genSNInit");
	}
	
	public ActionForward genSNUInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='0' and ds.isshow='1' "));
		return mapping.findForward("genSNUInit");
	}
	
	//生成注册码
	public ActionForward genSN(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		User user = this.getUser(request);
		
		DeskRegisterForm frm = (DeskRegisterForm)form;
		Integer count = frm.getCount();
		String softwareCode = frm.getCode();
		String sntype = frm.getSntype();
		String sntaobao = frm.getSntaobao();
		String sellprice = frm.getSellprice();
		String status = frm.getStatus();
		String remarks = frm.getRemarks();
		
		if(ParamUtils.chkString(sntype)){
			User su = userService.getUserByActCode(sntaobao);
			Integer userId = user.getUserId();
			if(su!=null){
				userId = su.getUserId();
			}
			String flag = userService.createDeskSN(softwareCode, sntype, sntaobao, sellprice,status, remarks, count,userId);
		}
		
		
		ActionForward af = mapping.findForward("genSN");
		StringBuffer path = new StringBuffer();
		path.append(af.getPath());
		path.append("&sntype="+sntype);
		if(ParamUtils.chkString(sntaobao)) path.append("&sntaobao="+sntaobao);
		//path.append("&softwareCode="+softwareCode);
		//path.append("&status="+status);
		return new ActionForward(af.getName(),path.toString(),true);
	}
	
	//生成注册码单个用户版
	public ActionForward genSNU(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		User user = this.getUser(request);
		
		DeskRegisterForm frm = (DeskRegisterForm)form;
		Integer count = frm.getCount();
		String softwareCode = frm.getCode();
		String sntype = frm.getSntype();
		String sntaobao = frm.getSntaobao();
		String sellprice = frm.getSellprice();
		String status = frm.getStatus();
		String remarks = frm.getRemarks();
		
		if(ParamUtils.chkString(sntype)){
			String flag = userService.createDeskSN(softwareCode, sntype, sntaobao, sellprice,status, remarks, count,user.getUserId());
		}
		
		return mapping.findForward("genSNU");
	}
	
	public ActionForward svEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		Integer deskSoftwareVersionId = ParamUtils.getIntegerParameter(request, "deskSoftwareVersionId");
		
		DeskRegisterForm frm = (DeskRegisterForm)form;
		if(ParamUtils.chkInteger(deskSoftwareVersionId)){
			DeskSoftwareVersion dsv = userService.getDeskSoftwareVersion(deskSoftwareVersionId);
			frm.setDsv(dsv);
			request.setAttribute("dsv", dsv);
		}
		
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='0'"));
		return mapping.findForward("svEdit");
	}
	
	public ActionForward svSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		DeskRegisterForm frm = (DeskRegisterForm)form;
		
		DeskSoftwareVersion dsvFrm = frm.getDsv();
		Integer deskSoftwareVersionId = dsvFrm.getDeskSoftwareVersionId();
		
		DeskSoftwareVersion dsv = null;
		if(ParamUtils.chkInteger(deskSoftwareVersionId)){
			dsv = userService.getDeskSoftwareVersion(deskSoftwareVersionId);
		}
		if(dsv!=null){
			
		}else{
			dsv = new DeskSoftwareVersion();
		}
		dsv.setSoftwareCode(dsvFrm.getSoftwareCode());
		dsv.setSoftwareVersion(dsvFrm.getSoftwareVersion());
		dsv.setResourceList(dsvFrm.getResourceList());
		userService.saveDeskSoftwareVersion(dsv);
		
		return mapping.findForward("svSave");
	}
	
	public ActionForward svList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",100);
		
		DeskRegisterForm frm = (DeskRegisterForm)form;
		String code = ParamUtils.getParameter(request, "code","").trim();
		String sntype = ParamUtils.getParameter(request, "sntype","1");
		String sntaobao = ParamUtils.getParameter(request, "sntaobao","");
		String keyword = ParamUtils.getParameter(request, "keyword","").trim();
		String status = ParamUtils.getParameter(request, "status","");
		String sendTime = ParamUtils.getParameter(request, "sendTime","").trim();
		String sendTime2 = ParamUtils.getParameter(request, "sendTime2","").trim();
		String email = ParamUtils.getParameter(request, "email","").trim();
		String version = ParamUtils.getParameter(request, "version","");
		String orderbys = ParamUtils.getParameter(request, "orderbys","");
		
		request.setAttribute("code", code);
		request.setAttribute("version", version);
		
		frm.setCode(code);
		frm.setVersion(version);
		
		StringBuffer hsql = new StringBuffer();
		List<Object> pars = new ArrayList<Object>();
		//String select = "select new com.ram.model.DeskRegister(dr.deskRegisterId,dr.serialNumber,dr.softwareCode,dr.status,dr.sendTime,dr.remarks,dr.sellprice,dr.sntype,dr.regMac,dr.regVer,dr.regIp,dr.regTime,dr.isupdate,dr.launchCount,dr.checkCount,ds.title)";
		String select = "";
		String orderby = " order by dsv.softwareCode,dsv.deskSoftwareVersionId desc";
		hsql.append("from DeskSoftwareVersion dsv where 1=1");
		
		if(ParamUtils.chkString(code)){
			hsql.append(" and dsv.softwareCode=?");
			pars.add(code);
		}
		
		if(ParamUtils.chkString(version)){
			hsql.append(" and dsv.softwareVersion=?");
			pars.add(version);
		}
		
		PaginationSupport ps = userService.findObjectPage(select, hsql.toString(), orderby, pars, startIndex, pageSize);

		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='0'"));
		
		return mapping.findForward("svList");
	}
	
	public ActionForward snListm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='0'"));
		return mapping.findForward("snList");
	}
	
	public ActionForward snList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		DeskRegisterForm frm = (DeskRegisterForm)form;
		
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",100);
		String action = ParamUtils.getParameter(request, "action","");
		
		String code = ParamUtils.getParameter(request, "code","").trim();
		String sntype = ParamUtils.getParameter(request, "sntype","1");
		String sntaobao = ParamUtils.getParameter(request, "sntaobao","");
		String keyword = ParamUtils.getParameter(request, "keyword","").trim();
		String status = ParamUtils.getParameter(request, "status","");
		String sendTime = ParamUtils.getParameter(request, "sendTime","").trim();
		String sendTime2 = ParamUtils.getParameter(request, "sendTime2","").trim();
		String email = ParamUtils.getParameter(request, "email","").trim();
		String version = ParamUtils.getParameter(request, "version","");
		String orderbys = ParamUtils.getParameter(request, "orderbys","");
		String regTime = ParamUtils.getParameter(request, "regTime","0");
		
		request.setAttribute("code", code);
		request.setAttribute("keyword", keyword);
		request.setAttribute("sntype", sntype);
		request.setAttribute("sntaobao", sntaobao);
		request.setAttribute("status", status);
		request.setAttribute("sendTime", sendTime);
		request.setAttribute("sendTime2", sendTime2);
		request.setAttribute("email", email);
		request.setAttribute("version", version);
		request.setAttribute("orderbys", orderbys);
		request.setAttribute("regTime", regTime);
		
		frm.setCode(code);
		frm.setKeyword(keyword);
		frm.setSntype(sntype);
		frm.setEmail(email);
		frm.setOrderbys(orderbys);
		frm.setRegTime(regTime);
		
		StringBuffer hsql = new StringBuffer();
		StringBuffer hsql2 = new StringBuffer();
		String hsqlsell = "";
		List<Object> pars = new ArrayList<Object>();
		//String select = "select new com.ram.model.DeskRegister(dr.deskRegisterId,dr.serialNumber,dr.softwareCode,dr.status,dr.sendTime,dr.remarks,dr.sellprice,dr.sntype,dr.regMac,dr.regVer,dr.regIp,dr.regTime,dr.isupdate,dr.launchCount,dr.checkCount,ds.title)";
		String select = "";
		String orderby = " order by dr.deskRegisterId desc";
		hsql.append("from DeskRegister dr where 1=1");
		hsql2.append("from DeskRegister dr,DeskSoftware ds where dr.softwareCode=ds.softwareCode ");
		
		if(ParamUtils.chkString(sntype)){
			hsql.append(" and dr.sntype=?");
			hsql2.append(" and dr.sntype=?");
			pars.add(sntype);	
		}
		
		if(ParamUtils.chkString(sntaobao)){
			hsql.append(" and dr.sntaobao=?");
			hsql2.append(" and dr.sntaobao=?");
			pars.add(sntaobao);	
		}
		
		if(ParamUtils.chkString(code)){
			hsql.append(" and dr.softwareCode=?");
			hsql2.append(" and dr.softwareCode=?");
			pars.add(code);	
		}
		if(ParamUtils.chkString(keyword)){
			hsql.append("  and (upper(dr.remarks) like ? or upper(dr.regMac) like ? or upper(dr.serialNumber) like ? or upper(dr.qq) like ? or upper(dr.regLog) like ?)");
			hsql2.append(" and (upper(dr.remarks) like ? or upper(dr.regMac) like ? or upper(dr.serialNumber) like ? or upper(dr.qq) like ? or upper(dr.regLog) like ?)");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
		}
		if(ParamUtils.chkString(status)){
			hsql.append(" and dr.status=?");
			hsql2.append(" and dr.status=?");
			pars.add(status);
		}
		
		if(ParamUtils.chkString(sendTime)){
			if(regTime.equals("1")){
				hsql.append(" and dr.regTime>=?");
				hsql2.append(" and dr.regTime>=?");
			}else{
				hsql.append(" and dr.sendTime>=?");
				hsql2.append(" and dr.sendTime>=?");
			}
			pars.add(DateTimeUtil.parse(sendTime+" 00:00:00"));
		}
		if(ParamUtils.chkString(sendTime2)){
			if(regTime.equals("1")){
				hsql.append(" and dr.regTime<=?");
				hsql2.append(" and dr.regTime<=?");
			}else{
				hsql.append(" and dr.sendTime<=?");
				hsql2.append(" and dr.sendTime<=?");
			}
			pars.add(DateTimeUtil.parse(sendTime2+" 23:59:59"));
		}
		if(ParamUtils.chkString(email)){
			hsql.append(" and upper(dr.qq) like ?");
			hsql2.append(" and upper(dr.qq) like ?");
			pars.add("%"+email.toUpperCase()+"%");
		}
		if(ParamUtils.chkString(version)){
			hsql.append(" and dr.regVer=?");
			hsql2.append(" and dr.regVer=?");
			pars.add(version);
		}
		
		User suser = userService.getUserByActCode(sntaobao);
		String actCode = "";
		if(suser!=null) actCode = suser.getActCode();
		
		hsqlsell = hsql.toString();
		if(ParamUtils.chkString(actCode) && Integer.valueOf(actCode)>0){
			hsqlsell += " and dr.status >0";
		}else{
			hsqlsell += " and dr.status in(0,1,2)";
		}
		if(ParamUtils.chkString(orderbys)){
			if(orderbys.startsWith("and")){
				hsql.append(" "+orderbys);
				hsqlsell += " "+orderbys;
			}else{
				orderby = " "+orderbys;
			}
		}
		
		PaginationSupport ps = userService.findObjectPage(select, hsql.toString(), orderby, pars, startIndex, pageSize);

		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='0'"));
		if(sntype.equals("1")){
			//hsql.append(" and dr.status in(0,1,2)");
			request.setAttribute("sellCount", userService.getDeskRegisterSell("1"));
			request.setAttribute("agentQuerySellCount", userService.getDeskRegisterSellBySql(hsql.toString(),pars));
		}
		if(sntype.equals("2")){
			if(ParamUtils.chkString(actCode) && Integer.valueOf(actCode)>0){
				request.setAttribute("userSellCount", userService.getDeskRegisterSellOfUser("2", suser.getUserId()));
				request.setAttribute("userQuerySellCount", userService.getDeskRegisterSellBySql(hsqlsell,pars));
			}else{
				//request.setAttribute("agentSellCount", userService.getDeskRegisterSellJoin("2"));
				request.setAttribute("agentSellCount", userService.getDeskRegisterSell2("2"));
				//hsql.append(" and dr.status in(0,1,2)");
				//request.setAttribute("agentQuerySellCount", userService.getDeskRegisterSellBySqlJoin(hsql2.toString(),pars));
				request.setAttribute("agentQuerySellCount", userService.getDeskRegisterSellBySql(hsqlsell,pars));
			}
		}
		return mapping.findForward("snList");
	}
	
	public ActionForward uList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		DeskRegisterForm frm = (DeskRegisterForm)form;
		User user  = this.getUser(request);
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",100);
		String action = ParamUtils.getParameter(request, "action","");
		
		String code = ParamUtils.getParameter(request, "code","").trim();
		String snkey = ParamUtils.getParameter(request, "snkey","").trim();
		String status = ParamUtils.getParameter(request, "status","");
		String version = ParamUtils.getParameter(request, "version","");
		String orderbys = ParamUtils.getParameter(request, "orderbys","");
		String sendTime = ParamUtils.getParameter(request, "sendTime","").trim();
		String sendTime2 = ParamUtils.getParameter(request, "sendTime2","").trim();
		String keyword = ParamUtils.getParameter(request, "keyword","").trim();
		
		request.setAttribute("code", code);
		request.setAttribute("keyword", snkey);
		request.setAttribute("status", status);
		request.setAttribute("version", version);
		request.setAttribute("orderbys", orderbys);
		request.setAttribute("sendTime", sendTime);
		request.setAttribute("sendTime2", sendTime2);
		request.setAttribute("keyword", keyword);
		
		frm.setCode(code);
		frm.setSnkey(snkey);
		frm.setVersion(version);
		frm.setStatus(status);
		frm.setOrderbys(orderbys);
		frm.setKeyword(keyword);
		
		StringBuffer hsql = new StringBuffer();
		String hsqlsell = "";
		List<Object> pars = new ArrayList<Object>();
		//String select = "select new com.ram.model.DeskRegister(dr.deskRegisterId,dr.serialNumber,dr.softwareCode,dr.status,dr.sendTime,dr.remarks,dr.sellprice,dr.sntype,dr.regMac,dr.regVer,dr.regIp,dr.regTime,dr.isupdate,dr.launchCount,dr.checkCount,ds.title)";
		String select = "";
		String orderby = " order by dr.deskRegisterId desc";
		hsql.append("from DeskRegister dr where 1=1");
		if(ParamUtils.chkString(snkey) || ParamUtils.chkString(keyword)){
			hsql.append(" and dr.sntype in(1,2)");
			if(ParamUtils.chkString(snkey)){
				hsql.append("  and upper(dr.serialNumber)=? ");
				pars.add(snkey.toUpperCase().trim());
			}else if(ParamUtils.chkString(keyword)){
				hsql.append("  and (upper(dr.remarks) like ? or upper(dr.qq) like ?)");
				pars.add("%"+keyword.toUpperCase()+"%");
				pars.add("%"+keyword.toUpperCase()+"%");
			}
		}else{
			hsql.append(" and dr.sntype=2");
			hsql.append(" and dr.userId="+user.getUserId());
		}
		
		if(ParamUtils.chkString(code)){
			hsql.append(" and dr.softwareCode=?");
			pars.add(code);	
		}
		
		if(ParamUtils.chkString(status)){
			hsql.append(" and dr.status=?");
			pars.add(status);
		}
		
		if(ParamUtils.chkString(version)){
			hsql.append(" and dr.regVer=?");
			pars.add(version);
		}
		
		if(ParamUtils.chkString(sendTime)){
			hsql.append(" and dr.sendTime>=?");
			pars.add(DateTimeUtil.parse(sendTime+" 00:00:00"));
		}
		if(ParamUtils.chkString(sendTime2)){
			hsql.append(" and dr.sendTime<=?");
			pars.add(DateTimeUtil.parse(sendTime2+" 23:59:59"));
		}
		hsqlsell = hsql.toString();
		hsqlsell += " and dr.status>0";
		if(ParamUtils.chkString(orderbys)){
			if(orderbys.startsWith("and")){
				hsql.append(" "+orderbys);
				hsqlsell += " "+orderbys;
			}else{
				orderby = " "+orderbys;
			}
		}
		
		PaginationSupport ps = userService.findObjectPage(select, hsql.toString(), orderby, pars, startIndex, pageSize);

		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='0' and ds.isshow='1' "));
		
		request.setAttribute("agentSellCount", userService.getDeskRegisterSellOfUser("2", user.getUserId()));
		request.setAttribute("agentQuerySellCount", userService.getDeskRegisterSellBySql(hsqlsell,pars));
		
		return mapping.findForward("uList");
	}
	
	public ActionForward snLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		DeskRegisterForm frm = (DeskRegisterForm)form;
		
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",100);
		
		String code = ParamUtils.getParameter(request, "code","").trim();
		String sntype = ParamUtils.getParameter(request, "sntype","");
		String sntaobao = ParamUtils.getParameter(request, "sntaobao","");
		String keyword = ParamUtils.getParameter(request, "keyword","").trim();
		String status = ParamUtils.getParameter(request, "status","");
		String sendTime = ParamUtils.getParameter(request, "sendTime","").trim();
		String sendTime2 = ParamUtils.getParameter(request, "sendTime2","").trim();
		String email = ParamUtils.getParameter(request, "email","").trim();
		String version = ParamUtils.getParameter(request, "version","");
		String orderbys = ParamUtils.getParameter(request, "orderbys","");
		
		request.setAttribute("code", code);
		request.setAttribute("keyword", keyword);
		request.setAttribute("sntype", sntype);
		request.setAttribute("sntaobao", sntaobao);
		request.setAttribute("status", status);
		request.setAttribute("sendTime", sendTime);
		request.setAttribute("sendTime2", sendTime2);
		request.setAttribute("email", email);
		request.setAttribute("version", version);
		request.setAttribute("orderbys", orderbys);
		
		frm.setCode(code);
		frm.setKeyword(keyword);
		frm.setSntype(sntype);
		frm.setEmail(email);
		frm.setOrderbys(orderbys);
		frm.setStatus(status);
		
		StringBuffer hsql = new StringBuffer();
		StringBuffer hsql2 = new StringBuffer();
		List<Object> pars = new ArrayList<Object>();
		//String select = "select new com.ram.model.DeskRegister(dr.deskRegisterId,dr.serialNumber,dr.softwareCode,dr.status,dr.sendTime,dr.remarks,dr.sellprice,dr.sntype,dr.regMac,dr.regVer,dr.regIp,dr.regTime,dr.isupdate,dr.launchCount,dr.checkCount,ds.title)";
		String select = "";
		String orderby = " order by dr.deskRegisterId desc";
		hsql.append("from DeskRegister dr where 1=1");
		hsql2.append("from DeskRegister dr,DeskSoftware ds where dr.softwareCode=ds.softwareCode ");
		
		if(ParamUtils.chkString(sntype)){
			hsql.append(" and dr.sntype=?");
			hsql2.append(" and dr.sntype=?");
			pars.add(sntype);	
		}
		
		if(ParamUtils.chkString(sntaobao)){
			hsql.append(" and dr.sntaobao=?");
			hsql2.append(" and dr.sntaobao=?");
			pars.add(sntaobao);	
		}
		
		if(ParamUtils.chkString(code)){
			hsql.append(" and dr.softwareCode=?");
			hsql2.append(" and dr.softwareCode=?");
			pars.add(code);	
		}
		if(ParamUtils.chkString(keyword)){
			hsql.append("  and (upper(dr.remarks) like ? or upper(dr.regMac) like ? or upper(dr.serialNumber) like ? or upper(dr.qq) like ? or upper(dr.regLog) like ?)");
			hsql2.append(" and (upper(dr.remarks) like ? or upper(dr.regMac) like ? or upper(dr.serialNumber) like ? or upper(dr.qq) like ? or upper(dr.regLog) like ?)");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
		}
		if(ParamUtils.chkString(status)){
			hsql.append(" and dr.status=?");
			hsql2.append(" and dr.status=?");
			pars.add(status);
		}
		
		if(ParamUtils.chkString(sendTime)){
			hsql.append(" and dr.sendTime>=?");
			hsql2.append(" and dr.sendTime>=?");
			pars.add(DateTimeUtil.parse(sendTime+" 00:00:00"));
		}
		if(ParamUtils.chkString(sendTime2)){
			hsql.append(" and dr.sendTime<=?");
			hsql2.append(" and dr.sendTime<=?");
			pars.add(DateTimeUtil.parse(sendTime2+" 23:59:59"));
		}
		if(ParamUtils.chkString(email)){
			hsql.append(" and upper(dr.qq) like ?");
			hsql2.append(" and upper(dr.qq) like ?");
			pars.add("%"+email.toUpperCase()+"%");
		}
		if(ParamUtils.chkString(version)){
			hsql.append(" and dr.regVer=?");
			hsql2.append(" and dr.regVer=?");
			pars.add(version);
		}
		if(ParamUtils.chkString(orderbys)){
			if(orderbys.startsWith("and")){
				hsql.append(" "+orderbys);
			}else{
				orderby = " "+orderbys;
			}
		}
		
		PaginationSupport ps = userService.findObjectPage(select, hsql.toString(), orderby, pars, startIndex, pageSize);

		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='0'"));
		
		return mapping.findForward("snLog");
	}
	
	public ActionForward trialList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",100);
		
		String code = ParamUtils.getParameter(request, "code","").trim();
		String keyword = ParamUtils.getParameter(request, "keyword","").trim();
		String sendTime = ParamUtils.getParameter(request, "sendTime","").trim();
		String sendTime2 = ParamUtils.getParameter(request, "sendTime2","").trim();
		String status = ParamUtils.getParameter(request, "status","").trim();
		String orderbys = ParamUtils.getParameter(request, "orderbys", "");
		int count = ParamUtils.getIntParameter(request, "count",0);
		
		request.setAttribute("code", code);
		request.setAttribute("keyword", keyword);
		request.setAttribute("sendTime", sendTime);
		request.setAttribute("sendTime2", sendTime2);
		request.setAttribute("status", status);
		request.setAttribute("count", count+"");
		request.setAttribute("orderbys", orderbys);
		
		StringBuffer hsql = new StringBuffer();
		List<Object> pars = new ArrayList<Object>();
		String select = "";
		String orderby = " order by dt.deskTrialId desc";
		hsql.append("from DeskTrial dt where 1=1");
		if(ParamUtils.chkString(code)){
			hsql.append(" and dt.softwareCode=?");
			pars.add(code);	
		}
		if(ParamUtils.chkString(status)){
			hsql.append(" and dt.usestatus=?");
			pars.add(status);	
		}
		if(ParamUtils.chkString(keyword)){
			hsql.append(" and upper(dt.userMac) like ?");
			hsql.append(" or upper(dt.vers) like ?");
			hsql.append(" or dt.userIp like ?)");
			pars.add("%"+keyword.toUpperCase()+"%");	
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword+"%");
		}
		
		if(ParamUtils.chkString(sendTime)){
			hsql.append(" and dt.lastTime>=?");
			pars.add(DateTimeUtil.parse(sendTime+" 00:00:00"));
		}
		if(ParamUtils.chkString(sendTime2)){
			hsql.append(" and dt.lastTime<=?");
			pars.add(DateTimeUtil.parse(sendTime2+" 23:59:59"));
		}
		
		if(count==1){
			hsql.append(" and dt.launchCount=1");
		}else if(count>1){
			hsql.append(" and dt.launchCount>1");
		}
		
		if (ParamUtils.chkString(orderbys)){
			if (orderbys.startsWith("and")){
				hsql.append((new StringBuilder(" ")).append(orderbys).toString());
			}else{
				orderby = (new StringBuilder(" ")).append(orderbys).toString();
			}
		}
		
		PaginationSupport ps = userService.findObjectPage(select, hsql.toString(), orderby, pars, startIndex, pageSize);
		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='1'"));
		return mapping.findForward("trialList");
	}
	
	public ActionForward trialMobileList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",100);
		
		String code = ParamUtils.getParameter(request, "code","").trim();
		String keyword = ParamUtils.getParameter(request, "keyword","").trim();
		String sendTime = ParamUtils.getParameter(request, "sendTime","").trim();
		String sendTime2 = ParamUtils.getParameter(request, "sendTime2","").trim();
		String status = ParamUtils.getParameter(request, "status","").trim();
		String orderbys = ParamUtils.getParameter(request, "orderbys", "");
		int count = ParamUtils.getIntParameter(request, "count",0);
		
		request.setAttribute("code", code);
		request.setAttribute("keyword", keyword);
		request.setAttribute("sendTime", sendTime);
		request.setAttribute("sendTime2", sendTime2);
		request.setAttribute("status", status);
		request.setAttribute("count", count+"");
		request.setAttribute("orderbys", orderbys);
		
		StringBuffer hsql = new StringBuffer();
		List<Object> pars = new ArrayList<Object>();
		String select = "";
		String orderby = " order by dt.mobileTrialId desc";
		hsql.append("from MobileTrial dt where 1=1");
		if(ParamUtils.chkString(code)){
			hsql.append(" and dt.softwareCode=?");
			pars.add(code);	
		}
		if(ParamUtils.chkString(status)){
			hsql.append(" and dt.usestatus=?");
			pars.add(status);	
		}
		if(ParamUtils.chkString(keyword)){
			hsql.append(" and upper(dt.userMac) like ?");
			hsql.append(" or upper(dt.vers) like ?");
			hsql.append(" or dt.userIp like ?)");
			pars.add("%"+keyword.toUpperCase()+"%");	
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword+"%");
		}
		
		if(ParamUtils.chkString(sendTime)){
			hsql.append(" and dt.lastTime>=?");
			pars.add(DateTimeUtil.parse(sendTime+" 00:00:00"));
		}
		if(ParamUtils.chkString(sendTime2)){
			hsql.append(" and dt.lastTime<=?");
			pars.add(DateTimeUtil.parse(sendTime2+" 23:59:59"));
		}
		
		if(count==1){
			hsql.append(" and dt.launchCount=1");
		}else if(count>1){
			hsql.append(" and dt.launchCount>1");
		}
		
		if (ParamUtils.chkString(orderbys)){
			if (orderbys.startsWith("and")){
				hsql.append((new StringBuilder(" ")).append(orderbys).toString());
			}else{
				orderby = (new StringBuilder(" ")).append(orderbys).toString();
			}
		}
		
		PaginationSupport ps = userService.findObjectPage(select, hsql.toString(), orderby, pars, startIndex, pageSize);
		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		//request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='1'"));
		return mapping.findForward("trialMobileList");
	}
	
	public ActionForward dr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		DeskRegisterForm frm = (DeskRegisterForm)form;
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		DeskRegister dr = userService.getDeskRegister(sn);
		if(dr!=null){
			frm.setDr(dr);
			String code = dr.getSoftwareCode();
			if(ParamUtils.chkString(code)){
				request.setAttribute("ds",userService.getDeskSoftware(dr.getSoftwareCode()));
			}
			request.setAttribute("dr", dr);
			
		}
		return mapping.findForward("dr");
	}
	
	public ActionForward drSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		DeskRegisterForm frm = (DeskRegisterForm)form;
		
		DeskRegister drFrm = frm.getDr();
		String sn = drFrm.getSerialNumber();
		if(ParamUtils.chkString(sn)){
			DeskRegister dr = userService.getDeskRegister(sn);
			if(dr!=null){
				dr.setRegVer(drFrm.getRegVer());
				dr.setCheckadfree(drFrm.getCheckadfree());
				if(ParamUtils.chkString(drFrm.getSnflag())){
					dr.setSnflag(drFrm.getSnflag());
				}
				if(ParamUtils.chkString(drFrm.getBadmac())){
					dr.setBadmac(drFrm.getBadmac());
				}else{
					dr.setBadmac(null);
				}
				
				if(ParamUtils.chkIntegerAll(drFrm.getRegCount())){
					dr.setRegCount(drFrm.getRegCount());
				}
				if(ParamUtils.chkIntegerAll(drFrm.getRemacCount())){
					dr.setRemacCount(drFrm.getRemacCount());
				}
				if(ParamUtils.chkString(drFrm.getRemarks())){
					dr.setRemarks(drFrm.getRemarks());
				}
				userService.saveDeskRegister(dr);
			}
		}
		ActionForward af = mapping.findForward("drSave");
		StringBuffer path = new StringBuffer();
		path.append(af.getPath());
		path.append("&sn="+sn);
		return new ActionForward(af.getName(),path.toString(),true);
	}
	
	public void getDr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String outString = "";
		JSONObject json = new JSONObject();
		if(ParamUtils.chkString(sn)){
			DeskRegister dr = userService.getDeskRegister(sn);
			try {
				json.put("id", dr.getDeskRegisterId());
				json.put("sn", dr.getSerialNumber());
				json.put("code", (ParamUtils.chkString(dr.getSoftwareCode())?dr.getSoftwareCode():""));
				json.put("status", dr.getStatus());
				json.put("st",  DateTimeUtil.DateToStringAll(dr.getSendTime()));
				json.put("update", (ParamUtils.chkString(dr.getIsupdate())?dr.getIsupdate():""));
				json.put("mac", (ParamUtils.chkString(dr.getRegMac())?dr.getRegMac():""));
				json.put("price", (ParamUtils.chkInteger(dr.getSellprice())?(dr.getSellprice()+""):""));
				json.put("qq", (ParamUtils.chkString(dr.getQq())?dr.getQq():""));
				json.put("remarks", (ParamUtils.chkString(dr.getRemarks())?dr.getRemarks():""));
				json.put("regLog", (ParamUtils.chkString(dr.getRegLog())?dr.getRegLog():""));
				
				List<DeskSoftware> softwares = userService.findDeskSoftwareList(" and ds.istrial='0'");
				JSONArray ja = new JSONArray();
				for(DeskSoftware ds:softwares){
					JSONObject dsJo = new JSONObject();
					dsJo.put("id", ds.getDeskSoftwareId());
					dsJo.put("code", ds.getSoftwareCode());
					dsJo.put("title", ds.getTitle());
					dsJo.put("price", (ParamUtils.chkInteger(ds.getSellprice())?(ds.getSellprice())+"":""));
					ja.put(dsJo);
				}
				json.put("softwares", ja);
			} catch (Exception e) {
				e.printStackTrace();
			}
			outString = json.toString();
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.print(outString);
		out.flush();
		out.close();
	}
	
	public void checkout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String sns = ParamUtils.getParameter(request, "sns","").trim();//结算sn串
		String checkStatus = ParamUtils.getParameter(request, "checkStatus","0");//结算状态 0:No,1:Ok
		String checkTime = ParamUtils.getParameter(request, "checkTime","");//结账日期
		String outString = "";
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.print(outString);
		out.flush();
		out.close();
	}
	
	public void saveSNData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException,JSONException {
		User user = this.getUser(request);
		String sn = ParamUtils.getParameter(request,"sn","").trim();
		String code = ParamUtils.getParameter(request,"code","").trim();
		String price = ParamUtils.getParameter(request, "price","").trim();
		String editCode = ParamUtils.getParameter(request,"editCode","0");
		String status = ParamUtils.getParameter(request, "status","");
		String remarks = ParamUtils.getParameter(request, "remarks","");
		String isupdate = ParamUtils.getParameter(request, "update","");
		String mac = ParamUtils.getParameter(request, "mac","");
		String editMac = ParamUtils.getParameter(request, "editMac","0");
		String qq = ParamUtils.getParameter(request, "qq","");
		
		String sendTime = ParamUtils.getParameter(request, "sendTime","");
		String editSendTime = ParamUtils.getParameter(request, "editSendTime","0");
		
		//log.fatal(sn+","+status+","+remarks);
		JSONObject json = new JSONObject();
		
		Date now = null;
		DeskRegister dr = userService.getDeskRegister(sn);
		if(dr!=null){
			if(ParamUtils.chkString(status)){
				dr.setStatus(status);
			}
			//if(ParamUtils.chkString(remarks)){
				dr.setRemarks(remarks);
			//}
			if(ParamUtils.chkString(isupdate)){
				dr.setIsupdate(isupdate);
			}
			if(editMac.equals("1")){
				dr.setRegMac(mac);
			}
			if(editCode.equals("1")){
				dr.setSoftwareCode(code);
				if(ParamUtils.chkString(price)){
					dr.setSellprice(Integer.valueOf(price));
				}else{
					dr.setSellprice(null);
				}
			}
			if(status.equals("2")){//发号状态
				try {
					if(editSendTime.equals("1")){
						now = DateTimeUtil.parse(sendTime);
					}else{
						if(dr.getSendTime()==null){
							now = DateTimeUtil.getJavaUtilDateNow();
						}
					}
					if(now!=null){
						dr.setSendTime(now);
					}
				} catch (Exception e) {
					log.error("## sendTime:"+sendTime);
					//e.printStackTrace();
				}
				
			}
			//if(ParamUtils.chkString(qq)){
				dr.setQq(qq);
			//}
			String ip =request.getRemoteAddr();
			if(user.getUserId()==22325){
				userService.saveObject(dr,null);
			}else{
				userService.updateDeskRegisterLog(dr,"edit."+user.getLoginName()+"."+user.getUserId(),mac,dr.getRegVer(),ip,code);
			}
			try {
				json.put("rs", "1");
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		if(now !=null){
			try {
				json.put("st", DateTimeUtil.DateToStringAll(now));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.print(json.toString());
		out.flush();
		out.close();
	}
	
	
	public void saveTrialData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException,JSONException {
		Integer deskTrialId = ParamUtils.getIntegerParameter(request,"deskTrialId");
		String usestatus = ParamUtils.getParameter(request, "usestatus","0");
		String flag = "0";
		if(ParamUtils.chkInteger(deskTrialId) && ParamUtils.chkString(usestatus)){
			flag = userService.updateDeskTrialData(deskTrialId, usestatus);
		}
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.print(flag);
		out.flush();
		out.close();
	}
}