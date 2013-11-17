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
import org.gitools.core.matrix.model.IAnnotations;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.matrix.model.hashmatrix.HashMatrixDimension;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.gitools.core.matrix.model.MatrixDimensionKey.COLUMNS;
import static org.gitools.core.matrix.model.MatrixDimensionKey.ROWS;

public class AnnotationMatrix extends HashMatrix implements IAnnotations {

    private static MatrixLayer<String> LAYER = new MatrixLayer<>("value", String.class);

    private Map<String, Map<String, String>> annotationsMetadata;

    public AnnotationMatrix() {
        super(new MatrixLayers(LAYER), new HashMatrixDimension(ROWS), new HashMatrixDimension(COLUMNS));

        this.annotationsMetadata = new HashMap<>();
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
        return getIdentifiers(ROWS);
    }

    @Override
    public Collection<String> getMetadataKeys() {
        return annotationsMetadata.keySet();
    }

    @Override
    public IMatrixDimension getLabels() {
        return getIdentifiers(COLUMNS);
    }

    @Override
    public String getAnnotation(String identifier, String label) {
        return get(LAYER, identifier, label);
    }

    public void setAnnotation(String identifier, String label, String value) {
        set(LAYER, value, identifier, label);
    }

    @Override
    public String getAnnotationMetadata(String key, String label) {
        if (!annotationsMetadata.containsKey(key)) {
            return null;
        }

        return annotationsMetadata.get(key).get(label);
    }

    public void setAnnotationMetadata(String key, String annotationLabel, String value) {

        if (value == null || value.isEmpty()) {
            return;
        }

        Map<String, String> ketMetadata = annotationsMetadata.get(key);

        if (ketMetadata == null) {
            ketMetadata = new HashMap<>();
            annotationsMetadata.put(key, ketMetadata);
        }

        ketMetadata.put(annotationLabel, value);
    }
}
