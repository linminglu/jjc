<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ include file="../common/inc_include.jsp"%>
<%@ page import="com.ram.BXSConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<c:import url="../include/meta.jsp"/>
<link type="text/css" rel="stylesheet" href="../style/base.css" media="all">
<link type="text/css" rel="stylesheet" href="../style/navigation.css" media="all">
<link type="text/css" rel="stylesheet" href="../style/avatar-table.css" media="all">
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/initialize.js"></script>
<script type="text/javascript" src="../js/json.js"></script>
<script type="text/javascript" src="../js/request.js"></script>
<script language="javascript">
function delEst(eid){
	var xmlHttp = new AJAXRequest();
	var url = "homeAction.do?method=delEst";
	
	xmlHttp.post(url,"eid="+eid+"&date="+new Date(),function(xmlHttp){calEst(xmlHttp,eid)});
}
function calEst(xmlHttp,eid){
	if(xmlHttp.readyState==4){
		if(xmlHttp.status==200){
			if(xmlHttp.responseText=="yesEst"){
				var est = document.getElementById(eid);
				est.parentNode.removeChild(est);
			}
		}
	}
}
</script>
<script language="javascript">
function delEve(eid){
	var xmlHttp = new AJAXRequest();
	var url = "homeAction.do?method=delEve";
	
	xmlHttp.post(url,"eid="+eid+"&date="+new Date(),function(xmlHttp){calEve(xmlHttp,eid)});
}
function calEve(xmlHttp,eid){
	if(xmlHttp.readyState==4){
		if(xmlHttp.status==200){
			if(xmlHttp.responseText=="yesEve"){
				var eve = document.getElementById(eid);
				eve.parentNode.removeChild(eve);
			}
		}
	}
}
</script>
<script language="javascript">
function delVote(vid){
	var xmlHttp = new AJAXRequest();
	var url = "homeAction.do?method=delVote";
	
	xmlHttp.post(url,"vid="+vid+"&date="+new Date(),function(xmlHttp){calVote(xmlHttp,vid)});
}
function calVote(xmlHttp,vid){
	if(xmlHttp.readyState==4){
		if(xmlHttp.status==200){
			if(xmlHttp.responseText=="yesVote"){
				var vote = document.getElementById(vid);
				vote.parentNode.removeChild(vote);
			}
		}
	}
}
</script>
<script language="javascript">
function delShare(sid){
	var xmlHttp = new AJAXRequest();
	var url = "homeAction.do?method=delShare";
	
	xmlHttp.post(url,"sid="+sid+"&date="+new Date(),function(xmlHttp){calShare(xmlHttp,sid)});
}
function calShare(xmlHttp,sid){
	if(xmlHttp.readyState==4){
		if(xmlHttp.status==200){
			if(xmlHttp.responseText=="yesShare"){
				var share = document.getElementById(sid);
				share.parentNode.removeChild(share);
			}
		}
	}
}
</script>
<script language="javascript">
function delTopic(tid){
	var xmlHttp = new AJAXRequest();
	var url = "homeAction.do?method=delTopic";
	
	xmlHttp.post(url,"tid="+tid+"&date="+new Date(),function(xmlHttp){calTopic (xmlHttp,tid)});
}
function calTopic(xmlHttp,tid){
	if(xmlHttp.readyState==4){
		if(xmlHttp.status==200){
			if(xmlHttp.responseText=="yesTopic"){
				var topic = document.getElementById(tid);
				topic.parentNode.removeChild(topic);
			}
		}
	}
}
</script>
<script language="javascript">
function delTribe(tid){
	var xmlHttp = new AJAXRequest();
	var url = "homeAction.do?method=delTribe";
	
	xmlHttp.post(url,"tid="+tid+"&date="+new Date(),function(xmlHttp){calTribe(xmlHttp,tid)});
}
function calTribe(xmlHttp,tid){
	if(xmlHttp.readyState==4){
		if(xmlHttp.status==200){
			if(xmlHttp.responseText=="yesTribe"){
				var tribe = document.getElementById(tid);
				tribe.parentNode.removeChild(tribe);
			}
		}
	}
}
</script>
<title>新鲜事</title>
</head>
<body id="registerPage">
<c:import url="../include/layout.jsp"/>
<c:if test="${dt == '0' || dt == '-1'}">
<textarea id="estudyInfo" rows="0" cols="0" style="display:none"><c:out value="${ufInfo.userEstudyInfo}"/></textarea>
</c:if>
<c:if test="${dt == '1' || dt == '-1'}">
<textarea id="topicInfo" rows="0" cols="0" style="display:none"><c:out value="${ufInfo.userTopicInfo}"/></textarea>
</c:if>
<c:if test="${dt == '2' || dt == '-1'}">
<textarea id="eventInfo" rows="0" cols="0" style="display:none"><c:out value="${ufInfo.userEventInfo}"/></textarea>
</c:if>
<c:if test="${dt == '3' || dt == '-1'}">
<textarea id="voteInfo" rows="0" cols="0" style="display:none"><c:out value="${ufInfo.userVoteInfo}"/></textarea>
</c:if>
<c:if test="${dt == '4' || dt == '-1'}">
<textarea id="tribeInfo" rows="0" cols="0" style="display:none"><c:out value="${ufInfo.userTribeInfo}"/></textarea>
</c:if>
<c:if test="${dt == '5' || dt == '-1'}">
<textarea id="shareInfo" rows="0" cols="0" style="display:none"><c:out value="${ufInfo.userShareInfo}"/></textarea>
</c:if>
<div id="cdata">
	<div id="single-column">
		<div class="content">
			<div id="oak" class="white-gray">
			<div id="wide-column">
				<div class="cube">
					<h2 class="newsfeed-header">
						<span class="more" id="replyMoreDiv">
							<a href="homeAction.do?method=init">返回首页</a>
						</span>
						<span class="newsfeedtextlink">
							<a class="float-left">新鲜事</a>
						</span>
					</h2>
					<div id="feedHome"></div>
				</div>
			</div>
			<div id="side-column">
				<div id="list-archive">
					<h4>分类</h4>
					<ol>
						<c:if test="${dt == '-1'}">
						<li class="selected">
						</c:if>
						<c:if test="${dt != '-1'}">
						<li>
						</c:if>
							<a href="userDynamicAction.do?method=init&dt=-1"><img src="../img/right.gif">&nbsp;所有分类</a>
						</li>
					</ol>
					<ol>
						<c:if test="${dt == '0'}">
						<li class="selected">
						</c:if>
						<c:if test="${dt != '0'}">
						<li>
						</c:if>
							<a href="userDynamicAction.do?method=init&dt=0"><img src="../img/blog.gif">&nbsp;日志</a>
						</li>
					</ol>
					<ol>
						<c:if test="${dt == '3'}">
						<li class="selected">
						</c:if>
						<c:if test="${dt != '3'}">
						<li>
						</c:if>
							<a href="userDynamicAction.do?method=init&dt=3"><img src="../img/vote.gif">&nbsp;投票</a>
						</li>
					</ol>
					<ol>
						<c:if test="${dt == '5'}">
						<li class="selected">
						</c:if>
						<c:if test="${dt != '5'}">
						<li>
						</c:if>
							<a href="userDynamicAction.do?method=init&dt=5"><img src="../img/share.gif">&nbsp;分享</a>
						</li>
					</ol>
					<ol>
						<c:if test="${dt == '2'}">
						<li class="selected">
						</c:if>
						<c:if test="${dt != '2'}">
						<li>
						</c:if>
							<a href="userDynamicAction.do?method=init&dt=2"><img src="../img/event.gif">&nbsp;活动</a>
						</li>
					</ol>
					<ol>
						<c:if test="${dt == '1'}">
						<li class="selected">
						</c:if>
						<c:if test="${dt != '1'}">
						<li>
						</c:if>
							<a href="userDynamicAction.do?method=init&dt=1"><img src="../img/topic.gif">&nbsp;提问</a>
						</li>
					</ol>
					<ol>
						<c:if test="${dt == '4'}">
						<li class="selected">
						</c:if>
						<c:if test="${dt != '4'}">
						<li>
						</c:if>
							<a href="userDynamicAction.do?method=init&dt=4"><img src="../img/tribe.gif">&nbsp;群组</a>
						</li>
					</ol>
				</div>
			</div>
			</div>
		</div>
	</div>
