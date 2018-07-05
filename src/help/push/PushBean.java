package help.push;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 推送实体
 * 
 * @author Mr.zang
 */
public class PushBean {
	public String type;// 消息类型1.普通消息2.网页3.新闻评论4.商家订单详情5. 用户订单详情
	public String url;// 网页
	public String id;// 数据id
	public String secondaryId;// 次要数据id

	public String title;// 标题
	public String description;// 描述

	public PushBean() {
	}

	/**
	 * 
	 * @param type
	 *            消息类型
	 * @param title
	 *            标题
	 * @param description
	 *            正文
	 * @param id
	 *            数据id
	 * @param secondaryId
	 *            次级id
	 */
	public PushBean(String type, String title, String description, String id,
			String secondaryId) {
		this.type = type;
		this.title = title;
		this.description = description;
		this.id = id;
		this.secondaryId = secondaryId;

	}

	/**
	 * 普通通知
	 * 
	 * @param type
	 * @param title
	 *            标题
	 * @param description
	 *            描述
	 */
	public PushBean(String type, String title, String description) {
		this.type = type;
		this.title = title;
		this.description = description;
	}

	/**
	 * 打开网页
	 * 
	 * @param type
	 * @param title
	 *            标题
	 * @param description
	 *            描述
	 * @param link
	 *            网址
	 */
	public PushBean(String type, String title, String description, String url) {
		this.type = type;
		this.title = title;
		this.description = description;
		this.url = url;
	}

	public String getSecondaryId() {
		return secondaryId;
	}

	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static final String getDebugJSON(PushBean pushBean,
			String machineType, String token) throws JSONException {
		return new JSONObject().put("type", pushBean.getType())
				.put("title", pushBean.getTitle())
				.put("content", pushBean.getDescription())
				.put("id", pushBean.getId()).put("machineType", machineType)
				.put("token", token).toString();
	}
}
