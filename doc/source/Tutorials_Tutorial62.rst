

Tutorial 6.2. Finding and visualizing mutually exclusive genes
-------------------------------------------------

Michael P Schroeder



Table of Contents
-------------------------------------------------

`Concept <#N1003B>`__  `#  <#N1003B>`__

`How to use Gitools for this task dit <#N10088>`__  `#  <#N10088>`__







Concept
-------------------------------------------------

We want to see if in a pathway - or theoretically in any group of genes - we can see a pattern of mutual exclusion. This is easily done with the *Sort by Mutual Exclusion* function in Gitools. In this tutorial we show you how this is done.

Methods have been developed to discover mutual-exclusive relationships between genes. We will take an example from the paper (  `Ciriello et. al 2011 <http://genome.cshlp.org/content/22/2/398.full>`__ ) and try reconstruct the result with an integrated cancer data matrix, pathway information and Gitools.

In figure 2C of the paper we can see that four genes of the  `p53-signalling pathway <http://www.genome.jp/kegg/pathway/hsa/hsa04115.html>`__  are detected as mutual exclusive in a significant manner: CDKN2A, MDM2, MDM4 and TP53. This is a great result, as you can see if you take look at the  `p53-signalling pathway <http://www.genome.jp/kegg/pathway/hsa/hsa04115.html>`__ , since they are all upstream and their losses and gains are concordant in downstream effect (theoretically).

Watch the  `video tutorial <http://www.youtube.com/watch?v=rIvBN_iw6rs>`__  and/or read the instructions below



How to use Gitools for this task
-------------------------------------------------

`**dit** <http://help.gitools.org/xwiki/bin/create/..%2F..%2F..%2F..%2F..%2Fbin%2Fcreate%2F..%252F..%252F..%252F..%252F.%2F%252Fbin%252Fedit%252FTutorials%252FTutorial41%253Fsection%253D2%3Fparent%3Dxwiki%253ATutorials/Tutorial42?parent=xwiki%3ATutorials.Tutorial62>`__

Load the alteration matrix ( by * File -> Open -> Heatmap*) into Gitools. Select the *Alteration* as cell value to be shown in the heatmap (  `this is how you do it <UserGuide_HowtoMultiDimensionalData.rst>`__ ).

Select the *Linear two-sided color scale*, so you can distinguish loss from gain which are designated by the values -2 and 2.

Visualizing p53-signalling pathway
-------------------------------------------------

In the tutorial data, we have prepared a file containing all the Ensembl-gene ids for the genes that are in the p53-signalling pathway.

To filter by this genes, select in Gitools the menu *Data* -> *Filter* -> *Filter by label*. Select *Rows* and then click the *Load*-Button next to the text area to load the file we prepared and then hit OK. You should now see the matrix with only the genes of the said pathway.

Sort by mutual exclusion
-------------------------------------------------

If you take a look at the pathway, you can see that there are four “main” upstream genes in this pathway: **CDKN2A**, **MDM2**, **MDM4** and **p53**. Select them - if you cannot find them instantly use the search button on top right of the matrix.

After having them selected, select the menu *Data *  ->  *Sort *  ->  *Sorty by mutual exclusion * . The selected genes should appear pre-filled in the text-area. Hit Ok and scroll to the top of the matrix. As you can see, these genes show a patter of mutual exclusion, and that the gains and losses of the genes are concordant in effect.

Hint: Can you find another upstream gene that even  extends this pattern of mutual exclusion by a few samples?
