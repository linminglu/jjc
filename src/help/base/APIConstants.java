package help.base;

import com.apps.Constants;


/**
 * api的常量
 * 
 * @author Mr.zang
 * 
 */
public class APIConstants {
	
	public static boolean DEBUG_MODE = true;//全局调试变量

	/** 请求成功 200 */
	public static final String CODE_REQUEST_SUCCESS = "200";
	/** 请求失败，一般是参数问题 201 */
	public static final String CODE_REQUEST_ERROR = "201";
	/** 未授权（无权限） 401 */
	public static final String CODE_UNAUTHORIZED_ERROR = "401";
	/** 没有数据 404 */
	public static final String CODE_NOT_FOUND = "404";
	/** 服务器出错 500 */
	public static final String CODE_SERVER_ERROR = "500";
	/**用户信息出错了*/
	public static final String CODE_USER_ERROR = "402";//
	
	public final static String PARAMS_EMPTY_MSG = "必要参数为空";
	public final static String PARAMS_NVOICE__HEAD_NOT_MSG = "请填写发票抬头";
	public final static String PRODUCT_NUM_LESS = "商品剩余数量不足";
	public final static String TIPS_BLACKLIST = "账户异常，请联系管理员";
	public final static String TIPS_FEEDBACK = "感谢您的反馈";
	public final static String TIPS_SERVER_ERROR = "出错了";
	public final static String TIPS_DEL_COLLECT = "取消收藏";
	public final static String TIPS_DEL_SUCCESS = "已删除";
	public final static String TIPS_COLLECT_SUCCESS = "恭喜您，收藏成功！";
	public final static String TIPS_NOT_DATA = "不是您的信息";
	public final static String TIPS_ORDER_SUBMIT_SUCC = "订单提交成功";
	public final static String TIPS_DATA_NULL = "没有更多了";// 分页404使用
	public final static String TIPS_DATA_NOT = "没有数据";// 404使用
	public final static String TIPS_MODIFY = "已修改";
	public final static String TIPS_SAVE = "已保存";
	public final static String TIPS_OPERATION_SUCC="操作成功";
	public final static String TIPS_OPERATION_ERROR="操作失败";
	
	public final static String FILE_TIP_NOT_FILE = "未发现上传文件";
	public final static String VER_NEWEST = "已经是最新版本了";
	public final static String VER_NEW = "发现新版本";

	public final static String ORDER_COMM_NOT = "订单不能评价";
	public final static String ORDER_CLOSE_NOT = "订单不能关闭";
	public final static String ORDER_COMM_CHONGFU = "不能重复评价";
	public final static String ORDER_COMM_SUCC = "评价成功";

	public final static String LOGIN_TIPS_NOT = "用户名密码错误";
	public final static String USER_TIPS_REG_NOT_PWD = "两次输入密码不同";
	public final static String USER_TIPS_UN_EXISTS = "手机号已存在！";
	public final static String USER_TIPS_REG_SUCCESS = "注册成功！";
	public final static String USER_TIPS_CODE_ERROR = "验证码不正确";
	public final static String USER_TIPS_LOGOUT = "已退出";
	public final static String USER_TIPS_BINDING_SUCCESS = "绑定成功";

	public final static String TIPS_NOT_USER = "用户不存在";
	public final static String MOBILE_NOT = "请输入正确的手机号";

	public final static String PAY_TIPS_SELLER_NOTDATE = "您选择的商品不在营业时间";

	public final static String SESSION_SMS_PHONE = "smsPhone";
	public final static String SESSION_CHECK_CODE= "chkCode";
	public final static String SESSION_SMS_CODE_NAME = "sdCode";
	public final static String SMS_REG_TEXT = "【可乐】{code}（动态验证码），工作人员不会向您索要，请勿向任何人泄露";
//	public final static String SMS_REG_SUCC = "【可乐】尊敬的{name}，恭喜您成为会员。账号：{**}";

	public final static String COUPONS_EXPIRE = "优惠券已过期";
	public final static String TIPS_PRODUCT_NOT_COUPONS = "购买的商品不能使用优惠券";
	public final static String TIPS_PRODUCT_NOT_SELLER_COUPONS = " 不是此商家的优惠券";

