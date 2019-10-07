<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.UserQueryParams"%>
<script type="text/javascript">
<!--
function change() {

	opwd=document.getElementById("opwd").value;
	npwd=document.getElementById("npwd").value;
	cpwd=document.getElementById("cpwd").value;
	
	if(!/^[0-9A-Za-z]+$/.test(opwd)){
		showMsg('A non-whitespace old password must be specified');
		return;
	}
	
	if(opwd == npwd){
		showMsg('Specified new password is same as old password');
		return;
	}
	
	if(!/^[0-9A-Za-z]{6,20}/.test(npwd)){
		showMsg('New password must be an alphanumeric sequence of 6-20 characters');
		return;
	}
	
	if(npwd != cpwd){
		showMsg('Both passwords donot match');
		return;
	}
	
	if(confirm("Are you sure you want to change password ?")){
		document.getElementById("changebtn").style.disabled=true;
		var queryParams = new Object();
		queryParams['<%=UserQueryParams.OLD_PASSWORD%>'] = opwd;
		queryParams['<%=UserQueryParams.NEW_PASSWORD%>'] = npwd; 
		queryParams['<%=UserQueryParams.RENEW_PASSWORD%>'] = cpwd; 
		
		try {
			$.getJSON('/smstarseelweb/admin/change_password.dm', queryParams,
				function(response) {
					document.getElementById("opwd").value = '';
					document.getElementById("npwd").value = '';
					document.getElementById("cpwd").value = '';
				
					showMsg(response['message']);
					document.getElementById("changebtn").style.disabled=false;
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
				<td colspan="2">Change your password</td>
			</tr>
			<tr>
				<td colspan="2"><span class="error" style="font-size: x-small; color: red">${errorMessage}</span></td>
			</tr>
			<tr>
			    <td>Old Password</td>
			    <td><input id="opwd" name="opwd" type="password" maxlength="15"/></td>
			  </tr>
			  <tr>
			    <td>New Password</td>
			    <td><input id="npwd" name="npwd" type="password" maxlength="15"/></td>
			  </tr>
			  <tr>
			    <td>Confirm New Password</td>
			    <td><input id="cpwd" name="cpwd" type="password" maxlength="15"/></td>
			  </tr>
			<tr>
				<td></td>
    			<td><input id="changebtn" type="button" value="Change" onclick="change();"/></td>
			</tr>
		</table>
	</div>
