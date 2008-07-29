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
	<div id="container">
		<div id="header">
			<div id="header-in">
				<h1>Report</h1>
			</div> <!-- end #header-in -->
		</div> <!-- end #header -->

		<div id="content-wrap" class="clear">
			<div class="content">
				<div class="content-in">
					<h2>Properties</h2>
					<table style="propTab">
						<tr>
							<th>Name</th>
							<th>Value</th>
						</tr>
						<#list report.properties.property as prop>
							<tr>
								<td>${prop.@name}</td>
								<td>${prop.@value}</td>
							</tr>
						</#list>
					</table>
					
					<h2>Analyses</h2>
					<table class="analysisTab">
						<tr>
							<td>Name</td>
							<td>Method</td>
							<td>Elapsed time</td>
						</tr>
						<#list report.analyses.analysis as analysis>
							<#assign desc = loadAnalysisDescription(analysis.@path) />
							<#assign fileName = "analysis" + analysis_index + "_" + desc.analysisName + ".html" />
							<@process template="analysis.ftl" output="${fileName}" 
								analysisIndex="${analysis_index}" />
								<tr>
								<td><a target="_blank" href="${fileName}">${desc.analysisName}</a></td>
								<td>${desc.methodName}</td>
								<td>${desc.elapsedTime}</td>
							</tr>
						</#list>
					</table>
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