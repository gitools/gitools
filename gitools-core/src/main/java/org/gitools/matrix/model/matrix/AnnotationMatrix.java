package org.gitools.matrix.model.matrix;

import org.gitools.model.Resource;

import java.util.*;

public class AnnotationMatrix extends Resource implements IAnnotations
{
    private Set<String> labels;
    private Map<String, Map<String, String>> annotations;

    public AnnotationMatrix()
    {
        this.labels = new HashSet<String>();
        this.annotations = new HashMap<String, Map<String, String>>();
    }

    public AnnotationMatrix(IAnnotations annotations)
    {
        this();

        addAnnotations(annotations);
    }

    public void addAnnotations(IAnnotations annotations)
    {
        for (String identifier : annotations.getIdentifiers())
        {
            for (String label : annotations.getLabels())
            {
                setAnnotation(identifier, label, annotations.getAnnotation(identifier, label));
            }
        }
    }

    @Override
    public boolean hasIdentifier(String identifier)
    {
        return annotations.containsKey(identifier);
    }

    @Override
    public Collection<String> getIdentifiers()
    {
        return annotations.keySet();
    }

    @Override
    public Collection<String> getLabels()
    {
        return labels;
    }

    @Override
    public String getAnnotation(String identifier, String annotationLabel)
    {
        if (!hasIdentifier(identifier))
        {
            return null;
        }

        return annotations.get(identifier).get(annotationLabel);
    }

    public void setAnnotation(String identifier, String annotationLabel, String value)
    {
        if (!labels.contains(annotationLabel))
        {
            labels.add(annotationLabel);
        }

        Map<String, String> identifierAnnotations = annotations.get(identifier);

        if (identifierAnnotations == null)
        {
            identifierAnnotations = new HashMap<String, String>(labels.size());
            annotations.put(identifier, identifierAnnotations);
        }

        identifierAnnotations.put(annotationLabel, value);
    }
}
