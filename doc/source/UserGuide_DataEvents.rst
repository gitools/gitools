==================
Data Events
==================

The event count in the selection details gives the user a quick idea about the selected data:
 - How many mutations (categorical or binary data) ?
 - How many over and under-expressed values (continuous data)?
 - How many significant p-values (test results)?
 - How many siginificant z-scores (z-score enrichment results)?


When selecting a rows and columns, Gitools shows some basic aggregations and counts the data events within the
selected data. Data events are a way to binarize the data on the fly which is also used f.ex. by the :doc:`UserGuide_MutualExclusivity`.

Which data points are considered as events and which aren't depends on the color scale that is being used to show the data.
Hovering the mouse over the ``Events`` label in the selectio details box will give you more information.

**Examples**:

- **Categorical scale**: all values represented in the categorical scale  are events. The rest of the values are non-events.
- **P-value scale**: all values falling below the significance threshold are events. Non-significant and null values are non-events
- **Linear scale**: All values falling below minimum and above maximum of the scale are events.
