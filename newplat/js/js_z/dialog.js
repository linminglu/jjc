var g_agt = navigator.userAgent.toLowerCase();
var is_opera = (g_agt.indexOf("opera") != -1);
var g_title = "";
var g_iframeno = 0;

function exist(s)
{
	return $(s)!=null;
}
function myInnerHTML(idname, html)
{
	if (exist(idname))
	{
		$(idname).innerHTML = html;
	}
}
function dialog(v_w, v_h, v_title)
{
	var width = v_w;
	var height = v_h;
	var title = v_title;
	g_title = title;
	
	var sClose = '<a href="javascript:void(0)" onclick="javascript:new dialog().close();">×</a>';

	var sBox = '\
		<div id="dialogBox" style="display:none;z-index:19999;width:'+width+'px;">\
			<div style="position:absolute;height:'+height+'px;top:8px;" >\
			<table border="0" cellpadding="0" cellspacing="0">\
			<tr style="height:'+(height)+'px;"><td ></td>\
			<td style="width:'+(width-14)+'px;">\
				<div style="">\
				<table width="100%" border="0" cellpadding="0" cellspacing="0">\
					<tr height="24" bgcolor="#E2E2E2">\
						<td>\
							<div class="ts3">\
								<div id="dialogBoxTitle" class="ts31">'+title+'</div>\
								<div id="dialogClose" class="ts32">' + sClose + '</div>\
							</div>\
						</td>\
					</tr>\
					<tr valign="top">\
						<td id="dialogBody" style="height:' + (height-28) + 'px" bgcolor="#FFF"></td>\
					</tr>\
				</table>\
				</div>\
			</td>\
			<td ></td></tr>\
			</table>\
			</div>\
		</div><div id="dialogBoxShadow" style="display:none;z-index:19998;"></div>\
	';
	
	var sIfram = '\
		<iframe id="dialogIframBG" name="dialogIframBG" frameborder="0" marginheight="0" marginwidth="0" hspace="0" vspace="0" scrolling="no" style="position:absolute;z-index:19997;display:none;"></iframe>\
	';
	
	var sBG = '\
		<div id="dialogBoxBG" style="position:absolute;top:0px;left:0px;width:100%;height:100%;"></div>\
	';
	
	this.init = function()
	{
		$('dialogCase') ? $('dialogCase').parentNode.removeChild($('dialogCase')) : function(){};
		var oDiv = document.createElement('span');
		oDiv.id = "dialogCase";
		if (!is_opera)
		{
			oDiv.innerHTML = sBG + sIfram + sBox;
		}
		else
		{
			oDiv.innerHTML = sBG + sBox;
		}
		document.body.appendChild(oDiv);
	}

	this.open = function(_sUrl)
	{		
		this.show();
		var openIframe = "<iframe width='100%' height='100%' name='iframe_parent' id='iframe_parent' src='" + _sUrl + "' frameborder='0' scrolling='no'></iframe>";
		myInnerHTML('dialogBody', openIframe);
	}

	this.show = function()
	{
		this.middle('dialogBox');
		if ($('dialogIframBG'))
		{
			$('dialogIframBG').style.top = $('dialogBox').style.top;
			$('dialogIframBG').style.left = $('dialogBox').style.left;
			$('dialogIframBG').style.width = $('dialogBox').offsetWidth + "px";
			$('dialogIframBG').style.height = $('dialogBox').offsetHeight + "px";
			$('dialogIframBG').style.display = 'block';
		}
		if (!is_opera) {
			this.shadow();
		}
	}
	
	this.reset = function()
	{
		this.close();
	}

	this.close = function()
	{
		if (window.removeEventListener) 
		{
			window.removeEventListener('resize', this.event_b, false);
			window.removeEventListener('scroll', this.event_b, false);
		} 
		else if (window.detachEvent) 
		{
			try {
				window.detachEvent('onresize', this.event_b);
				window.detachEvent('onscroll', this.event_b);
			} catch (e) {}
		}
		if ($('dialogIframBG')) {
			$('dialogIframBG').style.display = 'none';
		}
		$('dialogBox').style.display='none';
		$('dialogBoxBG').style.display='none';
		$('dialogBoxShadow').style.display = "none";
		if (typeof(parent.onDialogClose) == "function")
		{
			parent.onDialogClose($('dialogBoxTitle').innerHTML);
		}
	}

	this.shadow = function()
	{
		this.event_b_show();
		if (window.attachEvent)
		{
			window.attachEvent('onresize', this.event_b);
			window.attachEvent('onscroll', this.event_b);
		}
		else
		{
			window.addEventListener('resize', this.event_b, false);
			window.addEventListener('scroll', this.event_b, false);
		}
	}
	
	this.event_b = function()
	{
		var oShadow = $('dialogBoxShadow');
		
		if (oShadow.style.display != "none")
		{
			if (this.event_b_show)
			{
				this.event_b_show();
			}
		}
	}
	
	this.event_b_show = function()
	{
		var oShadow = $('dialogBoxShadow');
		oShadow['style']['position'] = "absolute";
		oShadow['style']['display']	= "";		
		oShadow['style']['opacity']	= "0.2";
		oShadow['style']['filter'] = "alpha(opacity=60)";
		oShadow['style']['background']	= "#fff";
		//var sClientWidth = parent ? parent.document.body.offsetWidth : document.body.offsetWidth;
		//var sClientHeight = parent ? parent.document.body.offsetHeight : document.body.offsetHeight;
		//var sScrollTop = parent ? (parent.document.body.scrollTop+parent.document.documentElement.scrollTop) : (document.body.scrollTop+document.documentElement.scrollTop);
		var sClientWidth = document.body.offsetWidth;
		var sClientHeight = document.body.offsetHeight;
		var sScrollTop = (document.body.scrollTop+document.documentElement.scrollTop);
		oShadow['style']['top'] = '0px';
		oShadow['style']['left'] = '0px';
		oShadow['style']['width'] = sClientWidth + "px";
		oShadow['style']['height'] = (sClientHeight + sScrollTop) + "px";
	}

	this.middle = function(_sId)
	{
		var dialogBox = $(_sId);
		dialogBox['style']['display'] = '';
		dialogBox['style']['position'] = "absolute";
		
		var sClientWidth = document.body.clientWidth;
		var sClientHeight = document.body.clientHeight;
		var sScrollTop = document.body.scrollTop+document.documentElement.scrollTop;
		//var sScrollTop = parent.document.body.scrollTop+parent.document.documentElement.scrollTop;

		var sleft = (sClientWidth - dialogBox.offsetWidth) / 2;
		var iTop = sScrollTop + 100;
		var sTop = iTop > 0 ? iTop : 0;
		
		sleft = sleft-80;//left menu width

		dialogBox['style']['left'] = sleft + "px";
		dialogBox['style']['top'] = sTop + "px";
	}
}

