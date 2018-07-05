package tools.rebot.cuisy;

import java.math.BigDecimal;
import java.util.Random;

import com.apps.Constants;
import com.framework.util.ManageFile;
import com.framework.util.StringUtil;

public class CuiTest {
	
	private static BigDecimal cacheMoney = new BigDecimal(1000000.000);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		Map<String,BigDecimal> moneyMap = new HashMap<String,BigDecimal>();
//		moneyMap.put("M123190", new BigDecimal("1290.910"));
//		moneyMap.put("M123190", new BigDecimal("0"));
//		System.out.println(moneyMap.get("M123190"));
////		
//		String remark = "dddd|dfdfd";
//		p(remark.split("\\|").length);
		
		
//		BigDecimal day_fee = new BigDecimal("0.000");
//		BigDecimal aa = day_fee.add( new BigDecimal("9.00"));
//		p(aa);
		
//		for(int i=0;i<10;i++){
//			cacheMoney = cacheMoney.add(getRandMoney()).setScale(2,BigDecimal.ROUND_HALF_UP);
//			p(cacheMoney.add(getRandMoney()).setScale(2,BigDecimal.ROUND_HALF_UP));
//		}
		
//		for(int i=0;i<100000;i++){
//			updateMoney(i,getRandMoney());
//		}
		
//		String data = ManageFile.loadTextFileUTF8("D:/_Projects/_bxs_app/dadicaipiao/application/src/timer.properties");
//		String[] arr = data.split("\n");
//		for(int i=0;i<arr.length;i++){
//			String s = arr[i];
//			if(s!=null && s.trim().length()>0 && !s.startsWith("#")){
//				p(arr[i]);
//			}
//		}
		
		
	}
	
	public static void updateMoney(int i,BigDecimal cashMoney){
		StringBuffer logs = new StringBuffer();
		logs.append("m1=").append(cacheMoney);
		logs.append(",cash=").append(cashMoney);
		
		cacheMoney = cacheMoney.add(cashMoney);
		
		logs.append(",m2=").append(cacheMoney);
		//ManageFile.writeTextToFile(logs.toString(), "d:/aaaaaaaaaaaa.logs", true);
		p((i+1)+">"+logs.toString());
	}
	
	public static BigDecimal getRandMoney(){
		BigDecimal bet = new BigDecimal(StringUtil.getRandomInt(1, 5000));
		Random r = new Random();
		BigDecimal nn = bet.multiply(new BigDecimal(r.nextFloat()));
		int rnd = StringUtil.getRandomInt(0,100);
		if(rnd%2==0)  nn = nn.multiply(new BigDecimal(-1));
		nn = nn.setScale(2,BigDecimal.ROUND_HALF_UP);
		//p(nn);
		return nn;
	}

	
	public static void p(Object obj){
		System.out.println(obj.toString());
	}
}
