package com.apps.model;

import java.io.Serializable;

/**
 * 用户余额明细和投注明细关联表
 * @author admin
 *
 */

public class UserTradeDetailRl  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer  rid;
	private Integer tradeDetailId;//用户余额明细id
	private Integer betDetailId;//用户投注明细id
	private String gfxy;//投注标识1=官方2=信用
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public Integer getTradeDetailId() {
		return tradeDetailId;
	}
	public void setTradeDetailId(Integer tradeDetailId) {
		this.tradeDetailId = tradeDetailId;
	}
	public Integer getBetDetailId() {
		return betDetailId;
	}
	public void setBetDetailId(Integer betDetailId) {
		this.betDetailId = betDetailId;
	}
	public String getGfxy() {
		return gfxy;
	}
	public void setGfxy(String gfxy) {
		this.gfxy = gfxy;
	}
	
	
	
}
