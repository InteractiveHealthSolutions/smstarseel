<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.QueryParams"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.OutboundQueryParams"%>
<%@page import="org.irdresearch.smstarseel.global.RequestParam.OuboundSmsParams"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.CommunicationQueryParams"%>
<%@page import="org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus"%>
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
Due Date From : <input id="dtbDueFrom" class="easyui-datebox" style="width:100px"/>  
        To: <input id="dtbDueTo" class="easyui-datebox" style="width:100px"/> 
<input id="txtRecipient" type="text" placeholder="Recipient Number" style="width: 105px" title="Recipient Number" class="easyui-tooltip">   
<input id="txtImei" type="text" placeholder="Phone Imei" style="width: 105px" title="Phone Imei" class="easyui-tooltip">

<br>Sent Date From: <input id="dtbSentFrom" class="easyui-datebox" style="width:100px"/>  
        To: <input id="dtbSentTo" class="easyui-datebox" style="width:100px"/>  
        
<select class="easyui-combobox" id="smsStatus" data-options="width:110">
	<option value="">Select status</option>
	<c:forEach items="<%= OutboundStatus.values()%>" var="obSt">
	<option>${obSt}</option>
	</c:forEach>
</select>
<script type="text/javascript"><!--
document.getElementById("smsStatus").selectedIndex=0;
//--></script>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="loadthedata(0);">Search</a>
</div>

<div style="float: right;">
<input id="sbReferenceNumber" class="easyui-searchbox"/>
<br><br>
        <a class="easyui-linkbutton" iconCls="icon-sms" onclick="$('#winSendSms').window('open');" style="float: right;">Send Sms</a>

</div>	

</div>
<script>
$( document ).ready(function() {
    if('${param.sendSmsModal}'){
    	$('#winSendSms').window('open');
    }
});


var queryParams = new Object();
queryParams['<%=QueryParams.PAGE_SIZE%>'] = 20;
queryParams['<%=QueryParams.PAGE_NUMBER%>'] = 1;

try{ 
	$('input[id^="dtbDue"]').datebox({
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
		loadthedata(0);
    },  
    prompt:'Enter reference number'
}); 

var datagridName = 'test';

$(function(){
	$('#'+datagridName).datagrid({
		title:'Outbounds (Outgoing sms)',
		iconCls:'icon-reminder',
		width:750,
		height:550,
		nowrap: false,
		autoRowHeight: false,
		//url:'/smstarseelweb/communication/get_data.do',
		loader: loadthedata,
		selectOnCheck: false,
		singleSelect: true,
		idField:'referenceNumber',
		frozenColumns:[[
               {title:'Reference Number',field:'referenceNumber',width:140,sortable:true}
		]],
		columns:[[  
		    {field:'recipient',title:'Recipient',width:100}, 
		    {field:'status',title:'Status',width:80}, 
		    {field:'dueDate',title:'Due Date',width:135, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    },  
		    {field:'sentdate',title:'Sent Date',width:135, 
		    	formatter: function(value,row,index){
		    		if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    },  
		    {field:'tries',title:'Tries',width:40}, 
		    {field:'validityPeriod',title:'Validity',width:60,
		    	formatter: function(value,row,index){
					if (value != null && value != ''){
						return row.validityPeriod+'  '+row.periodType;
					} else {
						return value;
					}
				}
		    }, 
		    {field:'failureCause',title:'Failure Cause',width:105}, 
		    {field:'imei',title:'Imei',width:115},
		    {field:'originator',title:'Originator',width:90},  
		    {field:'text',title:'Text',width:400}, 
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
	queryParams['<%=QueryParams.PAGE_SIZE%>'] = 90071992;
	queryParams['<%=QueryParams.PAGE_NUMBER%>'] = 1;
	
	loadthedata(1);
}
function loadthedata(downloadRequest){
	/* var dgChoosenData = new Object();
	dgChoosenData['rows']=[]; */
	queryParams['<%=CommunicationQueryParams.REFERENCE_NUMBER%>'] = $('#sbReferenceNumber').searchbox('getValue');
	queryParams['<%=OutboundQueryParams.DUEDATE_FROM%>'] = $('#dtbDueFrom').datebox('getValue');
	queryParams['<%=OutboundQueryParams.DUEDATE_TO%>'] = $('#dtbDueTo').datebox('getValue');
	queryParams['<%=OutboundQueryParams.SENTDATE_FROM%>'] = $('#dtbSentFrom').datebox('getValue');
	queryParams['<%=OutboundQueryParams.SENTDATE_TO%>'] = $('#dtbSentTo').datebox('getValue');
	queryParams['<%=CommunicationQueryParams.IMEI%>'] = document.getElementById("txtImei").value;
	queryParams['<%=CommunicationQueryParams.REFERRED_NUMBER%>'] = document.getElementById("txtRecipient").value;
	queryParams['<%=CommunicationQueryParams.STATUS%>'] = $('#smsStatus').datebox('getValue');
	
	try {
		$.getJSON('/smstarseelweb/communication/traverse_outbounds.do', queryParams,
			function(response) {
				
				if(downloadRequest == 1) {
					$('#' + datagridName).datagrid('loadData', response);
					var dat = $('#' + datagridName).datagrid('getData');
					// "outboundId", "text", "recipient", "originator",	"dueDate",	"tries", "status", "referenceNumber", "validityPeriod",	"periodType", "imei", "failureCause", "sentdate", "errormessage"
					exportCSV(dat, new Array("outboundId","systemProcessingStartDate", "systemProcessingEndDate", "type","priority", "description", "errorMessage", "projectId", "project", "createdDate"));
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
<div id="winSendSms"></div>
<script type="text/javascript"><!--
$('#winSendSms').window({
	title: 'Send sms instantly',
	href : '${pageContext.request.contextPath}/communication/sendSms.htm',
	width: 360, 
	collapsible: false,
    height: 450,
    minimizable: false,
    resizable: false,
    maximizable: false,
    draggable: false,
    closed: true,
    modal:true
}); 
//--></script>
<div id = "export_div" >
	<a onclick="downloadtheData()" id ="btn_export"  class="easyui-linkbutton right" >Export</a>
</div>
<%@include file="/template/footer.jsp"%>