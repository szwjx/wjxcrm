<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";

	Map<String,String> pmap = (Map<String, String>) application.getAttribute("pmap");

	Set<String> set = pmap.keySet();
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

<script type="text/javascript">
	var json = {

		<%
            for (String key:set){
                String value = pmap.get(key);
        %>

		"<%=key%>" : <%=value%>,

		<%
            }

        %>
	};

	$(function () {

		//日期插件（日期拾取器）
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});
		$(".time1").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//自动补全客户名称
		$("#edit-customerName").typeahead({
			source: function (query, process) {
				$.post(
						"workbench/transaction/getCustomerName.do",
						{ "name" : query },
						function (data) {
							/*
                                data
                                    [{客户名称1}，{2}，{3}]
                             */
							process(data);
						},
						"json"
				);
			},
			delay: 800
		});

		//为“放大镜”按钮绑定事件。打开搜索市场获得的模态窗口
		$("#openSearchModalBtn1").click(function () {

			$("#findMarketActivity").modal("show");

		})

		//市场活动源
		//为搜索操作模态窗口的 搜索框绑定事件，执行搜索并展现市场活动列表的操作
		$("#aname").keydown(function (event) {

			if (event.keyCode==13){

				$.ajax({
					url : "workbench/transaction/getActivityListByName.do",
					data : {
						"aname" : $.trim($("#aname").val())

					},
					type : "get",
					dataType : "json",
					success : function (data) {

						/*
                            data
                                [{市场活动1}，{2}，{3}]
                         */
						var html = "";

						$.each(data,function (i,n) {

							html += '<tr>';
							html += '<td><input type="radio" name="xz" value="'+n.id+'"/></td>';
							html += '<td id="'+n.id+'">'+n.name+'</td>';
							html += '<td>'+n.startDate+'</td>';
							html += '<td>'+n.endDate+'</td>';
							html += '<td>'+n.owner+'</td>';
							html += '</tr>';

						})

						$("#activitySearchBody").html(html);

					}
				})

				return false;
			}

		})

		//为提交（市场活动）按钮绑定事件，填充市场活动源，（填写两项信息 名字+id）
		$("#submitActivityBtn").click(function () {

			//取得选中市场活动的id
			var $xz = $("input[name=xz]:checked");
			var id = $xz.val();

			//取得选中的市场活动的名字
			var name = $("#"+id).html();

			//将以上两项信息填写到交易表单的市场活动源中
			$("#edit-activitySrc").val(name);
			$("#activityId").val(id);

			//清空模态窗口里的内容
			$("#aname").val("");
			$("#activityTable tbody").html("");

			//将模态窗口关闭掉
			$("#findMarketActivity").modal("hide")


		})

		//联系人源
		//为“放大镜”按钮绑定事件。打开搜索市场获得的模态窗口
		$("#openSearchModalBtn2").click(function () {

			$("#findContacts").modal("show");

		})

		//为搜索操作模态窗口的 搜索框绑定事件，执行搜索并展现联系人列表的操作
		$("#cname").keydown(function (event) {

			if (event.keyCode==13){

				$.ajax({
					url : "workbench/transaction/getContactsListByName.do",
					data : {
						"cname" : $.trim($("#cname").val())

					},
					type : "get",
					dataType : "json",
					success : function (data) {

						/*
                            data
                                [{市场活动1}，{2}，{3}]
                         */
						var html = "";

						$.each(data,function (i,n) {

							html += '<tr>';
							html += '<td><input type="radio" name="xz1" value="'+n.id+'"/></td>';
							html += '<td id="'+n.id+'">'+n.fullname+'</td>';
							html += '<td>'+n.email+'</td>';
							html += '<td>'+n.mphone+'</td>';
							html += '</tr>';

						})

						$("#contactsSearchBody").html(html);

					}
				})

				return false;
			}

		})

		//为提交（联系人）按钮绑定事件，填充联系人源，（填写两项信息 名字+id）
		$("#submitContactsBtn").click(function () {

			//取得选中市场活动的id
			var $xz = $("input[name=xz1]:checked");
			var id = $xz.val();

			//取得选中的市场活动的名字
			var name = $("#"+id).html();

			//将以上两项信息填写到交易表单的市场活动源中
			$("#edit-contactsName").val(name);
			$("#contactsId").val(id);

			//清空模态窗口里的内容
			$("#cname").val("");
			$("#contactsSearchBody tbody").html("");

			//将模态窗口关闭掉
			$("#findContacts").modal("hide")


		})

		//为阶段的下拉框，绑定选中下拉框事件，根据选择的阶段填写可能性
		$("#edit-stage").change(function () {

			//取得选中的阶段
			var stage = $("#edit-stage").val(); //也可以使用this.value
			//alert(stage);

			//取可能性，将pmap的键值对关系转换js中的键值对关系json
			/*
                pMap
                    pMap.put("01资质审查",10);
                转换为
                    var json = {"01资质审查":10,"02需求分析":25...};
             */
			//alert(stage);
			/*
                我们现在以json.key的形式不能取得value
                因为今天的stage是一个可变的变量
                如果是这样的key，那么我们就不能以传统的json.key的形式来取值
                我们要使用的取值方式为
                json[key]
             */

			//var possibility = json.stage;
			var  possibility = json[stage];
			//alert(possibility);

			//为可能性文本框赋值
			$("#edit-possibility").val(possibility);

		})

		//可能性赋值
		$("#edit-possibility").val(json[$("#edit-stage").val()])

		//为更新按钮绑定事件，执行交易更新操作
		$("#updateBtn").click(function () {

			$("#tranForm").submit();

		})

	});

