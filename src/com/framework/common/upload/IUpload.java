package com.framework.common.upload;

import java.io.IOException;

import javax.servlet.ServletException;

import com.framework.common.bean.UploadBean;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

public interface IUpload {
  /**
   * 检查上传文件大小
   * @param mySmartUpload SmartUpload
   * @return boolean
   */
  public boolean checkSize(SmartUpload mySmartUpload);
  /**
   * mySmartUpload.initialize(pageContext);mySmartUpload.upload();
   * @param mySmartUpload SmartUpload   初始化之后
   * @param sUrl String                 相对路径
   * @param path String                 物理路径
   * @throws SmartUploadException
   * @throws IOException
   * @throws ServletException
   * @return UploadBean
   */
  public UploadBean doUpload(SmartUpload mySmartUpload,String sUrl, String path)
      throws SmartUploadException, IOException;

  /**
   * mySmartUpload.initialize(pageContext);mySmartUpload.upload();
   * @param mySmartUpload SmartUpload    初始化之后
   * @throws SmartUploadException
   * @throws IOException
   * @throws ServletException
   * @return UploadBean
   */
  public void doUpload(SmartUpload mySmartUpload)
      throws SmartUploadException, IOException;
}
