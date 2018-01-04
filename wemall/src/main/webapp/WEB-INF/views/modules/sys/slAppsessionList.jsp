<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会话管理</title>
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
		<li class="active"><a href="${ctx}/sys/slAppsession/">会话列表</a></li>
		<shiro:hasPermission name="sys:slAppsession:edit"><li><a href="${ctx}/sys/slAppsession/form">会话添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="slAppsession" action="${ctx}/sys/slAppsession/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>sessionID：</label>
				<form:input path="sid" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>用户ID：</label>
				<sys:treeselect id="user" name="user.id" value="${slAppsession.user.id}" labelName="user.name" labelValue="${slAppsession.user.name}"
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
				<th>sessionID</th>
				<th>用户ID</th>
				<th>密钥过期时间</th>
				<th>修改时间</th>
				<shiro:hasPermission name="sys:slAppsession:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="slAppsession">
			<tr>
				<td><a href="${ctx}/sys/slAppsession/form?id=${slAppsession.id}">
					${slAppsession.sid}
				</a></td>
				<td>
					${slAppsession.user.name}
				</td>
				<td>
					${slAppsession.accessTokenExpiry}
				</td>
				<td>
					<fmt:formatDate value="${slAppsession.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="sys:slAppsession:edit"><td>
    				<a href="${ctx}/sys/slAppsession/form?id=${slAppsession.id}">修改</a>
					<a href="${ctx}/sys/slAppsession/delete?id=${slAppsession.id}" onclick="return confirmx('确认要删除该会话吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>