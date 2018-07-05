var userObj={
	defPageSize:15,
	gameType:'',
	gfxyType:'',
	cashType:'',
	pointType:'',
	init:function(){
		if(move.user&&move.user.userType=="1"){

		}else{
			$(".deposit").hide();
			$(".withdraw").hide();
			$(".settingbox").hide();
		}


	},
	bindSwitchGame:function(){
		$(".conditions .gameType").change(function(){
			userObj.gameType=$(this).val();
			userObj.buylist();
		});
		$(".conditions .gfxyType").change(function(){
			userObj.gfxyType=$(this).val();
			userObj.gameType='';
			$(".gameType option")[0].selected=true;
			if(userObj.gfxyType=='0'){//官方
				$(".gf").show();
				$(".xy").hide();
			}else if(userObj.gfxyType=='1'){
				$(".gf").hide();
				$(".xy").show();
			}
			userObj.buylist();
		});
		$(".conditions .type").change(function(){
			userObj.cashType=$(this).val();
			userObj.finance();
		});
		$(".conditions .type2").change(function(){
			userObj.cashType=$(this).val();
			userObj.rechargeDetail();
		});
		$(".conditions .pointType").change(function(){
			userObj.pointType=$(this).val();
			userObj.pointlist();
		});
	},
	bindPage:function(){
		$(".pager .first").unbind( "click");
		$(".pager .first").bind("click", function(){
			var type=$(this).attr('type');
			move.pageIndex=0;
			if(type=='record'){
				userObj.buylist();
			}else if(type=='finance'){
				userObj.finance();
			}else if(type=='point'){
				userObj.pointlist();
			}else if(type=='message'){
				userObj.messageList();
			}else if(type=='withdraw'){
				userObj.cashList();
			}else if(type=='deposit'){
				userObj.rechargeDetail();
			}
		});
		$(".pager .prev").unbind( "click");
		$(".pager .prev").bind("click", function(){
			var type=$(this).attr('type');
			if(move.pageIndex>0){
				move.pageIndex=move.pageIndex-1;
				if(type=='record'){
					userObj.buylist();
				}else if(type=='finance'){
					userObj.finance();
				}else if(type=='point'){
					userObj.pointlist();
				}else if(type=='message'){
					userObj.messageList();
				}else if(type=='withdraw'){
					userObj.cashList();
				}else if(type=='deposit'){
					userObj.rechargeDetail();
				}
			}
		});
		$(".pager .next").unbind( "click");
		$(".pager .next").bind("click", function(){
			var type=$(this).attr('type');
			var toatlpage=$('#toatlpage').text();
			toatlpage=parseInt(toatlpage);
			if((move.pageIndex+1)<toatlpage){
				move.pageIndex=move.pageIndex+1;
				if(type=='record'){
					userObj.buylist();
				}else if(type=='finance'){
					userObj.finance();
				}else if(type=='point'){
					userObj.pointlist();
				}else if(type=='message'){
					userObj.messageList();
				}else if(type=='withdraw'){
					userObj.cashList();
				}else if(type=='deposit'){
					userObj.rechargeDetail();
				}
			}
		});
		$(".pager .last").unbind( "click");
		$(".pager .last").bind("click", function(){
			var type=$(this).attr('type');
			var toatlpage=$('#toatlpage').text();
			toatlpage=parseInt(toatlpage);
			if((move.pageIndex+1)<toatlpage){
				move.pageIndex=(toatlpage-1);
				if(type=='record'){
					userObj.buylist();
				}else if(type=='finance'){
					userObj.finance();
				}else if(type=='point'){
					userObj.pointlist();
				}else if(type=='message'){
					userObj.messageList();
				}else if(type=='withdraw'){
					userObj.cashList();
				}else if(type=='deposit'){
					userObj.rechargeDetail();
				}
			}
		});
	},
	money:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var map = {};
		map['u'] =move.user.u;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.money,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				var html='';
				if(code=='200'){
					var money=data.money;
					var aggBetMoney=data.aggregateBetMoney;
					var points=data.userpoints;
					var logo=data.logo;
					var dayBetMoney=data.dayBetMoney;
					var isAuthentication=data.isAuthentication;
					var isVerifyPhone=data.isVerifyPhone;
					move.user.points=points;
					move.user.money=money;
					var qianWan=aggBetMoney/10000000;
					var qianWanYu=aggBetMoney%10000000;
					qianWan=parseInt(qianWan);
					
					var baiWan=qianWanYu/1000000;
					var baiWanYu=qianWanYu%1000000;
					baiWan=parseInt(baiWan);
					
					var shiWan=qianWanYu/1000000;
					var shiWanYu=qianWanYu%1000000;
					shiWan=parseInt(shiWan);
					
					var wan=shiWanYu/1000000;
					var wanYu=shiWanYu%1000000;
					wan=parseInt(wan);
					
					var levelhtml='';
					if(qianWan>0){
						levelhtml=levelhtml+'<span><img src="/images/static/star/crown.gif"> <img src="/images/static/star/'+qianWan+'.png"></span>';
					}
					if(baiWan>0){
						levelhtml=levelhtml+'<span><img src="/images/static/star/gold.gif"> <img src="/images/static/star/'+baiWan+'.png"></span>';
					}
					if(shiWan>0){
						levelhtml=levelhtml+'<span><img src="/images/static/star/diamond.gif"> <img src="/images/static/star/'+shiWan+'.png"></span>';
					}
					if(wan>0){
						levelhtml=levelhtml+'<span><img src="/images/static/star/star.gif"> <img src="/images/static/star/'+wan+'.png"></span>';
					}else{
						levelhtml=levelhtml+'<span><img src="/images/static/star/star.gif"></span>';
					}
					$('.levelbox').html(levelhtml);
					
					var cashmoney=parseInt(points/100);
					
					$('.username').html(move.user.userName);
					$('.info .win').text(dayBetMoney);
					$('.info .totalmoney').text(aggBetMoney);
					$('.info .money').text(money);
					$('.info .point').text(points);
					$('.info .cashmoney').text(cashmoney+"元");
					$('.logined .point').text(points);
					$('.logined .money').text(money);
					store.set('user', move.user);
				}else{
					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	userinfo:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var map = {};
		map['u'] =move.user.u;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.userinfo,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				var html='';
				if(code=='200'){
					var obj=data.obj;
					var money=obj.money;
					var aggBetMoney=obj.aggregateBetMoney;
					var points=obj.userpoints;
					var userName=obj.userName;
					move.user.userName=userName;
					move.user.points=points;
					move.user.money=money;
					var qianWan=aggBetMoney/10000000;
					var qianWanYu=aggBetMoney%10000000;
					qianWan=parseInt(qianWan);
					
					var baiWan=qianWanYu/1000000;
					var baiWanYu=qianWanYu%1000000;
					baiWan=parseInt(baiWan);
					
					var shiWan=qianWanYu/1000000;
					var shiWanYu=qianWanYu%1000000;
					shiWan=parseInt(shiWan);
					
					var wan=shiWanYu/1000000;
					var wanYu=shiWanYu%1000000;
					wan=parseInt(wan);
					
					var levelhtml='';
					if(qianWan>0){
						levelhtml=levelhtml+'<span><img src="/images/static/star/crown.gif"> <img src="/images/static/star/'+qianWan+'.png"></span>';
					}
					if(baiWan>0){
						levelhtml=levelhtml+'<span><img src="/images/static/star/gold.gif"> <img src="/images/static/star/'+baiWan+'.png"></span>';
					}
					if(shiWan>0){
						levelhtml=levelhtml+'<span><img src="/images/static/star/diamond.gif"> <img src="/images/static/star/'+shiWan+'.png"></span>';
					}
					if(wan>0){
						levelhtml=levelhtml+'<span><img src="/images/static/star/star.gif"> <img src="/images/static/star/'+wan+'.png"></span>';
					}else{
						levelhtml=levelhtml+'<span><img src="/images/static/star/star.gif"></span>';
					}
					$('.levelbox').html(levelhtml);
					
					
					$('.username').text(userName);
					$('.logined .point').text(points);
					$('.logined .money').text(money);
					$('.info .money').text(money);
					store.set('user', move.user);
					
					var realName=obj.realName;
					var birthday=obj.birthday;
					var gender=obj.gender;
					var cellPhone=obj.cellPhone;
					var qq=obj.qq;
					var email=obj.email;
					var address=obj.address;
					if(realName==''){
						$('#realName').removeAttr('disabled');
					}else{
						$('#realName').val(realName);
					}
					$('#birthday').val(birthday);
					$('#phone').val(cellPhone);
					$('#qq').val(qq);
					$('#email').val(email);
					$('#address').val(address);
					$('#gender'+gender).attr('checked','true');
					$('.btnsave').bind("click", function(){
						userObj.saveUserInfo();
					});
					
					var bindAccount=obj.bindAccount
					var bankName=obj.bankName
					var branch=obj.branch
					var bindName=obj.bindName
					var telephone=obj.telephone
					
					if(bindAccount==''){
						$('#bindAccount').removeAttr('readonly');
						$('#bindAccount').css('border','1px solid #eee');
						$('.btnsavebank').css('display','inline-block');
					}
					if(bankName==''){
						$('#bankName').removeAttr('readonly');
						$('#bankName').css('border','1px solid #eee');
						$('.btnsavebank').css('display','inline-block');
					}
					if(branch==''){
						$('#branch').removeAttr('readonly');
						$('#branch').css('border','1px solid #eee');
						$('.btnsavebank').css('display','inline-block');
					}
					if(bindName==''){
						$('#bindName').removeAttr('readonly');
						$('#bindName').css('border','1px solid #eee');
						$('.btnsavebank').css('display','inline-block');
					}
					if(telephone==''){
						$('#telephone').removeAttr('readonly');
						$('#telephone').css('border','1px solid #eee');
						$('.btnsavebank').css('display','inline-block');
					}
					
					$('#bindAccount').val(bindAccount);
					$('#bankName').val(bankName);
					$('#branch').val(branch);
					$('#bindName').val(bindName);
					$('#telephone').val(telephone);
					
					$('.btnsavebank').bind("click", function(){
						userObj.bindBank();
					});
				}else{
					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	bindBank:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var bankName=$('#bankName').val();
		var branch=$('#branch').val();
		var bindName=$('#bindName').val();
		var bindAccount=$('#bindAccount').val();
		var telephone=$('#telephone').val();
		if(bankName==''){
			mui.alert('请输入银行',document.title, function() {});
			return;
		}
		if(branch==''){
			mui.alert('请输入分行',document.title, function() {});
			return;
		}
		if(bindName==''){
			mui.alert('请输入持卡人',document.title, function() {});
			return;
		}
		if(bindAccount==''){
			mui.alert('请输入卡号',document.title, function() {});
			return;
		}
		if(telephone==''){
			mui.alert('请输入预留手机号码',document.title, function() {});
			return;
		}
		var map = {};
		map['u'] =move.user.u;
		map['bankName'] =bankName;
		map['branch'] =branch;
		map['bindName'] =bindName;
		map['bindAccount'] =bindAccount;
		map['telephone'] =telephone;
		var mw=baseObj.mw(map);
		
		var btnArray = ['确认', '取消'];
		mui.confirm('请确认填写资料真实性，保存后不可修改！', document.title, btnArray, function(e) {
			if (e.index == 0) {
				var mask=move.createLoading();
				mask.show();
				$.ajax({
					type: "POST",
					url:move.bindBank,
					dataType:'json',
					data:{
						mw:mw
					},
					success: function(ret){
						mask.close();
						var code=ret.code;
						var data=ret.data;
						var msg=ret.msg;
						var html='';
						if(code=='200'){
							$('#bankName').val('');
							$('#branch').val('');
							$('#bindName').val('');
							$('#bindAccount').val('');
							$('#telephone').val('');
						}else{
						}
						mui.alert(msg,document.title, function() {});
					},
					error: function (jqXHR, textStatus, errorThrown) {
					}
				});
			}
		})
	},
	saveUserInfo:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var realName=$('#realName').val();
		var birthday=$('#birthday').val();
		var cellPhone=$('#phone').val();
		var qq=$('#qq').val();
		var email=$('#email').val();
		var address=$('#address').val();
		var gender=$("input[name='gender']:checked").val();
		if(realName==''){
			mui.alert('请输入真实姓名',document.title, function() {});
			return;
		}
		if(birthday==''){
			mui.alert('请输入出生日期',document.title, function() {});
			return;
		}
		if(gender==''){
			mui.alert('请选择性别',document.title, function() {});
			return;
		}
		if(cellPhone==''){
			mui.alert('请输入电话号码',document.title, function() {});
			return;
		}
		if(qq==''){
			mui.alert('请输入QQ号码',document.title, function() {});
			return;
		}
		if(email==''){
			mui.alert('请输入电子邮箱',document.title, function() {});
			return;
		}
		if(address==''){
			mui.alert('请输入联系地址',document.title, function() {});
			return;
		}
		
		var map = {};
		map['u'] =move.user.u;
		map['realName'] =realName;
		map['birthday'] =birthday;
		map['gender'] =gender;
		map['cellPhone'] =cellPhone;
		map['email'] =email;
		map['qq'] =qq;
		map['address'] =address;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.userSave,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				mui.alert(msg,document.title, function() {});
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	changePwd:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var oldpassword=$('#oldpassword').val();
		var newpassword=$('#newpassword').val();
		var repassword=$('#repassword').val();
		if(oldpassword==''){
			mui.alert('请输入旧登录密码',document.title, function() {});
			return;
		}
		if(newpassword==''){
			mui.alert('请输入新登录密码',document.title, function() {});
			return;
		}
		if(repassword==''){
			mui.alert('请确认新登录密码',document.title, function() {});
			return;
		}
		
		var map = {};
		map['u'] =move.user.u;
		map['newpassword'] =hex_md5(newpassword).toUpperCase();
		map['repassword'] =hex_md5(repassword).toUpperCase();
		map['oldpassword'] =hex_md5(oldpassword).toUpperCase();
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.changePwd,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				mui.alert(msg,document.title, function() {});
				if(code=='200'){
					$('#oldpassword').val('')
					$('#newpassword').val('')
					$('#repassword').val('')
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	changeCashPwd:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var oldpassword=$('#oldpassword').val();
		var newpassword=$('#newpassword').val();
		var repassword=$('#repassword').val();
		if(oldpassword==''){
			mui.alert('请输入旧取款密码',document.title, function() {});
			return;
		}
		if(newpassword==''){
			mui.alert('请输入新取款密码',document.title, function() {});
			return;
		}
		if(repassword==''){
			mui.alert('请确认新取款密码',document.title, function() {});
			return;
		}
		
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['cashPassword'] =hex_md5(newpassword).toUpperCase();
		map['recashPassword'] =hex_md5(repassword).toUpperCase();
		map['password'] =hex_md5(oldpassword).toUpperCase();
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.changeCashPwd,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				mui.alert(msg,document.title, function() {});
				if(code=='200'){
					$('#oldpassword').val('')
					$('#newpassword').val('')
					$('#repassword').val('')
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	buylist:function(){
		if(this.gameType==''){
			var gameType=move.getParameter('gameType');
			if(gameType){
				this.gameType=gameType;
			}
		}
		if(this.gfxyType==''){
			var gfxyType=move.getParameter('gfxyType');
			if(gfxyType){
				this.gfxyType=gfxyType;
			}
		}
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['gameType'] =this.gameType;
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var reqUrl = move.xybetList;
		// gfxyType 0:官方 1:信用
		if(this.gfxyType && this.gfxyType==0){
			reqUrl = move.betList;
		}
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:reqUrl,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var html='';
				var dataheadHtml = "";
				if(userObj.gfxyType && userObj.gfxyType==0){
					dataheadHtml += "<tr><th>时间</th>" +
					"<th>期号</th>" +
					"<th>彩种</th>" +
					"<th>玩法</th>" +
					"<th>倍数</th>" +
					"<th>认购金额</th>" +
					"<th>奖金</th>" +
					"<th>状态说明</th></tr>";
				}else{
					dataheadHtml += "<tr><th>彩种</th>" +
					"<th>期号</th>" +
					"<th>玩法</th>" +
					"<th>投注项</th>" +
					"<th>倍率</th>" +
					"<th>认购金额</th>" +
					"<th>我的奖金</th>" +
					"<th>状态</th>" +
					"<th>下注时间</th></tr>";
				}	
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var html='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						if(userObj.gfxyType && userObj.gfxyType==0){
							var gameName=obj.gameName;
							var betMoney=obj.betMoney;//认购金额
							var winCash=obj.winCash;//中奖金额
							var betTime=obj.betTime;//认购时间
							var multiple=obj.multiple;//认购时间
							var playName=obj.playName;// 玩法
							var sessionNo=obj.sessionNo;// 期号
							var winResult2=obj.winResult2;
							
							if(winResult2=='已中奖'){
								winResult2='<span class="red">'+winResult2+'</span>';
							}
							html=html+'<tr><td>'+betTime+'</td>';
							html=html+'<td>'+sessionNo+'</td>';
							html=html+'<td>'+gameName+'</td>';
							html=html+'<td>'+playName+'</td>';
							html=html+'<td>'+multiple+'倍</td>';
							html=html+'<td>'+betMoney+'</td>';
							html=html+'<td>'+winCash+'</td>';
							html=html+'<td>'+winResult2+'</td></tr>';
						}else{
							var winCash=obj.winCash;
							var betPoint=obj.betPoint;
							var payoff=obj.payoff;
							var betTime=obj.betTime;
							var betName=obj.betName;//方案金额
							var sessionNo=obj.sessionNo;//认购金额
							var betRate=obj.betRate;//中奖金额
							var winStatus=obj.winResult;//输赢
							var playName=obj.playName;//认购时间
							var gameName=obj.gameName;
							var winResult2=obj.winResult2;
							
							html=html+'<tr><td>'+gameName+'</td>';
							html=html+'<td>'+sessionNo+'</td>';
							html=html+'<td>'+playName+'</td>';
							html=html+'<td>'+betName+'</td>';
							html=html+'<td>'+betRate+'</td>';
							html=html+'<td>'+betPoint+'</td>';
							html=html+'<td>'+winCash+'</td>';
							var winResult = "";
							if(winStatus==1){
								winResult += '<span class="red">中奖</span>';
							}else{
								winResult += '<span >未中奖</span>';
							}
							html=html+'<td>'+winResult+'</td>';
							html=html+'<td>'+betTime+'</td></tr>';
						}
						
					}
					var pageIndex=data.pageIndex;
					var pageSize=data.pageSize;
					var total=data.total;
					var totalPage=total/pageSize;
					if(parseInt(total/pageSize)<totalPage){//不整除
						totalPage=totalPage+1;
					}
					totalPage=parseInt(totalPage);
					var nowpage=(pageIndex+1);
					$('.pager').show();
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
					$('.pager').hide();
					html='<tr><td colspan="10" class="notfound">抱歉！没有找到符合条件的结果！</td></tr>';
				}
				$('#databox').html(html);
				$('#datahead').html(dataheadHtml);

				if(move.pageIndex==0){
					userObj.bindPage();
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	finance:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['cashType'] =this.cashType;
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.baseData_accountDetails,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var html='';
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var html='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var tradeTime=obj.tradeTime;
						var income=obj.income;
						var pay=obj.pay;
						var userMoney=obj.userMoney;
						var remark=obj.remark;
						
						html=html+'<tr><td>'+tradeTime+'</td>';
						html=html+'<td>'+income+'</td>';
						html=html+'<td>'+pay+'</td>';
						html=html+'<td>'+userMoney+'</td>';
						html=html+'<td>'+remark+'</td></tr>';
					}
					var pageIndex=data.pageIndex;
					var pageSize=data.pageSize;
					var total=data.total;
					var totalPage=total/pageSize;
					if(parseInt(total/pageSize)<totalPage){//不整除
						totalPage=totalPage+1;
					}
					totalPage=parseInt(totalPage);
					var nowpage=(pageIndex+1);
					$('.pager').show();
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
					$('.pager').hide();
					html='<tr><td colspan="10" class="notfound">抱歉！没有找到符合条件的结果！</td></tr>';
				}
				$('#databox').html(html);
				if(move.pageIndex==0){
					userObj.bindPage();
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	pointlist:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['cashType'] =this.pointType;
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.baseData_pointDetails,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var html='';
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var html='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var tradeTime=obj.tradeTime;
						var income=obj.income;
						var pay=obj.pay;
						var userPoint=obj.userPoint;
						var remark=obj.remark;
						
						html=html+'<tr><td>'+income+'</td>';
						html=html+'<td>'+pay+'</td>';
						html=html+'<td>'+userPoint+'</td>';
						html=html+'<td>'+tradeTime+'</td>';
						html=html+'<td>'+remark+'</td></tr>';
					}
					var pageIndex=data.pageIndex;
					var pageSize=data.pageSize;
					var total=data.total;
					var totalPage=total/pageSize;
					if(parseInt(total/pageSize)<totalPage){//不整除
						totalPage=totalPage+1;
					}
					totalPage=parseInt(totalPage);
					var nowpage=(pageIndex+1);
					$('.pager').show();
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
					$('.pager').hide();
					html='<tr><td colspan="10" class="notfound">抱歉！没有找到符合条件的结果！</td></tr>';
				}
				$('#databox').html(html);
				if(move.pageIndex==0){
					userObj.bindPage();
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	paybind:function(){
		$(".paytypebox li").unbind("click");
		$(".paytypebox li").bind("click", function(){
			// console.error("___"+prefix);
			$(".paytypebox li").removeClass('selected');
			$(this).addClass('selected');
			var paytype=$(this).attr('data');
			$('#paytype').val(paytype);
			$('.rebox').hide();
			var prefix = $(".tabs.paytabs a.current").attr("data");
			if(prefix&&prefix.length>0){
				$("#"+prefix+' #re'+paytype).show();
			}
			$('#re'+paytype).show();
		});
		$(".paytabs .tab").unbind("click");
		$(".paytabs .tab").bind("click", function(){
			$(".paybox").hide();
			var idzz=$(this).attr('data');
			$("#"+idzz).show();
			$(".paytabs .tab").removeClass('current');
			$(this).addClass('current');
		});
		$(".rechargelistbox .pricebox").unbind("click");
		$(".rechargelistbox .pricebox").bind("click", function(){
			var rid=$(this).attr('data');
			$('#rid').val(rid);
			$(".rechargelistbox .pricebox").removeClass('selected');
			$(this).addClass('selected');
		});
		// $(".paytypebox li").unbind("click");
		// $(".paytypebox li").bind("click",function(){
		// 	var data = $(this).attr("data");
		// 	$("#re"+data).show();
		// 	$(".paytypebox li").removeClass("selected");
		// 	$(this).addClass('selected');
		// });
	},
	cashSubmit:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var money=$('#money').val();
		var password=$('#password').val();
		var code=$('#code').val();
		var bankName=$("#bankList option:selected").val();
		var cid=$("#regionList option:selected").val();
		var accountNo=$('#cardNo').val();
		var code=$('#code').val();
		var type=$('#cashType').text();
		var userName=$('#userName').val();
		if(money==''){
			mui.alert('请输入要提现的金额',document.title, function() {});
			return;
		}
		if(password==''){
			mui.alert('请输入取款密码',document.title, function() {});
			return;
		}
		if(bankName==''){
			mui.alert('请选择所属银行',document.title, function() {});
			return;
		}
		if(cid==''){
			mui.alert('请选择开户行城市',document.title, function() {});
			return;
		}
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['cashMoney'] =money;
		map['cashPassword'] =hex_md5(password).toUpperCase();
		map['bankName'] =bankName;
		map['accountNo'] =accountNo;
		map['cid'] =cid;
		map['type'] =type;
		map['userName'] =userName;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.cashSubmit,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				mui.alert(msg,document.title, function() {});
				if(code=='200'){
					var money=data.money;
					move.user.money=money;
					$('.logined .money').text(money);
					store.set('user', move.user);
					$('#money').val('')
					$('#password').val('')
					userObj.money();
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	cashPointExchange:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var point=$('#point').val();
		if(point==''){
			mui.alert('请输入要提现的金额',document.title, function() {});
			return;
		}
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['point'] =point;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.cash_pointExchange,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				if(code=='200'){
					var points=data.points;
					move.user.points=points;
					var cashmoney=parseInt(points/100);
					$('.info .point').text(points);
					$('.info .cashmoney').text(cashmoney+"元");
					$('.logined .point').text(points);
					store.set('user', move.user);
					$('#point').val('')
				}
				move.closeLoading();
				mui.alert(msg,document.title, function() {});
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	rechargeList:function(){
		move.createLoading().show();
		$.ajax({
			type: "POST",
			url:move.rechargeList2,
			dataType:'json',
			data:{},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				var html='';
				var payHtml='';
				if(code=='200'){
					var items=data.items;
//					var pt=data.pt;
					for(var i=0;i<items.length;i++) {
						var temp=items[i];
						var type=temp.type;
						var title=temp.title;
						var img=temp.img;
						var clazz='';
						var style='';
						if(i==0){
							clazz='selected';
							style='display: inline-block;';
							$('#paytype').val(type);
						}
						if(type=='1'||type=='11'){
							img='/images/pay/zhifubao.png';
						}else if(type=='51'){
							img='/images/pay/weixin.png';
						}
						
						payHtml=payHtml+'<li class="'+clazz+'" data="'+type+'" title="'+title+'"><img src="'+img+'"></li>';
						var arr=temp.moneyArr;
						html=html+'<div class="rebox" id="re'+type+'" style="'+style+'">';
						for(var j=0;j<arr.length;j++) {
							var obj=arr[j];
							var rid=obj.rid;
							var price=obj.price;
							var parValue=obj.parValue;
							if(j==0||j==4){
								html=html+'<div>';
							}
							html=html+'<span class="pricebox curosr" data="'+rid+'">'+parValue+'</span>';
							if(j==3||j==7){
								html=html+'</div>';
							}
						}
						html=html+'</div>';
					}
					$('.paytypebox').html(payHtml);
					$('.rechargelistbox').html(html);
					
					userObj.paybind();
				}else{
					mui.alert(msg,document.title, function() {});
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	recharge:function(){
		var rid=$('#rid').val();
		var payType=$('#paytype').val();
		if(payType==''){
			mui.alert('请选择充值方式',document.title, function() {});
			return;
		}
		if(rid==''){
			mui.alert('请选择充值金额',document.title, function() {});
			return;
		}
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['rid'] =rid;
		map['device'] ='0';
		map['payType'] =payType;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.rechargeSubmit,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				var html='';
				if(code=='200'){
					var payObj=data.bxsPayObj;
					var payUrl=payObj.payUrl;
					baseObj.openView(payUrl);
				}else{
					mui.alert(msg,document.title, function() {});
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	messageList:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.messageList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				var html='';
				if(code=='200'){
					var items=data.items;
					
					for(var i=0;i<items.length;i++) {
						var temp=items[i];
						var title=temp.title;
						var id=temp.id;
						var content=temp.content;
						var createTime=temp.createTime;
		    			var status=temp.status;
		    			var statusStr = "";
		    			var clazz = "";
		    			if(status==1){
		    				statusStr = "未读";
		    				clazz = "colour-green";
		    			}else if(status==2){
		    				statusStr = "已读";
		    			}
						html=html+'<tr><td>'+(i+1)+'</td>';
						html=html+'<td><a href="/user/message/'+id+'">'+title+'</a></td>';
						html=html+'<td><a href="/user/message/'+id+'">'+content+'</a></td>';
						html=html+'<td><span class="'+clazz+'"">'+statusStr+'</span></td>';
						html=html+'<td>'+createTime+'</td></tr>';
					}
					var pageIndex=data.pageIndex;
					var pageSize=data.pageSize;
					var total=data.total;
					var totalPage=total/pageSize;
					if(parseInt(total/pageSize)<totalPage){//不整除
						totalPage=totalPage+1;
					}
					totalPage=parseInt(totalPage);
					var nowpage=(pageIndex+1);
					$('.pager').show();
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
					$('.pager').hide();
					html='<tr><td colspan="10" class="notfound">抱歉！没有找到符合条件的结果！</td></tr>';
				}
				$('#databox').html(html);
				if(move.pageIndex==0){
					userObj.bindPage();
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	messageView:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var url=window.location;
		if(url){
			url=url+"";
			var id=url.substring(url.lastIndexOf('/')+1,url.length);
			move.createLoading().show();
			var map = {};
			map['u'] =move.user.u;
			map['id'] =id;
			var mw=baseObj.mw(map);
			var mask=move.createLoading();
			mask.show();
			$.ajax({
				type: "POST",
				url:move.messageView,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					mask.close();
					var code=ret.code;
					var data=ret.data;
					var msg=ret.msg;
					var html='';
					if(code=='200'){
						var title=data.title;
						var content=data.content;
						$('#title').text(title);
						$('#content').text(content);
					}else{
					}
					move.closeLoading();
				},
				error: function (jqXHR, textStatus, errorThrown) {
					mask.close();
				}
			});
		}
	},
	payQRCode:function(){
		var map = {};
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.payQRCode,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				if(code=='200'){
					var alipayObj=data.alipayObj;
					var val=alipayObj.val;
					$('#alipaybox img').attr('src',val);
					var wechatObj=data.wechatObj;
					var val2=wechatObj.val;
					$('#wechatbox img').attr('src',val2);
					var tipsObj=data.tipsObj;
					var val3=tipsObj.val;
					if(val3!=''){
						$('#huibox').html(val3);
					}
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	cashList:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.cashList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				var html='';
				if(code=='200'){
					var items=data.items;
					
					for(var i=0;i<items.length;i++) {
						var temp=items[i];
						var cashMoney=temp.cashMoney;
						var createTime=temp.createTime;
						var status=temp.auditStatus;
						var remark=temp.remark;
						var statusName="";
						var statusClass="";
						if(status=='0'){
							statusName='审核中';
							statusClass='colour-orange';
						}else if(status=='1'){
							statusName='通过';
							statusClass='colour-blue';
						}else if(status=='2'){
							statusName='拒绝';
							statusClass='colour-red';
						}
						html=html+'<tr><td>'+(i+1)+'</td>';
						html=html+'<td>'+cashMoney+'</td>';
						html=html+'<td>'+createTime+'</td>';
						html=html+'<td><span class="'+statusClass+'">'+statusName+'</span></td>';
						html=html+'<td>'+(remark?remark:"")+'</td>';
						html=html+'</tr>';
					}
					var pageIndex=data.pageIndex;
					var pageSize=data.pageSize;
					var total=data.total;
					var totalPage=total/pageSize;
					if(parseInt(total/pageSize)<totalPage){//不整除
						totalPage=totalPage+1;
					}
					totalPage=parseInt(totalPage);
					var nowpage=(pageIndex+1);
					$('.pager').show();
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
					$('.pager').hide();
					html='<tr><td colspan="5" class="notfound">抱歉！没有找到符合条件的结果！</td></tr>';
				}
				$('#databox').html(html);
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	rechargeDetail:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		move.createLoading().show();
		var map = {};
		map['u'] =move.user.u;
		map['cashType'] =this.cashType;
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.rechargeDetail,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var html='';
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var html='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var tradeTime=obj.createDate;
						var pay=obj.money;
						var userMoney=obj.userMoney;
						var remark=obj.title;
						var status=obj.status;
						// var subTitle=obj.title;
						// if(subTitle=='未付款'){
						// 	subTitle='<span class="red">'+subTitle+'</span>';
						// }
						var statusStr = "";
						if(status==0){
							statusStr='<span >充值中</span>';
						}else if(status==1){
							statusStr='<span class=\"colour-green\">充值成功</span>';
						}else if(status==2){
							statusStr='<span class=\"colour-red\">充值失败</span>';
						}
						
						html=html+'<tr><td>'+tradeTime+'</td>';
						html=html+'<td>'+pay+'</td>';
						html=html+'<td>'+remark+'</td>';
						html=html+'<td>'+statusStr+' </td></tr>';
					}
					var pageIndex=data.pageIndex;
					var pageSize=data.pageSize;
					var total=data.total;
					var totalPage=total/pageSize;
					if(parseInt(total/pageSize)<totalPage){//不整除
						totalPage=totalPage+1;
					}
					totalPage=parseInt(totalPage);
					var nowpage=(pageIndex+1);
					$('.pager').show();
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
					$('.pager').hide();
					html='<tr><td colspan="10" class="notfound">抱歉！没有找到符合条件的结果！</td></tr>';
				}
				$('#databox').html(html);
				if(move.pageIndex==0){
					userObj.bindPage();
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	loadGameList:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		move.createLoading().show();
		$.ajax({
			type: "POST",
			url:move.lotteryNameAndType,
			dataType:'json',
			data:{},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var html='';
				if(code=='200'){
					var result=ret.data;
					var gfItems=result.gfItems;
					var xyItems=result.xyItems;
					var gfHtml='';
					if(gfItems){
						for(var i=0;i<gfItems.length;i++){
							var obj=gfItems[i];
							var gameName=obj.gameName;
							var gameType=obj.gameType;
							gfHtml=gfHtml+'<option class="gf" title="'+gameName+'-官方" value="'+gameType+'">'+gameName+'-官方</option>';
						}
					}
					// $(".gameType").append(gfHtml);
					var xyHtml='';
					if(xyItems){
						for(var i=0;i<xyItems.length;i++){
							var obj=xyItems[i];
							var gameName=obj.gameName;
							var gameType=obj.gameType;
							xyHtml=xyHtml+'<option class="xy" title="'+gameName+'-信用" value="'+gameType+'">'+gameName+'-信用</option>';
						}
					}
					xyHtml += gfHtml;
					$(".gameType").append(xyHtml);
				}else{
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	cityList:function(){
		$.ajax({
			type: "POST",
			url:move.cityList,
			dataType:'json',
			data:{
			},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					var items=ret.data.items;
					if(code=='200'){
		    		var data=ret.data;
		    		var items=data.items;
		    		store.set('cityList',items);
		    		var html='';
		    		html += "<option>请选择</option>";
		    		for (var i=0;i<items.length;i++) {
		    			var tmp = items[i];
		    			html += "<option value="+tmp.value+">";
		    			html += tmp.text;
		    			html += "</option>";
					}
			    	$('#provinceList').html(html);
			    	$("#provinceList").change(function(){
			    		userObj.changeCity(1,$(this).val());
			    	});
			    	}else{
			    		mui.alert(msg,document.title, function() {
			    		});
			    	}
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	changeCity:function(type,val){
		var items = store.get("cityList");
		var shtml = "<option>请选择</option>";
		var targetItem = null;
		for(var i = 0;i<items.length;i++){
			var tmp1 = items[i];
			var items1 = tmp1.children;
			if(type==1){
				if(tmp1.value == val){
					targetItem = tmp1;
					break;
				}
			}else{
				
				for(var j = 0;j<items1.length;j++){
					var tmp2 = items1[j];
					var items2 = tmp2.children;
					if(type==2){
						if(tmp2.value == val){
							targetItem = tmp2;
							break;
						}
					}
				}
			}
		}

		if(targetItem){
			var children = targetItem.children;
			for(var i = 0;i<children.length;i++){
				var tmp = children[i];
				shtml += "<option value="+tmp.value+">";
    			shtml += tmp.text;
    			shtml += "</option>";
			}
			if(type==1){
				$("#cityList").html(shtml);
				$("#cityList").change(function(){
		    		userObj.changeCity(2,$(this).val());
		    	});
			}else{
				$("#regionList").html(shtml);
			}

		}
	},
	bankList:function(){
		$.ajax({
		    type: "POST",
		    url:move.bankList,
		    dataType:'json',
		    data:{},
		    success: function(ret){
		    	var code=ret.code;
		    	var msg=ret.msg;
		    	if(code=='200'){
		    		var data=ret.data;
		    		var items=data.items;
		    		store.set('banklist',items);
		    		$("#cashType").text(items.cashType);
		    		var html='';
		    		html += "<option>请选择</option>";
		    		var subitems = items[0].subitems;
		    		for (var i=0;i<subitems.length;i++) {
		    			html += "<option value="+subitems[i]+">";
		    			html += subitems[i];
		    			html += "</option>";
					}
		    		$('#bankList').html(html);
		    	}else{
		    		mui.alert(msg,document.title, function() {
		    		});
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        }
		});
	},
	rechargeSwitch:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var map = {};
		map['u'] =move.user.u;
		var mw=baseObj.mw(map);

		$.ajax({
		    type: "POST",
		    url:move.userRechargeWay,
		    dataType:'json',
		    data:{
		    	mw:mw
		    },
		    success: function(ret){
		    	var code=ret.code;
		    	var shtml = "";
		    	if(code=='200'){
		    		var data=ret.data;
		    		var items = data.items;
		    		for(var i =0;i<items.length;i++){
		    			var tmp = items[i];
		    			
		    			if(tmp.channelType==2){
		    				// 快速充值
		    				shtml += "<div class=\"paybox\" id=\"fastrecharge\" style=\"display:none\">";
		    				var subHtml1 = "";
		    				var subHtml2 = "";
		    				for(var j=0;j<tmp.wayArr.length;j++){
		    					var subTmp = tmp.wayArr[j];
		    					subHtml1 += "<li class=\"\" data=\""+subTmp.id+"\" title=\""+subTmp.title+"\"><span>"+subTmp.title+"</span></li>";
		    					
		    					subHtml2 += "<div class=\"rebox\" id=\"re"+subTmp.id+"\" style=\"display: none;\">";
		    					subHtml2 += "<span class=\"btn\" onclick=\"javascript:userObj.openRecharge('"+subTmp.url+"')\"><a href="+subTmp.url+">"+subTmp.title+"</a></span>";
		    					subHtml2 += "</div>";
		    				}

		    				shtml += "<div class=\"inputline\">";
		    				shtml += "<label>充值方式：</label>";
		    				shtml += "<div class=\"paytypebox\">";
		    				shtml += "<ul >";
		    				shtml += subHtml1;
		    				shtml += "</ul>";
		    				shtml += "</div>";
		    				shtml += "</div>";

		    				shtml += "<div class=\"inputline rechargeline\">";
		    				shtml += "<label></label>";
		    				shtml += "<div class=\"rechargelistbox\">";
		    				shtml += subHtml2;
		    				shtml += "</div></div>";
		    				
		    			}else if(tmp.channelType==3){
		    				// 固定码充值
		    				shtml += "<div class=\"paybox\" id=\"qrcoderecharge\" style=\"display:none\">";
		    				var subHtml1 = "";
		    				var subHtml2 = "";
		    				for(var j=0;j<tmp.wayArr.length;j++){
		    					var subTmp = tmp.wayArr[j];
		    					subHtml1 += "<li class=\"\" data=\""+subTmp.id+"\" title=\""+subTmp.title+"\"><span>"+subTmp.title+"</span></li>";
		    					
		    					subHtml2 += "<div class=\"rebox\" id=\"re"+subTmp.id+"\" style=\"display: none;\">";
		    					subHtml2 += "<div class=\"line-highly\">";
		    					subHtml2 += "<img src=\""+subTmp.img+"\">";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label></label>";
		    					subHtml2 += "<div class=\"subline\"><span>充值完毕,在下面提交充值信息，才会上分</span></div>";

		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>充值金额：</label><div class=\"subline\"><input id=\"money\" maxlength=\"8\" type=\"text\" required=\"required\" class=\"input\" onkeyup=\"value=value.replace(/[^\\d|\\.]/g,\'\')\" onafterpaste=\"if(isNaN(value))execCommand('undo')\"></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label >ID：</label><div class=\"subline\"><span id=\"uid\">"+move.user.uid+"</span></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label ></label><span class=\"btnrecharge\">确定</span>";
		    					subHtml2 += "</div></div>";
		    				}

		    				shtml += "<div class=\"inputline\">";
		    				shtml += "<label>充值方式：</label>";
		    				shtml += "<ul class=\"paytypebox\">";
		    				shtml += subHtml1;
		    				shtml += "</ul>";
		    				shtml += "</div>";
		    				shtml += "<div class=\"inputline rechargeline\">";
		    				shtml += "<label></label>";
		    				shtml += "<div class=\"rechargelistbox\">";
		    				shtml += subHtml2;
		    				shtml += "</div></div>";
		    			}else if(tmp.channelType==4){
		    				// 线下充值一
		    				shtml += "<div class=\"paybox\" id=\"comrecharge1\" style=\"display:none\">";

		    				var subHtml1 = "";
		    				var subHtml2 = "";
		    				for(var j=0;j<tmp.wayArr.length;j++){
		    					var subTmp = tmp.wayArr[j];
		    					subHtml1 += "<li class=\"\" data=\""+subTmp.id+"\" title=\""+subTmp.title+"\"><span>"+subTmp.title+"</span></li>";
		    					
		    					subHtml2 += "<div class=\"rebox\" id=\"re"+subTmp.id+"\" style=\"display: none;\">";
		    					subHtml2 += "<div class=\"line-highly\">";
		    					subHtml2 += "<img src=\""+subTmp.img+"\">";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>用户名：</label><div class=\"subline\"><span class=\"userName\">"+subTmp.userName+"</span></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>国家：</label><div class=\"subline\"><span class=\"country\">"+subTmp.des+"</span></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>渠道：</label><div class=\"subline\"><span class=\"bankName\">"+subTmp.bankName+"</span></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>银行账户：</label><div class=\"subline\"><span class=\"bankNo\">"+subTmp.bankAccount+"</span></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>充值金额：</label><div class=\"subline\"><input id=\"money\" maxlength=\"8\" type=\"text\" required=\"required\" class=\"input\" onkeyup=\"value=value.replace(/[^\\d|\\.]/g,\'\')\" onafterpaste=\"if(isNaN(value))execCommand('undo')\"></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>存款人姓名：</label><div class=\"subline\"><input id=\"userName2\" maxlength=\"10\" type=\"text\" required=\"required\" class=\"input\"/></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label ></label><span class=\"btnrecharge\">确定</span>";
		    					subHtml2 += "</div></div>";
		    				}

		    				shtml += "<div class=\"inputline\">";
		    				shtml += "<label>充值方式：</label>";
		    				shtml += "<div class=\"paytypebox\">";
		    				shtml += "<ul >";
		    				shtml += subHtml1;
		    				shtml += "</ul>";
		    				shtml += "</div>";
		    				shtml += "</div>";
		    				shtml += "<div class=\"inputline rechargeline\">";
		    				shtml += "<label></label>";
		    				shtml += "<div class=\"rechargelistbox\">";
		    				shtml += subHtml2;
		    				shtml += "</div></div>";

		    			}else if(tmp.channelType==5){
		    				// 线下充值二
		    				shtml += "<div class=\"paybox\" id=\"comrecharge2\" style=\"display:none\">";

		    				var subHtml1 = "";
		    				var subHtml2 = "";
		    				for(var j=0;j<tmp.wayArr.length;j++){
		    					var subTmp = tmp.wayArr[j];
		    					subHtml1 += "<li class=\"\" data=\""+subTmp.id+"\" title=\""+subTmp.title+"\"><span>"+subTmp.title+"</span></li>";
		    					
		    					subHtml2 += "<div class=\"rebox\" id=\"re"+subTmp.id+"\" style=\"display: none;\">";
		    					subHtml2 += "<div class=\"line-highly\">";
		    					subHtml2 += "<label><img src=\""+subTmp.img+"\"></label>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>用户名：</label><div class=\"subline\"><span class=\"userName\">"+subTmp.userName+"</span></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>国家：</label><div class=\"subline\"><span class=\"country\">中国</span></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>渠道：</label><div class=\"subline\"><span class=\"bankName\">"+subTmp.bankName+"</span></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>银行账户：</label><div class=\"subline\"><span class=\"bankNo\">"+subTmp.bankAccount+"</span></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>充值金额：</label><div class=\"subline\"><input id=\"money\" maxlength=\"8\" type=\"text\" required=\"required\" class=\"input\" onkeyup=\"value=value.replace(/[^\\d|\\.]/g,\'\')\" onafterpaste=\"if(isNaN(value))execCommand('undo')\"></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label>存款人姓名：</label><div class=\"subline\"><input id=\"userName2\" maxlength=\"10\" type=\"text\" required=\"required\" class=\"input\"/></div>";
		    					subHtml2 += "</div>";
		    					subHtml2 += "<div class=\"line\">";
		    					subHtml2 += "<label ></label><span class=\"btnrecharge\">确定</span>";
		    					subHtml2 += "</div></div>";
		    				}

		    				shtml += "<div class=\"inputline\">";
		    				shtml += "<label>充值方式：</label>";
		    				shtml += "<ul class=\"paytypebox\">";
		    				shtml += subHtml1;
		    				shtml += "</ul>";
		    				shtml += "</div>";
		    				shtml += "<div class=\"inputline rechargeline\">";
		    				shtml += "<label></label>";
		    				shtml += "<div class=\"rechargelistbox\">";
		    				shtml += subHtml2;
		    				shtml += "</div></div>";
		    			}
		    			shtml += "</div>";
		    		}

		    		$(".exchange").html(shtml);
		    		userObj.paybind();
		    		$(".paybox .btnrecharge").unbind("click");
		    		$(".paybox .btnrecharge").bind("click",function(){
		    			userObj.rechargeSubmit();
		    			// userObj.codeRechargeSubmit();
		    		});

		    		$(".tabs.paytabs a").eq(0).trigger("click");
		    	}else{
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        }
		});
	},
	bankMsg:function(){
		$.ajax({
		    type: "POST",
		    url:move.bankMsg,
		    dataType:'json',
		    data:{},
		    success: function(ret){
		    	var code=ret.code;
		    	var msg=ret.msg;
		    	if(code=='200'){
		    		var data=ret.data;
		    		var items=data.items;
		    		store.set('banklist',items);
		    		var shtml = "";
		    		shtml += "<div class=\"inputline\">";	
		    		shtml += "<label>充值方式：</label>";
		    		// shtml += "<ul class=\"paytypebox\">";
		    		for(var i = 0;i<items.length;i++){
		    			var tmp = items[i];
		    			if(i%2==0){
		    				if(i>0){
		    					shtml += "<label></label><ul class=\"paytypebox\">";
		    				}else{
		    					shtml += "<ul class=\"paytypebox\">";
		    				}
		    				
		    			}
		    			if(i==0){
		    				shtml += "<li class=\"selected\" data="+(i+1)+" >"+tmp.bankName+"</li>";
		    			}else{
		    				shtml += "<li  data="+(i+1)+" >"+tmp.bankName+"</li>";
		    			}
		    			if(i%2==1){
		    				shtml += "</ul>";
		    			}
		    			
		    		}
		    		shtml += "</div>";
		    		shtml += "<div class=\"inputline rechargeline\">";
		    		shtml += "<div class=\"rechargelistbox\">";

		    		for(var i = 0;i<items.length;i++){
		    			var tmp = items[i];
		    			if(i==0){
		    				shtml += "<div class=\"rebox\" id=\"re"+(i+1)+"\" style=\"display: inline-block;\">";
		    			}else{
		    				shtml += "<div class=\"rebox\" id=\"re"+(i+1)+"\" style=\"display: none;\">";
		    			}
		    			shtml += "<div class=\"line\">";
		    			shtml += "<span class=\"title\">收款人信息</span></div>";
		    			shtml += "<div class=\"line\">";
		    			shtml += "<label class=\"receiveBankName\" >"+tmp.bankName+"</label></div>";
		    			shtml += "<div class=\"line\">";
		    			shtml += "<label class=\"receiveBankAccount\" >"+tmp.bankAccount+"</label></div>";
		    			shtml += "<div class=\"line\">";
		    			shtml += "<label class=\"receiveUserName\">"+tmp.userName+"</label></div>";
		    			shtml += "<div class=\"line\">";
		    			shtml += "<label><span class=\"receiveCountry\" >"+tmp.country+"</span></label></div>";

		    			shtml += "<div class=\"line\">";
		    			shtml += "<span class=\"title\">存款人信息</span></div>";
		    			shtml += "<div class=\"line\">";
		    			shtml += "<label >存款银行</label>";
		    			shtml += "<input id=\"money\" type=\"text\" required=\"required\" class=\"input depositorBankName\" /> </div>";
		    			shtml += "<div class=\"line\">";
		    			shtml += "<label >存款人姓名</label>";
		    			shtml += "<input id=\"money\" type=\"text\" required=\"required\" class=\"input depositorUserName\" /> </div>";
		    			shtml += "<div class=\"line\">";
		    			shtml += "<label >存款人账号</label>";
		    			shtml += "<input id=\"money\" type=\"text\" required=\"required\" class=\"input depositorBankAccount\" /> </div>";
		    			shtml += "<div class=\"line\">";
		    			shtml += "<label >存款金额</label>";
		    			shtml += "<input id=\"money\" type=\"text\" onkeyup=\"value=value.replace(/[^\\d|\\.]/g,\'\')\" maxlength=\"8\" required=\"required\" class=\"input money\" /> </div>";
		    			shtml += "<div class=\"line\">";
		    			shtml += "<label >存款方式</label>";
		    			shtml += "<select class=\"depositType\">";
		    			shtml += "<option value=\"1\">银行柜台存入</option>";
		    			shtml += "<option value=\"2\">手机转账</option>";
		    			shtml += "<option value=\"3\">网银转账</option>";
		    			shtml += "<option value=\"4\">ATM自动柜员机</option>";
		    			shtml += "<option value=\"5\">ATM现金存入</option>";
		    			shtml += "</select>";
		    			
			    		shtml += "<div class=\"line\"><label style=\"width:80px\"></label>";
			    		shtml += "<span class=\"btnrecharge\">确定</span></div>";
		    			shtml += "</div>";
		    			shtml += "</div>";
		    		}
		    		shtml += "</div>";
		    		shtml += "</div>";

		    		$("#huibox").html(shtml);
		    		userObj.paybind("huibox");
		    		$("#huibox .btnrecharge").unbind("click");
		    		$("#huibox .btnrecharge").bind("click",function(){
		    			userObj.offlineRechargeSubmit();
		    		});

		    	}else{
		    		mui.alert(msg,document.title, function() {
		    		});
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        }
		});
	},
	rechargeSubmit:function(){
		var currA = $(".tabs.paytabs a.current");
		if(currA){
			var data = $(currA).attr("data");
			if(data=="qrcoderecharge"){
				userObj.codeRechargeSubmit();
			}else if(data=="comrecharge1"){
				userObj.offlineSubmit();
			}else if(data=="comrecharge2"){
				userObj.offlineSubmit();
			}
		}
	},
	codeRechargeSubmit:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		if(!$("#money").val().length>0){
			return false;
		}
		if(!$("#uid").text().length>0){
			return false;
		}
		var map = {};
		map['u'] =move.user.u;
		map['money'] =$("#money").val();
		map['ID'] =$("#uid").text().trim();
		map['payType']='3';
		var mw=baseObj.mw(map);

		var mask=move.createLoading();
		mask.show();
		$.ajax({
		    type: "POST",
		    url:move.codeRechargeSubmit,
		    dataType:'json',
		    data:{
		    	mw:mw
		    },
		    success: function(ret){
		    	var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;

		    	mask.close();
		    	var code=ret.code;
		    	if(code=='200'){
		    		$("#money").val("");
		    		mui.alert(msg,document.title, function() {});
		    	}else{
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        	mask.close();
	        }
		});
	},
	offlineSubmit:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var currLi = $(".paytypebox li.selected");
		if(currLi){
			var data = $(currLi).attr("data");
			var bank=$('#re'+data+' .bankName').text();
			var bankNo=$('#re'+data+' .bankNo').text();
			var name=$('#re'+data+' .userName').text();
			var country=$('#re'+data+' .country').text();

			if(bank==''){
			mui.alert('收款人银行有错，请刷新重试',document.title, function() {});
			return;
			}
			if(bankNo==''){
				mui.alert('收款人卡号有错，请刷新重试',document.title, function() {});
				return;
			}
			if(name==''){
				mui.alert('收款人姓名有错，请刷新重试',document.title, function() {});
				return;
			}
			if(country==''){
				mui.alert('收款人国籍有错，请刷新重试',document.title, function() {});
				return;
			}
		}
		
		
//		var bank2=$('#bank2').val();
//		var bankNo2=$('#bankNo2').val();
		var name2=$('#re'+data+' #userName2').val();
		var money=$('#re'+data+' #money').val();
		// var depositType=$('re'+data+' .depositType').val();
//		if(bank2==''){
//			mui.alert('请输入汇款银行',document.title, function() {});
//			return;
//		}
		if(name2==''){
			mui.alert('请输入存款人姓名',document.title, function() {});
			return;
		}
//		if(bankNo2==''){
//			mui.alert('请输入存款人帐号',document.title, function() {});
//			return;
//		}
		if(money==''){
			mui.alert('请输入存款金额',document.title, function() {});
			return;
		}
//		if(depositType==''){
//			mui.alert('请选择存款类型',document.title, function() {});
//			return;
//		}
		
		var btnArray = ['确定', '取消'];
		mui.confirm('确认提交', document.title, btnArray, function(e) {
			if (e.index == 0) {
				var map = {};
				map['u']=move.user.u;
				map['receiveBankName']=bank;
				map['receiveBankAccount']=bankNo;
				map['receiveUserName']=name;
				map['receiveCountry']=country;
//				map['depositorBankName']=bank2;
//				map['depositorBankAccount']=bankNo2;
				map['depositorUserName']=name2;
//				map['depositType']=depositType;
				map['money']=money;
				var mw=baseObj.mw(map);
				var mask=move.createLoading();
				mask.show();
				$.ajax({
				    type: "POST",
				    url:move.rechargeOfflineSubmit,
				    dataType:'json',
				    data:{
				    	mw:mw
				    },
				    success: function(ret){
				    	mask.close();
				    	var code=ret.code;
				    	var msg=ret.msg;
				    	if(code=='200'){
				    		$(' #userName2').val("");
							$(' #money').val("");
				    		mui.alert('提交成功',document.title, function(){});
				    		
				    	}else{
				    		mui.alert(msg,document.title, function() {});
				    	}
				    },
			        error: function (jqXHR, textStatus, errorThrown) {
			        	mask.close();
			        }
				});
			}
		});
	},
	openRecharge:function(url){
		window.open(url);
		mui.alert("充值中");
		// baseObj.openView(url);
	}
	
}

$(document).ready(function(e) {
	userObj.init();
	$('#kinds').mousemove(function(e){
		$('#kinds').show();
	});
	$('.selebox').mousemove(function(e){
		$('#kinds').show();
	});
	$('#kinds').mouseout(function(e){
		$('#kinds').hide();
	});
	$('.selebox').mouseout(function(e){
		$('#kinds').hide();
	});
});