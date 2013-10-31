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

import org.apache.commons.lang.ArrayUtils;
import org.gitools.core.matrix.model.*;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.model.HashModuleMap;
import org.gitools.core.model.IModuleMap;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.impl.*;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrixUtils {

    private static final int MAX_UNIQUE = 30;
    public static final int MAXIMUM_ROWS_TO_SCAN = 2000;

    public static String[] createLabelArray(IMatrixDimension dimension) {
        String[] labels = new String[dimension.size()];

        for (int i=0; i < dimension.size(); i++) {
            labels[i] = dimension.getLabel(i);
        }

        return labels;
    }

    public static interface DoubleCast {
        Double getDoubleValue(Object value);
    }


    @Deprecated // Better use createDoubleCast() when accessing multiple values
    public static double doubleValue(Object value) {
        if (value == null) {
            return Double.NaN; //TODO null;
        }

        double v = Double.NaN;

        try {
            v = ((Double) value).doubleValue();
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
                    return value != null ? ((Double) value).doubleValue() : null;
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

    public static IColorScale inferScale(IMatrix data, int valueIndex) {

        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        IColorScale scale = null;
        String dataDimName = data.getLayers().get(valueIndex).getName();
        String pvalRegex = ("(?i:pval|p-val)");
        String zscoreRegex = ("(?i:z-score|zscore|zval|z-val)");

        double[] values = getUniquedValuesFromMatrix(data, valueIndex);
        for (int i = 0; i < values.length; i++) {
            double v = values[i];
            min = v < min ? v : min;
            max = v > max ? v : max;
        }


        if (values.length == 2) {
            BinaryColorScale bscale = new BinaryColorScale();
            bscale.setCutoff(max);
            bscale.setComparator(CutoffCmp.EQ.getShortName());
            scale = bscale;
        } else if (values.length > 2 && values.length < MAX_UNIQUE) {
            scale = new CategoricalColorScale(values);
        } else {
            if (min >= 0 && max <= 1 || dataDimName.matches(pvalRegex)) {
                scale = new PValueColorScale();
            } else if (dataDimName.matches(zscoreRegex)) {
                scale = new ZScoreColorScale();
            } else {
                LinearTwoSidedColorScale lscale = new LinearTwoSidedColorScale();
                lscale.getMin().setValue(min);
                lscale.getMax().setValue(max);
                if (lscale.getMax().getValue() > 0 && lscale.getMin().getValue() < 0) {
                    lscale.getMid().setValue(0.0);
                } else {
                    lscale.getMid().setValue((lscale.getMax().getValue() + lscale.getMin().getValue()) / 2);
                }
                scale = lscale;
            }
        }
        return scale;
    }

    public static int correctedValueIndex(IMatrixLayers layers, IMatrixLayer layer) {

        String id = "corrected-" + layer.getId();

        for (int i = 0; i < layers.size(); i++)
            if (id.equals(layers.get(i).getId())) {
                return i;
            }

        return -1;
    }

    public static IMatrix moduleMapToMatrix(IModuleMap mmap) {

        String[] columns = mmap.getModuleNames();
        String[] rows = mmap.getItemNames();

        IMatrix matrix = new HashMatrix(rows, columns, new MatrixLayers(Arrays.asList(new MatrixLayer("value", double.class))));

        for (int col = 0; col < mmap.getModuleCount(); col++)
            for (int row : mmap.getItemIndices(col))
                matrix.setValue(row, col, 0, 1.0);
        return matrix;
    }

    public static IModuleMap matrixToModuleMap(IMatrix matrix) {

        HashModuleMap moduleMap = new HashModuleMap();

        for (int row=0; row < matrix.getRows().size(); row++) {

            String item = matrix.getRows().getLabel(row);

            for (int column=0; column < matrix.getColumns().size(); column++) {

                String module = matrix.getColumns().getLabel(column);

                double value = MatrixUtils.doubleValue(matrix.getValue(row, column, 0));
                if (value == 1.0) {
                    moduleMap.addMapping(module, item);
                }

            }
        }

        return moduleMap;
    }

    public static double[] getUniquedValuesFromMatrix(IMatrix data, int valueDimension) {
        return getUniquedValuesFromMatrix(data, valueDimension, MAX_UNIQUE, new StreamProgressMonitor(System.out, true, true));
    }

    private static double[] getUniquedValuesFromMatrix(IMatrix data, int valueDimension, int maxUnique, IProgressMonitor monitor) {
        /* returns all values DIFFERENT from a heatmap dimension except if it is too many (50), it returns
        * an equally distributed array values from min to max*/

        List<Double> valueList = new ArrayList<Double>();
        MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(data.getLayers().get(valueDimension).getValueClass());
        Double min = Double.POSITIVE_INFINITY;
        Double max = Double.NEGATIVE_INFINITY;

        int colNb = data.getColumns().size();
        int rowNb = data.getRows().size();

        IProgressMonitor submonitor = monitor.subtask();
        String valueDimensionName = data.getLayers().get(valueDimension).getName();

        int randomRows = 50 > rowNb ? rowNb : 50;
        int[] randomRowsIdx = new int[randomRows];
        for (int i = 0; i < randomRows; i++)
            randomRowsIdx[i] = (int) (Math.random() * ((rowNb) + 1));


        int rr = 0;
        rowNb = (rowNb > MAXIMUM_ROWS_TO_SCAN) ? MAXIMUM_ROWS_TO_SCAN : rowNb;
        colNb = (colNb > MAXIMUM_ROWS_TO_SCAN) ? MAXIMUM_ROWS_TO_SCAN : colNb;

        submonitor.begin("Reading some values in data matrix for " + valueDimensionName, rowNb);
        for (int r = 0; r < rowNb; r++) {
            monitor.worked(1);
            for (int c = 0; c < colNb; c++) {
                Object v = data.getValue(r, c, valueDimension);
                if (v == null) {
                    continue;
                }
                double d = cast.getDoubleValue(v);
                if (!Double.isNaN(d)) {
                    if (valueList.size() <= maxUnique && !valueList.contains(d)) {
                        valueList.add(d);
                    }
                    min = d < min ? d : min;
                    max = d > max ? d : max;
                }
            }
            if (rr >= randomRows - 1) {
                break;
            } else if (valueList.size() >= maxUnique) {
                r = randomRowsIdx[rr];
                rr++;
            }

        }
        if (!valueList.contains(min)) {
            valueList.add(min);
        }
        if (!valueList.contains(max)) {
            valueList.add(max);
        }

        if (valueList.size() >= maxUnique) {
            valueList.clear();
            double spectrum = max - min;
            double step = spectrum / maxUnique;
            for (int i = 0; i < maxUnique; i++) {
                valueList.add(i * step - (spectrum - max));
            }
        }
        return ArrayUtils.toPrimitive(valueList.toArray(new Double[]{}));
    }
}
