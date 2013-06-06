

Tutorial 2.5: Overlapping analysis
-------------------------------------------------




Table of Contents
-------------------------------------------------

`Compare the overlapping of significantly up-regulated genes in our experiment and in other lung experiments imported from IntOGen. <#N10037>`__  `#  <#N10037>`__

`Files needed <#N1003D>`__  `#  <#N1003D>`__

`Run overlapping analysis <#N1006F>`__  `#  <#N1006F>`__

`Change labels of rows and columns <#N10093>`__  `#  <#N10093>`__

`Change the value to be displayed in the heatmap <#N100A1>`__  `#  <#N100A1>`__

`Explore the results <#N100AC>`__  `#  <#N100AC>`__







Compare the overlapping of significantly up-regulated genes in our experiment and in other lung experiments imported from IntOGen.
-------------------------------------------------



Files needed
-------------------------------------------------

The files needed for this tutorial are the same as the ones for tutorial 2.4.

`intogen-lung-upreg-plus-gse19188.cdm.gz <url('file:/usr/local/gitools/help/xwiki-enterprise-jetty-hsqldb-2.5/jetty/work/Jetty_0_0_0_0_8888_xwiki__xwiki__snanx9/PVxzDkGf/Tutorials.Tutorial25.intogen-lung-upreg-plus-gse19188.cdm.gz')>`__  : matrix file with p-values for up-regulation per gene for 11 experiments imported from IntOGen plus the experiment that we analyzed in the previous tutorial.

`intogen-lung-upreg-columns.tsv.gz <url('file:/usr/local/gitools/help/xwiki-enterprise-jetty-hsqldb-2.5/jetty/work/Jetty_0_0_0_0_8888_xwiki__xwiki__snanx9/PVxzDkGf/Tutorials.Tutorial25.intogen-lung-upreg-columns.tsv.gz')>`__ : file with annotations of columns (experiments) for the matrix above

`intogen-lung-upreg-rows.tsv.gz <url('file:/usr/local/gitools/help/xwiki-enterprise-jetty-hsqldb-2.5/jetty/work/Jetty_0_0_0_0_8888_xwiki__xwiki__snanx9/PVxzDkGf/Tutorials.Tutorial25.intogen-lung-upreg-rows.tsv.gz')>`__ : file with annotations of rows (genes) for the matrix above



Run overlapping analysis
-------------------------------------------------

See  `this chapter <UserGuide_Overlaps.rst>`__  for details on how to perform overlapping analysis

We will do an analysis to see how many genes that are significantly up-regulated in each of the experiments are also significantly up-regulated in the other experiments.

Select intogen-lung-upreg-plus-gse19188.cdm.gz as data file

In Data Filtering Options choose “Transform to 1 (0 otherwise) cells with values less than 0.05”

In Configure Overlapping Options, select: Replace empty values by 0. Apply to columns. Click Next.

Select a destination folder and file name and click Finish.



Change labels of rows and columns
-------------------------------------------------

In properties/columns, select the file “intogen-lung-upreg-columns.tsv.gz” and choose “authors” and “year” as label to show in the columns instead of the id of the experiment.

Do the same for rows.



Change the value to be displayed in the heatmap
-------------------------------------------------

In properties/cells, select the value “Row only proportions” to show in colors the proportion of genes in the experiment of the column that are up-regulated in the experiment of the row.



Explore the results
-------------------------------------------------


