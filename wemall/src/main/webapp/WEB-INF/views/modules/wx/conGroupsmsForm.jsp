<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>群发短信管理</title>
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
			changeSendType();
		});
		
		/*
		*	改变发送策略时触发
		*/
		function changeSendType() {
			//发送策略：0--即时发送；1--定时发送；
			if($("#sendTypeSelect").val() == "1") {
				$("#sendTimeDiv").show();
			} else {
				$("#sendTimeDiv").hide();
				$("#sendTime").val("");
			}
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/wx/conGroupsms/">群发短信列表</a></li>
		<li class="active"><a href="${ctx}/wx/conGroupsms/form?id=${conGroupsms.id}">群发短信<shiro:hasPermission name="wx:conGroupsms:edit">${not empty conGroupsms.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wx:conGroupsms:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="conGroupsms" action="${ctx}/wx/conGroupsms/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">短信模板ID：</label>
			<div class="controls">
				<form:input path="templeteId" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">短信内容数组：</label>
			<div class="controls">
				<form:textarea path="contentArr" htmlEscape="false" rows="4" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发送目标数组：</label>
			<div class="controls">
				<form:textarea path="mobileArr" htmlEscape="false" rows="4" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发送策略：</label>
			<div class="controls">
				<form:select id="sendTypeSelect" path="sendType" class="input-xlarge required" onchange="changeSendType(this)">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('con_groupSMS_sendType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group" id="sendTimeDiv">
			<label class="control-label">发送时间：</label>
			<div class="controls">
				<input id="sendTime" name="sendTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conGroupsms.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group" style="display:none">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('con_groupSMS_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="wx:conGroupsms:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>