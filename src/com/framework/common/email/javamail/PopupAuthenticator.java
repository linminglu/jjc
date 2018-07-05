package com.framework.common.email.javamail;

import javax.mail.Authenticator;
import javax.mail.*;
import javax.mail.internet.*;


public class PopupAuthenticator
    extends Authenticator {

  String username = null;
  String password = null;

  public PopupAuthenticator() {
  }

  public PasswordAuthentication performCheck(String user, String pass) {
    username = user;
    password = pass;
    return getPasswordAuthentication();
  }

  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(username, password);
  }

}
