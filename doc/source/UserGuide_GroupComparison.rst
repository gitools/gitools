================================================================
Group Comparison
================================================================


A group comparison lets you choose groups of columns and perform a statistical test for each row that assesses
if the two groups are significantly different. Consult the :doc:`Tutorials_Tutorial63` which features
video instructions on how a Group Analysis is performed.


Mann-Whitney-Wilcoxon Test
------------------------------
  The Mann-Withney-Wilcoxon compares  is a non-parametric test of the null hypothesis that two populations are the same
  against an alternative hypothesis, especially that a particular population tends to have larger values than the other. [1]_

Group by labels
...............................
Two groups can be specified or **by label** - that means that all the groups are the same for all rows. As an example
consider that you compare all colums whose Ids start with Brain-sample... against all columns whose Ids start with
Colon-sample..

Group by value
...............................

The alternative grouping allows to group **by value** where the groups are being built for each row differently.
The value used to group can differ to the value used to compare. Therefore it is possible to form groups by a value
that represents e.g. Copy number status but compare the values from the Expression data.


.. [1] Wikipedia article at: http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U
