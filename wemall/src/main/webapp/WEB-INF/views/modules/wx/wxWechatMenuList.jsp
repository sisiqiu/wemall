<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信服务号菜单管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, ids = [], rootIds = [];
			for (var i=0; i<data.length; i++){
				ids.push(data[i].id);
			}
			ids = ',' + ids.join(',') + ',';
			for (var i=0; i<data.length; i++){
				if (ids.indexOf(','+data[i].parentId+',') == -1){
					if ((','+rootIds.join(',')+',').indexOf(','+data[i].parentId+',') == -1){
						rootIds.push(data[i].parentId);
					}
				}
			}
			for (var i=0; i<rootIds.length; i++){
				addRow("#treeTableList", tpl, data, rootIds[i], true);
			}
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
						blank123:0}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/wx/wxWechatMenu/">微信服务号菜单列表</a></li>
		<shiro:hasPermission name="wx:wxWechatMenu:edit"><li><a href="${ctx}/wx/wxWechatMenu/form">微信服务号菜单添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxWechatMenu" action="${ctx}/wx/wxWechatMenu/" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>服务号ID：</label>
				<form:input path="serviceId" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>显示名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
			<shiro:hasPermission name="wx:core:createMenu">
				<li class="btns"><a href="${ctx}/wx/core/createMenu"><input id="createMenuSubmit" class="btn btn-primary" value="创建微信菜单"/></a></li>
			</shiro:hasPermission>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>显示名称</th>
				<th>服务号ID</th>
				<th>菜单key</th>
				<th>类型</th>
				<th>更新时间</th>
				<shiro:hasPermission name="wx:wxWechatMenu:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/wx/wxWechatMenu/form?id={{row.id}}">
				{{row.name}}
			</a></td>
			<td>
				{{row.serviceId}}
			</td>
			<td>
				{{row.menuKey}}
			</td>
			<td>
				{{row.type}}
			</td>
			<td>
				{{row.updateDate}}
			</td>
			<shiro:hasPermission name="wx:wxWechatMenu:edit"><td>
   				<a href="${ctx}/wx/wxWechatMenu/form?id={{row.id}}">修改</a>
				<a href="${ctx}/wx/wxWechatMenu/delete?id={{row.id}}" onclick="return confirmx('确认要删除该微信服务号菜单及所有子微信服务号菜单吗？', this.href)">删除</a>
				<a href="${ctx}/wx/wxWechatMenu/form?parent.id={{row.id}}">添加下级微信服务号菜单</a> 
			</td></shiro:hasPermission>
		</tr>
	</script>
</body>
</html>