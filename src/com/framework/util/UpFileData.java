package com.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UpFileData extends HttpServlet{

	 /**   
	    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.   
	    * @param request servlet request   
	    * @param response servlet response   
	    */   
	   // 定义文件的上传路径   
	 
	   private String uploadPath = "D:/temp/upFileData/";   
	 
	// 限制文件的上传大小   
	 
	   private int maxPostSize = 100 * 1024 * 1024;   
	 
	   public UpFileData(){   
	       super();   
	   }   
	 
	   public void destroy() {   
	       super.destroy();   
	   }   
	 
	   protected void processRequest(HttpServletRequest request, HttpServletResponse response)   
	           throws ServletException, IOException {   
	       System.out.println("Access !");   
	       response.setContentType("text/html;charset=UTF-8");   
	       PrintWriter out = response.getWriter();   
	       String returnData = "0";
	       String sn = ParamUtils.getParameter(request, "sn");
	       
	       System.out.println("sn:"+sn);
	    try {
			
	       //保存文件到服务器中   
	       DiskFileItemFactory factory = new DiskFileItemFactory();   
	       factory.setSizeThreshold(4096);   
	       ServletFileUpload upload = new ServletFileUpload(factory);   
	       upload.setSizeMax(maxPostSize);   
	       try {   
	           List fileItems = upload.parseRequest(request);   
	           Iterator iter = fileItems.iterator();   
	           while (iter.hasNext()) {   
	               FileItem item = (FileItem) iter.next();   
	               if (!item.isFormField()) {   
	                   String name = item.getName();
	                   name = name.replace("\\", "/");
	                   if(name.indexOf("/")>=0){
	                	   String[] arr = name.split("/");
	                	   name = arr[arr.length-1];
	                   }
	                   System.out.println("####:"+name);
	                   try {   
	                       item.write(new File(uploadPath + name));   
	                      // SaveFile s = new SaveFile();   
	                      // s.saveFile(name);   
	                   } catch (Exception e) {   
	                       e.printStackTrace();   
	                   }   
	               }   
	           }
	           returnData = "1";
	       } catch (FileUploadException e) {   
	           //e.printStackTrace();   
	           System.out.println(e.getMessage() + "结束");   
	       }   
	       
	    } catch (Exception e) {
	    	System.out.println(e.getMessage() + "非法请求"); 
		}
	    
	    response.setContentType("text/html; charset=utf-8");
		PrintWriter pw  = response.getWriter();
		pw .println(returnData);
		pw .flush();
		pw .close();
		
	   }   
	 
	   // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">  
	 
	   /**   
	    * Handles the HTTP <code>GET</code> method.   
	    * @param request servlet request   
	    * @param response servlet response   
	    */   
	   protected void doGet(HttpServletRequest request, HttpServletResponse response)   
	           throws ServletException, IOException {   
	       processRequest(request, response);   
	   }   
	 
	   /**   
	    * Handles the HTTP <code>POST</code> method.   
	    * @param request servlet request   
	    * @param response servlet response   
	    */   
	   protected void doPost(HttpServletRequest request, HttpServletResponse response)   
	            throws ServletException, IOException {   
	        processRequest(request, response);   
	    }   
	  
	    /**   
	     * Returns a short description of the servlet.   
	     */   
	    public String getServletInfo() {   
	        return "Short description";   
	    }   
	    // </editor-fold>  
}
