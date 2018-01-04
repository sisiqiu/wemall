<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户活动表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			if($("#user_statu").val()=="1"){
				$("#user_statu").val("已报名");
			}
			if($("#user_statu").val()=="2"){
				$("#user_statu").val("已签到");
			}
			if($("#user_statu").val()=="3"){
				$("#user_statu").val("已抽奖");
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/wx/conUserActivity/">用户活动表列表</a></li>
		<li class="active"><a href="${ctx}/wx/conUserActivity/form?id=${conUserActivity.id}">用户活动表<shiro:hasPermission name="wx:conUserActivity:edit">${not empty conUserActivity.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wx:conUserActivity:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="conUserActivity" action="${ctx}/wx/conUserActivity/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">用户ID：</label>
			<div class="controls">
				<form:input readOnly="true" path="user.name" htmlEscape="false" maxlength="100" class="input-xlarge "/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户名称：</label>
			<div class="controls">
				<form:input readOnly="true" path="userName" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">活动ID：</label>
			<div class="controls">
				<form:input readOnly="true" path="activityid" htmlEscape="false" maxlength="8" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系方式：</label>
			<div class="controls">
				<form:input readOnly="true" path="mobile" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">参会信息：</label>
			<div class="controls">
				<form:input readOnly="true" path="information" htmlEscape="false" maxlength="200" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:input readOnly="true" path="note" htmlEscape="false" maxlength="200" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户状态：</label>
			<div class="controls">
				<form:input id="user_statu" readOnly="true" path="status" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">奖项：</label>
			<div class="controls">
				<form:input readOnly="true" path="price" htmlEscape="false" maxlength="40" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">报名时间：</label>
			<div class="controls">
				<input readOnly="true" name="registrationTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conUserActivity.registrationTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">签到时间：</label>
			<div class="controls">
				<input readOnly="true"  name="attendanceTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conUserActivity.attendanceTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">抽奖时间：</label>
			<div class="controls">
				<input readOnly="true"  name="lotteryTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conUserActivity.lotteryTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="wx:conUserActivity:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>