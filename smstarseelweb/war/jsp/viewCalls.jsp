<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.CallQueryParams"%>
<%@page import="org.irdresearch.smstarseel.data.CallLog.CallStatus"%>
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
<input id="txtCaller" type="text" placeholder="Caller Number" style="width: 105px" title="Caller Number" class="easyui-tooltip">   
<input id="txtImei" type="text" placeholder="Phone Imei" style="width: 105px" title="Phone Imei" class="easyui-tooltip">

<select class="easyui-combobox" id="callStatus" data-options="width:110">
	<option value="">Select status</option>
	<c:forEach items="<%= CallStatus.values()%>" var="cSt">
	<option>${cSt}</option>
	</c:forEach>
</select>
<script type="text/javascript"><!--
document.getElementById("callStatus").selectedIndex=0;
//--></script>
<br>
Call Date From : <input id="dtbCallFrom" class="easyui-datebox" style="width:100px"/>  
        To: <input id="dtbCallTo" class="easyui-datebox" style="width:100px"/> 
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="loadthedata();">Search</a>
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
	$('input[id^="dtbCall"]').datebox({
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
		loadthedata();
    },  
    prompt:'Enter reference number'
}); 

var datagridName = 'test';

$(function(){
	$('#'+datagridName).datagrid({
		title:'Call Logs',
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
		    {field:'callerNumber',title:'Caller Number',width:100}, 
		    {field:'callStatus',title:'Status',width:80}, 
		    {field:'callDate',title:'Call Date',width:135, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    },  
		    {field:'imei',title:'Imei',width:115},
		    {field:'recipient',title:'Sim',width:90},  
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
	queryParams['<%=CommunicationQueryParams.REFERENCE_NUMBER%>'] = $('#sbReferenceNumber').searchbox('getValue');
	queryParams['<%=CallQueryParams.CALLDATE_FROM%>'] = $('#dtbCallFrom').datebox('getValue');
	queryParams['<%=CallQueryParams.CALLDATE_TO%>'] = $('#dtbCallTo').datebox('getValue');
	queryParams['<%=CommunicationQueryParams.IMEI%>'] = document.getElementById("txtImei").value;
	queryParams['<%=CommunicationQueryParams.REFERRED_NUMBER%>'] = document.getElementById("txtCaller").value;
	queryParams['<%=CommunicationQueryParams.STATUS%>'] = $('#callStatus').datebox('getValue');
	
	try {
		$.getJSON('/smstarseelweb/communication/traverse_calls.do', queryParams,
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