package com.xy.k10.gdk10.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.game.model.GaBetDetail;

import java.math.BigDecimal;

   /**
    * GdK10GaBet 实体类
    * author:	ch
    * createDate:	2017-03-23 18:29:40
    */ 


public class GdK10GaBet{
	private Integer betId;
	private Integer userId;
	private Integer sessionId;
	private Integer totalNum;
	private BigDecimal totalPoint;
	private BigDecimal winCash;
	private Date betTime;
	private String sessionNo;
	public void setBetId(Integer betId){
	this.betId=betId;
	}
	public Integer getBetId(){
		return betId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setSessionId(Integer sessionId){
	this.sessionId=sessionId;
	}
	public Integer getSessionId(){
		return sessionId;
	}
	public void setTotalNum(Integer totalNum){
	this.totalNum=totalNum;
	}
	public Integer getTotalNum(){
		return totalNum;
	}
	public void setTotalPoint(BigDecimal totalPoint){
	this.totalPoint=totalPoint;
	}
	public BigDecimal getTotalPoint(){
		return totalPoint;
	}
	public void setWinCash(BigDecimal winCash){
	this.winCash=winCash;
	}
	public BigDecimal getWinCash(){
		return winCash;
	}
	public void setBetTime(Date betTime){
	this.betTime=betTime;
	}
	public Date getBetTime(){
		return betTime;
	}
	public String getSessionNo() {
		return sessionNo;
	}
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
	
	


	
	public static void main(String[] args) {
		
		GdK10GaBet test = new GdK10GaBet();
		String results = "06,07,14,15,18";
		GaBetDetail detail = new GaBetDetail();
		detail.setPlayName("两面盘");
		detail.setBetName("第1球");
//		detail.setOptionTitle("白");
//		detail.setOptionTitle("发");
		detail.setOptionTitle("任选二");
		
		System.out.println(test.judgeWin(results, detail));
//		String value = "04";
//		Map<String,Boolean> map = test.getSingleBallResult(value);
//		System.out.println(map);
		
	}
	
	
	public String  judgeWin(String results,GaBetDetail detail){
		String array[]=results.split(",");//拆分结果
		if(detail.getPlayName().equals("两面盘")){//先用中文比对吧  后续改进
			if(detail.getBetName().equals("总和/龙虎")){
				Map<String,Boolean>  map=getResult(array);
				if(detail.getOptionTitle().equals("总和大")||detail.getOptionTitle().equals("总和小")){
					if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
						return "2";
					}else{
						if(map.get("和")!=null&&map.get("和")){
							return "1";
						}else{
							return "0";
						}
					}
				}else{
					if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
						return "2";
					}else{
						return "0";
					}
				}
			}else{
				int seq=Integer.parseInt(detail.getBetName().substring(1, detail.getBetName().length()-1));
				Map<String,Boolean>  map=getSingleBallResult(array[seq-1]);
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return "2";
				}else{
					return "0";
				}
			}
		}else if(detail.getPlayName().equals("连码")){//先用中文比对吧  后续改进
			int weight = 2;
			// 0:任选 1:二组
			int type = 0;
			
			if(detail.getBetName().equals("任选二")){
				// 任选二
				weight = 2;
			}else if(detail.getBetName().equals("任选二组")){
				// 任选二组
				weight = 2;
				type = 1;
			}else if(detail.getBetName().equals("任选三")){
				// 任选三
				weight = 3;
			}else if(detail.getBetName().equals("任选四")){
				// 任选四
				weight = 4;
			}else {
				// 任选五
				weight = 5;
			}
			int winNum = 0;
			int num = 0;
			String tmpArr[]=detail.getOptionTitle().split(",");
			// 对投注进行比较
			if(type==0){
				// 任选，不需要位置连续
				for(int i = 0;i<tmpArr.length;i++){
					if(tmpArr[i].equals(array[0])
							||tmpArr[i].equals(array[1])
							||tmpArr[i].equals(array[2])
							||tmpArr[i].equals(array[3])
							||tmpArr[i].equals(array[4])
							||tmpArr[i].equals(array[5])
							||tmpArr[i].equals(array[6])
							||tmpArr[i].equals(array[7])){
						num++;
					}
				}
				
				if(num>0){
					if(num<weight){
						winNum = 0;
					}else if(num==weight){
						winNum = 1;
					}else{
						int numerator = 1;
						for(int i = 0;i<weight;i++){
							numerator = numerator * (num-i);
						}
						int denominator = 1;
						for(int i = weight;i>0;i--){
							denominator = denominator * i;
						}
						winNum = numerator / denominator;
					}
				}
			}else{
				List<String> a = new ArrayList<String>();
				
				for(int i = 0;i<tmpArr.length;i++){
					a.add(tmpArr[i]);
				}
				Map<String,Boolean> map = new HashMap<String, Boolean>();
				String result = "";
				// 获取所有投注
				combind(a,weight,map,result);
				Set<String> keys = map.keySet();
				
				// 任选二组，需要两(?)个连续位置，顺序不限
				for(String key:keys){
					num = 0;
					String arr[]=key.split(",");
					for(int i = 0;i<array.length;i++){
						if(arr[0].equals(array[i])){
							if(i==0){
								if(arr[1].equals(array[i+1])){
									num ++;
								}
							}else if(i==array.length-1){
								if(arr[1].equals(array[i-1])){
									num ++;
								}
							}else{
								if(arr[1].equals(array[i-1])||arr[1].equals(array[i+1])){
									num ++;
								}
							}
						}
					}
					winNum += num;
				}
			}
			
			return ""+winNum;
		}else{//第1球~第8球
			int seq=Integer.parseInt(detail.getBetName().substring(1, detail.getBetName().length()-1));
			int value=Integer.parseInt(array[seq-1]);//第几球的值
			//下注的具体是几号
			int index=Integer.parseInt(detail.getOptionTitle().replaceAll("号", ""));//把几号的号字去掉只保留数字
			if(value==index){
				return "2";
			}else{
				return "0";
			}
		}	
	}
	/**
	 *  两面盘 根据传递的数字判断是否中奖  
	 * secondNum可传0，主要是判断1-5龙虎用的这个
	 */
	public Map<String,Boolean> getResult(int firstNum,int secondNum){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		if(firstNum>=6){
			map.put("大", true);
		}else{
			map.put("小", true);
		}
		if(firstNum%2==0){
			map.put("双", true);
		}else{
			map.put("单", true);
		}
		if(secondNum>0){
			if(firstNum>secondNum){
				map.put("龙", true);
			}else{
				map.put("虎", true);
			}
		}
		return map;
	}
	
	
	/**
	 *  总和龙虎 
	 * 
	 */
	public Map<String,Boolean> getResult(String[]  array){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		int sum=0;
		for(int i=0;i<array.length;i++){
			sum=sum+Integer.parseInt(array[i]);
		}
		if(sum%2==0){
			map.put("总和双", true);
		}else{
			map.put("总和单", true);
		}
		if(sum>=85&&sum<=132){
			map.put("总和大", true);
		}else if(sum>=36&&sum<=83){
			map.put("总和小", true);
		}else if(sum==84){
			map.put("和", true);
		}
		if(sum>=100){
			int val=sum%100%10;
			if(val>=5){
				map.put("总和尾大", true);
			}else{
				map.put("总和尾小", true);
			}
		}else{
			int val=sum%10;
			if(val>=5){
				map.put("总和尾大", true);
			}else{
				map.put("总和尾小", true);
			}
		}
		if(Integer.parseInt(array[0])>Integer.parseInt(array[7])){
			map.put("龙", true);
		}else{
			map.put("虎", true);
		}
		return map;
	}
	/**
	 *  两面盘 第1-8球的中奖结果
	 * 
	 */
	public Map<String,Boolean> getSingleBallResult(String value){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		int sum=Integer.parseInt(value);
		if(sum%2==0){
			map.put("双", true);
		}else{
			map.put("单", true);
		}
		if(sum>=11){
			map.put("大", true);
		}else if(sum<=10){
			map.put("小", true);
		}
		if(sum>=10){
			int val=sum%10;
			if(val>=5){
				map.put("尾大", true);
			}else{
				map.put("尾小", true);
			}
		}else{
			int val=sum;
			if(val>=5){
				map.put("尾大", true);
			}else{
				map.put("尾小", true);
			}
		}
		if(sum==1||sum==5||sum==9||sum==13||sum==17){
			map.put("东", true);
		}else if(sum==2||sum==6||sum==10||sum==14||sum==18){
			map.put("南", true);
		}else if(sum==3||sum==7||sum==11||sum==15||sum==19){
			map.put("西", true);
		}else if(sum==4||sum==8||sum==12||sum==16||sum==20){
			map.put("北", true);
		}
		
		if(sum==1||sum==2||sum==3||sum==4||sum==5||sum==6||sum==7){
			map.put("中", true);
		}else if(sum==8||sum==9||sum==10||sum==11||sum==12||sum==13||sum==14){
			map.put("发", true);
		}else if(sum==15||sum==16||sum==17||sum==18||sum==19||sum==20){
			map.put("白", true);
		}
		if(value.length()==2){
			String c1 = value.substring(0, 1);
			String c2 = value.substring(1, 2);
			Integer sumC = Integer.parseInt(c1)+Integer.parseInt(c2);
			if(sumC%2==1){
				map.put("合单", true);
			}else if(sumC%2==0){
				map.put("合双", true);
			}
		}
		
		
		return map;
	}
	// 排列组合生成投注
	public String combind(List<String> a,int n,Map<String,Boolean> map,String result){
		if(n>0){
			for(int i =0;i<a.size();i++){
				List<String> copyA = new ArrayList<String>();
				String copyResult = "";
				// 拷贝一份
				for(String number:a){
					copyA.add(number);
				}
				String number = a.get(i);
				if(result.length()==0){
					copyResult += number;
				}else{
					copyResult += result +","+number;
				}
				
				
				if(n==1){
					copyResult = popSort(copyResult);
					map.put(copyResult, true);
				}else{
					copyA.remove(i);
					combind(copyA,n-1,map,copyResult);
				}
			}
		}else{
			System.out.println(result);
			return result;
		}
		
		
		return null;
	}
	public String popSort(String nums){
		
		String [] arr = nums.split(",");
		
		for(int i = 0;i<arr.length;i++){
			for(int j=0;j<arr.length-1-i;j++){
				if(Integer.parseInt(arr[j])>Integer.parseInt(arr[j+1])){
					String tmp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = tmp;
				}
			}
		}
		String result = ""+arr[0];
		for(int i = 1;i<arr.length;i++){
			result = result+ ","+ arr[i];
		}
		return result;
	}
	
}
