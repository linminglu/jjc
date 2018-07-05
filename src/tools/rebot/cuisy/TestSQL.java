package tools.rebot.cuisy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.ram.service.user.IUserService;

public class TestSQL {
	
	private HQUtils hq = null; 
	
	public IUserService userService = (IUserService) ServiceLocatorImpl.getInstance().getService("userService");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestSQL t = new TestSQL();
		
		t.testDateFormat2();
		t.testDateFormat3();
	}
	
	public void testDateFormat2(){
		hq = new HQUtils();
		hq.setSelect("select utd.createTime,sum(utd.cashMoney)");
		hq.addHsql("from UserTradeDetail utd where (utd.cashType=1 or utd.cashType=27 or utd.cashType=28)  group by date_format(utd.createTime,'%Y%m')");
		hq.setOrderby(" order by utd.createTime desc");
		
		List<Object> list = userService.findObjects(hq);
		for(Object obj:list){
			Object[] arr = (Object[])obj;
			Date dt = (Date)arr[0];
			String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
			BigDecimal money = (BigDecimal)arr[1];
			P("月充值："+dtMonth+"="+money);
		}
	}
	
	public void testDateFormat3(){
		hq = new HQUtils();
		hq.setSelect("select a.betTime,sum(a.betMoney) ");
		hq.addHsql(" from GaBetDetail a where 1=1 group by DATE_FORMAT(a.betTime,'%Y%m')");
		hq.setOrderby(" order by a.betTime desc");
		
		List<Object> list = userService.findObjects(hq);
		for(Object obj:list){
			Object[] arr = (Object[])obj;
			Date dt = (Date)arr[0];
			String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
			Integer money = (Integer)arr[1];
			P("月投注："+dtMonth+"="+money);
		}
	}
	
	public void testDateFormat(){
		hq = new HQUtils();
		hq.setSelect("select u.registDateTime,count(*)");
		hq.addHsql("from User u group by date_format(u.registDateTime,'%Y%m')");
		
		List<Object> list = userService.findObjects(hq);
		for(Object obj:list){
			Object[] arr = (Object[])obj;
			P(arr[0]+","+arr[1]);
		}
	}
	
	public static void P(Object obj){
		System.out.println(obj!=null?obj.toString():"");
	}
}
