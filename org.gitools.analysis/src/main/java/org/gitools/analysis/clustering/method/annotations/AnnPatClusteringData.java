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
package org.gitools.analysis.clustering.method.annotations;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringDataInstance;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.utils.textpattern.TextPattern;
import org.gitools.utils.textpattern.TextPattern.VariableValueResolver;

public class AnnPatClusteringData implements ClusteringData {

    public final static String NA = "N/A";

    private final HeatmapDimension dimension;
    private final TextPattern pat;

    public AnnPatClusteringData(HeatmapDimension dimension, String pattern) {
        this.dimension = dimension;
        this.pat = new TextPattern(pattern);
    }

    @Override
    public int getSize() {
        return dimension.size();
    }

    @Override
    public String getLabel(int index) {
        return dimension.getLabel(index);
    }

    @Override
    public Iterable<String> getLabels() {
        return dimension;
    }

    @Override
    public ClusteringDataInstance getInstance(int index) {
        return getInstance(dimension.getLabel(index));
    }

    @Override
    public ClusteringDataInstance getInstance(String label) {
        return new Instance(new AnnotationResolver(dimension.getAnnotations(), label, NA));
    }

    public class Instance implements ClusteringDataInstance {

        private final VariableValueResolver resolver;

        public Instance(VariableValueResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public int getNumAttributes() {
            return 1;
        }


        @Override
        public String getAttributeName(int attribute) {
            return "value";
        }


        @Override
        public Class<?> getValueClass(int attribute) {
            return String.class;
        }

        @Override
        public Object getValue(int attribute) {
            return pat.generate(resolver);
        }


        @Override
        public <T> T getTypedValue(int attribute, Class<T> valueClass) {
            if (!String.class.equals(valueClass)) {
                throw new RuntimeException("Unsupported value class: " + valueClass.getName());
            }

            return (T) getValue(attribute);
        }
    }
}
