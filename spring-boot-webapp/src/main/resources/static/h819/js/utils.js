
/*
javascript 模块化
使用"立即执行函数"（Immediately-Invoked Function Expression，IIFE），可以达到不暴露私有成员的目的。
http://www.ruanyifeng.com/blog/2012/10/javascript_module.html
*/


/*
http://sugarjs.com/api/Date/format 也可以，但源文件太长，这里自己定义
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
		getNow : getNow
		//m2 : m2
		　　　　
	};
	　　
})();
