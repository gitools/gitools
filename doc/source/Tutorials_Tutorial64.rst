================================================================
Tutorial 6.4: Sample Level Enrichment Analysis (SLEA)
================================================================

Nuria Lopez-Bigas



Table of Contents
-------------------------------------------------

`Concept <#N1003B>`__  `#  <#N1003B>`__

`How to use Gitools for this task <#N10070>`__  `#  <#N10070>`__







Concept
-------------------------------------------------

Cancer is by nature very heterogeneous: The genomic alterations of one cancer patient can be very different compared with another patient. This is the case for different cancer types AND more importantly for this tutorial cancer samples can be very heterogenous although they are from the same tumor site. For this reason it is important to obtain detailed clinical information about a sample.

Having a the expression data of a cohort of patients, it is not very convenient to compare the expression differences on a sample-gene basis. In a first step the genes may be grouped into modules like f.ex. pathways, so they can be analysed as one unit. This is exactly what we propose with the *Sample Level Enrichment Analysis* (or short SLEA). With this analysis we can asses the transcriptional status of each pathway (or other modules) for each sample. In a second step we can relate the enrichment status with the clinical annotation, like cancer subtypes.

To see how it is done, watch the  `video tutorial <http://www.youtube.com/watch?v=EADA6TsGrVw>`__    and/or read the instructions below



How to use Gitools for this task
-------------------------------------------------

Make sure you have the data provided in  `step 1 <Tutorials/Tutorial61>`__  of this use case. If you don’t have it yet, download this  ` zip-file <http://www.gitools.org/tutorials/data_gitools.rst_case_study_6.zip>`__ , which includes:

Glioblastoma cancer data matrix. 

TP53 signalling-pathway gene list

Gene annotation file 

Then  follow the steps below.

Open Gitools

Click on the Enrichment Analysis button in the Welcome Tab 

A wizard will open. 

In the first step, select **glioblastoma.integrated.multmatrix.tdm** as the input file and then select as value the **median-centered expression value**.

In the second step, we leave everything blank, as we need no filtering options

In the third step, we choose the file that maps our modules (gene groups) to pathways. The files name is ** kegg2ensg.ensembl62.chosenpathways.gmt** and it contains the gene mapping for five pathways.

In the fourth step, we select input method the ** Z-score** since our data is continuous (Fisher and Binomial are used for binary data). Also, since it is only an example we **reduce the sampling size to 100 or 1000 **\ so it is executed in a shorter time.

In the fifth and sixth step we select where to save the result and give the analysis a title.

A new tab will open with the analysis summary.

Select to **open the heatmap** in the Results section.

We now can see that the five pathways we had in the modules file are now each represented as a row. If the pvalue is displayed we can see which modules are significantly different in expression.

To discriminate between over- and under-expressed pathways we select to **display the Z-Score** value (in the Cell Properties tab)

Now we can see in red the over and in blue the under-expressed pathways. But no clear pattern is  visible. Thus we choose to add clinical annotations for the samples:

Choose to **load** the annotation file: **gbm.clinical-annotation.tsv**.

Once it is loaded we click on add a header below, and choose to ** add a Color Label for the annotation subtype. **

Now if we sort (Data -> Sort -> by label -> Columns by ‘subtype **’) the columns by subtype we can see that some over and under-expression of pathways es quite specific to the glioblastoma subtype. **

Now if some specific pathway has gotten your interest, you can click the button on the top right of the heatmap to show all the genes of that pathway with the original expression data associated.
