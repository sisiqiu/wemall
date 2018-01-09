<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收货地址管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallUserAddress/">收货地址列表</a></li>
		<shiro:hasPermission name="wemall:wemallUserAddress:edit"><li><a href="${ctx}/wemall/wemallUserAddress/form">收货地址添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallUserAddress" action="${ctx}/wemall/wemallUserAddress/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<sys:treeselect id="user" name="user.id" value="${wemallUserAddress.user.id}" labelName="user.name" labelValue="${wemallUserAddress.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
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
			<li><label>收货人手机：</label>
				<form:input path="receiverMobile" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>收货人电话：</label>
				<form:input path="receiverPhone" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>是否默认：</label>
				<form:radiobuttons path="isDefault" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>国家</th>
				<th>省份</th>
				<th>城市</th>
				<th>区县</th>
				<th>邮政编码</th>
				<th>收货人手机</th>
				<th>收货人电话</th>
				<th>是否默认</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallUserAddress:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallUserAddress">
			<tr>
				<td><a href="${ctx}/wemall/wemallUserAddress/form?id=${wemallUserAddress.id}">
					${wemallUserAddress.id}
				</a></td>
				<td>
					${wemallUserAddress.user.name}
				</td>
				<td>
					${wemallUserAddress.receiverCountry}
				</td>
				<td>
					${wemallUserAddress.receiverProvince}
				</td>
				<td>
					${wemallUserAddress.receiverCity}
				</td>
				<td>
					${wemallUserAddress.receiverDistrict}
				</td>
				<td>
					${wemallUserAddress.receiverZip}
				</td>
				<td>
					${wemallUserAddress.receiverMobile}
				</td>
				<td>
					${wemallUserAddress.receiverPhone}
				</td>
				<td>
					${fns:getDictLabel(wemallUserAddress.isDefault, 'yes_no', '')}
				</td>
				<td>
					<fmt:formatDate value="${wemallUserAddress.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallUserAddress:edit"><td>
    				<a href="${ctx}/wemall/wemallUserAddress/form?id=${wemallUserAddress.id}">修改</a>
					<a href="${ctx}/wemall/wemallUserAddress/delete?id=${wemallUserAddress.id}" onclick="return confirmx('确认要删除该收货地址吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>