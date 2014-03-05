===============================
Gitools Heatmaps (.heatmap.zip)
===============================

Gitools saves and loads a format with the extension ``.heatmap`` or ``.heatmap.zip``. These files contain all the
necessary information to recreate the exact state of when the heatmap was saved.

What does the .heatmap.zip contain?
--------------------------------------
It is a zip folder that does not need to be unzipped in order to be opened by Gitools. If we assume that you saved a
heatmap with Gitools called :file:`superstudy.heatmap.zip`. Thus the heatmap.zip will contain:

- :file:`superstudy.heatmap`: An xml file contain information about visible rows, columns and as well row and column headeres
- :file:`superstudy-data.tdm.gz`: A data file which contains all the data for the heatmap. The extension can vary, depending on how the
                                    data was loaded.
- :file:`superstudy-columns-annotations.tsv.gz`: All the annotation data for the columns, also unused fields.
- :file:`superstudy-rows-annotations.tsv.gz`: All the annotation data for the rows, also unused fields.



