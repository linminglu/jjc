package com.framework.common.email;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.framework.common.bean.EmailBean;
import com.framework.exception.business.NoFromMailException;
import com.framework.exception.business.NoSendtoMailException;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author yangjy
 * @version 1.0
 */

public interface ISendEmail {
  /**
   * @得到一个地址的数组,参数addressSet中多个地址应用分号[;]隔开
   * @param addressSet String
   * @throws AddressException
   * @return InternetAddress[]
   */
  public InternetAddress[] getAddress(String addressSet) throws AddressException;
  /**
   * @发送邮件(不带附件)
   * @param emailBean EmailBean
   */
  public void sendMailNoFile(EmailBean emailBean)
      throws NoFromMailException,NoSendtoMailException,MessagingException;
  /**
   * @发送邮件(带附件)
   * @param emailBean EmailBean
   */
  public String sendMailAndFile(EmailBean emailBean)
      throws NoFromMailException,NoSendtoMailException,MessagingException;
}
