package org.gitools.core.matrix.filter;

import com.google.common.base.Function;
import org.gitools.core.matrix.model.IAnnotations;
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
