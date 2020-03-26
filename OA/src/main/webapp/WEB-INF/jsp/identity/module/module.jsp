<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
 <%@ taglib prefix="fk" uri="/longIt/oa"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>OA办公管理系统-用户管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
<meta http-equiv="description" content="This is my page" />
<link rel="stylesheet" href="${ctx }/res/resources/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript" src="${ctx }/res/resources/jquery/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${ctx }/res/resources/jquery/jquery-migrate-1.2.1.min.js"></script>
<!-- 导入bootStrap的库 -->
<script type="text/javascript" src="${ctx}/res/resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/res/resources/easyUI/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/res/resources/easyUI/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="${ctx}/res/resources/easyUI/easyui.css">
	<!-- 引入分页样式 -->
<link rel="stylesheet" href="${ctx}/res/css/pager.css">

<script type="text/javascript"
	src="${ctx}/res/js/operaPageCss.js"></script>
<script type="text/javascript">


$(function(){
	  
	  if("${message}"){
		$.messager.show({
			title:'提示信息',
			msg:"<font color='red'>${message}</font>",
			showType:'show',
			timeout:5000
		});
		
		//调用父页面的createDtree函数，让dTree刷新
		parent.createDtree();
	}
	//为添加按钮绑定点击事件
	$("#addModule").click(function(){
		
		$("#divDialog").dialog({   
			title : "添加模块", // 标题
			cls : "easyui-dialog", // class
			width : 580, // 宽度
			height : 355, // 高度
			maximizable : true, // 最大化
			minimizable : false, // 最小化
			collapsible : true, // 可伸缩
			modal : true, // 模态窗口
			onClose:function(){
				location.reload();
			}
		});   
		
        //设置iframe的src属性，通过iframe加载添加角色的jsp页面   此处的参数就是父级模块的code
		$("#iframe").prop("src","${ctx}/identity/module/showAddModule?code=${code}");
	})
	
	  //为删除按钮绑定点击事件
	$("#deleteModule").on("click",function(){
		
		//获取选中的checkbox
		//var boxes = $("input[name='box']:checked");
        var boxes = $("input[name='box']").filter(":checked");

        if(boxes.length == 0){
        	alert("请选择需要删除的角色信息！");
        }else{
        	
        	$.messager.confirm('提示信息', '是否确认删除？', function(r){
        		if (r){
        		     //创建数组
	               	 var arr =new Array();
	               	
	               	 boxes.each(function(){
	               		 arr.push(this.value);	
	                    })
                   //发送请求删除用户    删除分成  物理删除    和   逻辑删除
                   window.location = "${ctx}/identity/module/deleteModule?codes="+arr+"&pageIndex=${pageModel.pageIndex}&code=${code}";

        		}
        	});
        }
	  }) 
	  
})


	//修改模块信息
	function updateModule(code){
		  $("#divDialog").dialog({   
			title : "修改模块", // 标题
			cls : "easyui-dialog", // class
			width : 580, // 宽度
			height : 355, // 高度
			maximizable : true, // 最大化
			minimizable : false, // 最小化
			collapsible : true, // 可伸缩
			modal : true, // 模态窗口
			onClose:function(){
				location.reload();
			}
		});   
		
	    //设置iframe的src属性，通过iframe加载修改模块的jsp页面
		$("#iframe").prop("src","${ctx}/identity/module/showUpdateModule?code="+code);
		  
	}

</script>

</head>
<body style="overflow: hidden; width: 98%; height: 100%;">
	<div>
	
		<div class="panel panel-primary">
			<!-- 工具按钮区 -->
			<div style="padding: 5px;">
				<a  id="addModule" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加</a>
				<a  id="deleteModule" class="btn btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</a>
			</div>
			
			<div class="panel-body">
				<table class="table table-bordered" style="float: right;">
					<thead>
						<tr style="font-size: 12px;" align="center">
							<th style="text-align: center;"><input type="checkbox" id="checkAll"/></th>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">名称</th>
<!-- 							<th>备注</th> -->
							<th style="text-align: center;">链接</th>
							<th style="text-align: center;">操作</th>
<!-- 							<th style="text-align: center;">创建日期</th> -->
<!-- 							<th style="text-align: center;">创建人</th> -->
<!-- 							<th>修改日期</th> -->
							<th style="text-align: center;">修改人</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					  <c:forEach items="${modules}" var="module" varStatus="stat">
				        <tr align="center" id="dataTr_${stat.index}">
							<td><input type="checkbox" name="box" id="box_${stat.index}" value="${module.code}"/></td>
							<td>${module.code}</td>
							<td>${module.name.replaceAll("-","")}</td>
<%-- 							<td>${module.remark}</td> --%>
							<td>${module.url}</td>
							<td><span class="label label-success"><a href="/identity/module/loadSonModule?code=${module.code}" style="color: white;">查看下级</a></span></td>
<%-- 							<td><fmt:formatDate value="${module.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
<%-- 							<td>${module.creater.name }</td> --%>
<%-- 							<td><fmt:formatDate value="${module.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
							<td>${module.modifier.name }</td>
							<td><span class="label label-info"><a href="javascript:updateModule('${module.code}');" style="color: white;">修改</a></span></td>
						</tr>
				    </c:forEach>
				</table>
				
			</div>
			  <!-- 分页标签区 -->
				<fk:pager pageIndex="${pageModel.pageIndex}" pageSize="${pageModel.pageSize}" totalNum="${pageModel.recordCount}" submitUrl="${ctx}/identity/module/loadSonModule?pageIndex={0}&code=${code}"></fk:pager>
		</div>
	</div>
		<div id="divDialog" style="display: none;" >
			 <!-- 放置一个添加用户的界面  -->
			 <iframe id="iframe" frameborder="0" style="width: 100%;height: 100%;"></iframe>
		</div>
	
</body>
</html>