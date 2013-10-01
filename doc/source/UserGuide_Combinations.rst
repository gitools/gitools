

Combination of p-values
-------------------------------------------------




Table of Contents
-------------------------------------------------

`Wizard steps to perform a combination analysis <#N1003A>`__  `#  <#N1003A>`__

`Exploring the results of the analysis <#N100BF>`__  `#  <#N100BF>`__





When several experiments testing the same hypothesis are analysed, a natural question that arises is whether the combined evidence among them supports the hypothesis. However the individual experiments are often not directly compatible to produce a single large combined data set to be analysed together. Though, one can still produce a combined test of significance across a set of experiments. After computing p-values for each experiment independently we can integrate those results using the weighted Z-method. This method is very convenient for integrating results obtained with other analyses like oncodrive or enrichment.



Wizard steps to perform a combination analysis
-------------------------------------------------

Before running the analysis you should have this data prepared in files:

A matrix with p-values.

To start the wizard go to the menu *File > New > Analysis > Combination analysis ...*

You can also start a combination analysis from an opened heatmap by selecting the menu *Analysis > Combination.*

Presentation and example
-------------------------------------------------



If you select the option **Fill this wizard with an example** all the following wizard pages will be filled with example files and parameters. In some situations the example files will be downloaded automatically before you can go to the next page. By default, when Gitools is installed from the zip file, the examples are already included. But when Gitools is executed directly from the web page the example files have to be downloaded the first time they are used. In that case the files are cached at *$HOME/.gitools/examples*.

You can avoid this first presentation page by selecting the option **Don’t show this page next time**.

Selection of the data to analyze
-------------------------------------------------



If you follow the conventions on file formats explained  `here <UserGuide_LoadingData.rst>`__  then simply click on the button [Browse] and select the file containing the data to analyze. The format selector will recognize the extension of the file. If your data file doesn’t have a known file extension then you should specify which is the format of the file.

Configure combination options
-------------------------------------------------



**Size attribute** refers to the data matrix attribute that will be used to weight p-values. This option only applies when the data matrix has more than one attribute per cell. If no size attribute is specified all the columns/rows will have the same weight.

**P-value attribute** refers to the data matrix attribute that has the p-value that will be combined. This option only applies when the data matrix has more than one attribute per cell.

The option **Apply to** allows to select whether to combine by columns or rows.

Selection of the destination file
-------------------------------------------------



This wizard page allows to specify the prefix name for the files generated during the analysis and the folder where they will be created. The text in name and folder can be freely edited but it is also possible to navigate through the system folders in order to select an existing file by pressing the button [Browse].

Analysis details
-------------------------------------------------



This step is optional but recommended as it allows to give some details about the analysis for better organization and annotation of the results for future reviews.

It is possible to specify free attributes for the analysis as Organization, Operator, Platform and so on.



Exploring the results of the analysis
-------------------------------------------------

After performing the analysis a new editor with the details of the analysis will be opened:



To explore the data matrix resulting from the transformations with a heatmap click on the **Heatmap** button in the **Data** section.



To explore the results of the analysis with a heatmap click on the **Heatmap** button in the **Results** section.


