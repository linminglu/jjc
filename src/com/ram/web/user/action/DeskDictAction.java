package com.ram.web.user.action;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.ram.RamConstants;
import com.framework.util.FSO;
import com.framework.util.ManageFile;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class DeskDictAction extends BaseDispatchAction{

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		return mapping.findForward("init");
	}
	
	//检查参数
	public String checkParameter(String sn,String mac,String v){
		if(ParamUtils.chkString(sn) && ParamUtils.chkString(mac) && ParamUtils.chkString(v)){
			if(sn.length()!=RamConstants.DESK_SN_LENGTH){
				return RamConstants.DESK_SN_INVALID;
			}
			if(v.length()<3 || v.indexOf("v")<0){
				return RamConstants.DESK_SN_INVALID;
			}
			return "ok";
		}else{
			return RamConstants.DESK_PARAM_INCORRENT;
		}
	}
	
	//软件注册接口
	public void register(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String ip = request.getRemoteAddr();
		
		//log.fatal("##"+"register"+">code:"+code+",sn:"+sn+",mac:"+mac+",v:"+v+",ip:"+ip);
		
		//100:ok
		String returnData =  userService.updateDeskRegister(code,sn, mac, v, ip);
		
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	//上传单词正确率成绩
	public void upWords(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String words = ParamUtils.getParameter(request, "words","").trim();
		String ip = request.getRemoteAddr();
		
		log.fatal("##"+"upWord"+">code:"+code+",sn:"+sn+",mac:"+mac+",v:"+v+",ip:"+ip+",words:"+words);
		
		String returnData = "1";//ok
		
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public void upPoints(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String points = ParamUtils.getParameter(request, "points","").trim();
		String ip = request.getRemoteAddr();
		
		log.fatal("##"+"upPoints"+">code:"+code+",sn:"+sn+",mac:"+mac+",v:"+v+",ponits:"+points+",ip:"+ip);
		
		String returnData = "1";//ok
		
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public void launch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String ip = request.getRemoteAddr();
		
		log.fatal("##"+"launch"+">code:"+code+",sn:"+sn+",mac:"+mac+",v:"+v+",ip:"+ip);
		
		String returnData = userService.getReturnData("111", mac);
		
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public void check(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String ip = request.getRemoteAddr();
		
		log.fatal("##"+"check"+">code:"+code+",sn:"+sn+",mac:"+mac+",v:"+v+",ip:"+ip);
		
		String returnData = userService.getReturnData("111", mac);
		
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public ActionForward wordPercent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String word = ParamUtils.getParameter(request, "word","").trim();
		String ip = request.getRemoteAddr();
		
		log.fatal("##"+"wordPercent"+">code:"+code+",sn:"+sn+",mac:"+mac+",v:"+v+",ip:"+ip+",word:"+word);
		
		request.setAttribute("word", word);
		return mapping.findForward("wordPercent");
	}
}