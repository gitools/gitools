================================================================
Examples
================================================================




Gitools includes examples for each type of analysis. To this purpose, ready annotated data matrices are provided with the download, and example analyses perform semi-automatically following the “example” link in the first page of the wizard of for each analysis type. Given below is a page by page description of the example analyses.

`Enrichment analysis <#HEnrichmentanalysis>`__

`Oncodrive analysis <#HOncodriveanalysis>`__

`Correlations analysis <#HCorrelationsanalysis>`__

`Combinations analysis <#HCombinationsanalysis>`__

`Overlapping analysis <#HOverlappinganalysis>`__



Enrichment analysis
-------------------------------------------------

An enrichment analysis measures whether alterations observed for a data set at gene level group into pre-defined gene sets or modules. To this purpose, the values of all genes belonging to one module are integrated to one value per module which can be statistically significant or not. Modules with statistically significant alteration give information on altered biological functions in the data set. Frequently used lists of gene sets include the Gene Ontology (GO) Biological Processes, Molecular Function or Subcellular Location or the KEGG pathways. Through enrichment analysis, the number of input rows representing genes is reduced to a smaller number of rows representing modules, without modifying the number and label of the columns.

In the following example, we analyze which KEGG pathway modules are significantly up-regulated in different tumor types, starting from gene-by-gene expression data.

Presentation and example
-------------------------------------------------

We select the icon for Enrichment Analysis. In the following steps, the files and parameters for the analysis will be specified.

Select data source
-------------------------------------------------

In the first step, we select the data source, which is a continuous data matrix with labeled rows and columns containing p-values for genes (in rows) for 20 different tumor types (in columns). We load it into “File”. Since the file has the extension .cdm.gz, “Format” is set to Continuous data matrix automatically.

Select data filtering options
-------------------------------------------------

Before we start the analysis, there are several filtering options. First, a list of selected row IDs can be excluded from the input matrix prior to analysis. This option is not used in the example. We check the second box, “Transform to 1” and enter “less than” and 0.05. This part of the configuration determines the significance cutoff for the analysis. In this case, the input matrix contains p-values, therefore the threshold is set to less than 0.05. Finally, rows from the input matrix that do not have a match in the modules can be filtered out checking the third box. In the example this option is disabled.

Select modules
-------------------------------------------------

Next, we select the file that maps the genes to the KEGG pathway modules. This file is a two column table without header, therefore, we set “File format” to Two columns mapping. Optionally, modules can be filtered by size: Modules containing more or less than an arbitrary number of rows = elements can be omitted from the analysis. In the example, we enable the first option and set the minimum number of rows to 20 which is also the default setting.

Select statistical test
-------------------------------------------------

In the next step, we select the statistical test to be used for the enrichment analysis. For the example, we select Binomial (Bernoulli) which is also the default setting.

Select destination file
-------------------------------------------------

In this step, we indicate where to save the results of the analysis. We fill in a name and select the folder. For each enrichment analysis, Gitools will output 4 files: an analysis file \*.enrichment, a file summarizing the mapping between genes and modules \*-modules.ixm.gz, a results file \*-results.tdm.gz and a data file containing an binary intermediate of the analysis \*-data.cdm.gz.

Analysis details
-------------------------------------------------

Here, the user can add a title and free text notes that will be saved in the analysis file. Additional attributes like author, project etc. can be added at will. However, this step is optional and can be skipped. Click finish to perform the analysis.

Enrichment analysis results
-------------------------------------------------

A new tab in Gitools shows an overview of the analysis parameters. Clicking on the heat map button for results will another tab with the heat map displaying the results of the analysis.

Enrichment heatmap
-------------------------------------------------

