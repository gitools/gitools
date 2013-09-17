package org.gitools.utils.formatter;

import java.text.DecimalFormat;
import java.util.Formatter;

/**
 * Created with IntelliJ IDEA.
 * User: mschroeder
 * Date: 9/16/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class DetailsBoxFormatter extends HeatmapTextFormatter {

    protected final StringBuilder sb;
    protected final Formatter fmt;
    protected final DecimalFormat countFormat;

    public DetailsBoxFormatter() {
        sb = new StringBuilder(14);
        fmt = new Formatter(sb);
        countFormat = new DecimalFormat("#.#########");
    }

    @Override
    protected String decimal(double value) {

        if (value!=0 && value < 1e-99 && value > -1e-99) {
            return "~0.00";
        }

        if (value < 1 && value > -1) {
            sb.setLength(0);
            fmt.format("%.2g", value);
            return sb.toString();
        } else {
            return  countFormat.format(value);
        }
    }


}
