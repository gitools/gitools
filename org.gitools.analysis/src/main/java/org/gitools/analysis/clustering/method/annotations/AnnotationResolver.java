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

import org.gitools.api.matrix.IAnnotations;
import org.gitools.utils.textpattern.TextPattern.VariableValueResolver;

public class AnnotationResolver implements VariableValueResolver {

    private final IAnnotations am;

    private String label;

    private final String na;

    public AnnotationResolver(IAnnotations am, String label, String na) {
        this.am = am;
        if (label != null) {
            setLabel(label);
        }
        this.na = na;
    }

    final void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String resolveValue(String variableName) {
        if (variableName.equalsIgnoreCase("id")) {
            return label;
        }

        if (label == null) {
            return na;
        }

        String annotation = null;

        if (am != null) {
            annotation = am.getAnnotation(label, variableName);
        }

        if (annotation == null) {
            return na;
        }

        return annotation;
    }
}
