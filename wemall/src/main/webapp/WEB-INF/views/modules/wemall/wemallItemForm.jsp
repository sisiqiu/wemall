<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			if($("#salesNum").val() == "") {
				$("#salesNum").val("0");
			}
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
			initWemallSpecSelect();
		});
		
		var trObj;
		var classNameArr = [{"className":"id","required":false},
		                    {"className":"specName","required":true},
		                    {"className":"specInfoName","required":true},
		                    {"className":"sort","required":true},
		                    {"className":"price","required":true},
		                    {"className":"teamPrice","required":false},
		                    {"className":"storage","required":true},
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
			console.log($("#specInfoStr").val());
			$("#inputForm").submit();
		}
		
		
		
		function initWemallSpecSelect() {
			$.ajax({
	             type: "POST",
	             url: "${ctx}/wemall/wemallSpec/wemallSpecList",
	             data: {},
	             dataType: "json",
	             success: function(data){
					$.each(data, function(index, item){
					    var html = "<option value=\"" + item.id + "\" label=\"" + item.name + "\">" + item.name + "</option>";
					 	$('#wemallSpecSelect').append(html);
					});
				}
			});
		}
		
		function changeWemallSpecSelect() {
			var specId = $("#wemallSpecSelect").val();
			if(specId == "") return;
			$.ajax({
	             type: "POST",
	             url: "${ctx}/wemall/wemallSpecInfo/wemallSpecInfoList",
	             data: {"specId":specId},
	             dataType: "json",
	             success: function(data){
					$.each(data, function(index, item){
						$.each(classNameArr, function(objIndex, objItem) {
							if(objItem.className == "specInfoName") {
								eval("$(\"." + objItem.className + "\").last().val(item.name);");
							} else {
								eval("$(\"." + objItem.className + "\").last().val(item." + objItem.className + ");");
							}
						});
						$("#specInfoTable tbody").append(trObj);
					});
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/wemall/wemallItem/">商品列表</a></li>
		<li class="active"><a href="${ctx}/wemall/wemallItem/form?id=${wemallItem.id}">商品<shiro:hasPermission name="wemall:wemallItem:edit">${not empty wemallItem.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wemall:wemallItem:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="wemallItem" action="${ctx}/wemall/wemallItem/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">商品名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品类别id：</label>
			<div class="controls">
				<sys:treeselect id="sortId" name="sortId" value="${wemallItem.sortId}" labelName="sortName" labelValue="${wemallItem.sortName}"
						title="商品类别" url="/wemall/wemallItemSort/treeData" extId="${wemallItem.sortId}" cssClass="required"/>
				<%-- <form:input path="sortId" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/> --%>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品原价：</label>
			<div class="controls">
				<form:input path="originalPrice" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品现价：</label>
			<div class="controls">
				<form:input path="currentPrice" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品库存：</label>
			<div class="controls">
				<form:input path="storage" htmlEscape="false" maxlength="9" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">缩略图：</label>
			<div class="controls">
				<input type="hidden" id="photo" name="photo" value="${wemallItem.photo}" />
				<sys:ckfinder input="photo" type="thumb" uploadPath="/wemall" selectMultiple="false"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序序号：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="9" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否置顶：</label>
			<div class="controls">
				<form:radiobuttons path="isTop" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否新品：</label>
			<div class="controls">
				<form:radiobuttons path="isNew" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否上架：</label>
			<div class="controls">
				<form:radiobuttons path="isOnShelf" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">销量：</label>
			<div class="controls">
				<form:input path="salesNum" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> 初始默认为0</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产地：</label>
			<div class="controls">
				<form:input path="productPlace" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否可用奖励金：</label>
			<div class="controls">
				<form:radiobuttons path="canUseBounty" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否可用优惠券：</label>
			<div class="controls">
				<form:radiobuttons path="canUseCoupon" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否可用积分抵扣：</label>
			<div class="controls">
				<form:radiobuttons path="canUseScoreDeduct" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">积分最大抵扣金额：</label>
			<div class="controls">
				<form:input path="scoreDeductPrice" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否可用积分兑换：</label>
			<div class="controls">
				<form:radiobuttons path="canUseScoreExchange" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否支持下单减库存：</label>
			<div class="controls">
				<form:radiobuttons path="subStock" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否免邮：</label>
			<div class="controls">
				<form:radiobuttons path="freightFree" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">运费：</label>
			<div class="controls">
				<form:input path="freightPrice" htmlEscape="false" maxlength="9" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">参与的活动：</label>
			<div class="controls">
				<form:select path="activitySort" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('item_activity_sort')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">活动id：</label>
			<div class="controls">
				<form:input path="activityId" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品描述：</label>
			<div class="controls">
				<form:textarea path="desc" htmlEscape="false" rows="4" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片列表：</label>
			<div class="controls">
				<form:hidden id="photoUrls" path="photoUrls" htmlEscape="false" maxlength="2000" class="input-xlarge"/>
				<sys:ckfinder input="photoUrls" type="images" uploadPath="/wemall" selectMultiple="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品属性值列表：</label>
			<div class="controls">
				选择属性类别添加对应属性值列表到下列表单：
				<select id="wemallSpecSelect" class="input-medium" onchange="changeWemallSpecSelect()">
					<option value="" label=""/>
				</select>
				<table id="specInfoTable" class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<!-- <th>属性值id</th> -->
							<th>属性类别名称</th>
							<th>属性值名称</th>
							<th>排序</th>
							<th>价格</th>
							<th>拼团价</th>
							<th>库存量</th>
							<th>操作<a style="cursor:pointer;" onclick="addOne(this)">  向下添加</a></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<input type="hidden" class="input-medium valid id" type="text" >
							<td><input class="input-small valid specName" type="text" ></td>
							<td><input class="input-small valid specInfoName" type="text" ></td>
							<td><input class="valid sort" style="width:50px;" type="text" ></td>
							<td><input class="input-mini valid price" type="text" ></td>
							<td><input class="input-mini valid teamPrice" type="text" ></td>
							<td><input class="input-mini valid storage" type="text" ></td>
							<td>
								<a style="cursor:pointer;" onclick="insertOne(this)">向上插入</a>
								<a style="cursor:pointer;" onclick="deleteOne(this)">删除</a>
							</td>
						</tr>
					</tbody>
				</table>
			
				<textarea id="specInfoStr" name="specInfoStr" rows="4" style="display:none" class="input-xxlarge">${wemallItem.specInfoStr}</textarea>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="wemall:wemallItem:edit"><input id="btnSubmit" class="btn btn-primary" type="button" onclick="save()" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>