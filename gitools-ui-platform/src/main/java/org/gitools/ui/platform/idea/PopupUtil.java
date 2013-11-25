package org.gitools.ui.platform.idea;

import javax.swing.*;
import java.lang.reflect.Method;

public class PopupUtil {



    public static void setPopupType(final javax.swing.PopupFactory factory, final int type) {
        try {
            final Method method = PopupFactory.class.getDeclaredMethod("setPopupType", int.class);
            method.setAccessible(true);
            method.invoke(factory, type);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static int getPopupType(final PopupFactory factory) {
        try {
            final Method method = PopupFactory.class.getDeclaredMethod("getPopupType");
            method.setAccessible(true);
            final Object result = method.invoke(factory);
            return result instanceof Integer ? (Integer) result : -1;
        }
        catch (Throwable e) {
            e.printStackTrace();
        }

        return -1;
    }
}
