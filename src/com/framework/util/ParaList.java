package com.framework.util;

import java.util.ArrayList;

/**
 *
 * <p>Title: 参数集合类</p>
 * <p>Description: 封装sql/hql的参数到该集合类，便于处理和传递</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author lixiaodong
 * @version 1.0
 */

public class ParaList extends ArrayList {

  /**
   * 在指定位置添加一个参数对象
   * @param index：参数的索引值
   * @param p：需要加入的参数对象
   */
  public  void addParas(int index,Paras p){
      super.add(index,p);
  }

  /**
   * 在集合的最后位置添加一个参数对象
   * @param p：需要加入的参数对象
   */
  public void addParas(Paras p){
    super.add(p);
  }

  /**
   * 取得指定位置的参数对象
   * @param index：参数的索引值
   * @return：参数对象
   */
  public Paras getParas(int index){
      return (Paras)super.get(index) ;
  }
  /**
   * 取得指定参数的索引
   * @param p：参数对象
   * @return：参数索引
   */
  public int indexofParas(Paras p){
     return super.indexOf(p) ;
  }

  /**
   * 从集合中去掉一个指定的参数对象
   * @param index：参数索引
   */
  public void removeParas(int index){
    super.remove(index) ;
  } 

}  
