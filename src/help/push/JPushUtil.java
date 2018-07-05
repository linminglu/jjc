package help.push;

import help.base.APIConstants;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apps.Constants;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class JPushUtil {
	protected static final Logger LOG = LoggerFactory
			.getLogger(JPushUtil.class);
	protected static final String APP_KEY = Constants.JPUSH_APP_KEY;
	protected static final String MASTER_SECRET = Constants.JPUSH_MASTER_SECRET;

	/** 普通通知 **/
	public static String TYPE_MSG = "1";
	/** 网页 **/
	public static String TYPE_WEB = "2";
	/** 新闻评论 **/
	public static String TYPE_NEWS_COMMENT = "3";
	/** 商家订单详情 **/
	public static String TYPE_POST_COMMENT = "4";
	/** 用户订单详情 **/
	public static String TYPE_POST_VIEW = "5";
	/** 用户帐号重复登录 **/
	public static String TYPE_SIGN_OUT = "9";

	/** 商家发布新商品 **/
	public static String TYPE_NEW_PRODUCT = "10";

	public static void main(String[] args) {
		// 1.普通通知
		// PushBean pushBean = new PushBean(TYPE_MSG, "普通通知", "通知内容");
		// androidAllPush(pushBean);
		// 2.web
		// PushBean pushBean2 = new PushBean(TYPE_WEB, "闻页通知", "通知内容",
		// "https://www.baidu.com/");
		// androidAllPush(pushBean2);
		// 3.帖子正文
		// PushBean pushBean3 = new PushBean(TYPE_POST_VIEW, "帖子正文",
		// "通知内容","1",null);
		// androidAllPush(pushBean3);
		PushBean pushBean4 = new PushBean();
		pushBean4.setType(JPushUtil.TYPE_POST_COMMENT);
		pushBean4.setTitle(APIConstants.PUSH_TIPS_NEW_ORDER);
		pushBean4
				.setDescription(APIConstants.PUSH_TIPS_ORDER_PRE + "123123123");
		pushBean4.setId("212412463432423");
		pushBean4.setSecondaryId(null);
		String[] tokenArray = new String[1];
		String token = "171976fa8ab032ee05e";
		tokenArray[0] = token;

		pushOneDevice(pushBean4, "3", tokenArray);
	}

	/**
	 * 根据不同的设备类型推送消息
	 * 
	 * @param pushBean
	 *            推送实体
	 * @param deviceType
	 *            推送消息接收设备类型 1.安卓+苹果 2.安卓 3.苹果
	 */
	public static void push(PushBean pushBean, String deviceType) {
		if (deviceType.equals("1")) {// 全部
			all(pushBean);
		} else if (deviceType.equals("2")) {// android
			androidAllPush(pushBean);
		} else if (deviceType.equals("3")) {// IOS
			iosAllPush(pushBean);
		}
	}

	/**
	 * 全部设备的推送
	 */
	public static void all(PushBean pushBean) {
		iosAllPush(pushBean);
		androidAllPush(pushBean);
	}

	/**
	 * IOS设备推送
	 * 
	 * @param pushBean
	 *            推送消息类
	 */
	public static void iosAllPush(PushBean pushBean) {
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		PushPayload payload = null;
		Map<String, String> extras = new HashMap<String, String>();
		Builder builder = PushPayload.newBuilder().setPlatform(Platform.ios());
		Notification notification = null;
		// 通知
		String alert = pushBean.getDescription();
		String url = pushBean.getUrl();
		String id = pushBean.getId();
		String secondaryId = pushBean.getSecondaryId();

		String type = pushBean.getType();
		extras.put("type", type);
		if (TYPE_MSG.equals(type)) {// 普通通知
			notification = Notification.ios(alert, null);
		} else if (TYPE_WEB.equals(type)) {// url
			extras.put("url", url);
			notification = Notification.ios(alert, extras);
		} else if (TYPE_NEWS_COMMENT.equals(type)
				|| TYPE_POST_COMMENT.equals(type)) {
			extras.put("id", id);
			extras.put("secondaryId", secondaryId);
			notification = Notification.ios(alert, extras);
		} else if (TYPE_POST_VIEW.equals(type)) {
			extras.put("id", id);
			notification = Notification.ios(alert, extras);
		}
		builder.setAudience(Audience.all());
		builder.setNotification(notification);// 设置通知
		payload = builder.build();
		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
		} catch (APIRequestException e) {
		}
	}

	/**
	 * android设备推送
	 * 
	 * @param pushBean
	 *            推送消息类
	 */
	public static void androidAllPush(PushBean pushBean) {
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		PushPayload payload = null;
		Map<String, String> extras = new HashMap<String, String>();
		Builder builder = PushPayload.newBuilder();
		builder.setPlatform(Platform.android());

		Notification notification = null;
		// 通知
		String title = pushBean.getTitle();
		String alert = pushBean.getDescription();
		String url = pushBean.getUrl();
		String id = pushBean.getId();
		String secondaryId = pushBean.getSecondaryId();

		String type = pushBean.getType();
		extras.put("type", type);

		if (TYPE_MSG.equals(type)) {// 普通通知
			notification = Notification.android(alert, title, null);
		} else if (TYPE_WEB.equals(type)) {// url
			extras.put("url", url);
			notification = Notification.android(alert, title, extras);
		} else if (TYPE_NEWS_COMMENT.equals(type)
				|| TYPE_POST_COMMENT.equals(type)) {
			extras.put("id", id);
			extras.put("secondaryId", secondaryId);
			notification = Notification.android(alert, title, extras);
		} else if (TYPE_POST_VIEW.equals(type)) {
			extras.put("id", id);
			notification = Notification.android(alert, title, extras);
		}
		builder.setAudience(Audience.all());
		builder.setNotification(notification);// 设置通知
		payload = builder.build();
		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
		} catch (APIRequestException e) {
		}
	}

	/**
	 * 安卓单设备推送
	 * 
	 * @param pushBean
	 * @param token
	 *            设备码数组
	 * @param user
	 *            用户可选
	 */
	public static void androidPush(PushBean pushBean, String[] token) {
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		PushPayload payload = null;
		Map<String, String> extras = new HashMap<String, String>();
		Builder builder = PushPayload.newBuilder().setPlatform(
				Platform.android());

		Notification notification = null;
		// 通知
		String title = pushBean.getTitle();
		String alert = pushBean.getDescription();
		String url = pushBean.getUrl();
		String id = pushBean.getId();
		String secondaryId = pushBean.getSecondaryId();

		String type = pushBean.getType();
		extras.put("type", type);
		if (TYPE_MSG.equals(type)) {// 普通通知
			notification = Notification.android(alert, title, null);
		} else if (TYPE_WEB.equals(type)) {// url
			extras.put("url", url);
			notification = Notification.android(alert, title, extras);
		} else if (TYPE_NEWS_COMMENT.equals(type)
				|| TYPE_POST_COMMENT.equals(type)) {
			extras.put("id", id);
			extras.put("secondaryId", secondaryId);
			notification = Notification.android(alert, title, extras);
		} else if (TYPE_POST_VIEW.equals(type)) {
			extras.put("id", id);
			notification = Notification.android(alert, title, extras);
		}

		Audience audience = Audience.registrationId(token);
		builder.setAudience(audience);
		builder.setNotification(notification);// 设置通知
		payload = builder.build();
		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
		} catch (APIRequestException e) {
		}
	}

	/**
	 * IOS单设备推送
	 * 
	 * @param pushBean
	 *            推送消息类
	 */
	public static void iosAllPush(PushBean pushBean, String[] token) {
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		PushPayload payload = null;
		Map<String, String> extras = new HashMap<String, String>();
		Builder builder = PushPayload.newBuilder().setPlatform(Platform.ios());
		Notification notification = null;
		// 通知
		String alert = pushBean.getDescription();
		String url = pushBean.getUrl();
		String id = pushBean.getId();
		String secondaryId = pushBean.getSecondaryId();

		String type = pushBean.getType();
		extras.put("type", type);
		if (TYPE_MSG.equals(type)) {// 普通通知
			notification = Notification.ios(alert, null);
		} else if (TYPE_WEB.equals(type)) {// url
			extras.put("url", url);
			notification = Notification.ios(alert, extras);
		} else if (TYPE_NEWS_COMMENT.equals(type)
				|| TYPE_POST_COMMENT.equals(type)) {
			extras.put("id", id);
			extras.put("secondaryId", secondaryId);
			notification = Notification.ios(alert, extras);
		} else if (TYPE_POST_VIEW.equals(type)) {
			extras.put("id", id);
			notification = Notification.ios(alert, extras);
		}
		Audience audience = Audience.registrationId(token);
		builder.setAudience(audience);
		builder.setNotification(notification);// 设置通知
		payload = builder.build();
		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
		} catch (APIRequestException e) {
		}
	}

	/**
	 * 检查用户是否需要推送
	 * 
	 * @param user
	 * @return true 可以推送 false不接受推送
	 */
	// public static boolean chkPush(User user) {
	// String isDontDisturb = user.getIsDontDisturb();// 免打扰
	// if (ParamUtils.chkString(isDontDisturb)) {
	// if (Constants.PUB_STATUS_OPEN.equals(isDontDisturb)) {// 打开免打扰
	// String pattern = "HH:mm";
	// String time = DateTimeUtil.getDateTime(pattern);
	// return DateTimeUtil.isInTime("23:00-7:00", time);
	// } else {
	// return true;
	// }
	// } else {
	// return true;
	// }
	// }

	/**
	 * 根据不同的设备类型推送单个消息
	 * 
	 * @param pushBean
	 *            推送实体
	 * @param deviceType
	 *            推送消息接收设备类型 1.苹果 2.安卓 3.全部
	 */
	public static void pushOneDevice(PushBean pushBean, String deviceType,
			String[] token) {
		if (deviceType.equals("1")) {// IOS
			iosAllPush(pushBean, token);
		} else if (deviceType.equals("2")) {// android
			androidPush(pushBean, token);
		} else if (deviceType.equals("3")) {// 全部
			all(pushBean);
		}
	}

	/**
	 * 推送标签组
	 * 
	 * @param pushBean
	 * @param tagValue
	 */
	public static void tagAll(PushBean pushBean, String[] tagValue) {
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		PushPayload payload = null;
		Map<String, String> extras = new HashMap<String, String>();
		Builder builder = PushPayload.newBuilder().setPlatform(Platform.all());
		Notification notification = null;
		// 通知
		String alert = pushBean.getDescription();
		String url = pushBean.getUrl();
		String id = pushBean.getId();
		String secondaryId = pushBean.getSecondaryId();

		String type = pushBean.getType();
		extras.put("type", type);
		if (TYPE_MSG.equals(type)) {// 普通通知
			notification = Notification.ios(alert, null);
		} else if (TYPE_WEB.equals(type)) {// url
			extras.put("url", url);
			notification = Notification.ios(alert, extras);
		} else if (TYPE_NEWS_COMMENT.equals(type)
				|| TYPE_POST_COMMENT.equals(type)) {
			extras.put("id", id);
			extras.put("secondaryId", secondaryId);
			notification = Notification.ios(alert, extras);
		} else if (TYPE_POST_VIEW.equals(type)) {
			extras.put("id", id);
			notification = Notification.ios(alert, extras);
		}
		builder.setAudience(Audience.tag(tagValue));
		builder.setNotification(notification);// 设置通知
		payload = builder.build();
		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
		} catch (APIRequestException e) {
		}
	}
	
}
