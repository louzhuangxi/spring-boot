<#--声明变量-->
<#assign ctx = "${context.contextPath}">
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>登陆</title>
        <!-- ajax 请求时用到 default header name is X-CSRF-TOKEN 例子在 login.js 中 -->
       <meta name="_csrf" content="${_csrf.token}"/>
       <meta name="_csrf_header" content="${_csrf.headerName}"/>

		<meta name="description" content="User login page" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${ctx}/ace/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="${ctx}/ace/assets/css/font-awesome.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="${ctx}/ace/assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="${ctx}/ace/assets/css/ace.css" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${ctx}/ace/assets/css/ace-part2.css" />
		<![endif]-->
		<link rel="stylesheet" href="${ctx}/ace/assets/css/ace-rtl.css" />

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${ctx}/ace/assets/css/ace-ie.css" />
		<![endif]-->

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lt IE 9]>
		<script src="${ctx}/ace/assets/js/html5shiv.js"></script>
		<script src="${ctx}/ace/assets/js/respond.js"></script>
		<![endif]-->
	</head>

	<body class="login-layout">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center">
								<h1>
									<i class="ace-icon fa fa-leaf green"></i>
									<span class="red">内部</span>
									<span class="white" id="id-text2">管理系统${ctx}</span>
								</h1>
								<h4 class="blue" id="id-company-text">&copy; Company Name</h4>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger">
												<i class="ace-icon fa fa-coffee green"></i>
												填写登陆信息
											</h4>

											<div class="space-6"></div>

											<form action="${ctx}/login_process" method="post">
											<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                                <fieldset>
												<div class="form-group">													
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
														    <!-- email 方式登陆
															<input type="text" class="form-control" placeholder="Username" />
															-->
															<input name="login_email" type="email" class="form-control" id="login_email" placeholder="Email" value="1@qq.com" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>                                                        
												  </label>
													</div>												  
												<div class="form-group">	
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input name="login_password" type="password" class="form-control" id="login_password" placeholder="Password" value="password" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
												  </label>
													</div>
													<div class="space"></div>

													<div class="clearfix">
											    <div class="form-group">
														<label class="inline">
															<input name="login_remember_me" type="checkbox" class="ace" id="login_remember_me" />
															<span class="lbl">&nbsp;记住我的登录信息</span>
												    </label>
													</div>
														<button type="submit" class="width-35 pull-right btn btn-sm btn-primary">
															<i class="ace-icon fa fa-key"></i>
															<span class="bigger-110">登陆</span>
														</button>
													</div>

													<div class="space-4"></div>
												</fieldset>
											</form>
											<!--
											<div class="social-or-login center">
												<span class="bigger-110">Or Login Using</span>
											</div>
											-->
											<div class="space-6"></div>
											<!--
											<div class="social-login center">
												<a class="btn btn-primary">
													<i class="ace-icon fa fa-facebook"></i>
												</a>

												<a class="btn btn-info">
													<i class="ace-icon fa fa-twitter"></i>
												</a>

												<a class="btn btn-danger">
													<i class="ace-icon fa fa-google-plus"></i>
												</a>
											</div>
											-->
										</div><!-- /.widget-main -->

										<div class="toolbar clearfix">
											<div>
												<a href="#" data-target="#forgot-box" class="forgot-password-link">
													<i class="ace-icon fa fa-arrow-left"></i>
													忘记密码
												</a>
											</div>

											<div>
												<a href="#" data-target="#signup-box" class="user-signup-link">
													注册新用户
													<i class="ace-icon fa fa-arrow-right"></i>
												</a>
											</div>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.login-box -->

								<div id="forgot-box" class="forgot-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header red lighter bigger">
												<i class="ace-icon fa fa-key"></i>
												找回密码
											</h4>

											<div class="space-6"></div>
											<p>
												填写注册时的邮件地址,密码通过邮件方式发送
											</p>

											<form>
												<fieldset>
												<div class="form-group">													
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input name="find_back_email" type="email" class="form-control" id="find_back_email" placeholder="Email" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
												  </label>
												</div>
													<div class="clearfix">
														<button type="button" class="width-35 pull-right btn btn-sm btn-danger">
															<i class="ace-icon fa fa-lightbulb-o"></i>
															<span class="bigger-110">提交</span>
														</button>
													</div>
												</fieldset>
											</form>
										</div><!-- /.widget-main -->

										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												回到登陆页
												<i class="ace-icon fa fa-arrow-right"></i>
											</a>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.forgot-box -->

								<div id="signup-box" class="signup-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header green lighter bigger">
												<i class="ace-icon fa fa-users blue"></i>
												注册新用户
											</h4>

											<div class="space-6"></div>
											<p> 填写注册信息: </p>

											<form>
												<fieldset> 
													<div class="form-group">												
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input name="register_email" type="email" class="form-control" id="register_email" placeholder="Email" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
													</label>
													</div>
												<!-- email 方式注册，根据实名制要求，应该用手机号码。
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control" placeholder="Username" />
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>
													-->
												<div class="form-group">	
											  <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input name="register_password" type="password" class="form-control" id="register_password" placeholder="Password" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
												  </label>
												</div>												  
												<div class="form-group">	
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input name="register_re_password" type="password" class="form-control" id="register_re_password" placeholder="Repeat password" />
															<i class="ace-icon fa fa-retweet"></i>
														</span>
												  </label>
												</div>												  
												<div class="form-group">	
													<label class="block">
														<input name="register_remember_me" type="checkbox" class="ace" id="register_remember_me" />
														<span class="lbl">
															接受
															<a href="#">用户协议</a>
														</span>
												  </label>
												</div>
													<div class="space-24"></div>

													<div class="clearfix">
														<button type="reset" class="width-30 pull-left btn btn-sm">
															<i class="ace-icon fa fa-refresh"></i>
															<span class="bigger-110">重置</span>
														</button>

														<button type="button" class="width-65 pull-right btn btn-sm btn-success">
															<span class="bigger-110">注册</span>

															<i class="ace-icon fa fa-arrow-right icon-on-right"></i>
														</button>
													</div>
												</fieldset>
											</form>
										</div>

										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												<i class="ace-icon fa fa-arrow-left"></i>
												回到登陆页
											</a>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.signup-box -->
							</div><!-- /.position-relative -->

							<div class="navbar-fixed-top align-right">
								<br />
								&nbsp;
								<a id="btn-login-dark" href="#">Dark</a>
								&nbsp;
								<span class="blue">/</span>
								&nbsp;
								<a id="btn-login-blur" href="#">Blur</a>
								&nbsp;
								<span class="blue">/</span>
								&nbsp;
								<a id="btn-login-light" href="#">Light</a>
								&nbsp; &nbsp; &nbsp;
							</div>
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${ctx}/ace/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='${ctx}/ace/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${ctx}/ace/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {

			 $(document).on('click', '.toolbar a[data-target]', function(e) {
				e.preventDefault();
				var target = $(this).data('target');
				$('.widget-box.visible').removeClass('visible');//hide others
				$(target).addClass('visible');//show target
			 });
			});
			
			
			
			//you don't need this, just used for changing background
			jQuery(function($) {
			 $('#btn-login-dark').on('click', function(e) {
				$('body').attr('class', 'login-layout');
				$('#id-text2').attr('class', 'white');
				$('#id-company-text').attr('class', 'blue');
				
				e.preventDefault();
			 });
			 $('#btn-login-light').on('click', function(e) {
				$('body').attr('class', 'login-layout light-login');
				$('#id-text2').attr('class', 'grey');
				$('#id-company-text').attr('class', 'blue');
				
				e.preventDefault();
			 });
			 $('#btn-login-blur').on('click', function(e) {
				$('body').attr('class', 'login-layout blur-login');
				$('#id-text2').attr('class', 'white');
				$('#id-company-text').attr('class', 'light-blue');
				
				e.preventDefault();
			 });
			 
			});
		</script>
		
		<script>
           // 定义变量
           var context = "${ctx}";
			// ... 此处可以直接调用 ，也可以在本页面中引入的其他 js 文件中直接调用
       </script>
		
		<script  type="text/javascript" src="${ctx}/ace/assets/js/jquery.validate.js"></script>
		<script  type="text/javascript" src="${ctx}/ace/assets/js/additional-methods.js"></script>
		<script  type="text/javascript" src="${ctx}/ace/assets/my_custom/login.js"></script>
		
	</body>
</html>
