/*
 *  Copyright 2011 cperez.
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
 * LabelFilterPage.java
 *
 * Created on 22-mar-2011, 15:54:05
 */
package org.gitools.ui.wizard.add.data;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.operators.Operator;
import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.data.integration.DataIntegrationCriteria;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class DataIntegrationPage extends AbstractWizardPage {

        private String[] attrNames;
        DefaultTableModel model;

        private static class DataIntegrationCriteriaListCellRender extends JTextArea implements TableCellRenderer {
            public DataIntegrationCriteriaListCellRender() {
                setLineWrap(true);
                setWrapStyleWord(true);
                //setOpaque(true);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                List<DataIntegrationCriteria> criteriaList = (List<DataIntegrationCriteria>) value;
                String intend = "    ";
                String rendering = "";
                for (DataIntegrationCriteria c : criteriaList) {
                    Operator op = c.getOperator();
                    intend = (!op.equals(Operator.OR)) ?
                        "    " : "";
                    if (!op.equals(Operator.EMPTY))
                        rendering = rendering + intend + op.getLongName().toUpperCase() + "\n";
                    
                    rendering = rendering + "    " + c.getAttributeName() + " "
                            + c.getComparator().getLongName() +" "
                            + Double.toString(c.getValue()) + "\n";
                    

                }
                setText(rendering);
                return this;
            }
        }

	public DataIntegrationPage(Heatmap hm) {
		
		initComponents();
		setComplete(false);

		setTitle("Data Dimension Integration");
		setMessage("Choose from which dimensions and with what cut-offs"
                        + "to integrate");
                
               	List<IElementAttribute> attributes = hm.getMatrixView().getContents().getCellAdapter().getProperties();

                this.attrNames = new String[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			this.attrNames[i] = attributes.get(i).getName();
		}
                //this.model = new DefaultTableModel();
                model = (DefaultTableModel) table.getModel();
                
                //table.setModel(model);
                table.getColumnModel().getColumn(1).setCellRenderer(
                        new DataIntegrationCriteriaListCellRender());
                table.getColumnModel().getColumn(0).setMaxWidth(100);
                table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        update();
                    }
                });
	}


        private void update() {
            int rowNb = table.getRowCount();
            removeBtn.setEnabled(rowNb > 0 && table.getSelectedRow() >= 0);
            upBtn.setEnabled(rowNb > 1 && table.getSelectedRow() >= 0);
            downBtn.setEnabled(rowNb > 1 && table.getSelectedRow() >= 0);
            editBtn.setEnabled(rowNb > 0 && table.getSelectedRow() >= 0);
            setComplete(rowNb > 0);
        }

        private void updateRowHeights() {
            for (int i = 0; i < table.getRowCount(); i++) {
                List<DataIntegrationCriteria> criteriaList = (List<DataIntegrationCriteria>) table.getValueAt(i, 1);
                int lines = criteriaList.size()*2-1;
                table.setRowHeight(i, table.getRowHeight()*lines);
            }
        }



	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        applyGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        tableAddBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        upBtn = new javax.swing.JButton();
        downBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        editBtn = new javax.swing.JButton();

        jLabel1.setText("Select the data dimensions to integrate");

        tableAddBtn.setText("Add");
        tableAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableAddBtnActionPerformed(evt);
            }
        });

        removeBtn.setText("Remove");
        removeBtn.setEnabled(false);
        removeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });

        upBtn.setText("Up");
        upBtn.setEnabled(false);
        upBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upBtnActionPerformed(evt);
            }
        });

        downBtn.setText("Down");
        downBtn.setEnabled(false);
        downBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downBtnActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Value", "Criteria"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int row, int column)
            {
                return false;//This causes all cells to be not editable
            }
        });
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        editBtn.setText("Edit");
        editBtn.setEnabled(false);
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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

        private void tableAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableAddBtnActionPerformed
            String[] ops = new String[] {Operator.AND.getAbbreviation(), Operator.OR.getAbbreviation()};
            final DataIntegrationCriteriaDialog dlg =
                        new DataIntegrationCriteriaDialog(AppFrame.instance(),
                                                        attrNames,
                                                        CutoffCmp.comparators,
                                                        ops,
                                                        null,
                                                        "1");
            	dlg.setVisible(true);

		if (dlg.getReturnStatus() != DataIntegrationCriteriaDialog.RET_OK) {
			return;
		}
                List<DataIntegrationCriteria> criteriaList = dlg.getCriteriaList();
                String setToValue = dlg.getSetToValue();
                model.addRow(new Object[]{setToValue, criteriaList});
                updateRowHeights();
                update();
}//GEN-LAST:event_tableAddBtnActionPerformed

        private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
            model.removeRow(table.getSelectedRow());
            update();
}//GEN-LAST:event_removeBtnActionPerformed

        private void downBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downBtnActionPerformed
            int selectedPosition = table.getSelectedRow();
            int wantedPosition = (selectedPosition + 1 == table.getRowCount()) ?
                                selectedPosition : selectedPosition+1;
            model.moveRow(selectedPosition, selectedPosition , wantedPosition);
            updateRowHeights();
            table.getSelectionModel().setSelectionInterval(wantedPosition, wantedPosition);
        }//GEN-LAST:event_downBtnActionPerformed

        private void upBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upBtnActionPerformed
            int selectedPosition = table.getSelectedRow();
            int wantedPosition = (selectedPosition == 0) ?
                                selectedPosition : selectedPosition-1;
            model.moveRow(selectedPosition, selectedPosition , wantedPosition);
            updateRowHeights();
            table.getSelectionModel().setSelectionInterval(wantedPosition, wantedPosition);
        }//GEN-LAST:event_upBtnActionPerformed

        private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
            String[] ops = new String[] {Operator.AND.getAbbreviation(), Operator.OR.getAbbreviation()};
            List<DataIntegrationCriteria> criteria =
                    (List<DataIntegrationCriteria>) table.getValueAt(table.getSelectedRow(), 1);
            String setToValue = (String) table.getValueAt(table.getSelectedRow(), 0);
            final DataIntegrationCriteriaDialog dlg =
                        new DataIntegrationCriteriaDialog(AppFrame.instance(),
                                                        attrNames,
                                                        CutoffCmp.comparators,
                                                        ops,
                                                        criteria,
                                                        setToValue);
            	dlg.setVisible(true);

		if (dlg.getReturnStatus() != DataIntegrationCriteriaDialog.RET_OK) {
			return;
		}
                int selectedRow = table.getSelectedRow();
                List<DataIntegrationCriteria> criteriaList = dlg.getCriteriaList();
                setToValue = dlg.getSetToValue();
                model.removeRow(selectedRow);
                model.insertRow(selectedRow, new Object[]{setToValue, criteriaList});
                updateRowHeights();
                update();
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
