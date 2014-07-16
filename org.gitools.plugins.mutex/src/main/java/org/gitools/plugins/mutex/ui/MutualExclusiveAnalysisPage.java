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
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;


public class MutualExclusiveAnalysisPage extends AbstractWizardPage {
    private JComboBox columnGroupingPattern;
    private JPanel rootPanel;
    private JComboBox rowGroupingPattern;
    private JCheckBox allColumnsCheckBox;
    private JCheckBox columnAnnotationGroupingBox;
    private JComboBox eventsFunctionComboBox;
    private JLabel eventsFunctionDescription;
    private Heatmap heatmap;


    public MutualExclusiveAnalysisPage(Heatmap heatmap) {
        super("Mutual exclusion & Co-occurrence analysis");
        this.heatmap = heatmap;


        Collection<String> colAnnlabels = heatmap.getColumns().getAnnotations().getLabels();
        Collection<String> rowAnnlabels = heatmap.getRows().getAnnotations().getLabels();


        columnGroupingPattern.setModel(new DefaultComboBoxModel(
                colAnnlabels.toArray(new String[colAnnlabels.size()])
        ));

        rowGroupingPattern.setModel(new DefaultComboBoxModel(
                rowAnnlabels.toArray(new String[rowAnnlabels.size()])
        ));

        eventsFunctionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls();
            }
        });
        eventsFunctionComboBox.setModel(
                new ComboBoxAdapter<>(
                        new SelectionInList<NonEventToNullFunction>(
                                heatmap.getLayers().getTopLayer().getDecorator().getEventFunctionAlternatives(),
                                new PropertyAdapter<>(heatmap.getLayers().getTopLayer(), "eventFunction")
                        )
                )
        );


        columnAnnotationGroupingBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateControls();
            }
        });
        setComplete(true);


    }




    public String getRowsGroupsPattern() {
        return "${" + rowGroupingPattern.getSelectedItem() + "}";

    }

    public boolean isAllColumnsGroup() {
        return allColumnsCheckBox.isSelected();
    }

    public String getColumnGroupsPattern () {
        if (columnAnnotationGroupingBox.isSelected()) {
            return "${" + columnGroupingPattern.getSelectedItem() + "}";
        } else {
            return null;
        }
    }

    @Override
    public void updateControls() {
        columnGroupingPattern.setEnabled(columnAnnotationGroupingBox.isSelected());
        if (eventsFunctionDescription!=null && eventsFunctionComboBox.getSelectedItem() != null) {
            eventsFunctionDescription.setText(((NonEventToNullFunction) eventsFunctionComboBox.getSelectedItem()).getDescription());
        }
    }

    @Override
    public JComponent createControls() {
        return rootPanel;
    }

}
