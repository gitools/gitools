package org.gitools.persistence.formats.matrix;

import org.gitools.persistence._DEPRECATED.FileSuffixes;

public class GeneClusterTextMatrixFormat extends DoubleMatrixFormat {

    public GeneClusterTextMatrixFormat() {
        super(FileSuffixes.GENE_CLUSTER_TEXT, 1, Integer.valueOf(1));
    }

}
