<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>属性值信息管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallSpecInfo/">属性值信息列表</a></li>
		<shiro:hasPermission name="wemall:wemallSpecInfo:edit"><li><a href="${ctx}/wemall/wemallSpecInfo/form">属性值信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallSpecInfo" action="${ctx}/wemall/wemallSpecInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>属性值id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>属性类别id：</label>
				<form:input path="specId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>属性名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="40" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>属性值id</th>
				<th>属性类别id</th>
				<th>属性名称</th>
				<th>排序</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallSpecInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallSpecInfo">
			<tr>
				<td><a href="${ctx}/wemall/wemallSpecInfo/form?id=${wemallSpecInfo.id}">
					${wemallSpecInfo.id}
				</a></td>
				<td>
					${wemallSpecInfo.specId}
				</td>
				<td>
					${wemallSpecInfo.name}
				</td>
				<td>
					${wemallSpecInfo.sort}
				</td>
				<td>
					<fmt:formatDate value="${wemallSpecInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallSpecInfo:edit"><td>
    				<a href="${ctx}/wemall/wemallSpecInfo/form?id=${wemallSpecInfo.id}">修改</a>
					<a href="${ctx}/wemall/wemallSpecInfo/delete?id=${wemallSpecInfo.id}" onclick="return confirmx('确认要删除该属性值信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>