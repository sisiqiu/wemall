<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>限时打折活动管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallTimeDiscount/">限时打折活动列表</a></li>
		<shiro:hasPermission name="wemall:wemallTimeDiscount:edit"><li><a href="${ctx}/wemall/wemallTimeDiscount/form">限时打折活动添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallTimeDiscount" action="${ctx}/wemall/wemallTimeDiscount/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>活动id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>活动名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>开始时间：</label>
				<input name="beginStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallTimeDiscount.beginStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallTimeDiscount.endStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="beginEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallTimeDiscount.beginEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallTimeDiscount.endEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>类型：</label>
				<form:radiobuttons path="type" items="${fns:getDictList('percent_fixedValue')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>活动id</th>
				<th>活动名称</th>
				<th>排序</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>标签名</th>
				<th>类型</th>
				<th>折扣量</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallTimeDiscount:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallTimeDiscount">
			<tr>
				<td><a href="${ctx}/wemall/wemallTimeDiscount/form?id=${wemallTimeDiscount.id}">
					${wemallTimeDiscount.id}
				</a></td>
				<td>
					${wemallTimeDiscount.name}
				</td>
				<td>
					${wemallTimeDiscount.sort}
				</td>
				<td>
					<fmt:formatDate value="${wemallTimeDiscount.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${wemallTimeDiscount.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wemallTimeDiscount.label}
				</td>
				<td>
					${fns:getDictLabel(wemallTimeDiscount.type, 'percent_fixedValue', '')}
				</td>
				<td>
					${wemallTimeDiscount.discount}
				</td>
				<td>
					<fmt:formatDate value="${wemallTimeDiscount.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallTimeDiscount:edit"><td>
    				<a href="${ctx}/wemall/wemallTimeDiscount/form?id=${wemallTimeDiscount.id}">修改</a>
					<a href="${ctx}/wemall/wemallTimeDiscount/delete?id=${wemallTimeDiscount.id}" onclick="return confirmx('确认要删除该限时打折活动吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>