<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户反馈信息管理管理</title>
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
		<li class="active"><a href="${ctx}/cms/cmsFeedback/">用户反馈信息管理列表</a></li>
		<%-- <shiro:hasPermission name="cms:cmsFeedback:edit"><li><a href="${ctx}/cms/cmsFeedback/form">用户反馈信息管理添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsFeedback" action="${ctx}/cms/cmsFeedback/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>意见表主键：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>意见表主键</th>
				<!-- <th>反馈内容</th> -->
				<th>手机号码</th>
				<th>用户的名称</th>
				<th>用户表id</th>
				<th>创建人</th>
				<th>创建时间</th>
				<th>修改人</th>
				<th>修改时间</th>
				<shiro:hasPermission name="cms:cmsFeedback:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsFeedback">
			<tr>
				<td><a href="${ctx}/cms/cmsFeedback/form?id=${cmsFeedback.id}">
					${cmsFeedback.id}
				</a></td>
				<%-- <td>
					${cmsFeedback.opinionText}
				</td> --%>
				<td>
					${cmsFeedback.telephone}
				</td>
				<td>
					${cmsFeedback.username}
				</td>
				<td>
					${cmsFeedback.user.name}
				</td>
				<td>
					${cmsFeedback.createBy.name}
				</td>
				<td>
					<fmt:formatDate value="${cmsFeedback.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${cmsFeedback.updateBy.name}
				</td>
				<td>
					<fmt:formatDate value="${cmsFeedback.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="cms:cmsFeedback:edit"><td>
    				<a href="${ctx}/cms/cmsFeedback/form?id=${cmsFeedback.id}">修改</a>
					<a href="${ctx}/cms/cmsFeedback/delete?id=${cmsFeedback.id}" onclick="return confirmx('确认要删除该用户反馈信息管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>