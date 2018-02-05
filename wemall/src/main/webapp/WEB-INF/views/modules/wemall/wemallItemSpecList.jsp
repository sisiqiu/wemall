<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品-属性信息管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallItemSpec/">商品-属性信息列表</a></li>
		<shiro:hasPermission name="wemall:wemallItemSpec:edit"><li><a href="${ctx}/wemall/wemallItemSpec/form">商品-属性信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallItemSpec" action="${ctx}/wemall/wemallItemSpec/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>商品id：</label>
				<form:input path="itemId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>属性类别名：</label>
				<form:input path="specName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>属性值名称：</label>
				<form:input path="specInfoName" htmlEscape="false" maxlength="40" class="input-medium"/>
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
				<th>商品id</th>
				<th>属性类别名</th>
				<th>属性值名称</th>
				<th>排序</th>
				<th>价格</th>
				<th>拼团价</th>
				<th>库存量</th>
				<shiro:hasPermission name="wemall:wemallItemSpec:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallItemSpec">
			<tr>
				<td><a href="${ctx}/wemall/wemallItemSpec/form?id=${wemallItemSpec.id}">
					${wemallItemSpec.id}
				</a></td>
				<td>
					${wemallItemSpec.itemId}
				</td>
				<td>
					${wemallItemSpec.specName}
				</td>
				<td>
					${wemallItemSpec.specInfoName}
				</td>
				<td>
					${wemallItemSpec.sort}
				</td>
				<td>
					${wemallItemSpec.price}
				</td>
				<td>
					${wemallItemSpec.teamPrice}
				</td>
				<td>
					${wemallItemSpec.storage}
				</td>
				<shiro:hasPermission name="wemall:wemallItemSpec:edit"><td>
    				<a href="${ctx}/wemall/wemallItemSpec/form?id=${wemallItemSpec.id}">修改</a>
					<a href="${ctx}/wemall/wemallItemSpec/delete?id=${wemallItemSpec.id}" onclick="return confirmx('确认要删除该商品-属性信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>