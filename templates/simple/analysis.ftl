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
 	<#list views as view>
 		<#compress>
	 		<#include "view-${view}">
		</#compress>
	</#list>
 </body>
</html>
</#escape>