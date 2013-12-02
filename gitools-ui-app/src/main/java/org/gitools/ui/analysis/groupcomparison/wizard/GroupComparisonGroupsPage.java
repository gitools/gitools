package org.gitools.ui.analysis.groupcomparison.wizard;

import org.gitools.analysis.groupcomparison.ColumnGroup;
import org.gitools.ui.heatmap.panel.settings.headers.SpinnerCellEditor;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GroupComparisonGroupsPage extends AbstractWizardPage {
    private JTable groupsTable;
    private ColumnGroupsTableModel tableModel = new ColumnGroupsTableModel();
    private JPanel panel1;
    private JButton addButton;
    private JButton removeButton;

    public GroupComparisonGroupsPage(ColumnGroup... groups)  {
        super();

        groupsTable.setModel(tableModel);

        setTitle("Groups");
        //setId("groupspageid");

        TableColumnModel columnModel = groupsTable.getColumnModel();
        //columnModel.getColumn(0).setCellRenderer(new DefaultTableCellRenderer());
        //columnModel.getColumn(1).setCellRenderer(new DefaultTableCellRenderer());
        //columnModel.getColumn(2).setCellRenderer(new DefaultTableCellRenderer());


        columnModel.getColumn(2).setCellEditor(new SpinnerCellEditor(new SpinnerNumberModel()));
        columnModel.getColumn(2).getCellEditor().addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                tableModel.fireTableDataChanged();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {

            }
        });
        groupsTable.setRowHeight(30);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    @Override
    public JComponent createControls() {
        return panel1;
    }



    public void addGroups (ColumnGroup... groups) {
        tableModel.setGroups(groups);
    }
}
