<#escape x as x?html>
    <?xml version="1.0"?>
    <!DOCTYPE html
    PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "DTD/xhtml1-transitional.dtd">

    <html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <#if report.title?size gt 0>
            <title>${report.title}</title>
        </#if>

        <link href="style/reset.css" rel="stylesheet" type="text/css" media="all"/>
        <link href="style/default.css" rel="stylesheet" type="text/css" media="screen"/>
        <link href="style/styling.css" rel="stylesheet" type="text/css" media="screen"/>
        <link href="style/print.css" rel="stylesheet" type="text/css" media="print"/>
    </head>
    <body class="fullwidth">
    <div id="container">
        <div id="header">
            <div id="header-in">
                <h1>Report</h1>
                <#if report.title?size gt 0>
                    <h4>${report.title}</h4>
                </#if>
            </div>
            <!-- end #header-in -->
        </div>
        <!-- end #header -->

        <div id="content-wrap" class="clear">
            <div class="content">
                <div class="content-in">

                    <#if report.properties.property?size gt 0>
                        <h2>Properties</h2>
                        <table class="ptab">
                            <#list report.properties.property as prop>
                                <tr class="ptabr">
                                    <th class="ptabh">${prop.@name}</th>
                                    <td class="ptabd">${prop.@value}</td>
                                </tr>
                            </#list>
                        </table>
                    </#if>

                    <h2>Analyses</h2>
                    <table class="atab">
                        <tr class="atabr">
                            <th class="atabh">Name</th>
                            <th class="atabh">Method</th>
                            <th class="atabh">Elapsed time</th>
                        </tr>
                        <#list report.analyses.analysis as analysis>
                            <#assign desc = loadAnalysisDescription(analysis.@path) />
                            <#assign fileName = "analysis" + analysis_index + "_" + desc.analysisName + ".html" />
                            <@process template="analysis.ftl" output="${fileName}"
                            analysisIndex="${analysis_index}" />
                            <tr class="atabr">
                                <td class="atabd"><a target="_blank" href="${fileName}">${desc.analysisName}</a></td>
                                <td class="atabd">${desc.methodName}</td>
                                <td class="atabd">${desc.elapsedTime}</td>
                            </tr>
                        </#list>
                    </table>

                </div>
                <!-- end .content-in -->
            </div>
            <!-- end .content -->
        </div>
        <!-- end #content-wrap -->

        <div class="clear"></div>

        <div id="footer">
            <div id="footer-in">
                <#if report.url?size gt 0 && report.laboratory?size gt 0>
                    <h4><a href="${report.url}">${report.laboratory}</a></h4>
                </#if>
            </div>
            <!-- end #footer-in -->
        </div>
        <!-- end #footer -->
    </div>
    <!-- end div#container -->
    </body>
    </html>
</#escape>