	public final static String TIPS_YUE_NOT = "余额不够，请选择其他支付方式";
	public final static String TIPS_POINT_USER_SUCCESS="积分不足！";
	
	
	public final static String AWARD_SHAKE_NEED_LOGIN_TIPS = "参加摇一摇活动，请先登录！";
	
	public final static String MYT_USER_REPORT_COUNT = "一天举报次数最多为3次";
	
	//card充值模块
	public final static String CARD_INVALIDE_CARD = "无效卡号！";
	public final static String CARD_BEEN_ACTIVED = "卡号已激活！";
	public final static String CARD_BEEN_DEADLINE = "卡号已过期！";
	public final static String CARD_RECHARGE_SUCCESS = "充值成功！";
	
	//推送提示消息
	/**新接收到派单信息**/
	public final static String PUSH_TIPS_TASK = "新派单信息";
	/**新订单**/
	public final static String PUSH_TIPS_NEW_ORDER = "新订单";
	/**订单已发货**/
	public final static String PUSH_TIPS_ORDER_SEND = "订单已发货";
	/**订单已收货**/
	public final static String PUSH_TIPS_ORDER_ACCEPT = "订单已收货";
	/**订单已消费**/
	public final static String PUSH_TIPS_ORDER_USE = "订单已消费";
	/**订单已取消**/
	public final static String PUSH_TIPS_ORDER_CANCEL = "订单已取消";
	
	/**订单号前缀**/
	public final static String PUSH_TIPS_ORDER_PRE = "订单号：";
	
	public final static String PUSH_TIPS_ACCOUNT_LOGIN = "帐号异地登录";
	public final static String PUSH_TIPS_SIGN_OUT = "帐号在另一设备登录，时间：{date}";
	
	//大转盘
	public final static String TIPS_FAIL_REPORT_TIME="下一轮抽奖时间未到，请耐心等待！";
	public final static String TIPS_WIN_REPORT_SUCCESS="恭喜您，中奖了，请尽快与商家取得联系！";
	public final static String TIPS_FAIL_REPORT_SUCCESS="很遗憾，没有中奖，下次继续努力！";
	public final static String TIPS_FAIL_POINTS="积分不足，请及时充值！";
	public final static String TIPS_FAIL_CHEAK="校验码错误";
	public final static String TIPS_FAIL_REPORT_LOT="本时间段没有抽奖活动";
	

	/** 支付方式-支付宝 **/
	public final static String PAY_TYPE_ALIPAY = "1";
	public final static String PAY_TYPE_ALIPAY_ALIAS = "支付宝";
	/** 支付方式-银联 **/
	public final static String PAY_TYPE_UNIONPAY = "2";
	public final static String PAY_TYPE_UNIONPAY_ALIAS = "银联";
	/** 支付方式-货到付款 **/
	public final static String PAY_TYPE_NOT_PAY = "3";
	public final static String PAY_TYPE_NOT_PAY_ALIAS = "货到付款";
	/** 支付方式-微信 **/
	public final static String PAY_TYPE_WECHAT = "5";
	public final static String PAY_TYPE_WECHAT_ALIAS = "微信";
	/** 支付方式-微信JS支付 **/
	public final static String PAY_TYPE_WECHAT_JS = "6";
	public final static String PAY_TYPE_WECHAT_JS_ALIAS = "微信";
	/** 支付方式-积分支付 **/
	public final static String PAY_TYPE_POINTS = "7";
	public final static String PAY_TYPE_POINTS_ALIAS = "积分";

	/** 余额支付 */
	public final static String PAY_YUE = "1";
	public final static String PAY_YUE_ALIAS = "余额支付";
	/** 在线支付 */
	public final static String PAY_LINE = "2";
	public final static String PAY_LINE_ALIAS = "在线支付";
	/** 货到付款 */
	public final static String PAY_NOT_PAY = "3";
	public final static String PAY_NOT_PAY_ALIAS = "货到付款";

	
}
