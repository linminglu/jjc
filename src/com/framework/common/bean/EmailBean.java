package com.framework.common.bean;

public class EmailBean {
  public EmailBean() {
  }
  
  //收件人
  String sendto = null;
  
  //发件人
  String from = null;
  //主题
  String subject = null;
  //抄送人
  String cc = null;
  //暗抄送
  String bcc = null;
  // mail 内容
  String content = null;
  //附件的文件名
  String[] fileName = null;

  public EmailBean(String from,String sendto,String subject,String content) {
      this.from=from;
      this.sendto=sendto;
      this.subject=subject;
      this.content=content;      
  }
  
  public void setSendto(String sendto) {
    this.sendto = sendto;
  }
  public String getSendto() {
    return sendto;
  }

  public void setFrom(String from) {
    this.from = from;
  }
  public String getFrom() {
    return from;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }
  public String getSubject() {
    return subject;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }
  public String getCc() {
    return cc;
  }

  public void setBcc(String bcc) {
    this.bcc = bcc;
  }
  public String getBcc() {
    return bcc;
  }

  public void setContent(String content) {
    this.content = content;
  }
  public String getContent() {
    return content;
  }

  public void setFileName(String[] fileName) {
    this.fileName = fileName;
  }
  public String[] getFileName() {
    return fileName;
  }
}
