package org.gitools.ui.platform.idea;

import org.gitools.ui.platform.os.SystemInfo;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Method;

public class UIUtil {

    public static final Color AQUA_SEPARATOR_FOREGROUND_COLOR = Gray._190;
    public static final Color AQUA_SEPARATOR_BACKGROUND_COLOR = Gray._240;

    public static final Color GTK_AMBIANCE_TEXT_COLOR = new Color(223, 219, 210);

    public static final Insets PANEL_SMALL_INSETS = new Insets(5, 8, 5, 8);

    public static boolean isUnderGTKLookAndFeel() {
        return UIManager.getLookAndFeel().getName().contains("GTK");
    }

    public static boolean isWinLafOnVista() {
        return SystemInfo.isWinVistaOrNewer && "Windows".equals(UIManager.getLookAndFeel().getName());
    }

    public static boolean isUnderAquaLookAndFeel() {
        return SystemInfo.isMac && UIManager.getLookAndFeel().getName().contains("Mac OS X");
    }

    public static Paint getGradientPaint(float x1, float y1, Color c1, float x2, float y2, Color c2) {
        return new GradientPaint(x1, y1, c1, x2, y2, c2);
    }

    public static void setNotOpaqueRecursively(Component component) {
        if (!isUnderAquaLookAndFeel()) return;

        if (component.getBackground().equals(getPanelBackground()) || component instanceof JScrollPane || component instanceof JViewport) {
            if (component instanceof JComponent) {
                ((JComponent)component).setOpaque(false);
            }
            if (component instanceof Container) {
                for (Component c : ((Container)component).getComponents()) {
                    setNotOpaqueRecursively(c);
                }
            }
        }
    }

    public static Color getPanelBackground() {
        return UIManager.getColor("Panel.background");
    }

    public static Color getLabelForeground() {
        return UIManager.getColor("Label.foreground");
    }

    public static void addInsets(JComponent component, Insets insets) {
        if (component.getBorder() != null) {
            component.setBorder(new CompoundBorder(new EmptyBorder(insets), component.getBorder()));
        }
        else {
            component.setBorder(new EmptyBorder(insets));
        }
    }

    public static String getGtkThemeName() {
        final LookAndFeel laf = UIManager.getLookAndFeel();
        if (laf != null && "GTKLookAndFeel".equals(laf.getClass().getSimpleName())) {
            try {
                final Method method = laf.getClass().getDeclaredMethod("getGtkThemeName");
                method.setAccessible(true);
                final Object theme = method.invoke(laf);
                if (theme != null) {
                    return theme.toString();
                }
            }
            catch (Exception ignored) {
            }
        }
        return null;
    }

}
