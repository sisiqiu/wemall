<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>群发短信管理</title>
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
		<li class="active"><a href="${ctx}/wx/conGroupsms/">群发短信列表</a></li>
		<shiro:hasPermission name="wx:conGroupsms:edit"><li><a href="${ctx}/wx/conGroupsms/form">群发短信添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="conGroupsms" action="${ctx}/wx/conGroupsms/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>短信模板ID：</label>
				<form:input path="templeteId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>发送时间：</label>
				<input name="beginSendTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${conGroupsms.beginSendTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endSendTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${conGroupsms.endSendTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>发送策略：</label>
				<form:select path="sendType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('con_groupSMS_sendType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('con_groupSMS_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>创建者：</label>
				<sys:treeselect id="createBy" name="createBy.id" value="${conGroupsms.createBy.id}" labelName="createBy.name" labelValue="${conGroupsms.createBy.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
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
				<th>短信模板ID</th>
				<th>发送时间</th>
				<th>发送策略</th>
				<th>状态</th>
				<th>创建者</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wx:conGroupsms:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="conGroupsms">
			<tr>
				<td><a href="${ctx}/wx/conGroupsms/form?id=${conGroupsms.id}">
					${conGroupsms.templeteId}
				</a></td>
				<td>
					<fmt:formatDate value="${conGroupsms.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(conGroupsms.sendType, 'con_groupSMS_sendType', '')}
				</td>
				<td>
					${fns:getDictLabel(conGroupsms.status, 'con_groupSMS_status', '')}
				</td>
				<td>
					${conGroupsms.createBy.name}
				</td>
				<td>
					<fmt:formatDate value="${conGroupsms.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${conGroupsms.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wx:conGroupsms:edit"><td>
    				<a href="${ctx}/wx/conGroupsms/form?id=${conGroupsms.id}">修改</a>
					<a href="${ctx}/wx/conGroupsms/delete?id=${conGroupsms.id}" onclick="return confirmx('确认要删除该群发短信吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>