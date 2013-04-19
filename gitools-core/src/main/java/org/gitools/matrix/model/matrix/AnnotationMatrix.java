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
package org.gitools.matrix.model.matrix;

import org.gitools.model.Resource;

import java.util.*;

public class AnnotationMatrix extends Resource implements IAnnotations {
    private List<String> labels;
    private Map<String, Map<String, String>> annotations;

    public AnnotationMatrix() {
        this.labels = new ArrayList<String>();
        this.annotations = new HashMap<String, Map<String, String>>();
    }

    public void addAnnotations(IAnnotations annotations) {
        for (String identifier : annotations.getIdentifiers()) {
            for (String label : annotations.getLabels()) {
                setAnnotation(identifier, label, annotations.getAnnotation(identifier, label));
            }
        }
    }

    @Override
    public boolean hasIdentifier(String identifier) {
        return annotations.containsKey(identifier);
    }

    @Override
    public Collection<String> getIdentifiers() {
        return annotations.keySet();
    }

    @Override
    public List<String> getLabels() {
        return labels;
    }

    @Override
    public String getAnnotation(String identifier, String annotationLabel) {
        if (!hasIdentifier(identifier)) {
            return null;
        }

        return annotations.get(identifier).get(annotationLabel);
    }

    public void setAnnotation(String identifier, String annotationLabel, String value) {
        if (!labels.contains(annotationLabel)) {
            labels.add(annotationLabel);
        }

        Map<String, String> identifierAnnotations = annotations.get(identifier);

        if (identifierAnnotations == null) {
            identifierAnnotations = new HashMap<String, String>(labels.size());
            annotations.put(identifier, identifierAnnotations);
        }

        identifierAnnotations.put(annotationLabel, value);
    }
}
