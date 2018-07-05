package com.ram.model;

import java.io.Serializable;
import java.sql.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Friend implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer friendId;

    /** nullable persistent field */
    private Integer ownerUserId;

    /** nullable persistent field */
    private Date addDateTime;
    
    /** nullable persistent field */
    private  com.ram.model.User friendUser;
    

    /** full constructor */
    public Friend(Integer ownerUserId, Date addDateTime,com.ram.model.User friendUser) {
        this.ownerUserId = ownerUserId;
        this.addDateTime = addDateTime;
        this.friendUser=friendUser;
    }

    /** default constructor */
    public Friend() {
    }

    public Integer getFriendId() {
        return this.friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public Integer getOwnerUserId() {
        return this.ownerUserId;
    }

    public void setOwnerUserId(Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Date getAddDateTime() {
        return this.addDateTime;
    }

    public void setAddDateTime(Date addDateTime) {
        this.addDateTime = addDateTime;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("friendId", getFriendId())
            .toString();
    }

	public com.ram.model.User getFriendUser() {
		return friendUser;
	}

	public void setFriendUser(com.ram.model.User friendUser) {
		this.friendUser= friendUser;
	}

}
