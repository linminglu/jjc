var meObj={
	me:function(pageType){
		meObj.loadHead();
		meObj.loadMessage();
		if(move.user.u!=null){
			$('#loginbtn').hide();
			$('#u').text(move.user.loginName);
			$('#userId').text("ID："+move.user.uid);
			if(move.user.logo) $('#head-img').attr("src",move.user.logo);
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
			    	if(code=='200'){
			    		var data=ret.data;
			    		var money=data.money;
			    		var drawMoney=data.drawMoney;
			    		var userBalance=data.userBalance;
			    		var addUpRechargeMoney=data.addUpRechargeMoney;// 用户积分
			    		var logo=data.logo;
			    		var level=data.level;// 用户等级
			    		var nowLevelPoint=data.nowLevelPoint;// 当前等级的积分
			    		var nextLevelPoint=data.nextLevelPoint;// 下一等级的积分
			    		var scale=data.scale;// 进度条的比例
			    		var isTopLevel=data.isTopLevel;// 是否是顶级
			    		var qqUrl=data.qqUrl;// qq客服的点击地址
			    		if(money!=''){
			    			$('#yue').text(money);
			    			store.set('money',money);
			    			move.user.money=money;
			    		}
			    		if(drawMoney!=''){
			    			$('#tikuan').text(drawMoney);
			    		}
			    		if(userBalance!=''){
			    			$('#shuying').text(userBalance);
			    		}
			    		if(logo){
				    		var head=$('#head-img').attr('src');
				    		if(head!=logo){
				    			$('#head-img').attr('src',logo);
				    			var user=store.get('user');
				    			user.logo=logo;
				    			store.set('user',user);
				    		}
				    	}
			    		if (level) {
			    			if(isTopLevel=='1'){
			    				$('#now-level-icon').attr('src',"images/level/level_icon_"+(level-1)+".png");
			    				$('#next-level-icon').attr('src',"images/level/level_icon_"+level+".png");
			    				$('#lackPoint').html(0);
			    			}else{
			    				$('#now-level-icon').attr('src',"images/level/level_icon_"+level+".png");
			    				$('#next-level-icon').attr('src',"images/level/level_icon_"+(level+1)+".png");
			    				$('#lackPoint').html(nextLevelPoint-addUpRechargeMoney);
			    			}
			    			$('#level-img').attr('src',"images/level/level-"+level+".png");
			    			$('#level-img').css("width", "100%");
			    			$('#level-icon').attr('src',"images/level/level_icon_"+level+".png");
			    			$('.level-cell').css("display", "flex");
			    		}
			    		$('#nowLevelPoint').html(nowLevelPoint);
			    		$('#nextLevelPoint').html(nextLevelPoint);
			    		$('#myPoint').html(addUpRechargeMoney);
			    		$('.scale-line2').css("width", scale*100+"%");
			    	
			    		if(qqUrl){
			    			$('#qqCustomer').attr('href', "javascript:baseObj.openView('"+qqUrl+"');");
			    		}
			    	}
			    },
		        error: function (jqXHR, textStatus, errorThrown) {
		        }
			});
		}else{
			$('#u').hide();
			$('#loginbtn').show();
		}
	},
	logout:function(){//登出
		if(move.user!=null){
			var map = {};
			map['u'] =move.user.u;
			var mw=baseObj.mw(map);
			$.ajax({
			    type: "POST",
			    url:move.logout,
			    dataType:'json',
			    data:{
			    	mw:mw
			    },  
			    success: function(ret){
			    	var code=ret.error_no;
			    	if(code=='200'){
			    		user.clear();
			    	}else{
			    		user.clear();
			    	}
			    	baseObj.openIndex();
			    },
		        error: function (jqXHR, textStatus, errorThrown) {
		        	user.clear();
		        	baseObj.openIndex();
		        }
			});
		}else{
			user.clear();
			baseObj.openIndex();
		}
	},
	loadHead:function(){
		if(move.user&&move.user.loginTime){
			$("#head-img").attr("src","images/head/"+(move.user.loginTime%11)+".png");
		}
		$("#head-img").fadeIn();
	},
	rechargeDetail:function(isReset){
		if(isReset){
			move.pageIndex=0;
		}
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show(true);
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.rechargeDetail,
			dataType:'json',
			data:{
				mw:mw
			},  
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var length=items.length;
					if(length>0){
						var html='';
						for (var i = 0; i < length; i++) {
							var obj=items[i];
							var title=obj.title;
							var money=obj.money;
							var date=obj.createDate;
							var status=obj.status;
							var statusTitle="";
							var statusCss="";
							if(status=='0'){
								statusTitle="充值中";
								statusCss="colour-yellow";
							}else if(status=='1'){
								statusTitle="充值成功";
								statusCss="colour-green";
							}else if(status=='2'){
								statusTitle="充值失效";
								statusCss="colour-red";
							}
							html=html+'<li class="recharge-cell">';
							html=html+'<div class="title">'+date+'</div><div class="introduce"><span class="paytype">'+title+'</span><span class="paysatatus '+statusCss+'"> '+statusTitle+'</span></div>';
							html=html+'<div class="price-box">'+money+'元</div></li>';
						}
						if(isReset){
							$('.not-data').hide();
							$('#content').show();
	    					$("#dataList").html(html);
	    					mui('#content').pullRefresh().refresh(true);
	    				}else{
	    					$("#dataList").append(html);
	    				}
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	cashList:function(isReset){
		if(isReset){
			move.pageIndex=0;
		}
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
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
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var length=items.length;
					if(length>0){
						var html='';
						for (var i = 0; i < length; i++) {
							var obj=items[i];
							var title=obj.title;
							var money=obj.cashMoney;
							var date=obj.createTime;
							var status=obj.auditStatus;
							var statusTitle="";
							var statusCss="";
							if(status=='0'){
								statusTitle="审核中";
								statusCss="colour-yellow";
							}else if(status=='1'){
								statusTitle="通过";
								statusCss="colour-green";
							}else if(status=='2'){
								statusTitle="拒绝";
								statusCss="colour-red";
							}
							html=html+'<li class="recharge-cell">';
							html=html+'<div class="title">'+date+'</div><div class="introduce"><span class="paysatatus '+statusCss+'">'+statusTitle+'</span></div>';
							html=html+'<div class="price-box">'+money+'元</div></li>';
						}
						if(isReset){
							$('.not-data').hide();
							$('#content').show();
	    					$("#dataList").html(html);
	    					mui('#content').pullRefresh().refresh(true);
	    				}else{
	    					$("#dataList").append(html);
	    				}
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	tradeList:function(isReset){
		if(isReset){
			move.pageIndex=0;
		}
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.tradeList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var length=items.length;
					if(length>0){
						var html='';
						for (var i = 0; i < length; i++) {
							var obj=items[i];
							html=html+'<li class="recharge-cell">';
							html=html+'<div class="title">'+obj.tradeTime+'</div><div class="introduce"><span class="paysatatus">'+obj.tradeContent+'</span></div>';
							var tradeMoney=obj.tradeMoney;
							if(parseFloat(tradeMoney)>=0)
								html=html+'<div class="price-box red">'+tradeMoney+'元</div></li>';
							else
								html=html+'<div class="price-box green">'+tradeMoney+'元</div></li>';
						}
						if(isReset){
							$('.not-data').hide();
							$('#content').show();
	    					$("#dataList").html(html);
	    					mui('#content').pullRefresh().refresh(true);
	    				}else{
	    					$("#dataList").append(html);
	    				}
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	bindType:function(){// 投注页面绑定官方信用事件
		$(".tab-switch-cell").bind("click", function(){
			$(".tab-switch-cell").removeClass("mui-active");
			$(this).addClass("mui-active");
			meObj.touzhuList(true);
		});
	},
	touzhuList:function(isReset){
		if(isReset){
			move.pageIndex=0;
		}
		var data=$(".mui-active").attr('data');
		var url;
		if(data=='1'){
			url=move.betList;
		}else if(data=='2'){
			url=move.xyBetList;
		}
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:url,
			dataType:'json',
			data:{
				mw:mw
			},  
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var length=items.length;
					if(length>0){
						var html='';
						for (var i = 0; i < length; i++) {
							var obj=items[i];
							var betTime=obj.betTime;// 投注时间
							var gameName=obj.gameName;// 彩种名称
							var betMoney=obj.betMoney;// 投注金额
							var sessionNo=obj.sessionNo;// 期号
							var winCash=obj.winCash;// 奖金
							var playName=obj.playName;// 玩法名称
							var multiple=obj.multiple;// 购买倍数
							var winResult=obj.winResult;// 中奖状态值
							var winResult2=obj.winResult2;// 中奖状态中文
							
							var betName=obj.betName;//小玩法
							var betPoint=obj.betPoint;//投注数
							var betRate=obj.betRate;//倍数
							var betTime=obj.betTime;//投注时间
							var gameName=obj.gameName;// 彩种名称
							var betMoney=obj.betMoney;// 投注金额
							var playName=obj.playName;//玩法
							var room=obj.room;//房间号
							var sessionNo=obj.sessionNo;//期号
							var winCash=obj.winCash;//中奖金额
							var winResult=obj.winResult;//中奖状态0=未开奖1=中奖2=未中奖3=打和4=撤单
							
							var title=gameName+" "+sessionNo+"期";
							var subtitle="";
							if(url==move.betList){// 官方
								subtitle=betTime+" "+multiple+"倍 "+betMoney;
							}else if(url==move.xyBetList){
								subtitle=betTime+" "+room+" "+playName+" @"+betRate;
							}
							
							var statusTitle="";
							var statusCss="";
							if(url==move.betList){// 官方
								statusTitle=winResult2;
								if(winResult=='0'){
									statusCss="colour-yellow";
								}else if(winResult=='1'){
									statusCss="colour-coffee";
								}else if(winResult=='2'){
									statusCss="colour-yellow";
								}else if(winResult=='3'){
									statusCss="colour-coffee";
								}else if(winResult=='4'){
									statusCss="colour-red";
								}else if(winResult=='5'){
									statusCss="colour-green";
								}else if(winResult=='6'){
									statusCss="";
								}
								html += '<li class="recharge-cell">';
								html += '<div class="title">'+title+'</div><div class="subtitle">'+subtitle+'</div><div class="introduce"><span class="xtitle">'+playName+'</span> <span class="'+statusCss+'">('+statusTitle+')</span></div>';
								html += '<div class="price-box">投注：'+betMoney+'</div>';
								
								html += '<div class="win-cash-box">中奖：';
								if(winResult=='4'){
									html += '<span class="colour-red">+'+move.widget.fmtNumOut(winCash)+'</span></div></li>';
								}else if(winResult=='5'){
									html += '<span class="colour-green">-'+move.widget.fmtNumOut(betMoney)+'</span></div></li>';
								}else{
									html += move.widget.fmtNumOut(winCash)+'</div></li>';
								}
							}else if(url==move.xyBetList){// 信用
								if(winResult=='0'){
									statusTitle="(未开奖)";
									statusCss="colour-yellow";
								}else if(winResult=='1'){
									statusTitle="(已中奖)";
									statusCss="colour-red";
								}else if(winResult=='2'){
									statusTitle="(未中奖)";
									statusCss="colour-green";
								}else if(winResult=='3'){
									statusTitle="(和)";
									statusCss="colour-coffee";
								}else if(winResult=='4'){
									statusTitle="(撤单)";
									statusCss="colour-violet";
								}
								html=html+'<li class="recharge-cell">';
								html=html+'<div class="title">'+title+'</div><div class="subtitle">'+subtitle+'</div><div class="introduce"><span class="xtitle">'+betName+'</span> <span class="'+statusCss+'">'+statusTitle+'</span></div>';
								html=html+'<div class="price-box">投注：'+betMoney+'</div>';

								html += '<div class="win-cash-box">中奖：';
								if(winResult=='1'){
									html += '<span class="colour-red">+'+move.widget.fmtNumOut(winCash)+'</span></div></li>';
								}else if(winResult=='2'){
									html += '<span class="colour-green">-'+move.widget.fmtNumOut(betMoney)+'</span></div></li>';
								}else{
									html += move.widget.fmtNumOut(winCash)+'</div></li>';
								}
							}
						}
						if(isReset){
							$('.not-data').hide();
							$('#content').show();
	    					$("#dataList").html(html);
	    					mui('#content').pullRefresh().refresh(true);
	    				}else{
	    					$("#dataList").append(html);
	    				}
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	bindTypeZJ:function(){// 中奖记录页面绑定官方信用事件
		$(".tab-switch-cell").bind("click", function(){
			$(".tab-switch-cell").removeClass("mui-active");
			$(this).addClass("mui-active");
			meObj.zhongjiangList(true);
		});
	},
	zhongjiangList:function(isReset){
		if(isReset){
			move.pageIndex=0;
		}
		var data=$(".mui-active").attr('data');
		var url;
		var status;
		if(data=='1'){
			url=move.betList;
			status='3';
		}else if(data=='2'){
			url=move.xyBetList;
			status='1';
		}
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		map['status'] = status;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:url,
			dataType:'json',
			data:{
				mw:mw
			},  
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var length=items.length;
					if(length>0){
						var html='';
						for (var i = 0; i < length; i++) {
							var obj=items[i];
							var title=obj.gameName;
							var sessionNo=obj.sessionNo;//期号
							var betTime=obj.betTime;//投注时间
							var room=obj.room;//房间号
							var playName=obj.playName;//玩法
							var betRate=obj.betRate;//倍数
							var betMoney=obj.betMoney;//倍数
							var betName=obj.betName;//小玩法
							var betPoint=obj.betPoint;//投注数
							var status=obj.winStatus;//中奖状态
							var winCash=obj.winCash;//中奖金额
							var payoff=obj.payoff;//收益
							var orderNum=obj.orderNum;//方案号
							var spName=obj.spName;//发起人
							var money=obj.money;//方案金额
							var num=obj.num;//倍数
							var betTime=obj.betTime;//投注时间
							var room=obj.room;//房间号
							var playName=obj.playName;//玩法
							var betRate=obj.betRate;//倍数
							var betName=obj.betName;//小玩法
							var betPoint=obj.betPoint;//投注数
							var status=obj.winStatus;//中奖状态
							var winCash=obj.winCash;//中奖金额
							if(typeof payoff =='undefined'){
								payoff=(parseFloat(winCash)-parseFloat(betPoint)).toFixed(2);
							}
							title=title+" "+sessionNo+"期";
							var subtitle;
							if(url==move.betList){// 官方
								subtitle=betTime+" "+spName+" "+money+" @"+num;
							}else if(url==move.xyBetList){
								subtitle=betTime+" "+room+" "+playName+" @"+betRate;
							}
							
							html=html+'<li class="recharge-cell">';
							html=html+'<div class="title">'+title+'</div><div class="subtitle">'+subtitle+'</div>';
							html=html+'<div class="introduce">';
							if(url==move.betList){
								html=html+'<span class="xtitle">'+orderNum+'</span>';
							}
							html=html+'下注<span class="colour-red">'+betPoint+'元</span>奖金<span class="colour-red">'+winCash+'元</span>';
							html=html+'</div><div class="price-box colour-red">'+winCash+'元</div></li>';
						}
						if(isReset){
							$('.not-data').hide();
							$('#content').show();
	    					$("#dataList").html(html);
	    					mui('#content').pullRefresh().refresh(true);
	    				}else{
	    					$("#dataList").append(html);
	    				}
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	changePwd:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var oldpwd=$.trim($('#oldpwd').val());
		var newpwd=$.trim($('#newpwd').val());
		var newpwd2=$.trim($('#newpwd2').val());
		if(oldpwd==''){
			mui.toast('请输入旧密码',{ duration:'long', type:'div' });
			return;
		}
		if(newpwd==''){
			mui.toast('请输入新密码',{ duration:'long', type:'div' });
			return;
		}
		if(newpwd2==''){
			mui.toast('请确认新密码',{ duration:'long', type:'div' });
			return;
		}
		if(newpwd!=newpwd2){
			mui.toast('两次新密码不一致',{ duration:'long', type:'div' });
			return;
		}
		var btnArray = ['确定', '取消'];
		mui.confirm('确认修改密码？', document.title, btnArray, function(e) {
			if (e.index == 0) {
				var mask=move.createLoading();
				mask.show();
				var map = {};
				map['u'] =move.user.u;
				map['oldpassword'] =hex_md5(oldpwd).toUpperCase();
				map['newpassword'] =hex_md5(newpwd).toUpperCase();
				map['repassword'] =hex_md5(newpwd2).toUpperCase();
				var mw=baseObj.mw(map);
				$.ajax({
				    type: "POST",
				    url:move.changePwd,
				    dataType:'json',
				    data:{
				    	mw:mw
				    },  
				    success: function(ret){
				    	mask.close();
				    	var code=ret.error_no;
				    	var msg=ret.msg;
				    	if(code=='200'){
				    		$('#oldpwd').val('');
				    		$('#newpwd').val('');
				    		$('#newpwd2').val('');
				    		mui.toast('修改成功',{ duration:'long', type:'div' });
				    	}else{
				    		mui.toast(msg,{ duration:'long', type:'div' });
				    	}
				    },
			        error: function (jqXHR, textStatus, errorThrown) {
			        	mask.close();
			        }
				});
			}
		});
	},
	cashPassword:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var oldpwd=$.trim($('#oldpwd').val());
		var newpwd=$.trim($('#newpwd').val());
		var newpwd2=$.trim($('#newpwd2').val());
		if(oldpwd==''){
			mui.toast('请输入登录密码',{ duration:'long', type:'div' });
			return;
		}
		if(newpwd==''){
			mui.toast('请输入提现密码',{ duration:'long', type:'div' });
			return;
		}
		if(newpwd2==''){
			mui.toast('请确认提现密码',{ duration:'long', type:'div' });
			return;
		}
		if(newpwd!=newpwd2){
			mui.toast('两次提现密码不一致',{ duration:'long', type:'div' });
			return;
		}
		var btnArray = ['确定', '取消'];
		mui.confirm('确认设置提现密码？', document.title, btnArray, function(e) {
			if (e.index == 0) {
				var mask=move.createLoading();
				mask.show();
				var map = {};
				map['u'] =move.user.u;
				map['password'] =hex_md5(oldpwd).toUpperCase();
				map['cashPassword'] =hex_md5(newpwd).toUpperCase();
				map['recashPassword'] =hex_md5(newpwd2).toUpperCase();
				var mw=baseObj.mw(map);
				$.ajax({
				    type: "POST",
				    url:move.cashPassword,
				    dataType:'json',
				    data:{
				    	mw:mw
				    },  
				    success: function(ret){
				    	mask.close();
				    	var code=ret.error_no;
				    	var msg=ret.msg;
				    	if(code=='200'){
				    		$('#oldpwd').val('');
				    		$('#newpwd').val('');
				    		$('#newpwd2').val('');
				    		mui.toast('设置成功',{ duration:'long', type:'div' });
				    	}else{
				    		mui.toast(msg,{ duration:'long', type:'div' });
				    	}
				    },
			        error: function (jqXHR, textStatus, errorThrown) {
			        	mask.close();
			        }
				});
			}
		});
	},
	banklist:function(){
		$.ajax({
		    type: "POST",
		    url:move.banklist,
		    dataType:'json',
		    data:{},
		    success: function(ret){
		    	var code=ret.code;
		    	var msg=ret.msg;
		    	if(code=='200'){
		    		var data=ret.data;
		    		var items=data.items;
		    		store.set('banklist',items);
		    		var html='';
		    		for (var i=0;i<items.length;i++) {
		    			var obj=items[i];
		    			var bankName=obj.bankName;
		    			var no=obj.bankAccount;
		    			html=html+'<li class="mui-table-view-cell">';
		    			html=html+'<a class="mui-navigate-right" href="javascript:baseObj.openView(\'chongzhi.html?id='+no+'\');">';
		    			html=html+bankName+'</a></li>';
					}
		    		$('#banklist ul').html(html);
		    	}else{
		    		mui.alert(msg,document.title, function() {
		    		});
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        }
		});
	},
	bankList:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['u'] =move.user.u;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.bankList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var items=ret.data.items;
					var obj=items[0];
					var cashType=obj.cashType;
					$("#cashType").val(cashType);
					var subitems=obj.subitems;
					(function($, doc) {
						var timePicker = new mui.PopPicker({layer: 1}); 
						if(subitems){
							timePicker.setData(subitems);
							var serveTime = doc.getElementById('bank');
							serveTime.addEventListener('tap', function(event) {
								timePicker.show(function(subitems) {
									jQuery('#bank-data-val').val(subitems);
									jQuery('#bank-data-text').text(subitems);
								});
							}, false);
						}
					})(mui, document);
					$("#confirm").bind("click", function(){
						meObj.cashSubmit();
					});
					// var money=store.get('money');
					// if(typeof money!='undefined'&&money!=''){
					// 	$('#userMoney').val('可提款金额：￥'+money);
					// }
					
					var obj2=ret.data.obj;
		    		var accountNo=obj2.accountNo;
		    		var bankName=obj2.bankName;
		    		var cityName=obj2.cityName;
		    		var userName=obj2.userName;
		    		var isSMS=obj2.isSMS;
		    		if(bankName&&bankName!=''){
		    			$('#bank-data-text').text(bankName);
		    			$('#bank-data-val').val(bankName);
		    		}
		    		if(cityName&&cityName!=''){
		    			$('#city-data-text').text(cityName);
		    			$('#city-data-val').val(cityName);
		    		}
		    		if(accountNo&&accountNo!=''){
		    			$('#accountNo').val(accountNo);
		    		}
		    		if(userName&&userName!=''){
		    			$('#userName').val(userName);
		    		}
		    		if(isSMS=='1'){
		    			$('#isSMS').val('1');
		    			$('.sendcodebox').show();
		    			
		    			$("#sendCode").bind("click", function(){
		    				meObj.sendSMS();
		    			});
		    		}
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
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
					(function($, doc) {
						var timePicker = new mui.PopPicker({layer: 3}); 
						var timeData=items;
						if(timeData){
							timePicker.setData(timeData);
							var serveTime = doc.getElementById('city');
							serveTime.addEventListener('tap', function(event) {
								timePicker.show(function(items) {
									jQuery('#city-data-val').val(items[2].value);
									jQuery('#city-data-text').text(items[0].text+items[1].text+items[2].text);
								});
							}, false);
						}
					})(mui, document);
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	cashSubmit:function(){
		var bankName=$("#bank-data-text").text();
		var cityName=$("#city-data-text").text();

		var cashMoney=$("#cashMoney").val();
		var accountNo=$("#accountNo").val();
		var userName=$("#userName").val();
		var cashPassword=$("#cashPassword").val();
		var cid=$("#city-data-val").val();
		var type=$("#cashType").val();
		if(!bankName || bankName=='请选择'){
			mui.alert('请选择银行',document.title, function() {});
			return;
		}
		if(!cid){
			mui.alert('请选择城市',document.title, function() {});
			return;
		}
		if(!accountNo){
			mui.alert('请填写银行卡号',document.title, function() {});
			return;
		} 
		if(!userName){
			mui.alert('请填写账户姓名',document.title, function() {});
			return;
		}
		if(!cashPassword){
			mui.alert('请填写提款密码',document.title, function() {});
			return;
		}
		if(!cashMoney){
			mui.alert('请填写提款金额',document.title, function() {});
			return;
		}else{
			if(parseFloat(cashMoney)>parseFloat(store.get('money'))){
				mui.alert('您的余额不足',document.title, function() {});
				return;
			}else if(parseFloat(cashMoney)<100){
				mui.alert('单笔最少100',document.title, function() {});
				return;
			}
			
		}
		var map = {};
		map['u']=move.user.u;
		map['bankName']=bankName;
		map['cityName']=cityName;
		map['cashMoney']=cashMoney;
		map['accountNo']=accountNo;
		map['userName']=userName;
		map['cashPassword']=hex_md5(cashPassword).toUpperCase();
		map['cid']=cid;
		map['type']=type;
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
		    	var msg=ret.msg;
		    	if(code=='200'){
		    		mui.alert('提现成功！',document.title, function() {
		    			baseObj.openView('wallet.html');
		    		});
		    	}else{
		    		mui.alert(msg,document.title, function() {
		    			//baseObj.openView('wallet.html');
		    		});
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        	mask.close();
	        }
		});
	},
	recharge:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var paytype=$('#paytype').val();
		var payval=$('#payval').val();
		if(paytype==''){
			mui.alert('请选择支付类型',document.title, function() {});
			return;
		}
		if(payval==''){
			mui.alert('请选择充值金额',document.title, function() {});
			return;
		}
		var map = {};
		map['u']=move.user.u;
		map['rid']=payval;
		map['payType']=paytype;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
		    type: "POST",
		    url:move.recharge,
		    dataType:'json',
		    data:{
		    	mw:mw
		    },
		    success: function(ret){
		    	var code=ret.code;
		    	var msg=ret.msg;
		    	if(code=='200'){
		    		var data=ret.data;
		    		var obj=data.bxsPayObj;
		    		var url=obj.payUrl;
		    		window.location.href=url; 
		    		mask.close();
		    	}else{
		    		mask.close();
		    		mui.alert(msg,document.title, function() {});
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        	mask.close();
	        }
		});
	},
	chongzhiInit:function(){
		var id=move.getParameter('id');
		var items=store.get('banklist');
		for (var i=0;i<items.length;i++) {
			var obj=items[i];
			var bankName=obj.bankName;
			var no=obj.bankAccount;
			var userName=obj.userName;
			var objId=obj.id;
			var country=obj.des;
			if(objId==id){
				$('#bank').text(bankName);
				$('#bankNo').text(no);
				$('#name').text(userName);
				$('#country').text(country);
			}
		}
		$("#btncz").bind("click", function(){
			meObj.offlineSubmit();
		});
	},
	offlineSubmit:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var bank=$('#bank').text();
		var bankNo=$('#bankNo').text();
		var name=$('#name').text();
		// var country=$('#country').text();
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
		// if(country==''){
		// 	mui.alert('收款人国籍有错，请刷新重试',document.title, function() {});
		// 	return;
		// }
//		var bank2=$('#bank2').val();
//		var bankNo2=$('#bankNo2').val();
		var name2=$('#name2').val();
		var money=$('#money').val();
		var depositType=$('#depositType').val();
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
				map['receiveCountry']="中国";
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
				    		mui.alert('提交成功',document.title, function(){});
				    		baseObj.openView('rechargeList.html');
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
//					$("#loginName").val(loginName);
//					$("#password").val(password);
//					$(".accounts-info").show();
//					$(".mui-backdrop").show();
					user.init(uid,u,'',logo,cellPhone,loginName,username,money,point,userType);
					user.set(move.user);
					var redirect=move.getParameter('redirect');
					if(redirect){
						baseObj.openView(redirect);
					}else{
						baseObj.openView('index.html');
					}
//					$("#guestRegBut").bind("click", function(){
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
	rechargelist:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['u'] =move.user.u;
		var mw=baseObj.mw(map);
		$.ajax({
		    type: "POST",
		    url:move.rechargelist,
		    dataType:'json',
		    data:{
		    	mw:mw
		    },
		    success: function(ret){
		    	mask.close();
		    	var code=ret.code;
		    	if(code=='200'){
		    		var items=ret.data.items;
		    		var typehtml='';
		    		var moneyhtml='';
		    		var bankhtml='';
		    		for ( var i = 0; i < items.length; i++) {
		    			var obj=items[i];
		    			var moneyArr=obj.moneyArr;
		    			var bankArr=obj.bankArr;
		    			var title=obj.title;
		    			var type=obj.type;
		    			var img=obj.img;
		    			typehtml=typehtml+'<li class="paytype-cell" data="'+type+'"><span class="ico payico"><img src="'+img+'">';
		    			typehtml=typehtml+'</span><span class="lable">'+title+'</span><span class="checkbox">';
		    			var chkimg='images/checkbox.png';
		    			if(i==0){
		    				chkimg='images/checkboxed.png';
		    				$('#paytype').val(type);
		    			}
		    			typehtml=typehtml+'<img src="'+chkimg+'"></span></li>';
		    			
		    			var moneyclass='hide';
		    			if(i==0){
		    				moneyclass='show';
		    			}
		    			moneyhtml=moneyhtml+'<ul class="payval-box '+moneyclass+'" id="moneybox'+type+'">';
		    			for ( var j = 0; j < moneyArr.length; j++) {
		    				var moneyObj=moneyArr[j];
		    				var rid=moneyObj.rid;
		    				var price=moneyObj.price;
		    				var clazz='';
		    				if(i==0&&j==0){
		    					$('#payval').val(rid);
		    				}
		    				if(j==0){
		    					clazz='payval-active';
		    				}
		    				moneyhtml=moneyhtml+'<li class="option "><span class="payval '+clazz+'" data="'+rid+'">'+price+'</span></li>';
						}
		    			moneyhtml=moneyhtml+'</ul>';
		    			
		    			if(bankArr.length>0){
		    				bankhtml=bankhtml+'<ul class="banklist" id="bankbox">';
		    				for ( var k = 0; k < bankArr.length; k++) {
		    					var bankObj=bankArr[k];
		    					var bankId=bankObj.bankId;
		    					var title=bankObj.title;
		    					bankhtml=bankhtml+'<li><span class="bank" data="'+bankId+'">'+title+'</span> </li>';
		    					
							}
		    				bankhtml=bankhtml+'</ul>';
		    			}
					}
		    		$('#rechargetype').append(typehtml);
		    		$('#moneybox').append(moneyhtml);
		    		$('#bankbox').append(bankhtml);
		    		
		    		meObj.bindRechargeCell();
		    		$("#btncz").bind("click", function(){
		    			meObj.recharge();
		    		});
		    	}else{
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        	mask.close();
	        }
		});
	},
	userRechargeWay:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
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
		    	mask.close();
		    	var code=ret.code;
		    	if(code=='200'){
		    		var html='';
		    		var items=ret.data.items;
		    		var wayArrS = [];
		    		for(var i=0;i<items.length;i++){
		    			var obj=items[i];
		    			var title=obj.title;// 渠道名称
		    			var channelType=obj.channelType;// 渠道类型1.在线充值2.快速充值3.固定码充值4.线下充值一5.线下充值二
		    			var wayArr=obj.wayArr;// 渠道下的充值方式
		    			html=html+'<li><div id="offlineRecharge"><div class="greyline"></div><div class="chongzhi-box">';
		    			if(channelType==1){
		    				$("#linere").prepend('<div class="title">'+title+'</div>');
		    			}else{
		    				html=html+'<div class="title">'+title+'</div>';
		    			}
		    			html=html+'<div class="banklist" id="banklist"><ul class="mui-table-view">';
		    			var html2='';
		    			if(channelType==4||channelType==5){
							wayArrS = wayArrS.concat(wayArr);
		    			}
		    			for(var j=0;j<wayArr.length;j++){
		    				var obj2=wayArr[j];
		    				var id=obj2.id;
		    				var title2=obj2.title;// 渠道名称
		    				var bankAccount=obj2.bankAccount;// 银行卡号
		    				var url=obj2.url;// 银行卡号
		    				var img=obj2.img;// 固定码充值二维码
		    				if(channelType==1){// 2.在线充值
		    					$("#linere").show();
		    				}else if(channelType==2){// 2.快速充值
		    					html2=html2+'<li class="mui-table-view-cell">';
		    					html2=html2+'<a class="mui-navigate-right" href="javascript:baseObj.openIframeView(\'rechargeFast.html\',\''+url+'\');">';
		    					html2=html2+title2+'</a></li>';
		    				}else if(channelType==3){// 2.固定码充值
		    					store.set('imglist',wayArr);
		    					html2=html2+'<li class="mui-table-view-cell">';
		    					html2=html2+'<a class="mui-navigate-right" href="javascript:baseObj.openView(\'kscz.html?id='+id+'\');">';
		    					html2=html2+title2+'</a></li>';
		    				}else if(channelType==4){// 4.线下充值一
		    					html2=html2+'<li class="mui-table-view-cell view-cell"><img class="cell-img" src="'+img+'">';
		    					html2=html2+'<a class="mui-navigate-right cell-des" href="javascript:baseObj.openView(\'chongzhi.html?id='+id+'\');">';
		    					html2=html2+title2+'</a></li>';
		    				}else if(channelType==5){// 5.线下充值二
		    					html2=html2+'<li class="mui-table-view-cell">';
		    					html2=html2+'<a class="mui-navigate-right" href="javascript:baseObj.openView(\'chongzhi.html?id='+id+'\');">';
		    					html2=html2+title2+'</a></li>';
		    				}

		    			}

		    			store.set('banklist',wayArrS);
		    			html=html+html2;
		    			html=html+'</ul></div></div></div></li>';
					}
		    		$("#wayList").html(html);
		    	}else{
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        	mask.close();
	        }
		});
	},
	bindRechargeCell:function(){
		$(".paytype-cell").bind("click", function(){
			$(".paytype-cell").children('.checkbox').children('img').attr('src','images/checkbox.png')
			$(this).children('.checkbox').children('img').attr('src','images/checkboxed.png');
			var paytype=$(this).attr('data');
			$('#paytype').val(paytype);
			$('.payval-box').hide();
			$('#moneybox'+paytype).show();
			
			var payval=$('#payval').val();
			if(payval!=''){
				var newpay=$('#moneybox'+paytype).find(".payval[data='"+payval+"']").html();
				if(newpay){
					$('#moneybox'+paytype).find('.payval').removeClass('payval-active');
					$('#moneybox'+paytype).find(".payval[data='"+payval+"']").addClass('payval-active');
				}else{
					var payval=$('#moneybox'+paytype+' .payval-active').attr('data');
					$('#payval').val(payval);
				}
			}else{
				var payval=$('#moneybox'+paytype+' .payval-active').attr('data');
				$('#payval').val(payval);
			}
			
			$('#bankbox').hide();
			if(paytype=='UNIONPAY'||paytype=='bank'){
				$('#bankbox').show();
			}
		});
		$(".payval").bind("click", function(){
			$(this).parent('.option').parent('.payval-box').find('.payval').removeClass('payval-active');
			
//			$(".payval").removeClass('payval-active');
			$(this).addClass('payval-active');
			
			var val=$(this).attr('data');
			$('#payval').val(val);
		});
		$(".banklist .bank").bind("click", function(){
			$(".banklist .bank").removeClass('active');
			$(this).addClass('active');
			var val=$(this).attr('data');
			$('#bankid').val(val);
		});
	},
	rechargeSwitch:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		$.ajax({
		    type: "POST",
		    url:move.rechargeSwitch,
		    dataType:'json',
		    data:{
		    },
		    success: function(ret){
		    	var code=ret.code;
		    	if(code=='200'){
		    		var data=ret.data;
		    		var fastRecharge=data.fastRecharge;//快速充值开关
		    		var fastRechargeTitle=data.fastRechargeTitle;//title
		    		var fastRechargelink=data.fastRechargelink;//title
		    		if(fastRecharge=='1'){
		    			$('#kscz').append('<li class="mui-table-view-cell"><a class="mui-navigate-right" href="javascript:baseObj.openIframeView(\'rechargeFast.html\',\''+fastRechargelink+'\');">'+fastRechargeTitle+'</a></li>');
		    		}
		    		var offlineRecharge=data.offlineRecharge;
		    		var onlineRecharge=data.onlineRecharge;
		    		var codeRecharge=data.codeRecharge;
		    		var codeRechargeTitle=data.codeRechargeTitle;
		    		var codeRechargeUrl=data.codeRechargeUrl;
		    		if(onlineRecharge=='1'){
		    			$('#linere').show();
		    		}
		    		if(offlineRecharge=='1'){
		    			$('#offlineRecharge').show();
		    		}
		    		if(codeRecharge=='1'){
		    			$('#kscz').append('<li class="mui-table-view-cell"><a class="mui-navigate-right" href="javascript:baseObj.openView(\'kscz.html\');">'+codeRechargeTitle+'</a></li>');
		    			store.set('codeRechargeUrl',codeRechargeUrl);
		    		}
		    		
		    	}else{
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        }
		});
	},
	myMember:function(isReset){
		if(isReset){
			move.pageIndex=0;
		}
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.myMember,
			dataType:'json',
			data:{
				mw:mw
			},  
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var length=items.length;
					if(length>0){
						var html='';
						for (var i = 0; i < length; i++) {
							var obj=items[i];
							var logo=obj.logo;
							var userId=obj.userId;
							var createTime=obj.createTime;
							html=html+'<li><div class="logo"><img alt="" src="/game'+logo+'"></div>';
							html=html+'<div class="infobox"><div class="title"><span class="">徒弟ID：</span>'+userId+'</div>';
							html=html+'<div class="time">创建时间：'+createTime+'</div>';
							html=html+'</div></li>';
							
						}
						if(isReset){
							$('.not-data').hide();
							$('#content').show();
	    					$("#dataList").html(html);
	    					mui('#content').pullRefresh().refresh(true);
	    				}else{
	    					$("#dataList").append(html);
	    				}
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	recRevenue:function(isReset){
		if(isReset){
			move.pageIndex=0;
		}
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.user_recRevenue,
			dataType:'json',
			data:{
				mw:mw
			},  
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var recRevenue=result.recRevenue;
					$('#totalmoney').text(recRevenue);
					var items=result.items;
					var length=items.length;
					if(length>0){
						var html='';
						for (var i = 0; i < length; i++) {
							var obj=items[i];
							var date=obj.createTime;
							var remark=obj.remark;
							var money=obj.money;
							var title=obj.tradeContent;
							
							html=html+'<li class="recharge-cell">';
							html=html+'<div class="title">'+title+'</div><div class="introduce"><span class="paytype">'+date+'</span></div>';
							html=html+'<div class="price-box">'+money+'</div></li>';
						}
						if(isReset){
							$('.not-data').hide();
							$('#content').show();
	    					$("#dataList").html(html);
	    					mui('#content').pullRefresh().refresh(true);
	    				}else{
	    					$("#dataList").append(html);
	    				}
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	heroicList:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.heroicList,
			dataType:'json',
			data:{
				mw:mw
			},  
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var length=items.length;
					if(length>0){
						var html='';
						for (var i = 0; i < length; i++) {
							var obj=items[i];
							var logo=obj.logo;// 头像
							var level=obj.level;// 级别
							var userId=obj.userId;// 用户id
							var userPoints=obj.userPoints;// 用户积分
							
							html=html+'<li class="heroic-list-cell border-bottom"><div class="heroic-head fl">';
							html=html+'<img id="head-img" src="'+logo+'" onerror="javascript:this.src=\'images/head.png\';">';
							html=html+'</div><div class="level-img"><img id="level-img" src="images/level/level-'+level+'.png">';
							html=html+'</div><div class="heroic-info"><div class="heroic-id">';
							html=html+'帐号ID：<span class="user-id" id="userId">'+userId+'</span></div>';
							html=html+'<div class="heroic-point">累计充值：<span class="user-point" id="point">￥'+userPoints+'</span>';
							if(move.pageIndex==0){
								if(i==0){
									html=html+'</div><div class="ranking frist">1</div>';
								}else if(i==1){
									html=html+'</div><div class="ranking second">2</div>';
								}else if(i==2){
									html=html+'</div><div class="ranking third">3</div>';
								}else{
									html=html+'</div><div class="ranking">'+(i+1)+'</div>';
								}
							}else{
								html=html+'</div><div class="ranking">'+(move.pageIndex*20+i+1)+'</div>';
							}
							html=html+'</div></li>';
						}
						$("#dataList").append(html);
//						if(isReset){
//							$('.not-data').hide();
//							$('#content').show();
//	    					$("#dataList").html(html);
//	    					mui('#content').pullRefresh().refresh(true);
//	    				}else{
//	    					$("#dataList").append(html);
//	    				}
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	walletNotice:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}

		var map = {};
		map['type']=4;
		map['u']=move.user.u;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.importantNotice,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				if(code=='200'){
					if(data){
						$('#yue').text('￥'+data.money);

						//当从外部进入时才弹出
						var lastPage = move.loader.lastPage();
						if(lastPage.indexOf("/xy/")>1 
							|| lastPage.indexOf("/gf/")>1
							|| lastPage.indexOf("/game/index")>1
							|| lastPage.indexOf("/me")>1){
							if(data.title) mui.alert(data.content,data.title);
						}
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	promotionList:function(){// 优惠活动列表
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.promotionList,
			dataType:'json',
			data:{
				mw:mw
			},  
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var length=items.length;
					if(length>0){
						var html='';
						var detailsHtml='';
						for (var i = 0; i < length; i++) {
							var obj=items[i];
							var id=obj.id;// 数据id
							var content=obj.content;// 内容
							var img=obj.img;// 图片
							var startDate=obj.startDate;// 时间
							var title=obj.title;// 标题
							html=html+'<li class="content-line" id="'+id+'"><div class="title">'+title+'</div>';
							html=html+'<div class="content-img"><img src="'+img+'"/></div>';
							html=html+'<div class="content-date"><span class="data">起始时间：'+startDate+'</span>';
							html=html+'<span class="details">详情</span><span class="icon mui-icon mui-icon-arrowright"></span>';
							html=html+'<span class="icon2 mui-icon mui-icon-arrowright"></span></div></li>';
							detailsHtml=detailsHtml+'<li id="'+id+'">'+content+'</li>';
						}
						if(move.pageIndex==0){
							$('.not-data').hide();
							$('#content').show();
	    					$("#dataList").html(html);
	    					$("#detailsList").html(detailsHtml);
	    					mui('#content').pullRefresh().refresh(true);
	    				}else{
	    					$("#dataList").append(html);
	    					$("#detailsList").append(detailsHtml);
	    				}
						meObj.bindDetails();
						meObj.bindBackPage();
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
					}else{
						mui('#content').pullRefresh().endPullupToRefresh(true);
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	// 点击进详情事件
	bindDetails:function(){
		$(".content-line").bind("tap", function(){
			var id=$(this).attr("id");
			$("#content").hide();
			$("#detailsList li[id="+id+"]").show();
			meObj.unbindBackPage();
			meObj.bindBackList(id);
		});
	},
	// 详情返回列表的事件
	bindBackList:function(id){
		$(".mui-icon-left-nav").bind("tap", function(){
			$("#content").show();
			$("#detailsList li[id="+id+"]").hide();
			meObj.bindBackPage();
		});
	},
	// 列表返回上一页
	bindBackPage:function(){
		$(".mui-icon-left-nav").bind("tap", function(){
			history.back();
		});
	},
	unbindBackPage:function(){
		$(".mui-icon-left-nav").unbind("tap");
	},
	codeRechargeSubmit:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var payval=$('#payval').val();
		if(payval==''){
			mui.alert('请输入充值金额',document.title, function() {});
			return;
		}
		var map = {};
		map['ID']=move.user.uid;
		map['money']=payval;
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
				mask.close();
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					mui.alert(msg,document.title, function() {
						baseObj.openView('rechargeList.html');
					});
				}else{
					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	openViewWithCheckGuest:function(url){

		if(move.user&&move.user.u){
			if(move.user&&move.user.userType&&move.user.userType=="1"){
				baseObj.openView(url);
			}else{
				mui.alert("体验客户无权限！");
			}
		}else{
			//mui.alert("请先登录!");
			location = "/game/login.html";
		}
	},
	loadMessage:function(isReset){
		if(isReset){
			move.pageIndex=0;
		}
		if(move.user.u==null){
			return;
		}
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show(true);
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
				var msg=ret.msg;
				var items = ret.data.items;
				if(code=='200'){
					if(items.length>0){
						if($(".message")&&$(".message").length>0){
							var count = 0
							for ( var i = 0; i < items.length; i++) {	
								var obj = items[i];
								if(obj.status=="1"){
									count ++;
								}
							}
							if(count>0){
								var shtml = "<span class=\"mui-badge mui-badge-success\">"+count+"</span>";
								$(".message").addClass("active");
								$(".message .setting-lable").html(shtml+"消息");
							}
						}
						if($("#content")&&$("#content").length>0){
				    		var shtml = "";
				    		for ( var i = 0; i < items.length; i++) {
				    			var obj=items[i];
				    			var content=obj.content;
				    			var createTime=obj.createTime;
				    			var title=obj.title;
				    			var status=obj.status;
				    			var statusStr = "";
				    			var clazz = "";
				    			var id = obj.id;
				    			if(status==1){
				    				statusStr = "未读";
				    				clazz = "colour-green";
				    			}else if(status==2){
				    				statusStr = "已读";
				    			}
				    			shtml += "<li class=\"recharge-cell\" data=\""+id+"\">";
				    			shtml += "<div class=\"title\">"+createTime+"</div>";
				    			shtml += "<div class=\"introduce\">";
				    			shtml += "<span class=\"paytype\">"+title+"</span>";
				    			shtml += "<span class=\"paysatatus "+clazz+"\">"+statusStr+"</span>";
				    			shtml += "</div>";
				    			shtml += "<div class=\"price-box\">"+content+"</div>";
				    			shtml += "</li>";
							}
				    		// $("#dataList").html(shtml);
				    		if(isReset){
								$('.not-data').hide();
								$('#content').show();
		    					$("#dataList").html(shtml);
		    					mui('#content').pullRefresh().refresh(true);
		    				}else{
		    					$("#dataList").append(shtml);
		    				}
							move.pageIndex++;
		    				mui('#content').pullRefresh().endPullupToRefresh();
						}
						$(".mui-pull-bottom-pocket").hide();
						$("li.recharge-cell").bind("tap",function(){
							var data = $(this).attr("data");
							if(data){
								meObj.messageDetail(data);
							}
						});
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	messageDetail:function(data){

		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}

		var map = {};
		map['u'] =move.user.u;
		map['id'] =data;
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
				var msg=ret.msg;
				if(code=='200'){
					var obj = ret.data;
					var content=obj.content;
	    			var createTime=obj.createTime;
	    			var title=obj.title;
	    			var id = obj.id;
	    			var btnArray = ['确认'];
					mui.confirm(content,title , btnArray, function(e) {
	                    window.location.reload();
	                })
					
				}else{
					
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	}
}

