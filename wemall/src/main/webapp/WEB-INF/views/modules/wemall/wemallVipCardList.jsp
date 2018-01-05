<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员卡管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallVipCard/">会员卡列表</a></li>
		<shiro:hasPermission name="wemall:wemallVipCard:edit"><li><a href="${ctx}/wemall/wemallVipCard/form">会员卡添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallVipCard" action="${ctx}/wemall/wemallVipCard/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>会员卡id：</label>
				<form:input path="id" htmlEscape="false" maxlength="6" class="input-medium"/>
			</li>
			<li><label>会员卡名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>会员卡类别：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('vipCard_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>会员卡id</th>
				<th>会员卡名称</th>
				<th>会员卡类别</th>
				<th>排序</th>
				<th>享受折扣</th>
				<th>是否免邮</th>
				<th>交易笔数</th>
				<th>消费金额</th>
				<th>累计积分</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallVipCard:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallVipCard">
			<tr>
				<td><a href="${ctx}/wemall/wemallVipCard/form?id=${wemallVipCard.id}">
					${wemallVipCard.id}
				</a></td>
				<td>
					${wemallVipCard.name}
				</td>
				<td>
					${fns:getDictLabel(wemallVipCard.type, 'vipCard_type', '')}
				</td>
				<td>
					${wemallVipCard.sort}
				</td>
				<td>
					${wemallVipCard.discount}
				</td>
				<td>
					${fns:getDictLabel(wemallVipCard.freightFree, 'yes_no', '')}
				</td>
				<td>
					${wemallVipCard.orderNum}
				</td>
				<td>
					${wemallVipCard.consumeNum}
				</td>
				<td>
					${wemallVipCard.scoreNum}
				</td>
				<td>
					<fmt:formatDate value="${wemallVipCard.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallVipCard:edit"><td>
    				<a href="${ctx}/wemall/wemallVipCard/form?id=${wemallVipCard.id}">修改</a>
					<a href="${ctx}/wemall/wemallVipCard/delete?id=${wemallVipCard.id}" onclick="return confirmx('确认要删除该会员卡吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>