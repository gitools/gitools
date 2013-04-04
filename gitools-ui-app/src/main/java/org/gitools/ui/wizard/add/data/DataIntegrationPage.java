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
package org.gitools.ui.wizard.add.data;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.data.integration.DataIntegrationCriteria;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.operators.Operator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataIntegrationPage extends AbstractWizardPage
{

    private static class DataIntegrationCriteriaListCellRender extends JTextArea implements TableCellRenderer
    {
        public DataIntegrationCriteriaListCellRender()
        {
            setLineWrap(true);
            setWrapStyleWord(true);
            //setOpaque(true);
        }

        @NotNull
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            List<DataIntegrationCriteria> criteriaList = (List<DataIntegrationCriteria>) value;
            String intend = "    ";
            String rendering = "";
            for (DataIntegrationCriteria c : criteriaList)
            {
                Operator op = c.getOperator();
                intend = (!op.equals(Operator.OR)) ?
                        "    " : "";
                if (!op.equals(Operator.EMPTY))
                {
                    rendering = rendering + intend + op.getLongName().toUpperCase() + "\n";
                }

                rendering = rendering + "    " + c.getAttributeName() + " "
                        + c.getComparator().getLongName() + " "
                        + Double.toString(c.getCutoffValue()) + "\n";


            }
            setText(rendering);
            return this;
        }
    }

    private String[] attrNames;
    DefaultTableModel model;

    public DataIntegrationPage(@NotNull Heatmap hm)
    {

        initComponents();
        setComplete(false);

        setTitle("Data Dimension Integration");
        setMessage("Choose which data dimensions and what cut-offs"
                + " to integrate");

        List<IElementAttribute> attributes = hm.getMatrixView().getContents().getCellAdapter().getProperties();

        this.attrNames = new String[attributes.size()];
        for (int i = 0; i < attributes.size(); i++)
        {
            this.attrNames[i] = attributes.get(i).getName();
        }
        //this.model = new DefaultTableModel();
        model = (DefaultTableModel) table.getModel();

        //table.setModel(model);
        table.getColumnModel().getColumn(0).setMaxWidth(65);
        table.getColumnModel().getColumn(1).setMaxWidth(100);
        table.getColumnModel().getColumn(2).setCellRenderer(
                new DataIntegrationCriteriaListCellRender());
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                updateButtons();
            }
        });
    }

    @NotNull
    public double[] getValues()
    {
        double values[] = new double[table.getRowCount()];
        for (int i = 0; i < table.getRowCount(); i++)
        {
            values[i] = Double.parseDouble((String) table.getValueAt(i, 1));
        }
        return values;
    }

    @NotNull
    public List<ArrayList<DataIntegrationCriteria>> getCriteria()
    {
        ArrayList<ArrayList<DataIntegrationCriteria>> criteria =
                new ArrayList<ArrayList<DataIntegrationCriteria>>();
        for (int i = 0; i < table.getRowCount(); i++)
        {
            ArrayList<DataIntegrationCriteria> c =
                    (ArrayList<DataIntegrationCriteria>) table.getValueAt(i, 2);
            criteria.add(c);
        }
        return criteria;
    }


    private void updateButtons()
    {
        int rowNb = table.getRowCount();
        removeBtn.setEnabled(rowNb > 0 && table.getSelectedRow() >= 0);
        upBtn.setEnabled(rowNb > 1 && table.getSelectedRow() >= 0);
        downBtn.setEnabled(rowNb > 1 && table.getSelectedRow() >= 0);
        editBtn.setEnabled(rowNb > 0 && table.getSelectedRow() >= 0);
        setComplete(rowNb > 0);
    }

    private void updateRowHeightsAndPriorities()
    {
        for (int i = 0; i < table.getRowCount(); i++)
        {
            table.setValueAt(i + 1, i, 0);

            List<DataIntegrationCriteria> criteriaList = (List<DataIntegrationCriteria>) table.getValueAt(i, 2);
            int lines = criteriaList.size() * 2 - 1;
            table.setRowHeight(i, table.getRowHeight() * lines);


        }
    }


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        applyGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        tableAddBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        upBtn = new javax.swing.JButton();
        downBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        editBtn = new javax.swing.JButton();

        jLabel1.setText("Add integration rules according to their priorities");

        tableAddBtn.setText("Add");
        tableAddBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                tableAddBtnActionPerformed(evt);
            }
        });

        removeBtn.setText("Remove");
        removeBtn.setEnabled(false);
        removeBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                removeBtnActionPerformed(evt);
            }
        });

        upBtn.setText("Up");
        upBtn.setEnabled(false);
        upBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                upBtnActionPerformed(evt);
            }
        });

        downBtn.setText("Down");
        downBtn.setEnabled(false);
        downBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                downBtnActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Priority", "Value", "Criteria"
                }
        )
        {
            @NotNull
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }

            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
        });
        table.setColumnSelectionAllowed(true);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        editBtn.setText("Edit");
        editBtn.setEnabled(false);
        editBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(editBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(downBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(upBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(tableAddBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(removeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(tableAddBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(removeBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(upBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(downBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(editBtn))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableAddBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_tableAddBtnActionPerformed
        String[] ops = new String[]{Operator.AND.getAbbreviation(), Operator.OR.getAbbreviation()};
        final DataIntegrationCriteriaDialog dlg =
                new DataIntegrationCriteriaDialog(AppFrame.get(),
                        attrNames,
                        CutoffCmp.comparators,
                        ops,
                        null,
                        "1");
        dlg.setVisible(true);

        if (dlg.getReturnStatus() != DataIntegrationCriteriaDialog.RET_OK)
        {
            return;
        }
        List<DataIntegrationCriteria> criteriaList = dlg.getCriteriaList();
        String setToValue = dlg.getSetToValue();
        model.addRow(new Object[]{table.getRowCount(), setToValue, criteriaList});
        updateRowHeightsAndPriorities();
        updateButtons();
    }//GEN-LAST:event_tableAddBtnActionPerformed

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_removeBtnActionPerformed
        model.removeRow(table.getSelectedRow());
        updateRowHeightsAndPriorities();
        updateButtons();
    }//GEN-LAST:event_removeBtnActionPerformed

    private void downBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_downBtnActionPerformed
        int selectedPosition = table.getSelectedRow();
        int wantedPosition = (selectedPosition + 1 == table.getRowCount()) ?
                selectedPosition : selectedPosition + 1;
        model.moveRow(selectedPosition, selectedPosition, wantedPosition);
        updateRowHeightsAndPriorities();
        table.getSelectionModel().setSelectionInterval(wantedPosition, wantedPosition);
    }//GEN-LAST:event_downBtnActionPerformed

    private void upBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_upBtnActionPerformed
        int selectedPosition = table.getSelectedRow();
        int wantedPosition = (selectedPosition == 0) ?
                selectedPosition : selectedPosition - 1;
        model.moveRow(selectedPosition, selectedPosition, wantedPosition);
        updateRowHeightsAndPriorities();
        table.getSelectionModel().setSelectionInterval(wantedPosition, wantedPosition);
    }//GEN-LAST:event_upBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_editBtnActionPerformed
        String[] ops = new String[]{Operator.AND.getAbbreviation(), Operator.OR.getAbbreviation()};
        List<DataIntegrationCriteria> criteria =
                (List<DataIntegrationCriteria>) table.getValueAt(table.getSelectedRow(), 2);
        String setToValue = (String) table.getValueAt(table.getSelectedRow(), 1);
        final DataIntegrationCriteriaDialog dlg =
                new DataIntegrationCriteriaDialog(AppFrame.get(),
                        attrNames,
                        CutoffCmp.comparators,
                        ops,
                        criteria,
                        setToValue);
        dlg.setVisible(true);

        if (dlg.getReturnStatus() != DataIntegrationCriteriaDialog.RET_OK)
        {
            return;
        }
        int selectedRow = table.getSelectedRow();
        List<DataIntegrationCriteria> criteriaList = dlg.getCriteriaList();
        setToValue = dlg.getSetToValue();
        model.removeRow(selectedRow);
        model.insertRow(selectedRow, new Object[]{selectedRow + 1, setToValue, criteriaList});
        updateRowHeightsAndPriorities();
        updateButtons();
    }//GEN-LAST:event_editBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyGroup;
    private javax.swing.JButton downBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeBtn;
    private javax.swing.JTable table;
    private javax.swing.JButton tableAddBtn;
    private javax.swing.JButton upBtn;
    // End of variables declaration//GEN-END:variables


}
