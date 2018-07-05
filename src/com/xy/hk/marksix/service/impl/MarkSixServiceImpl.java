package com.xy.hk.marksix.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.eff.GameHelpUtil;
import com.apps.eff.dto.SessionItem;
import com.apps.model.UserTradeDetailRl;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.game.GameConstants;
import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.service.IGaService;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.xy.hk.marksix.MarkSixConstants;
import com.xy.hk.marksix.dao.IMarkSixDAO;
import com.xy.hk.marksix.model.MarkSixGaBet;
import com.xy.hk.marksix.model.MarkSixGaSession;
import com.xy.hk.marksix.model.MarkSixGaTrend;
import com.xy.hk.marksix.model.dto.MarkSixDTO;
import com.xy.hk.marksix.service.IMarkSixService;
import com.xy.hk.marksix.util.MarkSixUtil;

public class MarkSixServiceImpl  extends BaseService implements IMarkSixService {
	private IMarkSixDAO markSixDAO;
	private IUserService userService;
	private IGaService gaService;
	
	public void setMarkSixDAO(IMarkSixDAO markSixDAO) {
		this.markSixDAO = markSixDAO;
		super.dao = markSixDAO;
		
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
		
	/**
	 * 初始化场次
	 */
	public String updateInitSession(){
	
		String flag = "false"; //初始化是否成功
		//是否初始化今年期号。
		boolean isInitNo = this.checkInitSessionNo();
		List<MarkSixGaSession> saveList = null;
		if(!isInitNo){
			saveList = new ArrayList<MarkSixGaSession>();
			int yyyy = DateTimeUtil.getRightYear();
			Date ymd = DateTimeUtil.getNowDate();
			int days;//某年(year)的天数
			if(yyyy % 4 == 0 && yyyy % 100 != 0 || yyyy % 400 == 0){//闰年的判断规则
			   days=366;
			}else{
			   days=365;
			}
			int j = 1 ;// 第一期期号
			for(int i=0; i<days; i++){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
				Date firthDay = null;
				try {
					firthDay = format.parse(yyyy + "-01-01");
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Date dateAfter = DateTimeUtil.getDateAfter(firthDay, i, "yyyy-MM-dd HH:mm:ss");
				Calendar c = Calendar.getInstance();  
				 c.setTime(dateAfter);  
				 int dayForWeek = 0;  
				 if(c.get(Calendar.DAY_OF_WEEK) == 1){  
				  dayForWeek = 7;  
				 }else{  
				  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
				 }
				 

				 if(dayForWeek == 2 || dayForWeek == 4 || dayForWeek == 6){
						String sessionNo = yyyy + "" + String.format("%03d", j);
						j = j + 1;
						int beforDays = i-2;
						if(dayForWeek == 2){
							beforDays = i-3;
						}
						Date dateAfter2 = DateTimeUtil.getDateAfter(firthDay, beforDays, "yyyy-MM-dd HH:mm:ss");   

						String startTime = DateTimeUtil.DateToString(dateAfter2) + " 22:30:00"; //开盘时间
						String endTime = DateTimeUtil.DateToString(dateAfter) + " 21:30:00"; // 封盘时间

						MarkSixGaSession markSixGaSession = new MarkSixGaSession();
						markSixGaSession.setSessionNo(sessionNo);
						markSixGaSession.setStartTime(DateTimeUtil.parse(startTime));
						markSixGaSession.setEndTime(DateTimeUtil.parse(endTime));
						markSixGaSession.setOpenStatus(MarkSixConstants.MARKSIX_OPEN_STATUS_INIT);
//						markSixDAO.saveObject(markSixGaSession);
						saveList.add(markSixGaSession);
				 }
			}
			markSixDAO.updateObjectList(saveList, null);
			flag = "success";
		}else{
			flag = "inited";
		}
		return flag;
	}
	
	private boolean checkInitSessionNo() {
		String sessionNo;
		int year = DateTimeUtil.getRightYear();
		sessionNo = year + "067";
		HQUtils hq = new HQUtils(" from MarkSixGaSession six where six.sessionNo = ? ");
		hq.addPars(sessionNo);
		Integer count = markSixDAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	/**
	 * 开奖方法
	 * @param sessionNo
	 * @param result
	 * @return
	 */
	public   boolean openMarkSixSessionOpenResultMethod(MarkSixGaSession session,String result){
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_MARKSIX);
		try {
			HQUtils hql = new HQUtils("from GaBetDetail gd where ");
			hql.addHsql("gd.sessionId=? ");
			hql.addPars(session.getSessionId());
			hql.addHsql("and gd.betFlag=? " );
			hql.addPars("1");
			hql.addHsql("and gd.gameType=? ");
			hql.addPars(Constants.GAME_TYPE_XY_MARKSIX);	
			hql.addHsql("and gd.winResult=?" );
			hql.addPars(GameConstants.STATUS_0);
			
			String openResult = result;
			session.setOpenResult(result);

			//本期全部投注记录
			long startTiming = System.currentTimeMillis();
			List<Object> list= markSixDAO.findObjects(hql);
			GameHelpUtil.log(gameCode,"BETS ... ["+list.size()+"]["+session.getSessionNo()+"]["+(System.currentTimeMillis()-startTiming)+"ms]");
			
			Date startTime = session.getStartTime();
			
			MarkSixGaBet bet=new MarkSixGaBet();
			BigDecimal totalPoint=new BigDecimal(0);
			BigDecimal betCash=new BigDecimal(0);
			bet.setSessionId(session.getSessionId());
			Map<String,BigDecimal> betMap=new HashMap<String,BigDecimal>();
			betMap.put("totalPoint", totalPoint);
			betMap.put("betCash", betCash);
			
			if(list!=null && !list.isEmpty()){
				//开奖投注用户ids --by.cuisy.20171209
				List<Integer> userIds = new ArrayList<Integer>();
				
				for(Object object:list){
					GaBetDetail detail=(GaBetDetail) object;
					//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
					if(!StringUtil.chkListIntContains(userIds, detail.getUserId())){
						userIds.add(detail.getUserId());
					}//~
					
					detail.setOpenResult(session.getOpenResult());
					Map<String,Boolean> resultMap=null;
					
					if(detail.getPlayName().equals("特码A")||detail.getPlayName().equals("特码B")){
						resultMap=MarkSixUtil.getTeResult(openResult,startTime);			
						if(resultMap.get("和")!=null&&resultMap.get("和")==true){
							if(detail.getOptionTitle().equals("大")||detail.getOptionTitle().equals("小")
									||detail.getOptionTitle().equals("单")||detail.getOptionTitle().equals("双")
									||detail.getOptionTitle().equals("合大")||detail.getOptionTitle().equals("合小")
									||detail.getOptionTitle().equals("合单")||detail.getOptionTitle().equals("合双")
									||detail.getOptionTitle().equals("大单")||detail.getOptionTitle().equals("小单")
									||detail.getOptionTitle().equals("大双")||detail.getOptionTitle().equals("小双")
									||detail.getOptionTitle().equals("尾大")||detail.getOptionTitle().equals("尾小")){
								this.saveOpenResultDetail("3", detail, totalPoint, betCash,betMap);//3是打和
							}else{
								Boolean value=resultMap.get(detail.getOptionTitle());
								if(value!=null&&value==true){
									this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
								}else{
									this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
								}
							}
						}else{
							Boolean value=resultMap.get(detail.getOptionTitle());
							if(value!=null&&value==true){
								this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
							}else{
								this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//1是不中奖
							}
						}
					}else if(detail.getPlayName().equals("正码")){
						resultMap=MarkSixUtil.getZhengMaResult(openResult);
						Boolean value=resultMap.get(detail.getOptionTitle());
						if(value!=null&&value==true){
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
						}else{
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}
					}else if(detail.getPlayName().equals("正1特")||detail.getPlayName().equals("正2特")||detail.getPlayName().equals("正3特")
							||detail.getPlayName().equals("正4特")||detail.getPlayName().equals("正5特")||detail.getPlayName().equals("正6特")){
						Map<String,Map<String,Boolean>> resultMap1=MarkSixUtil.getZhengMaTeResult(detail.getPlayName(),openResult);
						resultMap=resultMap1.get(detail.getPlayName());
						Boolean value=resultMap.get(detail.getOptionTitle());
						if(value!=null&&value==true){
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
						}else{
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}
					}else if(detail.getPlayName().equals("正码1-6")){
						Map<String,Map<String,Boolean>> resultMap1=MarkSixUtil.getZhengMa16Result(detail.getBetName(),openResult);
						resultMap=resultMap1.get(detail.getBetName());
						Boolean value=resultMap.get(detail.getOptionTitle());
						if(value!=null&&value==true){
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
						}else{
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}
					}else if(detail.getPlayName().equals("过关")){
						String  resultValue=MarkSixUtil.getGuoguanResult(detail.getOptionTitle(), openResult);
						if(resultValue.equals("2")){
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}else{
							String rate=resultValue.split("&")[1];
							BigDecimal wincash=new BigDecimal(rate).multiply(new BigDecimal(detail.getBetMoney()));
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖
						}
					}else if(detail.getPlayName().equals("二全中")){
						String  resultValue=MarkSixUtil.getTwoQuanZhong(detail.getBetMoney(),detail.getOptionTitle(), openResult);
						if(resultValue.equals("2")){
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}else{
							String num=resultValue.split("&")[1];
							String singleMoney=resultValue.split("&")[3];
							BigDecimal wincash=detail.getBetRate().multiply(new BigDecimal(singleMoney)).multiply(new BigDecimal(num));
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖				
						}
					}else if(detail.getPlayName().equals("二中特")){
						String  resultValue=MarkSixUtil.getTwoZhongTe(detail.getBetMoney(),detail.getOptionTitle(), openResult);
						if(resultValue.equals("2")){
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}else{
							String array[]=resultValue.split("_");
							BigDecimal wincash=new BigDecimal(0);
							for(int k=1;k<array.length;k++){//注意从1开始   中奖返回的格式 ：  1_中奖数量&赔率&内容_ 中奖数量&赔率&内容
								String num=array[k].split("&")[0];
								String rate=array[k].split("&")[1];
								String betMoney=array[k].split("&")[2];
								wincash=wincash.add(new BigDecimal(betMoney).multiply(new BigDecimal(rate)).multiply(new BigDecimal(num)));
							}
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖				
						}
					}else if(detail.getPlayName().equals("特串")){
						String  resultValue=MarkSixUtil.getTeChuan(detail.getBetMoney(),detail.getOptionTitle(), openResult);
						if(resultValue.equals("2")){
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}else{
							String array[]=resultValue.split("&");
							BigDecimal wincash=new BigDecimal(0);
							String betMoney=array[2];
							String num=array[1];
							wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num)));
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖				
						}
					}else if(detail.getPlayName().equals("三全中")){
						String  resultValue=MarkSixUtil.getThreeQuanZhong(detail.getBetMoney(),detail.getOptionTitle(), openResult);
						if(resultValue.equals("2")){
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}else{
							String array[]=resultValue.split("&");
							BigDecimal wincash=new BigDecimal(0);
							String betMoney=array[2];
							String num=array[1];
							wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num)));
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
						}
					}else if(detail.getPlayName().equals("三中二")){
						String  resultValue=MarkSixUtil.getThreeZhongTwo(detail.getBetMoney(),detail.getOptionTitle(), openResult);
						if(resultValue.equals("2")){
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}else{
							String array[]=resultValue.split("_");
							BigDecimal wincash=new BigDecimal(0);
							for(int k=1;k<array.length;k++){//注意从1开始   中奖返回的格式 ：  1_中奖数量&赔率&内容_ 中奖数量&赔率&内容
								String num=array[k].split("&")[0];
								String rate=array[k].split("&")[1];
								String betMoney=array[k].split("&")[2];
								wincash=wincash.add(new BigDecimal(betMoney).multiply(new BigDecimal(rate)).multiply(new BigDecimal(num)));
							}
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖				
						}
					}else if(detail.getPlayName().equals("四全中")){
						String  resultValue=MarkSixUtil.getFourQuanZhong(detail.getBetMoney(),detail.getOptionTitle(), openResult);
						if(resultValue.equals("2")){
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}else{
							String array[]=resultValue.split("&");
							BigDecimal wincash=new BigDecimal(0);
							String betMoney=array[2];
							String num=array[1];
							wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num)));
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
						}
					}else if(detail.getPlayName().equals("半波")){
						resultMap=MarkSixUtil.getBanBo(openResult);
						if(resultMap.get("和")!=null&&resultMap.get("和")==true){
							this.saveOpenResultDetail("3", detail, totalPoint, betCash,betMap);//3是打和
						}else{
							Boolean value=resultMap.get(detail.getOptionTitle());
							if(value!=null&&value==true){
								this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
							}else{
								this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
							}
						}				
					}else if(detail.getPlayName().equals("一肖")){
						resultMap=MarkSixUtil.getOneZodiac(startTime,openResult);
						Boolean value=resultMap.get(detail.getOptionTitle());
						if(value!=null&&value==true){
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
						}else{
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}
					}else if(detail.getPlayName().equals("尾数")){
						resultMap=MarkSixUtil.getTailNum(openResult);
						Boolean value=resultMap.get(detail.getOptionTitle());
						if(value!=null&&value==true){
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
						}else{
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}
					}else if(detail.getPlayName().equals("特码生肖")){
						resultMap=MarkSixUtil.getailTeZodiac(startTime,openResult);
						Boolean value=resultMap.get(detail.getOptionTitle());
						if(value!=null&&value==true){
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
						}else{
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}
					}else if(detail.getPlayName().equals("二肖")||detail.getPlayName().equals("三肖")||detail.getPlayName().equals("四肖")
							||detail.getPlayName().equals("五肖")||detail.getPlayName().equals("六肖")||detail.getPlayName().equals("七肖")
							||detail.getPlayName().equals("八肖")||detail.getPlayName().equals("九肖")||detail.getPlayName().equals("十肖")
							||detail.getPlayName().equals("十一肖")){
						String value=MarkSixUtil.getHeZodiac(detail.getOptionTitle(), openResult,startTime);		
						if(value.equals("1")){
							this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
						}else if(value.equals("2")){
							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
						}else if(value.equals("3")){
							this.saveOpenResultDetail("3", detail, totalPoint, betCash,betMap);//3是打和
						}
					}else if(detail.getPlayName().equals("二肖连中")||detail.getPlayName().equals("三肖连中")||detail.getPlayName().equals("四肖连中")
							||detail.getPlayName().equals("五肖连中")){
						int num=getBetNum(detail.getPlayName());
						if(num>0){
							String resultValue=MarkSixUtil.getZodiacLianZhong(detail.getBetMoney(),num, detail.getOptionTitle(), openResult,startTime);
							if(resultValue.equals("2")){
								this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
							}else{
								String array[]=resultValue.split("&");
								BigDecimal wincash=new BigDecimal(0);
								String betMoney=array[2];
								String num1=array[1];
								wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
								this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
							}			
						}
					}else if(detail.getPlayName().equals("二肖连不中")||detail.getPlayName().equals("三肖连不中")
							||detail.getPlayName().equals("四肖连不中")){
						int num=getBetNum(detail.getPlayName());
						if(num>0){
							String resultValue=MarkSixUtil.getZodiacLianBuZhong(detail.getBetMoney(),num, detail.getOptionTitle(), openResult,startTime);
							if(resultValue.equals("2")){
								this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
							}else{
								String array[]=resultValue.split("&");
								BigDecimal wincash=new BigDecimal(0);
								String betMoney=array[2];
								String num1=array[1];
								wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
								this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
							}			
						}
					}else if(detail.getPlayName().equals("二尾连中")||detail.getPlayName().equals("三尾连中")
							||detail.getPlayName().equals("四尾连中")){
						int num=getBetNum(detail.getPlayName());
						if(num>0){
							String resultValue=MarkSixUtil.getTailLianZhong(detail.getBetMoney(),num, detail.getOptionTitle(), openResult);
							if(resultValue.equals("2")){
								this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
							}else{
								String array[]=resultValue.split("&");
								BigDecimal wincash=new BigDecimal(0);
								String betMoney=array[2];
								String num1=array[1];
								wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
								this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖		
							}
						}
					}else if(detail.getPlayName().equals("二尾连不中")||detail.getPlayName().equals("三尾不连中")
							||detail.getPlayName().equals("四尾连不中")){
						int num=getBetNum(detail.getPlayName());
						if(num>0){
							String resultValue=MarkSixUtil.getTailLianBuZhong(detail.getBetMoney(),num, detail.getOptionTitle(), openResult);
							if(resultValue.equals("2")){
								this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
							}else{
								String array[]=resultValue.split("&");
								BigDecimal wincash=new BigDecimal(0);
								String betMoney=array[2];
								String num1=array[1];
								wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
								this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
							}
						}
					}else if(detail.getPlayName().equals("五不中")||detail.getPlayName().equals("六不中")
							||detail.getPlayName().equals("七不中")||detail.getPlayName().equals("八不中")||detail.getPlayName().equals("九不中")
							||detail.getPlayName().equals("十不中")||detail.getPlayName().equals("十一不中")||detail.getPlayName().equals("十二不中")){
						int num=getBetNumValue(detail.getPlayName());
						if(num>0){
							String resultValue=MarkSixUtil.getAllNotWin(detail.getBetMoney(),num, detail.getOptionTitle(), openResult);
							if(resultValue.equals("2")){
								this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
							}else{
								String array[]=resultValue.split("&");
								BigDecimal wincash=new BigDecimal(0);
								String betMoney=array[2];
								String num1=array[1];
								wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
								this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
							}
						}
					}
					
				}
				GameHelpUtil.log(gameCode,"Calc ... ["+(System.currentTimeMillis()-startTiming)+"ms]");
				//批量更新开奖用户实时余额 --by.cuisy.20171209
				startTiming = System.currentTimeMillis();
				userService.updateUserMoney(userIds);
				GameHelpUtil.log(gameCode,"BatM ... uids="+userIds.size()+"["+(System.currentTimeMillis()-startTiming)+"ms]");
				
				//把资金明细里投注记录状态值改为有效
				long timginUtds = System.currentTimeMillis();
				userService.updateUserTradeDetailStatus(session.getSessionNo(), 
						Constants.GAME_TYPE_XY_MARKSIX, Constants.PUB_STATUS_OPEN);
				GameHelpUtil.log(gameCode,"BatD ... ["+(System.currentTimeMillis()-timginUtds)+"ms]");
			}
			
			bet.setTotalPoint(betMap.get("totalPoint"));
			bet.setWinCash(betMap.get("betCash"));
			markSixDAO.saveObject(bet);
			
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			GameHelpUtil.log(gameCode,"open err::["+session.getSessionNo()+"]=>"+e.getMessage(),e);
			return false;
		}
		
	}
	
