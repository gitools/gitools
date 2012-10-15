package edu.upf.bg.colorscale;


public class ColorScaleRange {

    public static String CONSTANT_TYPE = "constant";
    public static String EMPTY_TYPE = "empty";
    public static String LINEAR_TYPE = "linear";
    public static String LOGARITHMIC_TYPE = "logarithmic";
    
    private String type = "";
    private Object leftLabel = null;
    private Object centerLabel = null;
    private Object rightLabel = null;
    private double width;
    private double minValue;
    private double maxValue;

    private boolean borderEnabled = true;

        
    public ColorScaleRange(double minValue, double maxValue, double width) {
           this(minValue,maxValue,width,null,null,null,LINEAR_TYPE);
    }

    public ColorScaleRange(double minValue, double maxValue, double width, Object leftLabel, Object centerLabel, Object rightLabel, String type) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.width = width;
        this.leftLabel = leftLabel;
        this.rightLabel = rightLabel;
        this.centerLabel = centerLabel;
        this.type = type;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getLeftLabel() {
        return leftLabel;
    }

    public void setLeftLabel(Object leftLabel) {
        this.leftLabel = leftLabel;
    }

    public Object getCenterLabel() {
        return centerLabel;
    }

    public void setCenterLabel(Object centerLabel) {
        this.centerLabel = centerLabel;
    }

    public Object getRightLabel() {
        return rightLabel;
    }

    public void setRightLabel(Object rightLabel) {
        this.rightLabel = rightLabel;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isBorderEnabled() {
        return borderEnabled;
    }

    public void setBorderEnabled(boolean borderEnabled) {
        this.borderEnabled = borderEnabled;
    }
}
