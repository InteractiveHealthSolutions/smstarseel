<%@page import="org.irdresearch.smstarseel.web.util.WebGlobals.SettingQueryParams"%>
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
<div id="dlgEditSet" >
<div id="msgDiv"></div>
<div align="center" class="divCenter" style="width: auto; margin: 10px;padding: 5px;">
<table style="width: 100%">
	<tr><td colspan="2">
	<input type="hidden" id="settingRegex" name="settingRegex" value=""/>
	<span class="heading">Setting : <input id="settingBeingEdited" type="button" style="background: transparent;border: none"> </span></td></tr>
	<tr><td>Old value:</td><td><textarea id="oldValue" name="oldValue" rows="3" readonly="readonly" style="width: 200px; resize: none"></textarea></td></tr>
	<tr><td>New Value: </td><td><textarea id="newValue" name="newValue" rows="3" style="width: 200px; resize: none" maxlength="255"/></textarea></td></tr>
</table>
</div>
</div>  

<script>
function showMsg(msg){
	document.getElementById("msgDiv").innerHTML="<p><span style=\"color:green\">"+msg+"</span></p>";
}

$('#dlgEditSet').dialog({ 
	closed: true,  
    cache: false,  
    modal: true 
});
function editSetting(settingName, settingRegex, oldValue) {
	$('#dlgEditSet').dialog({  
	    title: 'Edit Setting (only for admin)',  
	    width: 400,  
	    height: 320,  
	    closed: false,  
	    cache: false,  
	    modal: true ,
	    onBeforeOpen : function () {
	    	document.getElementById('settingBeingEdited').value = settingName;
	    	document.getElementById('settingRegex').value = settingRegex;
	    	document.getElementById('oldValue').value = oldValue;
		},
		onClose: function() {
			showMsg('');
		},
		buttons:[{
			text:'OK',
			iconCls:'icon-ok',
			handler:function(){
				var settingBeingEdited=document.getElementById("settingBeingEdited").value;
				var settingRegex=document.getElementById("settingRegex").value;
				var oldValue=document.getElementById("oldValue").value;
				var newValue=document.getElementById("newValue").value;

				var stRegx = new RegExp(settingRegex+"$");
				
				alert(stRegx);
				
				if(stRegx.test(newValue) == false){
					showMsg('Invalid value specified. Check if value conform to pattern allowed');
					return;
				}
				
				if(confirm("Are you sure you want to edit value for setting '"+settingBeingEdited+"' ?")){
					var queryParams = new Object();
					queryParams['<%=SettingQueryParams.NAME%>'] = settingBeingEdited; 
					queryParams['<%=SettingQueryParams.NEW_VALUE%>'] = newValue; 
					
					try {
						$.getJSON('/smstarseelweb/admin/edit_setting.dm', queryParams,
							function(response) {
								//document.getElementById("authpwd").value = '';
								document.getElementById("settingBeingEdited").value = '';
								document.getElementById("settingRegex").value = '';
								document.getElementById("oldValue").value='';
								document.getElementById("newValue").value='';
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
		title:'Settings',
		iconCls:'icon-reminder',
		width:750,
		height:550,
		nowrap: false,
		autoRowHeight: false,
		loader: loadthedata,
		selectOnCheck: false,
		singleSelect: true,
		idField:'settingId', 
		/*frozenColumns:[[
               {title:'Reference Number',field:'referenceNumber',width:130,sortable:true}
		]],*/
		columns:[[  
		    {field:'settingId',title:'', hidden:true}, 
		    {title:'--',field:'abc', width:40, 
		    	formatter: function(value,row,index){
		    		if(row.isEditable){
			    		return '<a id="anc_'+row.name+'" onclick="editSetting(\''+row.name+'\', \''+row.validatorRegex+'\', \''+row.value+'\')">edit</a>';
		    		}
		    		
		    		return "";
				}
		    },
		    {field:'name',title:'ID',width:200}, 
		    {field:'displayName',title:'Name',width:200},
		    {field:'value',title:'Value',width:220, 
		    	formatter: function(value,row,index){
					return '<textarea readonly="readonly" style="width: 200px;resize: none; border:0" rows="3">'+value+'</textarea>';
				}
			},
		    {field:'description',title:'Description',width:300}, 
		    {field:'validatorRegex',title:'Regex',width:250}, 
		    {field:'lastEditedByUserId',title:'Last Editor',width:100}, 
		    {field:'lastUpdated',title:'Last Edit Date',width:135, 
		    	formatter: function(value,row,index){
					if (value != null && value != ''){return new Date(value).toString('dd-MMM-yyyy HH:mm:ss');} 
					else {return value;}
				}
		    }
		]]  , 
		rownumbers:true,
	});
});

function loadthedata(){
	try {
		$.getJSON('/smstarseelweb/admin/traverse_settings.do', 
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