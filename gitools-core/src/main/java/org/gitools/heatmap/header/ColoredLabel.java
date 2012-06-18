package org.gitools.heatmap.header;

import edu.upf.bg.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

/**
* Created by IntelliJ IDEA.
* User: mschroeder
* Date: 6/15/12
* Time: 3:21 PM
* To change this template use File | Settings | File Templates.
*/
public class ColoredLabel {

    protected String description;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    protected Color color;

    protected String value;

    public ColoredLabel() {
        description = "";
        color = Color.WHITE;
        value = "";
    }

    public ColoredLabel(String value, Color color) {
        this(value,value, color);
    }

    public ColoredLabel( String value, String description, Color color) {
        this.description = description;
        this.value = value;
        this.color = color;
    }

    public ColoredLabel(int value, String description, Color color) {
        this.description = description;
        setValue(description);
        this.color = color;
    }

    public ColoredLabel(double value, String description, Color color) {
        this.description = description;
        setValue(description);
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = Double.toString(value);
    }

    public void setValue(int value) {
        this.value = Integer.toString(value);
    }

    @Override
    public String toString() {
        return description;
    }
}
