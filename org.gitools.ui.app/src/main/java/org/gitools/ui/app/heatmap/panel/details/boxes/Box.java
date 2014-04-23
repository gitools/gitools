package org.gitools.ui.app.heatmap.panel.details.boxes;

import com.alee.laf.scroll.WebScrollPane;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.jdesktop.swingx.JXTaskPane;

import javax.swing.*;
import java.awt.*;

public abstract class Box extends JXTaskPane {

    protected JPopupMenu popupMenu;
    private Heatmap heatmap;

    public Box(String title, ActionSet actions, Heatmap heatmap) {
        setTitle(title);
        setSpecial(true);

        this.heatmap = heatmap;

        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(new WebScrollPane(getContainer(), false, false));

        popupMenu = ActionSetUtils.createPopupMenu(actions);
    }

    abstract Container getContainer();

    abstract void initContainer();

    public abstract void registerListeners();

    public abstract void update();

    public Heatmap getHeatmap() {
        return heatmap;
    }
}
