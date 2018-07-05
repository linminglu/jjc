package help.pay.lingdian.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by dell on 2015/1/13.
 */
@SuppressWarnings({ "deprecation", "resource" })
public class HttpClientUtil {

	/**
	 * 封装HTTP POST方法
	 * 
	 * @param
	 * @param
	 * @return
	 * @throws ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static String post(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> formparams = setHttpParams(paramMap);
		UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, "UTF-8");
		httpPost.setEntity(param);
		HttpResponse response = httpClient.execute(httpPost);
		String httpEntityContent = getHttpEntityContent(response);
		httpPost.abort();
		return httpEntityContent;
	}

	/**
	 * 封装HTTP POST方法
	 * 
	 * @param
	 * @param （如JSON串）
	 * @return
	 * @throws ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static String post(String url, String data) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "text/json; charset=utf-8");
		httpPost.setEntity(new StringEntity(URLEncoder.encode(data, "UTF-8")));
		HttpResponse response = httpClient.execute(httpPost);
		String httpEntityContent = getHttpEntityContent(response);
		httpPost.abort();
		return httpEntityContent;
	}

	public static String sendHttpsMsg(String url, String json) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		SSLContext ctx = SSLContext.getInstance("SSL");

		X509TrustManager tm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		/*
		 * KeyStore trustStore = KeyStore.getInstance("JKS"); FileInputStream
		 * instream = new FileInputStream(new File("D:/ykm.cer")); try {
		 * trustStore.load(instream, "123456".toCharArray()); } finally {
		 * instream.close(); }
		 */
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx);
		ClientConnectionManager ccm = httpClient.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 8800, ssf));

		HttpPost httpost = new HttpPost(url);
		httpost.setEntity(new StringEntity(json, HTTP.UTF_8));
		httpost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		HttpResponse response = httpClient.execute(httpost);
		if (response.getStatusLine().getStatusCode() == 200) {
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer();
			String line;
			while (null != (line = br.readLine())) {
				sb.append(line).append("\n");
			}
			br.close();
			if (sb.toString() != null && sb.toString() != "") {
				System.out.println("StatusCode == " + response.getStatusLine().getStatusCode());
				return sb.toString();
			} else {
				System.out.println("StatusCode == " + response.getStatusLine().getStatusCode());
				return "未返回任何数�?";
			}
		} else {
			System.out.println("StatusCode == " + response.getStatusLine().getStatusCode());
			return "请求出错";
		}
	}

	/**
	 * 封装HTTP GET方法
	 * 
	 * @param
	 * @return
	 * @throws ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static String get(String url) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet();
		httpGet.setURI(URI.create(url));
		HttpResponse response = httpClient.execute(httpGet);
		String httpEntityContent = getHttpEntityContent(response);
		httpGet.abort();
		return httpEntityContent;
	}

	/**
	 * 封装HTTP GET方法
	 * 
	 * @param
	 * @param
	 * @return
	 * @throws ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static String get(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet();
		List<NameValuePair> formparams = setHttpParams(paramMap);
		String param = URLEncodedUtils.format(formparams, "UTF-8");
		httpGet.setURI(URI.create(url + "?" + param));
		HttpResponse response = httpClient.execute(httpGet);
		String httpEntityContent = getHttpEntityContent(response);
		httpGet.abort();
		return httpEntityContent;
	}

	/**
	 * 封装HTTP PUT方法
	 * 
	 * @param
	 * @param
	 * @return
	 * @throws ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static String put(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(url);
		List<NameValuePair> formparams = setHttpParams(paramMap);
		UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, "UTF-8");
		httpPut.setEntity(param);
		HttpResponse response = httpClient.execute(httpPut);
		String httpEntityContent = getHttpEntityContent(response);
		httpPut.abort();
		return httpEntityContent;
	}

	/**
	 * 封装HTTP DELETE方法
	 * 
	 * @param
	 * @return
	 * @throws ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static String delete(String url) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete();
		httpDelete.setURI(URI.create(url));
		HttpResponse response = httpClient.execute(httpDelete);
		String httpEntityContent = getHttpEntityContent(response);
		httpDelete.abort();
		return httpEntityContent;
	}

	/**
	 * 封装HTTP DELETE方法
	 * 
	 * @param
	 * @param
	 * @return
	 * @throws ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static String delete(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete();
		List<NameValuePair> formparams = setHttpParams(paramMap);
		String param = URLEncodedUtils.format(formparams, "UTF-8");
		httpDelete.setURI(URI.create(url + "?" + param));
		HttpResponse response = httpClient.execute(httpDelete);
		String httpEntityContent = getHttpEntityContent(response);
		httpDelete.abort();
		return httpEntityContent;
	}

	/**
	 * 设置请求参数
	 * 
	 * @param
	 * @return
	 */
	private static List<NameValuePair> setHttpParams(Map<String, String> paramMap) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		Set<Map.Entry<String, String>> set = paramMap.entrySet();
		for (Map.Entry<String, String> entry : set) {
			formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return formparams;
	}

	/**
	 * 获得响应HTTP实体内容
	 * 
	 * @param response
	 * @return
	 * @throws java.io.IOException
	 * @throws java.io.UnsupportedEncodingException
	 */
	private static String getHttpEntityContent(HttpResponse response) throws IOException, UnsupportedEncodingException {
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream is = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = br.readLine();
			StringBuilder sb = new StringBuilder();
			while (line != null) {
				sb.append(line + "\n");
				line = br.readLine();
			}
			return sb.toString();
		}
		return "";
	}

	public static String sendHttpMsg(String reqUrl, String reqMethod, String reqMsg) {
		StringBuffer buffer = new StringBuffer();
		HttpURLConnection connection = null;
		try {
			System.out.println("reqUrl:"+reqUrl);
			URL url = new URL(reqUrl);
			connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod(reqMethod);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			connection.setRequestProperty("Accept-Language", "zh-CN");
			connection.setRequestProperty("Accept-Charset", "UTF-8");

			if (reqMsg != null && !reqMsg.trim().equals("")) {
				connection.getOutputStream().write(reqMsg.getBytes());
				connection.getOutputStream().flush();
				connection.getOutputStream().close();
			}
			connection.connect();

			int statusCode = connection.getResponseCode();
			if (statusCode == 200) {
				InputStreamReader in = new InputStreamReader(connection.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String line;
				while (null != (line = br.readLine())) {
					buffer.append(line);
				}
				in.close();
				br.close();
			} else {
				new RuntimeException("出错�?... " + "[" + statusCode + "]");
				return null;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return buffer.toString();
	}

}
