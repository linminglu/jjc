package tools.rebot.cuisy;

import java.math.BigDecimal;
import java.util.List;

import com.apps.model.UserTradeDetail;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ManageFile;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.ram.service.user.IUserService;

public class CheckDetailBAT {
	
	public IUserService userService = (IUserService) ServiceLocatorImpl.getInstance().getService("userService");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		CheckDetailBAT bat = new CheckDetailBAT();
		
		//bat.checkDetial("12300000004");
		
		//bat.checkDetialAll();
		bat.markErrDetial();
	}
	
	public void checkDetialAll(){//全部检查
		Integer iMax = 132724;//最大的明细id
		Integer iMin = 123202;
		
		HQUtils hq = null;
		
		Integer id = 0;
		UserTradeDetail utd = null;
		BigDecimal uMoney = null;
		
		int count = 0;
		int countOk = 0;
		int countErr = 0;
		
		String saveName = StringUtil.getSaveAsName();
		
		for(int i=0;i<(iMax-iMin+1);i++){
			id = iMax-i;
			utd = (UserTradeDetail)userService.getObject(UserTradeDetail.class,id);
			if(utd!=null){
				uMoney = utd.getUserMoney();
				if(uMoney==null) uMoney = new BigDecimal(0);
				hq = new HQUtils("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.tradeDetailId<=?");
				hq.addPars(utd.getUserId());
				hq.addPars(id);
				BigDecimal cMoney = (BigDecimal)userService.getObject(hq);
				if(cMoney==null) cMoney = new BigDecimal(-1);
				
				if(uMoney.doubleValue()==cMoney.doubleValue()){
					P((count+1)+"[SUCCESS]>"+"["+DateTimeUtil.DateToStringAll(utd.getCreateTime())+"]["+id+"]["+utd.getUserId()+"][utdMoney="+uMoney+"][cMoney="+cMoney+"]");
					countOk++;
				}else{
					String errLogs = (count+1)+"[ERROR]>"+"["+DateTimeUtil.DateToStringAll(utd.getCreateTime())+"]["+id+"]["+utd.getUserId()+"][utdMoney="+uMoney+"][cMoney="+cMoney+"]\n";
					countErr++;
					ManageFile.writeTextToFile(errLogs,"d:/detail.ali_"+saveName+".txt", true);
					P(errLogs);
				}
			}
			count++;
		}
		
		String logs = "[DONE][count="+count+"][countOk="+countOk+"][countErr="+countErr+"]\n";
		ManageFile.writeTextToFile(logs,"d:/detail.ali_"+saveName+".txt", true);
		P(logs);
		
	}
	
	public void markErrDetial(){//标记出错的明细
		Integer iMax = 132724;//最大的明细id
		Integer iMin = 123202;
		
		HQUtils hq = null;
		
		Integer id = 0;
		UserTradeDetail utd = null;
		BigDecimal uMoney = null;
		
		int count = 0;
		int countOk = 0;
		int countErr = 0;
		
		String saveName = StringUtil.getSaveAsName();
		
		for(int i=0;i<(iMax-iMin+1);i++){
			id = iMax-i;
			utd = (UserTradeDetail)userService.getObject(UserTradeDetail.class,id);
			if(utd!=null){
				uMoney = utd.getUserMoney();
				if(uMoney==null) uMoney = new BigDecimal(0);
				hq = new HQUtils("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.tradeDetailId<=?");
				hq.addPars(utd.getUserId());
				hq.addPars(id);
				BigDecimal cMoney = (BigDecimal)userService.getObject(hq);
				if(cMoney==null) cMoney = new BigDecimal(-1);
				
				String remarkNew = "";
				uMoney = uMoney.setScale(0,BigDecimal.ROUND_HALF_UP);
				cMoney = cMoney.setScale(0,BigDecimal.ROUND_HALF_UP);
				if(uMoney.subtract(cMoney).abs().doubleValue()<=1){
					P((count+1)+"[SUCCESS]>"+"["+DateTimeUtil.DateToStringAll(utd.getCreateTime())+"]["+id+"]["+utd.getUserId()+"][utdMoney="+uMoney+"][cMoney="+cMoney+"]");
					countOk++;
					String remark = utd.getRemark();
					if(!ParamUtils.chkString(remark)) remark = "";
					if(remark.length()>0 && remark.indexOf("|")>-1){
						try{
							remark = remark.split("\\|")[1];
						}catch (Exception e) {
							remark = "";
						}
					}
					remarkNew = ("["+cMoney+"]|"+remark);
				}else{
					String errLogs = (count+1)+"[ERROR]>"+"["+DateTimeUtil.DateToStringAll(utd.getCreateTime())+"]["+id+"]["+utd.getUserId()+"][utdMoney="+uMoney+"][cMoney="+cMoney+"]\n";
					countErr++;
					ManageFile.writeTextToFile(errLogs,"d:/detail.xiang_"+saveName+".txt", true);
					P(errLogs);
					String remark = utd.getRemark();
					if(!ParamUtils.chkString(remark)) remark = "";
					if(remark.length()>0 && remark.indexOf("|")>-1){
						try{
							remark = remark.split("\\|")[1];
						}catch (Exception e) {
							remark = "";
						}
					}
					remarkNew = ("_________Err["+cMoney+"]|"+remark);
					
				}
				utd.setRemark(remarkNew);
				userService.saveObject(utd, null);
			}
			count++;
		}
		
		String logs = "[DONE][count="+count+"][countOk="+countOk+"][countErr="+countErr+"]\n";
		ManageFile.writeTextToFile(logs,"d:/detail.xiang_"+saveName+".txt", true);
		P(logs);
		
	}
	
	public void checkDetial(String loginName){
		HQUtils hq = new HQUtils();
		hq.setSelect("select utd ");
		hq.addHsql("from UserTradeDetail utd,User u where utd.userId=u.userId and utd.tradeDetailId>277645 and u.loginName=? ");
		hq.addPars(loginName);
		hq.setOrderby(" order by utd.tradeDetailId desc");
		List<Object> list = userService.findObjects(hq);
		P("["+loginName+"]list.size="+list.size());
		int count = 0;
		int countOk = 0;
		int countErr = 0;
		for(Object obj:list){
			UserTradeDetail utd = (UserTradeDetail)obj;
			BigDecimal utdMoney = utd.getUserMoney();//明细余额
			if(utdMoney==null) utdMoney = new BigDecimal(0);
			hq = new HQUtils("select sum(utd.cashMoney) from UserTradeDetail utd,User u where utd.userId=u.userId and u.loginName=? and utd.tradeDetailId<=?");
			hq.addPars(loginName);
			hq.addPars(utd.getTradeDetailId());
			Object cObj = userService.getObject(hq);
			BigDecimal cMoney = (BigDecimal)cObj;//sum余额
			if(cMoney==null) cMoney = new BigDecimal(-1);
			//P((count+1)+">"+"["+loginName+"]["+utd.getTradeDetailId()+"][utdMoney="+utdMoney+"][cMoney="+cMoney+"]");
			if(utdMoney.doubleValue()==cMoney.doubleValue()){
				P((count+1)+"[SUCCESS]>"+"["+loginName+"]["+utd.getTradeDetailId()+"][utdMoney="+utdMoney+"][cMoney="+cMoney+"]");
				countOk++;
			}else{
				String errLogs = (count+1)+"[ERROR]>"+"["+loginName+"]["+utd.getTradeDetailId()+"][utdMoney="+utdMoney+"][cMoney="+cMoney+"]\n";
				countErr++;
				ManageFile.writeTextToFile(errLogs,"d:/detail."+loginName+".txt", true);
				P(errLogs);
			}
			count++;
		}
		String logs = "["+loginName+"][DONE][count="+count+"][countOk="+countOk+"][countErr="+countErr+"]\n";
		ManageFile.writeTextToFile(logs,"d:/detail."+loginName+".txt", true);
		P(logs);
	}
	
	public static void P(Object obj){
		System.out.println(obj.toString());
	}
}
