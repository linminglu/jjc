package help.sms;

import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;

import com.apps.Constants;
import com.framework.util.ParamUtils;

public class SmsSendUtil {

	// SMS
	public static String SMS_SEND_URL = "http://api.sms.cn/sms/";// utf-8编码时调用
	public static String smsUid =  Constants.getSmsUid();// 用户名
	public static String smsPassword = Constants.getSmsPassword();// 密码

	public static boolean sendSms(String msisdn, String smsContent, String t) {
		boolean isSuccess = false;
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(SMS_SEND_URL);
		post.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded;charset=utf-8");
		NameValuePair ac = new NameValuePair("ac", "send");
		NameValuePair userid = new NameValuePair("uid", smsUid);
		NameValuePair password = new NameValuePair("pwd", smsPassword);
		NameValuePair mobile = new NameValuePair("mobile", msisdn);
		NameValuePair content = new NameValuePair("content",
				URLEncoder.encode(smsContent));
		if (ParamUtils.chkString(t)) {
			NameValuePair template = new NameValuePair("template", t);
			post.setRequestBody(new NameValuePair[] { ac, userid, password,
					mobile, template, content });
		} else {
			post.setRequestBody(new NameValuePair[] { ac, userid, password,
					mobile, content });
		}

		try {
			// 发送短信
			int status = client.executeMethod(post);
			System.out.println("___sendSms.status=" + status + ",mobile="
					+ msisdn + ",content=" + smsContent);
			// 打出返回日志
			String str = post.getResponseBodyAsString();
			if (status == 200) {
				JSONObject obj = new JSONObject(str);
				String returnstatus = obj.getString("stat");
				if (returnstatus.equals("100")) {
					isSuccess = true;
				}
			}

		} catch (Exception e) {
			System.out.println("___sendSms.error=" + e);
		}
		return isSuccess;
	}

	public static class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			return "UTF-8";
		}
	}

	public static void main(String[] args) {
		String SMS_REG_TEXT = "【大地科技】111111（动态验证码），工作人员不会向您索要，请勿向任何人泄露";
		
		sendSms("13311286661", SMS_REG_TEXT, null);
	}

}
