var lotteryObj = {
	gameType : "3",
	defPageSize : 20,
	timer : null,
	timeout : {
		day : 0,
		hour : 0,
		minute : 0,
		second : 0
	},
	unit : 2.00,
	redNum : 0,
	blueNum : 0,
	currentType : 5,// 当前显示的玩法
	tempPlay : [],// 临时投注
	play : [],// 投注
	totalPrice : 0,// 总金额
	multiple : 1,// 倍数
	multiplePrice : 0,// 单倍金额
	totalNum : 0,// 总注数
	hemaiNum : 0,// 合买分为的份数
	sessionNo : null,
	sessionNoArr : [],
	limitNum : 400,
	zhuiHaoPrice : 0,
	zhuiHao : false,
	input : '',
	inputNum : 0,
	mask : null,
	countNum : 0,
	gameSortable : null,
	init : function() {
		mask = move.createLoading();
		mask.show();
		var tab_hash = window.location.hash;
		// alert(window.location.hash);
		var type = tab_hash.substring(1, tab_hash.length);
		lotteryObj.currentType = 256;
		if (type && type.length > 0) {

		} else {
			type = "cqssc";
			window.location = window.location + "#" + type;
		}
		if (type == "bj3") {
			lotteryObj.currentType = 256;
		} else if (type == "jsk3") {
			lotteryObj.currentType = 651;
		} else if (type == "bjk3") {
			lotteryObj.currentType = 653;
		} else if (type == "jxpick11") {
			lotteryObj.currentType = 552;
		} else if (type == "gdpick11") {
			lotteryObj.currentType = 551;
		} else if (type == "sdpick11") {
			lotteryObj.currentType = 553;
		} else if (type == "sflhc") {
			lotteryObj.currentType = 360;
		} else if (type == "marksix") {
			lotteryObj.currentType = 359;
		} else if (type == "poker") {
			lotteryObj.currentType = 358;
		} else if (type == "cqk10") {
			lotteryObj.currentType = 357;
		} else if (type == "gxk10") {
			lotteryObj.currentType = 356;
		} else if (type == "gdk10") {
			lotteryObj.currentType = 355;
		} else if (type == "bjlu28") {
			lotteryObj.currentType = 353;
		} else if (type == "xjplu28") {
			lotteryObj.currentType = 352;
		} else if (type == "five") {
			lotteryObj.currentType = 258;
		} else if (type == "bjssc") {
			lotteryObj.currentType = 255;
		} else if (type == "cqssc") {
			lotteryObj.currentType = 251;
		} else if (type == "xjssc") {
			lotteryObj.currentType = 252;
		} else if (type == "tjssc") {
			lotteryObj.currentType = 253;
		} else if (type == "jsft") {
			lotteryObj.currentType = 155;
		} else if (type == "xyft") {
			lotteryObj.currentType = 154;
		} else if (type == "sfpk102") {
			lotteryObj.currentType = 153;
		} else if (type == "sfpk10") {
			lotteryObj.currentType = 152;
		} else if (type == "bjpk10") {
			lotteryObj.currentType = 151;
		} 
		lotteryObj.load();
		mask.close();
	},
	bindTypeTab : function() {

		// 切换玩法
		$("#playtypes a").unbind("click");
		$("#playtypes a").bind("click", function() {
			var status = $(this).attr('status');
			if (status && status == "1") {
				$(".content").show();
				$(".analysis").html("");
				$(".analysis").hide();

				mask.show();
				lotteryObj.clearTimer();
				var playtype = $(this).attr('data');
				if (playtype == 2) {
					console.warn("2");
				}
				// 修改当前玩法
				lotteryObj.currentType = playtype;
				$('#playtypes a').removeClass('selected');
				$('.more-game').removeClass('selected');
				$(this).addClass('selected');

				$("#minute").html("--");
				$("#sec").html("--");
				$('#open-date #minute').html("--");
				$('#open-date #sec').html("--");

				// 切换玩法类别
				lotteryObj.changeCateTab(playtype);
				// 绑定点击事件
				lotteryObj.bindCateTab();
				// 切换下注盘
				lotteryObj.changeCate(playtype, 0);
				// 调用open方法
				lotteryObj.open();
				// 更新冷热排行
				lotteryObj.hotRanking();

				lotteryObj.updateTrendTab();
				lotteryObj.updateTrend(0);
				// lotteryObj.betRecords();

				lotteryObj.updateSelectedGameInSessionStorage(playtype);

				var shtml = "";
				shtml += "<i class=\"currentGameTxt\">更多游戏</i>";
				shtml += "&#9660;";
				$(".currentGameTxt").parent().html(shtml);
			} else {
				alert("暂未开放!");
			}

		});

		$(".gamebox a").unbind("click");
		$(".gamebox a").bind("click", function() {

			var open = $(this).attr('open');
			if (open && open == "1") {
				$(".content").show();
				$(".analysis").hide();
				$(".analysis").html("");
				mask.show();
				lotteryObj.clearTimer();
				var playtype = $(this).attr('data');
				// 修改当前玩法
				lotteryObj.currentType = playtype;
				$('#playtypes a').removeClass('selected');
				$(this).addClass('selected');
				// 切换玩法类别
				lotteryObj.changeCateTab(playtype);
				// 绑定点击事件
				lotteryObj.bindCateTab();
				// 切换下注盘
				lotteryObj.changeCate(playtype, 0);
				// 调用open方法
				lotteryObj.open();
				// 更新冷热排行
				lotteryObj.hotRanking();

				lotteryObj.updateTrendTab();
				lotteryObj.updateTrend(0);
				// lotteryObj.betRecords();

				lotteryObj.updateSelectedGameInSessionStorage(playtype);

				$(".more-game").addClass('selected');
				$(".currentGameTxt").text($(this).text());
				lotteryObj.changeCate(cate, playtype);
			} else {
				alert("暂未开放!");
			}

		});
	},
	bindCateTab:function(){
		//切换类别
		$(".cate_menu a").bind("click", function(){
			$(".content").show();
			$(".analysis").hide();
			mask.show();
			var playtype=$(this).attr('playtype');
			var cate=$(this).attr('cate');
			$('.cate_menu a').removeClass('router-link-exact-active');
			$('.cate_menu a').removeClass(' selected');
			$('.more-game').removeClass(' selected');
			$(this).addClass('router-link-exact-active');
			$(this).addClass('selected');
			lotteryObj.changeCate(cate,playtype);
			lotteryObj.open();

			if((cate==355||cate==356||cate==357)&&playtype==9){
				$(".submitbtn").unbind("click");
				$(".submitbtn").bind("click", function() {
					var buytype = $(this).val();
					lotteryObj.submit2();
				});
			}else{
				$(".submitbtn").unbind("click");
				$(".submitbtn").bind("click", function() {
					var buytype = $(this).val();
					lotteryObj.submit();
				});
			}
		});

		if($("#selectCate")&&$("#selectCate").length>0){
			$("#selectCate").change(function(){
				var option = $("#selectCate option:selected");
				var playtype=$(option).attr('playtype');
				var cate=$(option).attr('cate');
				lotteryObj.changeCate(cate,playtype);
			});
		}
	},
	bindTdInputEvent : function(el) {
		// 绑定下注盘的点击事件
		// 点击tr输入数量
		var el1;
		var el2;
		var el3;
		var data = el.attr("data");

		if (data && data.length > 0) {
			var flag = lotteryObj.checkSelectedNum(el);
			if (flag == true) {
				// td中有数据
				el.addClass("bg-yellow");
				// 取消事件
				el.unbind("click");
				el.bind("click", function() {
					lotteryObj.bindTdCancelEvent(el);
				});
			}

		} else {
			if (el.hasClass("name")) {
				// 第一个元素
				el1 = el;
				el2 = el.next();
				el3 = el2.next();
			} else if (el.hasClass("odds")) {
				// 第二个元素
				el1 = el.prev();
				el2 = el;
				el3 = el.next();
			} else if (el.hasClass("amount")) {
				// 第三个元素
				el3 = el;
				el2 = el3.prev();
				el1 = el2.prev();
			}
			if (el1 != null && el2 != null && el3 != null) {
				el1.addClass("bg-yellow");
				el2.addClass("bg-yellow");
				el3.addClass("bg-yellow");

				var amountEl = $(".bet-money").get(0);
				var amount = amountEl.value;
				if (amount.trim().length == 0) {
					// 没有输入数据

				} else {
					el3.children("input").val(amount);
				}
				// 取消事件
				el1.unbind("click");
				el1.bind("click", function() {
					lotteryObj.bindTdCancelEvent(el1);
				});
				el2.unbind("click");
				el2.bind("click", function() {
					lotteryObj.bindTdCancelEvent(el2);
				});
				el3.unbind("click");
				el3.bind("click", function() {
					lotteryObj.bindTdCancelEvent(el3);
				});
			}
		}

	},
	bindTdCancelEvent : function(el) {
		var cel1;
		var cel2;
		var cel3;
		var data = el.attr("data");
		if (data && data.length > 0) {
			el.removeClass("bg-yellow");
			el.unbind("click");
			el.bind("click", function() {
				lotteryObj.bindTdInputEvent(el);
			});
		} else {
			if (el.hasClass("name")) {
				// 第一个元素
				cel1 = el;
				cel2 = el.next();
				cel3 = cel2.next();
			} else if ($(el).hasClass("odds")) {
				// 第二个元素
				cel1 = el.prev();
				cel2 = el;
				cel3 = el.next();
			} else if ($(el).hasClass("amount")) {
				// 第三个元素
				cel3 = el;
				cel2 = cel3.prev();
				cel1 = cel2.prev();
			}
			if (cel1 != null && cel2 != null && cel3 != null) {
				cel1.removeClass("bg-yellow");
				cel2.removeClass("bg-yellow");
				cel3.removeClass("bg-yellow");

				cel3.children("input").val("");
				// 取消事件
				cel1.unbind("click");
				cel1.bind("click", function() {
					lotteryObj.bindTdInputEvent(cel1);
				});
				cel2.unbind("click");
				cel2.bind("click", function() {
					lotteryObj.bindTdInputEvent(cel2);
				});
				cel3.unbind("click");
				cel3.bind("click", function() {
					lotteryObj.bindTdInputEvent(cel3);
				});
			}
		}
	},
	checkSelectedNum : function(el) {
		var title = $(".select-area .selected");
		var cate = title.attr("cate");
		var pt = title.attr("playtype");
		var target;
		for (var i = 0; i < lotteryObj.maxBetNum.length; i++) {
			var tmp = lotteryObj.maxBetNum[i];
			if (tmp.c == cate && tmp.p == pt) {
				target = tmp;
			}
		}
		if (target) {
			var p = el.parent();
			var pp = p.parent();
			var childs = pp.find("td.bg-yellow");
			var p3 = pp.parent();
			var p4 = p3.parent();
			var p5 = p4.parent()
			var i = p4.index();
			var i2 = p5.index();

			var nums = target.n;
			if (i >= 0) {
				if (childs.length >= nums[i2 * 4 + i]) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	},
	changeCateTab : function(cate) {
		// 切换玩法选项

		$(".main-wrap").removeAttr("style");
		$(".siderbar").removeAttr("style");

		mask.show();
		var shtml = "";
		if (cate == 256) {
			// bj3
			$("#page_game_name").html("北京三分彩");
			$("#game_name").text("北京三分彩");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">1-5球</a>";
		} else if (cate == 151||cate == 152||cate == 153||cate == 154) {
			// bjpk10  sfpk10 sfpk102 xyft jsft
			// $("#page_game_name").html("北京pk10");
			// $("#game_name").text("北京pk10");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">数字盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"2\" class=\"\">冠亚组合</a>";
		} else if (cate == 352) {
			// xjplu28
			$("#page_game_name").html("新加坡幸运28");
			$("#game_name").text("新加坡幸运28");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">特码</a>";
		} else if (cate == 353) {
			// pcdd bjlu28
			$("#page_game_name").html("PC蛋蛋(北京幸运28)");
			$("#game_name").text("PC蛋蛋(北京幸运28)");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">特码</a>";
		} else if (cate == 251 || cate == 252 || cate == 253 || cate == 255 || cate==358 || cate==258) {
			// cqssc 
			// $("#page_game_name").html("重庆时时彩");
			// $("#game_name").text("重庆时时彩");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">1-5球</a> ";
		}else if (cate == 651||cate == 653) {
			// jsk3  bjk3
			$("#page_game_name").html("江苏快三");
			$("#game_name").text("江苏快三");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">两连</a> ";
		} else if (cate == 551) {
			// gd115
			$("#page_game_name").html("广东11选5");
			$("#game_name").text("广东11选5");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">1-5球</a>";

		} else if (cate == 553) {
			// sd115
			$("#page_game_name").html("山东11选5");
			$("#game_name").text("山东11选5");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">1-5球</a>";

		} else if (cate == 356) {
			// gxk10
			$("#page_game_name").html("广西快乐十分");
			$("#game_name").text("广西快乐十分");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">1球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"2\" class=\"\">2球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"3\" class=\"\">3球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"4\" class=\"\">4球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"5\" class=\"\">5球</a>";
			// shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
			// 		+ "\" playType=\"9\" class=\"\">连码</a>";
		} else if (cate == 359||cate == 360) {
			// liuhecai
			// $("#page_game_name").html("六合彩");
			// $("#game_name").text("六合彩");
			shtml += "<label>选择玩法：</label>"
			shtml += "<select id=\"selectCate\" style=\"width:120px;height:20px;border: #b9c2cb 1px solid !important;border-radius: 0px;position:relative;top:1px\">";
			shtml += "<option  cate=\"" + cate
					+ "\" playType=\"0\">特码A</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"1\" >特码B</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"2\" >正码</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"3\" >正1特</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"4\" >正2特</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"5\" >正3特</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"6\" >正4特</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"7\" >正5特</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"8\" >正6特</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"9\" >正码1-6</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"10\" >过关</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"11\" >二全中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"12\" >二中特</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"13\" >特串</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"14\" >三全中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"15\" >三中二</option>";
			// shtml += "<option cate=\"" + cate
			// 		+ "\" playType=\"16\" >四全中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"17\" >半波</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"18\" >一肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"19\" >尾数</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"20\" >特码生肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"21\" >二肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"22\" >三肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"23\" >四肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"24\" >五肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"25\" >六肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"26\" >七肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"27\" >八肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"28\" >九肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"29\" >十肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"30\" >十一肖</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"31\" >二肖连中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"32\" >三肖连中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"33\" >四肖连中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"34\" >五肖连中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"35\" >二肖连不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"36\" >三肖连不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"37\" >四肖连不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"38\" >二尾连中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"39\" >三尾连中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"40\" >四尾连中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"41\" >二尾连不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"42\" >三尾连不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"43\" >四尾连不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"44\" >五不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"45\" >六不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"46\" >七不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"47\" >八不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"48\" >九不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"49\" >十不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"50\" >十一不中</option>";
			shtml += "<option cate=\"" + cate
					+ "\" playType=\"51\" >十二不中</option>";
			shtml += "</select>";

		} else if (cate == 355 || cate==357) {
			// gdk10 cqk10
			// $("#page_game_name").html("广东快乐十分");
			// $("#game_name").text("广东快乐十分");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">1球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"2\" class=\"\">2球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"3\" class=\"\">3球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"4\" class=\"\">4球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"5\" class=\"\">5球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"6\" class=\"\">6球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"7\" class=\"\">7球</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"8\" class=\"\">8球</a>";
			// shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
			// 		+ "\" playType=\"9\" class=\"\">连码</a>";
		} else if (cate == 155) {
			// jsft
			$("#page_game_name").html("极速飞艇");
			$("#game_name").text("极速飞艇");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">数字盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"2\" class=\"\">冠亚组合</a>";
		} else if (cate == 152) {
			// sfpk10
			$("#page_game_name").html("极速赛车");
			$("#game_name").text("极速赛车");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">数字盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"2\" class=\"\">冠亚组合</a>";
		} else if (cate == 358) {
			// poker
			$("#page_game_name").html("快乐扑克3");
			$("#game_name").text("快乐扑克3");
			shtml += "<a href=\"javascript:void(0)\" cate=\""
					+ cate
					+ "\" playType=\"0\" class=\"router-link-exact-active selected\">两面盘</a>|";
			shtml += "<a href=\"javascript:void(0)\" cate=\"" + cate
					+ "\" playType=\"1\" class=\"\">特码</a>";
		}
		$(".cate_menu").html(shtml);
	},
	changeCate : function(cate, playtype) {
		// 切换玩法下注盘
		var shtml = "";
		if (cate == 256) {
			// bj3
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1-5球");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.bj3_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							shtml += "<table class=\"Ball_List\">";
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0) {
								// 顶部龙虎
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i == 4) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										if (i == 3) {
											shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
										} else {
											shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										}
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									shtml += "</tr>";
									shtml += "</tbody>";
								}
								lhhtml = shtml;
							}
							shtml += "</table>";

							// 1-5球的大小单双
							shtml += "<table class=\"Ball_List\"><tbody><tr>";

							var i = 0;
							if (playtype == 0) {
								i = 1;
							}
							for (i; i < items.length; i++) {
								var tmpItem = items[i];
								var ballprehtml = "<table class=\"u-table\"><thead><tr><th>号</th><th>赔率</th><th>金额</th><th>号</th><th>赔率</th>";
								ballprehtml += "<th>金额</th><th>号</th><th>赔率</th><th>金额</th><th>号</th><th>赔率</th>";
								ballprehtml += "<th>金额</th><th>号</th><th>赔率</th><th>金额</th></tr></thead>";
								var ballafthtml = "<tr>";

								shtml += "<td>";
								shtml += "<table id=\"table-ball-" + i
										+ "\" class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th colspan=\"3\" class=\"td_caption_1 \">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "</thead>";
								shtml += "<tbody>";
								var tmpItemOptions = tmpItem.optionItems;
								for (var j = 0; j < tmpItemOptions.length; j++) {
									var tmp = tmpItemOptions[j];
									shtml += "<tr class=\"Ball_tr_H\">";
									if (playtype == 1) {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1\"><span class=\" CQSSC1 No_"
												+ (j + 1)
												+ " ball_block\">&nbsp;</span></td>";
									} else {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
												+ tmp.title + "</td>";
									}
									shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
									shtml += "</tr>";

									ballafthtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
											+ tmp.title + "</td>";
									ballafthtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									ballafthtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
									ballafthtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									ballafthtml += "</td>";
								}
								shtml += "</tbody>";
								shtml += "</table>";
								shtml += "</td>";

								ballafthtml += "</tr>";
								ballPreHtmlsArr[i - 1] = ballprehtml;
								ballAftHtmlsArr[i - 1] = ballafthtml;
							}
							shtml += "</tr></tbody></table>";
							$(".bet-panel").html(shtml);
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						} else {
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 151) {
			// bjpk10
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("数字盘");
			} else if (playtype == 2) {
				// 冠亚组合
				$("#page_name").html("冠亚组合");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.bjPk10_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var result = ret.data;
							var items = result.betItems;
							var html = '';
							var htmlkj = '';
							var shtml = "";
							var weight = 5;
							if (playtype == 2) {
								// 冠亚组合
								weight = 4;
							}

							for (var i = 0; i < items.length; i++) {
								var cell = items[i];
								var tmpItem = cell;
								if (playtype == 0 || playtype == 1) {
									// 两面盘 或者 单号1~10
									if (i % 5 == 0) {
										shtml += "<table class=\"Ball_List\"><tbody>";
										shtml += "<tr>";
									}
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "<tr>";
									if (playtype == 1) {
										shtml += "<th class=\"td_caption_1 F_bold\">号码</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">赔率</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">金额</th>";
									}
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										if (playtype == 0) {
											// 两面盘
											shtml += "<td  class=\" name Jut_caption_1\"  width=\"38\">"
													+ tmp.title + "</td>";
										} else if (playtype == 1) {
											// shtml += "<td
											// class=\"name\"><span
											// class=\"ball
											// c-n"+(j+1)+"\"></td>";
											// shtml += "<td
											// class=\"name\"><span
											// class=\"Jut_caption_1
											// No_"+(j+1)+"\">&nbsp;</span></td>";
											shtml += "<td  width=\"38\" class=\"name Jut_caption_1 BJPK1 No_"
													+ (j + 1) + "\"></td>";
										}
										shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
									if (i % 5 == 4) {
										shtml += "</tr></tbody></table>";
									}
								} else if (playtype == 2) {
									shtml += "<table class=\"Ball_List\"><tbody>";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"12\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle
											+ "（冠军车号＋亚军车号 ＝ 和）</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										if (j > 0 && j % weight == 0) {
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										if (playtype == 2) {
											// 冠亚组合
											shtml += "<td width=\"49\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"62\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"90\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										} else {
											shtml += "<td width=\"38\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										}

									}
									// if (j > 0 && j % weight > 0) {
									// 	for (var m = 0; m < weight - j
									// 			% weight; m++) {
									// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// 	}
									// }
									shtml += "</tr>";
									shtml += "</tbody></table>";
								}
							}
							$(".bet-panel").html(shtml);
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});

						} else {
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 154) {
			// xyft
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("数字盘");
			} else if (playtype == 2) {
				// 冠亚组合
				$("#page_name").html("冠亚组合");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.xyft_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var result = ret.data;
							var items = result.betItems;
							var html = '';
							var htmlkj = '';
							var shtml = "";
							var weight = 5;
							if (playtype == 2) {
								// 冠亚组合
								weight = 4;
							}

							for (var i = 0; i < items.length; i++) {
								var cell = items[i];
								var tmpItem = cell;
								if (playtype == 0 || playtype == 1) {
									// 两面盘 或者 单号1~10
									if (i % 5 == 0) {
										shtml += "<table class=\"Ball_List\"><tbody>";
										shtml += "<tr>";
									}
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "<tr>";
									if (playtype == 1) {
										shtml += "<th class=\"td_caption_1 F_bold\">号码</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">赔率</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">金额</th>";
									}
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										if (playtype == 0) {
											// 两面盘
											shtml += "<td  class=\" name Jut_caption_1\"  width=\"38\">"
													+ tmp.title + "</td>";
										} else if (playtype == 1) {
											// shtml += "<td
											// class=\"name\"><span
											// class=\"ball
											// c-n"+(j+1)+"\"></td>";
											// shtml += "<td
											// class=\"name\"><span
											// class=\"Jut_caption_1
											// No_"+(j+1)+"\">&nbsp;</span></td>";
											shtml += "<td  width=\"38\" class=\"name Jut_caption_1 BJPK1 No_"
													+ (j + 1) + "\"></td>";
										}
										shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
									if (i % 5 == 4) {
										shtml += "</tr></tbody></table>";
									}
								} else if (playtype == 2) {
									shtml += "<table class=\"Ball_List\"><tbody>";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"12\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle
											+ "（冠军车号＋亚军车号 ＝ 和）</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										if (j > 0 && j % weight == 0) {
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										if (playtype == 2) {
											// 冠亚组合
											shtml += "<td width=\"49\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"62\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"90\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										} else {
											shtml += "<td width=\"38\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										}

									}
									// if (j > 0 && j % weight > 0) {
									// 	for (var m = 0; m < weight - j
									// 			% weight; m++) {
									// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// 	}
									// }
									shtml += "</tr>";
									shtml += "</tbody></table>";
								}
							}
							$(".bet-panel").html(shtml);
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});

						} else {
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 153) {
			// sfpk102
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("数字盘");
			} else if (playtype == 2) {
				// 冠亚组合
				$("#page_name").html("冠亚组合");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.sfPk102_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var result = ret.data;
							var items = result.betItems;
							var html = '';
							var htmlkj = '';
							var shtml = "";
							var weight = 5;
							if (playtype == 2) {
								// 冠亚组合
								weight = 4;
							}

							for (var i = 0; i < items.length; i++) {
								var cell = items[i];
								var tmpItem = cell;
								if (playtype == 0 || playtype == 1) {
									// 两面盘 或者 单号1~10
									if (i % 5 == 0) {
										shtml += "<table class=\"Ball_List\"><tbody>";
										shtml += "<tr>";
									}
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "<tr>";
									if (playtype == 1) {
										shtml += "<th class=\"td_caption_1 F_bold\">号码</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">赔率</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">金额</th>";
									}
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										if (playtype == 0) {
											// 两面盘
											shtml += "<td  class=\" name Jut_caption_1\"  width=\"38\">"
													+ tmp.title + "</td>";
										} else if (playtype == 1) {
											// shtml += "<td
											// class=\"name\"><span
											// class=\"ball
											// c-n"+(j+1)+"\"></td>";
											// shtml += "<td
											// class=\"name\"><span
											// class=\"Jut_caption_1
											// No_"+(j+1)+"\">&nbsp;</span></td>";
											shtml += "<td  width=\"38\" class=\"name Jut_caption_1 BJPK1 No_"
													+ (j + 1) + "\"></td>";
										}
										shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
									if (i % 5 == 4) {
										shtml += "</tr></tbody></table>";
									}
								} else if (playtype == 2) {
									shtml += "<table class=\"Ball_List\"><tbody>";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"12\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle
											+ "（冠军车号＋亚军车号 ＝ 和）</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										if (j > 0 && j % weight == 0) {
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										if (playtype == 2) {
											// 冠亚组合
											shtml += "<td width=\"49\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"62\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"90\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										} else {
											shtml += "<td width=\"38\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										}

									}
									// if (j > 0 && j % weight > 0) {
									// 	for (var m = 0; m < weight - j
									// 			% weight; m++) {
									// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// 	}
									// }
									shtml += "</tr>";
									shtml += "</tbody></table>";
								}
							}
							$(".bet-panel").html(shtml);
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});

						} else {
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 352) {
			// xjpLu28
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("特码");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);

			$.ajax({
					type : "POST",
					url : move.xjpLu28_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';

							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0) {
								// 双面
								shtml += "<table class=\"Ball_List\">";
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i == 4 || i == 8) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										shtml += "<input type=\"text\" class=\"inp1\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									shtml += "</tr>";
									shtml += "</tbody>";
								}
								lhhtml = shtml;
								shtml += "</table>";

								shtml += "<table class=\"Ball_List\">";
								firstItem = items[1];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];

										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									shtml += "</tr>";
									shtml += "</tbody>";
								}
								lhhtml = shtml;
								shtml += "</table>";
							} else if (playtype == 1) {
								// 特码
								shtml += "<table class=\"Ball_List\">";
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr class=\"td_caption_1 F_bold\"><th colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i != 0 && i % 4 == 0) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										shtml += "<input type=\"text\" class=\"inp1\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									shtml += "</tr>";
									shtml += "</tbody>";
								}
								lhhtml = shtml;
								shtml += "</table>";
							}

							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});

		} else if (cate == 353) {
			// bjLu28 / pcdd
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("特码");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);

			$.ajax({
					type : "POST",
					url : move.bjLu28_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0) {
								// 双面
								shtml += "<table class=\"Ball_List\">";
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr ><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i == 4 || i == 8) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										shtml += "<input type=\"text\" class=\"inp1\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									shtml += "</tr>";
									shtml += "</tbody>";
								}
								lhhtml = shtml;
								shtml += "</table>";

								shtml += "<table class=\"Ball_List\">";
								firstItem = items[1];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];

										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										shtml += "<input type=\"text\" class=\"inp1\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}

									shtml += "</tr>";
									shtml += "</tbody>";
								}
								lhhtml = shtml;
								shtml += "</table>";
							} else if (playtype == 1) {
								// 特码
								shtml += "<table class=\"Ball_List\">";
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i != 0 && i % 4 == 0) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\"><span class=\" PCDD1 No_"
												+ (i + 1)
												+ " ball_block\">&nbsp;</span></td>";
										// shtml += "<td width=\"49\"
										// class=\"name
										// Jut_caption_1\">"+tmpItem.title+"</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										shtml += "<input type=\"text\" class=\"inp1\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									shtml += "</tr>";
									shtml += "</tbody>";
								}
								lhhtml = shtml;
								shtml += "</table>";
							}

							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 251) {
			// cqssc
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1-5球");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.cqSsc_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							shtml += "<table class=\"Ball_List\">";
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0) {
								// 顶部龙虎
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i == 4) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										if (i == 3) {
											shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
										} else {
											shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										}
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									shtml += "</tr>";
									shtml += "</tbody>";
								}
							}
							shtml += "</table>";

							// 1-5球的大小单双
							shtml += "<table class=\"Ball_List\"><tbody><tr>";

							var i = 0;
							var maxNum = items.length;
							if (playtype == 0) {
								i = 1;
								maxNum = items.length - 2;
							}
							// 这里取出 1-5球竖着排列
							for (i; i < maxNum; i++) {
								var tmpItem = items[i];

								shtml += "<td>";
								shtml += "<table id=\"table-ball-" + i
										+ "\" class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th colspan=\"3\" class=\"td_caption_1\">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "</thead>";
								shtml += "<tbody>";
								var tmpItemOptions = tmpItem.optionItems;
								for (var j = 0; j < tmpItemOptions.length; j++) {
									var tmp = tmpItemOptions[j];
									shtml += "<tr class=\"Ball_tr_H\">";
									if (playtype == 1) {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1\"><span class=\" CQSSC1 No_"
												+ (j + 1)
												+ " ball_block\">&nbsp;</span></td>";
										// shtml += "<td width=\"38\"
										// class=\"name Jut_caption_1 CQSSC1
										// No_"+(j+1)+"\">&nbsp;</td>";
									} else {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
												+ tmp.title + "</td>";
									}
									shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
									shtml += "</tr>";
								}
								shtml += "</tbody>";
								shtml += "</table>";
								shtml += "</td>";
							}
							// 这里取出 斗牛、梭哈横着排列
							var weight = 5;
							for (i; i < items.length; i++) {
								var firstItem = items[i];
								// 标题
								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"15\">"
										+ firstItem.optionTitle
										+ "</th></tr></thead>";
								// 表格数据
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								var j = 0;
								for (var j = 0; j < firstItem.optionItems.length; j++) {
									var tmpItem = firstItem.optionItems[j];
									if (j > 0 && j % weight == 0) {
										// 换行
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
											+ tmpItem.title + "</td>";
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmpItem.rate + "</span></td>";
									if (j == 3) {
										shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
									} else {
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
									}
									shtml += "<input type=\"text\" title=\""
											+ tmpItem.bettitle
											+ "\" bstitle=\""
											+ tmpItem.title
											+ "\"  data=\""
											+ tmpItem.optionId
											+ "\" bs=\""
											+ tmpItem.rate
											+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
									shtml += "</td>";
								}
								// if (j > 0 && j % weight > 0) {
								// 	for (var m = 0; m < weight - j % weight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
								shtml += "</tbody>";
								shtml += "</table>";
							}

							shtml += "</tr></tbody></table>";
							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 252) {
			// xjssc
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1-5球");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.xjSsc_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							shtml += "<table class=\"Ball_List\">";
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0) {
								// 顶部龙虎
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i == 4) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										if (i == 3) {
											shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
										} else {
											shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										}
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									shtml += "</tr>";
									shtml += "</tbody>";
								}
							}
							shtml += "</table>";

							// 1-5球的大小单双
							shtml += "<table class=\"Ball_List\"><tbody><tr>";

							var i = 0;
							var maxNum = items.length;
							if (playtype == 0) {
								i = 1;
								maxNum = items.length - 2;
							}
							// 这里取出 1-5球竖着排列
							for (i; i < maxNum; i++) {
								var tmpItem = items[i];

								shtml += "<td>";
								shtml += "<table id=\"table-ball-" + i
										+ "\" class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th colspan=\"3\" class=\"td_caption_1\">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "</thead>";
								shtml += "<tbody>";
								var tmpItemOptions = tmpItem.optionItems;
								for (var j = 0; j < tmpItemOptions.length; j++) {
									var tmp = tmpItemOptions[j];
									shtml += "<tr class=\"Ball_tr_H\">";
									if (playtype == 1) {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1\"><span class=\" CQSSC1 No_"
												+ (j + 1)
												+ " ball_block\">&nbsp;</span></td>";
										// shtml += "<td width=\"38\"
										// class=\"name Jut_caption_1 CQSSC1
										// No_"+(j+1)+"\">&nbsp;</td>";
									} else {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
												+ tmp.title + "</td>";
									}
									shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
									shtml += "</tr>";
								}
								shtml += "</tbody>";
								shtml += "</table>";
								shtml += "</td>";
							}
							// 这里取出 斗牛、梭哈横着排列
							var weight = 5;
							for (i; i < items.length; i++) {
								var firstItem = items[i];
								// 标题
								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"15\">"
										+ firstItem.optionTitle
										+ "</th></tr></thead>";
								// 表格数据
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								var j = 0;
								for (var j = 0; j < firstItem.optionItems.length; j++) {
									var tmpItem = firstItem.optionItems[j];
									if (j > 0 && j % weight == 0) {
										// 换行
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
											+ tmpItem.title + "</td>";
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmpItem.rate + "</span></td>";
									if (j == 3) {
										shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
									} else {
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
									}
									shtml += "<input type=\"text\" title=\""
											+ tmpItem.bettitle
											+ "\" bstitle=\""
											+ tmpItem.title
											+ "\"  data=\""
											+ tmpItem.optionId
											+ "\" bs=\""
											+ tmpItem.rate
											+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
									shtml += "</td>";
								}
								// if (j > 0 && j % weight > 0) {
								// 	for (var m = 0; m < weight - j % weight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
								shtml += "</tbody>";
								shtml += "</table>";
							}

							shtml += "</tr></tbody></table>";
							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 253) {
			// tjssc
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1-5球");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.tjSsc_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							shtml += "<table class=\"Ball_List\">";
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0) {
								// 顶部龙虎
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i == 4) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										if (i == 3) {
											shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
										} else {
											shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										}
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									shtml += "</tr>";
									shtml += "</tbody>";
								}
							}
							shtml += "</table>";

							// 1-5球的大小单双
							shtml += "<table class=\"Ball_List\"><tbody><tr>";

							var i = 0;
							var maxNum = items.length;
							if (playtype == 0) {
								i = 1;
								maxNum = items.length - 2;
							}
							// 这里取出 1-5球竖着排列
							for (i; i < maxNum; i++) {
								var tmpItem = items[i];

								shtml += "<td>";
								shtml += "<table id=\"table-ball-" + i
										+ "\" class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th colspan=\"3\" class=\"td_caption_1\">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "</thead>";
								shtml += "<tbody>";
								var tmpItemOptions = tmpItem.optionItems;
								for (var j = 0; j < tmpItemOptions.length; j++) {
									var tmp = tmpItemOptions[j];
									shtml += "<tr class=\"Ball_tr_H\">";
									if (playtype == 1) {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1\"><span class=\" CQSSC1 No_"
												+ (j + 1)
												+ " ball_block\">&nbsp;</span></td>";
										// shtml += "<td width=\"38\"
										// class=\"name Jut_caption_1 CQSSC1
										// No_"+(j+1)+"\">&nbsp;</td>";
									} else {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
												+ tmp.title + "</td>";
									}
									shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
									shtml += "</tr>";
								}
								shtml += "</tbody>";
								shtml += "</table>";
								shtml += "</td>";
							}
							// 这里取出 斗牛、梭哈横着排列
							var weight = 5;
							for (i; i < items.length; i++) {
								var firstItem = items[i];
								// 标题
								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"15\">"
										+ firstItem.optionTitle
										+ "</th></tr></thead>";
								// 表格数据
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								var j = 0;
								for (var j = 0; j < firstItem.optionItems.length; j++) {
									var tmpItem = firstItem.optionItems[j];
									if (j > 0 && j % weight == 0) {
										// 换行
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
											+ tmpItem.title + "</td>";
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmpItem.rate + "</span></td>";
									if (j == 3) {
										shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
									} else {
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
									}
									shtml += "<input type=\"text\" title=\""
											+ tmpItem.bettitle
											+ "\" bstitle=\""
											+ tmpItem.title
											+ "\"  data=\""
											+ tmpItem.optionId
											+ "\" bs=\""
											+ tmpItem.rate
											+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
									shtml += "</td>";
								}
								// if (j > 0 && j % weight > 0) {
								// 	for (var m = 0; m < weight - j % weight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
								shtml += "</tbody>";
								shtml += "</table>";
							}

							shtml += "</tr></tbody></table>";
							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 258) {
			// five
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1-5球");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.five_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							shtml += "<table class=\"Ball_List\">";
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0) {
								// 顶部龙虎
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i == 4) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										if (i == 3) {
											shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
										} else {
											shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										}
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									shtml += "</tr>";
									shtml += "</tbody>";
								}
							}
							shtml += "</table>";

							// 1-5球的大小单双
							shtml += "<table class=\"Ball_List\"><tbody><tr>";

							var i = 0;
							var maxNum = items.length;
							if (playtype == 0) {
								i = 1;
								// five没有斗牛玩法
								// maxNum = items.length - 2;
							}
							// 这里取出 1-5球竖着排列
							for (i; i < maxNum; i++) {
								var tmpItem = items[i];

								shtml += "<td>";
								shtml += "<table id=\"table-ball-" + i
										+ "\" class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th colspan=\"3\" class=\"td_caption_1 \">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "</thead>";
								shtml += "<tbody>";
								var tmpItemOptions = tmpItem.optionItems;
								for (var j = 0; j < tmpItemOptions.length; j++) {
									var tmp = tmpItemOptions[j];
									shtml += "<tr class=\"Ball_tr_H\">";
									if (playtype == 1) {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1\"><span class=\" CQSSC1 No_"
												+ (j + 1)
												+ " ball_block\">&nbsp;</span></td>";
										// shtml += "<td width=\"38\"
										// class=\"name Jut_caption_1 CQSSC1
										// No_"+(j+1)+"\">&nbsp;</td>";
									} else {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
												+ tmp.title + "</td>";
									}
									shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
									shtml += "</tr>";
								}
								shtml += "</tbody>";
								shtml += "</table>";
								shtml += "</td>";
							}
							
							shtml += "</tr></tbody></table>";
							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 255) {
			// bjssc
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1-5球");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.bjssc_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							shtml += "<table class=\"Ball_List\">";
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0) {
								// 顶部龙虎
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i == 4) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
												+ tmpItem.rate
												+ "</span></td>";
										if (i == 3) {
											shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
										} else {
											shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
										}
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}
									// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									shtml += "</tr>";
									shtml += "</tbody>";
								}
							}
							shtml += "</table>";

							// 1-5球的大小单双
							shtml += "<table class=\"Ball_List\"><tbody><tr>";

							var i = 0;
							var maxNum = items.length;
							if (playtype == 0) {
								i = 1;
								// bjssc没有斗牛玩法
								// maxNum = items.length - 2;
							}
							// 这里取出 1-5球竖着排列
							for (i; i < maxNum; i++) {
								var tmpItem = items[i];

								shtml += "<td>";
								shtml += "<table id=\"table-ball-" + i
										+ "\" class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th colspan=\"3\" class=\"td_caption_1 \">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "</thead>";
								shtml += "<tbody>";
								var tmpItemOptions = tmpItem.optionItems;
								for (var j = 0; j < tmpItemOptions.length; j++) {
									var tmp = tmpItemOptions[j];
									shtml += "<tr class=\"Ball_tr_H\">";
									if (playtype == 1) {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1\"><span class=\" CQSSC1 No_"
												+ (j + 1)
												+ " ball_block\">&nbsp;</span></td>";
										// shtml += "<td width=\"38\"
										// class=\"name Jut_caption_1 CQSSC1
										// No_"+(j+1)+"\">&nbsp;</td>";
									} else {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
												+ tmp.title + "</td>";
									}
									shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
									shtml += "</tr>";
								}
								shtml += "</tbody>";
								shtml += "</table>";
								shtml += "</td>";
							}
							

							shtml += "</tr></tbody></table>";
							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 651) {
			// jsk3
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("两连");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.jiangsuk3_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							var weight = 4;
							if (playtype == 1) {
								weight = 3;
							}
							if (playtype == 1) {
								// 两连
								var tmpItem = items[0];

								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th class=\"td_caption_1 F_bold\" colspan=\"12\">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								for (var j = 0; j < tmpItem.optionItems.length; j++) {
									var tmpweight = 4;
									var tmp = tmpItem.optionItems[j];
									if (j > 0 && j % tmpweight == 0) {
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									var nums = tmp.title.split("、");
									var diceHtml = "";
									for (var k = 0; k < nums.length; k++) {
										diceHtml += "<span >" + nums[k]
												+ "</label>";
									}
									shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
											+ diceHtml + "</td>";
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
								}

								// if (j > 0 && j % tmpweight > 0) {
								// 	for (var m = 0; m < tmpweight - j
								// 			% tmpweight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
							}
							var i = 0;
							if (playtype == 1) {
								i = 1;
							}
							for (i; i < items.length; i++) {
								var tmpItem = items[i];
								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th class=\"td_caption_1 F_bold\" colspan=\"12\">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								for (var j = 0; j < tmpItem.optionItems.length; j++) {
									var tmp = tmpItem.optionItems[j];
									if (j > 0 && j % weight == 0) {
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									if (playtype == 1) {
										var nums = tmp.title.split("、");
										var diceHtml = "";
										for (var k = 0; k < nums.length; k++) {

											if (!isNaN(parseInt(nums[k]))) {
												// console.log(nums[k]+"___"+(parseInt(nums[k])!=
												// NaN));
												diceHtml += "<label style=\"display:inline-block\" class=\"JSK32 No_"
														+ nums[k]
														+ " \"></label>";
											} else {
												diceHtml += "<span >"
														+ nums[k]
														+ "</label>";
											}
										}
										shtml += "<td width=\"100\" class=\"name Jut_caption_1\">"
												+ diceHtml + "</td>";
									} else {
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmp.title + "</td>";
									}
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"106\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
								}

								// if (j > 0 && j % weight > 0) {
								// 	for (var m = 0; m < weight - j % weight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
							}
							shtml += "</tbody></table>";

							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		}else if (cate == 653) {
			// bjk3
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("两连");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.bjk3_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							var weight = 4;
							if (playtype == 1) {
								weight = 3;
							}
							if (playtype == 1) {
								// 两连
								var tmpItem = items[0];

								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th class=\"td_caption_1 F_bold\" colspan=\"12\">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								for (var j = 0; j < tmpItem.optionItems.length; j++) {
									var tmpweight = 4;
									var tmp = tmpItem.optionItems[j];
									if (j > 0 && j % tmpweight == 0) {
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									var nums = tmp.title.split("、");
									var diceHtml = "";
									for (var k = 0; k < nums.length; k++) {
										diceHtml += "<span >" + nums[k]
												+ "</label>";
									}
									shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
											+ diceHtml + "</td>";
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
								}

								// if (j > 0 && j % tmpweight > 0) {
								// 	for (var m = 0; m < tmpweight - j
								// 			% tmpweight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
							}
							var i = 0;
							if (playtype == 1) {
								i = 1;
							}
							for (i; i < items.length; i++) {
								var tmpItem = items[i];
								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th class=\"td_caption_1 F_bold\" colspan=\"12\">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								for (var j = 0; j < tmpItem.optionItems.length; j++) {
									var tmp = tmpItem.optionItems[j];
									if (j > 0 && j % weight == 0) {
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									if (playtype == 1) {
										var nums = tmp.title.split("、");
										var diceHtml = "";
										for (var k = 0; k < nums.length; k++) {

											if (!isNaN(parseInt(nums[k]))) {
												// console.log(nums[k]+"___"+(parseInt(nums[k])!=
												// NaN));
												diceHtml += "<label style=\"display:inline-block\" class=\"JSK32 No_"
														+ nums[k]
														+ " \"></label>";
											} else {
												diceHtml += "<span >"
														+ nums[k]
														+ "</label>";
											}
										}
										shtml += "<td width=\"100\" class=\"name Jut_caption_1\">"
												+ diceHtml + "</td>";
									} else {
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmp.title + "</td>";
									}
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"106\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
								}

								// if (j > 0 && j % weight > 0) {
								// 	for (var m = 0; m < weight - j % weight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
							}
							shtml += "</tbody></table>";

							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 661) {
			// jxk3
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("两连");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.jxk3_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							var weight = 4;
							if (playtype == 1) {
								weight = 3;
							}
							if (playtype == 1) {
								// 两连
								var tmpItem = items[0];

								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th class=\"td_caption_1 F_bold\" colspan=\"12\">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								for (var j = 0; j < tmpItem.optionItems.length; j++) {
									var tmpweight = 4;
									var tmp = tmpItem.optionItems[j];
									if (j > 0 && j % tmpweight == 0) {
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									var nums = tmp.title.split("、");
									var diceHtml = "";
									for (var k = 0; k < nums.length; k++) {
										diceHtml += "<span >" + nums[k]
												+ "</label>";
									}
									shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
											+ diceHtml + "</td>";
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
								}

								// if (j > 0 && j % tmpweight > 0) {
								// 	for (var m = 0; m < tmpweight - j
								// 			% tmpweight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
							}
							var i = 0;
							if (playtype == 1) {
								i = 1;
							}
							for (i; i < items.length; i++) {
								var tmpItem = items[i];
								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr>";
								shtml += "<th class=\"td_caption_1 F_bold\" colspan=\"12\">"
										+ tmpItem.optionTitle + "</th>";
								shtml += "</tr>";
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								for (var j = 0; j < tmpItem.optionItems.length; j++) {
									var tmp = tmpItem.optionItems[j];
									if (j > 0 && j % weight == 0) {
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									if (playtype == 1) {
										var nums = tmp.title.split("、");
										var diceHtml = "";
										for (var k = 0; k < nums.length; k++) {

											if (!isNaN(parseInt(nums[k]))) {
												// console.log(nums[k]+"___"+(parseInt(nums[k])!=
												// NaN));
												diceHtml += "<label style=\"display:inline-block\" class=\"JSK32 No_"
														+ nums[k]
														+ " \"></label>";
											} else {
												diceHtml += "<span >"
														+ nums[k]
														+ "</label>";
											}
										}
										shtml += "<td width=\"100\" class=\"name Jut_caption_1\">"
												+ diceHtml + "</td>";
									} else {
										shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
												+ tmp.title + "</td>";
									}
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmp.rate + "</span></td>";
									shtml += "<td width=\"106\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
											+ tmp.optionId
											+ "\" bs=\""
											+ tmp.rate
											+ "\" bstitle=\""
											+ tmp.title
											+ "\" title=\""
											+ tmp.bettitle + "\">";
									shtml += "</td>";
								}

								// if (j > 0 && j % weight > 0) {
								// 	for (var m = 0; m < weight - j % weight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
							}
							shtml += "</tbody></table>";

							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		}else if (cate == 551) {
			// gd115/gdpick11
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1-5球");
			} else if (playtype == 2) {
				// 任选
				$("#page_name").html("任选");
			} else if (playtype == 3) {
				// 组选
				$("#page_name").html("组选");
			} else if (playtype == 4) {
				// 直选
				$("#page_name").html("直选");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.gd115_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							shtml += "<table class=\"Ball_List\">";
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0 || playtype == 1) {
								if (playtype == 0) {
									// 顶部龙虎
									var firstItem = items[0];
									if (firstItem) {
										// 标题
										shtml += "<thead>";
										shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
												+ firstItem.optionTitle
												+ "</th></tr></thead>";
										// 表格数据
										shtml += "<tbody>";
										shtml += "<tr class=\"Ball_tr_H\">";
										var j = 0;
										for (var i = 0; i < firstItem.optionItems.length; i++) {
											var tmpItem = firstItem.optionItems[i];
											if (i == 4) {
												// 换行
												shtml += "</tr><tr class=\"Ball_tr_H\">";
											}
											shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
													+ tmpItem.title
													+ "</td>";
											shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmpItem.rate
													+ "</span></td>";
											shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" title=\""
													+ tmpItem.bettitle
													+ "\" bstitle=\""
													+ tmpItem.title
													+ "\"  data=\""
													+ tmpItem.optionId
													+ "\" bs=\""
													+ tmpItem.rate
													+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
											shtml += "</td>";
										}
										// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
										shtml += "</tr>";
										shtml += "</tbody>";
									}
									lhhtml = shtml;
								}
								shtml += "</table>";

								// 1-5球的大小单双
								shtml += "<table class=\"Ball_List\"><tbody><tr>";

								var i = 0;
								if (playtype == 0) {
									i = 1;
								}
								for (i; i < items.length; i++) {
									var tmpItem = items[i];

									// if(i>1&&i%4==1){
									// shtml += "</tr><tr>";
									// }
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										if (playtype == 1) {
											shtml += "<td width=\"49\" class=\"name Jut_caption_1\"><span class=\" GXK101 No_"
													+ (j + 1)
													+ " ball_block\">&nbsp;</span></td>";
										} else {
											shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
													+ tmp.title + "</td>";
										}
										shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
								}
								shtml += "</tr></tbody></table>";

								$(".bet-panel").html(shtml);
								// 点击输入数量
								$(".Ball_List td").bind("click",function() {
									lotteryObj.bindTdInputEvent($(this));
								});
							} else {
								// 2 3 4
								shtml += "<tbody>";
								var weight = 4;
								if (playtype == 3 || playtype == 4) {
									weight = 2;
								}
								for (var j = 0; j < items.length; j++) {
									var tmpItem = items[j];

									if (j % weight == 0) {
										shtml += "<tr>";
									}
									shtml += "<td>";
									shtml += "<table id=\"table-ball-0\" class=\"Ball_List\">";
									shtml += "<thead>";
									var strArr = tmpItem.optionTitle
											.split("-");
									var firstItem = tmpItem.optionItems[0];
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"3\">"
											+ strArr[1]
											+ ""
											+ firstItem.rate + "</th></tr>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"3\">号</th></tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									for (var i = 0; i < tmpItem.optionItems.length; i++) {
										var tmp = tmpItem.optionItems[i];
										shtml += "<tr class=\"Ball_tr_H\"><td width=\"171\" data="
												+ tmp.optionId
												+ " num="
												+ tmp.title
												+ " class=\"name Jut_caption_1\"><span class=\" GXK101 No_"
												+ tmp.title
												+ " ball_block\">&nbsp;</span></td></tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
									if (j % weight == 3) {
										shtml += "</tr>";
									}
								}

								shtml += "</tbody>";
								shtml += "</table>";

								$(".bet-panel").html(shtml);
								// 点击输入数量
								$(".Ball_List td")
										.bind(
												"click",
												function() {
													lotteryObj
															.bindTdInputEvent($(this));
												});
							}

						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 552) {
			// jxpick11
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1-5球");
			} else if (playtype == 2) {
				// 任选
				$("#page_name").html("任选");
			} else if (playtype == 3) {
				// 组选
				$("#page_name").html("组选");
			} else if (playtype == 4) {
				// 直选
				$("#page_name").html("直选");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.jxPick11_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							shtml += "<table class=\"Ball_List\">";
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0 || playtype == 1) {
								if (playtype == 0) {
									// 顶部龙虎
									var firstItem = items[0];
									if (firstItem) {
										// 标题
										shtml += "<thead>";
										shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
												+ firstItem.optionTitle
												+ "</th></tr></thead>";
										// 表格数据
										shtml += "<tbody>";
										shtml += "<tr class=\"Ball_tr_H\">";
										var j = 0;
										for (var i = 0; i < firstItem.optionItems.length; i++) {
											var tmpItem = firstItem.optionItems[i];
											if (i == 4) {
												// 换行
												shtml += "</tr><tr class=\"Ball_tr_H\">";
											}
											shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
													+ tmpItem.title
													+ "</td>";
											shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmpItem.rate
													+ "</span></td>";
											shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" title=\""
													+ tmpItem.bettitle
													+ "\" bstitle=\""
													+ tmpItem.title
													+ "\"  data=\""
													+ tmpItem.optionId
													+ "\" bs=\""
													+ tmpItem.rate
													+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
											shtml += "</td>";
										}
										// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
										shtml += "</tr>";
										shtml += "</tbody>";
									}
									lhhtml = shtml;
								}
								shtml += "</table>";

								// 1-5球的大小单双
								shtml += "<table class=\"Ball_List\"><tbody><tr>";

								var i = 0;
								if (playtype == 0) {
									i = 1;
								}
								for (i; i < items.length; i++) {
									var tmpItem = items[i];

									// if(i>1&&i%4==1){
									// shtml += "</tr><tr>";
									// }
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										if (playtype == 1) {
											shtml += "<td width=\"49\" class=\"name Jut_caption_1\"><span class=\" GXK101 No_"
													+ (j + 1)
													+ " ball_block\">&nbsp;</span></td>";
										} else {
											shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
													+ tmp.title + "</td>";
										}
										shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
								}
								shtml += "</tr></tbody></table>";

								$(".bet-panel").html(shtml);
								// 点击输入数量
								$(".Ball_List td").bind("click",function() {
									lotteryObj.bindTdInputEvent($(this));
								});
							} else {
								// 2 3 4
								shtml += "<tbody>";
								var weight = 4;
								if (playtype == 3 || playtype == 4) {
									weight = 2;
								}
								for (var j = 0; j < items.length; j++) {
									var tmpItem = items[j];

									if (j % weight == 0) {
										shtml += "<tr>";
									}
									shtml += "<td>";
									shtml += "<table id=\"table-ball-0\" class=\"Ball_List\">";
									shtml += "<thead>";
									var strArr = tmpItem.optionTitle
											.split("-");
									var firstItem = tmpItem.optionItems[0];
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"3\">"
											+ strArr[1]
											+ ""
											+ firstItem.rate + "</th></tr>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"3\">号</th></tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									for (var i = 0; i < tmpItem.optionItems.length; i++) {
										var tmp = tmpItem.optionItems[i];
										shtml += "<tr class=\"Ball_tr_H\"><td width=\"171\" data="
												+ tmp.optionId
												+ " num="
												+ tmp.title
												+ " class=\"name Jut_caption_1\"><span class=\" GXK101 No_"
												+ tmp.title
												+ " ball_block\">&nbsp;</span></td></tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
									if (j % weight == 3) {
										shtml += "</tr>";
									}
								}

								shtml += "</tbody>";
								shtml += "</table>";

								$(".bet-panel").html(shtml);
								// 点击输入数量
								$(".Ball_List td")
										.bind(
												"click",
												function() {
													lotteryObj
															.bindTdInputEvent($(this));
												});
							}

						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 553) {
			// sdpick11
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1-5球");
			} else if (playtype == 2) {
				// 任选
				$("#page_name").html("任选");
			} else if (playtype == 3) {
				// 组选
				$("#page_name").html("组选");
			} else if (playtype == 4) {
				// 直选
				$("#page_name").html("直选");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.sdPick11_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							shtml += "<table class=\"Ball_List\">";
							var lhhtml = "";
							var ballPreHtmlsArr = [];
							var ballAftHtmlsArr = [];
							if (playtype == 0 || playtype == 1) {
								if (playtype == 0) {
									// 顶部龙虎
									var firstItem = items[0];
									if (firstItem) {
										// 标题
										shtml += "<thead>";
										shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
												+ firstItem.optionTitle
												+ "</th></tr></thead>";
										// 表格数据
										shtml += "<tbody>";
										shtml += "<tr class=\"Ball_tr_H\">";
										var j = 0;
										for (var i = 0; i < firstItem.optionItems.length; i++) {
											var tmpItem = firstItem.optionItems[i];
											if (i == 4) {
												// 换行
												shtml += "</tr><tr class=\"Ball_tr_H\">";
											}
											shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
													+ tmpItem.title
													+ "</td>";
											shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmpItem.rate
													+ "</span></td>";
											shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" title=\""
													+ tmpItem.bettitle
													+ "\" bstitle=\""
													+ tmpItem.title
													+ "\"  data=\""
													+ tmpItem.optionId
													+ "\" bs=\""
													+ tmpItem.rate
													+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
											shtml += "</td>";
										}
										// shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
										shtml += "</tr>";
										shtml += "</tbody>";
									}
									lhhtml = shtml;
								}
								shtml += "</table>";

								// 1-5球的大小单双
								shtml += "<table class=\"Ball_List\"><tbody><tr>";

								var i = 0;
								if (playtype == 0) {
									i = 1;
								}
								for (i; i < items.length; i++) {
									var tmpItem = items[i];

									// if(i>1&&i%4==1){
									// shtml += "</tr><tr>";
									// }
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										if (playtype == 1) {
											shtml += "<td width=\"49\" class=\"name Jut_caption_1\"><span class=\" GXK101 No_"
													+ (j + 1)
													+ " ball_block\">&nbsp;</span></td>";
										} else {
											shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
													+ tmp.title + "</td>";
										}
										shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
								}
								shtml += "</tr></tbody></table>";

								$(".bet-panel").html(shtml);
								// 点击输入数量
								$(".Ball_List td").bind("click",function() {
									lotteryObj.bindTdInputEvent($(this));
								});
							} else {
								// 2 3 4
								shtml += "<tbody>";
								var weight = 4;
								if (playtype == 3 || playtype == 4) {
									weight = 2;
								}
								for (var j = 0; j < items.length; j++) {
									var tmpItem = items[j];

									if (j % weight == 0) {
										shtml += "<tr>";
									}
									shtml += "<td>";
									shtml += "<table id=\"table-ball-0\" class=\"Ball_List\">";
									shtml += "<thead>";
									var strArr = tmpItem.optionTitle
											.split("-");
									var firstItem = tmpItem.optionItems[0];
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"3\">"
											+ strArr[1]
											+ ""
											+ firstItem.rate + "</th></tr>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"3\">号</th></tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									for (var i = 0; i < tmpItem.optionItems.length; i++) {
										var tmp = tmpItem.optionItems[i];
										shtml += "<tr class=\"Ball_tr_H\"><td width=\"171\" data="
												+ tmp.optionId
												+ " num="
												+ tmp.title
												+ " class=\"name Jut_caption_1\"><span class=\" GXK101 No_"
												+ tmp.title
												+ " ball_block\">&nbsp;</span></td></tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
									if (j % weight == 3) {
										shtml += "</tr>";
									}
								}

								shtml += "</tbody>";
								shtml += "</table>";

								$(".bet-panel").html(shtml);
								// 点击输入数量
								$(".Ball_List td")
										.bind(
												"click",
												function() {
													lotteryObj
															.bindTdInputEvent($(this));
												});
							}

						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 356) {
			// gxk10
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1球");
			} else if (playtype == 2) {
				// 1-5球
				$("#page_name").html("2球");
			} else if (playtype == 3) {
				// 1-5球
				$("#page_name").html("3球");
			} else if (playtype == 4) {
				// 1-5球
				$("#page_name").html("4球");
			} else if (playtype == 5) {
				// 1-5球
				$("#page_name").html("5球");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);

			$.ajax({
					type : "POST",
					url : move.gxk10_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';

							if(playtype==9){
								// 连码
								for(var i = 0;i<items.length;i++){
									var tmpItem = items[i];
									shtml += "<table class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ tmpItem.optionTitle
											+ "</th></tr></thead>";
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";

									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmpOption = tmpItemOptions[j];
										if (j > 0 && j % 4 == 0) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
												+ tmpOption.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmpOption.rate + "</span></td>";
										shtml += "<td width=\"91\" class=\"amount Jut_caption_1 \">";
										shtml += "<i class=\"icon-selectors square\" data=\""+ tmpOption.optionId+ "\" bs=\""+ tmpOption.rate+ "\" bstitle=\""+ tmpOption.title+ "\" title=\""+ tmpOption.bettitle + "\">";
										shtml += "</td>";
									}
								}
								shtml += "</table>";
							}else{

								shtml += "<table class=\"Ball_List\">";
								// 顶部
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i > 0 && i % 4 == 0) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmpItem.rate + "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}

									// if (i > 0 && i % 4 > 0) {
									// 	for (var m = 0; m < 4 - i % 4; m++) {
									// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// 	}
									// }
									shtml += "</tr>";
									shtml += "</tbody>";
								}
								shtml += "</table>";

								// 1-5球的大小单双 如果有
								shtml += "<table class=\"Ball_List\"><tbody><tr>";
								for (var i = 1; i < items.length; i++) {
									var tmpItem = items[i];
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										shtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
												+ tmp.title + "</td>";
										shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
								}
								shtml += "</tr></tbody></table>";
							}
							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
							mask.close();
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
					}
				});
		}  else if (cate == 357) {
			// cqk10
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("1球");
			} else if (playtype == 2) {
				// 1-5球
				$("#page_name").html("2球");
			} else if (playtype == 3) {
				// 1-5球
				$("#page_name").html("3球");
			} else if (playtype == 4) {
				// 1-5球
				$("#page_name").html("4球");
			} else if (playtype == 5) {
				// 1-5球
				$("#page_name").html("5球");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);

			$.ajax({
					type : "POST",
					url : move.cqk10_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';

							if(playtype==9){
								// 连码
								for(var i = 0;i<items.length;i++){
									var tmpItem = items[i];
									shtml += "<table class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ tmpItem.optionTitle
											+ "</th></tr></thead>";
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";

									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmpOption = tmpItemOptions[j];
										if (j > 0 && j % 4 == 0) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
												+ tmpOption.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmpOption.rate + "</span></td>";
										shtml += "<td width=\"91\" class=\"amount Jut_caption_1 \">";
										shtml += "<i class=\"icon-selectors square\" data=\""+ tmpOption.optionId+ "\" bs=\""+ tmpOption.rate+ "\" bstitle=\""+ tmpOption.title+ "\" title=\""+ tmpOption.bettitle + "\">";
										shtml += "</td>";
									}
								}
								shtml += "</table>";
							}else{
								shtml += "<table class=\"Ball_List\">";
								// 顶部
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i > 0 && i % 4 == 0) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmpItem.rate + "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}

									// if (i > 0 && i % 4 > 0) {
									// 	for (var m = 0; m < 4 - i % 4; m++) {
									// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// 	}
									// }
									shtml += "</tr>";
									shtml += "</tbody>";
								}
								shtml += "</table>";

								// 1-8球的大小单双 如果有
								var weight = 4;
								shtml += "<table class=\"Ball_List\"><tbody><tr>";
								for (var i = 1; i < items.length; i++) {
									var tmpItem = items[i];
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									if(i%weight==1){
										shtml += "<tr>";
									}
									
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
												+ tmp.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
									if(i%weight==0){
										shtml += "</tr>";
									}
								}
								shtml += "</tbody></table>";
							}

							
							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
							mask.close();
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
					}
				});
		} else if (cate == 359) {
			// liuhecai/marksix
			// $("#page_name").html($(".cate_menu .selected").text());
			$("#page_name").html($("#selectCate option:selected").text());
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.liuhecai_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							for (var i = 0; i < items.length; i++) {
								var tmpItem = items[i];
								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"15\">"
										+ tmpItem.optionTitle
										+ "</th></tr></thead>";
								// 表格数据
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								for (var j = 0; j < tmpItem.optionItems.length; j++) {
									var optionItem = tmpItem.optionItems[j];
									if (j > 0 && j % 5 == 0) {
										// 换行
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									// LIUHECAI1

									if (lotteryObj
											.checkNumByChar(optionItem.title)) {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1\"><span class=\" LIUHECAI1 No_"
												+ (j + 1)
												+ " ball_block\">&nbsp;</span></td>";
									} else {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
												+ optionItem.title
												+ "</td>";
									}
									shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
											+ optionItem.rate
											+ "</span></td>";
									shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" title=\""
											+ optionItem.bettitle
											+ "\" bstitle=\""
											+ optionItem.title
											+ "\"  data=\""
											+ optionItem.optionId
											+ "\" bs=\""
											+ optionItem.rate
											+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
									shtml += "</td>";
								}
								// if (j > 0 && j % 5 > 0) {
								// 	for (var m = 0; m < 5 - j % 5; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
								shtml += "</tbody>";
								shtml += "</table>";
							}

							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
							mask.close();
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
					}
				});
		} else if (cate == 360) {
			// sflhc
			// $("#page_name").html($(".cate_menu .selected").text());
			$("#page_name").html($("#selectCate option:selected").text());
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.sflhc_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';
							for (var i = 0; i < items.length; i++) {
								var tmpItem = items[i];
								shtml += "<table class=\"Ball_List\">";
								shtml += "<thead>";
								shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"15\">"
										+ tmpItem.optionTitle
										+ "</th></tr></thead>";
								// 表格数据
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								for (var j = 0; j < tmpItem.optionItems.length; j++) {
									var optionItem = tmpItem.optionItems[j];
									if (j > 0 && j % 5 == 0) {
										// 换行
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									// LIUHECAI1

									if (lotteryObj
											.checkNumByChar(optionItem.title)) {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1\"><span class=\" LIUHECAI1 No_"
												+ (j + 1)
												+ " ball_block\">&nbsp;</span></td>";
									} else {
										shtml += "<td width=\"38\" class=\"name Jut_caption_1 \">"
												+ optionItem.title
												+ "</td>";
									}
									shtml += "<td width=\"52\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
											+ optionItem.rate
											+ "</span></td>";
									shtml += "<td width=\"70\" class=\"amount Jut_caption_1 \">";
									shtml += "<input type=\"text\" title=\""
											+ optionItem.bettitle
											+ "\" bstitle=\""
											+ optionItem.title
											+ "\"  data=\""
											+ optionItem.optionId
											+ "\" bs=\""
											+ optionItem.rate
											+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
									shtml += "</td>";
								}
								// if (j > 0 && j % 5 > 0) {
								// 	for (var m = 0; m < 5 - j % 5; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
								shtml += "</tbody>";
								shtml += "</table>";
							}

							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
							mask.close();
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
					}
				});
		} else if (cate == 355) {
			// gdk10
			$("#page_name").html($(".cate_menu .selected").text());

			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.gdK10_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';

							if(playtype==9){
								// 连码
								for(var i = 0;i<items.length;i++){
									var tmpItem = items[i];
									shtml += "<table class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ tmpItem.optionTitle
											+ "</th></tr></thead>";
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";

									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmpOption = tmpItemOptions[j];
										if (j > 0 && j % 4 == 0) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
												+ tmpOption.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmpOption.rate + "</span></td>";
										shtml += "<td width=\"91\" class=\"amount Jut_caption_1 \">";
										shtml += "<i class=\"icon-selectors square\" data=\""+ tmpOption.optionId+ "\" bs=\""+ tmpOption.rate+ "\" bstitle=\""+ tmpOption.title+ "\" title=\""+ tmpOption.bettitle + "\">";
										shtml += "</td>";
									}
								}
								shtml += "</table>";
							}else{

								shtml += "<table class=\"Ball_List\">";
								// 顶部
								var firstItem = items[0];
								if (firstItem) {
									// 标题
									shtml += "<thead>";
									shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
											+ firstItem.optionTitle
											+ "</th></tr></thead>";
									// 表格数据
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var j = 0;
									for (var i = 0; i < firstItem.optionItems.length; i++) {
										var tmpItem = firstItem.optionItems[i];
										if (i > 0 && i % 4 == 0) {
											// 换行
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
												+ tmpItem.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmpItem.rate + "</span></td>";
										shtml += "<td width=\"91\" class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" title=\""
												+ tmpItem.bettitle
												+ "\" bstitle=\""
												+ tmpItem.title
												+ "\"  data=\""
												+ tmpItem.optionId
												+ "\" bs=\""
												+ tmpItem.rate
												+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
										shtml += "</td>";
									}

									// if (i > 0 && i % 4 > 0) {
									// 	for (var m = 0; m < 4 - i % 4; m++) {
									// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// 	}
									// }
									shtml += "</tr>";
									shtml += "</tbody>";
								}
								shtml += "</table>";

								// 1-8球的大小单双 如果有
								shtml += "<table class=\"Ball_List\"><tbody><tr>";
								for (var i = 1; i < items.length; i++) {
									var tmpItem = items[i];
									if ((i - 1) > 0 && (i - 1) % 4 == 0) {
										shtml += "</tr><tr>";
									}
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										shtml += "<td width=\"49\" class=\"name Jut_caption_1 \">"
												+ tmp.title + "</td>";
										shtml += "<td width=\"62\" class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
								}
								shtml += "</tr></tbody></table>";
							}
							

							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
							mask.close();
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
					}
				});
		} else if (cate == 155) {
			// jsft
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("数字盘");
			} else if (playtype == 2) {
				// 冠亚组合
				$("#page_name").html("冠亚组合");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.jsft_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var result = ret.data;
							var items = result.betItems;
							var html = '';
							var htmlkj = '';
							var shtml = "";
							var weight = 5;
							if (playtype == 2) {
								// 冠亚组合
								weight = 4;
							}

							for (var i = 0; i < items.length; i++) {
								var cell = items[i];
								var tmpItem = cell;
								if (playtype == 0 || playtype == 1) {
									// 两面盘 或者 单号1~10
									if (i % 5 == 0) {
										shtml += "<table class=\"Ball_List\"><tbody>";
										shtml += "<tr>";
									}
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "<tr>";
									if (playtype == 1) {
										shtml += "<th class=\"td_caption_1 F_bold\">号码</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">赔率</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">金额</th>";
									}
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										if (playtype == 0) {
											// 两面盘
											shtml += "<td  class=\" name Jut_caption_1\"  width=\"38\">"
													+ tmp.title + "</td>";
										} else if (playtype == 1) {
											// shtml += "<td
											// class=\"name\"><span
											// class=\"ball
											// c-n"+(j+1)+"\"></td>";
											// shtml += "<td
											// class=\"name\"><span
											// class=\"Jut_caption_1
											// No_"+(j+1)+"\">&nbsp;</span></td>";
											shtml += "<td  width=\"38\" class=\"name Jut_caption_1 BJPK1 No_"
													+ (j + 1) + "\"></td>";
										}
										shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
									if (i % 5 == 4) {
										shtml += "</tr></tbody></table>";
									}
								} else if (playtype == 2) {
									shtml += "<table class=\"Ball_List\"><tbody>";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"12\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle
											+ "（冠军车号＋亚军车号 ＝ 和）</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										if (j > 0 && j % weight == 0) {
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										if (playtype == 2) {
											// 冠亚组合
											shtml += "<td width=\"49\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"62\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"90\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										} else {
											shtml += "<td width=\"38\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										}

									}
									// if (j > 0 && j % weight > 0) {
									// 	for (var m = 0; m < weight - j
									// 			% weight; m++) {
									// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// 	}
									// }
									shtml += "</tr>";
									shtml += "</tbody></table>";

								}
							}
							$(".bet-panel").html(shtml);
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});

						} else {
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 152) {
			// sfpk10
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("数字盘");
			} else if (playtype == 2) {
				// 冠亚组合
				$("#page_name").html("冠亚组合");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.sfPk10_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var result = ret.data;
							var items = result.betItems;
							var html = '';
							var htmlkj = '';
							var shtml = "";
							var weight = 5;
							if (playtype == 2) {
								// 冠亚组合
								weight = 4;
							}

							for (var i = 0; i < items.length; i++) {
								var cell = items[i];
								var tmpItem = cell;
								if (playtype == 0 || playtype == 1) {
									// 两面盘 或者 单号1~10
									if (i % 5 == 0) {
										shtml += "<table class=\"Ball_List\"><tbody>";
										shtml += "<tr>";
									}
									shtml += "<td>";
									shtml += "<table id=\"table-ball-" + i
											+ "\" class=\"Ball_List\">";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"3\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle + "</th>";
									shtml += "</tr>";
									shtml += "<tr>";
									if (playtype == 1) {
										shtml += "<th class=\"td_caption_1 F_bold\">号码</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">赔率</th>";
										shtml += "<th class=\"td_caption_1 F_bold\">金额</th>";
									}
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										shtml += "<tr class=\"Ball_tr_H\">";
										if (playtype == 0) {
											// 两面盘
											shtml += "<td  class=\" name Jut_caption_1\"  width=\"38\">"
													+ tmp.title + "</td>";
										} else if (playtype == 1) {
											// shtml += "<td
											// class=\"name\"><span
											// class=\"ball
											// c-n"+(j+1)+"\"></td>";
											// shtml += "<td
											// class=\"name\"><span
											// class=\"Jut_caption_1
											// No_"+(j+1)+"\">&nbsp;</span></td>";
											shtml += "<td  width=\"38\" class=\"name Jut_caption_1 BJPK1 No_"
													+ (j + 1) + "\"></td>";
										}
										shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
												+ tmp.rate + "</span></td>";
										shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
										shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
												+ tmp.optionId
												+ "\" bs=\""
												+ tmp.rate
												+ "\" bstitle=\""
												+ tmp.title
												+ "\" title=\""
												+ tmp.bettitle + "\">";
										shtml += "</td>";
										shtml += "</tr>";
									}
									shtml += "</tbody>";
									shtml += "</table>";
									shtml += "</td>";
									if (i % 5 == 4) {
										shtml += "</tr></tbody></table>";
									}
								} else if (playtype == 2) {
									shtml += "<table class=\"Ball_List\"><tbody>";
									shtml += "<thead>";
									shtml += "<tr>";
									shtml += "<th colspan=\"12\" class=\"td_caption_1 F_bold\">"
											+ tmpItem.optionTitle
											+ "（冠军车号＋亚军车号 ＝ 和）</th>";
									shtml += "</tr>";
									shtml += "</thead>";
									shtml += "<tbody>";
									shtml += "<tr class=\"Ball_tr_H\">";
									var tmpItemOptions = tmpItem.optionItems;
									for (var j = 0; j < tmpItemOptions.length; j++) {
										var tmp = tmpItemOptions[j];
										if (j > 0 && j % weight == 0) {
											shtml += "</tr><tr class=\"Ball_tr_H\">";
										}
										if (playtype == 2) {
											// 冠亚组合
											shtml += "<td width=\"49\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"62\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"90\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										} else {
											shtml += "<td width=\"38\"  class=\"name Jut_caption_1\" >"
													+ tmp.title + "</td>";
											shtml += "<td width=\"52\"  class=\"odds Jut_caption_1 \"><span class=\"c-txt3\">"
													+ tmp.rate
													+ "</span></td>";
											shtml += "<td width=\"70\"  class=\"amount Jut_caption_1 \">";
											shtml += "<input type=\"text\" class=\"inp1\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\" data=\""
													+ tmp.optionId
													+ "\" bs=\""
													+ tmp.rate
													+ "\" bstitle=\""
													+ tmp.title
													+ "\" title=\""
													+ tmp.bettitle + "\">";
											shtml += "</td>";
										}

									}
									// if (j > 0 && j % weight > 0) {
									// 	for (var m = 0; m < weight - j
									// 			% weight; m++) {
									// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
									// 	}
									// }
									shtml += "</tr>";
									shtml += "</tbody></table>";

								}
							}
							$(".bet-panel").html(shtml);
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});

						} else {
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		} else if (cate == 358) {
			// poker
			if (playtype == 0) {
				// 两面盘
				$("#page_name").html("两面盘");
			} else if (playtype == 1) {
				// 1-5球
				$("#page_name").html("特码");
			}
			var map = {};
			map['playType'] = "" + playtype;
			var mw = baseObj.mw(map);
			$.ajax({
					type : "POST",
					url : move.kuailepuke3_betPanel,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						if (code == '200') {
							var data = ret.data;
							var items = data.betItems;
							var shtml = '';

							for (var i = 0; i < items.length; i++) {
								var tmpItemA = items[i];
								// 标题
								shtml += "<table class=\"Ball_List\"><tbody><tr>";
								shtml += "<thead>";
								shtml += "<tr><th class=\"td_caption_1 F_bold\" colspan=\"12\">"
										+ tmpItemA.optionTitle
										+ "</th></tr></thead>";
								// 表格数据
								shtml += "<tbody>";
								shtml += "<tr class=\"Ball_tr_H\">";
								var weight = 4;
								for (var j = 0; j < tmpItemA.optionItems.length; j++) {
									var tmpItem = tmpItemA.optionItems[j];
									if (j > 0 && j % weight == 0) {
										// 换行
										shtml += "</tr><tr class=\"Ball_tr_H\">";
									}
									shtml += "<td width=\"49\" class=\"name Jut_caption_1\">"
											+ tmpItem.title + "</td>";
									shtml += "<td width=\"62\" class=\"odds Jut_caption_1\"><span class=\"c-txt3\">"
											+ tmpItem.rate + "</span></td>";
									if (j == 3) {
										shtml += "<td width=\"92\" class=\"amount Jut_caption_1\">";
									} else {
										shtml += "<td width=\"90\" class=\"amount Jut_caption_1\">";
									}
									shtml += "<input type=\"text\" title=\""
											+ tmpItem.bettitle
											+ "\" bstitle=\""
											+ tmpItem.title
											+ "\"  data=\""
											+ tmpItem.optionId
											+ "\" bs=\""
											+ tmpItem.rate
											+ "\" onkeyup=\"value=value.replace(/[^\\d]/g,'')\">";
									shtml += "</td>";
								}
								// if (j > 0 && j % weight > 0) {
								// 	for (var m = 0; m < weight - j % weight; m++) {
								// 		shtml += "<td colspan=\"3\" class=\"name not-event\"></td>";
								// 	}
								// }
								shtml += "</tr>";
								shtml += "</tbody>";

							}
							shtml += "</tr></tbody></table>";
							$(".bet-panel").html(shtml);
							// 点击输入数量
							$(".Ball_List td").bind("click", function() {
								lotteryObj.bindTdInputEvent($(this));
							});
						}
						mask.close();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mask.close();
					}
				});
		}
	},
	load : function() {
		mask = move.createLoading();
		mask.show();
		lotteryObj.clearTimer()
		// lotteryObj.userInfo();
		// lotteryObj.bindTypeTab();

		lotteryObj.changeCateTab(lotteryObj.currentType);
		lotteryObj.bindCateTab();
		lotteryObj.changeCate(lotteryObj.currentType, 0);
		lotteryObj.bindPageEvents();
		lotteryObj.open();

		// lotteryObj.hotRanking();

		// lotteryObj.updateTrendTab();
		// lotteryObj.updateTrend(0);
		lotteryObj.updateGFType();
		lotteryObj.changeLogoImg();
		lotteryObj.openlist();
		lotteryObj.bindGameGuide();
		// lotteryObj.betRecords();
		
		if (move.user.u) {
			// 已登录
			var shtml = "";
			shtml += "<span class=\"fr\">					<a rel=\"nofollow\" href=\"/user/\">我的账户</a>					|					<a href=\"/user/deposit\">充值</a>					|					<a href=\"/user/withdraw\">提款</a>					|					[<a class=\"logout\" id=\"logout\" href=\"#\">退出</a>]				</span>";
			$(".login-line").after(shtml);
			$(".login-line").remove();
			$("#logina").hide();

			var points = move.user.points;
			var money = move.user.money;
			var userName = move.user.userName;

			$('.logined .user').text(userName);
			$('.logined .money').text(money);
			$('.logined .point').text(points);
			$('.topline .loginline').hide();
			$('.topline .logined').show();

		}
		$(".logout").bind("click", function() {
			user.logout();
		});
		$('#kinds').mousemove(function(e) {
			$('#kinds').show();
		});
		$('.selebox').mousemove(function(e) {
			$('#kinds').show();
		});
		$('#kinds').mouseout(function(e) {
			$('#kinds').hide();
		});
		$('.selebox').mouseout(function(e) {
			$('#kinds').hide();
		});
		
	},
	bindGameGuide : function() {
		$(".pop-box .xywrap a").bind("click", function() {
			mask = move.createLoading();
			mask.show();
			var url = $(this).attr("href");
			var strs = url.split("#");
			var type = strs[1];
			lotteryObj.currentType = 256;

			if (type == "bj3") { 
				lotteryObj.currentType = 256;
			} else if (type == "jsk3") { 
				lotteryObj.currentType = 651;
			} else if (type == "bjk3") { 
				lotteryObj.currentType = 653;
			} else if (type == "jxpick11") { 
				lotteryObj.currentType = 552;
			} else if (type == "gdpick11") { 
				lotteryObj.currentType = 551;
			} else if (type == "sdpick11") { 
				lotteryObj.currentType = 553;
			} else if (type == "sflhc") { 
				lotteryObj.currentType = 360;
			} else if (type == "marksix") { 
				lotteryObj.currentType = 359;
			} else if (type == "poker") { 
				lotteryObj.currentType = 358;
			} else if (type == "cqk10") { 
				lotteryObj.currentType = 357;
			} else if (type == "gxk10") { 
				lotteryObj.currentType = 356;
			} else if (type == "gdk10") { 
				lotteryObj.currentType = 355;
			} else if (type == "bjlu28") { 
				lotteryObj.currentType = 353;
			} else if (type == "xjplu28") { 
				lotteryObj.currentType = 352;
			} else if (type == "five") { 
				lotteryObj.currentType = 258;
			} else if (type == "bjssc") { 
				lotteryObj.currentType = 255;
			} else if (type == "cqssc") { 
				lotteryObj.currentType = 251;
			}  else if (type == "xjssc") { 
				lotteryObj.currentType = 252;
			}  else if (type == "tjssc") { 
				lotteryObj.currentType = 253;
			} else if (type == "jsft") { 
				lotteryObj.currentType = 155;
			} else if (type == "xyft") { 
				lotteryObj.currentType = 154;
			} else if (type == "sfpk102") { 
				lotteryObj.currentType = 153;
			} else if (type == "sfpk10") { 
				lotteryObj.currentType = 152;
			} else if (type == "bjpk10") { 
				lotteryObj.currentType = 151;
			} else if (type == "jxk3") { 
				lotteryObj.currentType = 661;
			} 

			lotteryObj.clearTimer();
			lotteryObj.load();
		});
	},
	updateGFType : function() {
		var url = "";
		$(".xy-a").hide();
		if (lotteryObj.currentType == 56) {
			// bj3
			url = "../sfc";
		} else if (lotteryObj.currentType == 151) {
			url = "../bjpk10";
			$(".xy-a").show();
		} else if (lotteryObj.currentType == 352) {
			// xjpLu28
			url = "../bjpk10";
		} else if (lotteryObj.currentType == 353) {
			// pcdd
			url = "../bjk3";
		} else if (lotteryObj.currentType == 251) {
			// cqssc
			url = "../cqssc";
			$(".xy-a").show();
		}  else if (lotteryObj.currentType == 252) {
			// xjssc
			url = "../xjssc";
			$(".xy-a").show();
		}  else if (lotteryObj.currentType == 253) {
			// tjssc
			url = "../tjssc";
			$(".xy-a").show();
		} else if (lotteryObj.currentType == 651) {
			// jsk3
			url = "../jsk3";
			$(".xy-a").show();
		} else if (lotteryObj.currentType == 653) {
			// bjk3
			url = "../bjk3";
			$(".xy-a").show();
		} else if (lotteryObj.currentType == 551) {
			// gd115
			url = "../gdpick11";
			$(".xy-a").show();
		} else if (lotteryObj.currentType == 552) {
			// jxpick11
			url = "../jxpick11";
			$(".xy-a").show();
		} else if (lotteryObj.currentType == 553) {
			// sdpick11
			url = "../sdpick11";
			$(".xy-a").show();
		} else if (lotteryObj.currentType == 355) {
			// gxk10
			url = "../gdk10";
		} else if (lotteryObj.currentType == 359) {
			// liuhecai
			url = "../liuhecai";
		} else if (lotteryObj.currentType == 360) {
			// sflhc
			url = "../sflhc";
		} else if (lotteryObj.currentType == 155) {
			// jsft
			url = "../jsft";
			$(".xy-a").show();
		} else if (lotteryObj.currentType == 152) {
			// sfpk10
			url = "../sfpk10";
		}else if (lotteryObj.currentType == 358) {
			// poker
			url = "../poker";
		}else if (lotteryObj.currentType == 357) {
			// cqk10
			url = "../cqk10";
		}else if (lotteryObj.currentType == 258) {
			// five
			url = "../wfc";
			$(".xy-a").show();
		}else if (lotteryObj.currentType == 255) {
			// bjssc
			url = "../bjssc";
			$(".xy-a").show();
		}else if (lotteryObj.currentType == 154) {
			// xyft
			url = "../xyft";
			$(".xy-a").show();
		}else if (lotteryObj.currentType == 153) {
			// sfpj102
			url = "../sfpk102";
			$(".xy-a").show();
		}else if (lotteryObj.currentType == 661) {
			// jxk3
			url = "../jxk3";
		}
		$(".xy-a").attr("href", url);
	},
	openlist : function() {
		var map = {};
		map['pageSize'] = 10;
		var mw = baseObj.mw(map);
		var url = "";
		var gameTypeStr = "";
		if (lotteryObj.currentType == 256) {
			// bj3
			url = move.bj3_latestLottery;
			gameTypeStr = "北京三分彩";
		} else if (lotteryObj.currentType == 151) {
			// bjpk10
			url = move.bjPk10_latestLottery;
			gameTypeStr = "北京赛车";
		} else if (lotteryObj.currentType == 352) {
			// xjpLu28
			url = move.xjpLu28_latestLottery;
			gameTypeStr = "新加坡幸运28";
		} else if (lotteryObj.currentType == 353) {
			// pcdd
			url = move.bjLu28_latestLottery;
			gameTypeStr = "幸运28";
		} else if (lotteryObj.currentType == 251) {
			// cqssc
			url = move.cqSsc_latestLottery;
			gameTypeStr = "重庆时时彩";
		}  else if (lotteryObj.currentType == 252) {
			// xjssc
			url = move.xjSsc_latestLottery;
			gameTypeStr = "新疆时时彩";
		}  else if (lotteryObj.currentType == 253) {
			// tjssc
			url = move.tjSsc_latestLottery;
			gameTypeStr = "天津时时彩";
		} else if (lotteryObj.currentType == 651) {
			// jsk3
			url = move.jsK3_latestLottery;
			gameTypeStr = "江苏快三";
		} else if (lotteryObj.currentType == 653) {
			// bjk3
			url = move.bjk3_latest;
			gameTypeStr = "北京快三";
		} else if (lotteryObj.currentType == 551) {
			// gd115
			url = move.gdPick11_latestLottery;
			gameTypeStr = "广东11选5";
		} else if (lotteryObj.currentType == 552) {
			// jxpick11
			url = move.jxPick11_latestLottery;
			gameTypeStr = "江西11选5";
		} else if (lotteryObj.currentType == 553) {
			// sdpick11
			url = move.sdPick11_latestLottery;
			gameTypeStr = "山东11选5";
		} else if (lotteryObj.currentType == 356) {
			// gxk10
			url = move.gxk10_latestLottery;
			gameTypeStr = "广西快乐十分";
		} else if (lotteryObj.currentType == 359) {
			// liuhecai
			url = move.liuhecai_latestLottery;
			gameTypeStr = "香港六合彩";
		} else if (lotteryObj.currentType == 360) {
			// sflhc
			url = move.sflhc_latestLottery;
			gameTypeStr = "极速六合彩";
		} else if (lotteryObj.currentType == 155) {
			// jsft
			url = move.bj3_latestLottery;
			gameTypeStr = "极速飞艇";
		} else if (lotteryObj.currentType == 152) {
			// sfpk10
			url = move.sfPk10_latestLottery;
			gameTypeStr = "极速赛车";
		}else if (lotteryObj.currentType == 358) {
			// poker
			url = move.kuailepuke3_latestLottery;
			gameTypeStr = "快乐扑克3";
		}else if (lotteryObj.currentType == 357) {
			// cqk10
			url = move.cqk10_latestLottery;
			gameTypeStr = "重庆幸运农场";
		}else if (lotteryObj.currentType == 258) {
			// five
			url = move.five_latestLottery;
			gameTypeStr = "五分彩";
		}else if (lotteryObj.currentType == 255) {
			// bjssc
			url = move.bjssc_latestLottery;
			gameTypeStr = "北京时时彩";
		}else if (lotteryObj.currentType == 154) {
			// xyft
			url = move.xyft_latestLottery;
			gameTypeStr = "幸运飞艇";
		}else if (lotteryObj.currentType == 153) {
			// sfpj102
			url = move.sfPk102_latestLottery;
			gameTypeStr = "三分pk10";
		}else if (lotteryObj.currentType == 661) {
			// sfpj102
			url = move.jxk3_latestLottery;
			gameTypeStr = "江西快3";
		}else if (lotteryObj.currentType == 355) {
			// gdk10
			url = move.gdK10_latestLottery;
			gameTypeStr = "广东快乐十分";
		}
		
		$(".gametype").html(gameTypeStr);
		$(".winning .title").html(gameTypeStr + "中奖排行");

		$.ajax({
			type : "POST",
			url : url,
			dataType : 'json',
			data : {
				mw : mw
			},
			success : function(ret) {
				var code = ret.code;
				if (code == '200') {
					var result = ret.data;
					var items = result.items;
					var html = '';
					var firstItem = items[0];
					var openResultLength = firstItem.resultItems.length;
					html += "<table><thead>";
					html += "<tr>";
					html += "<th>期号</th>";
					html += "<th colspan=\"" + openResultLength
							+ "\">开奖号码</th>";
					html += "</tr>";
					html += "</thead>";
					html += "<tbody id=\"poenlistbox\">";

					for (var i = 0; i < items.length; i++) {
						var obj = items[i];
						var sessionNo = obj.sessionNo;
						var resultItems = obj.resultItems;
						var afThree = obj.afThree;// 后三
						var unit = obj.unit;// 个位
						var tenths = obj.tenths;// 十位
						html = html + '<tr><td>' + sessionNo + '</td>';
						if(lotteryObj.currentType==17||lotteryObj.currentType==6){// 快3类
							for (var j = 0; j < 3; j++) {
								var num = resultItems[j];
								html = html + '<td>' + num + '</td>';
							}
						}else{
							for (var j = 0; j < resultItems.length; j++) {
								var num = resultItems[j];
								html = html + '<td>' + num + '</td>';
							}
						}
						// html=html+'<td>'+tenths+'</td>';
						// html=html+'<td>'+unit+'</td>';
						// html=html+'<td>'+afThree+'</td>';
						html = html + '</tr>';
					}
					html += "</tbody>";
					$('.results').html(html);
				} else {
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
			}
		});
	},
	changeLogoImg : function() {
		var url = "";
		var adsStr = ""
		if (lotteryObj.currentType == 1) {
			// bj3
			url = "/images/column/bj3.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 151) {
			// pk10
			url = "/images/column/bjpk10.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		}  else if (lotteryObj.currentType == 153) {
			// sfpk102
			url = "/images/column/sfpk102.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 154) {
			// xyft
			url = "/images/column/xyft.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 155) {
			// jsft
			url = "/images/column/jsft.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 352) {
			// xjpLu28
			url = "/images/column/xjplu28.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 353) {
			// pcdd
			url = "/images/column/bjlu28.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 251) {
			// cqssc
			url = "/images/column/cqssc.png";
			adsStr = "5-10分钟一期，全天120期！";
		}  else if (lotteryObj.currentType == 252) {
			// xjssc
			url = "/images/column/xjssc.png";
			adsStr = "10分钟一期，全天96期！";
		}  else if (lotteryObj.currentType == 253) {
			// tjssc
			url = "/images/column/tjssc.png";
			adsStr = "10分钟一期，全天84期！";
		} else if (lotteryObj.currentType == 651) {
			// jsk3
			url = "/images/column/jsk3.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 653) {
			// bjk3
			url = "/images/column/bjk3.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 551) {
			// gd115
			url = "/images/column/gdpick11.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 553) {
			// sdpick11
			url = "/images/column/sdpick11.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 356) {
			// gxk10
			url = "/images/column/gxk10.png";
		} else if (lotteryObj.currentType == 359) {
			// marksix
			url = "/images/column/marksix.png";
		} else if (lotteryObj.currentType == 355) {
			// gdk10
			url = "/images/column/gdk10.png";
			adsStr = "追号火爆，易中奖，10分钟一期！";
		} else if (lotteryObj.currentType == 152) {
			// sfpk10
			url = "/images/column/sfpk10.png";
		} else if (lotteryObj.currentType == 552) {
			// jxpick11
			url = "/images/column/jxpick11.png";
		} else if (lotteryObj.currentType == 358) {
			// poker
			url = "/images/column/poker.png";
		} else if (lotteryObj.currentType == 360) {
			// sflhc
			url = "/images/column/sflhc.png";
		} else if (lotteryObj.currentType == 258) {
			// five
			url = "/images/column/wfc.png";
		} else if (lotteryObj.currentType == 255) {
			// bjssc
			url = "/images/column/bjssc.png";
		} else if (lotteryObj.currentType == 154) {
			// xyft
			url = "/images/column/xyft.png";
		} else if (lotteryObj.currentType == 153) {
			// sfpk102
			url = "/images/column/sfpk102.png";
		} else if (lotteryObj.currentType == 256) {
			// bj3
			url = "/images/column/bj3.png";
		} else if (lotteryObj.currentType == 357) {
			// bj3
			url = "/images/column/cqk10.png";
		} else if (lotteryObj.currentType == 661) {
			// bj3
			url = "/images/column/jxk3.png";
		} 
		$(".head-box .logo img").attr("src", url);
	},
	open : function(t) {
		if (t != -1) {
			
			$(".amount input").each(function() {
				$(this).removeAttr("disabled");
				$(this).val("");
			});
			$(".amount i").each(function() {
				$(this).removeClass("seal");
				$(this).addClass("square");
			});
			$("#bet-status").html("");
			$(".submitbtn").show();

			$(".Ball_List td").bind("click", function() {
				lotteryObj.bindTdInputEvent($(this));
			});
			lotteryObj.openlist();
		}
		var updateUrl = "";
		var timerOpenStr = "";
		var timerInitStr = "";
		if (lotteryObj.currentType == 256) {
			// bj3
			updateUrl = move.bj3_currentTime;
			timerOpenStr = "timer-bj3open";
			timerInitStr = "timer-bj3";
		} else if (lotteryObj.currentType == 151) {
			// bjpk10
			updateUrl = move.bjPk10_currentTime;
			timerOpenStr = "timer-bjopen";
			timerInitStr = "timer-bj";
		} else if (lotteryObj.currentType == 352) {
			// xjpLu28
			updateUrl = move.xjpLu28_currentTime;
			timerOpenStr = "timer-xjp28open";
			timerInitStr = "timer-xjp28";
		} else if (lotteryObj.currentType == 353) {
			// pcdd / bjlu28
			updateUrl = move.bjLu28_currentTime;
			timerOpenStr = "timer-bjpcddopen";
			timerInitStr = "timer-bjpcdd";
		} else if (lotteryObj.currentType == 251) {
			// cqssc
			updateUrl = move.cqSsc_currentTime;
			timerOpenStr = "timer-cqsscopen";
			timerInitStr = "timer-cqssc";
		}  else if (lotteryObj.currentType == 252) {
			// xjssc
			updateUrl = move.xjSsc_currentTime;
			timerOpenStr = "timer-xjsscopen";
			timerInitStr = "timer-xjssc";
		}  else if (lotteryObj.currentType == 253) {
			// tjssc
			updateUrl = move.tjSsc_currentTime;
			timerOpenStr = "timer-tjsscopen";
			timerInitStr = "timer-tjssc";
		} else if (lotteryObj.currentType == 651) {
			// jsk3
			updateUrl = move.jiangsuk3_currentTime;
			timerOpenStr = "timer-jiangsuk3open";
			timerInitStr = "timer-jiangsuk3";
		} else if (lotteryObj.currentType == 653) {
			// bjk3
			updateUrl = move.bjk3_currentTime;
			timerOpenStr = "timer-bjk3open";
			timerInitStr = "timer-bjk3";
		} else if (lotteryObj.currentType == 551) {
			// gd115/gdpick11
			updateUrl = move.gd115_currentTime;
			timerOpenStr = "timer-gd115open";
			timerInitStr = "timer-gd115";
		} else if (lotteryObj.currentType == 552) {
			// jxpick11
			updateUrl = move.jxPick11_currentTime;
			timerOpenStr = "timer-jx115open";
			timerInitStr = "timer-jx115";
		} else if (lotteryObj.currentType == 553) {
			// sdpick11
			updateUrl = move.sdPick11_currentTime;
			timerOpenStr = "timer-sd115open";
			timerInitStr = "timer-sd115";
		} else if (lotteryObj.currentType == 356) {
			// gxk10
			updateUrl = move.gxk10_currentTime;
			timerOpenStr = "timer-gxk10open";
			timerInitStr = "timer-gxk10";
		} else if (lotteryObj.currentType == 357) {
			// cqk10
			updateUrl = move.cqk10_currentTime;
			timerOpenStr = "timer-cqk10open";
			timerInitStr = "timer-cqk10";
		} else if (lotteryObj.currentType == 359) {
			// liuhecai/marksix
			updateUrl = move.liuhecai_currentTime;
			timerOpenStr = "timer-liuhecaiopen";
			timerInitStr = "timer-liuhecai";
		} else if (lotteryObj.currentType == 360) {
			// sflhc
			updateUrl = move.sflhc_currentTime;
			timerOpenStr = "timer-sflhcopen";
			timerInitStr = "timer-sflhc";
		} else if (lotteryObj.currentType == 355) {
			// gdk10
			updateUrl = move.gdK10_currentTime;
			timerOpenStr = "timer-gdk10open";
			timerInitStr = "timer-gdk10";
		} else if (lotteryObj.currentType == 152) {
			// sfpk10
			updateUrl = move.sfPk10_currentTime;
			timerOpenStr = "timer-sfpk10open";
			timerInitStr = "timer-sfpk10";
		}else if (lotteryObj.currentType == 358) {
			// kuailepuke3/poker
			updateUrl = move.kuailepuke3_currentTime;
			timerOpenStr = "timer-kuailepuke3open";
			timerInitStr = "timer-kuailepuke3";
		} else if (lotteryObj.currentType == 258) {
			// five
			updateUrl = move.five_currentTime;
			timerOpenStr = "timer-fiveopen";
			timerInitStr = "timer-five";
		} else if (lotteryObj.currentType == 255) {
			// bjssc
			updateUrl = move.bjssc_currentTime;
			timerOpenStr = "timer-bjSscopen";
			timerInitStr = "timer-bjSsc";
		} else if (lotteryObj.currentType == 251) {
			// cqssc
			updateUrl = move.cqSsc_currentTime;
			timerOpenStr = "timer-cqSscopen";
			timerInitStr = "timer-cqSsc";
		} else if (lotteryObj.currentType == 155) {
			// jsft
			updateUrl = move.jsft_currentTime;
			timerOpenStr = "timer-jsftopen";
			timerInitStr = "timer-jsft";
		} else if (lotteryObj.currentType == 154) {
			// xyft
			updateUrl = move.xyft_currentTime;
			timerOpenStr = "timer-xyftopen";
			timerInitStr = "timer-xyft";
		} else if (lotteryObj.currentType == 153) {
			// sfpk102
			updateUrl = move.sfPk102_currentTime;
			timerOpenStr = "timer-sfpk102open";
			timerInitStr = "timer-sfpk102";
		} else if (lotteryObj.currentType == 661) {
			// jxk3
			updateUrl = move.jxk3_currentTime;
			timerOpenStr = "timer-jxk3open";
			timerInitStr = "timer-jxk3";
		}

		var map = {};
		var mw = baseObj.mw(map);
		$.ajax({
			type : "POST",
			url : updateUrl,
			dataType : 'json',
			data : {
				mw : mw
			},
			success : function(ret) {
				var code = ret.code;
				if (code == '200') {
					var data = ret.data;
					var obj = data.obj;
					sessionNo = obj.sessionNo;
					// var preSessionNo=data.preSessionNo;
					// var noArray=data.noArray;
					var betTime = obj.betTime;// 截止投注时间
					var openTime = obj.openTime;// 距离当前期开奖时间
					var resultItems = obj.openResult;// 开奖结果
					timerObj.rest = betTime;
					timerObj.openrest = openTime;
					var awardDate = data.awardDate;// 兑奖截止时间
					var openDate = data.openDate;// 开奖时间
					var tempsession = $('.latest .sessionNo').text();
					var gains = obj.gains;// 当前收益
					var lastSessionNo = obj.lastSessionNo;// 当前收益
					lotteryObj.sessionNo = sessionNo;
					$('#openDate').text(openDate);
					$('#awardDate').text(awardDate);
					// $('#sessionNo').text(sessionNo);
					$(".sessionNo").each(function() {
						$(this).text(lastSessionNo);
					});
					$("#sessionNo").text(sessionNo);
					$('#gains').html(gains);
					$('.cur_turn_num').text(parseInt(sessionNo) - 1);
					// 更新开奖结果html
					
					html = lotteryObj.getXyOpenResultHtml(
							lotteryObj.currentType, resultItems);
					

					$(".open-number").each(function() {
						$(this).html(html);
					});

					var html = '';
					var length = resultItems.length;
					if (length == 0) {
						if (timeropenresult == null) {
							timeropenresult = window.setInterval(function() {
								timerObj.result(timerOpenStr);
							}, timerObj.intervalAPI);
						}
					} else {
						window.clearInterval(timeropenresult);
						timeropenresult = null;
					}
					if (timerxiazhu == null) {
						timerxiazhu = window.setInterval(function() {
							timerObj.init(timerObj.rest, timerInitStr);
						}, timerObj.interval);
					}
					if (timeropen == null) {
						timeropen = window.setInterval(function() {
							timerObj.open(timerObj.openrest, timerOpenStr);
						}, timerObj.interval);
					}

					$("#openStatus").val("1");
				} else {
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
			}
		});
	},
	submit : function() {
		optionArray = new Array();

		var num = 0;
		var shtml = "";
		// shtml += "<span class=\"el-dialog__title\">下注明细 (请确认注单)</span>";
		shtml += "<div>";
		// style=\"max-height: 380px; overflow-y: auto;margin:10px\"
		shtml += "<div >";
		shtml += "<table class=\"u-table10\">";
		shtml += "<thead><tr>";
		shtml += "<th style=\"width: 60%;\">号码</th>";
		shtml += "<th style=\"width: 15%;\">赔率</th>";
		shtml += "<th style=\"width: 15%;\">金额</th>";
		// shtml += "<th style=\"width: 10%;\">确认</th>";
		shtml += "</tr></thead>";
		shtml += "<tbody>";

		var groupNum = 0;
		var totalprice = 0;
		$(".amount input").each(function() {
			// 获取到当前区域的输入框并遍历
			if ($(this).val().length > 0) {
				// 下注
				var numval = $.trim($(this).val());
				var id = $.trim($(this).attr('data'));
				var bs = $.trim($(this).attr('bs'));
				var bstitle = $.trim($(this).attr('bstitle'));
				var title = $.trim($(this).attr('title'));
				if (numval != '') {
					shtml += "<tr>";
					shtml += "<td class=\"multiple\" style=\"text-align: left; padding-left: 5px;\">"
							+ title + " " + bstitle + "</td>";
					shtml += "<td ><span class=\"c-txt3\">"
							+ bs + "</span></td>";
					shtml += "<td ><span class=\"c-txt3\">"
							+ numval + "</span></td>";
					// shtml += "<td><input type=\"number\"
					// class=\"el-tooltip\" style=\"width:
					// 85%;\" value=\""+numval+"\"></td>";
					// shtml += "<td><input type=\"checkbox\"
					// checked=\"true\"></td>";
					shtml += "</tr>";
					// html=html+'<span class="con">'+title+'
					// '+bstitle+'</span>';
					optionArray[num] = bettingObj(id, numval);
					num++;
					groupNum++;
					totalprice += parseInt(numval);
					// totalprice=parseFloat(totalprice)+parseFloat(numval);
					// jiangjin=parseFloat(jiangjin)+parseFloat(numval*bs);//奖金
				}

			}
		});
		if (groupNum == 0) {
			var tables = $(".Ball_List");
			var numval = $(".bet-money").val();

			var title = $(".select-area .selected");
			var cate = title.attr("cate");
			var pt = title.attr("playtype");

			for (var i = 1; i < tables.length; i++) {
				// 获取单个table，查找其中被选中的项
				var tmpTable = tables[i];
				var heads = $(tmpTable).find("th");
				var head = heads[0];
				var headStr = $(head).text();
				var title = headStr.substring(0, 3);
				var bs = headStr.substring(3, headStr.length);
				var nums = "";

				var selectedTd = $(tmpTable).find("td.bg-yellow");
				var p4 = $(tmpTable).parent();
				var p5 = p4.parent();
				var index1 = p4.index();
				var index2 = p5.index();
				var index3 = index2 * 4 + index1;
				var ids = "";
				// 判断数目是否合理
				var target;
				for (var k = 0; k < lotteryObj.maxBetNum.length; k++) {
					var tmp = lotteryObj.maxBetNum[k];
					if (tmp.c == cate && tmp.p == pt) {
						target = tmp;
					}
				}
				if (selectedTd.length > 0) {
					if (selectedTd.length < target.n[index3]) {
						mui.alert("请添加完整投注内容", "提示");
						return false;
					} else if (selectedTd.length > target.n[index3]) {
						mui.alert("投注内容过多", "提示");
						return false;
					}
				}

				// 投注项数目正确
				for (var j = 0; j < selectedTd.length; j++) {
					var tmpTd = selectedTd[j];
					var tmpId = $(tmpTd).attr("data");
					var tmpNum = $(tmpTd).attr("num");
					ids = ids + tmpId + ",";
					nums = nums + tmpNum + ",";
				}
				if (ids.length > 0) {
					nums = nums.substring(0, nums.length - 1);

					shtml += "<tr>";
					shtml += "<td class=\"multiple\" style=\"text-align: left; padding-left: 5px;\">"
							+ title + " " + nums + "</td>";
					shtml += "<td ><span class=\"c-txt3\">" + bs
							+ "</span></td>";
					shtml += "<td ><span class=\"c-txt3\">" + numval
							+ "</span></td>";
					// shtml += "<td><input type=\"number\" class=\"el-tooltip\"
					// style=\"width: 85%;\" value=\""+numval+"\"></td>";
					// shtml += "<td><input type=\"checkbox\"
					// checked=\"true\"></td>";
					shtml += "</tr>";

					ids = ids.substring(0, ids.length - 1);
					var obj = new Object();
					obj.ids = ids;
					obj.p = numval;
					optionArray[num] = obj;
					num++;
					groupNum++;
					totalprice += parseInt(numval);
				}

			}

		}

		shtml += "</tbody>";
		shtml += "</table>";
		shtml += "</div>";
		shtml += "<div class=\"bet-bottom\" style=\"margin-top: 10px;\">";
		shtml += "<span class=\"bcount\">组数：<b class=\"bcountVal\">" + groupNum
				+ "</b></span>";
		shtml += "<span>&nbsp;</span><span>&nbsp;</span>";
		shtml += "<span class=\"bcount\">金额：<b class=\"bcountVal\">"
				+ totalprice + "</b></span>";
		shtml += "</div>";
		shtml += "<div class=\"cont-col3-hd clearfix\">";
		shtml += "<button  class=\"u-btn1 bet\">确定</button>";
		shtml += "&nbsp;&nbsp;&nbsp;&nbsp;";
		shtml += "<button  class=\"u-btn1 cancel\">取消</button>";
		shtml += "</div>";
		if (optionArray.length > 0) {
			// var mask=move.createLoading();
			// mask.show();
			// if($(".masktrans").length>0){
			// $(".masktrans").css("display","block");
			// }
			$("#confirm-box").html(shtml);
			$(".el-dialog__wrapper").css("display", "block");
			$(".masktrans").show();

			$(".bet").bind("click", function() {
				shtml = "";
				$("#confirm-box").html(shtml);
				$(".el-dialog__wrapper").css("display", "none");
				lotteryObj.bet();
			});
			$(".cancel").bind("click", function() {
				shtml = "";
				$("#confirm-box").html(shtml);
				$(".el-dialog__wrapper").css("display", "none");
				$(".masktrans").hide();
				optionArray = {};

				$(".bet").each(function() {
					$(this).unbind("click");
				});
				$(".cancel").each(function() {
					$(this).unbind("click");
				});
			});
		}
	},
	submit2 : function() {
		// 用于提交部分需要排列组合投注项的玩法
		optionArray = new Array();

		var betmoney = $(".bet-money").val();
		if(!betmoney){
			mui.alert("请填写投注金额");
			return false;
		}
		var num = 0;
		var shtml = "";
		// shtml += "<span class=\"el-dialog__title\">下注明细 (请确认注单)</span>";
		shtml += "<div>";
		// style=\"max-height: 380px; overflow-y: auto;margin:10px\"
		shtml += "<div >";
		shtml += "<table class=\"u-table10\">";
		shtml += "<thead><tr>";
		shtml += "<th style=\"width: 60%;\">号码</th>";
		shtml += "<th style=\"width: 15%;\">赔率</th>";
		shtml += "<th style=\"width: 15%;\">金额</th>";
		// shtml += "<th style=\"width: 10%;\">确认</th>";
		shtml += "</tr></thead>";
		shtml += "<tbody>";

		var groupNum = 0;
		var totalprice = 0;

		if(lotteryObj.currentType==355||lotteryObj.currentType==356||lotteryObj.currentType==357){
			//gdk10
			// 任选2()、任选2组、任选3、任选4、任选5
			var bettingNumLimit = [];

			var optionList = $(".Ball_List");
			var optionNumArr = [];
			for(var i = 0;i<optionList.length;i++){
				var tmpList = optionList[i];
				var iArr = $(tmpList).find(".bg-yellow i");

				optionNumArr.push({
					id:i,
					num:(iArr?iArr.length:0),
					itemArr:(iArr?iArr:null)
				});
			}

			for(var i = 0;i<optionNumArr.length;i++){
				var tmp = optionNumArr[i];
				if(tmp.id==0&&tmp.num!=0){
					// 任选2 
					if(tmp.num<2){
						mui.alert("任选二至少要选择2个号码");
						return false;
					}
					if(tmp.num>10){
						mui.alert("任选二最多选择10个号码");
						return false;
					}

				}else if(tmp.id==1&&tmp.num!=0){
					//  任选2组
					if(tmp.num<2){
						mui.alert("任选二组至少要选择2个号码");
						return false;
					}
					if(tmp.num>6){
						mui.alert("任选二组最多选择6个号码");
						return false;
					}
				}else if(tmp.id==2&&tmp.num!=0){
					// 任选三
					if(tmp.num<3){
						mui.alert("任选三至少要选择3个号码");
						return false;
					}
					if(tmp.num>6){
						mui.alert("任选三最多选择6个号码");
						return false;
					}
				}else if(tmp.id==3&&tmp.num!=0){
					// 任选四
					if(tmp.num<4){
						mui.alert("任选四至少要选择4个号码");
						return false;
					}
					if(tmp.num>7){
						mui.alert("任选四最多选择7个号码");
						return false;
					}
				}else if(tmp.id==4&&tmp.num!=0){
					// 任选五
					if(tmp.num<5){
						mui.alert("任选四至少要选择5个号码");
						return false;
					}
					if(tmp.num>8){
						mui.alert("任选四最多选择8个号码");
						return false;
					}
				}
			}

			for(var i = 0;i<optionNumArr.length;i++){
				var tmp = optionNumArr[i];
				if(tmp.itemArr!=null&&tmp.itemArr.length>0){

					var firstItem = tmp.itemArr[0];

					var numval = $.trim($(".bet-money").val());
					var id = $.trim($(firstItem).attr('data'));
					var bs = $.trim($(firstItem).attr('bs'));
					var bstitle = "";
					var title = $.trim($(firstItem).attr('title'));

					for(var j=0;j<tmp.itemArr.length;j++){
						var tmpItem = tmp.itemArr[j];
						bstitle += $.trim($(tmpItem).attr('bstitle')).replace("号","");
						if(j<tmp.itemArr.length-1){
							bstitle += ",";
						}
						var tmpId = $.trim($(tmpItem).attr('data'));
						optionArray[num] = bettingObj(tmpId, numval);
						num++;
					}

					shtml += "<tr>";
					shtml += "<td class=\"multiple\" style=\"text-align: left; padding-left: 5px;\">"
							+ title + " " + bstitle + "</td>";
					shtml += "<td ><span class=\"c-txt3\">"
							+ bs + "</span></td>";
					shtml += "<td ><span class=\"c-txt3\">"
							+ numval + "</span></td>";
					shtml += "</tr>";

					// 排列组合计算后的注数
					var combindNum = 0;

					var weight = 2;
					if(title=="任选二"){
						weight = 2;
					}else if(title=="任选二组"){
						weight = 2;
					}else if(title=="任选三"){
						weight = 3;
					}else if(title=="任选四"){
						weight = 4;
					}else{
						weight = 5;
					}

					var numerator = 1;
					for(var m = 0;m<weight;m++){
						numerator = numerator * (tmp.itemArr.length-m);
					}
					var denominator = 1;
					for(var n = weight;n>0;n--){
						denominator = denominator * n;
					}

					groupNum += (numerator / denominator);
					totalprice += (numerator / denominator)*numval;
				}
			}

		}

		shtml += "</tbody>";
		shtml += "</table>";
		shtml += "</div>";
		shtml += "<div class=\"bet-bottom\" style=\"margin-top: 10px;\">";
		shtml += "<span class=\"bcount\">组数：<b class=\"bcountVal\">" + groupNum
				+ "</b></span>";
		shtml += "<span>&nbsp;</span><span>&nbsp;</span>";
		shtml += "<span class=\"bcount\">金额：<b class=\"bcountVal\">"
				+ totalprice + "</b></span>";
		shtml += "</div>";
		shtml += "<div class=\"cont-col3-hd clearfix\">";
		shtml += "<button  class=\"u-btn1 bet\">确定</button>";
		shtml += "&nbsp;&nbsp;&nbsp;&nbsp;";
		shtml += "<button  class=\"u-btn1 cancel\">取消</button>";
		shtml += "</div>";
		if (optionArray.length > 0) {
			// var mask=move.createLoading();
			// mask.show();
			// if($(".masktrans").length>0){
			// $(".masktrans").css("display","block");
			// }
			$("#confirm-box").html(shtml);
			$(".el-dialog__wrapper").css("display", "block");
			$(".masktrans").show();

			$(".bet").bind("click", function() {
				shtml = "";
				$("#confirm-box").html(shtml);
				$(".el-dialog__wrapper").css("display", "none");
				lotteryObj.bet();
			});
			$(".cancel").bind("click", function() {
				shtml = "";
				$("#confirm-box").html(shtml);
				$(".el-dialog__wrapper").css("display", "none");
				$(".masktrans").hide();
				optionArray = {};

				$(".bet").each(function() {
					$(this).unbind("click");
				});
				$(".cancel").each(function() {
					$(this).unbind("click");
				});
			});
		}
	},
	bet : function() {
		var potions = JSON.stringify(optionArray);
		var sessionNo = $('#sessionNo').text();
		if (potions == 'null' || potions == '') {
			alert('请选择要下注的内容');
			return;
		}
		if (move.user.u != null) {
			var openStatus = $('#openStatus').val();
			if (openStatus == '1') {// 正常可投注
				var map = {};
				map['u'] = move.user.u;
				map['sessionNo'] = sessionNo;
				map['optionArray'] = potions;
				var mw = baseObj.mw(map);
				mask.show();
				var betUrl = "";

				if (lotteryObj.currentType == 256) {
					// bj3
					betUrl = move.bj3_bet;
				} else if (lotteryObj.currentType == 151) {
					// bjpk10
					betUrl = move.bjPk10_bet;
				} else if (lotteryObj.currentType == 352) {
					// xjpLu28
					betUrl = move.xjpLu28_bet;
				} else if (lotteryObj.currentType == 353) {
					// pcdd / bjlu28
					betUrl = move.bjLu28_bet;
				} else if (lotteryObj.currentType == 251) {
					// cqssc
					betUrl = move.cqSSc_bet;
				} else if (lotteryObj.currentType == 651) {
					// jsk3
					betUrl = move.jiangsuk3_bet;
				} else if (lotteryObj.currentType == 653) {
					// bjk3
					betUrl = move.bjk3_bet;
				} else if (lotteryObj.currentType == 551) {
					// gd115/gdpick11
					betUrl = move.gd115_bet;
				} else if (lotteryObj.currentType == 552) {
					// jxpick11
					betUrl = move.jxPick11_bet;
				} else if (lotteryObj.currentType == 553) {
					// sdpick11
					betUrl = move.sdPick11_bet;
				} else if (lotteryObj.currentType == 356) {
					// gxk10
					betUrl = move.gxk10_bet;
				} else if (lotteryObj.currentType == 357) {
					// cqk10
					betUrl = move.cqk10_bet;
				} else if (lotteryObj.currentType == 359) {
					// liuhecai/marksix
					betUrl = move.liuhecai_bet;
				} else if (lotteryObj.currentType == 360) {
					// sflhc
					betUrl = move.sflhc_bet;
				} else if (lotteryObj.currentType == 355) {
					// gdk10
					betUrl = move.gdK10_bet;
				} else if (lotteryObj.currentType == 152) {
					// sfpk10
					betUrl = move.sfPk10_bet;
				}else if (lotteryObj.currentType == 358) {
					// kuailepuke3/poker
					betUrl = move.kuailepuke3_bet;
				} else if (lotteryObj.currentType == 258) {
					// five
					betUrl = move.five_bet;
				} else if (lotteryObj.currentType == 255) {
					// bjssc
					betUrl = move.bjssc_bet;
				} else if (lotteryObj.currentType == 251) {
					// cqssc
					betUrl = move.cqSSc_bet;
				}  else if (lotteryObj.currentType == 252) {
					// xjssc
					betUrl = move.xjSSc_bet;
				}  else if (lotteryObj.currentType == 253) {
					// tjssc
					betUrl = move.tjSSc_bet;
				} else if (lotteryObj.currentType == 155) {
					// jsft
					betUrl = move.jsft_bet;
				} else if (lotteryObj.currentType == 154) {
					// xyft
					betUrl = move.xyft_bet;
				} else if (lotteryObj.currentType == 153) {
					// sfpk102
					betUrl = move.sfPk102_bet;
				} else if (lotteryObj.currentType == 661) {
					// jxk3
					betUrl = move.jxk3_bet;
				}
				$.ajax({
					type : "POST",
					url : betUrl,
					dataType : 'json',
					data : {
						mw : mw
					},
					success : function(ret) {
						var code = ret.code;
						var msg = ret.msg;
						if (code == '200') {
							var data = ret.data;
							var money = data.money;
							$('.logined .money').text(money);
							$(".amount input").each(function() {
								$(this).val("");
							});
							mask.close();
							mui.alert(msg, document.title, function() {
							});

							$(".Ball_List td").each(function() {
								$(this).removeClass("bg-yellow");
							});
							// lotteryObj.betRecords();
							// lotteryObj.userInfo();

						} else {
							mui.alert(msg, document.title, function() {
							});
							mask.close();
							$('#money').val('');
							// $('.form-box').hide();
							// $('.masktrans').hide();
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						mui.toast('网络出错', {
							duration : 'long',
							type : 'div'
						});
						mask.close();
						// $('.form-box').hide();
						// $('.masktrans').hide();
					}
				});
			} else {
				// var mask=move.createLoading();
				mui.toast('当前期已封盘', {
					duration : 'long',
					type : 'div'
				});
				mask.close();
			}
		} else {
			var directUrl = location.href;
			// directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect=' + directUrl)
			return;
		}
	},
	seal : function() {
		// var mask=move.createLoading();
		// mask.show();
		$(".submitbtn").hide();
		$(".amount input").each(function() {
			$(this).val("--封盘--");
			$(this).attr("disabled", "true");
		});
		$(".amount i").each(function() {
			$(this).removeClass("square");
			$(this).addClass("seal");
			$(this).attr("disabled", "true");
		});
		$(".Ball_List td").each(function() {
			$(this).removeClass("bg-yellow");
			$(this).unbind("click");
		});
		var shtml = "<span>已封盘</span>";
		$("#bet-status").html(shtml);
		$("#openStatus").val("0");
		$("#minute").html("00");
		$("#sec").html("00");
		$('.submitbtn').hide();
		// mask.close();
	},
	
	winRanking: function() {
		var map = {};
		var mw = baseObj.mw(map);
		$.ajax({
			type : "POST",
			url : move.cqSsc_winRanking,
			dataType : 'json',
			data : {
				mw : mw
			},
			success : function(ret) {
				var code = ret.code;
				if (code == '200') {
					var result = ret.data;
					var items = result.items;
					var html = '';
					for (var i = 0; i < items.length; i++) {
						var obj = items[i];
						var userName = obj.userName;
						var totalMoney = obj.totalMoney;
						html = html + '<tr><td>' + (i + 1) + '</td>';
						html = html + '<td>' + userName + '</td>';
						html = html + '<td><span class="red">' + totalMoney
								+ '</span>元</td>';
						html = html + '</tr>';
					}
					$('#winninglist').html(html);
				} else {
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
			}
		});
	},
	userInfo : function() {
		mask.show();
		var map = {};
		map['u'] = move.user.u;
		var mw = baseObj.mw(map);
		$.ajax({
			type : "POST",
			url : move.money,
			dataType : 'json',
			data : {
				mw : mw
			},
			success : function(ret) {
				mask.close();
				var code = ret.code;
				var data = ret.data;
				var html = '';
				if (code == '200') {
					var data = ret.data;
					$("#balance").html(data.money);
					$("#userinfo_name").html(move.user.userName);
				}
				mask.close();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	clearTimer : function() {
		// lotteryObj.countNum++;
		window.clearInterval(timeropenresult);
		timeropenresult = null;
		window.clearInterval(timerxiazhu);
		timerxiazhu = null;
		window.clearInterval(timeropen);
		timeropen = null;
	},
	getXyOpenResultHtml : function(playtype, resultItems) {
		// 传入游戏类型与开奖结果，返回开奖结果的html
		var shtml = "";
		var width = 5;
		if (playtype == 255 || playtype == 251 || playtype == 252 || playtype == 253 || playtype == 258 || playtype == 358
				|| playtype == 551 || playtype == 552 || playtype == 553 || playtype == 16) {
			// 5个开奖数字

		} else if (playtype == 151 || playtype == 155 || playtype== 154||playtype==153 ||playtype==152) {
			// 10开奖数字
			width = 10;
		} else if (playtype == 13) {
			// 6开奖数字
			width = 6;
		} else if (playtype == 661 || playtype == 651 || playtype == 256|| playtype == 653) {
			// 3开奖数字
			width = 3;
		}

		var bgCls = playtype == 151 || playtype == 155 || playtype== 154||playtype==153 ||playtype==152 ? "pkn-" : "bg-red";

		if (resultItems.length > 0) {
			for (var j = 0; j < resultItems.length && j < width; j++) {
				shtml += "<span class=\"num "
						+ (bgCls == "pkn-" ? "num-s" : "")
						+ " "
						+ (bgCls == "pkn-" ? bgCls + parseInt(resultItems[j])
								: bgCls) + "\">" + resultItems[j] + "</span>";
			}
		} else {
			for (var j = 0; j < width; j++) {
				shtml += "<span class=\"num "
						+ (bgCls == "pkn-" ? "num-s" : "")
						+ " bg-red\">?</span>";
			}
		}

		return shtml;
	},
	getDXHtml : function(playtype, resultItems) {
		// 大小
		// 传入玩法，结果，返回大小的html
		var shtml = "";
		var i = 0;
		if (playtype == 4) {
			// pcdd
			i = 1;
		}
		for (i; i < resultItems.length; i++) {
			if (resultItems[i] == "-") {
				shtml += "<td>-</td>";
			} else {
				shtml += "<td>";
				if (resultItems[i] == "大") {
					shtml += "<span class=\"colour-red\">" + resultItems[i]
							+ "</td>";
				} else if (resultItems[i] == "双") {
					shtml += "<span class=\"colour-red\">" + resultItems[i]
							+ "</td>";
				} else {
					shtml += "<span >" + resultItems[i] + "</td>";
				}
				shtml += "</td>";
			}
		}
		return shtml;
	},
	getLHHtml : function(playtype, resultStr) {
		// 龙虎
		// 传入龙虎，结果，返回大小的html
		var resultItems = [];
		if (playtype != 4 && playtype != 6) {
			// pcdd/jsk3以外
			for (var i = 0; i < resultStr.length; i++) {
				resultItems[i] = resultStr.substring(i, (i + 1));
			}
		} else {
			resultItems[0] = resultStr;
		}

		var shtml = "";
		for (var i = 0; i < resultItems.length; i++) {
			if (resultItems[i] == "-") {
				shtml += "<td>-</td>";
			} else {
				shtml += "<td>";
				if (resultItems[i] == "龙") {
					shtml += "<span class=\"colour-red\">" + resultItems[i]
							+ "</td>";
				} else {
					shtml += "<span >" + resultItems[i] + "</td>";
				}
				shtml += "</td>";
			}

		}
		return shtml;
	},
	bindPageEvents : function() {
		// 提交下注
		$(".submitbtn").bind("click", function() {
			var buytype = $(this).val();
			lotteryObj.submit();
		});
		// 清空选号
		$("a.clean").bind("click", function() {
			$(".amount input").each(function() {
				var tmp = $(this);
				tmp.val("");

			});
			$(".Ball_List TD").each(function() {
				$(this).removeClass("bg-yellow");
			});
		});
		// 同步金额
		$(".bet-money").keyup(function() {
			var moneyEl = $(this);
			if (moneyEl) {
				var money = moneyEl.val();
				$(".bet-money").each(function() {
					$(this).val(money);
				});
				$(".bg-yellow input").each(function() {
					$(this).val(money);
				});
			}
		});
		
	},
	checkNum : function(numStr) {
		var itemStr = "";
		if (typeof (numStr) == "number") {
			numStr = "" + numStr;
		}
		if (numStr != '0' && numStr.indexOf('0') == 0) {
			itemStr = numStr.substring(1, 2);
		} else {
			itemStr = numStr;
		}
		return itemStr;
	},
		
	checkNumByChar : function(str) {
		for (var i = 0; i < str.length; i++) {
			var tmpChar = str.substr(i, 1);
			if (isNaN(parseInt(tmpChar))) {
				return false;
			}
		}
		return true;
	}

}


/**
 * 计算注数，M选N个数
 */
function getTotalNum(m, n) {
	var fenzi = 1;
	var fenmu = 1;
	for (var k = m; k >= (m - n + 1); k--) {
		fenzi = fenzi * k;
	}
	for (var k = 1; k <= n; k++) {
		fenmu = fenmu * k;
	}
	return fenzi / fenmu;
}
/**
 * 计算注数，M选N个数
 */
function getTotalNumZusan(m, n) {
	var fenzi = 1;
	var fenmu = 1;
	for (var k = m; k >= (m - n + 1); k--) {
		fenzi = fenzi * k;
	}
	for (var k = 1; k <= n; k++) {
		fenmu = fenmu * k;
	}
	return (fenzi / fenmu) * 2;
}
