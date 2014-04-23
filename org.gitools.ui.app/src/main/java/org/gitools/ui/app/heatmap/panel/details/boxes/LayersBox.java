package org.gitools.ui.app.heatmap.panel.details.boxes;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.Decoration;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.ui.app.actions.edit.EditLayerAction;
import org.gitools.ui.platform.actions.ActionSet;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


public class LayersBox extends DetailsBox {
    /**
     * @param title   Optional title of the details table
     * @param actions
     */
    public LayersBox(String title, ActionSet actions, Heatmap heatmap) {
        super(title, actions, heatmap);
    }

    @Override
    public void registerListeners() {
        Heatmap heatmap = getHeatmap();
        PropertyChangeListener updateLayers = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        };

        heatmap.getRows().addPropertyChangeListener(updateLayers);
        heatmap.getColumns().addPropertyChangeListener(updateLayers);

        heatmap.getLayers().addPropertyChangeListener(updateLayers);
        heatmap.getLayers().getTopLayer().addPropertyChangeListener(updateLayers);


    }

    @Override
    public void update() {
        Heatmap heatmap = getHeatmap();
        String col = heatmap.getColumns().getFocus();
        String row = heatmap.getRows().getFocus();

        if (col != null && row != null) {

            Decorator decorator = heatmap.getLayers().getTopLayer().getDecorator();
            Decoration decoration = new Decoration();
            boolean showValue = decorator.isShowValue();
            decorator.setShowValue(true);
            decoration.reset();
            HeatmapLayer layer = heatmap.getLayers().getTopLayer();
            decorator.decorate(decoration, layer.getLongFormatter(), heatmap, layer, row, col);
            decorator.setShowValue(showValue);

            this.setTitle("Values: " + decoration.getFormatedValue());
        } else {
            this.setTitle("Values");
        }

        List<DetailsDecoration> layersDetails = new ArrayList<>();
        heatmap.getLayers().populateDetails(layersDetails, heatmap, heatmap.getRows().getFocus(), heatmap.getColumns().getFocus());
        this.draw(layersDetails);
    }

    @Override
    protected void onMouseClick(DetailsDecoration detail) {
        getHeatmap().getLayers().setTopLayerIndex(detail.getIndex());
    }

    @Override
    protected void onMouseDblClick(DetailsDecoration detail) {
        Object reference = detail.getReference();

        if (reference instanceof HeatmapLayer) {
            new EditLayerAction((HeatmapLayer) reference).actionPerformed(null);
        }
    }
}