function dialogT(v_w, v_h, v_title)
{
	var width = v_w;
	var height = v_h;
	var title = v_title;
	g_title = title;
	
	var sClose = '<a href="javascript:void(0)" onclick="javascript:new dialog().close();">×</a>';

	var sBox = '\
		<div id="dialogBox" style="display:none;z-index:19999;width:'+width+'px;">\
			<table border="0" cellpadding="0" cellspacing="0" width="100%">\
			<tr >\
			<td >\
				<div style="">\
				<table width="100%" border="0" cellpadding="0" cellspacing="0">\
					<tr height="24" bgcolor="#E2E2E2">\
						<td>\
							<div class="ts3">\
								<div id="dialogBoxTitle" class="ts31">'+title+'</div>\
							</div>\
						<div class="ts3">\
							<div id="dialogClose" class="ts32">' + sClose + '</div>\
						</div>\
					</td>\
					</tr>\
					<tr valign="top">\
						<td id="dialogBody" style="height:' + (height-80) + 'px" bgcolor="#FFF"></td>\
					</tr>\
				</table>\
				</div>\
			</td>\
			<td ></td></tr>\
			</table>\
		</div><div id="dialogBoxShadow" style="display:none;z-index:19998;"></div>\
	';
	
	var sIfram = '\
		<iframe id="dialogIframBG" name="dialogIframBG" frameborder="0" marginheight="0" marginwidth="0" hspace="0" vspace="0" scrolling="no" style="position:absolute;z-index:19997;display:none;"></iframe>\
	';
	
	var sBG = '\
		<div id="dialogBoxBG" style="position:absolute;top:0px;left:0px;width:100%;height:100%;"></div>\
	';
	
	this.init = function()
	{
		$('dialogCase') ? $('dialogCase').parentNode.removeChild($('dialogCase')) : function(){};
		var oDiv = document.createElement('span');
		oDiv.id = "dialogCase";
		if (!is_opera)
		{
			oDiv.innerHTML = sBG + sIfram + sBox;
		}
		else
		{
			oDiv.innerHTML = sBG + sBox;
		}
		document.body.appendChild(oDiv);
	}

	this.open = function(_sUrl)
	{		
		this.show();
		var openIframe = "<iframe width='100%' height='100%' name='iframe_parent' id='iframe_parent' src='" + _sUrl + "' frameborder='0' scrolling='no'></iframe>";
		myInnerHTML('dialogBody', openIframe);
	}

	this.show = function()
	{
		this.middle('dialogBox');
		if ($('dialogIframBG'))
		{
			$('dialogIframBG').style.top = $('dialogBox').style.top;
			$('dialogIframBG').style.left = $('dialogBox').style.left;
			$('dialogIframBG').style.width = $('dialogBox').offsetWidth + "px";
			$('dialogIframBG').style.height = $('dialogBox').offsetHeight + "px";
			$('dialogIframBG').style.display = 'block';
		}
		if (!is_opera) {
			this.shadow();
		}
	}
	
	this.reset = function()
	{
		this.close();
	}

	this.close = function()
	{
		if (window.removeEventListener) 
		{
			window.removeEventListener('resize', this.event_b, false);
			window.removeEventListener('scroll', this.event_b, false);
		} 
		else if (window.detachEvent) 
		{
			try {
				window.detachEvent('onresize', this.event_b);
				window.detachEvent('onscroll', this.event_b);
			} catch (e) {}
		}
		if ($('dialogIframBG')) {
			$('dialogIframBG').style.display = 'none';
		}
		$('dialogBox').style.display='none';
		$('dialogBoxBG').style.display='none';
		$('dialogBoxShadow').style.display = "none";
		if (typeof(parent.onDialogClose) == "function")
		{
			parent.onDialogClose($('dialogBoxTitle').innerHTML);
		}
	}

	this.shadow = function()
	{
		this.event_b_show();
		if (window.attachEvent)
		{
			window.attachEvent('onresize', this.event_b);
			window.attachEvent('onscroll', this.event_b);
		}
		else
		{
			window.addEventListener('resize', this.event_b, false);
			window.addEventListener('scroll', this.event_b, false);
		}
	}
	
	this.event_b = function()
	{
		var oShadow = $('dialogBoxShadow');
		
		if (oShadow.style.display != "none")
		{
			if (this.event_b_show)
			{
				this.event_b_show();
			}
		}
	}
	
	this.event_b_show = function()
	{
		var oShadow = $('dialogBoxShadow');
		oShadow['style']['position'] = "absolute";
		oShadow['style']['display']	= "";		
		oShadow['style']['opacity']	= "0.2";
		oShadow['style']['filter'] = "alpha(opacity=60)";
		oShadow['style']['background']	= "#fff";
		//var sClientWidth = parent ? parent.document.body.offsetWidth : document.body.offsetWidth;
		//var sClientHeight = parent ? parent.document.body.offsetHeight : document.body.offsetHeight;
		//var sScrollTop = parent ? (parent.document.body.scrollTop+parent.document.documentElement.scrollTop) : (document.body.scrollTop+document.documentElement.scrollTop);
		var sClientWidth = document.body.offsetWidth;
		var sClientHeight = document.body.offsetHeight;
		var sScrollTop = (document.body.scrollTop+document.documentElement.scrollTop);
		oShadow['style']['top'] = '0px';
		oShadow['style']['left'] = '0px';
		oShadow['style']['width'] = sClientWidth + "px";
		oShadow['style']['height'] = (sClientHeight + sScrollTop) + "px";
	}

	this.middle = function(_sId)
	{
		var dialogBox = $(_sId);
		dialogBox['style']['display'] = '';
		dialogBox['style']['position'] = "absolute";
		
		var sClientWidth = document.body.clientWidth;
		var sClientHeight = document.body.clientHeight;
		var sScrollTop = document.body.scrollTop+document.documentElement.scrollTop;
		//var sScrollTop = parent.document.body.scrollTop+parent.document.documentElement.scrollTop;

		var sleft = (sClientWidth - dialogBox.offsetWidth) / 2;
		var iTop = sScrollTop + 100;
		var sTop = iTop > 0 ? iTop : 0;
		
	//	sleft = sleft-80;//left menu width

		dialogBox['style']['left'] = sleft + "px";
		dialogBox['style']['top'] = sTop + "px";
	}
}


