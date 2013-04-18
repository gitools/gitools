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
package org.gitools.clustering.method.annotations;

import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringDataInstance;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.matrix.model.matrix.AnnotationResolver;
import org.gitools.utils.textpatt.TextPattern;
import org.gitools.utils.textpatt.TextPattern.VariableValueResolver;
import org.jetbrains.annotations.NotNull;

public class AnnPatClusteringData implements ClusteringData
{

    public class Instance implements ClusteringDataInstance
    {

        private final VariableValueResolver resolver;

        public Instance(VariableValueResolver resolver)
        {
            this.resolver = resolver;
        }

        @Override
        public int getNumAttributes()
        {
            return 1;
        }

        @NotNull
        @Override
        public String getAttributeName(int attribute)
        {
            return "value";
        }

        @NotNull
        @Override
        public Class<?> getValueClass(int attribute)
        {
            return String.class;
        }

        @Override
        public Object getValue(int attribute)
        {
            return pat.generate(resolver);
        }

        @NotNull
        @Override
        public <T> T getTypedValue(int attribute, @NotNull Class<T> valueClass)
        {
            if (!String.class.equals(valueClass))
            {
                throw new RuntimeException("Unsupported value class: " + valueClass.getName());
            }

            return (T) getValue(attribute);
        }
    }

    private final HeatmapDimension dimension;
    private final TextPattern pat;

    public AnnPatClusteringData(HeatmapDimension dimension, String pattern)
    {
        this.dimension = dimension;
        this.pat = new TextPattern(pattern);
    }

    @Override
    public int getSize()
    {
        return dimension.size();
    }

    @Override
    public String getLabel(int index)
    {
        return dimension.getLabel(index);
    }

    @NotNull
    @Override
    public ClusteringDataInstance getInstance(int index)
    {
        return new Instance(new AnnotationResolver(dimension.getAnnotations(), dimension.getLabel(index), "N/A"));
    }
}
