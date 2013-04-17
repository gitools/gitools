package org.gitools.matrix.model.matrix;

import org.gitools.persistence.IResource;

import java.util.Collection;

public interface IAnnotations extends IResource
{

    boolean hasIdentifier(String identifier);

    Collection<String> getLabel();

    String getAnnotation(String identifier, String annotationLabel);


}
