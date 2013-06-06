

Tutorial 4.2. Correlation of expression patterns in different tissues
-------------------------------------------------




Table of Contents
-------------------------------------------------

`Compare the expression profile of samples using correlations <#N10037>`__  `#  <#N10037>`__

`Files needed: <#N1003D>`__  `#  <#N1003D>`__

`Perform correlation analysis <#N1006C>`__  `#  <#N1006C>`__

`Colour the columns and rows according to the class label of the tissue samples and order them <#N1008C>`__  `#  <#N1008C>`__

`Explore the results <#N100A9>`__  `#  <#N100A9>`__







Compare the expression profile of samples using correlations
-------------------------------------------------



Files needed:
-------------------------------------------------

We will need the same files as in the tutorial 4.1:   `Edit <http://help.gitools.org/xwiki/bin/create/..%2F..%2F..%2F..%2F./%2Fbin%2Fedit%2FTutorials%2FTutorial41%3Fsection%3D2?parent=xwiki%3ATutorials.Tutorial42>`__

`Gene Atlas expression in Entrez IDS <url('file:/usr/local/gitools/help/xwiki-enterprise-jetty-hsqldb-2.5/jetty/work/Jetty_0_0_0_0_8888_xwiki__xwiki__snanx9/xZCwRsUx/Tutorials.Tutorial41.gse1133-entrez-log2-abs-reading.mediancentered.cdm.gz')>`__  which contains median-centered log-intensity values divided by standard deviation for 79 samples.

`Gene Atlas sample annotations <url('file:/usr/local/gitools/help/xwiki-enterprise-jetty-hsqldb-2.5/jetty/work/Jetty_0_0_0_0_8888_xwiki__xwiki__snanx9/xZCwRsUx/Tutorials.Tutorial41.gse1133-annotation-full.tsv')>`__  which contains the annotation of samples.



Perform correlation analysis
-------------------------------------------------

See  `this chapter <UserGuide_Correlations.rst>`__  for details on how to perform correlation analysis

Select gse1133-entrez-log2-abs-reading.mediancentered.cdm.gz as data file

Do not select any filtering option

Apply correlation to columns (as we want to correlate samples)

Give a name to the analysis. Select a directory where to safe it and click Finish.



Colour the columns and rows according to the class label of the tissue samples and order them
-------------------------------------------------

In the analysis details tab, click on “heatmap” under “Results” to view the heatmap of the results.

In properties/columns, load the file “gse1133-annotation-full.tsv” under Annotations and click Filter.

Go to “Add” under “Headers”, choose “Colored labels from annotations”. Choose “class” as label to show the type of tissue instead of the id of the sample as column name in the heatmap.

Do the same for rows.

Uncheck the “Grid between different clusters”.

Sort the samples by class by selecting Data>Sort>Sort by label and select columns>class.

Change the width of the cells in properties/cells to be able to see all the samples in the window and uncheck the option to show the columns grid.



Explore the results
-------------------------------------------------


