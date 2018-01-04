<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>标签管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			initSelectUserList();
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
		
		function initSelectUserList() {
			var userIds = $("#userIds").val();
			$("#selectUserList").click(function(){
				top.$.jBox.open("iframe:${ctx}/sys/user/selectUserList?userIds=" + userIds, "选择用户",810,$(top.document).height()-240,{
					buttons:{"确定选择":"ok", "清除已选":"clear", "关闭":true}, bottomText:"通过选择部门，然后可选中列出的人员到已选列表。",submit:function(v, h, f){
						var pre_ids = h.find("iframe")[0].contentWindow.pre_ids;
						var ids = h.find("iframe")[0].contentWindow.ids;
						var names = h.find("iframe")[0].contentWindow.names;
						//nodes = selectedTree.getSelectedNodes();
						if (v=="ok"){
							// 删除''的元素
							if(ids[0]==''){
								ids.shift();
								pre_ids.shift();
								names.shift();
							}
					    	// 执行保存
					    	//loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	var namesArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	for (var i = 0; i<names.length; i++) {
					    		namesArr = (namesArr + names[i]) + (((i + 1)== names.length) ? '':',');
					    	}
					    	//为userIds和userNames赋值
					    	$('#userIds').val(idsArr);
					    	$('#userNames').val(namesArr);
					    	return true;
						} else if (v=="clear"){
							h.find("iframe")[0].contentWindow.clearAssign();
							return false;
		                }
					}, loaded:function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/cmsPushTag/">标签列表</a></li>
		<li class="active"><a href="${ctx}/cms/cmsPushTag/form?id=${cmsPushTag.id}">标签<shiro:hasPermission name="cms:cmsPushTag:edit">${not empty cmsPushTag.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:cmsPushTag:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsPushTag" action="${ctx}/cms/cmsPushTag/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">标签名：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:radiobuttons path="status" items="${fns:getDictList('sl_his_regPoolStatus')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">用户名称列表：</label>
			<div class="controls">
				<form:hidden path="userIds" />
				<form:textarea path="userNames" htmlEscape="false" readonly="true" class="input-xlarge"/>
				<a id="selectUserList" class="btn btn-primary" type="submit">选择用户</a>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="cms:cmsPushTag:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>