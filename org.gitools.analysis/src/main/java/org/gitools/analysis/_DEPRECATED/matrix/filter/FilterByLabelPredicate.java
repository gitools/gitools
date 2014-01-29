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
package org.gitools.analysis._DEPRECATED.matrix.filter;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class FilterByLabelPredicate implements Predicate<String> {

    private LabelFilter filter;
    private Function<String, String> transformFunction;

    public FilterByLabelPredicate(Set<String> values, boolean useRegex) {
        this(new NoTransformFunction<String>(), values, useRegex);
    }

    public FilterByLabelPredicate(Function<String, String> transformFunction, Set<String> values, boolean useRegex) {
        this.transformFunction = transformFunction;
        this.filter = (useRegex ? new RegexFilter(values) : new StringFilter(values));
    }

    @Override
    public boolean apply(String input) {
        return filter.apply(transformFunction.apply(input));
    }


    private static interface LabelFilter {
        boolean apply(String label);
    }

    private static class StringFilter implements LabelFilter {
        private final Set<String> values;

        public StringFilter(Set<String> values) {
            this.values = values;
        }

        @Override
        public boolean apply(String label) {
            return values.contains(label);
        }
    }

    private static class RegexFilter implements LabelFilter {
        private final List<Pattern> patterns;

        public RegexFilter(Set<String> values) {
            patterns = new ArrayList<>(values.size());
            for (String value : values)
                if (!value.trim().isEmpty()) {
                    patterns.add(Pattern.compile(value));
                }
        }

        @Override
        public boolean apply(String label) {
            for (Pattern pattern : patterns) {
                if (pattern.matcher(label).matches()) {
                    return true;
                }
            }
            return false;
        }
    }

}
