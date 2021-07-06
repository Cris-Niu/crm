layui.use(['form', 'jquery', 'jquery_cookie', 'table'], function () {
    var form = layui.form,
        layer = layui.layer,
        table = layui.table,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    /**
     * 触发表单
     */
    //submit(addOrUpdateSaleChance)
    form.on("submit(addOrUpdateSaleChance)", function (obj) {
        var dataField = obj.field;
        console.log(obj.field + "<<<<");
        /**
         * 发送ajax
         */
            // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...", {
                icon: 16, // 图标
                time: false, // 不关闭
                shade: 0.8 // 设置遮罩的透明度
            });

        var url = ctx + "/sale_chance/save";

        if( $("input[name=id]").val()){
            url=ctx+"/sale_chance/update";
        }

        $.post(url,obj.field,function(data){
            if(data.code == 200){
                layer.close(index);
                layer.closeAll("iframe");
                parent.location.reload();
            }else{
                layer.msg(data.msg,{icon:5});
            }
        },"json");

        //阻止表单提交
        return false;
    });

    /**
     * 取消
     */

    $("#closeBtn").click(function () {
        //假设这是iframe页
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });


    /**
     * 加载下拉框
     */

    $.post(ctx+"/user/queryAllSales",function (data){

        var assignMan = $('input[name="man"]').val();
        for(var x in data){
            if(assignMan == data[x].id) {
                // 当前修改记录的指派人的值 与 循环到的值 相等，下拉框则选中
                $("#assignMan").append('<option value="' + data[x].id + '" selected>' +
                    '' + data[x].uname + '</option>');
            }else{
                $("#assignMan").append('<option value="'+data[x].id+'">'
                    +data[x].uname+'</option>');
            }
        }
        layui.form.render("select");
    });

});