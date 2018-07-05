package com.card.web.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import com.card.model.Card;
import com.card.model.RechargeWay;

public class RechargeWayForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	
	private RechargeWay rechargeWay = new RechargeWay();
	private Integer passageWayId;// 通道id
	private Integer channelId;// 渠道id
	private FormFile file;
	
	public RechargeWay getRechargeWay() {
		return rechargeWay;
	}
	public void setRechargeWay(RechargeWay rechargeWay) {
		this.rechargeWay = rechargeWay;
	}
	public Integer getPassageWayId() {
		return passageWayId;
	}
	public void setPassageWayId(Integer passageWayId) {
		this.passageWayId = passageWayId;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public FormFile getFile() {
		return file;
	}
	public void setFile(FormFile file) {
		this.file = file;
	}
}