package org.gitools.matrix.model.matrix;

public class Annotation
{
    private final String key;
    private final String value;

    public Annotation(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }
}
