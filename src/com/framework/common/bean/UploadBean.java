package com.framework.common.bean;

public class UploadBean {
  public UploadBean() {
  }

  private String uploadtime;
  private String fileExt;
  private String url;
  private String filename;
  private String absAddress;
  private String address;
  private int size;

  public String getUploadtime() {
      return uploadtime;
  }
  public void setUploadtime(String uploadtime) {
      this.uploadtime = uploadtime;
  }

  public String getFileExt() {
      return fileExt;
  }
  public void setFileExt(String fileExt) {
      this.fileExt = fileExt;
  }

  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getAbsAddress() {
    return absAddress;
  }

  public void setAbsAddress(String absAddress) {
    this.absAddress = absAddress;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

}
