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
 	<#compress>
	 	<p>Result names: 
		 	<#list resultNames as resultName>
		 		${resultName}<#if resultName_has_next>${", "}</#if> 
		 	</#list>
	 	</p>
 	</#compress>
 	
 	<#list showedResultIndices as resultIndice>
 		<h2>${resultNames[resultIndice]}</h2> 
		<table border="1" class="resultsTable">
			<tr>
				<#list condNames as condName>
					<#assign condIndex=condName_index>
					<th class="resultsTableCondName">${condName}</th>
				</#list>
				<th></th>
			</tr>
			<#list groupNames as groupName>
	 			<tr>
	 				<#list condNames as condName>
	 					<#assign result = results(groupName_index, condName_index, resultIndice)>
	 					<td style="background-color:${result.color}">${result.value}</td>
	 				</#list>
	 				<td class="resultsTableGroupName">${groupName}</td>
	 			</tr>
			</#list>
		</table>
	</#list>
 </body>
</html>
</#escape>