<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			$(".pic_show").fancybox(
				{
					'overlayOpacity':0.5,
					'transitionIn'	: 'elastic',
					'transitionOut'	: 'elastic'
				}
			);
		});
</script>

			<div class="msccs">
				<div class="msccsw">
					<div class="sbox">
						<h3><a href="/zx_0.html" class="mmore"><img src="../images/icon_more.jpg"/></a><strong>最新资讯</strong></h3>
						<div class="sborder">
							<ul class="zxhot">
								<logic:notEmpty name="zxHotList">
									<logic:iterate id="item" name="zxHotList" indexId="num">
										<li>
											<a onclick="newsClick(<c:out value='${item.id}'/>,'../zx/zx_<c:out value='${item.id}'/>_0.html');" href="javascript:void(0);" title="<c:out value="${item.title}"/>">
												<c:out value="${item.title}"/>
											</a>
										</li>
									</logic:iterate>
								</logic:notEmpty>
							</ul>
						</div>
					</div>
					
					<div class="sbox">
						<h3><a href="/hd_0.html" class="mmore"><img src="../images/icon_more.jpg"/></a><strong>最新活动</strong></h3>
						<div class="sborder">
							<ul class="zxhot">
								<logic:notEmpty name="hdHotList">
									<logic:iterate id="item" name="hdHotList" indexId="num">
										<li>
											<a onclick="activityClick(<c:out value='${item.id}'/>,'../hd/hd_<c:out value='${item.id}'/>_0.html');" href="javascript:void(0);" title="<c:out value="${item.title}"/>">
												<c:out value="${item.title}"/>
											</a>
										</li>
									</logic:iterate>
								</logic:notEmpty>
							</ul>
						</div>
					</div>
					<div class="sbox">
						<h3><a href="/pictures_0.html" class="mmore"><img src="../images/icon_more.jpg"/></a><strong>最新图片</strong></h3>
						<div class="sborder">
							<ul class="simger">
								<logic:notEmpty name="picNewList">
									<logic:iterate id="item" name="picNewList" indexId="num">
										<li>
											<a rel="group" class="pic_show" href="<c:out value='${item.picPath}'/>" title="<c:out value='${item.picExplain}'/>">
												<img src="<c:out value='${item.picPathMin}'/>" alt="<c:out value='${item.picExplain}'/>"/>
											</a>  		
										</li>
									</logic:iterate>
								</logic:notEmpty>
							</ul>
						</div>
					</div>
				</div>
			</div>
