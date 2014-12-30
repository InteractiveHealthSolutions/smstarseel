<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.ProjectQueryParams"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.CommunicationQueryParams"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.DeviceQueryParams"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.QueryParams"%>
<%@page import="org.irdresearch.smstarseel.data.Device.DeviceStatus"%>
<%@include file="/template/header.jsp"%>
<style><!--
.datagrid-cell {
  font-size: 10px;
}
.panel-title {
  font-size: 20px;
}
--></style>
   
<script type="text/javascript">
function editProject(projectId, projectName) {
	var newname = prompt("Enter a short and precise name for the project. \n\nNote: The project name would just change the display name and in backend would be associated with existing project ID. \n\nIf you want new project setup, add project from add button in the tab. \n\nEditing project name would keep existing devices, sms, and calls still associated with same project.", projectName);
	if(newname != null){
		try {
			var queryParams = new Object();
			queryParams['<%=ProjectQueryParams.PROJECT_NAME%>'] = newname; 
			queryParams['<%=ProjectQueryParams.PROJECT_ID%>'] = projectId; 
			
			$.getJSON('/smstarseelweb/admin/edit_project.dm', queryParams,
				function(response) {
					alert(response['message']);
					if(response['SUCCESS']){
						document.getElementById("prjspan"+projectId).innerHTML = ' --> '+newname;
					}
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
</script>
    <div id="tbAdmin" class="easyui-tabs">
        <div title="Projects" data-options="">  
       		<div>
       		<p style="color: red;">${message}</p>
       		<table>
       			<tr><th></th><th>Project ID</th><th>Name</th></tr>
	            <c:forEach items="${projects}" var="prj">
	            <tr><td><a onclick="editProject(${prj.projectId},'${prj.name}');">edit</a></td>
	            <td>${prj.projectId}</td><td>${prj.name}<span id="prjspan${prj.projectId}" style="font-weight:bold ;color: green"></span></td></tr>
	            </c:forEach>
       		</table>
            </div>
        </div>  
        <div title="Device Activity" data-options="" style="overflow:auto;">  
            <table id="test"></table>  
        </div>
    </div>  
    

<div id="tbFilterList" style="height: 30px">
<div style="float: left;">
<input id="txtSim" type="text" placeholder="Sim Number" style="width: 105px" title="Sim Number" class="easyui-tooltip">   
<input id="txtImei" type="text" placeholder="Phone Imei" style="width: 105px" title="Phone Imei" class="easyui-tooltip">
    
<select class="easyui-combobox" id="deviceStatus" data-options="width:110">
	<option value="">Select status</option>
	<c:forEach items="<%= DeviceStatus.values()%>" var="dvSt">
	<option>${dvSt}</option>
	</c:forEach>
</select>
<script type="text/javascript"><!--
document.getElementById("deviceStatus").selectedIndex=0;
//--></script>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="loadthedata();">Query</a>
</div>
</div>
<script>
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

function discardDevice(deviceId, imei, sim) {
	if(confirm("Are you sure to discard device with imei "+imei+" and sim "+sim+" ? Note that the action is irreversable and any active services working on device would be crashed. Make sure to stop all services on device before discarding.")){
		window.location = '/smstarseelweb/admin/discard_device.dm?DEVICE_ID='+deviceId;
	}
}
var datagridName = 'test';

$(function(){
	$('#'+datagridName).datagrid({
		title:'Device Activity',
		iconCls:'icon-reminder',
		width:750,
		height:500,
		noheader: true,
		nowrap: true,
		autoRowHeight: false,
		loader: loadthedata,
		selectOnCheck: false,
		singleSelect: true,
		idField:'imei',
		frozenColumns:[[
               {title:'Imei',field:'imei',width:100,sortable:true}
		]],
		columns:[[  
		    {field:'discard',title:'X',width:50, 
		    	formatter: function(value,row,index){
					if (row['status'].toLowerCase() == 'active'){
						return '<a onclick=\'discardDevice('+row['deviceId']+',"'+row['imei']+'","'+row['sim']+'")\'>discard</a>';
					} 
					else {return '';}
				}
		    }, 
		    {field:'sim',title:'SIM',width:90}, 
		    {field:'project',title:'Project',width:90, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return row['project'].name;} 
					else {return value;}
				}},
		    {field:'status',title:'Status',width:70}, 
		    {field:'dateAdded',title:'Add Date',width:120, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    },  
		    {field:'deviceName',title:'Device Name',width:130}, 
		    {field:'dateLastOutboundPing',title:'Outound Ping Date',width:145, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    }, 
		    {field:'dateLastInboundPing',title:'Inbound Ping Date',width:145, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    }, 
		    {field:'dateLastCalllogPing',title:'CallLog Ping Date',width:145, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    }, 
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
	queryParams['<%=CommunicationQueryParams.IMEI%>'] = document.getElementById("txtImei").value;
	queryParams['<%=CommunicationQueryParams.REFERRED_NUMBER%>'] = document.getElementById("txtSim").value;
	queryParams['<%=CommunicationQueryParams.STATUS%>'] = $('#deviceStatus').datebox('getValue');
	
	try {
		$.getJSON('/smstarseelweb/admin/traverse_devices.do', queryParams,
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