<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Basic DateBox - jQuery EasyUI Demo</title>
	
<%@include file="/template/include.jsp"%>

</head>
<body>
	<h2>Basic DateBox</h2>
	<div class="demo-info">
		<div class="demo-tip icon-tip"></div>
		<div>Click the calendar image on the right side.</div>
	</div>
	<div style="margin:10px 0;"></div>
	
	Due Date <input id="dateDuetxt" class="easyui-datebox" style="width:100px"/>  
    
    <script>

    $('#dateDuetxt').datebox({
    	value: (new Date().toString('dd-MMM-yyyy')), /* Date.js toString function to convert date into format that is being used by datebox. */
    	formatter : function(date){
        	return date.toString('dd-MMM-yyyy');
        },
        parser : function(s){
        	var t = Date.parse(s);
        	if (!isNaN(t)){
        		return new Date(t);
        	} else {
        		return null;
        	}
        }
    });
        
    </script>
</body>
</html>