<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单-地址信息管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallOrderAddress/">订单-地址信息列表</a></li>
		<shiro:hasPermission name="wemall:wemallOrderAddress:edit"><li><a href="${ctx}/wemall/wemallOrderAddress/form">订单-地址信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallOrderAddress" action="${ctx}/wemall/wemallOrderAddress/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订单号：</label>
				<form:input path="orderNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>国家：</label>
				<form:input path="receiverCountry" htmlEscape="false" maxlength="40" class="input-medium"/>
			</li>
			<li><label>省份：</label>
				<form:input path="receiverProvince" htmlEscape="false" maxlength="40" class="input-medium"/>
			</li>
			<li><label>城市：</label>
				<form:input path="receiverCity" htmlEscape="false" maxlength="40" class="input-medium"/>
			</li>
			<li><label>区县：</label>
				<form:input path="receiverDistrict" htmlEscape="false" maxlength="40" class="input-medium"/>
			</li>
			<li><label>邮政编码：</label>
				<form:input path="receiverZip" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>收货人姓名：</label>
				<form:input path="receiverName" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>收货人手机：</label>
				<form:input path="receiverMobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>收货人电话：</label>
				<form:input path="receiverPhone" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>订单号</th>
				<th>国家</th>
				<th>省份</th>
				<th>城市</th>
				<th>区县</th>
				<th>邮政编码</th>
				<th>收货人姓名</th>
				<th>收货人手机</th>
				<th>收货人电话</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallOrderAddress:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallOrderAddress">
			<tr>
				<td><a href="${ctx}/wemall/wemallOrderAddress/form?orderNo=${wemallOrderAddress.orderNo}">
					${wemallOrderAddress.orderNo}
				</a></td>
				<td>
					${wemallOrderAddress.receiverCountry}
				</td>
				<td>
					${wemallOrderAddress.receiverProvince}
				</td>
				<td>
					${wemallOrderAddress.receiverCity}
				</td>
				<td>
					${wemallOrderAddress.receiverDistrict}
				</td>
				<td>
					${wemallOrderAddress.receiverZip}
				</td>
				<td>
					${wemallOrderAddress.receiverName}
				</td>
				<td>
					${wemallOrderAddress.receiverMobile}
				</td>
				<td>
					${wemallOrderAddress.receiverPhone}
				</td>
				<td>
					<fmt:formatDate value="${wemallOrderAddress.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallOrderAddress:edit"><td>
    				<a href="${ctx}/wemall/wemallOrderAddress/form?orderNo=${wemallOrderAddress.orderNo}">修改</a>
					<a href="${ctx}/wemall/wemallOrderAddress/delete?orderNo=${wemallOrderAddress.orderNo}" onclick="return confirmx('确认要删除该订单-地址信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>