//	public   boolean openMarkSixSessionOpenResultMethod(MarkSixGaSession markSixsession,String result){
////		HQUtils hq = new HQUtils("from MarkSixGaSession bgs where bgs.sessionNo=?");
////		String sessionNo = markSixsession.getSessionNo();
////		hq.addPars(sessionNo);
////		MarkSixGaSession session = (MarkSixGaSession)markSixDAO.getObject(hq);
////		HQUtils hq1 = new HQUtils("from GaBetDetail gd where gd.sessionId=? and gd.betFlag='1' and gd.gameType='12' ");
////		hq1.addPars(markSixsession.getSessionId());
//
//		HQUtils hql = new HQUtils("from GaBetDetail gd where 1 = 1 ");
//		hql.addHsql(" and gd.sessionId=? ");
//		hql.addPars(markSixsession.getSessionId());
//		hql.addHsql(" and gd.betFlag=? " );
//		hql.addPars("1");
//		hql.addHsql(" and gd.gameType =? ");
//		hql.addPars(Constants.GAME_TYPE_XY_MARKSIX);	
//		hql.addHsql(" and gd.winResult=? " );
//		hql.addPars("0");
//		
//		String openResult = markSixsession.getOpenResult();
//
//		List<Object>  list= markSixDAO.findObjects(hql);
//		Date startTime = markSixsession.getStartTime();
//		
//		MarkSixGaBet bet=new MarkSixGaBet();
//		BigDecimal  totalPoint=new BigDecimal(0);
//		BigDecimal  betCash=new BigDecimal(0);
//		bet.setSessionId(markSixsession.getSessionId());
//		Map<String,BigDecimal> betMap=new HashMap<String,BigDecimal>();
//		betMap.put("totalPoint", totalPoint);
//		betMap.put("betCash", betCash);
//		if(list!=null&&list.size()>0){
//			//开奖投注用户ids --by.cuisy.20171209
//			List<Integer> userIds = new ArrayList<Integer>();
//			
//			for(Object object:list){
//				GaBetDetail detail=(GaBetDetail) object;
//				//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
//				if(!StringUtil.chkListIntContains(userIds, detail.getUserId())){
//					userIds.add(detail.getUserId());
//				}//~
//				
//				detail.setOpenResult(markSixsession.getOpenResult());
//				Map<String,Boolean> resultMap=null;
//				if(detail.getPlayName().equals("特码A")||detail.getPlayName().equals("特码B")){
//					resultMap=MarkSixUtil.getTeResult(openResult,startTime);			
//					if(resultMap.get("和")!=null&&resultMap.get("和")==true){
//						if(detail.getOptionTitle().equals("大")||detail.getOptionTitle().equals("小")
//								||detail.getOptionTitle().equals("单")||detail.getOptionTitle().equals("双")
//								||detail.getOptionTitle().equals("合大")||detail.getOptionTitle().equals("合小")
//								||detail.getOptionTitle().equals("合单")||detail.getOptionTitle().equals("合双")
//								||detail.getOptionTitle().equals("大单")||detail.getOptionTitle().equals("小单")
//								||detail.getOptionTitle().equals("大双")||detail.getOptionTitle().equals("小双")
//								||detail.getOptionTitle().equals("尾大")||detail.getOptionTitle().equals("尾小")){
//							this.saveOpenResultDetail("3", detail, totalPoint, betCash,betMap);//3是打和
//						}else{
//							Boolean value=resultMap.get(detail.getOptionTitle());
//							if(value!=null&&value==true){
//								this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//							}else{
//								this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//							}
//						}
//					}else{
//						Boolean value=resultMap.get(detail.getOptionTitle());
//						if(value!=null&&value==true){
//							this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//						}else{
//							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//1是不中奖
//						}
//					}
//				}else if(detail.getPlayName().equals("正码")){
//					resultMap=MarkSixUtil.getZhengMaResult(openResult);
//					Boolean value=resultMap.get(detail.getOptionTitle());
//					if(value!=null&&value==true){
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//					}else{
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}
//				}else if(detail.getPlayName().equals("正1特")||detail.getPlayName().equals("正2特")||detail.getPlayName().equals("正3特")
//						||detail.getPlayName().equals("正4特")||detail.getPlayName().equals("正5特")||detail.getPlayName().equals("正6特")){
//					Map<String,Map<String,Boolean>> resultMap1=MarkSixUtil.getZhengMaTeResult(detail.getPlayName(),openResult);
//					resultMap=resultMap1.get(detail.getPlayName());
//					Boolean value=resultMap.get(detail.getOptionTitle());
//					if(value!=null&&value==true){
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//					}else{
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}
//				}else if(detail.getPlayName().equals("正码1-6")){
//					Map<String,Map<String,Boolean>> resultMap1=MarkSixUtil.getZhengMa16Result(detail.getBetName(),openResult);
//					resultMap=resultMap1.get(detail.getBetName());
//					Boolean value=resultMap.get(detail.getOptionTitle());
//					if(value!=null&&value==true){
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//					}else{
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}
//				}else if(detail.getPlayName().equals("过关")){
//					String  resultValue=MarkSixUtil.getGuoguanResult(detail.getOptionTitle(), openResult);
//					if(resultValue.equals("2")){
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}else{
//						String rate=resultValue.split("&")[1];
//						BigDecimal wincash=new BigDecimal(rate).multiply(new BigDecimal(detail.getBetMoney()));
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖
//					}
//				}else if(detail.getPlayName().equals("二全中")){
//					String  resultValue=MarkSixUtil.getTwoQuanZhong(detail.getBetMoney(),detail.getOptionTitle(), openResult);
//					if(resultValue.equals("2")){
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}else{
//						String num=resultValue.split("&")[1];
//						String singleMoney=resultValue.split("&")[3];
//						BigDecimal wincash=detail.getBetRate().multiply(new BigDecimal(singleMoney)).multiply(new BigDecimal(num));
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖				
//					}
//				}else if(detail.getPlayName().equals("二中特")){
//					String  resultValue=MarkSixUtil.getTwoZhongTe(detail.getBetMoney(),detail.getOptionTitle(), openResult);
//					if(resultValue.equals("2")){
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}else{
//						String array[]=resultValue.split("_");
//						BigDecimal wincash=new BigDecimal(0);
//						for(int k=1;k<array.length;k++){//注意从1开始   中奖返回的格式 ：  1_中奖数量&赔率&内容_ 中奖数量&赔率&内容
//							String num=array[k].split("&")[0];
//							String rate=array[k].split("&")[1];
//							String betMoney=array[k].split("&")[2];
//							wincash=wincash.add(new BigDecimal(betMoney).multiply(new BigDecimal(rate)).multiply(new BigDecimal(num)));
//						}
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖				
//					}
//				}else if(detail.getPlayName().equals("特串")){
//					String  resultValue=MarkSixUtil.getTeChuan(detail.getBetMoney(),detail.getOptionTitle(), openResult);
//					if(resultValue.equals("2")){
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}else{
//						String array[]=resultValue.split("&");
//						BigDecimal wincash=new BigDecimal(0);
//						String betMoney=array[2];
//						String num=array[1];
//						wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num)));
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖				
//					}
//				}else if(detail.getPlayName().equals("三全中")){
//					String  resultValue=MarkSixUtil.getThreeQuanZhong(detail.getBetMoney(),detail.getOptionTitle(), openResult);
//					if(resultValue.equals("2")){
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}else{
//						String array[]=resultValue.split("&");
//						BigDecimal wincash=new BigDecimal(0);
//						String betMoney=array[2];
//						String num=array[1];
//						wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num)));
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
//					}
//				}else if(detail.getPlayName().equals("三中二")){
//					String  resultValue=MarkSixUtil.getThreeZhongTwo(detail.getBetMoney(),detail.getOptionTitle(), openResult);
//					if(resultValue.equals("2")){
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}else{
//						String array[]=resultValue.split("_");
//						BigDecimal wincash=new BigDecimal(0);
//						for(int k=1;k<array.length;k++){//注意从1开始   中奖返回的格式 ：  1_中奖数量&赔率&内容_ 中奖数量&赔率&内容
//							String num=array[k].split("&")[0];
//							String rate=array[k].split("&")[1];
//							String betMoney=array[k].split("&")[2];
//							wincash=wincash.add(new BigDecimal(betMoney).multiply(new BigDecimal(rate)).multiply(new BigDecimal(num)));
//						}
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖				
//					}
//				}else if(detail.getPlayName().equals("四全中")){
//					String  resultValue=MarkSixUtil.getFourQuanZhong(detail.getBetMoney(),detail.getOptionTitle(), openResult);
//					if(resultValue.equals("2")){
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}else{
//						String array[]=resultValue.split("&");
//						BigDecimal wincash=new BigDecimal(0);
//						String betMoney=array[2];
//						String num=array[1];
//						wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num)));
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
//					}
//				}else if(detail.getPlayName().equals("半波")){
//					resultMap=MarkSixUtil.getBanBo(openResult);
//					if(resultMap.get("和")!=null&&resultMap.get("和")==true){
//						this.saveOpenResultDetail("3", detail, totalPoint, betCash,betMap);//3是打和
//					}else{
//						Boolean value=resultMap.get(detail.getOptionTitle());
//						if(value!=null&&value==true){
//							this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//						}else{
//							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//						}
//					}				
//				}else if(detail.getPlayName().equals("一肖")){
//					resultMap=MarkSixUtil.getOneZodiac(startTime,openResult);
//					Boolean value=resultMap.get(detail.getOptionTitle());
//					if(value!=null&&value==true){
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//					}else{
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}
//				}else if(detail.getPlayName().equals("尾数")){
//					resultMap=MarkSixUtil.getTailNum(openResult);
//					Boolean value=resultMap.get(detail.getOptionTitle());
//					if(value!=null&&value==true){
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//					}else{
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}
//				}else if(detail.getPlayName().equals("特码生肖")){
//					resultMap=MarkSixUtil.getailTeZodiac(startTime,openResult);
//					Boolean value=resultMap.get(detail.getOptionTitle());
//					if(value!=null&&value==true){
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//					}else{
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}
//				}else if(detail.getPlayName().equals("二肖")||detail.getPlayName().equals("三肖")||detail.getPlayName().equals("四肖")
//						||detail.getPlayName().equals("五肖")||detail.getPlayName().equals("六肖")||detail.getPlayName().equals("七肖")
//						||detail.getPlayName().equals("八肖")||detail.getPlayName().equals("九肖")||detail.getPlayName().equals("十肖")
//						||detail.getPlayName().equals("十一肖")){
//					String value=MarkSixUtil.getHeZodiac(detail.getOptionTitle(), openResult,startTime);		
//					if(value.equals("1")){
//						this.saveOpenResultDetail("1", detail, totalPoint, betCash,betMap);//1是中奖
//					}else if(value.equals("2")){
//						this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//					}else if(value.equals("3")){
//						this.saveOpenResultDetail("3", detail, totalPoint, betCash,betMap);//3是打和
//					}
//				}else if(detail.getPlayName().equals("二肖连中")||detail.getPlayName().equals("三肖连中")||detail.getPlayName().equals("四肖连中")
//						||detail.getPlayName().equals("五肖连中")){
//					int num=getBetNum(detail.getPlayName());
//					if(num>0){
//						String resultValue=MarkSixUtil.getZodiacLianZhong(detail.getBetMoney(),num, detail.getOptionTitle(), openResult,startTime);
//						if(resultValue.equals("2")){
//							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//						}else{
//							String array[]=resultValue.split("&");
//							BigDecimal wincash=new BigDecimal(0);
//							String betMoney=array[2];
//							String num1=array[1];
//							wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
//							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
//						}			
//					}
//				}else if(detail.getPlayName().equals("二肖连不中")||detail.getPlayName().equals("三肖连不中")
//						||detail.getPlayName().equals("四肖连不中")){
//					int num=getBetNum(detail.getPlayName());
//					if(num>0){
//						String resultValue=MarkSixUtil.getZodiacLianBuZhong(detail.getBetMoney(),num, detail.getOptionTitle(), openResult,startTime);
//						if(resultValue.equals("2")){
//							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//						}else{
//							String array[]=resultValue.split("&");
//							BigDecimal wincash=new BigDecimal(0);
//							String betMoney=array[2];
//							String num1=array[1];
//							wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
//							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
//						}			
//					}
//				}else if(detail.getPlayName().equals("二尾连中")||detail.getPlayName().equals("三尾连中")
//						||detail.getPlayName().equals("四尾连中")){
//					int num=getBetNum(detail.getPlayName());
//					if(num>0){
//						String resultValue=MarkSixUtil.getTailLianZhong(detail.getBetMoney(),num, detail.getOptionTitle(), openResult);
//						if(resultValue.equals("2")){
//							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//						}else{
//							String array[]=resultValue.split("&");
//							BigDecimal wincash=new BigDecimal(0);
//							String betMoney=array[2];
//							String num1=array[1];
//							wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
//							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖		
//						}
//					}
//				}else if(detail.getPlayName().equals("二尾连不中")||detail.getPlayName().equals("三尾不连中")
//						||detail.getPlayName().equals("四尾连不中")){
//					int num=getBetNum(detail.getPlayName());
//					if(num>0){
//						String resultValue=MarkSixUtil.getTailLianBuZhong(detail.getBetMoney(),num, detail.getOptionTitle(), openResult);
//						if(resultValue.equals("2")){
//							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//						}else{
//							String array[]=resultValue.split("&");
//							BigDecimal wincash=new BigDecimal(0);
//							String betMoney=array[2];
//							String num1=array[1];
//							wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
//							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
//						}
//					}
//				}else if(detail.getPlayName().equals("五不中")||detail.getPlayName().equals("六不中")
//						||detail.getPlayName().equals("七不中")||detail.getPlayName().equals("八不中")||detail.getPlayName().equals("九不中")
//						||detail.getPlayName().equals("十不中")||detail.getPlayName().equals("十一不中")||detail.getPlayName().equals("十二不中")){
//					int num=getBetNumValue(detail.getPlayName());
//					if(num>0){
//						String resultValue=MarkSixUtil.getAllNotWin(detail.getBetMoney(),num, detail.getOptionTitle(), openResult);
//						if(resultValue.equals("2")){
//							this.saveOpenResultDetail("2", detail, totalPoint, betCash,betMap);//2是不中奖
//						}else{
//							String array[]=resultValue.split("&");
//							BigDecimal wincash=new BigDecimal(0);
//							String betMoney=array[2];
//							String num1=array[1];
//							wincash=wincash.add(new BigDecimal(betMoney).multiply(detail.getBetRate()).multiply(new BigDecimal(num1)));
//							this.saveOpenResultDetail("1", detail, totalPoint, betCash,wincash,betMap);//1是中奖			
//						}
//					}
//				}
//				
////				markSixDAO.updateObject(detail, null);
//			}
//			//批量更新开奖用户实时余额 --by.cuisy.20171209
//			userService.updateUserMoney(userIds);
//		}
//		bet.setTotalPoint(betMap.get("totalPoint"));
//		bet.setWinCash(betMap.get("betCash"));
////		markSixDAO.saveObject(bet, null);
//		return true;
//	}
	
	private int getBetNumValue(String val) {
		if(val.equals("五不中")){
			return 5;
		}else if(val.equals("六不中")){
			return 6;
		}else if(val.equals("七不中")){
			return 7;
		}else if(val.equals("八不中")){
			return 8;
		}else if(val.equals("九不中")){
			return 9;
		}else if(val.equals("十不中")){
			return 10;
		}else if(val.equals("十一不中")){
			return 11;
		}else if(val.equals("十二不中")){
			return 12;
		}else{
			return 0;
		}

	}
	private int getBetNum(String playName) {
		String val=playName.substring(0, 1);
		if(val.equals("二")){
			return 2;
		}else if(val.equals("三")){
			return 3;
		}else if(val.equals("四")){
			return 4;
		}else if(val.equals("五")){
			return 5;
		}else{
			return 0;
		}
		
	}
	
	public void saveOpenResultDetail(String flag,GaBetDetail detail,BigDecimal  totalPoint,BigDecimal  betCash,Map<String,BigDecimal> betMap){		
		String remark = "";
		if(flag.equals("1")){//中奖
			//User user=(User) gaService.getObject(User.class, detail.getUserId());
			detail.setWinResult(GameConstants.WIN);//中奖
			//中奖金额
			BigDecimal wincash=GameHelpUtil.round(detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())));
			detail.setWinCash(wincash);
			//统计
			betMap.put("totalPoint", betMap.get("totalPoint").add(new BigDecimal(detail.getBetMoney())));
			betMap.put("betCash", betMap.get("betCash").add(new BigDecimal(detail.getBetMoney())));
			//盈亏
			detail.setPayoff(GameHelpUtil.round(wincash.subtract(new BigDecimal(detail.getBetMoney()))));
			//备注
			remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
			
			userService.saveTradeDetail(null,detail.getUserId(), 
					Constants.TRADE_TYPE_INCOME,
					Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
					detail.getBetDetailId(), 
					Constants.GAME_TYPE_XY_MARKSIX,
					remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
		}else if(flag.equals("3")){//打和
			detail.setWinResult(GameConstants.WIN_HE);//打和
			//中奖金额
			BigDecimal wincash=GameHelpUtil.round(new BigDecimal(detail.getBetMoney()));//投注总钱数
			detail.setWinCash(wincash);
			//盈亏
			detail.setPayoff(new BigDecimal(0));
			//备注
			remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
			//统计
			betMap.put("totalPoint", betMap.get("totalPoint").add(new BigDecimal(detail.getBetMoney())));
			betMap.put("betCash", betMap.get("betCash").add(new BigDecimal(detail.getBetMoney())));
			
			userService.saveTradeDetail(null,detail.getUserId(), 
					Constants.TRADE_TYPE_INCOME,
					Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
					detail.getBetDetailId(), 
					Constants.GAME_TYPE_XY_MARKSIX,
					remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
		}else{//未中奖
			detail.setWinCash(new BigDecimal(0));
			detail.setWinResult(GameConstants.WIN_NOT);//未中奖
			betMap.put("totalPoint", betMap.get("totalPoint").add(new BigDecimal(detail.getBetMoney())));
		}
		
		//更新
		markSixDAO.saveObject(detail);
		
	}
	public void saveOpenResultDetail(String flag,GaBetDetail detail,BigDecimal  totalPoint,BigDecimal  betCash,BigDecimal wincash,Map<String,BigDecimal> betMap){		
		if(flag.equals("1")){//中奖
			detail.setWinResult(GameConstants.WIN);//中奖
			//中奖金额
			detail.setWinCash(wincash);
			//统计
			betMap.put("totalPoint", betMap.get("totalPoint").add(new BigDecimal(detail.getBetMoney())));
			betMap.put("betCash", betMap.get("betCash").add(new BigDecimal(detail.getBetMoney())));
			//盈亏
			detail.setPayoff(GameHelpUtil.round(wincash.subtract(new BigDecimal(detail.getBetMoney()))));
			
			//备注
			String remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);

			//更新
			markSixDAO.saveObject(detail);
			userService.saveTradeDetail(null,detail.getUserId(), 
					Constants.TRADE_TYPE_INCOME,
					Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
					detail.getBetDetailId(), 
					Constants.GAME_TYPE_XY_MARKSIX,
					remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
		}
	}
