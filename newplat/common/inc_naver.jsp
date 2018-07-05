<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%
String mpath=request.getParameter("mpath");
%>
<div id="plane">
	<a class="upToTop" target="_self" href="#" title="回到顶部">
		<span>回到顶部</span>
	</a>
</div>
<div id="loginDiv">
	<table >
		<tr>
			<th>帐号：</th>
			<td><input class="in-txt" type="text" id="loginName"/> </td>
		</tr>
		<tr>
			<th>密码：</th>
			<td><input class="in-txt" type="password" id="pwd"/> </td>
		</tr>
	</table>
</div>
<div class="topline"></div>
<div class="hdline">
	<div class="header">
		<div class="hdlogo">
			<h1>
				<a href="/">
					<img src="../images/gz_03.png"/>
				</a>
			</h1>
		</div>
		<div class="userTool">
			<c:if test="${loginUser eq null}">
				<!-- <div class="loginBtn">
					<a class="showdiv" href="../regSignup.html">注册</a>
				</div>
				<div class="postBtn">
				<a class="" href="javascript:void(0);" onclick="login();">登录</a>
					<a href="../login.html">登录</a>
				</div> -->
				<div class="hdulogins">
					<a href="../login.html"><img src="../images/btn_home_login.jpg"/></a><a href="../regSignup.html"><img src="../images/btn_home_reg.jpg"/></a>
				</div>
			</c:if>
			<c:if test="${loginUser ne null}">
				
				<%-- <div class="Welcome">
					<span class="fl">欢迎回来，</span><a class="name"><c:out value="${loginUser.loginName}"/></a>
					<span class="helpBtn">
						<a href="/myHome.html">个人中心</a>
					</span>
				</div>
				<div class="grayBtn fl">
					<a class="" href="/logout.html" >退出</a>
				</div> --%>
				
				<div class="hdulogins">
					<span class="weltxt">欢迎回来，<c:out value="${loginUser.userName}"/></span>
					<a target="_blank"  href="/myhome/index.html"><img src="../images/btn_ucenter.jpg"/></a>
					<c:if test="${loginUser.userType eq '2'}"><a href="/plat"><img src="../images/btn_home_admin.jpg"/></a></c:if>
					<a class="" href="/logout.html" ><img src="../images/btn_home_logout.jpg"/></a>
				</div>
				
			</c:if>
		</div>
	</div>
	<div class="naver">
		<div class="navmenu">
			<div class="nma"></div>
			<div class="nmitem" id="mainnavs">
				<a target="_blank"  href="../home.html"><%if(mpath.equals("home")){ %><img src="../images/gz_12b.png"/><%}else{%><img src="../images/gz_12.png" onmouseover="$(this).attr('src','../images/gz_12b.png');" onmouseout="$(this).attr('src','../images/gz_12.png');"/><%}%></a><a target="_blank"  href="../baby_0.html"><%if(mpath.equals("baby")){ %><img src="../images/gz_13b.png"/><%}else{%><img src="../images/gz_13.png" onmouseover="$(this).attr('src','../images/gz_13b.png');" onmouseout="$(this).attr('src','../images/gz_13.png');"/><%}%></a><a target="_blank"  href="../tmo_0.html"><%if(mpath.equals("tmo")){ %><img src="../images/gz_14b.png"/><%}else{%><img src="../images/gz_14.png" onmouseover="$(this).attr('src','../images/gz_14b.png');" onmouseout="$(this).attr('src','../images/gz_14.png');"/><%}%></a><a target="_blank"  href="../tms_0.html"><%if(mpath.equals("tms")){ %><img src="../images/gz_15b.png"/><%}else{%><img src="../images/gz_15.png" onmouseover="$(this).attr('src','../images/gz_15b.png');" onmouseout="$(this).attr('src','../images/gz_15.png');"/><%}%></a><a target="_blank"  href="http://bbs.guozilianmeng.com" target="_blank"><%if(mpath.equals("dblf2")){ %><img src="../images/gz_16gb.png"/><%}else{%><img src="../images/gz_16g.png" onmouseover="$(this).attr('src','../images/gz_16gb.png');" onmouseout="$(this).attr('src','../images/gz_16g.png');"/><%}%></a><a target="_blank"  href="../ryzr_0.html"><%if(mpath.equals("ryzr")){ %><img src="../images/gz_17b.png"/><%}else{%><img src="../images/gz_17.png" onmouseover="$(this).attr('src','../images/gz_17b.png');" onmouseout="$(this).attr('src','../images/gz_17.png');"/><%}%></a><a target="_blank"  href="../downloadApp.html"><%if(mpath.equals("app")){ %><img src="../images/gz_18b.jpg"/><%}else{%><img src="../images/gz_18.jpg" onmouseover="$(this).attr('src','../images/gz_18b.jpg');" onmouseout="$(this).attr('src','../images/gz_18.jpg');"/><%}%></a>
				
				<%-- <a href="../zx_0.html"><%if(mpath.equals("zx")){ %><img src="../images/gz_zxb.png"/><%}else{%><img src="../images/gz_zx.png" onmouseover="$(this).attr('src','../images/gz_zxb.png');" onmouseout="$(this).attr('src','../images/gz_zx.png');"/><%}%></a>
				<a href="../hd_0.html"><%if(mpath.equals("hd")){ %><img src="../images/gz_hdb.png"/><%}else{%><img src="../images/gz_hd.png" onmouseover="$(this).attr('src','../images/gz_hdb.png');" onmouseout="$(this).attr('src','../images/gz_hd.png');"/><%}%></a> --%>
				
			</div>
			<div class="nmb"></div>
		</div>
		
	</div>
</div>
