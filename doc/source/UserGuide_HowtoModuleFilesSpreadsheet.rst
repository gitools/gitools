
=======================================================================================
How to prepare module files for Gitools using a spreadsheet editor (f.ex. Excel)
=======================================================================================

Gitools accepts several easy to prepare formats to represent modules or gene sets.
See :doc:`this section <UserGuide_ImportingData>` of the userguide for more details
on the different formats. All of them could be easily prepared using spreadsheet editors.
Just save the file as “Tab separated file” and it will be ready to be used in Gitools.

For example, GMX file format is a simple tab delimited file to provide gene sets. Each column
describes a gene set, the first row indicates the name of the gene set and the second row the
description (you can leave description empty), the rest of rows are used to enumerate the genes
related to this gene set. You can prepare a file in this GMX format using any spreadsheet editor.


.. image:: img/formatGMX.png
   :width: 80%
   :align: center

Once the file is ready, save it in a txt format with columns separated by tabs and change the extension to
:file:`.gmx`. If you leave the extension :file:`.txt` it will also work in Gitools, but you will have to specify that it is a GMX file.
