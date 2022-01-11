<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
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

	$(function(){

		pageList(1,2);

		//为查询按钮绑定事件，触发pageList方法
		$("#searchBtn").click(function () {
			/*
			* 点击查询按钮时，应该将搜索框中的信息保存起来，保存到隐藏域中
			* */
			$("#hidden-name").val($.trim($("#search-name").val())),
			$("#hidden-owner").val($.trim($("#search-owner").val())),
			$("#hidden-customerId").val($.trim($("#search-phone").val())),
			$("#hidden-stage").val($.trim($("#search-stage").val())),
			$("#hidden-type").val($.trim($("#search-type").val())),
			$("#hidden-source").val($.trim($("#search-source").val())),
			$("#hidden-contactsId").val($.trim($("#search-contactsId").val())),

			pageList(1 ,$("#transactionPage").bs_pagination('getOption', 'rowsPerPage'));
		});

		//为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked)

		})

		$("#transactionBody").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

		})

		//修改按钮
		$("#editBtn").click(function () {
			var $xz = $("input[name=xz]:checked")
			if ($xz.length == 0) {
				alert("请选择你要修改的交易")
			} else if ($xz.length > 1) {
				alert("每次修改只能选择一个交易")
			}else {
				window.location.href='workbench/transaction/edit.do?id='+$('input[name=xz]:checked').val()+'';
			}
		})

		//为删除按钮绑定事件，执行交易删除操作
		$("#deleteBtn").click(function () {

			//找到所有被选中的复选框的jquery对象
			var $xz = $("input[name=xz]:checked");

			if ($xz.length==0){
				alert("请选择要删除的记录");
				//选择了有可能是一条有可能是多条
			}else {

				if (confirm("确认删除所选中的记录吗？")){

					//url:workbench/transactin/delete.do?id=xxx&id=xxx&id=xxx
					//拼接参数
					var param = "";

					//将$xz中的每一个dom对象遍历出来，取其value值就相当于取出了id值
					for (i = 0;i<$xz.length;i++){
						param += "id="+ $($xz[i]).val();

						//如果不是最后一个元素，在其后加&符
						if (i<$xz.length-1){
							param += "&";
						}
					}
					//alert(param);

					$.ajax({
						url : "workbench/transaction/delete.do",
						data : param,
						type : "post",
						dataType : "json",
						success : function (data) {
							/*
                            * data
                            *   {"success":true/false}
                            * */
							if (data.success){
								//删除成功
								//回到第一页，维持每页展现的记录数
								pageList(1,$("#transactionPage").bs_pagination('getOption', 'rowsPerPage'));
							}else {
								alert("删除交易失败！")
							}
						}
					})
				}

			}

		})



	});

	function pageList(pageNo,pageSize) {

		//重新刷新线索区域是将全选框取消勾选
		$("#qx").prop("checked",false);

		//查询前，将隐藏域中的信息取出来，重新赋予到搜索框
		$("#search-name").val($.trim($("#hidden-name").val())),
		$("#search-owner").val($.trim($("#hidden-owner").val())),
		$("#search-customerId").val($.trim($("#hidden-customerId").val())),
		$("#search-stage").val($.trim($("#hidden-stage").val())),
		$("#search-type").val($.trim($("#hidden-type").val())),
		$("#search-source").val($.trim($("#hidden-source").val())),
		$("#search-contactsId").val($.trim($("#hidden-contactsId").val())),

				$.ajax({
					url : "workbench/transaction/pageList.do",
					data : {

						"pageNo" : pageNo,
						"pageSize" : pageSize,
						"name" : $.trim($("#search-name").val()),
						"owner" : $.trim($("#search-owner").val()),
						"customerId" : $.trim($("#search-customerId").val()),
						"stage" : $.trim($("#search-stage").val()),
						"type" : $.trim($("#search-type").val()),
						"source" : $.trim($("#search-source").val()),
						"contactsId" : $.trim($("#search-contactsId").val()),

					},
					type : "get",
					dataType : "json",
					success : function (data) {

						/*
                            data

                                我们需要的：市场活动信息列表
                                [{市场活动1},{2},{3}] List<Activity> aList
                                一会分页插件需要的：查询出来的总记录数
                                {"total":100} int total

                                {"total":100,"dataList":[{市场活动1},{2},{3}]}
                         */
						var html = "";

						//每一个n就代表一个交易对象
						$.each(data.dataList,function (i,n) {

							html += '<tr>';
							html += '<td><input type="checkbox" name="xz" value="'+n.id+'" /></td>';
							html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/transaction/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
							html += '<td>'+n.customerId+'</td>';
							html += '<td>'+n.stage+'</td>';
							html += '<td>'+n.type+'</td>';
							html += '<td>'+n.owner+'</td>';
							html += '<td>'+n.source+'</td>';
							html += '<td>'+n.contactsId+'</td>';
							html += '</tr>';

						})

						$("#transactionBody").html(html);

						//计算总页数
						var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

						//数据处理完毕后，结合分页插件对前端展现分页信息
						$("#transactionPage").bs_pagination({
							currentPage: pageNo, // 页码
							rowsPerPage: pageSize, // 每页显示的记录条数
							maxRowsPerPage: 20, // 每页最多显示的记录条数
							totalPages: totalPages, // 总页数
							totalRows: data.total, // 总记录条数

							visiblePageLinks: 3, // 显示几个卡片

							showGoToPage: true,
							showRowsPerPage: true,
							showRowsInfo: true,
							showRowsDefaultInfo: true,

							//该回调函数是在，点击分页组件时触发的
							onChangePage : function(event, data){
								pageList(data.currentPage , data.rowsPerPage);
							}
						});

					}
				})
	}
	
