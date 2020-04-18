<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="shortcut icon" href="<%=path%>/images/favicon.ico">
<title>添加</title>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/common/layui/css/layui.css">
<script src="<%=path%>/common/layui/layui.js"></script>
<script src="<%=path%>/js/jquery.js"></script>
<style>
.layui-upload-img{width: 92px;height: 92px;margin: 0 10px 10px 0;}
</style>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
  <legend>赋值和取值</legend>
</fieldset>
 
<form class="layui-form" action="" lay-filter="example">
  <div class="layui-form-item">
    <label class="layui-form-label">姓名</label>
    <div class="layui-input-block">
    <input type="hidden" name="Id">
      <input type="text" name="Name" lay-verify="required" lay-reqtext="用户名是必填项，岂能为空？" placeholder="请输入" autocomplete="off" class="layui-input">
    </div>
  </div>
  
  <div class="layui-form-item">
    <div class="layui-inline">
      <label class="layui-form-label">手机号</label>
      <div class="layui-input-inline">
        <input type="tel" name="Phone" lay-verify="required|phone" autocomplete="off" class="layui-input">
      </div>
    </div>
  </div>
  
  <div class="layui-form-item">
    <div class="layui-inline">
      <label class="layui-form-label">年龄（非空数字）</label>
      <div class="layui-input-inline">
        <input type="text" name="Age" lay-verify="required|number" autocomplete="off" class="layui-input">
      </div>
    </div>
  </div>
  <div class="layui-form-item">
    <div class="layui-inline">
      <label class="layui-form-label">工资（非空数字）</label>
      <div class="layui-input-inline">
        <input type="text" name="Salary" lay-verify="required|number" autocomplete="off" class="layui-input">
      </div>
    </div>
  </div>
  
  <div class="layui-form-item">
    <label class="layui-form-label">所属地</label>
    <div class="layui-input-block">
      <select name="Nationalty" lay-filter="aihao" id="classifyId">
        <option value="0">请选择</option>
        <option value="中国" selected="">中国</option>
        <option value="美国">美国</option>
      </select>
    </div>
  </div>
  
  <div class="layui-form-item">
    <label class="layui-form-label">性别</label>
    <div class="layui-input-block">
      <input type="radio" name="sex" value="true" title="男" checked="">
      <input type="radio" name="sex" value="false" title="女">
    </div>
  </div>
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button type="submit" class="layui-btn" lay-submit="" lay-filter="demo1">立即提交</button>
    </div>
  </div>
      
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
  <legend>常规使用：普通图片上传</legend>
</fieldset>
 
<div class="layui-upload">
  <button type="button" class="layui-btn" id="test1">上传图片</button>
  <div class="layui-upload-list">
    <img class="layui-upload-img" id="demo1">
    <p id="demoText"></p>
  </div>
</div>   

</form>
 
          
<script>
layui.use('upload', function(){
	  var $ = layui.jquery
	  ,upload = layui.upload;
	  
	  //普通图片上传
	  var uploadInst = upload.render({
	    elem: '#test1'
	    ,url: '<%=path%>/upload' //改成您自己的上传接口
	    ,before: function(obj){
	      //预读本地文件示例，不支持ie8
	      obj.preview(function(index, file, result){
	        $('#demo1').attr('src', result); //图片链接（base64）
	      });
	    }
	    ,done: function(res){
		      console.log(res);
	      //如果上传失败
	      if(res.code <= 0){
	        return layer.msg('上传失败');
	      }
	      //上传成功
	    }
	    ,error: function(){
	      //演示失败状态，并实现重传
	      var demoText = $('#demoText');
	      demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
	      demoText.find('.demo-reload').on('click', function(){
	        uploadInst.upload();
	      });
	    }
	  });
	  
	  
	  
	});

layui.use(['form'], function(){
  var form = layui.form
  ,layer = layui.layer;  
  //自定义验证规则
  form.verify({
    title: function(value){
      if(value.length < 5){
        return '标题至少得5个字符啊';
      }
    }
    ,pass: [
      /^[\S]{6,12}$/
      ,'密码必须6到12位，且不能出现空格'
    ]
  });
//var ss="${ret}";
    form.val('example', {
      "Id": "${ret.data[0].Id}" ,
      "Name": "${ret.data[0].Name}" ,
      "Phone": "${ret.data[0].Phone}" ,
      "Age": "${ret.data[0].Age}" ,
      "Salary": "${ret.data[0].Salary}" ,
      "Nationalty": "${ret.data[0].Nationality}" ,
      "sex": "${ret.data[0].Sex}" 
    });
  //监听提交
  form.on('submit(demo1)', function(data){
    //layer.alert(JSON.stringify(data.field), {
      //title: '最终的提交信息'
    //})
    if(submit(data.field)){
    	location.href='<%=path%>/index';
    	return false;
    }else{
    	return false;
    };
  });

  select();
});
function submit(data){
	var rt=false;
	$.ajax( {    
	    url:'<%=path%>/submitEdit',// 跳转到 action    
	    data:JSON.stringify(data),    
	    type:'post',    
	    cache:false,    
	    dataType:'json', 
	    async : false,
	    success:function(data) {    
	        if(data.code =="1" ){  
	            rt = true;
	        }else{    
	        	alert(data.msg);    
	        }    
	     },    
	     error : function() { 
	          alert("异常！");    
	     }    
	}); 
	return rt;
}
function select(){
	var rt=false;
	$.ajax( {    
	    url:'<%=path%>/select',// 跳转到 action    
	    //data:JSON.stringify(data),    
	    type:'get',    
	    cache:false,    
	    //dataType:'json', 
	    async : false,
	    success:function(res) {    
        	if(res.code =="1" ){  
	            rt = true;
	            if(res.data.length>0){
	                for(var i =0;i<res.data.length;i++){
	                    $("#classifyId").append("<option value=\""+res.data[i].id+"\">"+res.data[i].name+"</option>");
	                }
	                //重新渲染
	                layui.form.render("select");
	            }
	        }else{    
	        	alert(data.msg);    
	        }  
	    },
	     error : function() { 
	          alert("异常！");    
	     }    
	}); 
	return rt;
}
</script>
</body>
</html>