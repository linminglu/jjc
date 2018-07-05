package tools.rebot.betTest;

import help.util.APIWebDesUtils;

import java.net.URLEncoder;
import java.util.HashMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.json.JSONObject;

import tools.rebot.util.PUtil;
import tools.rebot.util.URLUtil;

import com.framework.util.StringUtil;

public class _AllTest {
	public static String server = "http://127.0.0.1";
	
	public static int counter = 0;
	
	private static final String strDefaultKey = "P29lMhJ8";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initBetOptionArray();
		
		//login("13011111111");
		
		//下注批处理
		String[] codes = {
				"bjPk10",
				"sfPk10",
				"sfPk102",
				"xyft",
				"jsft",
				"cqSsc",
				"bjSsc",
//				"tjSsc",
//				"xjSsc",
//				"bj3",
//				"five",
//				"gdPick11",
//				"jxPick11",
//				"sdPick11",
//				"jsK3",
//				"bjK3",
//				"gdK10",
//				"gxK10",
//				"cqK10",
//				"xjpLu28",
//				"bjLu28",
//				"poker",
//				"markSix",
//				"sflhc"
		};
		betAuto(codes,100,5,13011111111L,0,0);
	}
	
	public static String[] _codes;
	public static int _buyNum=0;
	public static int _joiNum=0;
	public static long _startId=0;
	public static int _sleepMin=0;
	public static int _sleepMax=0;
	
	public static String betAuto(String[] _codes,int _buyNum,int _joiNum,long _startId,int _sleepMin,int _sleepMax){
		
		_AllTest._codes = _codes;
		_AllTest._buyNum = _buyNum;
		_AllTest._joiNum = _joiNum;
		_AllTest._startId = _startId;
		_AllTest._sleepMin = _sleepMin;
		_AllTest._sleepMax = _sleepMax;
		Thread t = new Thread(){
            //@SuppressWarnings("unused")
			public void run(){
            	counter = 0;
            	
            	String[] codes = _AllTest._codes;//购买彩种
            	int buyNum = _AllTest._buyNum;//购买次数
            	int joinNum = _AllTest._joiNum;//购买人数
            	long startId = _AllTest._startId;//购买账号//18800000001 ~ 18800001000
            	int sleepMin = _AllTest._sleepMin;
            	int sleepMax = _AllTest._sleepMax;
            	
            	for(int aa=0;aa<buyNum;aa++){
	            	for(int i=0;i<joinNum;i++){
		    			String loginName = (startId+i)+"";
		    			try {
		    				for(int kk=0;kk<codes.length;kk++){
		    					int sleepms = StringUtil.getRandomInt(sleepMin, sleepMax);
		    					bet(loginName,codes[kk],sleepms);
		    					if(sleepms>0){
					    			sleep(sleepms);
		    					}
		    				}
			    			
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
            }
		};
		t.start();
		return "Completed.";
	}
	
	public static Integer[] PRICES = {2,10,50,80,100,100,100,100,200,200,100,500,800,1000,2000};
	
	//玩法和投注金额
	public static String[] betOptionArray_XyBjPk10 = {
		"[{\"id\":\"741\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"742\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"743\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"744\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"745\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"746\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
	};
	public static String[] betOptionArray_XySfPk10 = {
		"[{\"id\":\"3338\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"3339\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"3340\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"3341\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"3342\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"3343\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
	};
	public static String[] betOptionArray_XySfPk102 = {
		"[{\"id\":\"5104\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"5105\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"5106\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"5107\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"5108\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"5109\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
	};
	public static String[] betOptionArray_XyXyFt = {
		"[{\"id\":\"6044\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"6045\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"6046\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"6047\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"6048\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"6049\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
	};
	public static String[] betOptionArray_XyJsFt = {
		"[{\"id\":\"5275\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"5276\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"5277\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"5278\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"5279\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"5280\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
	};
	public static String[] betOptionArray_XyCqSsc = {
		"[{\"id\":\"952\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"953\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"954\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"955\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"956\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"957\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
		"[{\"id\":\"958\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和
	};
	public static String[] betOptionArray_XyBjSsc = {
		"[{\"id\":\"5668\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"5669\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"5670\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"5671\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"5672\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"5673\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
		"[{\"id\":\"5674\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和
	};
	public static String[] betOptionArray_XyTjSsc = {
		"[{\"id\":\"9145\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"9146\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"9147\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"9148\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"9149\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"9150\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
		"[{\"id\":\"9151\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和
	};
	public static String[] betOptionArray_XyXjSsc = {
		"[{\"id\":\"8782\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"8783\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"8784\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"8785\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"8786\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"8787\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
		"[{\"id\":\"8788\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和
	};
	public static String[] betOptionArray_XyBj3 = {
		"[{\"id\":\"664\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"665\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"666\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"667\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"668\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"669\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
		"[{\"id\":\"670\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和
	};
	public static String[] betOptionArray_XyFive = {
		"[{\"id\":\"5591\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"5592\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"5593\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"5594\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"5595\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"5596\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
		"[{\"id\":\"5597\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和
	};
	public static String[] betOptionArray_XyGdPick11 = {
		"[{\"id\":\"1463\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和大
		"[{\"id\":\"1464\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和小
		"[{\"id\":\"1465\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和单
		"[{\"id\":\"1466\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和双
		"[{\"id\":\"1467\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//尾大
		"[{\"id\":\"1468\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//尾小
		"[{\"id\":\"1469\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"1470\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
	};
	public static String[] betOptionArray_XyJxPick11 = {
		"[{\"id\":\"5961\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和大
		"[{\"id\":\"5962\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和小
		"[{\"id\":\"5963\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和单
		"[{\"id\":\"5964\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和双
		"[{\"id\":\"5965\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//尾大
		"[{\"id\":\"5966\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//尾小
		"[{\"id\":\"5967\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"5968\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//虎
	};
	public static String[] betOptionArray_XySdPick11 = {
		"[{\"id\":\"6342\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和大
		"[{\"id\":\"6343\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和小
		"[{\"id\":\"6344\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和单
		"[{\"id\":\"6345\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//和双
		"[{\"id\":\"6346\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//尾大
		"[{\"id\":\"6347\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//尾小
		"[{\"id\":\"6348\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//龙
		"[{\"id\":\"6349\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]"//虎
	};
	public static String[] betOptionArray_XyJsK3 = {
		"[{\"id\":\"1560\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",
		"[{\"id\":\"1561\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",
		"[{\"id\":\"1562\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",
		"[{\"id\":\"1563\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",
		"[{\"id\":\"1564\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",
		"[{\"id\":\"1565\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",
	};
	public static String[] betOptionArray_XyBjK3 = {
		"[{\"id\":\"6302\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//17
		"[{\"id\":\"6303\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//18
		"[{\"id\":\"6304\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"6305\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"6306\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"6307\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
	};
	public static String[] betOptionArray_XyGdK10 = {
		"[{\"id\":\"1051\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和大
		"[{\"id\":\"1052\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和小
		"[{\"id\":\"1053\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和单
		"[{\"id\":\"1054\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和双
		"[{\"id\":\"1059\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//第1球大
		"[{\"id\":\"1060\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//第1球小
	};
	public static String[] betOptionArray_XyGxK10 = {
		"[{\"id\":\"1600\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和大
		"[{\"id\":\"1601\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和小
		"[{\"id\":\"1602\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和单
		"[{\"id\":\"1603\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和双
		"[{\"id\":\"1608\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//第1球大
		"[{\"id\":\"1609\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//第1球小
	};
	public static String[] betOptionArray_XyCqK10 = {
		"[{\"id\":\"5745\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和大
		"[{\"id\":\"5746\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和小
		"[{\"id\":\"5747\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和单
		"[{\"id\":\"5748\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//总和双
		"[{\"id\":\"5753\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//第1球大
		"[{\"id\":\"5754\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//第1球小
	};
	public static String[] betOptionArray_XyXjpLu28 = {
		"[{\"id\":\"910\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"911\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"912\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"913\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"914\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//极大
		"[{\"id\":\"915\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//极小
	};
	public static String[] betOptionArray_XyBjLu28 = {
		"[{\"id\":\"1009\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"1010\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"1011\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"1012\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"1013\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//极大
		"[{\"id\":\"1014\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//极小
	};
	public static String[] betOptionArray_XyPoker = {
		"[{\"id\":\"1443\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"1444\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"1445\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"1446\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"1448\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//对子
		"[{\"id\":\"1449\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//同花
	};
	public static String[] betOptionArray_XyMarkSix = {
		"[{\"id\":\"1797\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"1798\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"1799\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"1800\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"1801\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//合大
		"[{\"id\":\"1802\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//合小
	};
	public static String[] betOptionArray_XySflhc = {
		"[{\"id\":\"3563\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//大
		"[{\"id\":\"3564\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//小
		"[{\"id\":\"3565\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//单
		"[{\"id\":\"3566\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//双
		"[{\"id\":\"3567\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//合大
		"[{\"id\":\"3568\",\"p\":\""+PRICES[StringUtil.getRandomInt(0, 13)]+"\"}]",//合小
	};
	
	public static HashMap<String, String[]> BET_OPTION_MAP = new HashMap<String, String[]>();
	public static void initBetOptionArray(){
		BET_OPTION_MAP.put("bjPk10", betOptionArray_XyBjPk10);
		BET_OPTION_MAP.put("sfPk10", betOptionArray_XySfPk10);
		BET_OPTION_MAP.put("sfPk102", betOptionArray_XySfPk102);
		BET_OPTION_MAP.put("xyft", betOptionArray_XyXyFt);
		BET_OPTION_MAP.put("jsft", betOptionArray_XyJsFt);
		BET_OPTION_MAP.put("cqSsc", betOptionArray_XyCqSsc);
		BET_OPTION_MAP.put("bjSsc", betOptionArray_XyBjSsc);
		BET_OPTION_MAP.put("tjSsc", betOptionArray_XyTjSsc);
		BET_OPTION_MAP.put("xjSsc", betOptionArray_XyXjSsc);
		BET_OPTION_MAP.put("bj3", betOptionArray_XyBj3);
		BET_OPTION_MAP.put("five", betOptionArray_XyFive);
		BET_OPTION_MAP.put("gdPick11", betOptionArray_XyGdPick11);
		BET_OPTION_MAP.put("jxPick11", betOptionArray_XyJxPick11);
		BET_OPTION_MAP.put("sdPick11", betOptionArray_XySdPick11);
		BET_OPTION_MAP.put("jsK3", betOptionArray_XyJsK3);
		BET_OPTION_MAP.put("bjK3", betOptionArray_XyBjK3);
		BET_OPTION_MAP.put("gdK10", betOptionArray_XyGdK10);
		BET_OPTION_MAP.put("gxK10", betOptionArray_XyGxK10);
		BET_OPTION_MAP.put("cqK10", betOptionArray_XyCqK10);
		BET_OPTION_MAP.put("xjpLu28", betOptionArray_XyXjpLu28);
		BET_OPTION_MAP.put("bjLu28", betOptionArray_XyBjLu28);
		BET_OPTION_MAP.put("poker", betOptionArray_XyPoker);
		BET_OPTION_MAP.put("markSix", betOptionArray_XyMarkSix);
		BET_OPTION_MAP.put("sflhc", betOptionArray_XySflhc);
		
	}
	public static String[] getBetOptionArray(String code){
		return BET_OPTION_MAP.get(code);
	}
	
	public static void bet(String loginName,String code,int sleepms){
		String path = "/api/"+code+"_bet?";
		
		//登录获取ukey
		String ukey = login(loginName);
		String sessionNo = currentTime(ukey,code);
		String optionArray = getBetOptionArray(code)[StringUtil.getRandomInt(0,5)];
		
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
				if(returns!=null){
					returns = returns.replace("\n", "");
					returns = returns.replace("\r", "");
				}
				
				counter++;
				PUtil.pw("下注["+counter+"]["+code+"]["+sleepms+"ms]>"+loginName+">"+optionArray+">"+returns);
				
	//			JSONObject joR = new JSONObject(returns);
	//			JSONObject joData = joData.getJSONObject("data");
	//			JSONObject joObj = joR.getJSONObject("obj");
				
			} catch (Exception e) {
				counter++;
				//e.printStackTrace();
			}
		}else if(sessionNo.equals("-1")){//请求当前期出错
			counter++;
			PUtil.pw("网络请求出错["+counter+"]["+code+"]["+sleepms+"ms].............");
		}else if(sessionNo.equals("-2")){
			counter++;
			PUtil.pw("封盘["+counter+"]["+code+"]["+sleepms+"ms].............");
		}else{
			counter++;
			PUtil.pw("获取期号失败["+counter+"]["+code+"]["+sleepms+"ms].............");
		}
	}
	
	//获取当前期信息
	public static String currentTime(String ukey,String code){
		String path = "/api/"+code+"_currentTime";
		
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
				String betTime = joObj.get("betTime")+"";
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
	
	//打印
	public static void P(String info){
		System.out.println(info);
	}
}
