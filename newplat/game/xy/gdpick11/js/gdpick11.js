var lotteryObj={
	optionArray:null,
	init:function(){
		lotteryObj.playSelector.init();
		
		mask=move.createLoading();
		window.setTimeout(function(){mask.show(true);},10);
		var map = {};
		map['u'] =move.user.u;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.gdPick11_currentTime,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var obj=result.obj;
					var sessionNo=obj.sessionNo;//当前期号
					var lastSessionNo=obj.lastSessionNo;//上期开奖期号
					var openResult=obj.openResult;//开奖号码
					var openDate=obj.openDate;//开奖时间
					var gains=obj.gains;//当前收益
					var betTime=obj.betTime;//截止投注时间
					var openTime=obj.openTime;//距离当前期开奖时间
					var money=obj.money;//距离当前期开奖时间
					var html='';
					var length=openResult.length;
					if(length==0){
						if(timeropenresult==null){
							timeropenresult=window.setInterval(function(){timerObj.result('timer-gdPick11open');},timerObj.intervalAPI);
						}
						$('#openlable').html("开奖中...");
						$('#openlable').addClass("red");
					}else{
						var length=openResult.length;
						for (var j = 0; j <openResult.length; j++) {
							html=html+'<span class="num bg-red">'+openResult[j]+'</span>';
							if(j==4){
								html=html+'<span class="symbol">=</span>';
							}else if((openResult.length-1)==j){
							}else{
								html=html+'<span class="symbol">+</span>';
							}
						}
						$('#open-number').html(html);
						$('#openlable').html("开奖");
						console.info(lastSessionNo+'期开奖号码::'+openResult);
					}
					$('#balance').html(money);
					$('#gains').html(gains);
					$('#opentime').html(openDate);
					
					$('#sessionNo').html(sessionNo+" 期");
					$('#sessionNo').attr("data",sessionNo);
					$('#openqihao').html(lastSessionNo+"期");
					$('#openqihao').attr("data",lastSessionNo);
					
					timerObj.rest=betTime;
					timerObj.openrest=openTime;
					console.info('timerxiazhu::'+timerxiazhu);
					if(timerxiazhu==null){
						timerxiazhu=window.setInterval(function(){timerObj.init(timerObj.rest,'timer-gdPick11');},timerObj.interval); 
					}
					if(timeropen==null){
						timeropen=window.setInterval(function(){timerObj.open(timerObj.openrest,'timer-gdPick11open');},timerObj.interval);
					}
					lotteryObj.loadPanel("0");
					lotteryObj.submit();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	loadPanel:function(t){
		var mask=move.createLoading();
		mask.show(true);
		var map = {};
		map['playType'] =t;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.gdPick11_betPanel,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.betItems;
					var html='';
					var htmlkj='';
					for (var i = 0; i < items.length; i++) {
						var cell=items[i];
						var title=cell.optionTitle;
						var optionType=cell.optionType;
						var optionItems=cell.optionItems;
						html=html+'<li class="betting-cell"><div class="title">'+title+'</div>';
						htmlkj=htmlkj+'<li class="betting-cell" optionType="'+optionType+'"><div class="title">'+title+'</div>';
						if(t==4){// 直选玩法
							htmlkj=htmlkj+'<div class="second_title">第一球</div><div class="first">';
							html=html+'<div class="second_title">第一球</div><div class="first">';
							for (var j = 0; j < 11; j++) {
								var rate=optionItems[j].rate;
								var bstitle=optionItems[j].title;
								var id=optionItems[j].optionId;
								html=html+'<div class="betting"><span class="beishu"><span class="bstitle">'+bstitle+'</span> <span class="bsnum red">'+rate+'</span></span><span class="inpbox">';
								html=html+'<input type="tel" onkeyup="value=value.replace(/[^\\d]/g,\'\')" class="innum" data="'+id+'" bs="'+rate+'" bstitle="'+bstitle+'" title="'+title+'"></span></div>';
								htmlkj=htmlkj+'<div class="betting"><div class="touzhu-box" data="'+id+'" bs="'+rate+'" bstitle="'+bstitle+'" title="'+title+'"><span class="bstitle option">'+bstitle+'</span><span class="beinum bsnum">'+rate+'</span></div></div>';
							}
							htmlkj=htmlkj+'</div>';
							html=html+'</div>';
							htmlkj=htmlkj+'<div class="second_title">第二球</div><div class="second">';
							html=html+'<div class="second_title">第二球</div><div class="second">';
							for (var k = 11; k < 22; k++) {
								var rate=optionItems[k].rate;
								var bstitle=optionItems[k].title;
								var id=optionItems[k].optionId;
								html=html+'<div class="betting"><span class="beishu"><span class="bstitle">'+bstitle+'</span> <span class="bsnum red">'+rate+'</span></span><span class="inpbox">';
								html=html+'<input type="tel" onkeyup="value=value.replace(/[^\\d]/g,\'\')" class="innum" data="'+id+'" bs="'+rate+'" bstitle="'+bstitle+'" title="'+title+'"></span></div>';
								htmlkj=htmlkj+'<div class="betting"><div class="touzhu-box" data="'+id+'" bs="'+rate+'" bstitle="'+bstitle+'" title="'+title+'"><span class="bstitle option">'+bstitle+'</span><span class="beinum bsnum">'+rate+'</span></div></div>';
							}
							htmlkj=htmlkj+'</div>';
							html=html+'</div>';
							if(optionItems.length==33){// 如果是前三的话加上第三球
								htmlkj=htmlkj+'<div class="second_title">第三球</div><div class="third">';
								html=html+'<div class="second_title">第三球</div><div class="third">';
								for (var h = 22; h < 33; h++) {
									var rate=optionItems[h].rate;
									var bstitle=optionItems[h].title;
									var id=optionItems[h].optionId;
									html=html+'<div class="betting"><span class="beishu"><span class="bstitle">'+bstitle+'</span> <span class="bsnum red">'+rate+'</span></span><span class="inpbox">';
									html=html+'<input type="tel" onkeyup="value=value.replace(/[^\\d]/g,\'\')" class="innum" data="'+id+'" bs="'+rate+'" bstitle="'+bstitle+'" title="'+title+'"></span></div>';
									htmlkj=htmlkj+'<div class="betting"><div class="touzhu-box" data="'+id+'" bs="'+rate+'" bstitle="'+bstitle+'" title="'+title+'"><span class="bstitle option">'+bstitle+'</span><span class="beinum bsnum">'+rate+'</span></div></div>';
								}
								htmlkj=htmlkj+'</div>';
								html=html+'</div>';
							}
						}else{// 其他玩法
							for (var j = 0; j < optionItems.length; j++) {
								var rate=optionItems[j].rate;
								var bstitle=optionItems[j].title;
								var id=optionItems[j].optionId;
								html=html+'<div class="betting"><span class="beishu"><span class="bstitle">'+bstitle+'</span> <span class="bsnum red">'+rate+'</span></span><span class="inpbox">';
								html=html+'<input type="tel" onkeyup="value=value.replace(/[^\\d]/g,\'\')" class="innum" data="'+id+'" bs="'+rate+'" bstitle="'+bstitle+'" title="'+title+'"></span></div>';
								htmlkj=htmlkj+'<div class="betting"><div class="touzhu-box" data="'+id+'" bs="'+rate+'" bstitle="'+bstitle+'" title="'+title+'" optionType="'+optionType+'"><span class="bstitle option">'+bstitle+'</span><span class="beinum bsnum">'+rate+'</span></div></div>';
							}
						}
						html=html+'</li>';
						htmlkj=htmlkj+'</li>';
					}
					$('#zx'+t).html(html);
					$('#kj'+t).html(htmlkj);
					lotteryObj.bindKj();
					if(timerObj.rest==null){
						lotteryObj.seal();
					}
					mask.close();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	bindKj:function(){
		var t = $("#wanfatab ul>.mui-active").attr("t");
		$(".touzhu-box").unbind( "tap" );
		$(".touzhu-box").bind("tap", function(){
			if(t==2){// 任选
				if($(this).hasClass("touzhu-active")){
					$(this).toggleClass("touzhu-active");
				}else{
					var optiontype=$(this).attr("optiontype");
					var len=$(this).parent().parent().find(".touzhu-active").length;
					if(len>=parseInt(optiontype)+1) mui.toast('只能选择'+(parseInt(optiontype)+1)+'个号码',{ duration: 200, type:'div' }) 
					else $(this).toggleClass("touzhu-active");
				}
			}else if(t==3){// 组选
				if($(this).hasClass("touzhu-active")){
					$(this).toggleClass("touzhu-active");
				}else{
					var optiontype=$(this).attr("optiontype");
					var max;
					if(optiontype==0)max=2;
					else if(optiontype==1)max=3;
					var len=$(this).parent().parent().find(".touzhu-active").length;
					if(len>=max) mui.toast('只能选择'+max+'个号码',{ duration: 200, type:'div' }) 
					else $(this).toggleClass("touzhu-active");
				}
			}else if(t==4){// 直选
				if($(this).hasClass("touzhu-active")){
					$(this).toggleClass("touzhu-active");
				}else{
					var len=$(this).parent().parent().find(".touzhu-active").length;
					if(len>=1){
						mui.toast('只能选择1个号码',{ duration: 200, type:'div' });
					}else{
						var value=$(this).attr("bstitle");
						var add=true;
						$(this).parent().parent().parent().find(".touzhu-active").each(function(){
							var otherValue=$(this).attr("bstitle");
							if(value==otherValue){
								add=false;
							}
						});
						if(add) $(this).toggleClass("touzhu-active");
						else mui.toast('已选过此号码号码',{ duration: 200, type:'div' });
					}
				}
			}else{
				$(this).toggleClass("touzhu-active");
			}
		});
	},
	seal:function(){//封盘
		var mask=move.createLoading();
		mask.show();
		$('.betting .innum').attr('type','text');
		$('.betting .innum').val('封盘');
		$('.betting .innum').attr("disabled","false");
		$('.betting .innum').addClass("fengpan");
		$(".touzhu-box").each(function(){
			if($(this).find(".fengpanbox").length==0){
		    	$(this).append("<span class=\"fengpanbox\">封盘</span>");
			}
		});
		$('#openStatus').val('0');
		$('.betting .touzhu-box').removeClass("touzhu-active");
		$(".touzhu-box").unbind( "tap" );
		$('.form-box').hide();
		mask.close();
	},
	open:function(t){//开奖
		if(t!=-1){
			$('#open-number .num ').text('-');
			$('#openlable').html("开奖中...");
			$('#openlable').addClass("red");
			var sessiongNo=$('#sessionNo').attr("data");
			$('#openqihao').html(sessiongNo+"期");
			$('#openqihao').attr("data",sessiongNo);
			$('#openStatus').val('1');
			$('.betting .innum').attr('type','tel');
			$('.betting .innum').val('');
			$('.betting .innum').removeAttr("disabled");
			$('.betting .innum').removeClass("fengpan");
			$(".fengpanbox").each(function(){
				if($(this).find(".fengpanbox").length==0){
			    	$(this).remove();
				}
			});
		}
		var map = {};
		map['u'] =move.user.u;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.gdPick11_currentTime,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var obj=result.obj;
					var sessionNo=obj.sessionNo;//当前期号
					var lastSessionNo=obj.lastSessionNo;//上期开奖期号
					var openResult=obj.openResult;//开奖号码
					var openDate=obj.openDate;//开奖时间
					var gains=obj.gains;//当前收益
					var betTime=obj.betTime;//截止投注时间
					var openTime=obj.openTime;//距离当前期开奖时间
					var money=obj.money;//余额
					var html='';
					var length=openResult.length;
					if(length==0){
						if(timeropenresult==null){
							timeropenresult=window.setInterval(function(){timerObj.result('timer-gdPick11open');},timerObj.intervalAPI);
						}
					}else{
						window.clearInterval(timeropenresult);
						timeropenresult=null;
						
						var length=openResult.length;
						for (var j = 0; j <openResult.length; j++) {
							html=html+'<span class="num bg-red">'+openResult[j]+'</span>';
							if(j==4){
								html=html+'<span class="symbol">=</span>';
							}else if((openResult.length-1)==j){
							}else{
								html=html+'<span class="symbol">+</span>';
							}
						}
						
						$('#open-number').html(html);
						$('#openlable').html("开奖");
						$('#openlable').removeClass("red");
						console.info(lastSessionNo+'期开奖号码::'+openResult);
					}
					$('#balance').html(money);
					$('#gains').html(gains);
					$('#opentime').html(openDate);
					$('#sessionNo').html(sessionNo+" 期");
					$('#sessionNo').attr("data",sessionNo);
					$('#openqihao').html(lastSessionNo+"期");
					$('#openqihao').attr("data",lastSessionNo);
					timerObj.rest=betTime;
					timerObj.openrest=openTime;
					console.info('timerxiazhu2::'+timerxiazhu);
					if(timerxiazhu==null){
						timerxiazhu=window.setInterval(function(){timerObj.init(timerObj.rest,'timer-gdPick11');},timerObj.interval); 
					}
					if(timeropen==null){
						timeropen=window.setInterval(function(){timerObj.open(timerObj.openrest,'timer-gdPick11open');},timerObj.interval);
					}
					lotteryObj.bindKj();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	submit:function(){
		$("#submitbtn").bind("tap", function(){
			var openStatus=$('#openStatus').val();
			if(openStatus=='0'){
				mui.alert('当前期已封盘，敬请期待下一期',document.title, function() {});
				return;
			}
			var wanfa=$('#wanfa').val();
			var xiazhu='1';//0.自选1.快捷
			if(wanfa=='0'){//两面盘
				xiazhu=$('#liangmianpantab').val();
			}else if(wanfa=='1'){//1-5名
				xiazhu=$('#mingtab').val();
			}else if(wanfa=='2'){// 任选
				xiazhu=$('#renxuantab').val();
			}else if(wanfa=='3'){// 组选
				xiazhu=$('#zuxuantab').val();
			}else if(wanfa=='4'){// 直选
				xiazhu=$('#zhixuantab').val();
			}
			
			if(xiazhu=='0'){//自选下注
				optionArray= new Array();
				var num=0;
				var totalprice=0;
				var jiangjin=0;
				var yingli=0;
				var html='';
				$('#zx'+wanfa+' .innum').each(function(i){
					var numval=$.trim($(this).val());
					var id=$.trim($(this).attr('data'));
					var bs=$.trim($(this).attr('bs'));
					var bstitle=$.trim($(this).attr('bstitle'));
					var title=$.trim($(this).attr('title'));
					if(numval!=''){
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						optionArray[num]=bettingObj(id,numval);
						num++;
						totalprice=parseFloat(totalprice)+parseFloat(numval);
						jiangjin=parseFloat(jiangjin)+parseFloat(numval*bs);//奖金
					}
				 });
				var length=optionArray.length;
				if(length==0){
					mui.alert('请选择要下注的内容',document.title, function() {});
				}else{
					$('#moneybox').hide();
					jiangjin=Math.floor(jiangjin * 100) / 100;
					yingli=parseFloat(jiangjin)-parseFloat(totalprice);
					yingli=Math.floor(yingli * 100) / 100;
					$('#touzhushu').text(num);
					$('#totalprice').text(totalprice+"元");
					$('#jiangjin').text(jiangjin+"元");
					$('#yingli').text(yingli+"元");
					$('#zuxuanxiazhu div.betting-confirm').html(html);
					$('.form-box').show();
					$('.masktrans').show();
					// 绑定下注按钮提交事件
					lotteryObj.bindSubmit();
				}
			}else if(xiazhu=='1'){//快捷下注
				$('#moneybox').show();
				optionArray= new Array();
				var selectedArray=new Array();// bindBeInput方法计算金额的时候用到
				var num=0;
				var totalprice=0;
				var jiangjin=0;
				var yingli=0;
				var html='';
				var isCheck=false;
				if(wanfa==2){// 任选
					var len0=$('#kj2 [optiontype="0"] div.touzhu-active').length;//一中一
					if(len0==1){// 只能选一个号
						var bstitle=$.trim($('#kj2 [optiontype="0"] div.touzhu-active').attr('bstitle'));
						var title=$.trim($('#kj2 [optiontype="0"] div.touzhu-active').attr('title'));
						var id=$.trim($('#kj2 [optiontype="0"] div.touzhu-active').attr('data'));
						var bs=$.trim($('#kj2 [optiontype="0"] div.touzhu-active').attr('bs'));
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
					var len1=$('#kj2 [optiontype="1"] div.touzhu-active').length;//二中二
					if(len1==2){// 只能选二个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj2 [optiontype="1"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
					var len2=$('#kj2 [optiontype="2"] div.touzhu-active').length;//三中三
					if(len2==3){// 只能选三个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj2 [optiontype="2"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
					var len3=$('#kj2 [optiontype="3"] div.touzhu-active').length;//四中四
					if(len3==4){// 只能选四个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj2 [optiontype="3"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
					var len4=$('#kj2 [optiontype="4"] div.touzhu-active').length;//五中五
					if(len4==5){// 只能选五个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj2 [optiontype="4"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
					var len5=$('#kj2 [optiontype="5"] div.touzhu-active').length;//六中五
					if(len5==6){// 只能选六个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj2 [optiontype="5"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
					var len6=$('#kj2 [optiontype="6"] div.touzhu-active').length;//七中五
					if(len6==7){// 只能选七个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj2 [optiontype="6"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
					var len7=$('#kj2 [optiontype="7"] div.touzhu-active').length;//八中五
					if(len7==8){// 只能选八个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj2 [optiontype="7"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
				}else if(wanfa==3){// 组选
					var len0=$('#kj3 [optiontype="0"] div.touzhu-active').length;//前二
					if(len0==2){// 只能选二个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj3 [optiontype="0"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
					var len1=$('#kj3 [optiontype="1"] div.touzhu-active').length;//前三
					if(len1==3){// 只能选三个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj3 [optiontype="1"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
				}else if(wanfa==4){// 直选
					var len0=$('#kj4 [optiontype="0"] div.touzhu-active').length;//前二
					if(len0==2){// 只能选二个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj4 [optiontype="0"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
					var len1=$('#kj4 [optiontype="1"] div.touzhu-active').length;//前二
					if(len1==3){// 只能选二个号
						var title="";
						var bstitle="";
						var id="";// 最后一个号的optionId
						var bs="";// 赔率
						$('#kj4 [optiontype="1"] div.touzhu-active').each(function(i){
							bstitle=bstitle+$(this).attr('bstitle')+",";
							title=$.trim($(this).attr('title'));
							id=id+$.trim($(this).attr('data'))+",";
							bs=$.trim($(this).attr('bs'));
						});
						bstitle=bstitle.substring(0,bstitle.length-1);// 去掉最后的逗号
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						id=id.substring(0,id.length-1);// 去掉最后的逗号
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					}
				}else{
					$('#kj'+wanfa+' div.touzhu-active').each(function(i){
						var id=$.trim($(this).attr('data'));
						var bs=$.trim($(this).attr('bs'));
						var bstitle=$.trim($(this).attr('bstitle'));
						var title=$.trim($(this).attr('title'));
						html=html+'<span class="con">'+title+' '+bstitle+'</span>';
						num++;
						selectedArray.push({"id":id,"bs":bs});
						isCheck=true;
					});
				}
				if(!isCheck){
					mui.alert('请选择要下注的内容',document.title, function() {});
				}else{
					$('#touzhushu').text(num);
					$('#totalprice').text(totalprice+"元");
					$('#jiangjin').text(jiangjin+"元");
					$('#yingli').text(yingli+"元");
					$('#zuxuanxiazhu div.betting-confirm').html(html);
					$('.form-box').show();
					$('.masktrans').show();
					// 绑定下注按钮提交事件
					lotteryObj.bindSubmit();
					lotteryObj.bindBeInput(selectedArray);
				}
			}
			
		});
	},
	bindBeInput:function(selectedArray){
		$("#money").unbind("keyup");
		$("#money").bind("keyup", function(){
			var price=$.trim($('#money').val());
			if(price==''){
				price=0;
			}
			var totalprice=0;
			var jiangjin=0;
			var yingli=0;
			var num=0;
			for(var i=0;i<selectedArray.length;i++){
				var id=selectedArray[i].id;
				var bs=selectedArray[i].bs;
				optionArray[num]=bettingObj(id,price);
				num++;
				totalprice=parseFloat(totalprice)+parseFloat(price);
				jiangjin=parseFloat(jiangjin)+parseFloat(price*bs);//奖金
			}
			
//			jiangjin=Math.floor(jiangjin * 100) / 100;
			jiangjin=jiangjin.toFixed(2);
			yingli=parseFloat(jiangjin)-parseFloat(totalprice);
			yingli=yingli.toFixed(2);
		//	yingli=Math.floor(yingli * 100) / 100;
			
			$('#totalprice').text(totalprice+"元");
			$('#jiangjin').text(jiangjin+"元");
			$('#yingli').text(yingli+"元");
		});
	},
	bindSubmit:function(){
		//下注
		$(".btn-box .submit").unbind("tap");
		$(".btn-box .submit").bind("tap", function(){
			var touzhushu =$('#touzhushu').text();
			var totalprice =$('#totalprice').text();
			totalprice=totalprice.replace('元','');
			var balance =$('#balance').text();
			if(move.user.u==null){
				baseObj.openLogin();
			}
			if(parseFloat(totalprice)<=0){
				mui.alert('请输入投注额',document.title, function() {});
				return;
			}
			if(parseFloat(balance)-parseFloat(totalprice)<0){
				mui.alert('余额不足',document.title, function() {});
			}else{
				$(".btn-box .submit").unbind();// 解除绑定，防止重复提交
				lotteryObj.bet();
			}
		});
	},
	bet:function(){
		var potions=JSON.stringify(optionArray);
		var sessionNo=$('#sessionNo').attr('data');
		if(potions=='null'||potions==''){
			mui.alert('请选择要下注的内容',document.title, function() {});
			return;
		}
		if(move.user.u!=null){
			var openStatus=$('#openStatus').val();
			if(openStatus=='1'){//正常可投注
				var map = {};
				map['u'] =move.user.u;
				map['sessionNo'] =sessionNo;
				map['optionArray'] =potions;
				var mw=baseObj.mw(map);
				var mask=move.createLoading();
				mask.show();
				$.ajax({
					type: "POST",
					url:move.gdPick11_bet,
					dataType:'json',
					data:{
						mw:mw
					},
					success: function(ret){
						mask.close();
						var code=ret.code;
						var msg=ret.msg;
						if(code=='200'){
							var data=ret.data;
							var money=data.money;
							$('#balance').text(money);
							mui.toast('投注成功',{
							    duration:'long',
							    type:'div' 
							});
							$('#money').val('');
							$('.form-box').hide();
							$('.masktrans').hide();
							var wanfa=$('#wanfa').val();//0.两面盘 1.1-10名 2.冠亚军和
							openStatus=$('#openStatus').val();
							if(openStatus=='1'){
								if(wanfa=='0'){
									var liangmianpantab=$('#liangmianpantab').val();
									if(liangmianpantab=='0'){
										$('#zx0 .innum').val('');
									}else{//快捷下注
										$('#kj0 div.touzhu-active').removeClass('touzhu-active');
									}
								}else if(wanfa=='1'){
									var mingtab=$('#mingtab').val();
									if(mingtab=='0'){
										$('#zx1 .innum').val('');
									}else{//快捷下注
										$('#kj1 div.touzhu-active').removeClass('touzhu-active');
									}
								}else if(wanfa=='2'){
									var renxuantab=$('#renxuantab').val();
									if(renxuantab=='0'){
										$('#zx2 .innum').val('');
									}else{//快捷下注
										$('#kj2 div.touzhu-active').removeClass('touzhu-active');
									}
								}else if(wanfa=='3'){
									var zuxuantab=$('#zuxuantab').val();
									if(zuxuantab=='0'){
										$('#zx3 .innum').val('');
									}else{//快捷下注
										$('#kj3 div.touzhu-active').removeClass('touzhu-active');
									}
								}else if(wanfa=='4'){
									var zhixuantab=$('#zhixuantab').val();
									if(zhixuantab=='0'){
										$('#zx4 .innum').val('');
									}else{//快捷下注
										$('#kj4 div.touzhu-active').removeClass('touzhu-active');
									}
								}
							}
						}else{
							mui.toast(msg,{duration:'long',type:'div' });
							mask.close();
							$('#money').val('');
							$('.form-box').hide();
							$('.masktrans').hide();
						}
					},
					error: function (jqXHR, textStatus, errorThrown) {
						mui.toast('网络出错',{duration:'long',type:'div' });
						mask.close();
						$('.form-box').hide();
						$('.masktrans').hide();
					}
				});
			}else{
				//封盘
				mui.alert('当前期已封盘，敬请期待下一期',document.title, function() {});
			}
		}else{
			baseObj.openLogin();
		}
	},
	trend:function(type){
		var mask=move.createLoading();
		mask.show();
		if(typeof type=='undefined'){
			type='0';
		}
		var map = {};
		map['type'] =type;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.gdPick11_trend,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var length=items.length;
					var cssid="";
					if(type=='0'){
						cssid='haoma';
					}else if(type=='1'){
						cssid='daxiao';
					}else if(type=='2'){
						cssid='danshuang';
					}else if(type=='3'){
						cssid='guanyajun';
					}else if(type=='4'){
						cssid='longhu';
					}
					var html='<ul class="zoushi-table" id="'+cssid+'">';
					for (var i = 0; i < length; i++) {
						var liclass='single';
						if(i%2==1){
							liclass='double';
						}
						html=html+'<li class="'+liclass+'">';
						var obj=items[i];
						var size=obj.length;
						for (var j = 0; j < size; j++) {
							var va=obj[j];
							var spanclass='';
							if(va=='大'||va=='双'||va=='龙'){
								spanclass='colour-red';
							}else if(va=='小'||va=='单'||va=='虎'){
								spanclass='colour-green';
							}
							html=html+'<span class="lattice '+spanclass+'">'+va+'</span>';
						}
						html=html+'</li>';
					}
					html=html+'</ul>';
					
					$('#zoushibox .zoushi-table-box').html(html);
				}else{
					mui.toast(msg,{
					    duration:'long',
					    type:'div' 
					});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	ranking:function(){
		var mask=move.createLoading();
		mask.show();
		if(typeof type=='undefined'){
			type='0';
		}
		$.ajax({
			type: "POST",
			url:move.gdPick11_hotRanking,
			dataType:'json',
			data:{
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var length=items.length;
					var html='<ul class="paiming-table">';
					for (var i = 0; i < length; i++) {
						var liclass='single';
						if(i%2==1){
							liclass='double';
						}
						html=html+'<li class="'+liclass+'">';
						var obj=items[i];
						var title=obj.title;
						var num=obj.num;
						html=html+'<span class="lattice ">'+title+'</span><span class="lattice colour-red">已连开'+num+'期</span>';
						html=html+'</li>';
					}
					html=html+'</ul>';
					$('#paimingbox .paiming-table-box').html(html);
				}else{
					mui.toast(msg,{
					    duration:'long',
					    type:'div' 
					});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	openlist:function(isReset){
		if(isReset){
			move.pageIndex=0;
		}
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.gdPick11_openList,
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
							var openResult=obj.resultItems;//期号
							var sumItems=obj.sumItems;
							var time=obj.openTime;//开奖时间
							var longhu=obj.longhu;//龙虎
							var openhtml='';
							for ( var j = 0; j < openResult.length; j++) {
								var num=openResult[j];
								openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
							}
							var hehtml='';
							for ( var j = 0; j < sumItems.length; j++) {
								var num=sumItems[j];
								hehtml=hehtml+'<span class="">'+num+'</span>';
							}
							html=html+'<li class="open-cell"><div class="title-box"><div class="title">'+title+'</div><div class="time">第'+sessionNo+'期 '+time+'</div></div>';
							html=html+'<div class="open-number">'+openhtml+'</div>';
							html=html+'<div class="data-box"><div class="title"><div class="he">总和</div><div class="longhu">龙虎</div></div>';
							html=html+'<div class="data-details"><div class="he">'+hehtml+'</div><div class="longhu">'+longhu+'</div></div> </div></li>';
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
	}
}

move.gdPick11_currentTime=move.server+'/api/gdPick11_currentTime';
move.gdPick11_betPanel=move.server+'/api/gdPick11_betPanel';
move.gdPick11_bet=move.server+'/api/gdPick11_bet';
move.gdPick11_trend=move.server+'/api/gdPick11_trend';
move.gdPick11_hotRanking=move.server+'/api/gdPick11_hotRanking';
move.gdPick11_openList=move.server+'/api/gdPick11_openList';