<#escape x as x?html>
<?xml version="1.0"?>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<title>${report.title}</title>
	
	<link href="style/reset.css" rel="stylesheet" type="text/css" media="all" />
	<link href="style/default.css" rel="stylesheet" type="text/css" media="screen" />
	<link href="style/styling.css" rel="stylesheet" type="text/css" media="screen" />
	<link href="style/print.css" rel="stylesheet" type="text/css" media="print" />
</head>
<body class="fullwidth">
	<#assign index = params["analysisIndex"]?number />
	<#assign analysis = report.analyses.analysis[index] />
	<#assign desc = loadAnalysisDescription(analysis.@path) />
	
	<div id="container">

		<div id="header">
			<div id="header-in">
				<h1>Analysis</h1>
			</div> <!-- end #header-in -->
		</div> <!-- end #header -->

		<div id="content-wrap" class="clear">
			<div class="content">
				<div class="content-in">
				
					<h2>Description</h2>
					<table class="descTab">
						<tr>
							<td>Name</td>
							<td>${desc.analysisName}</td>
						</tr>
						<tr>
							<td>Method</td>
							<td>${desc.methodName}</td>
						</tr>
						<tr>
							<td>Elapsed time</td>
							<td>${desc.elapsedTime}</td>
						</tr>
					</table>
					
					<#if analysis.properties.property?size gt 0>
						<h2>Properties</h2>
						<table style="propTab">
							<tr>
								<th>Name</th>
								<th>Value</th>
							</tr>
							<#list analysis.properties.property as prop>
								<tr>
									<td>${prop.@name}</td>
									<td>${prop.@value}</td>
								</tr>
							</#list>
						</table>
					</#if>
					
					<#if analysis.views.view?size gt 0>
						<h2>Views</h2>
						<ul>
						<#list analysis.views.view as view>
							<li>
								<a href="#view${view_index}">${view.@name}</a>
							</li>
						</#list>
						</ul>
					</#if>
					
					<#assign results = loadAnalysisResults(analysis.@path) />
					
					<#list analysis.views.view as view>
						<h3><a name="view${view_index}"></a>${view.@name}</h3>
						<p>${pvalue}</p>
						<#include "view-${view.@name}.ftl" />
					</#list>
					 
				</div> <!-- end .content-in -->
			</div> <!-- end .content -->
		</div> <!-- end #content-wrap -->
		
		<div class="clear"></div>
		
		<div id="footer">
			<div id="footer-in">
				footer
			</div> <!-- end #footer-in -->
		</div> <!-- end #footer -->
	</div> <!-- end div#container -->
</body>
</html>
</#escape>