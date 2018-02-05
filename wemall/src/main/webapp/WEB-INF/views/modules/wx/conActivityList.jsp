<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>活动表管理</title>
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
		<li class="active"><a href="${ctx}/wx/conActivity/">活动表列表</a></li>
		<shiro:hasPermission name="wx:conActivity:edit"><li><a href="${ctx}/wx/conActivity/form">活动表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="conActivity" action="${ctx}/wx/conActivity/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>活动标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li><label>举办方：</label>
				<form:input path="organizer" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>开始时间：</label>
				<input name="beginFromdate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${conActivity.beginFromdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endFromdate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${conActivity.endFromdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="beginEnddate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${conActivity.beginEnddate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endEnddate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${conActivity.endEnddate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('con_activity_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
				<th>主键id</th>
				<th>活动标题</th>
				<th>活动开始时间</th>
				<th>活动结束时间</th>
				<th>最大人数</th>
				<th>当前已报名人数</th>
				<th>状态</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wx:conActivity:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="conActivity">
			<tr>
				<td><a href="${ctx}/wx/conActivity/form?id=${conActivity.id}">
					${conActivity.id}
				</a></td>
				<td><a href="${ctx}/wx/conActivity/form?id=${conActivity.id}">
					${conActivity.title}
				</a></td>
				<td>
					<fmt:formatDate value="${conActivity.fromdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${conActivity.enddate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${conActivity.maxpeoplenum}
				</td>
				<td>
					${conActivity.currentpeoplenum}
				</td>
				<td>
					${fns:getDictLabel(conActivity.status, 'con_activity_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${conActivity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wx:conActivity:edit"><td>
    				<a href="${ctx}/wx/conActivity/form?id=${conActivity.id}">修改</a>
					<a href="${ctx}/wx/conActivity/delete?id=${conActivity.id}" onclick="return confirmx('确认要删除该活动表吗？', this.href)">删除</a>
					<a href="${ctxStatic}/sanlen_website/newYear/index.html?id=${conActivity.id}" target="_blank">抽奖</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>