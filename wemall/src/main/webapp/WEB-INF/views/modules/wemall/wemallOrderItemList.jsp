<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单-商品信息管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallOrderItem/">订单-商品信息列表</a></li>
		<shiro:hasPermission name="wemall:wemallOrderItem:edit"><li><a href="${ctx}/wemall/wemallOrderItem/form">订单-商品信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallOrderItem" action="${ctx}/wemall/wemallOrderItem/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>商品id：</label>
				<form:input path="itemId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>商品数量：</label>
				<form:input path="itemNum" htmlEscape="false" maxlength="6" class="input-medium"/>
			</li>
			<li><label>商品标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>商品总金额：</label>
				<form:input path="totalFee" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>物流名称：</label>
				<form:input path="freightName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>物流单号：</label>
				<form:input path="freightNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>买家已评价：</label>
				<form:radiobuttons path="buyerComment" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>商品id</th>
				<th>商品数量</th>
				<th>商品标题</th> 
				<th>商品总金额</th>
				<th>状态</th>
				<th>物流名称</th>
				<th>物流单号</th>
				<th>买家昵称</th>
				<th>买家评分</th>
				<th>买家是否评价</th>
				<shiro:hasPermission name="wemall:wemallOrderItem:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallOrderItem">
			<tr>
				<td><a href="${ctx}/wemall/wemallOrderItem/form?id=${wemallOrderItem.id}">
					${wemallOrderItem.itemId}
				</a></td>
				<td>
					${wemallOrderItem.itemNum}
				</td>
				<td>
					${wemallOrderItem.title}
				</td>
				<td>
					${wemallOrderItem.totalFee}
				</td>
				<td>
					${fns:getDictLabel(wemallOrderItem.status, 'order_status', '')}
				</td>
				<td>
					${wemallOrderItem.freightName}
				</td>
				<td>
					${wemallOrderItem.freightNo}
				</td>
				<td>
					${wemallOrderItem.buyerNick}
				</td>
				<td>
					${wemallOrderItem.buyerScore}
				</td>
				<td>
					${fns:getDictLabel(wemallOrderItem.buyerComment, 'yes_no', '')}
				</td>
				<shiro:hasPermission name="wemall:wemallOrderItem:edit"><td>
    				<a href="${ctx}/wemall/wemallOrderItem/form?id=${wemallOrderItem.id}">修改</a>
					<a href="${ctx}/wemall/wemallOrderItem/delete?id=${wemallOrderItem.id}" onclick="return confirmx('确认要删除该订单-商品信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>