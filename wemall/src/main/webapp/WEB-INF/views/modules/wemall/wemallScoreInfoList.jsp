<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>积分明细管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallScoreInfo/">积分明细列表</a></li>
		<shiro:hasPermission name="wemall:wemallScoreInfo:edit"><li><a href="${ctx}/wemall/wemallScoreInfo/form">积分明细添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallScoreInfo" action="${ctx}/wemall/wemallScoreInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<sys:treeselect id="user" name="user.id" value="${wemallScoreInfo.user.id}" labelName="user.name" labelValue="${wemallScoreInfo.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>获取途径：</label>
				<form:select path="fromType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('score_fromType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>类型：</label>
				<form:radiobuttons path="type" items="${fns:getDictList('decrease_increase')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>分值：</label>
				<form:input path="score" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallScoreInfo.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallScoreInfo.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>主键id</th>
				<th>用户id</th>
				<th>获取途径</th>
				<th>类型</th>
				<th>分值</th>
				<th>创建时间</th>
				<shiro:hasPermission name="wemall:wemallScoreInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallScoreInfo">
			<tr>
				<td><a href="${ctx}/wemall/wemallScoreInfo/form?id=${wemallScoreInfo.id}">
					${wemallScoreInfo.id}
				</a></td>
				<td>
					${wemallScoreInfo.user.name}
				</td>
				<td>
					${fns:getDictLabel(wemallScoreInfo.fromType, 'score_fromType', '')}
				</td>
				<td>
					${fns:getDictLabel(wemallScoreInfo.type, 'decrease_increase', '')}
				</td>
				<td>
					${wemallScoreInfo.score}
				</td>
				<td>
					<fmt:formatDate value="${wemallScoreInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallScoreInfo:edit"><td>
    				<a href="${ctx}/wemall/wemallScoreInfo/form?id=${wemallScoreInfo.id}">修改</a>
					<a href="${ctx}/wemall/wemallScoreInfo/delete?id=${wemallScoreInfo.id}" onclick="return confirmx('确认要删除该积分明细吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>