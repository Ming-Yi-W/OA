<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fk" uri="/longIt/oa"%>  	
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set> 

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
<link rel="stylesheet"
	href="${ctx }/res/resources/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript"
	src="${ctx }/res/resources/jquery/jquery-1.11.0.min.js"></script>
<script type="text/javascript"
	src="${ctx }/res/resources/jquery/jquery-migrate-1.2.1.min.js"></script>
<!-- 导入bootStrap的库 -->
<script type="text/javascript"
	src="${ctx}/res/resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${ctx}/res/resources/easyUI/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/res/resources/easyUI/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="${ctx}/res/resources/easyUI/easyui.css">
<script type="text/javascript" src="${ctx}/res/resources/blockUI/jquery.blockUI.js"></script>
<!--  script type="text/javascript"
	src="${ctx}/res/js/operaPageCss.js"></script-->
<!-- 引入分页样式 -->
<link rel="stylesheet" href="${ctx}/res/css/pager.css">
<!--  script type="text/javascript">
  /** 文档加载完成*/
     $(function(){
    	     	 
	    	//$(document).ajaxStart($.blockUI({ css: { backgroundColor: '#11a9e2', color: '#fff' } , //message: '<h6>正在加载..</h6>'})).ajaxStop($.unblockUI);

		 /** 激活用户操作*/
    	   $("input[id^='checkUser_']").switchbutton({
              onChange: function(checked){
            	    window.location = "/identity/user/activeUser?status="+(checked?+"1":"0")+"&userId="+this.value+"&pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}"
              }
          }); 
          
          if("${message}"){ 
          $.messager.show({
				title:'提示信息',
				msg:"<font color='red'>${message}</font>",
				showType:'show'
			});
          
          }
          

           //通过异步请求加载部门以及职位信息
           $.post("${ctx}/identity/user/ajaxLoadDeptAndJob",function(result,status){
        	   
        	   if(status == "success"){
        		   //获取部门信息
        		   var depts = result.depts;
        		   $.each(depts,function(){
        			   //填充部门信息
        			   //$("#deptSelect").append("<option value='"+this.id+"'>"+this.name+"</option>");
        		         $("<option>").val(this.id).text(this.name).prop("selected",this.id == "${user.dept.id}").appendTo("#deptSelect");
        		   })
        		   
        		    //获取职位信息
        		    var jobs = result.jobs;
        		   $.each(jobs,function(){
        			   //填充职位信息
        			  // $("#jobSelect").append("<option value='"+this.code+"'>"+this.name+"</option>");
        			   $("<option>").val(this.code).text(this.name).prop("selected",this.code == "${user.job.code}").appendTo("#jobSelect");
        		   })
        		   
        	   }else{
        		   $.messager.alert('提示信息',"网络异常！",'warning');
        	   }
        	   
           },"json")
           
           
           //为添加按钮绑定点击事件
           $("#addUser").click(function(){
        	   
        	   $("#divDialog").dialog({
        		    title : "添加用户", // 标题
        			cls : "easyui-dialog", // class
        			width : 580, // 宽度
        			height : 455, // 高度
        			maximizable : true, // 最大化
        			minimizable : false, // 最小化
        			collapsible : true, // 可伸缩
        			modal : true, // 模态窗口
        			onClose : function(){ // 关闭窗口
        				window.location = "/identity/user/selectUser?pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}";
        			} 
        	   })
        	   
        	   //通过iframe将添加用户信息的页面引入
        	   $("#iframe").prop("src","/identity/user/showAddUser");
        	       
           })
           
           //为删除按钮绑定点击事件
           $("#deleteUser").click(function(){
        	   //获取选中的checkbox
        	   var boxes = $("input[name='box']:checked");
        	   if(boxes.length == 0){
        		   $.messager.alert('提示信息',"请选择需要删除的用户信息！",'warning');
        	   }else{
        		   
        		   $.messager.confirm('确认信息','是否确认删除该记录?',function(r){   
        			    //如果用户点击确认  那么  r的值为true
        			    if (r){   
        			        var arr = new Array();
        			    	$.each(boxes,function(){
        			    		arr.push(this.value);
        			    	})
        			    	//发送请求删除用户信息      
        			    	window.location = "/identity/user/deleteUser?userIds="+arr+"&pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}";
        			    }   
        			});  

        		   
        		   
        	   }
           })
     })
     
    function updateUser(userId){
	  
    	  $("#divDialog").dialog({
  		    title : "更新用户", // 标题
  			cls : "easyui-dialog", // class
  			width : 580, // 宽度
  			height : 455, // 高度
  			maximizable : true, // 最大化
  			minimizable : false, // 最小化
  			collapsible : true, // 可伸缩
  			modal : true, // 模态窗口
  			onClose : function(){ // 关闭窗口
  				window.location = "/identity/user/selectUser?pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}";
  			} 
  	   })
  	   
  	   //通过iframe将修改用户信息的页面引入
  	   $("#iframe").prop("src","/identity/user/showUpdateUser?userId="+userId);
    		  
    }
  
  function preUser(userId){
	  
	  $("#divDialog").dialog({
		    title : "预览用户", // 标题
			cls : "easyui-dialog", // class
			width : 860, // 宽度
			height : 455, // 高度
			maximizable : true, // 最大化
			minimizable : false, // 最小化
			collapsible : true, // 可伸缩
			modal : true// 模态窗口
			
	   })
	   
	   //通过iframe将修改用户信息的页面引入
	   $("#iframe").prop("src","/identity/user/preUser?userId="+userId);
  }
