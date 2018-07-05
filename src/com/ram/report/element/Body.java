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

public class Body extends Element {
  //table rows
  private int rows = 0;
  //table cols
  private int cols = 0;
  //row count per page
  private int pageRowSize = 0;
  //row width
  private int rowWidth;
  //row height
  private int rowHeight;
  //bodyheader the size should be same with the cols
  private java.util.ArrayList bodyHeader;
  //ArrayList every element in array is a ArrayList
  private java.util.ArrayList bodyData;

  private String[] header;
  private String[][] data;

  public Body() {
  }

  public Body(int rows,int cols,int pageRowSize) {
    this.rows = rows;
    this.cols = cols;
    this.pageRowSize = pageRowSize;
  }

  public int getCols() {
    return cols;
  }
  public int getPageRowSize() {
    return pageRowSize;
  }
  public int getRows() {
    return rows;
  }
  public void setCols(int cols) {
    this.cols = cols;
  }
  public void setPageRowSize(int pageRowSize) {
    this.pageRowSize = pageRowSize;
  }
  public void setRows(int rows) {
    this.rows = rows;
  }
  public int getRowWidth() {
    return rowWidth;
  }
  public void setRowWidth(int rowWidth) {
    this.rowWidth = rowWidth;
  }
  public int getRowHeight() {
    return rowHeight;
  }
  public void setRowHeight(int rowHeight) {
    this.rowHeight = rowHeight;
  }
  public java.util.ArrayList getBodyHeader() {
    return bodyHeader;
  }
  public void setBodyHeader(java.util.ArrayList bodyHeader) {
    this.bodyHeader = bodyHeader;
  }
  public java.util.ArrayList getBodyData() {
    return bodyData;
  }
  public void setBodyData(java.util.ArrayList bodyData) {
    this.bodyData = bodyData;
  }
  public String[] getHeader() {
    return header;
  }
  public void setHeader(String[] header) {
    this.header = header;
  }
  public String[][] getData() {
    return data;
  }
  public void setData(String[][] data) {
    this.data = data;
  }
  //label list
ArrayList list = new ArrayList();

public ArrayList getLabels(){
  return this.list;
}

public void add(Label label){
  list.add(label);
}

}