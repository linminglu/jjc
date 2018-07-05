<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.ram.model.User"%>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf8" />
<title>用户中心 - <c:out value="${slogan}"/></title>
<c:import url="../common/inc_style.jsp"/>
<script type="text/javascript">
function modifSub(){
	if(confirm('确定要更改信息吗？')){
		var userName=$("#userName");
		if(userName.val().length>10){
			$.dialog({
				title : '提示',
				content : '用户名字数不能大于10位',
			});
			return false;	
		}
		return true;
	}
	return false;
}

function gbcount(obj,total,used,remain)
{
	var max;
	max = total.value;
	if (obj.value.length > max) {
		obj.value = obj.value.substring(0,max);
		used.value = max;
		remain.value = 0;
		alert("留言不能超过100 个字!");
	}
	else {
		used.value = obj.value.length;
		remain.value = max - used.value;
	}
}
$(document).ready(function(){
	var err = $("#errors");
	if(err && err.val()!=""){
		if(err.val()=='ok'){
			$.dialog({
				title : '提示',
				content : '更改信息成功',
				ok:function(){
					window.location.href="../myhome/index.html";
				}
			});
		}
	}
	
	//----this.form.total,this.form.used,this.form.remain
	var infoForm = document.forms['infoForm'];
	var motto=document.getElementById("motto");
	gbcount(motto,infoForm.total,infoForm.used,infoForm.remain);
	
});
</script>

<script language="JavaScript">
function openimgupload()
	{
		//根据用户id命名logo，用户上传的logo只会是最新的，也避免了无用的logo的保存，上传的时候会把以前的logo覆盖掉
		<%
		User user= (User) request.getSession(true).getAttribute("loginUser");
		if(user!=null){
		request.setAttribute("userId",user.getUserId());
		}
		
		%>
		window.open('../upload/avatar.jsp?uid=<%=request.getAttribute("userId")%>&uploadtype=logo','图片上传','top=250,left=450,height=270,width=470,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no,z-look=yes');
	}
</script>
</head>
<body>
<input type="hidden" name="errors" id="errors" value="<c:out value="${error}"/>" />
<div class="hdover">
	<div class="topline"></div>
	<c:import url="../common/inc_naver.jsp">
			<c:param name="mpath" value=""></c:param>
	</c:import>
</div>


