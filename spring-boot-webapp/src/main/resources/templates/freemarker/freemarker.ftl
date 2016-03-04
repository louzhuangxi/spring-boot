
<#include "layout.ftl">

<#macro overrideHeader>
<div>header overriden</div>
</#macro>

<@layout header=overrideHeader>
<div>body overriden</div>
</@layout>