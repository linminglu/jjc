package com.framework.common.email.javamail;

import javax.mail.*;

public class Email_Autherticatorbean
    extends javax.mail.Authenticator {
  private String m_username = null;
  private String m_userpass = null;

  public PasswordAuthentication performCheck(String user, String pass) {
    m_username = user;
    m_userpass = pass;
    return getPasswordAuthentication();
  }

  public void setUsername(String username) {
    m_username = username;
  }

  public void setUserpass(String userpass) {
    m_userpass = userpass;
  }

  public Email_Autherticatorbean() {
    super();
  }

  public Email_Autherticatorbean(String username, String userpass) {
    super();
    setUsername(username);
    setUserpass(userpass);
  }

  //一定要有这个方法，它是在需要身份验证时自动被调用的
  public PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(m_username, m_userpass);
  }
}
