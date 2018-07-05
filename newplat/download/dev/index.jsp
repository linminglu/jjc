<%@page import="java.net.URLDecoder"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.Map"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.Writer"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%!//类处理方法

/**
 * 从网络Url中下载文件
 * @param urlStr
 * @param fileName
 * @param savePath
 * @throws IOException
 */
public static void  downloadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
	URL url = new URL(urlStr);  
	HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	//设置超时间为3秒
	conn.setConnectTimeout(3*1000);
	//防止屏蔽程序抓取而返回403错误
	conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

	//得到输入流
	InputStream inputStream = conn.getInputStream();  
	//获取自己数组
	byte[] getData = readInputStream(inputStream);    

	//文件保存位置
	File saveDir = new File(savePath);
	if(!saveDir.exists()){
		saveDir.mkdir();
	}
	File file = new File(saveDir+File.separator+fileName);    
	FileOutputStream fos = new FileOutputStream(file);     
	fos.write(getData); 
	if(fos!=null){
		fos.close();  
	}
	if(inputStream!=null){
		inputStream.close();
	}
	System.out.println("____[download success][from="+urlStr+"][to="+savePath+File.separator+fileName+"]"); 
}

 /**
 * 从输入流中获取字节数组
 * @param inputStream
 * @return
 * @throws IOException
 */
public static byte[] readInputStream(InputStream inputStream) throws IOException {  
	byte[] buffer = new byte[1024];  
	int len = 0;  
	ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	while((len = inputStream.read(buffer)) != -1) {  
		bos.write(buffer, 0, len);
	}  
	bos.close();  
	return bos.toByteArray();  
}

//字符串判空
public static boolean chkString(String str){
	if(str==null || str.trim().length()==0) return false;
	return true;
}

//解析版本数据将数据放入map中
public HashMap<String,String> getVerMapData(String verData){
	HashMap<String,String> map = new HashMap<String,String>();
	try{
		JSONObject verDataJo = new JSONObject(verData);
		JSONObject iosObj = verDataJo.isNull("iosObj")?null:verDataJo.getJSONObject("iosObj");
		JSONObject apkObj = verDataJo.isNull("apkObj")?null:verDataJo.getJSONObject("apkObj");
		
		map.put("proAlias",verDataJo.getString("proAlias"));
		map.put("appName",verDataJo.getString("appName"));
		map.put("appDownQR",verDataJo.getString("appDownQR"));
		
		if(iosObj!=null){
			if(!iosObj.isNull("appVer")) map.put("appVerIOS",iosObj.getString("appVer"));
			if(!iosObj.isNull("appBuild")) map.put("appBuildIOS",iosObj.getString("appBuild"));
			if(!iosObj.isNull("appDownUrl")) map.put("appDownUrlIOS",iosObj.getString("appDownUrl"));
			if(!iosObj.isNull("appUpdateUrl")) map.put("appUpdateUrlIOS",iosObj.getString("appUpdateUrl"));
			if(!iosObj.isNull("appstoreFirst") && !iosObj.isNull("appstoreUrl") && iosObj.getString("appstoreFirst").equals("1")){
				map.put("appUpdateUrlIOS",iosObj.getString("appstoreUrl"));//苹果商店优先则直接保存商店更新地址
			}
			map.put("appstoreUrl",iosObj.isNull("appstoreUrl")?"":iosObj.getString("appstoreUrl"));
			map.put("appstoreFirst",iosObj.isNull("appstoreFirst")?"":iosObj.getString("appstoreFirst"));
		}
		if(apkObj!=null){
			map.put("appVerAPK",apkObj.getString("appVer"));
			map.put("appBuildAPK",apkObj.getString("appBuild"));
			map.put("appDownUrlAPK",apkObj.getString("appDownUrl"));
			map.put("appUpdateUrlAPK",apkObj.getString("appUpdateUrl"));
			map.put("appDownNameAPK",apkObj.getString("appDownName"));
		}
		
		map.put("appPlatIOS","苹果");
		map.put("appPlatAPK","安卓");
		map.put("appPlatTextIOS","点击安装");
		map.put("appPlatTextAPK","点击下载");
		
		return map;	
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}
}

//解析版本数据将数据放入map中
public HashMap<String,String> getDBMapData(String dbData){
	HashMap<String,String> map = new HashMap<String,String>();
	try{
		JSONObject dbDataJo = new JSONObject(dbData);
		
		map.put("dbName",dbDataJo.getString("dbName"));//数据库名
		map.put("dbUser",dbDataJo.getString("dbUser"));//用户
		map.put("dbPass",dbDataJo.getString("dbPass"));//密码
		if(!dbDataJo.isNull("dbUpdateSQLIOS")) map.put("dbUpdateSQLIOS",dbDataJo.getString("dbUpdateSQLIOS"));//更新语句
		if(!dbDataJo.isNull("dbUpdateSQLAPK")) map.put("dbUpdateSQLAPK",dbDataJo.getString("dbUpdateSQLAPK"));
		
		return map;	
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}
}