The enrichment results heat map has the same columns as the original matrix; rows correspond to the gene modules, in our example KEGG pathways. By default, the color scale displays the right p-value from the enrichment test for each tumor type and module. Significant p-values show in yellow to red; cells with insignificant p-values remain grey. Some cells are white, indicating that in this particular tumor type, the minimum number of 20 genes in the module was not reached. Explore and customize the heat map as described. Select all columns and sort them clicking the “sort rows” icon in order to view the most significantly up-regulated modules across all tumor types. Select and sort any single column in order to compare one tumor type to the rest of the columns and to spot tumor-specific modules. Click on any cell to see details and values in the “Details” tab on the left part of the screen. Click on any cell and on “automatic update” in the lower part of the screen to see the list of genes inside the module and their value in the original matrix (reduced to 1=significantly altered or 0=not significantly altered). Further, perform clustering of the heat map by clicking on “clustering” in the “data” menu and select among several options. This example shows that several pathways (e.g. systemic lupus erythematosis, cell adhesion molecules or p53 signaling pathway) are strongly altered across many tumor types, while others are significant only in a single tumor type (e.g. olfactory transduction in lung).



Oncodrive analysis
-------------------------------------------------

An OncoDrive analysis can be used to identify genes that are altered more than expected by chance in a set of samples. It assists in distinguishing common key player genes from changes only observed in a particular sample. It has been designed to detect so-called driver genes in cancer, but is suitable to detect recurrent alterations at different levels across a large number of samples. Through oncodrive analysis, many input columns representing samples or experiments are reduced to one or a few columns, without modifying the number and label of the rows.

In the following example, we compute the driver potential of genes over-expressed in 60 samples comprising 2 subtypes of glioblastoma, classical and neuronal. All samples form part of the TCGA glioblastoma microarray experiment as described in PubMed ID 18772890. For easier accessibility, these example data contain only 60 samples and have been reduced to half of the probes of the original experiment.

Presentation and example
-------------------------------------------------

We select the icon for Driver Alterations Analysis. In the following steps, the files and parameters for the analysis will be specified.

Select data source
-------------------------------------------------

In the first step, we select the data source, which is a continuous data matrix with labeled rows and columns containing log2 fold-change values for 10050 probes (in rows) for 60 samples (in columns). We load it into “File”. Since the file has the extension .cdm.gz, “Format” is set to Continuous data matrix automatically.

Select data filtering options
-------------------------------------------------

Before we start the analysis, we define filtering options. First, a list of selected row IDs can be excluded from the input matrix prior to analysis. This option is not used in the example. We check the second box, “Transform to 1” and enter “greater or equal” and 1.094. This value has been determined statistically for the whole input matrix prior to Gitools analysis. It sets the significance cutoff for the analysis. Remember that in this case, the input matrix contains log2 fold-change values and we want to study significantly over-expressed genes. Therefore, the significance cutoff defines from which value a log2 fold-change is considered “significantly up-regulated” in this experiment. This value has to be adjusted by the user from case to case according to the type and range of data and the statistical test to be performed (see below).

Select sets of columns to be analysed independently
-------------------------------------------------

Optionally, we can include a tabular file indicating sets of columns of the input matrix to be analysed independently. The oncodrive analysis output will then be one column per set. Alternatively, all columns can be analysed together to give one single output column. In the example, we select a file which annotates each sample to a glioblastoma subtype, either “classical” or “neural”. This file is a two-column file without header with the sample names from the input matrix in the first column and the set names in the second column. We load this file into “File”. Since the file has the extension .tcm, “Format” is set to Two columns mapping automatically. Additionally, sets may be filtered by size: Sets containing more or less than an arbitrary number of rows = elements can be omitted from the analysis. These options are disabled in the example analysis.

Select statistical test
-------------------------------------------------

In the next step, we select the statistical test to be used for the oncodrive analysis. For the example, we select Binomial (Bernoulli) which is also the default setting. For multiple test correction, two methods are available, Benjamini Hochberg FDR and Bonferroni. For the example, we use the former method, which is also the default setting.

Select destination file
-------------------------------------------------

