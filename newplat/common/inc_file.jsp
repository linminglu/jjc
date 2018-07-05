<link href="../styleset/bxsplat.css" rel="stylesheet" type="text/css" media="all"/>
<link href="../styleset/bxsSupport.css" rel="stylesheet" type="text/css" media="all"/>
<link href="../css/manager.css" rel="stylesheet" type="text/css" media="all"/>
<link href="../layui/css/layui.css" rel="stylesheet" type="text/css" media="all"/>
<script src="../js/js_z/jquery-1.7.2.min.js" type="text/javascript"></script>
<!-- <script src="../js/js_z/manager.js" type="text/javascript"></script> -->
<!-- <script src="../js/js_z/cover.js" type="text/javascript"></script> -->
<!-- <script src="../js/prototype.js" type="text/javascript"></script> -->
<script src="../js/commonbase.js" type="text/javascript"></script>
<script src="../layui/layui.js" type="text/javascript"></script>
<script type="text/javascript">
layui.use('layer', function(){var $ = layui.jquery, layer = layui.layer;});
</script>
<script type="text/javascript">
function chkLoginValid(){
	jQuery.post("../basedata/baseDataAction.do?method=chkLoginValid",
		function(data){
			var code=data.code;
			var message=data.msg;
			if(code=='402'){
				var alertHtml=$('#layui-layer1').html();
				if(!alertHtml){
					layer.alert(message, function(index){
						layer.close(index);
						window.location.href='/logout.html';
					});  
				}
			}
	}, "json");
} 
chkLoginValid();
setInterval(function(){
	chkLoginValid();
},3000);
</script>