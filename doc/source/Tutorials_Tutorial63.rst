

Tutorial 6.3. Exploring the effect of genomic alterations on expression
-------------------------------------------------

Michael P Schroeder



Table of Contents
-------------------------------------------------

`Concept <#N1003B>`__  `#  <#N1003B>`__

`How to use Gitools for this task <#N1005D>`__  `#  <#N1005D>`__

`The Group Comparison Wizard - data and test <#N1007F>`__  `#  <#N1007F>`__







Concept
-------------------------------------------------

Cancer cells often exhibit an altered number of copies of certain genomic regions when compared to normal cells (Copy Number Alterations: CNAs). A few of these CNAs may have a direct influence on the expression of genes in the affected region. The change in the number of copies of a gene may be both positive, when additional copies are gained (and the genes thus amplified) or negative, when one or more alleles of the gene are lost. The influence of CNAs on the expression of these amplified or lost genes depends on whether it occurs hetero- or homozygously and also on other regulatory factors which may override the effect of the alteration. Therefore, an essential step to verify the importance of the amplification or deletion of a given gene in the tumorigenic process is to verify if its expression tends to respond to its genomic alterations.

In the matrix we have loaded in step one of the case study we have homozygous loss and multicopy gain - or loss in one allele plus a mutation in the other. With some easy steps we can verify in Gitools which genes are influenced significantly by the alteration in their locus.

Watch the  `video tutorial <http://www.youtube.com/watch?v=HPPHy5LNSBY>`__  and/or read the instructions below



How to use Gitools for this task
-------------------------------------------------

Load the data as described in  `step 1 <Tutorials_Tutorial62.rst>`__   or filter the matrix for your genes in interest and you are ready.



The Group Comparison Wizard - data and test
-------------------------------------------------

To analyse which genes are affected by alterations in their locus we have to make a Group Comparison Analysis. We select it by *Analysis* -> *Group Comparison.*

The first two things we have to select are:

What data values we want to compare for the genes (rows)?

Which sample groups (columns) do we want to compare?

The answers are:

We want to compare expression values (median-centered)

We want to compare two groups: altered vs non-altered for each gene.

Thus we will select for the first box: “ *expr* *median-centered*\ ” and “ *Group by value*\ ”. For the other boxes regarding the statistics, we can just leave the default selection and click “ *Next >*\ ”.



The Group Comparison Wizard - data and test
-------------------------------------------------

In the second step we choose how the groups to compare with each other are made.

The separating dimension is the alteration, so we choose *alteration* for the first field. 

Since gain and loss are designated by -2.0 and 2.0, we will choose that all samples that have an alteration value *absolute equal to 2.0* for Group 1

For the group two, we want all the samples that have no alteration, thus we select: equal to 0.0

Now we select the “ *Next >*\ ” button.



The Group Comparison Wizard - Running the analysis
-------------------------------------------------

As a last step we can choose to set a title and hit the *Finish *\ Button. The analysis will be run and a new tab opens in Gitools showing the report of the analysis. 

In the report we select the the *Heatmap* button in the Result section. A new tab will open with the result where we see one column of p-values - for each gene. 

Genes whose expression are influenced by loss will show a **left tail** significance, the genes whose expression are influenced by gain will show a **right-tail** significance. If you want to sort the result, use the two-tail value, if you want to filter, filter for both left and right tail signficiance.

Hint: In the details panel you can see how big the groups to compare of each gene were.
