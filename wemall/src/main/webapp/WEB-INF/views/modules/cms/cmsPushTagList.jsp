<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>标签管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//重置选中
			$("#btn_reset").click(function(){
				 location.replace(location.href);
			});
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
		<li class="active"><a href="${ctx}/cms/cmsPushTag/">标签列表</a></li>
		<shiro:hasPermission name="cms:cmsPushTag:edit"><li><a href="${ctx}/cms/cmsPushTag/form">标签添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsPushTag" action="${ctx}/cms/cmsPushTag/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>标签名：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户名：</label>
				<form:input path="userNames" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:radiobuttons path="status" items="${fns:getDictList('sl_his_regPoolStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="btns">
				<input id="btn_reset" class="btn btn-primary" type="button" value="重置"/>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键id</th>
				<th>标签名</th>
				<th>状态</th>
				<th>更新者</th>
				<th>更新时间</th>
				<shiro:hasPermission name="cms:cmsPushTag:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsPushTag">
			<tr>
				<td><a href="${ctx}/cms/cmsPushTag/form?id=${cmsPushTag.id}">
					${cmsPushTag.id}
				</a></td>
				<td>
					${cmsPushTag.name}
				</td>
				<td>
					${fns:getDictLabel(cmsPushTag.status, 'sl_his_regPoolStatus', '')}
				</td>
				<td>
					${cmsPushTag.updateBy.name}
				</td>
				<td>
					<fmt:formatDate value="${cmsPushTag.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="cms:cmsPushTag:edit"><td>
    				<a href="${ctx}/cms/cmsPushTag/form?id=${cmsPushTag.id}">修改</a>
					<a href="${ctx}/cms/cmsPushTag/delete?id=${cmsPushTag.id}" onclick="return confirmx('确认要删除该标签吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>