=============================================
Interaction: Heatmap manipulation and control
=============================================

Gitools is a tool that sets the emphasis on the interaction with the data. Below you find a few tips on how you may interact with Gitools heatmaps.

The heatmap
-----------

The heatmap has three parts that need to be distinguised in order to interact easily with Gitools:

    - The data matrix, the heatmap itself
    - Column annotations
    - Row annotations

Row and Column annotation is where most of the interaction happens. Annotations are the additional data that 
let the user understand what value is being viewed. One annotation that is 
always present is the id for each row and column. Furthermore it is very useful to have clinical / genomic annotations appended 
to rows and columns that put the data in several contexts.

See also :doc:`UserGuide_HeatmapEditor` and :doc:`UserGuide_Visualization`

Interacting with the heatmap
----------------------------

Clickin any cell immediately displays all the values associated to the cell itself, and to the corresponding
row and columns id. This information is displayed at the left of the heatmap in the detail boxes. The thin
dark lines indicate where the lead/focuse has ben set upon clicking the cell.

Interacting with the rows and columns
-------------------------------------

Most of the operations performed on the data depends on either rows or columns. Different types of informations
can be added to the rows and columns which allow different operations. A right click reveals all the options there are.
Common operations are:

    - Sort rows/columns
    - Filter rows/columns
    - Hide rows/columns

What parameters are applied depends on the header and user choice.
