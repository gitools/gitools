/*
 * Copyright (c) 2007-2012 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gitools.ui.platform.dialog;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class MessageUtils {

    private static Logger log = Logger.getLogger(MessageUtils.class);

    // Somewhat silly class, needed to pass values between threads
    static class ValueHolder {
        Object value;
    }

    /**
     * Log the exception and show {@code message} to the user
     *
     * @param e
     * @param message
     */
    public static void showErrorMessage(final Frame parent, String message, Exception e) {
        log.error(message, e);
        showMessage(parent, Level.ERROR, message);
    }

    public static void showMessage(final Frame parent, String message) {
        showMessage(parent, Level.INFO, message);
    }

    public static synchronized void showMessage(final Frame parent, Level level, String message) {

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
    public static synchronized boolean confirm(final Component component, final String message) {


        if (SwingUtilities.isEventDispatchThread()) {
            int opt = JOptionPane.showConfirmDialog(component, message, "Confirm", JOptionPane.YES_NO_OPTION);
            return opt == JOptionPane.YES_OPTION;
        } else {
            final ValueHolder returnValue = new ValueHolder();
            Runnable runnable = new Runnable() {
                public void run() {
                    int opt = JOptionPane.showConfirmDialog(component, message, "Confirm", JOptionPane.YES_NO_OPTION);
                    returnValue.value = (opt == JOptionPane.YES_OPTION);
                }
            };
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException e) {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e.getCause());
            }

            return (Boolean) (returnValue.value);

        }
    }

    public static String showInputDialog(final Frame parent, final String message, final String defaultValue) {

        if (SwingUtilities.isEventDispatchThread()) {
            String val = JOptionPane.showInputDialog(parent, message, defaultValue);
            return val;
        } else {
            final ValueHolder returnValue = new ValueHolder();
            Runnable runnable = new Runnable() {
                public void run() {
                    String val = JOptionPane.showInputDialog(parent, message, defaultValue);
                    returnValue.value = val;
                }
            };
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException e) {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e.getCause());
            }

            return (String) (returnValue.value);
        }
    }

    public static String showInputDialog(final Frame parent, final String message) {


        if (SwingUtilities.isEventDispatchThread()) {
            String val = JOptionPane.showInputDialog(parent, message);
            return val;
        } else {
            final ValueHolder returnValue = new ValueHolder();
            Runnable runnable = new Runnable() {
                public void run() {
                    String val = JOptionPane.showInputDialog(parent, message);
                    returnValue.value = val;
                }
            };
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException e) {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                log.error("Error in showMessage", e);
                throw new RuntimeException(e.getCause());
            }

            return (String) (returnValue.value);
        }
    }


}
