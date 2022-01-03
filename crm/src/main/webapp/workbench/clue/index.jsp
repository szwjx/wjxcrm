<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

<script type="text/javascript">

	$(function(){

		//为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function () {


			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			/*

				操作模态窗口的方式：

					需要操作的模态窗口的jquery对象，调用modal方法，为该方法传递参数 show:打开模态窗口   hide：关闭模态窗口


			 */
			//alert(123);
			//$("#createActivityModal").modal("show");
			//走后台，获得用户信息列表，为所有者下拉框赋值
			$.ajax({
				url :"workbench/clue/getUserList.do",
				data : {

				},
				type:"get",
				dataType:"json",
				success:function (data) {

					/*

						data
							[{"id":?,"name":?,"loginAct":?.......},{2},{3}...]

					 */

					var html = "<option></option>";

					//遍历出来的每一个n，就是每一个user对象
					$.each(data,function (i,n) {

						html += "<option value='"+n.id+"'>"+n.name+"</option>";

					})
					$("#create-owner").html(html);

					//将下拉框默认选项设为当前用户
					//取得当前用户的id
					//在js中使用EL表达式一定要套用在字符串中
					var id = "${user.id}";

					$("#create-owner").val(id);
					//当所有者下拉框处理完毕后，展现模态窗口
					$("#createClueModal").modal("show");
				}
			})
		})

		//为保存按钮绑定事件，执行添加操作
		$("#saveBtn").click(function () {

			$.ajax({
				url : "workbench/clue/save.do",
				data : {
					"owner" : $.trim($("#create-owner").val()),
					"company" : $.trim($("#create-company").val()),
					"appellation" : $.trim($("#create-appellation").val()),
					"fullname" : $.trim($("#create-fullname").val()),
					"job" : $.trim($("#create-job").val()),
					"email" : $.trim($("#create-email").val()),
					"phone" : $.trim($("#create-phone").val()),
					"website" : $.trim($("#create-website").val()),
					"mphone" : $.trim($("#create-mphone").val()),
					"state" : $.trim($("#create-state").val()),
					"source" : $.trim($("#create-source").val()),
					"description" : $.trim($("#create-description").val()),
					"contactSummary" : $.trim($("#create-contactSummary").val()),
					"nextContactTime" : $.trim($("#create-nextContactTime").val()),
					"address" : $.trim($("#create-address").val())
				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*

                        data
                            {"success":true/false}

                     */
					if(data.success){

						//添加成功后
						//刷新市场活动信息列表（局部刷新）
						//做完添加操作后，应该回到第一页，维持每页展现的记录数

						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						$("#clueAddForm")[0].reset();

						//关闭添加操作的模态窗口
						$("#createClueModal").modal("hide");

					}else{

						alert("添加市场活动失败");

					}
				}
			})

		})

		//页面加载完毕后触发一个方法
		//默认展开列表第一页，每页展现两条记录
		pageList(1,2);

		//为查询按钮绑定事件，出发pageList方法
		$("#searchBtn").click(function () {
			/*
			* 点击查询按钮时，应该将搜索框中的信息保存起来，保存到隐藏域中
			* */
			$("#hidden-fullname").val($.trim($("#search-fullname").val())),
			$("#hidden-owner").val($.trim($("#search-owner").val())),
			$("#hidden-company").val($.trim($("#search-company").val())),
			$("#hidden-mphone").val($.trim($("#search-mphone").val())),
			$("#hidden-phone").val($.trim($("#search-phone").val())),
			$("#hidden-state").val($.trim($("#search-state").val())),
			$("#hidden-source").val($.trim($("#search-source").val())),

			pageList(1 ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
		});


	})

		//为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked)

		})
		//为普通选框狂绑定事件
		//但以下操作不可以
		/*$("input[name=xz]").click(function () {

			alert(111)

		})*/
		//因为动态生成的元素不能以普通绑定事件形式进行操作
		/*

			动态生成的元素，我们要以on方法的形式来触发事件

			语法：
				$(需要绑定元素的有效的外层元素).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)

		 */
		$("#clueBody").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

		})

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function () {

			//找到所有被选中的复选框的jquery对象
			var $xz = $("input[name=xz]:checked");

			if ($xz.length==0){
				alert("请选择要删除的记录");
				//选择了有可能是一条有可能是多条
			}else {

				if (confirm("确认删除所选中的记录吗？")){

					//url:workbench/clue/delete.do?id=xxx&id=xxx&id=xxx
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
						url : "workbench/clue/delete.do",
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
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else {
								alert("删除市场活动失败！")
							}
						}
					})
				}

			}

		})

		//为修改按钮绑定事件，执行修改市场活动操作
		$("#editBtn").click(function () {

			//先获取要修改的是哪一条记录
			var $xz = $("input[name=xz]:checked");

			if ($xz.length==0){
				alert("请选择要修改的市场活动")
			}else if($xz.length>1){
				alert("只能选择一条记录进行修改")
				//肯定只选择了一条
			}else {
				var id = $xz.val();

				//走后台，获取用户信息列表和市场活动对象，为修改模态窗口内各项赋值

				$.ajax({
					url : "workbench/clue/getUserListAndClue.do",
					data : {

						"id":id
					},
					type : "get",
					dataType : "json",
					success : function (data) {

						/*
						data
						用户列表
						市场活动对象

						{"uList":[{用户1}，{用户2}]，"activity":{市场活动}}
					*/
						//处理所有者下拉框
						var html = "<option><?option>";

						$.each(data.uList,function (i,n) {

							html += "<option value='"+n.id+"'>"+n.name+"</option>";

						})
						$("#edit-owner").html(html);

						//处理单条clue
						$("#edit-id").val(data.c.id);
						$("#edit-owner").val(data.c.owner);
						$("#edit-appellation").val(data.c.appellation);
						$("#edit-company").val(data.c.company);
						$("#edit-fullname").val(data.c.fullname);
						$("#edit-job").val(data.c.job);
						$("#edit-email").val(data.c.email);
						$("#edit-website").val(data.c.website);
						$("#edit-phone").val(data.c.phone);
						$("#edit-mphone").val(data.c.mphone);
						$("#edit-state").val(data.c.state);
						$("#edit-source").val(data.c.source);
						$("#edit-description").val(data.c.description);
						$("#edit-contactSummary").val(data.c.contactSummary);
						$("#edit-nextContactTime").val(data.c.nextContactTime);
						$("#edit-address").val(data.c.address);

						//所有值都填写好后就可以打开修改市场活动的模态窗口
						$("#editClueModal").modal("show");
					}
				})
			}
		})

		//为更新按钮绑定事件，执行线索的修改操作
		$("#updateBtn").click(function () {

			$.ajax({
				url : "workbench/clue/update.do",
				data : {
					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-owner").val()),
					"company" : $.trim($("#edit-company").val()),
					"appellation" : $.trim($("#edit-appellation").val()),
					"fullname" : $.trim($("#edit-fullname").val()),
					"job" : $.trim($("#edit-job").val()),
					"email" : $.trim($("#edit-email").val()),
					"phone" : $.trim($("#edit-phone").val()),
					"mphone" : $.trim($("#edit-mphone").val()),
					"website" : $.trim($("#edit-website").val()),
					"state" : $.trim($("#edit-state").val()),
					"source" : $.trim($("#edit-source").val()),
					"description" : $.trim($("#edit-description").val()),
					"contactSummary" : $.trim($("#edit-contactSummary").val()),
					"nextContactTime" : $.trim($("#edit-nextContactTime").val()),
					"address" : $.trim($("#edit-address").val())
				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*

                        data
                            {"success":true/false}

                     */
					if(data.success){

						//修改成功后
						//刷新市场活动信息列表（局部刷新）
						//pageList(1,2);
						/*

							修改操作后，应该维持在当前页，维持每页展现的记录数

						 */
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));



						//关闭修改操作的模态窗口
						$("#editClueModal").modal("hide");
					}else{

						alert("修改市场活动失败");

					}
				}
			})


		})
		
		
		
	});

    function pageList(pageNo,pageSize) {

        //重新刷新线索区域是将全选框取消勾选
        $("#qx").prop("checked",false);

        //查询前，将隐藏域中的信息取出来，重新赋予到搜索框
        $("#search-fullname").val($.trim($("#hidden-fullname").val())),
		//$("#search-appellation").val($.trim($("#hidden-appellation").val())),
		$("#search-owner").val($.trim($("#hidden-owner").val())),
        $("#search-company").val($.trim($("#hidden-company").val())),
        $("#search-mphone").val($.trim($("#hidden-mphone").val())),
		$("#search-phone").val($.trim($("#hidden-phone").val())),
		$("#search-state").val($.trim($("#hidden-state").val())),
		$("#search-source").val($.trim($("#hidden-source").val())),

            $.ajax({
                url : "workbench/clue/pageList.do",
                data : {

                    "pageNo" : pageNo,
                    "pageSize" : pageSize,
                    "fullname" : $.trim($("#search-fullname").val()),
                    "owner" : $.trim($("#search-owner").val()),
                    "company" : $.trim($("#search-company").val()),
                    "mphone" : $.trim($("#search-mphone").val()),
					"phone" : $.trim($("#search-phone").val()),
					"state" : $.trim($("#search-state").val()),
					"source" : $.trim($("#search-source").val()),

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

                    //每一个n就代表一个市场活动对象
                    $.each(data.dataList,function (i,n) {
						html += '<tr class="active">';
						html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
						html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/clue/detail.do?id='+n.id+'\';">'+n.fullname+n.appellation+'</a></td>';
						html += '<td>'+n.company+'</td>';
						html += '<td>'+n.phone+'</td>';
						html += '<td>'+n.mphone+'</td>';
						html += '<td>'+n.source+'</td>';
						html += '<td>'+n.owner+'</td>';
						html += '<td>'+n.state+'</td>';
						html += '</tr>';

                    })

                    $("#clueBody").html(html);

                    //计算总页数
                    var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

                    //数据处理完毕后，结合分页插件对前端展现分页信息
                    $("#activityPage").bs_pagination({
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

    <input type="hidden" id="hidden-fullname" />
<%--	<input type="hidden" id="hidden-appellation" />--%>
	<input type="hidden" id="hidden-company" />
	<input type="hidden" id="hidden-phone" />
    <input type="hidden" id="hidden-mphone" />
    <input type="hidden" id="hidden-source" />
    <input type="hidden" id="hidden-owner" />
	<input type="hidden" id="hidden-state" />

	<!-- 创建线索的模态窗口 -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建线索</h4>
				</div>
				<div class="modal-body">
					<form id="clueAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="create-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-appellation">
								  <option></option>
								  <option>先生</option>
								  <option>夫人</option>
								  <option>女士</option>
								  <option>博士</option>
								  <option>教授</option>
								</select>
							</div>
							<label for="create-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-fullname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
							<label for="create-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
							<label for="create-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-state">
								  <option></option>
								  <option>试图联系</option>
								  <option>将来联系</option>
								  <option>已联系</option>
								  <option>虚假线索</option>
								  <option>丢失线索</option>
								  <option>未联系</option>
								  <option>需要条件</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
								  <option></option>
								  <option>广告</option>
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
								  <option>聊天</option>
								</select>
							</div>
						</div>
						

						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">线索描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control time" id="create-nextContactTime" readonly>
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>
						
						<div style="position: relative;top: 20px;">
							<div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
							</div>
						</div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改线索的模态窗口 -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id"/>
					
						<div class="form-group">
							<label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-appellation">
								  <option></option>
								  <option>先生</option>
								  <option>夫人</option>
								  <option>女士</option>
								  <option>博士</option>
								  <option>教授</option>
								</select>
							</div>
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-fullname" v>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" >
							</div>
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" >
							</div>
							<label for="edit-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" >
							</div>
							<label for="edit-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-state">
								  <option></option>
								  <option>试图联系</option>
								  <option>将来联系</option>
								  <option>已联系</option>
								  <option>虚假线索</option>
								  <option>丢失线索</option>
								  <option>未联系</option>
								  <option>需要条件</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
								  <option></option>
								  <option>广告</option>
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
								  <option>聊天</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control" id="edit-nextContactTime" >
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address"></textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-fullname">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司</div>
				      <input class="form-control" type="text" id="search-company">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="search-phone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索来源</div>
					  <select class="form-control" id="search-source">
					  	  <option></option>
					  	  <option>广告</option>
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
						  <option>聊天</option>
					  </select>
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>
				  
				  
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">手机</div>
				      <input class="form-control" type="text" id="search-mphone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索状态</div>
					  <select class="form-control" id="search-state">
					  	<option></option>
					  	<option>试图联系</option>
					  	<option>将来联系</option>
					  	<option>已联系</option>
					  	<option>虚假线索</option>
					  	<option>丢失线索</option>
					  	<option>未联系</option>
					  	<option>需要条件</option>
					  </select>
				    </div>
				  </div>

				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
						</tr>
					</thead>
					<tbody id="clueBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/clue/detail.jsp';">李四先生</a></td>
							<td>动力节点</td>
							<td>010-84846003</td>
							<td>12345678901</td>
							<td>广告</td>
							<td>zhangsan</td>
							<td>已联系</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>
                            <td>动力节点</td>
                            <td>010-84846003</td>
                            <td>12345678901</td>
                            <td>广告</td>
                            <td>zhangsan</td>
                            <td>已联系</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 60px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>