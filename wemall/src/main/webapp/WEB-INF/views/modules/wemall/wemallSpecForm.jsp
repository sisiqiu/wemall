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
			
			trObj = $("#specInfoTable tbody tr").prop("outerHTML");
			init($("#specInfoStr").val());
		});
		
		var trObj;
		var classNameArr = [{"className":"id","required":false},
		                    {"className":"name","required":true},
		                    {"className":"sort","required":true}
								];
		
		/**
		*	向上插入
		*/
		function insertOne(obj) {
			$(obj).parent().parent().before(trObj);
		}
		
		/**
		*	向下添加
		*/
		function addOne(obj) {
			$("#specInfoTable tbody").append(trObj);
		}
		
		function deleteOne(obj) {
			$(obj).parent().parent().remove();
		}
		
		function init(priceJsonStr) {
			var priceJson;
			try {
				priceJson = JSON.parse(priceJsonStr);
			} catch(e) {
				return;
			}
			
			$.each(priceJson, function(index, item) {
				$.each(classNameArr, function(objIndex, objItem) {
					eval("$(\"." + objItem.className + "\").last().val(item." + objItem.className + ");");
				});
				$("#specInfoTable tbody").append(trObj);
			});
		}
		
		function formatResult() {
			var trArr = [];
			$.each($("#specInfoTable tbody tr"), function(index, item) {
				var newTrJson = {};
				var canPush = true;
				$.each(classNameArr, function(objIndex, objItem) {
					eval("newTrJson." + objItem.className + " = $(item).find(\"." + objItem.className + "\").val();");
					if(objItem.required && eval("newTrJson." + objItem.className) == "") {
						canPush = false;
					}
				});
				if(canPush) {
					trArr.push(newTrJson);
				}
			});
			$("#specInfoStr").val(JSON.stringify(trArr));
		}
		
		function save() {
			formatResult();
			$("#inputForm").submit();
		}
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
		<div class="control-group">
			<label class="control-label">属性值列表：</label>
			<div class="controls">
				<table id="specInfoTable" class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<!-- <th>属性值id</th> -->
							<th>属性名称</th>
							<th>排序</th>
							<th>操作<a style="cursor:pointer;" onclick="addOne(this)">  向下添加</a></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<input type="hidden" class="input-medium valid id" type="text" >
							<td><input class="input-medium valid name" type="text" ></td>
							<td><input class="input-medium valid sort" type="text" ></td>
							<td>
								<a style="cursor:pointer;" onclick="insertOne(this)">向上插入</a>
								<a style="cursor:pointer;" onclick="deleteOne(this)">删除</a>
							</td>
						</tr>
					</tbody>
				</table>
			
				<textarea id="specInfoStr" name="specInfoStr" rows="4" style="display:none" class="input-xxlarge">${wemallSpec.specInfoStr}</textarea>
			</div>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="wemall:wemallSpec:edit"><input id="btnSubmit" class="btn btn-primary" type="button" onclick="save()" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>