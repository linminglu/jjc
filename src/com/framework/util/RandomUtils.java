package com.framework.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

import com.apps.eff.CacheUtil;
import com.game.model.GaSessionInfo;

public class RandomUtils {
  private static Random random = new Random();

  private static RandomUtils instance = new RandomUtils();

  public static RandomUtils getInstance (){
    return instance;
  }

  private RandomUtils() {
  }


  public static int getNext(int n) {
          int randomInt = -1;
          randomInt = random.nextInt(10);
          return randomInt;

  }

  public static int[] getYanzhengma() {
          int[] yanzhengma = { 0, 0, 0, 0 };
          for (int i = 0; i < yanzhengma.length; i++) {
                  yanzhengma[i] = getNext(10);
          }

          return yanzhengma;
  }

  public static String getUserID() {
	  String userId="";
      for (int i = 0; i < 5; i++) {
              int val = getNext(10);
              if(i==0){
            	  val = random.nextInt(4);
            	  if(val<=0){
            		  userId=userId+"";
            	  }
              }else{
            	  userId=userId+val;
              }            
      }
      return userId;
  }
  
  public static int getGameNext() {
      int randomInt = -1;
      randomInt = random.nextInt(13);
      List<GaSessionInfo> list=CacheUtil.getGameList();
      
      return randomInt;
  }
  
  public static String getGameTitle() {
      int randomInt = 0;
      List<GaSessionInfo> list=CacheUtil.getGameList();
      randomInt = random.nextInt(list.size());
      return list.get(randomInt).getGameTitle();
  }
  
  public static BigDecimal getMoney() {

	  BigDecimal randomInt =null;
      BigDecimal money=null;
      String array[]=new String[]{"4","10","50","100","1000","6","9","19","78"};
      money=new BigDecimal(array[random.nextInt(9)]);
      randomInt =new BigDecimal((random.nextInt(100)+1)).multiply(money).setScale(1, BigDecimal.ROUND_DOWN);
      return randomInt;
  }
  
  public static  void main(String args[]){
	  for(int i=0;i<20;i++){
//		  BigDecimal randomInt =null;
//	      BigDecimal money=null;
//	      String array[]=new String[]{"1.985","9.7","16","3.6","3.0","1.94","10.0","50","8.8","2.15","1.7","11.8","6.8","19.8"};
//	      money=new BigDecimal(array[random.nextInt(14)]);
//	      randomInt =new BigDecimal((random.nextInt(100)+1)).multiply(money).setScale(1, BigDecimal.ROUND_DOWN);
		  System.out.print(random.nextInt(3)+"  ");
	  }
	  
  }
  
}
