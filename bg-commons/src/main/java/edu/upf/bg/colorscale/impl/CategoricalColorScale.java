package edu.upf.bg.colorscale.impl;


import edu.upf.bg.color.utils.ColorUtils;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.NumericColorScale;

import java.awt.*;
import java.util.Arrays;

public class CategoricalColorScale extends NumericColorScale {

    private ColorScalePoint[] points;
    private Color[] predefinedColors = new Color[] {
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.YELLOW,
            Color.CYAN,
            Color.ORANGE,
            Color.PINK,
            Color.MAGENTA,
            Color.BLACK,
            Color.GRAY
    };

    public CategoricalColorScale() {
        this(
                new double[] {1.0,2.0}
        );
    }

    public CategoricalColorScale(ColorScalePoint[] points) {
        super();
        this.points = points;
    }
    
    public CategoricalColorScale(double pointValues[]) {

        setValues(pointValues);
    }

    public void setValues(double[] pointValues) {
        Arrays.sort(pointValues);
        this.points = new ColorScalePoint[pointValues.length];
        /*if (pointValues.length > predefinedColors.length) {
            try {
                throw new Exception("Too many different values for categorical scale");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } */

        
        Color[] palette = generateColors(pointValues.length);
        for (int i = 0; i<pointValues.length; i++) {
            this.points[i] =
                    new ColorScalePoint(pointValues[i],palette[i]);
            /*
            if (i < 10)
               this.points[i] =
                    new ColorScalePoint(pointValues[i],predefinedColors[i]);
            else if(i < 20)
                this.points[i] =
                        new ColorScalePoint(pointValues[i],predefinedColors[i-10].brighter());
            else if(i < 30)
                this.points[i] =
                        new ColorScalePoint(pointValues[i],predefinedColors[i-20].brighter().brighter());
            else if(i < 40)
                this.points[i] =
                        new ColorScalePoint(pointValues[i],predefinedColors[i-30].brighter().brighter().brighter());*/
        }

    }

    public Color[] generateColors(int n)
    {
        Color[] cols = new Color[n];
        for(int i = 0; i < n; i++)
        {
            cols[i] = Color.getHSBColor((float) i / (float) n, 0.85f, 1.0f);
        }
        return cols;
    }

    public ColorScalePoint getColorScalePoint(double value) {
        for (ColorScalePoint p: points) {
            if (p.getValue() == value)
                return p;
        }
        return null;
    }

    @Override
    protected Color colorOf(double value) {

       for (ColorScalePoint p: points) {
           if (p.getValue() == value)
               return p.getColor();
       }
       return super.getEmptyColor();
    }

    @Override
    public double[] getPoints() {
        double[] pointValues = new double[points.length];
        for(int i = 0; i<points.length;i++)
            pointValues[i] = points[i].getValue();
        return pointValues;
    }

    public ColorScalePoint getMin() {
        return points[0];
    }

    public ColorScalePoint getMax() {
        return points[points.length-1];
    }
}
