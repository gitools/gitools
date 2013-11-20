package org.gitools.core.matrix.filter;

import com.google.common.base.Function;
import org.gitools.core.matrix.model.IAnnotations;

public class AnnotationFunction implements Function<String, String> {

    private String label;
    private IAnnotations annotations;

    public AnnotationFunction(String label, IAnnotations annotations) {
        this.label = label;
        this.annotations = annotations;
    }

    @Override
    public String apply(String identifier) {
        return annotations.getAnnotation(identifier, label);
    }
}
