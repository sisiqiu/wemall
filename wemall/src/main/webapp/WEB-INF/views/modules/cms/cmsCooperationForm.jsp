<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>客户申请合作管理管理</title>
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
		<li><a href="${ctx}/cms/cmsCooperation/">客户申请合作管理列表</a></li>
		<li class="active"><a href="${ctx}/cms/cmsCooperation/form?id=${cmsCooperation.id}">客户申请合作管理查看<%-- <shiro:hasPermission name="cms:cmsCooperation:edit">${not empty cmsCooperation.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:cmsCooperation:edit">查看</shiro:lacksPermission> --%></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsCooperation" action="${ctx}/cms/cmsCooperation/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">外键用户id：</label>
			<div class="controls">
				<sys:treeselect id="user" name="user.id" value="${cmsCooperation.user.id}" labelName="user.name" labelValue="${cmsCooperation.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公司名称：</label>
			<div class="controls">
				<form:input path="companyName" htmlEscape="false" maxlength="200" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合作类型：</label>
			<div class="controls">
				<form:input path="cooperationType" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申请经销的产品：</label>
			<div class="controls">
				<form:input path="product" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申请经销的行业：</label>
			<div class="controls">
				<form:input path="trade" htmlEscape="false" maxlength="200" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申请经销的区域：</label>
			<div class="controls">
				<form:input path="area" htmlEscape="false" maxlength="200" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公司地址：</label>
			<div class="controls">
				<form:input path="addrees" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公司电话：</label>
			<div class="controls">
				<form:input path="telephone" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公司传真：</label>
			<div class="controls">
				<form:input path="fax" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公司邮编：</label>
			<div class="controls">
				<form:input path="postcode" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公司网址：</label>
			<div class="controls">
				<form:input path="companyUrl" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">负责人：</label>
			<div class="controls">
				<form:input path="principal" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">负责人电话：</label>
			<div class="controls">
				<form:input path="principalTel" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">负责人邮箱：</label>
			<div class="controls">
				<form:input path="principalEmail" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合作负责人：</label>
			<div class="controls">
				<form:input path="coopPrincipal" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合作负责人电话：</label>
			<div class="controls">
				<form:input path="coopPrincipalTel" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合作负责人邮箱：</label>
			<div class="controls">
				<form:input path="coopPrincipalEmail" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">销售人员数量：</label>
			<div class="controls">
				<form:input path="sellPeoples" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">技术人员数量：</label>
			<div class="controls">
				<form:input path="technologyPeoples" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">认证工程师数量：</label>
			<div class="controls">
				<form:input path="engineered" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">投入销售人员数量：</label>
			<div class="controls">
				<form:input path="putSellPeoples" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">投入技术人员数量：</label>
			<div class="controls">
				<form:input path="putTechnologyPeoples" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">销售负责人：</label>
			<div class="controls">
				<form:input path="sellPrincipal" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">销售负责人电话：</label>
			<div class="controls">
				<form:input path="sellPrincipalTel" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">销售负责人邮箱：</label>
			<div class="controls">
				<form:input path="sellPrincipalEmail" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">技术负责人：</label>
			<div class="controls">
				<form:input path="technologyPrincipal" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">技术负责人电话：</label>
			<div class="controls">
				<form:input path="technologyPrincipalTel" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">技术负责人邮箱：</label>
			<div class="controls">
				<form:input path="technologyPrincipalEmail" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">防病毒经验：1有；2无：</label>
			<div class="controls">
				<form:input path="preVirusExperience" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">经销过哪家的防病毒产品：</label>
			<div class="controls">
				<form:textarea path="preVirusProduct" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">经效过哪家的防黑客产品：</label>
			<div class="controls">
				<form:textarea path="preHackerProduct" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">网络安全产品的年销售额：</label>
			<div class="controls">
				<form:input path="annualSales" htmlEscape="false" maxlength="2000" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">除网络安全别的IT产品：</label>
			<div class="controls">
				<form:textarea path="otherItProduct" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">参加或者承担的项目：</label>
			<div class="controls">
				<form:textarea path="joinProjects" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">主要销售用户：</label>
			<div class="controls">
				<form:textarea path="mainSaleClient" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公司主要业务范围：</label>
			<div class="controls">
				<form:textarea path="businessScope" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年度销售计划：</label>
			<div class="controls">
				<form:textarea path="annualSalePlan" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">对山丽公司的想法和计划：</label>
			<div class="controls">
				<form:textarea path="ideaPlanTome" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<%-- <shiro:hasPermission name="cms:cmsCooperation:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission> --%>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>