function openWindow(_sUrl, _sWidth, _sHeight, _sTitle)
{
	var oEdit = new dialog(_sWidth, _sHeight, _sTitle);
	oEdit.init();
	oEdit.open(_sUrl);
}
function openWindowT(_sUrl, _sWidth, _sHeight, _sTitle)
{
	var oEdit = new dialogT(_sWidth, _sHeight, _sTitle);
	oEdit.init();
	oEdit.open(_sUrl);
}
function openAlert(_sWord, _sButton , _sWidth, _sHeight, _sTitle , _sAction)
{
	return _openAlert2(_sWord, _sButton , _sWidth, _sHeight, _sTitle , _sAction, "");
}

function openAlertBlue(_sWord, _sButton , _sWidth, _sHeight, _sTitle , _sAction)
{
	var excss = '.rbs1{border:1px solid #d7e7fe; float:left;}\n' +
'.rb1-12,.rb2-12{height:23px; color:#fff; font-size:12px; background:#355582; padding:3px 5px; border-left:1px solid #fff; border-top:1px solid #fff; border-right:1px solid #6a6a6a; border-bottom:1px solid #6a6a6a; cursor:pointer;}\n' +
'.rb2-12{background:#355582;}\n';
	return _openAlert(_sWord, _sButton , _sWidth, _sHeight, _sTitle , _sAction, excss);
}
function _openDialog(_sTitle,_sWidth, _sHeight,_sHtml){
	var oEdit = new dialog(_sWidth, _sHeight, _sTitle);
	oEdit.init();
	oEdit.show();
	myInnerHTML('dialogBody', "<div id=\"dialogBodyWide\" class='f14 p10 mt10 tac '></div>");
	//alert(_sHtml);
	myInnerHTML('dialogBodyWide',_sHtml);
}
function _openConfirm(_sTitle,_sWidth, _sHeight,_sHtml,_sFunc){
	var oEdit = new dialog(_sWidth, _sHeight, _sTitle);
	oEdit.init();
	oEdit.show();
	myInnerHTML('dialogBody', "<div id=\"dialogBodyWide\" class='f14 p10 mt10 tac '></div>");
	myInnerHTML('dialogBodyWide',_sHtml+"<div class='tac mt20'><input type='button' class='rb11' value='确定' onclick='"+_sFunc+"' /><input type='button' class='gb11' style='margin-left: 10px;' value='取消' onclick='new dialog().reset();' /></div>");
}
function _openPrompt(_sTitle,_sWidth, _sHeight,_sHtml){
	var oEdit = new dialog(_sWidth, _sHeight, _sTitle);
	oEdit.init();
	oEdit.show();
	myInnerHTML('dialogBody', "<div id=\"dialogBodyWide\" class='f14 p10 mt10 tac '></div>");
	myInnerHTML('dialogBodyWide',_sHtml+"<div class='tac mt20'><input type='button' class='rb11' value='确定' onclick='new dialog().reset();' /></div>");
}
function _openAlert2(_sWord, _sButton , _sWidth, _sHeight, _sTitle , _sAction, _excss){
	var oEdit = new dialog(_sWidth, _sHeight, _sTitle);
	oEdit.init();
	oEdit.show();
	myInnerHTML('dialogBody', "<div id=\"dialogBodyWide\"><div id='dialogAlertContent' class='tac p10'></div><div class='tac mt10'><input type='button' class='rb11' value='"+_sButton+"' onclick='new dialog().reset();' /></div></div>");
	myInnerHTML('dialogAlertContent',_sWord);
	
}
function _openAlert(_sWord, _sButton , _sWidth, _sHeight, _sTitle , _sAction, _excss)
{
	var oEdit = new dialog(_sWidth, _sHeight, _sTitle);
	oEdit.init();
	oEdit.show();
	
	var framename = "iframe_parent_" + g_iframeno++;
	var openIframe = "<iframe width='100%' height='100%' name='"+framename+"' id='"+framename+"' src='' frameborder='0' scrolling='no'></iframe>";
	myInnerHTML('dialogBody', openIframe);
	var iframe = window.frames[framename];
	iframe.document.open();
	iframe.document.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">\n');
	iframe.document.write('<html xmlns="http://www.w3.org/1999/xhtml">\n');
	iframe.document.write('<head>\n');
	iframe.document.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />\n');
	iframe.document.write('<link href="/css/s.css" rel="stylesheet" type="text/css" />\n');
	if (_excss && _excss.length)
	{
		iframe.document.write('<style>\n');
		iframe.document.write(_excss + '\n');
		iframe.document.write('</style>\n');
	}
	iframe.document.write('</head>\n');
	iframe.document.write('<body>\n');
	if(!_sAction)
	{
		_sAction = "new parent.dialog().reset();";
	}
	iframe.document.write(alertHtml(_sWord , _sButton , _sAction)+'\n');
	iframe.document.write('</body>\n');
	iframe.document.write('</html>\n');
	iframe.document.close();
}
function alertHtml2(_sWord , _sButton , _sAction){
	
}
function alertHtml(_sWord , _sButton , _sAction)
{
	var html = "";
	
	var html = '<div style="text-align:center;font-size:14px;">\
					<div style="padding: 10px;">\
					 '+_sWord+'\
					</div>\
					<div style="padding: 5px;">\
						<input type="button" value="'+_sButton+'" title="'+_sButton+'" class="gb11" onmouseover="this.className=\'rb2-12\';" onmouseout="this.className=\'rb1-12\';" onclick="javascript:'+_sAction+'" />\
					</div>\
		  		</div>';
	
	return html;
}

