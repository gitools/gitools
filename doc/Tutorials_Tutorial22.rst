

Tutorial 2.2. Expression profiles correlations
-------------------------------------------------




Table of Contents
-------------------------------------------------

`Compare the expression profile of samples using correlations <#N10037>`__  `#  <#N10037>`__

`Files needed: <#N1003D>`__  `#  <#N1003D>`__

`Perform correlation analysis <#N1005E>`__  `#  <#N1005E>`__

`Color the columns and rows according to the histology label of the samples and order them <#N1007E>`__  `#  <#N1007E>`__

`Explore the results <#N10098>`__  `#  <#N10098>`__







Compare the expression profile of samples using correlations
-------------------------------------------------



Files needed:
-------------------------------------------------

We will need the same files as in the tutorial 2.1:

`gse19188\_median-centered.cdm.gz <http://www.gitools.org/tutorials/data/gse19188_median-centered.cdm.gz>`__ : which contains median-centered log-intensity values divided by standard deviation for 156 samples. 

`gse19188\_sample\_annotations.txt <http://www.gitools.org/tutorials/data/gse19188_sample_annotations.txt>`__ : which contains the clinical annotation of samples



Perform correlation analysis
-------------------------------------------------

See  `this chapter <UserGuide_Correlations.rst>`__  for details on how to perform correlation analysis

Select gse19188\_median-centered.cdm.gz as data file

Do not select any filtering option

Apply correlation to columns (as we want to correlate samples)

Give a name to the analysis. Select a directory where to safe it and click Finish.



Color the columns and rows according to the histology label of the samples and order them
-------------------------------------------------

In the analysis details tab, click on “heatmap” under “Results” to view the heatmap of the results.

In properties/columns, select the file “gse19188\_sample\_annotations.txt” and choose “histology” as label to show the type of tumour instead of the id of the sample as column name in the heatmap.

Select annotate with color to show a color label for the type of histology of the samples.

Do the same for rows.

Order the samples by histology by selecting Data>Sort>Sort by label and select columns.

Change the width and height of the cells in properties/cells to be able to see all the samples in the window and uncheck the options to show the columns and row grid.



Explore the results
-------------------------------------------------


