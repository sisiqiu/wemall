<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>广告位管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//重置选中
			$("#btn_reset").click(function(){
				 location.replace(location.href);
			});
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
		<li class="active"><a href="${ctx}/sys/slSysAdvertise/">广告位列表</a></li>
		<shiro:hasPermission name="sys:slSysAdvertise:edit"><li><a href="${ctx}/sys/slSysAdvertise/form">广告位添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="slSysAdvertise" action="${ctx}/sys/slSysAdvertise/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>是否隐藏：</label>
				<form:radiobuttons path="isDisplay" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>创建者：</label>
				<sys:treeselect id="createBy" name="createBy.id" value="${slSysAdvertise.createBy.id}" labelName="createBy.name" labelValue="${slSysAdvertise.createBy.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>更新者：</label>
				<sys:treeselect id="updateBy" name="updateBy.id" value="${slSysAdvertise.updateBy.id}" labelName="updateBy.name" labelValue="${slSysAdvertise.updateBy.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li class="btns">
				<input id="btn_reset" class="btn btn-primary" type="button" value="重置"/>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</li>
			<li><label>状态：</label>
				<form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键id</th>
				<th>描述</th>
				<th>备注</th>
				<th>是否隐藏</th>
				<th>创建者</th>
				<th>更新者</th>
				<th>更新时间</th>
				<th>状态</th>
				<shiro:hasPermission name="sys:slSysAdvertise:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="slSysAdvertise">
			<tr>
				<td><a href="${ctx}/sys/slSysAdvertise/form?id=${slSysAdvertise.id}">
					${slSysAdvertise.id}
				</a></td>
				<td>
					${slSysAdvertise.adDesc}
				</td>
				<td>
					${slSysAdvertise.remarks}
				</td>
				<td>
					${fns:getDictLabel(slSysAdvertise.isDisplay, 'show_hide', '')}
				</td>
				<td>
					${slSysAdvertise.createBy.name}
				</td>
				<td>
					${slSysAdvertise.updateBy.name}
				</td>
				<td>
					<fmt:formatDate value="${slSysAdvertise.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(slSysAdvertise.delFlag, 'cms_del_flag', '')}
				</td>
				<shiro:hasPermission name="sys:slSysAdvertise:edit"><td>
    				<a href="${ctx}/sys/slSysAdvertise/form?id=${slSysAdvertise.id}">修改</a>
					<shiro:hasPermission name="sys:slSysAdvertise:audit">
						<a href="${ctx}/sys/slSysAdvertise/delete?id=${slSysAdvertise.id}${slSysAdvertise.delFlag ne 0?'&isRe=true':''}" onclick="return confirmx('确认要${slSysAdvertise.delFlag ne 0?'发布':'删除'}该广告位吗？', this.href)" >${slSysAdvertise.delFlag ne 0?'发布':'删除'}</a>
					</shiro:hasPermission>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>