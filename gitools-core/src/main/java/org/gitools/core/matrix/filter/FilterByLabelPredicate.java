package org.gitools.core.matrix.filter;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.gitools.core.matrix.NoTransformFunction;
import org.jetbrains.annotations.NotNull;

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
        return filter.apply( transformFunction.apply(input) );
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
        public RegexFilter(@NotNull Set<String> values) {
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
