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
package org.gitools.core.matrix.filter;

import com.google.common.base.Function;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.utils.textpatt.TextPattern;

public class PatternFunction implements Function<String, String>, TextPattern.VariableValueResolver {

    private IAnnotations annotations;
    private TextPattern pattern;

    private ThreadLocal<String> currentIdentifier;

    public PatternFunction(String pattern, IAnnotations annotations) {
        this.pattern = (pattern == null ? null : new TextPattern(pattern));
        this.annotations = annotations;
        this.currentIdentifier = new ThreadLocal<>();
    }

    @Override
    public String resolveValue(String variableName) {

        if (variableName.equalsIgnoreCase("id")) {
            return currentIdentifier.get();
        }

        String annotation = null;
        if (annotations != null) {
            annotation = annotations.getAnnotation(currentIdentifier.get(), variableName);
        }

        if (annotation == null) {
            return "";
        }

        return annotation;
    }

    @Override
    public String apply(String input) {

        if (input == null) {
            return null;
        }

        if (pattern == null) {
            return input;
        }

        currentIdentifier.set(input);

        return pattern.generate(this);
    }
}
