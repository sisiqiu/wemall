<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>限时返现活动管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallCashbackDiscount/">限时返现活动列表</a></li>
		<shiro:hasPermission name="wemall:wemallCashbackDiscount:edit"><li><a href="${ctx}/wemall/wemallCashbackDiscount/form">限时返现活动添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallCashbackDiscount" action="${ctx}/wemall/wemallCashbackDiscount/" method="post" class="breadcrumb form-search">
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
					value="<fmt:formatDate value="${wemallCashbackDiscount.beginStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallCashbackDiscount.endStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="beginEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallCashbackDiscount.beginEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallCashbackDiscount.endEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>类型：</label>
				<form:radiobuttons path="type" items="${fns:getDictList('cashback_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>返现比例：</label>
				<form:input path="cashback" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>返现订单数：</label>
				<form:input path="limit" htmlEscape="false" maxlength="9" class="input-medium"/>
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
				<th>返现比例</th>
				<th>返现订单数</th>
				<shiro:hasPermission name="wemall:wemallCashbackDiscount:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallCashbackDiscount">
			<tr>
				<td><a href="${ctx}/wemall/wemallCashbackDiscount/form?id=${wemallCashbackDiscount.id}">
					${wemallCashbackDiscount.id}
				</a></td>
				<td>
					${wemallCashbackDiscount.name}
				</td>
				<td>
					${wemallCashbackDiscount.sort}
				</td>
				<td>
					<fmt:formatDate value="${wemallCashbackDiscount.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${wemallCashbackDiscount.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wemallCashbackDiscount.label}
				</td>
				<td>
					${fns:getDictLabel(wemallCashbackDiscount.type, 'cashback_type', '')}
				</td>
				<td>
					${wemallCashbackDiscount.cashback}
				</td>
				<td>
					${wemallCashbackDiscount.limit}
				</td>
				<shiro:hasPermission name="wemall:wemallCashbackDiscount:edit"><td>
    				<a href="${ctx}/wemall/wemallCashbackDiscount/form?id=${wemallCashbackDiscount.id}">修改</a>
					<a href="${ctx}/wemall/wemallCashbackDiscount/delete?id=${wemallCashbackDiscount.id}" onclick="return confirmx('确认要删除该限时返现活动吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>