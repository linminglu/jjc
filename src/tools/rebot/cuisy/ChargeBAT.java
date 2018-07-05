package tools.rebot.cuisy;

import java.math.BigDecimal;

import com.framework.service.impl.ServiceLocatorImpl;
import com.ram.model.User;
import com.ram.service.user.IUserService;

public class ChargeBAT {
	
	public IUserService userService = (IUserService) ServiceLocatorImpl.getInstance().getService("userService");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChargeBAT t = new ChargeBAT();
		
//		long startId = 19900000638L;
//		for(int i=0;i<300;i++){
//			String loginName = (startId+i)+"";
//			t.charge(i,loginName);
//		}
		
//		long startId = 16600000008L;
//		for(int i=0;i<3;i++){
//			String loginName = (startId+i)+"";
//			t.charge(i,loginName);
//		}
		
		long startId = 12300000001L;
		for(int i=0;i<10;i++){
			String loginName = (startId+i)+"";
			t.charge(i,loginName);
		}
		
	}
	
	public void charge(int i,String loginName){
		User user = userService.getUserByLoginName(loginName);
		BigDecimal money = new BigDecimal(100000);
		userService.modifyBalance(user.getUserId(), money, "1");
		P((i+1)+">"+user.getUserId()+","+user.getLoginName()+",charge money="+money);
	}
	
	public static void P(Object obj){
		System.out.println(obj.toString());
	}
}
