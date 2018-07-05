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

public class DeskGenTestAction extends BaseDispatchAction{

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
	
	public void loginCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String un = ParamUtils.getParameter(request,"un");
		String pwd = ParamUtils.getParameter(request, "pwd");
		String ip = request.getRemoteAddr();
		
		String returnData =  "用户名或密码不正确！";//1学生2老师
		if(ParamUtils.chkString(un) && ParamUtils.chkString(pwd)){
			returnData = "1";
			if(un.equals("tutor") && pwd.equals("1")){
				returnData = "2";
			}
		}
		
		log.info("## loginCheck>un:"+un+",pwd="+pwd+",ip="+ip+",return:"+returnData);
		
//		StringBuffer sb = new StringBuffer();
//		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//		sb.append("<root>");
//		sb.append("<status>"+returnData+"</status>");
//		sb.append("</root>");
		
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	
	public void getList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String un = ParamUtils.getParameter(request,"un");
		
		String returnData =  userService.getGenTestPaperList();
		
		log.info("## getList>un:"+un);
				
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public void getXml(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String un = ParamUtils.getParameter(request,"un");
		String pid = ParamUtils.getParameter(request, "pid");
		
		String returnData =  userService.getGenTestPaperXml(pid);
		
		log.info("## getList>un:"+un+",pid="+pid);
				
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public void upAnswer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String un = ParamUtils.getParameter(request,"un");
		String pid = ParamUtils.getParameter(request, "pid");
		String score = ParamUtils.getParameter(request, "score");
		String answerData = ParamUtils.getParameter(request, "answerData");
		
		String returnData =  "0";
		
		if(ParamUtils.chkString(un) && ParamUtils.chkString(pid)){
			returnData = "1";
		}
		log.info("## upAnswer>un:"+un+",pid="+pid+",score="+score+",answerData="+answerData);
		
//		StringBuffer sb = new StringBuffer();
//		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//		sb.append("<root>");
//		sb.append("<status>"+returnData+"</status>");
//		sb.append("</root>");
				
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
//	
}