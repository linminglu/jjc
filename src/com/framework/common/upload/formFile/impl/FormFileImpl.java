package com.framework.common.upload.formFile.impl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.struts.upload.FormFile;

import com.framework.common.upload.formFile.IFormFile;

/**
 * @author wangyuanjun
 * createTime 2006-2-21
 * commpany bpc
 * project newplat
 */
public class FormFileImpl implements IFormFile{
	 /**
     * 文件信息
     */
    private FormFile file_info;


    /**
     * 构造器，得到文件信息
     * @param formfile　表单上传文件
     */
    public   FormFileImpl(FormFile formfile){
      if (formfile == null
                ||formfile.getFileName().equals(null)
                ||formfile.getFileSize()==0) {
           throw new java.lang.IllegalArgumentException("Fileinfo can not NULL or has not Value.");
        }

        file_info = formfile;
    }
    /**
     * 校验文件后缀名
     * @param file_ext 文件扩展名数组，如.jpg, .rar, .zip。
     * @return　true：是该扩展名数组中存在的后缀名　false：不是该扩展名数组中存在的后缀名
     */
    public boolean validateFileName(String[] file_ext){
        if (file_info == null
                ||file_info.getFileName().equals(null)
                ||file_info.getFileSize()==0) {
            return false;
        }
        if (file_ext == null||file_ext.length==0){
            return false;
        }

        String file_name = file_info.getFileName();

        int s_id = file_name.lastIndexOf(".");

        if (s_id == -1) {
            return false;
        }

        String ext = file_name.substring(s_id);
       



        int size = file_ext.length;

        for(int i=0;i<size;i++){
          if(ext.toLowerCase().lastIndexOf(".jsp") != -1
             || ext.toLowerCase().lastIndexOf(".js") != -1
             || ext.toLowerCase().lastIndexOf(".jspx") != -1){
            return false;
          }

          if(ext.toLowerCase().equals(file_ext[i].toLowerCase())){
            return true;
          }
        }

        return false;
    }

    /**
     * 校验aformfile对象的后缀名，
     * @param aformfile　实现FormFile接口的对象　
     * @param file_ext　文件扩展名数组，如.jpg, .rar, .zip。
     * @return　true：是该扩展名数组中存在的后缀名　false：不是该扩展名数组中存在的后缀名
     */
    public  boolean validateFileName(FormFile aformfile,String[] file_ext){
        if (aformfile == null
                ||aformfile.getFileName().equals(null)
                ||aformfile.getFileSize()==0) {
            return false;
        }
        if (file_ext == null||file_ext.length==0){
            return false;
        }

        String file_name = aformfile.getFileName();

        int s_id = file_name.lastIndexOf(".");

        if (s_id == -1) {
            return false;
        }

        String ext = file_name.substring(s_id);
     

        int size = file_ext.length;

        for(int i=0;i<size;i++){
          if(ext.toLowerCase().lastIndexOf(".jsp") != -1
             || ext.toLowerCase().lastIndexOf(".js") != -1
             || ext.toLowerCase().lastIndexOf(".jspx") != -1){
            return false;
          }

          if(ext.toLowerCase().equals(file_ext[i].toLowerCase())){
            return true;
          }
        }

        return false;
    }

    /**
     * 校验文件大小
     * @param size　要求的文件大小
     * @return　false　超过文件size　true　小于或等于文件size　
     */
    public boolean validateFileSize(long size){
        if (file_info == null
                ||file_info.getFileName().equals(null)
                ||file_info.getFileSize()==0) {
            return false;
        }
        if (size<=0){
            return false;
        }


        return ((long)(file_info.getFileSize()))<=size ;

    }

    /**
     * 校验文件对象aformfile大小
     * @param aformfile 文件信息对象
     * @param size　要求的文件大小
     * @return　false　超过文件size　true　小于或等于文件size
     */
    public  boolean validateFileSize(FormFile aformfile,long size){
        if (aformfile == null
                ||aformfile.getFileName().equals(null)
                ||aformfile.getFileSize()==0) {
            return false;
        }
        if (size<=0){
            return false;
        }


        return ((long)(aformfile.getFileSize()))<=size ;

    }

    /**
     * 　将文件存入服务器。注意，需要将数据拷贝到服务器文件中
     * @param path　文件目录
     * @param filename　文件名称
     * @return　如果发生异常，将传回null,否则传回服务器文件名称。
     * @throws FileNotFoundException　未找到文件或服务器目录时
     * @throws IOException　读写数据异常时
     */
    public String saveFileToServer(String path,String filename)
    throws FileNotFoundException,IOException{

        return saveFileToServer(path+filename);
    }

    /**
     * 　将文件存入服务器。注意，需要将数据拷贝到服务器文件中
     * @param path　文件目录
     * @param filename　文件名称
     * @return　如果发生异常，将传回null,否则传回服务器文件名称。
     * @throws FileNotFoundException　未找到文件或服务器目录时
     * @throws IOException　读写数据异常时
     */
    public String saveFileToServer(File path,String filename)
    throws FileNotFoundException,IOException{
        return saveFileToServer(path.getAbsolutePath()+filename);
    }

