(function(){
	
     $(function(){
    	 
    	   //获取所有的子checkbox
   	  var boxes = $("input[id^='box_']");
         
         //为全选绑定点击事件
         $("#checkAll").click(function(){
       	 //操作所有的子checkbox的选中状态
       	 boxes.prop("checked",this.checked);   
       	 //触发数据行的  mouseover事件或者mouseout事件
       	 $("[id^='dataTr_']").trigger(this.checked ? "mouseover" : "mouseout");
         })
         
         //为所有的子checkbox绑定事件
         boxes.click(function(event){
       	 //取消事件默认行为
       	  event.stopPropagation();
       	   //获取选中的子checkbox
       	    var checkedboxs = $("input[id^='box_']:checked");
       	   //操作全选的选中状态
       	  $("#checkAll").prop("checked",boxes.length == checkedboxs.length); 
       	  
         })
			 
         
         //为数据行绑定 mouseover以及mouseout事件
         $("[id^='dataTr_']").hover(function(){
       	       //mouseover事件
                 // $(this).css("background","#eeccff").css("cursor","pointer");
       	      $(this).css({"background":"#eeccff","cursor":"pointer"});
         },function(){
       	      //mouseout事件
       	      //如果当前tr中的checkbox是选中的则鼠标离开的时候背景色不去掉
       	      
       	      //获取tr的id
       	      var trId = this.id;
       	      //根据当前tr的id获取当前checkbox的id
       	      var boxId = trId.replace("dataTr","box");
       	      if(!$("#"+boxId).prop("checked")){
           	      $(this).css("background","");  
       	      }
         }).click(function(){
       	  //获取tr的id
   	      var trId = this.id;
   	      //根据当前tr的id获取当前checkbox的id
   	      var boxId = trId.replace("dataTr","box");
   	      //让checkbox的状态取反
   	      $("#"+boxId).prop("checked", !$("#"+boxId).prop("checked"));
   	      
   	       
      	       //操作全选的选中状态             boxes.filter(":checked"):获取选中的checkbox
      	      $("#checkAll").prop("checked",boxes.length == boxes.filter(":checked").length);
         })
     })
	
})()
