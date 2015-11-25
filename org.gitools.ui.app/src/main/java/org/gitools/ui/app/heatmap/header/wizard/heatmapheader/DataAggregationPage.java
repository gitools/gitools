/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.heatmap.header.wizard.heatmapheader;

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.api.analysis.IAggregator;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.TransformFunction;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.aggregation.AggregatorFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DataAggregationPage extends AbstractWizardPage {

    private final IAggregator[] aggregatorsArray;
    private final HeatmapDimension headerDimension;
    private final HeatmapDimension aggregationDimension;
    private final Heatmap heatmap;

    public DataAggregationPage(HeatmapDimension headerDimension, HeatmapDimension aggregationDimension, final Heatmap heatmap) {
        this.headerDimension = headerDimension;
        this.aggregationDimension = aggregationDimension;
        this.heatmap = heatmap;

        //public DataAggregationPage(HeatmapDimension headerDimension, HeatmapDimension aggregationDimension, List<String> layerNames, int selectedLayer) {

        List<String> layerNames = heatmap.getLayers().getLayerNames();
        int selectedLayer = heatmap.getLayers().getTopLayerIndex();

        this.aggregatorsArray = AggregatorFactory.getAggregatorsArray();
        String[] aggregatorNames = new String[aggregatorsArray.length];
        for (int i = 0; i < aggregatorsArray.length; i++)
            aggregatorNames[i] = aggregatorsArray[i].toString();

        initComponents();
        updateModel();

        boolean hasAnnotation = headerDimension.getAnnotations() != null;
        separateAggregationCb.setEnabled(hasAnnotation);

        valueCb.setModel(new DefaultComboBoxModel(layerNames.toArray()));
        valueCb.setSelectedIndex(selectedLayer);
        valueCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTransformFunctions(heatmap);
                updateControls();
            }
        });

        aggregatorCb.setModel(new DefaultComboBoxModel(aggregatorNames));
        aggregatorCb.setSelectedItem("Mean");
        aggregatorCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls();
            }
        });

        setTransformFunctions(heatmap);
        transformationCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls();
            }
        });

        useAllRb.setText("Use values from all " + aggregationDimension.getId());
        useSelectedRb.setText("Use values from " + aggregationDimension.getSelected().size() + " selected " + aggregationDimension.getId());
        separateAggregationCb.setText("aggregate by " + aggregationDimension.getId() + " annotations groups");

        setTitle("Choose the data to aggregate");

    }

    @Override
    public Container getParent() {
        return rootPanel;
    }

    private void setTransformFunctions(Heatmap heatmap) {
        List<TransformFunction> transformFunctions = new ArrayList<>();
        transformFunctions.add(new TransformFunction("") {

            @Override
            public String getDescription() {
                return "No transformation of values";
            }

            @Override
            public Double apply(Double value, IMatrixPosition position) {
                return value;
            }
        });
        transformFunctions.addAll(heatmap.getLayers().getTopLayer().getDecorator().getEventFunctionAlternatives());
        transformationCb.setModel(
                new ComboBoxAdapter<>(
                        new SelectionInList<>(
                                transformFunctions
                        )
                )
        );
        transformationCb.setSelectedIndex(0);
        functionDescription.setText(((TransformFunction) transformationCb.getSelectedItem()).getDescription());


    }

    public IAggregator getAggregator() {
        return aggregatorsArray[aggregatorCb.getSelectedIndex()];
    }

    public TransformFunction getTransformFunction() { return (TransformFunction) transformationCb.getSelectedItem(); }

    public int getAggregationLayer() {
        return valueCb.getSelectedIndex();
    }

    public String getSelectedDataValueName() {
        return valueCb.getSelectedItem().toString();
    }

    public boolean useAllColumnsOrRows() {
        return useAllRb.isSelected();
    }

    private void updateCompleted() {
        boolean completed = aggregatorCb.getSelectedIndex() > -1 && valueCb.getSelectedIndex() > -1;
        setComplete(completed);
    }

    public boolean aggregateAnnotationsSeparately() {
        return separateAggregationCb.isEnabled() && separateAggregationCb.isSelected();
    }

    @Override
    public void updateControls() {
        functionDescription.setText(((TransformFunction) transformationCb.getSelectedItem()).getDescription());
        useAllRb.setEnabled(!separateAggregationCb.isSelected());
        useSelectedRb.setEnabled(!separateAggregationCb.isSelected() && aggregationDimension.getSelected().size() > 0);
        updateCompleted();
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        allOrSelected = new ButtonGroup();

        aggregatorCb.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        allOrSelected.add(useAllRb);
        useAllRb.setSelected(true);
        useAllRb.setText("use all");
        useAllRb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                useAllRbActionPerformed(evt);
            }
        });

        allOrSelected.add(useSelectedRb);
        useSelectedRb.setText("use selected");
        useSelectedRb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                useSelectedRbActionPerformed(evt);
            }
        });

        separateAggregationCb.setText("aggregate sperately for annotation groups");
        separateAggregationCb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                separateAggregationCbActionPerformed(evt);
            }
        });

    }

    private void useAllRbActionPerformed(ActionEvent evt) {

    }

    private void useSelectedRbActionPerformed(ActionEvent evt) {
    }

    private void separateAggregationCbActionPerformed(java.awt.event.ActionEvent evt) {
        updateControls();
    }


    private JComboBox aggregatorCb;
    private ButtonGroup allOrSelected;
    private JCheckBox separateAggregationCb;
    private JRadioButton useAllRb;
    private JRadioButton useSelectedRb;
    private JComboBox valueCb;
    private JPanel rootPanel;
    private JComboBox transformationCb;
    private JLabel functionDescription;

    @Override
    public JComponent createControls() {
        return rootPanel;
    }

}
