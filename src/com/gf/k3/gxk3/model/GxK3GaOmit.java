package com.gf.k3.gxk3.model;

   /**
    * GxK3GaOmit 实体类
    * author:	ch
    * createDate:	2017-08-02 10:51:31
    */ 


public class GxK3GaOmit{
	private Integer oid;
	private String sessionNo;//期号
	private String openResult;//开奖结果
	private Integer fenbu1;//数字1的遗漏数量  以此类推
	private Integer fenbu2;
	private Integer fenbu3;
	private Integer fenbu4;
	private Integer fenbu5;
	private Integer fenbu6;
	private Integer fenbu7;
	private Integer fenbu8;
	private Integer fenbu9;
	private Integer fenbu10;
	private Integer fenbu11;
	private Integer he;//和值
	private Integer kuadu;//跨度    最大数和最小数之差
	private String daxiao;//开出的结果里面  大小比
	private String jiou;//开出的结果里面      奇偶比
	private String zhihe;//开出的结果里面   质合比（质数，合数）
	public void setOid(Integer oid){
	this.oid=oid;
	}
	public Integer getOid(){
		return oid;
	}
	public void setSessionNo(String sessionNo){
	this.sessionNo=sessionNo;
	}
	public String getSessionNo(){
		return sessionNo;
	}
	public void setOpenResult(String openResult){
	this.openResult=openResult;
	}
	public String getOpenResult(){
		return openResult;
	}
	public void setFenbu1(Integer fenbu1){
	this.fenbu1=fenbu1;
	}
	public Integer getFenbu1(){
		return fenbu1;
	}
	public void setFenbu2(Integer fenbu2){
	this.fenbu2=fenbu2;
	}
	public Integer getFenbu2(){
		return fenbu2;
	}
	public void setFenbu3(Integer fenbu3){
	this.fenbu3=fenbu3;
	}
	public Integer getFenbu3(){
		return fenbu3;
	}
	public void setFenbu4(Integer fenbu4){
	this.fenbu4=fenbu4;
	}
	public Integer getFenbu4(){
		return fenbu4;
	}
	public void setFenbu5(Integer fenbu5){
	this.fenbu5=fenbu5;
	}
	public Integer getFenbu5(){
		return fenbu5;
	}
	public void setFenbu6(Integer fenbu6){
	this.fenbu6=fenbu6;
	}
	public Integer getFenbu6(){
		return fenbu6;
	}
	public void setFenbu7(Integer fenbu7){
	this.fenbu7=fenbu7;
	}
	public Integer getFenbu7(){
		return fenbu7;
	}
	public void setFenbu8(Integer fenbu8){
	this.fenbu8=fenbu8;
	}
	public Integer getFenbu8(){
		return fenbu8;
	}
	public void setFenbu9(Integer fenbu9){
	this.fenbu9=fenbu9;
	}
	public Integer getFenbu9(){
		return fenbu9;
	}
	public void setFenbu10(Integer fenbu10){
	this.fenbu10=fenbu10;
	}
	public Integer getFenbu10(){
		return fenbu10;
	}
	public void setFenbu11(Integer fenbu11){
	this.fenbu11=fenbu11;
	}
	public Integer getFenbu11(){
		return fenbu11;
	}
	public void setHe(Integer he){
	this.he=he;
	}
	public Integer getHe(){
		return he;
	}
	public void setKuadu(Integer kuadu){
	this.kuadu=kuadu;
	}
	public Integer getKuadu(){
		return kuadu;
	}
	public void setDaxiao(String daxiao){
	this.daxiao=daxiao;
	}
	public String getDaxiao(){
		return daxiao;
	}
	public void setJiou(String jiou){
	this.jiou=jiou;
	}
	public String getJiou(){
		return jiou;
	}
	public void setZhihe(String zhihe){
	this.zhihe=zhihe;
	}
	public String getZhihe(){
		return zhihe;
	}
}
