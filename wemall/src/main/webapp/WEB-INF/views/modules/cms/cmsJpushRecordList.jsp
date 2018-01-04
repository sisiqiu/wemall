<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>极光推送历史记录管理</title>
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
		<li class="active"><a href="${ctx}/cms/cmsJpushRecord/">极光推送历史记录列表</a></li>
		<shiro:hasPermission name="cms:cmsJpushRecord:edit"><li><a href="${ctx}/cms/cmsJpushRecord/form">极光推送记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsJpushRecord" action="${ctx}/cms/cmsJpushRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>推送类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cms_push_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>定时时间：</label>
				<input name="beginScheduleTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${cmsJpushRecord.beginScheduleTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endScheduleTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${cmsJpushRecord.endScheduleTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>目标类型：</label>
				<form:select path="targetType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cms_push_targetType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>角色名称：</label>
				<form:input path="roleNames" htmlEscape="false" maxlength="500" class="input-medium"/>
			</li>
			<li><label>标签名称：</label>
				<form:input path="tagNames" htmlEscape="false" maxlength="500" class="input-medium"/>
			</li>
			<li><label>用户名称：</label>
				<form:input path="userNames" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label>推送标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>推送状态：</label>
				<form:radiobuttons path="status" items="${fns:getDictList('successOrFail')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${cmsJpushRecord.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${cmsJpushRecord.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>推送标题</th>
				<th>推送类型</th>
				<th>定时推送时间</th>
				<th>推送目标类型</th>
				<th>角色名称列表</th>
				<th>标签名称列表</th>
				<th>用户名称列表</th>
				<th>创建者</th>
				<th>推送结果</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="cms:cmsJpushRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsJpushRecord">
			<tr>
				<td><a href="${ctx}/cms/cmsJpushRecord/form?id=${cmsJpushRecord.id}">
					${cmsJpushRecord.title}
				</a></td>
				<td>
					${fns:getDictLabel(cmsJpushRecord.type, 'cms_push_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${cmsJpushRecord.scheduleTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(cmsJpushRecord.targetType, 'cms_push_targetType', '')}
				</td>
				<td>
					${cmsJpushRecord.roleNames}
				</td>
				<td>
					${cmsJpushRecord.tagNames}
				</td>
				<td>
					${cmsJpushRecord.userNames}
				</td>
				<td>
					${cmsJpushRecord.createBy.name}
				</td>
				<td>
					${fns:getDictLabel(cmsJpushRecord.status, 'successOrFail', '')}
				</td>
				<td>
					<fmt:formatDate value="${cmsJpushRecord.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${cmsJpushRecord.remarks}
				</td>
				<shiro:hasPermission name="cms:cmsJpushRecord:edit"><td>
    				<%-- <a href="${ctx}/cms/cmsJpushRecord/form?id=${cmsJpushRecord.id}">修改</a> --%>
					<a href="${ctx}/cms/cmsJpushRecord/delete?id=${cmsJpushRecord.id}" onclick="return confirmx('确认要删除该极光推送历史记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>