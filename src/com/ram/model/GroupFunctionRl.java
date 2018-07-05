package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class GroupFunctionRl implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer groupFunctionId;
    
    private Integer functionId;
    
    private Integer groupId;
    
    private Integer orderSn;

 
    /** full constructor */
    public GroupFunctionRl(Integer groupFunctionId, Integer functionId,Integer groupId,Integer orderSn) {
        this.groupFunctionId = groupFunctionId;
        this.functionId=functionId;
        this.groupId=groupId;
        this.orderSn= orderSn;
    }

    /** default constructor */
    public GroupFunctionRl() {
    }

    public Integer getGroupFunctionId() {
        return this.groupFunctionId;
    }

    public void setGroupFunctionId(Integer groupFunctionId) {
        this.groupFunctionId = groupFunctionId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("groupFunctionId", getGroupFunctionId())
            .toString();
    }

	public Integer getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(Integer orderSn) {
		this.orderSn = orderSn;
	}

}
