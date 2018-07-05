var managerObj={
	drawingResult:function(){
		$('.result').each(function(i){
			var type=$(this).attr('data');
			var resultStr=$(this).val();
			var openResult=resultStr.split(',');
			var openhtml='';
			if(openResult.length>1){
				if(type=='251'||type=='252'||type=='253'||type=='255'||type=='256'||type=='258'){
					// 时时彩类
					for ( var j = 0; j < openResult.length; j++) {
						var num=openResult[j];
						openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
					}
				}else if(type=='151'||type=='152'||type=='153'||type=='154'||type=='155'){//北京赛车类
					for ( var j = 0; j < openResult.length; j++) {
						var num=openResult[j];
						openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
					}
				}else if(type=='352'||type=='353'){//PC蛋蛋
					//0=无波色 1=绿波 2=蓝波 3=红波
					var length=openResult.length;
					if(length==5){
						for (var j = 0; j <3; j++) {
							openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
							if(j==2){
								openhtml=openhtml+'<span class="symbol">=</span>';
							}else{
								openhtml=openhtml+'<span class="symbol">+</span>';
							}
						}
						var colorSize=openResult[4];
						var colorClass='bg-gray';
						if(colorSize=='0'){
							colorClass='bg-gray';
						}else if(colorSize=='1'){
							colorClass='bg-green';
						}else if(colorSize=='2'){
							colorClass='bg-blue';
						}else if(colorSize=='3'){
							colorClass='bg-red';
						}
						openhtml=openhtml+'<span class="num '+colorClass+'">'+openResult[3]+'</span>';
					}
				}else if(type=='355'||type=='356'||type=='357'){//广东快乐十分
					for ( var j = 0; j < openResult.length; j++) {
						var num=openResult[j];
						openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
					}
				}else if(type=='358'){//快乐扑克3
					var length=openResult.length;
					if(length==5){
						for (var j = 0; j <3; j++) {
							openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
							if(j==2){
								openhtml=openhtml+'<span class="symbol">=</span>';
							}else{
								openhtml=openhtml+'<span class="symbol">+</span>';
							}
						}
						var colorSize=openResult[4];
						var colorClass='bg-white';
						if(colorSize=='0'){
							colorClass='bg-gray';
						}else if(colorSize=='1'){
							colorClass='bg-coffee';
						}else if(colorSize=='2'){
							colorClass='bg-blue2';
						}else if(colorSize=='3'){
							colorClass='bg-violet';
						}else if(colorSize=='4'){
							colorClass='bg-green2';
						}else if(colorSize=='5'){
							colorClass='bg-red2';
						}
						openhtml=openhtml+'<span class="num '+colorClass+'">'+openResult[3]+'</span>';
					}
				}else if(type=='551'||type=='552'||type=='553'){//广东11选5类
					var length=openResult.length;
					
					for (var j = 0; j <openResult.length; j++) {
						openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
					}
					
				}else if(type=='651'||type=='653'){//江苏快三
					//0=无波色 1=绿波 2=蓝波 3=红波
					var length=openResult.length;
					for (var j = 0; j <3; j++) {
						openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
						if(j==2){
							openhtml=openhtml+'<span class="symbol">=</span>';
						}else{
							openhtml=openhtml+'<span class="symbol">+</span>';
						}
					}
					var colorClass='bg-red';
					openhtml=openhtml+'<span class="num '+colorClass+'">'+openResult[3]+'</span>';
					
				}else if(type=='359'||type=='360'){
					for (var j = 0; j <openResult.length; j++) {
						if(j==5){
							var ss=openResult[j];
							var sss=ss.split('+');
							openhtml=openhtml+'<span class="num bg-red">'+sss[0]+'</span>';
							openhtml=openhtml+'<span class="symbol">=</span>';
							openhtml=openhtml+'<span class="num bg-red">'+sss[1]+'</span>';
						}else{
							openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
						}
					}
				}
			}
			
			$(this).after('<div class="open-number">'+openhtml+'</div>');
	 	});
	}
		
}