</div>
<script language="javascript">
var estInfos = document.getElementById( "estudyInfo" );
if( estInfos ){
	var estudyVal = estInfos.value;
	if(estudyVal!=''){
		var estudys = JSON.parse( estudyVal );
		var estLength = estudys.length;
		if( estLength > 6 )estLength = 6;
		for(var i=0;i<estLength;i++){
			var html = "";
			var est = estudys[i];
			html = "<div class='opi' id='" + est.eid +"'>";
			html += "<div class='opi-content'><div class='headline'><span class='title'>";
			html += "<div class='share-n-hide'><a class='x-2-hide' href='javascript:void(0)' onclick='delEst(" + est.eid + ")'>删除</a></div>";
			html += "<h2><img src='../img/blog.gif'>&nbsp;&nbsp;";
			html += "<a href='../user/userPageAction.do?method=userHomePage&uid=" + est.uid + "'>" + est.name + "</a>";
			html += "&nbsp;&nbsp;&nbsp;发表日志&nbsp;&nbsp;&nbsp;&nbsp;";
			html += "<a href='../estudy/estudyAction.do?method=getEstudyInfo&estudyId=" + est.eid + "' onclick='delEst(" + est.eid + ")'>" + est.title + "</a>";
			html += "</h2>";
			html += "</span></div>";
			//html += "<div class='feedbody'><h3>&nbsp;&nbsp;&nbsp;&nbsp;";
			//html += "<a href='../estudy/estudyAction.do?method=getEstudyInfo&estudyId=" + est.eid + "' onclick='delEst(" + est.eid + ")'>" + est.title + "</a>";
			//html += "</h3><p>";			
			//html += est.content;
			//html += "<a href='../estudy/estudyAction.do?method=getEstudyInfo&estudyId=" + est.eid + "' onclick='delEst(" + est.eid + ")'>&nbsp;&nbsp;<<继续阅读</a>";
			//html += "</p>";
			html += "</div>";
			document.all.feedHome.innerHTML += html;
		}
	}
}

