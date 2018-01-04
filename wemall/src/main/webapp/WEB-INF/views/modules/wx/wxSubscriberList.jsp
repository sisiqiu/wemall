<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信关注用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btn_reset").click(function(){
				 location.replace(location.href);
			})
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出微信关注用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						var oldAction = $("#searchForm").attr("action");
						$("#searchForm").attr("action","${ctx}/wx/wxSubscriber/export");
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
		<li class="active"><a href="${ctx}/wx/wxSubscriber/">微信关注用户列表</a></li>
		<shiro:hasPermission name="wx:wxSubscriber:edit"><li><a href="${ctx}/wx/wxSubscriber/form">微信关注用户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxSubscriber" action="${ctx}/wx/wxSubscriber/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>是否服务人员：</label>
				<form:radiobuttons path="isServicePerson" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>性别：</label>
				<form:radiobuttons path="sex" items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否预约：</label>
				<form:radiobuttons path="subscribe" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>登陆名：</label>
				<form:input path="loginName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="30" class="input-medium"/>
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
				<th>主键id</th>
				<th>用户名</th>
				<th>openId</th>
				<th>昵称</th>
				<th>性别</th>
				<th>手机号</th>
				<th>服务号ID</th>
				<th>状态值</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wx:wxSubscriber:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxSubscriber">
			<tr>
				<td><a href="${ctx}/wx/wxSubscriber/form?id=${wxSubscriber.subscriberId}">
					${wxSubscriber.subscriberId}
				</a></td>
				<td><a href="${ctx}/wx/wxSubscriber/form?id=${wxSubscriber.subscriberId}">
					${wxSubscriber.username}
				</a></td>
				<td>
					${wxSubscriber.openId}
				</td>
				<td>
					${wxSubscriber.nickname}
				</td>
				<td>
					${fns:getDictLabel(wxSubscriber.sex, 'sex', '')}
				</td>
				<td>
					${wxSubscriber.mobile}
				</td>
				<td>
					${wxSubscriber.serviceId}
				</td>
				<td>
					${fns:getDictLabel(wxSubscriber.status, 'wx_subscriber_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${wxSubscriber.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wx:wxSubscriber:edit"><td>
    				<a href="${ctx}/wx/wxSubscriber/form?id=${wxSubscriber.subscriberId}">修改</a>
					<a href="${ctx}/wx/wxSubscriber/delete?id=${wxSubscriber.subscriberId}" onclick="return confirmx('确认要删除该微信关注用户吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>