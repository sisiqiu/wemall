<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>退款信息管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallRefund/">退款信息列表</a></li>
		<shiro:hasPermission name="wemall:wemallRefund:edit"><li><a href="${ctx}/wemall/wemallRefund/form">退款信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallRefund" action="${ctx}/wemall/wemallRefund/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>退款业务号：</label>
				<form:input path="refundId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>订单号：</label>
				<form:input path="orderNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<sys:treeselect id="user" name="user.id" value="${wemallRefund.user.id}" labelName="user.name" labelValue="${wemallRefund.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>订单金额：</label>
				<form:input path="orderPrice" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>实付金额：</label>
				<form:input path="payment" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>退款金额：</label>
				<form:input path="refundFee" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>退款状态：</label>
				<form:select path="refundStatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('refund_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>退款时间：</label>
				<input name="beginRefundDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallRefund.beginRefundDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endRefundDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallRefund.endRefundDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>退款业务号</th>
				<th>订单号</th>
				<th>用户id</th>
				<th>订单金额</th>
				<th>实付金额</th>
				<th>退款金额</th>
				<th>退款状态</th>
				<th>退款时间</th>
				<shiro:hasPermission name="wemall:wemallRefund:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallRefund">
			<tr>
				<td><a href="${ctx}/wemall/wemallRefund/form?refundId=${wemallRefund.refundId}">
					${wemallRefund.refundId}
				</a></td>
				<td>
					${wemallRefund.orderNo}
				</td>
				<td>
					${wemallRefund.user.name}
				</td>
				<td>
					${wemallRefund.orderPrice}
				</td>
				<td>
					${wemallRefund.payment}
				</td>
				<td>
					${wemallRefund.refundFee}
				</td>
				<td>
					${fns:getDictLabel(wemallRefund.refundStatus, 'refund_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${wemallRefund.refundDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallRefund:edit"><td>
    				<a href="${ctx}/wemall/wemallRefund/form?refundId=${wemallRefund.refundId}">修改</a>
					<a href="${ctx}/wemall/wemallRefund/delete?refundId=${wemallRefund.refundId}" onclick="return confirmx('确认要删除该退款信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>