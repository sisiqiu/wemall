<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>满减送活动管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var itemArr = [];
		var currentArr = [];
		var checkedTr = {};
		$(document).ready(function() {
			var actIds = "${actIds}";
			initCheckBox(actIds);
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					var str = "";
					for(var i=0; i<itemArr.length; i++) {
					    var itemId = itemArr[i];
					    if(itemId) {
						    str += itemId+",";
					    }
					}
					$("#itemIds").val(str);
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a id="firstDis" href="${ctx}/wemall/wemallFullDiscount/">满减送活动列表</a></li>
		<li class="active"><a href="${ctx}/wemall/wemallFullDiscount/form?id=${wemallFullDiscount.id}">满减送活动<shiro:hasPermission name="wemall:wemallFullDiscount:edit">${not empty wemallFullDiscount.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wemall:wemallFullDiscount:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="wemallFullDiscount" action="${ctx}/wemall/wemallFullDiscount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">活动名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="6" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${wemallFullDiscount.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${wemallFullDiscount.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">标签名：</label>
			<div class="controls">
				<form:input path="label" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">满减表达式：</label>
			<div class="controls">
				<form:textarea path="discountCond" htmlEscape="false" rows="4" maxlength="1000" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> 表达式中用变量orderPrice表示订单原价</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="wemall:wemallFullDiscount:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<input type="hidden" name="itemIds" id="itemIds" value="${actIds}">
	</form:form>
	<ul class="nav nav-tabs discountTab">
		<li class="active"><a onclick="showDiv(1)">第一步：选择商品</a></li>
		<li><a onclick="showDiv(2)">第二步：确认商品</a></li>
	</ul>
	<div id="firstDiv" class="tabsDiv">
	<div>	
		<form:form id="firstQueryForm" modelAttribute="wemallItem" class="breadcrumb form-search">			
			<li><label>商品名称：</label>
				<input id="itemName" name="name" class="input-medium" type="text" value="" maxlength="100">
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" onclick="page()" type="button" value="查询"/>
				<input id="btn_reset" class="btn btn-primary" onclick="document.getElementById('firstQueryForm').reset();page()" type="button" value="重置"/>
			</li>
			<li class="clearfix"></li>
		</form:form>
	</div>
	<table id="firstTable" class="table table-striped table-bordered table-condensed">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<thead>
			<tr>
				<th><input type="checkbox" class="all" onchange="allChange()"></th>
				<th>商品名称</th>
				<th>商品类别id</th>
				<th>商品原价</th>
				<th>商品现价</th>
				<th>商品库存</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallItem">
			<tr>
			<c:set var="selectAllIds" value="${actIds}"/>
			<c:set var="wemallItemId" value=",${wemallItem.id},"/>
			<c:choose>
				<c:when test="${fn:contains(selectAllIds, wemallItemId)}">  
				  	<td class="checkboxTd">
				    	<input name="itemId" class="allChild" onchange="allChildChange(this)" type="checkbox" checked value="${wemallItem.id}">
				 	 </td>
				</c:when>
		   		<c:otherwise>
				   	<td class="checkboxTd">
				   		<input name="itemId" class="allChild" onchange="allChildChange(this)" type="checkbox" value="${wemallItem.id}">
				   	</td>
			   </c:otherwise>
			</c:choose>
				<td itemId="${wemallItem.id}">
					<img  src="${wemallItem.photo}" width="40px" height="40px">${wemallItem.name}
				</td>
				<td>
					${wemallItem.sortId}
				</td>
				<td>
					${wemallItem.originalPrice}
				</td>
				<td>
					${wemallItem.currentPrice}
				</td>
				<td>
					${wemallItem.storage}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	</div>
	<div id = "secondDiv" style="display: none" class="tabsDiv">
	<table id="secondTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>商品名称</th>
				<th>商品类别id</th>
				<th>商品原价</th>
				<th>商品现价</th>
				<th>商品库存</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	</div>
</body>
</html>