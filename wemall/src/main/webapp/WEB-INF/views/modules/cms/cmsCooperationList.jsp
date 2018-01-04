<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>客户申请合作管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/cmsCooperation/">客户申请合作管理列表</a></li>
		<%-- <shiro:hasPermission name="cms:cmsCooperation:edit"><li><a href="${ctx}/cms/cmsCooperation/form">客户申请合作管理添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsCooperation" action="${ctx}/cms/cmsCooperation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键id</th>
				<th>系统用户</th>
				<th>公司名称</th>
				<th>合作类型</th>
				<th>申请经销的产品</th>
				<th>申请经销的行业</th>
				<th>申请经销的区域</th>
				<!-- <th>公司地址</th> -->
				<th>公司电话</th>
				<!-- <th>公司传真</th>
				<th>公司邮编</th>
				<th>公司网址</th> -->
				<th>负责人</th>
				<th>负责人电话</th>
				<!-- <th>负责人邮箱</th>
				<th>合作负责人</th>
				<th>合作负责人电话</th>
				<th>合作负责人邮箱</th>
				<th>销售人员数量</th>
				<th>技术人员数量</th>
				<th>认证工程师数量</th>
				<th>投入销售人员数量</th>
				<th>投入技术人员数量</th>
				<th>销售负责人</th>
				<th>销售负责人电话</th>
				<th>销售负责人邮箱</th>
				<th>技术负责人</th>
				<th>技术负责人电话</th>
				<th>技术负责人邮箱</th>
				<th>防病毒经验：1有；2无</th>
				<th>经销过哪家的防病毒产品</th>
				<th>经效过哪家的防黑客产品</th>
				<th>网络安全产品的年销售额</th>
				<th>除网络安全别的IT产品</th>
				<th>参加或者承担的项目</th>
				<th>主要销售用户</th>
				<th>公司主要业务范围</th>
				<th>年度销售计划</th>
				<th>对山丽公司的想法和计划</th>
				<th>创建人</th>
				<th>创建时间</th>
				<th>修改人</th>
				<th>修改时间</th>
				<th>状态</th> -->
				<shiro:hasPermission name="cms:cmsCooperation:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsCooperation">
			<tr>
				<td><a href="${ctx}/cms/cmsCooperation/form?id=${cmsCooperation.id}">
					${cmsCooperation.id}
				</a></td>
				<td>
					${cmsCooperation.user.name}
				</td>
				<td>
					${cmsCooperation.companyName}
				</td>
				<td>
					${cmsCooperation.cooperationType}
				</td>
				<td>
					${cmsCooperation.product}
				</td>
				<td>
					${cmsCooperation.trade}
				</td>
				<td>
					${cmsCooperation.area}
				</td>
				<%-- <td>
					${cmsCooperation.addrees}
				</td> --%>
				<td>
					${cmsCooperation.telephone}
				</td>
				<%-- <td>
					${cmsCooperation.fax}
				</td>
				<td>
					${cmsCooperation.postcode}
				</td>
				<td>
					${cmsCooperation.companyUrl}
				</td> --%>
				<td>
					${cmsCooperation.principal}
				</td>
				<td>
					${cmsCooperation.principalTel}
				</td>
				<%-- <td>
					${cmsCooperation.principalEmail}
				</td>
				<td>
					${cmsCooperation.coopPrincipal}
				</td>
				<td>
					${cmsCooperation.coopPrincipalTel}
				</td>
				<td>
					${cmsCooperation.coopPrincipalEmail}
				</td>
				<td>
					${cmsCooperation.sellPeoples}
				</td>
				<td>
					${cmsCooperation.technologyPeoples}
				</td>
				<td>
					${cmsCooperation.engineered}
				</td>
				<td>
					${cmsCooperation.putSellPeoples}
				</td>
				<td>
					${cmsCooperation.putTechnologyPeoples}
				</td>
				<td>
					${cmsCooperation.sellPrincipal}
				</td>
				<td>
					${cmsCooperation.sellPrincipalTel}
				</td>
				<td>
					${cmsCooperation.sellPrincipalEmail}
				</td>
				<td>
					${cmsCooperation.technologyPrincipal}
				</td>
				<td>
					${cmsCooperation.technologyPrincipalTel}
				</td>
				<td>
					${cmsCooperation.technologyPrincipalEmail}
				</td>
				<td>
					${cmsCooperation.preVirusExperience}
				</td>
				<td>
					${cmsCooperation.preVirusProduct}
				</td>
				<td>
					${cmsCooperation.preHackerProduct}
				</td>
				<td>
					${cmsCooperation.annualSales}
				</td>
				<td>
					${cmsCooperation.otherItProduct}
				</td>
				<td>
					${cmsCooperation.joinProjects}
				</td>
				<td>
					${cmsCooperation.mainSaleClient}
				</td>
				<td>
					${cmsCooperation.businessScope}
				</td>
				<td>
					${cmsCooperation.annualSalePlan}
				</td>
				<td>
					${cmsCooperation.ideaPlanTome}
				</td>
				<td>
					${cmsCooperation.createBy.name}
				</td>
				<td>
					<fmt:formatDate value="${cmsCooperation.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${cmsCooperation.updateBy.name}
				</td>
				<td>
					<fmt:formatDate value="${cmsCooperation.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(cmsCooperation.delFlag, 'del_flag', '')}
				</td> --%>
				<shiro:hasPermission name="cms:cmsCooperation:edit"><td>
    				<a href="${ctx}/cms/cmsCooperation/form?id=${cmsCooperation.id}">修改</a>
					<a href="${ctx}/cms/cmsCooperation/delete?id=${cmsCooperation.id}" onclick="return confirmx('确认要删除该客户申请合作管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>