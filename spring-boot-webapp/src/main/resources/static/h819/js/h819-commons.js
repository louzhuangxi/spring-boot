/* 
判断是否是 ie
// https://github.com/nioteam/jquery-plugins/issues/12

使用方法
if(isIE(6)){
    // IE 6
}
// ...
if(isIE(9)){
    // IE 9
}

if(isIE()){
    // 是否是 ie
}
*/

/*******************************************************
 *
 * 名 称:   isIEBrowser()
 * 功 能:   判断是否是 ie 浏览器
 * 说 明：  查找到锁后才能执行后面的函数
 * 返     回：  true,fals
 * 参 考： https://github.com/nioteam/jquery-plugins/issues/12

 使用方法
 if(isIE(6)){
    // IE 6
}
 // ...
 if(isIE(9)){
    // IE 9
}

 if(isIE()){
    // 是否是 ie
}
 *
 **********************************************************/

var isIEBrowser = function (ver) {
    var b = document.createElement('b')
    b.innerHTML = '<!--[if IE ' + ver + ']><i></i><![endif]-->'
    return b.getElementsByTagName('i').length === 1
}


/**
 * sleep 指定时间后执行
 * -
 * 使用方法

 $.sleep(2, function () {
    alert('五秒后我才弹出') ; //此处为需要休眠2秒钟后执行的语句。
});

 *
 *
 */
(function ($) {
    var _sleeptimer;
    $.sleep = function (time2sleep, callback) {
        $.sleep._sleeptimer = time2sleep;
        $.sleep._cback = callback;
        $.sleep.timer = setInterval('$.sleep.count()', 1000);
    }
    $.extend($.sleep, {
        current_i: 1,
        _sleeptimer: 0,
        _cback: null,
        timer: null,
        count: function () {
            if ($.sleep.current_i === $.sleep._sleeptimer) {
                clearInterval($.sleep.timer);
                $.sleep._cback.call(this);
            }
            $.sleep.current_i++;
        }
    });
})(jQuery);

