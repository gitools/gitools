package org.gitools.analysis;

import junit.framework.Assert;
import org.gitools.api.matrix.IMatrix;

import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.MatrixDimensionKey;

public class AssertMatrix {

    protected AssertMatrix() {
    }

    static public void assertEquals(IMatrix m1, IMatrix m2) {

        for (MatrixDimensionKey key : m1.getDimensionKeys()) {
           assertEquals(m1.getDimension(key), m2.getDimension(key));
        }

        Assert.assertEquals(m1.getLayers().size(), m2.getLayers().size());
        for (IMatrixLayer l1 : m1.getLayers()) {
            IMatrixLayer l2 = m2.getLayers().get(l1.getId());
            assertEquals(l1, l2);

            IMatrixPosition p1 = m1.newPosition();
            IMatrixPosition p2 = m2.newPosition();
            for (MatrixDimensionKey key : m1.getDimensionKeys()) {
                IMatrixDimension d1 = m1.getDimension(key);
                IMatrixDimension d2 = m2.getDimension(key);

                for (String id : m1.getDimension(key)) {
                    p1.set(d1, id);
                    p2.set(d2, id);

                    Object v1 = m1.get(l1, p1);
                    Object v2 = m2.get(l2, p2);

                    Assert.assertEquals("layer: " + l1.getId() + " pos:" + p1.toString(), v1, v2);
                }
            }
        }

    }

    static public void assertEquals(IMatrixDimension d1, IMatrixDimension d2) {
        Assert.assertEquals(d1.size(), d2.size());

        for (String id : d1) {
            Assert.assertTrue(d2.contains(id));
        }
    }

    static public void assertEquals(IMatrixLayer l1, IMatrixLayer l2) {

        Assert.assertEquals(l1.getId(), l2.getId());
        Assert.assertEquals(l1.getName(), l2.getName());
        Assert.assertEquals(l1.getDescription(), l2.getDescription());
        Assert.assertEquals(l1.getValueClass(), l2.getValueClass());
        Assert.assertEquals(l1.getTranslator(), l2.getTranslator());
        Assert.assertEquals(l1.getSortDirection(), l2.getSortDirection());
        Assert.assertEquals(l1.getAggregator(), l2.getAggregator());

    }
}