</script>
</head>
<body>

<input type="hidden" id="hidden-name" />
<input type="hidden" id="hidden-customerId" />
<input type="hidden" id="hidden-stage" />
<input type="hidden" id="hidden-owner" />
<input type="hidden" id="hidden-type" />
<input type="hidden" id="hidden-source" />
<input type="hidden" id="hidden-contactsId" />


	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="search-customerId">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="search-stage">
					  	<option></option>
						  <c:forEach items="${stageList}" var="s">
							  <option value="${s.value}">${s.text}</option>
						  </c:forEach>
					  	<%--<option>资质审查</option>
					  	<option>需求分析</option>
					  	<option>价值建议</option>
					  	<option>确定决策者</option>
					  	<option>提案/报价</option>
					  	<option>谈判/复审</option>
					  	<option>成交</option>
					  	<option>丢失的线索</option>
					  	<option>因竞争丢失关闭</option>--%>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="search-type">
					  	<option></option>
						  <c:forEach items="${transactionTypeList}" var="t">
							  <option value="${t.value}">${t.text}</option>
						  </c:forEach>
					  	<%--<option>已有业务</option>
					  	<option>新业务</option>--%>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="search-source">
						  <option></option>
						  <c:forEach items="${sourceList}" var="s">
							  <option value="${s.value}">${s.text}</option>
						  </c:forEach>
						  <%--<option>广告</option>
						  <option>推销电话</option>
						  <option>员工介绍</option>
						  <option>外部介绍</option>
						  <option>在线商场</option>
						  <option>合作伙伴</option>
						  <option>公开媒介</option>
						  <option>销售邮件</option>
						  <option>合作伙伴研讨会</option>
						  <option>内部研讨会</option>
						  <option>交易会</option>
						  <option>web下载</option>
						  <option>web调研</option>
						  <option>聊天</option>--%>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="search-contactsId">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/getUserList.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="transactionBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">动力节点-交易01</a></td>
							<td>动力节点</td>
							<td>谈判/复审</td>
							<td>新业务</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>李四</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">动力节点-交易01</a></td>
                            <td>动力节点</td>
                            <td>谈判/复审</td>
                            <td>新业务</td>
                            <td>zhangsan</td>
                            <td>广告</td>
                            <td>李四</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 20px;">
				<div id="transactionPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>