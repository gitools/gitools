package edu.upf.bg.colorscale;

import java.awt.*;
import java.io.Serializable;

public class ColorScalePoint implements Serializable {

    private double value;
    private Color color;

    public ColorScalePoint(double value, Color color) {
        this.value = value;
        this.color = color;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
