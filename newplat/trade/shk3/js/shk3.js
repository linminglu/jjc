var lotteryObj={
	gameType:"614",
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
	currentType:8,//当前显示的玩法
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
		$(".select-mode .ball").bind("click", function(){
			$(this).toggleClass('selected');
			var data=$(this).attr('data');//取到data值，二同号单选会用到
			lotteryObj.calcZhushu(lotteryObj.currentType,data);
		});
		//清空选号
		$(".gametab").bind("click", function(){
			var name=$(this).attr('name');
			var val=$(this).val();
			var type=name.replace('g','');
			$('#play'+type+' .modebox').hide();
			$('#'+name+val).show();
		});
		//清空选号
		$(".select-mode a.clean").bind("click", function(){
			var type=$(this).attr('type');
			lotteryObj.clearSele(type);
		});
		//确认选号
		$(".select-mode a.confirm").bind("click", function(){
			var type=$(this).attr('type');
			lotteryObj.confirmSelect(type);
		});
		
		$(".select-mode .square").bind("click", function(){
			var action=$(this).attr('data');
			var type=lotteryObj.currentType;
			var selectArr=[];
			if(action=='da'){
				selectArr=['11','12','13','14','15','16','17','18'];
			}else if(action=='xiao'){
				selectArr=['03','04','05','06','07','08','09','10'];
			}else if(action=='ji'){
				selectArr=['01','03','05','07','09','11','13','15','17'];
			}else if(action=='ou'){
				selectArr=['02','04','06','08','10','12','14','16','18'];
			}else if(action=='clear'){
			}
			$(this).parent().prevAll().children('.ball').removeClass('selected');
			for (var i=0;i<selectArr.length;i++) {
				$(this).parent().prevAll().children('.ball[data='+selectArr[i]+']').addClass('selected');
			}
			lotteryObj.calcZhushu(type);
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
			var type=$(this).attr('type');
			lotteryObj.confirmInput(type);
		});
		//清空选号
		$(".input-mode a.clean").bind("click", function(){
			var type=$(this).attr('type');
			lotteryObj.cleanInput(type);
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
						$(".tab[data='8'] p:nth-child(2)").html(items[7].betRate+"-"+items[15].betRate);
						$(".tab[data='7'] p:nth-child(2)").html(items[14].betRate);
						$(".tab[data='6'] p:nth-child(2)").html(items[15].betRate);
						$(".tab[data='5'] p:nth-child(2)").html(items[16].betRate);
						$(".tab[data='4'] p:nth-child(2)").html(items[17].betRate);
						$(".tab[data='3'] p:nth-child(2)").html(items[18].betRate);
						$(".tab[data='2'] p:nth-child(2)").html(items[19].betRate);
						$(".tab[data='1'] p:nth-child(2)").html(items[20].betRate);
					}
				}
			},
			error:function (jqXHR, textStatus, errorThrown) {
				console.log("dataTimer.exec::reqSession::[ERR]"+errorThrown);
			}
		});
	},
	bindTypeTab:function(){
		//切换玩法
		$("#playtypes .tab").bind("click", function(){
			var playtype=$(this).attr('data');
			$('.select-area').hide();
			$('#playtypes .tab').removeClass('current');
			$(this).addClass('current');
			$('#play'+playtype).show();
			console.log('playtype::'+playtype);
			lotteryObj.currentType=playtype;
		});
	},
	gamePlayType:function(){
		var map = {};
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.shK3_gamePlayType,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var html='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var playName=obj.playName;
						var pointMultiple=obj.pointMultiple;
						var betRate=obj.betRate;
						var playType=obj.playType;
						var betRate=obj.betRate;
						var clazz="";
						if(playType==8){
							if(playType==lotteryObj.currentType){
								clazz="current";
							}
							$('#g'+items[0].playType+'1 .moneytext').text(items[0].betRate);
							$('#g'+items[0].playType+'1 .multipletext').text(items[0].pointMultiple);
							html='<a class="tab '+clazz+'" data="'+items[0].playType+'">'+items[0].playName+'<p>奖金</p><p>9-240元</p></a>';
						}else if(playType==0){
							if(playType==lotteryObj.currentType){
								clazz="current";
							}
							$('#g'+items[items.length-1].playType+'1 .moneytext').text(items[items.length-1].betRate);
							$('#g'+items[items.length-1].playType+'1 .multipletext').text(items[items.length-1].pointMultiple);
							html=html+'<a class="tab '+clazz+'" data="'+items[items.length-1].playType+'">'+items[items.length-1].playName+'<p>&nbsp;</p><p>简单易中</p></a>';
						}else{
							if(playType==lotteryObj.currentType){
								clazz="current";
							}
							$('#g'+playType+'1 .moneytext').text(betRate);
							$('#g'+playType+'1 .multipletext').text(pointMultiple);
							html=html+'<a class="tab '+clazz+'" data="'+playType+'">'+playName+'<p>奖金</p><p>'+betRate+'元</p></a>';
						}
					}
					$('#playtypes').html(html);
					$('.level1 .tab').css("width","111px");
					lotteryObj.bindTypeTab();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
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
	clearSele:function(group){
		$("#play"+group+" .select-mode .ball").removeClass('selected');
		$('#play'+group+' #totalNumB').text(0);
		$('#play'+group+' #totalPriceB').text(Number(0).toFixed(2));
	},
	randomBall: function(n, sp) {
		var balls = ['3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18'];
		var p = {};
		while(Object.keys(p).length < n) {
			var ball = balls[Math.floor(Math.random() * balls.length)];
			if(!p[ball]) {
				p[ball] = ball;
			}
		}
		return Object.keys(p).sort().join(sp || ',');
	},
	//二同号单选
	randomBallErTongDan: function(){
		var balls = ['11', '22', '33', '44', '55', '66'];
		var balls2 = ['1', '2', '3', '4', '5', '6'];
		var bs = balls[Math.ceil(Math.random()*5)];
		var bs2 = balls2[Math.ceil(Math.random()*5)];
		(function qc(){
			if(bs.match(bs2)){
				bs2 = balls2[Math.ceil(Math.random()*5)];
				qc()
			}
		})()
		return bs + '*' + bs2;
	},
	//三同号单选
	randomBallSanTongDanXuan: function(n, sp) {
		var balls = ['111', '222', '333', '444', '555', '666'];
		var p = {};
		while(Object.keys(p).length < n) {
			var ball = balls[Math.floor(Math.random() * balls.length)];
			if(!p[ball]) {
				p[ball] = ball;
			}
		}
		return Object.keys(p).sort().join(sp || ',');
	},
	randomBallSanBuTong: function(n, sp) {//三不同号
		var balls = ['1', '2', '3', '4', '5', '6'];
		var p = {};
		while(Object.keys(p).length < n) {
			var ball = balls[Math.floor(Math.random() * balls.length)];
			if(!p[ball]) {
				p[ball] = ball;
			}
		}
		return Object.keys(p).sort().join(sp || ',');
	},
	randomBallErTongFu: function(n, sp) {//二同号复选
		var balls = ['11*', '22*', '33*', '44*', '55*', '66*'];
		var p = {};
		while(Object.keys(p).length < n) {
			var ball = balls[Math.floor(Math.random() * balls.length)];
			if(!p[ball]) {
				p[ball] = ball;
			}
		}
		return Object.keys(p).sort().join(sp || ',');
	},
	randomDt: function(n) {
		var balls = ['3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18'];
		var d = balls[Math.floor(Math.random() * balls.length)];
		var p = {};
		while(Object.keys(p).length < n) {
			var ball = balls[Math.floor(Math.random() * balls.length)];
			if(ball != d && !p[ball]) {
				p[ball] = ball;
			}
		}
		return '胆: ' + d + '; 拖: ' + Object.keys(p).sort().join(',');
	},
	randomPlay:function(count){
		var group = this.currentType;
		var balls = ['3', '4', '5', '6', '7', '8', '9','10', '11', '12', '13', '14', '15', '16', '17', '18'];
		var text = ['大', '小', '单', '双'];
		var tab=$("input[name='g"+group+"']:checked").val();
		for(var c = 0; c < count; ++c) {
			var play = {num: 1, cost: this.unit,type:group};
			if(group == 8) {
				play.kind = tab == 1 ? '和值' : '和值';
				var rand=this.randomBall(1);
				if(rand==3){
					alert(rand);
					play.kind='三同号单选';
					play.play = '111';
				}else if(rand==18){
					play.kind='三同号单选';
					play.play = '666';
				}else{
					play.play = rand;
				}
			} else if(group == 7) {
				play.kind = tab == 1 ? '三同号通选' : '三同号通选';
				play.play = '三同号通选';
			} else if(group == 6) {
				play.kind = tab == 1 ? '三同号单选' : '三同号单选';
				play.play = this.randomBallSanTongDanXuan(1);
			} else if(group == 5) {
				play.kind = tab == 1 ? '三不同号' : '三不同号';
				play.play = this.randomBallSanBuTong(3);
			} else if(group == 4) {
				play.kind = tab == 1 ? '三连号通选' : '三连号通选';
				play.play = '三连号通选';
			} else if(group == 3) {
				play.kind = tab == 1 ? '二同号复选' : '二同号复选';
				play.play = this.randomBallErTongFu(1);
			} else if(group == 2) {
				play.kind = tab == 1 ? '二同号单选' : '二同号单选';
				play.play = this.randomBallErTongDan();
			} else if(group == 1) {
				play.kind = tab == 1 ? '二不同号' : '二不同号';
				play.play = this.randomBallSanBuTong(2);
			} else if(group == 0) {
				var typeArr = ['1', '2', '3', '4', '5', '6', '7', '8'];
				var type = typeArr[Math.ceil(Math.random()*5)];
				if(type == 8) {
					play.kind = tab == 1 ? '和值' : '和值';
					var rand=this.randomBall(1);
					if(rand==3){
						play.kind='三同号单选';
						play.play = '111';
					}else if(rand==18){
						play.kind='三同号单选';
						play.play = '666';
					}else{
						play.play = rand;
					}
				} else if(type == 7) {
					play.kind = tab == 1 ? '三同号通选' : '三同号通选';
					play.play = '三同号通选';
				} else if(type == 6) {
					play.kind = tab == 1 ? '三同号单选' : '三同号单选';
					play.play = this.randomBallSanTongDanXuan(1);
				} else if(type == 5) {
					play.kind = tab == 1 ? '三不同号' : '三不同号';
					play.play = this.randomBallSanBuTong(3);
				} else if(type == 4) {
					play.kind = tab == 1 ? '三连号通选' : '三连号通选';
					play.play = '三连号通选';
				} else if(type == 3) {
					play.kind = tab == 1 ? '二同号复选' : '二同号复选';
					play.play = this.randomBallErTongFu(1);
				} else if(type == 2) {
					play.kind = tab == 1 ? '二同号单选' : '二同号单选';
					play.play = this.randomBallErTongDan();
				} else if(type == 1) {
					play.kind = tab == 1 ? '二不同号' : '二不同号';
					play.play = this.randomBallSanBuTong(2);
				}
			} else {
				return;
			}
			var length=this.play.length;
			if(parseInt(length+1)>this.limitNum){
				mui.alert('请分多次投注',document.title, function() {});
				this.calcCost();
				return;
			}else{
				this.play.push(play);
			}
		}
		console.log(this.play);
		this.calcCost();
		this.changeHemai();
	},
	calcZhushu2:function(type,data){
		var totalNumB=0;
		if(type=='8'){
			var gesize=$('#play'+type+' .gebox .selected').size();
			totalNumB=gesize;
		}else if(type=='7'){
			var gesize=$('#play'+type+' .gebox .selected').size();
			totalNumB=gesize;
		}else if(type=='6'){
			var gesize=$('#play'+type+' .gebox .selected').size();
			totalNumB=gesize;
		}else if(type=='5'){
			var gesize=$('#play'+type+' .gebox .selected').size();
			totalNumB=getTotalNum(gesize,3);
		}else if(type=='4'){
			var gesize=$('#play'+type+' .gebox .selected').size();
			totalNumB=gesize;
		}else if(type=='3'){
			var gesize=$('#play'+type+' .gebox .selected').size();
			totalNumB=gesize;
		}else if(type=='2'){//二同号单选
			if(data){
				if(data.length==1){
					$('#play'+type+' .shibox .selected').each(function(){
						var data2=$(this).attr('data');
						if(data2.substr(0,1)==data){//如果号一样则删除选中状态
							$(this).removeClass('selected');
						}
					});
				}else if(data.length==2){
					$('#play'+type+' .gebox .selected').each(function(){
						var data2=$(this).attr('data');
						if(data.substr(0,1)==data2){//如果号一样则删除选中状态
							$(this).removeClass('selected');
						}
					});
				}
			}
			var shiArr=$('#play'+type+' .shibox .selected');
			var geArr=$('#play'+type+' .gebox .selected');
			if(shiArr.length>0&&geArr.length>0){//注数等于上下选中的相乘
				totalNumB=shiArr.length*geArr.length;
			}else{
				totalNumB=0;
			}
		}else if(type=='1'){
			var gesize=$('#play'+type+' .gebox .selected').size();
			totalNumB=getTotalNum(gesize,2);
		}else if(type=='0'){
			if(data){
				$('#play'+type+' .gebox .selected').each(function(){
					var data2=$(this).attr('data');
					if(data!=data2){//删除其他的选中状态
						$(this).removeClass('selected');
					}
				});
			}
			//因为每个号都有21注
			totalNumB=21;
		}
		return totalNumB;
	},
	calcZhushu:function(type,data){
		var totalNumB=this.calcZhushu2(type,data);
		$('#g'+type+'1 .combian-count').text(totalNumB);
		$('#g'+type+'1 .total-coast').text((Number(totalNumB)*lotteryObj.unit).toFixed(2));
	},
	calcCost:function(){
		var num = 0;
		var cost = 0.00;
		var html='';
		for(var play in this.play) {
			var play = this.play[play];
			num += play.num;
			cost += play.cost;
			html=html+'<div class="combain-cell"><span class="blue">['+play.kind+']&nbsp;</span><span>'+play.play+'</span><a class="remove">×</a></div>';
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
	verifyInput: function(play, count) {
		var balls = {};
		var playArr=play.split(',');
		for ( var i = 0; i < playArr.length; i++) {
			if(/^(0[1-9]|1[0-1])$/.test(playArr[i])) {
				balls[i] = playArr[i];
			}
		}
		console.log(Object.keys(balls).length);
		return Object.keys(balls).length == count;
	},
	checkInput:function(obj){
		var playlist = [];
		var unit = this.unit;
		var type=$(obj).attr('type');
		var inputtext=$(obj).val();
		inputtext.split("\n").forEach(function(line) {
			var play = line.trim();
			switch(type) {
				case '11':
					if(lotteryObj.verifyInput(play, 2)) {
						playlist.push({kind: '任二单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '10':
					if(lotteryObj.verifyInput(play, 3)) {
						playlist.push({kind: '任三单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '9':
					if(lotteryObj.verifyInput(play, 4)) {
						playlist.push({kind: '任四单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '8':
					if(lotteryObj.verifyInput(play, 5)) {
						playlist.push({kind: '任五单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '7':
					if(lotteryObj.verifyInput(play, 6)) {
						playlist.push({kind: '任六单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '6':
					if(lotteryObj.verifyInput(play, 7)) {
						playlist.push({kind: '任七单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '5':
					if(lotteryObj.verifyInput(play, 8)) {
						playlist.push({kind: '任八单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '4':
					if(lotteryObj.verifyInput(play, 1)) {
						playlist.push({kind: '前一单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '3':
					if(lotteryObj.verifyInput(play, 2)) {
						playlist.push({kind: '前二组选单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '2':
					if(lotteryObj.verifyInput(play, 2)) {
						playlist.push({kind: '前二直选单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '1':
					if(lotteryObj.verifyInput(play, 3)) {
						playlist.push({kind: '前三组选单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '0':
					if(lotteryObj.verifyInput(play, 3)) {
						playlist.push({kind: '前三直选单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
			}
		});
		var inputNum=playlist.length;
		this.inputNum = inputNum;
		$('#g'+type+'2 .inputNum').text(inputNum);
	},
	confirmSelect:function(type){
		var totalNum=this.calcZhushu2(type);
		if(totalNum>0){
			var baiArr={};
			var shiArr={};
			var geArr={};
			if(type=='8'||type=='7'||type=='6'||type=='5'||type=='4'||type=='3'||type=='1'||type=='0'){
				geArr=$('#play'+type+' .gebox .selected');
			}else if(type=='2'){
				shiArr=$('#play'+type+' .shibox .selected');
				geArr=$('#play'+type+' .gebox .selected');
			}
			var bai=[];
			var shi=[];
			var ge=[];
			for(var i=0;i<baiArr.length;i++) {
				bai[i]=$(baiArr[i]).text();
			}
			for(var i=0;i<shiArr.length;i++) {
				shi[i]=$(shiArr[i]).text();
			}
			for(var i=0;i<geArr.length;i++) {
				if(type==3){//二同号复选要在后面加*
					ge[i]=$(geArr[i]).attr('data');
				}else{
					ge[i]=$(geArr[i]).text();
				}
			}
			
			var play = {};
			if(type=='8'){
				var kind='';
				var play='';
				var del=0;//删除选号3或选号18，因为他们不属于玩法8
				$('#play'+type+' .gebox .selected').each(function(){
				   var ball=$(this).text();
				   if(ball==3||ball==18){//选的号如果是3或18的话就是三同号单选
					   kind='三同号单选';
					   if(ball==3){
						   del=del+1;
						   play='111';
						   var play3 = {
								type:6,//三同号单选的type是6，所以这里手动设置
								kind:kind,
								play: play,
								num: 1,
								cost: (Number(1)*lotteryObj.unit).toFixed(2)*1
							};
						   lotteryObj.play.push(play3);
						   totalNum=totalNum-1;
					   }else if(ball==18){
						   del=del+1;
						   play='666';
						   var play18 = {
								type:6,//三同号单选的type是6，所以这里手动设置
								kind:kind,
								play: play,
								num: 1,
								cost: (Number(1)*lotteryObj.unit).toFixed(2)*1
							};
						   lotteryObj.play.push(play18);
						   totalNum=totalNum-1;
					   }
				   }else{//其他的就是和值玩法
					   kind='和值';
				   }
				});
				if(del==1){//删除第一个3
					ge.splice(0,1);
				}else if(del==2){//删除第一个3和最后一个18
					ge.splice(0,1);
					ge.splice(ge.length-1,1);
				}
				play=ge.join(",");
				play = {
					type:type,
					kind:kind,
					play: play,
					num: totalNum,
					cost: (Number(totalNum)*lotteryObj.unit).toFixed(2)*1
				};
			}else if(type=='7'||type=='4'){
				var kind='';
				var play='';
				if(type=='7'){
					var kind='三同号通选';
					var play='三同号通选';
				}else if(type=='4'){
					var kind='三连号通选';
					var play='三连号通选';
				}
				play = {
					type:type,
					kind:kind,
					play:play,
					num: totalNum,
					cost: (Number(totalNum)*lotteryObj.unit).toFixed(2)*1
				};
			}else if(type=='6'||type=='5'||type=='3'||type=='1'){
				var kind='';
				if(type=='6'){
					var kind='三同号单选';
				}else if(type=='5'){
					var kind='三不同号';
				}else if(type=='3'){
					var kind='二同号复选';
				}else if(type=='1'){
					var kind='二不同号';
				}
				play = {
					type:type,
					kind:kind,
					play: ge.join(","),
					num: totalNum,
					cost: (Number(totalNum)*lotteryObj.unit).toFixed(2)*1
				};
			}else if(type=='2'){
				play = {
					type:type,
					kind:'前二直选复式',
					play:shi.join(",")+ "*" + ge.join(","),
					num: totalNum,
					cost: (Number(totalNum)*lotteryObj.unit).toFixed(2)*1
				};
			}else if(type=='0'){
				var data=ge.join(",");
				var play1={};
				var play='';
				for(var i=1;i<7;i++){//二同号单选
					if(data != i){
						play=data+''+data+'*'+i
						play1 = {
							type:2,
							kind:'二同号单选',
							play:play,
							num: 1,
							cost: (Number(1)*lotteryObj.unit).toFixed(2)*1
						};
						lotteryObj.play.push(play1);
					}
					if(data != i){
						play=i+''+i+'*'+data
						play1 = {
							type:2,
							kind:'二同号单选',
							play:play,
							num: 1,
							cost: (Number(1)*lotteryObj.unit).toFixed(2)*1
						};
						lotteryObj.play.push(play1);
					}
				}
				
				var arr=getSanBuTong(data);
				var play2={};
				for(var j=0;j<arr.length;j++){
					play2 = {
						type:5,
						kind:'三不同号',
						play:arr[j],
						num: 1,
						cost: (Number(1)*lotteryObj.unit).toFixed(2)*1
					};
					lotteryObj.play.push(play2);
				}
				
				var play3 = {
					type:6,
					kind:'三同号单选',
					play:data+''+data+''+data,
					num: 1,
					cost: (Number(1)*lotteryObj.unit).toFixed(2)*1
				};
				lotteryObj.play.push(play3);
				
			}
			//不等于21是因为：玩法0里的注数都已经push了
			if(totalNum>0 && totalNum!=21){
				lotteryObj.play.push(play);
			}
			console.log(lotteryObj.play);
			lotteryObj.calcCost();
			lotteryObj.clearSele(type);
			lotteryObj.changeHemai();
		}
	},
	confirmInput: function(type) {
		var playlist = [];
		var unit = this.unit;
		var inputtext=$('#play'+type+' .input-mode textarea').val();
		console.log(type);
		inputtext.split("\n").forEach(function(line) {
			var play = line.trim();
			switch(type) {
				case '11':
					if(lotteryObj.verifyInput(play, 2)) {
						playlist.push({kind: '任二单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '10':
					if(lotteryObj.verifyInput(play, 3)) {
						playlist.push({kind: '任三单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '9':
					if(lotteryObj.verifyInput(play, 4)) {
						playlist.push({kind: '任四单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '8':
					if(lotteryObj.verifyInput(play, 5)) {
						playlist.push({kind: '任五单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '7':
					if(lotteryObj.verifyInput(play, 6)) {
						playlist.push({kind: '任六单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '6':
					if(lotteryObj.verifyInput(play, 7)) {
						playlist.push({kind: '任七单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '5':
					if(lotteryObj.verifyInput(play, 8)) {
						playlist.push({kind: '任八单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '4':
					if(lotteryObj.verifyInput(play, 1)) {
						playlist.push({kind: '前一单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '3':
					if(lotteryObj.verifyInput(play, 2)) {
						playlist.push({kind: '前二组选单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '2':
					if(lotteryObj.verifyInput(play, 2)) {
						playlist.push({kind: '前二直选单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '1':
					if(lotteryObj.verifyInput(play, 3)) {
						playlist.push({kind: '前三组选单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
				case '0':
					if(lotteryObj.verifyInput(play, 3)) {
						playlist.push({kind: '前三直选单式',type:type, play: play, num: 1, cost: unit});
					}
					break;
			}
		});
		this.play = this.play.concat(playlist);
		console.log(playlist);
		var inputNum=playlist.length;
		$('#g'+type+'2 .inputNum').text(inputNum);
		this.cleanInput(type);
		this.calcCost();
		this.changeHemai();
	},
	cleanInput: function(type) {
		this.input = '';
		this.inputNum = 0;
		$('#g'+type+'2 textarea').val('');
		$('#g'+type+'2 .inputNum').text(this.inputNum);
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
//		$('#baodiPercentage').text(iTofixed(baodiPercentage,2));
	},
	bindHeimai:function(){
		$("#hemaiNum").keyup(function(){
//			lotteryObj.calcHeimai();
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
		lotteryObj.bindTypeTab();
		lotteryObj.gamePlayType();
		lotteryObj.open();
		if(lotteryObj.sessionNo==null){
			lotteryObj.openlist();
			lotteryObj.loadPanel();
			lotteryObj.winRanking();
		}
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
			url:move.shK3_sessionInfo,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					var data=ret.data;
					var sessionNo=data.sessionNo;
					var preSessionNo=data.preSessionNo;
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
					
					var tempsession=$('.latest .sessionNo').text();
					if(tempsession==preSessionNo){
						lotteryObj.openlist();
						lotteryObj.loadPanel();
						lotteryObj.winRanking();
					}
					lotteryObj.sessionNo=sessionNo;
					
					$('#openDate').text(openDate);
					$('#awardDate').text(awardDate);
					$('#sessionNo').text(sessionNo);
					$('.latest .sessionNo').text(preSessionNo);
					var html='';
					var length=resultItems.length;
					if(length==0){
						if(timeropenresult==null){
							timeropenresult=window.setInterval(function(){timerObj.result('timer-shk3open');},timerObj.intervalAPI);
						}
					}else{
						window.clearInterval(timeropenresult);
						timeropenresult=null;
						for (var i = 0; i < resultItems.length; i++) {
							var num=resultItems[i];
							var className='bg-red';
							html=html+'<span class="num '+className+'">'+num+'</span>';
						}
						$('.open-number').html(html);
						$('.open-number .num').removeClass('loading');
					}
					
					if(timerxiazhu==null){
						timerxiazhu=window.setInterval(function(){timerObj.init(timerObj.rest,'timer-shk3');},timerObj.interval); 
					}
					if(timeropen==null){
						timeropen=window.setInterval(function(){timerObj.open(timerObj.openrest,'timer-shk3open');},timerObj.interval);
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
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
//		console.log('倍数::'+this.multiple);
//		console.log('当前期::'+this.sessionNo);
//		console.log('是否追号::'+this.zhuiHao);
		var isPrivacy=$("input[name='access']:checked").val();
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
		var betType=$("input[name='buytype']:checked").val();
//		console.log('buytype::'+betType);
//		console.log('合买份数::'+$('#hemaiNum').val());
		
		var buyNum=$('#rengouNum').val();//认购份数
		var baodiNum=$('#baodiNum').val();//保底份数
		
//		console.log('保底份数::'+baodiNum);
//		console.log('是否保底::'+isBaodi);
		console.log('当前期号::'+this.sessionNo);
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
			var t=temp.type;
			var obj=betBallObj(bet,t);
			betBallArray.push(JSON.stringify(obj));
		}
		console.log('betBallArray::'+betBallArray);

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
			url:move.shK3_bet,
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
	loadPanel:function(type){
		var map = {};
		map['playType'] =type;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.shK3_betPanel,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
//				mask.close();
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					for (var i = 0; i < items.length; i++) {
						var redNum=items[i];
						var number=redNum.ballName;
						var trend=redNum.trend;
						$(".ball[data='"+number+"']").next('span').text(trend);
					}
					if(timerObj.rest==null){
						lotteryObj.seal();
					}
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	openlist:function(){
		var map = {};
		map['pageSize'] =10;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.shK3_latestLottery,
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
//		var mask=move.createLoading();
//		mask.show();
		$('.buy-area .bet-button').hide();
//		mask.close();
	},
	winRanking:function(){
		var map = {};
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.shK3_winRanking,
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
						var userName=obj.userName;
						var totalMoney=obj.totalMoney;
						html=html+'<tr><td>'+(i+1)+'</td>';
						html=html+'<td>'+userName+'</td>';
						html=html+'<td><span class="red">'+totalMoney+'</span>元</td>';
						html=html+'</tr>';
					}
					$('#winninglist').html(html);
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
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
/**
 * 计算注数，M选N个数
 */
function getTotalNum(m,n) { 
	var fenzi=1;
	var fenmu=1;
	for(var k=m;k>=(m-n+1);k--){
		fenzi=fenzi*k;
	}
	for(var k=1;k<=n;k++){
		fenmu=fenmu*k;
	}
	return fenzi/fenmu;
}
/**
 * 计算注数，M选N个数
 */
function getTotalNumZusan(m,n) { 
	var fenzi=1;
	var fenmu=1;
	for(var k=m;k>=(m-n+1);k--){
		fenzi=fenzi*k;
	}
	for(var k=1;k<=n;k++){
		fenmu=fenmu*k;
	}
	return (fenzi/fenmu)*2;
}

/**
 * 传一个数字，返回所有种类的三不同号
 */
function getSanBuTong(number){
	var arr = new Array();
	for (var i=1;i<=number;i++) {
		for (var j=1;j<=6;j++) {
			if (j > i) {
				for (var k=1;k<=6;k++) {
					if (k > j) {
						if (i == number || j == number || k == number) {
							arr.push(i+","+j+","+k);
						}
					}
				}
			}
		}
	}
	return arr;
}

move.shK3_gamePlayType=move.server+'/api/gfShK3_gamePlayType';
move.shK3_betPanel=move.server+'/api/gfShK3_betPanel';
move.shK3_sessionInfo=move.server+'/api/gfShK3_sessionInfo';
move.shK3_openList=move.server+'/api/gfShK3_openList';
move.shK3_latestLottery=move.server+'/api/gfShK3_latestLottery';
move.shK3_bet=move.server+'/api/gfShK3_bet';
move.shK3_winRanking=move.server+'/api/gfShK3_winRanking';
move.shK3_trend=move.server+'/api/gfShK3_trend';
$(document).ready(function(e) {
	
	
});
