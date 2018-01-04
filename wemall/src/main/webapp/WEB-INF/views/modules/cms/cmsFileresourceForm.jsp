<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上传文件信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			if($("#downloadNum").val() == "") {
				$("#downloadNum").val(0);
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
			
			/*
			*	复写打开ckfinder文件管理器的方法，设定默认文件夹为对应的模块和类别所属文件夹。并使用公共文件库。
			*/
			filePathFinderOpen = function () {
				var ctype = "commonFiles";
				var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
				var uploadPath = "/" + $("#module").val() + "/" + $("#category").val();
				var input = "filePath";
				var selectMultiple = "false";
				
				var dts = (ctype == 'thumb')?'1':'0';
				var sm = (selectMultiple == "true") ?1:0;
				var date = new Date(), year = date.getFullYear(), month = (date.getMonth()+1)>9?date.getMonth()+1:"0"+(date.getMonth()+1);
				var url = "${ctxStatic}/ckfinder/ckfinder.html?viewCommonFile=1&type=" + ctype + "&start=" + ctype + ":" + uploadPath + "/"+year+"/"+month+
					"/&action=js&func=" + input + "SelectAction&thumbFunc=" + input + "ThumbSelectAction&cb=" + input + "Callback&dts=" + dts + "&sm=" + sm;
				windowOpen(url,"文件管理",1000,700);
				//top.$.jBox("iframe:"+url+"&pwMf=1", {title: "文件管理", width: 1000, height: 500, buttons:{'关闭': true}});
			};
			
			$("#downloadFile").click(function () {
				var filePath = $("#filePath").val();
				window.open("${ctxFront}" + "/download?filePath=" + filePath);
			});
		});
		
		//ckfinder的回调
		function changefilePath (fileData) {
			var fileUrl = decodeURI(fileData.fileUrl);
			var fileUrlArr = fileUrl.split("/");
			var fileName = fileUrlArr[fileUrlArr.length-1];
			$("#name").val(fileName);
			$("#suffix").val(fileName.substring(fileName.indexOf(".") + 1));
			$("#size").val(fileData.fileSize);
			$("#filePath").val(fileUrl);
		};
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/cmsFileresource/">上传文件信息列表</a></li>
		<li class="active"><a href="${ctx}/cms/cmsFileresource/form?id=${cmsFileresource.id}">上传文件信息<shiro:hasPermission name="cms:cmsFileresource:edit">${not empty cmsFileresource.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:cmsFileresource:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsFileresource" action="${ctx}/cms/cmsFileresource/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">所属模块：</label>
			<div class="controls">
				<form:input path="module" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所属类别：</label>
			<div class="controls">
				<form:input path="category" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">文件地址：</label>
			<div class="controls">
                <input type="hidden" id="filePath" name="filePath" value="${cmsFileresource.filePath}" />
				<sys:ckfinder input="filePath" type="commonFiles" uploadPath="" selectMultiple="false"/>
				<a id="downloadFile" class="btn">下载</a>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
				<span class="help-inline">即文件名称，在文件选择完毕后自动赋值。</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">大小：</label>
			<div class="controls">
				<form:input path="size" readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
				<span class="help-inline">单位（KB），在文件选择完毕后自动赋值</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">格式：</label>
			<div class="controls">
				<form:input path="suffix" readonly="true" htmlEscape="false" maxlength="10" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
				<span class="help-inline">即文件后缀名，在文件选择完毕后自动赋值。</span>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">文件地址：</label>
			<div class="controls">
				<form:input path="filePath" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">下载数：</label>
			<div class="controls">
				<form:input path="downloadNum" htmlEscape="false" maxlength="11" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态值：</label>
			<div class="controls">
				<form:radiobuttons path="status" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="cms:cmsFileresource:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>