/*
 * #%L
 * gitools-ui-platform
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
