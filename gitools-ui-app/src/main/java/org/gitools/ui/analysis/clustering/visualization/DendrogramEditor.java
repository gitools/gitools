package org.gitools.ui.analysis.clustering.visualization;

import org.gitools.analysis.clustering.hierarchical.Cluster;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.editor.AbstractEditor;

import java.awt.*;

public class DendrogramEditor extends AbstractEditor {

    private Cluster model;

    public DendrogramEditor(Cluster model) {
        super();

        this.model = model;
        setName("Hierarchical clustering '" + model.getName() + "'");
        setIcon(IconNames.CREATE_IMAGE_SMALL_ICON);
        setLayout(new BorderLayout());
        setSaveAllowed(false);
        setSaveAsAllowed(false);
        setBackground(Color.WHITE);

        DendrogramPanel panel = new DendrogramPanel();
        panel.setModel(model);
        panel.setBackground(Color.WHITE);

        add(panel);
    }

    @Override
    public Object getModel() {
        return model;
    }
}
