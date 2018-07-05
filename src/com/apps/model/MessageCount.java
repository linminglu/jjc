package com.apps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 平台短信记录
 * 
 * @author Mr.zang
 */
public class MessageCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer msgId;
	private String receivePhone;// 接收者手机号
	private Date sendTime;// 发送时间
	private String sendIp;// 发送者IP
	private String content;// 发送内容
	private Integer num;// 发送短信条数

	public MessageCount() {
	}

	/**
	 * 构造函数
	 * 
	 * @param receivePhone
	 *            接收者手机
	 * @param sendTime
	 *            发送时间
	 * @param sendIp
	 *            发送者IP
	 */
	// public MessageCount(String receivePhone, Date sendTime, String sendIp) {
	// this.receivePhone = receivePhone;
	// this.sendTime = sendTime;
	// this.sendIp = sendIp;
	// }
	/**
	 * 构造函数
	 * 
	 * @param receivePhone
	 *            接收者手机
	 * @param sendTime
	 *            发送时间
	 * @param sendIp
	 *            发送者IP
	 * @param content
	 *            发送内容
	 */
	public MessageCount(String receivePhone, Date sendTime, String sendIp,
			String content) {
		this.receivePhone = receivePhone;
		this.sendTime = sendTime;
		this.sendIp = sendIp;
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendIp() {
		return sendIp;
	}

	public void setSendIp(String sendIp) {
		this.sendIp = sendIp;
	}

}
