var roleCode = null;
var roleName = null;
var addBtn =null;
var backBtn = null;
function priceReg (value){
    value = value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
    value = value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
    value = value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
    value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");//去掉特殊符号￥
    if(value.indexOf(".")>0){
        value = value.substring(0,value.indexOf(".")+3);
    }
    return value;
}


$(function(){
    roleCode = $("#roleCode")
    roleName = $("#roleName")
    addBtn = $("#add");
    backBtn = $("#back");

/*
     * 验证
     * 失焦\获焦
     * jquery的方法传递
     */
    roleCode.on("blur",function(){
        if(roleCode.val() != null && roleCode.val() != ""){
            validateTip(roleCode.next(),{"color":"green"},imgYes,true);
        }else{
            validateTip(roleCode.next(),{"color":"red"},imgNo+" 编码不能为空，请重新输入",false);
        }
    }).on("focus",function(){
        //显示友情提示
        validateTip(roleCode.next(),{"color":"#666666"},"* 请输入订单编码",false);
    }).focus();

    roleName.on("focus",function(){
        validateTip(roleName.next(),{"color":"#666666"},"* 请输入商品名称",false);
    }).on("blur",function(){
        if(roleName.val() != null && roleName.val() != ""){
            validateTip(roleName.next(),{"color":"green"},imgYes,true);
        }else{
            validateTip(roleName.next(),{"color":"red"},imgNo+" 商品名称不能为空，请重新输入",false);
        }

    });
    addBtn.on("click",function(){
            if(confirm("是否确认提交数据")){
                $("#roleForm").submit();
            }
    });

    backBtn.on("click",function(){
        if(referer != undefined
            && null != referer
            && "" != referer
            && "null" != referer
            && referer.length > 4){
            window.location.href = referer;
        }else{
            history.back(-1);
        }
    })
});