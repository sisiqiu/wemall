<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/wemall/wemallOrder/">订单列表</a></li>
		<shiro:hasPermission name="wemall:wemallOrder:edit"><li><a href="${ctx}/wemall/wemallOrder/form">订单添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallOrder" action="${ctx}/wemall/wemallOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订单号：</label>
				<form:input path="orderNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>平台订单号：</label>
				<form:input path="platformOrderNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>订单金额：</label>
				<form:input path="orderPrice" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>实付金额：</label>
				<form:input path="payment" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>支付类型：</label>
				<form:select path="paymentType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('payment_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>总退款金额：</label>
				<form:input path="totalRefundFee" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>订单名称：</label>
				<form:input path="title" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>付款时间：</label>
				<input name="beginPaymentDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallOrder.beginPaymentDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endPaymentDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallOrder.endPaymentDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>类别：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>会员卡id：</label>
				<form:input path="vipCardId" htmlEscape="false" maxlength="6" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>订单号</th>
				<th>订单金额</th>
				<th>实付金额</th>
				<th>支付类型</th>
				<th>总退款金额</th>
				<th>订单名称</th>
				<th>状态</th>
				<th>付款时间</th>
				<th>类别</th>
				<shiro:hasPermission name="wemall:wemallOrder:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallOrder">
			<tr>
				<td><a href="${ctx}/wemall/wemallOrder/form?orderNo=${wemallOrder.orderNo}">
					${wemallOrder.orderNo}
				</a></td>
				<td>
					${wemallOrder.orderPrice}
				</td>
				<td>
					${wemallOrder.payment}
				</td>
				<td>
					${fns:getDictLabel(wemallOrder.paymentType, 'payment_type', '')}
				</td>
				<td>
					${wemallOrder.totalRefundFee}
				</td>
				<td>
					${wemallOrder.title}
				</td>
				<td>
					${fns:getDictLabel(wemallOrder.status, 'order_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${wemallOrder.paymentDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(wemallOrder.type, 'order_type', '')}
				</td>
				<shiro:hasPermission name="wemall:wemallOrder:edit"><td>
    				<a href="${ctx}/wemall/wemallOrder/form?orderNo=${wemallOrder.orderNo}">修改</a>
					<a href="${ctx}/wemall/wemallOrder/delete?orderNo=${wemallOrder.orderNo}" onclick="return confirmx('确认要删除该订单吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>