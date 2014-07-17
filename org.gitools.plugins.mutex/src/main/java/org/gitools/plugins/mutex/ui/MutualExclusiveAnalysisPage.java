/*
 * #%L
 * org.gitools.mutex
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
package org.gitools.plugins.mutex.ui;

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.decorator.impl.NonEventToNullFunction;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;


public class MutualExclusiveAnalysisPage extends AbstractWizardPage {
    private JComboBox columnAnnotationCombo;
    private JPanel rootPanel;
    private JComboBox rowAnnotationCombo;
    private JCheckBox allColumnsCheckBox;
    private JCheckBox columnAnnotationGroupingBox;
    private JComboBox eventsFunctionComboBox;
    private JLabel eventsFunctionDescription;
    private JSpinner permutationSpinner;
    private JCheckBox discardEmpty;
    private JLabel testDescription;
    private Heatmap heatmap;


    public MutualExclusiveAnalysisPage(Heatmap heatmap) {
        super("Mutual exclusion & Co-occurrence analysis");
        this.heatmap = heatmap;

        setTitle("Mutual Exclusion & Co-occurrence analysis");
        setMessage("Select which groups of columns and rows you want to test");

        Collection<String> colAnnlabels = heatmap.getColumns().getAnnotations().getLabels();
        Collection<String> rowAnnlabels = heatmap.getRows().getAnnotations().getLabels();


        columnAnnotationCombo.setModel(new DefaultComboBoxModel(
                colAnnlabels.toArray(new String[colAnnlabels.size()])
        ));

        rowAnnotationCombo.setModel(new DefaultComboBoxModel(
                rowAnnlabels.toArray(new String[rowAnnlabels.size()])
        ));

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls();
            }
        };
        eventsFunctionComboBox.addActionListener(actionListener);
        eventsFunctionComboBox.setModel(
                new ComboBoxAdapter<>(
                        new SelectionInList<NonEventToNullFunction>(
                                heatmap.getLayers().getTopLayer().getDecorator().getEventFunctionAlternatives(),
                                new PropertyAdapter<>(heatmap.getLayers().getTopLayer(), "eventFunction")
                        )
                )
        );
        allColumnsCheckBox.addActionListener(actionListener);
        columnAnnotationGroupingBox.addActionListener(actionListener);


        permutationSpinner.setModel(new SpinnerNumberModel(10000, 100, 10000000, 1000));

        testDescription.setText("All " + heatmap.getContents().getRows().size() + " " + heatmap.getRows().getId().getLabel() + "s will be taken into account " +
                "for a weighted permutation test to assess probability of mutual exclusion and co-occurence");

        updateControls();


    }




    public String getRowsGroupsPattern() {
        return "${" + rowAnnotationCombo.getSelectedItem() + "}";

    }

    public boolean isAllColumnsGroup() {
        return allColumnsCheckBox.isSelected();
    }

    public String getColumnGroupsPattern () {
        if (columnAnnotationGroupingBox.isSelected()) {
            return "${" + columnAnnotationCombo.getSelectedItem() + "}";
        } else {
            return null;
        }
    }

    @Override
    public void updateControls() {


        boolean hasColAnnotation = columnAnnotationCombo.getModel().getSize() > 0;
        boolean hasRowAnnotation = rowAnnotationCombo.getModel().getSize() > 0;

        if (!hasColAnnotation) {
            columnAnnotationGroupingBox.setEnabled(false);
            columnAnnotationGroupingBox.setSelected(false);
            allColumnsCheckBox.setSelected(true);
        }

        if (!hasRowAnnotation) {
            setComplete(false);
            setMessage(MessageStatus.ERROR, "No row annotation available for grouping. For all rows, use Edit > Row > Sort by Mut.Ex.");
        }

        columnAnnotationCombo.setEnabled(columnAnnotationGroupingBox.isSelected());
        if (eventsFunctionDescription!=null && eventsFunctionComboBox.getSelectedItem() != null) {
            eventsFunctionDescription.setText(((NonEventToNullFunction) eventsFunctionComboBox.getSelectedItem()).getDescription());
        }
        setComplete(columnAnnotationGroupingBox.isSelected() || isAllColumnsGroup());
    }

    @Override
    public JComponent createControls() {
        return rootPanel;
    }

    public int getPermutations() {
        return Integer.parseInt(permutationSpinner.getValue().toString());
    }

    public boolean getDiscardEmpty() {
        return discardEmpty.isSelected();
    }
}
