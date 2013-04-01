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
package org.gitools.matrix;

import org.apache.commons.lang.ArrayUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.ModuleMap;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.impl.*;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;

import java.util.ArrayList;
import java.util.List;

public class MatrixUtils
{

    private static int MAX_UNIQUE = 30;

    public static interface DoubleCast
    {
        Double getDoubleValue(Object value);
    }

    public static int intValue(Object value)
    {
        int v = 0;
        if (value != null)
        {
            try
            {
                v = ((Integer) value).intValue();
            } catch (Exception e1)
            {
                try
                {
                    v = ((Double) value).intValue();
                } catch (Exception e2)
                {
                    try
                    {
                        v = Integer.parseInt((String) value);
                    } catch (Exception e3)
                    {
                    }
                }
            }
        }
        return v;
    }

    @Deprecated // Better use createDoubleCast() when accessing multiple values
    public static double doubleValue(Object value)
    {
        if (value == null)
        {
            return Double.NaN; //TODO null;
        }

        double v = Double.NaN;

        try
        {
            v = ((Double) value).doubleValue();
        } catch (Exception e1)
        {
            try
            {
                v = ((Integer) value).doubleValue();
            } catch (Exception e2)
            {
                /*try { v = Double.parseDouble((String) value); }
				catch (Exception e3) { }*/
            }
        }

        return v;
    }

    public static DoubleCast createDoubleCast(Class cls)
    {
        if (cls.equals(Double.class) || cls.equals(double.class))
        {
            return new DoubleCast()
            {
                @Override
                public Double getDoubleValue(Object value)
                {
                    return value != null ? ((Double) value).doubleValue() : null;
                }
            };
        }
        else if (cls.equals(Float.class) || cls.equals(float.class))
        {
            return new DoubleCast()
            {
                @Override
                public Double getDoubleValue(Object value)
                {
                    return value != null ? ((Float) value).doubleValue() : null;
                }
            };
        }
        else if (cls.equals(Integer.class) || cls.equals(int.class))
        {
            return new DoubleCast()
            {
                @Override
                public Double getDoubleValue(Object value)
                {
                    return value != null ? ((Integer) value).doubleValue() : null;
                }
            };
        }
        else if (cls.equals(Long.class) || cls.equals(long.class))
        {
            return new DoubleCast()
            {
                @Override
                public Double getDoubleValue(Object value)
                {
                    return value != null ? ((Long) value).doubleValue() : null;
                }
            };
        }

        return new DoubleCast()
        {
            @Override
            public Double getDoubleValue(Object value)
            {
                return value != null ? Double.NaN : null;
            }
        };
    }

    public static IColorScale inferScale(IMatrix data, int valueIndex)
    {

        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        IColorScale scale = null;
        String dataDimName = data.getCellAttributes().get(valueIndex).getName();
        String pvalRegex = ("(?i:pval|p-val)");
        String zscoreRegex = ("(?i:z-score|zscore|zval|z-val)");

        double[] values = getUniquedValuesFromMatrix(data, data.getCellAdapter(), valueIndex);
        for (int i = 0; i < values.length; i++)
        {
            double v = values[i];
            min = v < min ? v : min;
            max = v > max ? v : max;
        }


        if (values.length == 2)
        {
            BinaryColorScale bscale = new BinaryColorScale();
            bscale.setCutoff(max);
            bscale.setComparator(CutoffCmp.EQ.getShortName());
            scale = bscale;
        }
        else if (values.length > 2 && values.length < MAX_UNIQUE)
        {
            scale = new CategoricalColorScale(values);
        }
        else
        {
            if (min >= 0 && max <= 1 || dataDimName.matches(pvalRegex))
            {
                scale = new PValueColorScale();
            }
            else if (dataDimName.matches(zscoreRegex))
            {
                scale = new ZScoreColorScale();
            }
            else
            {
                LinearTwoSidedColorScale lscale = new LinearTwoSidedColorScale();
                lscale.getMin().setValue(min);
                lscale.getMax().setValue(max);
                if (lscale.getMax().getValue() > 0 && lscale.getMin().getValue() < 0)
                {
                    lscale.getMid().setValue(0.0);
                }
                else
                {
                    lscale.getMid().setValue(
                            (lscale.getMax().getValue() +
                                    lscale.getMin().getValue())
                                    / 2);
                }
                scale = lscale;
            }
        }
        return scale;
    }

    public static int correctedValueIndex(IElementAdapter adapter, IElementAttribute prop)
    {
        int numProps = adapter.getPropertyCount();

        String id = "corrected-" + prop.getId();

        for (int i = 0; i < numProps; i++)
            if (id.equals(adapter.getProperty(i).getId()))
            {
                return i;
            }

        return -1;
    }

