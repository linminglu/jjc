<%@page import="com.ram.model.User"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_css.jsp"%>
<style type="text/css">
::-webkit-scrollbar { width: 11px;height: 11px;}
::-webkit-scrollbar-track {-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3); -webkit-border-radius: 10px;border-radius: 10px;}
::-webkit-scrollbar-thumb { -webkit-border-radius: 10px;border-radius: 10px;background: #d6d6d6; -webkit-box-shadow: inset 0 0 6px #999; }
::-webkit-scrollbar-thumb:window-inactive {background: #b2b2b2; }
</style>
</head>
<%
	String sellerUserType=(String)session.getAttribute("sellerUserType");
	User user=(User)session.getAttribute("loginUser");
	Integer userId  = user.getUserId();
	//request.setAttribute("loginUserGroup",userGroup);
	//request.setAttribute("loginUser",user);
	 String path = "";
    String scrollSet = "auto";
	if(user.getUserType().equals("5")){//订餐
//		path = "menu.gen/menutree_eat.jsp";
		if(sellerUserType.equals("2")){//派单人员
			path = "menu.gen/menutree_eat_paidan.jsp";
		}else{
			path = "menu.gen/menutree_eat.jsp";
		}
	}
	if(user.getUserType().equals("6")){//电商
	//	path = "menu.gen/menutree_store.jsp";
		if(sellerUserType.equals("2")){//派单人员
			path = "menu.gen/menutree_store_paidan.jsp";
		}else if(sellerUserType.equals("3")){//送货人员
			path = "menu.gen/menutree_seller_songhuo.jsp";
		}else if(sellerUserType.equals("4")){//商家管理员
			path = "menu.gen/menutree_store.jsp";
		}else if(sellerUserType.equals("1")){//普通员工
			path = "menu.gen/menutree_store_employee.jsp";
		}
	}
	if(user.getUserType().equals("7")){//团购
		path = "menu.gen/menutree_buy.jsp";
	}
	if(user.getUserType().equals("2")){// 管理员
		if(user.getLoginName().equals("admin") || user.getLoginName().equals("adminkf")){
			path = "menu.gen/menutree_admin.jsp";
		}else{
			path = "menu.gen/menutree.jsp";
		}
	}
	if(user.getUserType().equals("3")){//商家
		path = "menu.gen/menutree_seller.jsp";
	}
	if(user.getUserType().equals("4")){//超级管理员
		path = "menu.gen/menutree_super.jsp";
	}
	if(user.getUserType().equals("9")){//代理管理
		path = "menu.gen/menutree_cc.jsp";
	}
	if(user.getUserType().equals("10")){//物业管理员
		path = "menu.gen/menutree_estate.jsp";
	}
	if(user.getUserType().equals("12")){//物业管理员
		if(user.getAgentId()!=null&&user.getAgentId()>0){
			path = "menu.gen/menutree_agentTwo.jsp";
		}else{
			path = "menu.gen/menutree_agent.jsp";
		}
	}
//    System.err.println("path:"+path);
%>

<body  bottommargin="0" topmargin="0" leftmargin="0" rightmargin="0" >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="jrmenu-bg__">
	  <tr class="jrmenu-table-text__">
	    <td height="100%" valign="top" width="100%" style="border-right: 2px solid #eee;">
			<iframe id="fraMenuId" name="fraMenu" style="HEIGHT:100%; VISIBILITY: inherit; WIDTH: 240; Z-INDEX: 2" frameborder="0" scrolling="<%=scrollSet%>" src="<%=path%>" resizeable=no></iframe>
	    </td>
	  </tr>
</table>

</body>
</html>
