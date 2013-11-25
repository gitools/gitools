package org.gitools.ui.platform.idea;

import javax.swing.*;
import java.awt.*;

public class ScreenUtil {
    public static final String DISPOSE_TEMPORARY = "dispose.temporary";

    public static void moveRectangleToFitTheScreen(Rectangle aRectangle) {
        int screenX = aRectangle.x + aRectangle.width / 2;
        int screenY = aRectangle.y + aRectangle.height / 2;
        Rectangle screen = getScreenRectangle(screenX, screenY);

        moveToFit(aRectangle, screen, null);
    }

    public static Rectangle getScreenRectangle(int x, int y) {
        return getScreenRectangle(new Point(x, y));
    }

    public static Rectangle getScreenRectangle(Point p) {
        double distance = -1;
        Rectangle answer = null;

        Rectangle[] allScreenBounds = getAllScreenBounds();
        for (Rectangle rect : allScreenBounds) {
            if (rect.contains(p)) {
                return rect;
            }

            final double d = findNearestPointOnBorder(rect, p).distance(p.x, p.y);
            if (answer == null || distance > d) {
                distance = d;
                answer = rect;
            }
        }

        if (answer == null) {
            throw new IllegalStateException("It's impossible to determine target graphics environment for point (" + p.x + "," + p.y + ")");
        }

        return answer;
    }

    private static Rectangle[] getAllScreenBounds() {
        final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice[] devices = env.getScreenDevices();
        Rectangle[] result = new Rectangle[devices.length];
        for (int i = 0; i < devices.length; i++) {
            GraphicsDevice device = devices[i];
            GraphicsConfiguration configuration = device.getDefaultConfiguration();
            result[i] = new Rectangle(configuration.getBounds());
            applyInsets(result[i], getScreenInsets(configuration));
        }
        return result;
    }

    public static Point findNearestPointOnBorder(Rectangle rect, Point p) {
        final int x0 = rect.x;
        final int y0 = rect.y;
        final int x1 = x0 + rect.width;
        final int y1 = y0 + rect.height;
        double distance = -1;
        Point best = null;
        final Point[] variants = {new Point(p.x, y0), new Point(p.x, y1), new Point(x0, p.y), new Point(x1, p.y)};
        for (Point variant : variants) {
            final double d = variant.distance(p.x, p.y);
            if (best == null || distance > d) {
                best = variant;
                distance = d;
            }
        }
        assert best != null;
        return best;
    }

    private static Rectangle applyInsets(Rectangle rect, Insets i) {
        if (i == null) {
            return rect;
        }

        return new Rectangle(rect.x + i.left, rect.y + i.top, rect.width - (i.left + i.right), rect.height - (i.top + i.bottom));
    }

    public static Insets getScreenInsets(final GraphicsConfiguration gc) {
        //if (ourInsetsCache == null) {
            return calcInsets(gc);
        //}

        /*
        synchronized (ourInsetsCache) {
            Pair<Insets, Long> data = ourInsetsCache.get(gc);
            final long now = System.currentTimeMillis();
            if (data == null || now > data.second + ourInsetsTimeout) {
                data = Pair.create(calcInsets(gc), now);
                ourInsetsCache.put(gc, data);
            }
            return data.first;
        } */
    }

    private static Insets calcInsets(GraphicsConfiguration gc) {
        if (Patches.SUN_BUG_ID_7172665 && GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length > 1) {
            return new Insets(0, 0, 0, 0);
        }

        return Toolkit.getDefaultToolkit().getScreenInsets(gc);
    }

    public static void moveToFit(final Rectangle rectangle, final Rectangle container, Insets padding) {
        Insets insets = padding != null ? padding : new Insets(0, 0, 0, 0);

        Rectangle move = new Rectangle(rectangle.x - insets.left, rectangle.y - insets.top, rectangle.width + insets.left + insets.right,
                rectangle.height + insets.top + insets.bottom);

        if (move.getMaxX() > container.getMaxX()) {
            move.x = (int)container.getMaxX() - move.width;
        }


        if (move.getMinX() < container.getMinX()) {
            move.x = (int)container.getMinX();
        }

        if (move.getMaxY() > container.getMaxY()) {
            move.y = (int)container.getMaxY() - move.height;
        }

        if (move.getMinY() < container.getMinY()) {
            move.y = (int)container.getMinY();
        }

        rectangle.x = move.x + insets.left;
        rectangle.y = move.y + insets.right;
        rectangle.width = move.width - insets.left - insets.right;
        rectangle.height = move.height - insets.top - insets.bottom;
    }

    /**
     * Method removeNotify (and then addNotify) will be invoked for all components when main frame switches between states "Normal" <-> "FullScreen".
     * In this case we shouldn't call Disposer  in removeNotify and/or release some resources that we won't initialize again in addNotify (e.g. listeners).
     */
    public static boolean isStandardAddRemoveNotify(Component component) {
        JRootPane rootPane = findMainRootPane(component);
        return rootPane == null || rootPane.getClientProperty(DISPOSE_TEMPORARY) == null;
    }

    private static JRootPane findMainRootPane(Component component) {
        while (component != null) {
            Container parent = component.getParent();
            if (parent == null) {
                return component instanceof RootPaneContainer ? ((RootPaneContainer)component).getRootPane() : null;
            }
            component = parent;
        }
        return null;
    }


}
