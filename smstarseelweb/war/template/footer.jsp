</div>
<!-- end content -->
<style><!--
.datagrid-cell {
  font-size: 10px;
}
#tbQuickSummary .panel-title {
  font-size: 12px;
  padding-left: 15px;
}
--></style>
<div id="sidebar" >
<div class="sidebarlistwrapper">
<h3>Tasks</h3>
<jsp:include page="/template/sidebar_${navigationType}.jsp" />
</div>
<h3>Quick Summary</h3>
<div id="pgQuickSummaryDiv" style="width:225px;height:310px;" >
	<table id="pgQuickSummary" class="easyui-propertygrid" style="width:220px;height:800px;background: transparent;overflow: visible;"></table>  
</div>
	<script type="text/javascript">
	<!--
	$('#pgQuickSummary').propertygrid({  
	    url: '${pageContext.request.contextPath}/genericdata/quick_summary.do',  
	    showGroup: true,  
	    scrollbarSize: 0,
	    striped: false,
	    nowrap: false,
	    showHeader: false,
	}); 
	
	(function($){
        $(window).load(function(){
            $("#pgQuickSummaryDiv").mCustomScrollbar();
        });
    })(jQuery);
	</script>

</div>

<div style="clear: both;"> </div>

</div>
<!-- end contentwrap -->

<!-- <div id="bottom"> </div> -->
<div id="footer"><p>Copyright &copy; IRD <a href="http://www.irdresearch.org">(Interactive Research and Development)</a></p></div>

</div>
<!-- end wrap -->
</body>
</html>
