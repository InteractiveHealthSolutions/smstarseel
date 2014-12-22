<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.UserQueryParams"%>
<%@page import="org.irdresearch.smstarseel.data.User.UserStatus"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.QueryParams"%>
<%@include file="/template/header.jsp"%>
<style><!--
.datagrid-cell {
  font-size: 10px;
}
.panel-title {
  font-size: 20px;
}
--></style>
<table id="test" style="overflow: auto;"></table>
<div id="tbFilterList" style="height:30px">
<div style="float: left;">
<input id="txtUserPartOfName" type="text" placeholder="First/Last name" style="width: 105px" title="First/Last name" class="easyui-tooltip">   
<input id="txtEmail" type="text" placeholder="Email" style="width: 105px" title="Email" class="easyui-tooltip">

<select class="easyui-combobox" id="userStatus" data-options="width:110">
	<option value="">Select status</option>
	<c:forEach items="<%= UserStatus.values()%>" var="usSt">
	<option>${usSt}</option>
	</c:forEach>
</select>
<script type="text/javascript"><!--
document.getElementById("userStatus").selectedIndex=0;
//--></script>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="loadthedata();">Query</a>
</div>

<div style="float: right;">
    <a class="easyui-linkbutton" iconCls="icon-add" onclick="$('#winNewUser').window('open');">New User</a>
    <div id="winNewUser"></div>
</div>	
</div>
<div id="dlgResPwd" >
<div id="msgDiv"></div>
<div align="center" class="divCenter" style="width: auto;">
<table style="width: 100%">
	<tr><td colspan="2"><span class="heading">User : <input id="usernameUnderProcess" type="button" style="background: transparent;border: none"> </span></td></tr>
	<!-- <tr><td>Your Password(for authentication): </td><td><input type="password" id="authpwd" name="authpwd" value="" /></td></tr> -->
	<tr><td>New Password:</td><td><input type="password" id="npwd" name="npwd" value="" /></td></tr>
	<tr><td>Re-Enter Password: </td><td><input type="password" id="repwd" name="repwd" value="" /></td></tr>
</table>
</div>
</div>  

<script>
function showMsg(msg){
	document.getElementById("msgDiv").innerHTML="<p><span style=\"color:green\">"+msg+"</span></p>";
}

// NEW USER WINDOW
$('#winNewUser').window({
	title: 'Create New User',
	href : '${pageContext.request.contextPath}/admin/addNewUser.htm',
	width: 500,  
    height: 400,
    minimizable: false,
    resizable: false,
    maximizable: false,
    draggable: false,
    closed: true,
    modal:true
}); 

var queryParams = new Object();
queryParams['<%=QueryParams.PAGE_SIZE%>'] = 20;
queryParams['<%=QueryParams.PAGE_NUMBER%>'] = 1;

//INIT TOOLTIPS OF TEXTBOXES WITHOUT ANY LABEL
$('input[id^="txt"]').on( 'focus', function() {
	$(this).tooltip( {hideDelay: 200000} );
    $(this).tooltip( 'show' );
});

$('input[id^="txt"]').on( 'blur', function() {
	$(this).tooltip( {hideDelay: 100} );
    $(this).tooltip( 'hide' );
});

$('#dlgResPwd').dialog({ 
	closed: true,  
    cache: false,  
    modal: true 
});
function resetPwd(username) {
	$('#dlgResPwd').dialog({  
	    title: 'Reset Pwd (only for admin)',  
	    width: 400,  
	    height: 320,  
	    closed: false,  
	    cache: false,  
	    modal: true ,
	    onBeforeOpen : function () {
	    	document.getElementById('usernameUnderProcess') .value = username;
		},
		buttons:[{
			text:'OK',
			iconCls:'icon-ok',
			handler:function(){
				var username=document.getElementById("usernameUnderProcess").value;
				//var opwd=document.getElementById("authpwd").value;
				var npwd=document.getElementById("npwd").value;
				var cpwd=document.getElementById("repwd").value;
				
				/* if(!/^[0-9A-Za-z]+$/.test(opwd)){
					showMsg('A non-whitespace your authentication password must be specified');
					return;
				} */
				
				if(!/^[0-9A-Za-z]{6,20}/.test(npwd)){
					showMsg('New password must be an alphanumeric sequence of 6-20 characters');
					return;
				}
				
				if(npwd != cpwd){
					showMsg('Both passwords donot match');
					return;
				}
				
				if(confirm("Are you sure you want to reset password for user '"+username+"' ?")){
					var queryParams = new Object();
					queryParams['<%=UserQueryParams.USERNAME%>'] = username; 
					queryParams['<%=UserQueryParams.NEW_PASSWORD%>'] = npwd; 
					queryParams['<%=UserQueryParams.RENEW_PASSWORD%>'] = cpwd; 
					
					try {
						$.getJSON('/smstarseelweb/admin/reset_password.dm', queryParams,
							function(response) {
								//document.getElementById("authpwd").value = '';
								document.getElementById("npwd").value = '';
								document.getElementById("repwd").value = '';
								document.getElementById("usernameUnderProcess").value='';
								showMsg(response['message']);
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
		}]
	});
}

var datagridName = 'test';

$(function(){
	$('#'+datagridName).datagrid({
		title:'Users',
		iconCls:'icon-reminder',
		width:750,
		height:550,
		nowrap: true,
		autoRowHeight: false,
		loader: loadthedata,
		selectOnCheck: false,
		singleSelect: true,
		idField:'userId', 
		/*frozenColumns:[[
               {title:'Reference Number',field:'referenceNumber',width:130,sortable:true}
		]],*/
		columns:[[  
		    {field:'userId',title:'', hidden:true}, 
		    {field:'name',title:'Login ID',width:80}, 
		    {field:'firstName',title:'First Name',width:100},
		    {field:'lastName',title:'Last Name',width:100},  
		    {field:'email',title:'Email',width:150}, 
		    {field:'status',title:'Status',width:100}, 
		    {field:'createdDate',title:'Created Date',width:135, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    },
		    {title:'--',field:'abc', width:100, 
		    	formatter: function(value,row,index){
					if (row.name != 'admin' && row.name != 'administrator')
					{return "<a title='"+row.name+"' onclick='resetPwd(this.title);'>reset password</a>";} 
					else {return '';}
				}
		    }
		]]  , 
		rownumbers:true,
		toolbar: '#tbFilterList',
		pagination:true,
	});
	var p = $('#'+datagridName).datagrid('getPager');
	$(p).pagination({
		pageSize: queryParams['<%=QueryParams.PAGE_SIZE%>'],
		onSelectPage: function(pageNumber, pageSize){
			queryParams['<%=QueryParams.PAGE_SIZE%>'] = pageSize;
			queryParams['<%=QueryParams.PAGE_NUMBER%>'] = pageNumber;
			
			loadthedata();
        }
	}); 
});

function loadthedata(){
	/* var dgChoosenData = new Object();
	dgChoosenData['rows']=[]; */
	queryParams['<%=UserQueryParams.EMAIL%>'] = document.getElementById("txtEmail").value;
	queryParams['<%=UserQueryParams.PART_OF_NAME%>'] = document.getElementById("txtUserPartOfName").value;
	queryParams['<%=UserQueryParams.STATUS%>'] = $('#userStatus').datebox('getValue');
	
	try {
		$.getJSON('/smstarseelweb/admin/traverse_users.do', queryParams,
			function(response) {
				$('#' + datagridName).datagrid('loadData', response);
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
		//alert(JSON.stringify(queryParams));

		return false;
}
</script>
<%@include file="/template/footer.jsp"%>