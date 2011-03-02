package org.gitools.matrix.clustering.methods;

public interface ProposedClusterResults {

	/** Return the cluster title by the index of the cluster */
	String getClusterName(int index);

	/** Returns the cluster index for a given row/column label in the matrix.
	 * If there is not cluster associated then return -1. */
	int getClusterIndex(String id);
}
