package com.ram.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 桌面软件注册信息
 * @author Administrator
 */
public class DeskRegister implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer deskRegisterId;
	private String serialNumber;//软件序列号
	private Integer softwareId;//软件类型1桌面软件
	private String softwareCode;//软件代码
	private String status;//状态 0初始化 1已激活使用2已发出
	private String isupdate;//0不升级1可升级
	private String regMac;//注册mac地址
	private String regVer;//版本号
	private String regIp;//注册IP地址
	private Integer useDays;//使用时间
	private Integer launchCount;
	private Integer checkCount;
	private Integer regCount;//注册统计
	private Integer remacCount;//网卡地址变换注册统计
	private Date regTime;//注册时间
	private String regLog;//注册日志
	private String remarks;//备注
	private Date sendTime;//发号时间
	private Integer sellprice;//价格
	private String sntype;//1销售0测试
	private String qq;
	private String sntaobao;//代理商
//	private String checkout;//结账
//	private Date checktime;//结账时间
	private Date launchTime;
	private String badmac;//非法mac地址
	private String snflag;//1表示忽略注册检查 一般出现在客户机mac地址为空的情况
	
	//
	private String dsCode;
	private String dsTitle;
	
	private String checkverfree;
	private String checkadfree;
	
	private Integer userId;
	
	
//	public DeskRegister(){}
//	
//	public DeskRegister(Integer deskRegisterId,String serialNumber,String softwareCode,String status,
//			Date sendTime,String remarks,Integer sellprice,String sntype,
//			String regMac,String regVer,String regIp,Date regTime,
//			String isupdate,Integer launchCount,Integer checkCount,
//			String dsTitle
//			){
//		this.deskRegisterId=deskRegisterId;
//		this.serialNumber=serialNumber;
//		this.softwareCode=softwareCode;
//		this.status=status;
//		this.sendTime=sendTime;
//		this.remarks=remarks;
//		this.sellprice=sellprice;
//		this.sntype=sntype;
//		this.regMac=regMac;
//		this.regVer=regVer;
//		this.regIp=regIp;
//		this.regTime=regTime;
//		this.isupdate=isupdate;
//		this.launchCount=launchCount;
//		this.checkCount=checkCount;
//		this.dsTitle=dsTitle;
//	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCheckverfree() {
		return checkverfree;
	}

	public void setCheckverfree(String checkverfree) {
		this.checkverfree = checkverfree;
	}

	public String getCheckadfree() {
		return checkadfree;
	}

	public void setCheckadfree(String checkadfree) {
		this.checkadfree = checkadfree;
	}

	public Integer getDeskRegisterId() {
		return deskRegisterId;
	}

	public void setDeskRegisterId(Integer deskRegisterId) {
		this.deskRegisterId = deskRegisterId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getSoftwareId() {
		return softwareId;
	}

	public void setSoftwareId(Integer softwareId) {
		this.softwareId = softwareId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRegMac() {
		return regMac;
	}

	public void setRegMac(String regMac) {
		this.regMac = regMac;
	}

	public String getRegVer() {
		return regVer;
	}

	public void setRegVer(String regVer) {
		this.regVer = regVer;
	}

	public String getRegIp() {
		return regIp;
	}

	public void setRegIp(String regIp) {
		this.regIp = regIp;
	}

	public Integer getUseDays() {
		return useDays;
	}

	public void setUseDays(Integer useDays) {
		this.useDays = useDays;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public String getRegLog() {
		return regLog;
	}

	public void setRegLog(String regLog) {
		this.regLog = regLog;
	}

	public String getSoftwareCode() {
		return softwareCode;
	}

	public void setSoftwareCode(String softwareCode) {
		this.softwareCode = softwareCode;
	}

	public Integer getLaunchCount() {
		return launchCount;
	}

	public void setLaunchCount(Integer launchCount) {
		this.launchCount = launchCount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getIsupdate() {
		return isupdate;
	}

	public void setIsupdate(String isupdate) {
		this.isupdate = isupdate;
	}

	public Integer getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(Integer checkCount) {
		this.checkCount = checkCount;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getSellprice() {
		return sellprice;
	}

	public void setSellprice(Integer sellprice) {
		this.sellprice = sellprice;
	}

	public String getSntype() {
		return sntype;
	}

	public void setSntype(String sntype) {
		this.sntype = sntype;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getDsCode() {
		return dsCode;
	}

	public void setDsCode(String dsCode) {
		this.dsCode = dsCode;
	}

	public String getDsTitle() {
		return dsTitle;
	}

	public void setDsTitle(String dsTitle) {
		this.dsTitle = dsTitle;
	}

	public String getSntaobao() {
		return sntaobao;
	}

	public void setSntaobao(String sntaobao) {
		this.sntaobao = sntaobao;
	}

	public Integer getRegCount() {
		return regCount;
	}

	public void setRegCount(Integer regCount) {
		this.regCount = regCount;
	}

	public Integer getRemacCount() {
		return remacCount;
	}

	public void setRemacCount(Integer remacCount) {
		this.remacCount = remacCount;
	}

	public String getBadmac() {
		return badmac;
	}

	public void setBadmac(String badmac) {
		this.badmac = badmac;
	}

	public String getSnflag() {
		return snflag;
	}

	public void setSnflag(String snflag) {
		this.snflag = snflag;
	}

	public Date getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(Date launchTime) {
		this.launchTime = launchTime;
	}

	
}