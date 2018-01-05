<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>属性类别管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallSpec/">属性类别列表</a></li>
		<shiro:hasPermission name="wemall:wemallSpec:edit"><li><a href="${ctx}/wemall/wemallSpec/form">属性类别添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallSpec" action="${ctx}/wemall/wemallSpec/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>属性类别id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>属性类别名：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>属性类别id</th>
				<th>属性类别名</th>
				<th>排序</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallSpec:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallSpec">
			<tr>
				<td><a href="${ctx}/wemall/wemallSpec/form?id=${wemallSpec.id}">
					${wemallSpec.id}
				</a></td>
				<td>
					${wemallSpec.name}
				</td>
				<td>
					${wemallSpec.sort}
				</td>
				<td>
					<fmt:formatDate value="${wemallSpec.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallSpec:edit"><td>
    				<a href="${ctx}/wemall/wemallSpec/form?id=${wemallSpec.id}">修改</a>
					<a href="${ctx}/wemall/wemallSpec/delete?id=${wemallSpec.id}" onclick="return confirmx('确认要删除该属性类别吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>