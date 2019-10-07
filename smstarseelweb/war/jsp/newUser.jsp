<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.UserQueryParams"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
<!--
function submitRequest() {

	var username = document.getElementById("newusername").value;
	var pwd = document.getElementById("pwd").value;
	var repwd = document.getElementById("rePwd").value;
	var firstName = document.getElementById("firstName").value;
	var lastName = document.getElementById("lastName").value;
	var email = document.getElementById("email").value;
	var userRoleName = document.getElementById("userRoleName").value;

	if(!/^[0-9A-Za-z]{6,20}$/.test(username)){
		showMsg('Username must be an alphanumeric sequence of 6-20 characters');
		return;
	}

	if(!/^[0-9A-Za-z]{6,20}$/.test(pwd)){
		showMsg('Password must be an alphanumeric sequence of 6-20 characters');
		return;
	}
	
	if(pwd != repwd){
		showMsg('Both passwords donot match');
		return;
	}
	
	if(!/^[A-Za-z]+$/.test(firstName)){
		showMsg('Valid first name must be provided');
		return;
	}
	
	if(!/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(email)){
		showMsg('Valid email must be provided');
		return;
	}
	
	if(userRoleName == ''){
		showMsg('Specify role for new user');
		return;
	}
	
	if(confirm("Are you sure to submit data ?")){
		document.getElementById("btnSubmit").style.disabled=true;
		var queryParams = new Object();
		queryParams['<%=UserQueryParams.USERNAME%>'] = username;
		queryParams['<%=UserQueryParams.NEW_PASSWORD%>'] = pwd; 
		queryParams['<%=UserQueryParams.RENEW_PASSWORD%>'] = repwd; 
		queryParams['<%=UserQueryParams.FIRSTNAME%>'] = firstName; 
		queryParams['<%=UserQueryParams.LASTNAME%>'] = lastName;
		queryParams['<%=UserQueryParams.EMAIL%>'] = email; 
		queryParams['<%=UserQueryParams.ROLE%>'] = userRoleName; 
		
		try {
			$.getJSON('/smstarseelweb/admin/add_user.dm', queryParams,
				function(response) {
					document.getElementById("newusername").value = '';
					document.getElementById("pwd").value = '';
					document.getElementById("rePwd").value = '';
					document.getElementById("firstName").value = '';
					document.getElementById("lastName").value = '';
					document.getElementById("email").value = '';
					
					showMsg(response['message']);
					document.getElementById("btnSubmit").style.disabled=false;
					if(response['message'].startsWith('SUCCESS')) window.location.reload();
				})
			//feel free to use chained handlers, or even make custom events out of them!
			/* .success(function() { alert("second success"); }) */
			.error(function() {
				alert("Error while loading data....");
			})
			/* .complete(function() { alert("complete"); }) */;
		} catch (e) {
				alert('exception:' + e);
		}
	}
	

}

function showMsg(msg){
	document.getElementById("messageDiv").innerHTML="<p><span style=\"color:green\">"+msg+"</span></p>";
}
//-->
</script>

<div id="messageDiv"></div>

	<div align="center" class="divCenter" style="width: auto;">
		<table>
			<tr style="background-color: silver;">
			</tr>
			<tr><td>Username/Login ID : </td><td><input type="text" id="newusername" name="newusername" value="" /></td></tr>
			<tr><td>Password : </td><td><input type="password" id="pwd" name="pwd" value="" /></td></tr>
			<tr><td>Re-Enter Password : </td><td><input type="password" id="rePwd" name="rePwd" value="" /></td></tr>
			<tr><td>First Name : </td><td><input type="text" id="firstName" name="firstName" value="" /></td></tr>
			<tr><td>Last Name : </td><td><input type="text" id="lastName" name="lastName" value="" /></td></tr>
			<tr><td>Email : </td><td><input type="email" id="email" name="email" value="" /></td></tr>
			<tr><td>User Role : </td>
			<td>
			<select id="userRoleName" name="userRoleName">
				<option></option>
				<c:forEach items="${roles}" var="rol">
				<option>${rol.name}</option>
				</c:forEach>
			</select></td></tr>
			<tr><td></td><td><input id="btnSubmit" type="button" value="OK" onclick="submitRequest();"/></td></tr>
		</table>
	</div>