//	public void saveOpenResultDetail(String flag,GaBetDetail detail,BigDecimal  totalPoint,BigDecimal  betCash,Map<String,BigDecimal> betMap){		
//		if(flag.equals("1")){//中奖
//			User user=(User) gaService.getObject(User.class, detail.getUserId());
//			detail.setWinResult(MarkSixConstants.MARKSIX_WIN);//中奖
//			BigDecimal wincash=detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP);
//			detail.setWinCash(wincash);
//			betMap.put("totalPoint", betMap.get("totalPoint").add(new BigDecimal(detail.getBetMoney())));
//			betMap.put("betCash", betMap.get("betCash").add(new BigDecimal(detail.getBetMoney())));
////			totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
////			betCash=betCash.add(wincash);
//
//			detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
////			userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_MARKSIX, wincash, detail.getBetDetailId());	
//			StringBuffer remark=new StringBuffer();
//			remark.append("彩票中奖 奖金 ")
//			    .append(wincash).append("元");
//			
////			BigDecimal userBal=null;
////			if(user.getUserBalance()!=null){
////				userBal=user.getUserBalance();
////			}else{
////				userBal=new BigDecimal(0);
////			}
////			user.setUserBalance(userBal.add(wincash).setScale(2, BigDecimal.ROUND_HALF_UP));
////			markSixDAO.updateObject(user,null);
//			user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_MARKSIX,remark.toString());
//		}else if(flag.equals("3")){//打和
//			
//			detail.setWinResult(MarkSixConstants.MARKSIX_WIN_HE);//打和
//			BigDecimal wincash=new BigDecimal(detail.getBetMoney());//投注总钱数
//		
//
//			detail.setWinCash(wincash);
////				userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_MARKSIX, wincash, detail.getBetDetailId());		
//			StringBuffer remark=new StringBuffer();
//			remark.append("彩票打和 退款")
//			    .append(wincash).append("元");
////			BigDecimal userBal=null;
//			User user=(User) gaService.getObject(User.class, detail.getUserId());
////			if(user.getUserBalance()!=null){
////				userBal=user.getUserBalance();
////			}else{
////				userBal=new BigDecimal(0);
////			}
////			user.setUserBalance(userBal.add(wincash));
////				markSixDAO.updateObject(user,null);
//			user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_MARKSIX,remark.toString());
//
////			totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
////			betCash=betCash.add(wincash);
//			betMap.put("totalPoint", betMap.get("totalPoint").add(new BigDecimal(detail.getBetMoney())));
//			betMap.put("betCash", betMap.get("betCash").add(new BigDecimal(detail.getBetMoney())));
//		}else{//未中奖
//			detail.setWinCash(new BigDecimal(0));
//			detail.setWinResult(MarkSixConstants.MARKSIX_WIN_NO);//未中奖
////			totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//			betMap.put("totalPoint", betMap.get("totalPoint").add(new BigDecimal(detail.getBetMoney())));
//		}
//		markSixDAO.saveObject(detail);
//	}
//	public void saveOpenResultDetail(String flag,GaBetDetail detail,BigDecimal  totalPoint,BigDecimal  betCash,BigDecimal wincash,Map<String,BigDecimal> betMap){		
//		if(flag.equals("1")){//中奖
//
//			detail.setWinResult(MarkSixConstants.MARKSIX_WIN);//中奖
////			BigDecimal wincash=new BigDecimal(rate).multiply(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP);
//			detail.setWinCash(wincash);
////			totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
////			betCash=betCash.add(wincash);
//			betMap.put("totalPoint", betMap.get("totalPoint").add(new BigDecimal(detail.getBetMoney())));
//			betMap.put("betCash", betMap.get("betCash").add(new BigDecimal(detail.getBetMoney())));
//		
//			detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
////			userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_MARKSIX, wincash, detail.getBetDetailId());			
//			StringBuffer remark=new StringBuffer();
//			remark.append("彩票中奖 奖金")
//			    .append(wincash).append("元");
////			BigDecimal userBal=null;
//			User user=(User) gaService.getObject(User.class, detail.getUserId());
////			if(user.getUserBalance()!=null){
////				userBal=user.getUserBalance();
////			}else{
////				userBal=new BigDecimal(0);
////			}
////			user.setUserBalance(userBal.add(wincash).setScale(2, BigDecimal.ROUND_HALF_UP));
//			user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_MARKSIX,remark.toString());
//
//			markSixDAO.updateObject(detail, null);
//		}
//	}
	

	private Map<String, Boolean> getType(String[] array) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		int speCode = Integer.parseInt(array[6]);
		if(speCode >=1 && speCode <=  10){
			map.put("1-10", true);
		}else if(speCode >= 11 && speCode <= 20){
			map.put("11-20", true);
		}else if(speCode >= 21 && speCode <= 30){
			map.put("21-30", true);
		}else if(speCode >= 31 && speCode <= 40){
			map.put("31-40", true);
		}else if(speCode >= 41 && speCode <= 49){
			map.put("41-49", true);
		}
		
		if(speCode == 49){
			map.put("和", true);
		}else{
			if(speCode <= 24){
				map.put("小", true);
			}else if(speCode >= 25){
				map.put("大", true);
			}
			
			if(speCode %2 == 0){
				map.put("双", true);
			}else if(speCode %2 == 1){
				map.put("单", true);
			}
		}
		return map;
	}
	
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
		if(sessionNoMap==null || sessionNoMap.isEmpty()) return "fail::no open sessionNo";
		//当前场次及开奖场次处理
		MarkSixGaSession curtSession = markSixDAO.getCurrentSession(new Date());
		if(curtSession==null) return "fail::no current session";
		MarkSixGaSession lastSession = (MarkSixGaSession)markSixDAO.getObject(MarkSixGaSession.class,curtSession.getSessionId()-1);
		if(lastSession==null) return "fail::no last session::id="+curtSession.getSessionId();
		
		//开奖场次期号
		String lastSessionNo = lastSession.getSessionNo();
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_MARKSIX);
		
		//迭代开奖无序
		List<MarkSixGaSession> openedList = new ArrayList<MarkSixGaSession>();//开奖场次
		String sNo;//期号
		SessionItem sessionItem;//开奖结果
		String status;//状态
		String result;//开奖号
		try {
			GameHelpUtil.log(gameCode,"-------- OPENing --------");
			for(Map.Entry<String, SessionItem> entry:sessionNoMap.entrySet()){
				sNo = entry.getKey();sessionItem = entry.getValue();
				MarkSixGaSession session = markSixDAO.getPreviousSessionBySessionNo(sNo);
				if(session!=null){//开奖
					status = session.getOpenStatus();
					result = sessionItem.getResult();
					session.setOpenResult(result);//设置开奖号
					if(status.equals(GameConstants.OPEN_STATUS_INIT) || status.equals(GameConstants.OPEN_STATUS_OPENING)){
						GameHelpUtil.log(gameCode,"Start ... ["+sNo+"],status="+status+",result="+result);
						long timingOpen = System.currentTimeMillis();
						boolean flag = openMarkSixSessionOpenResultMethod(session, result);
						if(flag){
							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());//本系统开奖时间
							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
							markSixDAO.saveObject(session);
							openedList.add(session);
						}
						GameHelpUtil.log(gameCode,"End ... ["+sNo+"]["+(System.currentTimeMillis()-timingOpen)+"ms],status="+session.getOpenStatus()+",result="+result);
					}
				}else{
					GameHelpUtil.log(gameCode,"opening session is null::"+sNo);
				}
			}
			
			//更新彩种场次状态
			long timingGsi = System.currentTimeMillis();
			GaSessionInfo sessionInfo = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_MARKSIX);
			if(sessionInfo!=null){
				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
				if(lastItem!=null){
					sessionInfo.setOpenResult(lastItem.getResult());
					sessionInfo.setOpenSessionNo(lastSessionNo);
					sessionInfo.setEndTime(lastSession.getEndTime());
				}
				sessionInfo.setLatestSessionNo(curtSession.getSessionNo());
				markSixDAO.saveObject(sessionInfo);
				CacheUtil.updateGameList();
				GameHelpUtil.log(gameCode,"gsi last ... ["+(System.currentTimeMillis()-timingGsi)+"ms]"+lastSessionNo+","+lastItem.getResult());
			}
			
			//更新走势 上面已成功开奖的场次
			long startTrending = System.currentTimeMillis();
			for(MarkSixGaSession session:openedList){
				this.updateTrendResult(session);
			}
			GameHelpUtil.log(gameCode,"trend ... ["+openedList.size()+"]["+(System.currentTimeMillis()-startTrending)+"ms]");
			
			sessionNoMap.clear();
			return "success";
		} catch (Exception e) {
			GameHelpUtil.log(gameCode,"open err::=>"+e.getMessage(),e);
			return "err::throw exception...";
		}
	}
	
