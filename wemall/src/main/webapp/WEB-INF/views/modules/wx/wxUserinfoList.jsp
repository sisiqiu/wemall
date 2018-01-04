<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信绑定用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//重置选中
			$("#btn_reset").click(function(){
				 location.replace(location.href);
			})
		
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出微信绑定用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						var oldAction = $("#searchForm").attr("action");
						$("#searchForm").attr("action","${ctx}/wx/wxUserinfo/export");
						$("#searchForm").submit();
						$("#searchForm").attr("action",oldAction);
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
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
		<li class="active"><a href="${ctx}/wx/wxUserinfo/">微信绑定用户列表</a></li>
		<shiro:hasPermission name="wx:wxUserinfo:edit"><li><a href="${ctx}/wx/wxUserinfo/form">微信绑定用户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxUserinfo" action="${ctx}/wx/wxUserinfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>微信号：</label>
				<form:input path="wxNo" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>用户名：</label>
				<form:input path="username" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>昵称：</label>
				<form:input path="nickname" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>性别：</label>
				<form:radiobuttons id="sex_redio" path="sex" items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>用户ID：</label>
				<sys:treeselect id="slUser" name="slUser.id" value="${wxUserinfo.slUser.id}" labelName="slUser.name" labelValue="${wxUserinfo.slUser.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('wx_subscriber_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns">
				<input id="btn_reset" class="btn btn-primary" type="button" value="重置"/>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>微信号</th>
				<th>手机号</th>
				<th>用户名</th>
				<th>昵称</th>
				<th>性别</th>
				<th>用户ID</th>
				<th>状态</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wx:wxUserinfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxUserinfo">
			<tr>
				<td><a href="${ctx}/wx/wxUserinfo/form?id=${wxUserinfo.wxUserId}">
					${wxUserinfo.wxNo}
				</a></td>
				<td>
					${wxUserinfo.mobile}
				</td>
				<td>
					${wxUserinfo.username}
				</td>
				<td>
					${wxUserinfo.nickname}
				</td>
				<td>
					${fns:getDictLabel(wxUserinfo.sex, 'sex', '')}
				</td>
				<td>
					${wxUserinfo.slUser.name}
				</td>
				<td>
					${fns:getDictLabel(wxUserinfo.status, 'wx_subscriber_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${wxUserinfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wx:wxUserinfo:edit"><td>
    				<a href="${ctx}/wx/wxUserinfo/form?id=${wxUserinfo.wxUserId}">修改</a>
					<a href="${ctx}/wx/wxUserinfo/delete?id=${wxUserinfo.wxUserId}" onclick="return confirmx('确认要删除该微信绑定用户吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>