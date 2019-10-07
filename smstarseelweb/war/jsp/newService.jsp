<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.UserQueryParams"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@include file="/template/header.jsp"%>

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
<form:form id="frm" action="${pageContext.request.contextPath}/admin/addNewService.htm" commandName="service" modelAttribute="service" method="post">
<h1>Register New Service</h1>
<div id="messageDiv"></div>

	<div align="center" style="width: auto;">
		<table>
			<tr style="background-color: silver;">
			</tr>
			<tr><td>Service Name : </td><td><form:input path="serviceName" />
			<form:errors path="serviceName" cssClass="field-error" /></td></tr>
			<tr><td>Service ID : </td><td><form:input path="serviceIdentifier" />
			<form:errors path="serviceIdentifier" cssClass="field-error" /></td></tr>
			<tr><td>Outbound Success Report Url : </td><td><form:textarea path="outboundSuccessReportUrl" />
			<form:errors path="outboundSuccessReportUrl" cssClass="field-error" /></td></tr>
			<tr><td>Outbound Failure Report Url : </td><td><form:textarea path="outboundFailureReportUrl" />
			<form:errors path="outboundFailureReportUrl" cssClass="field-error" /></td></tr>
			<tr><td>Inbound Report Url : </td><td><form:textarea path="inboundReportUrl" />
			<form:errors path="inboundReportUrl" cssClass="field-error" /></td></tr>
			<tr><td>Delivery Report Url : </td><td><form:textarea path="deliveryReportUrl" />
			<form:errors path="deliveryReportUrl" cssClass="field-error" /></td></tr>
			<tr><td>Project : </td>
			<td>
			<select id="project" name="project.projectId">
				<option></option>
				<c:forEach items="${projects}" var="p">
				<option value="${p.projectId}">${p.name}</option>
				</c:forEach>
			</select>
			<form:errors path="project.projectId" cssClass="field-error" /></td></tr>
			<tr><td>Additional Note : </td><td><form:textarea path="description" /></td></tr>
			<tr><td></td><td><input id="btnSubmit" type="submit" value="OK" /></td></tr>
		</table>
	</div>
</form:form>

<%@include file="/template/footer.jsp"%>