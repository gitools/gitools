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
package org.gitools.clustering.method.value;

import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringDataInstance;
import org.gitools.matrix.model.IMatrix;
import org.jetbrains.annotations.NotNull;

/**
 * @noinspection ALL
 */
public class MatrixRowClusteringData implements ClusteringData
{

    public class Instance implements ClusteringDataInstance
    {

        final int index;

        public Instance(int index)
        {
            this.index = index;
        }

        @Override
        public int getNumAttributes()
        {
            return matrix.getColumns().size();
        }

        @Override
        public String getAttributeName(int attribute)
        {
            return matrix.getColumns().getLabel(attribute);
        }

        @Override
        public Class<?> getValueClass(int attribute)
        {
            return attributeClass;
        }

        @Override
        public Object getValue(int attribute)
        {
            return matrix.getCellValue(index, attribute, matrixAttribute);
        }

        @NotNull
        @Override
        public <T> T getTypedValue(int attribute, @NotNull Class<T> valueClass)
        {
            if (!valueClass.equals(getValueClass(attribute)))
            {
                throw new RuntimeException("Unsupported type: " + valueClass.getCanonicalName());
            }

            return (T) matrix.getCellValue(index, attribute, matrixAttribute);
        }
    }

    private IMatrix matrix;
    private final int matrixAttribute;
    private Class<?> attributeClass;

    public MatrixRowClusteringData(@NotNull IMatrix matrix, int matrixAttribute)
    {
        this.matrix = matrix;
        this.matrixAttribute = matrixAttribute;
        this.attributeClass = matrix.getCellAttributes().get(matrixAttribute).getValueClass();
    }

    @Override
    public int getSize()
    {
        return matrix.getRows().size();
    }

    @Override
    public String getLabel(int index)
    {
        return matrix.getRows().getLabel(index);
    }

    @NotNull
    @Override
    public ClusteringDataInstance getInstance(int index)
    {
        return new Instance(index);
    }

    public Class<?> getAttributeClass()
    {
        return attributeClass;
    }

    public void setAttributeClass(Class<?> attributeClass)
    {
        this.attributeClass = attributeClass;
    }

    public IMatrix getMatrix()
    {
        return matrix;
    }

    public void setMatrix(IMatrix matrix)
    {
        this.matrix = matrix;
    }

    public int getNumAttributes()
    {
        return matrix.getColumns().size();
    }
}