</script>

</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="aname" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable4" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="activitySearchBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitActivityBtn">提交</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="cname" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="contactsSearchBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitContactsBtn">提交</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>更新交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form action="workbench/transaction/update.do" id="tranForm" method="post" class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<input type="hidden" id="edit-id" name="id" value="${t.id}">
		<div class="form-group">
			<label for="edit-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-transactionOwner" name="owner">
					<%--与其他的下拉框不同，不是从数据字典获取的，而是request域中的数据${request.uList} 可以省略request--%>
					<c:forEach items="${uList}" var="u">
						<option value="${u.id}" ${user.id eq u.id ? "selected" : ""}>${u.name}</option>
					</c:forEach>
				  <%--<option selected>zhangsan</option>
				  <option>lisi</option>
				  <option>wangwu</option>--%>
				</select>
			</div>
			<label for="edit-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-money" name="money" value="${t.money}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-transactionName" name="name" value="${t.name}">
			</div>
			<label for="edit-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time1" id="edit-expectedClosingDate" name="expectedDate" value="${t.expectedDate}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="customerId" value="${t1.customerId}" name="customerId">
				<input type="text" class="form-control" id="edit-customerName"  value="${t.customerId}" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="edit-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="edit-stage" name="stage" >
			  	<option>${t.stage}</option>
				  <c:forEach items="${stageList}" var="s">
					  <option value="${s.value}">${s.text}</option>
				  </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-transactionType" name="type" >
				  <option>${t.type}</option>
					<c:forEach items="${transactionTypeList}" var="t">
						<option value="${t.value}">${t.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="edit-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-possibility">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-clueSource" name="source" >
				  <option>${t.source}</option>
					<c:forEach items="${sourceList}" var="s">
						<option value="${s.value}">${s.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="edit-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearchModalBtn1"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-activitySrc" value="${t.activityId}">
				<input type="hidden"  id="activityId" name="activityId" value="${t1.activityId}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearchModalBtn2"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-contactsName" value="${t.contactsId}" >
				<input type="hidden"  id="contactsId" name="contactsId" value="${t1.contactsId}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe" name="description">${t.description}</textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary">${t.contactSummary}</textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time" id="create-nextContactTime" name="nextContactTime" value="${t.nextContactTime}">
			</div>
		</div>
		
	</form>
</body>
</html>