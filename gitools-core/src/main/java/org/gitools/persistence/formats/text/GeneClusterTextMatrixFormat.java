package org.gitools.persistence.formats.text;

import org.gitools.persistence.FileSuffixes;

public class GeneClusterTextMatrixFormat extends DoubleMatrixFormat {

    public GeneClusterTextMatrixFormat() {
        super(FileSuffixes.GENE_CLUSTER_TEXT, 1, Integer.valueOf(1));
    }

}
