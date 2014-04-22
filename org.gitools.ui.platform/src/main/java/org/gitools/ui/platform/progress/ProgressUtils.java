package org.gitools.ui.platform.progress;


import javax.swing.*;
import java.awt.*;

public class ProgressUtils {


    public static Window getParentGlassPaneWindow(Component component) {
        if (component.getParent() == null) {
            return null;
        } else if (component.getParent() instanceof Window &&
                component.getParent() instanceof RootPaneContainer) {
            return (Window) component.getParent();
        } else {
            return getParentGlassPaneWindow(component.getParent());
        }
    }
}
