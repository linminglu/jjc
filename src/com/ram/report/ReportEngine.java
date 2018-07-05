package com.ram.report;

import inde.pdf.BaseFont;
import inde.pdf.PDFEngine;

import java.io.OutputStream;
import java.util.ArrayList;

import com.ram.report.element.Body;
import com.ram.report.element.Bottom;
import com.ram.report.element.Label;
import com.ram.report.element.Memo;
import com.ram.report.element.Title;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ReportEngine {

  private PDFEngine pdf= null;

  private Title title = null;
  private Body body = null;
  private Bottom bottom = null;
  private Memo memo = null;

  public static double PAGE_WIDTH=595.5D;
  //public static double PAGE_HEIGHT=842D;
  public static double PAGE_HEIGHT=295.4D;


  public ReportEngine() {
  }


  public void startReport(OutputStream os,String fontPath)
  {
     pdf = new PDFEngine(os);
     //pdf.SetFontMapTable("simsong","simsong",0);
     pdf.SetFontFilePath(fontPath) ;

     //pdf.SetEmbedFontMapTable("arial", "Arial",1,"arial.ttf");
     pdf.SetEmbedFontMapTable("simsun", "simsun",3,"simsun.ttc");
     pdf.SetEmbedFontMapTable("simhei", "simhei",3,"simhei.ttf");

     //A4 page
     pdf.setPageSize(this.PAGE_WIDTH,this.PAGE_HEIGHT);
  }

  public void drawPage(){
   int pageCount = 0;
   if( this.body.getData().length%this.body.getPageRowSize()>0){
     pageCount = this.body.getData().length/this.body.getPageRowSize()+1;
   }else{
      pageCount = this.body.getData().length/this.body.getPageRowSize();
   }

    for(int i = 0;i<pageCount;i++){
      this.drawTitle();
      this.drawBody(i);
      if(i<pageCount-1)
        this.drawMemo();
      else
        this.drawBottom();
      pdf.dispose();
    }
  }

  public void drawPage(Title title){
      this.drawTitle();
      pdf.dispose();
  }


  public void setPageContent(Title title,Body body,Bottom bottom,Memo memo){
    this.title = title;
    this.body = body;
    this.bottom = bottom;
    this.memo = memo;
  }

  public void drawTitle(){
    if(this.title == null){
      return ;
    }

    // params  fontname  ,,fontsize,
    //pdf.SetLineWidth(2);

    ArrayList labels = this.title.getLabels();
    for(int i = 0;i<labels.size();i++){
      Label label = (Label) labels.get(i);
      inde.pdf.BaseFont font = null;
      if(label.getFontName() != null && label.getFontName().equals("arial")){
       font = new BaseFont("arial", BaseFont.PLAIN,
                                              label.getFontsize(), false, true);
      }else{
        font = new BaseFont("simhei", BaseFont.PLAIN,
                                               label.getFontsize(), false, true);
      }
      pdf.SetFont(font);
      pdf.DrawString(label.getName(),label.getX(),label.getY());
    }
  }

  public void drawMemo(){
    if(this.memo == null){
      return ;
    }

    // params  fontname  ,,fontsize,
    //pdf.SetLineWidth(2);
    ArrayList labels = this.memo.getLabels();
    for(int i = 0;i<labels.size();i++){
      Label label = (Label) labels.get(i);
      inde.pdf.BaseFont font = null;
      if(label.getFontName() != null && label.getFontName().equals("arial")){
       font = new BaseFont("arial", BaseFont.PLAIN,
                                              label.getFontsize(), false, true);
      }else{
        font = new BaseFont("simhei", BaseFont.PLAIN,
                                               label.getFontsize(), false, true);
      }
      pdf.SetFont(font);
      pdf.DrawString(label.getName(),label.getX(),label.getY());
    }
  }

  public void drawBody(int index){
    if(this.body == null){
      return;
    }

    inde.pdf.BaseFont font = null;
    //new BaseFont("simsun", BaseFont.PLAIN,
                          //this.body.getFontsize(), false, true);

    //pdf.SetFont(font);
    pdf.SetLineWidth(0.5);

    int rows = this.body.getRows();
    int cols = this.body.getCols();
    cols = 7;
    int rowpage = this.body.getPageRowSize();

    double startX = this.body.getX();
    double startY = this.body.getY();
    //pdf.SetTextCharSpace(1.);
    double[] colWidth={40.,70.,110.,100.,40.,60.,80.};
    /*
    double rowWidth1 = 50;

    double rowWidth2 = 50;
    double rowWidth3 = 50;
    double rowWidth4 = 50;
    double rowWidth5 = 50;
    double rowWidth1 = 50;
    double rowWidth1 = 50;
    double rowWidth1 = 50;
    */
   // double rowWidth = (this.PAGE_WIDTH-2*startX)/cols;
    double rowHeight = 20;//(this.PAGE_HEIGHT -2*startY)/rows;

    for(int j= 0;j<rows;j++){
      double curY = startY+rowHeight * (j);
      double curX = startX;
      for (int i = 0; i < cols; i++) {
        double rowWidth = colWidth[i];
        pdf.drawRect(curX,curY,curX+rowWidth,curY+rowHeight,0);

        if (j == 0) {
          double dif = 5.0;
          switch (i) {
            case 2:
              dif = 5.0;
              break;
            case 3:
              dif = 5.0;
              break;
            default:
              dif = 5.0;
              break;

          }
          font = new BaseFont("simhei", BaseFont.PLAIN,
                                                this.body.getFontsize(), false, true);
          pdf.SetFont(font);

          pdf.DrawString(this.body.getHeader()[i], curX + dif, curY + 13);
        }
        else if (j <= rowpage) {
          double dif = 15.0;
          switch (i) {
            case 1:
              dif = 8.0;
              break;
            case 2:
              font = new BaseFont("arial", BaseFont.PLAIN,
                                  this.body.getFontsize(), false, true);
              pdf.SetFont(font);
              dif = 5.0;
              break;
            case 3:
              font = new BaseFont("arial", BaseFont.PLAIN,
                                  this.body.getFontsize(), false, true);
              pdf.SetFont(font);
              dif = 10.0;
              break;

            default:
              font = new BaseFont("simhei", BaseFont.PLAIN,
                                  this.body.getFontsize(), false, true);
              pdf.SetFont(font);
              dif = 15.0;
              break;

          }
          if (index * rowpage + j - 1 > this.body.getData().length) {
            return;
          }
          else {
            pdf.DrawString(this.body.getData()[index * rowpage + j - 1][i],
                           curX + dif, curY + 13);
          }
        }
        else {
          continue;
        }
        curX += rowWidth;

      }
    }
    ArrayList labels = this.body.getLabels();
    for (int i = 0; i < labels.size(); i++) {
      Label label = (Label) labels.get(i);
      if (label.getFontName() != null && label.getFontName().equals("arial")) {
        font = new BaseFont("arial", BaseFont.PLAIN,
                            label.getFontsize(), false, true);
      }
      else {
        font = new BaseFont("simhei", BaseFont.PLAIN,
                            label.getFontsize(), false, true);
      }
      pdf.SetFont(font);
      pdf.DrawString(label.getName(), label.getX(), label.getY());
    }
  }


  public void drawBody(){
    if(this.body == null){
      return;
    }

    inde.pdf.BaseFont font=new BaseFont("simsun", BaseFont.PLAIN,this.body.getFontsize(),false,true);

    pdf.SetFont(font);

    pdf.SetLineWidth(0.5);

    int rows = this.body.getRows();
    int cols = this.body.getCols();

    double startX = this.body.getX();
    double startY = this.body.getY();

    //double rowWidth = this.body.getRowWidth();
    //double rowHeight = this.body.getRowHeight();

    double rowWidth = (this.PAGE_WIDTH-2*startX)/cols;
    double rowHeight = 20;//(this.PAGE_HEIGHT -2*startY)/rows;



    for(int j= 0;j<rows;j++){
      double curY = startY+rowHeight * (j);
      for (int i = 0; i < cols; i++) {
        double curX = startX + rowWidth*(i);
        pdf.drawRect(curX,curY,curX+rowWidth,curY+rowHeight,0);

        if(j == 0 && this.body.getHeader() != null){
          pdf.DrawString(this.body.getHeader()[i],curX+10,curY+10);
        }else{
          pdf.DrawString(this.body.getData()[j-1][i],curX+10,curY+10);
        }
      }
    }
  }

  public void drawBottom(){
    if(this.bottom == null){
      return ;
    }
    ArrayList labels = this.bottom.getLabels();
    for(int i = 0;i<labels.size();i++){
      Label label = (Label) labels.get(i);
      inde.pdf.BaseFont font = null;
      if(label.getFontName() != null && label.getFontName().equals("arial")){
       font = new BaseFont("arial", BaseFont.PLAIN,
                                              label.getFontsize(), false, true);
      }else{
        font = new BaseFont("simhei", BaseFont.PLAIN,
                                               label.getFontsize(), false, true);
      }
      pdf.SetFont(font);
      pdf.DrawString(label.getName(),label.getX(),label.getY());
    }

  }
  /*
  public void drawBottom(int index){
  if(this.bottom == null){
    return ;
  }
  ArrayList labels = this.bottom.getLabels();
  for(int i = 0;i<labels.size();i++){
    Label label = (Label) labels.get(i);
    inde.pdf.BaseFont font = null;
    if(label.getFontName() != null && label.getFontName().equals("arial")){
     font = new BaseFont("arial", BaseFont.PLAIN,
                                            label.getFontsize(), false, true);
    }else{
      font = new BaseFont("simhei", BaseFont.PLAIN,
                                             label.getFontsize(), false, true);
    }
    pdf.SetFont(font);
    pdf.DrawString(label.getName(),label.getX(),label.getY());
  }

}
*/
  public void endReport()
  {
     pdf.dispose();
     pdf.close();
  }


  public static void main(String[] args){
    System.exit(0);

  }
}