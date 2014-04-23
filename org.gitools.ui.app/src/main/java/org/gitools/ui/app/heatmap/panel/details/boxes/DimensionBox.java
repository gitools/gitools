package org.gitools.ui.app.heatmap.panel.details.boxes;

import org.apache.commons.lang.StringUtils;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.actions.edit.EditHeaderAction;
import org.gitools.ui.platform.actions.ActionSet;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class DimensionBox extends DetailsBox {
    private final HeatmapDimension dimension;

    /**
     * @param title     Optional title of the details table
     * @param actions
     * @param dimension
     */
    public DimensionBox(String title, ActionSet actions, Heatmap heatmap, HeatmapDimension dimension) {
        super(title, actions, heatmap);
        this.dimension = dimension;
    }

    @Override
    public void registerListeners() {
        dimension.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
    }

    @Override
    public void update() {
        String lead = dimension.getFocus();
        String label = StringUtils.capitalize(dimension.getId().getLabel());

        if (lead != null) {
            this.setTitle(label + ": " + lead + " [" + (dimension.indexOf(lead) + 1) + "]");
        } else {
            this.setTitle(label);
        }
        List<DetailsDecoration> details = new ArrayList<>();
        dimension.populateDetails(details);
        this.draw(details);
    }

    @Override
    protected void onMouseDblClick(DetailsDecoration detail) {
        Object reference = detail.getReference();

        if (reference instanceof HeatmapHeader) {
            new EditHeaderAction((HeatmapHeader) reference).actionPerformed(null);
        }
    }
}
