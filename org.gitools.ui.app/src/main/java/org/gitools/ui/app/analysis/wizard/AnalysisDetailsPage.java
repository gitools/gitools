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
package org.gitools.ui.app.analysis.wizard;

import org.gitools.analysis._DEPRECATED.model.Property;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class AnalysisDetailsPage extends AbstractWizardPage {

    private static final long serialVersionUID = -6310021084299136899L;

    private static class AttributesModel implements TableModel {


        private final List<Property> attrs;

        private final List<TableModelListener> listeners = new ArrayList<TableModelListener>();

        public AttributesModel() {
            attrs = new ArrayList<Property>();
        }

        public AttributesModel(List<Property> attrs) {
            this.attrs = attrs != null ? attrs : new ArrayList<Property>();
        }

        @Override
        public int getRowCount() {
            return attrs.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }


        @Override
        public String getColumnName(int columnIndex) {
            return columnIndex == 0 ? "Name" : "Value";
        }


        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Property attr = attrs.get(rowIndex);
            return columnIndex == 0 ? attr.getName() : attr.getValue();
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            listeners.add(l);
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            listeners.remove(l);
        }


        public List<Property> getAttributes() {
            return attrs;
        }

        public Property getAttribute(int index) {
            return attrs.get(index);
        }

        public void addAttribute(Property attr) {
            attrs.add(attr);
            for (TableModelListener l : listeners)
                l.tableChanged(new TableModelEvent(this));
        }

        private void modifyAttribute(int index, Property attribute) {
            Property attr = getAttribute(index);
            attr.setName(attribute.getName());
            attr.setValue(attribute.getValue());
            for (TableModelListener l : listeners)
                l.tableChanged(new TableModelEvent(this));
        }

        public void removeAttribute(int index) {
            attrs.remove(index);
            for (TableModelListener l : listeners)
                l.tableChanged(new TableModelEvent(this));
        }
    }

    private AttributesModel attrModel;

    /**
     * Creates new form AnalysisDetailsPanel
     */
    public AnalysisDetailsPage() {
        setTitle("Analysis details");

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_ANALYSIS_DETAILS, 96));

        setComplete(true);

        initComponents();

        attrTable.setModel(attrModel = new AttributesModel());
        attrTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attrTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = attrTable.getSelectedRow();
                attrEditBtn.setEnabled(row != -1);
                attrRemoveBtn.setEnabled(row != -1);
            }
        });
    }


    @Override
    public JComponent createControls() {
        return this;
    }

    public String getAnalysisTitle() {
        return titleField.getText();
    }

    public void setAnalysisTitle(String title) {
        titleField.setText(title);
    }

    public String getAnalysisNotes() {
        return notesArea.getText();
    }

    public void setAnalysisNotes(String notes) {
        notesArea.setText(notes);
    }


    public List<Property> getAnalysisAttributes() {
        return attrModel.getAttributes();
    }

    public void setAnalysisAttributes(List<Property> attrs) {
        attrTable.setModel(attrModel = new AttributesModel(attrs));
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        notesArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        attrTable = new javax.swing.JTable();
        attrAddBtn = new javax.swing.JButton();
        attrRemoveBtn = new javax.swing.JButton();
        attrEditBtn = new javax.swing.JButton();

        jLabel1.setText("Title");

        jLabel2.setText("Notes");

        notesArea.setColumns(20);
        notesArea.setLineWrap(true);
        notesArea.setRows(3);
        notesArea.setTabSize(4);
        jScrollPane1.setViewportView(notesArea);

        jLabel3.setText("Attributes");

        jScrollPane2.setEnabled(false);

        attrTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{

        }, new String[]{"Name", "Value"}) {

            final Class[] types = new Class[]{java.lang.String.class, java.lang.String.class};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jScrollPane2.setViewportView(attrTable);

        attrAddBtn.setText("Add");
        attrAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attrAddBtnActionPerformed(evt);
            }
        });

        attrRemoveBtn.setText("Remove");
        attrRemoveBtn.setEnabled(false);
        attrRemoveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attrRemoveBtnActionPerformed(evt);
            }
        });

        attrEditBtn.setText("Edit");
        attrEditBtn.setEnabled(false);
        attrEditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attrEditBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE).addComponent(titleField, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE).addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE).addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(attrRemoveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(attrEditBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(attrAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))).addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(attrAddBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(attrEditBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(attrRemoveBtn)).addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void attrAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attrAddBtnActionPerformed
        AttributeDialog dlg = new AttributeDialog(null);
        dlg.setVisible(true);
        if (dlg.isCancelled()) {
            return;
        }

        attrModel.addAttribute(dlg.getAttribute());
    }//GEN-LAST:event_attrAddBtnActionPerformed

    private void attrEditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attrEditBtnActionPerformed
        int row = attrTable.getSelectedRow();
        if (row == -1) {
            return;
        }

        AttributeDialog dlg = new AttributeDialog(null);
        dlg.setAttribute(attrModel.getAttribute(row));
        dlg.setVisible(true);
        if (dlg.isCancelled()) {
            return;
        }

        attrModel.modifyAttribute(row, dlg.getAttribute());
    }//GEN-LAST:event_attrEditBtnActionPerformed

    private void attrRemoveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attrRemoveBtnActionPerformed
        int row = attrTable.getSelectedRow();
        if (row == -1) {
            return;
        }

        attrModel.removeAttribute(row);
    }//GEN-LAST:event_attrRemoveBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton attrAddBtn;
    private javax.swing.JButton attrEditBtn;
    private javax.swing.JButton attrRemoveBtn;
    private javax.swing.JTable attrTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea notesArea;
    private javax.swing.JTextField titleField;
    // End of variables declaration//GEN-END:variables

}
