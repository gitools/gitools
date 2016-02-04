/*
 * #%L
 * gitools-core
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
package org.gitools.heatmap;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.jgoodies.binding.beans.Model;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.view.IMatrixViewLayers;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.matrix.element.LayerDef;
import org.gitools.utils.events.EventUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapLayers extends Model implements IMatrixViewLayers<HeatmapLayer> {
    public static final String PROPERTY_TOP_LAYER_INDEX = "topLayerIndex";
    public static final String PROPERTY_TOP_LAYER = "topLayer";
    public static final String PROPERTY_LAYERS = "layers";
    public static final String PROPERTY_SELECTED_GROUP = "selectedGroup";

    @XmlElement(name = "top-layer")
    private int topLayer;

    @XmlElement(name = "layer")
    private List<HeatmapLayer> layers;
    private transient Map<String, Integer> layersIdToIndex;
    private transient List<String> layerNames;

    private String selectedGroup;

    private transient HashSet<String> groups;

    public HeatmapLayers() {
        this.topLayer = 0;
    }

    public HeatmapLayers(IMatrix matrix) {
        this();
        createLayers(matrix);
        init(matrix);
    }

    private void createLayers(IMatrix matrix) {
        IMatrixLayers<? extends IMatrixLayer> matrixLayers = matrix.getLayers();
        this.layers = new ArrayList<>(matrixLayers.size());
        this.layerNames = null;
        this.layersIdToIndex = null;

        for (IMatrixLayer layer : matrixLayers) {
            initLayer(layer);
        }
    }

    public void initLayer(IMatrixLayer layer) {
        this.layers.add(new HeatmapLayer(layer));
    }

    public void init(IMatrix matrix) {
        IMatrixLayers<?> matrixLayers = matrix.getLayers();

        // Check if there is a new data layer
        for (IMatrixLayer dataLayer : matrixLayers) {

            HeatmapLayer newLayer = null;
            for (HeatmapLayer heatmapLayer : this.layers) {
                if (heatmapLayer.getId().equals(dataLayer.getId())) {
                    newLayer = heatmapLayer;
                    break;
                }
            }

            // This is a new layer
            if (newLayer == null) {
                this.layers.add(new HeatmapLayer(dataLayer));
            }

        }

        initTransient();

    }

    public void updateLayers() {
        initTransient();
        firePropertyChange(PROPERTY_LAYERS, null, null);
    }

    private void initTransient() {

        this.layersIdToIndex = new HashMap<>(layers.size());
        this.layerNames = new ArrayList<>(layers.size());

        // Init transient parameters
        for (int i = 0; i < layers.size(); i++) {
            this.layersIdToIndex.put(layers.get(i).getId(), i);
            this.layerNames.add(layers.get(i).getName());
        }
    }

    public HeatmapLayer getTopLayer() {
        return layers.get(topLayer);
    }

    public void setTopLayer(HeatmapLayer topLayer) {
        setTopLayerIndex(layers.indexOf(topLayer));
    }

    @Deprecated
    public int getTopLayerIndex() {
        return topLayer;
    }

    @Deprecated
    public void setTopLayerById(String id) {
        int layer = indexOf(id);

        if (layer != -1) {
            setTopLayerIndex(layer);
        }
    }

    @Deprecated
    public void setTopLayerIndex(int layerIndex) {

        int old = this.topLayer;

        // Move listeners
        HeatmapLayer oldLayer = layers.get(old);
        HeatmapLayer newLayer = layers.get(layerIndex);
        if (oldLayer == newLayer) {
            return;
        }


        this.topLayer = layerIndex;

        firePropertyChange(PROPERTY_TOP_LAYER_INDEX, old, layerIndex);
        firePropertyChange(PROPERTY_TOP_LAYER, layers.get(old), layers.get(layerIndex));

        EventUtils.moveListeners(oldLayer.getDecorator(), newLayer.getDecorator());
        EventUtils.moveListeners(oldLayer, newLayer);
    }

    public void remove(HeatmapLayer layer) {
        if(getTopLayer() == layer) {
            int idx = layers.indexOf(layer) - 1;
            if (idx < 0) {
                setTopLayer(get(idx));
            } else {
                setTopLayer(get(0));
            }
        } else if(layers.indexOf(getTopLayer()) == layers.size()-1 ) {
            setTopLayer(layers.get(layers.size()-2));
        }
        this.layers.remove(layer);
        this.updateLayers();
    }

    @Override
    public String[] getIds() {
        return layersIdToIndex.keySet().toArray(new String[size()]);
    }

    @Override
    public HeatmapLayer get(String layer) {

        if (layer == null) {
            return null;
        }

        return layers.get(indexOf(layer));
    }

    @Override
    public HeatmapLayer get(int layer) {
        return layers.get(layer);
    }

    public int indexOf(String id) {
        Integer layer = layersIdToIndex.get(id);

        if (layer == null) {
            return -1;
        }

        return layer;
    }

    public void setSelectedGroup(String selectedGroup) {

        String old = this.selectedGroup;
        this.selectedGroup = selectedGroup;

        firePropertyChange(PROPERTY_SELECTED_GROUP, old, selectedGroup);
    }

    @Override
    public String getSelectedGroup() {
        if (Strings.isNullOrEmpty(selectedGroup)) {
            return LayerDef.ALL_DATA_GROUP;
        }
        return selectedGroup;
    }

    @Override
    public Set<String> getGroups() {

        if (this.groups == null) {
            HashSet groups = new HashSet<>();
            for (IMatrixLayer layer : layers) {
                groups.addAll(layer.getGroups());
            }
            this.groups = groups;
        }
        return groups;
    }

    public String getId() {
        return MatrixLayers.LAYERS_ID;
    }

    @Override
    public int size() {
        return layers.size();
    }

    public String getLabel(int index) {
        return layers.get(index).getId();
    }

    public List<String> getLayerNames() {
        return layerNames;
    }

    public List<HeatmapLayer> asList() {
        return layers;
    }

    public void populateDetails(List<DetailsDecoration> details, IMatrix matrix, String row, String column) {

        int i = -1;
        String displayedGroup = getSelectedGroup();
        for (HeatmapLayer layer : layers) {
            i++;
            boolean isSelected = (i == topLayer);
            if (!isSelected && displayedGroup != null &&
                    !layer.getGroups().contains(displayedGroup)) {
                continue;
            }
            layer.populateDetails(details, matrix, row, column, i, isSelected);
        }

    }

    @Override
    public Iterator<HeatmapLayer> iterator() {
        return layers.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeatmapLayers strings = (HeatmapLayers) o;

        return Objects.equal(getId(), strings.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
