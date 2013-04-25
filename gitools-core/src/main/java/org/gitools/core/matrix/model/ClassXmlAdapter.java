package org.gitools.core.matrix.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ClassXmlAdapter extends XmlAdapter<String, Class> {
    @Override
    public Class unmarshal(String v) throws Exception {

        if (v.equalsIgnoreCase("double")) {
            return double.class;
        }

        if (v.equalsIgnoreCase("integer")) {
            return int.class;
        }

        if (v.equalsIgnoreCase("boolean")) {
            return boolean.class;
        }

        return String.class;
    }

    @Override
    public String marshal(Class v) throws Exception {

        if (v.equals(double.class) || v.equals(Double.class)) {
            return "double";
        }

        if (v.equals(int.class) || v.equals(Integer.class)) {
            return "integer";
        }

        if (v.equals(boolean.class) || v.equals(Boolean.class)) {
            return "boolean";
        }

        return "string";

    }
}
