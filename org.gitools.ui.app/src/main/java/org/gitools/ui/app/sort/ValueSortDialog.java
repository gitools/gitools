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
package org.gitools.ui.app.sort;

import org.gitools.api.analysis.IAggregator;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.SortDirection;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class ValueSortDialog extends javax.swing.JDialog {
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    private static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    private static final int RET_OK = 1;

    private static class ComboBoxCellRenderer extends JComboBox implements TableCellRenderer {

        public ComboBoxCellRenderer(Object[] values) {
            super(values);
        }


        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            // Select the current value
            setSelectedItem(value);
            return this;
        }
    }

    private static class ComboBoxCellEditor extends DefaultCellEditor {
        public ComboBoxCellEditor(Object[] values) {
            super(new JComboBox(values));
        }
    }

    private final IMatrixLayers<IMatrixLayer> layers;
    private final IAggregator[] aggregators;
    private final SortDirection[] directions;

    private final ValueSortCriteriaTableModel criteriaModel;

    /**
     * Creates new form FilterDialog
     */
    public ValueSortDialog(Frame parent, IMatrixLayers<IMatrixLayer> layers, MatrixDimensionKey dimension, IAggregator[] aggregators, SortDirection[] directions, IMatrixLayer... initialCriteriaList) {

        super(parent, true);

        this.layers = layers;
        this.aggregators = aggregators;
        this.directions = directions;
        this.criteriaModel = new ValueSortCriteriaTableModel(layers, initialCriteriaList);

        initComponents();

        if (dimension.equals(MatrixDimensionKey.ROWS)) {
            applyToRowsRb.setSelected(true);
        } else if (dimension.equals(MatrixDimensionKey.COLUMNS)) {
            applyToColumnsRb.setSelected(true);
        }

        table.setModel(criteriaModel);
        criteriaModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                tableRemoveBtn.setEnabled(criteriaModel.getList().size() > 0);
            }
        });

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellEditor(new ComboBoxCellEditor(layers.getIds()));
        columnModel.getColumn(1).setCellEditor(new ComboBoxCellEditor(aggregators));
        columnModel.getColumn(2).setCellEditor(new ComboBoxCellEditor(directions));
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public boolean isCancelled() {
        return returnStatus != RET_OK;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        applyToGroup = new javax.swing.ButtonGroup();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableAddBtn = new javax.swing.JButton();
        tableRemoveBtn = new javax.swing.JButton();
        loadBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        applyToRowsRb = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        applyToRowsAndColumnsRb = new javax.swing.JRadioButton();
        applyToColumnsRb = new javax.swing.JRadioButton();

        setTitle("Sort by value");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{

        }, new String[]{"Value Layer", "Condition", "Value"}) {

            final Class[] types = new Class[]{java.lang.String.class, java.lang.Object.class, java.lang.Double.class};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        table.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        tableAddBtn.setText("Add");
        tableAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableAddBtnActionPerformed(evt);
            }
        });

        tableRemoveBtn.setText("Remove");
        tableRemoveBtn.setEnabled(false);
        tableRemoveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableRemoveBtnActionPerformed(evt);
            }
        });

        loadBtn.setText("Load...");
        loadBtn.setEnabled(false);

        saveBtn.setText("Save...");
        saveBtn.setEnabled(false);

        applyToGroup.add(applyToRowsRb);
        applyToRowsRb.setSelected(true);
        applyToRowsRb.setText("rows");

        jLabel2.setText("Apply to:");

        applyToGroup.add(applyToRowsAndColumnsRb);
        applyToRowsAndColumnsRb.setText("rows and columns");

        applyToGroup.add(applyToColumnsRb);
        applyToColumnsRb.setText("columns");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(loadBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(tableAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(tableRemoveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addComponent(applyToRowsRb).addComponent(applyToColumnsRb).addComponent(applyToRowsAndColumnsRb).addComponent(jLabel2)).addContainerGap()));

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{cancelButton, okButton});

        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(tableAddBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tableRemoveBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(loadBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveBtn)).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)).addGap(18, 18, 18).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(applyToRowsRb).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(applyToColumnsRb).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(applyToRowsAndColumnsRb).addGap(18, 18, 18).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton)).addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void tableAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableAddBtnActionPerformed
        criteriaModel.addCriteria(layers.get(0));
    }//GEN-LAST:event_tableAddBtnActionPerformed

    private void tableRemoveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableRemoveBtnActionPerformed
        criteriaModel.removeCriteria(table.getSelectedRows());
    }//GEN-LAST:event_tableRemoveBtnActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton applyToColumnsRb;
    private javax.swing.ButtonGroup applyToGroup;
    private javax.swing.JRadioButton applyToRowsAndColumnsRb;
    private javax.swing.JRadioButton applyToRowsRb;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton loadBtn;
    private javax.swing.JButton okButton;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTable table;
    private javax.swing.JButton tableAddBtn;
    private javax.swing.JButton tableRemoveBtn;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

    public boolean isApplyToRowsChecked() {
        return applyToRowsRb.isSelected() || applyToRowsAndColumnsRb.isSelected();
    }

    public boolean isApplyToColumnsChecked() {
        return applyToColumnsRb.isSelected() || applyToRowsAndColumnsRb.isSelected();
    }

    public List<IMatrixLayer> getCriteriaList() {
        return criteriaModel.getList();
    }
}