//	public void updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
//		MarkSixGaSession currentSession= markSixDAO.getCurrentSession(DateTimeUtil.getJavaUtilDateNow());
//		MarkSixGaSession tempsession = (MarkSixGaSession)markSixDAO.getObject(MarkSixGaSession.class, currentSession.getSessionId()-1);
//		String lastSessionNo=tempsession.getSessionNo();
//		
//		if(!sessionNoMap.isEmpty()){
//			GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_MARKSIX);
//			for(String key:sessionNoMap.keySet()){
//				HQUtils hq = new HQUtils("from MarkSixGaSession gks where gks.sessionNo=?");
//				hq.addPars(key);
//				MarkSixGaSession session = (MarkSixGaSession)markSixDAO.getObject(hq);
//				if(session!=null){
//					String openStatus = session.getOpenStatus();//开奖状态
//					if(openStatus.equals(MarkSixConstants.MARKSIX_OPEN_STATUS_INIT) || openStatus.equals(MarkSixConstants.MARKSIX_OPEN_STATUS_OPENING)){//状态为初始化 或 开奖中 则开始开奖
//						//更新开奖结果
//						SessionItem sessionItem = (SessionItem)sessionNoMap.get(key);
//						String openResult = sessionItem.getResult();
//						session.setOpenResult(openResult);//getOpenResultValue()
//						session.setCountResult(openResult);
//						
//						session.setOpenTime(DateTimeUtil.stringToDate(sessionItem.getTime(), "yyyy-MM-dd HH:mm:ss"));
//						session.setOpenStatus(MarkSixConstants.MARKSIX_OPEN_STATUS_OPENED);
//						markSixDAO.saveObject(session);
//						
//						//进入开奖程序方法
//						boolean flag = this.openMarkSixSessionOpenResultMethod(session, openResult);
//						
//						if(flag){
//							if(sessionInfo!=null){
//								if(sessionInfo.getOpenSessionNo()!=null&&tempsession!=null&&Integer.parseInt(sessionInfo.getOpenSessionNo())<Integer.parseInt(tempsession.getSessionNo())){
//									SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
//									if(lastItem!=null&&tempsession!=null){
//										sessionInfo.setOpenResult(lastItem.getResult());//开奖的结果
//										sessionInfo.setOpenSessionNo(tempsession.getSessionNo());//最新开奖的期号
//										sessionInfo.setEndTime(tempsession.getEndTime());//								
//									}
//								}		
//							}
//						}else{
//							
//						}
//					}
//					// 把资金明细里投注记录状态值改为有效
//					userService.updateUserTradeDetailStatus(session.getSessionNo(), 
//							Constants.GAME_TYPE_XY_MARKSIX, Constants.PUB_STATUS_OPEN);
//				}
//			}
//			sessionInfo.setLatestSessionNo(currentSession.getSessionNo());//最新未开奖的期号
//			markSixDAO.saveObject(sessionInfo);
//			CacheUtil.updateGameList();
//		}
//	}