var eventInfos = document.getElementById( "eventInfo" );
if( eventInfos ){
	var eventVal = eventInfos.value;
	if(eventVal!=''){
		var events = JSON.parse( eventVal );
		var eveLength = events.length;
		if( eveLength > 6 )eveLength=6;
		for(var i=0;i<eveLength;i++){
			var html = "";
			var eve = events[i];
			html = "<div class='opi' id='" + eve.eid +"'>";
			html += "<div class='opi-content'><div class='headline'><span class='title'>";
			html += "<div class='share-n-hide'><a class='x-2-hide' href='javascript:void(0)' onclick='delEve(" + eve.eid + ")'>删除</a></div>";
			html += "<h2><img src='../img/event.gif'>&nbsp;&nbsp;";
			html += "<a href='../user/userPageAction.do?method=userHomePage&uid=" + eve.uid + "'>" + eve.name + "</a>";
			html += "&nbsp;&nbsp;&nbsp;发起新的活动&nbsp;&nbsp;&nbsp;&nbsp;";
			html += "<a href='../event/eventAction.do?method=getEventInfo&eventId=" + eve.eid + "' onclick='delEve(" + eve.eid + ")'>" + eve.title + "</a>";
			html += "</h2>";
			html += "</span></div>";
			//html += "<div class='feedbody'><h3>&nbsp;&nbsp;&nbsp;&nbsp;";
			//html += "<a href='../event/eventAction.do?method=getEventInfo&eventId=" + eve.eid + "' onclick='delEve(" + eve.eid + ")'>" + eve.title + "</a>";
			//html += "</h3><p>";			
			//html += eve.content;
			//html += "<a href='../event/eventAction.do?method=getEventInfo&eventId=" + eve.eid + "' onclick='delEve(" + eve.eid + ")'>&nbsp;&nbsp;<<继续阅读</a>";
			//html += "</p>";
			html += "</div>";
			document.all.feedHome.innerHTML += html;
		}
	}
}
var voteInfos = document.getElementById("voteInfo");
if( voteInfos ){
	var voteVal = voteInfos.value;
	if(voteVal!=''){
		var votes = JSON.parse( voteVal );
		var votLength = votes.length;
		if( votLength > 6 )votLength = 6;
		for(var i=0;i<votLength;i++){
			var html = "";
			var vote = votes[i];
			html = "<div class='opi' id='" + vote.vid +"'>";
			html += "<div class='opi-content'><div class='headline'><span class='title'>";
			html += "<div class='share-n-hide'><a class='x-2-hide' href='javascript:void(0)' onclick='delVote(" + vote.vid + ")'>删除</a></div>";
			html += "<h2><img src='../img/vote.gif'>&nbsp;&nbsp;";
			html += "<a href='../user/userPageAction.do?method=userHomePage&uid=" + vote.uid + "'>" + vote.name + "</a>"
			html += "&nbsp;&nbsp;&nbsp;&nbsp;发表新的投票&nbsp;&nbsp;&nbsp;&nbsp;";
			html += "<a href='../vote/voteAction.do?method=findVoteOptions&voteId=" + vote.vid + "' onclick='delVote(" + vote.vid + ")'>" + vote.title + "</a>";
			html += "</h2>";
			html += "</span></div>";
			//html += "<div class='feedbody'><h3>";
			//html += "<a href='../vote/voteAction.do?method=findVoteOptions&voteId=" + vote.vid + "' onclick='delVote(" + vote.vid + ")'>" + vote.title + "</a>";
			//html += "</h3><p>";
			//html += vote.content;
			//html += "<a href='../vote/voteAction.do?method=findVoteOptions&voteId=" + vote.vid + "' onclick='delVote(" + vote.vid + ")'>&nbsp;&nbsp;<<继续阅读</a>";
			//html += "</p>";
			html += "</div>";
			document.all.feedHome.innerHTML += html;
		}
	}
}

