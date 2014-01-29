/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.core.utils;

import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.core.model.IModuleMap;

public class MatrixUtils {

    public static interface DoubleCast {
        Double getDoubleValue(Object value);
    }

    @Deprecated
    public static double doubleValue(Object value) {
        if (value == null) {
            return Double.NaN; //TODO null;
        }

        double v = Double.NaN;

        try {
            v = (Double) value;
        } catch (Exception e1) {
            try {
                v = ((Integer) value).doubleValue();
            } catch (Exception e2) {
            }
        }

        return v;
    }

    public static DoubleCast createDoubleCast(Class cls) {
        if (cls.equals(Double.class) || cls.equals(double.class)) {
            return new DoubleCast() {
                @Override
                public Double getDoubleValue(Object value) {
                    return value != null ? (Double) value : null;
                }
            };
        } else if (cls.equals(Float.class) || cls.equals(float.class)) {
            return new DoubleCast() {
                @Override
                public Double getDoubleValue(Object value) {
                    return value != null ? ((Float) value).doubleValue() : null;
                }
            };
        } else if (cls.equals(Integer.class) || cls.equals(int.class)) {
            return new DoubleCast() {
                @Override
                public Double getDoubleValue(Object value) {
                    return value != null ? ((Integer) value).doubleValue() : null;
                }
            };
        } else if (cls.equals(Long.class) || cls.equals(long.class)) {
            return new DoubleCast() {
                @Override
                public Double getDoubleValue(Object value) {
                    return value != null ? ((Long) value).doubleValue() : null;
                }
            };
        }

        return new DoubleCast() {
            @Override
            public Double getDoubleValue(Object value) {
                return value != null ? Double.NaN : null;
            }
        };
    }

    public static IMatrix moduleMapToMatrix(IModuleMap mmap) {

        MatrixLayer<Double> valueLayer = new MatrixLayer("value", Double.class);
        IMatrix matrix = new HashMatrix(
                new MatrixLayers<MatrixLayer>(valueLayer),
                new HashMatrixDimension(MatrixDimensionKey.ROWS, mmap.getItems()),
                new HashMatrixDimension(MatrixDimensionKey.COLUMNS, mmap.getModules())
        );

        for (String column : mmap.getModules()) {
            for (String row : mmap.getMappingItems(column)) {
                matrix.set(valueLayer, 1.0, row, column);
            }
        }

        return matrix;
    }

}
