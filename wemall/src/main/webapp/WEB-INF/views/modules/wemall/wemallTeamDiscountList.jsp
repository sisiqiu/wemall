<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>限时拼团活动管理</title>
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
		<li class="active"><a href="${ctx}/wemall/wemallTeamDiscount/">限时拼团活动列表</a></li>
		<shiro:hasPermission name="wemall:wemallTeamDiscount:edit"><li><a href="${ctx}/wemall/wemallTeamDiscount/form">限时拼团活动添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallTeamDiscount" action="${ctx}/wemall/wemallTeamDiscount/" method="post" class="breadcrumb form-search">
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
					value="<fmt:formatDate value="${wemallTeamDiscount.beginStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallTeamDiscount.endStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="beginEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallTeamDiscount.beginEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallTeamDiscount.endEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>团有效时间：</label>
				<form:input path="usefulTime" htmlEscape="false" maxlength="6" class="input-medium"/>
			</li>
			<li><label>团失败人数：</label>
				<form:input path="failUserNum" htmlEscape="false" maxlength="6" class="input-medium"/>
			</li>
			<li><label>商品限购数：</label>
				<form:input path="limitItemNum" htmlEscape="false" maxlength="9" class="input-medium"/>
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
				<th>团有效时间</th>
				<th>拼团失败人数</th>
				<th>商品限购数</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wemall:wemallTeamDiscount:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallTeamDiscount">
			<tr>
				<td><a href="${ctx}/wemall/wemallTeamDiscount/form?id=${wemallTeamDiscount.id}">
					${wemallTeamDiscount.id}
				</a></td>
				<td>
					${wemallTeamDiscount.name}
				</td>
				<td>
					${wemallTeamDiscount.sort}
				</td>
				<td>
					<fmt:formatDate value="${wemallTeamDiscount.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${wemallTeamDiscount.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wemallTeamDiscount.label}
				</td>
				<td>
					${wemallTeamDiscount.usefulTime}
				</td>
				<td>
					${wemallTeamDiscount.failUserNum}
				</td>
				<td>
					${wemallTeamDiscount.limitItemNum}
				</td>
				<td>
					<fmt:formatDate value="${wemallTeamDiscount.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="wemall:wemallTeamDiscount:edit"><td>
    				<a href="${ctx}/wemall/wemallTeamDiscount/form?id=${wemallTeamDiscount.id}">修改</a>
					<a href="${ctx}/wemall/wemallTeamDiscount/delete?id=${wemallTeamDiscount.id}" onclick="return confirmx('确认要删除该限时拼团活动吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>