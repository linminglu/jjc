package com.ram.model;
import java.util.Date;

   /**   新闻资讯类
    * NewsInformation 实体类
    * author:	ch
    * createDate:	2017-07-19 18:58:11
    */ 


public class NewsInformation{
	private Integer nid;
	private String title;//名称
	private String  subTitle;//副标题
	private String content;//内容
	private Date submitTime;//生成时间
	private String link;//
	private String type;//新闻分类   直接填写的推荐  新闻  分析 中奖等
	private Integer tid;//新闻类型id   表示属于哪些方面的新闻
	private String author;//作者
	private String company;//机构名称 
	private Integer prevId;//上一条数据的id，  在本条数据进行删除的时候，需要更新其下面一条数据对应的这个值
	private String status;
	private String img;//图片
	public void setNid(Integer nid){
		this.nid=nid;
	}
	public Integer getNid(){
		return nid;
	}
	public void setTitle(String title){
	this.title=title;
	}
	public String getTitle(){
		return title;
	}
	public void setContent(String content){
	this.content=content;
	}
	public String getContent(){
		return content;
	}
	public void setSubmitTime(Date submitTime){
	this.submitTime=submitTime;
	}
	public Date getSubmitTime(){
		return submitTime;
	}
	public void setLink(String link){
	this.link=link;
	}
	public String getLink(){
		return link;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setAuthor(String author){
	this.author=author;
	}
	public String getAuthor(){
		return author;
	}
	public void setCompany(String company){
	this.company=company;
	}
	public String getCompany(){
		return company;
	}
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	public Integer getPrevId() {
		return prevId;
	}
	public void setPrevId(Integer prevId) {
		this.prevId = prevId;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

}
