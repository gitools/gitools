package org.gitools.ui.wizard.common;

public class AnnotationOption {

    private String key;
    private String description;

    public AnnotationOption(String key) {
        this(key, null);
    }

    public AnnotationOption(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "<html><strong>" + key + "</strong>" + (description == null? "" : " - " +description) + "</html>";
    }
}
