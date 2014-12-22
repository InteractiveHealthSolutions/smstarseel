<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.InboundQueryParams"%>
<%@page import="org.irdresearch.smstarseel.data.InboundMessage.InboundStatus"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.QueryParams"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.CommunicationQueryParams"%>
<%@include file="/template/header.jsp"%>
<style><!--
.datagrid-cell {
  font-size: 10px;
}
.panel-title {
  font-size: 20px;
}
--></style>

<table id="test"></table>
<div id="tbFilterList" style="height:70px">
<div style="float: left;">
<input id="txtOriginator" type="text" placeholder="Originator Number" style="width: 105px" title="Originator Number" class="easyui-tooltip">   
<input id="txtImei" type="text" placeholder="Phone Imei" style="width: 105px" title="Phone Imei" class="easyui-tooltip">

<select class="easyui-combobox" id="smsStatus" data-options="width:110">
	<option value="">Select status</option>
	<c:forEach items="<%= InboundStatus.values()%>" var="ibSt">
	<option>${ibSt}</option>
	</c:forEach>
</select>
<script type="text/javascript"><!--
document.getElementById("smsStatus").selectedIndex=0;
//--></script>
<br>
Received From : <input id="dtbReceiveFrom" class="easyui-datebox" style="width:100px"/>  
        To: <input id="dtbReceiveTo" class="easyui-datebox" style="width:100px"/> 
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="loadthedata(0);">Query</a>
</div>

<div style="float: right;">
<input id="sbReferenceNumber" class="easyui-searchbox"/>
</div>	

</div>
<script>
var queryParams = new Object();
queryParams['<%=QueryParams.PAGE_SIZE%>'] = 20;
queryParams['<%=QueryParams.PAGE_NUMBER%>'] = 1;

try{ 
	$('input[id^="dtbReceive"]').datebox({
		value: (new Date().toString('dd-MMM-yyyy'))
	});
}
catch (e) {
	alert(e);
}
$('input[id^="dtb"]').datebox({formatter : function(date){
	return date.toString('dd-MMM-yyyy');
}});

$('input[id^="dtb"]').datebox({parser : function(s){
	var t = Date.parse(s);
	if (!isNaN(t)){
		return new Date(t);
	} else {
		return null;
	}
}});

//INIT TOOLTIPS OF TEXTBOXES WITHOUT ANY LABEL
$('input[id^="txt"]').on( 'focus', function() {
	$(this).tooltip( {hideDelay: 200000} );
    $(this).tooltip( 'show' );
});

$('input[id^="txt"]').on( 'blur', function() {
	$(this).tooltip( {hideDelay: 100} );
    $(this).tooltip( 'hide' );
});

// INIT REFERENCE NUMBER SEARCHBOX
$('#sbReferenceNumber').searchbox({ 
    searcher:function(value, name){  
		queryParams['<%=CommunicationQueryParams.REFERENCE_NUMBER%>'] = value;
		loadthedata(0);
    },  
    prompt:'Enter reference number'
}); 

var datagridName = 'test';

$(function(){
	$('#'+datagridName).datagrid({
		title:'Inbounds (Incoming sms)',
		iconCls:'icon-reminder',
		width:750,
		height:550,
		nowrap: true,
		autoRowHeight: false,
		loader: loadthedata,
		selectOnCheck: false,
		singleSelect: true,
		idField:'referenceNumber',
		frozenColumns:[[
               {title:'Reference Number',field:'referenceNumber',width:130,sortable:true}
		]],
		columns:[[  
		    {field:'originator',title:'Originator',width:100}, 
		    {field:'status',title:'Status',width:80}, 
		    {field:'systemRecieveDate',title:'System Receive Date',width:135, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    }, 
		    {field:'recieveDate',title:'Receive Date',width:135, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
	    	},
		    {field:'imei',title:'Imei',width:115},
		    {field:'recipient',title:'Sim',width:90},  
		    {field:'text',title:'Text',width:200}, 
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
			
			loadthedata(0);
        }
	}); 
});
function downloadtheData() {
	// Integer.MAX value of java = 2147483647
	queryParams['<%=QueryParams.PAGE_SIZE%>'] = 2147483647; 
	queryParams['<%=QueryParams.PAGE_NUMBER%>'] = 1;
	
	loadthedata(1);
}
function loadthedata(downloadRequest){
	/* var dgChoosenData = new Object();
	dgChoosenData['rows']=[]; */
	queryParams['<%=InboundQueryParams.RECEIVEDATE_FROM%>'] = $('#dtbReceiveFrom').datebox('getValue');
	queryParams['<%=InboundQueryParams.RECEIVEDATE_TO%>'] = $('#dtbReceiveTo').datebox('getValue');
	queryParams['<%=CommunicationQueryParams.IMEI%>'] = document.getElementById("txtImei").value;
	queryParams['<%=CommunicationQueryParams.REFERRED_NUMBER%>'] = document.getElementById("txtOriginator").value;
	queryParams['<%=CommunicationQueryParams.STATUS%>'] = $('#smsStatus').datebox('getValue');
	
	try {
		$.getJSON('/smstarseelweb/communication/traverse_inbounds.do', queryParams,
			function(response) {
				
				if(downloadRequest == 1) {
					$('#' + datagridName).datagrid('loadData', response);
					var dat = $('#' + datagridName).datagrid('getData');
					// "text","systemRecieveDate","originator","recieveDate"
					exportCSV(dat, new Array("inboundId","recipient","systemProcessingStartDate","systemProcessingEndDate","status","type","imei","referenceNumber","project", "projectId"));
				} else {
					$('#' + datagridName).datagrid('loadData', response);
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
		//alert(JSON.stringify(queryParams));

		return false;
}
</script>
<div id = "export_div" >
	<a onclick="downloadtheData()" id ="btn_export"  class="easyui-linkbutton right" >Export</a>
</div>
<%@include file="/template/footer.jsp"%>