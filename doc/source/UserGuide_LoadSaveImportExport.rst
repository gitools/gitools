=============================================
Load, Save, Import and Export data in Gitools
=============================================


Gitools' file formats
---------------------
When saving a heatmap or an analysis, the format in which the heatmap/analysis is saved will be a native Gitools format.
Thus, these file types can be opened with Gitools only. Also note that Gitools can directly open and save in
\*.zip format, so there is no need to extract the archive.

Read about the native :doc:`UserGuide_GitoolsHeatmapsFormat` which you can **load** and **save** from within Gitools.
Loading and Saving are actions which need no input from the user at all. Gitools can store and load heatmaps
and all the visual attributes in ``.heatmap`` and ``.heatmap.zip`` which therefore are great formats to pass on a data
set with a certain formatting to your collaborators and colleagues.

If you have preformed an analysis, the result can be saved as :file:`name-of-analysis.analysis-type.zip`.


Here is a list of the native Gitools formats:

==========================  =======================================
Gitools data type           File extension
==========================  =======================================
Heatmap                     ``.heatmaps, .heatmaps.zip``
Enrichment Analysis         ``.enrichment, .enrichment.zip``
Overlap Analyis             ``.overlapping, .overlapping.zip``
Group Comparison Analysis   ``.comparison, .comparison.zip``
Correlation Analysis        ``.correlations, .correlations.zip``
Combination Analysis        ``.combinations, .combinations.zip``
==========================  =======================================


When **Importing**, Gitools will ask for user input in order to know how to represent the data. **Exporting** refers to
a data form which can either not be loaded again in Gitools (such as a figure/image) or to a rudimentary data form which
needs to be imported again, such as flat text files.

Import my own data into Gitools
-----------------------------------


Import any data in form of a text file or excel sheet containing your data into Gitools. Read more at  :doc:`UserGuide_DataTypes`.

If you want to know about specific data files used by Gitools internally, read more here: :doc:`UserGuide_SpecialFileExtensions`.
in Gitools.


Export data from Gitools
-----------------------------------

Find a list of possible ways to :doc:`UserGuide_Export`




