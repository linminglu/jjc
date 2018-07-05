package tools.rebot.betTest;

import help.util.APIWebDesUtils;

import java.net.URLEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.json.JSONObject;

import tools.rebot.util.PUtil;
import tools.rebot.util.URLUtil;

import com.framework.util.StringUtil;

public class CqSscTest {
	
	//public static String server = "http://127.0.0.1";
	public static String server = "http://dadi.bighetao.com";
	
	public static int counter = 0;
	
	private static final String strDefaultKey = "P29lMhJ8";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//下注批处理
		betAuto(10,1,130111111L);
	}
	
	
	public static String betAuto(int _buyNum,int _joiNum,long _startId){
		
//		Thread t = new Thread(){
//            public void run(){
            	counter = 0;
            	
            	int buyNum = _buyNum;//购买次数
            	int joinNum = _joiNum;//购买人数
            	long startId = _startId;//购买账号//18800000001 ~ 18800001000
            	
            	for(int aa=0;aa<buyNum;aa++){
	            	for(int i=0;i<joinNum;i++){
		    			String loginName = (startId+i)+"";
		    			try {
		    				bet_bet(loginName);
		    				//int sleepms = StringUtil.getRandomInt(50, 500);
		    				//P("sleep["+sleepms+"ms]......");
			    			//sleep(sleepms);
			    			
	//		    			if(i>=end-1){
	//		    				//P("sleep[3000ms] betAuto()......");
	//		    				sleep(500);
	//		    				betAuto();
	//		    			}
						} catch (Exception e) {
							//e.printStackTrace();
						}
		    			
		    		}
            	}
//            }
//		};
//		t.start();
		return "Completed.";
	}
	
	public static Integer[] PRICES = {2,10,50,80,100,100,100,100,200,200,100,500,800,1000,2000};
	
	//玩法和投注金额
	public static String[] betOptionArray = {
		"[{\"id\":\"498\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和大
		"[{\"id\":\"499\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和小
		"[{\"id\":\"500\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和单
		"[{\"id\":\"501\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和双
		"[{\"id\":\"502\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"503\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
		"[{\"id\":\"504\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和
	};
//	public static String[] betOptionArray = {
//		"[{\"id\":\"498\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//总和大
//		"[{\"id\":\"499\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//总和小
//		"[{\"id\":\"500\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//总和单
//		"[{\"id\":\"501\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//总和双
//		"[{\"id\":\"502\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//龙
//		"[{\"id\":\"503\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//虎
//		"[{\"id\":\"504\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//和
//	};
	public static void bet_bet(String loginName){
		String path = "/api/bet_bet?";
		
		//登录获取ukey
		String ukey = CqSscTest.login(loginName);
		String sessionNo = CqSscTest.bet_currentTime(ukey);
		String optionArray = betOptionArray[StringUtil.getRandomInt(0,6)];
		
		if(sessionNo!=null && sessionNo.length()>5){
			//构造下注参数
			StringBuffer sb = new StringBuffer();
			sb.append("u=").append(ukey);
			sb.append("&sessionNo=").append(sessionNo);
			sb.append("&optionArray=").append(optionArray);
			try {
				DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	
				APIWebDesUtils crypt = new APIWebDesUtils(keyFactory.generateSecret(key));
				String querys = "mw="+URLEncoder.encode(crypt.encryptBase64(sb.toString()),"utf-8");
				
				//P("source>"+sb.toString());
				//P("target>"+querys);
				
				String returns = URLUtil.HttpRequestUTF8(server+path+querys);
				
				counter++;
				PUtil.pw("下注["+counter+"]>"+loginName+">"+optionArray+">"+returns);
				
	//			JSONObject joR = new JSONObject(returns);
	//			JSONObject joData = joData.getJSONObject("data");
	//			JSONObject joObj = joR.getJSONObject("obj");
				
			} catch (Exception e) {
				counter++;
				//e.printStackTrace();
			}
		}else if(sessionNo.equals("-1")){//请求当前期出错
			counter++;
			PUtil.pw("网络请求出错["+counter+"].............");
		}else if(sessionNo.equals("-2")){
			counter++;
			PUtil.pw("封盘["+counter+"].............");
		}else{
			counter++;
			PUtil.pw("获取期号失败["+counter+"].............");
		}
	}
	
	//获取当前期信息
	public static String bet_currentTime(String ukey){
		String path = "/api/bet_currentTime";
		
		String sessionNo = "";
		
		//构造下注参数
		StringBuffer sb = new StringBuffer();
		sb.append("u=").append(ukey);
		
		try {
			DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			APIWebDesUtils crypt = new APIWebDesUtils(keyFactory.generateSecret(key));
			String querys = "mw="+URLEncoder.encode(crypt.encryptBase64(sb.toString()),"utf-8");
			
			//P("source>"+sb.toString());
			//P("target>"+querys);
			
			String returns = URLUtil.HttpRequestUTF8(server+path, querys);
			//P("returns>"+returns);
			if(returns!=null && returns.length()>10){
				JSONObject joR = new JSONObject(returns);
				JSONObject joData = joR.getJSONObject("data");
				JSONObject joObj = joData.getJSONObject("obj");
				String betTime = joObj.getString("betTime");
				if(Integer.valueOf(betTime)>0){//可以投注返回期号
					sessionNo = joObj.getString("sessionNo");
				}else{
					sessionNo = "-2";
				}
			}else{
				sessionNo = "-1";
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		//P("期号>"+sessionNo);
		return sessionNo;
	}
	
	//登录
	public static String login(String loginName){
		String path = "/api/user_login";
		
		String ukey = "";
		
		StringBuffer sb = new StringBuffer();
		sb.append("loginName=").append(loginName);
		sb.append("&password=").append("96E79218965EB72C92A549DD5A330112");
		sb.append("&repassword=").append("96E79218965EB72C92A549DD5A330112");
		sb.append("&machineType=").append("3");
		
		try {
			DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			APIWebDesUtils crypt = new APIWebDesUtils(keyFactory.generateSecret(key));
			String querys = "mw="+URLEncoder.encode(crypt.encryptBase64(sb.toString()),"utf-8");
			
			//P("source>"+sb.toString());
			//P("target>"+querys);
			
			String returns = URLUtil.HttpRequestUTF8(server+path, querys);
			//PUtil.pw("login>"+returns);
			
			JSONObject joR = new JSONObject(returns);
			JSONObject joData = joR.getJSONObject("data");
			JSONObject joObj = joData.getJSONObject("obj");
			ukey = joObj.getString("u");
		} catch (Exception e) {
			PUtil.pw("[登录失败]"+sb.toString());
		}
		
		//P("登录>"+loginName+">ukey="+ukey);
		return ukey;
	}
	
}
