package edu.upf.bg.colorscale;

import java.awt.*;
import java.io.Serializable;

public class ColorScalePoint implements Serializable, Comparable<ColorScalePoint> {

    private double value;
    private Color color;
    private String name;

    public ColorScalePoint(double value, Color color, String name) {
        this.value = value;
        this.color = color;
        this.name = name;
    }
    
    public ColorScalePoint(double value, Color color) {
        this.value = value;
        this.color = color;
        this.name = "";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(ColorScalePoint o) {
        if (this.value < o.getValue()) return -1;
        if (value > o.getValue()) return 1;
        return 0;
    }
}
