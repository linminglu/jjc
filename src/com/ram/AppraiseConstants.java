package com.ram;

public final class AppraiseConstants {

	//评估调查status字段的状态
	//0：关闭状态
	public final static String CLOSE_STATUS = "0";

	//1:尚未申请审核
	public final static String NOT_REQUISITION_EXAMINE = "1";

	//2：正在申请中
	public final static String REQUISITION = "2";

	//3：审核不通过
	public final static String NOT_EXAMINE_PASS = "3";
	
	//4：审核通过
	public final static String EXAMINE_PASS = "4";

	//5: 发布状态
	public final static String RELEASE_STATUS = "5";

	
	//评估调查类型
	//1：网院教师评估：参与评估的人必须使网院的用户
	public final static String NET_COLLEGE_TUTOR_APPRAISE = "1";
	
	//2：网院中心评估：参与评估的人必须使网院的用户
	public final static String NET_COLLEGE_CENTER_APPRAISE = "2";

	//3：网院调查：参与调查的人必须网院用户并且必须登录。
	public final static String NET_COLLEGE_INQUIRY = "3";

	//4：社会调查：参与调查的人可以不是平台用户或者不用登录平台。
	public final static String SOCIETY_INQUIRY = "4";

	
	//评估对象类型:
	//1:用户
	public final static String APPRAISE_USER = "1";

	//2：中心
	public final static String APPRAISE_CENTER = "2";

	
	//问题的类型:
	//1:单选题
	public final static String SINGLE_CHOICE = "1";

	//2:多选题
	public final static String MANY_CHOICE = "2";

	//3:问答题
	public final static String QUESTION_ANSWER = "3";
}
