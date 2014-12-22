<%@include file="/template/header.jsp"%>
<table>
  <tr>
    <td>
<div id="pnSummarytbls" class="easyui-panel" title="Summary (${summaryDateRange})" 
style="width:500px;height:540px;padding:0px;background: transparent;display: inline-block;text-align: center;" >
<script type="text/javascript">
<!--
	// The function MUST be called like this , else it appends two scrollbars to container
	(function($){
        $(window).load(function(){
        	$("#pnSummarytbls").mCustomScrollbar({});
			//$("#systemSummaryDiv").mCustomScrollbar({horizontalScroll: true});
        });
    })(jQuery);
	/* $(window).load(function() {
		(function($) {
			$("#pnSummarytbls").mCustomScrollbar({horizontalScroll: true});
			$("#systemSummaryDiv").mCustomScrollbar({});
		})(jQuery);
	}); */
//-->
</script>
<table class="homesummary" style="width: 100%;">
	<tr>
		<td colspan="7" ><span class="tableheadingmain">Outbounds (Outgoing sms)</span></td>
	</tr>
	<c:forEach items="${outboundSummary}" var="obsr" varStatus="i">
		<tr>
		<c:forEach items="${obsr}" var="obsc" >
			<c:choose>
			<c:when test="${i.index==0}">
				<th>${obsc}</th>
			</c:when>
			<c:otherwise>
				<td>${obsc}</td>
			</c:otherwise>
			</c:choose>
		</c:forEach>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="7">.<br>.<br>.</td>
	</tr>
</table>
<br>
<table class="homesummary" style="width: 225px;min-height: 200px">
	<tr>
		<td colspan="14" ><span class="tableheadingmain">Inbounds (Incoming sms)</span></td>
	</tr>
	<c:forEach items="${inboundSummary}" var="ibsr" varStatus="i">
		<tr>
		<c:forEach items="${ibsr}" var="ibsc" >
			<c:choose>
			<c:when test="${i.index==0}">
				<th>${ibsc}</th>
			</c:when>
			<c:otherwise>
				<td>${ibsc}</td>
			</c:otherwise>
			</c:choose>
		</c:forEach>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="14">.<br>.<br>.</td>
	</tr>
</table>

<table class="homesummary" style="width: 225px;min-height: 200px">
	<tr>
		<td colspan="14" ><span class="tableheadingmain">Calls</span></td>
	</tr>
	<c:forEach items="${calllogSummary}" var="clsr" varStatus="i">
		<tr>
		<c:forEach items="${clsr}" var="clsc" >
			<c:choose>
			<c:when test="${i.index==0}">
				<th>${clsc}</th>
			</c:when>
			<c:otherwise>
				<td>${clsc}</td>
			</c:otherwise>
			</c:choose>
		</c:forEach>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="14">.<br>.<br>.</td>
	</tr>
</table>
</div>

</td>
<td>

<div id="systemSummaryDiv" class="homesummarynarrow"  style="width: 270px;" >
${projects}<br><br>
	<table class="homesummarynarrowtable" style="width: 100%">
	<thead>
	<tr>
		<th colspan="14" ><span class="tableheadingmain">Active Devices</span></th>
	</tr>
	</thead>
		<tr>
			<th>Imei</th><th>Project (Sim)</th></tr>
		
	<c:forEach items="${devices}" var="dev">
		<tr>
			<td>${dev.imei}</td><td>${dev.project.name}<br>(${dev.sim})</td>
		</tr>
	</c:forEach>
	</table>
	
	<table class="homesummarynarrowtable" style="width: 100%;">
	<thead>
	<tr>
		<th colspan="14" ><span class="tableheadingmain">Settings</span></th>
	</tr>
	</thead>
		<tr><th>Name = Value</th></tr>
	<c:forEach items="${settings}" var="sett">
	<c:forEach items="${sett}" var="s">
		<tr>
			<td>-- <span style="font-style: italic;">${s.key}</span> = <span style="word-wrap: break-word;">${s.value}</span></td>
		</tr>
	</c:forEach>
	</c:forEach>
	</table>
</div>

</td>
  </tr>
</table>
<%@include file="/template/footer.jsp"%>