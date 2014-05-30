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
import com.jgoodies.binding.beans.Model;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.view.IMatrixViewLayers;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.heatmap.decorator.impl.LinearDecorator;
import org.gitools.matrix.model.MatrixLayers;
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

    @XmlElement(name = "top-layer")
    private int topLayer;

    @XmlElement(name = "layer")
    private List<HeatmapLayer> layers;
    private transient Map<String, Integer> layersIdToIndex;
    private transient List<String> layerNames;

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
        Decorator defaultDecorator = new LinearDecorator();
        this.layers.add(new HeatmapLayer(layer.getId(), layer.getValueClass(), defaultDecorator));
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
                Decorator defaultDecorator = new LinearDecorator();
                newLayer = new HeatmapLayer(dataLayer.getId(), dataLayer.getValueClass(), defaultDecorator);
                this.layers.add(newLayer);
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
        this.topLayer = layerIndex;

        firePropertyChange(PROPERTY_TOP_LAYER_INDEX, old, layerIndex);
        firePropertyChange(PROPERTY_TOP_LAYER, layers.get(old), layers.get(layerIndex));

        // Move listeners
        HeatmapLayer oldLayer = layers.get(old);
        HeatmapLayer newLayer = layers.get(layerIndex);

        EventUtils.moveListeners(oldLayer.getDecorator(), newLayer.getDecorator());
        EventUtils.moveListeners(oldLayer, newLayer);
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

        int i = 0;
        for (HeatmapLayer layer : layers) {
            layer.populateDetails(details, matrix, row, column, i, (i == topLayer));
            i++;
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
