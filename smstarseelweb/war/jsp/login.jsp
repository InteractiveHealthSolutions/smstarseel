<%@page import="org.irdresearch.smstarseel.web.util.UserSessionUtils"%>
<%@ include file="/template/headersimple.jsp"%>

<%
	if (UserSessionUtils.getActiveUser(request) != null) {
%>
<c:redirect url="${pageContext.request.contextPath}/home/home.htm"></c:redirect>
<%
	}
%>
<script type="text/javascript">
function submitForm() {
	var username = document.getElementById("username").value;
	var pwd = document.getElementById("password").value;

	if(trim(username) == ''){
		alert('Username not specified');
		return;
	}

	if(trim(pwd) == ''){
		alert('Password not specified');
		return;
	}
	document.getElementById("formId").submit();
}
</script>
<form id="formId" name="formId" method="post">
	<div align="center" class="divCenter">
		<table>
			<tr style="background-color: silver;">
				<td colspan="2">Log in</td>
			</tr>
			<tr>
				<td colspan="2"><span class="error" style="font-size: x-small; color: red">${errorMessage}</span></td>
			</tr>
			<tr>
				<td>Username:</td>
				<td><input type="text" id="username" name="username" value="" /></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" id="password" name="password" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" id="submitBtn" value="Login" onclick="submitForm();"></td>
			</tr>
		</table>
	</div>
</form>
<script type="text/javascript">
 document.getElementById('username').focus();
</script>

<%@ include file="/template/footersimple.jsp"%>