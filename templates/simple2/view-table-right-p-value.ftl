<table border="1" class="resTab">
	<tr>
		<#list results.condNames as condName>
			<#assign condIndex=condName_index>
			<th class="resTabCond">${condName}</th>
		</#list>
		<th></th>
	</tr>
	<#list results.groupNames as groupName>
		<tr>
			<#list results.condNames as condName>
				<#assign pvalue = results.paramValue(groupName_index, condName_index, "right-p-value") />
				<#assign N = results.paramValue(groupName_index, condName_index, "N") />
				<#assign color = results.colorScale(pvalue, "log") />
				<td 
					class="resTabCell" 
					style="background-color:${color}" 
					alt="p-value = ${pvalue}, N = ${N}">
				</td>
			</#list>
			<td class="resTabGroup">${groupName}</td>
		</tr>
	</#list>
</table>