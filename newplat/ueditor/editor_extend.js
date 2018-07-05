var UECreator = {
	def:function(editorId,width,height,autoHeight){
		UECreator.editor = new UE.ui.Editor({
			wordCount:false,
			initialFrameWidth:width?width:"100%",
		    initialFrameHeight:height?height:320,
			minFrameHeight:height?height:320,
			autoHeightEnabled:autoHeight?true:false
		});
		UECreator.editor.render(editorId);
		UECreator.editor.ready(function(){
			UECreator.editor.addListener("beforesubmit",function(){
	    		alert("beforesubmit");
	    	});
		});
	},
	ques:function(editorId,width,height,autoHeight){
		UECreator.editor = new UE.ui.Editor({
			wordCount:false,
			toolbars:[[
			           	'source','|',
		                'bold', 'italic', 'underline','|',
		                'forecolor', 'backcolor','|',
		                'insertimage','bxsptextbox'
		             ]],
	        initialFrameWidth:width?width:500,
	        initialFrameHeight:height?height:320,
			//minFrameHeight:height?height:320,
	        minFrameHeight:height?height:320,
			autoHeightEnabled:autoHeight?true:false
		});
		UECreator.editor.render(editorId);
	},
	options:function(editorId,width,height,autoHeight){
		UECreator.editor = new UE.ui.Editor({
			wordCount:false,
			toolbars:[[
			           	'source',
			            'bold', 'italic', 'underline',
		                'insertimage'
		             ]],
	        initialFrameWidth:width?width:500,
	        initialFrameHeight:height?height:320,
			//minFrameHeight:height?height:320,
	        minFrameHeight:height?height:320,
			autoHeightEnabled:autoHeight?true:false
		});
		UECreator.editor.render(editorId);
	},
	mini:function(editorId,width,height,autoHeight){
		UECreator.editor = new UE.ui.Editor({
			wordCount:false,
			toolbars:[[
		                'bold', 'italic', 'underline','|',
		                'forecolor', 'backcolor','|',
		                'fontfamily', 'fontsize'
		             ]],
		    initialFrameWidth:width?width:500,
		    initialFrameHeight:height?height:320,
			minFrameHeight:height?height:320,
			autoHeightEnabled:autoHeight?true:false
		});
		UECreator.editor.render(editorId);
	},
	discuss:function(editorId,width,height,autoHeight){
		UECreator.editor = new UE.ui.Editor({
			wordCount:false,
			toolbars:[[
		                'emotion','|','bold', 'italic', 'underline','|',
		                'forecolor', 'backcolor','|',
		                'fontfamily', 'fontsize'
		             ]],
	        initialFrameWidth:width?width:500,
	        initialFrameHeight:height?height:320,
			//minFrameHeight:height?height:320,
	        minFrameHeight:height?height:320,
			autoHeightEnabled:autoHeight?true:false
		});
		UECreator.editor.render(editorId);
	},
	emotion:function(editorId,width,height,autoHeight,maxWord){
		UECreator.editor = new UE.ui.Editor({
			toolbars:[['emotion']],
		    wordCount:maxWord?true:false,
			maximumWords:maxWord,
		    wordCountMsg:'可输入{#leave} 个字',
		    wordOverFlowMsg:'<span style=\'color:red;\'>已超出100个字！</span>',
		    initialFrameWidth:width?width:500,
		    initialFrameHeight:height?height:320,
		    //minFrameWidth:width?width:500,
			minFrameHeight:height?height:320,
			autoHeightEnabled:autoHeight?true:false
		});
		UECreator.editor.render(editorId);
	},
	limitWord:function(editorId,widthStyle,height,autoHeight,max){
		if(widthStyle){//100% or number
			UECreator.editorHidden = document.getElementById(editorId);
			if(UECreator.editorHidden) UECreator.editorHidden.style.width = widthStyle;
		}
		UECreator.editor = new UE.ui.Editor({
			wordCount:true,
			maximumWords:max,
			//wordOverFlowMsg:'字数超出范围',
			minFrameHeight:height?height:320,
			autoHeightEnabled:autoHeight?true:false
		});
		UECreator.editor.render(editorId);
	},
	formatUrl:function(contents){
		var context = (window.location.href.split("/").length==6?"/"+location.href.split("//")[1].split("/")[1]:"");
		if(context && contents.length>1){
			return contents.replace(new RegExp(context,'g'),"..");
		}
		return contents;
	},
	setFormatUrl:function(id){
		if(id){
			var contents = UECreator.formatUrl(UE.getEditor(id).getContent());
			UE.getEditor(id).setContent(contents);
		}else{
			var contents = UECreator.formatUrl(UECreator.editor.getContent());
			UECreator.editor.setContent(contents);
		}
	},
	getContentTxt:function(id){
		if(id){
			return UECreator.formatUrl(UE.getEditor(id).getContentTxt());
		}else{
			return UECreator.formatUrl(UECreator.editor.getContentTxt());
		}
	},
	getContent:function(id){
		if(id){
			return UECreator.formatUrl(UE.getEditor(id).getContent());
		}else{
			return UECreator.formatUrl(UECreator.editor.getContent());
		}
	},
	setContent:function(id,content){
		UE.getEditor(id).setContent(content);
	},
	setFocus:function(id){
		UE.getEditor(id).focus();
	},
	clear:function(id){
		UE.getEditor(id).setContent("");
	},
	insertHtml:function(id,s){
		UE.getEditor(id).execCommand("insertHtml",s)
	}
};
