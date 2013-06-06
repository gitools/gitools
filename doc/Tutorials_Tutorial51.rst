

Tutorial 5.1. Regulatory Modules enrichment analysis
-------------------------------------------------




Table of Contents
-------------------------------------------------

`Study the misregulation in different cancer types of modules derived from binding experiments such as ChIP-sequencing <#N10037>`__  `#  <#N10037>`__

`Files needed: dit <#N1003D>`__  `#  <#N1003D>`__

`Perform enrichment analysis <#N10075>`__  `#  <#N10075>`__

`Use annotations for cancer types <#N100A5>`__  `#  <#N100A5>`__

`Explore the results <#N100B9>`__  `#  <#N100B9>`__







Study the misregulation in different cancer types of modules derived from binding experiments such as ChIP-sequencing
-------------------------------------------------



**Files needed:  **\ `**dit** <http://help.gitools.org/xwiki/bin/create/..%2F..%2F..%2F..%2F..%2Fbin%2Fcreate%2F..%252F..%252F..%252F..%252F.%2F%252Fbin%252Fedit%252FTutorials%252FTutorial41%253Fsection%253D2%3Fparent%3Dxwiki%253ATutorials/Tutorial42?parent=xwiki%3ATutorials.Tutorial51>`__

Significantly down-regulated genes in each tumour morphology type from IntOGen, which contains p-values for cancer driver genes. To obtain it follow  `tutorial 1.1 <url('file:/usr/local/gitools/help/xwiki-enterprise-jetty-hsqldb-2.5/jetty/work/Jetty_0_0_0_0_8888_xwiki__xwiki__snanx9/eK8qnQ17/Tutorials.WebHome.tutorial11importintogentumortypes.pdf')>`__ , but change the type of alteration to “downreg”.

Set of genes (module) experimentally found to be bound by specific transcription factors. We will use the set from  `Boyer et al 2005 <http://www.sciencedirect.com/science/article/pii/S0092867405008251>`__  (Supplementary Table 3), which has been already mapped into Ensembl gene IDs for this tutorial  `here <url('file:/usr/local/gitools/help/xwiki-enterprise-jetty-hsqldb-2.5/jetty/work/Jetty_0_0_0_0_8888_xwiki__xwiki__snanx9/eK8qnQ17/Tutorials.Tutorial51.boyer.tcm')>`__ .



Perform enrichment analysis
-------------------------------------------------

See  `this tutorial <url('file:/usr/local/gitools/help/xwiki-enterprise-jetty-hsqldb-2.5/jetty/work/Jetty_0_0_0_0_8888_xwiki__xwiki__snanx9/eK8qnQ17/Tutorials.WebHome.tutorial13runenrichmentIntOGenKEGG.pdf')>`__  for details on how to perform enrichment analysis.

Select the IntOGen down-regulated cancer genes as data file, and transform the continuous data matrix to a binary one by choosing a transformation criterion of “less that 0.05”, since the matrix contains p-values.

Select the experimental binding sites file as module file (boyer.tcm).

Select binomial statistical test. Leave estimator and multiple test correction as default.

Give a name to the analysis. Select a directory where to save it and click Finish.

If you have a memory problem, see memory configuration in (  `Installation  <UserGuide_Installation.rst>`__ ) to increase the memory allocated to run Gitools.



Use annotations for cancer types
-------------------------------------------------

In the analysis details tab, click on “heatmap” under “Results” to view the heatmap of the results.

In properties/columns, select the file for IntOGen column annotations and choose “topography” as label to show the name of the cancer type instead of the id of the sample in the heatmap.

Sort the samples by cancer type by selecting Data>Sort>Sort by label and select columns > “topography”.

Change the width of the cells in properties/cells to be able to see all the samples in the window and adapt the header size in the columns.



Explore the results
-------------------------------------------------


