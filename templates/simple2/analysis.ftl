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
	<#assign analysis = report.analyses.analysis[index][0] />
	<#assign desc = loadAnalysisDescription(analysis.@path) />
	
	<div id="container">

		<div id="header">
			<div id="header-in">
				<h1>Analysis</h1>
				<#if analysis.@title?size gt 0>
					<h4>${analysis.@title}</h4>
				</#if>
			</div> <!-- end #header-in -->
		</div> <!-- end #header -->

		<div id="content-wrap" class="clear">
			<div class="content">
				<div class="content-in">
				
					<h2>Description</h2>
					<table class="dtab">
						<tr class="dtabr">
							<th class="dtabh">Name</th>
							<td class="dtabd">${desc.analysisName}</td>
						</tr>
						<tr class="dtab">
							<th class="dtah">Method</th>
							<td class="dtad">${desc.methodName}</td>
						</tr>
						<tr class="dtar">
							<th class="dtah">Elapsed time</th>
							<td class="dtad">${desc.elapsedTime}</td>
						</tr>
					</table>
					
					<#if analysis.properties.property?size gt 0>
						<h2>Properties</h2>
						<table class="ptab">
							<#list analysis.properties.property as prop>
								<tr class="ptabr">
									<th class="ptabh">${prop.@name}</th>
									<td class="ptabd">${prop.@value}</td>
								</tr>
							</#list>
						</table>
					</#if>
					
					<#if analysis.views.view?size gt 0>
						<h2>Views</h2>
						<ul>
						<#list analysis.views.view as view>
							<#if view.@title?size gt 0>
								<#assign viewTitle = view.@title />
							<#else>
								<#assign viewTitle = view.@name />
							</#if>
							<li>
								<a href="#view${view_index}">${viewTitle}</a>
							</li>
						</#list>
						</ul>
					</#if>
					
					<#assign results = loadAnalysisResults(analysis.@path) />
					
					<#list analysis.views.view as view>
						<h3><a name="view${view_index}"></a>${viewTitle}</h3>
						<#include "view-${view.@name}.ftl" />
					</#list>
					 
				</div> <!-- end .content-in -->
			</div> <!-- end .content -->
		</div> <!-- end #content-wrap -->
		
		<div class="clear"></div>
		
		<div id="footer">
			<div id="footer-in">
				<#if report.url?size gt 0 && report.laboratory?size gt 0>
					<h4><a href="${report.url!""}">${report.laboratory!""}</a></h4>
				</#if>
			</div> <!-- end #footer-in -->
		</div> <!-- end #footer -->
	</div> <!-- end div#container -->
</body>
</html>
</#escape>