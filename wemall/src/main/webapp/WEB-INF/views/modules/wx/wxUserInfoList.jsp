<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信用户信息管理</title>
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
		<li class="active"><a href="${ctx}/wx/wxUserInfo/">微信用户信息列表</a></li>
		<shiro:hasPermission name="wx:wxUserInfo:edit"><li><a href="${ctx}/wx/wxUserInfo/form">微信用户信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxUserInfo" action="${ctx}/wx/wxUserInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<sys:treeselect id="user" name="user.id" value="${wxUserInfo.user.id}" labelName="user.name" labelValue="${wxUserInfo.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>服务号id：</label>
				<form:input path="serviceId" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>微信openid：</label>
				<form:input path="openId" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>昵称：</label>
				<form:input path="nickName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>是否关注：</label>
				<form:radiobuttons path="isFocus" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否已获取用户信息：</label>
				<form:radiobuttons path="isGetUserInfo" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否已修改密码：</label>
				<form:radiobuttons path="isChangePw" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否已绑定手机：</label>
				<form:radiobuttons path="isBindMobile" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>服务号id</th>
				<th>微信openid</th>
				<th>用户名</th>
				<th>昵称</th>
				<th>性别</th>
				<th>手机号</th>
				<th>是否关注</th>
				<th>是否已获取用户信息</th>
				<th>是否已修改密码</th>
				<th>是否已绑定手机</th>
				<shiro:hasPermission name="wx:wxUserInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxUserInfo">
			<tr>
				<td><a href="${ctx}/wx/wxUserInfo/form?id=${wxUserInfo.id}">
					${wxUserInfo.id}
				</a></td>
				<td>
					${wxUserInfo.user.name}
				</td>
				<td>
					${wxUserInfo.serviceId}
				</td>
				<td>
					${wxUserInfo.openId}
				</td>
				<td>
					${wxUserInfo.userName}
				</td>
				<td>
					${wxUserInfo.nickName}
				</td>
				<td>
					${fns:getDictLabel(wxUserInfo.sex, 'sex', '')}
				</td>
				<td>
					${wxUserInfo.mobile}
				</td>
				<td>
					${fns:getDictLabel(wxUserInfo.isFocus, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(wxUserInfo.isGetUserInfo, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(wxUserInfo.isChangePw, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(wxUserInfo.isBindMobile, 'yes_no', '')}
				</td>
				<shiro:hasPermission name="wx:wxUserInfo:edit"><td>
    				<a href="${ctx}/wx/wxUserInfo/form?id=${wxUserInfo.id}">修改</a>
					<a href="${ctx}/wx/wxUserInfo/delete?id=${wxUserInfo.id}" onclick="return confirmx('确认要删除该微信用户信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>