//写文件
public static boolean writeTextToFile(String text, String fileName, boolean append) {
    boolean flags = append;
    try {
        Writer write = new FileWriter(fileName, flags);
        write.write(text);
        write.flush();
        write.close();
        return true;
    } catch (IOException ex) {
        System.err.println(ex.getMessage());
        return false;
    }
}
//读取文件
public static String loadTextFileGBK(String fn) {
	try {
		InputStreamReader read = new InputStreamReader (new FileInputStream(fn),"GBK");
		BufferedReader reader=new BufferedReader(read);
		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		read.close();
		return sb.toString();
	} catch (Exception e) {
		e.printStackTrace();
		return "";
	}
}
//更新客户平台版本数据
public void updateVersionData(HashMap<String,String> verMap,HashMap<String,String> dbMap) throws ClassNotFoundException, SQLException{
	Class.forName("com.mysql.jdbc.Driver");
	Connection conn = DriverManager.getConnection(dbMap.get("dbName"), dbMap.get("dbUser"), dbMap.get("dbPass"));
	Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
	PreparedStatement ps=null;
	int updateCount = 0;
	
	//"UPDATE version SET tite=#1#,lnk=#2# WHERE publish_id=8"
	String updateSQLIOS = dbMap.get("dbUpdateSQLIOS");//ios
	
	 if(chkString(updateSQLIOS)){
		if(chkString(verMap.get("appVerIOS")) && chkString(verMap.get("appUpdateUrlIOS"))){
			updateSQLIOS = updateSQLIOS.replaceAll("#1#",verMap.get("appVerIOS"));
			updateSQLIOS = updateSQLIOS.replaceAll("#2#",verMap.get("appUpdateUrlIOS"));
			ps = conn.prepareStatement(updateSQLIOS);
			updateCount = ps.executeUpdate();
			System.out.println("____[update row num ios]"+updateCount);
			ps.close();
		}
	}
	
	
	String updateSQLAPK = dbMap.get("dbUpdateSQLAPK");//apk
	if(chkString(updateSQLAPK)){
		if(chkString(verMap.get("appVerAPK")) && chkString(verMap.get("appUpdateUrlAPK"))){
			updateSQLAPK = updateSQLAPK.replaceAll("#1#",verMap.get("appVerAPK"));
			updateSQLAPK = updateSQLAPK.replaceAll("#2#",verMap.get("appUpdateUrlAPK"));
			ps = conn.prepareStatement(updateSQLAPK);
			updateCount = ps.executeUpdate();
			System.out.println("____[update row num apk]"+updateCount);
			ps.close();
		}
	}
	
	conn.close();
}

//检查设备类型
public static String checkMobileAgent(String userAgent){
   	if(userAgent!=null && userAgent.length()>0){
   		if(userAgent.toLowerCase().indexOf("iphone")>-1 || userAgent.toLowerCase().indexOf("ipad")>-1){
   			return "ios";
   		}else if(userAgent.toLowerCase().indexOf("android")>-1){
   			return "android";
   		}else{
   			return "unknown";
   		}
   	}else{
   		return "unknown";
   	}
   }
%>



<%//处理开始--------------------------------------------------------
String deviceOS = request.getParameter("deviceOS");
if(!chkString(deviceOS)) deviceOS = checkMobileAgent(request.getHeader("user-agent"));//访问设备

String action = request.getParameter("action");//执行动作
String sellerVer = request.getParameter("sellerVer");//商家版
String appstoreFirst = request.getParameter("appstoreFirst");//苹果商店优先
if(!chkString(action)) action = "download";
if(!chkString(sellerVer)){
	if(request.getRequestURI().indexOf("download/sj")>-1) sellerVer = "1";
}
if(!chkString(sellerVer)) sellerVer = "0";
if(!chkString(appstoreFirst)) appstoreFirst = "0";

String returnCode = "fail";//返回代码
String downPath = application.getRealPath("/")+File.separator+"download"+(sellerVer.equals("1")?"/sj":"");
downPath += (request.getRequestURL().indexOf("bz100")>-1 && request.getRequestURL().indexOf("dev")>-1?"/dev":"");

String dataCnf = "downData_cnf.d";
String dbCnf = "dbData_cnf.d";
String iosServiceUrl = "itms-services://?action=download-manifest&url=";

HashMap<String,String> verMap = null;//版本参数
HashMap<String,String> dbMap = null;//数据参数

