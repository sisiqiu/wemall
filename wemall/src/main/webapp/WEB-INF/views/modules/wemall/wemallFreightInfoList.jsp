<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>物流明细信息管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallFreightInfo/">物流明细信息列表</a></li>
		<shiro:hasPermission name="wemall:wemallFreightInfo:edit"><li><a href="${ctx}/wemall/wemallFreightInfo/form">物流明细信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallFreightInfo" action="${ctx}/wemall/wemallFreightInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>物流单号：</label>
				<form:input path="freightNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>物流名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>包裹所在地：</label>
				<form:input path="curPlace" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>包裹派送人：</label>
				<form:input path="sendPeople" htmlEscape="false" maxlength="40" class="input-medium"/>
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
				<th>物流单号</th>
				<th>物流名称</th>
				<th>包裹所在地</th>
				<th>包裹派送人</th>
				<th>创建时间</th>
				<shiro:hasPermission name="wemall:wemallFreightInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallFreightInfo">
			<tr>
				<td><a href="${ctx}/wemall/wemallFreightInfo/form?id=${wemallFreightInfo.id}">
					${wemallFreightInfo.id}
				</a></td>
				<td>
					${wemallFreightInfo.freightNo}
				</td>
				<td>
					${wemallFreightInfo.name}
				</td>
				<td>
					${wemallFreightInfo.curPlace}
				</td>
				<td>
					${wemallFreightInfo.sendPeople}
				</td>
				<td>
					<fmt:formatDate value="${wemallFreightInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallFreightInfo:edit"><td>
    				<a href="${ctx}/wemall/wemallFreightInfo/form?id=${wemallFreightInfo.id}">修改</a>
					<a href="${ctx}/wemall/wemallFreightInfo/delete?id=${wemallFreightInfo.id}" onclick="return confirmx('确认要删除该物流明细信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>