var topicInfos = document.getElementById("topicInfo");
if( topicInfos ){
	var topicVal = topicInfos.value;
	if(topicVal!=''){
		var topics = JSON.parse( topicVal );
		var topLength = topics.length;
		if( topLength > 6 )topLength =6;
		for(var i=0;i<topLength;i++){
			var html = "";
			var topic = topics[i];
			html = "<div class='opi' id='" + topic.tid +"'>";
			html += "<div class='opi-content'><div class='headline'><span class='title'>";
			html += "<div class='share-n-hide'><a class='x-2-hide' href='javascript:void(0)' onclick='delTopic(" + topic.tid + ")'>删除</a></div>";
			html += "<h2><img src='../img/topic.gif'>&nbsp;&nbsp;";
			html += "<a href='../user/userPageAction.do?method=userHomePage&uid=" + topic.uid + "'>" + topic.name + "</a>";
			html += "&nbsp;&nbsp;&nbsp;&nbsp;发表提问&nbsp;&nbsp;&nbsp;&nbsp;";
			html += "<a href='../topic/userTopicAction.do?method=findTopicById&userTopicId=" + topic.tid + "' onclick='delTopic(" + topic.tid + ")'>" + topic.title + "</a>";
			html += "</h2>";
			html += "</span></div>";
			//html += "<div class='feedbody'><h3>";
			//html += "<a href='../topic/userTopicAction.do?method=findTopicById&userTopicId=" + topic.tid + "' onclick='delTopic(" + topic.tid + ")'>" + topic.title + "</a>";
			//html += "</h3><p>";
			//html += topic.content;
			//html += "<a href='../topic/userTopicAction.do?method=findTopicById&userTopicId=" + topic.tid + "' onclick='delTopic(" + topic.tid + ")'>&nbsp;&nbsp;<<继续阅读</a>";
			//html += "</p>";
			html += "</div>";
			document.all.feedHome.innerHTML += html;
		}
	}
}

