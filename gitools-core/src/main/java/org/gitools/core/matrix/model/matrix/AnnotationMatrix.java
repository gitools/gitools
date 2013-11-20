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
package org.gitools.core.matrix.model.matrix;

import org.apache.commons.lang.StringUtils;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;

import java.util.AbstractList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class AnnotationMatrix extends HashMatrix implements IAnnotations {

    private Map<String, Map<String, String>> layersMetadata;
    private Collection<String> labels;

    public AnnotationMatrix() {
        super(ROWS);

        this.layersMetadata = new HashMap<>();
        this.labels = new LabelsAdapter();
    }

    public void addAnnotations(IAnnotations annotations) {

        // Copy all the annotations
        for (String identifier : annotations.getIdentifiers()) {
            for (String label : annotations.getLabels()) {
                setAnnotation(identifier, label, annotations.getAnnotation(identifier, label));
            }
        }

        // Copy annotations metadata
        for (String label : annotations.getLabels()) {
            for (String key : annotations.getMetadataKeys()) {
                String metadata = annotations.getAnnotationMetadata(key, label);
                if (!StringUtils.isEmpty(metadata)) {
                    setAnnotationMetadata(key, label, metadata);
                }
            }
        }

    }

    @Override
    public IMatrixDimension getIdentifiers() {
        return getDimension(ROWS);
    }

    @Override
    public Collection<String> getMetadataKeys() {
        return layersMetadata.keySet();
    }

    @Override
    public Collection<String> getLabels() {
        return labels;
    }

    @Override
    public String getAnnotation(String identifier, String label) {
        return get(getLayer(label), identifier);
    }

    private IMatrixLayer<String> getLayer(String label) {
        return getLayers().get(label);
    }

    public void setAnnotation(String identifier, String label, String value) {
        set(label, value, identifier);
    }

    @Override
    public String getAnnotationMetadata(String key, String label) {
        if (!layersMetadata.containsKey(key)) {
            return null;
        }

        return layersMetadata.get(key).get(label);
    }

    public void setAnnotationMetadata(String key, String annotationLabel, String value) {

        if (value == null || value.isEmpty()) {
            return;
        }

        Map<String, String> ketMetadata = layersMetadata.get(key);

        if (ketMetadata == null) {
            ketMetadata = new HashMap<>();
            layersMetadata.put(key, ketMetadata);
        }

        ketMetadata.put(annotationLabel, value);
    }

    private class LabelsAdapter extends AbstractList<String> {

        @Override
        public String get(int index) {
            IMatrixLayer layer = getLayers().get(index);

            return (layer == null ? null : layer.getId());
        }

        @Override
        public int size() {
            return getLayers().size();
        }
    }
}
