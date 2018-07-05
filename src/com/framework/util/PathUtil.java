package com.framework.util;

import java.io.File;
import java.net.URL;

import com.framework.exception.business.NoExistFileException;

public class PathUtil {
	private static String servletClassesPath = null;
	private static PathUtil instance = new PathUtil();
	 
  public static void main(String args[]){
   try{
   }catch(Exception e){
   }
  }

  /**
   *
   * @param path String 锟斤拷锟铰凤拷锟?
   * @return String 锟斤拷锟铰凤拷锟?
   */
  public static String getRealPath(String path) throws NoExistFileException{
    PathUtil p = new PathUtil();
    URL url = null;
    String result = null;
    try{
      if(path != null || !("").equals(path) || !("DEFALUT").equals(path)){
        url = p.getClass().getClassLoader().getResource(path); //com.ycc.properties.
     
        result = url.getFile();
       // result = result.substring(1,result.length());
      }else{
        result = path;
      }
    }
    catch(Exception e){
      e.printStackTrace();
      throw new NoExistFileException("锟侥硷拷锟斤拷锟斤拷锟斤拷,锟斤拷锟斤拷路锟斤拷锟斤拷");
    }
    return result;
  }
  /**
   * add by lgj
   * gain the servletClass path
   * @param path
   */
  public static void setServletClassesPath(String path) {
    //log.debug("FileUtil.setServletClassesPath called with path = " + path);

    servletClassesPath = path;
    if (servletClassesPath.endsWith(File.separator) == false) {
        servletClassesPath = servletClassesPath + File.separatorChar;
        LogUtil.getLogger().debug("PathUtil.setServletClassesPath change path to value = " + servletClassesPath);
    }
}
  /**
   * add by lgj
   * @return
   */
  public static String getServletClassesPath() {
    if (servletClassesPath == null) {        
            ClassLoader classLoader = instance.getClass().getClassLoader();
            URL url = classLoader.getResource("/");
            servletClassesPath = url.getPath();       
            LogUtil.getLogger().debug("servletClassesPath = " + servletClassesPath);
        if (servletClassesPath.endsWith(File.separator) == false) {
            servletClassesPath = servletClassesPath + File.separatorChar;
            LogUtil.getLogger().debug("servletClassesPath does not end with /: " + servletClassesPath);
        }
    }
   
    return servletClassesPath;
}
}
