<#macro vert str>
    <#list 0..str?length - 1 as idx>
        ${str?string[idx]?upper_case}<br>
    </#list>
</#macro>

<#assign boxSize = "30px" />

<#if view.param?size gt 0>
    <#assign paramName = view.param />
    <#else>
        <#assign paramName = "right-p-value" />
</#if>

<#if view.scale?size gt 0>
    <#assign scaleName = view.scale />
    <#else>
        <#assign scaleName = "log" />
</#if>

<#if view["min-module-size"]?size gt 0>
    <#assign minModuleSize = view["min-module-size"]?number />
    <#else>
        <#assign minModuleSize = 0 />
</#if>

<table class="rtab">
    <#-- Header -->
        <tr class="rtabr">
            <#list results.condNames as condName>
                <#assign condIndex=condName_index>
                    <th class="rtabh" style="width: ${boxSize}"><@vert str="${condName}" /></th>
            </#list>
            <th class="rtabh"></th>
        </tr>

        <#-- Modules loop -->
            <#list results.groupNames as groupName>

                <#-- Check that module size for all conditions is greater than minModuleSize -->
                    <#assign show = false />
                    <#list results.condNames as condName>
                        <#assign N = results.paramValue(groupName_index, condName_index, "N") />
                        <#if (N >= minModuleSize)>
                            <#assign show = true />
                        </#if>
                    </#list>

                    <#if show>
                        <tr class="rtabr" style="height: ${boxSize}">
                            <#list results.condNames as condName>
                                <#assign pvalue = results.paramValue(groupName_index, condName_index, paramName) />
                                <#assign N = results.paramValue(groupName_index, condName_index, "N") />
                                <#if pvalue?is_number>
                                    <#assign color = results.colorScale(pvalue, scaleName) />
                                    <#else>
                                        <#assign color = "rgb(0,0,0)" />
                                </#if>
                                <td
                                        class="rtabd"
                                        style="background-color:${color}"
                                        title="p-value = ${pvalue?string("
                                0.######")}, N = ${N?int?c}">
                                </td>
                            </#list>
                            <td class="rtabg">${groupName}</td>
                        </tr>
                    </#if>

            </#list>
            <#-- End Modues loop -->
</table>