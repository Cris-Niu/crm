layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //用户表单提交
    form.on("submit(login)",function(obj){
        var dataField= obj.field;

        //验证用户名是否为空
        if(dataField.username == "undefined" || dataField.username.trim()==''){
            layer.msg("用户名不能为空");
            return false;
        }
        //验证密码是否为空
        if(dataField.password == "undefined" || dataField.password.trim()==''){
            layer.msg("用户密码不能为空");
            return  false;
        }
        //发送
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:dataField.username,
                userPwd:dataField.password
            },
            dataType:"json",
            success:function(obj){
                if(obj.code==200){
                    layer.msg("登录成功了",function(){
                        //将对象信息存储到Cookie
                        $.cookie("userIdStr",obj.result.userIdStr);
                        $.cookie("userUname",obj.result.userName);
                        $.cookie("trueName",obj.result.trueName);

                        // 如果⽤户选择"记住我"，则设置cookie的有效期为7天
                        $.cookie("userIdStr", obj.result.userIdStr, { expires: 7});
                        $.cookie("userName", obj.result.userName, { expires: 7 });
                        $.cookie("trueName", obj.result.trueName, { expires: 7 });
                        location.href=ctx + "/main";
                    })
                }else{
                    //登录失败的提示
                    layer.msg(obj.msg);
                }
            }
        });
        //阻止
        return false;
    });


});