<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>充值设定管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallRecharge/">充值设定列表</a></li>
		<shiro:hasPermission name="wemall:wemallRecharge:edit"><li><a href="${ctx}/wemall/wemallRecharge/form">充值设定添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallRecharge" action="${ctx}/wemall/wemallRecharge/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="6" class="input-medium"/>
			</li>
			<li><label>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>需要填写地址：</label>
				<form:radiobuttons path="needaddress" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>名称</th>
				<th>充值价格</th>
				<!-- <th>充值现价</th> -->
				<th>是否需要填写地址</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallRecharge:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallRecharge">
			<tr>
				<td><a href="${ctx}/wemall/wemallRecharge/form?id=${wemallRecharge.id}">
					${wemallRecharge.name}
				</a></td>
				<td>
					￥<fmt:formatNumber type="number" value="${wemallRecharge.originalPrice/100}" pattern="0.00" maxFractionDigits="2"/>
				</td>
				<%-- <td>
					￥<fmt:formatNumber type="number" value="${wemallRecharge.currentPrice/100}" pattern="0.00" maxFractionDigits="2"/>
					${wemallRecharge.currentPrice}
				</td> --%>
				<td>
					${fns:getDictLabel(wemallRecharge.needaddress, 'yes_no', '')}
				</td>
				<td>
					<fmt:formatDate value="${wemallRecharge.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallRecharge:edit"><td>
    				<a href="${ctx}/wemall/wemallRecharge/form?id=${wemallRecharge.id}">修改</a>
					<a href="${ctx}/wemall/wemallRecharge/delete?id=${wemallRecharge.id}" onclick="return confirmx('确认要删除该充值设定吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>