<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信服务号管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//重置选中
			$("#btn_reset").click(function(){
				 location.replace(location.href);
			})
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
		<li class="active"><a href="${ctx}/wx/wxServiceaccount/">微信服务号列表</a></li>
		<shiro:hasPermission name="wx:wxServiceaccount:edit"><li><a href="${ctx}/wx/wxServiceaccount/form">微信服务号添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxServiceaccount" action="${ctx}/wx/wxServiceaccount/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>服务号ID：</label>
				<form:input path="serviceId" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>服务号NO：</label>
				<form:input path="serviceNo" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>服务号名称：</label>
				<form:input path="saName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>认证状态：</label>
				<form:radiobuttons path="authStatus" items="${fns:getDictList('wx_sa_authstatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>服务号类型：</label>
				<form:select path="accountType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('wx_sa_accounttype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>状态：</label>
				<form:radiobuttons path="status" items="${fns:getDictList('wx_sa_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				<input id="btn_reset" class="btn btn-primary" type="button" value="重置"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键ID</th>
				<th>服务号ID</th>
				<th>服务号NO</th>
				<th>服务号名称</th>
				<th>认证状态</th>
				<th>服务号类型</th>
				<th>状态</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wx:wxServiceaccount:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxServiceaccount">
			<tr>
				<td><a href="${ctx}/wx/wxServiceaccount/form?id=${wxServiceaccount.saId}">
					${wxServiceaccount.saId}
				</a></td>
				<td><a href="${ctx}/wx/wxServiceaccount/form?id=${wxServiceaccount.saId}">
					${wxServiceaccount.serviceId}
				</a></td>
				<td>
					${wxServiceaccount.serviceNo}
				</td>
				<td>
					${wxServiceaccount.saName}
				</td>
				<td>
					${fns:getDictLabel(wxServiceaccount.authStatus, 'wx_sa_authstatus', '')}
				</td>
				<td>
					${fns:getDictLabel(wxServiceaccount.accountType, 'wx_sa_accounttype', '')}
				</td>
				<td>
					${fns:getDictLabel(wxServiceaccount.status, 'wx_sa_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${wxServiceaccount.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wx:wxServiceaccount:edit"><td>
    				<a href="${ctx}/wx/wxServiceaccount/form?id=${wxServiceaccount.saId}">修改</a>
					<a href="${ctx}/wx/wxServiceaccount/delete?id=${wxServiceaccount.saId}" onclick="return confirmx('确认要删除该微信服务号吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>