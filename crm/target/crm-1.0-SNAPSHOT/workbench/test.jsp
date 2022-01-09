<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2022/1/4
  Time: 19:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

$.ajax({
url : "",
data : {

},
type : "",
dataType : "json",
success : function (data) {

}
})

String createBy = ((User)request.getSession().getAttribute("user")).getName();
String createTime = DateTimeUtil.getSysTime();

$(".time").datetimepicker({
minView: "month",
language:  'zh-CN',
format: 'yyyy-mm-dd',
autoclose: true,
todayBtn: true,
pickerPosition: "top-left"
});

</body>
</html>
