package org.gitools.ui.app.heatmap.panel.details.boxes;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.plugin.IBoxPlugin;
import org.gitools.heatmap.plugin.SelectionPropertiesPlugin;

import java.util.ArrayList;


public class BoxCreator {

    Heatmap heatmap;

    public BoxCreator(Heatmap heatmap) {
        this.heatmap = heatmap;
    }

    public Box[] create(IBoxPlugin p) {
        ArrayList<Box> boxlist = new ArrayList<>();
        if (p instanceof SelectionPropertiesPlugin) {
            boxlist.add(new SelectionBox("Selection", null, heatmap));
        }
        return boxlist.toArray(new Box[boxlist.size()]);
    }
}