</script-->


<script type="text/javascript">
	

	$(document).ready(function(){
	
		//获取所有的单选按钮
		var boxs=$("input[id^='box_']");
		var trs=$("tr[id^='dataTr_']");
		
		//全选和全不选操作
		$("#checkAll").click(function(){
			boxs.attr("checked",this.checked);
			//boxs.prop("checked",$(this).prop("checked"));
			trs.trigger(this.checked ? "mouseover":"mouseout");
			
		});
	
		//所有的单选选中，全选自动选中
		boxs.click(function(event){
			//点击的时候，tr也执行了一次，所以需要去掉本次的默认
			event.stopPropagation();
			//选中的单选框
			var clickboxs=$("input[id^='box_']:checked");
			$("#checkAll").prop("checked",boxs.length==clickboxs.length);
			
		});
		
		
		
		trs.hover(function(){
			$(this).css({background:"#eeccff",cursor:"pointer"});
		},function(){
			//选中后颜色固定
			var trId=this.id;
			var boxId=trId.replace("dataTr","box");
			if(!$("#"+boxId).prop("checked")){
				$(this).css({background:""});
			}	
		}).click(function(){    //事件返回的是对象，所有可以连续绑定事件
			//js操作
			var trId=this.id;
			var boxId=trId.replace("dataTr","box");
			$("#"+boxId).prop("checked",!$("#"+boxId).prop("checked"));
			
			//通过tr全部选中后，全选也选中
			//var clickboxs=$("input[id^='box_']:checked");
			var clickboxs=boxs.filter(":checked");
			$("#checkAll").prop("checked",boxs.length==clickboxs.length);
			
				
			
		});
		
		
		$.post("${ctx}/identity/user/getDeptandJobJson",
				function(result,status){
					
					if(status=="success"){
						var depts=result.depts;  //dept是key,后台写入的
						var jobs=result.jobs;
						$.each(depts,function(){
						var expression= this.id=='${user.dept.id}'?'selected==true':'';
						$("#deptSelect").append("<option  value="+this.id+" "+expression+" >"+this.name+"</option>"   );
							
						}); 
						$.each(jobs,function(){
							var expression= this.code=='${user.job.code}'?'selected==true':'';
							$("#jobSelect").append("<option  value="+this.code+" "+expression+">"+this.name+"</option>"   );
						});
						
						
					}else{
						 $.messager.show({
								title:'提示信息',
								msg:'网络异常!',
								timeout:3000,
								showType:'slide'
							});
					}
		
				},
				"json"
		
		);
		
		
	


    //为添加按钮绑定点击事件
    $("#addUser").click(function(){
 	   
 	   $("#divDialog").dialog({
 		    title : "添加用户", // 标题
 			cls : "easyui-dialog", // class
 			width : 580, // 宽度
 			height : 455, // 高度
 			maximizable : true, // 最大化
 			minimizable : false, // 最小化
 			collapsible : true, // 可伸缩
 			modal : true, // 模态窗口
 			onClose : function(){ // 关闭窗口
 				window.location = "/identity/user/selectUser?pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}";
 			} 
 	   })
 	   
 	   //通过iframe将添加用户信息的页面引入
 	   $("#iframe").prop("src","/identity/user/showAddUser");
 	       
    });

    if("${message}"){ 
        $.messager.show({
				title:'提示信息',
				msg:"<font color='red'>${message}</font>",
				showType:'show'
			});
        
        }
    
    
    //为删除按钮绑定点击事件
    $("#deleteUser").click(function(){
 	   //获取选中的checkbox
 	   var boxes = $("input[name='box']:checked");
 	   if(boxes.length == 0){
 		   $.messager.alert('提示信息',"请选择需要删除的用户信息！",'warning');
 	   }else{
 		   
 		   $.messager.confirm('确认信息','是否确认删除该记录?',function(r){   
 			    //如果用户点击确认  那么  r的值为true
 			    if (r){   
 			        var arr = new Array();
 			    	$.each(boxes,function(){
 			    		arr.push(this.value);
 			    	})
 			    	//发送请求删除用户信息      
 			    	window.location = "/identity/user/deleteUser?userIds="+arr+"&pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}";
 			    }   
 			});  

 		   
 		   
 	   }
    });
    
    
    /** 激活用户操作*/
	   $("input[id^='checkUser_']").switchbutton({
       onChange: function(checked){
    	   
    		 $.ajax({
    			 type:"post",
    			 url:"/identity/user/activeUser",
    			 data:"status="+(checked?+"1":"0")+"&userId="+this.value+"&pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}",
    			 success:function(message){
    				 $.messager.show({
							title:'提示信息',
							msg:message,
							timeout:3000,
							showType:'slide'
						});
    			 }
    		 });
    	   
     	    //window.location = "/identity/user/activeUser?status="+(checked?+"1":"0")+"&userId="+this.value+"&pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}";
       }
   }); 
    
    
});

    function updateUser(userId){
  	  
  	  $("#divDialog").dialog({
		    title : "更新用户", // 标题
			cls : "easyui-dialog", // class
			width : 580, // 宽度
			height : 455, // 高度
			maximizable : true, // 最大化
			minimizable : false, // 最小化
			collapsible : true, // 可伸缩
			modal : true, // 模态窗口
			onClose : function(){ // 关闭窗口
				window.location = "/identity/user/selectUser?pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}";
			} 
	   })
	   
	   //通过iframe将修改用户信息的页面引入
	   $("#iframe").prop("src","/identity/user/showUpdateUser?userId="+userId);
  		  
  }

