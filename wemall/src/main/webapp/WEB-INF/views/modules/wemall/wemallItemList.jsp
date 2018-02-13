<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallItem/">商品列表</a></li>
		<shiro:hasPermission name="wemall:wemallItem:edit"><li><a href="${ctx}/wemall/wemallItem/form">商品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallItem" action="${ctx}/wemall/wemallItem/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>商品名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>商品类别id：</label>
				<sys:treeselect id="sortId" name="sortId" value="${wemallItem.sortId}" labelName="sortName" labelValue="${wemallItem.sortName}"
						title="商品类别" url="/wemall/wemallItemSort/treeData" extId="${wemallItem.sortId}" cssClass="required"/>
				<%-- <form:input path="sortId" htmlEscape="false" maxlength="11" class="input-medium"/> --%>
			</li>
			<li><label>商品原价：</label>
				<form:input path="originalPrice" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>商品现价：</label>
				<form:input path="currentPrice" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>商品库存：</label>
				<form:input path="storage" htmlEscape="false" maxlength="9" class="input-medium"/>
			</li>
			<li><label>是否置顶：</label>
				<form:radiobuttons path="isTop" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否新品：</label>
				<form:radiobuttons path="isNew" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否上架：</label>
				<form:radiobuttons path="isOnShelf" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>销量：</label>
				<form:input path="salesNum" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<%-- <li><label>是否可用奖励金：</label>
				<form:radiobuttons path="canUseBounty" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否可用优惠券：</label>
				<form:radiobuttons path="canUseCoupon" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否可用积分抵扣：</label>
				<form:radiobuttons path="canUseScoreDeduct" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否可用积分兑换：</label>
				<form:radiobuttons path="canUseScoreExchange" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>是否支持下单减库存：</label>
				<form:radiobuttons path="subStock" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li> --%>
			<li><label>是否免邮：</label>
				<form:radiobuttons path="freightFree" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>运费：</label>
				<form:input path="freightPrice" htmlEscape="false" maxlength="9" class="input-medium"/>
			</li>
			<li><label>参与的活动：</label>
				<form:select path="activitySort" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('item_activity_sort')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>活动id：</label>
				<form:input path="activityId" htmlEscape="false" maxlength="11" class="input-medium"/>
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
				<th>商品名称</th>
				<th>商品类别id</th>
				<th>商品原价</th>
				<th>商品现价</th>
				<th>商品库存</th>
				<th>排序序号</th>
				<th>销量</th>
				<th>运费</th>
				<th>参与的活动</th>
				<th>活动id</th>
				<th>创建时间</th>
				<shiro:hasPermission name="wemall:wemallItem:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallItem">
			<tr>
				<td><a href="${ctx}/wemall/wemallItem/form?id=${wemallItem.id}">
					${wemallItem.id}
				</a></td>
				<td>
					${wemallItem.name}
				</td>
				<td>
					${wemallItem.sortName}
				</td>
				<td>
					${wemallItem.originalPrice}
				</td>
				<td>
					${wemallItem.currentPrice}
				</td>
				<td>
					${wemallItem.storage}
				</td>
				<td>
					${wemallItem.sort}
				</td>
				<td>
					${wemallItem.salesNum}
				</td>
				<td>
					${wemallItem.freightPrice}
				</td>
				<td>
					${fns:getDictLabel(wemallItem.activitySort, 'item_activity_sort', '')}
				</td>
				<td>
					${wemallItem.activityId}
				</td>
				<td>
					<fmt:formatDate value="${wemallItem.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallItem:edit"><td>
    				<a href="${ctx}/wemall/wemallItem/form?id=${wemallItem.id}">修改</a>
					<a href="${ctx}/wemall/wemallItem/delete?id=${wemallItem.id}" onclick="return confirmx('确认要删除该商品吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>