//	public void updateFetchAndOpenResult() {
//		MarkSixGaSession currentSession= markSixDAO.getCurrentSession(new Date());
//		MarkSixGaSession tempsession =markSixDAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession!=null){
//		}else{
//			tempsession=(MarkSixGaSession) markSixDAO.getObject(MarkSixGaSession.class, (currentSession.getSessionId()-1));
//		}
//		final String lastSessionNo=tempsession.getSessionNo();
//		final Map<String,String> sessionNoMap=new HashMap<String,String>();
//		final Map<String,String> timeMap=new HashMap<String,String>();
//		Thread t=new Thread(){
//            public void run(){
//               try {
//            	   int countRun=0;
//            	   out:
//            	   while(true){
//            		   if(countRun==108){
//            			   interrupt();
//            			   break out;
//            		   }
//            		    countRun=countRun+1;
//            	
//	               		//从这里 ---------------------------------------------------------------------------
////	               	    String officalURL = Constants.getGameMarkSixOpenResultUrl();
//            		    GaSessionInfo sessionInfo=CacheUtil.getGameOpenUrl().get("xymarksix"); 		    
//            		    String officalURL ="";
//            		    String urlSwitch=sessionInfo.getUrlSwitch();
//            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//            		    	officalURL = sessionInfo.getKaicaiUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }else if(urlSwitch.equals("2")){
//            		    	officalURL = sessionInfo.getCaipiaoUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }
//            		    
//	               		log.info("___[markSix start fetch result xml data...]________________");
//	               		ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+"___[markSix start fetch result xml data...]________________", Constants.getWebRootPath()+"/gamelogo/markSix.txt", true);
//	             	    sleep(5000);
//	               		String resultXML = URLUtil.HttpRequestUTF8(officalURL);
//	               		
////	               		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+lastSessionNo);
////	             		log.info(resultXML);
////	               		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+lastSessionNo);
//	               		
//	               		//到这里 ---------------------------------------------------------------------------		
//	           
////	            	    ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+resultXML, Constants.getWebRootPath()+"/gamelogo/markSix.txt", true);
//	               		//log.info("___[fetch result xml data]"+resultXML);
//	               		if(ParamUtils.chkString(resultXML)){
//	               			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);
//	               				               			
//	               			//开始解析场次开奖数据
//	               		
//	               			String sessionNo="";//场次号
//	               			String result="";//开奖结果5组数字
//	               			String time="";//开奖结果5组数字
////	               			boolean isnew=false;
//	               			
//	               			if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//	               				NodeList nList = xmlDoc.getElementsByTagName("row");
//		               			for(int i =0;i<nList.getLength();i++){
//		               				Node node = nList.item(i);
//		               				sessionNo = XmlUtil.getElementAttribute((Element)node, "expect");
//		               				result = XmlUtil.getElementAttribute((Element)node, "opencode");
//		               				time = XmlUtil.getElementAttribute((Element)node, "opentime");
////		               				if(ParamUtils.chkString(result)){
////		               					result = result.substring(0, result.indexOf("+"));
////		               				}
////		               				sessionNo=sessionNo.substring(2);
//	           						if(sessionNoMap.get(sessionNo)==null){
//	           							sessionNoMap.put(sessionNo, result);
//	           							timeMap.put(sessionNo, time);
//	           						}
//	           						if(sessionNo.equals(lastSessionNo)){
//	           							interrupt();
//	           							break out;
//	           						}
//		               			}
//	               			}else if(urlSwitch.equals("2")){//1=开彩网  2=彩票控
//	               				NodeList nList = xmlDoc.getElementsByTagName("item");
//	                   			for(int i =0;i<nList.getLength();i++){
//	                   				Node node = nList.item(i);
//	                  				sessionNo = XmlUtil.getElementAttribute((Element)node, "id");
//	                  				time=XmlUtil.getElementAttribute((Element)node, "dateline");  				
//	                  				NodeList employeeMeta = node.getChildNodes(); 
//	                  				result=employeeMeta.item(0).getNodeValue();
//	                  				if(sessionNoMap.get(sessionNo)==null){
//	           							sessionNoMap.put(sessionNo, result);
//	           							timeMap.put(sessionNo, time);
//	           						}
//	           						if(sessionNo.equals(lastSessionNo)){
//	           							interrupt();
//	           							break out;
//	           						}
//	                   			}
//	               			}
//	               		}
//            	   }
//               } catch (Exception e) {
//
//               }
//            }
//        };
//        t.start();
//        
//        try {
//			t.join();//该方法是等 t 线程结束以后再执行后面的代码
//			if(!sessionNoMap.isEmpty()){
//				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_MARKSIX);
//				for(String key:sessionNoMap.keySet()){
//					HQUtils hq = new HQUtils("from MarkSixGaSession gks where gks.sessionNo=?");
//					hq.addPars(key);
//					MarkSixGaSession session = (MarkSixGaSession)markSixDAO.getObject(hq);
//					if(session!=null){
//						String openStatus = session.getOpenStatus();//开奖状态
//						
//						
//						log.info("sessionNo:___________________"+session.getSessionNo()+"   openStatus:___________________"+session.getOpenStatus());
//
//						
//						if(openStatus.equals(MarkSixConstants.MARKSIX_OPEN_STATUS_INIT) || openStatus.equals(MarkSixConstants.MARKSIX_OPEN_STATUS_OPENING)){//状态为初始化 或 开奖中 则开始开奖
//							//更新开奖结果
//							String openResult = sessionNoMap.get(key);
//							session.setOpenResult(openResult);//getOpenResultValue()
//							log.error(DateTimeUtil.DateToStringAll(new Date())+"______________sessionNo:"+key+" openResult:"+openResult);
////							markSixDAO.saveObject(session);
////							String countResult=getOpenResultValue();
//							session.setCountResult(openResult);
//							
//							session.setOpenTime(DateTimeUtil.stringToDate(timeMap.get(key), "yyyy-MM-dd HH:mm:ss"));
//							session.setOpenStatus(MarkSixConstants.MARKSIX_OPEN_STATUS_OPENED);
//							markSixDAO.saveObject(session);
//							
//							//进入开奖程序方法
//							boolean flag = this.openMarkSixSessionOpenResultMethod(session, openResult);
//							
//							if(flag){
////								Map<String,String>  map=this.openResult(openResult);
////								countResult=map.get("number1")+","+map.get("number2")+","+map.get("number3")+","+map.get("number")+","+map.get("colour");
//			
//								//更新场次状态，TODO 盈亏数据也需要在开奖
////								tempsession.setOpenTime(timeMap.get(tempsession.getSessionNo()));
//
//								
//
//								if(sessionInfo!=null){
//									if(sessionInfo.getOpenSessionNo()!=null&&tempsession!=null&&Integer.parseInt(sessionInfo.getOpenSessionNo())<Integer.parseInt(tempsession.getSessionNo())){
//										if(sessionNoMap.get(lastSessionNo)!=null&&tempsession!=null){
//											sessionInfo.setOpenResult(sessionNoMap.get(lastSessionNo));//开奖的结果
//											sessionInfo.setOpenSessionNo(tempsession.getSessionNo());//最新开奖的期号
//											sessionInfo.setEndTime(tempsession.getEndTime());//								
//										}
//									}		
//								}
//								
////								log.info("___[open result success sessionNO["+sessionNo+"]]");
//							}else{
////								log.info("___[open result fail sessionNO["+sessionNo+"], please check...]");
//							}
//						}
//					}
//				}
//				sessionInfo.setLatestSessionNo(currentSession.getSessionNo());//最新未开奖的期号
//				markSixDAO.saveObject(sessionInfo);
//				CacheUtil.updateGameList();
//				sessionNoMap.clear();
//				timeMap.clear();
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	private String getOpenResultValue(){
		String array[]={"01","02","03","04","05","06","07","08","09","10",
				"11","12","13","14","15","16","17","18","19","20",
				"21","22","23","24","25","26","27","28","29","30",
				"31","32","33","34","35","36","37","38","39","40",
				"41","42","43","44","45","46","47","48","49"};
		
		Map<Integer,Integer> map=new HashMap<Integer,Integer>();
		
		int count=0;
		while(count<7){
			int  val = new Random().nextInt(49);
			if(map.get(val)==null){
				count++;
				if(count==6){
					map.put(999, val);
				}else{
					map.put(val, val);
				}
				
			}
		}
		String value="";
		for(Integer key:map.keySet()){
			if(key!=999){
				value=value+array[key]+",";
			}
		}
		value=value.substring(0, value.length()-1);
		value=value+"+"+array[map.get(999)];
		return value;
	}
	
	
	public MarkSixGaSession getCurrentSession(Date now) {
		return markSixDAO.getCurrentSession(now);
	}

	public MarkSixGaSession getPreSession(Date now) {
		return markSixDAO.getPreSession(now);
	}
	
	public MarkSixGaSession getPreviousSessionBySessionNo(String sessionNo) {
		return markSixDAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	public User saveUserBetInfo(String room, Map<Integer, Integer> betMap,
			List<GaBetOption> list, MarkSixGaSession session, User user,
			BigDecimal betAll) {
		//彩种缓存
		GaSessionInfo gsi = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_MARKSIX);
		//投注与明细关联
		List<UserTradeDetailRl> rlList = new ArrayList<UserTradeDetailRl>();
		//用户类型
		String userType = user.getUserType();
		
		List<GaBetDetail> betDetailList=new ArrayList<GaBetDetail>();
		
		BigDecimal backMoney=new BigDecimal(0);//特码B返水金额
		String playType = list.get(0).getPlayType();
		int type = Integer.parseInt(playType);
		
		if(type == 10){// 过关
			GaBetDetail betDetail=new GaBetDetail();
			GaBetOption betOption1 = list.get(0);
			String titles = "";
			String ids = "";
			BigDecimal betRates = new BigDecimal(1);
			for(int i = 0; i< list.size(); i++){
				GaBetOption betOption = list.get(i);
				ids = ids + betOption.getBetOptionId()+",";
				titles = titles + betOption.getTitle() + "," + betOption.getOptionTitle()+";";
				betRates = betRates.multiply(betOption.getBetRate());
			}
			ids = ids.substring(0, ids.length()-1);
			titles = titles.substring(0, titles.length()-1);
			betRates = betRates.setScale(3, BigDecimal.ROUND_HALF_UP);
			
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			betDetail.setSessionId(session.getSessionId());
			betDetail.setUserId(user.getUserId());
			
			betDetail.setType(userType);
			betDetail.setLoginName(user.getLoginName());
			betDetail.setGameName(gsi.getGameTitle());
			betDetail.setGameType(gsi.getGameType());
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
			
			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId()));
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());

			betDetail.setBetRate(betRates);
			betDetail.setOptionIds(ids);
			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
			betDetail.setOptionTitle(titles);
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			markSixDAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);
			
		}else if(type == 11 || type == 12 || type == 13 || type ==31 || type==35 
				|| type==38 || type==41){ //二全中,二中特，特串,二肖连中,二肖连不中,38二尾连中，41二尾连不中
			GaBetDetail betDetail=new GaBetDetail();
			GaBetOption betOption1 = list.get(0);
			String titles = "";
			String ids = "";
			for(int i = 0; i< list.size(); i++){
				GaBetOption betOption = list.get(i);
				ids = ids + betOption.getBetOptionId()+",";
				titles = titles + betOption.getOptionTitle()+",";
			}
			ids = ids.substring(0, ids.length()-1);
			titles = titles.substring(0, titles.length()-1);
			
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			betDetail.setSessionId(session.getSessionId());
			betDetail.setUserId(user.getUserId());
			
			betDetail.setType(userType);
			betDetail.setLoginName(user.getLoginName());
			betDetail.setGameName(gsi.getGameTitle());
			betDetail.setGameType(gsi.getGameType());
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
			
			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), 2));
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());

			betDetail.setBetRate(betOption1.getBetRate());
			betDetail.setOptionIds(ids);
			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
			betDetail.setOptionTitle(titles);
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			markSixDAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);

		}else if(type == 14 || type == 15 || type == 32 || type==36 || type==39|| type== 42){
			//三全中,三中二,三肖连中,三肖连不中,39三尾连中,42三尾连不中
			GaBetDetail betDetail=new GaBetDetail();
			GaBetOption betOption1 = list.get(0);
			String titles = "";
			String ids = "";
			for(int i = 0; i< list.size(); i++){
				GaBetOption betOption = list.get(i);
				ids = ids + betOption.getBetOptionId()+",";
				titles = titles + betOption.getOptionTitle()+",";
			}
			ids = ids.substring(0, ids.length()-1);
			titles = titles.substring(0, titles.length()-1);
			
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			betDetail.setSessionId(session.getSessionId());
			betDetail.setUserId(user.getUserId());
			betDetail.setType(userType);
			betDetail.setLoginName(user.getLoginName());
			betDetail.setGameName(gsi.getGameTitle());
			betDetail.setGameType(gsi.getGameType());
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间

			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), 3));
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());

			betDetail.setBetRate(betOption1.getBetRate());
			betDetail.setOptionIds(ids);
			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
			betDetail.setOptionTitle(titles);
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			markSixDAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);

		}else if(type == 16 || type == 33 || type==37|| type== 40 || type==43){ //四全中,四肖连中,四肖连不中,40四尾连中,43四尾连不中
			GaBetDetail betDetail=new GaBetDetail();
			GaBetOption betOption1 = list.get(0);
			String titles = "";
			String ids = "";
			for(int i = 0; i< list.size(); i++){
				GaBetOption betOption = list.get(i);
				ids = ids + betOption.getBetOptionId()+",";
				titles = titles + betOption.getOptionTitle()+",";
			}
			ids = ids.substring(0, ids.length()-1);
			titles = titles.substring(0, titles.length()-1);
			
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			betDetail.setSessionId(session.getSessionId());
			betDetail.setUserId(user.getUserId());
			
			betDetail.setType(userType);
			betDetail.setLoginName(user.getLoginName());
			betDetail.setGameName(gsi.getGameTitle());
			betDetail.setGameType(gsi.getGameType());
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
			
			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), 4));
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());

			betDetail.setBetRate(betOption1.getBetRate());
			betDetail.setOptionIds(ids);
			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
			betDetail.setOptionTitle(titles);
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			markSixDAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);

		}else if(type == 34){ //五肖连中
			GaBetDetail betDetail=new GaBetDetail();
			GaBetOption betOption1 = list.get(0);
			String titles = "";
			String ids = "";
			for(int i = 0; i< list.size(); i++){
				GaBetOption betOption = list.get(i);
				ids = ids + betOption.getBetOptionId()+",";
				titles = titles + betOption.getOptionTitle()+",";
			}
			ids = ids.substring(0, ids.length()-1);
			titles = titles.substring(0, titles.length()-1);
			
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			betDetail.setSessionId(session.getSessionId());
			betDetail.setUserId(user.getUserId());
			
			betDetail.setType(userType);
			betDetail.setLoginName(user.getLoginName());
			betDetail.setGameName(gsi.getGameTitle());
			betDetail.setGameType(gsi.getGameType());
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
			
			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), 5));
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());

			betDetail.setBetRate(betOption1.getBetRate());
			betDetail.setOptionIds(ids);
			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
			betDetail.setOptionTitle(titles);
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			markSixDAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);

		}else if(type>=44 && type<=51){ // 全不中  5全不中-12全不中
			GaBetDetail betDetail=new GaBetDetail();
			GaBetOption betOption1 = list.get(0);
			String titles = "";
			String ids = "";
			for(int i = 0; i< list.size(); i++){
				GaBetOption betOption = list.get(i);
				ids = ids + betOption.getBetOptionId()+",";
				titles = titles + betOption.getOptionTitle()+",";
			}
			ids = ids.substring(0, ids.length()-1);
			titles = titles.substring(0, titles.length()-1);
			
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			betDetail.setSessionId(session.getSessionId());
			betDetail.setUserId(user.getUserId());
			
			betDetail.setType(userType);
			betDetail.setLoginName(user.getLoginName());
			betDetail.setGameName(gsi.getGameTitle());
			betDetail.setGameType(gsi.getGameType());
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间

			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), type-39));
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());

			betDetail.setBetName(this.getBetNameByOptionType(betOption1.getPlayType(),betOption1.getOptionType().toString()));
			
			betDetail.setBetRate(betOption1.getBetRate());
			betDetail.setOptionIds(ids);
			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
			betDetail.setOptionTitle(titles);
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			markSixDAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);

		}else if(type>=21 && type<=30){ // 合肖
			GaBetDetail betDetail=new GaBetDetail();
			GaBetOption betOption1 = list.get(0);
			String titles = "";
			String ids = "";
			for(int i = 0; i< list.size(); i++){
				GaBetOption betOption = list.get(i);
				ids = ids + betOption.getBetOptionId()+",";
				titles = titles + betOption.getOptionTitle()+",";
			}
			ids = ids.substring(0, ids.length()-1);
			titles = titles.substring(0, titles.length()-1);
			
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			betDetail.setSessionId(session.getSessionId());
			betDetail.setUserId(user.getUserId());
			
			betDetail.setType(userType);
			betDetail.setLoginName(user.getLoginName());
			betDetail.setGameName(gsi.getGameTitle());
			betDetail.setGameType(gsi.getGameType());
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
			
			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId()));
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());
			
			betDetail.setBetName(this.getBetNameByOptionType(betOption1.getPlayType(),betOption1.getOptionType().toString()));
			betDetail.setBetRate(betOption1.getBetRate());
			betDetail.setOptionIds(ids);
			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
			betDetail.setOptionTitle(titles);
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			markSixDAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);

		}else{
			for (int i = 0; i < list.size(); i++) {
				GaBetOption betOption = list.get(i);
				GaBetDetail betDetail=new GaBetDetail();
				if(betOption!=null){
					betDetail.setBetRate(betOption.getBetRate());
				}
				betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
				betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
				betDetail.setSessionId(session.getSessionId());
				betDetail.setUserId(user.getUserId());
				
				betDetail.setType(userType);
				betDetail.setLoginName(user.getLoginName());
				betDetail.setGameName(gsi.getGameTitle());
				betDetail.setGameType(gsi.getGameType());
				betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
				
				betDetail.setBetOptionId(betOption.getBetOptionId());
				betDetail.setBetMoney(betMap.get(betOption.getBetOptionId()));

				if(type==1){//特码B
					BigDecimal betMonty=new BigDecimal(betMap.get(betOption.getBetOptionId()));
					backMoney=backMoney.add(betMonty.multiply(new BigDecimal("0.1")));//特码B返水10%
				}
				
				betDetail.setRoom(room);
				betDetail.setSessionNo(session.getSessionNo());

				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
				betDetail.setPlayName(this.setPlayName(betOption.getPlayType()));
				betDetail.setOptionTitle(betOption.getOptionTitle());
				
				//不能为空字段初始化
				betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
				betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
				betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
				betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
				
				markSixDAO.saveObject(betDetail);
				
				//关联构造
				UserTradeDetailRl rl = new UserTradeDetailRl();
				rl.setBetDetailId(betDetail.getBetDetailId());
				rlList.add(rl);
		  }
	   }
		
		//更新账户信息
		String remark = GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_BUY_LOTO, betAll);
		Integer tradeDetailId = userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll, null, 
				Constants.GAME_TYPE_XY_MARKSIX,remark,session.getSessionNo(),user.getUserType(),user.getLoginName());

		//更新用户实时余额  --by.cuisy.20171209
		userService.updateUserMoney(user.getUserId());
		userService.updateUserBanlance(user.getUserId());
		
		//保存关联
		for(UserTradeDetailRl rl:rlList){
			rl.setTradeDetailId(tradeDetailId);
			rl.setGfxy(Constants.GAME_PLAY_CATE_XY);
		}
	
		markSixDAO.updateObjectList(rlList, null);
		
		if(type==1&&backMoney.compareTo(new BigDecimal("0"))>0){//六合彩特码B返水
			remark = GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_BUY_LOTO, betAll);
			tradeDetailId = userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF, betAll, null, 
					Constants.GAME_TYPE_XY_MARKSIX,remark,session.getSessionNo(),user.getUserType(),user.getLoginName());
		}
		
		return user;
	}
	
