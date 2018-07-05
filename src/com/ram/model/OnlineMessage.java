package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class OnlineMessage implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer onlineMessageId;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String content;

    /** nullable persistent field */
    private Integer sendUserId;

    /** nullable persistent field */
    private Integer recieveBizGroupId;

    /** nullable persistent field */
    private Integer recieveUserId;

    /** nullable persistent field */
    private Date updateDateTime;

    /** persistent field */
    private String messageStatus;

    /** full constructor */
    public OnlineMessage(String title, String content, Integer sendUserId, Integer recieveBizGroupId, Integer recieveUserId, Date updateDateTime, String messageStatus) {
        this.title = title;
        this.content = content;
        this.sendUserId = sendUserId;
        this.recieveBizGroupId = recieveBizGroupId;
        this.recieveUserId = recieveUserId;
        this.updateDateTime = updateDateTime;
        this.messageStatus = messageStatus;
    }

    /** default constructor */
    public OnlineMessage() {
    }

    /** minimal constructor */
    public OnlineMessage(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Integer getOnlineMessageId() {
        return this.onlineMessageId;
    }

    public void setOnlineMessageId(Integer onlineMessageId) {
        this.onlineMessageId = onlineMessageId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSendUserId() {
        return this.sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Integer getRecieveBizGroupId() {
        return this.recieveBizGroupId;
    }

    public void setRecieveBizGroupId(Integer recieveBizGroupId) {
        this.recieveBizGroupId = recieveBizGroupId;
    }

    public Integer getRecieveUserId() {
        return this.recieveUserId;
    }

    public void setRecieveUserId(Integer recieveUserId) {
        this.recieveUserId = recieveUserId;
    }

    public Date getUpdateDateTime() {
        return this.updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getMessageStatus() {
        return this.messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("onlineMessageId", getOnlineMessageId())
            .toString();
    }

}
