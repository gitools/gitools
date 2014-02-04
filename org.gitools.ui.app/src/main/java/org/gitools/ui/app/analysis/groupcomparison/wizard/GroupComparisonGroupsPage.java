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
package org.gitools.ui.app.analysis.groupcomparison.wizard;

import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroup;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupEnum;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupValue;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class GroupComparisonGroupsPage extends AbstractWizardPage {
    private JTable groupsTable;
    private DimensionGroupTableModel tableModel = new DimensionGroupTableModel();
    private JPanel panel1;
    private JButton addButton;
    private JButton removeButton;
    private List<DimensionGroup> removedItems = new ArrayList<DimensionGroup>();


    private DimensionGroupEnum columnGroupType;

    public GroupComparisonGroupsPage(DimensionGroup... groups) {
        super();

        groupsTable.setModel(tableModel);

        setTitle("Groups");
        setMessage("Add / Remove groups and merge by selecting the same group number.");

        TableColumnModel columnModel = groupsTable.getColumnModel();

        columnModel.getColumn(2).setCellEditor(new SpinnerCellEditor(new SpinnerNumberModel()));
        columnModel.getColumn(2).getCellEditor().addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                tableModel.fireTableDataChanged();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                tableModel.fireTableDataChanged();
            }
        });
        groupsTable.setRowHeight(30);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelected();
            }
        });


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (columnGroupType.equals(DimensionGroupEnum.Annotation)) {
                    //TODO: create Dialog with removedItems
                } else if (columnGroupType.equals(DimensionGroupEnum.Free)) {
                    //TODO: create Dialog with paste labels
                } else if (columnGroupType.equals(DimensionGroupEnum.Value)) {
                    //TODO: create Dialog with binary filters
                }
            }
        });
    }

    private void removeSelected() {
        if (columnGroupType.equals(DimensionGroupEnum.Annotation)) {
            for (int cg : groupsTable.getSelectedRows()) {
                removedItems.add(tableModel.getGroupAt(cg));
            }

        }
        tableModel.removeGroup(groupsTable.getSelectedRows());
    }


    public void setColumnGroupType(DimensionGroupEnum columnGroupType) {
        this.columnGroupType = columnGroupType;
    }

    public void addGroups(DimensionGroupValue... groups) {
        tableModel.setGroups(groups);
    }

    @Override
    public JComponent createControls() {
        return panel1;
    }
}
