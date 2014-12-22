<%@page
	import="org.irdresearch.smstarseel.web.util.WebGlobals.QueryParams"%>
<%@page
	import="org.irdresearch.smstarseel.web.util.WebGlobals.OutboundQueryParams"%>
<%@page
	import="org.irdresearch.smstarseel.global.RequestParam.OuboundSmsParams"%>
<%@page
	import="org.irdresearch.smstarseel.web.util.WebGlobals.CommunicationQueryParams"%>
<%@page
	import="org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus"%>
<%@include file="/template/header.jsp"%>

<%-- <form ENCTYPE="multipart/form-data" id="formId" name="formId" method="post" >
	<div align="center">
		<table>
			<tr style="background-color: silver;">
				<td colspan="2">CSV Uploader</td>
			</tr>
			<tr>
				<td colspan="2"><span class="error" style="font-size: x-small; color: red">${message}</span></td>
			</tr>
			<tr>
				<td>Select:</td>
				<td><input type="file" name="file" /></td>
			</tr>
			<tr>
				<td colspan="2"> <input type="submit" value="Upload" ></td>
			</tr>
			
		</table>
	</div>
</form>
 --%>
<script type="text/javascript">
<!--
	$(document).ready(function() {
		var settings = {
			url : "${pageContext.request.contextPath}/communication/smsscheduler.htm",
			method : "POST",
			allowedTypes : "csv",
			fileName : "myfile",
			onSuccess:function(files,data,xhr)
			{
				try {
					var dat = JSON.parse(data);
				
				
					$('#tt').datagrid({
						frozenColumns:[[
					        {field:'recipient',title:'Recipient',width:100,align:'right'},
					        {field:'referencenumber',title:'Reference number',width:125,align:'right'}
				        ]],
				        columns:[[
							{field:'projectid',title:'Project id',width:50},
					        {field:'duedate',title:'Due date',width:150},
					        {field:'validityduration',title:'Validity duration',width:120,align:'right'},
					        {field:'durationtype',title:'Duration Type',width:110},
					        {field:'message',title:'Message',width:300},
					        {field:'priority',title:'Priority',width:100},
					        {field:'description',title:'Description',width:500},
				        ]]
					});
					$('#tt').datagrid('loadData', dat );
					$('#export_div').show();
					//document.getElementById('export_div').style.display = "block";
				} catch(e) {
				    alert(data);
				}
			} 
		};
		
		var uploadObj = $("#tarseel_csv_uploader").uploadFile(settings);
		$('#export_div').hide();
		
		
	   
	});
//-->
</script>
<script type="text/javascript">
    function exportCSV() {
    	//this will get all the data in table in JSON format
        var row=$('#tt').datagrid('getRows');
        // alert(JSON.stringify(row));
        if (row) {
        	JSONToCSVConvertor(row, "data "+new Date(), true);
        }
    }

    
    
    function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
        //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
        var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
        
        var CSV = '';    
        //Set Report title in first row or line
        
        CSV += ReportTitle + '\r\n\n';

        //This condition will generate the Label/Header
        if (ShowLabel) {
            var row = "";
            
            //This loop will extract the label from 1st index of on array
            for (var index in arrData[0]) {
                
                //Now convert each value to string and comma-seprated
                row += index + ',';
            }

            row = row.slice(0, -1);
            
            //append Label row with line break
            CSV += row + '\r\n';
        }
        
        //1st loop is to extract each row
        for (var i = 0; i < arrData.length; i++) {
            var row = "";
            
            //2nd loop will extract each column and convert it in string comma-seprated
            for (var index in arrData[i]) {
                row += '"' + arrData[i][index] + '",';
            }

            row.slice(0, row.length - 1);
            
            //add a line break after each row
            CSV += row + '\r\n';
        }

        if (CSV == '') {
            alert("Invalid data");
            return;
        }
        
        //Generate a file name
        
        //this will remove the blank-spaces from the title and replace it with an underscore
        var fileName = ReportTitle.replace(/ /g,"_");   
        
        //Initialize file format you want csv or xls
        var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);
        
        // Now the little tricky part.
        // you can use either>> window.open(uri);
        // but this will not work in some browsers
        // or you will not get the correct file extension    
        
        //this trick will generate a temp <a /> tag
        var link = document.createElement("a");    
        link.href = uri;
        
        //set the visibility hidden so it will not effect on your web-layout
        link.style = "visibility:hidden";
        link.download = fileName + ".csv";
        
        //this part will append the anchor tag and remove it after automatic click
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
 </script>

<div align="center">
	<div id="tarseel_csv_uploader">Upload</div>
	<div id="status"></div>
</div>

<div align="center" id="responseData" class="homesummarynarrowtable">
	<table id="tt" singleSelect="true" >
		
	</table>
</div>
<div id = "export_div" class="homesummarynarrowtable">
	<a onclick="exportCSV()" id ="btn_export"  class="easyui-linkbutton right" >Export</a>
</div>
<%@include file="/template/footer.jsp"%>
