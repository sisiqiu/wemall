<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
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
		<li><a href="${ctx}/wemall/wemallOrder/">订单列表</a></li>
		<li class="active"><a href="${ctx}/wemall/wemallOrder/form?orderNo=${wemallOrder.orderNo}">订单<shiro:hasPermission name="wemall:wemallOrder:edit">${not empty wemallOrder.orderNo?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wemall:wemallOrder:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="wemallOrder" action="${ctx}/wemall/wemallOrder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<c:if test="${not empty wemallOrder.orderNo}">
			<div class="control-group">
				<label class="control-label">订单号：</label>
				<div class="controls">
					<form:input path="orderNo" htmlEscape="false" maxlength="64" readonly="readonly" class="input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
		</c:if>
		<div class="control-group">
			<label class="control-label">用户id：</label>
			<div class="controls">
				<sys:treeselect id="user" name="user.id" value="${wemallOrder.user.id}" labelName="user.name" labelValue="${wemallOrder.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="required" allowClear="true" notAllowSelectParent="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付平台订单号：</label>
			<div class="controls">
				<form:input path="platformOrderNo" htmlEscape="false" maxlength="64" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">原始订单金额：</label>
			<div class="controls">
				<form:input path="orderPrice" value= "${allData['wemallOrder'].originalOrderPrice}" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">订单金额：</label>
			<div class="controls">
				<form:input path="orderPrice" value= "${allData['wemallOrder'].orderPrice}" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">实付金额：</label>
			<div class="controls">
				<form:input path="payment" value= "${allData['wemallOrder'].payment}" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付类型：</label>
			<div class="controls">
				<form:select path="paymentType" class="input-xlarge required">
					<form:option value="微信支付" label=""/>
					<form:options items="${fns:getDictList('payment_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">总运费：</label>
			<div class="controls">
				<form:input path="freightPrice" value= "${allData['wemallOrder'].freightPrice}" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">总退款金额：</label>
			<div class="controls">
				<form:input path="totalRefundFee" value= "${allData['wemallOrder'].totalRefundFee}" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">订单名称：</label>
			<div class="controls">
				<form:input path="title" value= "${allData['wemallOrder'].totalRefundFee}" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">订单描述：</label>
			<div class="controls">
				<form:textarea path="body" value= "${allData['wemallOrder'].totalRefundFee}" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge required"/>
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
			<label class="control-label">付款时间：</label>
			<div class="controls">
				<input name="paymentDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${allData['wemallOrder'].paymentDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">物流公司：</label>
			<div class="controls">
				<input name="freightName" type="text" readonly="readonly" maxlength="50" class="input-xlarge required" value="${allData['wemallOrder'].freightName}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">物流单号：</label>
			<div class="controls">
				<input name="freightNo" type="text" readonly="readonly" maxlength="50" class="input-xlarge required" value="${allData['wemallOrder'].freightNo}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发货时间：</label>
			<div class="controls">
				<input name="consignDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${allData['wemallOrder'].consignDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易完成时间：</label>
			<div class="controls">
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${allData['wemallOrder'].endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易关闭时间：</label>
			<div class="controls">
				<input name="closeDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${allData['wemallOrder'].closeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">类别：</label>
			<div class="controls">
				<form:select path="type" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">使用积分数：</label>
			<div class="controls">
				<form:input path="scoreUsageNum" value="${allData['wemallOrder'].scoreUsageNum}" htmlEscape="false" maxlength="11" class="input-xlarge digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">使用奖励金数：</label>
			<div class="controls">
				<form:input path="bountyUsageNum" value="${allData['wemallOrder'].bountyUsageNum}" htmlEscape="false" maxlength="11" class="input-xlarge digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">使用优惠券数：</label>
			<div class="controls">
				<form:input path="couponUsageNum" value="${allData['wemallOrder'].couponUsageNum}" htmlEscape="false" maxlength="11" class="input-xlarge digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">使用会员卡id：</label>
			<div class="controls">
				<form:input path="vipCardId" value="${allData['wemallOrder'].vipCardId}" htmlEscape="false" maxlength="6" class="input-xlarge digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品参与活动类别：</label>
			<div class="controls">
				<form:select path="activityType" class="input-xlarge">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('item_activity_sort')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品参与活动id：</label>
			<div class="controls">
				<form:input path="activityId" value="${allData['wemallOrder'].activityId}" htmlEscape="false" maxlength="6" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否申请退货：</label>
			<div class="controls">
				<form:radiobuttons path="applyForReject" value="${allData['wemallOrder'].applyForReject}" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申请退货时间：</label>
			<div class="controls">
				<input name="rejectDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${allData['wemallOrder'].rejectDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">买家留言：</label>
			<div class="controls">
				<form:textarea path="buyerMessage" value="${allData['wemallOrder'].buyerMessage}" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" value="${allData['wemallOrder'].remarks}" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			<a id="btn_detail" target="_blank" class="btn btn-primary" type="button" href="${ctx}/wemall/wemallOrder/orderQuery?orderNo=${allData['wemallOrder'].platformOrderNo}&paymentType=0" >查看第三方订单详情</a>
		</div>
	</form:form>
	<div>
		<h3>订单商品详情</h3>
		<table id="firstTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>商品名称</th>
				<th>商品规格</th>
				<th>购买数量</th>
				<th>总价格</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${allData.orderItemList}" var="wemallItem">
			<tr>
				<td itemId="${wemallItem.id}">
					<img  src="${wemallItem.photo}" width="40px" height="40px">${wemallItem.title}
				</td>
				<td>
					${wemallItem.itemsData}
				</td>
				<td>
					${wemallItem.itemNum}
				</td>
				<td>
					${wemallItem.totalFee}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
	<div>
		<h3>订单收货地址详情</h3>
		<table>
			<tr>
				订单收货地址:${allData.wemallOrderAddress.receiverProvince}${allData.wemallOrderAddress.receiverCity}${allData.wemallOrderAddress.receiverDistrict}${allData.wemallOrderAddress.receiverAddress}
			</tr></br></br>
			<tr>
				收货人:${allData.wemallOrderAddress.receiverName}
			</tr></br></br>
			<tr>
				联系电话:${allData.wemallOrderAddress.receiverMobile}
			</tr>
		</table>
	</div>
</body>
</html>