function preUser(userId){
	  
	  $("#divDialog").dialog({
		    title : "预览用户", // 标题
			cls : "easyui-dialog", // class
			width : 860, // 宽度
			height : 455, // 高度
			maximizable : true, // 最大化
			minimizable : false, // 最小化
			collapsible : true, // 可伸缩
			modal : true// 模态窗口
			
	   })
	   
	   //通过iframe将修改用户信息的页面引入
	   $("#iframe").prop("src","/identity/user/preUser?userId="+userId);
}


</script>
</head>
<body style="overflow: hidden; width: 98%; height: 100%;" >
   	<!-- 工具按钮区 -->
	<form class="form-horizontal" 
			action="${ctx }/identity/user/selectUser" method="post" style="padding-left: 5px;" >
			<table class="table-condensed">
					<tbody>
					<tr>
					   <td>
						<input name="name" type="text" class="form-control"
							placeholder="姓名" value="${user.name}" >
						</td>
						<td>	
						<input type="text" name="phone" class="form-control"
							placeholder="手机号码" value="${user.phone}" >
						</td>
<!-- 						<td>	 -->
<!-- 						   <input type="text" class="form-control" placeholder="状态"> -->
<!-- 						</td> -->
						<td>	
						<select class="btn btn-default"
							placeholder="部门" id="deptSelect" name="dept.id">
							<option value="0">==请选择部门==</option>
						</select>
						</td>
						<td>	
						<select class="btn btn-default"
							placeholder="职位" id="jobSelect" name="job.code">
							<option value="0">==请选择职位==</option>
						</select>
						</td>
						<td>	
						<button type="submit" id="selectUser" class="btn btn-info"><span class="glyphicon glyphicon-search"></span>&nbsp;查询</button>
						<fk:permission permiss="user:addUser"> <a  id="addUser" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加</a></fk:permission>
					   <fk:permission permiss="user:deleteUser">  <a  id="deleteUser" class="btn btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</a></fk:permission>
					 </td>
					</tr>
					</tbody>
				</table>
		</form>
 		<div class="panel panel-primary" style="padding-left: 10px;">
 			<div class="panel-heading" style="background-color: #11a9e2;">
				<h3 class="panel-title">用户信息列表</h3>
			</div>
			<div class="panel-body" >
				<table class="table table-bordered">
					<thead>
						<tr style="font-size: 12px;" align="center">
							<th style="text-align: center;"><input id="checkAll"
								type="checkbox" /></th>
							<th style="text-align: center;">账户</th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">性别</th>
							<th style="text-align: center;">部门</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">手机号码</th>
							<th style="text-align: center;">邮箱</th>
							<fk:permission permiss="user:checkUser">
							<th style="text-align: center;">激活状态</th>
							</fk:permission>
							<th style="text-align: center;">审核人</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<c:forEach items="${users }" var="user" varStatus="status">
					      <tr id="dataTr_${status.index }" align="center">
							<td><input type="checkbox" name="box" id="box_${status.index }"
								value="${user.userId }" /></td>
							<td>${user.userId }</td>
							<td>${user.name }</td>
							<td>${user.sex==1 ? '男' : '女'}</td>
							<td>${user.dept.name }</td>
							<td>${user.job.name }</td>
							<td>${user.phone }</td>
							<td>${user.email }</td>
							<fk:permission permiss="user:checkUser">
							<td>
								<c:if test="${user.status == 1 }">
									<input id="checkUser_${user.userId }" value="${user.userId }" name="status" class="easyui-switchbutton" data-options="onText:'激活',offText:'冻结'" checked>
								</c:if>
								<c:if test="${user.status != 1 }">
									<input id="checkUser_${user.userId }" value="${user.userId }" name="status" class="easyui-switchbutton" data-options="onText:'激活',offText:'冻结'" >
								</c:if>
						    </td>
						    </fk:permission>
						    <td>${user.checker.name}</td>
						    <td>
						   	 <fk:permission permiss="user:updateUser">	 <span  class="label label-info"><a href="javascript:updateUser('${user.userId}')"  style="color: white;">修改</a></span></fk:permission>
							 <fk:permission permiss="user:preUser">	<span  class="label label-success"><a href="javascript:preUser('${user.userId}')"  style="color: white;">预览</a></span></fk:permission>
							</td>
						</tr>
					</c:forEach>
				</table>
				
			</div>
                <!-- 分页标签区 -->
                <fk:pager pageIndex="${page.pageIndex }" pageSize="${page.pageSize }" totalNum="${page.recordCount }" submitUrl="${ctx }/identity/user/selectUser?pageIndex={0}&name=${user.name}&phone=${user.phone }&dept.id=${user.dept.id }&job.code=${user.job.code }" ></fk:pager>
		</div>
		
		<div id="divDialog" style="display: none;" >
			 <!-- 放置一个添加用户的界面  -->
			 <iframe id="iframe" frameborder="0" style="width: 100%;height: 100%;"></iframe>
		</div>
	
</body>
</html>