In this step, we indicate where to save the results of the analysis. We fill in a name and select the folder. For each oncodrive analysis, Gitools will output 4 files: an analysis file \*.oncodrive, a file summarizing the mapping between elements and sets (if applied) \*-modules.ixm.gz, the results matrix \*-results.tdm.gz and a data file containing an binary intermediate of the analysis \*-data.cdm.gz.

Analysis details
-------------------------------------------------

Here, the user can add a title and free text notes that will be saved in the analysis file. Additional attributes like author, project etc. can be added at will. However, this step is optional and can be skipped. Click finish to perform the analysis.

Oncodrive analysis results
-------------------------------------------------

The analysis.oncodrive file is opened in a new tab in Gitools, showing an overview of the analysis parameters and details. Clicking on the heat map button for Results will open another tab with the heat map displaying the results of the analysis.

Oncodrive heat map
-------------------------------------------------

The oncodrive heat map has one column for each set of columns selected during the analysis. Otherwise, one single column labeled “all data columns” will be output. Here, the columns are labeled after the two subtypes of glioblastoma we indicated: neural and classical. Rows are the same as in the input matrix. They are labeled with geneIDs; probe names of the Affimetrix HG U133A microarray are given in parenthesis. Note that several geneIDs repeat with different probe names. By default, the color scale displays the right p-value from the oncodrive test for each set and probe. Significant p-values show in yellow to red; cells with insignificant p-values remain grey. Explore and customize the heat map as described. Select one or both columns and sort them clicking the “sort rows” icon in order to view the most significantly up-regulated genes first. Click on any cell to see details and values in the “Details” tab on the left part of the screen. Click on any cell and on “automatic update” in the lower part of the screen to see the list of samples inside the subtype set and their values in the original matrix (reduced to 1=significantly altered or 0=not significantly altered).



Correlations analysis
-------------------------------------------------

A correlations analysis helps to get an overall measure of similarity between two vectors. GiTools calculates the Pearson Correlation Coefficient for all possible pairs of columns or rows in a data matrix. Through correlations analysis, each pair of input columns is collapsed to one single value represented in a heat map that has column labels in both dimensions. Original row labels are not visible any more.

In the following example, we analyze correlations between significantly down-regulated genes in 14 experiments assaying several subtypes of brain and kidney tumors.

Presentation and example
-------------------------------------------------

We select the icon for Correlations Analysis. In the following steps, the files and parameters for the analysis will be specified.

Select data source
-------------------------------------------------

In the first step, we select the data source, which is a continuous data matrix with labeled rows and columns containing p-values for genes (in rows) for 14 experiments (in columns). We load it into “File”. Since the file has the extension .cdm.gz, “Format” is set to Continuous data matrix automatically.

Select data filtering options
-------------------------------------------------

Optionally, a cutoff for binary processing can be selected. This will transform the input matrix to a matrix containing only 1 or 0 as values depending on the threshold. The correlation coefficient will be calculated from this binarized matrix. In the example analysis, this option is disabled.

Configure correlation options
-------------------------------------------------

We select the correlation method options: Empty values can be replaced by a user-defined value. For the example, we disable this option. Correlations can be calculated by columns (typically samples or conditions) which is the default, or by rows (typically genes or modules).

Select destination file
-------------------------------------------------

In this step, we indicate where to save the results of the analysis. We fill in a name and select the folder. For each correlations analysis, Gitools will output 3 files: an analysis file \*.correlations, a data file containing an intermediate of the analysis (binary if applied, identical to input otherwise) \*-data.cdm.gz and a results file \*-results.tdm.gz.

Analysis details
-------------------------------------------------

Here, the user can add a title and free text notes that will be saved in the analysis file. Additional attributes like author, project etc. can be added at will. However, this step is optional and can be skipped. Click finish to perform the analysis.

Correlations analysis results
-------------------------------------------------

A new tab in Gitools shows an overview of the analysis parameters. Clicking on the heat map button for results will open another tab with the heat map displaying the results of the analysis.

Correlations heatmap
-------------------------------------------------

