package com.ram.report.element;

import java.util.ArrayList;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Bottom extends Element {
  public Bottom() {
  }

  //label list
  ArrayList list = new ArrayList();

  public ArrayList getLabels(){
    return this.list;
  }

  public void add(Label label){
    list.add(label);
  }

  public void remove(Label label){
   int index = list.indexOf(label);
   if(index != -1){
     list.remove(index);
   }else{

   }
  }

}