<div class="content">
	<div class="signup">
		<div class="n-left-menu">
			<h3></h3>
			<ul>
				<li class="selected">
					<a href="/myhome/index.html">个人信息</a>
				</li>
				<li >
					<a href="/myhome/resetLogo.html">修改头像</a>
				</li>
				<li >
					<a href="/myhome/resetPassword.html">修改密码</a>
				</li>
			</ul>
		</div>
		<div class="n-right">
			<div class="title_1">
				<a class="selected" href="#">基本资料</a>
			</div>
			<div class="m1L" style="position:relative;">
			<form action="/myhome/myInfoModif.html" method="post" onsubmit="return modifSub();" name="infoForm">
			<html:hidden property="user.userId" name="userForm" />
				<table class="table1" width="100%" cellspacing="0" cellpadding="0" style="margin-left:100px;">
					<tbody>
						<tr>
							<th align="right"><strong>邮箱：</strong></th>
							<td><strong class="themail"><c:out value="${loginUser.userEmail}"/></strong></td>
						</tr>
						<tr>
							<th width="70" align="right">登录名：</th>
							<td width="280"><c:out value="${loginUser.loginName}"/></td>
						</tr>
						<tr>
							<th width="70" align="right">昵称：</th>
							<td width="280">
								<html:text property="user.userName" name="userForm" maxlength="10" styleId="userName"></html:text>
							</td>
						</tr>
						<tr>
							<th width="70" align="right">性别：</th>
							<td width="280">
								<c:if test="${loginUser.gender ne null}">
									<label>
										<html:radio property="user.gender" name="userForm" value="1"  ></html:radio>
										男
									</label>
									<label>
										<html:radio property="user.gender" name="userForm" value="2"></html:radio>
										女
									</label>
								</c:if>
								<c:if test="${loginUser.gender eq null}">
									<label>
										<input type="radio" checked="checked" name="user.gender" value="1"/>
										男
									</label>
									<label>
										<input type="radio"  name="user.gender" value="2"/>
										女
									</label>
								</c:if>
							</td>
						</tr>
						<tr>
							<th width="70" align="right">民族：</th>
							<td width="280">
								<html:select property="user.extend2"  name="userForm" styleId="" style="width: 153px;">
									<html:option value="">请选择</html:option>
									<html:option value="汉族">汉族</html:option>
									<html:option value="蒙古族">蒙古族</html:option>
									<html:option value="回族">回族</html:option>
									<html:option value="藏族">藏族</html:option>
									<html:option value="维吾尔族">维吾尔族</html:option>
									<html:option value="苗族">苗族</html:option>
									<html:option value="彝族">彝族</html:option>
									<html:option value="壮族">壮族</html:option>
									<html:option value="布依族">布依族</html:option>
									<html:option value="朝鲜族">朝鲜族</html:option>
									<html:option value="满族">满族</html:option>
									<html:option value="侗族">侗族</html:option>
									<html:option value="瑶族">瑶族</html:option>
									<html:option value="白族">白族</html:option>
									<html:option value="土家族">土家族</html:option>
									<html:option value="哈尼族">哈尼族</html:option>
									<html:option value="哈萨克族">哈萨克族</html:option>
									<html:option value="傣族">傣族</html:option>
									<html:option value="黎族">黎族</html:option>
									<html:option value="傈僳族">傈僳族</html:option>
									<html:option value="佤族">佤族</html:option>
									<html:option value="畲族">畲族</html:option>
									<html:option value="高山族">高山族</html:option>
									<html:option value="拉祜族">拉祜族</html:option>
									<html:option value="水族">水族</html:option>
									<html:option value="东乡族">东乡族</html:option>
									<html:option value="纳西族">纳西族</html:option>
									<html:option value="景颇族">景颇族</html:option>
									<html:option value="柯尔克孜族">柯尔克孜族</html:option>
									<html:option value="土族">土族</html:option>
									<html:option value="达斡尔族">达斡尔族</html:option>
									<html:option value="仫佬族">仫佬族</html:option>
									<html:option value="羌族">羌族</html:option>
									<html:option value="布朗族 ">布朗族 </html:option>
									<html:option value="撒拉族">撒拉族</html:option>
									<html:option value="毛难族">毛难族</html:option>
									<html:option value="仡佬族">仡佬族</html:option>
									<html:option value="锡伯族">锡伯族</html:option>
									<html:option value="阿昌族">阿昌族</html:option>
									<html:option value="普米族">普米族</html:option>
									<html:option value="塔吉克族">塔吉克族</html:option>
									<html:option value="怒族">怒族</html:option>
									<html:option value="乌孜别克族">乌孜别克族</html:option>
									<html:option value="俄罗斯族">俄罗斯族</html:option>
									<html:option value="鄂温克族">鄂温克族</html:option>
									<html:option value="崩龙族">崩龙族</html:option>
									<html:option value="保安族">保安族</html:option>
									<html:option value="裕固族 ">裕固族 </html:option>
									<html:option value="京族 ">京族 </html:option>
									<html:option value="塔塔尔族 ">塔塔尔族 </html:option>
									<html:option value="独龙族 ">独龙族 </html:option>
									<html:option value="鄂伦春族 ">鄂伦春族 </html:option>
									<html:option value="赫哲族 ">赫哲族 </html:option>
									<html:option value="门巴族">门巴族</html:option>
									<html:option value="珞巴族">珞巴族</html:option>
									<html:option value="基诺族">基诺族</html:option>
									<html:option value="其他">其他</html:option>
								</html:select>
							</td>
							<td width="200"></td>
						</tr>
						<tr>
							<th width="70" align="right">手机号：</th>
							<td width="280">
								<html:text property="user.cellPhone" name="userForm" onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="11"></html:text>
							</td>
						</tr>
						<tr>
							<th width="70" align="right">职业：</th>
							<td width="280">
								<html:text property="user.extend3" name="userForm" maxlength="20"></html:text>
							</td>
						</tr>
						<tr>
							<th width="70" align="right">年龄：</th>
							<td width="280">
								<html:text property="user.extend1" name="userForm" onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="3"></html:text>
							</td>
						</tr>
						<tr>
							<th width="70" align="right">个人说明：</th>
							<td width="280">
								<html:textarea name="userForm" property="user.motto" styleId="motto" style="max-width: 350px; width: 350px; " cols="10" rows="5" 
								onkeydown="gbcount(this,this.form.total,this.form.used,this.form.remain);" onkeyup="gbcount(this,this.form.total,this.form.used,this.form.remain);"/>
								<p>最多字数：
								<input disabled="disabled"  name="total" size="3" value="100" class="inputtext"/>
								已用字数：
								<input disabled="disabled"  name="used" size="3" value="0" class="inputtext"/>
								剩余字数：
								<input disabled="disabled"  name="remain" size="3" value="100" class="inputtext"/>
								</p>
							</td>
						</tr>
						<tr>
							<th width="70" align="right"></th>
							<td width="280">
								<input type="submit" class="btn_save" value="保存" />
							</td>
						</tr>
					</tbody>
				</table>
				</form>
				<%--
				<div style="width: 80px; height: 90px; position: absolute; top: 10px; right: 300px;">
					<span style="margin-left: 10px;">
					<img src="<c:out value='${loginUser.logo}'/>" id="logo"/><br/>
					</span>
					<input onclick="openimgupload();" value="更换头像" type="button"/>
				</div>
				 --%>
			</div>
			
		</div>
	</div>
</div>
<c:import url="../common/inc_footer.jsp"/>
</body>
</html>
