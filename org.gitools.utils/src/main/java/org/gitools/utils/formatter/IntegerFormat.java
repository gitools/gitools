package org.gitools.utils.formatter;

import java.text.DecimalFormat;
import java.text.ParsePosition;

public class IntegerFormat extends DecimalFormat {

    private static IntegerFormat INSTANCE = new IntegerFormat();

    public static IntegerFormat get() {
        return INSTANCE;
    }

    private IntegerFormat() {
        super("#");
    }

    @Override
    public Number parse(String text, ParsePosition pos) {

        Number result = super.parse(text, pos);
        if (result instanceof Long) {
            return result.intValue();
        }

        return result;
    }
}
