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
package org.gitools.plugins.mutex.analysis;

import com.google.common.base.Strings;
import org.gitools.analysis.Analysis;
import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringException;
import org.gitools.analysis.clustering.annotations.AnnPatClusteringData;
import org.gitools.analysis.clustering.annotations.AnnPatClusteringMethod;
import org.gitools.api.analysis.Clusters;
import org.gitools.api.matrix.IKey;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.Key;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.ResourceReference;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.heatmap.decorator.impl.NonEventToNullFunction;
import org.gitools.heatmap.decorator.impl.PValueDecorator;
import org.gitools.heatmap.decorator.impl.ZScoreDecorator;
import org.gitools.matrix.modulemap.HashModuleMap;
import org.gitools.resource.Property;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.utils.progressmonitor.DefaultProgressMonitor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@XmlRootElement
public class MutualExclusiveAnalysis extends Analysis {

    @XmlTransient
    public static IKey<MutualExclusiveAnalysis> CACHE_KEY_MUTEX_ANALYSIS = new Key<>();

    private String layer;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<Heatmap> results;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<Heatmap> data;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IModuleMap> rowsModuleMap;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IModuleMap> columnsModuleMap;

    private MatrixDimensionKey testDimensionKey;

    private MatrixDimensionKey weightDimensionKey;

    private int iterations = 10000;

    @XmlTransient
    private NonEventToNullFunction eventFunction;

    private boolean discardEmpty = false;

    public MutualExclusiveAnalysis(Heatmap heatmap) {
        setData(heatmap);
    }

    public MutualExclusiveAnalysis() {
        //JAXB requirement
    }

    public static HashModuleMap createModules(String columnPattern, boolean allItemGroup, HeatmapDimension columns) {

        HashModuleMap moduleMap = new HashModuleMap();

        Map<String, Set<String>> colGroups = new HashMap<>();
        if (!Strings.isNullOrEmpty(columnPattern)) {
            ClusteringData data = new AnnPatClusteringData(columns, columnPattern);
            Clusters results = null;
            try {
                results = new AnnPatClusteringMethod().cluster(data, new DefaultProgressMonitor());
            } catch (ClusteringException e) {
                e.printStackTrace();
            }
            colGroups = results.getClustersMap();
        }

        for (String key : colGroups.keySet()) {
            moduleMap.addMapping(key, colGroups.get(key));
        }

        if (allItemGroup) {
            moduleMap.addMapping("All " + columns.getId().getLabel() + "s", columns.toList());
        }

        return moduleMap;
    }


    public IMatrixDimension getTestDimension() {
        return getData().get().getDimension(testDimensionKey);
    }

    public void setTestDimension(IMatrixDimension testDimension) {
        this.testDimensionKey = testDimension.getId();
        this.weightDimensionKey = testDimensionKey.equals(MatrixDimensionKey.ROWS) ?
                MatrixDimensionKey.COLUMNS : MatrixDimensionKey.ROWS;
    }

    public ResourceReference<IModuleMap> getRowsModuleMap() {
        return rowsModuleMap;
    }

    public void setRowsModuleMap(ResourceReference<IModuleMap> rowsModuleMap) {
        this.rowsModuleMap = rowsModuleMap;
    }

    public void setRowModuleMap(IModuleMap rowsModuleMap) {
        setRowsModuleMap(new ResourceReference<>("rowsModuleMap", rowsModuleMap));
    }

    public ResourceReference<IModuleMap> getColumnsModuleMap() {
        return columnsModuleMap;
    }

    public void setColumnsModuleMap(ResourceReference<IModuleMap> columnsModuleMap) {
        this.columnsModuleMap = columnsModuleMap;
    }

    public void setColumnsModuleMap(IModuleMap columnsModuleMap) {
        setColumnsModuleMap(new ResourceReference<>("setColumnsModuleMap", columnsModuleMap));
    }

    public ResourceReference<Heatmap> getData() {
        return data;
    }

    private void setData(ResourceReference<Heatmap> data) {
        this.data = data;
    }

    private void setData(Heatmap data) {
        setData(new ResourceReference<>("data", data));
    }

    public String getLayer() {

        if (layer == null) {
            return getData().get().getLayers().iterator().next().getId();
        }

        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public ResourceReference<Heatmap> getResults() {
        return results;
    }

    public void setResults(ResourceReference<Heatmap> results) {
        this.results = results;

        Heatmap heatmap = results.get();
        heatmap.setTitle(getTitle() + " (Results)");

        heatmap.setAuthorName(Settings.get().getAuthorName());
        heatmap.setAuthorEmail(Settings.get().getAuthorEmail());

        HeatmapLayers layers = heatmap.getLayers();
        layers.get(MutualExclusiveResult.COOC_PVALUE).setDecorator(new PValueDecorator());
        layers.get(MutualExclusiveResult.MUTEX_PVALUE).setDecorator(new PValueDecorator());
        layers.get(MutualExclusiveResult.Z_SCORE).setDecorator(new ZScoreDecorator());
        layers.setTopLayer(layers.get(MutualExclusiveResult.Z_SCORE));
    }

    public IMatrixDimension getWeightDimension() {
        return getData().get().getDimension(weightDimensionKey);
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public NonEventToNullFunction getEventFunction() {
        this.addProperty(new Property("Events", eventFunction.getDescription()));
        return eventFunction;
    }

    public void setEventFunction(NonEventToNullFunction eventFunction) {
        this.eventFunction = eventFunction;
    }

    public boolean isDiscardEmpty() {
        return discardEmpty;
    }

    public void setDiscardEmpty(boolean discardEmpty) {
        this.discardEmpty = discardEmpty;
    }
}
