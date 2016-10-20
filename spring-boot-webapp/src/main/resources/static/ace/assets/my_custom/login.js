
/**
登陆验证
//documentation : http://jqueryvalidation.org/validate

实际使用时，还有许多参数，参考官方文档

// 拷贝自 html/form-wizard.html
 */
jQuery(function ($) {
	
	//自定义电话号码验证
	jQuery.validator.addMethod("phone", function (value, element) {
		return this.optional(element) || /^\(\d{3}\) \d{3}\-\d{4}( x\d{1,6})?$/.test(value);
	}, "Enter a valid phone number.");
	
	/*
	spring security ajax csrf
	
	1. 页面中配置
		<!-- default header name is X-CSRF-TOKEN -->
		<meta name="_csrf" th:content="${_csrf.token}"/>
		<meta name="_csrf_header" th:content="${_csrf.headerName}"/>
	2. ajax 如定义如下函数即可
	*/
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
				 xhr.setRequestHeader(header, token);
		});


	//表单验证
	$('form').each(function () { //多个表单，一起验证。会逐个验证 form 标签。如果只有一个表单，去掉 each ，只用 	$('#validation-form').validate({ ... }); 即可
		$(this).validate({
			debug : false, // true,只进行验证，不提交
			onkeyup : false, //避免每次按键都会提交请求，尤其是 remote 方式时，会每次按键都会提交。只有鼠标离开才会进行验证。
			errorElement : 'div',
			errorClass : 'help-block',
			focusInvalid : false,
			submitHandler : function (form) {
				//验证成功后自动提交 : $(form).ajaxSubmit();
				//点击提交按钮提交 form.submit();
				form.submit();
			},
			rules : {

				// =================== register =================== //

				register_email : {
					required : true,
					email : true,
					// http://jqueryvalidation.org/remote-method
					// validate plugin 的 remote 方法，是覆盖 jquery 的 ajax 方法。
					// 此处再次设置，覆盖了原 jQuery Validation Plugin remote 部分的设置
					// 在 validate plugin remote 参数中中，该 plugin 对 remoter 的相关处理，都默认设置好了，所以此处不能设置常规的的 ajax  success 方法来回调处理结果，用 plugin 的默认处理逻辑即可
					// 在 validate plugin 中规定 :
					// url 的返回值为字符串 "true" ,验证通过 , 返回其他的字符串，都为不通过，并且返回的信息直接作为出错信息，被 validate plugin 显示在页面上
					// 所以可以返回自定义的字符串，来代表不同的信息。下文的 messages : {} 不必再定义出错信息。
					remote : {
						type : "POST",
						url : context + "/ajax/validate/login/email.html" ,
						data : { //传递的参数和值
							email : function () {
								return $("#register_email").val();
							},
							checktype : "register"
						},
						dataType : "html" //data 发送到 服务器端的格式 // 不要用 json ，会有 syntaxerror unexpected token < 错误发生，直接跳到 error : 部分
					}
				},				
				
				register_password : {
					
					required : true,
					minlength : 2
				},

				register_re_password : {
					required : true,
					minlength : 2,
					equalTo : "#register_password"
				},

				// =================== login =================== //
				login_email : {
					required : true,
					email : true,
					remote : { //远程验证，登陆名是否存在，以下同理
						type : "POST",
						url : context + "/ajax/validate/login/email.html" ,
						data : { //传递的参数和值
							email : function () {
								return $("#login_email").val();
							},
							checktype : "login" //,
							//logintag : "loginame"
						},
						dataType : "html" //data 发送到 服务器端的格式 // 不要用 json ，会有 syntaxerror unexpected token < 错误发生，直接跳到 error : 部分
					}
				},

				login_password : {
					required : true,
					remote : {
						type : "POST",
						url :  context + "/ajax/validate/login/password.html" ,
						data : { //传递的参数和值
							email : function () {
								return $("#login_email").val();
							},
							loginpasword : function () {
								return $("#login_password").val();
							}
						},
						dataType : "html" //data 发送到 服务器端的格式 // 不要用 json ，会有 syntaxerror unexpected token < 错误发生，直接跳到 error : 部分
					}
				},

				// =================== find back  =================== //

				find_back_email : {
					required : true,
					email : true,
					remote : {
						type : "POST",
						url : context + "/ajax/validate/login/email.html" ,
						data : { //传递的参数和值
							email : function () {
								return $("#find_back_email").val();
							},
							checktype : "find"
						},
						dataType : "html" //data 发送到 服务器端的格式 // 不要用 json ，会有 syntaxerror unexpected token < 错误发生，直接跳到 error : 部分
					}
				},

				phone : {
					required : true,
					phone : 'required'
				},
				url : {
					required : true,
					url : true
				},
				comment : {
					required : true
				},
				state : {
					required : true
				},
				platform : {
					required : true
				},
				subscription : {
					required : true
				},
				gender : {
					required : true
				},
				agreeregister : {
					required : true
				}
			},

			messages : {

				// =================== register =================== //
				register_email : {
					required : "email 不能为空",
					email : "email 格式不对"
				},

				register_password : {
					required : "密码不能为空",
					minlength : "至少六位"
				},

				register_re_password : {
					required : "密码不能为空",
					minlength : "至少六位",
					equalTo : "两次密码不一致"
				},

				// =================== login =================== //

				login_email : {
					required : "email 不能为空",
					email : "email 格式不对"
				},

				login_password : {
					required : "密码不能为空"
				},

				// =================== find back  =================== //

				find_back_email : {
					required : "email 不能为空",
					email : "email 格式不对"
				},

				state : "Please choose state",
				subscription : "Please choose at least one option",
				gender : "Please choose gender",
				agreeregister : "同意协议才能注册"
			},

			/*
			下面的几个函数，是高亮错误输入框和错误信息
			高亮参考了 from-wizard.html 的做法，把需要高亮的输入框，添加标签 <div class="form-group"> input ....  </div>	
			这样，就可以给 div 增加或者删除样式 class 了，输入框就会高亮			
			*/
			
			// form-group 是包含
			highlight : function (e) {
				$(e).closest('.form-group').removeClass('has-success').addClass('has-error');
			},

			success : function (e) {
				$(e).closest('.form-group').removeClass('has-error').addClass('has-success');
				$(e).remove();
			},

			errorPlacement : function (error, element) {
				if (element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
					var controls = element.closest('div[class*="col-"]');
					if (controls.find(':checkbox,:radio').length > 1)
						controls.append(error);
					else
						error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
				} else if (element.is('.select2')) {
					error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
				} else if (element.is('.chosen-select')) {
					error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
				} else
					error.insertAfter(element.parent());
			},

			invalidHandler : function (form) {}
		});

	}); //each
});
