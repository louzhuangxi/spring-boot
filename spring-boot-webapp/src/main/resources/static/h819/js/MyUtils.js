/*
 javascript 模块化
 使用"立即执行函数"（Immediately-Invoked Function Expression，IIFE），可以达到不暴露私有成员的目的。
 http://www.ruanyifeng.com/blog/2012/10/javascript_module.html
 */


/*
 http://sugarjs.com/api/Date/format 也可以，但源文件太长，这里自己定义

 用法：
 DateUtils.getNow();
 DateUtils.getNow();

 */
var DateUtils = (function () {
    var _count = 0; // 内部变量 例子
    /*
     获取当前时间 yyyy-MM-dd hh:mm:ss 格式 ( 2016-01-22 09:49:02 )
     */
    var getNow = function () {
        var date = new Date();
        //date.format("yyyy-mm-dd  HH:MM:ss");  // Date 没有 format 函数，所有的函数见 http://www.w3schools.com/jsref/jsref_obj_date.asp
        //return date.toUTCString(); //
        var year = date.getFullYear();
        var month = (date.getMonth() + 1).toString();
        var day = date.getDate().toString();
        var hours = date.getHours().toString();
        var minutes = date.getMinutes().toString();
        var secondes = date.getSeconds().toString();
        //补足两位
        month = month.length < 2 ? '0' + month : month;
        day = day.length < 2 ? '0' + day : day;
        hours = hours.length < 2 ? '0' + hours : hours;
        minutes = minutes.length < 2 ? '0' + minutes : minutes;
        secondes = secondes.length < 2 ? '0' + secondes : secondes;

        return [year, month, day].join('-') + " " + [hours, minutes, secondes].join(':');

    };
    /*
     var m2 = function(){
     　　　　　　//...
     　　　　};　*/


    return {
        getNow: getNow

    };

})();

/*
 StrUtils.replaceAll();
 */
var StrUtils = (function () {

    /**
     * js 中没有 replaceAll 函数, replace 只能替换第一个字符串，本函数可以替换指定字符串
     * @param str 源字符串
     * @param searchString 被替换的字符串
     * @param replaceString 替换的字符串
     * @returns {*|string|XML|void}
     */
    var replaceAll = function (str, searchString, replaceString) {

        //规范影响正则表达式的特殊字符
        searchString = searchString.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
        return str.replace(new RegExp(searchString, 'g'), replaceString);
    };

    /*
     var m2 = function(){
     　　　　　　//...
     　　　　};　*/


    return {
        replaceAll: replaceAll
        //m2 : m2

    };

})();


var ArrayUtils = (function () {

    function removeDuplicate(array) {
        var result = [];
        $.each(array, function (i, e) {
            if ($.inArray(e, result) == -1) result.push(e);
        });
        return result;
    }


    function contains(value, array) {
        var result = jQuery.inArray(value, array); //inArray 返回 value 在 array 位置
        if (result !== -1) //找到
            return true;
        else return false;
    }

    function remove(value, array) {
        return jQuery.grep(array, function (v) {
            return v != value;
        });
    }


    /**
     * 判断是否包含
     * @param value
     * @param array
     */
    var contains = function (value, array) {
        var result = jQuery.inArray(value, array); //inArray 返回 value 在 array 位置
        if (result !== -1) //找到
            return true;
        else return false;
    };

    /**
     去掉指定元素
     */
    var remove = function (value, array) {
        return jQuery.grep(array, function (v) {
            return v != value;
        });
    }

    /**
     去掉 array 中重复的元素
     */
    var removeDuplicate = function (array) {
        var result = [];
        $.each(array, function (i, e) {
            if ($.inArray(e, result) == -1) result.push(e);
        });
        return result;
    }


    return {
        contains: contains,
        remove: remove,
        removeDuplicate: removeDuplicate

    }

})();
