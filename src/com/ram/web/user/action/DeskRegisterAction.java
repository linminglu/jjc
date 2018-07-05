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
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class DeskRegisterAction extends BaseDispatchAction{

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		String referer = request.getHeader("Referer");
		String reqURL = request.getRequestURL().toString();
		log.fatal("============Referer=" + referer);//页面访问来源
		log.fatal("============reqURL=" + reqURL);//请求页面地址

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
		
		String returnData =  userService.updateDeskRegister(code,sn, mac, v, ip);
		
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public void getAutoSNBak(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = ParamUtils.getParameter(request, "softwareCode");
		String sn = ParamUtils.getParameter(request, "sn", "").trim();
		String mac = ParamUtils.getParameter(request, "mac", "").trim();
		String v = ParamUtils.getParameter(request, "v", "").trim();
		String ip = request.getRemoteAddr();
		String sntype = "5";
		String status = "0";
		String returnData = userService.updateAndGetAutoSN(code, sn, sntype,
				status, mac, v, ip);
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}

	public void getFreeSNBak(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = ParamUtils.getParameter(request, "softwareCode");
		String sn = ParamUtils.getParameter(request, "sn", "").trim();
		String mac = ParamUtils.getParameter(request, "mac", "").trim();
		String v = ParamUtils.getParameter(request, "v", "").trim();
		String ip = request.getRemoteAddr();
		String sntype = "5";
		String status = "0";
		String returnData = userService.updateAndGetFreeSN(code, sn, sntype,
				status, mac, v, ip);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	//软件注册接口2
	public void newRegister(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String ip = request.getRemoteAddr();
		
		String returnData =  userService.updateDeskNewRegister(code,sn, mac, v, ip);
		
		//response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control ","no-cache"); 
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public void verify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String ip = request.getRemoteAddr();
		
		String returnData = userService.updateAndCheckDeskVerify(code,sn, mac, v, ip);
		
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=utf-8");
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
		
		String returnData = userService.updateAndCheckDeskLaunch(code,sn, mac, v, ip);
		
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public void trial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","SN0000000000").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String ip = request.getRemoteAddr();
		
		String returnData = userService.updateAndCheckDeskTrialVersion(code,sn, mac, v, ip);
		
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	//定时检查，用于软件运行过程中，间隔45分钟
	public void check(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String ip = request.getRemoteAddr();
		
		String returnData = userService.updateAndCheckDeskRegister(code,sn, mac, v, ip);
		
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
//	//移动设备launch
//	public void mobileLaunch(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
//		String code = ParamUtils.getParameter(request,"code");
//		String sn = ParamUtils.getParameter(request, "sn","").trim();
//		String mac = ParamUtils.getParameter(request, "mac","").trim();
//		String v = ParamUtils.getParameter(request, "v","").trim();
//		String ip = request.getRemoteAddr();
//		
//		String returnData = userService.updateMobileLaunch(code,sn, mac, v, ip);
//		
//		//response.setCharacterEncoding("UTF-8");
//		response.setContentType("text/html; charset=utf-8");
//		PrintWriter out  = response.getWriter();
//		out.println(returnData);
//		out.flush();
//		out.close();
//	}
	
	
	//软件注册接口
	public ActionForward scoreQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String pid = ParamUtils.getParameter(request, "pid","").trim();
		String ip = request.getRemoteAddr();
		
		System.out.print("scoreQuery>code="+code+",sn="+sn+",pid="+pid+",v="+v+",mac="+mac);
		
		return mapping.findForward("scoreQuery");
	}
	
//	public void upFileData(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
//	}
	
	public String upFile(HttpServletRequest request,String filePath, FormFile file,String fileName) {
		log.info("###uploaded ok:"+filePath);
		String returnStr = null;
		FSO fso = new FSO();
		try {
			fso.createFolder(filePath);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		OutputStream os = null;
		InputStream is = null;
		try {
			// if (!(new File(dir).isDirectory())) {
			// new File(dir).mkdir();
			// }
			is = file.getInputStream();
			String tempFileName = file.getFileName();
			if (fileName != null && fileName.length() > 0) {
				tempFileName = fileName;
			}
			os = new FileOutputStream(filePath + tempFileName);// 建立一个上传文件的输出
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);// 将文件写入服务器
			}
			returnStr = tempFileName;
			log.info("###uploaded ok:"+tempFileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return returnStr;
	}
}