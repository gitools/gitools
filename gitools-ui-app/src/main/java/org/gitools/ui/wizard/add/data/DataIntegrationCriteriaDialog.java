/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

/*
 * FilterDialog.java
 *
 * Created on Jan 19, 2010, 2:04:30 PM
 */

package org.gitools.ui.wizard.add.data;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.operators.Operator;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.gitools.matrix.data.integration.DataIntegrationCriteria;

public class DataIntegrationCriteriaDialog extends javax.swing.JDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

        private static class OperatorCellRenderer extends DefaultTableCellRenderer {
            public OperatorCellRenderer() {
                super();
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Operator op = (Operator) value;
                String opString = op.getAbbreviation();
                if (op.equals(Operator.AND))
                    opString = "    " + op.getAbbreviation();
                return super.getTableCellRendererComponent(table, opString, isSelected, hasFocus, row, column);
            }
        }

	private static class ComboBoxCellEditor extends DefaultCellEditor {
		public ComboBoxCellEditor(Object[] values) {
        		super(new JComboBox(values));
		}
	}
        
	private String[] attributeNames;
	private CutoffCmp[] comparators;
	private String[] operators;

	private DataIntegrationCriteriaTableModel criteriaModel;

    /** Creates new form FilterDialog */
    public DataIntegrationCriteriaDialog(java.awt.Frame parent,
			String[] attributeNames,
                        CutoffCmp[] comparators,
                        String[] operators,
			List<DataIntegrationCriteria> initialCriteriaList,
                        String setToValue) {

        super(parent, true);

		this.attributeNames = attributeNames;
		this.comparators = comparators;
		this.operators = operators;

		this.criteriaModel = new DataIntegrationCriteriaTableModel(attributeNames);
		initComponents();

                this.setToValue.setText(setToValue);
		table.setModel(criteriaModel);

		criteriaModel.addTableModelListener(new TableModelListener() {
			@Override public void tableChanged(TableModelEvent e) {
				tableRemoveBtn.setEnabled(criteriaModel.getList().size() > 0);
			}
		});

		if (initialCriteriaList != null)
			criteriaModel.addAllCriteria(initialCriteriaList);
		
		TableColumnModel columnModel = table.getColumnModel();
                columnModel.getColumn(0).setCellEditor(new ComboBoxCellEditor(operators));
                columnModel.getColumn(0).setCellRenderer(new OperatorCellRenderer());
                columnModel.getColumn(0).setMaxWidth(70);
		columnModel.getColumn(1).setCellEditor(new ComboBoxCellEditor(attributeNames));
                columnModel.getColumn(1).setMinWidth(200);
		columnModel.getColumn(2).setCellEditor(new ComboBoxCellEditor(comparators));
                columnModel.getColumn(2).setMaxWidth(70);
                columnModel.getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()));
                columnModel.getColumn(3).setMaxWidth(100);

                if (initialCriteriaList == null)
                    addCriteria(true);

    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public boolean isCancelled() {
        return returnStatus != RET_OK;
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    private void addCriteria(boolean first) {
        if (first)
            criteriaModel.addCriteria(new DataIntegrationCriteria(attributeNames[0], 0, CutoffCmp.EQ, 1.0, Operator.EMPTY));
        else
            criteriaModel.addCriteria(new DataIntegrationCriteria(attributeNames[0], 0, CutoffCmp.EQ, 1.0, Operator.AND));

    }

    /** This method is called from within the constructor to
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
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        setToValue = new javax.swing.JTextField();

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

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Operator", "Attribute", "Condition", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
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

        jLabel2.setText("Set value:");

        setToValue.setText("0");
        setToValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setToValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tableAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tableRemoveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(setToValue, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tableAddBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tableRemoveBtn))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(setToValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

	private void tableAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableAddBtnActionPerformed
            boolean first = table.getRowCount() == 0;
            addCriteria(first);
	}//GEN-LAST:event_tableAddBtnActionPerformed

	private void tableRemoveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableRemoveBtnActionPerformed
		criteriaModel.removeCriteria(table.getSelectedRows());
	}//GEN-LAST:event_tableRemoveBtnActionPerformed

        private void setToValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setToValueActionPerformed
            // TODO add your handling code here:
        }//GEN-LAST:event_setToValueActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyToGroup;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField setToValue;
    private javax.swing.JTable table;
    private javax.swing.JButton tableAddBtn;
    private javax.swing.JButton tableRemoveBtn;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

	public List<DataIntegrationCriteria> getCriteriaList() {
		return criteriaModel.getList();
	}

        public String getSetToValue() {
            return setToValue.getText();
        }
}
