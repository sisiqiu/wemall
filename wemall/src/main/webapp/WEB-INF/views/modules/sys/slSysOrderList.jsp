<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//重置选中
			$("#btn_reset").click(function(){
				 location.replace(location.href);
			});
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
		<li class="active"><a href="${ctx}/sys/slSysOrder/">订单列表</a></li>
		<shiro:hasPermission name="sys:slSysOrder:edit"><li><a href="${ctx}/sys/slSysOrder/form">订单添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="slSysOrder" action="${ctx}/sys/slSysOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订单号：</label>
				<form:input path="orderNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户ID：</label>
				<sys:treeselect id="user" name="user.id" value="${slSysOrder.user.id}" labelName="user.name" labelValue="${slSysOrder.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>商品名称：</label>
				<form:input path="subject" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>订单类别：</label>
				<form:select path="orderType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>订单状态：</label>
				<form:radiobuttons path="status" items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>支付状态：</label>
				<form:select path="payState" class="input-xlarge">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('pay_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>支付方式：</label>
				<form:radiobuttons path="payMethod" items="${fns:getDictList('pay_method')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="btns">
				<input id="btn_reset" class="btn btn-primary" type="button" value="重置"/>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>订单号</th>
				<th>用户ID</th>
				<th>商品名称</th>
				<th>订单类别</th>
				<th>订单价格</th>
				<th>实际支付价格</th>
				<th>支付方式</th>
				<th>运费</th>
				<th>订单状态</th>
				<th>支付状态</th>
				<th>下单日期</th>
				<th>手机号</th>
				<th>更新时间</th>
				<shiro:hasPermission name="sys:slSysOrder:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="slSysOrder">
			<tr>
				<td><a href="${ctx}/sys/slSysOrder/form?id=${slSysOrder.orderNo}">
					${slSysOrder.orderNo}
				</a></td>
				<td>
					${slSysOrder.user.name}
				</td>
				<td>
					${slSysOrder.subject}
				</td>
				<td>
					${fns:getDictLabel(slSysOrder.orderType, 'order_type', '')}
				</td>
				<td>
					${slSysOrder.orderPrice}
				</td>
				<td>
					${slSysOrder.actualPayment}
				</td>
				<td>
					${fns:getDictLabel(slSysOrder.payMethod, 'pay_method', '')}
				</td>
				<td>
					${slSysOrder.freightFee}
				</td>
				<td>
					${fns:getDictLabel(slSysOrder.status, 'order_status', '')}
				</td>
				<td>
					${fns:getDictLabel(slSysOrder.payState, 'pay_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${slSysOrder.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${slSysOrder.mobile}
				</td>
				<td>
					<fmt:formatDate value="${slSysOrder.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="sys:slSysOrder:edit"><td>
    				<a href="${ctx}/sys/slSysOrder/form?id=${slSysOrder.orderNo}">修改</a>
					<a href="${ctx}/sys/slSysOrder/delete?id=${slSysOrder.orderNo}" onclick="return confirmx('确认要删除该订单吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>