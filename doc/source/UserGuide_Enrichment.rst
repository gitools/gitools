===========
Enrichment
===========


.. contents::

Enrichment analysis consists in a quantitative measure to infer if the values of a biological condition (e.g. fold changes) shows statistically significant, concordant values or increased proportion for a particular set of biological annotations (module or gene set).

Data files needed
----------------------------------------------

Before running an enrichment analysis you should have this data prepared in files:


**Data Matrix**: A file with either continuous (i.e. an expression matrix) data or binary data (i.e. genes or probesets absence/presence).

**Modules**: A file grouping the rows which are analyzed as a module (i.e. gene sets related by Gene Ontology biological process).
    See :doc:`UserGuide_SpecialFileExtensions` to know the module file formats.
    Note that it is possible to download modules from within Gitools. See: :doc:`UserGuide_ExternalDataSources`

**Population elements**: Opionally, a file which is a list of all elements (i.e. probesets or genes) that conform the background
population of your analysis. For example, in the case of an affymetrix microarray the population would be all the
probesets of the microarray. This is not necessary if you have a matrix with data for the whole population.

Running the analysis
-----------------------

#. Open the data matrix as heatmap
#. Select ``Analysis->Enrichment..`` and follow the steps in the wizard.

Things to consider:

    **Omitting small/big modules**

        If the number of elements annotated for one module is too low some tests could not generate reliable results (i.e. zscore or bionamial tests), on the other hand there are tests best suited for small modules like fisher’s exact test. It is possible to discard the modules with less or more than a certain quantity using this filters.

    **Selection of statistical test**

        - Use the **Fisher Exact** or **Binomial** statistical test if you have binary data.
            - You may **transform** (binarize) continuous data during the wizard
        - Select the **Z score** test for continuous data

        See also:
        `Binomial test <http://en.wikipedia.org/wiki/Binomial_test>`__ ,
        `Fisher’s exact test <http://en.wikipedia.org/wiki/Fisher's_exact_test>`__ ,
        `Z-score test <http://en.wikipedia.org/wiki/Z-test>`__  with  `bootstrapping <http://en.wikipedia.org/wiki/Bootstrapping_(statistics)')>`__

    **Multiple test correction**

        As multiple tests are performed it is important to adjust the p-values.
        The method for adjusting the p-values can be chosen in the Multiple test correction option.

    **Filter rows not present in modules**

        Sometimes is convenient to restrict the background population to only those elements belonging to any module, for example, the data file could have information for all the genes of a microarray but only the genes in your data with GO biological process annotations should be considered for the background.

    **Transform to 1 / 0:**

        Some statistical tests are designed to work with discrete events (as Binomial or Fisher’s exact tests), this option allows to transform a matrix with real values into a binary matrix containing only 1’s and 0’s for the analysis. All the values which satisfy the condition will be transformed to 1 and the rest to 0. For example, if the data file is an expression matrix with log :sub:`2` ratios it can be transformed to a binary matrix having a 1 for all the log :sub:`2` ratios greater than 1.5. Other possible application is when the matrix have p-values, if a significance of 0.05 is considered, all values less than 0.05 could be transformed to 1’s.

    **Population / Background elements:**

        This field can be left blank if the data file contains data for all background elements, otherwise a file with the elements of the background population should be selected. The format of this file is a simple text file with one element per line. For example the list of all the protein coding genes, each one in a different row. All the rows in the data that don’t appear in the population will be removed and all the elements specified in the population that don’t appear in the data will be added to the data with the default value specified by the user.



The Enrichment Results
-------------------------

After performing the analysis a new editor with the details of the analysis will be opened:

To explore the data matrix resulting from the transformations with a heatmap click on the **Heatmap** button in the **Data** section.

To explore the results of the analysis with a heatmap click on the **Heatmap** button in the **Results** section.


