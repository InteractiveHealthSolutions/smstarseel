<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.TarseelSetting"%>
<%@page import="org.irdresearch.smstarseel.context.TarseelContext"%>
<%@page import="org.irdresearch.smstarseel.data.OutboundMessage.PeriodType"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.OutboundQueryParams"%>
<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.CommunicationQueryParams"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style><!--
/* .datagrid-cell {
  font-size: 10px;
} */
.panel-title {
  font-size: 20px;
}
--></style>
<div id='pnSendSms' class="easyui-panel" style="width:350px; height: 360px;"
data-options="style:{ background: transparent}">  

<table id="dgRecipientList"></table>

<div id="tbRecipientList">
	
<select class="easyui-combobox" id="projects" data-options="width:110">
	<option value="">Select project</option>
	<c:forEach items="${projects}" var="proj">
	<option>${proj.name}</option>
	</c:forEach>
</select>
		<span style="font-size: xx-small; float: right;">Validity : <input id="txbvalidityPeriod" type="text" value="8" maxlength="2" class="easyui-tooltip" title="Discard sms if not sent within given hours from now" style="width: 30px">(hrs)
		</span>
<textarea id="txtMsg" rows="5" class="easyui-tooltip" title="Sms text to send" placeholder="Enter sms text here" maxlength="1000" style="width: 345px; resize: none"></textarea> 
<input id="txtManualRecipientNumber" style="width:100px;" maxlength="11" class="easyui-tooltip" title="Recipient Number" placeholder="03354444555">
<a class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addCellNumberToRecipientList(document.getElementById('txtManualRecipientNumber'));">Add</a>
<a class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteCellNum();"></a>

<a class="easyui-linkbutton" iconCls="icon-save" onclick="sendSMSs();" style="float: right;">Send</a>
</div>
<textarea id="txtDescr" rows="3" style="width: 345px; resize: none" maxlength="255" class="easyui-tooltip" title="Additional note" placeholder="Additional note for sms record"></textarea> 
</div>
<script type="text/javascript">
<!--
//INIT TOOLTIPS OF TEXTBOXES WITHOUT ANY LABEL
$('input[id^="txt"]').on( 'focus', function() {
	$(this).tooltip( {hideDelay: 200000} );
    $(this).tooltip( 'show' );
});

$('input[id^="txt"]').on( 'blur', function() {
	$(this).tooltip( {hideDelay: 100} );
    $(this).tooltip( 'hide' );
});

var dgSelectedRecipientListId = "dgRecipientList";

// DATAGRID FOR SELECTED RECIPIENTS
$('#'+dgSelectedRecipientListId).datagrid({
	iconCls:'icon-reminder', 
	width:350,
	height:350,
	nowrap: true, 
	autoRowHeight: false,
	showHeader: false,
	checkOnSelect: false,
	collapsible:false, 
	idField:'recipientNumber', 
	frozenColumns:[[
              {field:'ck',checkbox:true},
	]], 
	columns:[[
		{field:'recipientNumber',title:'Number',width:100},
	]],
	toolbar: '#tbRecipientList',
	rownumbers:true
});

function validateCellNum(cellnum){
	var rgx = '<%=TarseelContext.getSetting(TarseelSetting.CELL_NUMBER_VALIDATOR_REGEX.NAME(), "")%>';
	if(rgx == null || rgx == '' || rgx == 'null'){
		rgx = "^\\d{3,15}$";
	}
	var reg = new RegExp(rgx);
	 if(reg.test(cellnum) == false) {
	      return false;
	 }
	 return true;
}

function findInRecipientList(numberAdded) {
	var allrows = $('#'+dgSelectedRecipientListId).datagrid('getRows');
	
	for ( var i = 0; i < allrows.length; i++) {
		if (allrows[i].number === numberAdded) {
			return true;
		}
	}
	return false;
}

function addCellNumberToRecipientList(txtbox){
	var numberAdded = txtbox.value;
	if(!validateCellNum(numberAdded)){
		alert('Invalid Number. Cell number must conform to specified format');
		return;
	}
	var index = $('#'+dgSelectedRecipientListId).datagrid('getRowIndex', numberAdded);
	if(index != -1){
		alert('Given number already exists in the list');
		return;
	}
	
	
	if (findInRecipientList(numberAdded)) {
		alert('Given number already exists in the list');
		return;
	}

	$('#' + dgSelectedRecipientListId).datagrid('appendRow', {
		recipientNumber : numberAdded,
	});
	
	txtbox.value="";
}

function deleteCellNum() {
	var selected = $('#' + dgSelectedRecipientListId).datagrid('getChecked');
	for ( var i = 0; i < selected.length; i++) {
		var index = $('#' + dgSelectedRecipientListId).datagrid('getRowIndex', selected[i].recipientNumber);
		$('#' + dgSelectedRecipientListId).datagrid('deleteRow', index);
	}
}

function sendSMSs() {
	var msg = document.getElementById("txtMsg").value;
	var hrs = document.getElementById("txbvalidityPeriod").value;
	var descr = document.getElementById("txtDescr").value;
	var project = $('#projects').combobox('getValue');
	if (isNaN(hrs) || hrs > 23 || hrs < 0) {
		alert('Please enter validity period between 0 to 23');
		return;
	}
	if(trim(project) == ''){
		alert('Project should be specified');
		return;
	}
	
	if (trim(msg) != '') {
		var maplist = $('#' + dgSelectedRecipientListId).datagrid('getRows');
		var recipArr = [];
		for ( var i = 0; i < maplist.length; i++) {
			recipArr.push(maplist[i]['recipientNumber']);
		}
		
		if (maplist.length != 0) {
			
		var queryParams = new Object();
		queryParams['<%=CommunicationQueryParams.REFERRED_NUMBER%>'] = recipArr;
		queryParams['<%=CommunicationQueryParams.PROJECT%>'] = project; 
		queryParams['<%=OutboundQueryParams.ADD_NOTE%>'] = descr; 
		queryParams['<%=OutboundQueryParams.TEXT%>'] = msg; 
		queryParams['<%=OutboundQueryParams.VALIDITY%>'] = hrs; 
		queryParams['<%=OutboundQueryParams.VALIDITY_TYPE%>'] = '<%=PeriodType.HOUR%>'; 
		
		try {
			$.getJSON('/smstarseelweb/communication/queue_sms.dm', queryParams,
				function(response) {
					alert(response['message']);
					if(response['message'].toLowerCase().indexOf('error') == -1){
						document.getElementById("txtMsg").value = "";
						document.getElementById("txtDescr").value = "";
						$('#projects').combobox('setValue',"");
						var rowsRecpln = $('#' + dgSelectedRecipientListId).datagrid('getRows').length;
						for ( var i = 0; i < rowsRecpln; i++) {
							$('#' + dgSelectedRecipientListId).datagrid('deleteRow', 0);//After deleting row below moves up hence 0 would always be the index to delete until we have iterated full datagrid length found above
						}
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
		} 
		else {
			alert('No recipients specified');
		}
	} else {
		document.getElementById("txtMsg").value = "";
		alert('Please enter an appropriate message text');
	}
}
//-->
</script>