//	public User saveUserBetInfo(String room, Map<Integer, Integer> betMap,
//			List<GaBetOption> list, MarkSixGaSession session, User user,
//			BigDecimal betAll) {
//
//		List<GaBetDetail> betDetailList=new ArrayList<GaBetDetail>();
//		BigDecimal backMoney=new BigDecimal(0);//特码B返水金额
//		String playType = list.get(0).getPlayType();
//		int type = Integer.parseInt(playType);
//		
//		if(type == 10){// 过关
//			GaBetDetail betDetail=new GaBetDetail();
//			GaBetOption betOption1 = list.get(0);
//			String titles = "";
//			String ids = "";
//			BigDecimal betRates = new BigDecimal(1);
//			for(int i = 0; i< list.size(); i++){
//				GaBetOption betOption = list.get(i);
//				ids = ids + betOption.getBetOptionId()+",";
//				titles = titles + betOption.getTitle() + "," + betOption.getOptionTitle()+";";
//				betRates = betRates.multiply(betOption.getBetRate());
//			}
//			ids = ids.substring(0, ids.length()-1);
//			titles = titles.substring(0, titles.length()-1);
//			betRates = betRates.setScale(3, BigDecimal.ROUND_HALF_UP);
//			
//			betDetail.setWinResult("0");//未开奖
//			betDetail.setBetFlag("1");//有效投注
//			betDetail.setSessionId(session.getSessionId());
//			betDetail.setUserId(user.getUserId());
//			betDetail.setBetTime(new Date());
//			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId()));
//			betDetail.setRoom(room);
//			betDetail.setSessionNo(session.getSessionNo());
//			betDetail.setGameName("香港六合彩");
//
//			betDetail.setBetRate(betRates);
//			betDetail.setOptionIds(ids);
//			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
//			betDetail.setOptionTitle(titles);
//			betDetail.setGameType(Constants.GAME_TYPE_XY_MARKSIX);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			betDetailList.add(betDetail);
//		}else if(type == 11 || type == 12 || type == 13 || type ==31 || type==35 
//				|| type==38 || type==41){ //二全中,二中特，特串,二肖连中,二肖连不中,38二尾连中，41二尾连不中
//			GaBetDetail betDetail=new GaBetDetail();
//			GaBetOption betOption1 = list.get(0);
//			String titles = "";
//			String ids = "";
//			for(int i = 0; i< list.size(); i++){
//				GaBetOption betOption = list.get(i);
//				ids = ids + betOption.getBetOptionId()+",";
//				titles = titles + betOption.getOptionTitle()+",";
//			}
//			ids = ids.substring(0, ids.length()-1);
//			titles = titles.substring(0, titles.length()-1);
//			
//			betDetail.setWinResult("0");//未开奖
//			betDetail.setBetFlag("1");//有效投注
//			betDetail.setSessionId(session.getSessionId());
//			betDetail.setUserId(user.getUserId());
//			betDetail.setBetTime(new Date());
//			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), 2));
//			betDetail.setRoom(room);
//			betDetail.setSessionNo(session.getSessionNo());
//			betDetail.setGameName("香港六合彩");
//
//			betDetail.setBetRate(betOption1.getBetRate());
//			betDetail.setOptionIds(ids);
//			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
//			betDetail.setOptionTitle(titles);
//			betDetail.setGameType(Constants.GAME_TYPE_XY_MARKSIX);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			betDetailList.add(betDetail);
//
//		}else if(type == 14 || type == 15 || type == 32 || type==36 || type==39|| type== 42){
//			//三全中,三中二,三肖连中,三肖连不中,39三尾连中,42三尾连不中
//			GaBetDetail betDetail=new GaBetDetail();
//			GaBetOption betOption1 = list.get(0);
//			String titles = "";
//			String ids = "";
//			for(int i = 0; i< list.size(); i++){
//				GaBetOption betOption = list.get(i);
//				ids = ids + betOption.getBetOptionId()+",";
//				titles = titles + betOption.getOptionTitle()+",";
//			}
//			ids = ids.substring(0, ids.length()-1);
//			titles = titles.substring(0, titles.length()-1);
//			
//			betDetail.setWinResult("0");//未开奖
//			betDetail.setBetFlag("1");//有效投注
//			betDetail.setSessionId(session.getSessionId());
//			betDetail.setUserId(user.getUserId());
//			betDetail.setBetTime(new Date());
//			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), 3));
//			betDetail.setRoom(room);
//			betDetail.setSessionNo(session.getSessionNo());
//			betDetail.setGameName("香港六合彩");
//
//			betDetail.setBetRate(betOption1.getBetRate());
//			betDetail.setOptionIds(ids);
//			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
//			betDetail.setOptionTitle(titles);
//			betDetail.setGameType(Constants.GAME_TYPE_XY_MARKSIX);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			betDetailList.add(betDetail);
//
//		}else if(type == 16 || type == 33 || type==37|| type== 40 || type==43){ //四全中,四肖连中,四肖连不中,40四尾连中,43四尾连不中
//			GaBetDetail betDetail=new GaBetDetail();
//			GaBetOption betOption1 = list.get(0);
//			String titles = "";
//			String ids = "";
//			for(int i = 0; i< list.size(); i++){
//				GaBetOption betOption = list.get(i);
//				ids = ids + betOption.getBetOptionId()+",";
//				titles = titles + betOption.getOptionTitle()+",";
//			}
//			ids = ids.substring(0, ids.length()-1);
//			titles = titles.substring(0, titles.length()-1);
//			
//			betDetail.setWinResult("0");//未开奖
//			betDetail.setBetFlag("1");//有效投注
//			betDetail.setSessionId(session.getSessionId());
//			betDetail.setUserId(user.getUserId());
//			betDetail.setBetTime(new Date());
//			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), 4));
//			betDetail.setRoom(room);
//			betDetail.setSessionNo(session.getSessionNo());
//			betDetail.setGameName("香港六合彩");
//
//			betDetail.setBetRate(betOption1.getBetRate());
//			betDetail.setOptionIds(ids);
//			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
//			betDetail.setOptionTitle(titles);
//			betDetail.setGameType(Constants.GAME_TYPE_XY_MARKSIX);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			betDetailList.add(betDetail);
//
//		}else if(type == 34){ //五肖连中
//			GaBetDetail betDetail=new GaBetDetail();
//			GaBetOption betOption1 = list.get(0);
//			String titles = "";
//			String ids = "";
//			for(int i = 0; i< list.size(); i++){
//				GaBetOption betOption = list.get(i);
//				ids = ids + betOption.getBetOptionId()+",";
//				titles = titles + betOption.getOptionTitle()+",";
//			}
//			ids = ids.substring(0, ids.length()-1);
//			titles = titles.substring(0, titles.length()-1);
//			
//			betDetail.setWinResult("0");//未开奖
//			betDetail.setBetFlag("1");//有效投注
//			betDetail.setSessionId(session.getSessionId());
//			betDetail.setUserId(user.getUserId());
//			betDetail.setBetTime(new Date());
//			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), 5));
//			betDetail.setRoom(room);
//			betDetail.setSessionNo(session.getSessionNo());
//			betDetail.setGameName("香港六合彩");
//
//			betDetail.setBetRate(betOption1.getBetRate());
//			betDetail.setOptionIds(ids);
//			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
//			betDetail.setOptionTitle(titles);
//			betDetail.setGameType(Constants.GAME_TYPE_XY_MARKSIX);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			betDetailList.add(betDetail);
//
//		}else if(type>=44 && type<=51){ // 全不中  5全不中-12全不中
//			GaBetDetail betDetail=new GaBetDetail();
//			GaBetOption betOption1 = list.get(0);
//			String titles = "";
//			String ids = "";
//			for(int i = 0; i< list.size(); i++){
//				GaBetOption betOption = list.get(i);
//				ids = ids + betOption.getBetOptionId()+",";
//				titles = titles + betOption.getOptionTitle()+",";
//			}
//			ids = ids.substring(0, ids.length()-1);
//			titles = titles.substring(0, titles.length()-1);
//			
//			betDetail.setWinResult("0");//未开奖
//			betDetail.setBetFlag("1");//有效投注
//			betDetail.setSessionId(session.getSessionId());
//			betDetail.setUserId(user.getUserId());
//			betDetail.setBetTime(new Date());
//			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId())*MarkSixUtil.getTotal(list.size(), type-39));
//			betDetail.setRoom(room);
//			betDetail.setSessionNo(session.getSessionNo());
//			betDetail.setGameName("香港六合彩");
//
//			betDetail.setBetName(this.getBetNameByOptionType(betOption1.getPlayType(),betOption1.getOptionType().toString()));
//			
//			betDetail.setBetRate(betOption1.getBetRate());
//			betDetail.setOptionIds(ids);
//			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
//			betDetail.setOptionTitle(titles);
//			betDetail.setGameType(Constants.GAME_TYPE_XY_MARKSIX);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			betDetailList.add(betDetail);
//
//		}else if(type>=21 && type<=30){ // 合肖
//			GaBetDetail betDetail=new GaBetDetail();
//			GaBetOption betOption1 = list.get(0);
//			String titles = "";
//			String ids = "";
//			for(int i = 0; i< list.size(); i++){
//				GaBetOption betOption = list.get(i);
//				ids = ids + betOption.getBetOptionId()+",";
//				titles = titles + betOption.getOptionTitle()+",";
//			}
//			ids = ids.substring(0, ids.length()-1);
//			titles = titles.substring(0, titles.length()-1);
//			
//			betDetail.setWinResult("0");//未开奖
//			betDetail.setBetFlag("1");//有效投注
//			betDetail.setSessionId(session.getSessionId());
//			betDetail.setUserId(user.getUserId());
//			betDetail.setBetTime(new Date());
//			betDetail.setBetMoney(betMap.get(betOption1.getBetOptionId()));
//			betDetail.setRoom(room);
//			betDetail.setSessionNo(session.getSessionNo());
//			betDetail.setGameName("香港六合彩");
//			
//			betDetail.setBetName(this.getBetNameByOptionType(betOption1.getPlayType(),betOption1.getOptionType().toString()));
//			
//			betDetail.setBetRate(betOption1.getBetRate());
//			betDetail.setOptionIds(ids);
//			betDetail.setPlayName(this.setPlayName(betOption1.getPlayType()));
//			betDetail.setOptionTitle(titles);
//			betDetail.setGameType(Constants.GAME_TYPE_XY_MARKSIX);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			betDetailList.add(betDetail);
//
//		}else{
//			for (int i = 0; i < list.size(); i++) {
//				GaBetOption betOption = list.get(i);
//				GaBetDetail betDetail=new GaBetDetail();
//				if(betOption!=null){
//					betDetail.setBetRate(betOption.getBetRate());
//				}
//				betDetail.setWinResult("0");//未开奖
//				betDetail.setBetFlag("1");//有效投注
//				betDetail.setSessionId(session.getSessionId());
//				betDetail.setUserId(user.getUserId());
//				betDetail.setBetOptionId(betOption.getBetOptionId());
//				betDetail.setBetTime(new Date());
//				betDetail.setBetMoney(betMap.get(betOption.getBetOptionId()));
//
//				if(type==1){//特码B
//					BigDecimal betMonty=new BigDecimal(betMap.get(betOption.getBetOptionId()));
//					backMoney=backMoney.add(betMonty.multiply(new BigDecimal("0.1")));//特码B返水10%
//				}
//				
//				betDetail.setRoom(room);
//				betDetail.setSessionNo(session.getSessionNo());
//				betDetail.setGameName("香港六合彩");
//
//				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
//				betDetail.setPlayName(this.setPlayName(betOption.getPlayType()));
//				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
//				betDetail.setOptionTitle(betOption.getOptionTitle());
//				betDetail.setGameType(Constants.GAME_TYPE_XY_MARKSIX);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//				betDetailList.add(betDetail);
//		  }
//	   }
//		
//	
//		
////			user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_MARKSIX, betAll.subtract(tempMoney), null);
//		StringBuilder remark = new StringBuilder();
//		remark.append("购买彩票 扣款 ")
//		    .append(betAll).append("元");
////		BigDecimal userBalance=user.getUserBalance();
////		if(userBalance==null){
////			userBalance=new BigDecimal(0);
////		}
////		user.setUserBalance(userBalance.subtract(betAll));
////		
////		BigDecimal dayBet=user.getDayBet();
////		if(dayBet==null){
////			dayBet=new BigDecimal(0);
////		}
////		user.setDayBet(dayBet.add(betAll));
//		Integer userId=user.getUserId();
//		user=userService.saveTradeDetail(null,userId, Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll, null, Constants.GAME_TYPE_XY_MARKSIX,remark.toString());
//
//		if(type==1&&backMoney.compareTo(new BigDecimal("0"))>0){
//			StringBuilder remark1 = new StringBuilder();
//			remark1.append("购买六合彩特码B返水，金额： ")
//			    .append(backMoney).append("元");
//			user=userService.saveTradeDetail(null,userId, Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF, backMoney, null, Constants.GAME_TYPE_XY_MARKSIX,remark1.toString());
//		}
//		
//		//更新用户实时余额  --by.cuisy.20171209
//		userService.updateUserMoney(userId);		
//		userService.updateUserBanlance(userId);
//		
//		markSixDAO.updateObjectList(betDetailList, null);
//	
//		return user;
//	}
	
	
	
	private String setPlayName(String playType) {
		Map<String, String> map = CacheUtil.getPlayName();		
		return map.get(playType);
	}
	public List<MarkSixGaTrend> findMarkSixTrendList() {
		return markSixDAO.findMarkSixGaTrendList();
	}
	
	public PaginationSupport findMarkSixGaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		return markSixDAO.findMarkSixGaSessionList(hql,para,pageNum,pageSize);
	}
	
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals("9")||playType.equals("10")){//正码1-6  过关
			 if(optionType.equals("0")){
						return "正码1"; 
			 }else if(optionType.equals("1")){
					return "正码2"; 
			 }else if(optionType.equals("2")){
					return "正码3"; 
			 }else if(optionType.equals("3")){
					return "正码4"; 
			 }else if(optionType.equals("4")){
					return "正码5"; 
			 }else if(optionType.equals("5")){
					return "正码6"; 
			 }
		 }else{
			if(optionType.equals("0")){
				return "号码";
			}else if(optionType.equals("1")){
				return "类型";
			}
		}
		 return "";
	 }
	 
	 public void updateTrendResult(MarkSixGaSession session){
		if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
			List<MarkSixGaTrend> list=markSixDAO.findMarkSixGaTrendAllList();
			List<MarkSixGaTrend> savelist=new ArrayList<MarkSixGaTrend>();
			Map<String,Boolean> map=getTrendResult(session.getOpenResult(),session.getStartTime());;
			if(!map.isEmpty()){
				for(int i=0;i<list.size();i++){
					MarkSixGaTrend trend=list.get(i);
					if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
						trend.setTrendCount(trend.getTrendCount()+1);				
					}else{
						trend.setTrendCount(0);
					}
					savelist.add(trend);
				}
				markSixDAO.updateObjectList(savelist, null);
			}
		}
	}
	 
