<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>属性类别管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/wemall/wemallSpec/">属性类别列表</a></li>
		<li class="active"><a href="${ctx}/wemall/wemallSpec/form?id=${wemallSpec.id}">属性类别<shiro:hasPermission name="wemall:wemallSpec:edit">${not empty wemallSpec.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wemall:wemallSpec:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="wemallSpec" action="${ctx}/wemall/wemallSpec/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">属性类别名：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="6" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>属性值id</th>
					<th>属性类别id</th>
					<th>属性名称</th>
					<th>排序</th>
					<th>更新时间</th>
					<shiro:hasPermission name="wemall:wemallSpecInfo:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${specInfoList}" var="wemallSpecInfo">
				<tr>
					<td><a href="${ctx}/wemall/wemallSpecInfo/form?id=${wemallSpecInfo.id}">
						${wemallSpecInfo.id}
					</a></td>
					<td>
						${wemallSpecInfo.specName}
					</td>
					<td>
						${wemallSpecInfo.name}
					</td>
					<td>
						${wemallSpecInfo.sort}
					</td>
					<td>
						<fmt:formatDate value="${wemallSpecInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<shiro:hasPermission name="wemall:wemallSpecInfo:edit"><td>
	    				<a href="${ctx}/wemall/wemallSpecInfo/form?id=${wemallSpecInfo.id}">修改</a>
						<a href="${ctx}/wemall/wemallSpecInfo/delete?id=${wemallSpecInfo.id}" onclick="return confirmx('确认要删除该属性值信息吗？', this.href)">删除</a>
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
		<div class="form-actions">
			<shiro:hasPermission name="wemall:wemallSpec:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>