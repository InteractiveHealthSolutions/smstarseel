<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="org.irdresearch.smstarseel.web.util.UserSessionUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<base href="${pageContext.request.contextPath}"/>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<%@include file="include.jsp"%>

<title>SmsTarseel</title>
</head>
<body>

<div id="wraptemp">

<div id="headertemp">
<div class="headerLogo">
<span class="headerLogotext1">Sms</span><span class="headerLogotext2">Tarseel</span>
</div>
<div class="loggedInUser">
<a href="${pageContext.request.contextPath}/admin/logout.do" style="margin-right:10px">logout</a> | 
<a onclick="$('#winpwd').window('open');">change password</a><br>
<%=UserSessionUtils.getActiveUser(request).getUser().getFullName()%></div>

<div id="winpwd"></div>
<script type="text/javascript"><!--
$('#winpwd').window({
	title: 'Change password',
	href : '${pageContext.request.contextPath}/admin/changePassword.htm',
	width: 500,  
    height: 300,
    minimizable: false,
    resizable: false,
    maximizable: false,
    draggable: false,
    closed: true,
    modal:true
}); 
//--></script>
</div>

<!-- end header -->
<div id="menutemp">
<ul id="navigationul">
<li><a id="home" href="<c:url value='/home/home.htm'/>">Home</a></li>
<li><a id="communication" href="<c:url value='/communication/viewInbounds.htm'/>">Communication</a></li>
<li><a id="admin" href="<c:url value='/admin/admin.htm'/>">Admin</a></li>
<!-- <li><a id="troubleshoot" href="troubleshoot.htm">Troubleshoot/Help</a></li>
<li><a id="about" href="about.htm">About</a></li> -->
</ul>
</div>
<!-- end menu -->
<script type="text/javascript">
<!--
	$("#navigationul li a").each(function() {var thisstr = this.id.toLowerCase();
		if ('${navigationType}' == thisstr) {/* $("li.highlight").removeClass("highlight"); */$(this).parent().addClass("highlight");}});
//-->
</script>


<div id="contentwrap"> 

<div id="contenttemp">