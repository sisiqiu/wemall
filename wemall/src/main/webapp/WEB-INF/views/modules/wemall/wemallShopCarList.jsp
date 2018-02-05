<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>购物车信息管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallShopCar/">购物车信息列表</a></li>
		<shiro:hasPermission name="wemall:wemallShopCar:edit"><li><a href="${ctx}/wemall/wemallShopCar/form">购物车信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallShopCar" action="${ctx}/wemall/wemallShopCar/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<sys:treeselect id="user" name="user.id" value="${wemallShopCar.user.id}" labelName="user.name" labelValue="${wemallShopCar.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>商品id：</label>
				<form:input path="itemId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>商品名称：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>商品数量：</label>
				<form:input path="itemNum" htmlEscape="false" maxlength="6" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:radiobuttons path="status" items="${fns:getDictList('usable')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键id</th>
				<th>用户id</th>
				<th>商品id</th>
				<th>商品名称</th>
				<th>商品数量</th>
				<th>状态</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallShopCar:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallShopCar">
			<tr>
				<td><a href="${ctx}/wemall/wemallShopCar/form?id=${wemallShopCar.id}">
					${wemallShopCar.id}
				</a></td>
				<td>
					${wemallShopCar.user.name}
				</td>
				<td>
					${wemallShopCar.itemId}
				</td>
				<td>
					${wemallShopCar.itemName}
				</td>
				<td>
					${wemallShopCar.itemNum}
				</td>
				<td>
					${fns:getDictLabel(wemallShopCar.status, 'usable', '')}
				</td>
				<td>
					<fmt:formatDate value="${wemallShopCar.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallShopCar:edit"><td>
    				<a href="${ctx}/wemall/wemallShopCar/form?id=${wemallShopCar.id}">修改</a>
					<a href="${ctx}/wemall/wemallShopCar/delete?id=${wemallShopCar.id}" onclick="return confirmx('确认要删除该购物车信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>