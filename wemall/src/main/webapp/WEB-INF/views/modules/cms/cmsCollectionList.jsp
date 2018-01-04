<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人收藏管理管理</title>
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
		<li class="active"><a href="${ctx}/cms/cmsCollection/">个人收藏管理列表</a></li>
		<shiro:hasPermission name="cms:cmsCollection:edit"><li><a href="${ctx}/cms/cmsCollection/form">个人收藏管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsCollection" action="${ctx}/cms/cmsCollection/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<sys:treeselect id="user" name="user.id" value="${cmsCollection.user.id}" labelName="user.name" labelValue="${cmsCollection.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
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
				<th>类型</th>
				<th>细分类别</th>
				<th>对应模块</th>
				<th>模块数据id</th>
				<th>状态值</th>
				<th>更新时间</th>
				<shiro:hasPermission name="cms:cmsCollection:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsCollection">
			<tr>
				<td><a href="${ctx}/cms/cmsCollection/form?id=${cmsCollection.id}">
					${cmsCollection.id}
				</a></td>
				<td>
					${cmsCollection.user.name}
				</td>
				<td>
					${cmsCollection.type}
				</td>
				<td>
					${cmsCollection.category}
				</td>
				<td>
					${cmsCollection.module}
				</td>
				<td>
					${cmsCollection.msgid}
				</td>
				<td>
					${cmsCollection.status}
				</td>
				<td>
					<fmt:formatDate value="${cmsCollection.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="cms:cmsCollection:edit"><td>
    				<a href="${ctx}/cms/cmsCollection/form?id=${cmsCollection.id}">修改</a>
					<a href="${ctx}/cms/cmsCollection/delete?id=${cmsCollection.id}" onclick="return confirmx('确认要删除该个人收藏管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>