================
Group Comparison
================


.. contents::

A group comparison lets you compare groups of columns and perform a statistical test for each row in order to
assess their dissimilarity (e.g. differential gene expression).


Data files needed
----------------------------------------------
**Data Matrix**: A file with either continuous (i.e. an expression matrix) data or binary data (i.e. genes or probesets absence/presence).

Optionally, **annotations**: Annotation for rows or columns (e.g. clinical sample details, gene properties) which helps to group the rows and columns.


Running the analysis
-----------------------

#. Open the data matrix as heatmap
#. Select ``Analysis->Group Comparison..`` and follow the steps in the wizard.

Things to consider

    **Data to compare**

    Select which data layer you want to comapre in case you have loaded multiple.

    **Group dimension**

        Select which dimension you will be grouping: columns or rows. You may assess differences between samples (e.g. in columns)

    **NA values**

        It is important to consider the meaning of NA values. By default NA values in the comparison data will be discarded.

    **Group by annotation, values or without constraint**

        If you are grouping columns, for each row the comparison will be carried out. If for each row the groups shall be
        same for all the rows (e.g. grouping by a clinical annotation), you may want to group the columns by *annotation* or
        *without constraint*. If the groups depend on values in another data layer (e.g. CNA or mutation status) you may want to
        choose *group by value*. In this scenario, the grouping of columns will vary for each row that is compared.

    **Group size**

        The groups must have a sample size of at lest 3.

    **Mann-Whitney-Wilcoxon Test**
        The Mann-Withney-Wilcoxon compares  is a non-parametric test of the null hypothesis that two populations are the same
        against an alternative hypothesis, especially that a particular population tends to have larger values than the other. [1]_

Selecting groups
..................

**Group by annotation values**

    Group by annotation, e.g. group together all samples that belong to the same gender and compare them. You need to have
    annotations loaded or load them in the process. The groups do not vary: If you group together columns, for each row
    the same column groups will be compared.

**Group by heatmap vaules**

    - When defining a grouping by values you should select a **data layer** *different* to the data layer that is being comapred.
    - You may combine different criteria for one group. E.g. ``mutation status == 1`` or ``CNA status == -2.0``
    - It is important to consider **empty conversion**. If the criteria is ``mutation status != 1`` the empty values may
      be converted to 0 in order not to discard them. By default, null values are not included in the group.

**Group without constraint**

    The grouping without constraints is very similar to the group by annotation values with the exception that the
    user defines all the groups manually.


The Group comparison result
----------------------------

The created groups are being compared with each other in pairs. The results heatmap will contain one column
for each pair, where the first column header shows Group 1 and the second shows Group 2.

For each row and group pair the group comparison is carried out and will yield the following values:

**Statistics**

    - **P-value log sum**: Yellow-to-red if values in Group 1 are significantly shifted
      to higher values than Group 2 and blueish in the in the case of the contrary. Right-tail significance
      yields a positive p-value log sum and left-tail significance yields a negative p-value log sum.
    - **(Corrected) right p-value**: Significance of hypothesis that Group 1 is higher than Group 2.
    - **(Corrected) left p-value**: Significance of hypothesis  that Group 1 is smaller than Group 2.
    - **(Corrected) two-tail p-value**: Group 1 and group Group 2 are different

**Group properties**
    - **Ns**: total or group-specific sample size, not including possible null values in the groups
    - **Means**: Mean values of group 1 and 2
    - **U1** and **U2** statistics for mann-whitney-wilcoxon test.


.. [1] Wikipedia article at: http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U
