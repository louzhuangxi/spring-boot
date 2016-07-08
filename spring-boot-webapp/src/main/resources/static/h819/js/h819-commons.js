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
* 返	 回：  true,fals
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

var isIEBrowser = function(ver){
    var b = document.createElement('b')
    b.innerHTML = '<!--[if IE ' + ver + ']><i></i><![endif]-->'
    return b.getElementsByTagName('i').length === 1
}
