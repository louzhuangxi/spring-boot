<#assign ctx = "${context.contextPath}">

<!-- custom begin -->

<#if model["usersChecked"] ?? || model["usersUnChecked"] ??>
<!-- usersChecked 或者 usersUnChecked 不为 null -->
<div class="control-group">
    <!-- #section:custom/checkbox -->
    <div class="checkbox inline">
        <label>
            <input name="form-field-radio" type="radio" class="ace" value="true"/>
            <span class="lbl"> 全选</span>
        </label>
    </div>
    <div class="checkbox inline">
        <label>
            <input name="form-field-radio" type="radio" class="ace" value="false"/>
            <span class="lbl"> 全不选</span>
        </label>
    </div>
</div>

<!--分为两组，已经和 group 关联的/未关联的-->
<div class="control-group">
    <#if model["usersChecked"] ??>
        <#list model["usersChecked"] as user>
            <div class="checkbox inline">
                <label>
                    <input name="form-field-checkbox" type="checkbox" value="${user.id}"
                           checked class="ace"/>
                    <span class="lbl"> ${user.userName}</span>
                </label>
            </div>
        </#list>
    </#if>
</div>
<div class="control-group">
    <#if model["usersUnChecked"] ??>
        <#list model["usersUnChecked"] as user>
            <div class="checkbox inline">
                <label>
                    <input name="form-field-checkbox" type="checkbox" value="${user.id}"
                           class="ace"/>
                    <span class="lbl"> ${user.userName}</span>
                </label>
            </div>
        </#list>
    </#if>
</div>
<#else>
加载未完成
</#if>

<!-- custom end -->


<script type="text/javascript">

    /**
     *  select checkbox
     */
    $('.control-group input[type="radio"]').click(function () {
        //    console.log($(this).val());

        if ($(this).val() === 'true') {
            $('.control-group input[type="checkbox"]').prop('checked', true);
        } else if ($(this).val() === 'false') {
            $('.control-group input[type="checkbox"]').prop('checked', false);
        }
    });

</script>

