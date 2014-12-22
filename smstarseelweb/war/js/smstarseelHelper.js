function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

String.prototype.startsWith = function(str) {
	return (this.match("^"+str)==str);
};

function datediff(datesmaller,dategreater,interval) {
    var second=1000, minute=second*60, hour=minute*60, day=hour*24, week=day*7;
    var date1=datesmaller;
    var date2=dategreater;
    var timediff = date2 - date1;
    if (isNaN(timediff)) return NaN;
    switch (interval) {
        case "years": return date2.getFullYear() - date1.getFullYear();
        case "months": return (
            ( date2.getFullYear() * 12 + date2.getMonth() )
            -
            ( date1.getFullYear() * 12 + date1.getMonth() )
        );
        case "weeks"  : return Math.floor(timediff / week);
        case "days"   : return Math.floor(timediff / day); 
        case "hours"  : return Math.floor(timediff / hour); 
        case "minutes": return Math.floor(timediff / minute);
        case "seconds": return Math.floor(timediff / second);
        default: return undefined;
    }
}

function isNumber (o) {
	return ! isNaN (o-0);
}

function makeDateFromString(dategiven) {
    var str1 = dategiven;
    var dt1  = parseInt(str1.substring(0,2),10);
    var mon1 = parseInt(str1.substring(3,5),10)-1;
    var yr1  = parseInt(str1.substring(6,10),10);
    
    var date1 = new Date(yr1, mon1, dt1);
    return date1;
}
function trim(s)
{
	if(s.length==0) return '';
	var l=0; var r=s.length -1;
	while(l < s.length && s[l] == ' ')
	{	l++; }
	while(r > l && s[r] == ' ')
	{	r-=1;	}
	return s.substring(l, r+1);
}

function makeTextSelectedInDD(selectControl,textToSelect){
	var sel = selectControl;
    var val = textToSelect;
	for (var i=0; i<sel.options.length; i++) {
		if (sel.options[i].text == val) {
			sel.selectedIndex = i;
		}
	}
}
function makeValueSelectedInDD(selectControl,valueToSelect){
	var sel = selectControl;
    var val = valueToSelect;
	for (var i=0; i<sel.options.length; i++) {
		if (sel.options[i].value == val) {
			sel.selectedIndex = i;
		}
	}
}

function getValueSelectedInChoiceGroup(choiceControls){
     for(var k=0 ; k < choiceControls.length ; k++){
       if(choiceControls[k].checked){
         return choiceControls[k].value;
       }
     }
	return "";
}

function verifyTimeValues() {
	var inputs = document.getElementsByTagName('input');
	for (var i=0; i < inputs.length; i++)
	{
	   if (inputs[i].getAttribute('type') == 'text')
	   {
		   var reg = /^time([0-9_\-\.])+/;
		   if(reg.test(inputs[i].name)==true){
			   var reg2 = /^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]/;
				if(reg2.test(inputs[i].value)==false){
					alert(inputs[i].name+" is not a valid time value");
					return false;
				}
		   }
	   }
	}
	return true;
}

function exportCSV(JSONData, exclude) {
	var data = JSONData.rows;
	
	for(var i=0;i<data.length;i++){
		// alert(JSON.stringify(data[i]));
		var alterable;
		if((alterable = data[i]['systemRecieveDate'])) {
			data[i]['systemRecieveDate'] = new Date(parseInt(data[i]['systemRecieveDate'])).toString('dd-MMM-yyyy HH:mm:ss');
			
		}
		if((alterable = data[i]['recieveDate'])){
			data[i]['recieveDate'] = new Date(parseInt(data[i]['recieveDate'])).toString('dd-MMM-yyyy HH:mm:ss');
			
		}
		if((alterable = data[i]['dueDate'])){
			data[i]['dueDate'] = new Date(parseInt(data[i]['dueDate'])).toString('dd-MMM-yyyy HH:mm:ss');
			
		}
        
		for(var e in exclude) {
			var a = exclude[e];
			
	        delete data[i][a];
		}
	}
	
		
    	JSONToCSVConvertor(data, "data "+new Date().toString('dd-MMM-yyyy HH:mm:ss'), true);
	
		
    
}



function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    // alert(arrData);
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
