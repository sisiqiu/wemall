<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户-极光注册id管理</title>
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
		<li class="active"><a href="${ctx}/cms/cmsUserRegid/">用户-极光注册id列表</a></li>
		<shiro:hasPermission name="cms:cmsUserRegid:edit"><li><a href="${ctx}/cms/cmsUserRegid/form">用户-极光注册id添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsUserRegid" action="${ctx}/cms/cmsUserRegid/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>极光注册id：</label>
				<form:input path="registrationId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:radiobuttons path="status" items="${fns:getDictList('sl_his_regPoolStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>极光注册id</th>
				<th>用户id</th>
				<th>状态</th>
				<th>更新时间</th>
				<shiro:hasPermission name="cms:cmsUserRegid:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsUserRegid">
			<tr>
				<td><a href="${ctx}/cms/cmsUserRegid/form?id=${cmsUserRegid.id}">
					${cmsUserRegid.id}
				</a></td>
				<td>
					${cmsUserRegid.registrationId}
				</td>
				<td>
					${cmsUserRegid.userId}
				</td>
				<td>
					${fns:getDictLabel(cmsUserRegid.status, 'sl_his_regPoolStatus', '')}
				</td>
				<td>
					<fmt:formatDate value="${cmsUserRegid.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="cms:cmsUserRegid:edit"><td>
    				<a href="${ctx}/cms/cmsUserRegid/form?id=${cmsUserRegid.id}">修改</a>
					<a href="${ctx}/cms/cmsUserRegid/delete?id=${cmsUserRegid.id}" onclick="return confirmx('确认要删除该用户-极光注册id吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>