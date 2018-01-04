<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户活动表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户活动数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						var oldAction = $("#searchForm").attr("action");
						$("#searchForm").attr("action","${ctx}/wx/conUserActivity/export");
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
		<li class="active"><a href="${ctx}/wx/conUserActivity/">用户活动表列表</a></li>
		<shiro:hasPermission name="wx:conUserActivity:edit"><li><a href="${ctx}/wx/conUserActivity/form">用户活动表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="conUserActivity" action="${ctx}/wx/conUserActivity/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户ID：</label>
				<sys:treeselect id="user" name="user.id" value="${conUserActivity.user.id}" labelName="user.name" labelValue="${conUserActivity.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>活动ID：</label>
				<form:input path="activityid" htmlEscape="false" maxlength="8" class="input-medium"/>
			</li>
			<li><label>联系方式：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>用户状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('con_activity_user_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>奖项：</label>
				<form:input path="price" htmlEscape="false" maxlength="40" class="input-medium"/>
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
				<a class="btn btn-primary" href="${ctxBasic}/f/wx/conActivity/prizeDraw"  target="_blank">抽奖</a>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户ID</th>
				<th>活动ID</th>
				<th>联系方式</th>
				<th>用户状态</th>
				<th>奖项</th>
				<shiro:hasPermission name="wx:conUserActivity:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="conUserActivity">
			<tr>
				<td><a href="${ctx}/wx/conUserActivity/form?id=${conUserActivity.id}">
					${conUserActivity.user.name}
				</a></td>
				<td>
					${conUserActivity.activityid}
				</td>
				<td>
					${conUserActivity.mobile}
				</td>
				<td>
					${fns:getDictLabel(conUserActivity.status, 'con_activity_user_status', '')}
				</td>
				<td>
					${conUserActivity.price}
				</td>
				<shiro:hasPermission name="wx:conUserActivity:edit"><td>
    				<a href="${ctx}/wx/conUserActivity/form?id=${conUserActivity.id}">修改</a>
					<a href="${ctx}/wx/conUserActivity/delete?id=${conUserActivity.id}" onclick="return confirmx('确认要删除该用户活动表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>