Note that in the correlations heat map, both columns and rows label with the original column labels for a correlations analysis by columns. Correlations heat maps have their own scale in the range of -1 to 1, covering all possible values of the Pearson correlation coefficient. However, minimum and maximum can be adjusted manually. Along the diagonal, we find the self-to-self correlations which are all 1. Click on any cell to see details and values in the “Details” tab on the left part of the screen. Select a whole row or column and click on “Results” and “automatic update” in the lower part of the screen to see details for all the cells in a table. Move selected columns to group by tumor type. Note that moving columns automatically moves the corresponding row so as not to disturb the geometry of the heat map. This example illustrates that gene expression is more similar between experiments covering the same tumor type (kidney-kidney, brain-brain) than between two experiments from different tumor types (kidney-brain), as reflected by a different correlation value. Note that there are still remarkable differences within one tumor type, due to differences in experimental platform, sample selection etc. A correlations analysis is therefore ideal to mark the beginning to more in-depth studies of a particular data set.



Combinations analysis
-------------------------------------------------

Combinations analysis can be useful, when we want to integrate a number of experiments that comprise similar samples, e.g. from the same tumor tissue type. The result of the combinations analysis is one value per gene and tumor tissue originating from the combination of all values for the same gene in different experiments. Through combinations analysis, many input columns representing samples or experiments are reduced to one or a few columns, without modifying the number and label of the rows.

In the following example, we deduce a generic profile for significant gene up-regulation in lung and breast tumor from 19 independent lung and 10 breast tumor experiments.

Presentation and example
-------------------------------------------------

We select the icon for Combinations Analysis. In the following steps, the files and parameters for the analysis will be specified.

Select data source
-------------------------------------------------

In the first step, we select the data source, which is a continuous data matrix with labeled rows and columns containing p-values for genes (in rows) for 19+10 experiments (in columns). We load it into “File”. Since the file has the extension .cdm.gz, “Format” is set to Continuous data matrix automatically.

Configure combination options
-------------------------------------------------

For the data type Continuous data matrix, the combinations analysis can be performed by column or by row. For the example, we select the default option, columns, which will combine the experiments.

Select sets of columns/rows to combine
-------------------------------------------------

Optionally, we can include a tabular file indicating sets of columns of the input matrix to be analysed independently. The combinations analysis will then produce one column per set. Alternatively, all columns can be analysed together to give one single output column. In the example, we select a file which annotates each experiment either to lung tumor or breast tumor. This file is a two-column file without header with the sample names from the input matrix in the first column and the set names in the second column. We load this file into “File”. Since the file has the extension .tcm, “Format” is set to Two columns mapping automatically.

Select destination file
-------------------------------------------------

In this step, we indicate where to save the results of the analysis. We fill in a name and select the folder. For each combinations analysis, Gitools will output 3 files: an analysis file \*.combination, a file summarizing the mapping between elements and sets (if applied) \*-modules.ixm.gz and the results matrix \*-results.tdm.gz.

Analysis details
-------------------------------------------------

Here, the user can add a title and free text notes that will be saved in the analysis file. Additional attributes like author, project etc. can be added at will. However, this step is optional and can be skipped. Click finish to perform the analysis.

Combinations analysis results
-------------------------------------------------

The analysis.combination file is opened in a new tab in Gitools, showing an overview of the analysis parameters. Clicking on the heat map button for Results will open another tab with the heat map displaying the results of the analysis.

Combinations heat map
-------------------------------------------------

The combinations heat map has one column for each set of columns selected during the analysis. Otherwise, one single column labeled “all data columns” will be output. Here, the columns correspond to breast and lung tumor. Rows are the same as in the input matrix. They are labeled with ensembl IDs. By default, the color scale displays the combined p-value from the combinations test for each set and gene. Significant p-values show in yellow to red; cells with insignificant p-values remain grey. Explore and customize the heat map as described. Select one or both columns and sort them clicking the “sort rows” icon in order to view the most significantly up-regulated genes first. Sort the rows by label (Data>Sort>Sort by label) to find a gene quickly. Click on any cell to see details and values in the “Details” tab on the left part of the screen. Click on any cell and on “automatic update” in the lower part of the screen to see the list of experiments for the selected tumor type and the individual p-values of the selected gene in the original matrix.



