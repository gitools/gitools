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

package org.gitools.ui.heatmap.header.coloredlabels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class ColoredLabelsGroupsPage extends AbstractWizardPage {

    private boolean valueEditable;

    class ColorChooserEditor extends AbstractCellEditor implements TableCellEditor {

        private JButton delegate = new JButton();

        Color savedColor;

        public ColorChooserEditor() {
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    Color color = JColorChooser.showDialog(delegate, "Color Chooser", savedColor);
                    ColorChooserEditor.this.changeColor(color);
                }
            };
            delegate.setFocusPainted(false);
            delegate.setContentAreaFilled(false);
            delegate.setFocusable(false);
            delegate.setOpaque(true);
            delegate.addActionListener(actionListener);
        }

        public Object getCellEditorValue() {
            return savedColor;
        }

        private void changeColor(Color color) {
            if (color != null) {
                savedColor = color;
                delegate.setBackground(color);
            }
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            changeColor((Color) value);
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
        setComplete(true);
    }

    public void setColoredLabels(ColoredLabel[] coloredLabels) {
        this.coloredLabels = coloredLabels;
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        model.addAllLabels(coloredLabels);
    }
    
    public void setValueEditable(boolean editable) {
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        model.setValueEditable(editable);
    }

    public boolean isValueEditable() {
        ColoredLabelsTableModel model = (ColoredLabelsTableModel) table.getModel();
        return model.isValueEditable();
    }

	@Override
	public void updateControls() {
		super.updateControls();
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

        jLabel5.setText("Labels");

        table.setModel(new org.gitools.ui.heatmap.header.coloredlabels.ColoredLabelsTableModel());
        table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(false);
        jScrollPane2.setViewportView(table);

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
                        .addGap(84, 84, 84)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addGap(58, 58, 58))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

}
