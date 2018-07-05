package com.ram.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Function implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer functionId;

    /** nullable persistent field */
    private String functionTitle;

    /** nullable persistent field */
    private String functionLinkAddress;

    /** nullable persistent field */
    private Integer parentId;

    /** nullable persistent field */
    private Integer subFunctionNumber;

    /** nullable persistent field */
    private String isSystemFunction;

    private String functionActionPath;
    
    private Integer rootId;
    private Integer orderSn;
    private Set groupFunctionRl=new TreeSet();
    private Set roleFunctionRl=new TreeSet();
    
    
    /** full constructor */
    public Function(Integer functionId, String functionTitle, 
    		String functionLinkAddress, Integer parentId, 
    		String isSystemFunction,String functionActionPath,Integer rootId,Integer orderSn,Integer subFunctionNumber, 
    		Set groupFunctionRl,Set roleFunctionRl
    ) {
        this.functionId = functionId;
        this.functionTitle = functionTitle;
        this.functionLinkAddress = functionLinkAddress;
        this.parentId = parentId;
        this.subFunctionNumber = subFunctionNumber;
        this.isSystemFunction = isSystemFunction;
        this.functionActionPath=functionActionPath;
        this.groupFunctionRl=groupFunctionRl;
        this.roleFunctionRl=roleFunctionRl;
        this.rootId=rootId;
        this.orderSn=orderSn;
    }

    /** default constructor */
    public Function() {
    }

  
    /** minimal constructor */
    public Function(Integer functionId,Set groupFunctionRl,Set roleFunctionRl) {
        this.functionId = functionId;
        this.groupFunctionRl=groupFunctionRl;
        this.roleFunctionRl=roleFunctionRl;
    }

    public Integer getFunctionId() {
        return this.functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }

    public String getFunctionTitle() {
        return this.functionTitle;
    }

    public void setFunctionTitle(String functionTitle) {
        this.functionTitle = functionTitle;
    }

    public String getFunctionLinkAddress() {
        return this.functionLinkAddress;
    }

    public void setFunctionLinkAddress(String functionLinkAddress) {
        this.functionLinkAddress = functionLinkAddress;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSubFunctionNumber() {
        return this.subFunctionNumber;
    }
 
    public void setSubFunctionNumber(Integer subFunctionNumber) {
        this.subFunctionNumber = subFunctionNumber;
    }

    public String getIsSystemFunction() {
        return this.isSystemFunction;
    }

    public void setIsSystemFunction(String isSystemFunction) {
        this.isSystemFunction = isSystemFunction;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("functionId", getFunctionId())
            .toString();
    }

	public String getFunctionActionPath() {
		return functionActionPath;
	}

	public void setFunctionActionPath(String functionActionPath) {
		this.functionActionPath = functionActionPath;
	}

	public Set getGroupFunctionRl() {
		return groupFunctionRl;
	}

	public void setGroupFunctionRl(Set groupFunctionRl) {
		this.groupFunctionRl = groupFunctionRl;
	}

	public Set getRoleFunctionRl() {
		return roleFunctionRl;
	}

	public void setRoleFunctionRl(Set roleFunctionRl) {
		this.roleFunctionRl = roleFunctionRl;
	}

	public Integer getRootId() {
		return rootId;
	}

	public void setRootId(Integer rootId) {
		this.rootId = rootId;
	}

	public Integer getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(Integer orderSn) {
		this.orderSn = orderSn;
	}

}
