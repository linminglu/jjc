package com.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class FSO {

  public FSO() {
  }
  /**
  * 拷贝一个文件到另一个目录
  */
    public boolean copyFile(String from,String to){

      File fromFile,toFile;
      fromFile = new File(from);
      toFile = new File(to);
      FileInputStream fis = null;
      FileOutputStream fos = null;

      try{
        toFile.createNewFile();
        fis = new FileInputStream(fromFile);
        fos = new FileOutputStream(toFile);
        int bytesRead;
        byte[] buf = new byte[4 * 1024];  // 4K buffer
        while((bytesRead=fis.read(buf))!=-1){
          fos.write(buf,0,bytesRead);
        }
        fos.flush();
        fos.close();
        fis.close();
      }catch(IOException e){
//        System.out.println(e);
        return false;
      }
      return true;

    } 
  public void createFolder(String folder) throws Exception {
    boolean success = true;
    File fileDirectory = new File(folder);
    if (!fileDirectory.exists()) {
      success = fileDirectory.mkdirs();
    }
    fileDirectory = null;
    if (!success) {
      Exception exception = new Exception(String.valueOf(String.valueOf( (new
          StringBuffer("文件目录：")).append(folder).append("没有创建成功"))));
      throw exception;
    }
    else {
      return;
    }
  }
  
  public boolean isFileExists(String srcFileName) {
		File file = new File(srcFileName);
		////System.out.println(file.isDirectory()+","+file.isFile()+","+file.exists());
		if(file.exists() && file.isFile()) return true;
		return false;
	}

  public void deleteFolder(String folder) throws Exception {
    boolean success = true;
    File fileDirectory = new File(folder);
    if (fileDirectory.exists()) {
      fileDirectory.delete();
    }
    if (!success) {
      Exception exception = new Exception(String.valueOf(String.valueOf( (new
          StringBuffer("文件目录：")).append(folder).append("没有删除成功"))));
      throw exception;
    }
    else {
      return;
    }
  }

  public void moveFile(String srcFileName, String desFolder) throws Exception {
    boolean success = true;
    File srcFile = new File(srcFileName);
    createFolder(desFolder);
    String fileName = srcFile.getName();
    String desFileName = String.valueOf(String.valueOf( (new StringBuffer(
        String.valueOf(String.valueOf(desFolder)))).append(File.separatorChar).
        append(fileName)));
    File desFile = new File(desFileName);
    srcFile.renameTo(desFile);
    if (!success) {
      Exception exception = new Exception(String.valueOf(String.valueOf( (new
          StringBuffer("文件：")).append(srcFile).append("没有拷贝成功"))));
      throw exception;
    }
    else {
      return;
    }
  }

  public void deleteFile(String srcFileName) throws Exception {
    boolean success = true;
    File file = new File(srcFileName);
    if (file.exists()) {
      file.delete();
    }
    if (!success) {
      Exception exception = new Exception(String.valueOf(String.valueOf( (new
          StringBuffer("文件：")).append(srcFileName).append("没有删除成功"))));
      throw exception;
    }
    else {
      return;
    }
  }
}