if(action.equals("releaseVer")){//发版数据处理
	//request.setCharacterEncoding("UTF-8");

	//获取发版数据json
	String appName = request.getParameter("appName");
	String verData = request.getParameter("verData");
	String dbData = request.getParameter("dbData");
	
	System.out.println("_____appName:"+appName);
	System.out.println("_____sellerVer:"+sellerVer);
	System.out.println("_____appstoreFirst:"+appstoreFirst);
	System.out.println("_____verData:"+verData);
	System.out.println("_____dbData:"+dbData);
	
	if(chkString(verData) && chkString(dbData)){
		try{
			//封装数据
			verMap = getVerMapData(verData);
			dbMap = getDBMapData(dbData);
			
			//将数据写入配置文件
			writeTextToFile(verData,downPath+File.separator+dataCnf,false);
			
			//开始下载发版本文件 apk
			String apkUrl = verMap.get("appDownUrlAPK");//apk下载地址
			String fileName = verMap.get("appDownNameAPK");//apk下载版本文件名
			if(chkString(apkUrl) && chkString(apkUrl)){
				downloadFromUrl(apkUrl,fileName,downPath);
			}
			
			//更新客户平台版本及升级地址
			updateVersionData(verMap,dbMap);
			
			//将原来的index.html改名隐藏
			File indexHtml = new File(downPath+File.separator+"index.html");
			if(indexHtml.exists()){
				indexHtml.renameTo(new File(downPath+File.separator+"index_bak.html"));
			}
			
			returnCode = "success";
			
		}catch(Exception e){
			e.printStackTrace();
			
			returnCode = "error";
		}
	}
	out.println(returnCode);
}else{
	//从本地读取版本数据文件
	String verData = loadTextFileGBK(downPath+File.separator+dataCnf);
	if(chkString(verData)){
		verMap = getVerMapData(verData);//封装数据
	}
	
	
	if(verMap != null){
		appstoreFirst = verMap.get("appstoreFirst");
	}
	if(!chkString(appstoreFirst)) appstoreFirst = "0";
%>
<!Doctype html>
<html>
<head>
<title><%=(verMap!=null && chkString(verMap.get("appName"))?verMap.get("appName"):"Download")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
<meta content="telephone=no" name="format-detection">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta http-equiv="X-UA-Compatible" content="edge">
<style type="text/css">
*{padding:0;margin:0;}
img{border:0;max-width: 100%;}
.installbox{}
.installbox h1{background:#fefefe;text-align:center;padding:15px;border-bottom:1px solid #eee;}
.installbox h1 strong{font-weight:normal;display:block;text-align:center;font-size:22px;color:#666;padding:0 0 5px 0;}
.installbox h1 span{font-weight:normal;display:block;text-align:center;font-size:12px;color:#666;}
.appbox{padding:15px;}
.appbox h2{text-align:center;}
.appbox h2 img{width:120px;height:120px;}
.appbox .appmulspan{display:inline-block;}
.appbox .appmulspan span{display:block;text-align:center;font-size:12px;padding:3px;color:#666;}
.appbox div{margin:15px;text-align:center;}
.appbox div a{display:inline-block;width:150px;height:38px;line-height:38px;background:#56BC94;font-size:16px;color:#fff;text-decoration:none;border-radius: 5px;}
.appbox div a:hover{background:#56997C;text-decoration:none;}
.appbox div a:visited{background:#56997C;text-decoration:none;}
.appqr{border-top:1px solid #eee;}
.appqr h3{font-weight:normal;text-align:center;color:#333;font-size:14px;padding:25px;}
.appqr p{text-align:center;padding:15px;}
.appqr p img{text-align:center;width:145px;height:145px;}

.novertip{padding:35px;}
.novertip h3{font-weight:normal;text-align:center;width:450px;background:#fefefe;text-align:center;padding:15px;margin:0 auto;border:1px solid #eee;border-radius: 5px;box-shadow:0 0 2px rgba(0,0,0,.14),0 4px 8px rgba(0,0,0,.28);}
</style>
<script type="text/javascript">
function is_weixin(){//是否微信
	var ua = navigator.userAgent.toLowerCase();
	if (ua.match(/MicroMessenger/i) == "micromessenger") {
		return true;
	} else {
		return false;
	}
}
function loadHtml(osType){//构建提示html
	if(!osType) osType = "ios";
	var div = document.createElement('div');
	div.id = 'weixin-tip';
	div.innerHTML = '<p><img src="http://ver.bxsnet.com/app/download/img/weixin/tip_weixin_'+osType+'.png" alt="微信打开"/></p>';
	document.body.appendChild(div);
}

function loadStyleText(cssText) {//设置样式
	var style = document.createElement('style');
	style.rel = 'stylesheet';
	style.type = 'text/css';
	try {
		style.appendChild(document.createTextNode(cssText));
	} catch (e) {
		style.styleSheet.cssText = cssText; //ie9以下
	}
	var head=document.getElementsByTagName("head")[0]; //head标签之间加上style样式
	head.appendChild(style); 
}

var cssText = "#weixin-tip{position: fixed; left:0; top:0; background: rgba(0,0,0,0.7); filter:alpha(opacity=70); width: 100%; height:100%; z-index: 100;} #weixin-tip p{text-align: center; margin-top: 10%; padding:0 5%;}";
function winXinTip(osType){	
	//document.write("<div style='padding:5px 15px;text-align:center;'><img src='"+appImgUrl+"' width='80'></div>");
	//document.write("<h3 style='padding:5px 15px;text-align:center;'>"+appName+"</h3>");
	loadHtml(osType);
	loadStyleText(cssText);
}
function checkInstall(obj){
	obj.innerHTML = "加载...";
	window.setTimeout(function(){
		obj.innerHTML = "按Home键桌面查看";
	},3000);
}
</script>
</head>
<body>
<%if(verMap!=null){%>
<div class="installbox">
	<h1>
		<strong><%=verMap.get("appName")%></strong>
		<%if(deviceOS.equals("ios")){%>
			<%if(appstoreFirst.equals("1") && chkString(verMap.get("appstoreUrl"))){%>
			<%}else{%>
			<span>版本：<%=verMap.get("appVerIOS")%>(build <%=verMap.get("appBuildIOS")%>)</span>
			<%} %>
		<%}else if(deviceOS.equals("android")){%>
		<span>版本：<%=verMap.get("appVerAPK")%>(build <%=verMap.get("appBuildAPK")%>)</span>
		<%}else{%>
		<%}%>
	</h1>
	<div class="appdes"></div>
	<div class="appbox">
		<h2><img src="http://ver.bxsnet.com/app/download/img/logo/<%=verMap.get("proAlias")%>_install.png"/></h2>
		<%if(deviceOS.equals("ios")){%>
			<%if(appstoreFirst.equals("1") && chkString(verMap.get("appstoreUrl"))){%>
			<div class="appaction"><a onclick="checkInstall(this)" href="<%=verMap.get("appstoreUrl")%>">(<%=verMap.get("appPlatIOS")%>)<%=verMap.get("appPlatTextIOS")%></a></div>
			<%}else{%>
			<div class="appaction"><a onclick="checkInstall(this)" href="<%=iosServiceUrl%><%=verMap.get("appDownUrlIOS")%>">(<%=verMap.get("appPlatIOS")%>)<%=verMap.get("appPlatTextIOS")%></a></div>
			<%}%>
		<%}else if(deviceOS.equals("android")){%>
		<div class="appaction"><a href="<%=verMap.get("appUpdateUrlAPK")%>">(<%=verMap.get("appPlatAPK")%>)<%=verMap.get("appPlatTextAPK")%></a></div>
		<%}else{%>
		<div class="appaction">
			<span class="appmulspan">
				<%if(appstoreFirst.equals("1") && chkString(verMap.get("appstoreUrl"))){%>
				<a onclick="checkInstall(this)" href="<%=verMap.get("appstoreUrl")%>">(<%=verMap.get("appPlatIOS")%>)<%=verMap.get("appPlatTextIOS")%></a>
				<span>&nbsp;</span>
				<%}else{ %>
				<a onclick="checkInstall(this)" href="<%=iosServiceUrl%><%=verMap.get("appDownUrlIOS")%>">(<%=verMap.get("appPlatIOS")%>)<%=verMap.get("appPlatTextIOS")%></a>
				<span>版本：<%=verMap.get("appVerIOS")%>(build <%=verMap.get("appBuildIOS")%>)</span>
				<%} %>
			</span>
			<span class="appmulspan">
				<a href="<%=verMap.get("appUpdateUrlAPK")%>">(<%=verMap.get("appPlatAPK")%>)<%=verMap.get("appPlatTextAPK")%></a>
				<span>版本：<%=verMap.get("appVerAPK")%>(build <%=verMap.get("appBuildAPK")%>)</span>
			</span>
		</div>
		<%} %>
		<div class="appqr">
			<h3>或者用手机扫描下面的二维码安装</h3>
			<p><img src="<%=verMap.get("appDownQR")%>"/></p>
		</div>
	</div>
</div>
<%}else{%>
	<div class="novertip"><h3>系统维护中...请稍后重试！</h3></div>
<%}%>

<script type="text/javascript">
var isWeixin = is_weixin();
if(isWeixin){
	winXinTip("<%=deviceOS%>");
}
</script>

</body>
</html>
<%}%>
