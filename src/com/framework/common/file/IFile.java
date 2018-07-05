package com.framework.common.file;

import java.io.IOException;
import com.framework.exception.business.FilesFileException;
import com.framework.exception.business.NoExistFileException;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author yangjy
 * @version 1.0
 */

public interface IFile {
  /**
   * 建立目录
   * @param folder String
   * @throws FilesException
   * @throws NoExistException
   */
  public void createFolder(String folder) throws FilesFileException,NoExistFileException;
  /**
   * 删除目录
   * @param folder String
   * @throws FilesException
   */
  public void deleteFolder(String folder) throws FilesFileException;
  /**
   * 建立文件
   * @param folder String
   * @param fileName String
   * @param htmlText String
   * @throws FilesException
   * @throws NoExistException
   * @throws IOException
   */
  public void createFile(String folder, String fileName, String text) throws
      FilesFileException, NoExistFileException,IOException;
  /**
   * 移动文件
   * @param srcFileName String
   * @param desFolder String
   * @throws FilesException
   * @throws NoExistException
   */
  public void moveFile(String srcFileName, String desFolder) throws
      FilesFileException,NoExistFileException;
  /**
   *  删除文件
   * @param srcFileName String
   * @throws FilesException
   */
  public void deleteFile(String srcFileName) throws FilesFileException;

  /**
   * 获取文件的扩展名
   * @param  fileName 文件名
   * @return
   */
  public String getFileExt(String fileName);

  /**
   * filePath路径下是否存在文件file
   * @param  filePath  文件路径
   * @param  file      文件名
   * @return boolean
   */
  public boolean isFileExist(String filePath, String file);

}
