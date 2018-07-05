package com.ram.model;

import java.io.Serializable;

public class IpRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer ipRecordId;
	private String ip;//注册ip
    private Integer count;//该ip注册次数	

	public Integer getIpRecordId() {
		return ipRecordId;
	}
	public void setIpRecordId(Integer ipRecordId) {
		this.ipRecordId = ipRecordId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
