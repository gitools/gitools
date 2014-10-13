========================================
Mutual exclusion and co-occurrence test
========================================

.. contents::

There are two scenarios where Gitools can perform a mutex or co-oc test: 

#. When sorting heatmap rows according to mutual exclusivity of :doc:`UserGuide_DataEvents` it is possible to also carry
   out a test of significance for the mutual-exclusive or co-occurring distribution of those events
#. Having row and/or column annotation which describe subgroups of each dimension allow to carry out a test for 
   each combination of column-row group via the Analysis - menu

Data files needed
----------------------------------------------
**Data Matrix**: A file with either binary (i.e. presence/absence), categorical (i.e. alteration status) or continuous (i.e. an expression matrix) data.


Running the analysis
-----------------------

#. Open the data matrix as heatmap
#. Make sure that your data is shown with an accurate color scale.
#. Select ``Edit->Rows->Sort by mutual exclusion``.
#. Add the id's of the items you want to sort and select ``perform statistical test``

Things to consider

    **The test**

        The test is based on weighted permutations assessing the deviation of the observed coverage
        (number of columns with a signal) compared to expected obtained by permuting events, maintaining the number
        of events per row and weighted permutations for columns.

    **Data used**

        The column weights, used for the permutations are based on the "events" (:doc:`UserGuide_DataEvents`) in each column **including hidden rows**.
        The more rows that are included, the more accurate the weight parameter, therefore if you are performing the test
        on a dataset that contains only is a subset and rows are missing (e.g. not all genes present) this parameter may
        be inaccurate and the end result may be inaccurate.



The mutual exclusive result
----------------------------

The result of a test will yield the following values:

**Statistics**

    - **Z-score**: Zscore shows the deviation of the observed coverage (number of columns with a signal) compared to
      expected, obtained by permuting events, maintaining the number of events per row and weighted permutations for columns.
    - **MutEx p-value**: Significance p-value of mutual exclusivity derived from the Z-score
    - **Co-occurrence p-value**: Significance p-value of co-occurrence derived from the Z-Score

**Other measures**
    - **Signal**: Number of positive events (see :doc:`UserGuide_DataEvents`) within the data selected data.
    - **Coverage**: Number of columns with at least a signal of one.
    - **Sig/Cov Ratio**: Ratio of Signal to Coverage
    - **Mean coverage**: Mean coverage of the 10'000 permutations
    - **Variance**: The variance of the coverage from the 10'000 permutations

