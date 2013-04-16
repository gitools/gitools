package org.gitools.utils;

import com.jgoodies.binding.beans.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EventUtils
{
    public static boolean isAny(PropertyChangeEvent event, Class sourceClass, String... propertyNames)
    {
        String propertyName = event.getPropertyName();
        Class eventClass = event.getSource().getClass();

        if (!sourceClass.isAssignableFrom(eventClass))
        {
            return false;
        }

        if (propertyNames.length == 0)
        {
            return true;
        }

        for (String name : propertyNames)
        {
            if (name.equals(propertyName))
            {
                return true;
            }
        }

        return false;
    }

    public static void removeListeners(Model model)
    {
        PropertyChangeListener[] listeners = model.getPropertyChangeListeners();

        for (PropertyChangeListener listener : listeners)
        {
            model.removePropertyChangeListener(listener);
        }
    }

    public static void moveListeners(Model from, Model to)
    {
        removeListeners(to);
        for (PropertyChangeListener listener : from.getPropertyChangeListeners())
        {
            to.addPropertyChangeListener(listener);
        }

        removeListeners(from);
    }
}
