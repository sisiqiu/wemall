<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>极光推送记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			initMapDataByHtml("role");
			initMapDataByHtml("tag");
			initMapDataByHtml("user");
			initSelectUserList();
			//$("#name").focus();
			$("#type").change(function() {
				var type = $("#type").val();
				if(type == "1") $("#scheduleTimeDiv").show();
				else $("#scheduleTimeDiv").hide();
			});
			$("#targetType").change(function() {
				var targetType = $("#targetType").val();
				if(targetType == "1") {
					//推送所有人
					$(".byTargetTypeDiv").hide();
				} else if(targetType == "2") {
					//通过角色推送
					$(".byTargetTypeDiv").hide();
					$("#roleNamesDiv").show();
				} else if(targetType == "3") {
					//通过标签推送
					$(".byTargetTypeDiv").hide();
					$("#tagNamesDiv").show();
				} else if(targetType == "4") {
					//通过用户列表推送
					$(".byTargetTypeDiv").hide();
					$("#userNamesDiv").show();
				}
			});
			
			$("#roleNamesSelect").change(function() {
				var value = $("#roleNamesSelect").val();//对应id
				var text = $("#roleNamesSelect").children("option:selected").text();//对应name
				if(typeof(roleMap[value]) == "undefined" || roleMap[value]=="") {
					roleMap[value] = text;
					appendTo("role", text, value);
				}
			});
			$("#tagNamesSelect").change(function() {
				var value = $("#tagNamesSelect").val();//对应id
				var text = $("#tagNamesSelect").children("option:selected").text();//对应name
				if(typeof(tagMap[value]) == "undefined" || tagMap[value]=="") {
					tagMap[value] = text;
					appendTo("tag", text, value);
				}
			});
			/* $("#userNamesSelect").change(function() {
				var value = $("#userNamesSelect").val();//对应id
				var text = $("#userNamesSelect").children("option:selected").text();//对应name
				if(typeof(userMap[value]) == "undefined" || userMap[value]=="") {
					userMap[value] = text;
					appendTo("user", text, value);
				}
			}); */
			//初始触发
			$("#type").change();
			$("#targetType").change();
			
			$("#inputForm").validate({
				submitHandler: function(form){
					//推送时间验证部分
					var targetType = $("#targetType").val();
					var type = $("#type").val();
					if(type == "1" && $("#scheduleTime").val() == "") {
						top.$.jBox.tip("推送为定时推送类型时，定时推送时间不能为空！");
						return;
					}
					if(type == "0") $("#scheduleTime").val("");
					
					//推送目标验证以及格式化部分
					var eleStr = "";
					var tipStr = "";
					if(targetType == "1") {
						//推送所有人
						$(".byTargetType").val("");
						loading('正在提交，请稍等...');
						form.submit();
						return;
					} else if(targetType == "2") {
						//通过角色推送
						eleStr = "role";
					} else if(targetType == "3") {
						//通过标签推送
						eleStr = "tag";
					} else if(targetType == "4") {
						//通过用户列表推送
						eleStr = "user";
					}
					
					//从全局变量中取选择的数据。赋值input中
					var ids = "";
					var names = "";
					if(eleStr != "user") {
						eval("var dataMap=" + eleStr + "Map");
						for(var id in dataMap) {
							ids = ids + id + ",";
							names = names + dataMap[id] + ",";
						}
						if(ids.length != 0) ids = ids.substring(0, ids.length-1);
						if(names.length != 0) names = names.substring(0, names.length-1);
					} else {
						ids = $("#userIds").val();
						names = $("#userNames").val();
					}
					
					if(ids == "" || names == "") {
						if(targetType == "2") top.$.jBox.tip("推送目标为通过角色推送时，角色名称列表不能为空！");
						else if(targetType == "3") top.$.jBox.tip("推送目标为通过标签推送时，标签名称列表不能为空！");
						else if(targetType == "4") top.$.jBox.tip("推送目标为通过用户列表推送时，用户名称列表不能为空！");
						return;
					}
					//$(".byTargetType").val("");
					
					$("input[name='" + eleStr + "Ids']").val(ids);
					$("input[name='" + eleStr + "Names']").val(names);
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
		
		function del(obj, type) {
			var value = $(obj).prev().attr("value");
			if(value == $("#" + type + "NamesSelect").val()) {
				$("#" + type + "NamesSelect").val("");
			}
			if(type == "role") delete roleMap[value];
			if(type == "tag") delete tagMap[value];
			if(type == "user") delete userMap[value];
			$(obj).parent().remove();
		}
		
		var roleMap = {};
		var tagMap = {};
		var userMap = {};
		
		function appendTo(type, text, value) {
			var obj;
			if(type == "role") obj = $("#roleNames");
			if(type == "tag") obj = $("#tagNames");
			if(type == "user") obj = $("#userNames");
			obj.append("<li><a value=\"" + value + "\">" + text + "</a>&nbsp;&nbsp;<a onclick=\"del(this,'" + type + "');\">×</a></li>");
		}
		
		/*
		*	初始化数据
		*/
		function initMapDataByHtml(type) {
			var lis = $("#" + type + "Names").children("li");
			$.each(lis, function(index, item) {
				var aObj = $(item).children("a:first");
				var id = aObj.attr("value");
				var name = aObj.text();
				if(type == "role") {
					roleMap[id] = name;
				} else if(type == "tag") {
					tagMap[id] = name;
				} else if(type == "user") {
					userMap[id] = name;
				}
			});
		}
		
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
		<li><a href="${ctx}/cms/cmsJpushRecord/">极光推送历史记录列表</a></li>
		<li class="active"><a href="${ctx}/cms/cmsJpushRecord/form?id=${cmsJpushRecord.id}">极光推送记录<shiro:hasPermission name="cms:cmsJpushRecord:edit">${not empty cmsJpushRecord.id?'查看':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:cmsJpushRecord:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsJpushRecord" action="${ctx}/cms/cmsJpushRecord/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">推送类型：</label>
			<div class="controls">
				<form:select path="type" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cms_push_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group" id="scheduleTimeDiv" style="display:none;">
			<label class="control-label">定时推送时间：</label>
			<div class="controls">
				<input id="scheduleTime" name="scheduleTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${cmsJpushRecord.scheduleTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推送目标类型：</label>
			<div class="controls">
				<form:select path="targetType" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('cms_push_targetType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group byTargetTypeDiv" id="roleNamesDiv" style="display:none;">
			<label class="control-label">角色名称列表：</label>
			<div class="controls">
				<input type="hidden" name="roleIds" />
				<input type="hidden" name="roleNames" />
				<ol id="roleNames">
					<c:forEach items="${curRoleList}" var="role">
						<li><a value="${role.id}">${role.name}</a>&nbsp;&nbsp;<a onclick="del(this,'role');">×</a></li>
					</c:forEach>
				</ol>
				<select id="roleNamesSelect" class="input-medium">
					<option value="" ></option>
					<c:forEach items="${roleList}" var="role">
						<option value="${role.id}" >${role.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group byTargetTypeDiv" id="tagNamesDiv" style="display:none;">
			<label class="control-label">标签名称列表：</label>
			<div class="controls">
				<input type="hidden" name="tagIds" />
				<input type="hidden" name="tagNames" />
				<ol id="tagNames">
					<c:forEach items="${curTagList}" var="tag">
						<li><a value="${tag.id}">${tag.name}</a>&nbsp;&nbsp;<a onclick="del(this,'tag');">×</a></li>
					</c:forEach>
				</ol>
				<select id="tagNamesSelect" class="input-medium">
					<option value="" ></option>
					<c:forEach items="${tagList}" var="tag">
						<option value="${tag.id}" >${tag.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group byTargetTypeDiv" id="userNamesDiv" style="display:none;">
			<label class="control-label">用户名称列表：</label>
			<div class="controls">
				<form:hidden path="userIds" />
				<form:textarea path="userNames" htmlEscape="false" readonly="true" class="input-xlarge byTargetType"/>
				<a id="selectUserList" class="btn btn-primary" type="submit">选择用户</a>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推送标题：</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推送内容：</label>
			<div class="controls">
				<form:textarea path="content" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接地址：</label>
			<div class="controls">
				<form:input path="href" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">附加信息：</label>
			<div class="controls">
				<form:textarea path="extra" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推送状态：</label>
			<div class="controls">
				<form:radiobuttons path="status" items="${fns:getDictList('successOrFail')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<c:if test="${empty cmsJpushRecord.id}">
				<shiro:hasPermission name="cms:cmsJpushRecord:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>