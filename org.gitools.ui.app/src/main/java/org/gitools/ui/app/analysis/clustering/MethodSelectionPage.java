/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.analysis.clustering;

import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import java.util.List;

import static com.jgoodies.binding.adapter.Bindings.bind;

public class MethodSelectionPage extends AbstractWizardPage {

    private JPanel root;
    private JComboBox method;
    private JComboBox dimension;
    private JComboBox layer;

    private ClusteringMethod selectedMethod;
    private MatrixDimensionKey clusteringDimension;
    private String clusteringLayer;

    public MethodSelectionPage(List<? extends ClusteringMethod> methods, IMatrixView data) {
        super("method selection");
        setTitle("Method selection");

        setSelectedMethod(methods.get(0));
        setClusteringDimension(MatrixDimensionKey.COLUMNS);
        setClusteringLayer(data.getLayers().getTopLayer().getId());

        // Bind components
        bind(this.method, new SelectionInList<>(methods, new PropertyAdapter<>(this, "selectedMethod")));
        bind(this.dimension, new SelectionInList<>(data.getDimensionKeys(), new PropertyAdapter<>(this, "clusteringDimension")));
        bind(this.layer, new SelectionInList<>(data.getLayers().getIds(), new PropertyAdapter<>(this, "clusteringLayer")));

    }

    public ClusteringMethod getSelectedMethod() {
        return selectedMethod;
    }

    public void setSelectedMethod(ClusteringMethod selectedMethod) {
        this.selectedMethod = selectedMethod;
        fireUpdated();
    }

    public MatrixDimensionKey getClusteringDimension() {
        return clusteringDimension;
    }

    public void setClusteringDimension(MatrixDimensionKey clusteringDimension) {
        this.clusteringDimension = clusteringDimension;
    }

    public String getClusteringLayer() {
        return clusteringLayer;
    }

    public void setClusteringLayer(String clusteringLayer) {
        this.clusteringLayer = clusteringLayer;
    }

    @Override
    public boolean isComplete() {
        return selectedMethod != null;
    }

    @Override
    public JComponent createControls() {
        return root;
    }

    public MatrixDimensionKey getAggregationDimension() {
        return (getClusteringDimension() == MatrixDimensionKey.COLUMNS ? MatrixDimensionKey.ROWS : MatrixDimensionKey.COLUMNS);
    }
}
