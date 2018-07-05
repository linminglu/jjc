package com.ram.web.user.action;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import com.ram.web.user.form.DeskRegisterForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.FSO;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class DataBaseAction extends BaseDispatchAction{

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		return mapping.findForward("init");
	}
	
	
	public ActionForward bak(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		
		String filePath = "";
		String fileFolder = "";
		String fileName = "";
		String mysql = "";
		
		String outStr = "";
		
		try { 
	        Runtime rt = Runtime.getRuntime();
	        
	        //mysqldump -e -uroot -pbxs083210 --default-character-set=utf8 dbdeskregister
		    Process child = rt.exec(mysql);// 设置导出编码为utf8。这里必须是utf8在此要注意，有时会发生一个mysqldump: Got error: 1045的错误，此时mysqldump必须加上你要备份的数据库的IP地址，即mysqldump -h192.168.0.1 -uroot -pmysql dbname，今天我就遇到了这样的问题，呵呵	            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行 
		    InputStream in = child.getInputStream();// 控制台的输出信息作为输入流 
		    InputStreamReader xx = new InputStreamReader(in, "utf8");// 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码 
		    String inStr; 
		    StringBuffer sb = new StringBuffer(""); 
		    // 组合控制台输出信息字符串 
		    BufferedReader br = new BufferedReader(xx); 
		    while ((inStr = br.readLine()) != null) { 
		        sb.append(inStr + "\r\n"); 
		    } 
		    outStr = sb.toString(); 
		    in.close(); 
		    xx.close(); 
		    br.close(); 
		    
			new FSO().createFolder(filePath+fileFolder);
			
			OutputStreamWriter  w = new OutputStreamWriter(new FileOutputStream(filePath+fileFolder+fileName),"UTF-8");
			w.write(outStr);
			w.flush();
			w.close();
	    	
	        //Writer write = new FileWriter(filePath+fileFolder+fileName,false);
	        //write.write(outStr);
	        //write.flush();
	        //write.close();
	        
	        //System.out.println("write db file:<div><a href=\""+contextPath+fileFolder+fileName+"\">"+filePath+fileFolder+fileName+"</a></div>");
	        
		} catch (Exception e) { 
			 System.out.println(e);
		} 
		
		return mapping.findForward("bak");
	}
	
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		
		return mapping.findForward("download");
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
	
	
	
	public ActionForward snList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		DeskRegisterForm frm = (DeskRegisterForm)form;
		
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",100);
		
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
		
		StringBuffer hsql = new StringBuffer();
		List<Object> pars = new ArrayList<Object>();
		//String select = "select new com.ram.model.DeskRegister(dr.deskRegisterId,dr.serialNumber,dr.softwareCode,dr.status,dr.sendTime,dr.remarks,dr.sellprice,dr.sntype,dr.regMac,dr.regVer,dr.regIp,dr.regTime,dr.isupdate,dr.launchCount,dr.checkCount,ds.title)";
		String select = "";
		String orderby = " order by dr.deskRegisterId desc";
		hsql.append("from DeskRegister dr where 1=1");
		
		if(ParamUtils.chkString(sntype)){
			hsql.append(" and dr.sntype=?");
			pars.add(sntype);	
		}
		
		if(ParamUtils.chkString(sntaobao)){
			hsql.append(" and dr.sntaobao=?");
			pars.add(sntaobao);	
		}
		
		if(ParamUtils.chkString(code)){
			hsql.append(" and dr.softwareCode=?");
			pars.add(code);	
		}
		if(ParamUtils.chkString(keyword)){
			hsql.append(" and (upper(dr.remarks) like ? or upper(dr.regMac) like ? or upper(dr.serialNumber) like ?)");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
			pars.add("%"+keyword.toUpperCase()+"%");
		}
		if(ParamUtils.chkString(status)){
			hsql.append(" and dr.status=?");
			pars.add(status);
		}
		
		if(ParamUtils.chkString(sendTime)){
			hsql.append(" and dr.sendTime>=?");
			pars.add(DateTimeUtil.parse(sendTime+" 00:00:00"));
		}
		if(ParamUtils.chkString(sendTime2)){
			hsql.append(" and dr.sendTime<=?");
			pars.add(DateTimeUtil.parse(sendTime2+" 23:59:59"));
		}
		if(ParamUtils.chkString(email)){
			hsql.append(" and upper(dr.qq) like ?");
			pars.add("%"+email.toUpperCase()+"%");
		}
		if(ParamUtils.chkString(version)){
			hsql.append(" and dr.regVer=?");
			pars.add(version);
		}
		if(ParamUtils.chkString(orderbys)){
			orderby = " "+orderbys;
		}
		
		PaginationSupport ps = userService.findObjectPage(select, hsql.toString(), orderby, pars, startIndex, pageSize);

		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='0'"));
		if(sntype.equals("1")){
			request.setAttribute("sellCount", userService.getDeskRegisterSell("1"));
			request.setAttribute("agentQuerySellCount", userService.getDeskRegisterSellBySql(hsql.toString(),pars));
		}
		if(sntype.equals("2")){
			request.setAttribute("agentSellCount", userService.getDeskRegisterSell("2"));
			request.setAttribute("agentQuerySellCount", userService.getDeskRegisterSellBySql(hsql.toString(),pars));
		}
		return mapping.findForward("snList");
	}
	
	public ActionForward trialList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",100);
		
		String code = ParamUtils.getParameter(request, "code","").trim();
		String keyword = ParamUtils.getParameter(request, "keyword","").trim();
		String sendTime = ParamUtils.getParameter(request, "sendTime","").trim();
		String sendTime2 = ParamUtils.getParameter(request, "sendTime2","").trim();
		int count = ParamUtils.getIntParameter(request, "count",0);
		
		request.setAttribute("code", code);
		request.setAttribute("keyword", keyword);
		request.setAttribute("sendTime", sendTime);
		request.setAttribute("sendTime2", sendTime2);
		request.setAttribute("count", count+"");
		
		StringBuffer hsql = new StringBuffer();
		List<Object> pars = new ArrayList<Object>();
		String select = "";
		String orderby = " order by dt.deskTrialId desc";
		hsql.append("from DeskTrial dt where 1=1");
		if(ParamUtils.chkString(code)){
			hsql.append(" and dt.softwareCode=?");
			pars.add(code);	
		}
		if(ParamUtils.chkString(keyword)){
			hsql.append(" and upper(dt.userMac) like ?");
			pars.add("%"+keyword.toUpperCase()+"%");	
		}
		
		if(ParamUtils.chkString(sendTime)){
			hsql.append(" and dt.firstTime>=?");
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
		
		PaginationSupport ps = userService.findObjectPage(select, hsql.toString(), orderby, pars, startIndex, pageSize);
		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		request.setAttribute("codes", userService.findDeskSoftwareList(" and ds.istrial='1'"));
		return mapping.findForward("trialList");
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
	
	public void saveSNData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException,JSONException {
		
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
			if(ParamUtils.chkString(qq)){
				dr.setQq(qq);
			}
			userService.saveObject(dr,null);
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
	
}