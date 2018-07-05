var sFilter = {//select通用过滤代码
	focus:function(obj,target){
		if(obj.value.trim()=="输入关键字...") obj.value="";
	},
	blur:function(obj,target,func){
		if(!obj.value) obj.value="输入关键字...";
		if(obj.value && obj.value.trim()!="输入关键字...") sFilter.filterImpl(obj,target);
		if(func) func.call();
	},
	filter:function(obj,target,func){//obj:关键字对象 taget:下拉列表
		if(!target) return;
		sFilter.focus(obj,target);
		sFilter.initData(target);
		if(!obj.isblur) obj.onblur = function(){sFilter.blur(this,target,func);this.isblur = true;}
		if(!obj.iskeyup) obj.onkeyup = function(){sFilter.filterImpl(this,target);this.iskeyup = true;}
		
	},
	initData:function(target){
		if(!sFilter[target.name] || sFilter[target.name].length==0){
			sFilter[target.name] = [];
			for(var i=0;i<target.options.length;i++){
				sFilter[target.name].push({id:target.options[i].value,tx:target.options[i].text});
			}
		}
	},
	filterImpl:function(obj,target){
		if(!target.cwidth) target.cwidth = target.clientWidth;
		if(target.cwidth) target.style.width = (target.cwidth+2)+"px";
		target.innerHTML = "";
		if(obj.value.trim().length>0){
			for(var i=0;i<sFilter[target.name].length;i++){
				if(obj.value && sFilter[target.name][i].tx.indexOf(obj.value.trim())>-1){
					target.add(new Option(sFilter[target.name][i].tx,sFilter[target.name][i].id));
				}
			}
		}else{
			for(var i=0;i<sFilter[target.name].length;i++){
				target.add(new Option(sFilter[target.name][i].tx,sFilter[target.name][i].id)); 
			}
		}
	}
};