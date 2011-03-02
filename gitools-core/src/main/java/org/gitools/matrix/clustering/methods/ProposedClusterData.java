package org.gitools.matrix.clustering.methods;

/** Generic data interface for clustering methods */
public interface ProposedClusterData {

	/** Return the number of elements in the data */
	int getSize();

	/** Returns the label associated with the element at index <index> */
	String getLabel(int index);

	/** Returns the values associated with the element at index <index> for the attribute <attrIndex> */
	Object getValue(int index, int attrIndex);
}
