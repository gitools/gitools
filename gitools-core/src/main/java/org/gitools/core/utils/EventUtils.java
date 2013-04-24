/*
 * #%L
 * gitools-core
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
package org.gitools.core.utils;

import com.jgoodies.binding.beans.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EventUtils {
    public static boolean isAny(PropertyChangeEvent event, Class sourceClass, String... propertyNames) {
        String propertyName = event.getPropertyName();
        Class eventClass = event.getSource().getClass();

        if (!sourceClass.isAssignableFrom(eventClass)) {
            return false;
        }

        if (propertyNames.length == 0) {
            return true;
        }

        for (String name : propertyNames) {
            if (name.equals(propertyName)) {
                return true;
            }
        }

        return false;
    }

    public static void removeListeners(Model model) {
        PropertyChangeListener[] listeners = model.getPropertyChangeListeners();

        for (PropertyChangeListener listener : listeners) {
            model.removePropertyChangeListener(listener);
        }
    }

    public static void moveListeners(Model from, Model to) {
        removeListeners(to);
        for (PropertyChangeListener listener : from.getPropertyChangeListeners()) {
            to.addPropertyChangeListener(listener);
        }

        removeListeners(from);
    }
}