    public static BaseMatrix moduleMapToMatrix(ModuleMap mmap)
    {
        DoubleBinaryMatrix matrix = new DoubleBinaryMatrix();
        String[] columns = mmap.getModuleNames();
        String[] rows = mmap.getItemNames();
        matrix.setColumns(columns);
        matrix.setRows(rows);
        matrix.makeCells(rows.length, columns.length);
        for (int col = 0; col < mmap.getModuleCount(); col++)
            for (int row : mmap.getItemIndices(col))
                matrix.setCellValue(row, col, 0, 1.0);
        return matrix;
    }

    public static ModuleMap matrixToModuleMap(IMatrix matrix)
    {
        String[] itemNames = new String[matrix.getRowCount()];
        for (int i = 0; i < matrix.getRowCount(); i++)
            itemNames[i] = matrix.getRowLabel(i);

        String[] modNames = new String[matrix.getColumnCount()];
        for (int i = 0; i < matrix.getColumnCount(); i++)
            modNames[i] = matrix.getColumnLabel(i);

        ModuleMap map = new ModuleMap();
        map.setItemNames(itemNames);
        map.setModuleNames(modNames);

        int[][] mapIndices = new int[matrix.getColumnCount()][];
        for (int col = 0; col < matrix.getColumnCount(); col++)
        {
            List<Integer> indexList = new ArrayList<Integer>();
            for (int row = 0; row < matrix.getRowCount(); row++)
            {
                double value = MatrixUtils.doubleValue(matrix.getCellValue(row, col, 0));
                if (value == 1.0)
                {
                    indexList.add(row);
                }
            }
            int[] indexArray = new int[indexList.size()];
            for (int i = 0; i < indexList.size(); i++)
                indexArray[i] = indexList.get(i);
            mapIndices[col] = indexArray;
        }

        map.setAllItemIndices(mapIndices);

        return map;
    }

    public static double[] getUniquedValuesFromMatrix(IMatrix data, IElementAdapter cellAdapter, int valueDimension, IProgressMonitor monitor)
    {
        return getUniquedValuesFromMatrix(data, cellAdapter, valueDimension, MAX_UNIQUE, new StreamProgressMonitor(System.out, true, true));
    }

    public static double[] getUniquedValuesFromMatrix(IMatrix data, IElementAdapter cellAdapter, int valueDimension)
    {
        return getUniquedValuesFromMatrix(data, cellAdapter, valueDimension, MAX_UNIQUE, new StreamProgressMonitor(System.out, true, true));
    }


    public static double[] getUniquedValuesFromMatrix(IMatrix data, IElementAdapter cellAdapter, int valueDimension, int maxUnique, IProgressMonitor monitor)
    {
        /* returns all values DIFFERENT from a heatmap dimension except if it is too man (50), it returns
        * an equally distributed array values from min to max*/

        Double[] values = null;
        List<Double> valueList = new ArrayList<Double>();
        MatrixUtils.DoubleCast cast = MatrixUtils.createDoubleCast(
                cellAdapter.getProperty(valueDimension).getValueClass());
        Double min = Double.POSITIVE_INFINITY;
        Double max = Double.NEGATIVE_INFINITY;

        int colNb = data.getColumnCount();
        int rowNb = data.getRowCount();

        IProgressMonitor submonitor = monitor.subtask();
        String valueDimensionName = data.getCellAttributes().get(valueDimension).getName();
        submonitor.begin("Reading all values in data matrix for " + valueDimensionName, rowNb);

        int randomRows = 50 > rowNb ? rowNb : 50;
        int[] randomRowsIdx = new int[randomRows];
        for (int i = 0; i < randomRows; i++)
            randomRowsIdx[i] = (int) (Math.random() * ((rowNb) + 1));


        int rr = 0;
        for (int r = 0; r < rowNb; r++)
        {
            monitor.worked(1);
            for (int c = 0; c < colNb; c++)
            {
                Object v = data.getCellValue(r, c, valueDimension);
                if (v == null)
                {
                    continue;
                }
                double d = cast.getDoubleValue(v);
                if (!Double.isNaN(d))
                {
                    if (valueList.size() <= maxUnique && !valueList.contains(d))
                    {
                        valueList.add(d);
                    }
                    min = d < min ? d : min;
                    max = d > max ? d : max;
                }
            }
            if (rr >= randomRows - 1)
            {
                break;
            }
            else if (valueList.size() >= maxUnique)
            {
                r = randomRowsIdx[rr];
                rr++;
            }

        }
        if (!valueList.contains(min))
        {
            valueList.add(min);
        }
        if (!valueList.contains(max))
        {
            valueList.add(max);
        }

        if (valueList.size() >= maxUnique)
        {
            valueList.clear();
            double spectrum = max - min;
            double step = spectrum / maxUnique;
            for (int i = 0; i < maxUnique; i++)
            {
                valueList.add(i * step - (spectrum - max));
            }
        }
        return ArrayUtils.toPrimitive(valueList.toArray(new Double[]{}));
    }
}
