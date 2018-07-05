package com.framework.common.download;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.*;
import com.framework.web.action.BaseDispatchAction;



public class DownloadUtil extends BaseDispatchAction{
	
	public DownloadUtil() {

	  }
	    public static byte[] getBytes(InputStream inputStream) throws IOException {
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
	        byte[] block = new byte[512];
	        while (true) {
	            int readLength = inputStream.read(block);
	            if (readLength == -1) break;// end of file
	            byteArrayOutputStream.write(block, 0, readLength);
	        }
	        byte[] retValue = byteArrayOutputStream.toByteArray();
	        byteArrayOutputStream.close();
	        return retValue;
	    }

	   public void downloadAttachment(HttpServletRequest request, HttpServletResponse response,String path)
	         throws Exception{
	         InputStream inputStream = null;
	         OutputStream outputStream = null;
	       
	         try {
                 
	             String attachpath=path;

                  //获得传进的参数的完整路径，request.getRealPath获得当前目录的路径。
	             
	             String attachFilename=request.getRealPath("\\")+attachpath.replace('/','\\');
	            
	             log.info("================="+attachFilename);
	             /** 
                   * 定义一个数组，（路径名是以"/"为分割符号）将"/"分割路径下的参数定义为数组：
                   * file_upload/resource/2006-02-13/34.txt
	               */ 
	               String fileHeaders[]=attachpath.split("/");
	             /** 
                  * 取数组中最后的一个34txt,作为fileHeader的名字
                  * file_upload/resource/2006-02-13/34.txt
	               */ 
	             String fileHeader=fileHeaders[fileHeaders.length-1];
	            	 log.info("================="+fileHeader);
	            	 
//	            try {
	                 inputStream = new FileInputStream(attachFilename);

//	             } catch (Exception ex) {
//	             
//	            throw new Exception("不能打开下载的文件");
//	                 
//	                
//	             }
	             

	             byte[]buffer = getBytes(inputStream);
	             inputStream.close();
	             inputStream = null;// no close twice
	             //设置response的属性，以使浏览器知道这次是下载文件，而不是网页
	             outputStream = response.getOutputStream();
	             response.setContentType("application/octet-stream");//jpg
	             response.setHeader("Location", fileHeader);
	             //added by Dejan
	             response.setHeader("Content-Disposition", "attachment; filename="+fileHeader+"");
	             outputStream.write(buffer);
	             outputStream.flush();
	             outputStream.close();
	             outputStream = null;// no close twice
	         } catch (Exception ex) {
	        	
	         throw ex;
	           
	         } finally {
	             if (inputStream != null) {
	                 try {
	                     inputStream.close();
	                 } catch (IOException ex) { }
	             }
	             if (outputStream != null) {
	                 try {
	                     outputStream.close();
	                 } catch (IOException ex) { }
	             }
	         }
	     }
	  }
