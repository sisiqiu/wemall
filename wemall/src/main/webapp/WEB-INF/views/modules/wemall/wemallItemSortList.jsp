<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品分类管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallItemSort/">商品分类列表</a></li>
		<shiro:hasPermission name="wemall:wemallItemSort:edit"><li><a href="${ctx}/wemall/wemallItemSort/form">商品分类添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallItemSort" action="${ctx}/wemall/wemallItemSort/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>商品类别id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>商品类别名：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>父级编号：</label>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>商品类别id</th>
				<th>商品类别名</th>
				<th>排序</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallItemSort:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallItemSort">
			<tr>
				<td><a href="${ctx}/wemall/wemallItemSort/form?id=${wemallItemSort.id}">
					${wemallItemSort.id}
				</a></td>
				<td>
					${wemallItemSort.name}
				</td>
				<td>
					${wemallItemSort.sort}
				</td>
				<td>
					<fmt:formatDate value="${wemallItemSort.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${wemallItemSort.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallItemSort:edit"><td>
    				<a href="${ctx}/wemall/wemallItemSort/form?id=${wemallItemSort.id}">修改</a>
					<a href="${ctx}/wemall/wemallItemSort/delete?id=${wemallItemSort.id}" onclick="return confirmx('确认要删除该商品分类吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>