//	 public void updateFetchAndOpenTrendResult(Integer count){
//		 
//			if(count==9){//执行10次
//				count=null;
//				return;
//			}
//			String lastSessionNo=(Integer.parseInt(markSixDAO.getCurrentSession(new Date()).getSessionNo())-1)+"";
//			MarkSixGaSession session =markSixDAO.getPreviousSessionBySessionNo(lastSessionNo);
//			if(!DateTimeUtil.DateToString(session.getEndTime()).equals(DateTimeUtil.DateToString(new Date()))){
//				return ;
//			}
//			if(session.getOpenStatus().equals(MarkSixConstants.MARKSIX_OPEN_STATUS_OPENED)){
//				List<MarkSixGaTrend> list=markSixDAO.findMarkSixGaTrendAllList();
//				List<MarkSixGaTrend> savelist=new ArrayList<MarkSixGaTrend>();
//				Map<String,Boolean> map=getTrendResult(session.getOpenResult(),session.getStartTime());;
//				if(!map.isEmpty()){
//					for(int i=0;i<list.size();i++){
//						MarkSixGaTrend trend=list.get(i);
//						if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
//							trend.setTrendCount(trend.getTrendCount()+1);				
//						}else{
//							trend.setTrendCount(0);
//						}
//						savelist.add(trend);
//					}
//					markSixDAO.updateObjectList(savelist, null);
//					count=null;
//					lastSessionNo=null;
//					session=null;
//					return;
//				}
//
//			}else{
//				Thread t1=new Thread(){
//		            public void run(){
//		               try {
//		            	   sleep(3000);
//		            	   interrupt();
//		               } catch (Exception e) {
//		            	   interrupt();
//		               }
//		            }
//				};
//				t1.start();
//				try {
//					t1.join();
//					t1=null;
//					count++;
//					lastSessionNo=null;
//					session=null;
//					updateFetchAndOpenTrendResult(count);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	 
	private Map<String, Boolean> getTrendResult(String openResult,Date startTime) {
		return MarkSixUtil.getTeResult(openResult,startTime);
	}
		public boolean saveOpenResult(MarkSixGaSession session,String openResult){
			boolean flag=false;
//			session.setOpenResult(openResult);
			session.setCountResult(openResult);
			markSixDAO.updateObject(session, null);
			flag=true;
			return flag;
		}
		
		public boolean saveAndOpenResult(MarkSixGaSession session,String openResult){

			boolean flag=false;
			session.setOpenResult(openResult); //这里是用户手动输入的值。没有经过计算。
			boolean flag1 = openMarkSixSessionOpenResultMethod(session, session.getOpenResult());
			if(flag1){      
				session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
				session.setOpenStatus(MarkSixConstants.MARKSIX_OPEN_STATUS_OPENED);
				markSixDAO.updateObject(session, null);
				log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
					flag=true;
			}else{
				log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
			}
			return flag;
		}
		@Override
		public PaginationSupport findMarkSixGaBetList(String hql,
				List<Object> para, int pageNum, int pageSize) {
			return markSixDAO.findMarkSixGaBetList(hql, para, pageNum, pageSize);
		}

		@Override
		public PaginationSupport findGaBetDetail(String hql, List<Object> para,
				int pageNum, int pageSize) {
			return markSixDAO.findGaBetDetail(hql, para, pageNum, pageSize);
		}
		@Override
		public List<MarkSixDTO> findGaBetDetailById(String hql,
				List<Object> para) {
			return markSixDAO.findGaBetDetailById(hql, para);
		}
		
		@Override
		public boolean modifyDate(MarkSixGaSession session,
				String endTime) {
			String sessinNo = session.getSessionNo();
			int nextSessionNo = Integer.parseInt(sessinNo) + 1;
			Date firthDay = DateTimeUtil.getDateAfter(DateTimeUtil.parse(endTime), 2,"yyyy-MM-dd HH:mm:ss");
			Date lastDay = null;
			try {
				lastDay = new SimpleDateFormat("yyyy-MM-dd").parse(DateTimeUtil.getRightYear() + "-12-31");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int days = DateTimeUtil.daysBetween(firthDay, lastDay);
			for(int i=0; i<days; i++){
				
				Date dateAfter = DateTimeUtil.getDateAfter(firthDay, i, "yyyy-MM-dd HH:mm:ss");
				Calendar c = Calendar.getInstance();  
				 c.setTime(dateAfter);  
				 int dayForWeek = 0;  
				 if(c.get(Calendar.DAY_OF_WEEK) == 1){  
				  dayForWeek = 7;  
				 }else{  
				  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
				 }				 

				 if(dayForWeek == 2 || dayForWeek == 4 || dayForWeek == 6){
						int sessionNo = nextSessionNo;
						nextSessionNo = nextSessionNo + 1;
						int beforDays = i-2;
						if(dayForWeek == 2){
							beforDays = i-3;
						}
						Date dateAfter2 = DateTimeUtil.getDateAfter(firthDay, beforDays, "yyyy-MM-dd HH:mm:ss");   
						String startTime2;
						if(sessionNo == (Integer.parseInt(sessinNo) + 1)){
							startTime2 = endTime; //开盘时间
						}else{
							startTime2 = DateTimeUtil.DateToString(dateAfter2) + " 22:30:00"; //开盘时间
						}
						String endTime2 = DateTimeUtil.DateToString(dateAfter) + " 21:30:00"; // 封盘时间
//						System.out.println(sessionNo);
//						System.out.println(startTime2);
//						System.out.println(endTime2);
//						System.out.println();

						MarkSixGaSession markSixGaSession = markSixDAO.getPreviousSessionBySessionNo(sessionNo+"");
						markSixGaSession.setStartTime(DateTimeUtil.parse(startTime2));
						markSixGaSession.setEndTime(DateTimeUtil.parse(endTime2));
						markSixDAO.updateObject(markSixGaSession, null);
				 }
			}	
			return true;
		}
		@Override
		public boolean saveRevokePrize(MarkSixGaSession session) {
			//删除MarkSixGaBet表的记录
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and sessionId = ? ");
			para.add(session.getSessionId());
			markSixDAO.deleteMarkSixGaBet(hql.toString(),para);

			boolean result = gaService.saveXyRevokePrize(session.getSessionId(), Constants.GAME_TYPE_XY_MARKSIX,session.getSessionNo());
			if(result){
				session.setOpenStatus(Constants.OPEN_STATUS_INIT);//未开奖
				gaService.saveObject(session, null);
			}
			return result;
		}
}
