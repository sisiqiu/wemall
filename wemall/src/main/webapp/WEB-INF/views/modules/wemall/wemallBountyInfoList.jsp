<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>余额明细信息管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallBountyInfo/">余额明细信息列表</a></li>
		<shiro:hasPermission name="wemall:wemallBountyInfo:edit"><li><a href="${ctx}/wemall/wemallBountyInfo/form">余额明细信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallBountyInfo" action="${ctx}/wemall/wemallBountyInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<sys:treeselect id="user" name="user.id" value="${wemallBountyInfo.user.id}" labelName="user.name" labelValue="${wemallBountyInfo.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>获取途径：</label>
				<form:select path="fromType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('bounty_fromType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>订单号：</label>
				<form:input path="orderNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>类型：</label>
				<form:radiobuttons path="type" items="${fns:getDictList('out_in')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>金额：</label>
				<form:input path="price" htmlEscape="false" maxlength="11" class="input-medium"/>
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
				<th>订单号</th>
				<th>类型</th>
				<th>金额</th>
				<th>创建时间</th>
				<shiro:hasPermission name="wemall:wemallBountyInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallBountyInfo">
			<tr>
				<td><a href="${ctx}/wemall/wemallBountyInfo/form?id=${wemallBountyInfo.id}">
					${wemallBountyInfo.id}
				</a></td>
				<td>
					${wemallBountyInfo.user.name}
				</td>
				<td>
					${fns:getDictLabel(wemallBountyInfo.fromType, 'bounty_fromType', '')}
				</td>
				<td>
					${wemallBountyInfo.orderNo}
				</td>
				<td>
					${fns:getDictLabel(wemallBountyInfo.type, 'out_in', '')}
				</td>
				<td>
					￥<fmt:formatNumber type="number" value="${wemallBountyInfo.price/100}" pattern="0.00" maxFractionDigits="2"/>
				</td>
				<td>
					<fmt:formatDate value="${wemallBountyInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallBountyInfo:edit"><td>
    				<a href="${ctx}/wemall/wemallBountyInfo/form?id=${wemallBountyInfo.id}">修改</a>
					<a href="${ctx}/wemall/wemallBountyInfo/delete?id=${wemallBountyInfo.id}" onclick="return confirmx('确认要删除该余额明细信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>