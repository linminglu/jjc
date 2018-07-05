//package help.push;
//
//import net.sf.json.JSONObject;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.apps.Constants;
//import com.baidu.yun.core.log.YunLogEvent;
//import com.baidu.yun.core.log.YunLogHandler;
//import com.baidu.yun.push.auth.PushKeyPair;
//import com.baidu.yun.push.client.BaiduPushClient;
//import com.baidu.yun.push.constants.BaiduPushConstants;
//import com.baidu.yun.push.exception.PushClientException;
//import com.baidu.yun.push.exception.PushServerException;
//import com.baidu.yun.push.model.PushMsgToAllRequest;
//import com.baidu.yun.push.model.PushMsgToAllResponse;
//import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
//import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
//import com.framework.util.ParamUtils;
//import com.framework.util.StringUtil;
//
///**
// * 推送工具类-商家版
// * @author zang
// * 
// */
//public class BaiduPushUtilSeller {
//	protected static final Logger LOG = LoggerFactory
//			.getLogger(BaiduPushUtilSeller.class);
//	
//	/** 安卓 */
//	public static String APIKEY_ANDROID = Constants.BAIDU_PUSH_APIKEY_ANDROID_SEND;
//	public static String SECRETKEY_ANDROID = Constants.BAIDU_PUSH_SECRETKEY_ANDROID_SEND;
//	
//	/** IOS*/
//	public static String APIKEY_IOS = Constants.IOS_PUBLISH_MODE.equals(Constants.IOS_PUBLISH_MODE_INHOUSE)?Constants.BAIDU_PUSH_APIKEY_IOS_SEND_INHOUSE:Constants.BAIDU_PUSH_APIKEY_IOS_SEND_APPSTORE;
//	public static String SECRETKEY_IOS = Constants.IOS_PUBLISH_MODE.equals(Constants.IOS_PUBLISH_MODE_INHOUSE)?Constants.BAIDU_PUSH_SECRETKEY_IOS_SEND_INHOUSE:Constants.BAIDU_PUSH_SECRETKEY_IOS_SEND_APPSTORE;
//	
//	/** 普通通知 **/
//	public static String TYPE_MSG = "1";
//	/** 网页 **/
//	public static String TYPE_WEB = "2";
//	/** 商家主页 **/
//	public static String TYPE_SELLER = "3";
//	/** 商家订单详情 **/
//	public static String TYPE_SELLER_ORDER_VIEW = "4";
//	/** 用户订单详情 **/
//	public static String TYPE_USER_ORDER_VIEW = "5";
//
//	public static void main(String[] args) {
//		//5723844623464862359 5c
//		//4278653909176945730 motox
//		//String token = "5143889161481988847";
//		String token = "3785079496256291265";
//		
//		// 1.普通通知
//		PushBean pushBean = new PushBean(TYPE_MSG, "普通通知", "通知内容");
//		// androidAllPush(pushBean);
//		// iosAllPush(pushBean);
//		// 2.网页通知
//		PushBean pushBean2 = new PushBean(TYPE_WEB, "网页通知", "通知内容",
//				"https://www.baidu.com/");
//		// androidAllPush(pushBean2);
//		// 3.商家主页
//		PushBean pushBean3 = new PushBean(TYPE_SELLER, "商家主页", "通知内容",Constants.MODULE_EAT, 1);
//		// all(pushBean3);
//		// 4.商家订单详情
//		PushBean pushBean4 = new PushBean(TYPE_SELLER_ORDER_VIEW, "商家订单详情",
//				"通知内容"+StringUtil.getRandomInt(999),Constants.MODULE_STORE, 139);
//		// iosPush(pushBean4, token);
//		// 5.用户订单详情
//		PushBean pushBean5 = new PushBean(TYPE_USER_ORDER_VIEW, "用户订单详情",
//				"通知内容"+StringUtil.getRandomInt(999),Constants.MODULE_STORE, 139);
//		
//		PushBean pushBean6 = new PushBean(TYPE_USER_ORDER_VIEW, "用户订单详情",
//				"通知内容",Constants.MODULE_GROUP, 76);
//		
//		// iosAllPush(pushBean2);
//		//androidPush(pushBean2, token);
//		
//		androidPush(pushBean4, token);
//		//iosPush(pushBean4, token);
//		
//		// androidPush(pushBean4, token);
//		// androidPush(pushBean5, token);
//		// androidAllPush(pushBean3);
//		// androidAllPush(pushBean4);
//		// androidAllPush(pushBean5);
//		// push(pushBean4, "2", token);
//		// iosAllPush(pushBean4);
//		// iosAllPush(pushBean5);
//	}
//
//	/**
//	 * 根据不同的设备类型推送消息
//	 * 
//	 * @param pushBean
//	 *            推送实体
//	 * @param type
//	 *            推送消息接收设备类型 1.安卓+苹果 2.安卓 3.苹果
//	 */
//	public static void push(PushBean pushBean, String type) {
//		LOG.info("___[push info]____"+PushBean.getDebugJSON(pushBean, type, ""));
//		if (type.equals("1")) {// 全部
//			all(pushBean);
//		} else if (type.equals("2")) {// android
//			androidAllPush(pushBean);
//		} else if (type.equals("3")) {// IOS
//			iosAllPush(pushBean);
//		}
//	}
//
//	/**
//	 * 单设备推送
//	 * 
//	 * @param pushBean
//	 *            推送实体
//	 * @param type
//	 *            设备类型 1.IOS 2.安卓
//	 * @param token
//	 *            设备码
//	 */
//	public static void push(PushBean pushBean, String type, String token) {
//		LOG.info("___[push info]____"+PushBean.getDebugJSON(pushBean, type, token));
//		if (ParamUtils.chkString(token)) {
//			if (type.equals("2")) {// 安卓
//				androidPush(pushBean, token);
//			} else if (type.equals("1")) {// ios
//				iosPush(pushBean, token);
//			}
//		}
//	}
//
//	/**
//	 * 全部设备的推送
//	 */
//	public static void all(PushBean pushBean) {
//		iosAllPush(pushBean);
//		androidAllPush(pushBean);
//	}
//
//	/**
//	 * IOS单设备推送
//	 * 
//	 * @param pushBean
//	 *            推送消息类
//	 * @param token
//	 *            设备码，百度推送的ChannelId
//	 */
//	public static void iosPush(PushBean pushBean, String token) {
//		PushKeyPair pair = new PushKeyPair(APIKEY_IOS, SECRETKEY_IOS);
//
//		// 2. 创建BaiduPushClient，访问SDK接口
//		BaiduPushClient pushClient = new BaiduPushClient(pair,
//				BaiduPushConstants.CHANNEL_REST_URL);
//
//		// 3. 注册YunLogHandler，获取本次请求的交互信息
//		pushClient.setChannelLogHandler(new YunLogHandler() {
//			@Override
//			public void onHandle(YunLogEvent event) {
//				LOG.info(event.getMessage());
//			}
//		});
//		try {
//
//			JSONObject notification = new JSONObject();
//			JSONObject jsonAPS = new JSONObject();
//			jsonAPS.put("alert", pushBean.getDescription());
//			// jsonAPS.put("badge", 0);//角标
//			jsonAPS.put("sound", "ttt"); // 设置通知铃声样式,例如"ttt"，用户自定义。
//			notification.put("aps", jsonAPS);
//			notification.put("type", pushBean.getType());
//			notification.put("module", pushBean.getModule());
//			notification.put("url", pushBean.getUrl());
//			notification.put("id", pushBean.getId());
//
//			// 4. 设置请求参数，创建请求实例
//			PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest();
//			request.addChannelId(token);
//			// 设置消息的有效时间,单位秒,默认3600*5.
//			request.addMsgExpires(new Integer(3600));
//			// 设置消息类型,0表示透传消息,1表示通知,默认为0.
//			request.addMessageType(1);
//			// IOS,DeployStatus 1: Developer2:Production.
//			request.addDeployStatus(Integer.valueOf(Constants.IOS_DEPLOY_MODE));
//			request.addMessage(notification.toString());
//			// 设置设备类型，deviceType => 1.web, 2. pc,3.android, 4. ios, 5 .wp.
//			request.addDeviceType(4);
//			// 5. 执行Http请求
//			PushMsgToSingleDeviceResponse response = pushClient
//					.pushMsgToSingleDevice(request);
//			// 6. Http请求返回值解析
//			LOG.info("msgId: " + response.getMsgId() + ",sendTime: "
//					+ response.getSendTime());
//		} catch (PushClientException e) {
//			// ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
//			// 'true' 表示抛出, 'false' 表示捕获。
//			if (BaiduPushConstants.ERROROPTTYPE) {
//				try {
//					throw e;
//				} catch (PushClientException e1) {
//					e1.printStackTrace();
//				}
//			} else {
//				e.printStackTrace();
//			}
//		} catch (PushServerException e) {
//			if (BaiduPushConstants.ERROROPTTYPE) {
//				try {
//					throw e;
//				} catch (PushServerException e1) {
//					e1.printStackTrace();
//				}
//			} else {
//				LOG.info(String.format(
//						"requestId: %d, errorCode: %d, errorMsg: %s",
//						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
//			}
//		}
//	}
//
//	/**
//	 * IOS设备推送
//	 * 
//	 * @param pushBean
//	 *            推送消息类
//	 */
//	public static void iosAllPush(PushBean pushBean) {
//		PushKeyPair pair = new PushKeyPair(APIKEY_IOS, SECRETKEY_IOS);
//
//		// 2. 创建BaiduPushClient，访问SDK接口
//		BaiduPushClient pushClient = new BaiduPushClient(pair,
//				BaiduPushConstants.CHANNEL_REST_URL);
//
//		// 3. 注册YunLogHandler，获取本次请求的交互信息
//		pushClient.setChannelLogHandler(new YunLogHandler() {
//			@Override
//			public void onHandle(YunLogEvent event) {
//				LOG.info(event.getMessage());
//			}
//		});
//		try {
//
//			JSONObject notification = new JSONObject();
//			JSONObject jsonAPS = new JSONObject();
//			jsonAPS.put("alert", pushBean.getDescription());
//			// jsonAPS.put("badge", 0);//角标
//			// jsonAPS.put("sound", "ttt"); // 设置通知铃声样式,例如"ttt"，用户自定义。
//			notification.put("aps", jsonAPS);
//			notification.put("type", pushBean.getType());
//			notification.put("module", pushBean.getModule());
//			notification.put("url", pushBean.getUrl());
//			notification.put("id", pushBean.getId());
//
//			String msg = notification.toString();
//			LOG.info(msg);
//
//			// 4. 设置请求参数，创建请求实例
//			PushMsgToAllRequest request = new PushMsgToAllRequest();
//			// 设置消息的有效时间,单位秒,默认3600*5.
//			request.addMsgExpires(new Integer(3600));
//			// 设置消息类型,0表示透传消息,1表示通知,默认为0.
//			request.addMessageType(1);
//			// IOS,DeployStatus 1: Developer2:Production.
//			request.addDepolyStatus(Integer.valueOf(Constants.IOS_DEPLOY_MODE));
//			request.addMessage(msg);
//			// 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例2分钟后推送
//			// request.addSendTime(System.currentTimeMillis() / 1000 + 120);
//			// 设置设备类型，deviceType => 1.web, 2. pc,3.android, 4. ios, 5 .wp.
//			request.addDeviceType(4);
//			// 5. 执行Http请求
//			PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
//			// 6. Http请求返回值解析
//			LOG.info("msgId: " + response.getMsgId() + ",sendTime: "
//					+ response.getSendTime());
//		} catch (PushClientException e) {
//			// ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
//			// 'true' 表示抛出, 'false' 表示捕获。
//			if (BaiduPushConstants.ERROROPTTYPE) {
//				try {
//					throw e;
//				} catch (PushClientException e1) {
//					e1.printStackTrace();
//				}
//			} else {
//				e.printStackTrace();
//			}
//		} catch (PushServerException e) {
//			if (BaiduPushConstants.ERROROPTTYPE) {
//				try {
//					throw e;
//				} catch (PushServerException e1) {
//					e1.printStackTrace();
//				}
//			} else {
//				LOG.info(String.format(
//						"requestId: %d, errorCode: %d, errorMsg: %s",
//						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
//			}
//		}
//	}
//
//	/**
//	 * android单设备推送
//	 * 
//	 * @param pushBean
//	 *            推送消息类
//	 * @param token
//	 *            设备码，百度推送的ChannelId
//	 */
//	public static void androidPush(PushBean pushBean, String token) {
//		PushKeyPair pair = new PushKeyPair(APIKEY_ANDROID, SECRETKEY_ANDROID);
//
//		// 2. 创建BaiduPushClient，访问SDK接口
//		BaiduPushClient pushClient = new BaiduPushClient(pair,
//				BaiduPushConstants.CHANNEL_REST_URL);
//
//		// 3. 注册YunLogHandler，获取本次请求的交互信息
//		pushClient.setChannelLogHandler(new YunLogHandler() {
//			@Override
//			public void onHandle(YunLogEvent event) {
//				LOG.info(event.getMessage());
//			}
//		});
//		try {
//
//			// 4. 设置请求参数，创建请求实例
//			PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest();
//			request.addChannelId(token);
//			// 设置消息的有效时间,单位秒,默认3600*5.
//			request.addMsgExpires(new Integer(3600));
//			// 设置消息类型,0表示透传消息,1表示通知,默认为0.
//			// 若要通知，
//			request.setMessageType(1);
//
//			// 创建Android通知
//			JSONObject notification = new JSONObject();
//			notification.put("title", pushBean.getTitle());
//			notification.put("description", pushBean.getDescription());
//			// notification.put("notification_builder_id", 0);
//			// notification.put("notification_basic_style", 4);
//			// notification.put("open_type", 1);
//			// notification.put("url", "http://push.baidu.com");
//			JSONObject jsonCustormCont = new JSONObject();
//			jsonCustormCont.put("type", pushBean.getType()); // 自定义内容，key-value
//			jsonCustormCont.put("module", pushBean.getModule());
//			jsonCustormCont.put("id", pushBean.getId());
//			String url = pushBean.getUrl();
//			if (ParamUtils.chkString(url)) {
//				jsonCustormCont.put("url", url);
//			} else {
//				jsonCustormCont.put("url", "");
//			}
//			notification.put("custom_content", jsonCustormCont);
//			String msg = notification.toString();
//			LOG.info(msg);
//			request.addMessage(msg);
//
//			// 设置设备类型，deviceType => 1.web, 2. pc,3.android, 4. ios, 5 .wp.
//			request.addDeviceType(3);
//			// 5. 执行Http请求
//			PushMsgToSingleDeviceResponse response = pushClient
//					.pushMsgToSingleDevice(request);
//			// 6. Http请求返回值解析
//			LOG.info("msgId: " + response.getMsgId() + ",sendTime: "
//					+ response.getSendTime());
//		} catch (PushClientException e) {
//			// ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
//			// 'true' 表示抛出, 'false' 表示捕获。
//			if (BaiduPushConstants.ERROROPTTYPE) {
//				try {
//					throw e;
//				} catch (PushClientException e1) {
//					e1.printStackTrace();
//				}
//			} else {
//				e.printStackTrace();
//			}
//		} catch (PushServerException e) {
//			if (BaiduPushConstants.ERROROPTTYPE) {
//				try {
//					throw e;
//				} catch (PushServerException e1) {
//					e1.printStackTrace();
//				}
//			} else {
//				LOG.info(String.format(
//						"requestId: %d, errorCode: %d, errorMsg: %s",
//						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
//			}
//		}
//	}
//
//	/**
//	 * android设备推送
//	 * 
//	 * @param pushBean
//	 *            推送消息类
//	 */
//	public static void androidAllPush(PushBean pushBean) {
//		PushKeyPair pair = new PushKeyPair(APIKEY_ANDROID, SECRETKEY_ANDROID);
//
//		// 2. 创建BaiduPushClient，访问SDK接口
//		BaiduPushClient pushClient = new BaiduPushClient(pair,
//				BaiduPushConstants.CHANNEL_REST_URL);
//
//		// 3. 注册YunLogHandler，获取本次请求的交互信息
//		pushClient.setChannelLogHandler(new YunLogHandler() {
//			@Override
//			public void onHandle(YunLogEvent event) {
//				LOG.info(event.getMessage());
//			}
//		});
//		try {
//			// 4. 设置请求参数，创建请求实例
//			PushMsgToAllRequest request = new PushMsgToAllRequest();
//			// 设置消息的有效时间,单位秒,默认3600*5.
//			request.addMsgExpires(new Integer(3600));
//			// 设置消息类型,0表示透传消息,1表示通知,默认为0.
//			request.addMessageType(1);
//			// 创建Android通知
//			JSONObject notification = new JSONObject();
//			notification.put("title", pushBean.getTitle());
//			notification.put("description", pushBean.getDescription());
//			// notification.put("notification_builder_id", 0);
//			// notification.put("notification_basic_style", 4);
//			// notification.put("open_type", 1);
//			// notification.put("url", "http://push.baidu.com");
//			JSONObject jsonCustormCont = new JSONObject();
//			jsonCustormCont.put("type", pushBean.getType()); // 自定义内容，key-value
//			jsonCustormCont.put("module", pushBean.getModule());
//			jsonCustormCont.put("id", pushBean.getId());
//			String url = pushBean.getUrl();
//			if (ParamUtils.chkString(url)) {
//				jsonCustormCont.put("url", url);
//			} else {
//				jsonCustormCont.put("url", "");
//			}
//			notification.put("custom_content", jsonCustormCont);
//
//			String msg = notification.toString();
//			LOG.info(msg);
//			request.addMessage(msg);
//			// 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例2分钟后推送
//			// request.addSendTime(System.currentTimeMillis() / 1000 + 120);
//			// 设置设备类型，deviceType => 1.web, 2. pc,3.android, 4. ios, 5 .wp.
//			request.addDeviceType(3);
//			// 5. 执行Http请求
//			PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
//			// 6. Http请求返回值解析
//			LOG.info("msgId: " + response.getMsgId() + ",sendTime: "
//					+ response.getSendTime());
//		} catch (PushClientException e) {
//			// ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
//			// 'true' 表示抛出, 'false' 表示捕获。
//			if (BaiduPushConstants.ERROROPTTYPE) {
//				try {
//					throw e;
//				} catch (PushClientException e1) {
//					e1.printStackTrace();
//				}
//			} else {
//				e.printStackTrace();
//			}
//		} catch (PushServerException e) {
//			if (BaiduPushConstants.ERROROPTTYPE) {
//				try {
//					throw e;
//				} catch (PushServerException e1) {
//					e1.printStackTrace();
//				}
//			} else {
//				LOG.info(String.format(
//						"requestId: %d, errorCode: %d, errorMsg: %s",
//						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
//			}
//		}
//	}
//}
