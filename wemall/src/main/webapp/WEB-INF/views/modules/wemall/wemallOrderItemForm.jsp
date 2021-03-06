<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单-商品信息管理</title>
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
		<li><a href="${ctx}/wemall/wemallOrderItem/">订单-商品信息列表</a></li>
		<li class="active"><a href="${ctx}/wemall/wemallOrderItem/form?id=${wemallOrderItem.id}">订单-商品信息<shiro:hasPermission name="wemall:wemallOrderItem:edit">${not empty wemallOrderItem.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wemall:wemallOrderItem:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="wemallOrderItem" action="${ctx}/wemall/wemallOrderItem/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">订单号：</label>
			<div class="controls">
				<form:input path="orderNo" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品id：</label>
			<div class="controls">
				<form:input path="itemId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户id：</label>
			<div class="controls">
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品数量：</label>
			<div class="controls">
				<form:input path="itemNum" htmlEscape="false" maxlength="6" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品标题：</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品缩略图：</label>
			<div class="controls">
				<input type="hidden" id="photo" name="photo" value="${wemallOrderItem.photo}" />
				<sys:ckfinder input="photo" type="thumb" uploadPath="/wemall" selectMultiple="false"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品总金额：</label>
			<div class="controls">
				<form:input path="totalFee" value="￥${wemallOrderItem.totalFee/100}" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品规格说明：</label>
			<div class="controls">
				<form:textarea path="itemsData" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">物流名称：</label>
			<div class="controls">
				<form:input path="freightName" htmlEscape="false" maxlength="50" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">物流单号：</label>
			<div class="controls">
				<form:input path="freightNo" htmlEscape="false" maxlength="64" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">买家留言：</label>
			<div class="controls">
				<form:input path="buyerMessage" htmlEscape="false" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">买家昵称：</label>
			<div class="controls">
				<form:input path="buyerNick" htmlEscape="false" maxlength="50" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">买家头像：</label>
			<div class="controls">
				<input type="hidden" id="buyerPhoto" name="buyerPhoto" value="${wemallOrderItem.buyerPhoto}" />
				<sys:ckfinder input="buyerPhoto" type="thumb" uploadPath="/wemall" selectMultiple="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">买家评分：</label>
			<div class="controls">
				<form:input path="buyerScore" htmlEscape="false" maxlength="1" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">买家是否已评价：</label>
			<div class="controls">
				<form:radiobuttons path="buyerComment" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">评价时间：</label>
			<div class="controls">
				<input name="commentTime" type="text" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${wemallOrderItem.commentTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="form-actions">
			<%-- <shiro:hasPermission name="wemall:wemallOrderItem:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission> --%>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>