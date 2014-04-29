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
package org.gitools.ui.app.dialog.filter;

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.matrix.filter.ValueFilterFunction;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

/**
 * @noinspection ALL
 */
public class ValueFilterPage extends AbstractWizardPage {
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    private static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    private final IMatrixLayer visibleLayer;

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

    private final IMatrixLayers layers;
    private final CutoffCmp[] comparators;

    private final ValueFilterCriteriaTableModel criteriaModel;

    /**
     * Creates new form FilterDialog
     */
    public ValueFilterPage(Frame parent,
                           IMatrixLayers layers,
                           MatrixDimensionKey dimension, CutoffCmp[] comparators,
                           List<ValueFilterFunction> initialCriteriaList,
                           IMatrixLayer visibleLayer) {

        this.layers = layers;
        this.comparators = comparators;
        this.visibleLayer = visibleLayer;

        this.criteriaModel = new ValueFilterCriteriaTableModel(layers);

        initComponents();

        if (dimension.equals(MatrixDimensionKey.COLUMNS)) {
            applyToColumnsRb.setSelected(true);
        } else if (dimension.equals(MatrixDimensionKey.ROWS)) {
            applyToRowsRb.setSelected(true);
        }

        table.setModel(criteriaModel);

        criteriaModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                tableRemoveBtn.setEnabled(criteriaModel.getList().size() > 0);
                updateFilterDescription();
            }
        });

        if (initialCriteriaList != null) {
            criteriaModel.addAllCriteria(initialCriteriaList);
        }


        CellEditorListener cellEditorListener = new CellEditorListener() {
            /**
             * This tells the listeners the editor has ended editing
             */
            @Override
            public void editingStopped(ChangeEvent e) {
                updateFilterDescription();
            }

            /**
             * This tells the listeners the editor has canceled editing
             */
            @Override
            public void editingCanceled(ChangeEvent e) {
                updateFilterDescription();
            }
        };
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellEditor(new ComboBoxCellEditor(layers.getIds()));
        columnModel.getColumn(0).getCellEditor().addCellEditorListener(cellEditorListener);
        columnModel.getColumn(1).setCellEditor(new ComboBoxCellEditor(comparators));
        columnModel.getColumn(1).getCellEditor().addCellEditorListener(cellEditorListener);
        columnModel.getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));
        columnModel.getColumn(2).getCellEditor().addCellEditorListener(cellEditorListener);

        ChangeListener changeListener = new ChangeListener() {
            /**
             * Invoked when the target of the listener has changed its state.
             *
             * @param e a ChangeEvent object
             */
            @Override
            public void stateChanged(ChangeEvent e) {
                updateFilterDescription();
            }
        };
        allCriteriaCheck.addChangeListener(changeListener);
        allElementsCheck.addChangeListener(changeListener);
        hideMatchingCheck.addChangeListener(changeListener);
        applyToRowsAndColumnsRb.addChangeListener(changeListener);
        applyToColumnsRb.addChangeListener(changeListener);
        applyToRowsRb.addChangeListener(changeListener);


        setTitle("Filter by values");
        updateFilterDescription();
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    private void updateFilterDescription() {

        StringBuilder sb = new StringBuilder();

        sb.append(isHideMatching() ? "<html><b>Hide</b> all " : "<html><b>Keep</b> all ");


        if (!isApplyToRows()) {
            sb.append("columns ");
            allElementsCheck.setText("All values in a column must match");
        } else if (!isApplyToColumns()) {
            sb.append("rows ");
            allElementsCheck.setText("All values in a row must match");
        } else {
            sb.append("rows and columns ");
            allElementsCheck.setText("All values in a row/columns must match");
        }

        sb.append("where ");
        sb.append(isAllElementsMatch() ? "all elements have " : "at least 1 element has ");

        //list all criterias
        sb.append("<ul>");
        for (ValueFilterFunction c : getCriteriaList()) {
            sb.append("<li>");
            sb.append(c.getLayer().getId());
            sb.append(" ");
            sb.append(c.getComparator().getLongName());
            sb.append(" ");
            sb.append(c.getCutoffValue());
            sb.append(" ");
            if (getCriteriaList().size() - 1 > getCriteriaList().indexOf(c)) {
                sb.append(isAllCriteriaMatch() ? "and " : "or ");
            }
            sb.append("</li>");
        }

        sb.append("</ul></html>");
        setMessage(MessageStatus.WARN, sb.toString());


        updateComplete();
    }

    private void updateComplete() {
        setComplete(getCriteriaList().size() > 0);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableAddBtn = new javax.swing.JButton();
        tableRemoveBtn = new javax.swing.JButton();
        allCriteriaCheck = new javax.swing.JCheckBox();
        allElementsCheck = new javax.swing.JCheckBox();
        hideMatchingCheck = new javax.swing.JCheckBox();
        loadBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        applyToRowsRb = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        applyToRowsAndColumnsRb = new javax.swing.JRadioButton();
        applyToColumnsRb = new javax.swing.JRadioButton();

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Value Dimension", "Condition", "Cell Value"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.Object.class, java.lang.Double.class
            };

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

        allCriteriaCheck.setText("All of the above criteria should match");

        allElementsCheck.setText("All values should match");

        hideMatchingCheck.setText("Hide matching (Hide unmatching otherwise)");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(loadBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(tableAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(tableRemoveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(applyToRowsRb)
                                                        .addComponent(applyToColumnsRb)
                                                        .addComponent(applyToRowsAndColumnsRb)
                                                        .addComponent(jLabel2)
                                                        .addComponent(allCriteriaCheck)
                                                        .addComponent(allElementsCheck)
                                                        .addComponent(hideMatchingCheck))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(tableAddBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tableRemoveBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(loadBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saveBtn))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(allCriteriaCheck)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(allElementsCheck)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hideMatchingCheck)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(applyToRowsRb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(applyToColumnsRb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(applyToRowsAndColumnsRb)
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    private void tableAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableAddBtnActionPerformed
        criteriaModel.addCriteria(new ValueFilterFunction(visibleLayer, comparators[0], 0.0, null));
        updateFilterDescription();
    }//GEN-LAST:event_tableAddBtnActionPerformed

    private void tableRemoveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableRemoveBtnActionPerformed
        criteriaModel.removeCriteria(table.getSelectedRows());
        updateFilterDescription();
    }//GEN-LAST:event_tableRemoveBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox allCriteriaCheck;
    private javax.swing.JCheckBox allElementsCheck;
    private javax.swing.JRadioButton applyToColumnsRb;
    private javax.swing.ButtonGroup applyToGroup;
    private javax.swing.JRadioButton applyToRowsAndColumnsRb;
    private javax.swing.JRadioButton applyToRowsRb;
    private javax.swing.JCheckBox hideMatchingCheck;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTable table;
    private javax.swing.JButton tableAddBtn;
    private javax.swing.JButton tableRemoveBtn;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

    public boolean isApplyToRows() {
        return applyToRowsRb.isSelected() || applyToRowsAndColumnsRb.isSelected();
    }

    public boolean isApplyToColumns() {
        return applyToColumnsRb.isSelected() || applyToRowsAndColumnsRb.isSelected();
    }

    public boolean isAllCriteriaMatch() {
        return allCriteriaCheck.isSelected();
    }

    public boolean isAllElementsMatch() {
        return allElementsCheck.isSelected();
    }

    public boolean isHideMatching() {
        return hideMatchingCheck.isSelected();
    }

    public List<ValueFilterFunction> getCriteriaList() {
        return criteriaModel.getList();
    }
}
