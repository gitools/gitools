package org.gitools.ui.analysis.groupcomparison.wizard;

import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroup;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupEnum;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupValue;
import org.gitools.ui.heatmap.panel.settings.headers.SpinnerCellEditor;
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
