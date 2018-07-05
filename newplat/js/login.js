var loginObj={
	init:function(){
		$("#checkCode").bind("click", function(){
			var timestamp = Date.parse(new Date());
			$(this).attr('src','/api/baseData_checkCode?t='+timestamp);
		});
		$("#regbtn").bind("click", function(){
			loginObj.reg();
		});
		$("#guestReg").bind("click", function(){
			loginObj.guestReg();
		});
		$("#loginbtn2").bind("click", function(){
			loginObj.login();
		});
	},
	login:function(){
		var loginName=$("input[name='loginName']").val();
		var password=$("input[name='password']").val();
		var imgCode=$("input[name='imgCode']").val();
		
		if(loginName.length>0&&loginName.length<11){
			if(isNaN(loginName)){
				mui.alert('请输入正确的登录账号',document.title, function() {});
		    	return;
			}
		}else if(loginName.length==11){
			if(!move.widget.isPhone(loginName)){
			    mui.alert('请输入有效的手机号码',document.title, function() {});
			    return;
			}
		}
		
	    if ($.trim(password)==''){
	    	mui.alert('请输入密码',document.title, function() {});
	    }
	    if($.trim(imgCode)==''){
	    	mui.alert('请输入验证码',document.title, function() {});
	    	return;
	    }
	    var map = {};
	    map['loginName'] =loginName;
	    map['password'] =hex_md5(password).toUpperCase();
	    map['machineType'] ='3';
	    map['imgCode'] =imgCode;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
	    $.ajax({
			type: "POST",
			url:move.login,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var result=ret.data;
					var obj=result.obj;
					var logo=obj.logo;
					var uid=obj.uid;
					var u=obj.u;
					var loginName=obj.loginName;
					var cellPhone=obj.cellPhone;
					var userName=obj.userName;
					var money=obj.money;
					var point=obj.point;
					var userType=obj.userType;
					user.init(uid,u,'',logo,cellPhone,loginName,userName,money,point,userType);
					
					user.set(move.user);
					mui.toast('登陆成功',{ duration:'long', type:'div' }); 
					var redirect=move.getParameter('redirect');
					if(redirect){
						baseObj.openView(redirect);
					}else{
						baseObj.openView('index.html');
					}
				}else{
					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	reg:function(){
		var loginName=$(".log-form input[name='loginName']").val().trim();
		var password=$(".log-form input[name='password']").val();
		var repassword=$(".log-form input[name='repassword']").val();
		var imgCode=$(".log-form  input[name='imgCode']").val().trim();
		
		if(!move.widget.isPhone(loginName)){
		    mui.alert('请输入有效的手机号码',document.title, function() {});
		    return;
		}
		if(password.length<6 || password.length>16){
			mui.alert('请输入6-16密码(字母、数字、下划线)',document.title, function() {});
			return;
		}
	    if (password!=repassword){
	    	mui.alert('两次密码不一致',document.title, function() {});
	    	return;
	    }
	    if(imgCode.length==0){
	    	mui.alert('请输入验证码',document.title, function() {});
	    	return;
	    }
	    var map = {};
	    map['loginName'] =loginName;
	    map['password'] =hex_md5(password).toUpperCase();
	    map['repassword'] =hex_md5(repassword).toUpperCase();
	    map['machineType'] ='3';
	    map['inpcode'] =imgCode;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
	    $.ajax({
			type: "POST",
			url:move.reg,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var result=ret.data;
					var obj=result.obj;
					var logo=obj.logo;
					var uid=obj.uid;
					var u=obj.u;
					var loginName=obj.loginName;
					var cellPhone=obj.cellPhone;
					var username=obj.userName;
					var money=obj.money;
					var point=obj.point;
					var userType=obj.userType;

					user.init(uid,u,'',logo,cellPhone,loginName,username,money,point,userType);
					user.set(move.user);
					mui.toast('注册成功',{ duration:'long', type:'div' }); 
					var redirect=move.getParameter('redirect');
					if(redirect){
						baseObj.openView(redirect);
					}else{
						baseObj.openView('index.html');
					}
				}else{
					$("#checkCode").attr("src",$("#checkCode").attr("src").split("_")[0]+"_checkCode&t="+new Date().getTime());
					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	guestReg:function(){
		var mask=move.createLoading();
		mask.show();
	    $.ajax({
			type: "POST",
			url:move.guestReg,
			dataType:'json',
			data:{},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var result=ret.data;
					var obj=result.obj;
					var loginName=obj.loginName;
					var password=obj.password;
					var logo=obj.logo;
					var uid=obj.uid;
					var u=obj.u;
					var cellPhone=obj.cellPhone;
					var username=obj.userName;
					var money=obj.money;
					var point=obj.point;
					var userType=obj.userType;
					var isGuest=obj.isGuest;

//					$("#loginName").val(loginName);
//					$("#password").val(password);
					user.init(uid,u,'',logo,cellPhone,loginName,username,money,point,userType);
					user.set(move.user);
					mui.toast('登陆成功',{ duration:'long', type:'div' }); 
					var redirect=move.getParameter('redirect');
					if(redirect){
						baseObj.openView(redirect);
					}else{
						baseObj.openView('index.html');
					}
//					$(".accounts-info").show();
//					$(".mui-backdrop").show();
//					$("#guestRegBut").bind("click", function(){
//						user.init(uid,u,'',logo,cellPhone,loginName,username,money,point);
//						user.set(move.user);
//						mui.toast('登陆成功',{ duration:'long', type:'div' }); 
//						var redirect=move.getParameter('redirect');
//						if(redirect){
//							baseObj.openView(redirect);
//						}else{
//							baseObj.openView('index.html');
//						}
//						$(".accounts-info").hide();
//						$(".mui-backdrop").hide();
//					});
				}else{
					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
				mui.alert("网络忙，请稍后再试！",document.title, function() {});
			}
		});
	},
}

