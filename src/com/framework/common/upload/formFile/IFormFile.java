/**
 * 
 */
package com.framework.common.upload.formFile;
import org.apache.struts.upload.FormFile;

/**
 * @author wangyuanjun
 * createTime 2006-2-21
 * commpany bpc
 * project newplat
 */
public interface IFormFile {
	   /**
     * 校验文件后缀名
     * @param file_ext 文件扩展名数组，如.jpg, .rar, .zip。
     * @return　true：是该扩展名数组中存在的后缀名　false：不是该扩展名数组中存在的后缀名
     */
    public boolean validateFileName(String[] file_ext);
    
    /**
     * 校验aformfile对象的后缀名，
     * @param aformfile　实现FormFile接口的对象　
     * @param file_ext　文件扩展名数组，如.jpg, .rar, .zip。
     * @return　true：是该扩展名数组中存在的后缀名　false：不是该扩展名数组中存在的后缀名
     */
    public  boolean validateFileName(FormFile aformfile,String[] file_ext);
    
    /**
     * 校验文件大小
     * @param size　要求的文件大小
     * @return　false　超过文件size　true　小于或等于文件size　
     */
    public boolean validateFileSize(long size);
    
    /**
     * 校验文件对象aformfile大小
     * @param aformfile 文件信息对象
     * @param size　要求的文件大小
     * @return　false　超过文件size　true　小于或等于文件size
     */
    public  boolean validateFileSize(FormFile aformfile,long size);
    
    /**
     * 　将文件存入服务器。注意，需要将数据拷贝到服务器文件中
     * @param path　文件目录
     * @param filename　文件名称
     * @return　如果发生异常，将传回null,否则传回服务器文件名称。
     * @throws FileNotFoundException　未找到文件或服务器目录时
     * @throws IOException　读写数据异常时
     */
 //   public String saveFileToServer(File path,String filename);

    
    

}
