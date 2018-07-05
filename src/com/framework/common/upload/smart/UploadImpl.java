package com.framework.common.upload.smart;

import java.io.IOException;

import javax.servlet.ServletException;

import com.framework.common.bean.UploadBean;
import com.framework.common.properties.IReadProperties;
import com.framework.common.properties.impl.ReadPropertiesImpl;
import com.framework.common.upload.IUpload;
import com.framework.util.DateTimeUtil;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

public class UploadImpl
    implements IUpload {
  private IReadProperties read = new ReadPropertiesImpl();
  public UploadImpl() {
  }

  /**
   * checkSize()限制上传文件大小
   * @param mySmartUpload SmartUpload
   * @return boolean
   */
  public boolean checkSize(SmartUpload mySmartUpload) {
    boolean result = false;
    int maxSize = Integer.parseInt(read.getValue("UPLOAD.SIZE")) * 1024;

    for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
      com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);
      if (!myFile.isMissing()) {
        int size = 0;
        size = myFile.getSize();
        if (size < maxSize) {
          result = true;
        }
        else {
          result = false;
          return result;
        }
      }
    }
    return result;
  }

  /**
   * mySmartUpload.initialize(pageContext);mySmartUpload.upload();
   * @param mySmartUpload SmartUpload 初始化之后
   * @param sUrl String 相对路径
   * @param path String 物理路径
   * @throws SmartUploadException
   * @throws IOException
   * @return UploadBean
   */
  public UploadBean doUpload(SmartUpload mySmartUpload, String sUrl,
                             String path) throws SmartUploadException,
      IOException {
    UploadBean bean = new UploadBean();
    String fileExt = "";
    String address = "";
    String absAddress = "";
    String filename = "";
    String folder = "";
    int size = 0;
    try {
      folder = read.getValue("UPLOAD.FOLDER");
      for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
        com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);
        //mySmartUpload.save("/" + folder);
        if (!myFile.isMissing()) {
          //注释部分更改文件名
          String randomstr = DateTimeUtil.GetUniqueID();
          myFile.saveAs("/" + folder + "/" + randomstr + mySmartUpload.getFiles().getFile(0).getFileExt(), SmartUpload.SAVE_VIRTUAL);
          filename = mySmartUpload.getFiles().getFile(0).getFileName();
          fileExt = myFile.getFileExt();
          size = myFile.getSize();
          address = sUrl + "/" + randomstr;
          absAddress = path + folder + "\\" + randomstr;
          String url = sUrl + address + "/" + randomstr;
          bean.setFileExt(fileExt);
          bean.setFilename(randomstr);
          bean.setAbsAddress(absAddress);
          bean.setAddress(address);
          bean.setUrl(url);
          bean.setSize(size);
        }
      }
    }
    catch (SmartUploadException sue) {
      throw sue;
    }
    catch (IOException ioe) {
      throw ioe;
    }
    return bean;
  }

  /**
   * mySmartUpload.initialize(pageContext);mySmartUpload.upload();
   * @param mySmartUpload SmartUpload 初始化之后

   * @throws SmartUploadException
   * @throws IOException
   * @throws ServletException
   * @return UploadBean
   */
  public void doUpload(SmartUpload mySmartUpload) throws SmartUploadException,
      IOException{
    String folder = "";
    try {
      folder = read.getValue("UPLOAD.FOLDER");
      for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
        com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);
        String randomstr = DateTimeUtil.GetUniqueID();
        myFile.saveAs("/" + folder + "/" + randomstr + mySmartUpload.getFiles().getFile(0).getFileExt(), SmartUpload.SAVE_VIRTUAL);
//        mySmartUpload.save("/" + folder);
      }
    }
    catch (SmartUploadException sue) {
      throw sue;
    }
    catch (IOException ioe) {
      throw ioe;
    }
  }

}
