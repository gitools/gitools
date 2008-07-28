<#escape x as x?html>
<?xml version="1.0"?>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
 <head>
  <title>${analysisName}</title>
 </head>
 <body>
 	<p>Analysis name: ${analysisName}</p>
 	<p>Method name: ${methodName}</p>
 	<p>Result names: <#list resultNames as resultName>${resultName} </#list></p> 
	<table border="1">
		<tr>
			<#list condNames as condName>
				<#assign condIndex=condName_index>
				<th class="tableCondName">${condName}</th>
			</#list>
			<th></th>
		</tr>
		<#list groupNames as groupName>
 			<tr>
 				<#list condNames as condName>
 					<td>${results(groupName_index, condName_index, 0)?c}</td>
 				</#list>
 				<td class="tableGroupName">${groupName}</td>
 			</tr>
		</#list>
	</table>
 </body>
</html>
</#escape>