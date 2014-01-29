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
package org.gitools.ui.platform;

import com.alee.laf.WebLookAndFeel;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import org.gitools.ui.platform.idea.PopupUtil;
import org.gitools.ui.platform.idea.ScreenUtil;
import org.gitools.ui.platform.idea.UIUtil;
import org.gitools.ui.platform.os.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LaFManager {

    private static final Logger log = LoggerFactory.getLogger(LaFManager.class);

    private static final String[] ourOptionPaneIconKeys = {"OptionPane.errorIcon", "OptionPane.informationIcon",
            "OptionPane.warningIcon", "OptionPane.questionIcon"};

    public static void install() {
       installGTKLookAndFeel();
       updateUI();
    }

    private static void installWebLookAndFeel() {
        WebLookAndFeel.install();
        WebLookAndFeel.initializeManagers();
    }

    private static void installGTKLookAndFeel() {
        LookAndFeel laf = new GTKLookAndFeel();

        try {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void updateUI() {
        final UIDefaults uiDefaults = UIManager.getLookAndFeelDefaults();

        fixPopupWeight();

        fixGtkPopupStyle();

        fixMenuIssues(uiDefaults);

        if (UIUtil.isWinLafOnVista()) {
            uiDefaults.put("ComboBox.border", null);
        }

        uiDefaults.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);

        patchOptionPaneIcons(uiDefaults);

        fixSeparatorColor(uiDefaults);

        for (Frame frame : Frame.getFrames()) {
            updateUI(frame);
        }

    }

    private static void fixSeparatorColor(UIDefaults uiDefaults) {
        if (UIUtil.isUnderAquaLookAndFeel()) {
            uiDefaults.put("Separator.background", UIUtil.AQUA_SEPARATOR_BACKGROUND_COLOR);
            uiDefaults.put("Separator.foreground", UIUtil.AQUA_SEPARATOR_FOREGROUND_COLOR);
        }
    }

    private static void fixMenuIssues(UIDefaults uiDefaults) {
        uiDefaults.put("MenuItem.background", UIManager.getColor("Menu.background"));
    }

    /**
     * The following code is a trick! By default Swing uses lightweight and "medium" weight
     * popups to show JPopupMenu. The code below force the creation of real heavyweight menus -
     * this increases speed of popups and allows to get rid of some drawing artifacts.
     */
    private static void fixPopupWeight() {
        int popupWeight = OurPopupFactory.WEIGHT_MEDIUM;
        String property = System.getProperty("idea.popup.weight");
        if (property != null) property = property.toLowerCase().trim();
        if (SystemInfo.isMacOSLeopard) {
            // force heavy weight popups under Leopard, otherwise they don't have shadow or any kind of border.
            popupWeight = OurPopupFactory.WEIGHT_HEAVY;
        }
        else if (property == null) {
            // use defaults if popup weight isn't specified
            if (SystemInfo.isWindows) {
                popupWeight = OurPopupFactory.WEIGHT_HEAVY;
            }
        }
        else {
            if ("light".equals(property)) {
                popupWeight = OurPopupFactory.WEIGHT_LIGHT;
            }
            else if ("medium".equals(property)) {
                popupWeight = OurPopupFactory.WEIGHT_MEDIUM;
            }
            else if ("heavy".equals(property)) {
                popupWeight = OurPopupFactory.WEIGHT_HEAVY;
            }
            else {
                log.error("Illegal value of property \"idea.popup.weight\": " + property);
            }
        }

        PopupFactory factory = PopupFactory.getSharedInstance();
        if (!(factory instanceof OurPopupFactory)) {
            factory = new OurPopupFactory(factory);
            PopupFactory.setSharedInstance(factory);
        }
        PopupUtil.setPopupType(factory, popupWeight);
    }

    /**
     * Swing menus are looking pretty bad on Linux when the GTK LaF is used (See
     * bug #6925412). It will most likely never be fixed anytime soon so this
     * method provides a workaround for it. It uses reflection to change the GTK
     * style objects of Swing so popup menu borders have a minimum thickness of
     * 1 and menu separators have a minimum vertical thickness of 1.
     */
    public static void fixGtkPopupStyle()
    {
        // Get current look-and-feel implementation class
        LookAndFeel laf = UIManager.getLookAndFeel();
        Class<?> lafClass = laf.getClass();

        // Do nothing when not using the problematic LaF
        if (!lafClass.getName().equals(
                "com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) return;

        // We do reflection from here on. Failure is silently ignored. The
        // workaround is simply not installed when something goes wrong here
        try
        {
            // Access the GTK style factory
            Field field = lafClass.getDeclaredField("styleFactory");
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Object styleFactory = field.get(laf);
            field.setAccessible(accessible);

            // Fix the horizontal and vertical thickness of popup menu style
            Object style = getGtkStyle(styleFactory, new JPopupMenu(),
                    "POPUP_MENU");
            fixGtkThickness(style, "yThickness");
            fixGtkThickness(style, "xThickness");

            // Fix the vertical thickness of the popup menu separator style
            style = getGtkStyle(styleFactory, new JSeparator(),
                    "POPUP_MENU_SEPARATOR");
            fixGtkThickness(style, "yThickness");
        }
        catch (Exception e)
        {
            // Silently ignored. Workaround can't be applied.
        }
    }

    /**
     * Called internally by installGtkPopupBugWorkaround to fix the thickness
     * of a GTK style field by setting it to a minimum value of 1.
     *
     * @param style
     *            The GTK style object.
     * @param fieldName
     *            The field name.
     * @throws Exception
     *             When reflection fails.
     */
    private static void fixGtkThickness(Object style, String fieldName)
            throws Exception
    {
        Field field = style.getClass().getDeclaredField(fieldName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.setInt(style, Math.max(1, field.getInt(style)));
        field.setAccessible(accessible);
    }

    /**
     * Called internally by installGtkPopupBugWorkaround. Returns a specific
     * GTK style object.
     *
     * @param styleFactory
     *            The GTK style factory.
     * @param component
     *            The target component of the style.
     * @param regionName
     *            The name of the target region of the style.
     * @return The GTK style.
     * @throws Exception
     *             When reflection fails.
     */
    private static Object getGtkStyle(Object styleFactory,
                                      JComponent component, String regionName) throws Exception
    {
        // Create the region object
        Class<?> regionClass = Class.forName("javax.swing.plaf.synth.Region");
        Field field = regionClass.getField(regionName);
        Object region = field.get(regionClass);

        // Get and return the style
        Class<?> styleFactoryClass = styleFactory.getClass();
        Method method = styleFactoryClass.getMethod("getStyle",
                new Class<?>[] { JComponent.class, regionClass });
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        Object style = method.invoke(styleFactory, component, region);
        method.setAccessible(accessible);
        return style;
    }


    private static class OurPopupFactory extends PopupFactory {
        public static final int WEIGHT_LIGHT = 0;
        public static final int WEIGHT_MEDIUM = 1;
        public static final int WEIGHT_HEAVY = 2;

        private final PopupFactory myDelegate;

        public OurPopupFactory(final PopupFactory delegate) {
            myDelegate = delegate;
        }

        @Override
        public Popup getPopup(final Component owner, final Component contents, final int x, final int y) throws IllegalArgumentException {
            final Point point = fixPopupLocation(contents, x, y);

            final int popupType = UIUtil.isUnderGTKLookAndFeel() ? WEIGHT_HEAVY : PopupUtil.getPopupType(this);
            if (popupType >= 0) {
                PopupUtil.setPopupType(myDelegate, popupType);
            }

            final Popup popup = myDelegate.getPopup(owner, contents, point.x, point.y);
            fixPopupSize(popup, contents);
            return popup;
        }

        private static Point fixPopupLocation(final Component contents, final int x, final int y) {
            if (!(contents instanceof JToolTip)) return new Point(x, y);

            final PointerInfo info;
            try {
                info = MouseInfo.getPointerInfo();
            }
            catch (InternalError e) {
                // http://www.jetbrains.net/jira/browse/IDEADEV-21390
                // may happen under Mac OSX 10.5
                return new Point(x, y);
            }
            int deltaY = 0;

            if (info != null) {
                final Point mouse = info.getLocation();
                deltaY = mouse.y - y;
            }

            final Dimension size = contents.getPreferredSize();
            final Rectangle rec = new Rectangle(new Point(x, y), size);
            ScreenUtil.moveRectangleToFitTheScreen(rec);

            if (rec.y < y) {
                rec.y += deltaY;
            }

            return rec.getLocation();
        }

        private static void fixPopupSize(final Popup popup, final Component contents) {
            if (!UIUtil.isUnderGTKLookAndFeel() || !(contents instanceof JPopupMenu)) return;

            for (Class<?> aClass = popup.getClass(); aClass != null && Popup.class.isAssignableFrom(aClass); aClass = aClass.getSuperclass()) {
                try {
                    final Method getComponent = aClass.getDeclaredMethod("getComponent");
                    getComponent.setAccessible(true);
                    final Object component = getComponent.invoke(popup);
                    if (component instanceof JWindow) {
                        ((JWindow)component).setSize(new Dimension(0, 0));
                    }
                    break;
                }
                catch (Exception ignored) { }
            }
        }
    }

    private static void patchOptionPaneIcons(final UIDefaults defaults) {
        if (UIUtil.isUnderGTKLookAndFeel() && defaults.get(ourOptionPaneIconKeys[0]) == null) {
            // GTK+ L&F keeps icons hidden in style
            final SynthStyle style = SynthLookAndFeel.getStyle(new JOptionPane(""), Region.DESKTOP_ICON);
            if (style != null) {
                for (final String key : ourOptionPaneIconKeys) {
                    final Object icon = style.get(null, key);
                    if (icon != null) defaults.put(key, icon);
                }
            }
        }
    }

    private static void updateUI(Window window){
        if(!window.isDisplayable()){
            return;
        }
        SwingUtilities.updateComponentTreeUI(window);
        Window[] children=window.getOwnedWindows();
        for (Window aChildren : children) {
            updateUI(aChildren);
        }
    }


}
