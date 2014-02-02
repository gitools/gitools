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
package org.gitools.matrix.model.iterable;

import org.gitools.api.matrix.IAnnotations;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.IMatrixPredicate;
import org.gitools.utils.textpatt.TextPattern;

import java.util.Set;


public class IdentifiersPredicate<T> implements IMatrixPredicate<T>, TextPattern.VariableValueResolver {

    private IAnnotations annotations;
    private IMatrixDimension iterationDimension;
    private TextPattern pattern;
    private Set<String> values;

    private transient String currentIdentifier;

    public IdentifiersPredicate(IMatrixDimension iterationDimension, Set<String> values) {
        this(iterationDimension, values, null, null);
    }

    public IdentifiersPredicate(IMatrixDimension iterationDimension, Set<String> values, String pattern, IAnnotations annotations) {
        this.iterationDimension = iterationDimension;
        this.pattern = (pattern == null ? null : new TextPattern(pattern));
        this.annotations = annotations;
        this.values = values;
    }

    @Override
    public boolean apply(T object, IMatrixPosition position) {
        currentIdentifier = position.get(iterationDimension);
        String value = (pattern != null ? pattern.generate(this) : currentIdentifier);
        return values.contains(value);
    }

    @Override
    public String resolveValue(String variableName) {

        if (variableName.equalsIgnoreCase("id")) {
            return currentIdentifier;
        }

        String annotation = null;
        if (annotations != null) {
            annotation = annotations.getAnnotation(currentIdentifier, variableName);
        }

        if (annotation == null) {
            return "";
        }

        return annotation;

    }
}