var shareInfos = document.getElementById("shareInfo");
if( shareInfos ){
	var shareVal = shareInfos.value;
	if(shareVal!=''){
		var shares = JSON.parse( shareVal );
		var shaLength = shares.length;
		if( shaLength > 6 )shaLength = 6;
		for(var i=0;i<shaLength;i++){
			var html = "";
			var sha = shares[i];
			html = "<div class='opi' id='" + sha.sid +"'>";
			html += "<div class='opi-content'><div class='headline'><span class='title'>";
			html += "<div class='share-n-hide'><a class='x-2-hide' href='javascript:void(0)' onclick='delShare(" + sha.sid + ")'>删除</a></div>";
			html += "<h2><img src='../img/share.gif'>&nbsp;&nbsp;";
			html += "<a href='../user/userPageAction.do?method=userHomePage&uid=" + sha.uid + "'>" + sha.name + "</a>";
			html += "&nbsp;&nbsp;&nbsp;&nbsp;分享信息&nbsp;&nbsp;&nbsp;&nbsp;";
			html += "<a href='" + sha.url + "' onclick='delShare(" + sha.sid + ")'>" + sha.title + "</a>";
			html += "</h2>";
			html += "</span></div>";
			//html += "<div class='feedbody'><h3>";
			//html += "<a href='" + sha.url + "' onclick='delShare(" + sha.sid + ")'>" + sha.title + "</a>";
			//html += "</h3><p>";
			//html += sha.content;
			//html += "<a href='" + sha.url + "' onclick='delShare(" + sha.sid + ")'>&nbsp;&nbsp;<<继续阅读</a>";
			//html += "</p>";
			html += "</div>";
			document.all.feedHome.innerHTML += html;
		}
	}
}

var tribeInfos = document.getElementById( "tribeInfo" );
if( tribeInfos ){
	var tribeVal = tribeInfos.value;
	if( tribeVal != '' ){
		var tribes = JSON.parse( tribeVal );
		var triLength = tribes.length;
		if( triLength > 6 )triLength = 6;
		for(var i=0;i<triLength;i++){
			var html = "";
			var tri = tribes[i];
			html = "<div class='opi' id='" + tri.tid +"'>";
			html += "<div class='opi-content'><div class='headline'><span class='title'>";
			html += "<div class='share-n-hide'><a class='x-2-hide' href='javascript:void(0)' onclick='delTribe(" + tri.tid + ")'>删除</a></div>";
			html += "<h2><img src='../img/tribe.gif'>&nbsp;&nbsp;";
			html += "<a href='../user/userPageAction.do?method=userHomePage&uid=" + tri.uid + "'>" + tri.name + "</a>";
			html += "&nbsp;&nbsp;&nbsp;&nbsp;群组信息&nbsp;&nbsp;&nbsp;&nbsp;";
			html += "<a href='../tribe/tribeAction.do?method=findTopicById&tribeId=" + tri.tid + "' onclick='delTribe(" + tri.tid + ")'>" + tri.title + "</a>";
			html += "</h2>";
			html += "</span></div>";
			//html += "<div class='feedbody'><h3>";
			//html += "<a href='../tribe/tribeAction.do?method=findTopicById&tribeId=" + tri.tid + "' onclick='delTribe(" + tri.tid + ")'>" + tri.title + "</a>";
			//html += "</h3><p>";
			//html += topic.content;
			//html += "<a href='../tribe/tribeAction.do?method=findTopicById&tribeId=" + tri.tid + "' onclick='delTribe(" + tri.tid + ")'>&nbsp;&nbsp;<<继续阅读</a>";
			//html += "</p>";
			html += "</div>";
			document.all.feedHome.innerHTML += html;
		}
	}
}
</script>
</body>
</html>