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
<div style="color:green">${param.message}</div>
<div>A service can call url to send sms with param auth=API_KEY and project=projectName. If no project name is specified 
sms would be send for project service is registered for. Note : discarding a service invalidates the auth key an a new 
registeration is required:
For telenor: add service=telenor in param</div>
<table id="test" style="overflow: auto;"></table>
<div id="tbFilterList" style="height:30px">
<div style="float: right;">
    <a class="easyui-linkbutton" iconCls="icon-add" href="${pageContext.request.contextPath}/admin/addNewService.htm">New Service</a>
    <div id="winNewService"></div>
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

$('#dlgResPwd').dialog({ 
	closed: true,  
    cache: false,  
    modal: true 
});

function discardService(serviceId, authKey) {
	if(confirm("Are you sure to discard service with authKey "+authKey+"? Note that the action is irreversable and any active services would be crashed. Make sure to stop all services on device before discarding.")){
		window.location = '/smstarseelweb/admin/discard_service.dm?SERVICE_ID='+serviceId;
	}
}

var datagridName = 'test';

$(function(){
	$('#'+datagridName).datagrid({
		title:'Services',
		iconCls:'icon-reminder',
		width:750,
		height:550,
		nowrap: true,
		autoRowHeight: false,
		loader: loadthedata,
		selectOnCheck: false,
		singleSelect: true,
		idField:'serviceId', 
		columns:[[
			{field:'discard',title:'X',width:50, 
		    	formatter: function(value,row,index){
					if (row['status'].toLowerCase() == 'active'){
						return '<a onclick=\'discardService('+row['serviceId']+',"'+row['authenticationKey']+'")\'>discard</a>';
					} 
					else {return '';}
				}
		    },
		    {field:'serviceId',title:'', hidden:true}, 
		    {field:'serviceName',title:'Name/ID'}, 
		    {field:'status',title:'Status',width:70}, 
		    {field:'authenticationKey',title:'API Key'}, 
		    {field:'project',title:'Project',width:120, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return row['project'].name;} 
					else {return value;}
			}},
			{field:'dateAdded',title:'Date Added',width:135, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    },
		    {field:'description',title:'Description',width:100},  
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
	
	try {
		$.getJSON('/smstarseelweb/admin/traverse_services.do', queryParams,
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