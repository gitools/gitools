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
package org.gitools.core.heatmap;

import com.jgoodies.binding.beans.Model;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.IMatrixLayers;
import org.gitools.core.matrix.model.IMatrixViewLayers;
import org.gitools.core.model.decorator.Decorator;
import org.gitools.core.model.decorator.DecoratorFactory;
import org.gitools.core.utils.EventUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapLayers extends Model implements IMatrixViewLayers<HeatmapLayer> {
    public static final String PROPERTY_TOP_LAYER_INDEX = "topLayerIndex";
    public static final String PROPERTY_TOP_LAYER = "topLayer";

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
        this.layers = new ArrayList<HeatmapLayer>(matrixLayers.size());
        this.layerNames = null;
        this.layersIdToIndex = null;

        for (int i = 0; i < matrixLayers.size(); i++) {
            IMatrixLayer layer = matrixLayers.get(i);
            Decorator defaultDecorator = DecoratorFactory.defaultDecorator(matrix, i);
            this.layers.add(new HeatmapLayer(layer.getId(), layer.getValueClass(), defaultDecorator));
        }
    }

    public void init(IMatrix matrix) {
        IMatrixLayers matrixLayers = matrix.getLayers();
        this.layersIdToIndex = new HashMap<String, Integer>(matrixLayers.size());
        this.layerNames = new ArrayList<String>(matrixLayers.size());
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

    @Override
    public int getTopLayerIndex() {
        return topLayer;
    }

    @Override
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
    public HeatmapLayer get(int layerIndex) {
        return layers.get(layerIndex);
    }

    @Override
    public int findId(String id) {
        return layersIdToIndex.get(id);
    }

    @Override
    public int size() {
        return layers.size();
    }

    @Override
    public Iterator<HeatmapLayer> iterator() {
        return layers.iterator();
    }

    public List<String> getLayerNames() {
        return layerNames;
    }
}
