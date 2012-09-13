/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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
 * ColoredClustersPage.java
 *
 * Created on 03-mar-2011, 18:51:34
 */

package org.gitools.ui.heatmap.header.wizard.coloredlabels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class ColoredLabelsGroupsPage extends AbstractWizardPage {

    class ColorChooserEditor extends AbstractCellEditor implements TableCellEditor {

        private JButton delegate = new JButton();

        Color savedColor;

        public ColorChooserEditor() {
            delegate.setFocusPainted(false);
            delegate.setContentAreaFilled(false);
            delegate.setFocusable(false);
            delegate.setOpaque(true);


            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    updateButtons();
                }
            });

            MouseInputListener mouseInputListener = new MouseInputListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    Component c = e.getComponent();
                    Point p = new Point(c.getX(),c.getY());
                    int clickedRowIndex = table.rowAtPoint(p);
                    int clickedColIndex = table.columnAtPoint(p);
                    Color color = JColorChooser.showDialog(delegate, "Color Chooser", savedColor);
                    ColorChooserEditor.this.changeColor(color, clickedRowIndex, clickedColIndex);
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            };

            KeyListener keyListener = new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void keyReleased(KeyEvent e) {
                   if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                       int row = table.getSelectedRow();
                       int col = table.getSelectedColumn();
                       Color color = JColorChooser.showDialog(delegate, "Color Chooser", savedColor);
                       ColorChooserEditor.this.changeColor(color, row, col);
                   }
                }
            };        
            delegate.addMouseListener(mouseInputListener);
            delegate.addKeyListener(keyListener);
        }

        public Object getCellEditorValue() {
            return savedColor;
        }

        private void changeColor(Color color, int row, int col) {
            if (color != null) {
                savedColor = color;
                delegate.setBackground(color);
                table.getModel().setValueAt(color, row, col);
                table.getSelectionModel().clearSelection();
            }
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            changeColor((Color) value, row, column);
            return delegate;
        }
    }

    private static class ColorCellRenderer extends DefaultTableCellRenderer {
        public ColorCellRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Color c = (Color) value;
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            component.setBackground(c);
            component.setForeground(c);
            return component;
        }
    }


	private ColoredLabel[] coloredLabels;

    public ColoredLabelsGroupsPage(ColoredLabel[] coloredLabels) {

        this.coloredLabels = coloredLabels;
		
        initComponents();

        ColoredLabelsTableModel tm = new ColoredLabelsTableModel(coloredLabels);
        table.setModel(tm);

        tm.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField()));
        columnModel.getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()));
        columnModel.getColumn(2).setCellEditor(new ColorChooserEditor());
        columnModel.getColumn(2).setCellRenderer(new ColorCellRenderer());
                
        setTitle("Labels configuration");
        updateButtons();
    }

    public void setColoredLabels(ColoredLabel[] coloredLabels) {
        this.coloredLabels = coloredLabels;
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        model.addAllLabels(coloredLabels);
    }
    
    public ColoredLabel[] getColoredLabels() {
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        ColoredLabel[] coloredLabels = new ColoredLabel[model.getList().size()];
        model.getList().toArray(coloredLabels);
        return coloredLabels;
    }
    
    public void setValueEditable(boolean editable) {
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        model.setValueEditable(editable);
    }

    public boolean isValueEditable() {
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        return model.isValueEditable();
    }

    public boolean isValueMustBeNumeric() {
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        return model.isValueMustBeNumeric();
    }

    public void setValueMustBeNumeric(boolean valueMustBeNumeric) {
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        model.setValueMustBeNumeric(valueMustBeNumeric);
    }

    @Override
    public void updateControls() {
            super.updateControls();
    }

    private void updateButtons() {
        int rowNb = table.getRowCount();
        removeBtn.setEnabled(rowNb > 0 && table.getSelectedRow() >= 0);
        setComplete(rowNb > 0);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableAddBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();

        jLabel5.setText("Labels");

        table.setModel(new org.gitools.ui.heatmap.header.wizard.coloredlabels.ColoredLabelsTableModel());
        table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setShowHorizontalLines(false);
        jScrollPane2.setViewportView(table);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tableAddBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeBtn))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tableAddBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeBtn)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                        .addGap(58, 58, 58))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableAddBtnActionPerformed
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        model.addLabel(new ColoredLabel("0.0",Color.GRAY));

        updateButtons();
}//GEN-LAST:event_tableAddBtnActionPerformed

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
       ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
       model.removeLabel(table.getSelectedRows());
       updateButtons();
}//GEN-LAST:event_removeBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton removeBtn;
    private javax.swing.JTable table;
    private javax.swing.JButton tableAddBtn;
    // End of variables declaration//GEN-END:variables

}
