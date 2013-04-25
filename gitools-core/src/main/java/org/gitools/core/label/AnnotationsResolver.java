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
package org.gitools.core.label;

import org.gitools.core.matrix.model.IAnnotations;
import org.gitools.utils.textpatt.TextPattern;
import org.jetbrains.annotations.NotNull;

public class AnnotationsResolver implements TextPattern.VariableValueResolver {

    private LabelProvider labelProvider;
    private IAnnotations am;
    private int index;

    public AnnotationsResolver(LabelProvider labelProvider, IAnnotations annotations) {
        this.labelProvider = labelProvider;
        this.am = annotations;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String resolveValue(@NotNull String variableName) {
        String label = labelProvider.getLabel(index);
        if (variableName.equalsIgnoreCase("id")) {
            return label;
        }

        String annotation = null;

        if (am != null) {
            annotation = am.getAnnotation(label, variableName);
        }

        if (annotation == null) {
            return "${" + variableName + "}";
        }

        return annotation;
    }
}
