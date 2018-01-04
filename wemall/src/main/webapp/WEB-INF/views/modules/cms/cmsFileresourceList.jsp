<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上传文件信息管理</title>
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
		<li class="active"><a href="${ctx}/cms/cmsFileresource/">上传文件信息列表</a></li>
		<shiro:hasPermission name="cms:cmsFileresource:edit"><li><a href="${ctx}/cms/cmsFileresource/form">上传文件信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsFileresource" action="${ctx}/cms/cmsFileresource/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>所属模块：</label>
				<form:input path="module" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>所属类别：</label>
				<form:input path="category" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>大小(KB)：</label>
				<form:input path="beginSize" htmlEscape="false" maxlength="11" class="input-medium"/> -  
				<form:input path="endSize" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>格式：</label>
				<form:input path="suffix" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>下载数：</label>
				<form:input path="beginDownloadNum" htmlEscape="false" maxlength="11" class="input-medium"/> -  
				<form:input path="endDownloadNum" htmlEscape="false" maxlength="11" class="input-medium"/>
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
				<th>所属模块</th>
				<th>所属类别</th>
				<th>名称</th>
				<th>大小(KB)</th>
				<th>格式</th>
				<th>下载数</th>
				<th>创建者</th>
				<th>更新时间</th>
				<shiro:hasPermission name="cms:cmsFileresource:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsFileresource">
			<tr>
				<td><a href="${ctx}/cms/cmsFileresource/form?id=${cmsFileresource.id}">
					${cmsFileresource.id}
				</a></td>
				<td><a href="${ctx}/cms/cmsFileresource/form?id=${cmsFileresource.id}">
					${cmsFileresource.module}
				</a></td>
				<td>
					${cmsFileresource.category}
				</td>
				<td>
					${cmsFileresource.name}
				</td>
				<td>
					${cmsFileresource.size}
				</td>
				<td>
					${cmsFileresource.suffix}
				</td>
				<td>
					${cmsFileresource.downloadNum}
				</td>
				<td>
					${cmsFileresource.createBy.name}
				</td>
				<td>
					<fmt:formatDate value="${cmsFileresource.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="cms:cmsFileresource:edit"><td>
    				<a href="${ctx}/cms/cmsFileresource/form?id=${cmsFileresource.id}">修改</a>
					<a href="${ctx}/cms/cmsFileresource/delete?id=${cmsFileresource.id}" onclick="return confirmx('确认要删除该上传文件信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>