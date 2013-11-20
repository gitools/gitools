package org.gitools.core.matrix.model.iterable;

import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.matrix.model.IAnnotations;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixPosition;
import org.gitools.core.matrix.model.IMatrixPredicate;
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

    public IdentifiersPredicate(HeatmapDimension iterationDimension, String pattern, Set<String> values) {
        this(iterationDimension, values, pattern, iterationDimension.getAnnotations());
    }

    private IdentifiersPredicate(IMatrixDimension iterationDimension, Set<String> values, String pattern, IAnnotations annotations) {
        this.iterationDimension = iterationDimension;
        this.pattern = (pattern == null ? null : new TextPattern(pattern));
        this.annotations = annotations;
        this.values = values;
    }

    @Override
    public boolean apply(T object, IMatrixPosition position) {
        currentIdentifier = position.get(iterationDimension);
        String value = (pattern != null ? pattern.generate( this ) : currentIdentifier);
        return values.contains( value );
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
