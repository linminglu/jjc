package com.ram.report.element;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public abstract class Element {
  //x coordinate
  protected double x;
  //y coordinae
  protected double y;
  //element name
  protected String name;
  //element value
  protected String value;
  //fontsize
  protected float fontsize;
  //fontname
  protected String fontName;

  public Element() {
  }

  public Element(double x,double y){
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }
  public void setX(double x) {
    this.x = x;
  }
  public double getY() {
    return y;
  }
  public void setY(double y) {
    this.y = y;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public float getFontsize() {
    return fontsize;
  }
  public void setFontsize(float fontsize) {
    this.fontsize = fontsize;
  }
  public String getFontName() {
    return fontName;
  }
  public void setFontName(String fontName) {
    this.fontName = fontName;
  }


}