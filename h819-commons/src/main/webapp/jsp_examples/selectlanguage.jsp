<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
function langSelecter_onChanged()
{
	document.getElementById("langForm").submit();
}
</script>

<s:set name="SESSION_LOCALE" value="#session['WW_TRANS_I18N_LOCALE']" />
<s:bean id="locales" name="com.h819.util.i18n.Locales">
	<s:param name="current"
		value="#SESSION_LOCALE == null ? locale : #SESSION_LOCALE" />
</s:bean>

<form action="<s:url/>" id="langForm" method="post" 
	style="background-color: #bbbbbb; padding-top: 4px; padding-bottom: 4px;">
	<s:text name="language.choose" />
	<s:select label="language.choose" list="#locales.locales" listKey="value"
		listValue="key"
		value="#SESSION_LOCALE == null ? locale : #SESSION_LOCALE"
		name="request_locale" id="langSelecter"
		onchange="langSelecter_onChanged()" theme="simple" />
</form>

<!-- 

参见
struts2 权威指南
http://www.iocblog.net/project/struts2.0/struts2.0-183.html
需要以 action 的方式访问页面才会起作用。

可能大家会问为什么一定要通过Action来访问页面呢？
你可以试一下不用Action而直接用JSP的地址来访问页面，结果会是无论你在下拉列表中选择什么，
语言都不会改变。这表示不能正常运行的。其原因为如果直接使用JSP访问页面，Struts 2.0在
web.xml的配置的过滤器（Filter）就不会工作，所以拦截器链也不会工作。 

		<action name="login">
    		<result>/jsp/user/login_user.jsp</result>
		</action>
		
		如果需要在所有的页面中出现语言选择框，那么就要把所有的 jsp 页面以 action 
		的方式访问。
		<action name="*">
    		<result>/jsp/user/{1}.jsp</result>
		</action>

 -->

