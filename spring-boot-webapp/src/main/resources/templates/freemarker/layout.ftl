<#-- 模板 -->
<#macro defaultHeader>
<div>defaultHeader here</div>
</#macro>

<#macro defaultFooter>
<div>defaultFooter here</div>
</#macro>

<#macro layout header=defaultHeader footer=defaultFooter>
    <@header />
      <#nested>
    <@footer />
</#macro>

<#-- 模板 -->
<#macro layout>
<html>
<body>
<div id="head"><#nested "head" /></div>
<div id="content"><#nested "content" /></div>
</body>
</html>
</#macro>

<#-- 模板 -->
<#macro heading>
DEFAULT HEADING
</#macro>

<#macro content>
DEFAULT CONTENT
</#macro>

<#macro leftNav>
DEFAULT LEFT NAV
</#macro>


<#macro doLayout>
<html>
<body>
<div id="head"><@heading /></div>
<div id="leftNav"><@leftNav /></div>
<div id="content"><@content /></div>
</body>
</html>
</#macro>