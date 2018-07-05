<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
 
<html> 
<HEAD>
<LINK href="<%=request.getContextPath() %>/css/main.css" type=text/css rel=stylesheet>
<SCRIPT language=javascript type=text/javascript>
    var Rows ="2";
    //赢的方法的选中
    function ModeSelect(getMode,modes)
    {
       var obj= document.getElementById("MODES"+modes);
       for(i=0;i<Rows;i++)
       {
         if(getMode==obj.options[i].value)
         {
           obj.options[i].selected=true;
         }
       }
    }
    
    //输的方法的选中
    function ModeSelect1(getMode,modes)
    {
       var obj= document.getElementById("MODESW"+modes);
       for(i=0;i<Rows;i++)
       {
         if(getMode==obj.options[i].value)
         {
           obj.options[i].selected=true;
         }
       }
    }
    
    </SCRIPT>

<META content="MSHTML 6.00.6000.16674" name=GENERATOR></HEAD>
<BODY>
<FORM id=form1 name=form1 action=Auto28SettingDo.aspx method=post>
<DIV class=title>
<SPAN><A href="<%=request.getContextPath() %>/pce/betSessionAction.do?method=preSaveBetModel">抽奖模式编辑</A></SPAN> 
<SPAN><A href="<%=request.getContextPath() %>/pce/betSessionAction.do?method=preUseMode">自动抽奖设置</A></SPAN> 
</DIV>
<DIV class=center>
<TABLE cellSpacing=0 cellPadding=0 width="95%" border=0>
  <TBODY>
  <TR>
    <TD align=left width="90%" 
      height=25>&nbsp;&nbsp;高级功能允许您在多种抽奖模式间切换，请先设置抽奖规则：</TD>
    <TD align=middle width="10%">&nbsp;</TD></TR></TBODY></TABLE>
<P>&nbsp;</P>
<TABLE cellSpacing=1 cellPadding=0 width="90%" bgColor=#cccccc border=0>
  <TBODY>
  <TR>
    <TD align=middle bgColor=#ffffff>
      <TABLE cellSpacing=1 cellPadding=2 width="90%" bgColor=#ffb91f border=0>
        <TBODY>
        <TR background="/img/pub/xy28_bg.gif">
          <TD width="26%" background=<%=request.getContextPath()%>/images/bg.gif 
          height=25>抽奖模式</TD>
          <TD width="19%" background=<%=request.getContextPath()%>/images/bg.gif>抽奖积分</TD>
          <TD width="27%" background=<%=request.getContextPath()%>/images/bg.gif>赢后使用的抽奖模式</TD>
          <TD width="28%" 
background=<%=request.getContextPath()%>/images/bg.gif>输后使用的抽奖模式</TD></TR>
        <TR align=middle>
          <TD bgColor=#fffac7 height=22><A 
            href="http://www2.pceggs.com/play/pg28ModesEdit.aspx" 
            target=_blank>aaa</A></TD>
          <TD bgColor=#ffffff>30<IMG height=10 src="<%=request.getContextPath()%>/images/egg.gif" 
            width=10></TD>
          <TD bgColor=#ffffff><SELECT id=MODES1 name=WINMODES> <OPTION 
              value=1 selected>aaa</OPTION><OPTION value=2>bbb</OPTION></SELECT>
            <SCRIPT language=javascript 
            type=text/javascript>ModeSelect('1',1);</SCRIPT>
             </TD>
          <TD bgColor=#ffffff><SELECT id=MODESW1 name=LOSTMODES> <OPTION 
              value=1 selected>aaa</OPTION><OPTION value=2>bbb</OPTION></SELECT>
            <SCRIPT language=javascript 
            type=text/javascript>ModeSelect1('1',1);</SCRIPT>
             <INPUT id=Modes type=hidden value=1 name=Modes> </TD></TR>
        <TR align=middle>
          <TD bgColor=#fffac7 height=22><A 
            href="http://www2.pceggs.com/play/pg28ModesEdit.aspx" 
            target=_blank>bbb</A></TD>
          <TD bgColor=#f4f4f4>100<IMG height=10 src="PC蛋蛋--高级_files/egg.gif" 
            width=10></TD>
          <TD bgColor=#f4f4f4><SELECT id=MODES2 name=WINMODES> <OPTION 
              value=1 selected>aaa</OPTION><OPTION value=2>bbb</OPTION></SELECT>
            <SCRIPT language=javascript 
            type=text/javascript>ModeSelect('2',2);</SCRIPT>
             </TD>
          <TD bgColor=#f4f4f4><SELECT id=MODESW2 name=LOSTMODES> <OPTION 
              value=1 selected>aaa</OPTION><OPTION value=2>bbb</OPTION></SELECT>
            <SCRIPT language=javascript 
            type=text/javascript>ModeSelect1('2',2);</SCRIPT>
             <INPUT id=Modes type=hidden value=2 name=Modes> 
    </TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD align=middle width="100%" bgColor=#ffffff height=500><INPUT type=image 
      src="<%=request.getContextPath()%>/images/submit.gif">&nbsp;&nbsp;<A 
      href="http://www2.pceggs.com/play/auto28.aspx"><IMG 
      src="<%=request.getContextPath()%>/images/back3.gif" border=0></A> </TD></TR></TBODY></TABLE>
<TABLE cellSpacing=0 cellPadding=0 width="90%" border=0>
  <TBODY>
  <TR>
    <TD align=left colSpan=2 height=30><STRONG class=STYLE3>备注：</STRONG></TD></TR>
  <TR>
    <TD width="5%" height=30>&nbsp;</TD>
    <TD style="LINE-HEIGHT: 20px" align=left width="95%">
      <P>自动抽奖模式能帮助您自动进行好运来的抽奖操作，使用在自动抽奖模式前，您需要保存至少一种的抽奖模式！</P>
      <P><SPAN class=STYLE3>抽奖方法</SPAN>：</P>
      <P>1、选择您要进行自动抽奖的抽奖模式。</P>
      <P>2、设置自动抽奖开始与结束的期数。</P>
      <P>3、设置账户积分的最少值，当账户积分少于您设置的最小值或大于您设置的最大值时自动停止抽奖。</P>
      <P>4、点击开始抽奖后，自动抽奖程序开始运行！</P></TD></TR></TBODY></TABLE></DIV>

</FORM></BODY></HTML>