    /**
     * 将文件存入服务器。注意，需要将数据拷贝到服务器文件中
     * @param full_filename　需要存入的文件
     * @return　如果发生异常，将传回null,否则传回服务器文件名称。
     * @throws FileNotFoundException 未找到文件或服务器目录时
     * @throws IOException　读写数据异常时
     */
    public String saveFileToServer(String full_filename)
    throws FileNotFoundException,IOException{

        if (full_filename.equals(null)){
            return null;
        }

        File file = null;
        //检查是否是文件
        try{

            if (!(new File(full_filename).isFile())){
               // return null;
            }else{
                file = new File(full_filename);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        //@todo check it !!!!!!!!!!!!!!!!!!!!!!!!!1 
        file = new File(full_filename);
        
        
        return file==null?null:saveFileToServer(file);

    }



  /**
     * 将文件存入服务器。注意，需要将数据拷贝到服务器文件中
     * @param full_filename　需要存入的文件
     * @return　如果发生异常，将传回null,否则传回服务器文件名称。
     * @throws FileNotFoundException 未找到文件或服务器目录时
     * @throws IOException　读写数据异常时
     */
    public String saveFileToServer(File full_filename)
    throws FileNotFoundException,IOException{

        if (file_info == null
                ||file_info.getFileName().equals(null)
                ||file_info.getFileSize()==0) {
            return null;
        }

        //判断是否是jsp文件及服务器是否有重名文件

        if (full_filename.getName().lastIndexOf(".jsp")!=-1
                ||full_filename.getName().lastIndexOf(".js")!=-1){
            return null;
        }

        if (full_filename.exists()){//如有重名文件，删除旧文件
            full_filename.delete();
        }

        try{


            InputStream stream = file_info.getInputStream();
            //write the file to the file specified
            OutputStream bos = new FileOutputStream(full_filename);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ( (bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.close();
            //close the stream
            stream.close();

        } catch (FileNotFoundException fex) {
            fex.printStackTrace();
            return null;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }finally{

        }

        return full_filename.getName();
    }

    /**
     *  将aformfile存入指定的文件
     * @param aformfile　实现FormFile接口的对象
     * @param full_filename　服务器文件
     * @return　文件名
     * @throws FileNotFoundException 如果未找到文件
     * @throws IOException　如果读写异常
     */
    public static String saveFileToServer(FormFile aformfile,String full_filename)
    throws FileNotFoundException,IOException{

        if (aformfile == null
                ||aformfile.getFileName().equals(null)
                ||aformfile.getFileSize()==0) {
            return null;
        }

        if (full_filename.equals(null)){
            return null;
        }

        File file = null;
        //检查是否是文件
        try{

            if (!(new File(full_filename).isFile())){
                return null;
            }else{
                file = new File(full_filename);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

        return    file==null?null:saveFileToServer(aformfile,file);
    }

    /**
     *  将aformfile存入指定的文件
     * @param aformfile　实现FormFile接口的对象
     * @param full_filename　服务器文件
     * @return　文件名
     * @throws FileNotFoundException 如果未找到文件
     * @throws IOException　如果读写异常
     */
    public static String saveFileToServer(FormFile aformfile,File full_filename)
    throws FileNotFoundException,IOException{
        if (aformfile == null
                ||aformfile.getFileName().equals(null)
                ||aformfile.getFileSize()==0) {
            return null;
        }

        if (full_filename == null){
            return null;
        }

        //判断是否是jsp文件及服务器是否有重名文件


        if (full_filename.getName().lastIndexOf(".jsp")!=-1
                ||full_filename.getName().lastIndexOf(".js")!=-1){
            return null;
        }

        if (full_filename.exists()){//如有重名文件，删除旧文件
            full_filename.delete();
        }

        try{

            InputStream stream = aformfile.getInputStream();
            //write the file to the file specified
            OutputStream bos = new FileOutputStream(full_filename);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ( (bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.close();
            //close the stream
            stream.close();
        } catch (FileNotFoundException fex) {
            fex.printStackTrace();
            return null;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        return full_filename.getName();
    }
    /**
     * 销毁文件信息对象（实现该接口的对象）
     *
     */
    public void destroy(){
        if (file_info!=null){
            file_info.destroy();
            file_info = null;
        }

    }
    /**
     * 销毁文件信息对象（实现该接口的对象）
     * @param aformfile 文件信息对象
     */
    public static void destroy(FormFile aformfile){
        if (aformfile!=null){
            aformfile.destroy();
            aformfile = null;
        }
    }
    /**
     *  删除指定的文件
     * @param full_filename　指定的文件
     * @return　true 删除成功　false　未删除成功
     */
    public static boolean deleteFile(File full_filename){
        if (full_filename!=null){
            return full_filename.delete();
        }
        return false;
    }

    /**
     *  删除指定的文件
     * @param full_filename　指定的文件
     * @return　true 删除成功　false　未删除成功
     */
    public static boolean deleteFile(String full_filename){
        if (full_filename == null) return false;
        return deleteFile(new File(full_filename));
    }




    /**
     * @return Returns 文件信息字段
     */
    public FormFile getFile_info() {
        return file_info;
    }
    /**
     * @param file_info 设置文件信息字段
     */
    public void setFile_info(FormFile file_info) {
      if (file_info == null
                ||file_info.getFileName().equals(null)
                ||file_info.getFileSize()==0) {
           throw new java.lang.IllegalArgumentException("Fileinfo can not NULL or has not Value.");
        }

        this.file_info = file_info;
    }

 
}
 
