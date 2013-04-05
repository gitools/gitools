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
package org.gitools.ui.platform.dialog;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @noinspection ALL
 */
public class MessageUtils
{

    private static final Logger log = Logger.getLogger(MessageUtils.class);

    // Somewhat silly class, needed to pass values between threads
    static class ValueHolder
    {
        Object value;
    }

    /**
     * Log the exception and show {@code message} to the user
     *
     * @param e
     * @param message
     */
    public static void showErrorMessage(final Frame parent, String message, Exception e)
    {
        log.error(message, e);
        showMessage(parent, Level.ERROR, message);
    }

    public static void showMessage(final Frame parent, String message)
    {
        showMessage(parent, Level.INFO, message);
    }

    private static synchronized void showMessage(@Nullable final Frame parent, Level level, String message)
    {

        log.log(level, message);
        // Always use HTML for message displays, but first remove any embedded <html> tags.
        message = "<html>" + message.replaceAll("<html>", "");
        Color background = parent != null ? parent.getBackground() : Color.lightGray;
        //So users can select text
        JEditorPane content = new JEditorPane();
        content.setContentType("text/html");
        content.setText(message);
        content.setBackground(background);
        JOptionPane.showMessageDialog(parent, content);
    }

    /**
     * Show a yes/no confirmation dialog.
     *
     * @param component
     * @param message
     * @return
     */
    public static synchronized boolean confirm(final Component component, final String message)
    {


        if (SwingUtilities.isEventDispatchThread())
        {
            int opt = JOptionPane.showConfirmDialog(component, message, "Confirm", JOptionPane.YES_NO_OPTION);
            return opt == JOptionPane.YES_OPTION;
        }
        else
        {
            final ValueHolder returnValue = new ValueHolder();
            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    int opt = JOptionPane.showConfirmDialog(component, message, "Confirm", JOptionPane.YES_NO_OPTION);
                    returnValue.value = (opt == JOptionPane.YES_OPTION);
                }
            };
            try
            {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException e)
            {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e);
            } catch (InvocationTargetException e)
            {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e.getCause());
            }

            return (Boolean) (returnValue.value);

        }
    }

    public static String showInputDialog(final Frame parent, final String message, final String defaultValue)
    {

        if (SwingUtilities.isEventDispatchThread())
        {
            String val = JOptionPane.showInputDialog(parent, message, defaultValue);
            return val;
        }
        else
        {
            final ValueHolder returnValue = new ValueHolder();
            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    String val = JOptionPane.showInputDialog(parent, message, defaultValue);
                    returnValue.value = val;
                }
            };
            try
            {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException e)
            {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e);
            } catch (InvocationTargetException e)
            {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e.getCause());
            }

            return (String) (returnValue.value);
        }
    }

    public static String showInputDialog(final Frame parent, final String message)
    {


        if (SwingUtilities.isEventDispatchThread())
        {
            String val = JOptionPane.showInputDialog(parent, message);
            return val;
        }
        else
        {
            final ValueHolder returnValue = new ValueHolder();
            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    String val = JOptionPane.showInputDialog(parent, message);
                    returnValue.value = val;
                }
            };
            try
            {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException e)
            {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e);
            } catch (InvocationTargetException e)
            {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e.getCause());
            }

            return (String) (returnValue.value);
        }
    }


}
