<%@page import="org.irdresearch.smstarseel.data.Role"%>
<%@page import="org.irdresearch.smstarseel.data.Project"%>
<%@page import="java.util.List"%>
<%@page import="org.irdresearch.smstarseel.context.TarseelServices"%>
<%@page import="org.irdresearch.smstarseel.context.TarseelContext"%>
<%@page import="java.util.Arrays"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.ServiceType"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals"%>
<%@page import="org.irdresearch.smstarseel.SmsTarseel"%>

<head>
<title>SmsTarseel</title>
<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width" />
<link rel="stylesheet" type="text/css" href="css/webStyle.css" media="screen and (min-device-width: 481px)" />
<link rel="stylesheet" type="text/css" href="css/mobileStyle.css" media="only screen and (max-device-width: 480px)" />
</head>
<script type="text/javascript">
<!--
function trim(s)
{
	if(s.length==0) return '';
	var l=0; var r=s.length -1;
	while(l < s.length && s[l] == '  ')
	{	l++; }
	while(r > l && s[r] == ' ')
	{	r-=1;	}
	return s.substring(l, r+1);
}

function resetDivs(serviceType) 
{
	var servArr = '<%=ServiceType.convertToString(null, null, ",")%>'.split(',');
	for ( var i = 0; i < servArr.length; i++) {
		if(servArr[i] == serviceType){
			document.getElementById(servArr[i] + "Div").style.display = 'block';
			document.getElementById(servArr[i] + "Anc").style.backgroundColor = 'blue';
		}
		else {
			document.getElementById(servArr[i] + "Div").style.display = 'none';
			document.getElementById(servArr[i] + "Anc").style.backgroundColor = 'transparent';
		}
	}
}
function submitRequest(serviceType) 
{
	if(serviceType == '<%=ServiceType.CHANGE_OWN_PWD%>')
	{
		var username = document.getElementById("ownusername").value;
		var oldpwd = document.getElementById("ownoldPwd").value;
		var newpwd = document.getElementById("ownnewPwd").value;
		var repwd = document.getElementById("ownreNewPwd").value;

		if(trim(username) == ''){
			alert('Specify your username');
			return;
		}

		if(trim(oldpwd) == ''){
			alert('Specify your old password');
			return;
		}

		if(trim(newpwd) == ''){
			alert('Specify your new password');
			return;
		}

		if(trim(repwd) == ''){
			alert('Re-enter your new password');
			return;
		}
		
		if(newpwd != repwd){
			alert('Both passwords donot match');
			return;
		}
	}
	else if(serviceType == '<%=ServiceType.CHANGE_OTHER_USER_PWD%>')
	{
		var actionPwd = document.getElementById("actionPwd").value;
		var username = document.getElementById("username").value;
		var pwd = document.getElementById("newPwd").value;
		var repwd = document.getElementById("reNewPwd").value;
		
		if(trim(actionPwd) == ''){
			alert('Admin password must be provided');
			return;
		}
		if(trim(username) == ''){
			alert('Specify Username for which password is being changed');
			return;
		}
		
		if(trim(pwd) == ''){
			alert('Specify new password');
			return;
		}
		
		if(trim(repwd) == ''){
			alert('Re-enter new password');
			return;
		}
		
		if(pwd != repwd){
			alert('Both passwords donot match');
			return;
		}
	}
	else if(serviceType == '<%=ServiceType.ADD_USER%>')
	{
		var actionPwd = document.getElementById("newActionPwd").value;
		var username = document.getElementById("newusername").value;
		var pwd = document.getElementById("pwd").value;
		var repwd = document.getElementById("rePwd").value;
		var firstName = document.getElementById("firstName").value;
		var lastName = document.getElementById("lastName").value;
		var userRoleName = document.getElementById("userRoleName").value;

		if(trim(actionPwd) == ''){
			alert('Admin password must be provided');
			return;
		}
		if(trim(username) == ''){
			alert('Specify Username for which password is being changed');
			return;
		}
		
		if(trim(pwd) == ''){
			alert('Specify new password');
			return;
		}
		
		if(trim(repwd) == ''){
			alert('Re-enter new password');
			return;
		}
		
		if(pwd != repwd){
			alert('Both passwords donot match');
			return;
		}
		
		if(trim(firstName) == ''){
			alert('First name must be provided');
			return;
		}
		if(userRoleName == ''){
			alert('Specify role for new user');
			return;
		}
	}
	else if(serviceType == '<%=ServiceType.SEND_TEST_SMS%>')
	{
		var actionPwd = document.getElementById("smsActionPwd").value;
		var cellNumber = document.getElementById("cellNumber").value;
		var text = document.getElementById("smsText").value;
		var project = document.getElementById("smsProject").value;
		if(trim(actionPwd) == ''){
			alert('Admin password must be provided');
			return;
		}
		if(trim(cellNumber) == ''){
			alert('Specify recipient cell number');
			return;
		}
		
		if(text == ''){
			alert('Specify text to send');
			return;
		}
		
		if(project == ''){
			alert('Specify project on which phone is registered');
			return;
		}
	}
	
	if(confirm("Are you sure ?")){
		document.formId.action = "mainpageaction?srvtyp="+serviceType;
		document.getElementById("formId").submit();
	}
}
//-->
</script>
<form id="formId" name="formId" method="post">
	<div align="center" class="divCenter">
		<h1>Welcome to SmsTarseel</h1>
		<table style="text-align: center;">
			<tr>
				<td style="background-color: #B4CFEC;">
				<a href="login.htm">login</a>
				<a id="<%=ServiceType.CHANGE_OWN_PWD%>Anc" onclick="resetDivs('<%=ServiceType.CHANGE_OWN_PWD%>');">Change your password</a><br>
				<a id="<%=ServiceType.CHANGE_OTHER_USER_PWD%>Anc" onclick="resetDivs('<%=ServiceType.CHANGE_OTHER_USER_PWD%>');">Change other user password</a><br>
				<a id="<%=ServiceType.ADD_USER%>Anc" onclick="resetDivs('<%=ServiceType.ADD_USER%>');">Add a new user</a><br>
				<a id="<%=ServiceType.SEND_TEST_SMS%>Anc" onclick="resetDivs('<%=ServiceType.SEND_TEST_SMS%>');">Send test sms</a>
				</td>
			</tr>
			<%if(request.getAttribute("errorMessage") != null || request.getAttribute("successMessage") != null){%>
			<tr>
				<td colspan="2"><div id="messageDiv"><input class="closeButton" type="button" onclick="document.getElementById('messageDiv').style.display='none';" value="X">
					<span class="error"	style="font-size: x-small; color: red">${requestScope.errorMessage}</span>
					<span class="error"	style="font-size: x-small; color: red">${requestScope.successMessage}</span>
				</div></td>
			</tr>
			<%}%>
			<tr>
				<td colspan="2">
				<div id="<%=ServiceType.CHANGE_OWN_PWD%>Div" style="display: none;">
					<table>
						<tr><td colspan="2"><span class="heading">CHANGE OWN PASSWORD</span></td></tr>
						<tr><td>Enter Your Username : </td><td><input type="text" id="ownusername" name="ownusername" value="" /></td></tr>
						<tr><td>Enter Old Password : </td><td><input type="password" id="ownoldPwd" name="ownoldPwd" value="" /></td></tr>
						<tr><td>Enter New Password : </td><td><input type="password" id="ownnewPwd" name="ownnewPwd" value="" /></td></tr>
						<tr><td>Re-Enter Password : </td><td><input type="password" id="ownreNewPwd" name="ownreNewPwd" value="" /></td></tr>
						<tr><td></td><td><input type="button" value="OK" onclick="submitRequest('<%=ServiceType.CHANGE_OWN_PWD%>');"/></td></tr>
					</table>
				</div>
				<div id="<%=ServiceType.CHANGE_OTHER_USER_PWD%>Div" style="display: none;">
					<table>
						<tr><td colspan="2"><span class="heading">CHANGE/RESET OTHER USER PASSWORD (only for admin)</span></td></tr>
						<tr><td>Enter Admin Password : </td><td><input type="password" id="actionPwd" name="actionPwd" value="" /></td></tr>
						<tr><td>Enter Other Username : </td><td><input type="text" id="username" name="username" value="" /></td></tr>
						<tr><td>Enter New Password:</td><td><input type="password" id="newPwd" name="newPwd" value="" /></td></tr>
						<tr><td>Re-Enter Password : </td><td><input type="password" id="reNewPwd" name="reNewPwd" value="" /></td></tr>
						<tr><td></td><td><input type="button" value="OK" onclick="submitRequest('<%=ServiceType.CHANGE_OTHER_USER_PWD%>');"/></td></tr>
					</table>
				</div>
				<div id="<%=ServiceType.ADD_USER%>Div" style="display: none;">
				<%
				TarseelServices tsc1 = TarseelContext.getServices();
				List<Role> rl = tsc1.getUserService().getAllRoles();
				tsc1.closeSession();
				%>
				<table>
						<tr><td colspan="2"><span class="heading">ADD NEW USER</span></td></tr>
						<tr><td>Enter Admin Password : </td><td><input type="password" id="newActionPwd" name="newActionPwd" value="" /></td></tr>
						<tr><td>Enter Username/Login ID : </td><td><input type="text" id="newusername" name="newusername" value="" /></td></tr>
						<tr><td>Enter  Password : </td><td><input type="password" id="pwd" name="pwd" value="" /></td></tr>
						<tr><td>Re-Enter Password : </td><td><input type="password" id="rePwd" name="rePwd" value="" /></td></tr>
						<tr><td>Enter  First Name : </td><td><input type="text" id="firstName" name="firstName" value="" /></td></tr>
						<tr><td>Enter  Last Name : </td><td><input type="text" id="lastName" name="lastName" value="" /></td></tr>
						<tr><td>Choose Role : </td>
						<td>
						<select id="userRoleName" name="userRoleName">
						<option></option>
						<%for(Role r : rl){
							pageContext.setAttribute("roleName",r.getName());
						%>	
							<option value="${roleName}">${roleName}</option>
						<%}%>
						</select></td></tr>
						<tr><td></td><td><input type="button" value="OK" onclick="submitRequest('<%=ServiceType.ADD_USER%>');"/></td></tr>
					</table>
				</div>
				<div id="<%=ServiceType.SEND_TEST_SMS%>Div" style="display: none;">
				<%
				TarseelServices tsc2 = TarseelContext.getServices();
				List<Project> pl = tsc2.getDeviceService().getAllProjects(0, 100);
				tsc2.closeSession();
				%>
				<table>
						<tr><td colspan="2"><span class="heading">SEND TEST SMS</span></td></tr>
						<tr><td>Enter Admin Password : </td><td><input type="password" id="smsActionPwd" name="smsActionPwd" value="" /></td></tr>
						<tr><td>Enter Cell Number : </td><td><input type="text" id="cellNumber" name="cellNumber" value="" /></td></tr>
						<tr><td>Enter Text : </td><td><input type="text" id="smsText" name="smsText" value="" /></td></tr>
						<tr><td>Choose Project : </td>
						<td>
						<select id="smsProject" name="smsProject">
						<option></option>
						<%for(Project p : pl){
							pageContext.setAttribute("projectName",p.getName());
						%>	
							<option value="${projectName}">${projectName}</option>
						<%}%>
						</select></td></tr>
						<tr><td></td><td><input type="button" value="OK" onclick="submitRequest('<%=ServiceType.SEND_TEST_SMS%>');"/></td></tr>
				</table>
				</div>
			</tr>
		</table>
	</div>
</form>