Overlapping analysis
-------------------------------------------------

An overlapping analysis counts shared elements between two vectors. To this purpose, a continuous data matrix is transformed to a binary data matrix introducing a user-defined cutoff. GiTools outputs the Jaccard Index of shared elements as well as related values such as counts of unique elements for all possible pairs of columns or rows in a data matrix. Through overlapping analysis, each pair of input columns is collapsed to one single value represented in a heat map that has column labels in both dimensions. Original row labels are not visible any more.

In the following example, we analyze overlaps between lists of significantly down-regulated genes in 14 experiments assaying several subtypes of brain and kidney tumors.

Presentation and example
-------------------------------------------------

We select the icon for Overlapping Analysis. In the following steps, the files and parameters for the analysis will be specified.

Select data source
-------------------------------------------------

In the first step, we select the data source, which is a continuous data matrix with labeled rows and columns containing p-values for genes (in rows) for 14 experiments (in columns). We load it into “File”. Since the file has the extension .cdm.gz, “Format” is set to Continuous data matrix automatically.

Select data filtering options
-------------------------------------------------

Next, a cutoff for binary processing will be selected. This will transform the continuous data input matrix to a matrix containing only 1 or 0 as values depending on a user-defined cutoff. The overlapping analysis can only be performed based on a binarized matrix. In the example analysis, the input matrix contains p-values and we are looking for genes with significant p-values. Therefore, we enter “less than” and 0.05. As a consequence, p-values below 0.05 will transform to 1 and be counted as (positive) events, while p-values above 0.05 will transform to 0 and will not be counted.

Configure overlapping options
-------------------------------------------------

Here, we can define more options: Empty values can be replaced by a user-defined value, by default 0. Overlapping can be calculated column-wise (typically samples or conditions) which is the default, or row-wise (typically genes or modules).

Select destination file
-------------------------------------------------

In this step, we indicate where to save the results of the analysis. We fill in a name and select the folder. For each overlapping analysis, Gitools will output 3 files: an analysis file \*.overlapping, a data file containing a copy of the input file \*-data.cdm.gz and a results file \*-results.tdm.gz.

Analysis details
-------------------------------------------------

Here, the user can add a title and free text notes that will be saved in the analysis file. Additional attributes like author, project etc. can be added at will. However, this step is optional and can be skipped. Click finish to perform the analysis.

Overlapping analysis results
-------------------------------------------------

A new tab in Gitools shows an overview of the analysis parameters. Clicking on the heat map button for results will open another tab with the heat map displaying the results of the analysis.

Overlapping heatmap
-------------------------------------------------

Note that in the overlapping heat map, both columns and rows label with the original column labels for an overlapping analysis by columns. During the overlapping analysis, several values are calculated, that can displayed alternatively selecting from the list under “value” in the left-hand “cell” tab of Gitools. By default, the Jaccard Index is displayed which shows a the fraction of shared elements from the sum of all elements in the pair of vectors to be compared. Overlapping heat maps have their own color scale in the range of 0 to 1, covering all possible values of the Jaccard Index values which are displayed by default. However, minimum and maximum have to be adjusted manually when displaying other values such as row or column count that can be in the range of hundreds or thousands.

Along the diagonal, we find the self-to-self comparisons which are all 1. Move selected columns to group samples. Note that moving columns automatically moves the corresponding row so as not to disturb the geometry of the heat map. This example illustrates that experiments covering the same tumor type (kidney-kidney, brain-brain) have more down-regulated genes in common than two experiments from different tumor types (kidney-brain). Note that there are still remarkable differences within one tumor type, due to differences in experimental platform, sample selection etc.
