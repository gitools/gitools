/*
 * #%L
 * org.gitools.analysis
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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

                    assertSimilar("layer: " + l1.getId() + " pos:" + p1.toString(), v1, v2, 0.5e-5);
                }
            }
        }

    }

    static public void assertSimilar(String message, Object v1, Object v2, double delta) {

        if (v1 == null && v2 == null) {
            return;
        }

        if (v1 == null) {
            Assert.failNotEquals(message, v1, v2);
        }

        if (v1 instanceof Double) {
            Assert.assertEquals(message, ((Double) v1).doubleValue(), ((Double)v2).doubleValue(), delta);
            return;
        }

        Assert.assertEquals(message, v1, v2);
    }

    static public void assertEquals(IMatrixDimension d1, IMatrixDimension d2) {
        Assert.assertEquals("Dimension '"+d1.getId()+"' size", d1.size(), d2.size());

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
