package com.framework.common.email.javamail;

import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import java.util.Date;
import javax.mail.Transport;
import javax.mail.MessagingException;
import com.framework.common.email.ISendEmail;
import javax.mail.Message;
import com.framework.common.bean.EmailBean;
import com.framework.common.properties.IReadProperties;
import com.framework.common.properties.impl.ReadPropertiesImpl;
import javax.mail.internet.AddressException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.framework.exception.business.NoSendtoMailException;
import com.framework.exception.business.NoFromMailException;
import com.framework.exception.BusinessException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author yangjy
 * @version 1.0
 */
public class SendEmailImpl
    implements ISendEmail {
   /**  log */
   private static Log log = LogFactory.getLog(SendEmailImpl.class);
   /**
   * 构造方法

   */
   public SendEmailImpl() {
  }

  public void sendMailNoFile(EmailBean emailBean)
      throws NoFromMailException,NoSendtoMailException,MessagingException{
    //得到properties属性

    IReadProperties ir = new ReadPropertiesImpl();
    //username
    String userName = ir.getValue("MAIL.USERNAME");
    //password
    String password = ir.getValue("MAIL.PASSWORD");
    //protocol
    String protocol = ir.getValue("MAIL.PROTOCOL");
    //mailHost
    String mailHost = ir.getValue("MAIL.HOST");
    //mail.stmp.auth
    String auth = ir.getValue("MAIL.SMTP.AUTH");
    //check
    String check = ir.getValue("MAIL.SMTP.CHECK");
    //发件人

    String from = emailBean.getFrom();
    //收件人

    String sendto = emailBean.getSendto();
    //抄送人
    String cc = emailBean.getCc();
    //暗抄送人
    String bcc = emailBean.getBcc();
    //标题
    String subject = emailBean.getSubject();
    //内容
    String content = emailBean.getContent();

    try {
      Properties props = new Properties();
      Session sendMailSession;
      Transport transport;
      Email_Autherticatorbean aEmail_Autherticatorbean = new
          Email_Autherticatorbean();
      //验证用户名和密码
      aEmail_Autherticatorbean.performCheck(userName, password);
      sendMailSession = Session.getInstance(props, aEmail_Autherticatorbean);
      //设置smtp服务器

      props.put(protocol, mailHost);
      //设成true使认证起作用
      props.put(auth, check);

      Message newMessage = new MimeMessage(sendMailSession);
      if(from != null && !from.equals("")) {
        newMessage.setFrom(new InternetAddress(new String(from.getBytes("GBK"),
            "8859_1")));
      } else {
        log.error("发送邮件时候错误，没有得到发件人地址...................");
        //throw new NoFromMailException("没有得到发件人地址!");
      }
      if(sendto != null && !sendto.equals("")) {
        newMessage.setRecipients(Message.RecipientType.TO,
                                 this.getAddress(sendto));
      } else {
        log.error("发送邮件时候错误，没有输入收件人地址...................");
        //throw new NoSendtoMailException("没有输入收件人地址!");

      }
      if (cc != null && !cc.equals("")) {
        newMessage.setRecipients(Message.RecipientType.CC,
                                this.getAddress(cc));
      }

      if (bcc != null && !bcc.equals("")) {
        newMessage.setRecipients(Message.RecipientType.BCC,
                                this.getAddress(bcc));
      }
      if(subject != null && !subject.equals("")){
        newMessage.setSubject(subject);
      }
      newMessage.setSentDate(new Date());
      if(content != null && !content.equals("")){
        newMessage.setContent(content, "text/html;charset=GBK");
      } else {
        newMessage.setContent(new String(), "text/html;charset=GBK");
      }
      transport = sendMailSession.getTransport("smtp");
      Transport.send(newMessage);
    } catch (MessagingException m) {
      m.printStackTrace();
      log.error("邮件发送时出现错误1：" + m.getMessage());
      
    }catch (Exception e) {
      log.error("邮件发送时出现错误2：" + e.getMessage());
      //throw new BusinessException();
    }
  }

  /** 解析地址集合字符串 */
  public InternetAddress[] getAddress(String addressSet) throws AddressException {
    ArrayList list = new ArrayList();
    StringTokenizer tokens = new StringTokenizer(addressSet, ";");
    while (tokens.hasMoreTokens()) {
      list.add(new InternetAddress(tokens.nextToken().trim()));
    }
    InternetAddress[] addressArray = new InternetAddress[list.size()];
    list.toArray(addressArray);
    return addressArray;
  }


  public String sendMailAndFile(EmailBean emailBean)
      throws NoFromMailException,NoSendtoMailException,MessagingException{
    //得到properties属性

    IReadProperties ir = new ReadPropertiesImpl();
    //username
    String user = ir.getValue("MAIL.USERNAME");
    //password
    String password = ir.getValue("MAIL.PASSWORD");
    //protocol
    String protocol = ir.getValue("MAIL.PROTOCOL");
    //mailHost
    String mailhost = ir.getValue("MAIL.HOST");
    //mail.stmp.auth
    String auth = ir.getValue("MAIL.SMTP.AUTH");
    //check
    String check = ir.getValue("MAIL.SMTP.CHECK");
    //收件人

    String to = emailBean.getSendto();
    //发件人

    String from = emailBean.getFrom();
    //主题
    String subject = emailBean.getSubject();
    //抄送人
    String cc = emailBean.getCc();
    //暗抄送

    String bcc = emailBean.getBcc();
    // mail 内容
    String content = emailBean.getContent();
    //附件的文件名
    String[] attachments = emailBean.getFileName();
    //MimeMessage is 能理解MIME 类型和头的电子邮件消息

    MimeMessage mimeMsg = null;
    //邮件会话对象
    Session session = null;


    from = emailBean.getFrom();
    to = emailBean.getSendto();
    cc = emailBean.getCc();
    bcc = emailBean.getBcc();
    subject = emailBean.getSubject();
    content = emailBean.getContent();

    try {
      Properties props = System.getProperties(); //获得系统属性


      props.put(protocol, mailhost); //设置SMTP主机
      props.put(auth, check); //设置身份验证为真，若须身份验证则必须设为真


      //获得邮件会话对象
      //session = Session.getDefaultInstance(props,null);
      //获得邮件会话对象
      session = Session.getDefaultInstance(
                     props, new Email_Autherticatorbean(user,password));

      //创建MIME邮件对象
      mimeMsg = new MimeMessage(session);
      //设置发信人

      if(from != null && !from.equals("")){
        mimeMsg.setFrom(new InternetAddress(new String(from.getBytes("GBK"),
            "8859_1")));
      } else {
        log.debug("没有得到发件人...................................");
        throw new NoFromMailException("没有得到发件人");
      }

      //设置收信人

      if (to != null && !to.equals("")) {
        mimeMsg.setRecipients(RecipientType.TO,
                              this.getAddress(to));
      } else{
        log.debug("没有得到收件人...................................");
        throw new NoSendtoMailException("没有得到收件人");
      }

      //设置抄送人
      if (cc != null && !cc.equals("")) {
        mimeMsg.setRecipients(RecipientType.CC,
                              this.getAddress(cc));
      }

      //设置暗送人
      if (bcc != null && !bcc.equals("")) {
        mimeMsg.setRecipients(RecipientType.BCC,
                              this.getAddress(bcc));
      }
      // 创建 Multipart 并放入每个 MimeBodyPart
      Multipart mp = new MimeMultipart();

      if(subject != null && !subject.equals("")) {
        //设置邮件主题
        mimeMsg.setSubject(subject, "GBK");
      } else {
        mimeMsg.setSubject(new String(), "GBK");
      }
      // 第一部分信息
      MimeBodyPart mbp1 = new MimeBodyPart();
      if(content != null && !content.equals("")){
        mbp1.setText(content, "GBK");
      } else {
        mbp1.setText(new String(), "GBK");
      }
      mp.addBodyPart(mbp1);

      // 在第二部分信息中附加文件
      if (attachments != null) {
        for (int i = 0; i < attachments.length; i++) {
          MimeBodyPart mbp2 = new MimeBodyPart();
          FileDataSource fds = new FileDataSource(attachments[i]);
          mbp2.setDataHandler(new DataHandler(fds));
          mbp2.setFileName(new String(fds.getName().getBytes("GBK"), "8859_1"));
          mp.addBodyPart(mbp2);
        }
      }
      // 增加 Multipart 到信息体
      mimeMsg.setContent(mp);

      //发送日期

      mimeMsg.setSentDate(new Date());
      //发送邮件

      Transport.send(mimeMsg);
      //log.debug( "邮件发送成功："+to);
    }catch (MessagingException e) {
      log.debug("邮件发送时出现错误1：" + to);
      throw new MessagingException("邮件发送时出现错误");
    }catch (Exception e) {
      log.debug("邮件发送时出现错误：" + to);
      return to;
    }
    return "";
  }


  

}
