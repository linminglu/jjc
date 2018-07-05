	//回到顶部
	$(document).ready(function(){
			var ie6 = !window.XMLHttpRequest;//判断浏览器是否为ie6；
			var a = document.getElementById('plane');
			if(a!=null){
				a.style.position = ie6 ? 'absolute' : 'fixed';
				if (ie6) {
					window.onscroll = function() {
						a.className = a.className;
					};
				}
				function fixed_goback(){
					var y =$(window).scrollTop(); //滚动条跟顶部的距离超过100px返回顶部代码出现，否则隐藏。
					if(y > 100){
						$("#plane").show();
					}else{
						$("#plane").hide();
					}
				}
				$('#plane').click(function(){$('html,body').animate({scrollTop: '0px'}, 500);return false;});
				$(window).scroll( function(){fixed_goback();} );//滚动滚动条时触发fixed_goback()
	    		$(window).resize( function(){fixed_goback();} );//改变浏览器大小时触发fixed_goback()
			}
	});
