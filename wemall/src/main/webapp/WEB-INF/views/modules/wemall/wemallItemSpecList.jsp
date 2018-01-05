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
			<li><label>商品id：</label>
				<form:input path="itemId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>属性类别id：</label>
				<form:input path="specId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>属性值id：</label>
				<form:input path="specInfoId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>价格：</label>
				<form:input path="price" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>拼团价：</label>
				<form:input path="teamPrice" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>库存量：</label>
				<form:input path="storage" htmlEscape="false" maxlength="11" class="input-medium"/>
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
				<th>属性类别id</th>
				<th>属性值id</th>
				<th>价格</th>
				<th>拼团价</th>
				<th>库存量</th>
				<shiro:hasPermission name="wemall:wemallItemSpec:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallItemSpec">
			<tr>
				<td><a href="${ctx}/wemall/wemallItemSpec/form?itemId=${wemallItemSpec.itemId}&specId=${wemallItemSpec.specId}">
					${wemallItemSpec.itemId}
				</a></td>
				<td>
					${wemallItemSpec.specId}
				</td>
				<td>
					${wemallItemSpec.specInfoId}
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
    				<a href="${ctx}/wemall/wemallItemSpec/form?itemId=${wemallItemSpec.itemId}&specId=${wemallItemSpec.specId}">修改</a>
					<a href="${ctx}/wemall/wemallItemSpec/delete?itemId=${wemallItemSpec.itemId}&specId=${wemallItemSpec.specId}" onclick="return confirmx('确认要删除该商品-属性信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>