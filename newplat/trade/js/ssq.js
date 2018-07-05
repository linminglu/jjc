var lotteryObj={
	gameType:"311",
	defPageSize:20,
	timer:null,
	timeout: {
		day: 0,
		hour: 0,
		minute: 0,
		second: 0
	},
	unit:1.00,
	redNum:0,
	blueNum:0,
	tempPlay:[],//临时投注
	play:[],//投注
	totalPrice:0,//总金额
	multiple:1,//倍数
	multiplePrice:0,//单倍金额
	totalNum:0,//总注数
	hemaiNum:0,//合买分为的份数
	sessionNo:null,
	sessionNoArr:[],
	limitNum:400,
	zhuiHaoPrice:0,
	zhuiHao:false,
	input:'',
	inputNum:0,
	init:function(){
		//
		$(".buytype").bind("click", function(){
			var buytype=$(this).val();
			if(buytype=='0'){
				$('#daigou').show();
				$('#hemai').hide();
			}else if(buytype=='1'){
				$('#daigou').hide();
				$('#hemai').show();
				var isTure=$("#zhuihao").is(':checked');
				if(isTure){
					$('#zhuihao').click();
				}
			}
		});
		//追号
		$("#zhuihao").bind("click", function(){
			var isTure=$("#zhuihao").is(':checked');
			if(isTure){
				$('#zhuihaostop').attr("checked",'true');
				$('#zhuihaobox').show();
				lotteryObj.createZhuiHao(5);
				lotteryObj.zhuiHao=true;
				lotteryObj.calcCost();
			}else{
				$('#zhuihaostop').removeAttr("checked");
				$('#zhuihaobox').hide();
				$("#zhuihaobox select").val(5);
				$('#betbox').html('');
				lotteryObj.zhuiHao=false;
				lotteryObj.calcCost();
			}
		});
		$("#zhuihaobox select").change(function(){
			var num=$(this).val();
			lotteryObj.createZhuiHao(num);
			lotteryObj.calcCost();
		});
		//选号
		$(".red-balls .ball").bind("click", function(){
			lotteryObj.redNum=$(".red-balls .selected").size();
			if(lotteryObj.redNum<20){
				$(this).toggleClass('selected');
				lotteryObj.redNum=$(".red-balls .selected").size();
				$('#redNum').text(lotteryObj.redNum);
				lotteryObj.buleNum=$(".blue-balls .selected").size();
				var totalNumB=getTotalNum(lotteryObj.redNum,lotteryObj.buleNum);
				$('#totalNumB').text(totalNumB);
				$('#totalPriceB').text((Number(totalNumB)*lotteryObj.unit).toFixed(2));
			}else{
				mui.alert('最多可选'+lotteryObj.redNum+'个红球',document.title, function() {}); 
			}
			
		});
		$(".blue-balls .ball").bind("click", function(){
			$(this).toggleClass('selected');
			lotteryObj.buleNum=$(".blue-balls .selected").size();
			$('#blueNum').text(lotteryObj.buleNum);
			lotteryObj.redNum=$(".red-balls .selected").size();
			var totalNumB=getTotalNum(lotteryObj.redNum,lotteryObj.buleNum);
			$('#totalNumB').text(totalNumB);
			$('#totalPriceB').text((Number(totalNumB)*lotteryObj.unit).toFixed(2));
			
		});
		//清空选号
		$(".gametab").bind("click", function(){
			var name=$(this).attr('name');
			var val=$(this).val();
			$('.modebox').hide();
			$('#'+name+val).show();
		});
		//清空选号
		$(".select-mode a.clean").bind("click", function(){
			lotteryObj.clearSele();
		});
		//确认选号
		$(".select-mode a.confirm").bind("click", function(){
			var redArr=$(".red-balls .selected");
			var blueArr=$(".blue-balls .selected");
			var redNum=redArr.size();
			var blueNum=blueArr.size();
			var totalNum=getTotalNum(redNum,blueNum);
			if(totalNum>0){
				var red=[];
				var blue=[];
				for(var i=0;i<redArr.length;i++) {
					red[i]=$(redArr[i]).text();
				}
				for(var i=0;i<blueArr.length;i++) {
					blue[i]=$(blueArr[i]).text();
				}
				var play = {
					play: red.join(",") + "|" + blue.join(","),
					num: totalNum,
					cost: (Number(totalNum)*lotteryObj.unit).toFixed(2)*1
				};
				lotteryObj.play.push(play);
				lotteryObj.calcCost();
				lotteryObj.clearSele();
				lotteryObj.changeHemai();
			}
		});
		
		$(".todo .radom").bind("click", function(){
			var num=$(this).attr('data');
			lotteryObj.randomPlay(num);
		});
		
		$(".todo .clean-selected").bind("click", function(){
			$('#combainlist').html('');
			lotteryObj.play=[];
			lotteryObj.calcCost();
			lotteryObj.clearHemai();
		});
		//改变倍数
		$("#multiple").keyup(function(){
			lotteryObj.multiple=$(this).val();
			$('#betbox .multiple').val(lotteryObj.multiple);
			$('#betbox input.sessionNoCheckbox').val(lotteryObj.multiple);
			var multiple=lotteryObj.multiple;
			var multiplePrice=lotteryObj.multiplePrice;
			var totalPrice=Number(multiple).toFixed(2)*Number(multiplePrice).toFixed(2);
			$('#betbox .sessionPrice').text(totalPrice);
			lotteryObj.calcCost();
			lotteryObj.changeHemai();
		});
		//输入注数
		$(".input-mode textarea").keyup(function(){
			lotteryObj.checkInput(this);
		});
		//确认选号
		$(".input-mode a.confirm").bind("click", function(){
			lotteryObj.confirmInput();
		});
		//清空选号
		$(".input-mode a.clean").bind("click", function(){
			lotteryObj.cleanInput();
		});
		$('.buy-area .bet-button').bind("click", function(){
			lotteryObj.bet();
		});
		lotteryObj.bindHeimai();
		lotteryObj.load();
		lotteryObj.gameOption();
	},
	gameOption:function(){// 玩法
		$.ajax({
			type: "POST",
			url:move.gameOption,
			dataType:"json",
			data:{
				mw:baseObj.mw({gameType:lotteryObj.gameType})//加密参数
			},
			success: function(ret){
				if(ret && ret.code=="200"){
					var items=ret.data.items;
					if(items){
						$("#playtypes .tab p:nth-child(2)").each(function(i){
							$(this).html(items[i].betRate);
						});
					}
				}
			},
			error:function (jqXHR, textStatus, errorThrown) {
				console.log("dataTimer.exec::reqSession::[ERR]"+errorThrown);
			}
		});
	},
	bindZhuiHaoMultplt:function(){//改变追号倍数
		//追号改变倍数
		$("#betbox .multiple").keyup(function(){
			var sessionNo=$(this).attr('name');
			var multiple=$(this).val();
			$('#betbox input.sessionNoCheckbox[name="'+sessionNo+'"]').val(multiple);
			var multiplePrice=lotteryObj.multiplePrice;
			var totalPrice=Number(multiple).toFixed(2)*Number(multiplePrice).toFixed(2);
			$(this).nextAll('.sessionPrice').text(totalPrice);
			lotteryObj.calcCost();
		});
	},
	createZhuiHao:function(num){//创建追号
		var html='';
		var multiple=this.multiple;
		var multiplePrice=this.multiplePrice;
		var totalPrice=Number(multiple).toFixed(2)*Number(multiplePrice).toFixed(2);
		
		for(var i=0;i<num;i++) {
			var no=i+1+'';
			if(no.length==1){
				no='0'+no;
			}
			var sessionNo=lotteryObj.sessionNoArr[i];
			if(sessionNo){
				html=html+'<div class="middle"><span>'+no+'.</span><input class="sessionNoCheckbox" type="checkbox" checked="checked" name="'+sessionNo+'" value="'+multiple+'">  ';
				html=html+'<span class="period">'+sessionNo+'期</span>&nbsp;<input type="text" name="'+sessionNo+'" value="'+multiple+'" class="multiple">倍&nbsp;￥<span class="sessionPrice">'+totalPrice+'</span>元</div>';
			}
		}
		$('#betbox').html(html);
		this.bindZhuiHaoMultplt();
		
	},
	updateTotalPrice:function(){
		$('#totalPrice').text(this.totalPrice);
	},
	bindRemovePlay:function(){
		$("#combainlist a.remove").unbind();
		$("#combainlist a.remove").bind("click", function(){
			lotteryObj.removePlay($(this).parent().index());
			$($(this).parent()).remove();
		});
	},
	removePlay:function(index){
		this.play.splice(index, 1);
		this.calcCost();
	},
	clearSele:function(){
		$(".blue-balls .ball").removeClass('selected');
		$(".red-balls .ball").removeClass('selected');
		$('#redNum').text(0);
		$('#blueNum').text(0);
		$('#totalNumB').text(0);
		$('#totalPriceB').text(Number(0).toFixed(2));
		lotteryObj.redNum=0;
		lotteryObj.buleNum=0;
	},
	randomPlay:function(count){
		var rs = ['01','02','03','04','05','06','07','08','08','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33'];
		var bs = ['01','02','03','04','05','06','07','08','08','10','11','12','13','14','15','16'];
		for(var c = 0; c < count; ++c) {
			var red = {};
			var blue = {};
			for(var i = 0; i < 6; ++i) {
				while(true) {
					var index = Math.floor(Math.random() * rs.length);
					var ball = rs[index];
					if(!red[ball]) {
						red[ball] = ball;
						break;
					}
				}
			}
			for(var i = 0; i < 1; ++i) {
				while(true) {
					var index = Math.floor(Math.random() * bs.length);
					var ball = bs[index];
					if(!blue[ball]) {
						blue[ball] = ball;
						break;
					}
				}
			}
			//console.log(Object.keys(red).sort());	
			var play = {
				play: Object.keys(red).sort().join(",") + "|" + Object.keys(blue).sort().join(","),
				num: 1,
				cost: 2.00
			};
			var length=this.play.length;
			if(parseInt(length+1)>this.limitNum){
				mui.alert('请分多次投注',document.title, function() {});
				this.calcCost();
				return;
			}else{
				this.play.push(play);
			}
		}
		this.calcCost();
		this.changeHemai();
	},
	calcCost:function(){
		console.log(this.play);
		var num = 0;
		var cost = 0.00;
		var html='';
		for(var play in this.play) {
			var play = this.play[play];
			num += play.num;
			cost += play.cost;
			html=html+'<div class="combain-cell"><span>'+play.play+'</span><a class="remove">×</a></div>';
		}
		this.totalPrice=cost;
		this.totalNum=num;
		
		this.multiplePrice=this.totalPrice;
		this.totalPrice=Number(this.multiple).toFixed(2)*Number(this.totalPrice).toFixed(2);
		
		var multiplePrice=this.multiplePrice;//单注金额
		
		
		if(this.zhuiHao){
			//更新金额
			var multipleArr=$('#betbox .multiple');//追号的倍数
			
			for(var i = 0;i<multipleArr.length;i++) {
				var mult=$(multipleArr[i]).val();
				var price=Number(mult).toFixed(2)*parseInt(multiplePrice);
				$(multipleArr[i]).nextAll('.sessionPrice').text(price);
			}
			
			var sessionPriceArr=$('#betbox .sessionPrice');
			var zhuiHaoPrice=0;
			for(var i = 0;i<sessionPriceArr.length;i++) {
				var price=$(sessionPriceArr[i]).text();
				zhuiHaoPrice=parseInt(zhuiHaoPrice)+parseInt(price);
			}
			this.zhuiHaoPrice=zhuiHaoPrice;
			this.totalPrice=this.totalPrice+parseInt(this.zhuiHaoPrice);
			
		}
		
		$('#multiplePrice').text(Number(this.multiplePrice).toFixed(2));
		$('#totalPrice').text(Number(this.totalPrice).toFixed(2));
		$('#totalNum').text(this.totalNum);
		
		$('#combainlist').html(html);
		lotteryObj.bindRemovePlay();
	},
	checkInput:function(obj){
		var playlist = [];
		var unit = this.unit;
		var inputtext=$(obj).val();
		inputtext.split("\n").forEach(function(line) {
			var play = line.trim();
			if(/^(0[1-9]|[1-2][0-9]|3[0-3])(,(0[1-9]|[1-2][0-9]|3[0-3])){5}\|(0[1-9]|1[0-6])$/.test(play)) {
				playlist.push({play: play, num: 1, cost: unit});
			}
		});
		var inputNum=playlist.length;
		this.inputNum = inputNum;
		$('#inputNum').text(inputNum);
	},
	confirmInput: function() {
		var playlist = [];
		var unit = this.unit;
		var inputtext=$('.input-mode textarea').val();
		inputtext.split("\n").forEach(function(line) {
			var play = line.trim();
			if(/^(0[1-9]|[1-2][0-9]|3[0-3])(,(0[1-9]|[1-2][0-9]|3[0-3])){5}\|(0[1-9]|1[0-6])$/.test(play)) {
				playlist.push({play: play, num: 1, cost: unit});
			}
		});
		this.play = this.play.concat(playlist);
		console.log(playlist);
		var inputNum=playlist.length;
		$('#inputNum').text(inputNum);
		this.cleanInput();
		this.calcCost();
		this.changeHemai();
	},
	cleanInput: function() {
		this.input = '';
		this.inputNum = 0;
		$('.input-mode textarea').val('');
		$('#inputNum').text(this.inputNum);
	},
	changeHemai:function(){
		var totalPrice=this.totalPrice;
		var totalNum=this.totalNum;
		var num=this.totalPrice;
		num=parseInt(num);
		this.hemaiNum=num;
		$('#hemaiNum').val(num);
		var rengouNum=1;
		$('#rengouNum').val(rengouNum);
		var rengouPirce=1.00;
		$('#rengouPirce').text(rengouPirce.toFixed(2));
		var rengouPercentage=(100/num)*rengouNum;
		$('#rengouPercentage').text(rengouPercentage.toFixed(2));
		var baodiNum=num-rengouNum;
		$('#baodiNum').val(baodiNum);
		$('#baoduPirce').text((totalPrice-1).toFixed(2));
		var baodiPercentage=(100/num)*baodiNum;
		$('#baodiPercentage').text(baodiPercentage.toFixed(2));
	},
	bindHeimai:function(){
		$("#hemaiNum").keyup(function(){
			var totalPrice=lotteryObj.totalPrice;
			var totalNum=lotteryObj.totalPrice;
			var num=$('#hemaiNum').val();
			
			if(num>totalNum){
				$('#hemaiNum').val(totalNum);
				this.hemaiNum=totalNum;
				mui.alert('最多'+totalNum+'份',document.title, function() {});
				return;
			}else if(num<=0){
				$('#hemaiNum').val(totalNum);
				lotteryObj.hemaiNum=totalNum;
				return;
			}
			if(!num){
				num=lotteryObj.totalPrice;
			}
			num=parseInt(num);
			var rengouNum;
			if(num==1){
				rengouNum=1
				$('#rengouNum').val(rengouNum);
			}else{
				rengouNum=$('#rengouNum').val();
			}
			
			var unitPrice=totalPrice/num;
			$('#hemai .unit_value').text(unitPrice.toFixed(2));
			lotteryObj.hemaiNum=num;
			$('#hemaiNum').val(num);
			var rengouNum=$('#rengouNum').val();
			
			if(!rengouNum){
				rengouNum=1;
			}else if(rengouNum==0){
				rengouNum=1
			}
			if(rengouNum>num){
				rengouNum=num-1;
			}
			$('#rengouNum').val(rengouNum);
			var rengouPirce= unitPrice*rengouNum;
			$('#rengouPirce').text(rengouPirce.toFixed(2));
			var rengouPercentage=(100/num)*rengouNum;
			$('#rengouPercentage').text(rengouPercentage.toFixed(2));
			
			var baodiNum=num-rengouNum;
			$('#baodiNum').val(baodiNum);
			$('#baoduPirce').text((totalPrice-rengouPirce).toFixed(2));
			var baodiPercentage=(100/num)*baodiNum;
			$('#baodiPercentage').text(baodiPercentage.toFixed(2));
		});
		$("#rengouNum").keyup(function(){
//			var hemaiNum=$('#hemaiNum').val();
			var hemaiNum=lotteryObj.hemaiNum;
			var rengouNum=parseInt($(this).val());
			
			if(parseInt(rengouNum)<=0){
				$(this).val(1);
				$('#baodiNum').val(hemaiNum-1);
			}else if(parseInt(rengouNum)>parseInt(hemaiNum)){
				$('#rengouNum').val('1');
				$('#baodiNum').val(hemaiNum-1);
//				mui.alert('认购份数不能超过总份数',document.title, function() {});
			}
			var rengouNum=parseInt($(this).val());
			var baodiNum=parseInt($('#baodiNum').val());
			if((baodiNum+rengouNum)>hemaiNum){
				$('#baodiNum').val(hemaiNum-rengouNum);
			}
			lotteryObj.calcHeimai();
		});
		$("#baodiNum").keyup(function(){
//			var hemaiNum=parseInt($('#hemaiNum').val());
			var hemaiNum=parseInt(lotteryObj.hemaiNum);
			var baodiNum=parseInt($(this).val());
			var rengouNum=parseInt($('#rengouNum').val());
			if(baodiNum<0){
				$(this).val(hemaiNum-1);
				$('#rengouNum').val();
			}else if(baodiNum>=hemaiNum){
				$('#rengouNum').val('1');
				$('#baodiNum').val(hemaiNum-1);
			}else if((baodiNum+rengouNum)>hemaiNum){
				$('#rengouNum').val(hemaiNum-baodiNum);
//				$('#baodiNum').val(hemaiNum-1);
			}
			var totalPrice=lotteryObj.totalPrice;
			if(!hemaiNum){
				hemaiNum=totalPrice.totalPrice;
			}
			var unitPrice=totalPrice/hemaiNum;
			rengouNum=$('#rengouNum').val();
			baodiNum=$('#baodiNum').val();
			
			
//			if(baodiNum>hemaiNum){
//				baodiNum=hemaiNum-rengouNum;
//			}
			var rengouPirce=rengouNum*unitPrice;
			var baoduPirce=baodiNum*unitPrice;
			$('#baoduPirce').text(baoduPirce.toFixed(2));
			var baodiPercentage=(100/hemaiNum)*baodiNum;
			$('#baodiPercentage').text(baodiPercentage.toFixed(2));
			
//			lotteryObj.calcHeimai();
		});
	},
	calcHeimai:function(){
		var totalPrice=this.totalPrice;
		var totalNum=this.totalPrice;
		var num=$('#hemaiNum').val();
		
		if(num>totalNum){
			$('#hemaiNum').val(totalNum);
			this.hemaiNum=totalNum;
			mui.alert('最多'+totalNum+'份',document.title, function() {});
			return;
		}else if(num<=0){
			$('#hemaiNum').val(totalNum);
			this.hemaiNum=totalNum;
			return;
		}
		if(!num){
			num=this.totalPrice;
		}
		num=parseInt(num);
		if(num==1){
			rengouNum=1
			$('#rengouNum').val(rengouNum);
		}
		
		var unitPrice=totalPrice/num;
		$('#hemai .unit_value').text(unitPrice.toFixed(2));
		this.hemaiNum=num;
		$('#hemaiNum').val(num);
		var rengouNum=$('#rengouNum').val();
		
		if(!rengouNum){
			rengouNum=1;
		}else if(rengouNum==0){
			rengouNum=1
		}
		if(rengouNum>num){
			rengouNum=num-1;
		}
		$('#rengouNum').val(rengouNum);
		var rengouPirce= unitPrice*rengouNum;
		$('#rengouPirce').text(rengouPirce.toFixed(2));
		var rengouPercentage=(100/num)*rengouNum;
		$('#rengouPercentage').text(rengouPercentage.toFixed(2));
		
//		var baodiNum=num-rengouNum;
//		$('#baodiNum').val(baodiNum);
//		$('#baoduPirce').text(totalPrice-rengouPirce);
//		var baodiPercentage=(100/num)*baodiNum;
//		$('#baodiPercentage').text(baodiPercentage.toFixed(2));
	},
	clearHemai:function(){
		this.hemaiNum=0;
		$('#hemaiNum').val(0);
		$('#hemai .unit_value').val(1.00);
		$('#rengouNum').val(1);
		$('#rengouPirce').text('0.00');
		$('#rengouPercentage').text(0.00);
		$('#baodiNum').val(0);
		$('#baoduPirce').text('0.00');
		$('#baodiPercentage').text(0.00);
//		var totalPrice=this.totalPrice;
//		var num=this.totalPrice;
//		num=parseInt(num);
//		var unitPrice=totalPrice/num;
	},
	load:function(){
		lotteryObj.open();
		lotteryObj.openlist();
	},
	open:function(t){
		if(t!=-1){
			$('.open-number .num ').text('?');
			$('.open-number .num ').addClass('loading');
		}
		var map = {};
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.ssq_sessionInfo,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					var data=ret.data;
					var sessionNo=data.sessionNo;
					var noArray=data.noArray;
					var betTime=data.betTime;//截止投注时间
					var openTime=data.openTime;//距离当前期开奖时间
					var resultItems=data.resultItems;//开奖结果
					timerObj.rest=betTime;
					timerObj.openrest=openTime;
					if(noArray){
						lotteryObj.sessionNoArr=noArray;
					}
					var awardDate=data.awardDate;//兑奖截止时间
					var openDate=data.openDate;//开奖时间
					if(lotteryObj.sessionNo!=sessionNo){
						lotteryObj.openlist();
						lotteryObj.loadPanel();
					}
					lotteryObj.sessionNo=sessionNo;
					$('#openDate').text(openDate);
					$('#awardDate').text(awardDate);
					$('#sessionNo').text(sessionNo);
					var html='';
					
					var length=resultItems.length;
					if(length==0){
						if(timeropenresult==null){
							timeropenresult=window.setInterval(function(){timerObj.result('timer-ssqopen');},timerObj.intervalAPI);
						}
					}else{
						window.clearInterval(timeropenresult);
						timeropenresult=null;
						for (var i = 0; i < resultItems.length; i++) {
							var num=resultItems[i];
							var className='bg-red';
							if(i==6){//蓝球
								className='bg-blue';
							}
							html=html+'<span class="num '+className+'">'+num+'</span>';
						}
						$('.open-number').html(html);
						$('.open-number .num').removeClass('loading');
					}
					
					if(timerxiazhu==null){
						timerxiazhu=window.setInterval(function(){timerObj.init(timerObj.rest,'timer-ssq');},timerObj.interval); 
					}
					if(timeropen==null){
						timeropen=window.setInterval(function(){timerObj.open(timerObj.openrest,'timer-ssqopen');},timerObj.interval);
					}
					
					$('.buy-area .bet-button').show();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	bet:function(){
		var length=this.play.length;
		if(length==0){
			return;
		}
		if(move.user.u==null){
//			baseObj.openLogin();
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
//		console.log('倍数::'+this.multiple);
//		console.log('当前期::'+this.sessionNo);
//		console.log('是否追号::'+this.zhuiHao);
		var isWinStop=$("#zhuihaostop").is(':checked');//是否中奖停止
		var isGuaranteed=$("#isGuaranteed").is(':checked');//是否保底
		var baodiNum=$("#baodiNum").val();//保底份数
		
		var isBaodi="0";
		if(isGuaranteed){
			isBaodi="1";
		}
		var isStop="0";
		if(isWinStop){
			isStop="1";
		}
		var isPrivacy=$("input[name='access']:checked").val();
		var betType=$("input[name='buytype']:checked").val();
//		console.log('buytype::'+betType);
//		console.log('合买份数::'+$('#hemaiNum').val());
		
		var buyNum=$('#rengouNum').val();//认购份数
		var baodiNum=$('#baodiNum').val();//保底份数
		
//		console.log('保底份数::'+baodiNum);
//		console.log('是否保底::'+isBaodi);
//		console.log('当前期号::'+this.sessionNo);
//		console.log('当前倍数::'+this.multiple);
		var sessionArray=[];
		var obj=zhuiHaoObj(this.sessionNo,this.multiple);
		sessionArray.push(JSON.stringify(obj));
		console.log('sessionArray::'+sessionArray);
		var isAddNo="0";
		if(this.zhuiHao){//是否追号
			isAddNo="1";
			$('#betbox input.sessionNoCheckbox:checked').each(function(e){
				var multipleTemp=$(this).val();
				var sessionNoTemp=$(this).attr('name');
				var obj=zhuiHaoObj(sessionNoTemp,multipleTemp);
				sessionArray.push(JSON.stringify(obj));
			}); 
			
		}
		
		var playArr=this.play;
		var betBallArray=[];
		for(var i=0;i<playArr.length;i++) {
			var temp=playArr[i];
			var bet=temp.play;
			var obj=betBallObj(bet,'');
			betBallArray.push(JSON.stringify(obj));
		}
//		console.log('betBallArray::'+betBallArray);

		var map = {};
		map['u'] =move.user.u;
		map['betBallArray'] ='['+betBallArray+']';
		map['sessionArray'] ='['+sessionArray+']';
		map['betType'] =betType;
		map['isAddNo'] =isAddNo;
		map['isWinStop'] =isStop;
		map['num'] =this.hemaiNum;
		map['buyNum'] =buyNum;
		map['isGuaranteed'] =isBaodi;
		map['isPrivacy'] =isPrivacy;
		map['guaranteedNum'] =baodiNum;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.ssq_bet,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var data=ret.data;
				var code=ret.code;
				var msg=ret.msg;
				console.log('code::'+code);
				if(code=='200'){
					var money=data.money;
					move.user.money=money;
					$('.logined .money').text(money);
					store.set('user', move.user);
					lotteryObj.clearAll();
				}else{
				}
				mui.alert(msg,document.title, function() {});
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
		
		
		
	},
	loadPanel:function(){
//		var mask=move.createLoading();
//		mask.show();
		var map = {};
//		map['playType'] =t;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.ssq_betPanel,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
//				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var redItems=result.redItems;
					for (var i = 0; i < redItems.length; i++) {
						var redNum=redItems[i];
						var number=redNum.ballName;
						var trend=redNum.trend;
						$(".red-balls .ball[data='"+number+"']").next('span').text(trend);
					}
					
					var blueItems=result.blueItems;
					for (var i = 0; i < blueItems.length; i++) {
						var redNum=blueItems[i];
						var number=redNum.ballName;
						var trend=redNum.trend;
						$(".blue-balls .ball[data='"+number+"']").next('span').text(trend);
					}
					if(timerObj.rest==null){
						lotteryObj.seal();
					}
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
//				mask.close();
			}
		});
	},
	openlist:function(){
		var map = {};
		map['pageSize'] =13;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.ssq_openList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var html='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var sessionNo=obj.sessionNo;
						var resultItems=obj.resultItems;
						html=html+'<tr><td>'+sessionNo+'</td>';
						for (var j=0;j<resultItems.length;j++) {
							var num=resultItems[j];
							html=html+'<td>'+num+'</td>';
						}
						html=html+'</tr>';
					}
					$('#poenlistbox').html(html);
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	seal:function(){
		var mask=move.createLoading();
		mask.show();
		$('.buy-area .bet-button').hide();
		mask.close();
	},
	clearAll:function(){
		this.cleanInput();
		this.clearHemai();
		this.redNum=0;
		this.blueNum=0;
		this.tempPlay=[];//临时投注
		this.play=[];//投注
		this.totalPrice=0;//总金额
		this.multiple=1;//倍数
		this.multiplePrice=0;//单倍金额
		this.totalNum=0;//总注数
		this.hemaiNum=0;//合买分为的份数
		this.zhuiHaoPrice=0;
		this.zhuiHao=false;
		this.input='';
		this.inputNum=0;
		var isTure=$("#zhuihao").is(':checked');
		if(isTure){
			$('#zhuihao').click();
		}
		this.calcCost();
		$('#multiple').val(this.multiple);
	},
	bindPage:function(){
		$(".pager .first").unbind( "click");
		$(".pager .first").bind("click", function(){
			var type=$(this).attr('type');
			move.pageIndex=0;
			lotteryObj.myBet();
		});
		$(".pager .prev").unbind( "click");
		$(".pager .prev").bind("click", function(){
			var type=$(this).attr('type');
			if(move.pageIndex>0){
				move.pageIndex=move.pageIndex-1;
				lotteryObj.myBet();
			}
		});
		$(".pager .next").unbind( "click");
		$(".pager .next").bind("click", function(){
			var type=$(this).attr('type');
			var toatlpage=$('#toatlpage').text();
			toatlpage=parseInt(toatlpage);
			if((move.pageIndex+1)<toatlpage){
				move.pageIndex=move.pageIndex+1;
				lotteryObj.myBet();
			}
		});
		$(".pager .last").unbind( "click");
		$(".pager .last").bind("click", function(){
			var type=$(this).attr('type');
			var toatlpage=$('#toatlpage').text();
			toatlpage=parseInt(toatlpage);
			if((move.pageIndex+1)<toatlpage){
				move.pageIndex=(toatlpage-1);
				lotteryObj.myBet();
			}
		});
		$(".search").unbind( "click");
		$(".search").bind("click", function(){
			move.pageIndex=0;
			lotteryObj.myBet();
		});
	},
	myBet:function(){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		
		var status=$('.conditions .status').val();
		var startDate=$('#startDate').val();
		var endDate=$('#endDate').val();
		
		var map = {};
		map['u'] =move.user.u;
		map['status'] =status;
		map['startTime'] =startDate;
		map['endTime'] =endDate;
		map['gameType'] =this.gameType;
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.betList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var data=ret.data;
				var html='';
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					var html='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var gameName=obj.gameName;
						var orderNum=obj.orderNum;
						var betType=obj.betType;
						var spName=obj.spName;
						var money=obj.money;//方案金额
						var betMoney=obj.betMoney;//认购金额
						var winCash=obj.winCash;//中奖金额
						var betPoint=obj.betPoint;//奖励积分
						var betTime=obj.betTime;//认购时间
						var num=obj.num;//总份数
						var preMoney=obj.preMoney;//每份金额
						var id=obj.jointId;
						var winResult2=obj.winResult2;
						
						html=html+'<tr><td>'+(i+1)+'</td>';
						html=html+'<td><a href="/trade/hemai/'+id+'">'+orderNum+'</a></td>';
						html=html+'<td>'+spName+'</td>';
						html=html+'<td>'+money+'</td>';
						html=html+'<td>'+preMoney+'</td>';
						if(betType=='合'){
							betType="合买";
						}else if(betType=='单'){
							betType="代购";
						}
						if(winResult2=='已中奖'){
							winResult2='<span class="red">'+winResult2+'</span>';
						}
						html=html+'<td>'+betType+'</td>';
						html=html+'<td>'+num+'</td>';
						html=html+'<td>'+betMoney+'</td>';
						html=html+'<td>'+winCash+'</td>';
						html=html+'<td>'+betTime+'</td>';
						html=html+'<td>'+winResult2+'</td></tr>';
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
					lotteryObj.bindPage();
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	}
	
	
}



function getTotalNum(redNum,blueNum){
	var m=redNum;
	var n=6;
	var mTotal=1;
	var nTotal=1;
	for(var i=m;i>=(m-n+1);i--){
		mTotal=mTotal*i;
	}
	for(var j=1;j<=n;j++){
		nTotal=nTotal*j;
	}
	return blueNum*mTotal/nTotal;
}

$(document).ready(function(e) {
	
	
});